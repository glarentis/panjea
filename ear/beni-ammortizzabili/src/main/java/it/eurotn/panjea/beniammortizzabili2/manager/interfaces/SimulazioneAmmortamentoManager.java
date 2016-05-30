package it.eurotn.panjea.beniammortizzabili2.manager.interfaces;

import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliSimulazioneException;
import it.eurotn.panjea.beniammortizzabili.exception.MancatoConsolidamentoAnnoPrecedenteException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoConsolidatoException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoSimulatoException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamento;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface SimulazioneAmmortamentoManager {

	/**
	 * Aggiorna il flag {@link BeneAmmortizzabile#isIndAmmortamento()} in base al residuo.
	 */
	void aggiornaBeniInAmmortamento();

	/**
	 * Riclacola la simulazione che contiene il bene salvato.
	 *
	 * @param bene
	 *            bene associato alla simulazione
	 * @param anno
	 *            anno per cui ricalcolare la simulazione
	 */
	void calcolaSimulazioniBene(BeneAmmortizzabile bene, Integer anno);

	/**
	 * Cancella la simulazione e le simulazioni che si basano su di essa La funzione è ricorsiva.
	 *
	 * @param simulazione
	 *            da eliminare
	 */
	void cancellaSimulazione(Simulazione simulazione);

	/**
	 * Carica una politicaCalcolo.
	 *
	 * @param politicaCalcolo
	 *            politica da caricare
	 * @return PoliticaCalcolo politica caricata
	 */
	PoliticaCalcolo caricaPoliticaCalcolo(PoliticaCalcolo politicaCalcolo);

	/**
	 * Carica la politica di calcolo bene legata alla quota di ammortamento.
	 *
	 * @param quotaAmmortamento
	 *            la quota da cui recuperare la politica di calcolo
	 * @return PoliticaCalcoloBene
	 */
	PoliticaCalcoloBene caricaPoliticaCalcoloBeneByQuotaAmmortamento(QuotaAmmortamento quotaAmmortamento);

	/**
	 * Carica tutte le politiche di calcolo della simulazione relative ai beni
	 *
	 * @param simulazioneId
	 *            id della simulazione
	 * @return politiche caricate
	 */
	List<PoliticaCalcolo> caricaPoliticheCalcoloBeni(Integer simulazioneId);

	/**
	 * Carica tutte le politiche di calcolo della simulazione relative al gruppo.
	 *
	 * @param simulazioneId
	 *            id della simulazione
	 * @return politiche caricate
	 */
	List<PoliticaCalcolo> caricaPoliticheCalcoloGruppo(Integer simulazioneId);

	/**
	 * Carica tutte le politiche di calcolo della simulazione relative alla sottospecie.
	 *
	 * @param simulazioneId
	 *            id della simulazione
	 * @return politiche caricate
	 */
	List<PoliticaCalcolo> caricaPoliticheCalcoloSottoSpecie(Integer simulazioneId);

	/**
	 * Carica tutte le politiche di calcolo della simulazione relative alla specie.
	 *
	 * @param simulazioneId
	 *            id della simulazione
	 * @return politiche caricate
	 */
	List<PoliticaCalcolo> caricaPoliticheCalcoloSpecie(Integer simulazioneId);

	/**
	 * Carica una <code>Simulazione</code> eseguendo il calcolo dei totali raggruppati per gruppo, specie e sottospecie.
	 *
	 * @param simulazione
	 *            simulazione da caricare
	 * @return <code>Simulazione</code> caricata
	 */
	Simulazione caricaSimulazione(Simulazione simulazione);

	/**
	 * Carica tutte le simulazioni dell'azienda.
	 *
	 * @return <code>List</code> di <code>Simulazione</code> caricate.
	 */
	List<Simulazione> caricaSimulazioni();

	/**
	 * Carica le simulazioni per l'anno.
	 *
	 * @param anno
	 *            l'anno in cui cercare le simulazioni
	 * @return List Simulazione
	 */
	List<Simulazione> caricaSimulazioniAnno(Integer anno);

	/**
	 * Consolida una simulazione e le sue quote. Cancella eventuali simulazioni presenti nell'anno e di conseguenza
	 * quelle collegate
	 *
	 * @param simulazione
	 *            la simulazione da consolidare
	 * @throws MancatoConsolidamentoAnnoPrecedenteException
	 *             rilanciata se l'anno precedente non risulta consolidato
	 */
	void consolidaSimulazione(Simulazione simulazione) throws MancatoConsolidamentoAnnoPrecedenteException;

	/**
	 * Crea una nuova simulazione.
	 *
	 * @param descrizione
	 *            descrizione della nuova simulazione
	 * @param data
	 *            Data della nuova simulazione
	 * @param simulazioneRiferimento
	 *            simulazione da associare alla nuova creata
	 * @return Simulazione
	 */
	Simulazione creaSimulazione(String descrizione, Date data, Simulazione simulazioneRiferimento);

	/**
	 * Crea una nuova <code>Simulazione</code> da una gi� esistente.
	 *
	 * @param descrizione
	 *            Descrizione della nuova simulazione
	 * @param data
	 *            Data della nuova simulazione
	 * @param idSimulazioneOld
	 *            id della simulazione old
	 * @return Nuova <code>Simulaizone</code>
	 * @param simulazioneRiferimento
	 *            simulazione di riferimento
	 * @return
	 */
	Simulazione creaSimulazione(String descrizione, Date data, Simulazione simulazioneRiferimento,
			Integer idSimulazioneOld);

	/**
	 * Salva una politica di calcolo.
	 *
	 * @param politicaCalcolo
	 *            politica di calcolo
	 */
	void salvaPoliticaCalcolo(PoliticaCalcolo politicaCalcolo);

	/**
	 * Salva una <code>Simulazione</code> e le sue politiche di calcolo generando le quote di ammortamento, fiscali e
	 * civilistiche, se si tratta di una <code>PoliticaCalcoloBene</code>.
	 *
	 * @param simulazione
	 *            Simulazione da salvare
	 * @return <code>Simulazione</code> salvata
	 * @throws AreeContabiliSimulazioneException .
	 */
	Simulazione salvaSimulazione(Simulazione simulazione) throws AreeContabiliSimulazioneException;

	/**
	 * Salva una <code>Simulazione</code> e le sue politiche di calcolo generando le quote di ammortamento, fiscali e
	 * civilistiche, se si tratta di una <code>PoliticaCalcoloBene</code>.
	 *
	 * @param simulazione
	 *            Simulazione da salvare
	 * @param forzaCancellazioneAC
	 *            indica se forzare la cancellazione delle relative aree contabili se esistono
	 * @return <code>Simulazione</code> salvata
	 * @throws AreeContabiliSimulazioneException .
	 */
	Simulazione salvaSimulazione(Simulazione simulazione, boolean forzaCancellazioneAC)
			throws AreeContabiliSimulazioneException;

	/**
	 * Salva una simulazione senza effettuare controlli.
	 *
	 * @param simulazione
	 *            Simulazione da salvare
	 */
	void salvaSimulazioneNoCheck(Simulazione simulazione);

	/**
	 * Verifica se l'anno di vendita � stato eseguito dopo la data di ultima simulazione consolidata o prima di essa.
	 *
	 * @param annoVendita
	 *            l'anno della vendita
	 * @throws VenditaInAnnoConsolidatoException
	 *             exception
	 * @throws VenditaInAnnoSimulatoException
	 *             exception
	 */
	void verificaAnnoVenditaBene(Integer annoVendita) throws VenditaInAnnoConsolidatoException,
			VenditaInAnnoSimulatoException;

	/**
	 * Verifica che la nuova simulazione posso essere creata.
	 *
	 * @param anno
	 *            Anno della nuova simulazione
	 * @return <code>ArrayList</code> vuota se posso creare la nuova simulazione e non devo allegarne una esistente<BR>
	 *         <code>ArrayList</code> di <code>SimulazioneVO</code> da cui devo scegliere una simulazione da legare alla
	 *         nuova<BR>
	 *         Errori:<BR>
	 *         <code>Integer</code> con valore <code>0</code> se l'anno della nuova simulazione < dell'ultimo anno
	 *         consolidato<br>
	 *         <code>Integer</code> con valore <code>1</code> se non esiste nessuna simulazione non consolidata
	 *         nell'anno precedente<br>
	 */
	Object verificaNuovaSimulazione(Integer anno);

}
