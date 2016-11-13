package nz.co.wing.tvmanager.view;

import io.dropwizard.views.View;

public class TorrentManagerView extends View {

	public TorrentManagerView() {
		super("/torrents.ftl");
	}
}
