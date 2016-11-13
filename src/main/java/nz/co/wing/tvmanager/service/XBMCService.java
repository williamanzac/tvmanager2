package nz.co.wing.tvmanager.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nz.co.wing.tvmanager.config.XBMCConfiguration;
import nz.co.wing.tvmanager.model.TreeNode;
import nz.co.wing.tvmanager.model.xbmc.List.Limit;
import nz.co.wing.tvmanager.model.xbmc.List.Sort;
import nz.co.wing.tvmanager.model.xbmc.RPCRequest;
import nz.co.wing.tvmanager.model.xbmc.RPCResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class XBMCService {
	private static final Logger LOGGER = Logger.getLogger(XBMCService.class);

	private final HttpClient xbmcClient;
	private final XBMCConfiguration configuration;

	public XBMCService(final HttpClient xbmcClient, final XBMCConfiguration configuration) {
		this.xbmcClient = xbmcClient;
		this.configuration = configuration;
	}

	private <T> T rpcMethod(final String rpcMethod) throws ServiceException {
		return rpcMethod(rpcMethod, Collections.emptyMap());
	}

	@SuppressWarnings("unchecked")
	private <T> T rpcMethod(final String rpcMethod, final Map<String, Object> params) throws ServiceException {
		final ObjectMapper mapper = new ObjectMapper();
		final RPCRequest request = new RPCRequest();
		request.setMethod(rpcMethod);
		for (final Entry<String, Object> entry : params.entrySet()) {
			request.addParam(entry.getKey(), entry.getValue());
		}
		final HttpPost method = new HttpPost(configuration.getApi());
		try {
			final String reqStr = mapper.writeValueAsString(request);
			LOGGER.info(reqStr);
			final StringEntity entity = new StringEntity(reqStr, ContentType.APPLICATION_JSON);
			method.setEntity(entity);
			final HttpResponse response = xbmcClient.execute(method);
			final InputStream content = response.getEntity().getContent();
			final ByteArrayOutputStream stream = new ByteArrayOutputStream();
			IOUtils.copy(content, stream);
			final String string = stream.toString();
			LOGGER.info("return: " + string);
			final RPCResponse<T> result = mapper.readValue(string, RPCResponse.class);
			final T value = result.getResult();
			return value;
		} catch (final IOException e) {
			throw new ServiceException(e);
		}
	}

	public String inputBack() throws ServiceException {
		return rpcMethod("Input.Back");
	}

	public String inputContextMenu() throws ServiceException {
		return rpcMethod("Input.ContextMenu");
	}

	public String inputDown() throws ServiceException {
		return rpcMethod("Input.Down");
	}

	public String inputHome() throws ServiceException {
		return rpcMethod("Input.Home");
	}

	public String inputShowOSD() throws ServiceException {
		return rpcMethod("Input.ShowOSD");
	}

	public String inputInfo() throws ServiceException {
		return rpcMethod("Input.Info");
	}

	public String inputLeft() throws ServiceException {
		return rpcMethod("Input.Left");
	}

	public String inputRight() throws ServiceException {
		return rpcMethod("Input.Right");
	}

	public String inputSelect() throws ServiceException {
		return rpcMethod("Input.Select");
	}

	public String inputUp() throws ServiceException {
		return rpcMethod("Input.Up");
	}

	public Map<String, Object> playerGetProperties(final int playerId, final String... properties)
			throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("properties", properties);
		params.put("playerid", playerId);
		return rpcMethod("Player.GetProperties", params);
	}

	public List<Map<String, Object>> playerGetActivePlayers() throws ServiceException {
		return rpcMethod("Player.GetActivePlayers");
	}

	public Map<String, Object> playerGetItem(final int playerId, final String... properties) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("properties", properties);
		params.put("playerid", playerId);
		return rpcMethod("Player.GetItem", params);
	}

	public String playerGoTo(final int playerId, final String to) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("to", to);
		params.put("playerid", playerId);
		return rpcMethod("Player.GoTo", params);
	}

	public Map<String, Object> playerPlayPause(final int playerId) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("play", "toggle");
		params.put("playerid", playerId);
		return rpcMethod("Player.PlayPause", params);
	}

	public String playerStop(final int playerId) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("playerid", playerId);
		return rpcMethod("Player.Stop", params);
	}

	public String playerOpen(final String file) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		final Map<String, Object> item = new HashMap<>();
		item.put("file", file);
		params.put("item", item);
		return rpcMethod("Player.Open", params);
	}

	public String systemEjectOpticalDrive() throws ServiceException {
		return rpcMethod("System.EjectOpticalDrive");
	}

	public String systemHibernate() throws ServiceException {
		return rpcMethod("System.Hibernate");
	}

	public String systemReboot() throws ServiceException {
		return rpcMethod("System.Reboot");
	}

	public String systemShutdown() throws ServiceException {
		return rpcMethod("System.Shutdown");
	}

	public String systemSuspend() throws ServiceException {
		return rpcMethod("System.Suspend");
	}

	public int applicationSetVolume(final int volume) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("volume", volume);
		return rpcMethod("Application.SetVolume", params);
	}

	public int applicationSetVolume(final boolean increment) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("volume", increment ? "increment" : "decrement");
		return rpcMethod("Application.SetVolume", params);
	}

	public boolean applicationSetMute() throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("mute", "toggle");
		return rpcMethod("Application.SetMute", params);
	}

	public List<Map<String, Object>> playlistGetPlaylists() throws ServiceException {
		return rpcMethod("Playlist.GetPlaylists");
	}

	public Map<String, Object> playlistGetItems(final int playlistId, final List<String> properties,
			final Limit limits, final Sort sort) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("playlistid", playlistId);
		if (properties != null && !properties.isEmpty()) {
			params.put("properties", properties);
		}
		if (limits != null) {
			params.put("limits", limits);
		}
		if (sort != null) {
			params.put("sort", sort);
		}
		return rpcMethod("Playlist.GetItems", params);
	}

	public Map<String, Object> filesGetDirectory(final String dir) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		params.put("directory", dir);
		return rpcMethod("Files.GetDirectory", params);
	}

	public Map<String, Object> filesGetFileDetails(final String file) throws ServiceException {
		final Map<String, Object> params = new HashMap<>();
		final List<String> properties = new ArrayList<>();
		properties.add("lastplayed");
		properties.add("playcount");
		properties.add("resume");
		params.put("file", file);
		params.put("properties", properties);
		return rpcMethod("Files.GetFileDetails", params);
	}

	public TreeNode getFileTree() throws ServiceException {
		final TreeNode root = new TreeNode();
		root.setText("storage");
		root.getState().setExpanded(true);
		root.setSelectable(false);
		final TreeNode tvNode = new TreeNode();
		tvNode.setText("tvshows");
		tvNode.getState().setExpanded(true);
		tvNode.setSelectable(false);
		root.getNodes().add(tvNode);
		getFiles("/storage/tvshows", tvNode);
		return root;
	}

	@SuppressWarnings("unchecked")
	private void getFiles(final String path, final TreeNode parent) throws ServiceException {
		final Map<String, Object> map = filesGetDirectory(path);
		final List<Map<String, Object>> files = (List<Map<String, Object>>) map.get("files");
		files.forEach(f -> {
			final TreeNode node = new TreeNode();
			node.setText((String) f.get("label"));
			if ("directory".equals(f.get("filetype"))) {
				node.setIcon("glyphicon glyphicon-folder-close");
				node.setSelectable(false);
				try {
					getFiles(path + "/" + node.getText(), node);
				} catch (final Exception e) {
					LOGGER.error(e);
				}
			} else {
				node.setSelectable(true);
				node.setIcon("glyphicon glyphicon-file");
			}
			parent.getNodes().add(node);
		});
	}
}
