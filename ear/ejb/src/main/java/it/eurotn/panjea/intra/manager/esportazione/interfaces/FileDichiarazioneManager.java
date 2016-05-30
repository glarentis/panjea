package it.eurotn.panjea.intra.manager.esportazione.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface FileDichiarazioneManager {
	/**
	 * 
	 * @param idFileDichiarazione
	 *            .
	 * @param nome
	 *            .
	 * @return .
	 */
	FileDichiarazione aggiornaNomeFileDichiarazione(int idFileDichiarazione, String nome);

	/**
	 * 
	 * @param fileDichiarazione
	 *            {@link FileDichiarazione} da cancellare
	 */
	void cancellaFileDichiarazioni(FileDichiarazione fileDichiarazione);

	/**
	 * 
	 * @param id
	 * @return file delle dichiarazioni generati
	 */
	List<FileDichiarazione> caricaFileDichiarazioni();

	/**
	 * 
	 * @param dichiarazioni
	 *            lista di dichiarazioni da includere nel file
	 * @param salvaRisultati
	 *            true salva i risultati,false genera solamente il file.
	 * @return file scambi.cee
	 * @throws PreferenceNotFoundException
	 *             rilanciata se non trova la preferenza dirTemplate
	 */
	FileDichiarazione generaFileEsportazione(List<Integer> dichiarazioni, boolean salvaRisultati)
			throws PreferenceNotFoundException;

	/**
	 * spedisce il file della dichiarazione precedentemente generato.
	 * 
	 * @param fileDichiarazione
	 *            fileDichiarazione da spedire
	 */
	void spedisciFileEsportazione(FileDichiarazione fileDichiarazione);

	/**
	 * Genera e spedisce il file della dichiarazione.
	 * 
	 * @param dichiarazioni
	 *            dichiarazioni da spedire
	 * @throws PreferenceNotFoundException
	 *             rilanciata se non trova la preferenza dirTemplate
	 */
	void spedisciFileEsportazione(List<Integer> dichiarazioni) throws PreferenceNotFoundException;
}
