package nz.co.wing.tvmanager.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;
import nz.co.wing.tvmanager.model.Series;

public class SeriesDAO extends AbstractDAO<Series> {

	public SeriesDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Series read(final String id) {
		return get(id);
	}

	public List<Series> list() {
		return list(currentSession().createCriteria(getEntityClass()));
	}

	@Override
	public Series persist(final Series series) {
		return super.persist(series);
	}

	public void delete(final String id) {
		final Series series = read(id);
		currentSession().delete(series);
	}
}
