package nz.co.wing.tvmanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.ccil.cowan.tagsoup.Parser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import nz.co.wing.tvmanager.model.Torrent;

public class TorrentProjectService {
	private static final String searchPattern = "https://torrentproject.se/?s={0}";
	private static final Pattern hashPattern = Pattern.compile("/([0-9a-z]+)/");

	private final HttpClient httpClient;
	private final SAXReader htmlReader = new SAXReader(new Parser());

	public TorrentProjectService(final HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public List<Torrent> findTorrents(final String search) throws ServiceException {
		final String query = search.replaceAll("\\s", "+");
		final String url = MessageFormat.format(searchPattern, query);
		Document document;
		try (InputStream response = getResponse(url);) {
			document = htmlReader.read(response);
		} catch (final DocumentException | IOException e) {
			throw new ServiceException(e);
		}
		final List<Torrent> torrents = parseXML(document);
		return torrents;
	}

	private InputStream getResponse(final String url) throws ServiceException {
		final HttpGet method = new HttpGet(url);
		// method.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		try {
			final CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(method);
			return response.getEntity().getContent();
		} catch (UnsupportedOperationException | IOException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	List<Torrent> parseXML(final Document document) throws ServiceException {
		System.out.println(document.asXML());
		final List<Element> itemNodes = document.selectNodes("//html:div[@class='torrent']");
		if (itemNodes == null || itemNodes.isEmpty()) {
			return null;
		}
		final List<Torrent> torrents = new ArrayList<>();
		for (final Element itemNode : itemNodes) {
			final Element titleNode = (Element) itemNode.selectSingleNode("html:h3/html:a");
			final Element categoryNode = (Element) itemNode
					.selectSingleNode("html:div[@class='gl sti']/html:span[@class='bc cate']");
			// final Element dateElement = (Element)
			// itemNode.selectSingleNode("html:div[@class='gl sti']/html:span[@class='bc cated']");
			final Element seedersNode = (Element) itemNode
					.selectSingleNode("html:div[@class='gl sti']/html:span[@class='bc seeders']/html:span/html:b");
			final Element leechersNode = (Element) itemNode
					.selectSingleNode("html:div[@class='gl sti']/html:span[@class='bc leechers']/html:span/html:b");

			final String title = titleNode.getTextTrim();
			// final String url = getMagnetLink(titleNode.attributeValue("href"));
			final String url = titleNode.attributeValue("href");

			final Set<String> categories = new HashSet<>();
			for (final String category : categoryNode.getTextTrim().split("\\s+")) {
				categories.add(category);
			}
			// final Date date = dateFormat.parse(dateElement.getTextTrim());
			final Matcher m = hashPattern.matcher(url);
			String hash = null;
			if (m.find()) {
				hash = m.group(1);
			}

			final Torrent torrent = new Torrent();
			torrent.setTitle(title);
			torrent.setUrl(url);
			torrent.setLeechers(Integer.parseInt(leechersNode.getTextTrim()));
			torrent.setSeeds(Integer.parseInt(seedersNode.getTextTrim()));
			torrent.setHash(hash);
			// torrent.setSize(size);
			if (categories != null) {
				torrent.getCategories().addAll(categories);
			}
			// torrent.setPubDate(date);

			torrents.add(torrent);
		}
		return torrents;
	}

	public String getMagnetLink(final String url) throws ServiceException {
		Document document;
		try (final InputStream response = getResponse("https://torrentproject.se" + url);) {
			document = htmlReader.read(response);
		} catch (final DocumentException | IOException e) {
			throw new ServiceException(e);
		}
		return parseDetail(document);
	}

	@SuppressWarnings("unchecked")
	String parseDetail(final Document document) {
		System.out.println(document.asXML());
		final List<Element> linkNodes = document.selectNodes("//html:a[contains(@href,'magnet:')]");
		for (final Element element : linkNodes) {
			final String link = element.attributeValue("href");
			System.out.println(link);
			if (link.startsWith("magnet:")) {
				return link;
			}
		}
		return null;
	}
}
