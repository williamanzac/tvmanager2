package nz.co.wing.tvmanager.view;

import io.dropwizard.views.View;
import nz.co.wing.tvmanager.model.Banners;

public class BannersView extends View {
	final Banners banners;

	public BannersView(final Banners banners) {
		super("/banners.ftl");
		this.banners = banners;
	}

	public Banners getBanners() {
		return banners;
	}
}
