package sample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SAMPLE_BOARD")
public @Getter @Setter class SampleBoard extends AbstractPersistable<Long> {
	@Column(length=100, nullable=false) private String subject;
	@Column private String content;
	@Column(length=50, nullable=false) private String userName;
	@Column(length=1) private String useYn = "Y";

	public SampleBoard() {
		this(null);
	}

	/**
	 * create new instance
	 * @param object
	 */
	public SampleBoard(Long id) {
		this.setId(id);
	}
	
	public SampleBoard(Long id, String subject, String content, String userName) {
		this.setId(id);
		this.subject = subject;
		this.content = content;
		this.userName = userName;
	}
	
	public static interface SB {
		Long getId();
		String getSubject();
		String getContent();
		String userName();
	}
}
