package nz.co.wing.tvmanager;

import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import nz.co.wing.tvmanager.config.AppConfiguration;
import nz.co.wing.tvmanager.config.UTorrentConfiguration;
import nz.co.wing.tvmanager.config.XBMCConfiguration;
import nz.co.wing.tvmanager.dao.BannerDAO;
import nz.co.wing.tvmanager.dao.EpisodeDAO;
import nz.co.wing.tvmanager.dao.FileTaskDAO;
import nz.co.wing.tvmanager.dao.SeriesDAO;
import nz.co.wing.tvmanager.dao.TorrentDAO;
import nz.co.wing.tvmanager.model.Banner;
import nz.co.wing.tvmanager.model.Banners;
import nz.co.wing.tvmanager.model.Episode;
import nz.co.wing.tvmanager.model.FileTask;
import nz.co.wing.tvmanager.model.Series;
import nz.co.wing.tvmanager.model.Torrent;
import nz.co.wing.tvmanager.resource.EpisodeResource;
import nz.co.wing.tvmanager.resource.FilesResource;
import nz.co.wing.tvmanager.resource.SeriesResource;
import nz.co.wing.tvmanager.resource.TorrentResource;
import nz.co.wing.tvmanager.resource.UIResource;
import nz.co.wing.tvmanager.resource.XBMCResource;
import nz.co.wing.tvmanager.service.EpisodeService;
import nz.co.wing.tvmanager.service.FileMonitorThread;
import nz.co.wing.tvmanager.service.FilesService;
import nz.co.wing.tvmanager.service.SeriesService;
import nz.co.wing.tvmanager.service.TheTVDBService;
import nz.co.wing.tvmanager.service.TorrentApiService;
import nz.co.wing.tvmanager.service.TorrentMonitorThread;
import nz.co.wing.tvmanager.service.TorrentProjectService;
import nz.co.wing.tvmanager.service.TorrentService;
import nz.co.wing.tvmanager.service.UTorrentService;
import nz.co.wing.tvmanager.service.XBMCService;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.hibernate.SessionFactory;

public class TvApp extends Application<AppConfiguration> {

	private final HibernateBundle<AppConfiguration> hibernate = new HibernateBundle<AppConfiguration>(Series.class,
			Episode.class, Banners.class, Banner.class, Torrent.class, FileTask.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(final AppConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	public static void main(final String[] args) throws Exception {
		new TvApp().run(args);
	}

	@Override
	public String getName() {
		return "Tv Manager App";
	}

	@Override
	public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
		super.initialize(bootstrap);
		// bootstrap.addBundle(new MigrationsBundle<AppConfiguration>() {
		// @Override
		// public DataSourceFactory getDataSourceFactory(AppConfiguration
		// configuration) {
		// return configuration.getDataSourceFactory();
		// }
		// });
		bootstrap.addBundle(hibernate);
		bootstrap.addBundle(new ViewBundle<AppConfiguration>());
	}

	@Override
	public void run(final AppConfiguration configuration, final Environment environment) throws Exception {
		final SessionFactory sessionFactory = hibernate.getSessionFactory();
		final SeriesDAO seriesDAO = new SeriesDAO(sessionFactory);
		final EpisodeDAO episodeDAO = new EpisodeDAO(sessionFactory);
		final BannerDAO bannerDAO = new BannerDAO(sessionFactory);
		final TorrentDAO torrentDAO = new TorrentDAO(sessionFactory);
		final FileTaskDAO fileTaskDAO = new FileTaskDAO(sessionFactory);

		final UTorrentConfiguration uTorrentConfiguration = configuration.getuTorrent();
		final XBMCConfiguration xbmcConfiguration = configuration.getXbmc();

		final CredentialsProvider uCredentialsProvider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials uCredentials = new UsernamePasswordCredentials(
				uTorrentConfiguration.getUsername(), uTorrentConfiguration.getPassword());
		uCredentialsProvider.setCredentials(AuthScope.ANY, uCredentials);

		final CredentialsProvider xCredentialsProvider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials xCredentials = new UsernamePasswordCredentials(
				xbmcConfiguration.getUsername(), xbmcConfiguration.getPassword());
		xCredentialsProvider.setCredentials(AuthScope.ANY, xCredentials);

		final HttpClient torrentProjectClient = new HttpClientBuilder(environment).using(
				configuration.getHttpClientConfiguration()).build("TorrentProject");
		final HttpClient uTorrentClient = new HttpClientBuilder(environment).using(uCredentialsProvider)
				.using(configuration.getHttpClientConfiguration()).build("uTorrent");
		final HttpClient xbmcClient = new HttpClientBuilder(environment).using(xCredentialsProvider)
				.using(configuration.getHttpClientConfiguration()).build("xbmc");

		final UTorrentService uTorrentService = new UTorrentService(uTorrentConfiguration, uTorrentClient);
		final TorrentProjectService torrentProjectService = new TorrentProjectService(torrentProjectClient);
		final TorrentApiService torrentApiService = new TorrentApiService();
		final TorrentService torrentService = new TorrentService(torrentProjectService, uTorrentService, torrentDAO,
				torrentApiService);
		final TheTVDBService tvdbService = new TheTVDBService(configuration);
		final EpisodeService episodeService = new EpisodeService(episodeDAO, seriesDAO, torrentService);
		final SeriesService seriesService = new SeriesService(seriesDAO, bannerDAO, episodeDAO, tvdbService);
		final FilesService filesService = new FilesService(configuration, fileTaskDAO);
		final XBMCService xbmcService = new XBMCService(xbmcClient, xbmcConfiguration);

		final SeriesResource seriesResource = new SeriesResource(seriesService, episodeService);
		final UIResource uiResource = new UIResource();
		final EpisodeResource episodeResource = new EpisodeResource(episodeService);
		final TorrentResource torrentResource = new TorrentResource(torrentService);
		final FilesResource filesResource = new FilesResource(filesService);
		final XBMCResource xbmcResource = new XBMCResource(xbmcService);

		final TorrentMonitorThread torrentMonitorThread = new TorrentMonitorThread(uTorrentService, torrentDAO,
				episodeDAO, sessionFactory);
		final FileMonitorThread fileMonitorThread = new FileMonitorThread(filesService, sessionFactory);

		environment.jersey().register(seriesResource);
		environment.jersey().register(uiResource);
		environment.jersey().register(episodeResource);
		environment.jersey().register(torrentResource);
		environment.jersey().register(filesResource);
		environment.jersey().register(xbmcResource);

		environment.lifecycle().manage(new Managed() {
			@Override
			public void start() throws Exception {
				torrentMonitorThread.start();
			}

			@Override
			public void stop() throws Exception {
				torrentMonitorThread.setStopping(true);
			}
		});

		environment.lifecycle().manage(new Managed() {
			@Override
			public void start() throws Exception {
				fileMonitorThread.start();
			}

			@Override
			public void stop() throws Exception {
				fileMonitorThread.setStopping(true);
			}
		});
	}
}
