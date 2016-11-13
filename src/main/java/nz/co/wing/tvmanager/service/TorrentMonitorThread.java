package nz.co.wing.tvmanager.service;

import java.util.List;

import nz.co.wing.tvmanager.dao.EpisodeDAO;
import nz.co.wing.tvmanager.dao.TorrentDAO;
import nz.co.wing.tvmanager.model.Episode;
import nz.co.wing.tvmanager.model.EpisodeState;
import nz.co.wing.tvmanager.model.Torrent;
import nz.co.wing.tvmanager.model.TorrentState;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;

public class TorrentMonitorThread extends Thread {
	private final UTorrentService uTorrentService;
	private final TorrentDAO torrentDAO;
	private final EpisodeDAO episodeDAO;
	private boolean stopping = false;
	private final SessionFactory sessionFactory;

	public TorrentMonitorThread(final UTorrentService uTorrentService, final TorrentDAO torrentDAO,
			final EpisodeDAO episodeDAO, final SessionFactory sessionFactory) {
		super();
		this.uTorrentService = uTorrentService;
		this.torrentDAO = torrentDAO;
		this.sessionFactory = sessionFactory;
		this.episodeDAO = episodeDAO;
	}

	@Override
	public void run() {
		while (!stopping) {
			final Session session = sessionFactory.openSession();
			try {
				ManagedSessionContext.bind(session);
				final Transaction transaction = session.beginTransaction();
				try {
					final List<Torrent> torrentList = uTorrentService.getTorrentList();
					torrentList.forEach(t -> {
						final String hash = t.getHash();
						final Torrent torrent = torrentDAO.read(hash);
						if (torrent == null) {
							torrentDAO.persist(t);
						} else {
							torrent.setLeechers(t.getLeechers());
							torrent.setPercentComplete(t.getPercentComplete());
							torrent.setPubDate(t.getPubDate());
							torrent.setSeeds(t.getSeeds());
							torrent.setSize(t.getSize());
							torrent.setState(t.getState());
							torrent.setTitle(t.getTitle());
							torrent.setUrl(t.getUrl());
							torrent.setEta(t.getEta());
							torrent.setUpSpeed(t.getUpSpeed());
							torrent.setDownSpeed(t.getDownSpeed());
							torrentDAO.persist(torrent);

							if (torrent.getState() == TorrentState.SEEDING
									|| torrent.getState() == TorrentState.SEEDING_QUEUED) {
								try {
									uTorrentService.stopTorrent(torrent.getHash());
								} catch (final Exception e) {
									e.printStackTrace();
								}
							}

							final Episode episode = episodeDAO.find(torrent.getHash());
							if (episode != null) {
								switch (torrent.getState()) {
								case SEEDING:
								case SEEDING_QUEUED:
									episode.setState(EpisodeState.DOWNLOADED);
									try {
										uTorrentService.stopTorrent(torrent.getHash());
									} catch (final Exception e) {
										e.printStackTrace();
									}
									break;
								case DOWNLOADING:
									episode.setState(EpisodeState.DOWNLOADING);
									break;
								case DOWNLOADING_QUEUED:
								case PAUSED:
									episode.setState(EpisodeState.QUEUED);
									break;
								default:
									break;
								}
								episodeDAO.persist(episode);
							}
						}
					});
					transaction.commit();
				} catch (final Exception e) {
					transaction.rollback();
					throw new RuntimeException(e);
				}
			} finally {
				session.close();
				ManagedSessionContext.unbind(sessionFactory);
			}

			try {
				Thread.sleep(10000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isStopping() {
		return stopping;
	}

	public void setStopping(final boolean stopping) {
		this.stopping = stopping;
	}
}