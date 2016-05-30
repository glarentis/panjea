package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Descrive una situazione di una rata.
 * 
 * @author giangi
 */
public class SituazioneRata implements Serializable {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum StatoCarrello {
		SELEZIONABILE, AGGIUNTO_CARRELLO, NON_SELEZIONABILE, DA_AGGIUNGERE
	}

	private static final long serialVersionUID = -8632971587627748037L;

	private StatoCarrello statoCarrello;

	private String protocollo;
	private Integer idAreaContabile;
	private Integer idAreaContabilePagamenti;

	private Date maxDataPagamento;
	private Integer numPagamenti;

	private Rata rata;

	private EntitaDocumento entita;

	private Documento documento;

	private String capEntita;
	private Integer codiceEntita;
	private String denominazione;
	private Integer idAnagraficaEntita;
	private Integer idEntita;
	private String indirizzo;
	private String livelloAmministrativo1;
	private String livelloAmministrativo2;
	private String livelloAmministrativo3;
	private String livelloAmministrativo4;
	private String localita;
	private String nazione;
	private Integer tipoEntita;

	private Integer codiceAgente;
	private String denominazioneAgente;
	private Integer idAgente;
	private AgenteLite agente;

	private String codiceTipoDocumento;
	private String descrizioneTipoDocumento;
	private Integer idTipoDocumento;

	private String codiceSedeEntita;
	private String codiceZonaGeografica;
	private String descrizioneSedeEntita;
	private String descrizioneZonaGeografica;
	private Integer idSedeEntita;
	private Integer idZonaGeografica;
	private String localitaSedeEntita;

	private String codiceCodicePagamento;
	private String descrizioneCodicePagamento;
	private Integer idCodicePagamento;

	private Date dataScadenzaAnticipoFatture;

