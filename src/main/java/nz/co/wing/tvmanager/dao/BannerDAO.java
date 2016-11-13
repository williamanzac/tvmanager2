package nz.co.wing.tvmanager.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import io.dropwizard.hibernate.AbstractDAO;
import nz.co.wing.tvmanager.model.Banners;

public class BannerDAO extends AbstractDAO<Banners> {

	public BannerDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Banners read(final int id) {
		return get(id);
	}

	public List<Banners> list() {
		return list(currentSession().createCriteria(getEntityClass()));
	}

	@Override
	public Banners persist(final Banners banners) {
		return super.persist(banners);
	}

	public void delete(final int id) {
		final Banners b = read(id);
		currentSession().delete(b);
	}
}
