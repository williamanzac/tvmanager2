package nz.co.wing.tvmanager.view;

import java.util.List;

import nz.co.wing.tvmanager.model.TorrentFile;
import io.dropwizard.views.View;

public class TorrentFilesView extends View {
	private final List<TorrentFile> files;

	public TorrentFilesView(final List<TorrentFile> files) {
		super("/torrentFiles.ftl");
		this.files = files;
	}

	public List<TorrentFile> getFiles() {
		return files;
	}
}
