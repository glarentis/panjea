package it.eurotn.panjea.bi.manager.interfaces;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.domain.dashboard.DashBoardAnalisi;

import java.util.List;

public interface DashBoardManager {
	/**
	 * Aggiorna i dati dell'analisi a cui fa riferimento la {@link DashBoardAnalisi}.
	 *
	 * @param nomeAnalisiOld
	 *            vecchio nome dell'analisi
	 * @param categoriaAnalisiOld
	 *            vecchia categoria dell'analisi
	 * @param nomeAnalisiNew
	 *            nuovo nome dell'analsi
	 * @param categoriaAnalisiNew
	 *            nuova categoria dell'analisi
	 */
	void aggiornaDashBoardAnalisi(String nomeAnalisiOld, String categoriaAnalisiOld, String nomeAnalisiNew,
			String categoriaAnalisiNew);

	/**
	 * Cancella la dashBoard cone nome nomeDashBoard.
	 *
	 * @param nomeDashBoard
	 *            nome della dashBoard da cancellare
	 */
	void cancellaDashBoard(String nomeDashBoard);

	/**
	 * Cancella tutte le {@link DashBoardAnalisi} che fanno riferimento all'analisi.
	 *
	 * @param analisiBi
	 *            analisi
	 */
	void cancellaDashBoardAnalisi(AnalisiBi analisiBi);

	/**
	 * Carica tutte le {@link DashBoard} che contengono almento una {@link DashBoardAnalisi} che si riferisce
	 * all'analisi passata come parametro.
	 *
	 * @param analisiBi
	 *            analisi di riferimento
	 * @return {@link DashBoard} caricate
	 */
	List<DashBoard> caricaDashBoard(AnalisiBi analisiBi);

	/**
	 *
	 * @param nomeDashBoard
	 *            nome della dashboard da caricare, se non esiste nome per la nuova dashboard
	 * @return dashboard caricata, se non esiste viene craeta una nuova dashboard
	 */
	DashBoard caricaDashBoard(String nomeDashBoard);

	/**
	 *
	 * @return lista di dashboard presenti per l'utente loggato
	 */
	List<DashBoard> caricaListaDashBoard();

	/**
	 *
	 * @param dashBoard
	 *            salva i dati di una dashboard
	 * @return dashboard salvata
	 */
	DashBoard salvaDashBoard(DashBoard dashBoard);
}
