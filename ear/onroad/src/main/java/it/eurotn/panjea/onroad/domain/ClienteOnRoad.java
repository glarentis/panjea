/**
 *
 */
package it.eurotn.panjea.onroad.domain;

import java.io.Serializable;

/**
 * @author leonardo
 */
public class ClienteOnRoad implements Serializable {

	private static final long serialVersionUID = -5476276238825301523L;

	private String clienteBloccato;
	private String codiceAgente;
	private String codiceCliente;
	private String codiceDestinatario;
	private String codiceFiscale;
	private String codiceListino;
	private String codicePagamento;
	private String denominazione;
	private String indirizzo;
	private String lingua;
	private String partitaIva;
	private String ragioneSociale;
	private String ragioneSociale1;
	private String telefono;
	private String tipoCliente;
	private String provincia;
	private String localita;
	private String cap;

	/**
	 * Corstruttore.
	 */
	public ClienteOnRoad() {
		super();
	}

	/**
	 * @return the cap
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * @return the clienteBloccato
	 */
	public String getClienteBloccato() {
		return clienteBloccato;
	}

	/**
	 * @return the codiceAgente
	 */
	public String getCodiceAgente() {
		return codiceAgente;
	}

	/**
	 * @return the codiceCliente
	 */
	public String getCodiceCliente() {
		return codiceCliente;
	}

	/**
	 * @return the codiceDestinatario
	 */
	public String getCodiceDestinatario() {
		return codiceDestinatario;
	}

	/**
	 * @return the codiceFiscale
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * @return the codiceListino
	 */
	public String getCodiceListino() {
		return codiceListino;
	}

	/**
	 * @return the codicePagamento
	 */
	public String getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return the denominazione
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @return the indirizzo
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * @return the lingua
	 */
	public String getLingua() {
		return lingua;
	}

	/**
	 * @return the localita
	 */
	public String getLocalita() {
		return localita;
	}

	/**
	 * @return the partitaIva
	 */
	public String getPartitaIva() {
		return partitaIva;
	}

	/**
	 * @return the provincia
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @return the ragioneSociale
	 */
	public String getRagioneSociale() {
		return ragioneSociale;
	}

	/**
	 * @return the ragioneSociale1
	 */
	public String getRagioneSociale1() {
		return ragioneSociale1;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @return the tipoCliente
	 */
	public String getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * @return cliente bloccato
	 */
	public boolean isSedeBloccata() {
		return (getClienteBloccato() != null && getClienteBloccato().equals("0")) ? true : false;
	}

	/**
	 * @param cap
	 *            the cap to set
	 */
	public void setCap(String cap) {
		this.cap = cap;
	}

	/**
	 * @param clienteBloccato
	 *            the clienteBloccato to set
	 */
	public void setClienteBloccato(String clienteBloccato) {
		this.clienteBloccato = clienteBloccato;
	}

	/**
	 * @param codiceAgente
	 *            the codiceAgente to set
	 */
	public void setCodiceAgente(String codiceAgente) {
		this.codiceAgente = codiceAgente;
	}

	/**
	 * @param codiceCliente
	 *            the codiceCliente to set
	 */
	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	/**
	 * @param codiceDestinatario
	 *            the codiceDestinatario to set
	 */
	public void setCodiceDestinatario(String codiceDestinatario) {
		this.codiceDestinatario = codiceDestinatario;
	}

	/**
	 * @param codiceFiscale
	 *            the codiceFiscale to set
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * @param codiceListino
	 *            the codiceListino to set
	 */
	public void setCodiceListino(String codiceListino) {
		this.codiceListino = codiceListino;
	}

	/**
	 * @param codicePagamento
	 *            the codicePagamento to set
	 */
	public void setCodicePagamento(String codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	/**
	 * @param denominazione
	 *            the denominazione to set
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @param indirizzo
	 *            the indirizzo to set
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * @param lingua
	 *            the lingua to set
	 */
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	/**
	 * @param localita
	 *            the localita to set
	 */
	public void setLocalita(String localita) {
		this.localita = localita;
	}

	/**
	 * @param partitaIva
	 *            the partitaIva to set
	 */
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	/**
	 * @param provincia
	 *            the provincia to set
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 * @param ragioneSociale
	 *            the ragioneSociale to set
	 */
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	/**
	 * @param ragioneSociale1
	 *            the ragioneSociale1 to set
	 */
	public void setRagioneSociale1(String ragioneSociale1) {
		this.ragioneSociale1 = ragioneSociale1;
	}

	/**
	 * @param telefono
	 *            the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @param tipoCliente
	 *            the tipoCliente to set
	 */
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

}
