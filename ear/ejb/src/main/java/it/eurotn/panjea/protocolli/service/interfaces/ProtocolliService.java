package it.eurotn.panjea.protocolli.service.interfaces;

import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.protocolli.domain.ProtocolloAnno;
import it.eurotn.panjea.protocolli.domain.ProtocolloValore;
import it.eurotn.panjea.protocolli.service.exception.ProtocolliException;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ProtocolliService {

	/**
	 * Esegue la cancellazione di {@link Protocollo}.
	 * 
	 * @param protocollo
	 *            il protocollo da eliminare.
	 * @throws ProtocolliException
	 *             exception sollevata nel caso in cui si presenta un errore o il protocollo risulta essere utilizzato.
	 */
	void cancellaProtocollo(Protocollo protocollo) throws ProtocolliException;

	/**
	 * Esegue la cancellazione di {@link ProtocolloAnno} e nel caso {@link Protocollo} non abbia piu' rierimenti a
	 * {@link ProtocolloAnno} viene a sua volta cancellata.
	 * 
	 * @param protocolloAnno
	 *            il protocollo da eliminare
	 * @throws ProtocolliException
	 *             exception sollevata nel caso in cui si presenta un errore o il protocollo risulta essere utilizzato.
	 */
	void cancellaProtocolloAnno(ProtocolloAnno protocolloAnno) throws ProtocolliException;

	/**
	 * Carica la lista di {@link Protocollo} che rispondono al parametro di ricerca filter.
	 * 
	 * @param codiceFilter
	 *            il filtro sul campo codice per cui ricercare il protocollo
	 * @return una lista di {@link Protocollo}
	 */
	List<Protocollo> caricaProtocolli(String codiceFilter);

	/**
	 * Esegue la ricerca di {@link ProtocolloAnno} che rispondono al criterio anno e a filter.
	 * 
	 * @param anno
	 *            l'anno dei protocolli da ricercare
	 * @param filterCodice
	 *            il filtro sul campo codice da ricercare
	 * @return List<ProtocolloAnno>
	 */
	List<ProtocolloAnno> caricaProtocolliAnno(Integer anno, String filterCodice);

	/**
	 * Carica la lista di {@link ProtocolloValore} che rispondono al parametro di ricerca filter.
	 * 
	 * @param filterCodice
	 *            il filtro sul campo codice da ricercare
	 * @return List<ProtocolloValore>
	 */
	List<ProtocolloValore> caricaProtocolliValore(String filterCodice);

	/**
	 * Carica il {@link Protocollo} idetificato da id.
	 * 
	 * @param id
	 *            l'identificativo del protocollo da caricare
	 * @return Protocollo
	 * @throws ProtocolliException
	 *             sollevata nel caso in cui il protocollo non esista per l'id specificato
	 */
	Protocollo caricaProtocollo(Integer id) throws ProtocolliException;

	/**
	 * Restituisce il {@link ProtocolloAnno} identificato da id.
	 * 
	 * @param id
	 *            l'identificativo del protocolloAnno da caricare
	 * @return ProtocolloAnno
	 * @throws ProtocolliException
	 *             sollevata nel caso in cui il protocolloAnno non esista per l'id specificato
	 */
	ProtocolloAnno caricaProtocolloAnno(Integer id) throws ProtocolliException;

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
	 *            il protocollo da salvare
	 * @return ProtocolloValore
	 * @throws ProtocolliException
	 *             sollevata nel caso in cui il protocolloAnno non esista per l'id specificato
	 */
	ProtocolloValore salvaProtocollo(ProtocolloValore protocollo) throws ProtocolliException;

	/**
	 * Esegue il salvataggio di {@link ProtocolloAnno} e lo restituisce.
	 * 
	 * @param protocolloAnno
	 *            il protocolloAnno da salvare
	 * @return ProtocolloAnno
	 * @throws ProtocolliException
	 *             sollevata nel caso in cui il protocolloAnno non esista per l'id specificato
	 */
	ProtocolloAnno salvaProtocolloAnno(ProtocolloAnno protocolloAnno) throws ProtocolliException;
}
