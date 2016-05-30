package it.eurotn.panjea.anagrafica.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.ChartLayout;
import it.eurotn.panjea.anagrafica.domain.DockedLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;

import java.util.List;

import javax.ejb.Local;

@Local
public interface LayoutManager {

	/**
	 * Cancella un layout.
	 * 
	 * @param layout
	 *            layout da cancellare
	 */
	void cancella(AbstractLayout layout);

	/**
	 * Carica tutti i layout dei chart in base alla chiave.
	 * 
	 * @param key
	 *            chiave di riferimento
	 * @return layouts caricati
	 */
	List<ChartLayout> caricaChartLayout(String key);

	/**
	 * Carica il layout di default.
	 * 
	 * @param key
	 *            chave del docked
	 * @return layout caricato, <code>null</code> se non esiste
	 */
	DockedLayout caricaDefaultDockedLayout(String key);

	/**
	 * Carica il layout dell'editor. Viene caricato il layout dell'utente, se non viene trovato si carica quello di
	 * default.
	 * 
	 * @param key
	 *            chiave del docked
	 * @return layout caricato, layout caricato, <code>null</code> se non esiste
	 */
	DockedLayout caricaDockedLayout(String key);

	/**
	 * Carica tutti i layout tabella in base alla chiave.
	 * 
	 * @param key
	 *            chiave di riferimento
	 * @return layouts caricati
	 */
	List<TableLayout> caricaTableLayout(String key);

	/**
	 * Salva il layout.
	 * 
	 * @param layout
	 *            layout da salvare
	 * @return layout salvato
	 */
	AbstractLayout salva(AbstractLayout layout);
}
