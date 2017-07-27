package com.eshore.yxt.media.model.base;

/**
 * 使用SeqMolde必须在所继承的实体类上声明生成策略注解
 * @SequenceGenerator(name="SEQ",sequenceName="SEQ_Table")  
 */
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class SeqModel {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SEQ")
	private long id;

	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeqModel other = (SeqModel) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setId(long id) {
		this.id = id;
	}

	
}
