/**
 *
 */
package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.exception.RigheListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.util.RigaListinoCalcolo;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface ListinoManager {

	/**
	 * Cancella un listino, le sue versioni e le relative righe.
	 *
	 *
	 * @param listino
	 *            listino da cancellare.
	 */
	void cancellaListino(Listino listino);

	/**
	 * cancella una riga listino.
	 *
	 * @param rigaListino
	 *            riga da cancellare
	 */
	void cancellaRigaListino(RigaListino rigaListino);

	/**
	 * Cancella delle righe da un listino.
	 *
	 * @param righeListino
	 *            righe da cancellare
	 */
	void cancellaRigheListino(List<RigaListino> righeListino);

	/**
	 * Cancella la <code>VersioneListino</code> e quindi le righe listino associate.
	 *
	 * @param versioneListino
	 *            versione da cancellare
	 */
	void cancellaVersioneListino(VersioneListino versioneListino);

	/**
	 * Carica l'importo della riga listino in base all'articolo e listino. La versione listino considerata è quella in
	 * vigore alla data attuale. L'importo ha la scale con il numero di decimali preso dalla riga listino.
	 *
	 * @param idListino
	 *            id listino
	 * @param idArticolo
	 *            id articolo
	 * @return importo della riga con il numero di decimali della riga listino, <code>null</code> se non esiste
	 */
	BigDecimal caricaImportoListino(Integer idListino, Integer idArticolo);

	/**
	 * Carica tutti i listini relativi all'azienda. Le righe listino non vengono inizializzate.
	 *
	 * @return listini azienda.i
	 */
	List<Listino> caricaListini();

	/**
	 * Carica tutti i listini relativi all'azienda. Le righe listino non vengono inizializzate.
	 *
	 * @param tipoListino
	 *            tipo listino da caricare. Se <code>null</code> venono caricati tutti i listini
	 * @param searchField
	 *            il campo su cui eseguire la ricerca ( codice o descrizione )
	 * @param searchValue
	 *            valore per la ricerca ( codice o descrizione )
	 * @return listini azienda.i
	 */
	List<Listino> caricaListini(ETipoListino tipoListino, String searchField, String searchValue);

	/**
	 * Carica un listino. Il parametro <code>initializeRighe</code> indica se inizializzare le collezioni del listino
	 * perchè di default sono lazy.
	 *
	 * @param listino
	 *            listino da caricare
	 * @param initializeLazy
	 *            inizializza le versioni e le righe
	 * @return listino caricato
	 */
	Listino caricaListino(Listino listino, boolean initializeLazy);

	/**
	 * Carica la riga listino
	 *
	 * @param idRiga
	 *            id della riga da caricare
	 * @return riga caricata
	 */
	RigaListino caricaRigaListino(Integer idRiga);

	/**
	 * Carica le righe listino appartenenti ad un articolo per ogni listino e versione presente.
	 *
	 * @param idArticolo
	 *            id dell'articolo interessato
	 * @return righe per l'articolo in tutti i listini e versioni presenti
	 */
	List<RigaListinoDTO> caricaRigheListinoByArticolo(Integer idArticolo);

	/**
	 * Carica tutte le righe listino della versione che sono collegate all'articolo passato come riferimento.
	 *
	 * @param versioneListino
	 *            versione del listino di riferimento
	 * @param idArticolo
	 *            articolo da ricercare
	 * @return <code>List</code> di <code>RigaListino</code> trovate nella versione
	 */
	List<RigaListino> caricaRigheListinoByArticolo(VersioneListino versioneListino, Integer idArticolo);

	/**
	 * Carica le righe di un listino data una versione.
	 *
	 * @param versioneListino
	 *            versione del listino. deve avere l'id avvalorato.
	 * @return righe della versione
	 */
	List<RigaListinoDTO> caricaRigheListinoByVersione(VersioneListino versioneListino);

	/**
	 *
	 * @param data
	 *            data per recuperare la versione interessata
	 * @param articoli
	 *            articoli interessati
	 * @return lista di tutte le righe per gli articoli interessati appartenenti a tutti i listini (con la versione
	 *         valida nella data richiesta) che posso essere aggiornate all'ultimo costo
	 */
	List<RigaListino> caricaRigheListinoDaAggiornare(Date data, List<ArticoloLite> articoli);

	/**
	 * Carica solo le righe listino relative all'articolo passato solo per la versione valida per la data di riferimento
	 * di quel listino. <b>NB</b>Le righe listino contengono solamente i dati utili per il calcolo del prezzo. Metodo
	 * implementato per aumentare le performance dello use case calcolo prezzo.
	 *
	 * @param listino
	 *            Listino di riferimento
	 * @param data
	 *            Data di riferimento
	 * @param idArticolo
	 *            filtra le righe solamente per l'articolo interessato
	 * @return Righe del listino per l'articolo righiesto contenenti i dati utili per il calcolo del prezzo.
	 */
	List<RigaListinoCalcolo> caricaRigheListinoPrezzoCalculator(Listino listino, Date data, Integer idArticolo);

	/**
	 *
	 * @param listino
	 *            listino o lisino alternativo da caricare
	 * @return sediMagazzino legate al listino.
	 */
	List<RiepilogoSedeEntitaDTO> caricaSediMagazzinoByListino(Listino listino);

	/**
	 * Carica lo storico dello scaglione di riferimento.
	 *
	 * @param scaglioneListino
	 *            scaglione
	 * @param numeroVersione
	 *            se presente verranno filtrati gli storici della versione
	 * @return storico caricato
	 */
	List<ScaglioneListinoStorico> caricaStoricoScaglione(ScaglioneListino scaglioneListino, Integer numeroVersione);

	/**
	 * Utilizzato dal report per carivare la versione.
	 *
	 * @see ListinoManager#caricaVersioneListino(VersioneListino, boolean)
	 * @param parametri
	 *            parametri per il metodo ListinoManager#caricaVersioneListino(VersioneListino, boolean)
	 * @return versione caricata
	 */
	VersioneListino caricaVersioneListino(Map<Object, Object> parametri);

	/**
	 * Carica una <code>VersioneListino</code>.
	 *
	 * @param versioneListino
	 *            versione da caricare
	 * @param initializeLazy
	 *            <code>true</code> inizializza anche tutte le collezioni che di default sono lazy.
	 * @return versione caricata
	 */
	VersioneListino caricaVersioneListino(VersioneListino versioneListino, boolean initializeLazy);

	/**
	 * Cerca tra le versioni listino quella valida per la data fornita.
	 *
	 * @param listino
	 *            <code>Listino</code> di riferimento
	 * @param data
	 *            Data per la ricerca della versione
	 * @return <code>VersioneListino</code> trovata, <code>null</code> se non esiste una versione valida
	 */
	VersioneListino caricaVersioneListinoByData(Listino listino, Date data);

	/**
	 * Carica tutte le versioni listino presenti per l'azienda corrente.
	 *
	 * @param valueSearch
	 *            valore da ricercare
	 * @param fieldSearch
	 *            campo da cercare
	 *
	 * @param tipoListino
	 *            tipo di listino da caricare
	 * @return List<VersioneListino>
	 */
	List<VersioneListino> caricaVersioniListino(String fieldSearch, String valueSearch, ETipoListino tipoListino);

	/**
	 * Crea una nuova <code>VersioneListino</code> da una già esistente. La copia si incarica di copiare anche tutte le
	 * eventuali righe listino della versione di riferimento.
	 *
	 * @param versioneListino
	 *            Versione da copiare
	 * @param dataNuovaVersioneListino
	 *            data entrata in vigore della nuova versione listino
	 * @return Nuova copia della versione creata
	 */
	VersioneListino copiaVersioneListino(VersioneListino versioneListino, Date dataNuovaVersioneListino);

	/**
	 * Salva un listino.
	 *
	 * @param listino
	 *            listino da salvare
	 * @return listino salvato.
	 */
	Listino salvaListino(Listino listino);

	/**
	 * Salva il prezzo di una lista di righe listino eseguendo sempre il controllo sulla quantità della riga sulla base
	 * della tipologia del listino.
	 *
	 * @param listRigheListino
	 *            righe da salvare
	 * @return righe salvate
	 * @throws RigheListinoListiniCollegatiException
	 *             sollevata se ci sono righe listino da salvare che fanno parte di listini base
	 */
	List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino)
			throws RigheListinoListiniCollegatiException;

	/**
	 * Salva il prezzo di una lista di righe listino eseguendo sempre il controllo sulla quantità della riga sulla base
	 * della tipologia del listino.
	 *
	 * @param listRigheListino
	 *            righe da salvare
	 * @param aggiornaListiniCollegati
	 *            aggiorna il prezzo sui listini collegati
	 * @return righe salvate
	 */
	List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino, boolean aggiornaListiniCollegati);

	/**
	 * Salva una riga listino. Se il listino della riga è di tipo normale la riga non potrà essere salvata se la
	 * quantità non è uguale a 0 e viene rilanciata una eccezione.
	 *
	 * @param rigaListino
	 *            riga da salvare
	 * @return rigaListino salvata
	 * @throws RigaListinoListiniCollegatiException
	 *             sollevata se alla riga listino sono configurati dei listini collegati
	 */
	RigaListino salvaRigaListino(RigaListino rigaListino) throws RigaListinoListiniCollegatiException;

	/**
	 * Salva una riga listino. Se il listino della riga è di tipo normale la riga non potrà essere salvata se la
	 * quantità non è uguale a 0 e viene rilanciata una eccezione.
	 *
	 * @param rigaListino
	 *            riga da salvare
	 * @param aggiornaListiniCollegati
	 *            aggiorna il prezzo sui listini collegati
	 * @return rigaListino salvata
	 */
	RigaListino salvaRigaListino(RigaListino rigaListino, boolean aggiornaListiniCollegati);

	/**
	 * Salva un {@link ScaglioneListinoStorico}.
	 *
	 * @param scaglioneListinoStorico
	 *            scaglione da salvare
	 * @return scaglione salvato
	 */
	ScaglioneListinoStorico salvaScaglioneListinoStorico(ScaglioneListinoStorico scaglioneListinoStorico);

	/**
	 * Salva una versione listino. Se la versione da salvare è nuova viene assegnato l'ultimo codice valido per il
	 * listino.
	 *
	 * @param versioneListino
	 *            versione da salvare
	 * @return versione salvata
	 */
	VersioneListino salvaVersioneListino(VersioneListino versioneListino);
}
