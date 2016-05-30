package it.eurotn.panjea.rate.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.util.RataRV;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 *
 * Interfaccia local per il Session Bean RateManager.
 *
 * @author vittorio
 * @version 1.0, 24/nov/2009
 *
 */
@Local
public interface RateManager {

	/**
	 * Associa il rapporto bancario dell'entita' definito sul documento alla rata.
	 *
	 * @param rata
	 *            Rata
	 * @param areaDocumento
	 *            IAreaDocumento
	 * @param tipoPagamento
	 *            TipoPagamento
	 * @param salvaRata
	 *            se si deve fare il salvataggio della rata
	 * @return Rata
	 */

	Rata associaRapportoBancario(Rata rata, IAreaDocumento areaDocumento, TipoPagamento tipoPagamento, boolean salvaRata);

	/**
	 * Cancella la {@link Rata} solo se lo stato della rata e' aperto.
	 *
	 * @param rata
	 *            la rata da eliminare
	 * @throws DocumentiCollegatiPresentiException
	 *             rilanciata quando ci sono delle rate gia' pagate
	 */
	void cancellaRata(Rata rata) throws DocumentiCollegatiPresentiException;

	/**
	 * Cancella la {@link Rata} senza controlli.
	 *
	 * @param rata
	 *            la rata da eliminare
	 */

	void cancellaRataNoCheck(Rata rata);

	/**
	 * carica una rata.
	 *
	 * @param idRata
	 *            della rata a cercare.
	 * @return rata.
	 */
	Rata caricaRata(Integer idRata);

	/**
	 * Carica tutte le rate collegate a quella di riferimento.
	 *
	 * @param rata
	 *            rata di riferimento
	 * @return rate collegate
	 */
	List<Rata> caricaRateCollegate(Rata rata);

	/**
	 * Ricerca tutte le rate che devono essere stampate per la richiesta di versamento a mezzo bonifico elettronico in
	 * pase ai parametri.
	 *
	 * @param parametri
	 *            parametri
	 * @return lista di rate caricate
	 */
	List<RataRV> caricaRatePerRichiestaVersamento(Map<Object, Object> parametri);

	/**
	 * Ricerca tutte le rate che devono essere stampate per la richiesta di versamento a mezzo bonifico elettronico in
	 * base ai parametri e le raggruppa per cliente.
	 *
	 * @param parametri
	 *            parametri
	 * @return lista di rate caricate
	 */
	List<RataRV> caricaRateRaggruppatePerRichiestaVersamento(Map<Object, Object> parametri);

	/**
	 * Restituisce la lista di {@link Rata} secondo i parametri {@link ParametriRicercaRate}.
	 *
	 * @param parametriRicercaRate
	 *            i {@link ParametriRicercaRate} per eseguire la ricerca
	 * @return la lista di {@link SituazioneRata}
	 */
	List<SituazioneRata> ricercaRate(ParametriRicercaRate parametriRicercaRate);

	/**
	 * Dalla rata da riemettere genera le nuove rate ad essa collegata. Solo se la rata è insoluta è possibile
	 * riemetterla.
	 *
	 * @param rataRiemessaDTO
	 *            contiene la rata da riemettere e i valori delle rate da generare. Le rate da generare solo contenute
	 *            delle proprietà <code>rateDaCreare</code> di cui verranno usati i valori dell'importo e della data di
	 *            scadenza per la generazione
	 */
	void riemettiRate(RataRiemessa rataRiemessaDTO);

	/**
	 * Metodo che salva le modifiche alle rate (Individualmente), si preoccupa di invalidare l'area collegata se valida.
	 *
	 * @param rata
	 *            Rata
	 * @return Rata
	 */
	Rata salvaRata(Rata rata);

	/**
	 * Metodo che salva la rata senza verifiche e invalidazione aree.
	 *
	 * @param rata
	 *            Rata
	 * @return Rata
	 */
	Rata salvaRataNoCheck(Rata rata);

}
