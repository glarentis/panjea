package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AreaOrdineFullDTOStampa extends AreaOrdineFullDTO {

	private static final long serialVersionUID = 667356456661079301L;

	private String vettoreDenominazione = null;
	private String vettoreCap = null;
	private String vettoreIndirizzo = null;
	private String vettoreLocalita = null;
	private String vettoreProvincia = null;
	private String vettorePIVA = null;
	private String vettoreNRIscrizioneAlbo = null;
	private boolean vettorePresente = false;
	private Date dataConsegna = null;

	private RapportoBancarioSedeEntita rapportoBancarioSedeEntita;

	private SedeAzienda sedeAzienda;

	/**
	 * Costruttore.
	 *
	 * @param areaOrdine
	 *            area ordine
	 * @param areaRate
	 *            area rate
	 * @param righeOrdine
	 *            righe ordine
	 * @param sedeAzienda
	 *            sede azienda
	 */
	public AreaOrdineFullDTOStampa(final AreaOrdine areaOrdine, final AreaRate areaRate,
			final List<RigaOrdine> righeOrdine, final SedeAzienda sedeAzienda) {
		super();
		setSedeAzienda(sedeAzienda);
		setAreaOrdine(areaOrdine);
		setAreaRateEnabled(areaRate.getId() != null);
		setAreaRate(areaRate);
		setRigheOrdine(righeOrdine);
		initDatiStampa();
	}

	/**
	 * @return Returns the dataConsegna.
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return the rapportoBancarioSedeEntita
	 */
	public RapportoBancarioSedeEntita getRapportoBancarioSedeEntita() {
		return rapportoBancarioSedeEntita;
	}

	/**
	 * @return the sedeAzienda
	 */
	public SedeAzienda getSedeAzienda() {
		return sedeAzienda;
	}

	/**
	 * @return Returns the vettoreCap.
	 */
	public String getVettoreCap() {
		return vettoreCap;
	}

	/**
	 * @return Returns the vettoreDenominazione.
	 */
	public String getVettoreDenominazione() {
		return vettoreDenominazione;
	}

	/**
	 * @return Returns the vettoreIndirizzo.
	 */
	public String getVettoreIndirizzo() {
		return vettoreIndirizzo;
	}

	/**
	 * @return Returns the vettoreLocalita.
	 */
	public String getVettoreLocalita() {
		return vettoreLocalita;
	}

	/**
	 * @return Returns the vettoreNRIscrizioneAlbo.
	 */
	public String getVettoreNRIscrizioneAlbo() {
		return vettoreNRIscrizioneAlbo;
	}

	/**
	 * @return Returns the vettorePIVA.
	 */
	public String getVettorePIVA() {
		return vettorePIVA;
	}

	/**
	 * @return Returns the vettoreProvincia.
	 */
	public String getVettoreProvincia() {
		return vettoreProvincia;
	}

	/**
	 * Assegno a dataConsegna la prima dataConsegna che trovo nelle righe.
	 */
	private void initDataConsegna() {

		Iterator<RigaOrdine> iterator = getRigheOrdine().iterator();
		while (dataConsegna == null && iterator.hasNext()) {
			RigaOrdine riga = iterator.next();

			if (riga instanceof RigaArticolo) {
				dataConsegna = ((RigaArticolo) riga).getDataConsegna();
			}
		}
	}

	/**
	 * Inizializza i dati per la stampa.
	 *
	 * @param trasferimento
	 */
	private void initDatiStampa() {
		initVettore();
		initDataConsegna();
	}

	/**
	 * Inizializza tutti i dati del vettore.
	 */
	private void initVettore() {
		vettorePresente = false;
		vettoreDenominazione = "";
		vettoreIndirizzo = "";
		vettoreCap = "";
		vettoreLocalita = "";
		vettoreProvincia = "";
		vettorePIVA = "";
		vettoreNRIscrizioneAlbo = "";

		if (getAreaOrdine().getVettore() != null && getAreaOrdine().getVettore().getId() != null) {
			vettorePresente = true;
			VettoreLite vettore = getAreaOrdine().getVettore();
			SedeEntita sedeVettore = getAreaOrdine().getSedeVettore();
			vettoreDenominazione = vettore.getAnagrafica().getDenominazione();
			if (sedeVettore != null && sedeVettore.getId() != null) {
				vettoreIndirizzo = sedeVettore.getSede().getIndirizzo();
				vettoreCap = sedeVettore.getSede().getDatiGeografici().getDescrizioneCap();
				vettoreLocalita = sedeVettore.getSede().getDatiGeografici().getDescrizioneLocalita();
				vettoreProvincia = sedeVettore.getSede().getDatiGeografici().getSiglaProvincia();
			}
			vettorePIVA = vettore.getAnagrafica().getPartiteIVA();
			vettoreNRIscrizioneAlbo = vettore.getNumeroIscrizioneAlbo();
		}
	}

	/**
	 * @return Returns the vettorePresente.
	 */
	public boolean isVettorePresente() {
		return vettorePresente;
	}

	/**
	 * @param rapportoBancarioSedeEntita
	 *            the rapportoBancarioSedeEntita to set
	 */
	public void setRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancarioSedeEntita) {
		this.rapportoBancarioSedeEntita = rapportoBancarioSedeEntita;
	}

	/**
	 * @param sedeAzienda
	 *            the sedeAzienda to set
	 */
	public void setSedeAzienda(SedeAzienda sedeAzienda) {
		this.sedeAzienda = sedeAzienda;
	}

}
