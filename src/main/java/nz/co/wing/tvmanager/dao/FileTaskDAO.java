package nz.co.wing.tvmanager.dao;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import nz.co.wing.tvmanager.model.FileTask;

import org.hibernate.SessionFactory;

public class FileTaskDAO extends AbstractDAO<FileTask> {

	public FileTaskDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public FileTask read(final Long id) {
		return get(id);
	}

	public List<FileTask> list() {
		return list(currentSession().createCriteria(getEntityClass()));
	}

	@Override
	public FileTask persist(final FileTask task) {
		return super.persist(task);
	}

	public void delete(final Long id) {
		final FileTask task = read(id);
		currentSession().delete(task);
	}
}
