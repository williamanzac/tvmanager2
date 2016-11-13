package nz.co.wing.tvmanager.dao;

import static org.hibernate.criterion.Restrictions.ilike;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.wing.tvmanager.model.Torrent;

public class TorrentDAO extends AbstractDAO<Torrent> {

	public TorrentDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Torrent read(final String id) {
		return get(id.toUpperCase());
	}

	public List<Torrent> list() {
		return list(createCriteria());
	}

	private Criteria createCriteria() {
		return currentSession().createCriteria(getEntityClass());
	}

	@Override
	public Torrent persist(final Torrent series) {
		series.setHash(series.getHash().toUpperCase());
		return super.persist(series);
	}

	public void delete(final String id) {
		// final Torrent series = read(id);
		final Torrent series = uniqueResult(createCriteria().add(ilike("hash", id)));
		currentSession().delete(series);
	}
}
