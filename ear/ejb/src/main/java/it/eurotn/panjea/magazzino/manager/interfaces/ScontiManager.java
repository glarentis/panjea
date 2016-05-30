/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.service.exception.ScontoNotValidException;

import java.util.List;

import javax.ejb.Local;

/**
 * Interfaccia che offre tutti i metodi per eseguire il CRUD sulla classe <code>Sconto</code>.
 * 
 * @author fattazzo
 * 
 */
@Local
public interface ScontiManager {

	/**
	 * Cancella uno <code>Sconto</code>.
	 * 
	 * @param sconto
	 *            <code>Sconto</code> da cancellare
	 */
	void cancellaSconto(Sconto sconto);

	/**
	 * carica tutti gli sconti dell'azienda loggata.
	 * 
	 * @return <code>List</code> di <code>Sconto</code> caricati
	 */
	List<Sconto> caricaSconti();

	/**
	 * Carica uno <code>Sconto</code>.
	 * 
	 * @param sconto
	 *            <code>Sconto</code> da caricare
	 * @return <code>Sconto</code> caricato.
	 */
	Sconto caricaSconto(Sconto sconto);

	/**
	 * Ricerca tutti gli sconti il cui codice inizia con il parametro passato.<br>
	 * Se il parametro è uguale a <code>null</code> vengono restituiti tutti gli sconti dell'azienda loggata, altrimenti
	 * verrà appunto eseguita una like sul codice sconto.
	 * 
	 * @param codiceSconto
	 *            Valore sul quale eseguire la like nella ricerca
	 * @return <code>List</code> di <code>Sconto</code> trovati
	 */
	List<Sconto> ricercaSconti(String codiceSconto);

	/**
	 * Salva uno <code>Sconto</code>. Il metodo esegue anche il controllo relativo agli sconti inseriti. Non è possibile
	 * inserire uno sconto se quello precedente non è stato settato. ( Esempio: non posso inserire lo sconto2 se sconto1
	 * non ha valore ). In questo caso il metodo solleva un'eccezione.
	 * 
	 * @param sconto
	 *            <code>Sconto</code> da salvare
	 * @return <code>Sconto</code> salvato
	 * @throws ScontoNotValidException
	 *             sollevata se gli sconti non sono validi
	 */
	Sconto salvaSconto(Sconto sconto) throws ScontoNotValidException;
}
