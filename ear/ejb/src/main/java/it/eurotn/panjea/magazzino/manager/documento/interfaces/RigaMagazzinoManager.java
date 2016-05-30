package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRegoleValidazioneRighe;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

@Local
public interface RigaMagazzinoManager {

    /**
     * Aggiorna il prezzo unitario della riga. L'area magazzino viene invalidata e messa in
     * provvisorio.
     *
     * @param riga
     *            con il nuovo prezzo da settare
     * @return true se ho aggiornato la riga. False se il prezzo è uguale e quindi non c'è stato
     *         nessun aggiornamento.
     */
    boolean aggiornaPrezzoRiga(RigaArticoloLite riga);

    /**
     * Aggiunge o rimuove, in base all'azione, ad ogni riga dell'area uno sconto come primo sconto,
     * shiftando quelli già presenti.
     *
     * @param areaMagazzino
     *            area di riferimento
     * @param importoSconto
     *            importo dello sconto commerciale da aggiungere. Se lo sconto è <code>null</code> o
     *            0 verrà tolto dalle righe se presente
     */
    void aggiornaScontoCommerciale(AreaMagazzino areaMagazzino, BigDecimal importoSconto);

    /**
     * Aggiunge una riga componente ad una rigaDistinta
     *
     * @param idArticolo
     *            idArticolo
     * @param qta
     *            qta per il componente
     * @param rigaDistinta
     *            distinta alla quale aggiungere
     * @return riga aggiunta
     */
    RigaArticoloComponente aggiungiRigaComponente(Integer idArticolo, double qta, RigaArticolo rigaDistinta);

    /**
     * Applica la lista delle regole di validazione a tutte le righe articolo delle aree magazzino.
     *
     * @param parametriRegoleValidazioneRighe
     *            parametri di validazione
     * @return lista di righe che non sono valide
     */
    List<RigaArticoloLite> applicaRegoleValidazione(ParametriRegoleValidazioneRighe parametriRegoleValidazioneRighe);

    /**
     * Calcola la politica prezzo della riga di riferimento.
     *
     * @param rigaArticolo
     *            riga articolo
     * @param codicePagamento
     *            codice pagamento per la percentuale sconto commerciale
     * @return politica prezzo
     */
    PoliticaPrezzo calcolaPoliticaPrezzo(RigaArticolo rigaArticolo, CodicePagamento codicePagamento);

    /**
     * Calcola la politicaPrezzo per un articolo.
     *
     * @param parametriCalcoloPrezzi
     *            parametri per il calcolo dei prezzi
     * @return {@link PoliticaPrezzo} con il prezzo calcolato
     */
    PoliticaPrezzo calcolaPrezzoArticolo(ParametriCalcoloPrezzi parametriCalcoloPrezzi);

    /**
     * Carica il codice iva associato al tipo omaggio scelto. Se il tipoOmaggio non deve cambiare il
     * codice iva ritorna il codice dell'articolo.
     *
     *
     * @param rigaArticolo
     *            rigaArticolo di cui caricare il codice iva in relazione all'omaggio. Se
     *            TipoOmaggio è null viene impostato su TipoOmaggio.NESSUNO
     * @return CodiceIva associato al tipo omaggio
     * @throws CodiceIvaPerTipoOmaggioAssenteException
     *             il codice iva per il tipo omaggio non è definito
     */
    CodiceIva caricaCodiceIvaPerSostituzione(IRigaArticoloDocumento rigaArticolo)
            throws CodiceIvaPerTipoOmaggioAssenteException;

    /**
     *
     * @param righeMagazzinoDaCambiare
     *            righe da collegare all'ultima testata inserita
     */
    void collegaTestata(Set<Integer> righeMagazzinoDaCambiare);

    /**
     * Crea una riga note automatica per l'area magazzino indicata.
     *
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @param note
     *            note
     * @return <code>true</code> se la riga viene generata e salvata correttamente
     */
    boolean creaRigaNoteAutomatica(AreaMagazzino areaMagazzino, String note);

    /**
     *
     * @return dao generico per la gestione delle righe magazzino.
     */
    RigaMagazzinoDAO getDao();

    /**
     *
     * @param parametriCreazioneRigaArticolo
     *            parametriCreazioneRigaArticolo parametri per determinare il dao che gestisce la
     *            riga
     * @return dao specifico per la gestione della rigaMagazzino(che può essere un
     *         componente,distinta,padre,normale)
     */
    RigaMagazzinoDAO getDao(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

    /**
     *
     * @param rigaMagazzino
     *            riga magazzino da gestire con il dao richiesto
     * @return dao specifico per la gestione della rigaMagazzino(che può essere un
     *         componente,distinta,padre,normale)
     */
    RigaMagazzinoDAO getDao(RigaMagazzino rigaMagazzino);

    /**
     *
     * Crea e aslva le righe di magazzino per il raggruppamento voluto.
     *
     * @param idAreaMagazzino
     *            area magazzino alla quale associare le nuove righe
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
     *            coedice iva da usare sulla riga articolo, se null viene usato il codice iva
     *            dell'articolo
     * @param idTipoMezzo
     *            = id del tipo mezzo
     * @param idZonaGeografica
     *            id della zona geografica
     * @param noteSuDestinazione
     *            imposta se stampare le note riga sul documento di destinazione
     * @param tipoMovimento
     *            tipo movimento
     * @param codiceValuta
     *            codice della valuta di riferimento
     * @param codiceLingua
     *            codice della lingua di riferimento
     * @param tipologiaCodiceIvaAlternativo
     *            tipologia di un eventuale codice iva alternativo
     * @param percentualeScontoCommerciale
     *            percentuale sconto commerciale
     * @param idAgente
     *            id agente
     * @throws RimanenzaLottiNonValidaException
     *             rilanciata se il lotto non ha più quantità disponibili
     * @throws RigheLottiNonValideException
     *             rilanciata se le righe lotto della riga magazzino non sono valide
     * @throws QtaLottiMaggioreException
     *             rilanciata se la quantità assegnata ai lotti supera la quantità della riga
     *             articolo
     */
    void inserisciRaggruppamentoArticoli(Integer idAreaMagazzino, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, TipoMovimento tipoMovimento, String codiceValuta,
            String codiceLingua, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale)
                    throws RimanenzaLottiNonValidaException, RigheLottiNonValideException, QtaLottiMaggioreException;

    /**
     * Ricalcola il prezzo della riga articolo.
     *
     * @param rigaArticolo
     *            riga articolo
     * @param codicePagamento
     *            codice pagamento per la percentuale sconto commerciale
     * @return RigaArticolo
     */
    RigaArticolo ricalcolaPrezziRigaArticolo(RigaArticolo rigaArticolo, CodicePagamento codicePagamento);

}