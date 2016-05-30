package it.eurotn.panjea.anagrafica.manager.rapportibancarisedeentita.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

@Local
public interface RapportiBancariSedeEntitaManager {

    /**
     * Cancella il rapporto bancario associato alla sede do un entità.
     *
     * @param rapportoBancario
     *            rapporto bancario da cancellare
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    void cancellaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancario)
            throws AnagraficaServiceException;

    /**
     * Carica i rapporti bancari per la sede specificata.
     *
     * @param idSedeEntita
     *            id della sede di riferimento
     * @return rapporti bancari caricati
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita)
            throws AnagraficaServiceException;

    /**
     * Carica i rapporti bancari per la sede specificata ignorando o considerando l'ereditarietà delle sedi.
     *
     * @param idSedeEntita
     *            id della sede di riferimento
     * @param ignoraEredita
     *            <code>true</code> considera l'ereditarietà
     * @return rapporti bancari caricati
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita, boolean ignoraEredita)
            throws AnagraficaServiceException;

    /**
     * Carica i rapporti bancari per l'entità specificata.
     *
     * @param valueSearch
     *            valore fa filtrare
     * @param fieldSearch
     *            campo da filtrare
     *
     * @param idEntita
     *            id dell'entità di riferimento
     * @return rapporti bancari caricati
     */
    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntitaPricipale(String fieldSearch, String valueSearch,
            Integer idEntita);

    /**
     * Il metodo restituisce il {@link RapportoBancarioSedeEntita} in funzione del {@link TipoPagamento}<br>
     * La logica di reperimento e': se il tipoPagamento non e' trovato sui rapporti della sede passata <br>
     * si carica la sede principale dell'entita' e si cerca sui rapporti di questa; se non si trova si restitusce il
     * primo di questa sede.
     *
     * @param tipoPagamento
     *            tipo pagamento
     * @param sedeEntita
     *            sede entita
     * @param idEntita
     *            id entità di riferimento
     * @return rapporto bancario caricato
     */
    RapportoBancarioSedeEntita caricaRapportoBancarioPerTipoPagamentoDefault(TipoPagamento tipoPagamento,
            SedeEntita sedeEntita, Integer idEntita);

    /**
     * Carica un {@link RapportoBancarioSedeEntita}.
     *
     * @param idRapportoBancario
     *            id del rapporto da caricare
     * @return rapporto bancario caricato
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    RapportoBancarioSedeEntita caricaRapportoBancarioSedeEntita(Integer idRapportoBancario)
            throws AnagraficaServiceException;

    /**
     * Carica il riepilogo dei dati bancari delle entità.
     *
     * @return riepilogo
     */
    List<RapportoBancarioSedeEntita> caricaRiepilogoDatiBancari();

    /**
     * Salva un {@link RapportoBancarioSedeEntita}.
     *
     * @param rapportoBancarioSedeEntita
     *            rapporto da salvare
     * @return rapporto salvato
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    RapportoBancarioSedeEntita salvaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancarioSedeEntita)
            throws AnagraficaServiceException;

    /**
     * Sostituisce i dati bancari ai {@link RapportoBancarioSedeEntita}.
     *
     * @param rapporti
     *            rapporti bancarii
     * @param banca
     *            banca di sostituzione
     * @param filiale
     *            filiale di sostituzione
     */
    void sostituisciDatiBancari(List<RapportoBancarioSedeEntita> rapporti, Banca banca, Filiale filiale);
}
