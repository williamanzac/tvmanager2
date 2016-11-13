package nz.co.wing.tvmanager.view;

import java.util.Collections;
import java.util.List;

import nz.co.wing.tvmanager.model.Torrent;
import nz.co.wing.tvmanager.model.TorrentState;
import io.dropwizard.views.View;

public class TorrentListView extends View {
	final List<Torrent> list;

	public TorrentListView(final List<Torrent> list) {
		super("/torrentList.ftl");
		this.list = list;
		Collections.sort(list, (o1, o2) -> {
			final boolean downloading1 = o1.getState() == TorrentState.DOWNLOADING;
			final boolean downloading2 = o2.getState() == TorrentState.DOWNLOADING;
			if (downloading1 && !downloading2) {
				return -1;
			}
			if (!downloading1 && downloading2) {
				return 1;
			}

			final boolean queued1 = o1.getState() == TorrentState.DOWNLOADING_QUEUED;
			final boolean queued2 = o2.getState() == TorrentState.DOWNLOADING_QUEUED;
			if (queued1 && !queued2) {
				return -1;
			}
			if (!queued1 && queued2) {
				return 1;
			}

			return Float.compare(o2.getPercentComplete(), o1.getPercentComplete());
		});
	}

	public List<Torrent> getList() {
		return list;
	}
}
