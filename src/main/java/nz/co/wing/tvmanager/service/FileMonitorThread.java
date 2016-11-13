package nz.co.wing.tvmanager.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import nz.co.wing.tvmanager.model.FileTask;
import nz.co.wing.tvmanager.model.FileTaskType;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;

public class FileMonitorThread extends Thread {
	private static final Logger LOGGER = Logger.getLogger(FileMonitorThread.class);
	private final FilesService filesService;
	private boolean stopping = false;
	private final SessionFactory sessionFactory;

	private static class CallbackByteChannel implements ReadableByteChannel {
		private final ReadableByteChannel rbc;
		private long sizeRead;
		private final SessionFactory sessionFactory;
		private final FileTask fileTask;
		private final FilesService filesService;

		public CallbackByteChannel(final ReadableByteChannel rbc, final SessionFactory sessionFactory,
				final FilesService filesService, final FileTask fileTask) {
			this.rbc = rbc;
			this.fileTask = fileTask;
			this.sessionFactory = sessionFactory;
			this.filesService = filesService;
		}

		@Override
		public void close() throws IOException {
			rbc.close();
		}

		@Override
		public boolean isOpen() {
			return rbc.isOpen();
		}

		@Override
		public int read(final ByteBuffer bb) throws IOException {
			int n;
			double progress;
			if ((n = rbc.read(bb)) > 0) {
				sizeRead += n;
				progress = fileTask.getSize() > 0 ? sizeRead / (double) fileTask.getSize() * 100.0 : -1.0;
				final Session s = sessionFactory.openSession();
				try {
					ManagedSessionContext.bind(s);
					final Transaction t = s.beginTransaction();
					fileTask.setPercentComplete(Double.valueOf(progress).floatValue());
					filesService.persistFileTask(fileTask);
					t.commit();
				} finally {
					s.close();
					ManagedSessionContext.unbind(sessionFactory);
				}
			}
			return n;
		}
	}

	public FileMonitorThread(final FilesService filesService, final SessionFactory sessionFactory) {
		super();
		this.filesService = filesService;
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void run() {
		while (!stopping) {
			try {
				final List<FileTask> tasks;
				Session session = null;
				try {
					session = sessionFactory.openSession();
					ManagedSessionContext.bind(session);
					final Transaction transaction = session.beginTransaction();
					tasks = filesService.listFileTasks();
					transaction.commit();
				} finally {
					session.close();
					ManagedSessionContext.unbind(sessionFactory);
				}
				LOGGER.info(tasks);
				if (!tasks.isEmpty()) {
					final FileTask fileTask = tasks.get(0);
					final Path sourcePath = FileSystems.getDefault().getPath(fileTask.getSource());
					if (Files.exists(sourcePath)) {
						final Path targetPath = FileSystems.getDefault().getPath(fileTask.getTarget());
						final ReadableByteChannel inChannel = Files.newByteChannel(sourcePath, StandardOpenOption.READ);
						final CallbackByteChannel channel = new CallbackByteChannel(inChannel, sessionFactory,
								filesService, fileTask);
						final FileChannel outChannel = (FileChannel) Files.newByteChannel(targetPath,
								StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
								StandardOpenOption.WRITE);
						outChannel.transferFrom(channel, 0, Long.MAX_VALUE);
						outChannel.close();
						channel.close();
						if (fileTask.getType() == FileTaskType.move) {
							try {
								Files.delete(sourcePath);
							} catch (final IOException e) {
								LOGGER.error(e);
							}
						}
					}
					try {
						session = sessionFactory.openSession();
						ManagedSessionContext.bind(session);
						final Transaction transaction = session.beginTransaction();
						filesService.deleteFileTask(fileTask.getId());
						transaction.commit();
					} finally {
						session.close();
						ManagedSessionContext.unbind(sessionFactory);
					}
				}
			} catch (final Exception e) {
				LOGGER.error(e);
				throw new RuntimeException(e);
			}

			try {
				Thread.sleep(10000);
			} catch (final InterruptedException e) {
				LOGGER.error(e);
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
