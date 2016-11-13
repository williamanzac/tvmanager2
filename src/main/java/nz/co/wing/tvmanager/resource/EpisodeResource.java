package nz.co.wing.tvmanager.resource;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import nz.co.wing.tvmanager.model.Episode;
import nz.co.wing.tvmanager.model.EpisodeState;
import nz.co.wing.tvmanager.model.Torrent;
import nz.co.wing.tvmanager.service.EpisodeService;
import nz.co.wing.tvmanager.service.ServiceException;
import nz.co.wing.tvmanager.view.EpisodeView;
import nz.co.wing.tvmanager.view.TorrentSearchView;

import com.codahale.metrics.annotation.Timed;

@Path("/episodes")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
@Consumes(MediaType.APPLICATION_JSON)
public class EpisodeResource {

	private final EpisodeService episodeService;

	public EpisodeResource(final EpisodeService service) {
		super();
		episodeService = service;
	}

	@GET
	@Timed
	@UnitOfWork
	public Response list() {
		final List<Episode> list = episodeService.listEpisodes();
		return Response.ok(list).build();
	}

	@DELETE
	@Timed
	@UnitOfWork
	@Path("/{id}")
	public void delete(final @PathParam("id") String id) {
		episodeService.deleteEpisode(id);
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/{id}")
	public Response read(final @PathParam("id") String id) {
		final Episode read = episodeService.readEpisode(id);
		final EpisodeView view = new EpisodeView(read);
		return Response.ok(view).build();
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/{id}/search")
	public Response search(final @PathParam("id") String id) throws ServiceException {
		final List<Torrent> list = episodeService.searchEpisode(id);
		final TorrentSearchView view = new TorrentSearchView(list);
		return Response.ok(view).build();
	}

	@POST
	@Timed
	@UnitOfWork
	@Path("/{id}/torrent")
	public Response addTorrent(final @PathParam("id") String id, final Torrent torrent) throws ServiceException {
		final Torrent create = episodeService.addTorrent(id, torrent);
		return Response.created(UriBuilder.fromMethod(TorrentResource.class, "read").build(create.getHash()))
				.entity(create).build();
	}

	@PUT
	@Timed
	@UnitOfWork
	@Path("/{id}/status/{status}")
	public Response setStatus(final @PathParam("id") String id, final @PathParam("status") EpisodeState status)
			throws ServiceException {
		final Episode episode = episodeService.setStatus(id, status);
		return Response.ok(episode).build();
	}
}
