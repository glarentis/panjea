package it.eurotn.panjea.beniammortizzabili2.service;

import it.eurotn.dao.exception.VincoloException;
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
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.BeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.BeniAmmortizzabiliSettingsManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.ReportBeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.SimulazioneAmmortamentoManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.TabelleBeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.service.interfaces.BeniAmmortizzabiliService;
import it.eurotn.panjea.beniammortizzabili2.util.SituazioneBene;
import it.eurotn.panjea.beniammortizzabili2.util.registrobeni.RegistroBene;
import it.eurotn.panjea.beniammortizzabili2.util.venditeannuali.VenditaAnnualeBene;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author adriano
 * @version 1.0, 02/ott/07
 *
 */
@Stateless(name = "Panjea.BeniAmmortizzabiliService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.BeniAmmortizzabiliService")
public class BeniAmmortizzabiliServiceBean implements BeniAmmortizzabiliService {

	@EJB
	protected TabelleBeniAmmortizzabiliManager tabelleBeniAmmortizzabiliManager;

	@EJB
	protected BeniAmmortizzabiliManager beniAmmortizzabiliManager;

	@EJB
	protected SimulazioneAmmortamentoManager simulazioneAmmortamentoManager;

	@EJB
	protected ReportBeniAmmortizzabiliManager reportBeniAmmortizzabiliManager;

	@EJB
	protected BeniAmmortizzabiliSettingsManager beniAmmortizzabiliSettingsManager;

	@Resource
	protected SessionContext sessionContext;

	@Override
	public void aggiornaBeniInAmmortamento() {
		simulazioneAmmortamentoManager.aggiornaBeniInAmmortamento();
	}

	@Override
	@TransactionTimeout(value = 7200)
	public void calcolaSimulazioniBene(BeneAmmortizzabile bene, Integer anno) {
		simulazioneAmmortamentoManager.calcolaSimulazioniBene(bene, anno);
	}

