package nz.co.wing.tvmanager.view;

import io.dropwizard.views.View;

import java.util.List;

import nz.co.wing.tvmanager.model.FileTask;

public class FileTasksView extends View {
	private final List<FileTask> data;

	public FileTasksView(final List<FileTask> data) {
		super("/filetasks.ftl");
		this.data = data;
	}

	public List<FileTask> getData() {
		return data;
	}
}
