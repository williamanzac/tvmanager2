package nz.co.wing.tvmanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "filetask")
public class FileTask {
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String source;
	@Column
	private String target;
	@Column
	private long size;
	@Column
	private float percentComplete;
	@Column
	private FileTaskType type;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public long getSize() {
		return size;
	}

	public void setSize(final long size) {
		this.size = size;
	}

	public float getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(final float percentComplete) {
		this.percentComplete = percentComplete;
	}

	public FileTaskType getType() {
		return type;
	}

	public void setType(final FileTaskType type) {
		this.type = type;
	}
}
