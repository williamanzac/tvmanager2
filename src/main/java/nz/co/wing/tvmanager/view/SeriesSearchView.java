package nz.co.wing.tvmanager.view;

import java.util.List;

import nz.co.wing.tvmanager.model.Series;
import io.dropwizard.views.View;

public class SeriesSearchView extends View {
	private final List<Series> data;

	public SeriesSearchView(final List<Series> data) {
		super("/seriesSearch.ftl");
		this.data = data;
	}

	public List<Series> getData() {
		return data;
	}
}
