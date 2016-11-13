package nz.co.wing.tvmanager.resource;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import nz.co.wing.tvmanager.view.FileManagerView;
import nz.co.wing.tvmanager.view.TorrentManagerView;
import nz.co.wing.tvmanager.view.TvManagerView;
import nz.co.wing.tvmanager.view.XBMCView;

import org.h2.util.IOUtils;

@Path("/")
public class UIResource {

	@GET
	@Path("js/{name}")
	@Produces({ "application/javascript", "text/javascript" })
	public Response getJS(final @PathParam("name") String name) {
		final InputStream in = getClass().getResourceAsStream("/js/" + name);
		final StreamingOutput stream = output -> {
			try {
				IOUtils.copy(in, output);
			} catch (final Exception e) {
				throw new WebApplicationException(e);
			}
		};

		return Response.ok(stream).header("content-disposition", "attachment; filename = " + name).build();
	}

	@GET
	@Path("css/{name}")
	@Produces("text/css")
	public Response getCSS(final @PathParam("name") String name) {
		final InputStream in = getClass().getResourceAsStream("/css/" + name);
		final StreamingOutput stream = output -> {
			try {
				IOUtils.copy(in, output);
			} catch (final Exception e) {
				throw new WebApplicationException(e);
			}
		};

		return Response.ok(stream).header("content-disposition", "attachment; filename = " + name).build();
	}

	@GET
	@Path("ui")
	@Produces(MediaType.TEXT_HTML)
	public Response getIndex() {
		final InputStream in = getClass().getResourceAsStream("/ui/ui.html");
		final StreamingOutput stream = output -> {
			try {
				IOUtils.copy(in, output);
			} catch (final Exception e) {
				throw new WebApplicationException(e);
			}
		};

		return Response.ok(stream).build();
	}

	@GET
	@Path("ui/shows")
	@Produces(MediaType.TEXT_HTML)
	public Response getUI() {
		return Response.ok(new TvManagerView()).build();
	}

	@GET
	@Path("ui/torrents")
	@Produces(MediaType.TEXT_HTML)
	public Response getTorrentUI() {
		return Response.ok(new TorrentManagerView()).build();
	}

	@GET
	@Path("ui/files")
	@Produces(MediaType.TEXT_HTML)
	public Response getFileUI() {
		return Response.ok(new FileManagerView()).build();
	}

	@GET
	@Path("ui/xbmc")
	@Produces(MediaType.TEXT_HTML)
	public Response getXBMCUI() {
		return Response.ok(new XBMCView()).build();
	}
}