	@Override
	public void cancellaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) throws VincoloException {
		beniAmmortizzabiliManager.cancellaBeneAmmortizzabile(beneAmmortizzabile);
	}

	@Override
	public void cancellaSimulazione(Simulazione simulazione) {
		simulazioneAmmortamentoManager.cancellaSimulazione(simulazione);
	}

	@Override
	public void cancellaSottoSpecie(SottoSpecie sottoSpecie) {
		tabelleBeniAmmortizzabiliManager.cancellaSottoSpecie(sottoSpecie);
	}

	@Override
	public void cancellaSpecie(Specie specie) {
		tabelleBeniAmmortizzabiliManager.cancellaSpecie(specie);
	}

	@Override
	public void cancellaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione) {
		tabelleBeniAmmortizzabiliManager.cancellaTipologiaEliminazione(tipologiaEliminazione);
	}

	@Override
	public void cancellaUbicazione(Ubicazione ubicazione) {
		tabelleBeniAmmortizzabiliManager.cancellaUbicazione(ubicazione);
	}

	@Override
	public void cancellaValutazioneBene(ValutazioneBene valutazioneBene) {
		beniAmmortizzabiliManager.cancellaValutazioneBene(valutazioneBene);
	}

	@Override
	public void cancellaVenditaBene(VenditaBene venditaBene) throws VenditaInAnnoConsolidatoException,
			VenditaInAnnoSimulatoException {
		Date d = venditaBene.getDataVendita();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int year = calendar.get(Calendar.YEAR);
		// HACK per evitare referenze circolari tra beans simulazione e
		// beniammortizz.
		verificaAnnoVenditaBene(year);
		beniAmmortizzabiliManager.cancellaVenditaBene(venditaBene);
	}

	@Override
	public BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		return beniAmmortizzabiliManager.caricaBeneAmmortizzabile(beneAmmortizzabile);
	}

	@Override
	public BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabileLite beneAmmortizzabileLite) {
		return beniAmmortizzabiliManager.caricaBeneAmmortizzabile(beneAmmortizzabileLite);
	}

	@Override
	public List<BeneAmmortizzabile> caricaBeniAmmortizzabiliAzienda() {
		return beniAmmortizzabiliManager.caricaBeniAmmortizzabiliAzienda();
	}

	@Override
	public List<BeneAmmortizzabile> caricaBeniAmmortizzabiliFigli(BeneAmmortizzabile beneAmmortizzabilePadre) {
		return beniAmmortizzabiliManager.caricaBeniAmmortizzabiliFigli(beneAmmortizzabilePadre);
	}

	@Override
	public List<BeneAmmortizzabileLite> caricaBeniDaAmmortizzareLite(Date date) {
		return beniAmmortizzabiliManager.caricaBeniDaAmmortizzareLite(date);
	}

	@Override
	public BeniSettings caricaBeniSettings() {
		return beniAmmortizzabiliSettingsManager.caricaBeniSettings();
	}

	@Override
	public List<Gruppo> caricaGruppi(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException {
		return tabelleBeniAmmortizzabiliManager.caricaGruppi(fieldSearch, valueSearch);
	}

	@Override
	public Gruppo caricaGruppoAzienda() {
		return tabelleBeniAmmortizzabiliManager.caricaGruppoAzienda();
	}

	@Override
	public PoliticaCalcolo caricaPoliticaCalcolo(PoliticaCalcolo politicaCalcolo) {
		return simulazioneAmmortamentoManager.caricaPoliticaCalcolo(politicaCalcolo);
	}

	@Override
	public PoliticaCalcoloBene caricaPoliticaCalcoloBeneByQuotaAmmortamento(QuotaAmmortamento quotaAmmortamento) {
		return simulazioneAmmortamentoManager.caricaPoliticaCalcoloBeneByQuotaAmmortamento(quotaAmmortamento);
	}

	@Override
	public List<QuotaAmmortamentoCivilistico> caricaQuoteAmmortamentoCivilistiche(
			BeneAmmortizzabile beneAmmortizzabile, boolean isConsolidate) {
		return beniAmmortizzabiliManager.caricaQuoteAmmortamentoCivilistiche(beneAmmortizzabile, isConsolidate);
	}

	@Override
	public List<QuotaAmmortamentoFiscale> caricaQuoteAmmortamentoFiscali(BeneAmmortizzabile beneAmmortizzabile,
			boolean isConsolidate) {
		return beniAmmortizzabiliManager.caricaQuoteAmmortamentoFiscali(beneAmmortizzabile, isConsolidate);
	}

	@Override
	public List<RegistroBene> caricaRegistroBeni(Map<String, Object> parameters) {
		return reportBeniAmmortizzabiliManager.caricaRegistroBeni(parameters);
	}

	@Override
	public Simulazione caricaSimulazione(Simulazione simulazione) {
		return simulazioneAmmortamentoManager.caricaSimulazione(simulazione);
	}

	@Override
	public List<Simulazione> caricaSimulazioni() {
		return simulazioneAmmortamentoManager.caricaSimulazioni();
	}

	@Override
	public List<Simulazione> caricaSimulazioniAnno(Integer anno) {
		return simulazioneAmmortamentoManager.caricaSimulazioniAnno(anno);
	}

	@Override
	public List<SituazioneBene> caricaSituazioneBeni(Map<Object, Object> parameters) {
		return reportBeniAmmortizzabiliManager.caricaSituazioneBeni(parameters);
	}

	@Override
	public SottoSpecie caricaSottoSpecie(Integer id) throws BeniAmmortizzabiliException {
		return tabelleBeniAmmortizzabiliManager.caricaSottoSpecie(id);
	}

	@Override
	public List<SottoSpecie> caricaSottoSpecie(String fieldSearch, String valueSearch)
			throws BeniAmmortizzabiliException {
		return tabelleBeniAmmortizzabiliManager.caricaSottoSpecie(fieldSearch, valueSearch);
	}

	@Override
	public List<Specie> caricaSpeci(String fieldSearch, String valueSearch) throws BeniAmmortizzabiliException {
		return tabelleBeniAmmortizzabiliManager.caricaSpeci(fieldSearch, valueSearch);
	}

	@Override
	public List<Specie> caricaSpecie(Gruppo gruppo) throws BeniAmmortizzabiliException {
		return tabelleBeniAmmortizzabiliManager.caricaSpecie(gruppo);
	}

	@Override
	public Specie caricaSpecie(Integer id) throws BeniAmmortizzabiliException {
		return tabelleBeniAmmortizzabiliManager.caricaSpecie(id);
	}

	@Override
	public TipologiaEliminazione caricaTipologiaEliminazione(Integer id) throws BeniAmmortizzabiliException {
		return tabelleBeniAmmortizzabiliManager.caricaTipologiaEliminazione(id);
	}

	@Override
	public List<TipologiaEliminazione> caricaTipologieEliminazione(String codice) {
		return tabelleBeniAmmortizzabiliManager.caricaTipologieEliminazione(codice);
	}

	@Override
	public List<Ubicazione> caricaUbicazioni(String codice) {
		return tabelleBeniAmmortizzabiliManager.caricaUbicazioni(codice);
	}

	@Override
	public List<ValutazioneBene> caricaValutazioniBene(BeneAmmortizzabile beneAmmortizzabile) {
		return beniAmmortizzabiliManager.caricaValutazioniBene(beneAmmortizzabile);
	}

	@Override
	public List<VenditaAnnualeBene> caricaVenditeAnnualiBeniPadri(Map<Object, Object> parameters) {
		return reportBeniAmmortizzabiliManager.caricaVenditeAnnualiBeniPadri(parameters);
	}

	@Override
	public List<VenditaBene> caricaVenditeBene(BeneAmmortizzabile beneAmmortizzabile) {
		return beniAmmortizzabiliManager.caricaVenditeBene(beneAmmortizzabile);
	}

	@Override
	@TransactionTimeout(value = 7200)
	public void consolidaSimulazione(Simulazione simulazione) throws MancatoConsolidamentoAnnoPrecedenteException {
		simulazioneAmmortamentoManager.consolidaSimulazione(simulazione);
	}

	@Override
	public ValutazioneBene creaNuovaValutazioneBene(BeneAmmortizzabile beneAmmortizzabile) {
		return beniAmmortizzabiliManager.creaNuovaValutazioneBene(beneAmmortizzabile);
	}

	@Override
	public VenditaBene creaNuovaVenditaBene(BeneAmmortizzabile beneAmmortizzabile) {
		return beniAmmortizzabiliManager.creaVenditaBene(beneAmmortizzabile);
	}

	@Override
	public Simulazione creaSimulazione(String descrizione, Date data, Simulazione simulazioneRiferimento) {
		return simulazioneAmmortamentoManager.creaSimulazione(descrizione, data, simulazioneRiferimento);
	}

	@Override
	public Simulazione creaSimulazione(String descrizione, Date data, Simulazione simulazioneRiferimento,
			Integer idSimulazioneOld) {
		return simulazioneAmmortamentoManager.creaSimulazione(descrizione, data, simulazioneRiferimento,
				idSimulazioneOld);
	}

	@Override
	public void fillDataBaseBeniAmmortamento() {
		beniAmmortizzabiliManager.fillDataBaseBeniAmmortamento();
	}

	@Override
	public List<BeneAmmortizzabileConFigli> ricercaBeniAcquistati(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		return reportBeniAmmortizzabiliManager.ricercaBeniAcquistati(criteriaRicercaBeniAmmortizzabili);
	}

	@Override
	public List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(Map<String, Object> parametri) {
		return beniAmmortizzabiliManager.ricercaBeniAmmortizzabili(parametri);
	}

	@Override
	public List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(String fieldSearch, String valueSearch) {
		return beniAmmortizzabiliManager.ricercaBeniAmmortizzabili(fieldSearch, valueSearch);
	}

	@Override
	public List<BeneAmmortizzabileConFigli> ricercaBeniAmmortizzabiliPerFornitore(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		return beniAmmortizzabiliManager.ricercaBeniAmmortizzabiliPerFornitore(criteriaRicercaBeniAmmortizzabili);
	}

	@Override
	public List<BeneAmmortizzabileConFigli> ricercaBeniAmmortizzabiliPerUbicazione(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		return beniAmmortizzabiliManager.ricercaBeniAmmortizzabiliPerUbicazione(criteriaRicercaBeniAmmortizzabili);
	}

	@Override
	public List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamento(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) throws BeniAmmortizzabiliException {
		return reportBeniAmmortizzabiliManager.ricercaQuoteAmmortamento(criteriaRicercaBeniAmmortizzabili);
	}

	@Override
	public List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamentoFiscali(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		return beniAmmortizzabiliManager.ricercaQuoteAmmortamentoFiscali(criteriaRicercaBeniAmmortizzabili);
	}

	@Override
	public List<BeneAmmortizzabileConFigli> ricercaRubricaBeni(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		return reportBeniAmmortizzabiliManager.ricercaRubricaBeni(criteriaRicercaBeniAmmortizzabili);
	}

	@Override
	public List<VenditaBene> ricercaVenditeBeni(CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		return beniAmmortizzabiliManager.ricercaVenditeBeni(criteriaRicercaBeniAmmortizzabili);
	}

	@Override
	public BeneAmmortizzabile salvaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		return beniAmmortizzabiliManager.salvaBeneAmmortizzabile(beneAmmortizzabile);
	}

	@Override
	public BeniSettings salvaBeniSettings(BeniSettings beniSettings) {
		return beniAmmortizzabiliSettingsManager.salvaBeniSettings(beniSettings);
	}

	@Override
	public Gruppo salvaGruppo(Gruppo gruppo) {
		return tabelleBeniAmmortizzabiliManager.salvaGruppo(gruppo);
	}

	@Override
	public QuotaAmmortamentoCivilistico salvaQuotaAmmortamentoCivilistico(
			QuotaAmmortamentoCivilistico quotaAmmortamentoCivilistico) {
		return beniAmmortizzabiliManager.salvaQuotaAmmortamentoCivilistico(quotaAmmortamentoCivilistico);
	}

	@Override
	public QuotaAmmortamentoFiscale salvaQuotaAmmortamentoFiscale(QuotaAmmortamentoFiscale quotaAmmortamentoFiscale) {
		return beniAmmortizzabiliManager.salvaQuotaAmmortamentoFiscale(quotaAmmortamentoFiscale);
	}

	@Override
	@TransactionTimeout(value = 7200)
	public Simulazione salvaSimulazione(Simulazione simulazione) throws AreeContabiliSimulazioneException {
		return simulazioneAmmortamentoManager.salvaSimulazione(simulazione);
	}

	@Override
	@TransactionTimeout(value = 7200)
	public Simulazione salvaSimulazione(Simulazione simulazione, boolean forzaCancellazioneAC)
			throws AreeContabiliSimulazioneException {
		return simulazioneAmmortamentoManager.salvaSimulazione(simulazione, forzaCancellazioneAC);
	}

	@Override
	public SottoSpecie salvaSottoSpecie(SottoSpecie sottoSpecie) {
		return tabelleBeniAmmortizzabiliManager.salvaSottoSpecie(sottoSpecie);
	}

	@Override
	public Specie salvaSpecie(Specie specie) {
		return tabelleBeniAmmortizzabiliManager.salvaSpecie(specie);
	}

	@Override
	public TipologiaEliminazione salvaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione) {
		return tabelleBeniAmmortizzabiliManager.salvaTipologiaEliminazione(tipologiaEliminazione);
	}

	@Override
	public Ubicazione salvaUbicazione(Ubicazione ubicazione) {
		return tabelleBeniAmmortizzabiliManager.salvaUbicazione(ubicazione);
	}

	@Override
	public ValutazioneBene salvaValutazioneBene(ValutazioneBene valutazioneBene) {
		return beniAmmortizzabiliManager.salvaValutazioneBene(valutazioneBene);
	}

	/*
	 * @see it.eurotn.panjea.beniammortizzabili2.service.interfaces.
	 * BeniAmmortizzabiliService#salvaVenditaBene(it.eurotn. panjea.beniammortizzabili2.domain.VenditaBene) per
	 * sistemare problema referenza circolare
	 */@Override
	public VenditaBene salvaVenditaBene(VenditaBene venditaBene) throws VenditaInAnnoConsolidatoException,
			VenditaInAnnoSimulatoException {
		Date d = venditaBene.getDataVendita();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int year = calendar.get(Calendar.YEAR);
		// HACK per evitare referenze circolari tra beans simulazione e
		// beniammortizz.
		verificaAnnoVenditaBene(year);
		return beniAmmortizzabiliManager.salvaVenditaBene(venditaBene);
	}

	@Override
	public VenditaBene salvaVenditaBene(VenditaBene venditaBene, boolean forzaRicalcolo) {
		VenditaBene venditaBeneSalvata = beniAmmortizzabiliManager.salvaVenditaBene(venditaBene, forzaRicalcolo);
		return venditaBeneSalvata;
	}

	@Override
	public Gruppo settaGruppoAzienda(Gruppo gruppo) {
		return tabelleBeniAmmortizzabiliManager.settaGruppoAzienda(gruppo);
	}

	@Override
	public void verificaAnnoVenditaBene(Integer annoVendita) throws VenditaInAnnoConsolidatoException,
			VenditaInAnnoSimulatoException {
		simulazioneAmmortamentoManager.verificaAnnoVenditaBene(annoVendita);
	}

	@Override
	public Object verificaNuovaSimulazione(Integer anno) {
		return simulazioneAmmortamentoManager.verificaNuovaSimulazione(anno);
	}

}
