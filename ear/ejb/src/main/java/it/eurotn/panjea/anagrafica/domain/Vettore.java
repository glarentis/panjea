package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.spedizioni.domain.DatiSpedizioni;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@DiscriminatorValue("V")
public class Vettore extends Entita {

	private static final long serialVersionUID = 7294214322778816932L;

	/**
	 * @uml.property name="ordine"
	 */
	private static int ordine = 3;

	/**
	 * @uml.property name="numeroCopiePerStampa"
	 */
	private Integer numeroCopiePerStampa;

	/**
	 * @uml.property name="numeroIscrizioneAlbo"
	 */
	@Column(length = 20)
	private String numeroIscrizioneAlbo;

	@Embedded
	private DatiSpedizioni datiSpedizioni;

	/**
	 * Costruttore.
	 *
	 */
	public Vettore() {
		super();
		datiSpedizioni = new DatiSpedizioni();
		numeroCopiePerStampa = 1;
	}

	/**
	 * @return the datiSpedizioni
	 */
	public DatiSpedizioni getDatiSpedizioni() {
		if (datiSpedizioni == null) {
			datiSpedizioni = new DatiSpedizioni();
		}
		return datiSpedizioni;
	}

	/**
	 * @return the numeroCopiePerStampa
	 * @uml.property name="numeroCopiePerStampa"
	 */
	public Integer getNumeroCopiePerStampa() {
		return numeroCopiePerStampa;
	}

	/**
	 * @return the numeroIscrizioneAlbo
	 * @uml.property name="numeroIscrizioneAlbo"
	 */
	public String getNumeroIscrizioneAlbo() {
		return numeroIscrizioneAlbo;
	}

	/**
	 * @return ordine
	 * @uml.property name="ordine"
	 */
	@Override
	public int getOrdine() {
		return ordine;
	}

	@Override
	public TipoEntita getTipo() {
		return TipoEntita.VETTORE;
	}

	/**
	 * @param datiSpedizioni
	 *            the datiSpedizioni to set
	 */
	public void setDatiSpedizioni(DatiSpedizioni datiSpedizioni) {
		this.datiSpedizioni = datiSpedizioni;
	}

	/**
	 * @param numeroCopiePerStampa
	 *            the numeroCopiePerStampa to set
	 * @uml.property name="numeroCopiePerStampa"
	 */
	public void setNumeroCopiePerStampa(Integer numeroCopiePerStampa) {
		this.numeroCopiePerStampa = numeroCopiePerStampa;
	}

	/**
	 * @param numeroIscrizioneAlbo
	 *            the numeroIscrizioneAlbo to set
	 * @uml.property name="numeroIscrizioneAlbo"
	 */
	public void setNumeroIscrizioneAlbo(String numeroIscrizioneAlbo) {
		this.numeroIscrizioneAlbo = numeroIscrizioneAlbo;
	}
}
