package nz.co.wing.tvmanager.view;

import io.dropwizard.views.View;

import java.util.List;

import nz.co.wing.tvmanager.model.Torrent;

public class TorrentSearchView extends View {
	private final List<Torrent> data;

	public TorrentSearchView(final List<Torrent> data) {
		super("/torrentSearch.ftl");
		this.data = data;
	}

	public List<Torrent> getData() {
		return data;
	}
}
