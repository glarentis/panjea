package it.eurotn.panjea.contabilita.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;

@Local
public interface StrutturaContabileManager {

    /**
     * Cancella una {@link ControPartita}.
     * 
     * @param controPartita
     *            <code>ControPartita</code> da cancellare
     */
    void cancellaControPartita(ControPartita controPartita);

    /**
     * Cancella una struttura contabile. Se ci sono delle contro partite legate alla struttura contabile da cancellare
     * vengono eliminate.
     * 
     * @param strutturaContabile
     *            {@link StrutturaContabile} da cancellare
     */
    void cancellaStrutturaContabile(StrutturaContabile strutturaContabile);

    /**
     * Di un tipoAreaContabile cancella tutte le strutture contabili collegate che siano legate al tipoDocumento o
     * all'entita.
     * 
     * @param tipoAreaContabile
     *            il tipo area di cui cancellare la struttura
     */
    void cancellaStrutturaContabile(TipoAreaContabile tipoAreaContabile);

    /**
     * Carica una {@link ControPartita}.
     * 
     * @param controPartita
     *            <code>ControPartita</code> da caricare
     * @return <code>ControPartita</code> caricata
     * @throws ContabilitaException
     *             errore generico contabilita
     */
    ControPartita caricaControPartita(ControPartita controPartita) throws ContabilitaException;

    /**
     * Carica tutte le {@link ControPartita} in base all'area contabile.
     * 
     * @param areaContabile
     *            areaContabile
     * @return List<ControPartita>
     * @throws ContabilitaException
     *             errore generico contabilita
     */
    List<ControPartita> caricaControPartite(AreaContabile areaContabile) throws ContabilitaException;

    /**
     * Carica tutte le {@link ControPartita} della struttura contabile.
     * 
     * @param tipoDocumento
     *            tipoDocumento
     * @return <code>List</code> di <code>ControPartita</code> caricate
     */
    List<ControPartita> caricaControPartite(TipoDocumento tipoDocumento);

    /**
     * Carica tutte le {@link ControPartita} della struttura contabile.
     * 
     * @param tipoDocumento
     *            tipoDocumento
     * @param entita
     *            entita
     * @return <code>List</code> di <code>ControPartita</code> caricate
     */
    List<ControPartita> caricaControPartite(TipoDocumento tipoDocumento, EntitaLite entita);

    /**
     * Carica tutte le {@link ControPartita} in base all'area contabile colcolandone l'importo se è stata specificata la
     * formula.
     * 
     * @param areaContabile
     *            areaContabile
     * @return List<ControPartita>
     * @throws ContabilitaException
     *             errore generico contabilita
     * @throws FormulaException
     *             FormulaException
     */
    List<ControPartita> caricaControPartiteConImporto(AreaContabile areaContabile)
            throws ContabilitaException, FormulaException;

    /**
     * Carica tutte le entità che hanno una struttura contabile personalizzata per il tipo documento richiesto.
     * 
     * @param tipoDocumento
     *            tipo documento
     * 
     * @return entita caricate
     */
    List<EntitaLite> caricaEntitaConStrutturaContabile(TipoDocumento tipoDocumento);

    /**
     * Carica tutte le {@link StrutturaContabile} in base all'area contabile.
     * 
     * @param areaContabile
     *            areaContabile
     * @return List<StrutturaContabile>
     * @throws ContabilitaException
     *             errore generico contabilita
     */
    List<StrutturaContabile> caricaStrutturaContabile(AreaContabile areaContabile) throws ContabilitaException;

    /**
     * Carica la struttura contabile relativa al tipo documento.
     * 
     * @param tipoDocumento
     *            tipoDocumento
     * @return List<StrutturaContabile>
     */
    List<StrutturaContabile> caricaStrutturaContabile(TipoDocumento tipoDocumento);

    /**
     * Carica la struttura contabile relativa al tipo documento e all'entità. Se l'entità è <code>null</code> viene
     * caricata la struttura contabile base del tipo documento.
     * 
     * @param tipoDocumento
     *            tipoDocumento
     * @param entita
     *            entita
     * @param loadDefault
     *            se per l'entità passata non è presente una struttura contabile viene caricata quella di default del
     *            tipo documento
     * @return List<StrutturaContabile>
     * @throws ContabilitaException
     *             exception generica contabilita
     */
    List<StrutturaContabile> caricaStrutturaContabile(TipoDocumento tipoDocumento, EntitaLite entita,
            boolean loadDefault) throws ContabilitaException;

    /**
     * Restituisce l'elenco delle variabili che si possono usare nella contro partita.
     * 
     * @return List<String>
     */
    List<String> caricaVariabiliFormulaControPartite();

    /**
     * Restituisce l'elenco delle variabili che si possono usare nella struttura contabile.
     * 
     * @return List<String>
     */
    List<String> caricaVariabiliFormulaStrutturaContabile();

    /**
     * Crea le righe contabili per l'area contabile basandosi sulla struttura contabile dell'area.
     * 
     * @param areaContabile
     *            areaContabile
     * @param list
     *            list
     * @return List<RigaContabile>
     * @throws ContabilitaException
     *             ContabilitaException
     * @throws ContoRapportoBancarioAssenteException
     *             ContoRapportoBancarioAssenteException
     * @throws ContiEntitaAssentiException
     *             ContiEntitaAssentiException
     * @throws FormulaException
     *             FormulaException
     */
    List<RigaContabile> creaRigheContabili(AreaContabile areaContabile, List<ControPartita> list)
            throws ContabilitaException, FormulaException, ContoRapportoBancarioAssenteException,
            ContiEntitaAssentiException;

    /**
     * Crea tutte le righe contabili automatiche.
     * 
     * @param areaContabile
     *            area di riferimento
     * @param ordinamento
     *            ordinamento di partenza
     */
    void creaRigheContabiliAutomatiche(AreaContabile areaContabile, long ordinamento);

    /**
     * Salva una {@link ControPartita}.
     * 
     * @param controPartita
     *            <code>ControPartita</code> da salvare
     * @return <code>ControPartita</code> salvata
     * @throws FormulaException
     *             FormulaException
     */
    ControPartita salvaControPartita(ControPartita controPartita) throws FormulaException;

    /**
     * Salva una {@link StrutturaContabile}.
     * 
     * @param strutturaContabile
     *            <code>StrutturaContabile</code> da salvare
     * @return <code>StrutturaContabile</code> salvata
     * @throws FormulaException
     *             FormulaException
     */
    StrutturaContabile salvaStrutturaContabile(StrutturaContabile strutturaContabile) throws FormulaException;
}
