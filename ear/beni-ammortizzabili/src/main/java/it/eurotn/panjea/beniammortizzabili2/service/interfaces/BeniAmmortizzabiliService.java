package it.eurotn.panjea.beniammortizzabili2.service.interfaces;

import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliSimulazioneException;
import it.eurotn.panjea.beniammortizzabili.exception.BeniAmmortizzabiliException;
import it.eurotn.panjea.beniammortizzabili.exception.MancatoConsolidamentoAnnoPrecedenteException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoConsolidatoException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoSimulatoException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileConFigli;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.BeniSettings;
import it.eurotn.panjea.beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamento;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoCivilistico;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.panjea.beniammortizzabili2.domain.ValutazioneBene;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;
import it.eurotn.panjea.beniammortizzabili2.util.SituazioneBene;
import it.eurotn.panjea.beniammortizzabili2.util.registrobeni.RegistroBene;
import it.eurotn.panjea.beniammortizzabili2.util.venditeannuali.VenditaAnnualeBene;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 *
 * @author adriano
 * @version 1.0, 02/ott/07
 */
@Remote
public interface BeniAmmortizzabiliService {

	/**
	 * Aggiorna tutti i beni in ammortamento.
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
	 * Cacnella un bene ammortizzabile solo se non ha figli, in caso contrario rilancia una VincoloException.
	 *
	 * @param beneAmmortizzabile
	 *            il bene da cancellare
	 * @throws VincoloException
	 *             se il bene ha dei figli collegati
	 */
	void cancellaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) throws VincoloException;

	/**
	 * Cancella la simulazione e le simulazioni che si basano su di essa La funzione è ricorsiva.
	 *
	 * @param simulazione
	 *            da eliminare
	 */
	void cancellaSimulazione(Simulazione simulazione);

	/**
	 * cancella {@link SottoSpecie}.
	 *
	 * @param sottoSpecie
	 *            sottospecie da cancellare
	 */
	void cancellaSottoSpecie(SottoSpecie sottoSpecie);

	/**
	 * cancella {@link Specie}.
	 *
	 * @param specie
	 *            specie da cancellare
	 * @throw BeniAmmortizzabiliException exception
	 */
	void cancellaSpecie(Specie specie);

	/**
	 * cancella {@link TipologiaEliminazione}.
	 *
	 * @param tipologiaEliminazione
	 *            tipologia da eliminare
	 */
	void cancellaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione);

	/**
	 * cancella {@link Ubicazione}.
	 *
	 * @param ubicazione
	 *            Ubicazione da cancellare
	 */
	void cancellaUbicazione(Ubicazione ubicazione);

	/**
	 * Cancella una <code>ValutazioneBene</code>.
	 *
	 * @param valutazioneBene
	 *            la <code>ValutazioneBene</code> da cancellare
	 * @throws BeniAmmortizzabiliException
	 */
	void cancellaValutazioneBene(ValutazioneBene valutazioneBene);

	/**
	 * Cancella la <code>VenditaBene</code>.
	 *
	 * @param venditaBene
	 *            la vendita da cancellare
	 * @throws VenditaInAnnoSimulatoException .
	 * @throws VenditaInAnnoConsolidatoException .
	 */
	void cancellaVenditaBene(VenditaBene venditaBene) throws VenditaInAnnoConsolidatoException,
	VenditaInAnnoSimulatoException;

	/**
	 * Carica un <code>BeneAmmortizzabile</code> con l'<code>ID</code> del bene passato.
	 *
	 * @param beneAmmortizzabile
	 *            Bene ammortizzabile da caricare
	 * @return BeneAmmortizzabile Bene ammortizzabile caricato
	 */
	BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile);

	/**
	 * Carica un <code>BeneAmmortizzabile</code> con l'<code>ID</code> del beneLite passato.
	 *
	 * @param beneAmmortizzabileLite
	 *            Bene ammortizzabile da caricare
	 * @return BeneAmmortizzabile Bene ammortizzabile caricato
	 */
	BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabileLite beneAmmortizzabileLite);

	/**
	 * Carica la lista di <code>BeneAmmortizzabile</code> dell'azienda dell'utente corrente.
	 *
	 * @return List BeneAmmortizzabile
	 */
	List<BeneAmmortizzabile> caricaBeniAmmortizzabiliAzienda();

	/**
	 * Carica tutti i beni ammortizzabili figli del bene padre passato.
	 *
	 * @param beneAmmortizzabilePadre
	 *            il <code>BeneAmmortizzabile</code> padre
	 * @return <code>List</code> di <code>BeneAmmortizzabile</code> che contiene tutti i beni figli caricati.
	 */
	List<BeneAmmortizzabile> caricaBeniAmmortizzabiliFigli(BeneAmmortizzabile beneAmmortizzabilePadre);

	/**
	 * Carica tutti i <code>BeneAmmortizzabile</code> ancora da ammortizzare. datiCivilistici.ammortamentoInCorso = true
	 * o datiFiscali.ammortamentoInCorso = true e FLAG_BENE_DI_PROPRIETA = true.
	 *
	 * @param date
	 *            data ammortamento
	 * @return <code>List</code> di <code>BeneAmmortizzabile</code> caricati.
	 */
	List<BeneAmmortizzabileLite> caricaBeniDaAmmortizzareLite(Date date);

	/**
	 * Carica le settings dei beni ammortizzabili.
	 *
	 * @return settings
	 */
	BeniSettings caricaBeniSettings();

	/**
	 * carica {@link List} di {@link Gruppo}.
	 *
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return Gruppi caricati
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	List<Gruppo> caricaGruppi(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link Gruppo} per l'azienda corrente.
	 *
	 * @return gruppo azienda caricato
	 */
	Gruppo caricaGruppoAzienda();

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
	 * Carica tutte le quote di ammortamento civilistiche, consolidate o no, del bene ammortizzabile.
	 *
	 * @param beneAmmortizzabile
	 *            Bene ammortizzabile
	 * @param isConsolidate
	 *            se <code>true</code> carica solo le quote consolidate, se <code>false</code> solo quelle non
	 *            consolidate
	 * @return <code>List</code> di <code>QuoteAmmortamentoCivilisticheVO</code> caricate
	 */
	List<QuotaAmmortamentoCivilistico> caricaQuoteAmmortamentoCivilistiche(BeneAmmortizzabile beneAmmortizzabile,
			boolean isConsolidate);

	/**
	 * Carica tutte le quote di ammortamento fiscali, consolidate o no, del bene ammortizzabile.
	 *
	 * @param beneAmmortizzabile
	 *            <code>BeneAmmortizzabile</code>
	 * @param isConsolidate
	 *            se <code>true</code> carica solo le quote consolidate, se <code>false</code> solo quelle non
	 *            consolidate
	 * @return <code>List</code> di <code>QuoteAmmortamentoFiscali</code> caricate
	 */
	List<QuotaAmmortamentoFiscale> caricaQuoteAmmortamentoFiscali(BeneAmmortizzabile beneAmmortizzabile,
			boolean isConsolidate);

	/**
	 * Carica il registro dei beni.
	 *
	 * @param parameters
	 *            parametri
	 * @return beni caricati
	 */
	List<RegistroBene> caricaRegistroBeni(Map<String, Object> parameters);

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
	 * Carica la situazione dei beni.
	 *
	 * @param parameters
	 *            parametri
	 * @return situazioni bene caricate
	 */
	List<SituazioneBene> caricaSituazioneBeni(Map<Object, Object> parameters);

	/**
	 * carica {@link SottoSpecie}.
	 *
	 * @param id
	 *            id della sottospecie da caricare
	 * @return sottospecie caricata
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	SottoSpecie caricaSottoSpecie(Integer id) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link List} di {@link SottoSpecie}.
	 *
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return sottospecie caricate
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	List<SottoSpecie> caricaSottoSpecie(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException;

	/**
	 * carica Specie per azienda corrente.
	 *
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return speci caricate
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	List<Specie> caricaSpeci(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link List} di {@link Specie} per {@link Gruppo}.
	 *
	 * @param gruppo
	 *            gruppo di riferimento
	 * @return speci caricate
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	List<Specie> caricaSpecie(Gruppo gruppo) throws BeniAmmortizzabiliException;

	/**
	 * carica Specie.
	 *
	 * @param id
	 *            id della specie da caricare
	 * @return Specie caricata
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	Specie caricaSpecie(Integer id) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link TipologiaEliminazione}.
	 *
	 * @param id
	 *            id della tipologia da caricare
	 * @return Tipologia caricata
	 * @throws BeniAmmortizzabiliException
	 *             exception
	 */
	TipologiaEliminazione caricaTipologiaEliminazione(Integer id) throws BeniAmmortizzabiliException;

	/**
	 * carica {@link List} di {@link TipologiaEliminazione}.
	 *
	 * @param codice
	 *            codice da filtrare
	 * @return tipologie caricate
	 */
	List<TipologiaEliminazione> caricaTipologieEliminazione(String codice);

	/**
	 * carica {@link List} di {@link Ubicazione}.
	 *
	 * @param codice
	 *            codice da filtrare
	 * @return ubicazioni caricate
	 */
	List<Ubicazione> caricaUbicazioni(String codice);

	/**
	 * Carica tutte le rivalutazioni/svalutazioni di un bene.
	 *
	 * @param beneAmmortizzabile
	 *            il bene ammortizzabile
	 * @return <code>List</code> di <code>Valutazione</code> caricate per il bene
	 */
	List<ValutazioneBene> caricaValutazioniBene(BeneAmmortizzabile beneAmmortizzabile);

	/**
	 * Carica tutti i beni con vendite effettuate nell'anno specificato nei parametri.
	 *
	 * @param parameters
	 *            parametri
	 * @return beni caricati
	 */
	List<VenditaAnnualeBene> caricaVenditeAnnualiBeniPadri(Map<Object, Object> parameters);

	/**
	 * Carica tutte le <code>VenditeBene</code> del bene ammortizzabile.
	 *
	 * @param beneAmmortizzabile
	 *            il <code>BeneAmmortizzabile</code>
	 * @return <code>List</code> di <code>VenditeBene</code> caricate
	 */
	List<VenditaBene> caricaVenditeBene(BeneAmmortizzabile beneAmmortizzabile);

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
	 * Crea una nuova valutazione del bene.
	 *
	 * @param beneAmmortizzabile
	 *            bene associato alla valutazione
	 * @return ValutazioneBene
	 */
	ValutazioneBene creaNuovaValutazioneBene(BeneAmmortizzabile beneAmmortizzabile);

	/**
	 * Crea una nuova vendita del bene.
	 *
	 * @param beneAmmortizzabile
	 *            bene associato alla vendita
	 * @return VenditaBene
	 */
	VenditaBene creaNuovaVenditaBene(BeneAmmortizzabile beneAmmortizzabile);

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
	 * Popola il database con valori di default.
	 */
	void fillDataBaseBeniAmmortamento();

	/**
	 * recupera la {@link List} {@link BeneAmmortizzabileConFigli} per il report dei beni acquistati.
	 *
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return beni trovati
	 */
	List<BeneAmmortizzabileConFigli> ricercaBeniAcquistati(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	/**
	 * Restituisce una List di {@link BeneAmmortizzabileLite} filtrata con i parametri ricevuti.
	 *
	 * @param parametri
	 *            parametri da utilizzare per filtrare la ricerca
	 * @return List di BeneAmmortizzabileLite
	 */
	List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(Map<String, Object> parametri);

	/**
	 * Restituisce una List di {@link BeneAmmortizzabileLite} filtrata con i parametri ricevuti.
	 *
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return List di BeneAmmortizzabileLite
	 */
	List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(String fieldSearch, String valueSearch);

	/**
	 * Restiutisce la {@link List} di {@link BeneAmmortizzabileConFigli} per {@link FornitoreLite}.
	 *
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return List BeneAmmortizzabileConFigli
	 */
	List<BeneAmmortizzabileConFigli> ricercaBeniAmmortizzabiliPerFornitore(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	/**
	 * Restituisce {@link BeneAmmortizzabileConFigli} per {@link Ubicazione}.
	 *
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return List BeneAmmortizzabileConFigli
	 */
	List<BeneAmmortizzabileConFigli> ricercaBeniAmmortizzabiliPerUbicazione(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	/**
	 * recupera la {@link List} di {@link QuotaAmmortamentoFiscale} per il report degli ammortamenti.
	 *
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return quote trovate
	 * @throws BeniAmmortizzabiliException
	 *             BeniAmmortizzabiliException
	 */
	List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamento(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) throws BeniAmmortizzabiliException;

	/**
	 * Restituisce la {@link List} di {@link QuotaAmmortamentoFiscale}.
	 *
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return List QuotaAmmortamentoFiscale
	 */
	List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamentoFiscali(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	// List<BeneAmmortizzabile> caricaBeniDaAmmortizzare();

	/**
	 * recupera la {@link List} {@link BeneAmmortizzabileConFigli} per la rubrica beni.
	 *
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return lista di beni trovati
	 */
	List<BeneAmmortizzabileConFigli> ricercaRubricaBeni(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	/**
	 * Restituisce una List di {@link VenditaBene}.
	 *
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return List VenditaBene
	 */
	List<VenditaBene> ricercaVenditeBeni(CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	/**
	 * Salva un <code>BeneAmmortizzabile</code>.
	 *
	 * @param beneAmmortizzabile
	 *            <code>BeneAmmortizzabile</code> da salvare
	 * @return Value Object del <code>BeneAmmortizzabile</code> salvato
	 */
	BeneAmmortizzabile salvaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile);

	/**
	 * Salva le settings dei beni ammortizzabili.
	 *
	 * @param beniSettings
	 *            settings da salvare
	 * @return settings salvate
	 */
	BeniSettings salvaBeniSettings(BeniSettings beniSettings);

	/**
	 * salva {@link Gruppo}.
	 *
	 * @param gruppo
	 *            gruppo da salvare
	 * @return Gruppo gruppo salvato
	 */
	Gruppo salvaGruppo(Gruppo gruppo);

	/**
	 * Salva una quota ammortamento civilistica.
	 *
	 * @param quotaAmmortamentoCivilistico
	 *            la quota da salvare
	 * @return QuotaAmmortamentoCivilistico
	 */
	QuotaAmmortamentoCivilistico salvaQuotaAmmortamentoCivilistico(
			QuotaAmmortamentoCivilistico quotaAmmortamentoCivilistico);

	/**
	 * Salva una quota ammortamento fiscale.
	 *
	 * @param quotaAmmortamentoFiscale
	 *            la quota da salvare
	 * @return QuotaAmmortamentoFiscale salvata
	 */
	QuotaAmmortamentoFiscale salvaQuotaAmmortamentoFiscale(QuotaAmmortamentoFiscale quotaAmmortamentoFiscale);

	// List<BeneAmmortizzabile> caricaBeniDaAmmortizzare();

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
	 * salva {@link SottoSpecie}.
	 *
	 * @param sottoSpecie
	 *            sottospecie da salvare
	 * @return sottospecie salvata
	 */
	SottoSpecie salvaSottoSpecie(SottoSpecie sottoSpecie);

	/**
	 * salva {@link Specie}.
	 *
	 * @param specie
	 *            specie da salvare
	 * @return specie salvata
	 */
	Specie salvaSpecie(Specie specie);

	/**
	 * salva {@link TipologiaEliminazione}.
	 *
	 * @param tipologiaEliminazione
	 *            tipologia da salvare
	 * @return tipologia salvata
	 */
	TipologiaEliminazione salvaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione);

	/**
	 * salva {@link Ubicazione}.
	 *
	 * @param ubicazione
	 *            ubicazione da salvare
	 * @return ubicazione salvata
	 */
	Ubicazione salvaUbicazione(Ubicazione ubicazione);

	/**
	 * Salva una <code>ValutazioneBene</code>.
	 *
	 * @param valutazioneBene
	 *            la <code>ValutazioneBene</code> da salvare
	 * @return <code>ValutazioneBene</code> salvata
	 */
	ValutazioneBene salvaValutazioneBene(ValutazioneBene valutazioneBene);

	/**
	 * Salva una <code>VenditaBene</code>.
	 *
	 * @param venditaBene
	 *            Vendita da salvare
	 * @return vendita salvata
	 * @throws VenditaInAnnoConsolidatoException
	 *             se la vendita è in un anno consolidato
	 * @throws VenditaInAnnoSimulatoException
	 *             se la vendita è in un anno simulato
	 */
	VenditaBene salvaVenditaBene(VenditaBene venditaBene) throws VenditaInAnnoConsolidatoException,
	VenditaInAnnoSimulatoException;

	/**
	 * salva una {@link VenditaBene} e esegue il ricalcolo delle simulazioni collegate.
	 *
	 * @param venditaBene
	 *            vendita del bene
	 * @param forzaRicalcolo
	 *            forza il ricalcolo
	 * @return Vendita salvata
	 */
	VenditaBene salvaVenditaBene(VenditaBene venditaBene, boolean forzaRicalcolo);

	/**
	 * Imposta il gruppo predefinito per l'azienda loggata togliendo l'eventuale associazione al gruppo precedente
	 * selezionato.
	 *
	 * @param gruppo
	 *            Gruppo da associare all'azienda
	 * @return Gruppo associato all'azienda,
	 */
	Gruppo settaGruppoAzienda(Gruppo gruppo);

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
