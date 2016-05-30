package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.domain.SedeOrdine;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;

import org.apache.commons.lang3.StringEscapeUtils;

public class SedeEntitaOnRoad {

	private SedeEntita sedeEntita;
	private SedePagamento sedePagamento;
	private SedeMagazzino sedeMagazzino;
	private SedeOrdine sedeOrdine;

	/**
	 * Costruttore vuoto per importazione.
	 */
	public SedeEntitaOnRoad() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param sedeEntita
	 *            sede entità
	 * @param sedePagamento
	 *            sede pagamento
	 * @param sedeMagazzino
	 *            sede magazzino
	 * @param sedeOrdine
	 *            sede ordine
	 */
	public SedeEntitaOnRoad(final SedeEntita sedeEntita, final SedePagamento sedePagamento,
			final SedeMagazzino sedeMagazzino, final SedeOrdine sedeOrdine) {
		super();
		this.sedeEntita = sedeEntita;
		this.sedePagamento = sedePagamento;
		this.sedeMagazzino = sedeMagazzino;
		this.sedeOrdine = sedeOrdine;
	}

	/**
	 * @return 0 se libera 1 se bloccata
	 */
	public String getBloccata() {
		return getSedeEntita().getEntita().getBloccoSede().isBlocco() || getSedeEntita().getBloccoSede().isBlocco() ? "0"
				: "1";
	}

	/**
	 * @return codice agente o 0000 se null
	 */
	public String getCodiceAgente() {
		String codiceAgente = "0000";
		AgenteLite agente = sedeEntita.getAgente();
		if (agente != null && agente.getCodiceAton() != null && !agente.getCodiceAton().isEmpty()) {
			codiceAgente = agente.getCodiceAton();
		}
		return codiceAgente;
	}

	/**
	 * @return codiceCategoriaSedeMagazzino
	 */
	public String getCodiceCategoriaSedeMagazzino() {
		String codiceCategoriaSedeMagazzino = "";
		CategoriaSedeMagazzino categoriaSedeMagazzino = sedeMagazzino.getCategoriaSedeMagazzino();
		if (categoriaSedeMagazzino != null) {
			codiceCategoriaSedeMagazzino = categoriaSedeMagazzino.getDescrizione();
		}
		return codiceCategoriaSedeMagazzino;
	}

	/**
	 * @return the codiceDestinatario
	 */
	public String getCodiceDestinatario() {
		if (!sedeEntita.getTipoSede().isSedePrincipale()) {
			return sedeEntita.getCodice();
		}
		return sedeEntita.getEntita().getCodice() + "";
	}

	/**
	 * @return la lingua della sede
	 */
	public String getLingua() {
		return sedeEntita.getLingua() != null ? sedeEntita.getLingua().toUpperCase() : "IT";
	}

	/**
	 * @return the ragioneSocialeEntita
	 */
	public String getRagioneSocialeEntita() {
		String ragSociale = new String("");
		if (!sedeEntita.getTipoSede().isSedePrincipale()) {
			ragSociale = sedeEntita.getSede().getDescrizione();
		} else {
			ragSociale = sedeEntita.getEntita().getAnagrafica().getDenominazione();
		}
		if (ragSociale != null) {
			ragSociale = StringEscapeUtils.unescapeHtml4(ragSociale).replaceAll("[^\\x20-\\x7e]", "");
		}
		return ragSociale;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the sedeMagazzino
	 */
	public SedeMagazzino getSedeMagazzino() {
		return sedeMagazzino;
	}

	/**
	 * @return the sedeOrdine
	 */
	public SedeOrdine getSedeOrdine() {
		return sedeOrdine;
	}

	/**
	 * @return the sedePagamento
	 */
	public SedePagamento getSedePagamento() {
		return sedePagamento;
	}

	/**
	 * Valorizzare il campo TipoCliente con 'NO' per i clienti "normali" quindi con CodiceDestinatario = CodiceCliente e
	 * con 'LU' i clienti che rappresentano un "Luogo di consegna" ossia quelli che hanno Destinatario diverso dal
	 * CodiceCliente;'CE' per i fornitori che sono inclusi per i riferimenti con i codici articolo fornitore.
	 * 
	 * @return LU o NO
	 */
	public String getTipoCliente() {
		if (TipoEntita.FORNITORE.equals(sedeEntita.getEntita().getTipo())) {
			return "CE";
		}
		if (!sedeEntita.getTipoSede().isSedePrincipale()) {
			return "LU";
		}
		return "NO";
	}

	/**
	 * @return FT (codice Aton) se il tipo documento ha codice Panjea FAC, NVA altrimenti
	 */
	public String getTipoDocumento() {
		String tipo = "NVA";
		if (sedeOrdine != null) {
			TipoAreaMagazzino tipoAreaEvasione = sedeOrdine.getTipoAreaEvasione();
			if (tipoAreaEvasione != null && tipoAreaEvasione.getTipoDocumento() != null) {
				TipoDocumento tipoDocumento = tipoAreaEvasione.getTipoDocumento();
				if (tipoDocumento.getClasseTipoDocumento().equals(ClasseFattura.class.getName())) {
					tipo = "FT";
				}
			}
		}
		return tipo;
	}

	/**
	 * @param bloccata
	 *            bloccata
	 */
	public void setBloccata(String bloccata) {

	}

	/**
	 * @param codiceAgente
	 *            codiceAgente
	 */
	public void setCodiceAgente(String codiceAgente) {

	}

	/**
	 * @param codiceCategoriaSedeMagazzino
	 *            the codiceCategoriaSedeMagazzino to set
	 */
	public void setCodiceCategoriaSedeMagazzino(String codiceCategoriaSedeMagazzino) {

	}

	/**
	 * @param codiceDestinatario
	 *            the codiceDestinatario to set
	 */
	public void setCodiceDestinatario(String codiceDestinatario) {

	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntita(String codiceEntita) {

	}

	/**
	 * @param lingua
	 *            the lingua to set
	 */
	public void setLingua(String lingua) {

	}

	/**
	 * @param ragioneSocialeEntita
	 *            the ragioneSocialeEntita to set
	 */
	public void setRagioneSocialeEntita(String ragioneSocialeEntita) {

	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param sedeMagazzino
	 *            the sedeMagazzino to set
	 */
	public void setSedeMagazzino(SedeMagazzino sedeMagazzino) {
		this.sedeMagazzino = sedeMagazzino;
	}

	/**
	 * @param sedeOrdine
	 *            the sedeOrdine to set
	 */
	public void setSedeOrdine(SedeOrdine sedeOrdine) {
		this.sedeOrdine = sedeOrdine;
	}

	/**
	 * @param sedePagamento
	 *            the sedePagamento to set
	 */
	public void setSedePagamento(SedePagamento sedePagamento) {
		this.sedePagamento = sedePagamento;
	}

	/**
	 * @param tipoCliente
	 *            the tipoCliente to set
	 */
	public void setTipoCliente(String tipoCliente) {

	}

	/**
	 * @param tipoDocumento
	 *            tipoDocumento
	 */
	public void setTipoDocumento(String tipoDocumento) {

	}

}
