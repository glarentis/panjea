package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.OperazioneAreaContabileNonTrovata;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.partite.domain.AreaPartite;

/**
 *
 * Manager per AreaMagazzino.
 *
 * @author adriano
 * @version 1.0, 27/ago/2008
 *
 */
@Local
public interface AreaMagazzinoManager {

    /**
     *
     * @param areaMagazzino
     *            areaMagazzinodaAggiornare
     * @param sedeEntita
     *            sedeEnttita da associare
     * @return areamagazzino aggiornata (non salvata)
     */
    AreaMagazzino aggiornaDatiSede(AreaMagazzino areaMagazzino, SedeEntita sedeEntita);

    /**
     * Aggiunge una variazione e modifica la percentuale di variazione a ogni riga articolo dell'area magazzino.
     *
     * @param idAreaMagazzino
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
    void aggiungiVariazione(Integer idAreaMagazzino, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy);

    /**
     * Cambia lo stato dell'area magazzino.<br.> Salva l'area di magazzino senza eseguire una flush.
     *
     * @param areaMagazzino
     *            areaMagazzino da cambiare
     * @return areaMagazzino con lo stato modificato.
     */
    AreaMagazzino cambiaStatoDaConfermatoInProvvisorio(AreaMagazzino areaMagazzino);

    /**
     * Cambia lo stato dell'area magazzino.<br.> Salva l'area di magazzino senza eseguire una flush.
     *
     * @param areaMagazzino
     *            areaMagazzino da cambiare
     * @return areaMagazzino con lo stato modificato.
     */
    AreaMagazzino cambiaStatoDaProvvisorioInConfermato(AreaMagazzino areaMagazzino);

    /**
     * Cambia lo stato dell'area magazzino.<br.> Salva l'area di magazzino senza eseguire una flush.
     *
     * @param areaMagazzino
     *            areaMagazzino da cambiare
     * @return areaMagazzino con lo stato modificato.
     */
    AreaMagazzino cambiaStatoDaProvvisorioInForzato(AreaMagazzino areaMagazzino);

    /**
     * Cambia lo stato dell'area magazzino.<br.> Salva l'area di magazzino senza eseguire una flush.
     *
     * @param areaMagazzino
     *            areaMagazzino da cambiare
     * @return areaMagazzino con lo stato modificato.
     */
    AreaMagazzino cambiaStatoInProvvisorio(AreaMagazzino areaMagazzino);

    /**
     * Carica la lista degli agenti presenti sulle varie righe di un area magazzino.
     *
     * @param idAreaMagazzino
     *            id dell'area magazzino interessata
     * @return lista degli agenti presenti sulle righe. Lista vuota se non ci sono agenti sulle righe.
     */
    List<AgenteLite> caricaAgentiPerAreaMagazzino(int idAreaMagazzino);

    /**
     * Carica {@link AreaMagazzino} identificato dall'attributo id dell'argomento {@link AreaMagazzino}.
     *
     * @param areaMagazzino
     *            areaMagazzino da caricare con id avvalorato
     * @return areaMagazzino caricata
     */
    AreaMagazzino caricaAreaMagazzino(AreaMagazzino areaMagazzino);

    /**
     * Carica l'area magazzino legata al documento.
     *
     * @param documento
     *            documento interessato
     * @return areaMagazzino per il documento
     */
    AreaMagazzino caricaAreaMagazzinoByDocumento(Documento documento);

    /**
     * metodo incaricato di eseguire il caricamento di {@link AreaMagazzinoFullDTO}.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}.
     * @return {@link AreaMagazzinoFullDTO}
     */
    AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(AreaMagazzino areaMagazzino);

    /**
     * Carica una lista di aree magazzino per statoAreaMagazzino.
     *
     * @param statoAreaMagazzino
     *            lo stato di cui trovare le aree magazzino
     * @return la lista di aree magazzino con stato scelto
     */
    List<AreaMagazzino> caricaAreeMagazzinoByStato(StatoAreaMagazzino statoAreaMagazzino);

    /**
     * Data una lista di areeMagazzino carica le aree collegate a queste.<br/>
     * (Es. data una lista di fatture recupero i ddt che le hanno generate).<br/>
     *
     * @param areeMagazzino
     *            areeMagazzino per le quali caricare le aree a loro collegati
     * @return aree magazzino collegate
     */
    List<AreaMagazzinoLite> caricaAreeMagazzinoCollegate(List<AreaMagazzino> areeMagazzino);

    /**
     * Carica tutte le aree magazzino che hanno la richiesta di uno o più dati accompagnatori configurati sul tipo area.
     *
     * @param dataEvasione
     *            data di evasione
     * @return aree con richiesta dati accompagnatori
     */
    List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(Date dataEvasione);

    /**
     * Carica tutte le aree magazzino che hanno la richiesta di uno o più dati accompagnatori configurati sul tipo area.
     *
     * @param idAree
     *            id aree
     * @return aree con richiesta dati accompagnatori
     */
    List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(List<Integer> idAree);

    /**
     * Restituisce gli id delle aree magazzino ordinati per la stampa dell'evasione.
     *
     * @param aree
     *            aree magazzino
     * @return id ordinati
     */
    List<Integer> caricaIdAreeMagazzinoPerStampaEvasione(List<AreaMagazzinoRicerca> aree);

