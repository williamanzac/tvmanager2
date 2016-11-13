package nz.co.wing.tvmanager.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nz.co.wing.tvmanager.config.UTorrentConfiguration;
import nz.co.wing.tvmanager.model.Torrent;
import nz.co.wing.tvmanager.model.TorrentFile;
import nz.co.wing.tvmanager.model.TorrentPriority;
import nz.co.wing.tvmanager.model.TorrentState;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.ccil.cowan.tagsoup.Parser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UTorrentService {
	private static String authtoken;

	private final UTorrentConfiguration configuration;
	private final SAXReader htmlReader = new SAXReader(new Parser());
	private final HttpClient httpClient;

	public UTorrentService(final UTorrentConfiguration configuration, final HttpClient httpClient) {
		super();
		this.configuration = configuration;
		this.httpClient = httpClient;
	}

	private InputStream getResponse(final String path) throws ServiceException {
		return getResponse(path, null);
	}

	private InputStream getResponse(final Map<String, String> additional) throws ServiceException {
		return getResponse(null, additional);
	}

	private InputStream getResponse(final String path, final Map<String, String> additional) throws ServiceException {
		System.out.println("path: " + path + ", additional: " + additional);
		try {
			final URIBuilder b = new URIBuilder(buildWebUIUrl());
			if (StringUtils.isNotBlank(path)) {
				b.setPath("/gui/" + path);
			} else {
				b.setPath("/gui/");
				String query = "token=" + getToken();
				if (additional != null) {
					for (final Entry<String, String> entry : additional.entrySet()) {
						query += "&" + entry.getKey() + "=" + entry.getValue();
					}
				}
				b.setCustomQuery(query);
			}
			final URI uri = b.build();
			System.out.println("uri: " + uri);
			final HttpGet method = new HttpGet(uri);
			final CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(method);
			return response.getEntity().getContent();
		} catch (UnsupportedOperationException | IOException | URISyntaxException e) {
			throw new ServiceException(e);
		}
	}

	private JSONObject getuTorrentResponse(final Map<String, String> additional) throws ServiceException {
		final InputStream inputStream = getResponse(additional);
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			IOUtils.copy(inputStream, stream);
			final String string = stream.toString();
			System.out.println("return: " + string);
			return new JSONObject(new JSONTokener(string));
		} catch (final IOException e) {
			throw new ServiceException(e);
		}
	}

	private String buildWebUIUrl() {
		return configuration.getApi();
	}

	private synchronized String getToken() throws ServiceException {
		if (authtoken == null) {
			try (final InputStream stream = getResponse("token.html");) {
				final Document document = htmlReader.read(stream);
				authtoken = parseToken(document);
			} catch (final DocumentException | IOException e) {
				throw new ServiceException(e);
			}
		}
		return authtoken;
	}

	private String parseToken(final Document document) throws ServiceException {
		System.out.println(document.asXML());
		final Element node = (Element) document.selectSingleNode("//html:div");
		final String string = node.getTextTrim();
		return string;
	}

	public List<Torrent> getTorrentList() throws ServiceException {
		final Map<String, String> map = new HashMap<>();
		map.put("list", "1");
		final JSONObject object = getuTorrentResponse(map);
		return parseTorrentList(object);
	}

	public void addTorrent(final Torrent torrent) throws ServiceException {
		final String url = torrent.getUrl();
		System.out.println(url);
		final Map<String, String> map = new HashMap<>();
		map.put("action", "add-url");
		map.put("s", url);
		final JSONObject response = getuTorrentResponse(map);
		System.out.println(response);
	}

	public List<TorrentFile> getFiles(final String hash) throws ServiceException {
		final Map<String, String> map = new HashMap<>();
		map.put("action", "getfiles");
		map.put("hash", hash);
		final JSONObject object = getuTorrentResponse(map);
		return parseFileList(object);
	}

	private List<TorrentFile> parseFileList(final JSONObject object) {
		final JSONArray jsonArray = object.getJSONArray("files");
		System.out.println("jsonArray:" + jsonArray);
		final List<TorrentFile> files = new ArrayList<>();
		final JSONArray filesArray = jsonArray.getJSONArray(1);
		System.out.println("filesArray:" + filesArray);
		for (int i = 0; i < filesArray.length(); i++) {
			final JSONArray fileArray = filesArray.getJSONArray(i);
			final String name = fileArray.getString(0);
			final int size = fileArray.getInt(1);
			final int downloaded = fileArray.getInt(2);
			final int int1 = fileArray.getInt(3);
			final TorrentPriority priority = TorrentPriority.values()[int1];

			final TorrentFile file = new TorrentFile();
			file.setDownloaded(downloaded);
			file.setName(name);
			file.setPriority(priority);
			file.setSize(size);

			files.add(file);
		}
		return files;
	}

	private List<Torrent> parseTorrentList(final JSONObject object) {
		final JSONArray jsonArray = object.getJSONArray("torrents");
		final List<Torrent> torrents = new ArrayList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			final JSONArray tor = jsonArray.getJSONArray(i);
			final String hash = tor.getString(0);
			final int status = tor.getInt(1);
			final String name = tor.getString(2);
			final int size = tor.getInt(3); // bytes
			final int percent = tor.getInt(4);
			final int upSpeed = tor.getInt(8);
			final int downSpeed = tor.getInt(9);
			final int eta = tor.getInt(10);

			final boolean finished = percent == 1000l;

			final Torrent torrent = new Torrent();
			torrent.setHash(hash);
			torrent.setPercentComplete(percent / 1000f * 100);
			torrent.setSize(size);
			torrent.setTitle(name);
			torrent.setState(convertState(status, finished));
			torrent.setDownSpeed(downSpeed);
			torrent.setEta(eta);
			torrent.setUpSpeed(upSpeed);

			torrents.add(torrent);
		}
		return torrents;
	}

	private TorrentState convertState(final int status, final boolean finished) {
		// Convert bitwise int to uTorrent status codes
		// Now based on http://forum.utorrent.com/viewtopic.php?id=50779
		if ((status & 16) == 16) {
			// Error
			return TorrentState.ERROR;
		} else if ((status & 32) == 32) {
			// Paused
			return TorrentState.PAUSED;
		} else if ((status & 1) == 1) {
			// Started
			return !finished ? TorrentState.DOWNLOADING : TorrentState.SEEDING;
		} else if ((status & 128) == 128) {
			// Queued
			return !finished ? TorrentState.DOWNLOADING_QUEUED : TorrentState.SEEDING_QUEUED;
		}
		return null;
	}

	public void startTorrent(final String hash) throws ServiceException {
		final Map<String, String> map = new HashMap<>();
		map.put("action", "start");
		map.put("hash", hash);
		getuTorrentResponse(map);
	}

	public void pauseTorrent(final String hash) throws ServiceException {
		final Map<String, String> map = new HashMap<>();
		map.put("action", "pause");
		map.put("hash", hash);
		getuTorrentResponse(map);
	}

	public void stopTorrent(final String hash) throws ServiceException {
		final Map<String, String> map = new HashMap<>();
		map.put("action", "stop");
		map.put("hash", hash);
		getuTorrentResponse(map);
	}

	public void removeTorrent(final String hash) throws ServiceException {
		final Map<String, String> map = new HashMap<>();
		map.put("action", "remove");
		map.put("hash", hash);
		getuTorrentResponse(map);
	}
}
