package it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.AreaMagazzinoFatturazione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface PreparaFatturazioneManager {

	/**
	 * Cancella i movimenti in fatturazione creati da una fatturazione differita temporanea dell'utente specificato.
	 *
	 * @param utente
	 *            utente di riferimento
	 */
	void cancellaMovimentiInFatturazione(String utente);

	/**
	 * Carica le aree magazzino da fatturare. Vengono caricati solo i dati che servono creando oggetti di tipo
	 * {@link AreaMagazzinoFatturazione}.
	 *
	 * @param areeDaFatturare
	 *            aree da fatturare
	 * @return aree caricate
	 */
	List<AreaMagazzinoFatturazione> caricaAreeMagazzinoFatturazione(List<AreaMagazzinoLite> areeDaFatturare);

	/**
	 * Ordina i documenti in fatturazione dell'utente secondo l'ordinamento scelto.
	 *
	 * @param utente
	 *            utente
	 */
	void ordinaFatturazione(String utente);

}
