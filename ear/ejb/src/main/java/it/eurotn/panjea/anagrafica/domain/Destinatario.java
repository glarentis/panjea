package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Destinatario implements Comparable<Destinatario>, Serializable {

	private static final long serialVersionUID = 4090717448102125202L;

	@Column(length = 60)
	private String nome;

	@Column(name = "email", length = 60)
	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entita_id")
	private EntitaLite entita;

	@Override
	public int compareTo(Destinatario o) {
		return this.getEmail().compareTo(o.getEmail());
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

}
