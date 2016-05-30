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
import it.eurotn.panjea.beniammortizzabili2.service.interfaces.BeniAmmortizzabiliService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class BeniAmmortizzabiliBD extends AbstractBaseBD implements IBeniAmmortizzabiliBD {

	private static Logger logger = Logger.getLogger(BeniAmmortizzabiliBD.class);

	private BeniAmmortizzabiliService beniAmmortizzabiliService;

	/**
	 * Costruttore di default.
	 */
	public BeniAmmortizzabiliBD() {
		super();
	}

	@Override
	public void cancellaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		start("cancellaBeneAmmortizzabile");
		logger.debug("--> Enter cancellaBeneAmmortizzabile");

		try {
			beniAmmortizzabiliService.cancellaBeneAmmortizzabile(beneAmmortizzabile);
		} catch (Exception e) {
			end("cancellaBeneAmmortizzabile");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit cancellaBeneAmmortizzabile");
		end("cancellaBeneAmmortizzabile");
	}

	@Override
	public void cancellaBeneAmmortizzabile(BeneAmmortizzabileLite beneAmmortizzabileLite) {
		logger.debug("--> Enter cancellaBeneAmmortizzabile");
		BeneAmmortizzabile beneAmmortizzabile = new BeneAmmortizzabile();
		beneAmmortizzabile.setId(beneAmmortizzabileLite.getId());
		beneAmmortizzabile.setVersion(beneAmmortizzabileLite.getVersion());
		cancellaBeneAmmortizzabile(beneAmmortizzabile);
		logger.debug("--> Exit cancellaBeneAmmortizzabile");
	}

	@Override
	public void cancellaSimulazione(Simulazione simulazione) {
		start("cancellaSimulazione");
		logger.debug("--> Enter cancellaSimulazione");

		try {
			beniAmmortizzabiliService.cancellaSimulazione(simulazione);
		} catch (Exception e) {
			end("cancellaSimulazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("cancellaSimulazione");
		logger.debug("--> Exit cancellaSimulazione");
	}

	@Override
	public void cancellaSottoSpecie(SottoSpecie sottoSpecie) {
		start("cancellaSottoSpecie");
		logger.debug("--> Enter cancellaSottoSpecie");

		try {
			beniAmmortizzabiliService.cancellaSottoSpecie(sottoSpecie);
		} catch (Exception e) {
			end("cancellaSottoSpecie");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit cancellaSottoSpecie");
		end("cancellaSottoSpecie");
	}

	@Override
	public void cancellaSpecie(Specie specie) {
		start("cancellaSpecie");
		logger.debug("--> Enter cancellaSpecie");

		try {
			beniAmmortizzabiliService.cancellaSpecie(specie);
		} catch (Exception e) {
			end("cancellaSpecie");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit cancellaSpecie");
		end("cancellaSpecie");
	}

	@Override
	public void cancellaTipologieEliminazione(TipologiaEliminazione tipologiaEliminazione) {
		start("cancellaTipologieEliminazione");
		logger.debug("--> Enter cancellaTipologieEliminazione");

		try {
			beniAmmortizzabiliService.cancellaTipologiaEliminazione(tipologiaEliminazione);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione della tipologiaEliminazione " + tipologiaEliminazione);
			end("cancellaTipologieEliminazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit cancellaTipologieEliminazione");
		end("cancellaTipologieEliminazione");
	}

	@Override
	public void cancellaUbicazione(Ubicazione ubicazione) {
		start("cancellaUbicazione");
		logger.debug("--> Enter cancellaUbicazione");

		try {
			beniAmmortizzabiliService.cancellaUbicazione(ubicazione);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione dell'ubicazione " + ubicazione);
			end("cancellaUbicazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit cancellaUbicazione");
		end("cancellaUbicazione");
	}

	@Override
	public void cancellaValutazioneBene(ValutazioneBene valutazioneBene) {
		start("cancellaValutazioneBene");
		logger.debug("--> Enter cancellaValutazioneBene");

		try {
			beniAmmortizzabiliService.cancellaValutazioneBene(valutazioneBene);
		} catch (Exception e) {
			end("cancellaValutazioneBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("cancellaValutazioneBene");
		logger.debug("--> Exit cancellaValutazioneBene");
	}

	@Override
	public void cancellaVenditaBene(VenditaBene venditaBene) throws VenditaInAnnoSimulatoException,
	VenditaInAnnoConsolidatoException {
		start("cancellaVenditaBene");
		logger.debug("--> Enter cancellaVenditaBene");
		try {
			beniAmmortizzabiliService.cancellaVenditaBene(venditaBene);
		} catch (VenditaInAnnoConsolidatoException ex) {
			throw ex;
		} catch (VenditaInAnnoSimulatoException ex2) {
			throw ex2;
		} catch (Exception e) {
			end("cancellaVenditaBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		end("cancellaVenditaBene");
		logger.debug("--> Exit cancellaVenditaBene");
	}

	@Override
	public BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		start("caricaBeneAmmortizzabile");
		logger.debug("--> Enter caricaBeneAmmortizzabile");

		BeneAmmortizzabile beneAmmortizzabile2 = null;
		try {
			beneAmmortizzabile2 = beniAmmortizzabiliService.caricaBeneAmmortizzabile(beneAmmortizzabile);
		} catch (Exception e) {
			end("caricaBeneAmmortizzabile");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaBeneAmmortizzabile");
		end("caricaBeneAmmortizzabile");
		return beneAmmortizzabile2;
	}

	@Override
	public BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabileLite beneAmmortizzabileLite) {
		start("caricaBeneAmmortizzabile");
		logger.debug("--> Enter caricaBeneAmmortizzabile");
		BeneAmmortizzabile beneAmmortizzabile = null;
		try {
			beneAmmortizzabile = beniAmmortizzabiliService.caricaBeneAmmortizzabile(beneAmmortizzabileLite);
		} catch (Exception e) {
			end("caricaBeneAmmortizzabile");
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		logger.debug("--> Exit caricaBeneAmmortizzabile");
		end("caricaBeneAmmortizzabile");
		return beneAmmortizzabile;
	}

	@Override
	public List<BeneAmmortizzabile> caricaBeniAmmortizzabiliFigli(BeneAmmortizzabile beneAmmortizzabilePadre) {
		start("caricaBeniAmmortizzabiliFigli");
		logger.debug("--> Enter caricaBeniAmmortizzabiliFigli");

		List<BeneAmmortizzabile> listResult = new ArrayList<BeneAmmortizzabile>();
		try {
			listResult = beniAmmortizzabiliService.caricaBeniAmmortizzabiliFigli(beneAmmortizzabilePadre);
		} catch (Exception e) {
			end("caricaBeniAmmortizzabiliFigli");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaBeniAmmortizzabiliFigli");
		end("caricaBeniAmmortizzabiliFigli");
		return listResult;
	}

	@Override
	public BeniSettings caricaBeniSettings() {
		logger.debug("--> Enter caricaBeniSettings");
		start("caricaBeniSettings");

		BeniSettings beniSettings = null;
		try {
			beniSettings = beniAmmortizzabiliService.caricaBeniSettings();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaBeniSettings");
		}
		logger.debug("--> Exit caricaBeniSettings ");
		return beniSettings;
	}

	@Override
	public List<Gruppo> caricaGruppi(String fieldSearch, String valueSearch) {
		start("caricaGruppi");
		logger.debug("--> Enter caricaGruppi");

		List<Gruppo> listResult = new ArrayList<Gruppo>();
		try {
			listResult = beniAmmortizzabiliService.caricaGruppi(fieldSearch, valueSearch);
		} catch (Exception e) {
			end("caricaGruppi");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaGruppi");
		end("caricaGruppi");
		return listResult;
	}

	@Override
	public Gruppo caricaGruppoAzienda() {
		start("caricaGruppoAzienda");
		logger.debug("--> Enter caricaGruppoAzienda");

		Gruppo gruppo = null;
		try {
			gruppo = beniAmmortizzabiliService.caricaGruppoAzienda();
		} catch (Exception e) {
			end("caricaGruppoAzienda");
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		end("caricaGruppoAzienda");
		logger.debug("--> Exit caricaGruppoAzienda");
		return gruppo;
	}

	@Override
	public PoliticaCalcolo caricaPoliticaCalcolo(PoliticaCalcolo politicaCalcolo) {
		start("caricaPoliticaCalcolo");
		logger.debug("--> Enter caricaPoliticaCalcolo");

		PoliticaCalcolo politicaCalcoloCaricata = null;
		try {
			politicaCalcoloCaricata = beniAmmortizzabiliService.caricaPoliticaCalcolo(politicaCalcolo);
		} catch (Exception e) {
			end("caricaPoliticaCalcolo");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("caricaPoliticaCalcolo");
		logger.debug("--> Exit caricaPoliticaCalcolo");
		return politicaCalcoloCaricata;
	}

	@Override
	public PoliticaCalcoloBene caricaPoliticaCalcoloBeneByQuotaAmmortamento(QuotaAmmortamento quotaAmmortamento) {
		start("caricaPoliticaCalcoloBeneByQuotaAmmortamento");
		logger.debug("--> Enter caricaPoliticaCalcoloBeneByQuotaAmmortamento");

		PoliticaCalcoloBene politicaCalcoloBene = null;
		try {
			politicaCalcoloBene = beniAmmortizzabiliService
					.caricaPoliticaCalcoloBeneByQuotaAmmortamento(quotaAmmortamento);
		} catch (Exception e) {
			end("caricaPoliticaCalcoloBeneByQuotaAmmortamento");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("caricaPoliticaCalcoloBeneByQuotaAmmortamento");
		logger.debug("--> Exit caricaPoliticaCalcoloBeneByQuotaAmmortamento");
		return politicaCalcoloBene;
	}

	@Override
	public List<QuotaAmmortamentoCivilistico> caricaQuoteAmmortamentoCivilisticheConsolidate(
			BeneAmmortizzabile beneAmmortizzabile) {
		start("caricaQuoteAmmortamentoCivilisticheConsolidate");
		logger.debug("--> Enter caricaQuoteAmmortamentoCivilisticheConsolidate");

		List<QuotaAmmortamentoCivilistico> listResult = new ArrayList<QuotaAmmortamentoCivilistico>();
		try {
			listResult = beniAmmortizzabiliService.caricaQuoteAmmortamentoCivilistiche(beneAmmortizzabile, true);
		} catch (Exception e) {
			end("caricaQuoteAmmortamentoCivilisticheConsolidate");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaQuoteAmmortamentoCivilisticheConsolidate");
		end("caricaQuoteAmmortamentoCivilisticheConsolidate");
		return listResult;
	}

	@Override
	public List<QuotaAmmortamentoCivilistico> caricaQuoteAmmortamentoCivilisticheNonConsolidate(
			BeneAmmortizzabile beneAmmortizzabile) {
		start("caricaQuoteAmmortamentoCivilisticheNonConsolidate");
		logger.debug("--> Enter caricaQuoteAmmortamentoCivilisticheNonConsolidate");

		List<QuotaAmmortamentoCivilistico> listResult = new ArrayList<QuotaAmmortamentoCivilistico>();
		try {
			listResult = beniAmmortizzabiliService.caricaQuoteAmmortamentoCivilistiche(beneAmmortizzabile, false);
		} catch (Exception e) {
			end("caricaQuoteAmmortamentoCivilisticheNonConsolidate");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaQuoteAmmortamentoCivilisticheNonConsolidate");
		end("caricaQuoteAmmortamentoCivilisticheNonConsolidate");
		return listResult;
	}

	@Override
	public List<QuotaAmmortamentoFiscale> caricaQuoteAmmortamentoFiscaliConsolidate(
			BeneAmmortizzabile beneAmmortizzabile) {
		start("caricaQuoteAmmortamentoFiscaliConsolidate");
		logger.debug("--> Enter caricaQuoteAmmortamentoFiscaliConsolidate");

		List<QuotaAmmortamentoFiscale> listResult = new ArrayList<QuotaAmmortamentoFiscale>();
		try {
			listResult = beniAmmortizzabiliService.caricaQuoteAmmortamentoFiscali(beneAmmortizzabile, true);
		} catch (Exception e) {
			end("caricaQuoteAmmortamentoFiscaliConsolidate");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaQuoteAmmortamentoFiscaliConsolidate");
		end("caricaQuoteAmmortamentoFiscaliConsolidate");
		return listResult;
	}

	@Override
	public List<QuotaAmmortamentoFiscale> caricaQuoteAmmortamentoFiscaliNonConsolidate(
			BeneAmmortizzabile beneAmmortizzabile) {
		start("caricaQuoteAmmortamentoFiscaliNonConsolidate");
		logger.debug("--> Enter caricaQuoteAmmortamentoFiscaliNonConsolidate");

		List<QuotaAmmortamentoFiscale> listResult = new ArrayList<QuotaAmmortamentoFiscale>();
		try {
			listResult = beniAmmortizzabiliService.caricaQuoteAmmortamentoFiscali(beneAmmortizzabile, false);
		} catch (Exception e) {
			end("caricaQuoteAmmortamentoFiscaliNonConsolidate");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaQuoteAmmortamentoFiscaliNonConsolidate");
		end("caricaQuoteAmmortamentoFiscaliNonConsolidate");
		return listResult;
	}

	@Override
	public Simulazione caricaSimulazione(Simulazione simulazione) {
		start("caricaSimulazione");
		logger.debug("--> Enter caricaSimulazione");

		Simulazione simulazione2 = null;
		try {
			simulazione2 = beniAmmortizzabiliService.caricaSimulazione(simulazione);
		} catch (Exception e) {
			end("caricaSimulazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("caricaSimulazione");
		logger.debug("--> Exit caricaSimulazione");
		return simulazione2;
	}

	@Override
	public List<Simulazione> caricaSimulazioni() {
		start("caricaSimulazioni");
		logger.debug("--> Enter caricaSimulazioni");

		List<Simulazione> listResult = new ArrayList<Simulazione>();
		try {
			listResult = beniAmmortizzabiliService.caricaSimulazioni();
		} catch (Exception e) {
			end("caricaSimulazioni");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("caricaSimulazioni");
		logger.debug("--> Exit caricaSimulazioni");
		return listResult;
	}

	@Override
	public SottoSpecie caricaSottoSpecie(Integer id) {
		start("caricaSottoSpecie");
		logger.debug("--> Enter caricaSottoSpecie");

		SottoSpecie sottoSpecie = null;
		try {
			sottoSpecie = beniAmmortizzabiliService.caricaSottoSpecie(id);
		} catch (Exception e) {
			end("caricaSottoSpecie");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("caricaSottoSpecie");
		logger.debug("--> Exit caricaSottoSpecie");
		return sottoSpecie;
	}

	@Override
	public List<SottoSpecie> caricaSottoSpecie(String fieldSearch, String valueSearch) {
		start("caricaSottoSpecie");
		logger.debug("--> Enter caricaSottoSpecie");

		List<SottoSpecie> listResult = new ArrayList<SottoSpecie>();
		try {
			listResult = beniAmmortizzabiliService.caricaSottoSpecie(fieldSearch, valueSearch);
		} catch (Exception e) {
			end("caricaSottoSpecie");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit ricercaBeniAmmortizzabili");
		end("caricaSottoSpecie");
		return listResult;
	}

	@Override
	public List<Specie> caricaSpeci(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaSpecieAzienda");
		List<Specie> specie = null;
		try {
			specie = beniAmmortizzabiliService.caricaSpeci(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		return specie;
	}

	@Override
	public List<Specie> caricaSpecie(Gruppo gruppo) {
		start("caricaSpecie");
		logger.debug("--> Enter caricaSpecie");

		List<Specie> listResult = new ArrayList<Specie>();
		try {
			listResult = beniAmmortizzabiliService.caricaSpecie(gruppo);
			logger.debug("--> Caricate " + listResult.size() + " specie.");
		} catch (Exception e) {
			end("caricaSpecie");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaSpecie");
		end("caricaSpecie");
		return listResult;
	}

	@Override
	public List<TipologiaEliminazione> caricaTipologieEliminazione(String codice) {
		start("caricaTipologieEliminazione");
		logger.debug("--> Enter caricaTipologieEliminazione");

		List<TipologiaEliminazione> listResult = null;
		try {
			listResult = beniAmmortizzabiliService.caricaTipologieEliminazione(codice);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento delle tipologieEliminazione", e);
			end("caricaTipologieEliminazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaTipologieEliminazione");
		end("caricaTipologieEliminazione");
		return listResult;
	}

	@Override
	public List<Ubicazione> caricaUBicazioni(String codice) {
		start("caricaUBicazioni");
		logger.debug("--> Enter caricaUBicazioni");

		List<Ubicazione> listResult = null;
		try {
			listResult = beniAmmortizzabiliService.caricaUbicazioni(codice);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento delle ubicazioni", e);
			end("caricaUBicazioni");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaUBicazioni");
		end("caricaUBicazioni");
		return listResult;
	}

	@Override
	public List<ValutazioneBene> caricaValutazioniBene(BeneAmmortizzabile beneAmmortizzabile) {
		start("caricaValutazioniBene");
		logger.debug("--> Enter caricaValutazioniBene");

		List<ValutazioneBene> listResult = new ArrayList<ValutazioneBene>();
		try {
			listResult = beniAmmortizzabiliService.caricaValutazioniBene(beneAmmortizzabile);
		} catch (Exception e) {
			end("caricaValutazioniBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit caricaValutazioniBene");
		end("caricaValutazioniBene");
		return listResult;
	}

	@Override
	public List<VenditaBene> caricaVenditeBene(BeneAmmortizzabile beneAmmortizzabile) {
		start("caricaVenditeBene");
		logger.debug("--> Enter caricaVenditeBene");

		List<VenditaBene> vendite = new ArrayList<VenditaBene>();
		try {
			vendite = beniAmmortizzabiliService.caricaVenditeBene(beneAmmortizzabile);
		} catch (Exception e) {
			end("caricaVenditeBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("caricaVenditeBene");
		logger.debug("--> Exit caricaVenditeBene");
		return vendite;
	}

	@Override
	public void consolidaSimulazione(Simulazione simulazione) {
		start("consolidaSimulazione");
		logger.debug("--> Enter consolidaSimulazione");

		try {
			beniAmmortizzabiliService.consolidaSimulazione(simulazione);
		} catch (Exception e) {
			end("consolidaSimulazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("consolidaSimulazione");
		logger.debug("--> Exit consolidaSimulazione");
	}

	@Override
	public ValutazioneBene creaNuovaValutazioneBene(BeneAmmortizzabile beneAmmortizzabile) {
		start("creaNuovaValutazioneBene");
		logger.debug("--> Enter creaNuovaValutazioneBene");

		ValutazioneBene valutazioneBene = null;
		try {
			valutazioneBene = beniAmmortizzabiliService.creaNuovaValutazioneBene(beneAmmortizzabile);
		} catch (Exception e) {
			end("creaNuovaValutazioneBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("creaNuovaValutazioneBene");
		logger.debug("--> Exit creaNuovaValutazioneBene");
		return valutazioneBene;
	}

	@Override
	public VenditaBene creaNuovaVenditaBene(BeneAmmortizzabile beneAmmortizzabile) {
		start("creaNuovaVenditaBene");
		logger.debug("--> Enter creaNuovaVenditaBene");

		VenditaBene venditaBene = null;
		try {
			venditaBene = beniAmmortizzabiliService.creaNuovaVenditaBene(beneAmmortizzabile);
		} catch (Exception e) {
			end("creaNuovaVenditaBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("creaNuovaVenditaBene");
		logger.debug("--> Exit creaNuovaVenditaBene");
		return venditaBene;
	}

	@Override
	public Simulazione creaSimulazione(String descrizione, Date data, Simulazione simulazioneRiferimento) {
		start("creaSimulazione");
		logger.debug("--> Enter creaSimulazione");

		Simulazione simulazione = null;
		try {
			simulazione = beniAmmortizzabiliService.creaSimulazione(descrizione, data, simulazioneRiferimento);
		} catch (Exception e) {
			end("creaSimulazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("creaSimulazione");
		logger.debug("--> Exit creaSimulazione");
		return simulazione;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(Map parametri) {
		start("ricercaBeniAmmortizzabili");
		logger.debug("--> Enter ricercaBeniAmmortizzabili");

		List<BeneAmmortizzabileLite> beniTrovati = new ArrayList<BeneAmmortizzabileLite>();
		try {
			beniTrovati = beniAmmortizzabiliService.ricercaBeniAmmortizzabili(parametri);
		} catch (Exception e) {
			end("ricercaBeniAmmortizzabili");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit ricercaBeniAmmortizzabili");
		end("ricercaBeniAmmortizzabili");
		return beniTrovati;
	}

	@Override
	public List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(String fieldSearch, String valueSearch) {
		start("ricercaBeniAmmortizzabili");
		logger.debug("--> Enter ricercaBeniAmmortizzabili");

		List<BeneAmmortizzabileLite> beniTrovati = new ArrayList<BeneAmmortizzabileLite>();
		try {
			beniTrovati = beniAmmortizzabiliService.ricercaBeniAmmortizzabili(fieldSearch, valueSearch);
		} catch (Exception e) {
			end("ricercaBeniAmmortizzabili");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit ricercaBeniAmmortizzabili");
		end("ricercaBeniAmmortizzabili");
		return beniTrovati;
	}

	@Override
	public BeneAmmortizzabile salvaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		start("salvaBeneAmmortizzabile");
		logger.debug("--> Enter salvaBeneAmmortizzabile");

		BeneAmmortizzabile beneAmmortizzabileSalvato = null;
		try {
			beneAmmortizzabileSalvato = beniAmmortizzabiliService.salvaBeneAmmortizzabile(beneAmmortizzabile);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaBeneAmmortizzabile");
		}

		logger.debug("--> Exit salvaBeneAmmortizzabile");
		end("salvaBeneAmmortizzabile");
		return beneAmmortizzabileSalvato;
	}

	@Override
	public BeniSettings salvaBeniSettings(BeniSettings beniSettings) {
		logger.debug("--> Enter salvaBeniSettings");
		start("salvaBeniSettings");

		BeniSettings beniSettingsSalvate = null;
		try {
			beniSettingsSalvate = beniAmmortizzabiliService.salvaBeniSettings(beniSettings);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaBeniSettings");
		}
		logger.debug("--> Exit salvaBeniSettings ");
		return beniSettingsSalvate;
	}

	@Override
	public QuotaAmmortamentoCivilistico salvaQuotaAmmortamentoCivilistico(
			QuotaAmmortamentoCivilistico quotaAmmortamentoCivilistico) {
		start("salvaQuotaAmmortamentoCivilistico");
		logger.debug("--> Enter salvaQuotaAmmortamentoCivilistico");

		QuotaAmmortamentoCivilistico quotaAmmortamentoCivilistico2 = null;
		try {
			quotaAmmortamentoCivilistico2 = beniAmmortizzabiliService
					.salvaQuotaAmmortamentoCivilistico(quotaAmmortamentoCivilistico);
		} catch (Exception e) {
			end("salvaQuotaAmmortamentoCivilistico");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit salvaQuotaAmmortamentoCivilistico");
		end("salvaQuotaAmmortamentoCivilistico");
		return quotaAmmortamentoCivilistico2;
	}

	@Override
	public QuotaAmmortamentoFiscale salvaQuotaAmmortamentoFiscale(QuotaAmmortamentoFiscale quotaAmmortamentoFiscale) {
		start("salvaQuotaAmmortamentoFiscale");
		logger.debug("--> Enter salvaQuotaAmmortamentoFiscale");

		QuotaAmmortamentoFiscale quotaAmmortamentoFiscale2 = null;
		try {
			quotaAmmortamentoFiscale2 = beniAmmortizzabiliService
					.salvaQuotaAmmortamentoFiscale(quotaAmmortamentoFiscale);
		} catch (Exception e) {
			end("salvaQuotaAmmortamentoFiscale");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit salvaQuotaAmmortamentoFiscale");
		end("salvaQuotaAmmortamentoFiscale");
		return quotaAmmortamentoFiscale2;
	}

	@Override
	public Simulazione salvaSimulazione(Simulazione simulazione) throws AreeContabiliSimulazioneException {
		start("salvaSimulazione");
		logger.debug("--> Enter salvaSimulazione");

		Simulazione simulazioneSalvata = null;
		try {
			simulazioneSalvata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		} catch (AreeContabiliSimulazioneException e1) {
			throw e1;
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaSimulazione");
			logger.debug("--> Exit salvaSimulazione");
		}

		return simulazioneSalvata;
	}

	@Override
	public Simulazione salvaSimulazione(Simulazione simulazione, boolean forzaCancellazioneAC) {
		start("salvaSimulazione");
		logger.debug("--> Enter salvaSimulazione");

		Simulazione simulazioneSalvata = null;
		try {
			simulazioneSalvata = beniAmmortizzabiliService.salvaSimulazione(simulazione, forzaCancellazioneAC);
		} catch (Exception e) {
			end("salvaSimulazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("salvaSimulazione");
		logger.debug("--> Exit salvaSimulazione");
		return simulazioneSalvata;
	}

	@Override
	public SottoSpecie salvaSottoSpecie(SottoSpecie sottoSpecie) {
		start("salvaSottoSpecie");
		logger.debug("--> Enter salvaSottoSpecie");

		SottoSpecie sottoSpecie2 = null;
		try {
			sottoSpecie2 = beniAmmortizzabiliService.salvaSottoSpecie(sottoSpecie);
		} catch (Exception e) {
			end("salvaSottoSpecie");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit salvaSottoSpecie");
		end("salvaSottoSpecie");
		return sottoSpecie2;
	}

	@Override
	public Specie salvaSpecie(Specie specie) {
		start("salvaSpecie");
		logger.debug("--> Enter salvaSpecie");

		Specie specie2 = null;
		try {
			specie2 = beniAmmortizzabiliService.salvaSpecie(specie);
		} catch (Exception e) {
			end("salvaSpecie");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit enclosing_method");
		end("salvaSpecie");
		return specie2;
	}

	@Override
	public TipologiaEliminazione salvaTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione) {
		start("salvaTipologiaEliminazione");
		logger.debug("--> Enter salvaTipologiaEliminazione");

		TipologiaEliminazione tipologiaEliminazione2 = null;

		try {
			tipologiaEliminazione2 = beniAmmortizzabiliService.salvaTipologiaEliminazione(tipologiaEliminazione);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio di " + tipologiaEliminazione);
			end("salvaTipologiaEliminazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit salvaTipologiaEliminazione");
		end("salvaTipologiaEliminazione");
		return tipologiaEliminazione2;
	}

	@Override
	public Ubicazione salvaUbicazione(Ubicazione ubicazione) {
		start("salvaUbicazione");
		logger.debug("--> Enter salvaUbicazione");

		Ubicazione ubicazione2 = null;

		try {
			ubicazione2 = beniAmmortizzabiliService.salvaUbicazione(ubicazione);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio di " + ubicazione);
			end("salvaUbicazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit salvaUbicazione");
		end("salvaUbicazione");
		return ubicazione2;
	}

	@Override
	public ValutazioneBene salvaValutazioneBene(ValutazioneBene valutazioneBene) {
		start("salvaValutazioneBene");
		logger.debug("--> Enter salvaValutazioneBene");

		ValutazioneBene valutazioneBeneSalvata = null;
		try {
			valutazioneBeneSalvata = beniAmmortizzabiliService.salvaValutazioneBene(valutazioneBene);
		} catch (Exception e) {
			end("salvaValutazioneBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("salvaValutazioneBene");
		logger.debug("--> Exit salvaValutazioneBene");
		return valutazioneBeneSalvata;
	}

	@Override
	public VenditaBene salvaVenditaBene(VenditaBene venditaBene) throws VenditaInAnnoSimulatoException {
		start("salvaVenditaBene");
		logger.debug("--> Enter salvaVenditaBene");

		VenditaBene venditaBene2 = null;
		try {
			venditaBene2 = beniAmmortizzabiliService.salvaVenditaBene(venditaBene);
		} catch (VenditaInAnnoSimulatoException e) {
			logger.debug("--> VenditaBene in anno simulato da ricalcolare simulazione !");
			end("salvaVenditaBene");
			throw e;
		} catch (Exception e) {
			end("salvaVenditaBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("salvaVenditaBene");
		logger.debug("--> Exit salvaVenditaBene");
		return venditaBene2;
	}

	@Override
	public VenditaBene salvaVenditaBene(VenditaBene venditaBene, boolean forzaRicalcolo) {
		start("salvaVenditaBene");
		logger.debug("--> Enter salvaVenditaBene");

		VenditaBene venditaBene2 = null;
		try {
			venditaBene2 = beniAmmortizzabiliService.salvaVenditaBene(venditaBene, forzaRicalcolo);
		} catch (Exception e) {
			end("salvaVenditaBene");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("salvaVenditaBene");
		logger.debug("--> Exit salvaVenditaBene");
		return venditaBene2;
	}

	/**
	 * @param beniAmmortizzabiliService
	 *            the beniAmmortizzabiliService to set
	 */
	public void setBeniAmmortizzabiliService(BeniAmmortizzabiliService beniAmmortizzabiliService) {
		this.beniAmmortizzabiliService = beniAmmortizzabiliService;
	}

	@Override
	public Gruppo settaGruppoAzienda(Gruppo gruppo) {
		logger.debug("--> Enter settaGruppoAzienda");
		start("settaGruppoAzienda");

		Gruppo gruppoSalvato = null;
		try {
			gruppoSalvato = beniAmmortizzabiliService.settaGruppoAzienda(gruppo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("settaGruppoAzienda");
		}
		logger.debug("--> Exit settaGruppoAzienda ");
		return gruppoSalvato;
	}

	@Override
	public Object verificaNuovaSimulazione(Integer anno) {
		start("verificaNuovaSimulazione");
		logger.debug("--> Enter verificaNuovaSimulazione");

		Object object = null;
		try {
			object = beniAmmortizzabiliService.verificaNuovaSimulazione(anno);
		} catch (Exception e) {
			end("verificaNuovaSimulazione");
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		end("verificaNuovaSimulazione");
		logger.debug("--> Exit verificaNuovaSimulazione");
		return object;
	}
}