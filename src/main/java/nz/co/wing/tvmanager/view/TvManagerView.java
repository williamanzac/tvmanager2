package nz.co.wing.tvmanager.view;

import io.dropwizard.views.View;

public class TvManagerView extends View {

	public TvManagerView() {
		super("/manager.ftl");
	}
}
