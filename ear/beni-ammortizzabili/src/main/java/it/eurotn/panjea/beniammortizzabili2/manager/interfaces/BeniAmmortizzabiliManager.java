package it.eurotn.panjea.beniammortizzabili2.manager.interfaces;

import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.beniammortizzabili.exception.BeniAmmortizzabiliException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoConsolidatoException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoSimulatoException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileConFigli;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoCivilistico;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.panjea.beniammortizzabili2.domain.ValutazioneBene;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface BeniAmmortizzabiliManager {

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
	 */
	void cancellaVenditaBene(VenditaBene venditaBene);

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
	 * @param data
	 *            data
	 * @return <code>List</code> di <code>BeneAmmortizzabile</code> caricati.
	 */
	List<BeneAmmortizzabileLite> caricaBeniDaAmmortizzareLite(java.util.Date data);

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
	 * Carica tutte le rivalutazioni/svalutazioni di un bene.
	 * 
	 * @param beneAmmortizzabile
	 *            il bene ammortizzabile
	 * @return <code>List</code> di <code>Valutazione</code> caricate per il bene
	 */
	List<ValutazioneBene> caricaValutazioniBene(BeneAmmortizzabile beneAmmortizzabile);

	/**
	 * Carica tutte le <code>VenditeBene</code> del bene ammortizzabile.
	 * 
	 * @param beneAmmortizzabile
	 *            il <code>BeneAmmortizzabile</code>
	 * @return <code>List</code> di <code>VenditeBene</code> caricate
	 */
	List<VenditaBene> caricaVenditeBene(BeneAmmortizzabile beneAmmortizzabile);

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
	VenditaBene creaVenditaBene(BeneAmmortizzabile beneAmmortizzabile);

	/**
	 * Popola il database con valori di default.
	 */
	void fillDataBaseBeniAmmortamento();

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
	 * Restituisce la {@link List} di {@link QuotaAmmortamentoFiscale}.
	 * 
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return List QuotaAmmortamentoFiscale
	 */
	List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamentoFiscali(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	/**
	 * Restituisce una List di {@link VenditaBene}.
	 * 
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            parametri di ricerca
	 * @return List VenditaBene
	 */
	List<VenditaBene> ricercaVenditeBeni(CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili);

	// List<BeneAmmortizzabile> caricaBeniDaAmmortizzare();

	/**
	 * Salva un <code>BeneAmmortizzabile</code>.
	 * 
	 * @param beneAmmortizzabile
	 *            <code>BeneAmmortizzabile</code> da salvare
	 * @return Value Object del <code>BeneAmmortizzabile</code> salvato
	 */
	BeneAmmortizzabile salvaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile);

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

}
