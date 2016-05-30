/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.service.exception.EntitaRateNonCoerentiException;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreaChiusure;

import java.util.List;

import javax.ejb.Local;

/**
 * Metodi che servono a gestire correttamente la creazione e la ricerca dei manager<br>
 * delle ArreaEffetti e AreaPagamenti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 * 
 */
@Local
public interface AreaChiusureManager {

	/**
	 * Cancella una specifica areaChiusure.
	 * 
	 * @param areaChiusure
	 *            da cancellare
	 */
	void cancellaAreaChiusure(AreaChiusure areaChiusure);

	/**
	 * Carica un'area di chiusura.
	 * 
	 * @param id
	 *            id dell'area di chiusura
	 * @return area di chiusura
	 */
	AreaChiusure caricaAreaChisura(Integer id);

	/**
	 * Carica un'area di chiusura in base al documento.
	 * 
	 * @param documento
	 *            documento di riferimento
	 * @return area di chiusura caricata
	 */
	AreaChiusure caricaAreaChiusura(Documento documento);

	/**
	 * Carica tutti i pagamenti della rata.
	 * 
	 * @param rata
	 *            rada di cui caricare i pagamenti
	 * @return pagamenti caricati
	 */
	List<Pagamento> caricaPagamenti(Rata rata);

	/**
	 * Crea le aree chiusure da un lista di pagamenti.<br/>
	 * Il tipo dell'area chiusura dipende dal {@link TipoOperazione}.<br/>
	 * Il pagamento deve avere settato l'importo ed eventualmente la chiusura forzata.<br/>
	 * Viene calcolata la valuta in azienda con il tasso in data passata sui {@link ParametriCreazioneAreaChiusure} e
	 * l'importo forzato.<br/>
	 * Se l'importo della rata risulta essere uguale all'importo dello sconto finanziario la setta come sconto
	 * finanziario.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            parametri di creazione
	 * @param pagamenti
	 *            pagamenti
	 * @return aree create
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 * @throws CambioNonPresenteException
	 *             rilanciata se sto cercando di creare un pagamento senza avere il cambio della valuta nella data del
	 *             documento.
	 * @throws EntitaRateNonCoerentiException
	 *             la lista di rate non è della stessa entità
	 * 
	 */
	List<AreaChiusure> creaAreaChiusure(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException, CambioNonPresenteException,
			EntitaRateNonCoerentiException;

	/**
	 * Ricerca le aree chiusure.
	 * 
	 * @param parametriRicercaAreaChiusure
	 *            i parametri di ricerca
	 * @return la lista delle aree
	 */
	List<AreaChiusure> ricercaAreeChiusure(ParametriRicercaAreaChiusure parametriRicercaAreaChiusure);
}
