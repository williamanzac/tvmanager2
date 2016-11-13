package nz.co.wing.tvmanager.view;

import io.dropwizard.views.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.co.wing.tvmanager.model.Episode;
import nz.co.wing.tvmanager.model.Torrent;

public class EpisodeListView extends View {
	final List<Episode> episodes;
	final List<Torrent> torrents;
	final Map<String, Float> torrentMap = new HashMap<>();

	public EpisodeListView(final List<Episode> episodes, final List<Torrent> torrents) {
		super("/episodeList.ftl");
		this.episodes = episodes;
		this.torrents = torrents;
		torrents.forEach(t -> {
			torrentMap.put(t.getHash(), t.getPercentComplete());
		});
		Collections.sort(episodes, (o1, o2) -> {
			final int s1 = o1.getSeasonNumber();
			final int s2 = o2.getSeasonNumber();
			final int e1 = o1.getEpisodeNumber();
			final int e2 = o2.getEpisodeNumber();
			if (s1 > s2) {
				return -1;
			}
			if (s2 > s1) {
				return 1;
			}
			if (e1 > e2) {
				return -1;
			}
			if (e2 > e1) {
				return 1;
			}
			return 0;
		});
	}

	public List<Episode> getEpisodes() {
		return episodes;
	}

	public List<Torrent> getTorrents() {
		return torrents;
	}

	public Map<String, Float> getTorrentMap() {
		return torrentMap;
	}
}
