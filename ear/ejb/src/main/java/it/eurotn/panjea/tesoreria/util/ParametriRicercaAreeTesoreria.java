/**
 * 
 */
package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Parametri per la ricerca delle aree.
 * 
 * @author Leonardo
 */
@Entity
@Table(name = "para_ricerca_aree_tesoreria")
public class ParametriRicercaAreeTesoreria extends AbstractParametriRicerca implements Serializable {

	private static final long serialVersionUID = 1;

	/**
	 * Crea i parametri per la ricerca delle area tesoreria .
	 * 
	 * @param areaTesoreria
	 *            area tesoreria
	 * @return parametri impostati
	 */
	public static ParametriRicercaAreeTesoreria creaParametriRicercaAreeTesoreria(AreaTesoreria areaTesoreria) {
		ParametriRicercaAreeTesoreria parametriRicerca = new ParametriRicercaAreeTesoreria();
		parametriRicerca.setEffettuaRicerca(true);

		List<AreaTesoreria> aree = new ArrayList<AreaTesoreria>();
		aree.add(areaTesoreria);
		parametriRicerca.setAreeTesoreria(aree);
		return parametriRicerca;
	}

	/**
	 * Crea i parametri per la ricerca delle area tesoreria .
	 * 
	 * @param areeTesoreria
	 *            lista di area tesoreria
	 * @return parametri impostati
	 */
	public static ParametriRicercaAreeTesoreria creaParametriRicercaAreeTesoreria(List<AreaTesoreria> areeTesoreria) {
		ParametriRicercaAreeTesoreria parametriRicerca = new ParametriRicercaAreeTesoreria();
		parametriRicerca.setEffettuaRicerca(true);

		parametriRicerca.setAreeTesoreria(areeTesoreria);
		return parametriRicerca;
	}

	@ManyToOne(optional = true)
	private TipoAreaPartita tipoAreaPartita;

	private Periodo periodo;

	private Integer daNumeroDocumento;

	private Integer aNumeroDocumento;

	@ManyToOne(optional = true)
	private RapportoBancarioAzienda rapportoBancarioAzienda;

	@Transient
	private List<AreaTesoreria> areeTesoreria = null;

	private boolean escludiTipiAreaPartiteDistinta;

	{
		if (areeTesoreria == null) {
			areeTesoreria = new ArrayList<AreaTesoreria>();
		}
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAreeTesoreria() {
	}

	/**
	 * @return the aNumeroDocumento
	 */
	public Integer getANumeroDocumento() {
		return aNumeroDocumento;
	}

	/**
	 * @return the areeTesoreria
	 */
	public List<AreaTesoreria> getAreeTesoreria() {
		return areeTesoreria;
	}

	/**
	 * @return the daNumeroDocumento
	 */
	public Integer getDaNumeroDocumento() {
		return daNumeroDocumento;
	}

	public Periodo getPeriodo() {
		if (periodo == null) {
			periodo = new Periodo();
		}
		return periodo;
	}

	/**
	 * @return the rapportoBancarioAzienda
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return the tipoAreaPartita
	 */
	public TipoAreaPartita getTipoAreaPartita() {
		return tipoAreaPartita;
	}

	/**
	 * @return the escludiTipiAreaPartiteDistinta
	 */
	public boolean isEscludiTipiAreaPartiteDistinta() {
		return escludiTipiAreaPartiteDistinta;
	}

	/**
	 * @param numeroDocumento
	 *            the aNumeroDocumento to set
	 */
	public void setANumeroDocumento(Integer numeroDocumento) {
		aNumeroDocumento = numeroDocumento;
	}

	/**
	 * @param areeTesoreria
	 *            the areeTesoreria to set
	 */
	public void setAreeTesoreria(List<AreaTesoreria> areeTesoreria) {
		this.areeTesoreria = areeTesoreria;
	}

	/**
	 * @param daNumeroDocumento
	 *            the daNumeroDocumento to set
	 */
	public void setDaNumeroDocumento(Integer daNumeroDocumento) {
		this.daNumeroDocumento = daNumeroDocumento;
	}

	/**
	 * @param escludiTipiAreaPartiteDistinta
	 *            the escludiTipiAreaPartiteDistinta to set
	 */
	public void setEscludiTipiAreaPartiteDistinta(boolean escludiTipiAreaPartiteDistinta) {
		this.escludiTipiAreaPartiteDistinta = escludiTipiAreaPartiteDistinta;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
	}

	/**
	 * @param tipoAreaPartita
	 *            the tipoAreaPartita to set
	 */
	public void setTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		this.tipoAreaPartita = tipoAreaPartita;
	}

}
