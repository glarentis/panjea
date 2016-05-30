package it.eurotn.panjea.ordini.manager.documento.evasione;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import java.util.HashMap;
import java.util.Map;

/**
 * Contiene i dati delle distinte di carico per effettuare un'evasione.
 * 
 * @author giangi
 * @version 1.0, 18/feb/2011
 * 
 */
public class DatiDistintaCaricoEvasione {

	private Map<Integer, TipoAreaMagazzino> tipiAreaMagazzinoSediEreditate;
	private Map<Integer, TipoAreaMagazzino> tipiAreaMagazzinoSedi;
	private Map<Integer, TipoAreaMagazzino> tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore;
	private HashMap<String, TipoAreaMagazzino> tipiAreaMagazzinoSedeEntitaArticoloContoTezi;

	private Map<Integer, CodicePagamento> codiciPagamento;
	private Map<Integer, CodicePagamento> codiciPagamentoSedi;
	private Map<Integer, CodicePagamento> codiciPagamentoSediEreditate;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param tipiAreaMagazzinoSediEreditate
	 *            tipiAreaOrdineSediEreditate
	 * @param tipiAreaMagazzinoSedi
	 *            tipiAreaMagazzinoSedi
	 * @param tipiAreaMagazzinoArticoliContoTerziEntita
	 *            tipi aree magazzino per l'entità
	 * @param tipiAreaMagazzinoSedeEntitaArticoliContoTerziEntita
	 *            tipi aree magazzino per la sede di rifatturazione
	 * @param tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore
	 *            tipi aree magazzino per il fornitore conto terzi
	 * @param codiciPagamento
	 *            codiciPagamento aree ordine
	 * @param codiciPagamentoSedi
	 *            codici pagamento sedi principali
	 * @param codiciPagamentoSediEreditate
	 *            codici pagamento sedi ereditate
	 */
	public DatiDistintaCaricoEvasione(final Map<Integer, TipoAreaMagazzino> tipiAreaMagazzinoSediEreditate,
			final Map<Integer, TipoAreaMagazzino> tipiAreaMagazzinoSedi,
			final Map<String, TipoAreaMagazzino> tipiAreaMagazzinoArticoliContoTerziEntita,
			final Map<Integer, CodicePagamento> codiciPagamento,
			final Map<Integer, CodicePagamento> codiciPagamentoSedi,
			final Map<Integer, CodicePagamento> codiciPagamentoSediEreditate,
			final Map<String, TipoAreaMagazzino> tipiAreaMagazzinoSedeEntitaArticoliContoTerziEntita,
			final Map<Integer, TipoAreaMagazzino> tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore) {
		super();
		this.tipiAreaMagazzinoSediEreditate = tipiAreaMagazzinoSediEreditate;
		this.tipiAreaMagazzinoSedi = tipiAreaMagazzinoSedi;

		this.codiciPagamento = codiciPagamento;
		this.codiciPagamentoSedi = codiciPagamentoSedi;
		this.codiciPagamentoSediEreditate = codiciPagamentoSediEreditate;

		// unisco le mappe di conto terzi entita ed entita rifatturazione ( svrascrivendo eventuali entità con quelle di
		// rifatturazione perchè hanno la precedenza)
		// la chiave della mappa è idSede#idArticolo
		tipiAreaMagazzinoSedeEntitaArticoloContoTezi = new HashMap<String, TipoAreaMagazzino>();
		tipiAreaMagazzinoSedeEntitaArticoloContoTezi.putAll(tipiAreaMagazzinoArticoliContoTerziEntita);
		tipiAreaMagazzinoSedeEntitaArticoloContoTezi.putAll(tipiAreaMagazzinoSedeEntitaArticoliContoTerziEntita);

		// dalla mappa dei fornitori tengo solo quelli che hanno conto terzi su entità
		// this.tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore = new HashMap<Integer, TipoAreaMagazzino>();
		// for (Entry<Integer, TipoAreaMagazzino> entryFornitori :
		// tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore
		// .entrySet()) {
		// if (mapEntita.containsKey(entryFornitori.getKey())) {
		// this.tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore.put(entryFornitori.getKey(),
		// entryFornitori.getValue());
		// }
		// }
		this.tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore = tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore;
	}

	/**
	 * Restituisce il codice pagamento dell'area ordine se esiste, altrimenti quello della sede tenendo conto
	 * dell'ereditarietà.
	 * 
	 * @param areaOrdine
	 *            areaOrdine
	 * @param sedeEntita
	 *            sede entità
	 * @return codicePagamento
	 */
	public CodicePagamento getCodicePagamento(AreaOrdine areaOrdine, SedeEntita sedeEntita) {
		CodicePagamento codicePagamento = null;

		if (codiciPagamento.containsKey(areaOrdine.getId())) {
			codicePagamento = codiciPagamento.get(areaOrdine.getId());
		} else if (codiciPagamentoSediEreditate.containsKey(sedeEntita.getId())) {
			codicePagamento = codiciPagamentoSediEreditate.get(sedeEntita.getId());
		} else if (codiciPagamentoSedi.containsKey(sedeEntita.getId())) {
			codicePagamento = codiciPagamentoSedi.get(sedeEntita.getId());
		}

		return codicePagamento;
	}

	/**
	 * Se l'articolo è conto tezi devo caricare l'area ordine dall'fornitore conto terzi, altrimenti quello dalla sede
	 * considerando l'ereditarietà.
	 * 
	 * @param sedeEntita
	 *            sedeEntità associata alla riga della distinta di carico
	 * @param articolo
	 *            articolo associato alla riga della distinta di carico
	 * @return id del tipo documento di destinazione basato sulla sede o articolo della riga.
	 */
	public TipoAreaMagazzino getTipoDocumentoDestinazione(SedeEntita sedeEntita, ArticoloLite articolo) {
		// Verifico se l'articolo è a conto terzi
		TipoAreaMagazzino tam = null;

		String chiave = sedeEntita.getId() + "#" + articolo.getId();

		if (tipiAreaMagazzinoSedeEntitaArticoloContoTezi.get(chiave) != null
				&& tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore.get(articolo.getId()) != null) {
			tam = tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore.get(articolo.getId());
		} else if (tipiAreaMagazzinoSedi.get(sedeEntita.getId()) != null) {
			// Area magazzino di destinazione se la sede non eredita
			tam = tipiAreaMagazzinoSedi.get(sedeEntita.getId());
		} else if (tipiAreaMagazzinoSediEreditate.get(sedeEntita.getId()) != null) {
			tam = tipiAreaMagazzinoSediEreditate.get(sedeEntita.getId());
		}
		return tam;
	}

	/**
	 * @param sedeEntita
	 *            sedeEntità associata alla riga della distinta di carico
	 * @param articolo
	 *            articolo da testare
	 * @return true se l'articolo per questa evasione è in conto terzi
	 */
	public boolean isContoTerzi(SedeEntita sedeEntita, ArticoloLite articolo) {
		String chiave = sedeEntita.getId() + "#" + articolo.getId();
		return tipiAreaMagazzinoSedeEntitaArticoloContoTezi.get(chiave) != null
				&& tipiAreaMagazzinoSedeEntitaArticoliContoTerziFornitore.get(articolo.getId()) != null;
	}
}
