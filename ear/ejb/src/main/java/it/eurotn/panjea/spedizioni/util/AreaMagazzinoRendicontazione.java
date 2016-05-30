package it.eurotn.panjea.spedizioni.util;

import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.spedizioni.domain.Segnacollo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;

public class AreaMagazzinoRendicontazione implements Serializable {

	private static final long serialVersionUID = -1978620746650089663L;

	private AziendaAnagraficaDTO aziendaAnagrafica;

	private AreaMagazzino areaMagazzino;

	private Vettore vettore;

	private Integer segnacolloDal;

	private Integer segnacolloAl;

	/**
	 * Costruttore.
	 * 
	 * @param azienda
	 *            azienda di riferimento
	 * @param areaMagazzino
	 *            area magazzino
	 * @param vettore
	 *            vettore
	 */
	public AreaMagazzinoRendicontazione(final AziendaAnagraficaDTO azienda, final AreaMagazzino areaMagazzino,
			final Vettore vettore) {
		super();
		this.aziendaAnagrafica = azienda;
		this.areaMagazzino = areaMagazzino;
		this.vettore = vettore;
		this.segnacolloDal = 0;
		this.segnacolloAl = 0;
		if (!areaMagazzino.getSegnacolli().isEmpty()) {
			this.segnacolloDal = areaMagazzino.getSegnacolli().iterator().next().getNumeroSegnacollo();

			Integer ultimoSegnacollo = 0;
			Iterator<Segnacollo> iterator = areaMagazzino.getSegnacolli().iterator();
			while (iterator.hasNext()) {
				ultimoSegnacollo = iterator.next().getNumeroSegnacollo();
			}

			if (!ultimoSegnacollo.equals(segnacolloDal)) {
				this.segnacolloAl = ultimoSegnacollo;
			}
		}
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
	 * Restituisce il codice bolla.
	 * 
	 * @return codice bolla
	 */
	public String getCodiceBolla() {

		boolean importoContr = areaMagazzino.getDatiSpedizioniDocumento().getImportoContrassegnoSpedizione() != null
				&& areaMagazzino.getDatiSpedizioniDocumento().getImportoContrassegnoSpedizione()
						.compareTo(BigDecimal.ZERO) != 0;

		return (importoContr) ? "4 " : "1 ";
	}

	/**
	 * Restituisce il codice del mittente assegnato dal corriere a seconda della nazionalità della sede entita sull'area
	 * magazzino.
	 * 
	 * @return codice del cliente mittente
	 */
	public String getCodiceClienteMittente() {

		Nazione nazioneAzienda = aziendaAnagrafica.getSedeAzienda().getSede().getDatiGeografici().getNazione();
		Nazione nazioneEntita = areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getNazione();

		String codiceClienteMittente = vettore.getDatiSpedizioni().getCodiceClienteMittenteItalia();

		if (!nazioneAzienda.equals(nazioneEntita)) {
			codiceClienteMittente = vettore.getDatiSpedizioni().getCodiceClienteMittenteEstero();
		}

		return codiceClienteMittente;
	}

	/**
	 * Restituisce il codice tariffa assegnato dal corriere a seconda della nazionalità della sede entita sull'area
	 * magazzino.
	 * 
	 * @return codice tariffa
	 */
	public String getCodiceTariffa() {

		Nazione nazioneAzienda = aziendaAnagrafica.getSedeAzienda().getSede().getDatiGeografici().getNazione();
		Nazione nazioneEntita = areaMagazzino.getDocumento().getSedeEntita().getSede().getDatiGeografici().getNazione();

		String codiceTariffa = vettore.getDatiSpedizioni().getCodiceTariffaItalia();

		if (!nazioneAzienda.equals(nazioneEntita)) {
			codiceTariffa = vettore.getDatiSpedizioni().getCodiceTariffaEstero();
		}

		return codiceTariffa;
	}

	/**
	 * Restituisce il codice della consegna.
	 * 
	 * @return codice consegna
	 */
	public String getConsegna() {

		return areaMagazzino.getDatiSpedizioniDocumento().getConsegna().getCodice();
	}

	/**
	 * Restituisce il codice della modalità di incasso.
	 * 
	 * @return codice modalità incasso
	 */
	public String getModalitaIncasso() {

		return areaMagazzino.getDatiSpedizioniDocumento().getModalitaIncasso().getCodice();
	}

	/**
	 * @return the segnacolloAl
	 */
	public Integer getSegnacolloAl() {
		return segnacolloAl;
	}

	/**
	 * @return the segnacolloDal
	 */
	public Integer getSegnacolloDal() {
		return segnacolloDal;
	}

	/**
	 * @return the vettore
	 */
	public Vettore getVettore() {
		return vettore;
	}

	/**
	 * Restituisce la zona di consegna. La zona è ottenuta dalla zona configurata sul primo segnacollo dell'area
	 * magazzino.
	 * 
	 * @return zona consegna
	 */
	public String getZonaConsegna() {

		String zonaConsegna = "";

		if (!areaMagazzino.getSegnacolli().isEmpty()) {
			zonaConsegna = areaMagazzino.getSegnacolli().iterator().next().getZonaSegnacollo();
		}

		return zonaConsegna;
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
	 * Metodo creato solamente perchè richiesto dal framework di esportazione.
	 * 
	 * @param codice
	 *            codice
	 */
	public void setCodiceBolla(String codice) {

	}

	/**
	 * Metodo creato solamente perchè richiesto dal framework di esportazione.
	 * 
	 * @param codice
	 *            codice
	 */
	public void setCodiceClienteMittente(String codice) {

	}

	/**
	 * Metodo creato solamente perchè richiesto dal framework di esportazione.
	 * 
	 * @param codice
	 *            codice
	 */
	public void setCodiceTariffa(String codice) {

	}

	/**
	 * Metodo creato solamente perchè richiesto dal framework di esportazione.
	 * 
	 * @param codice
	 *            codice
	 */
	public void setConsegna(String codice) {

	}

	/**
	 * Metodo creato solamente perchè richiesto dal framework di esportazione.
	 * 
	 * @param codice
	 *            codice
	 */
	public void setModalitaIncasso(String codice) {

	}

	/**
	 * @param segnacolloAl
	 *            the segnacolloAl to set
	 */
	public void setSegnacolloAl(Integer segnacolloAl) {
		this.segnacolloAl = segnacolloAl;
	}

	/**
	 * @param segnacolloDal
	 *            the segnacolloDal to set
	 */
	public void setSegnacolloDal(Integer segnacolloDal) {
		this.segnacolloDal = segnacolloDal;
	}

	/**
	 * @param vettore
	 *            the vettore to set
	 */
	public void setVettore(Vettore vettore) {
		this.vettore = vettore;
	}

	/**
	 * Metodo creato solamente perchè richiesto dal framework di esportazione.
	 * 
	 * @param codice
	 *            codice
	 */
	public void setZonaConsegna(String codice) {

	}
}
