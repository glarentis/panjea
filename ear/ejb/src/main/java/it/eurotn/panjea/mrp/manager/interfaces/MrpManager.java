package it.eurotn.panjea.mrp.manager.interfaces;

import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.OrdiniClienteCalcolo;
import it.eurotn.panjea.mrp.domain.OrdiniFornitoreCalcolo;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface MrpManager {
	/**
	 * Calcola le giacenze per ogni articolo e deposito.
	 *
	 * @param idDeposito
	 *            deposito
	 * @param data
	 *            ata per il calcolo
	 * @return giacenze
	 */
	List<Giacenza> calcolaGiacenzeFlat(int idDeposito, Date data);

	/**
	 * @param selectedObjects
	 *            risultati del calcolo mrp da cancellare
	 */
	void cancellaRigheRisultati(List<RisultatoMrpFlat> selectedObjects);

	/**
	 * Calcola mrp.<br/>
	 * Cancella un eventuale mrp già presente e pubblica sulla coda panjeamdb
	 * l'inizio del calcolo.
	 *
	 * @param numBucket
	 *            numero di bucket da calcolare
	 * @param startDate
	 *            data iniziale per il calcolo
	 */
	// void calcolaMrp(int numBucket, Date startDate) throws ConnectException;

	/**
	 *
	 * @return articolo con i dati di anagrafica ed eventualmente i dati del
	 *         foritore abituale
	 */
	List<ArticoloAnagrafica> caricaArticoloAnagrafica();

	/**
	 *
	 *
	 * @return adjacent list delle disinte base.
	 */
	List<Object[]> caricaBomBase();

	/**
	 * * @param configurazioniUtilizzate configurazioni utilizzate nelle righe
	 * ordini che verranno considerate.
	 *
	 * @return adjacent list delle disinte base.
	 */
	List<Object[]> caricaBomConfigurazioni(Set<Integer> configurazioniUtilizzate);

	/**
	 *
	 * @param dataInizio
	 *            di
	 * @param dataFine
	 *            df
	 * @return lista di ordini ordine non evasi
	 */
	List<OrdiniClienteCalcolo> caricaOrdiniClienteCalcolo(Date dataInizio, Date dataFine);

	/**
	 *
	 * @param dataInizio
	 *            di
	 * @param dataFine
	 *            df
	 * @return lista di ordini fornitori non evasi con la lista ordini
	 *         produzione e clienti collegata.
	 */
	List<OrdiniFornitoreCalcolo> caricaOrdiniFornitoreCalcolo(Date dataInizio, Date dataFine);

	/**
	 * Carica ordinato cliente da evadere.
	 *
	 * @param dataInizio
	 *            inizio
	 * @param dataFine
	 *            fine
	 * @return righe risultato bucket. Le righe di risultato da calcolare. Le
	 *         righe risultato partono da righe cliente da evadere.
	 */
	List<RisultatoMRPArticoloBucket> caricaRigheClienteDaEvadere(Date dataInizio, Date dataFine);

	/**
	 * Carica le righe degli ordini produzione esistenti.
	 *
	 * @param dataInizio
	 *            data inizio
	 * @param dataFine
	 *            data fine
	 * @return righe risultato bucket. Le righe produzione esistenti.
	 */
	List<RisultatoMRPArticoloBucket> caricaRigheProduzionePresenti(Date dataInizio, Date dataFine);

	/**
	 *
	 * @param parametriMrpRisultato
	 *            parametri
	 * @return risultato del calcolo mrp
	 */
	List<RisultatoMrpFlat> caricaRisultatoMrp(ParametriMrpRisultato parametriMrpRisultato);

	/**
	 * @return id dei depositi abilitati per il calcolo MRP.
	 */
	List<Integer> getIdDepositi();

	/**
	 *
	 * @param righeMrpFlat
	 *            rihge da salvare
	 * @return righe salvate
	 */
	List<RisultatoMrpFlat> salvaRigheRisultato(List<RisultatoMrpFlat> righeMrpFlat);

	/**
	 * @param risultati
	 *            risultati del calcolo mrp da salvare
	 */
	void salvaRisultatoMRP(List<RisultatoMRPArticoloBucket> risultati);

	/**
	 * Svuota i risultati mrp.
	 */
	void svuotaRigheRisultati();

}
