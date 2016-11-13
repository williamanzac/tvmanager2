package nz.co.wing.tvmanager.service;

import java.util.List;

import nz.co.wing.tvmanager.dao.TorrentDAO;
import nz.co.wing.tvmanager.model.Torrent;
import nz.co.wing.tvmanager.model.TorrentFile;

public class TorrentService {
	private final TorrentProjectService torrentProjectService;
	private final UTorrentService uTorrentService;
	private final TorrentDAO torrentDAO;
	private final TorrentApiService torrentApiService;

	public TorrentService(final TorrentProjectService torrentProjectService, final UTorrentService uTorrentService,
			final TorrentDAO torrentDAO, final TorrentApiService torrentApiService) {
		super();
		this.torrentProjectService = torrentProjectService;
		this.uTorrentService = uTorrentService;
		this.torrentDAO = torrentDAO;
		this.torrentApiService = torrentApiService;
	}

	public List<Torrent> listTorrents() {
		return torrentDAO.list();
	}

	public Torrent createTorrent(final Torrent torrent) throws ServiceException {
		if (!torrent.getUrl().startsWith("magnet:")) {
			final String magnet = torrentProjectService.getMagnetLink(torrent.getUrl());
			torrent.setUrl(magnet);
		}
		final Torrent torrent2 = torrentDAO.persist(torrent);
		uTorrentService.addTorrent(torrent2);
		return torrent2;
	}

	public Torrent readTorrent(final String id) {
		return torrentDAO.read(id);
	}

	public void deleteTorrent(final String id) throws ServiceException {
		torrentDAO.delete(id);
		uTorrentService.removeTorrent(id);
	}

	public List<Torrent> findTorrents(final String search) throws ServiceException {
		return torrentApiService.findTorrents(search);
	}

	public List<TorrentFile> getFiles(final String hash) throws ServiceException {
		return uTorrentService.getFiles(hash);
	}

	public void startTorrent(final String id) throws ServiceException {
		uTorrentService.startTorrent(id);
	}

	public void pauseTorrent(final String id) throws ServiceException {
		uTorrentService.pauseTorrent(id);
	}

	public void stopTorrent(final String id) throws ServiceException {
		uTorrentService.stopTorrent(id);
	}
}
