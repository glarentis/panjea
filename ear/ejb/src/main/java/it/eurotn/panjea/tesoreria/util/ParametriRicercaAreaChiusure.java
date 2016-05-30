package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;

import java.io.Serializable;
import java.util.Date;

/**
 * Parametri ricerca areaChiusure (AreaEffetti e AreaPagamenti).
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
public class ParametriRicercaAreaChiusure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4996310711742896177L;
	// flag utilizzato per sapere se effettuare la ricerca o no
	/**
	 * @uml.property name="effettuaRicerca"
	 */
	private boolean effettuaRicerca = false;

	/**
	 * @uml.property name="tipoAreaPartita"
	 * @uml.associationEnd
	 */
	private TipoAreaPartita tipoAreaPartita = null;

	/**
	 * @uml.property name="daDataDocumento"
	 */
	private Date daDataDocumento = null;
	/**
	 * @uml.property name="aDataDocumento"
	 */
	private Date aDataDocumento = null;

	/**
	 * @uml.property name="daNumeroDocumento"
	 */
	private Integer daNumeroDocumento = null;
	/**
	 * @uml.property name="aNumeroDocumento"
	 */
	private Integer aNumeroDocumento = null;

	/**
	 * @uml.property name="rapportoBancarioAzienda"
	 * @uml.associationEnd
	 */
	private RapportoBancarioAzienda rapportoBancarioAzienda = null;

	/**
	 * @return the aDataDocumento
	 * @uml.property name="aDataDocumento"
	 */
	public Date getaDataDocumento() {
		return aDataDocumento;
	}

	/**
	 * @return the aNumeroDocumento
	 * @uml.property name="aNumeroDocumento"
	 */
	public Integer getaNumeroDocumento() {
		return aNumeroDocumento;
	}

	/**
	 * @return the daDataDocumento
	 * @uml.property name="daDataDocumento"
	 */
	public Date getDaDataDocumento() {
		return daDataDocumento;
	}

	/**
	 * @return the daNumeroDocumento
	 * @uml.property name="daNumeroDocumento"
	 */
	public Integer getDaNumeroDocumento() {
		return daNumeroDocumento;
	}

	/**
	 * @return the rapportoBancarioAzienda
	 * @uml.property name="rapportoBancarioAzienda"
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return the tipoAreaPartita
	 * @uml.property name="tipoAreaPartita"
	 */
	public TipoAreaPartita getTipoAreaPartita() {
		return tipoAreaPartita;
	}

	/**
	 * @return the effettuaRicerca
	 * @uml.property name="effettuaRicerca"
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param paramADataDocumento
	 *            the aDataDocumento to set
	 */
	public void setADataDocumento(Date paramADataDocumento) {
		this.aDataDocumento = paramADataDocumento;
	}

	/**
	 * @param paramANumeroDocumento
	 *            the aNumeroDocumento to set
	 */
	public void setANumeroDocumento(Integer paramANumeroDocumento) {
		this.aNumeroDocumento = paramANumeroDocumento;
	}

	/**
	 * @param daDataDocumento
	 *            the daDataDocumento to set
	 * @uml.property name="daDataDocumento"
	 */
	public void setDaDataDocumento(Date daDataDocumento) {
		this.daDataDocumento = daDataDocumento;
	}

	/**
	 * @param daNumeroDocumento
	 *            the daNumeroDocumento to set
	 * @uml.property name="daNumeroDocumento"
	 */
	public void setDaNumeroDocumento(Integer daNumeroDocumento) {
		this.daNumeroDocumento = daNumeroDocumento;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 * @uml.property name="effettuaRicerca"
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 * @uml.property name="rapportoBancarioAzienda"
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
	}

	/**
	 * @param tipoAreaPartita
	 *            the tipoAreaPartita to set
	 * @uml.property name="tipoAreaPartita"
	 */
	public void setTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		this.tipoAreaPartita = tipoAreaPartita;
	}

}
