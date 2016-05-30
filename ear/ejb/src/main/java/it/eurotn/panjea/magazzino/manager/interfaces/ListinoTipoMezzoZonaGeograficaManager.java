package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;

import java.util.List;

import javax.ejb.Local;

/**
 * Manager per gestire il CRUD del listino che associa un prezzo al tipo mezzo di una determinata zona geografica.
 * 
 * @author Leonardo
 */
@Local
public interface ListinoTipoMezzoZonaGeograficaManager {

	/**
	 * Cancella il listino tipo mezzo zona geografica.
	 * 
	 * @param listinoTipoMezzoZonaGeografica
	 *            il listino da cancellare
	 */
	void cancellaListinoTipoMezzoZonaGeografica(ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica);

	/**
	 * Carica i listini prezzo per tipo mezzo e zona geografica disponibili per l'azienda.
	 * 
	 * @return restituisce la lista di listini
	 */
	List<ListinoTipoMezzoZonaGeografica> caricaListiniTipoMezzoZonaGeografica();

	/**
	 * Carica un {@link ListinoTipoMezzoZonaGeografica} in base ai parametri passati. Se non esiste nessuno listino
	 * ritorna <code>null</code>.
	 * 
	 * @param tipoMezzoTrasporto
	 *            tipo mezzo di traporto
	 * @param zonaGeografica
	 *            zona geografica
	 * @return {@link ListinoTipoMezzoZonaGeografica} caricato
	 */
	ListinoTipoMezzoZonaGeografica caricaListinoTipoMezzoZonaGeografica(TipoMezzoTrasporto tipoMezzoTrasporto,
			ZonaGeografica zonaGeografica);

	/**
	 * Salva un listino prezzo per tipo mezzo e zona geografica.
	 * 
	 * @param listinoTipoMezzoZonaGeografica
	 *            il listino da salvare
	 * @return restituisce il listino prezzo per tipo mezzo e zona geografica salvato
	 */
	ListinoTipoMezzoZonaGeografica salvaListinoTipoMezzoZonaGeografica(
			ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica);

}
