package it.eurotn.panjea.ordini.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;

import java.util.List;

public interface TipiAreaOrdineManager {

	/**
	 * cancella un {@link TipoAreaOrdine}.
	 * 
	 * @param tipoAreaOrdine
	 *            {@link TipoAreaOrdine} da cancellare
	 */
	void cancellaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine);

	/**
	 * Carica i tipi area ordine.
	 * 
	 * @param valueSearch
	 *            valore da filtrare
	 * @param fieldSearch
	 *            field da filtrare
	 * 
	 * @param loadTipiDocumentoDisabilitati
	 *            true se voglio caricare anche i tipi ordine disabilitati
	 * @return lista dei tipiAreaOrdine
	 */
	List<TipoAreaOrdine> caricaTipiAreaOrdine(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentoDisabilitati);

	/**
	 * 
	 * @return lista dei tipiDocumento legati ai TipiAreaOrdine
	 */
	List<TipoDocumento> caricaTipiDocumentiOrdine();

	/**
	 * 
	 * @param id
	 *            id del tipiAreaOrdine da caricare
	 * @return TipiAreaOrdine caricata
	 */
	TipoAreaOrdine caricaTipoAreaOrdine(Integer id);

	/**
	 * 
	 * @param idTipoDocumento
	 *            id Tipo Documento riferito al tipoAreaOrdine
	 * @return tipoAreaOrdine legata al tipoDocumento
	 */
	TipoAreaOrdine caricaTipoAreaOrdinePerTipoDocumento(Integer idTipoDocumento);

	/**
	 * 
	 * @param tipoAreaOrdine
	 *            tipoAreaOrdine da salvare.
	 * @return tipoAreaOrdine salvata
	 */
	TipoAreaOrdine salvaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine);

}