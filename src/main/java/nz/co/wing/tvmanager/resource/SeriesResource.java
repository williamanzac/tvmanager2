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

import nz.co.wing.tvmanager.model.Banners;
import nz.co.wing.tvmanager.model.Episode;
import nz.co.wing.tvmanager.model.Series;
import nz.co.wing.tvmanager.model.Torrent;
import nz.co.wing.tvmanager.service.EpisodeService;
import nz.co.wing.tvmanager.service.SeriesService;
import nz.co.wing.tvmanager.service.ServiceException;
import nz.co.wing.tvmanager.service.TorrentService;
import nz.co.wing.tvmanager.view.BannersView;
import nz.co.wing.tvmanager.view.EpisodeListView;
import nz.co.wing.tvmanager.view.SeriesListView;
import nz.co.wing.tvmanager.view.SeriesSearchView;

import com.codahale.metrics.annotation.Timed;

@Path("/series")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
@Consumes(MediaType.APPLICATION_JSON)
public class SeriesResource {

	private final SeriesService seriesService;
	private final EpisodeService episodeService;
	private final TorrentService torrentService;

	public SeriesResource(final SeriesService seriesService, final EpisodeService episodeService,
			final TorrentService torrentService) {
		super();
		this.seriesService = seriesService;
		this.episodeService = episodeService;
		this.torrentService = torrentService;
	}

	@GET
	@Timed
	@UnitOfWork
	@Produces(MediaType.TEXT_HTML)
	public Response listHTML() {
		final List<Series> list = seriesService.list();
		final SeriesListView view = new SeriesListView(list);
		return Response.ok(view).build();
	}

	@GET
	@Timed
	@UnitOfWork
	@Produces(MediaType.APPLICATION_JSON)
	public Response listJSON() {
		final List<Series> list = seriesService.list();
		return Response.ok(list).build();
	}

	@GET
	@Timed
	@Path("/search")
	public Response search(final @QueryParam("s") String search) throws ServiceException {
		final List<Series> list = seriesService.search(search);
		final SeriesSearchView view = new SeriesSearchView(list);
		return Response.ok(view).build();
	}

	@POST
	@Timed
	@UnitOfWork
	public Response create(final Series series) throws ServiceException {
		final Series create = seriesService.create(series);
		return Response.created(UriBuilder.fromMethod(getClass(), "read").build(create.getId())).entity(create).build();
	}

	@DELETE
	@Timed
	@UnitOfWork
	@Path("/{id}")
	public void delete(final @PathParam("id") String id) {
		seriesService.delete(id);
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/{id}")
	public Response read(final @PathParam("id") String id) {
		final Series read = seriesService.read(id);
		return Response.ok(read).build();
	}

	@POST
	@Timed
	@UnitOfWork
	@Path("/{id}/update")
	public void update(final @PathParam("id") String id) throws ServiceException {
		seriesService.update(id);
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/{id}/episodes")
	public Response listEpisodes(final @PathParam("id") String id) throws ServiceException {
		final List<Episode> episodes = episodeService.listEpisodes(id);
		final List<Torrent> torrents = torrentService.listTorrents();
		final EpisodeListView view = new EpisodeListView(episodes, torrents);
		return Response.ok(view).build();
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/{id}/banners")
	public Response listBanners(final @PathParam("id") String id) throws ServiceException {
		final Banners banners = seriesService.banners(id);
		final BannersView view = new BannersView(banners);
		return Response.ok(view).build();
	}
}
