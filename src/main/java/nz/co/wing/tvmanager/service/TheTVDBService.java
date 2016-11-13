package nz.co.wing.tvmanager.service;

import java.util.ArrayList;
import java.util.List;

import nz.co.wing.tvmanager.config.AppConfiguration;
import nz.co.wing.tvmanager.model.Banner;
import nz.co.wing.tvmanager.model.Banners;
import nz.co.wing.tvmanager.model.Episode;
import nz.co.wing.tvmanager.model.Series;
import nz.co.wing.tvmanager.util.Transformer;

import org.apache.log4j.Logger;

import com.omertron.thetvdbapi.TheTVDBApi;
import com.omertron.thetvdbapi.TvDbException;

public class TheTVDBService {
	private static final Logger LOGGER = Logger.getLogger(TheTVDBService.class);

	private final AppConfiguration configuration;

	private static class SeriesTransformer extends Transformer<Series, com.omertron.thetvdbapi.model.Series> {
	}

	private static class EpisodeTransformer extends Transformer<Episode, com.omertron.thetvdbapi.model.Episode> {
	}

	private static class BannersTransformer extends Transformer<Banners, com.omertron.thetvdbapi.model.Banners> {
		private static final BannerTransformer bannerTransformer = new BannerTransformer();

		@Override
		public Banners transform(final com.omertron.thetvdbapi.model.Banners from) {
			final Banners to = new Banners();
			from.getFanartList().forEach(i -> {
				to.getFanartList().add(bannerTransformer.transform(i));
			});
			from.getPosterList().forEach(i -> {
				to.getPosterList().add(bannerTransformer.transform(i));
			});
			from.getSeasonList().forEach(i -> {
				to.getSeasonList().add(bannerTransformer.transform(i));
			});
			to.setSeriesId(from.getSeriesId());
			from.getSeriesList().forEach(i -> {
				to.getSeriesList().add(bannerTransformer.transform(i));
			});
			return to;
		}
	}

	private static class BannerTransformer extends Transformer<Banner, com.omertron.thetvdbapi.model.Banner> {
	}

	public TheTVDBService(final AppConfiguration configuration) {
		this.configuration = configuration;
	}

	public List<Series> search(final String search) throws ServiceException {
		final TheTVDBApi api = new TheTVDBApi(configuration.getTvdb().getApikey());
		final List<Series> list = new ArrayList<>();
		final SeriesTransformer transformer = new SeriesTransformer();
		try {
			final List<com.omertron.thetvdbapi.model.Series> series = api.searchSeries(search, null);
			for (final com.omertron.thetvdbapi.model.Series s : series) {
				LOGGER.info(s);
				final Series to = transformer.transform(s);
				list.add(to);
			}
		} catch (final TvDbException e) {
			throw new ServiceException(e);
		}
		return list;
	}

	public Series getSeries(final String id) throws ServiceException {
		final TheTVDBApi api = new TheTVDBApi(configuration.getTvdb().getApikey());
		try {
			final com.omertron.thetvdbapi.model.Series s = api.getSeries(id, null);
			final SeriesTransformer transformer = new SeriesTransformer();
			final Series to = transformer.transform(s);
			return to;
		} catch (final TvDbException e) {
			throw new ServiceException(e);
		}
	}

	public List<Episode> getEpisodes(final String id) throws ServiceException {
		final TheTVDBApi api = new TheTVDBApi(configuration.getTvdb().getApikey());
		final List<Episode> list = new ArrayList<>();
		final EpisodeTransformer transformer = new EpisodeTransformer();
		try {
			final List<com.omertron.thetvdbapi.model.Episode> episodes = api.getAllEpisodes(id, null);
			for (final com.omertron.thetvdbapi.model.Episode episode : episodes) {
				final Episode to = transformer.transform(episode);
				list.add(to);
			}
		} catch (final TvDbException e) {
			throw new ServiceException(e);
		}
		return list;
	}

	public Banners getBanners(final String id) throws ServiceException {
		final TheTVDBApi api = new TheTVDBApi(configuration.getTvdb().getApikey());
		final BannersTransformer transformer = new BannersTransformer();
		try {
			final com.omertron.thetvdbapi.model.Banners b = api.getBanners(id);
			LOGGER.info(b);
			final Banners banners = transformer.transform(b);
			LOGGER.info(banners);
			return banners;
		} catch (final TvDbException e) {
			throw new ServiceException(e);
		}
	}
}
