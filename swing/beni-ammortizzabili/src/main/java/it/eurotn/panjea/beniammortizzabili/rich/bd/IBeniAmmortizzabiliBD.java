package it.eurotn.panjea.beniammortizzabili.rich.bd;

import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliSimulazioneException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoConsolidatoException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoSimulatoException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.BeniSettings;
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
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IBeniAmmortizzabiliBD {

	void cancellaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile);

	void cancellaBeneAmmortizzabile(BeneAmmortizzabileLite beneAmmortizzabileLite);

	void cancellaSimulazione(Simulazione simulazione);

	void cancellaSottoSpecie(SottoSpecie sottoSpecie);

	void cancellaSpecie(Specie specie);

	void cancellaTipologieEliminazione(TipologiaEliminazione tipologiaEliminazione);

	void cancellaUbicazione(Ubicazione ubicazione);

	void cancellaValutazioneBene(ValutazioneBene valutazioneBene);

	void cancellaVenditaBene(VenditaBene venditaBene) throws VenditaInAnnoSimulatoException,
	VenditaInAnnoConsolidatoException;

	BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile);

	BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabileLite beneAmmortizzabileLite);

	List<BeneAmmortizzabile> caricaBeniAmmortizzabiliFigli(BeneAmmortizzabile beneAmmortizzabilePadre);

	/**
	 * Carica le settings dei beni ammortizzabili.
	 *
	 * @return settings
	 */
	BeniSettings caricaBeniSettings();

	@AsyncMethodInvocation
	List<Gruppo> caricaGruppi(String fieldSearch, String valueSearch);

	@AsyncMethodInvocation
	Gruppo caricaGruppoAzienda();

	PoliticaCalcolo caricaPoliticaCalcolo(PoliticaCalcolo politicaCalcolo);

	PoliticaCalcoloBene caricaPoliticaCalcoloBeneByQuotaAmmortamento(QuotaAmmortamento quotaAmmortamento);

	@AsyncMethodInvocation
	List<QuotaAmmortamentoCivilistico> caricaQuoteAmmortamentoCivilisticheConsolidate(
			BeneAmmortizzabile beneAmmortizzabile);

	@AsyncMethodInvocation
	List<QuotaAmmortamentoCivilistico> caricaQuoteAmmortamentoCivilisticheNonConsolidate(
			BeneAmmortizzabile beneAmmortizzabile);

	@AsyncMethodInvocation
	List<QuotaAmmortamentoFiscale> caricaQuoteAmmortamentoFiscaliConsolidate(BeneAmmortizzabile beneAmmortizzabile);

	@AsyncMethodInvocation
	List<QuotaAmmortamentoFiscale> caricaQuoteAmmortamentoFiscaliNonConsolidate(BeneAmmortizzabile beneAmmortizzabile);

	@AsyncMethodInvocation
	Simulazione caricaSimulazione(Simulazione simulazione);

	@AsyncMethodInvocation
	List<Simulazione> caricaSimulazioni();

	SottoSpecie caricaSottoSpecie(Integer id);

	@AsyncMethodInvocation
	List<SottoSpecie> caricaSottoSpecie(String fieldSearch, String valueSearch);

	@AsyncMethodInvocation
	List<Specie> caricaSpeci(String fieldSearch, String valueSearch);

	List<Specie> caricaSpecie(Gruppo gruppo);

	List<TipologiaEliminazione> caricaTipologieEliminazione(String codice);

	List<Ubicazione> caricaUBicazioni(String codice);

	@AsyncMethodInvocation
	List<ValutazioneBene> caricaValutazioniBene(BeneAmmortizzabile beneAmmortizzabile);

	@AsyncMethodInvocation
	List<VenditaBene> caricaVenditeBene(BeneAmmortizzabile beneAmmortizzabile);

	@AsyncMethodInvocation
	void consolidaSimulazione(Simulazione simulazione);

	ValutazioneBene creaNuovaValutazioneBene(BeneAmmortizzabile beneAmmortizzabile);

	VenditaBene creaNuovaVenditaBene(BeneAmmortizzabile beneAmmortizzabile);

	@AsyncMethodInvocation
	Simulazione creaSimulazione(String descrizione, Date data, Simulazione simulazioneRiferimento);

	@SuppressWarnings("unchecked")
	@AsyncMethodInvocation
	List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(Map parametri);

	/**
	 * Restituisce una List di {@link BeneAmmortizzabileLite} filtrata con i parametri ricevuti.
	 *
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return List di BeneAmmortizzabileLite
	 */
	@AsyncMethodInvocation
	List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(String fieldSearch, String valueSearch);

	BeneAmmortizzabile salvaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile);

	/**
	 * Salva le settings dei beni ammortizzabili.
	 *
	 * @param beniSettings
	 *            settings da salvare
	 * @return settings salvate
	 */
	BeniSettings salvaBeniSettings(BeniSettings beniSettings);

	QuotaAmmortamentoCivilistico salvaQuotaAmmortamentoCivilistico(
			QuotaAmmortamentoCivilistico quotaAmmortamentoCivilistico);

	QuotaAmmortamentoFiscale salvaQuotaAmmortamentoFiscale(QuotaAmmortamentoFiscale quotaAmmortamentoFiscale);

	@AsyncMethodInvocation
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
	 */
	Simulazione salvaSimulazione(Simulazione simulazione, boolean forzaCancellazioneAC);

	SottoSpecie salvaSottoSpecie(SottoSpecie sottoSpecie);

	Specie salvaSpecie(Specie specie);

	TipologiaEliminazione salvaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione);

	Ubicazione salvaUbicazione(Ubicazione ubicazione);

	ValutazioneBene salvaValutazioneBene(ValutazioneBene valutazioneBene);

	VenditaBene salvaVenditaBene(VenditaBene venditaBene) throws VenditaInAnnoSimulatoException;

	VenditaBene salvaVenditaBene(VenditaBene venditaBene, boolean forzaRicalcolo);

	Gruppo settaGruppoAzienda(Gruppo gruppo);

	Object verificaNuovaSimulazione(Integer anno);

}
