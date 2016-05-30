package it.eurotn.panjea.spedizioni.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class DatiSpedizioni implements Serializable {

	public enum TIPOINVIO {
		NESSUNO, FTP
	}

	private static final long serialVersionUID = -5644568844777091562L;

	private String tipoStampante;

	private String puntoOperativoPartenzaMerce;

	private String tipoServizioBolle;

	private String codiceClienteMittenteItalia;

	private String codiceClienteMittenteEstero;

	private String codiceTariffaItalia;

	private String codiceTariffaEstero;

	private String naturaMerceMittente;
	private String etichettePath;
	private String nameFileEtichetteToExport;

	private String nameFileEtichetteToImport;

	private String pathFileTemplateEtichette;
	private String pathFileRendiconto;

	private String pathFileTemplateRendiconto;

	private String numeratore;

	private String numeroSerie;

	private String puntoOperativoArrivo;

	private String codiceTrattamentoMerce;

	private TIPOINVIO tipoInvio;

	private String indirizzoInvio;

	private String userInvio;

	private String passwordInvio;

	/**
	 * Costruttore.
	 * 
	 */
	public DatiSpedizioni() {
		super();
	}

	/**
	 * @return the codiceClienteMittenteEstero
	 */
	public String getCodiceClienteMittenteEstero() {
		return codiceClienteMittenteEstero;
	}

	/**
	 * @return the codiceClienteMittenteItalia
	 */
	public String getCodiceClienteMittenteItalia() {
		return codiceClienteMittenteItalia;
	}

	/**
	 * @return the codiceTariffaEstero
	 */
	public String getCodiceTariffaEstero() {
		return codiceTariffaEstero;
	}

	/**
	 * @return the codiceTariffaItalia
	 */
	public String getCodiceTariffaItalia() {
		return codiceTariffaItalia;
	}

	/**
	 * @return the codiceTrattamentoMerce
	 */
	public String getCodiceTrattamentoMerce() {
		return codiceTrattamentoMerce;
	}

	/**
	 * @return the etichettePath
	 */
	public String getEtichettePath() {
		return etichettePath;
	}

	/**
	 * @return the indirizzoInvio
	 */
	public String getIndirizzoInvio() {
		return indirizzoInvio;
	}

	/**
	 * @return the nameFileEtichetteToExport
	 */
	public String getNameFileEtichetteToExport() {
		return nameFileEtichetteToExport;
	}

	/**
	 * @return the nameFileEtichetteToImport
	 */
	public String getNameFileEtichetteToImport() {
		return nameFileEtichetteToImport;
	}

	/**
	 * @return the naturaMerceMittente
	 */
	public String getNaturaMerceMittente() {
		return naturaMerceMittente;
	}

	/**
	 * @return the numeratore
	 */
	public String getNumeratore() {
		return numeratore;
	}

	/**
	 * @return the numeroSerie
	 */
	public String getNumeroSerie() {
		return numeroSerie;
	}

	/**
	 * @return the passwordInvio
	 */
	public String getPasswordInvio() {
		return passwordInvio;
	}

	/**
	 * @return the pathFileRendiconto
	 */
	public String getPathFileRendiconto() {
		return pathFileRendiconto;
	}

	/**
	 * @return the pathFileTemplateEtichette
	 */
	public String getPathFileTemplateEtichette() {
		return pathFileTemplateEtichette;
	}

	/**
	 * @return the pathFileTemplateRendiconto
	 */
	public String getPathFileTemplateRendiconto() {
		return pathFileTemplateRendiconto;
	}

	/**
	 * @return the puntoOperativoArrivo
	 */
	public String getPuntoOperativoArrivo() {
		return puntoOperativoArrivo;
	}

	/**
	 * @return the puntoOperativoPartenzaMerce
	 */
	public String getPuntoOperativoPartenzaMerce() {
		return puntoOperativoPartenzaMerce;
	}

	/**
	 * @return the tipoInvio
	 */
	public TIPOINVIO getTipoInvio() {
		return tipoInvio;
	}

	/**
	 * @return the tipoServizioBolle
	 */
	public String getTipoServizioBolle() {
		return tipoServizioBolle;
	}

	/**
	 * @return the tipoStampante
	 */
	public String getTipoStampante() {
		return tipoStampante;
	}

	/**
	 * @return the userInvio
	 */
	public String getUserInvio() {
		return userInvio;
	}

	/**
	 * Verifica se tutti i path per la generazione e lettura dei file per le etichette e rendicontazione sono
	 * configurati.
	 * 
	 * @return <code>true</code> se tutti i path sono configutrati
	 */
	public boolean isPathFileConfigured() {
		return etichettePath != null && nameFileEtichetteToExport != null && nameFileEtichetteToImport != null
				&& pathFileTemplateEtichette != null && pathFileRendiconto != null
				&& pathFileTemplateRendiconto != null;
	}

	/**
	 * @param codiceClienteMittenteEstero
	 *            the codiceClienteMittenteEstero to set
	 */
	public void setCodiceClienteMittenteEstero(String codiceClienteMittenteEstero) {
		this.codiceClienteMittenteEstero = codiceClienteMittenteEstero;
	}

	/**
	 * @param codiceClienteMittenteItalia
	 *            the codiceClienteMittenteItalia to set
	 */
	public void setCodiceClienteMittenteItalia(String codiceClienteMittenteItalia) {
		this.codiceClienteMittenteItalia = codiceClienteMittenteItalia;
	}

	/**
	 * @param codiceTariffaEstero
	 *            the codiceTariffaEstero to set
	 */
	public void setCodiceTariffaEstero(String codiceTariffaEstero) {
		this.codiceTariffaEstero = codiceTariffaEstero;
	}

	/**
	 * @param codiceTariffaItalia
	 *            the codiceTariffaItalia to set
	 */
	public void setCodiceTariffaItalia(String codiceTariffaItalia) {
		this.codiceTariffaItalia = codiceTariffaItalia;
	}

	/**
	 * @param codiceTrattamentoMerce
	 *            the codiceTrattamentoMerce to set
	 */
	public void setCodiceTrattamentoMerce(String codiceTrattamentoMerce) {
		this.codiceTrattamentoMerce = codiceTrattamentoMerce;
	}

	/**
	 * @param etichettePath
	 *            the etichettePath to set
	 */
	public void setEtichettePath(String etichettePath) {
		this.etichettePath = etichettePath;
	}

	/**
	 * @param indirizzoInvio
	 *            the indirizzoInvio to set
	 */
	public void setIndirizzoInvio(String indirizzoInvio) {
		this.indirizzoInvio = indirizzoInvio;
	}

	/**
	 * @param nameFileEtichetteToExport
	 *            the nameFileEtichetteToExport to set
	 */
	public void setNameFileEtichetteToExport(String nameFileEtichetteToExport) {
		this.nameFileEtichetteToExport = nameFileEtichetteToExport;
	}

	/**
	 * @param nameFileEtichetteToImport
	 *            the nameFileEtichetteToImport to set
	 */
	public void setNameFileEtichetteToImport(String nameFileEtichetteToImport) {
		this.nameFileEtichetteToImport = nameFileEtichetteToImport;
	}

	/**
	 * @param naturaMerceMittente
	 *            the naturaMerceMittente to set
	 */
	public void setNaturaMerceMittente(String naturaMerceMittente) {
		this.naturaMerceMittente = naturaMerceMittente;
	}

	/**
	 * @param numeratore
	 *            the numeratore to set
	 */
	public void setNumeratore(String numeratore) {
		this.numeratore = numeratore;
	}

	/**
	 * @param numeroSerie
	 *            the numeroSerie to set
	 */
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	/**
	 * @param passwordInvio
	 *            the passwordInvio to set
	 */
	public void setPasswordInvio(String passwordInvio) {
		this.passwordInvio = passwordInvio;
	}

	/**
	 * @param pathFileRendiconto
	 *            the pathFileRendiconto to set
	 */
	public void setPathFileRendiconto(String pathFileRendiconto) {
		this.pathFileRendiconto = pathFileRendiconto;
	}

	/**
	 * @param pathFileTemplateEtichette
	 *            the pathFileTemplateEtichette to set
	 */
	public void setPathFileTemplateEtichette(String pathFileTemplateEtichette) {
		this.pathFileTemplateEtichette = pathFileTemplateEtichette;
	}

	/**
	 * @param pathFileTemplateRendiconto
	 *            the pathFileTemplateRendiconto to set
	 */
	public void setPathFileTemplateRendiconto(String pathFileTemplateRendiconto) {
		this.pathFileTemplateRendiconto = pathFileTemplateRendiconto;
	}

	/**
	 * @param puntoOperativoArrivo
	 *            the puntoOperativoArrivo to set
	 */
	public void setPuntoOperativoArrivo(String puntoOperativoArrivo) {
		this.puntoOperativoArrivo = puntoOperativoArrivo;
	}

	/**
	 * @param puntoOperativoPartenzaMerce
	 *            the puntoOperativoPartenzaMerce to set
	 */
	public void setPuntoOperativoPartenzaMerce(String puntoOperativoPartenzaMerce) {
		this.puntoOperativoPartenzaMerce = puntoOperativoPartenzaMerce;
	}

	/**
	 * @param tipoInvio
	 *            the tipoInvio to set
	 */
	public void setTipoInvio(TIPOINVIO tipoInvio) {
		this.tipoInvio = tipoInvio;
	}

	/**
	 * @param tipoServizioBolle
	 *            the tipoServizioBolle to set
	 */
	public void setTipoServizioBolle(String tipoServizioBolle) {
		this.tipoServizioBolle = tipoServizioBolle;
	}

	/**
	 * @param tipoStampante
	 *            the tipoStampante to set
	 */
	public void setTipoStampante(String tipoStampante) {
		this.tipoStampante = tipoStampante;
	}

	/**
	 * @param userInvio
	 *            the userInvio to set
	 */
	public void setUserInvio(String userInvio) {
		this.userInvio = userInvio;
	}
}
