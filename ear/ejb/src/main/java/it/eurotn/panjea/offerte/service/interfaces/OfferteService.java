/**
 * 
 */
package it.eurotn.panjea.offerte.service.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.offerte.domain.AreaOfferta;
import it.eurotn.panjea.offerte.domain.RigaOfferta;
import it.eurotn.panjea.offerte.domain.TipoAreaOfferta;
import it.eurotn.panjea.offerte.util.AreaOffertaFullDTO;
import it.eurotn.panjea.offerte.util.ParametriRicercaOfferte;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * @author Leonardo
 * 
 */
@Remote
public interface OfferteService {

	/**
	 * Cancella l'area offerta e del documento legato.
	 * 
	 * @param areaOfferta
	 *            area da cancellare
	 */
	void cancellaAreaOfferta(AreaOfferta areaOfferta);

	/**
	 * Cancella la riga offerta.
	 * 
	 * @param rigaOfferta
	 *            riga da cancellare
	 */
	void cancellaRigaOfferta(RigaOfferta rigaOfferta);

	/**
	 * Cancella tutte le righe offerta dell'area.
	 * 
	 * @param idAreaOfferta
	 *            area di riferimento
	 */
	void cancellaRigheOfferta(Integer idAreaOfferta);

	/**
	 * Cancella il tipo area offerta.
	 * 
	 * @param idTipoAreaOfferta
	 *            id del tipo area offerta da cancellare
	 */
	void cancellaTipoAreaOfferta(Integer idTipoAreaOfferta);

	/**
	 * Carica l'area offerta.
	 * 
	 * @param idAreaOfferta
	 *            id dell'area da caricare
	 * @return area caricata
	 */
	AreaOfferta caricaAreaOfferta(Integer idAreaOfferta);

	/**
	 * Carica l'area offerta dal documento.
	 * 
	 * @param documento
	 *            documento di riferimento
	 * @return area caricata
	 */
	AreaOfferta caricaAreaOffertaByDocumento(Documento documento);

	/**
	 * Carica l' {@link AreaOffertaFullDTO} per l'area offerta.
	 * 
	 * @param idAreaOfferta
	 *            id dell'area offerta
	 * @return {@link AreaOffertaFullDTO}
	 */
	AreaOffertaFullDTO caricaAreaOffertaFullDTO(Integer idAreaOfferta);

	/**
	 * Carica l' {@link AreaOffertaFullDTO} per l'area offerta.
	 * 
	 * @param parametri
	 *            parametri
	 * @return {@link AreaOffertaFullDTO}
	 */
	AreaOffertaFullDTO caricaAreaOffertaFullDTO(Map<Object, Object> parametri);

	/**
	 * Carica le righe dell'area offerta.
	 * 
	 * @param idAreaOfferta
	 *            area di riferimento
	 * @return righe caricate
	 */
	List<RigaOfferta> caricaRigheOfferta(Integer idAreaOfferta);

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
	 * Crea una riga offerta.
	 * 
	 * @param idArticolo
	 *            id articolo
	 * @return riga creata
	 */
	RigaOfferta creaRigaOfferta(Integer idArticolo);

	/**
	 * Ricerca tutte le aree offerte che corrispondono ai parametri di ricerca.
	 * 
	 * @param parametriRicercaOfferte
	 *            parametri di ricerca
	 * @return aree trovate
	 */
	List<RigaOfferta> ricercaOfferte(ParametriRicercaOfferte parametriRicercaOfferte);

	/**
	 * Salva l'area offerta.
	 * 
	 * @param areaOfferta
	 *            area da salvare
	 * @return area offerta salva
	 * @throws DocumentoDuplicateException
	 *             rilanciata se esiste già il documento
	 */
	AreaOfferta salvaAreaOfferta(AreaOfferta areaOfferta) throws DocumentoDuplicateException;

	/**
	 * Salva una riga offerta.
	 * 
	 * @param rigaOfferta
	 *            riga da salvare
	 * @return riga salvata
	 */
	RigaOfferta salvaRigaOfferta(RigaOfferta rigaOfferta);

	/**
	 * Salva il tipo area offerta.
	 * 
	 * @param tipoAreaOfferta
	 *            tipo area offerta da salvare
	 * @return tipo area offerta salvata
	 */
	TipoAreaOfferta salvaTipoAreaOfferta(TipoAreaOfferta tipoAreaOfferta);

}
