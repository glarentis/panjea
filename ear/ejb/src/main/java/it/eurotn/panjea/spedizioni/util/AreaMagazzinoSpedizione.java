package it.eurotn.panjea.spedizioni.util;

import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.io.Serializable;

public class AreaMagazzinoSpedizione implements Serializable {

	private static final long serialVersionUID = -3810538569825753169L;

	private AreaMagazzino areaMagazzino;

	private Vettore vettore;

	/**
	 * Numero del segnacollo all'interno della spedizione. ( es: 2 di 3 )
	 */
	private Integer numeroSegnacolloDi;
	private Integer numeroSegnacollo;

	private AziendaAnagraficaDTO aziendaAnagrafica;

	// ------------------------------------ DATI ERRORI DI GENERAZIONE ------------------------------------

	private String messaggioErrore;

	private String descrizionePuntoOperativoPartenza;

	private String terminalArrivo;

	private String puntoOperativoArrivoCodice;

	private String puntoOperativoArrivoDescrizione;

	private String zonaSegnacollo;

	/**
	 * Costruttore.
	 * 
	 */
	public AreaMagazzinoSpedizione() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param areaMagazzino
	 *            area magazzino
	 * @param vettore
	 *            vettore
	 * @param numeroSegnacollo
	 *            numero del segnacollo
	 * @param numeroSegnacolloDi
	 *            numero del segnacollo all'interno della spedizione. ( es: 2 di 3 )
	 * @param azienda
	 *            azienda
	 */
	public AreaMagazzinoSpedizione(final AreaMagazzino areaMagazzino, final Vettore vettore,
			final Integer numeroSegnacollo, final Integer numeroSegnacolloDi, final AziendaAnagraficaDTO azienda) {
		super();
		this.areaMagazzino = areaMagazzino;
		this.vettore = vettore;
		this.numeroSegnacollo = numeroSegnacollo;
		this.numeroSegnacolloDi = numeroSegnacolloDi;
		this.aziendaAnagrafica = azienda;

		this.messaggioErrore = "";
		this.descrizionePuntoOperativoPartenza = "";
		this.terminalArrivo = "000";
		this.puntoOperativoArrivoCodice = "000";
		this.puntoOperativoArrivoDescrizione = "";
		this.zonaSegnacollo = "00";
	}

	/**
	 * @return the areaMagazzino
	 */
	public AreaMagazzino getAreaMagazzino() {
		return areaMagazzino;
	}

	/**
	 * @return the aziendaAnagrafica
	 */
	public AziendaAnagraficaDTO getAziendaAnagrafica() {
		return aziendaAnagrafica;
	}

	/**
	 * @return the descrizionePuntoOperativoPartenza
	 */
	public String getDescrizionePuntoOperativoPartenza() {
		return descrizionePuntoOperativoPartenza;
	}

	/**
	 * @return the messaggioErrore
	 */
	public String getMessaggioErrore() {
		return messaggioErrore;
	}

	/**
	 * @return the numeroSegnacollo
	 */
	public Integer getNumeroSegnacollo() {
		return numeroSegnacollo;
	}

	/**
	 * @return the numeroSegnacolloDi
	 */
	public Integer getNumeroSegnacolloDi() {
		return numeroSegnacolloDi;
	}

	/**
	 * @return the puntoOperativoArrivoCodice
	 */
	public String getPuntoOperativoArrivoCodice() {
		return puntoOperativoArrivoCodice;
	}

	/**
	 * @return the puntoOperativoArrivoDescrizione
	 */
	public String getPuntoOperativoArrivoDescrizione() {
		return puntoOperativoArrivoDescrizione;
	}

	/**
	 * @return the terminalArrivo
	 */
	public String getTerminalArrivo() {
		return terminalArrivo;
	}

	/**
	 * @return the vettore
	 */
	public Vettore getVettore() {
		return vettore;
	}

	/**
	 * @return the zonaSegnacollo
	 */
	public String getZonaSegnacollo() {
		return zonaSegnacollo;
	}

	/**
	 * @param areaMagazzino
	 *            the areaMagazzino to set
	 */
	public void setAreaMagazzino(AreaMagazzino areaMagazzino) {
		this.areaMagazzino = areaMagazzino;
	}

	/**
	 * @param aziendaAnagrafica
	 *            the aziendaAnagrafica to set
	 */
	public void setAziendaAnagrafica(AziendaAnagraficaDTO aziendaAnagrafica) {
		this.aziendaAnagrafica = aziendaAnagrafica;
	}

	/**
	 * @param descrizionePuntoOperativoPartenza
	 *            the descrizionePuntoOperativoPartenza to set
	 */
	public void setDescrizionePuntoOperativoPartenza(String descrizionePuntoOperativoPartenza) {
		this.descrizionePuntoOperativoPartenza = descrizionePuntoOperativoPartenza;
	}

	/**
	 * @param messaggioErrore
	 *            the messaggioErrore to set
	 */
	public void setMessaggioErrore(String messaggioErrore) {
		this.messaggioErrore = messaggioErrore;
	}

	/**
	 * @param numeroSegnacollo
	 *            the numeroSegnacollo to set
	 */
	public void setNumeroSegnacollo(Integer numeroSegnacollo) {
		this.numeroSegnacollo = numeroSegnacollo;
	}

	/**
	 * @param numeroSegnacolloDi
	 *            the numeroSegnacolloDi to set
	 */
	public void setNumeroSegnacolloDi(Integer numeroSegnacolloDi) {
		this.numeroSegnacolloDi = numeroSegnacolloDi;
	}

	/**
	 * @param puntoOperativoArrivoCodice
	 *            the puntoOperativoArrivoCodice to set
	 */
	public void setPuntoOperativoArrivoCodice(String puntoOperativoArrivoCodice) {
		this.puntoOperativoArrivoCodice = puntoOperativoArrivoCodice;
	}

	/**
	 * @param puntoOperativoArrivoDescrizione
	 *            the puntoOperativoArrivoDescrizione to set
	 */
	public void setPuntoOperativoArrivoDescrizione(String puntoOperativoArrivoDescrizione) {
		this.puntoOperativoArrivoDescrizione = puntoOperativoArrivoDescrizione;
	}

	/**
	 * @param terminalArrivo
	 *            the terminalArrivo to set
	 */
	public void setTerminalArrivo(String terminalArrivo) {
		this.terminalArrivo = terminalArrivo;
	}

	/**
	 * @param vettore
	 *            the vettore to set
	 */
	public void setVettore(Vettore vettore) {
		this.vettore = vettore;
	}

	/**
	 * @param zonaSegnacollo
	 *            the zonaSegnacollo to set
	 */
	public void setZonaSegnacollo(String zonaSegnacollo) {
		this.zonaSegnacollo = zonaSegnacollo;
	}
}
