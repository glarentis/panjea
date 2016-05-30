package it.eurotn.panjea.ordini.manager.interfaces;

import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

@Local
public interface ImportazioneOrdiniManager {
	/**
	 * Aggiorna i prezzi delle righe importate.
	 * 
	 * @param parametri
	 *            parametri per trovare le righe da aggiornare
	 */
	void aggiornaPrezziDeterminatiOrdiniImportati(ParametriRicercaOrdiniImportati parametri);

	/**
	 * Aggiorna le righe nel backorder inserrendo i link alle tabelle di panjea<br/>
	 * Quando si importa si importa solamente il codice articolo. Questo metodo inserisce l'id articolo e le altre
	 * foreign key.
	 * 
	 * @param parametri
	 *            parametri per l'aggiornamento. viene considerata la provenienza degli ordini
	 */
	void aggiornaRigheOrdineImportate(ParametriRicercaOrdiniImportati parametri);

	/**
	 * 
	 * @param numeroOrdini
	 *            ordini da cancellare
	 */
	void cancellaOrdiniImportati(Collection<String> numeroOrdini);

	/**
	 * 
	 * Cancella tutte le righe degli ordini importati per un agente.
	 * 
	 * @param codiceAgente
	 *            codice dell'agente.
	 */
	void cancellaOrdiniImportatiPerAgente(String codiceAgente);

	/**
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return lista delle righe ordine importate
	 */
	List<RigaOrdineImportata> caricaRigheOrdineImportate(ParametriRicercaOrdiniImportati parametri);

	/**
	 * Verifica che non ci siano ordini con lo stesso codice sia nel backOrder che negli ordini definitivi.<br/>
	 * NB:Nei backOrder si controlla il numero ordine, negli ordini importati il numero ordine del riferimento del
	 * cliente
	 * 
	 * @param numeroOrdine
	 *            numeroOrdine da verificare
	 * @param codiceCliente
	 *            codiceCliente per il quale verificare gli ordini.
	 * @return true se l'ordine è univoco
	 */
	boolean checkOrdineUnivoco(String numeroOrdine, String codiceCliente);

	/**
	 * Conferma gli ordini importati.<br/>
	 * La conferma crea la testata, carica i dati commerciali dei clienti<br/>
	 * inserisce le righe e ne calcola il prezzo in base ai dati commerciali. Infine totalizza l'ordine.
	 * 
	 * @param ordiniDaConfermare
	 *            ordini da confermare
	 * 
	 * @return timestamp indicante la dataCreazioneTimeStamp degli ordini creati
	 */
	Long confermaOrdiniImportati(Collection<OrdineImportato> ordiniDaConfermare);

	/**
	 * Salva una riga ordine importata. Restituisce una lista di righe perchè nel caso di una modifica di una proprietà
	 * dell'ordine importato ( es. codice di pagamento ) verranno modificate tutte le righe dell'ordine.
	 * 
	 * @param rigaOrdine
	 *            riga da salvare
	 * @return riga/righe salvata/e
	 */
	List<RigaOrdineImportata> salvaRigaOrdineImportata(RigaOrdineImportata rigaOrdine);
}
