package nz.co.wing.tvmanager.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nz.co.wing.tvmanager.model.Torrent;

import com.janoz.torrentapi.SearchParamsBuilder;
import com.janoz.torrentapi.SearchParamsBuilder.Sort;
import com.janoz.torrentapi.TorrentApi;
import com.janoz.torrentapi.impl.TorrentApiImpl;
import com.janoz.torrentlib.model.Magnet;

public class TorrentApiService {
	public List<Torrent> findTorrents(final String string) throws ServiceException {
		final List<Torrent> torrents = new ArrayList<>();
		final TorrentApi api = new TorrentApiImpl();
		final SearchParamsBuilder params = new SearchParamsBuilder();
		params.query(string);
		params.sort(Sort.SEEDERS);
		try {
			final List<Magnet> list = api.queryApi(params);
			System.out.println(list);
			list.forEach(m -> {
				final Torrent torrent = new Torrent();
				torrent.setTitle(m.getDisplayName());
				torrent.setHash(m.getExactTopic().replace("urn:btih:", ""));
				try {
					torrent.setUrl(m.toUrl());
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				torrents.add(torrent);
			});
		} catch (final IOException e) {
			throw new ServiceException(e);
		}
		return torrents;
	}
}
