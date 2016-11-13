package nz.co.wing.tvmanager.resource;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nz.co.wing.tvmanager.model.TreeNode;
import nz.co.wing.tvmanager.service.ServiceException;
import nz.co.wing.tvmanager.service.XBMCService;

@Path("/xbmc")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
@Consumes(MediaType.APPLICATION_JSON)
public class XBMCResource {
	private final XBMCService xbmcService;

	public XBMCResource(final XBMCService xbmcService) {
		super();
		this.xbmcService = xbmcService;
	}

	@POST
	@Path("/input/back")
	public Response inputBack() throws ServiceException {
		final String input = xbmcService.inputBack();
		return Response.ok(input).build();
	}

	@POST
	@Path("/input/up")
	public Response inputUp() throws ServiceException {
		final String input = xbmcService.inputUp();
		return Response.ok(input).build();
	}

	@POST
	@Path("/input/down")
	public Response inputDown() throws ServiceException {
		final String input = xbmcService.inputDown();
		return Response.ok(input).build();
	}

	@POST
	@Path("/input/left")
	public Response inputLeft() throws ServiceException {
		final String input = xbmcService.inputLeft();
		return Response.ok(input).build();
	}

	@POST
	@Path("/input/right")
	public Response inputRight() throws ServiceException {
		final String input = xbmcService.inputRight();
		return Response.ok(input).build();
	}

	@POST
	@Path("/input/select")
	public Response inputSelect() throws ServiceException {
		final String input = xbmcService.inputSelect();
		return Response.ok(input).build();
	}

	@POST
	@Path("/input/home")
	public Response inputHome() throws ServiceException {
		final String input = xbmcService.inputHome();
		return Response.ok(input).build();
	}

	@POST
	@Path("/input/showosd")
	public Response inputShowOSD() throws ServiceException {
		final String input = xbmcService.inputShowOSD();
		return Response.ok(input).build();
	}

	@POST
	@Path("/player/getActivePlayers")
	public Response getActivePlayers() throws ServiceException {
		final List<Map<String, Object>> map = xbmcService.playerGetActivePlayers();
		return Response.ok(map).build();
	}

	@POST
	@Path("/player/{id}/getProperties")
	public Response getPlayerProperties(final @PathParam("id") int playerId, final String... properties)
			throws ServiceException {
		final Map<String, Object> map = xbmcService.playerGetProperties(playerId, properties);
		return Response.ok(map).build();
	}

	@POST
	@Path("/player/{id}/getItem")
	public Response getPlayerItem(final @PathParam("id") int playerId, final String... properties)
			throws ServiceException {
		final Map<String, Object> map = xbmcService.playerGetItem(playerId, properties);
		return Response.ok(map).build();
	}

	@POST
	@Path("/player/{id}/goto/{to}")
	public Response playerGoTo(final @PathParam("id") int playerId, final @PathParam("to") String to)
			throws ServiceException {
		final String res = xbmcService.playerGoTo(playerId, to);
		return Response.ok(res).build();
	}

	@POST
	@Path("/player/{id}/playPause")
	public Response playerPlayPause(final @PathParam("id") int playerId) throws ServiceException {
		final Map<String, Object> res = xbmcService.playerPlayPause(playerId);
		return Response.ok(res).build();
	}

	@POST
	@Path("/player/{id}/stop")
	public Response playerStop(final @PathParam("id") int playerId) throws ServiceException {
		final String res = xbmcService.playerStop(playerId);
		return Response.ok(res).build();
	}

	@POST
	@Path("/player/open")
	public Response playerOpen(final @QueryParam("file") String file) throws ServiceException {
		final String res = xbmcService.playerOpen(file);
		return Response.ok(res).build();
	}

	@POST
	@Path("/system/ejectOpticalDrive")
	public Response systemEjectOpticalDrive() throws ServiceException {
		final String res = xbmcService.systemEjectOpticalDrive();
		return Response.ok(res).build();
	}

	@POST
	@Path("/system/hibernate")
	public Response systemHibernate() throws ServiceException {
		final String res = xbmcService.systemHibernate();
		return Response.ok(res).build();
	}

	@POST
	@Path("/system/reboot")
	public Response systemReboot() throws ServiceException {
		final String res = xbmcService.systemReboot();
		return Response.ok(res).build();
	}

	@POST
	@Path("/system/shutdown")
	public Response systemShutdown() throws ServiceException {
		final String res = xbmcService.systemShutdown();
		return Response.ok(res).build();
	}

	@POST
	@Path("/system/suspend")
	public Response systemSuspend() throws ServiceException {
		final String res = xbmcService.systemSuspend();
		return Response.ok(res).build();
	}

	@POST
	@Path("/application/setVolume/{volume}")
	public Response applicationSetVolume(final @PathParam("volume") int volume) throws ServiceException {
		final int res = xbmcService.applicationSetVolume(volume);
		return Response.ok(res).build();
	}

	@POST
	@Path("/application/setVolume/{increment:true|false}")
	public Response applicationSetVolume(final @PathParam("increment") boolean increment) throws ServiceException {
		final int res = xbmcService.applicationSetVolume(increment);
		return Response.ok(res).build();
	}

	@POST
	@Path("/application/mute")
	public Response applicationSetMute() throws ServiceException {
		final boolean res = xbmcService.applicationSetMute();
		return Response.ok(res).build();
	}

	@POST
	@Path("/playlist/getPlaylists")
	public Response playlistGetPlaylists() throws ServiceException {
		final List<Map<String, Object>> res = xbmcService.playlistGetPlaylists();
		return Response.ok(res).build();
	}

	@POST
	@Path("/playlist/{id}/getItems")
	public Response playlistGetItems(final @PathParam("id") int playlistId, final List<String> properties)
			throws ServiceException {
		final Map<String, Object> res = xbmcService.playlistGetItems(playlistId, properties, null, null);
		return Response.ok(res).build();
	}

	@POST
	@Path("/files/getDirectory")
	public Response filesGetDirectory(final @QueryParam("dir") String dir) throws ServiceException {
		final Map<String, Object> res = xbmcService.filesGetDirectory(dir);
		return Response.ok(res).build();
	}

	@POST
	@Path("/files/getFileDetails")
	public Response filesGetFileDetails(final @QueryParam("file") String file) throws ServiceException {
		final Map<String, Object> res = xbmcService.filesGetFileDetails(file);
		return Response.ok(res).build();
	}

	@GET
	@Path("/files")
	public Response getFileTree() throws ServiceException {
		final TreeNode node = xbmcService.getFileTree();
		return Response.ok(node).build();
	}
}
