package nz.co.wing.tvmanager.resource;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.Collections;
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
import nz.co.wing.tvmanager.service.EpisodeService;
import nz.co.wing.tvmanager.service.SeriesService;
import nz.co.wing.tvmanager.service.ServiceException;

import com.codahale.metrics.annotation.Timed;

@Path("/series")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
@Consumes(MediaType.APPLICATION_JSON)
public class SeriesResource {

	private final SeriesService seriesService;
	private final EpisodeService episodeService;

	public SeriesResource(final SeriesService seriesService, final EpisodeService episodeService) {
		super();
		this.seriesService = seriesService;
		this.episodeService = episodeService;
	}

	@GET
	@Timed
	@UnitOfWork
	@Produces(MediaType.TEXT_HTML)
	public Response listHTML() {
		final List<Series> list = seriesService.list();
		return Response.ok(list).build();
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
		return Response.ok(list).build();
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
		Collections.sort(episodes, (o1, o2) -> {
			final int s1 = o1.getSeasonNumber();
			final int s2 = o2.getSeasonNumber();
			final int e1 = o1.getEpisodeNumber();
			final int e2 = o2.getEpisodeNumber();
			if (s1 > s2) {
				return -1;
			}
			if (s2 > s1) {
				return 1;
			}
			if (e1 > e2) {
				return -1;
			}
			if (e2 > e1) {
				return 1;
			}
			return 0;
		});

		return Response.ok(episodes).build();
	}

	@GET
	@Timed
	@UnitOfWork
	@Path("/{id}/banners")
	public Response listBanners(final @PathParam("id") String id) throws ServiceException {
		final Banners banners = seriesService.banners(id);
		return Response.ok(banners).build();
	}
}
