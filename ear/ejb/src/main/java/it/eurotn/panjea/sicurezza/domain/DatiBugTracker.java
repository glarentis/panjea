/**
 *
 */
package it.eurotn.panjea.sicurezza.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author leonardo
 */
@Embeddable
public class DatiBugTracker implements Serializable, Cloneable {

	public static final String PROP_PASSWORD = "passwordMail";
	public static final String PROP_EMAIL = "email";

	private static final long serialVersionUID = 1554225696011611298L;

	@Column(name = "emailBugTracker", length = 60)
	private String username;

	@Column(name = "passwordBugTracker", length = 60)
	private String password;

	/**
	 * Constructor.
	 */
	public DatiBugTracker() {
		super();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
	 *
	 * @return toString
	 */
	@Override
	public String toString() {
		final String spazio = " ";
		StringBuffer retValue = new StringBuffer();
		retValue.append("DatiBugTracker[ ").append("username=").append(this.username).append(spazio)
		.append("password=").append(this.password).append("]");
		return retValue.toString();
	}
}
