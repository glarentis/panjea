/**
 *
 */
package it.eurotn.panjea.sicurezza.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author gianluca
 */
@Embeddable
public class DatiJasperServer implements Serializable, Cloneable {

	private static final long serialVersionUID = 1554225696011611298L;

	@Column(name = "usernameJasperServer", length = 60)
	private String username;

	@Column(name = "passwordJasperServer", length = 60)
	private String password;

	@Column(name = "urlJasperServer", length = 50)
	private String url;

	/**
	 * Constructor.
	 */
	public DatiJasperServer() {
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
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
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
	 * @param url
	 *            The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
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
		retValue.append("DatiJasperServer[ ").append("username=").append(this.username).append(spazio)
		.append("password=").append(this.password).append("]");
		return retValue.toString();
	}
}
