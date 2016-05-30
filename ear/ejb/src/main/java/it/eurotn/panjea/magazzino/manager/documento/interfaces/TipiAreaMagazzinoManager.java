package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

import java.util.List;

import javax.ejb.Local;

/**
 * Gestione di TipoAreaMagazzino.
 * 
 * @author giangi
 * 
 */

@Local
public interface TipiAreaMagazzinoManager {

	/**
	 * metodo che cancella {@link TipoAreaMagazzino}.
	 * 
	 * @param tipoAreaMagazzino
	 *            tipo area da cancellare
	 */
	void cancellaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino);

	/**
	 * Cancella il {@link TipoDocumentoBaseMagazzino} senza controlli.
	 * 
	 * @param tipoDocumentoBase
	 *            tipo documento base da cancellare
	 */
	void cancellaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase);

	/**
	 * Metodo che restituisce i {@link TipoAreaMagazzino} per l'azienda loggata.
	 * 
	 * @param valueSearch
	 *            valore da filtrare
	 * @param fieldSearch
	 *            campo da filtrare
	 * 
	 * @return tipo area caricata
	 * @param loadTipiDocumentoDisabilitati
	 *            carica anche i tipi documento disabilitati
	 */
	List<TipoAreaMagazzino> caricaTipiAreaMagazzino(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentoDisabilitati);

	/**
	 * Metodo che restituisce l'elenco di {@link TipoDocumento} che hanno delle {@link TipoAreaMagazzino}.
	 * 
	 * @return tipi documento caricati
	 */
	List<TipoDocumento> caricaTipiDocumentiMagazzino();

	/**
	 * Carica tutti tipi documento che posso associare al tipo area magazzino per la fatturazione.
	 * 
	 * @return tipi documento caricati
	 */
	List<TipoDocumento> caricaTipiDocumentoAnagraficaPerFatturazione();

	/**
	 * Carica tutti i {@link TipoDocumentoBaseMagazzino} per azienda.
	 * 
	 * @return list di {@link TipoDocumentoBaseMagazzino} caricati
	 */
	List<TipoDocumentoBaseMagazzino> caricaTipiDocumentoBase();

	/**
	 * metodo che restituisce {@link TipoAreaMagazzino} sempre l'oggetto da caricare e non l'id.
	 * 
	 * @param id
	 *            id del tipo area da caricare
	 * @return tipo area caricata
	 */
	TipoAreaMagazzino caricaTipoAreaMagazzino(Integer id);

	/**
	 * Carica il tipo area magazzino per l'inventario.
	 * 
	 * @return tipo area magazzino
	 */
	TipoAreaMagazzino caricaTipoAreaMagazzinoInventario();

	/**
	 * Metodo che restituisce il {@link TipoAreaMagazzino} per l'id di {@link TipoDocumento} passato per parametro..
	 * 
	 * @param idTipoDocumento
	 *            id tipo documento
	 * @return tipo area magazzino caricata
	 */
	TipoAreaMagazzino caricaTipoAreaMagazzinoPerTipoDocumento(Integer idTipoDocumento);

	/**
	 * Carica il {@link TipoDocumentoBaseMagazzino} con il tipo operazione specificato da
	 * {@link TipoDocumentoBaseMagazzino.TipoOperazioneTipoDocumento}.
	 * 
	 * @param tipoOperazione
	 *            tipo operazione
	 * @return {@link TipoDocumentoBaseMagazzino} caricato
	 * @throws ObjectNotFoundException
	 *             ObjectNotFoundException
	 */
	TipoDocumentoBaseMagazzino caricaTipoDocumentoBase(
			TipoDocumentoBaseMagazzino.TipoOperazioneTipoDocumento tipoOperazione) throws ObjectNotFoundException;

	/**
	 * metodo che esegue il salvataggio di {@link TipoAreaMagazzino}.
	 * 
	 * @param tipoAreaMagazzino
	 *            tipo area magazzino da salvare
	 * @return tipo area magazzino salvata
	 */
	TipoAreaMagazzino salvaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino);

	/**
	 * Esegue il salvataggio di {@link TipoDocumentoBaseMagazzino} e lo restituisce salvato.
	 * 
	 * @param tipoDocumentoBase
	 *            tipo documento base da salvare
	 * @return {@link TipoDocumentoBaseMagazzino} salvato
	 */
	TipoDocumentoBaseMagazzino salvaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase);
}
