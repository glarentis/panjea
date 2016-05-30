package it.eurotn.panjea.ordini.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.domain.OrdiniSettings;
import it.eurotn.panjea.ordini.domain.SedeOrdine;

import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

@Remote
public interface AnagraficaOrdiniService {

	/**
	 * Associa ad ogni {@link EntitaLite} il {@link TipoAreaMagazzino} come tipo documento di evasione.
	 *
	 * @param map
	 *            mappa che contiene gli estremi {@link EntitaLite} - {@link TipoAreaMagazzino} per l'associazione
	 */
	void associaTipoAreaEvasione(Map<TipoAreaMagazzino, Set<EntitaLite>> map);

	/**
	 * Carica il settings degli ordini.<br/>
	 * Se non esiste ne crea uno, lo salva e lo restituisce.
	 *
	 * @return <code>OrdiniSettings</code> caricato
	 */
	OrdiniSettings caricaOrdiniSettings();

	/**
	 * Carica {@link SedeOrdine} collegata a {@link SedeEntita}.
	 *
	 * @param sedeEntita
	 *            sedeEntita di riferimento
	 * @param ignoraEreditaDatiCommerciali
	 *            se true forza il caricamento della sede ordine ignorando {@link SedeEntita#isEreditaDatiCommerciali()}
	 * @return {@link SedeOrdine} della sedeEntita
	 */
	SedeOrdine caricaSedeOrdineBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali);

	/**
	 * Salva un {@link OrdiniSettings}.
	 *
	 * @param ordiniSettings
	 *            il settings degli ordini da salvare
	 * @return <code>OrdiniSettings</code> salvato
	 */
	OrdiniSettings salvaOrdiniSettings(OrdiniSettings ordiniSettings);

	/**
	 * Rende persistente la {@link SedeOrdine} e la restituisce.
	 *
	 * @param sedeOrdine
	 *            sede ordine da salvare
	 * @return {@link SedeOrdine} salvata
	 */
	SedeOrdine salvaSedeOrdine(SedeOrdine sedeOrdine);
}
