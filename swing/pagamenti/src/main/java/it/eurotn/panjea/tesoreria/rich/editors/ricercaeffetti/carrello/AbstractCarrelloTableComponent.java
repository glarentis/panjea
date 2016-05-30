package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.carrello;

import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;

import java.util.List;

import org.springframework.richclient.factory.AbstractControlFactory;

public abstract class AbstractCarrelloTableComponent extends AbstractControlFactory {

	/**
	 * Aggiunge una lista di situazioni effetto.
	 * 
	 * @param situazioneEffetti
	 *            lista da aggiungere
	 * @param tesoreriaBD
	 *            tesoreriaBD
	 */
	abstract void addSituazioneEffetti(List<SituazioneEffetto> situazioneEffetti, ITesoreriaBD tesoreriaBD);

	/**
	 * Genera le aree tesoreria degli effetti gestiti.
	 * 
	 * @param tesoreriaBD
	 *            tesoreriaBD
	 * 
	 * @return <code>false</code> se non è possibile generare le aree
	 */
	abstract boolean generaAreeTesoreria(ITesoreriaBD tesoreriaBD);

	/**
	 * Restituisce tutte le {@link SituazioneEffetto} gestite.
	 * 
	 * @return lista di {@link SituazioneEffetto}
	 */
	abstract List<SituazioneEffetto> getSituazioniEffetti();

	/**
	 * Cancella tutta la lista di situazioni effetti.
	 */
	abstract void removeAll();

	/**
	 * Cancella tutti gli oggetti selezionati.
	 */
	abstract void removeSelected();

}
