package sample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SAMPLE")
@NamedQueries({
	@NamedQuery(name = "Sample.findByName", query = "from Sample u where u.name = ?1"),
	@NamedQuery(name = "Sample.findAllByName", query = "from Sample u where u.name like ?1"),
	@NamedQuery(name = "Sample.list", query = "from Sample u where u.name like ?1"),
})

public @Getter @Setter class Sample extends AbstractPersistable<Long> {
	@Column(unique = true) private String name;
	
	public Sample() {
		this(null);
	}

	/**
	 * create new instance
	 * @param object
	 */
	public Sample(Long id) {
		this.setId(id);
	}
}
