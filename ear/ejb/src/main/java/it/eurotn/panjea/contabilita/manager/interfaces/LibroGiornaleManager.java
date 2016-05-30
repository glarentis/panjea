/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.Giornale;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.GiornaliNonValidiException;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * Manager per la gestione del giornale.
 * 
 * @author fattazzo
 * @version 1.0, 25/set/07
 * 
 */
@Local
public interface LibroGiornaleManager {

	/**
	 * Carica le note associate al giornale.<br/>
	 * Quando viene invalidato un giornale si inseriscono i motivi dell'invalidazione come note.<br/>
	 * <br/>
	 * Le note vengono create dagli eventi sul giornale e sono di sola lettura.
	 * 
	 * @param idGiornale
	 *            id del giornale interessato
	 */
	void cancellaNoteGiornale(Integer idGiornale);

	/**
	 * Carica il giornale del mese e anno richiesto.
	 * 
	 * @param anno
	 *            anno del giornare
	 * @param mese
	 *            mese del giornale
	 * @return giornale del mese richiesto. NULL se non viene trovato il giornale
	 * @throws ContabilitaException
	 *             rilanciato se avviene un errore generico.
	 */
	Giornale caricaGiornale(Integer anno, Integer mese) throws ContabilitaException;

	/**
	 * Carica il giornale precedente a quello attuale.
	 * 
	 * @param giornaleAttuale
	 *            giornale di riferimento.
	 * @return <code>Giornale</code> precedente se esiste, <code>NULL</code> altrimenti.<br/>
	 *         Se il giornale e' il primo dell'anno contabile ritorna null.
	 */
	Giornale caricaGiornalePrecedente(Giornale giornaleAttuale);

	/**
	 * Carica tutti i giornali dell'azienda per l'anno selezionato.<br/>
	 * Se non esistono tutti i giornali per l'anno vengono creati i giornali mancanti con stato non valido.<br>
	 * .
	 * 
	 * @param annoCompetenza
	 *            anno di competenza per i giornali
	 * @return lista dei giornali. Ritorna sempre una lista di 12 giornali.
	 * @throws ContabilitaException
	 *             rilanciato se avviene un errore generico.
	 */
	List<Giornale> caricaGiornali(int annoCompetenza) throws ContabilitaException;

	/**
	 * Carica il giornale successivo a quello attuale.
	 * 
	 * @param giornaleAttuale
	 *            giornale di riferimento.
	 * @return <code>Giornale</code> successivo se esiste, <code>NULL</code> altrimenti.<br/>
	 * @throws ContabilitaException
	 *             rilanciato se avviene un errore generico.
	 */
	List<Giornale> caricaGiornaliSuccessivi(Giornale giornaleAttuale) throws ContabilitaException;

	/**
	 * Invalida il libro giornale e i successivi del mese in cui rientra il documento usato per l'invalidazione nel caso
	 * in cui non viene modificata la testata del documento ma solo lo stato.
	 * 
	 * @param areaContabile
	 *            l'area contabile da cui trovare la data per l'invalidazione giornale
	 * @throws ContabilitaException
	 *             rilanciato se avviene un errore generico.
	 */
	void invalidaLibroGiornale(AreaContabile areaContabile) throws ContabilitaException;

	/**
	 * Invalida il libro giornale dalla data più lontana tra la data registrazione e la data registrazione precedente e
	 * i successivi giornali, usato nel caso in cui si modifica la data di registrazione, dove bisogna invalidare il
	 * giornale nella nuova data di registrazione, ma anche nella data di registrazione originale prima della modifica.
	 * Non modifica lo stato dei documenti che rientrano in quel giornale.
	 * 
	 * @param areaContabile
	 *            l'area contabile da cui trovare la data per l'invalidazione giornale
	 * @param dataRegistrazionePrecedente
	 *            la data di registrazione prima della modifica della data di registrazione
	 * @throws ContabilitaException
	 *             rilanciato se avviene un errore generico.
	 */
	void invalidaLibroGiornale(AreaContabile areaContabile, Date dataRegistrazionePrecedente)
			throws ContabilitaException;

	/**
	 * Salva il giornale.
	 * 
	 * @param giornale
	 *            giornale da salvare
	 * @return giornale salvato.
	 */
	Giornale salvaGiornale(Giornale giornale);

	/**
	 * Sfoglia i giornali e verifica che il loro stato sia valido.
	 * 
	 * @param annoEsercizio
	 *            anno contabile per la chiusura
	 * @throws GiornaliNonValidiException
	 *             lanciata quando ci sono giornali non validi all'interno dell'anno di esercizio
	 */
	void verificaStatoGiornali(Integer annoEsercizio) throws GiornaliNonValidiException;
}
