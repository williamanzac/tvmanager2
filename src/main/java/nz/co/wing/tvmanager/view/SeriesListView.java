package nz.co.wing.tvmanager.view;

import java.util.List;

import nz.co.wing.tvmanager.model.Series;
import io.dropwizard.views.View;

public class SeriesListView extends View {
	final List<Series> list;

	public SeriesListView(final List<Series> list) {
		super("/seriesList.ftl");
		this.list = list;
	}

	public List<Series> getList() {
		return list;
	}
}
