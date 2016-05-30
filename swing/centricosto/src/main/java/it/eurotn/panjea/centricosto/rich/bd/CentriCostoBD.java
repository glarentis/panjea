package it.eurotn.panjea.centricosto.rich.bd;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.centricosto.service.interfaces.CentriCostoService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;

import org.apache.log4j.Logger;

public class CentriCostoBD extends AbstractBaseBD implements ICentriCostoBD {
	private CentriCostoService centriCostoService;
	private static Logger logger = Logger.getLogger(CentriCostoBD.class);

	@Override
	public void cancellaCentroCosto(CentroCosto centroCosto) {
		logger.debug("--> Enter cancellaCentroCosto");
		start("cancellaCentroCosto");
		try {
			centriCostoService.cancellaCentroCosto(centroCosto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCentroCosto");
		}
		logger.debug("--> Exit cancellaCentroCosto ");
	}

	@Override
	public List<CentroCosto> caricaCentriCosto(String codice) {
		logger.debug("--> Enter caricaCentriCosto");
		start("caricaCentriCosto");
		List<CentroCosto> result = null;
		try {
			result = centriCostoService.caricaCentriCosto(codice);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCentriCosto");
		}
		logger.debug("--> Exit caricaCentriCosto ");
		return result;
	}

	@Override
	public CentroCosto salvaCentroCosto(CentroCosto centroCosto) {
		logger.debug("--> Enter salvaCentroCosto");
		start("salvaCentroCosto");
		CentroCosto result = null;
		try {
			result = centriCostoService.salvaCentroCosto(centroCosto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCentroCosto");
		}
		logger.debug("--> Exit salvaCentroCosto ");
		return result;
	}

	/**
	 * @param centriCostoService
	 *            The centriCostoService to set.
	 */
	public void setCentriCostoService(CentriCostoService centriCostoService) {
		this.centriCostoService = centriCostoService;
	}

}
