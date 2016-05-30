package it.eurotn.panjea.preventivi.rich.bd;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.interfaces.IAreaDocumentoBD;
import it.eurotn.panjea.preventivi.service.exception.ClientePotenzialePresenteException;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.panjea.preventivi.util.RigaMovimentazione;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPreventiviBD extends IRigheBD<AreaPreventivo>, ICopiaDocumentoBD,
IAreaDocumentoBD<AreaPreventivo, AreaPreventivoFullDTO> {

	/**
	 * Aggiunge una variazione e modifica la percentuale di variazione a ogni riga articolo dell'area magazzino.
	 *
	 * @param idAreaPreventivo
	 *            area magazzino
	 * @param variazione
	 *            variazione
	 * @param percProvvigione
	 *            percentuale di provvigione
	 * @param variazioneScontoStrategy
	 *            strategia di variazione dello sconto
	 * @param tipoVariazioneScontoStrategy
	 *            tipo di variazione dello sconto
	 * @param variazioneProvvigioneStrategy
	 *            strategia di variazione della provvigione
	 * @param tipoVariazioneProvvigioneStrategy
	 *            tipo di variazione della provvigione
	 */
	void aggiungiVariazione(Integer idAreaPreventivo, BigDecimal variazione, BigDecimal percProvvigione,
			RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
			TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
			RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
			TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy);

	/**
	 *
	 * @param areaPreventivo
	 *            areaPreventivo
	 * @param statoDaApplicare
	 *            statoDaApplicare
	 * @return areaPreventivo con statoModificato.
	 * @throws ClientePotenzialePresenteException
	 *             sollevata il preventivo fa riferimento ad un cliente potenziale all'accettazione
	 */
	AreaPreventivo cambiaStatoSeApplicabile(AreaPreventivo areaPreventivo, StatoAreaPreventivo statoDaApplicare)
			throws ClientePotenzialePresenteException;

	/**
	 * Cancella un'area preventivo.
	 *
	 * @param areaPreventivo
	 *            areaPreventivo da cancellare
	 */
	void cancellaAreaPreventivo(AreaPreventivo areaPreventivo);

	/**
	 * Cancella una lista di aree preventivo.
	 *
	 * @param areaPreventivo
	 *            aree da cancellare
	 */
	void cancellaAreePreventivo(List<AreaPreventivo> areaPreventivo);

	/**
	 * Cancella una {@link RigaPreventivo} e restituisce l'area ordine a cui era legata.
	 *
	 * @param riga
	 *            riga da cancellare
	 * @return area preventivo legata alla riga cancellata
	 */
	AreaPreventivo cancellaRigaPreventivo(RigaPreventivo riga);

	/**
	 * cancella un {@link TipoAreaPreventivo}.
	 *
	 * @param tipoAreaPreventivo
	 *            {@link TipoAreaPreventivo} da cancellare
	 */
	void cancellaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo);

	/**
	 * Carica una {@link AreaPreventivo}.
	 *
	 * @param areaPreventivo
	 *            area da caricare
	 * @return area caricata
	 */
	AreaPreventivo caricaAreaPreventivo(AreaPreventivo areaPreventivo);

	/**
	 * metodo incaricato di eseguire il caricamento di {@link AreaPreventivoFullDTO} e controllo.
	 *
	 * @param paramenters
	 *            parametri
	 * @return AreaPreventivoFullDTO caricata
	 */
	AreaPreventivoFullDTO caricaAreaPreventivoControlloFullDTO(Map<Object, Object> paramenters);

	/**
	 * metodo incaricato di eseguire il caricamento di {@link AreaPreventivoFullDTO} .
	 *
	 * @param areaPreventivo
	 *            area preventivo da caricare
	 * @return AreaPreventivoFullDTO caricata
	 */
	AreaPreventivoFullDTO caricaAreaPreventivoFullDTO(AreaPreventivo areaPreventivo);

	/**
	 * metodo incaricato di eseguire il caricamento di {@link AreaPreventivoFullDTO} .
	 *
	 * @param paramenters
	 *            parametri
	 * @return AreaPreventivoFullDT0
	 */
	AreaPreventivoFullDTO caricaAreaPreventivoFullDTOStampa(Map<Object, Object> paramenters);

	/**
	 * Carica la movimentazione dei preventivi con la possibilità di paginazione.
	 *
	 * @param parametriRicercaMovimentazione
	 *            parametri di ricerca
	 * @param page
	 *            pagina caricata
	 * @param sizeOfPage
	 *            dimensione della pagina
	 * @return righe di movimentazione caricate
	 */
	List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
			int page, int sizeOfPage);

	/**
	 * Carica una riga preventivo.
	 *
	 * @param rigaPreventivo
	 *            Riga ordine da caricare
	 * @return Riga preventivo caricata
	 */
	RigaPreventivo caricaRigaPreventivo(RigaPreventivo rigaPreventivo);

	/**
	 * Carica tutte le righe collegate alle riga preventivo passata come parametro.
	 *
	 * @param rigaPreventivo
	 *            riga preventivo di riferimento
	 * @return righe collegate caricate
	 */
	List<RigaDestinazione> caricaRigheCollegate(RigaPreventivo rigaPreventivo);

	/**
	 * Carica tutte le righe che possono essere ancora evase del preventivo di riferimento.
	 *
	 * @param idAreaPreventivo
	 *            id preventivo da evadere
	 * @return righe evasione
	 */
	List<RigaEvasione> caricaRigheEvasione(Integer idAreaPreventivo);

	/**
	 * Carica le righe di una area preventivo scelta.
	 *
	 * @param areaPreventivo
	 *            l'area preventivo di cui caricare le righe
	 * @return List<RigaPreventivo>
	 */
	List<RigaPreventivo> caricaRighePreventivo(AreaPreventivo areaPreventivo);

	/**
	 * Carica le righe DTO di areaPreventivo scelta.
	 *
	 * @param areaPreventivo
	 *            l'area preventivo di cui caricare le righe
	 * @return List<RigaPreventivo>
	 */
	List<RigaPreventivoDTO> caricaRighePreventivoDTO(AreaPreventivo areaPreventivo);

	/**
	 * Carica i tipi area preventivo.
	 *
	 * @param valueSearch
	 *            valore da filtrare
	 * @param fieldSearch
	 *            field da filtrare
	 *
	 * @param loadTipiDocumentoDisabilitati
	 *            true se voglio caricare anche i tipi preventivo disabilitati
	 * @return lista dei TipoAreaPreventivo
	 */
	List<TipoAreaPreventivo> caricaTipiAreaPreventivo(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentoDisabilitati);

	/**
	 *
	 * @return lista dei tipiDocumento legati ai TipiAreaPreventivo
	 */
	List<TipoDocumento> caricaTipiDocumentiPreventivo();

	/**
	 *
	 * @param id
	 *            id del TipoAreaPreventivo da caricare
	 * @return TipoAreaPreventivo caricata
	 */
	TipoAreaPreventivo caricaTipoAreaPreventivo(Integer id);

	/**
	 *
	 * @param idTipoDocumento
	 *            id Tipo Documento riferito al TipoAreaPreventivo
	 * @return TipoAreaPreventivo legata al tipoDocumento
	 */
	TipoAreaPreventivo caricaTipoAreaPreventivoPerTipoDocumento(Integer idTipoDocumento);

	/**
	 *
	 * @param righePreventivoDaCambiare
	 *            righe da collegare all'ultima testata inserita
	 */
	@Override
	void collegaTestata(Set<Integer> righePreventivoDaCambiare);

	/**
	 * Esegue una copia del preventivo di riferimento (area prevetivo, documento) e restituisce l'
	 * {@link idAreaPreventivo} generata.
	 *
	 * @param idAreaPreventivo
	 *            id del preventivo da copiare
	 * @return copia effettuata
	 */
	AreaPreventivoFullDTO copiaPreventivo(Integer idAreaPreventivo);

	/**
	 * Crea una riga articolo.
	 *
	 * @param parametriCreazioneRigaArticolo
	 *            di creazione
	 * @return nuova rigaArticolo con i parametri settatti.
	 */
	RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

	/**
	 * Crea una riga note automatica per l'area preventivo indicata.
	 *
	 * @param areaDocumento
	 *            area di riferimento
	 * @param note
	 *            note
	 * @return <code>true</code> se la riga viene generata e salvata correttamente
	 */
	@Override
	boolean creaRigaNoteAutomatica(AreaPreventivo areaDocumento, String note);

	/**
	 * Effettua l'evasione delle righe evasione del preventivo.
	 *
	 * @param rigaEvasione
	 *            righe da evadere
	 * @param tipoAreaOrdine
	 *            tipo area ordine di destinazione
	 * @param deposito
	 *            deposito di destinazione
	 * @param dataOrdine
	 *            data da assegnare all'ordine
	 * @param agente
	 *            agente che verrà assegnato all'ordine
	 * @param dataConsegnaOrdine
	 *            data di consegna dell'ordine
	 */
	void evadiPreventivi(List<RigaEvasione> rigaEvasione, TipoAreaOrdine tipoAreaOrdine, DepositoLite deposito,
			Date dataOrdine, AgenteLite agente, Date dataConsegnaOrdine);

	/**
	 *
	 * Crea e aslva le righe di magazzino per il raggruppamento voluto.
	 *
	 * @param idAreaPreventivo
	 *            area preventivo alla quale associare le nuove righe
	 * @param provenienzaPrezzo
	 *            provenienza del prezzo. Da listino (comprende anche i contratti) o da costoUltimo.
	 * @param idRaggruppamentoArticoli
	 *            id del raggruppamento da inserire
	 * @param data
	 *            data del documento
	 * @param idSedeEntita
	 *            sede magazzino documento
	 * @param idListinoAlternativo
	 *            listino alternativo documento
	 * @param idListino
	 *            listino documento
	 * @param importo
	 *            importo con parametri di default per la valuta settati
	 * @param codiceIvaAlternativo
	 *            coedice iva da usare sulla riga articolo, se null viene usato il codice iva dell'articolo
	 * @param idTipoMezzo
	 *            = id del tipo mezzo
	 * @param idZonaGeografica
	 *            id della zona geografica
	 * @param noteSuDestinazione
	 *            imposta se stampare le note riga sul documento di destinazione
	 * @param codiceValuta
	 *            codice della valuta di riferimento
	 * @param codiceLingua
	 *            codice della lingua di riferimento
	 * @param idAgente
	 *            id agente
	 * @param tipologiaCodiceIvaAlternativo
	 *            tipologia co. iva sost.
	 * @param percentualeScontoCommerciale
	 *            percentuale sconto commerciale
	 */
	void inserisciRaggruppamentoArticoli(Integer idAreaPreventivo, ProvenienzaPrezzo provenienzaPrezzo,
			Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
			Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
			Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
			Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
			BigDecimal percentualeScontoCommerciale);

	/**
	 * Ricalcola i prezzi dell'area ordine e sue righe e la restituisce.
	 *
	 * @param idAreaPreventivo
	 *            id area preventivo
	 * @return area preventivo ricalcolata
	 */
	AreaPreventivoFullDTO ricalcolaPrezziPreventivo(Integer idAreaPreventivo);

	/**
	 * metodo di ricerca per {@link AreaPreventivo}.
	 *
	 * @param parametriRicercaAreaPreventivo
	 *            parametri per la ricerca
	 *
	 * @return Collection di {@link AreaPreventivoRicerca} che soddisfano i criteri di ricerca
	 */
	List<AreaPreventivoRicerca> ricercaAreePreventivo(ParametriRicercaAreaPreventivo parametriRicercaAreaPreventivo);

	/**
	 * Salva una {@link AreaAreaPreventivoOrdine}.
	 *
	 * @param areaPreventivo
	 *            areaPreventivo area da salvare
	 *
	 * @param areaRate
	 *            Arae rate
	 * @return area salvata
	 */
	AreaPreventivoFullDTO salvaAreaPreventivo(AreaPreventivo areaPreventivo, AreaRate areaRate);

	/**
	 * Salva la riga ordine senza effettuare nessun controllo.
	 *
	 * @param riga
	 *            Riga preventivo da salvare
	 * @param checkRiga
	 *            indica se eseguire i vari controlli e salvare i dati collegati che cambiamo(come lo stato dell'area
	 *            oridne)
	 * @return Riga preventivo salvata
	 */
	RigaPreventivo salvaRigaPreventivo(RigaPreventivo riga, boolean checkRiga);

	/**
	 *
	 * @param tipoAreaPreventivo
	 *            tipoAreaPreventivo da salvare.
	 * @return tipoAreaPreventivo salvata
	 */
	TipoAreaPreventivo salvaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo);

	/**
	 *
	 * @param righeDaSpostare
	 *            id delle righe da spostare all'interno del documento
	 * @param idDest
	 *            id della riga di riferimento per lo spostamento. Le righe verranno spostate sopra questa
	 */
	@Override
	void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest);

	/**
	 * Totalizza il documento. Calcola il totale del documento e i vari totalizzatori (spese, spese varie etc...).<br/>
	 * Non salva il documento lo totalizza solamente
	 *
	 * @param areaPreventivo
	 *            areaPreventivo da totalizzare * @param areaRate areaRate
	 * @return areaPreventivo totalizzata
	 */
	AreaPreventivo totalizzaDocumento(AreaPreventivo areaPreventivo, AreaRate areaRate);

	/**
	 * Valida le righe ordine.
	 *
	 * @param areaPreventivo
	 *            area preventivo
	 * @param areaRate
	 *            areaRate
	 * @param cambioStato
	 *            indica se al termine della verifica si dovrà cambiare lo stato dell'area
	 * @return AreaPreventivoFullDTO
	 * @throws TotaleDocumentoNonCoerenteException
	 */
	AreaPreventivoFullDTO validaRighePreventivo(AreaPreventivo areaPreventivo, AreaRate areaRate, boolean cambioStato);
}