    /**
     * Carica il DTO che contiene tutti i dati relativi alla sede entità, necessari per l'area magazzino.
     *
     * @param sedeEntita
     *            <code>SedeEntita</code> di riferimento
     * @return <code>SedeAreaMagazzinoDTO</code> caricato
     */
    SedeAreaMagazzinoDTO caricaSedeAreaMagazzinoDTO(SedeEntita sedeEntita);

    /**
     * Controlla se è presente un'area magazzino sul documento.
     *
     * @param documento
     *            documento da controllare
     * @return areaMagazzinoLite se presente (solamente con id avvalorato), null altrimenti
     */
    AreaMagazzinoLite checkAreaMagazzino(Documento documento);

    /**
     * Ordina le righe di un area magazzino
     *
     * @param areaMagazzino
     *            areaMagazznio con le righe da ordinare
     */
    void ordinaRighe(AreaMagazzino areaMagazzino);

    AreaMagazzino ricalcolaPrezziMagazzino(Integer idAreaMagazzino);

    /**
     * metodo di ricerca per {@link AreaMagazzino} .
     *
     * @param parametriRicercaAreaMagazzino
     *            parametri per la ricerca
     *
     * @return Collection di {@link AreaMagazzinoRicerca} che soddisfano i criteri di ricerca
     */
    List<AreaMagazzinoRicerca> ricercaAreeMagazzino(ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino);

    /**
     * esegue il salvataggio dell'argomento {@link AreaMagazzino} <br>
     * Se nuova area magazzino verifica l'esistenza di {@link Documento} per gli attributi {@link TipoDocumento}, numero
     * (attributo codice), <br>
     * {@link Entita} e data documento; Se il {@link Documento} viene trovato viene associato all'areaMagazzino
     * altrimenti viene eseguito il suo salvataggio. <br>
     * L'attributo forzaSalvataggio indica di eseguire il salvataggio di {@link AreaMagazzino} se non esistesse
     * {@link Documento} <br>
     * per bypassare l'indicatore {@link OperazioneAreaContabileNonTrovata} AVVISARE
     *
     * @param areaMagazzino
     *            areaMagazzino da salvare
     * @param forzaSalvataggio
     *            se forzato crea tutte le aree senza chiedere e rilanciare eccezioni
     * @return area magazzino salvata
     * @throws DocumentiEsistentiPerAreaMagazzinoException
     *             DocumentiEsistentiPerAreaMagazzinoException
     * @throws DocumentoAssenteBloccaException
     *             DocumentoAssenteBloccaException
     * @throws DocumentoAssenteAvvisaException
     *             DocumentoAssenteAvvisaException
     */
    AreaMagazzino salvaAreaMagazzino(AreaMagazzino areaMagazzino, boolean forzaSalvataggio)
            throws DocumentoAssenteAvvisaException, DocumentoAssenteBloccaException,
            DocumentiEsistentiPerAreaMagazzinoException;

    /**
     *
     * @param righeDaSpostare
     *            id delle righe da spostare all'interno del documento
     * @param idDest
     *            id della riga di riferimento per lo spostamento. Le righe verranno spostate sopra questa
     */
    void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest);

    /**
     * Totalizza il documento<br/>
     * . Calcola il totale del documento e i vari totalizzatori (spese, spese varie etc...).<br/>
     * Non salva il documento lo totalizza solamente
     *
     * @param strategia
     *            strategia di totalizzazione
     * @param areaMagazzino
     *            areaMagazzino da totalizzare
     * @param areaPartite
     *            area partite
     * @return areaMagazzino totalizzata
     */
    AreaMagazzino totalizzaDocumento(StrategiaTotalizzazioneDocumento strategia, AreaMagazzino areaMagazzino,
            AreaPartite areaPartite);

    /**
     * Totalizza il documento<br/>
     * . Calcola il totale del documento e i vari totalizzatori (spese, spese varie etc...).<br/>
     * Non salva il documento lo totalizza solamente
     *
     * @param strategia
     *            strategia di totalizzazione
     * @param areaMagazzino
     *            areaMagazzino da totalizzare
     * @param righeIva
     *            righeIva per il documento
     * @return areaMagazzino totalizzata
     */
    AreaMagazzino totalizzaDocumento(StrategiaTotalizzazioneDocumento strategia, AreaMagazzino areaMagazzino,
            List<RigaIva> righeIva);

    /**
     * Esegue la conferma del Documento. <br>
     * La conferma del documento implica la generazione dell'area iva, la generazione dell'area partite e il cambio
     * stato dell'area magazzino
     *
     * @param areaMagazzino
     *            areaMagazzino da validare
     * @param areaPartite
     *            areaPartite del documento
     * @param areaContabilePresente
     *            se ho un area contabile non devo generare le aree Iva e partite
     * @param forzaStato
     *            se TRUE e il totale non corrisponde forza lo stato dell'area a <code>FORZATO</code>
     * @return areaMagazzino validata
     * @exception TotaleDocumentoNonCoerenteException
     *                lanciato se la parte iva calcolata tramite l'area magazzino non è uguale a quella presente
     *                (calcolata dalla parte contabile).
     */
    AreaMagazzino validaRigheMagazzino(AreaMagazzino areaMagazzino, AreaPartite areaPartite,
            Boolean areaContabilePresente, boolean forzaStato) throws TotaleDocumentoNonCoerenteException;
}
