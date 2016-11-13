package nz.co.wing.tvmanager.view;

import io.dropwizard.views.View;

import nz.co.wing.tvmanager.model.Episode;

public class EpisodeView extends View {
	final Episode episode;

	public EpisodeView(final Episode episode) {
		super("/episode.ftl");
		this.episode = episode;
	}

	public Episode getEpisode() {
		return episode;
	}
}
