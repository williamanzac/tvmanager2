package nz.co.wing.tvmanager.dao;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.or;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.wing.tvmanager.model.Episode;

public class EpisodeDAO extends AbstractDAO<Episode> {

	public EpisodeDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Episode read(final String id) {
		final Episode episode = get(id);
		if (episode != null && episode.getHash() != null) {
			episode.setHash(episode.getHash().toUpperCase());
		}
		return episode;
	}

	public List<Episode> list() {
		return list(createCriteria());
	}

	public List<Episode> list(final String seriesId) {
		return list(createCriteria().add(eq("seriesId", seriesId)));
	}

	public Episode find(final String hash) {
		return uniqueResult(createCriteria().add(or(eq("hash", hash.toUpperCase()), eq("hash", hash.toLowerCase()))));
	}

	private Criteria createCriteria() {
		return currentSession().createCriteria(getEntityClass());
	}

	public Episode find(final String seriesId, final int season, final int episode) {
		return uniqueResult(createCriteria().add(eq("seriesId", seriesId)).add(eq("seasonNumber", season))
				.add(eq("episodeNumber", episode)));
	}

	@Override
	public Episode persist(final Episode episode) {
		if (episode.getHash() != null) {
			episode.setHash(episode.getHash().toUpperCase());
		}
		return super.persist(episode);
	}

	public void delete(final String id) {
		final Episode episode = read(id);
		currentSession().delete(episode);
	}
}
