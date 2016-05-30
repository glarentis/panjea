package it.eurotn.panjea.preventivi.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;

import java.util.List;

import javax.ejb.Local;

@Local
public interface TipiAreaPreventivoManager {
	/**
	 * cancella un {@link TipoAreaPreventivo}.
	 * 
	 * @param tipoAreaPreventivo
	 *            {@link TipoAreaPreventivo} da cancellare
	 */
	void cancellaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo);

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
	List<TipoAreaPreventivo> caricaTipiAreaPreventivo(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentoDisabilitati);

	/**
	 * 
	 * @return lista dei tipiDocumento legati ai TipiAreaOrdine
	 */
	List<TipoDocumento> caricaTipiDocumentiPreventivo();

	/**
	 * 
	 * @param id
	 *            id del tipiAreaOrdine da caricare
	 * @return TipiAreaOrdine caricata
	 */
	TipoAreaPreventivo caricaTipoAreaPreventivo(Integer id);

	/**
	 * 
	 * @param idTipoDocumento
	 *            id Tipo Documento riferito al tipoAreaOrdine
	 * @return tipoAreaOrdine legata al tipoDocumento
	 */
	TipoAreaPreventivo caricaTipoAreaPreventivoPerTipoDocumento(Integer idTipoDocumento);

	/**
	 * 
	 * @param tipoAreaPreventivo
	 *            tipoAreaOrdine da salvare.
	 * @return tipoAreaOrdine salvata
	 */
	TipoAreaPreventivo salvaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo);
}
