package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AreaEffettiManager extends IAreaTesoreriaDAO {

	/**
	 * Cancella uno specifico effetto. Ritotalizza il documento e ricrea l'area contabile.
	 * 
	 * @param effetto
	 *            da cancellare
	 */
	void cancellaEffetto(Effetto effetto);

	/**
	 * Carica il numero delle aree che puntato alla {@link AreaEffetti} passata come parametro.
	 * 
	 * @param areaEffetti
	 *            area effetti
	 * @return numero di arre
	 */
	Long caricaNumeroAreeCollegate(AreaEffetti areaEffetti);

	/**
	 * Crea l' area effetti specificata dai parametri. E' richiamato da AreaChiusureMenager.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            parametri di creazione
	 * @param pagamenti
	 *            pagamenti
	 * @return la lista delle areeEffetti
	 * @throws DocumentoDuplicateException
	 *             eccezione
	 */
	AreaEffetti creaAreaEffetti(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure, List<Pagamento> pagamenti)
			throws DocumentoDuplicateException;

	/**
	 * Ricerca gli effetti, parametri ricerca definiscono i limiti, risalendo dai pagamenti.
	 * 
	 * @param parametriRicercaEffetti
	 *            limiti della ricerca
	 * @return lista degli effetti
	 */
	List<SituazioneEffetto> ricercaEffetti(ParametriRicercaEffetti parametriRicercaEffetti);

	/**
	 * Salva un {@link Effetto}.
	 * 
	 * @param effetto
	 *            effetto da salvare
	 * @return effetto salvato
	 */
	Effetto salvaEffetto(Effetto effetto);

}
