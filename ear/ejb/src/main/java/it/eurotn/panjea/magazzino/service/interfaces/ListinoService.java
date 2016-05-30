package it.eurotn.panjea.magazzino.service.interfaces;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.exception.ArticoliDuplicatiManutenzioneListinoException;
import it.eurotn.panjea.magazzino.exception.ListinoManutenzioneNonValidoException;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.exception.RigheListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.util.ConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface ListinoService {

	/**
	 * Dati {@link ParametriAggiornaManutenzioneListino} inserisce aggiorna la versione listino scelta con le sue righe.<br/>
	 * Nota che il valore viene arrotondato al numero decimali scelto a questa fase del processo, cioe' quando viene
	 * aggiornata/inserita la rigaListino.
	 *
	 * @param parametriAggiornaManutenzioneListino
	 *            i parametri per aggiornare/creare la versione listino
	 * @throws ArticoliDuplicatiManutenzioneListinoException
	 *             sollevata se esistono articoli duplicati
	 */
	void aggiornaListinoDaManutenzione(ParametriAggiornaManutenzioneListino parametriAggiornaManutenzioneListino)
			throws ArticoliDuplicatiManutenzioneListinoException;

	void cancellaListino(Listino listino);

	void cancellaRigaListino(RigaListino rigaListino);

	void cancellaRigheListino(List<RigaListino> righeListino);

	void cancellaRigheManutenzioneListino(List<RigaManutenzioneListino> righeManutenzioneListino);

	void cancellaVersioneListino(VersioneListino versioneListino);

	/**
	 * Carica il confronto in base ai parametri specificati.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @return confronto
	 */
	ConfrontoListinoDTO caricaConfrontoListino(ParametriRicercaConfrontoListino parametri);

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
	 *            tipo listino da caricare. Se <code>null</code> vengono caricati tutti i listini
	 * @param searchField
	 *            il campo su cui eseguire la ricerca ( codice o descrizione )
	 * @param searchValue
	 *            valore per la ricerca ( codice o descrizione )
	 * @return listini azienda.i
	 */
	List<Listino> caricaListini(ETipoListino tipoListino, String searchField, String searchValue);

	Listino caricaListino(Listino listino, boolean initializeLazy);

	/**
	 * Carica la riga listino
	 *
	 * @param idRiga
	 *            id della riga da caricare
	 * @return riga caricata
	 */
	RigaListino caricaRigaListino(Integer idRiga);

	List<RigaListinoDTO> caricaRigheListinoByArticolo(Integer idArticolo);

	List<RigaListinoDTO> caricaRigheListinoByVersione(Integer idVersioneListino);

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

	List<RigaManutenzioneListino> caricaRigheManutenzioneListino();

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

	VersioneListino caricaVersioneListino(Map<Object, Object> parametri);

	VersioneListino caricaVersioneListino(VersioneListino versioneListino, boolean initializeLazy);

	VersioneListino caricaVersioneListinoByData(Listino listino, Date data);

	/**
	 * Carica tutte le versioni listino presenti per l'azienda corrente.
	 *
	 * @param valueSearch
	 *            .
	 * @param fieldSearch
	 *            .
	 *
	 * @param tipoListino
	 *            tipo di listino da caricare
	 * @return List<VersioneListino>
	 */
	List<VersioneListino> caricaVersioniListino(String fieldSearch, String valueSearch, ETipoListino tipoListino);

	VersioneListino copiaVersioneListino(VersioneListino versioneListino, Date dataNuovaVersioneListino);

	/**
	 * Dati i {@link ParametriRicercaManutenzioneListino} ricerca e inserisce le {@link RigaManutenzioneListino}
	 * associate ai parametri.
	 *
	 * @param parametriRicercaManutenzioneListino
	 *            ParametriRicercaManutenzioneListino da cui trovare ed inserire le righe
	 * @throws ListinoManutenzioneNonValidoException
	 *             sollevata nel caso in cui il listino dei parametri manutenzione non sia valido con le eventuali righe
	 *             manutenzione già presenti
	 */
	void inserisciRigheRicercaManutenzioneListino(
			ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino)
					throws ListinoManutenzioneNonValidoException;

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

	List<RigaManutenzioneListino> salvaRigaManutenzioneListino(RigaManutenzioneListino rigaManutenzioneListino);

	VersioneListino salvaVersioneListino(VersioneListino versioneListino);
}
