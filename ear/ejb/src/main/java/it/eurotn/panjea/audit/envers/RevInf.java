package it.eurotn.panjea.audit.envers;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

/**
 * @author angelo
 */
@Entity
@RevisionEntity(UserDateListener.class)
@Table(name = "revinfo")
public class RevInf extends DefaultRevisionEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property name="username"
	 */
	private String username;

	/**
	 * @return username
	 * @uml.property name="username"
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 * @uml.property name="username"
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
