/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.bd;

import it.eurotn.panjea.beniammortizzabili2.domain.*;
import it.eurotn.panjea.beniammortizzabili2.service.interfaces.BeniAmmortizzabiliService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author adriano
 * @version 1.0, 24/nov/06
 */
public class ReportBeniAmmortizzabiliBD extends AbstractBaseBD implements IReportBeniAmmortizzabiliBD {

	static Logger logger = Logger.getLogger(ReportBeniAmmortizzabiliBD.class);

	public ReportBeniAmmortizzabiliBD() {
		super();
	}

	private BeniAmmortizzabiliService beniAmmortizzabiliService;

	/**
	 * @return Returns the beniAmmortizzabiliService.
	 */
	public BeniAmmortizzabiliService getBeniAmmortizzabiliService() {
		return beniAmmortizzabiliService;
	}

	/**
	 * @param beniAmmortizzabiliService
	 *            The beniAmmortizzabiliService to set.
	 */
	public void setBeniAmmortizzabiliService(BeniAmmortizzabiliService beniAmmortizzabiliService) {
		this.beniAmmortizzabiliService = beniAmmortizzabiliService;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BeneAmmortizzabileConFigli> ricercaBeniAcquistati(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaBeniAcquistati");
		List<BeneAmmortizzabileConFigli> list = null;
		try {
			list = beniAmmortizzabiliService.ricercaBeniAcquistati(criteriaRicercaBeniAmmortizzabili);
		} catch (Exception e) {
			logger.error("--> errore in caricaBeniAcquistati ", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		logger.debug("--> Exit ricercaBeniAcquistati");
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BeneAmmortizzabileConFigli> ricercaRubricaBeni(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaRubricaBeni");
		List<BeneAmmortizzabileConFigli> list = null;
		try {
			list = beniAmmortizzabiliService.ricercaRubricaBeni(criteriaRicercaBeniAmmortizzabili);
		} catch (Exception e) {
			logger.error("--> errore in caricaStampaRubricaBeni ", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		logger.debug("--> Exit ricercaRubricaBeni");
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VenditaBene> ricercaVenditeBeni(CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaVenditeBeni");
		List<VenditaBene> list = null;
		try {
			list = beniAmmortizzabiliService.ricercaVenditeBeni(criteriaRicercaBeniAmmortizzabili);
		} catch (Exception e) {
			logger.error("--> errore in caricaVenditeBeni ", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		logger.debug("--> Exit ricercaVenditeBeni");
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamento(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaQuoteAmmortamento");
		List<QuotaAmmortamentoFiscale> quote = null;
		try {
			quote = beniAmmortizzabiliService.ricercaQuoteAmmortamento(criteriaRicercaBeniAmmortizzabili);
		} catch (Exception e) {
			logger.error("--> errore in ricerca quote ammortamento ", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		logger.debug("--> Exit ricercaQuoteAmmortamento");
		return quote;
	}

}
