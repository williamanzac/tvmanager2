package nz.co.wing.tvmanager.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.co.wing.tvmanager.config.AppConfiguration;
import nz.co.wing.tvmanager.config.FilesConfiguration;
import nz.co.wing.tvmanager.dao.FileTaskDAO;
import nz.co.wing.tvmanager.model.FileTask;
import nz.co.wing.tvmanager.model.FileTaskType;
import nz.co.wing.tvmanager.model.TreeNode;

public class FilesService {
	// private static final Logger LOGGER = Logger.getLogger(FilesService.class);
	private final FileTaskDAO taskDAO;

	private final AppConfiguration configuration;

	public FilesService(final AppConfiguration configuration, final FileTaskDAO taskDAO) {
		this.configuration = configuration;
		this.taskDAO = taskDAO;
	}

	public TreeNode getTree(final File dir) throws ServiceException {
		final Map<String, TreeNode> nodeMap = new HashMap<>();
		final Path path = dir.toPath();
		try {
			Files.walk(path).forEach(p -> {
				final TreeNode node = new TreeNode();
				node.setText(p.getFileName() != null ? p.getFileName().toString() : "/");
				if (Files.isDirectory(p)) {
					node.setIcon("glyphicon glyphicon-folder-close");
				} else {
					node.setIcon("glyphicon glyphicon-file");
				}
				node.setSelectable(true);
				nodeMap.put(p.toAbsolutePath().toString(), node);

				final Path parent = p.getParent();
				if (parent != null) {
					final String pp = parent.toAbsolutePath().toString();
					final TreeNode parentNode = nodeMap.get(pp);
					if (parentNode != null) {
						parentNode.getNodes().add(node);
					}
				}
			});
		} catch (final IOException e) {
			throw new ServiceException(e);
		}

		// LOGGER.info(nodeMap);
		// LOGGER.info(path.toAbsolutePath().toString());
		final TreeNode rootNode = nodeMap.get(path.toAbsolutePath().toString());
		rootNode.setText(path.getFileName() != null ? path.getFileName().toString() : "/");
		rootNode.setIcon("glyphicon glyphicon-folder-close");
		rootNode.getState().setExpanded(true);
		return rootNode;
	}

	public TreeNode getTorrentDirTree() throws ServiceException {
		return getTree(getTorrentDir());
	}

	private FilesConfiguration getFiles() {
		return configuration.getFiles();
	}

	public TreeNode getDestinationDirTree() throws ServiceException {
		return getTree(getDestinationDir());
	}

	public TreeNode getIntermediateDirTree() throws ServiceException {
		return getTree(getIntermediateDir());
	}

	private void moveFile(final Path orgPath, final Path newPath) throws ServiceException {
		Path targetPath = newPath;
		if (Files.isDirectory(newPath)) {
			targetPath = newPath.resolve(orgPath.getFileName());
		}
		final FileTask task = new FileTask();
		task.setSource(orgPath.toString());
		task.setTarget(targetPath.toString());
		task.setType(FileTaskType.move);
		try {
			task.setSize(Files.size(orgPath));
		} catch (final IOException e) {
			throw new ServiceException(e);
		}
		persistFileTask(task);
	}

	private void renameFile(final Path orgPath, final Path newPath) throws ServiceException {
		try {
			Files.move(orgPath, newPath);
		} catch (final IOException e) {
			throw new ServiceException(e);
		}
	}

	private void removeFile(final Path path) throws ServiceException {
		try {
			Files.delete(path);
		} catch (final IOException e) {
			throw new ServiceException(e);
		}
	}

	public void renameTorrentFile(final String orgName, final String newName) throws ServiceException {
		final Path orgPath = new File(getTorrentDir(), orgName).toPath();
		final Path newPath = new File(getTorrentDir(), newName).toPath();
		renameFile(orgPath, newPath);
	}

	public void removeTorrentFile(final String name) throws ServiceException {
		final Path path = new File(getTorrentDir(), name).toPath();
		removeFile(path);
	}

	private File getTorrentDir() {
		return getFiles().getTorrentDir();
	}

	public void renameIntermediateFile(final String orgName, final String newName) throws ServiceException {
		final Path orgPath = new File(getIntermediateDir(), orgName).toPath();
		final Path newPath = new File(getIntermediateDir(), newName).toPath();
		renameFile(orgPath, newPath);
	}

	public void removeIntermediateFile(final String name) throws ServiceException {
		final Path path = new File(getIntermediateDir(), name).toPath();
		removeFile(path);
	}

	private File getIntermediateDir() {
		return getFiles().getIntermediateDir();
	}

	public void renameDestinationFile(final String orgName, final String newName) throws ServiceException {
		final Path orgPath = new File(getDestinationDir(), orgName).toPath();
		final Path newPath = new File(getDestinationDir(), newName).toPath();
		renameFile(orgPath, newPath);
	}

	public void removeDestinationFile(final String name) throws ServiceException {
		final Path path = new File(getDestinationDir(), name).toPath();
		removeFile(path);
	}

	private File getDestinationDir() {
		return getFiles().getDestinationDir();
	}

	public void moveTorrentToIntermediate(final String orgName, final String newName) throws ServiceException {
		final Path orgPath = new File(getTorrentDir(), orgName).toPath();
		final Path newPath = new File(getIntermediateDir(), newName).toPath();
		moveFile(orgPath, newPath);
	}

	public void moveIntermediateToDestination(final String orgName, final String newName) throws ServiceException {
		final Path orgPath = new File(getIntermediateDir(), orgName).toPath();
		final Path newPath = new File(getDestinationDir(), newName).toPath();
		moveFile(orgPath, newPath);
	}

	public void moveTorrentToDestination(final String orgName, final String newName) throws ServiceException {
		final Path orgPath = new File(getTorrentDir(), orgName).toPath();
		final Path newPath = new File(getDestinationDir(), newName).toPath();
		moveFile(orgPath, newPath);
	}

	public FileTask readFileTask(final Long id) {
		return taskDAO.read(id);
	}

	public List<FileTask> listFileTasks() {
		return taskDAO.list();
	}

	public FileTask persistFileTask(final FileTask task) {
		return taskDAO.persist(task);
	}

	public void deleteFileTask(final Long id) {
		taskDAO.delete(id);
	}
}
