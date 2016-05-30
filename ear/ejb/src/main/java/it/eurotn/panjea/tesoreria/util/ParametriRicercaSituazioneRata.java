package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ParametriRicercaSituazioneRata implements Serializable {

	private static final long serialVersionUID = 6497314584741907370L;

	private Date dataIniziale;
	private Date dataFinale;
	private TipoPartita tipoPartita;
	private CategoriaEntita categoriaEntita;
	// proprietà aggiunta solamente per poter nella search text selezionare i clienti o fornitor
	private TipoEntita tipoEntita;
	private EntitaLite entitaLite;
	private boolean stampaDettaglio;
	private String reportName;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaSituazioneRata() {
		super();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -2);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);

		this.setDataIniziale(calendar.getTime());

		calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, 11);
		this.setDataFinale(calendar.getTime());

		this.tipoPartita = TipoPartita.ATTIVA;

		this.tipoEntita = TipoEntita.CLIENTE;
		this.entitaLite = new FornitoreLite();

		this.stampaDettaglio = true;
		this.reportName = "Dettaglio";

		this.categoriaEntita = new CategoriaEntita();
	}

	/**
	 * @return Returns the categoriaEntita.
	 */
	public CategoriaEntita getCategoriaEntita() {
		return categoriaEntita;
	}

	/**
	 * @return the dataFinale
	 * @uml.property name="dataFinale"
	 */
	public Date getDataFinale() {
		return dataFinale;
	}

	/**
	 * @return the dataIniziale
	 * @uml.property name="dataIniziale"
	 */
	public Date getDataIniziale() {
		return dataIniziale;
	}

	/**
	 * @return the entitaLite
	 * @uml.property name="entitaLite"
	 */
	public EntitaLite getEntitaLite() {
		return entitaLite;
	}

	/**
	 * @return Returns the reportName.
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @return the tipoEntita
	 * @uml.property name="tipoEntita"
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * @return the tipoPartita
	 * @uml.property name="tipoPartita"
	 */
	public TipoPartita getTipoPartita() {
		return tipoPartita;
	}

	/**
	 * @return the stampaDettaglio
	 * @uml.property name="stampaDettaglio"
	 */
	public boolean isStampaDettaglio() {
		return stampaDettaglio;
	}

	/**
	 * @param categoriaEntita
	 *            The categoriaEntita to set.
	 */
	public void setCategoriaEntita(CategoriaEntita categoriaEntita) {
		this.categoriaEntita = categoriaEntita;
	}

	/**
	 * @param dataFinale
	 *            the dataFinale to set
	 * @uml.property name="dataFinale"
	 */
	public void setDataFinale(Date dataFinale) {
		this.dataFinale = dataFinale;
	}

	/**
	 * @param dataIniziale
	 *            the dataIniziale to set
	 * @uml.property name="dataIniziale"
	 */
	public void setDataIniziale(Date dataIniziale) {
		this.dataIniziale = dataIniziale;
	}

	/**
	 * @param entitaLite
	 *            the entitaLite to set
	 * @uml.property name="entitaLite"
	 */
	public void setEntitaLite(EntitaLite entitaLite) {
		this.entitaLite = entitaLite;
	}

	/**
	 * @param reportName
	 *            The reportName to set.
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @param stampaDettaglio
	 *            the stampaDettaglio to set
	 * @uml.property name="stampaDettaglio"
	 */
	public void setStampaDettaglio(boolean stampaDettaglio) {
		this.stampaDettaglio = stampaDettaglio;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 * @uml.property name="tipoEntita"
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.tipoEntita = tipoEntita;
	}

	/**
	 * @param tipoPartita
	 *            the tipoPartita to set
	 * @uml.property name="tipoPartita"
	 */
	public void setTipoPartita(TipoPartita tipoPartita) {
		this.tipoPartita = tipoPartita;
	}

}
