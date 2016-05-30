package it.eurotn.panjea.tesoreria.util.parametriricerca;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParametriRicercaAcconti implements Serializable {

	public enum EStatoAcconto {
		APERTO, CHIUSO, TUTTI
	}

	private static final long serialVersionUID = -449280078735808658L;

	/**
	 * Crea i parametri per la ricerca degli acconti.
	 * 
	 * @param areaAcconto
	 *            area acconto
	 * @return parametri impostati
	 */
	public static ParametriRicercaAcconti creaParametriRicercaAcconti(AreaAcconto areaAcconto) {
		ParametriRicercaAcconti parametriRicerca = new ParametriRicercaAcconti();
		parametriRicerca.setEffettuaRicerca(true);

		List<AreaAcconto> aree = new ArrayList<AreaAcconto>();
		aree.add(areaAcconto);
		parametriRicerca.setAreeAcconti(aree);
		return parametriRicerca;
	}

	/**
	 * Crea i parametri per la ricerca degli acconti.
	 * 
	 * @param areeAcconto
	 *            lista di area acconto
	 * @return parametri impostati
	 */
	public static ParametriRicercaAcconti creaParametriRicercaAcconti(List<AreaAcconto> areeAcconto) {
		ParametriRicercaAcconti parametriRicerca = new ParametriRicercaAcconti();
		parametriRicerca.setEffettuaRicerca(true);

		parametriRicerca.setAreeAcconti(areeAcconto);
		return parametriRicerca;
	}

	private EStatoAcconto statoAcconto;

	private TipoEntita tipoEntita;
	private EntitaLite entita;

	private boolean effettuaRicerca;

	private List<AreaAcconto> areeAcconti;

	private Date daDataDocumento = null;
	private Date aDataDocumento = null;

	private String codiceValuta;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAcconti() {
		super();
		this.statoAcconto = EStatoAcconto.APERTO;
		this.effettuaRicerca = false;
		this.tipoEntita = TipoEntita.CLIENTE;
		this.entita = null;
		this.areeAcconti = null;
		this.codiceValuta = null;
	}

	/**
	 * @return the aDataDocumento
	 */
	public Date getADataDocumento() {
		return aDataDocumento;
	}

	/**
	 * @return the areeAcconti
	 */
	public List<AreaAcconto> getAreeAcconti() {
		return areeAcconti;
	}

	/**
	 * @return the codiceValuta
	 */
	public String getCodiceValuta() {
		return codiceValuta;
	}

	/**
	 * @return the daDataDocumento
	 */
	public Date getDaDataDocumento() {
		return daDataDocumento;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the statoAcconto
	 */
	public EStatoAcconto getStatoAcconto() {
		return statoAcconto;
	}

	/**
	 * @return the tipoEntita
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * @return the effettuaRicerca
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
	 * @param areeAcconti
	 *            the areeAcconti to set
	 */
	public void setAreeAcconti(List<AreaAcconto> areeAcconti) {
		this.areeAcconti = areeAcconti;
	}

	/**
	 * @param codiceValuta
	 *            the codiceValuta to set
	 */
	public void setCodiceValuta(String codiceValuta) {
		this.codiceValuta = codiceValuta;
	}

	/**
	 * @param daDataDocumento
	 *            the daDataDocumento to set
	 */
	public void setDaDataDocumento(Date daDataDocumento) {
		this.daDataDocumento = daDataDocumento;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param statoAcconto
	 *            the statoAcconto to set
	 */
	public void setStatoAcconto(EStatoAcconto statoAcconto) {
		this.statoAcconto = statoAcconto;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.tipoEntita = tipoEntita;
	}
}
