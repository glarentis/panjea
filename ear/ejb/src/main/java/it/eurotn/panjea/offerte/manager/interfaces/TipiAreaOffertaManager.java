/**
 * 
 */
package it.eurotn.panjea.offerte.manager.interfaces;

import it.eurotn.panjea.offerte.domain.TipoAreaOfferta;

import java.util.List;

import javax.ejb.Local;

/**
 * @author Leonardo
 * 
 */
@Local
public interface TipiAreaOffertaManager {

	/**
	 * Cancella il tipo area offerta.
	 * 
	 * @param idTipoAreaOfferta
	 *            id del tipo area offerta da cancellare
	 */
	void cancellaTipoAreaOfferta(Integer idTipoAreaOfferta);

	/**
	 * Carica i tipi area offerta.
	 * 
	 * @param loadTipiDocumentoDisabilitati
	 *            <code>true</code> se devono essere caricati anche quelli disabilitati
	 * @return tipi aree offerta caricate
	 */
	List<TipoAreaOfferta> caricaTipiAreaOfferta(boolean loadTipiDocumentoDisabilitati);

	/**
	 * Carica il tipo area offerta.
	 * 
	 * @param idTipoAreaOfferta
	 *            id del tipo area offerta da caricare
	 * @return tipo area offerta caricata
	 */
	TipoAreaOfferta caricaTipoAreaOfferta(Integer idTipoAreaOfferta);

	/**
	 * Carica il tipo area offerta in base al tipo documento.
	 * 
	 * @param idTipoDocumento
	 *            id del tipo documento
	 * @return tipo area offerta caricata
	 */
	TipoAreaOfferta caricaTipoAreaOffertaPerTipoDocumento(Integer idTipoDocumento);

	/**
	 * Salva il tipo area offerta.
	 * 
	 * @param tipoAreaOfferta
	 *            tipo area offerta da salvare
	 * @return tipo area offerta salvata
	 */
	TipoAreaOfferta salvaTipoAreaOfferta(TipoAreaOfferta tipoAreaOfferta);

}
