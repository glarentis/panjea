/**
 * 
 */
package it.eurotn.panjea.protocolli.rich.bd;

import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.protocolli.domain.ProtocolloAnno;
import it.eurotn.panjea.protocolli.domain.ProtocolloValore;
import it.eurotn.panjea.protocolli.service.interfaces.ProtocolliService;

import java.util.List;

/**
 * Interfaccia Business Delegate per {@link ProtocolliService}.
 * 
 * @author adriano
 * @version 1.0, 10/mag/07
 * 
 */
public interface IProtocolliBD {

	/**
	 * Esegue la cancellazione di {@link Protocollo}.
	 * 
	 * @param protocollo
	 *            da cancellare
	 * 
	 */
	void cancellaProtocollo(Protocollo protocollo);

	/**
	 * Esegue la cancellazione di {@link ProtocolloAnno} e nel caso {@link Protocollo} non abbia pi� rierimenti a
	 * {@link ProtocolloAnno} viene a sua volta cancellata.
	 * 
	 * @param protocolloAnno
	 *            protocollo anno da cancellare
	 * 
	 */
	void cancellaProtocolloAnno(ProtocolloAnno protocolloAnno);

	/**
	 * Carica la lista di {@link Protocollo} che rispondono al parametro di ricerca filter.
	 * 
	 * @param filter
	 *            filtro di ricerca
	 * @return protocolli caricati
	 */
	List<Protocollo> caricaProtocolli(String filter);

	/**
	 * Esegue la ricerca di {@link ProtocolloAnno} che rispondono al criterio anno e a filter.
	 * 
	 * @param anno
	 *            anno di riferimento
	 * @param filter
	 *            filtro di ricerca
	 * @return protocolli caricati
	 */
	List<ProtocolloAnno> caricaProtocolliAnno(Integer anno, String filter);

	/**
	 * Carica la lista di protocolli formattata in stringa codice + descrizione.
	 * 
	 * @param anno
	 *            anno
	 * @return protocolli caricati
	 */
	List<String> caricaProtocolliCodiceDescrizione(Integer anno);

	/**
	 * Carica la lista di {@link ProtocolloValore} che rispondono al parametro di ricerca filter.
	 * 
	 * @param filter
	 *            filtro di ricerca
	 * @return lista di protocolli caricati
	 */
	List<ProtocolloValore> caricaProtocolliValore(String filter);

	/**
	 * Carica la lista di protocolli valore formattata in stringa codice + descrizione.
	 * 
	 * @return lista di protocolli
	 */
	List<String> caricaProtocolliValoreCodiceDescrizione();

	/**
	 * carica il {@link Protocollo} idetificato da id.
	 * 
	 * @param id
	 *            id del protocollo
	 * @return protocollo caricato
	 * 
	 */
	Protocollo caricaProtocollo(Integer id);

	/**
	 * Restituisce il {@link ProtocolloAnno} identificato da id.
	 * 
	 * @param id
	 *            id del protocollo
	 * @return protocollo anno caricato
	 * 
	 */
	ProtocolloAnno caricaProtocolloAnno(Integer id);

	/**
	 * Crea tutti i protocolli a 0 per l'anno indicato prendendo come riferimento il primo anno precedente utile.
	 * 
	 * @param anno
	 *            anno in cui create i nuovi protocolli
	 */
	void creaProtocolliPerAnno(int anno);

	/**
	 * Salva la classe {@link Protocollo}.
	 * 
	 * @param protocollo
	 *            protocollo da salvare
	 * @return protocollo salvato
	 * 
	 */
	ProtocolloValore salvaProtocollo(ProtocolloValore protocollo);

	/**
	 * Esegue il salvataggio di {@link ProtocolloAnno} e lo restituisce.
	 * 
	 * @param protocolloAnno
	 *            protocollo da salvare
	 * @return protocollo salvato
	 * 
	 */
	ProtocolloAnno salvaProtocolloAnno(ProtocolloAnno protocolloAnno);

}