	/**
	 * Costruttore.
	 */
	{
		statoCarrello = StatoCarrello.SELEZIONABILE;
		rata = new Rata();
		documento = new Documento();
		documento.setTotale(new Importo());
		rata.getAreaRate().setTipoAreaPartita(new TipoAreaPartita());
		rata.getAreaRate().setDocumento(documento);
		rata.setInitialized(true);// setto i valori dall'esterno
		rata.setRataRiemessa(new Rata());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SituazioneRata other = (SituazioneRata) obj;
		if (rata == null) {
			if (other.rata != null) {
				return false;
			}
		} else if (!rata.equals(other.rata)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the agente.
	 */
	public AgenteLite getAgente() {
		if (agente == null && idAgente != null) {
			agente = new AgenteLite();
			agente.setId(idAgente);
			agente.setCodice(codiceAgente);
			agente.getAnagrafica().setDenominazione(denominazioneAgente);
		}
		return agente;
	}

	/**
	 * @return the dataScadenzaAnticipoFatture
	 */
	public Date getDataScadenzaAnticipoFatture() {
		return dataScadenzaAnticipoFatture;
	}

	/**
	 * @return documento della rata.
	 */
	public Documento getDocumento() {
		if (documento.getTipoDocumento().getId() == null) {
			documento.getTipoDocumento().setId(idTipoDocumento);
			documento.getTipoDocumento().setCodice(codiceTipoDocumento);
			documento.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
		}
		if (documento.getSedeEntita() == null) {
			documento.setSedeEntita(new SedeEntita());
			documento.getSedeEntita().setId(idSedeEntita);
			documento.getSedeEntita().setCodice(codiceSedeEntita);
			documento.getSedeEntita().getSede().setDescrizione(descrizioneSedeEntita);

			documento.getSedeEntita().setZonaGeografica(new ZonaGeografica());
			documento.getSedeEntita().getZonaGeografica().setId(idZonaGeografica);
			documento.getSedeEntita().getZonaGeografica().setCodice(codiceZonaGeografica);
			documento.getSedeEntita().getZonaGeografica().setDescrizione(descrizioneZonaGeografica);

			documento.getSedeEntita().getSede().setDatiGeografici(new DatiGeografici());
			documento.getSedeEntita().getSede().getDatiGeografici().setLocalita(new Localita());
			documento.getSedeEntita().getSede().getDatiGeografici().getLocalita().setDescrizione(localitaSedeEntita);
		}
		return documento;
	}

	/**
	 * @return the entita
	 */
	public EntitaDocumento getEntita() {
		if (entita == null && idEntita != null) {
			entita = new EntitaDocumento();
			entita.setTipoEntita(TipoEntita.values()[tipoEntita]);
			entita.setId(idEntita);
			entita.setCodice(codiceEntita);
			entita.setDescrizione(denominazione);
			entita.setIdAnagrafica(idAnagraficaEntita);
			entita.setCap(capEntita);
			entita.setIndirizzo(indirizzo);
			entita.setLivelloAmministrativo1(livelloAmministrativo1);
			entita.setLivelloAmministrativo2(livelloAmministrativo2);
			entita.setLivelloAmministrativo3(livelloAmministrativo3);
			entita.setLivelloAmministrativo4(livelloAmministrativo4);
			entita.setLocalita(localita);
			entita.setNazione(nazione);
		}
		return entita;
	}

	/**
	 * @return the idAreaContabile
	 */
	public Integer getIdAreaContabile() {
		return idAreaContabile;
	}

	/**
	 * @return the idAreaContabilePagamenti
	 */
	public Integer getIdAreaContabilePagamenti() {
		return idAreaContabilePagamenti;
	}

	/**
	 * @return the maxDataPagamento
	 */
	public Date getMaxDataPagamento() {
		return maxDataPagamento;
	}

	/**
	 * @return the numPagamenti
	 */
	public Integer getNumPagamenti() {
		return numPagamenti;
	}

	/**
	 * @return the protocollo
	 */
	public String getProtocollo() {
		return protocollo;
	}

	/**
	 * Metodo utilizzato per restituire solo id e versione della rata che rappresenta la situazione.
	 * 
	 * @return the rata
	 */
	public Rata getRata() {
		if (rata.getAreaRate().getCodicePagamento() == null) {
			rata.getAreaRate().setCodicePagamento(new CodicePagamento());
			rata.getAreaRate().getCodicePagamento().setId(idCodicePagamento);
			rata.getAreaRate().getCodicePagamento().setCodicePagamento(codiceCodicePagamento);
			rata.getAreaRate().getCodicePagamento().setDescrizione(descrizioneCodicePagamento);
		}
		return rata;
	}

	/**
	 * Residuo della rata.
	 * 
	 * @return importo
	 */
	public Importo getResiduoRata() {
		Importo residuoRata = getRata().getResiduo();
		// se la rata è IN_LAVORAZIONE e non ho un'area contabile il residuo della rata è uguale al suo importo perchè
		// in realtà per il cliente non è ancora pagata
		if (getStatoRata() == StatoRata.IN_LAVORAZIONE && getIdAreaContabilePagamenti() == null) {
			residuoRata = getRata().getImporto();
		}

		return residuoRata;
	}

	/**
	 * @return Returns the inCarrello.
	 */
	public StatoCarrello getStatoCarrello() {
		return statoCarrello;
	}

	/**
	 * @return stato del carrello in base allo stato della rata
	 */
	public StatoCarrello getStatoCarrelloFromStatoRata() {
		if (getStatoRata() == StatoRata.CHIUSA || getStatoRata() == StatoRata.IN_LAVORAZIONE
				|| getStatoRata() == StatoRata.IN_RIASSEGNAZIONE || getStatoRata() == StatoRata.RIEMESSA) {
			return StatoCarrello.NON_SELEZIONABILE;
		} else {
			return StatoCarrello.SELEZIONABILE;
		}
	}

	/**
	 * @return stato della rata
	 */
	public StatoRata getStatoRata() {
		return Rata.calcolaStatoRata(rata, numPagamenti, maxDataPagamento != null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rata == null) ? 0 : rata.hashCode());
		return result;
	}

	/**
	 * @param cap
	 *            the cap to set
	 */
	public void setCap(String cap) {
		this.capEntita = cap;
	}

	/**
	 * @param codiceAgente
	 *            the codiceAgente to set
	 */
	public void setCodiceAgente(Integer codiceAgente) {
		this.codiceAgente = codiceAgente;
	}

	/**
	 * @param codiceCodicePagamento
	 *            codiceCodicePagamento to set
	 */
	public void setCodiceCodicePagamento(String codiceCodicePagamento) {
		this.codiceCodicePagamento = codiceCodicePagamento;
	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.codiceEntita = codiceEntita;
	}

	/**
	 * @param codiceSedeEntita
	 *            the codiceSedeEntita to set
	 */
	public void setCodiceSedeEntita(String codiceSedeEntita) {
		this.codiceSedeEntita = codiceSedeEntita;
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.codiceTipoDocumento = codiceTipoDocumento;

	}

	/**
	 * @param codiceValuta
	 *            valore da settare
	 */
	public void setCodiceValuta(String codiceValuta) {
		rata.getImporto().setCodiceValuta(codiceValuta);
		rata.getResiduo().setCodiceValuta(codiceValuta);
	}

	/**
	 * @param codiceZonaGeografica
	 *            the codiceZonaGeografica to set
	 */
	public void setCodiceZonaGeografica(String codiceZonaGeografica) {
		this.codiceZonaGeografica = codiceZonaGeografica;

	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		documento.setDataDocumento(dataDocumento);
	}

	/**
	 * @param dataScadenza
	 *            the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		rata.setDataScadenza(dataScadenza);
	}

	/**
	 * @param dataScadenzaAnticipoFatture
	 *            the dataScadenzaAnticipoFatture to set
	 */
	public void setDataScadenzaAnticipoFatture(Date dataScadenzaAnticipoFatture) {
		this.dataScadenzaAnticipoFatture = dataScadenzaAnticipoFatture;
	}

	/**
	 * @param denominazione
	 *            the denominazione to set
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @param denominazioneAgente
	 *            the denominazioneAgente to set
	 */
	public void setDenominazioneAgente(String denominazioneAgente) {
		this.denominazioneAgente = denominazioneAgente;
	}

	/**
	 * 
	 * @param descrizioneCodicePagamento
	 *            descrizioneCodicePagamento to set
	 */
	public void setDescrizioneCodicePagamento(String descrizioneCodicePagamento) {
		this.descrizioneCodicePagamento = descrizioneCodicePagamento;
	}

	/**
	 * @param descrizioneSedeEntita
	 *            the descrizioneSedeEntita to set
	 */
	public void setDescrizioneSedeEntita(String descrizioneSedeEntita) {
		this.descrizioneSedeEntita = descrizioneSedeEntita;

	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.descrizioneTipoDocumento = descrizioneTipoDocumento;
	}

	/**
	 * @param descrizioneZonaGeografica
	 *            the descrizioneZonaGeografica to set
	 */
	public void setDescrizioneZonaGeografica(String descrizioneZonaGeografica) {
		this.descrizioneZonaGeografica = descrizioneZonaGeografica;
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaDocumento entita) {
		this.entita = entita;
	}

	/**
	 * 
	 * @param ngiorni
	 *            ngiorni to set
	 */
	public void setGiorniLimitiScontoFinanziario(Integer ngiorni) {
		rata.getAreaRate().setGiorniLimite(ngiorni);
	}

	/**
	 * @param idAgente
	 *            the idAgente to set
	 */
	public void setIdAgente(Integer idAgente) {
		this.idAgente = idAgente;
	}

	/**
	 * @param idAnagraficaEntita
	 *            the idAnagraficaEntita to set
	 */
	public void setIdAnagraficaEntita(Integer idAnagraficaEntita) {
		this.idAnagraficaEntita = idAnagraficaEntita;
	}

	/**
	 * @param idAreaContabile
	 *            the idAreaContabile to set
	 */
	public void setIdAreaContabile(Integer idAreaContabile) {
		this.idAreaContabile = idAreaContabile;
	}

	/**
	 * @param idAreaContabilePagamenti
	 *            the idAreaContabilePagamenti to set
	 */
	public void setIdAreaContabilePagamenti(Integer idAreaContabilePagamenti) {
		this.idAreaContabilePagamenti = idAreaContabilePagamenti;
	}

	/**
	 * @param idAreaRate
	 *            idAreaRate to set
	 */
	public void setIdAreaRate(Integer idAreaRate) {
		rata.getAreaRate().setId(idAreaRate);
	}

	/**
	 * @param idCodicePagamento
	 *            idCodicePagamento to set
	 */
	public void setIdCodicePagamento(Integer idCodicePagamento) {
		this.idCodicePagamento = idCodicePagamento;
	}

	/**
	 * @param idDocumento
	 *            id documento to set
	 */
	public void setIdDocumento(int idDocumento) {
		documento.setId(idDocumento);
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		this.idEntita = idEntita;
	}

	/**
	 * @param idRata
	 *            the idRata to set
	 */
	public void setIdRata(Integer idRata) {
		rata.setId(idRata);
	}

	/**
	 * @param idRataRiemessa
	 *            the idRataRiemessa to set
	 */
	public void setIdRataRiemessa(Integer idRataRiemessa) {
		rata.getRataRiemessa().setId(idRataRiemessa);
	}

	/**
	 * @param idSedeEntita
	 *            the idSedeEntita to set
	 */
	public void setIdSedeEntita(Integer idSedeEntita) {
		this.idSedeEntita = idSedeEntita;
	}

	/**
	 * @param idTipoDocumento
	 *            the idTipoDocumento to set
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	/**
	 * @param idZonaGeografica
	 *            the idZonaGeografica to set
	 */
	public void setIdZonaGeografica(Integer idZonaGeografica) {
		this.idZonaGeografica = idZonaGeografica;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImportoInValuta(BigDecimal importo) {
		rata.getImporto().setImportoInValuta(importo);
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImportoInValutaAzienda(BigDecimal importo) {
		rata.getImporto().setImportoInValutaAzienda(importo);
	}

	/**
	 * @param importoInValutaAziendaRateCollegate
	 *            The numeroRateCollegate to set.
	 */
	public void setImportoInValutaAziendaRateCollegate(BigDecimal importoInValutaAziendaRateCollegate) {
		this.rata.setImportoInValutaAziendaRateCollegate(importoInValutaAziendaRateCollegate);
	}

	/**
	 * @param importoInValutaRateCollegate
	 *            The numeroRateCollegate to set.
	 */
	public void setImportoInValutaRateCollegate(BigDecimal importoInValutaRateCollegate) {
		this.rata.setImportoInValutaRateCollegate(importoInValutaRateCollegate);
	}

	/**
	 * @param tassoDiCambio
	 *            tasso di cambio della rata
	 */
	public void setImportoTassoDiCambio(BigDecimal tassoDiCambio) {
		rata.getImporto().setTassoDiCambio(tassoDiCambio);
	}

	/**
	 * @param indirizzo
	 *            the indirizzo to set
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * @param livelloAmministrativo1
	 *            the livelloAmministrativo1 to set
	 */
	public void setLivelloAmministrativo1(String livelloAmministrativo1) {
		this.livelloAmministrativo1 = livelloAmministrativo1;
	}

	/**
	 * @param livelloAmministrativo2
	 *            the livelloAmministrativo2 to set
	 */
	public void setLivelloAmministrativo2(String livelloAmministrativo2) {
		this.livelloAmministrativo2 = livelloAmministrativo2;
	}

	/**
	 * @param livelloAmministrativo3
	 *            the livelloAmministrativo3 to set
	 */
	public void setLivelloAmministrativo3(String livelloAmministrativo3) {
		this.livelloAmministrativo3 = livelloAmministrativo3;
	}

	/**
	 * @param livelloAmministrativo4
	 *            the livelloAmministrativo4 to set
	 */
	public void setLivelloAmministrativo4(String livelloAmministrativo4) {
		this.livelloAmministrativo4 = livelloAmministrativo4;
	}

	/**
	 * @param localita
	 *            the localita to set
	 */
	public void setLocalita(String localita) {
		this.localita = localita;
	}

	/**
	 * @param localitaSedeEntita
	 *            the localitaSedeEntita to set
	 */
	public void setLocalitaSedeEntita(String localitaSedeEntita) {
		this.localitaSedeEntita = localitaSedeEntita;
	}

	/**
	 * @param maxDataPagamento
	 *            the maxDataPagamento to set
	 */
	public void setMaxDataPagamento(Date maxDataPagamento) {
		this.maxDataPagamento = maxDataPagamento;
	}

	/**
	 * @param nazione
	 *            the nazione to set
	 */
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	/**
	 * @param noteRata
	 *            the noteRata to set
	 */
	public void setNoteRata(String noteRata) {
		rata.setNote(noteRata);
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		documento.getCodice().setCodice(numeroDocumento);
	}

	/**
	 * @param numeroRata
	 *            the numeroRata to set
	 */
	public void setNumeroRata(Integer numeroRata) {
		rata.setNumeroRata(numeroRata);
	}

	/**
	 * @param numeroRataRiemessa
	 *            The numeroRataRiemessa to set.
	 */
	public void setNumeroRataRiemessa(Integer numeroRataRiemessa) {
		rata.getRataRiemessa().setNumeroRata(numeroRataRiemessa);
	}

	/**
	 * @param numPagamenti
	 *            the numPagamenti to set
	 */
	public void setNumPagamenti(Integer numPagamenti) {
		this.numPagamenti = numPagamenti;
	}

	/**
	 * @param sconto
	 *            sconto to set
	 */
	public void setPercentualeScontoFinanziario(BigDecimal sconto) {
		rata.getAreaRate().setPercentualeSconto(sconto);
	}

	/**
	 * @param protocollo
	 *            the protocollo to set
	 */
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	/**
	 * @param rata
	 *            the rata to set
	 */
	public void setRata(Rata rata) {
		this.rata = rata;
	}

	/**
	 * @param ritenutaAcconto
	 *            the ritenutaAcconto to set
	 */
	public void setRitenutaAcconto(boolean ritenutaAcconto) {
		rata.setRitenutaAcconto(ritenutaAcconto);
	}

	/**
	 * @param statoCarrello
	 *            The statoCarrello to set.
	 */
	public void setStatoCarrello(StatoCarrello statoCarrello) {
		this.statoCarrello = statoCarrello;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(Integer tipoEntita) {
		this.tipoEntita = tipoEntita;
	}

	/**
	 * @param tipoPagamento
	 *            the tipoPagamento to set
	 */
	public void setTipoPagamento(Integer tipoPagamento) {
		rata.setTipoPagamento(TipoPagamento.values()[tipoPagamento]);
	}

	/**
	 * @param tipoPartita
	 *            the tipoPartita to set
	 */
	public void setTipoPartita(Integer tipoPartita) {
		rata.getAreaRate().getTipoAreaPartita().setTipoPartita(TipoPartita.values()[tipoPartita]);
	}

	/**
	 * 
	 * @param codiceValuta
	 *            valore da settare
	 */
	public void setTotaleDocumentoCodiceValuta(String codiceValuta) {
		documento.getTotale().setCodiceValuta(codiceValuta);
	}

	/**
	 * 
	 * @param importo
	 *            valore da settare
	 */
	public void setTotaleDocumentoInValuta(BigDecimal importo) {
		documento.getTotale().setImportoInValuta(importo);
	}

	/**
	 * 
	 * @param importo
	 *            valore da settare
	 */
	public void setTotaleDocumentoInValutaAzienda(BigDecimal importo) {
		documento.getTotale().setImportoInValutaAzienda(importo);
	}

	/**
	 * @param totalePagato
	 *            the totalePagato to set
	 */
	public void setTotalePagatoValuta(BigDecimal totalePagato) {
		rata.getTotalePagato().setImportoInValuta(totalePagato);
		rata.getResiduo().setImportoInValuta(
				rata.getImporto().getImportoInValuta().subtract(totalePagato)
						.subtract(rata.getImportoInValutaRateCollegate()));
	}

	/**
	 * @param totalePagato
	 *            the totalePagato to set
	 */
	public void setTotalePagatoValutaAzienda(BigDecimal totalePagato) {
		rata.getTotalePagato().setImportoInValutaAzienda(totalePagato);
		rata.getResiduo().setImportoInValutaAzienda(
				rata.getImporto().getImportoInValutaAzienda().subtract(totalePagato)
						.subtract(rata.getImportoInValutaAziendaRateCollegate()));
	}

	/**
	 * @param versioneRata
	 *            the versioneRata to set
	 */
	public void setVersioneRata(Integer versioneRata) {
		rata.setVersion(versioneRata);
	}

	/**
	 * @param versioneRataRiemessa
	 *            the versioneRataRiemessa to set
	 */
	public void setVersioneRataRiemessa(Integer versioneRataRiemessa) {
		rata.getRataRiemessa().setVersion(versioneRataRiemessa);
	}
}
