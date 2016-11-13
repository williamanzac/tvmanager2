package nz.co.wing.tvmanager.service;

import java.util.List;

import nz.co.wing.tvmanager.dao.BannerDAO;
import nz.co.wing.tvmanager.dao.EpisodeDAO;
import nz.co.wing.tvmanager.dao.SeriesDAO;
import nz.co.wing.tvmanager.model.Banners;
import nz.co.wing.tvmanager.model.Episode;
import nz.co.wing.tvmanager.model.Series;

public class SeriesService {
	private final SeriesDAO seriesDAO;
	private final BannerDAO bannerDAO;
	private final EpisodeDAO episodeDAO;
	private final TheTVDBService tvdbService;

	public SeriesService(final SeriesDAO seriesDAO, final BannerDAO bannerDAO, final EpisodeDAO episodeDAO,
			final TheTVDBService tvdbService) {
		super();
		this.seriesDAO = seriesDAO;
		this.bannerDAO = bannerDAO;
		this.episodeDAO = episodeDAO;
		this.tvdbService = tvdbService;
	}

	public List<Series> list() {
		final List<Series> list = seriesDAO.list();
		return list;
	}

	public void delete(final String id) {
		seriesDAO.delete(id);
		episodeDAO.list(id).forEach(e -> {
			episodeDAO.delete(e.getId());
		});
	}

	public Series read(final String id) {
		return seriesDAO.read(id);
	}

	public Banners banners(final String id) {
		final Banners banners = bannerDAO.read(Integer.valueOf(id));
		banners.getFanartList().size();
		banners.getPosterList().size();
		banners.getSeasonList().size();
		banners.getSeriesList().size();
		return banners;
	}

	public Series create(final Series series) throws ServiceException {
		final Series s = tvdbService.getSeries(series.getId());
		final List<Episode> episodes = tvdbService.getEpisodes(s.getId());
		final Banners banners = tvdbService.getBanners(s.getId());
		seriesDAO.persist(s);
		for (final Episode episode : episodes) {
			episodeDAO.persist(episode);
		}
		bannerDAO.persist(banners);
		return s;
	}

	public List<Series> search(final String search) throws ServiceException {
		return tvdbService.search(search);
	}

	public void update(final String id) throws ServiceException {
		// final Series dbSeries = seriesDAO.read(id);
		// final Series s = tvdbService.getSeries(id);
		// dbSeries.update(s);
		// seriesDAO.persist(dbSeries);
		final List<Episode> episodes = tvdbService.getEpisodes(id);
		for (final Episode episode : episodes) {
			final Episode dbEpisode = episodeDAO.read(episode.getId());
			if (dbEpisode == null) {
				episodeDAO.persist(episode);
			} else {
				dbEpisode.update(episode);
				episodeDAO.persist(dbEpisode);
			}
		}
		// bannerDAO.delete(Integer.parseInt(id));
		// final Banners banners = tvdbService.getBanners(id);
		// bannerDAO.persist(banners);
	}
}
