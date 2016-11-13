package nz.co.wing.tvmanager.service;

import java.util.List;

import nz.co.wing.tvmanager.dao.EpisodeDAO;
import nz.co.wing.tvmanager.dao.SeriesDAO;
import nz.co.wing.tvmanager.model.Episode;
import nz.co.wing.tvmanager.model.EpisodeState;
import nz.co.wing.tvmanager.model.Series;
import nz.co.wing.tvmanager.model.Torrent;

public class EpisodeService {
	private final EpisodeDAO episodeDAO;
	private final SeriesDAO seriesDAO;
	private final TorrentService torrentService;

	public EpisodeService(final EpisodeDAO episodeDAO, final SeriesDAO seriesDAO, final TorrentService torrentService) {
		super();
		this.episodeDAO = episodeDAO;
		this.seriesDAO = seriesDAO;
		this.torrentService = torrentService;
	}

	public List<Episode> listEpisodes() {
		return episodeDAO.list();
	}

	public List<Episode> listEpisodes(final String id) {
		return episodeDAO.list(id);
	}

	public Episode readEpisode(final String id) {
		return episodeDAO.read(id);
	}

	public void deleteEpisode(final String id) {
		episodeDAO.delete(id);
	}

	public List<Torrent> searchEpisode(final String id) throws ServiceException {
		final Episode episode = episodeDAO.read(id);
		final Series series = seriesDAO.read(episode.getSeriesId());
		final String seriesName = series.getSeriesName().replaceAll("\\(.+\\)", "");
		final String formatString = String.format("%1$s s%2$02de%3$02d", seriesName, episode.getSeasonNumber(),
				episode.getEpisodeNumber());
		return torrentService.findTorrents(formatString);
	}

	public Torrent addTorrent(final String id, final Torrent torrent) throws ServiceException {
		final Episode episode = episodeDAO.read(id);
		final Torrent torrent2 = torrentService.createTorrent(torrent);
		episode.setHash(torrent2.getHash().toUpperCase());
		episodeDAO.persist(episode);
		return torrent2;
	}

	public Episode setStatus(final String id, final EpisodeState status) {
		final Episode episode = episodeDAO.read(id);
		episode.setState(status);
		return episodeDAO.persist(episode);
	}
}
