package it.eurotn.panjea.bi.manager.interfaces;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.sql.detail.RigaDettaglioAnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.domain.dashboard.DashBoardAnalisi;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;
import it.eurotn.panjea.magazzino.exception.AnalisiPresenteInDashBoardException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

/**
 * Gestisce la persistenza delle analisi di business intelligence.
 *
 * @author giangi
 *
 */
@Local
public interface AnalisiBIManager {

	/**
	 * cancella un analisi.
	 *
	 * @param analisiBI
	 *            analisi da cancellare
	 * @param removeFromDashboard
	 *            se <code>true</code> rimuove dalle {@link DashBoard} tutte le {@link DashBoardAnalisi} che fanno
	 *            riferimento all'analisi da cancellare * @throws AnalisiPresenteInDashBoardException rilanciata se
	 *            l'analisi è presente in qualche {@link DashBoardAnalisi} e non ne è stata forzata la rimozione
	 * @throws AnalisiPresenteInDashBoardException
	 *             rilanciata se l'analisi è presente in qualche {@link DashBoard}
	 * @throws AnalisiNonPresenteException
	 *             rilanciata se l'analisi non esiste
	 */
	void cancellaAnalisi(AnalisiBi analisiBI, boolean removeFromDashboard) throws AnalisiPresenteInDashBoardException,
			AnalisiNonPresenteException;

	/**
	 * Carica i dati di analisi dato il nome dell'analisi.
	 *
	 * @param nomeAnalisi
	 *            nome dell'analisi da caricare
	 * @param categoriaAnalisi
	 *            categoria dell'analisi
	 * @return dati dell'analisi:disposizione colonne (layout pivotTablePan), layout delle pagine e nome dell'analisi
	 * @throws AnalisiNonPresenteException
	 *             rilanciata se l'analisi non esiste
	 */
	AnalisiBi caricaAnalisi(String nomeAnalisi, String categoriaAnalisi) throws AnalisiNonPresenteException;

	/**
	 * Carica il nome di tutte le liste delle analisi.<br/>
	 * Il metodo carica solamente il nome e la categoria. I campi Blob non vengono caricati.
	 *
	 * @return Lista di array di stringa dove 0=nome e 1=categoria
	 */
	List<AnalisiBi> caricaListaAnalisi();

	/**
	 *
	 * @param colonna
	 *            colonna dell'analisi
	 * @return valori contenuti nella colonna
	 */
	Set<Object> caricaValoriPerColonna(Colonna colonna);

	/**
	 *
	 * @param analisi
	 *            da copiare
	 */
	void copiaAnalisi(AnalisiBi analisi);

	/**
	 *
	 * @param analisi
	 *            modello dell'analisi
	 * @param page
	 *            pagina da caricare
	 * @param detailFilter
	 *            filtri da applicare al dettaglio in aggiunta a quelli presenti nell'analisi
	 * @param colonnaMisura
	 *            misura di riferimento
	 * @param sizeOfPage
	 *            dimensioni della pagina
	 * @return righe del dettaglio contenute nella pagina
	 */
	List<RigaDettaglioAnalisiBi> drillThrough(AnalisiBi analisi, Map<Colonna, Object[]> detailFilter,
			Colonna colonnaMisura, int page, int sizeOfPage);

	/**
	 * Esegue l'analisi e ne restituisce i risultati .
	 *
	 * @param analisiBi
	 *            modello con i parametri impostati per caricare i dati (colonne e filtri da utilizzare)
	 * @return risultati
	 */
	AnalisiBIResult eseguiAnalisi(AnalisiBi analisiBi);

	/**
	 * Salva un analisi.
	 *
	 * @param analisiBI
	 *            parametri dell'analisi
	 * @return analisi salvata
	 */
	AnalisiBi salvaAnalisi(AnalisiBi analisiBI);
}
