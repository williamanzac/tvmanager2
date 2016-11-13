package nz.co.wing.tvmanager.resource;

import java.util.List;

import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nz.co.wing.tvmanager.model.FileTask;
import nz.co.wing.tvmanager.model.TreeNode;
import nz.co.wing.tvmanager.service.FilesService;
import nz.co.wing.tvmanager.service.ServiceException;
import com.codahale.metrics.annotation.Timed;

@Path("/files")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
@Consumes(MediaType.APPLICATION_JSON)
public class FilesResource {

	private final FilesService filesService;

	public FilesResource(final FilesService filesService) {
		this.filesService = filesService;
	}

	@GET
	@Path("/torrent")
	@Timed
	@UnitOfWork
	public Response getTorrentTree() throws ServiceException {
		final TreeNode node = filesService.getTorrentDirTree();
		return Response.ok(node).build();
	}

	public static class RenameFile {
		public String orgName;
		public String newName;
	}

	@POST
	@Path("/torrent/rename")
	@Timed
	@UnitOfWork
	public Response renameTorrentFile(final RenameFile arg) throws ServiceException {
		filesService.renameTorrentFile(arg.orgName, arg.newName);
		return Response.ok(arg).build();
	}

	@POST
	@Path("/torrent/move/intermediate")
	@Timed
	@UnitOfWork
	public Response moveTorrentToIntermediate(final RenameFile arg) throws ServiceException {
		filesService.moveTorrentToIntermediate(arg.orgName, arg.newName);
		return Response.ok(arg).build();
	}

	@POST
	@Path("/torrent/move/destination")
	@Timed
	@UnitOfWork
	public Response moveTorrentToDesination(final RenameFile arg) throws ServiceException {
		filesService.moveTorrentToDestination(arg.orgName, arg.newName);
		return Response.ok(arg).build();
	}

	@DELETE
	@Path("/torrent")
	@Timed
	@UnitOfWork
	public Response removeTorrentFile(final RenameFile arg) throws ServiceException {
		filesService.removeTorrentFile(arg.orgName);
		return Response.ok(arg).build();
	}

	@GET
	@Path("/intermediate")
	@Timed
	@UnitOfWork
	public Response getIndermediateTree() throws ServiceException {
		final TreeNode node = filesService.getIntermediateDirTree();
		return Response.ok(node).build();
	}

	@POST
	@Path("/intermediate/rename")
	@Timed
	@UnitOfWork
	public Response renameIntermediateFile(final RenameFile arg) throws ServiceException {
		filesService.renameIntermediateFile(arg.orgName, arg.newName);
		return Response.ok(arg).build();
	}

	@DELETE
	@Path("/intermediate")
	@Timed
	@UnitOfWork
	public Response removeIntermediateFile(final RenameFile arg) throws ServiceException {
		filesService.removeIntermediateFile(arg.orgName);
		return Response.ok(arg).build();
	}

	@GET
	@Path("/destination")
	@Timed
	@UnitOfWork
	public Response getDestinationTree() throws ServiceException {
		final TreeNode node = filesService.getDestinationDirTree();
		return Response.ok(node).build();
	}

	@POST
	@Path("/destination/rename")
	@Timed
	@UnitOfWork
	public Response renameDestinationFile(final RenameFile arg) throws ServiceException {
		filesService.renameDestinationFile(arg.orgName, arg.newName);
		return Response.ok(arg).build();
	}

	@DELETE
	@Path("/destination")
	@Timed
	@UnitOfWork
	public Response removeDestinationFile(final RenameFile arg) throws ServiceException {
		filesService.removeDestinationFile(arg.orgName);
		return Response.ok(arg).build();
	}

	@GET
	@Path("/tasks")
	@Timed
	@UnitOfWork
	public Response getTasks() throws ServiceException {
		final List<FileTask> list = filesService.listFileTasks();
		return Response.ok(list).build();
	}
}
