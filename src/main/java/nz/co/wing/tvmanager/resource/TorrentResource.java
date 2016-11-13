package nz.co.wing.tvmanager.resource;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import nz.co.wing.tvmanager.model.Torrent;
import nz.co.wing.tvmanager.model.TorrentFile;
import nz.co.wing.tvmanager.service.ServiceException;
import nz.co.wing.tvmanager.service.TorrentService;
import nz.co.wing.tvmanager.view.TorrentFilesView;
import nz.co.wing.tvmanager.view.TorrentListView;
import nz.co.wing.tvmanager.view.TorrentSearchView;

import com.codahale.metrics.annotation.Timed;

@Path("/torrents")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
@Consumes(MediaType.APPLICATION_JSON)
public class TorrentResource {

	private final TorrentService service;

	public TorrentResource(final TorrentService service) {
		super();
		this.service = service;
	}

	@GET
	@Timed
	@UnitOfWork
	public Response list() {
		final List<Torrent> list = service.listTorrents();
		final TorrentListView view = new TorrentListView(list);
		return Response.ok(view).build();
	}

	@POST
	@Timed
	@UnitOfWork
	public Response create(final Torrent torrent) throws ServiceException {
		final Torrent create = service.createTorrent(torrent);
		return Response.created(UriBuilder.fromMethod(getClass(), "read").build(create.getHash())).entity(create)
				.build();
	}

	@DELETE
	@Timed
	@UnitOfWork
	@Path("/{hash}")
	public void delete(final @PathParam("hash") String hash) throws ServiceException {
		service.deleteTorrent(hash);
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/{hash}")
	public Response read(final @PathParam("hash") String hash) {
		final Torrent read = service.readTorrent(hash);
		return Response.ok(read).build();
	}

	@POST
	@Timed
	@UnitOfWork
	@Path("/{hash}/start")
	public void start(final @PathParam("hash") String hash) throws ServiceException {
		service.startTorrent(hash);
	}

	@POST
	@Timed
	@UnitOfWork
	@Path("/{hash}/pause")
	public void pause(final @PathParam("hash") String hash) throws ServiceException {
		service.pauseTorrent(hash);
	}

	@POST
	@Timed
	@UnitOfWork
	@Path("/{hash}/stop")
	public void stop(final @PathParam("hash") String hash) throws ServiceException {
		service.stopTorrent(hash);
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/search")
	public Response search(final @QueryParam("s") String search) throws ServiceException {
		final List<Torrent> list = service.findTorrents(search);
		final TorrentSearchView view = new TorrentSearchView(list);
		return Response.ok(view).build();
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/{hash}/files")
	public Response getFiles(final @PathParam("hash") String hash) throws ServiceException {
		final List<TorrentFile> list = service.getFiles(hash);
		final TorrentFilesView view = new TorrentFilesView(list);
		return Response.ok(view).build();
	}
}
