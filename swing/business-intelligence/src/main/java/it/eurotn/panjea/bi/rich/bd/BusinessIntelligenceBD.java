package it.eurotn.panjea.bi.rich.bd;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.sql.detail.RigaDettaglioAnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.service.interfaces.BusinessIntelligenceService;
import it.eurotn.panjea.bi.util.Mappa;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;
import it.eurotn.panjea.magazzino.exception.AnalisiPresenteInDashBoardException;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class BusinessIntelligenceBD extends AbstractBaseBD implements IBusinessIntelligenceBD {

	private static Logger logger = Logger.getLogger(BusinessIntelligenceBD.class);
	public static final String BEAN_ID = "businessIntelligenceBD";
	private BusinessIntelligenceService businessIntelligenceService;

	@Override
	public void cancellaAnalisi(int idAnalisiBi) throws AnalisiPresenteInDashBoardException {
		logger.debug("--> Enter cancellaAnalisi");
		start("cancellaAnalisi");
		try {
			businessIntelligenceService.cancellaAnalisi(idAnalisiBi);
		} catch (AnalisiPresenteInDashBoardException e) {
			throw e;
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaAnalisi");
		}
		logger.debug("--> Exit cancellaAnalisi ");

	}

	@Override
	public void cancellaAnalisi(int idAnalisiBI, boolean removeFromDashboard)
			throws AnalisiPresenteInDashBoardException {
		logger.debug("--> Enter cancellaAnalisi");
		start("cancellaAnalisi");
		try {
			businessIntelligenceService.cancellaAnalisi(idAnalisiBI, removeFromDashboard);
		} catch (AnalisiPresenteInDashBoardException e) {
			throw e;
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaAnalisi");
		}
		logger.debug("--> Exit cancellaAnalisi ");
	}

	@Override
	public void cancellaDashBoard(String nomeDashBoard) {
		logger.debug("--> Enter cancellaDashBoard");
		start("cancellaDashBoard");
		try {
			businessIntelligenceService.cancellaDashBoard(nomeDashBoard);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaDashBoard");
		}
		logger.debug("--> Exit cancellaDashBoard ");

	}

	@Override
	public AnalisiBi caricaAnalisi(String nomeAnalisi, String categoriaAnalisi) throws AnalisiNonPresenteException {
		logger.debug("--> Enter caricaAnalisi");
		start("caricaAnalisi");
		AnalisiBi result = null;
		try {
			result = businessIntelligenceService.caricaAnalisi(nomeAnalisi, categoriaAnalisi);
		} catch (RuntimeException e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAnalisi");
		}
		logger.debug("--> Exit caricaAnalisi ");
		return result;
	}

	@Override
	public DashBoard caricaDashBoard(String nomeDashBoard) {
		logger.debug("--> Enter caricaDashBoard");
		start("caricaDashBoard");
		DashBoard result = null;
		try {
			result = businessIntelligenceService.caricaDashBoard(nomeDashBoard);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDashBoard");
		}
		logger.debug("--> Exit caricaDashBoard ");
		return result;
	}

	@Override
	public Map<String, byte[]> caricaFilesMappa(String nomeFileMappa) {
		logger.debug("--> Enter caricaFilesMappa");
		start("caricaFilesMappa");
		Map<String, byte[]> result = null;
		try {
			result = businessIntelligenceService.caricaFilesMappa(nomeFileMappa);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaFilesMappa");
		}
		logger.debug("--> Exit caricaFilesMappa ");
		return result;
	}

	@Override
	public List<AnalisiBi> caricaListaAnalisi() {
		logger.debug("--> Enter caricaListaAnalisi");
		start("caricaListaAnalisi");
		List<AnalisiBi> result = null;
		try {
			result = businessIntelligenceService.caricaListaAnalisi();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaListaAnalisi");
		}
		logger.debug("--> Exit caricaListaAnalisi ");
		return result;
	}

	@Override
	public List<DashBoard> caricaListaDashBoard() {
		logger.debug("--> Enter caricaListaDashBoard");
		start("caricaListaDashBoard");
		List<DashBoard> result = null;
		try {
			result = businessIntelligenceService.caricaListaDashBoard();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaListaDashBoard");
		}
		logger.debug("--> Exit caricaListaDashBoard ");
		return result;
	}

	@Override
	public List<Mappa> caricaMappe() {
		logger.debug("--> Enter caricaMappe");
		start("caricaMappe");

		List<Mappa> mappe = null;
		try {
			mappe = businessIntelligenceService.caricaMappe();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaMappe");
		}
		logger.debug("--> Exit caricaMappe ");
		return mappe;
	}

	@Override
	public Set<Object> caricaValoriPerColonna(Colonna colonna) {
		logger.debug("--> Enter caricaValoriPerColonna");
		start("caricaValoriPerColonna");
		Set<Object> result = null;
		try {
			result = businessIntelligenceService.caricaValoriPerColonna(colonna);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaValoriPerColonna");
		}
		logger.debug("--> Exit caricaValoriPerColonna ");
		return result;
	}

	@Override
	public void copiaAnalisi(AnalisiBi analisi) {
		logger.debug("--> Enter copiaAnalisi");
		start("copiaAnalisi");
		try {
			businessIntelligenceService.copiaAnalisi(analisi);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("copiaAnalisi");
		}
		logger.debug("--> Exit copiaAnalisi ");

	}

	@Override
	public String creaJrxml(AnalisiBi analisiBi, String template) {
		logger.debug("--> Enter creaJrxml");
		start("creaJrxml");
		String result = null;
		try {
			result = businessIntelligenceService.creaJrxml(analisiBi, template);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaJrxml");
		}
		logger.debug("--> Exit creaJrxml ");
		return result;
	}

	@Override
	public String creaJrxml(DashBoard dashBoard, String template) {
		logger.debug("--> Enter creaJrxml");
		start("creaJrxml");
		String result = null;
		try {
			result = businessIntelligenceService.creaJrxml(dashBoard, template);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaJrxml");
		}
		logger.debug("--> Exit creaJrxml ");
		return result;
	}

	@Override
	public List<RigaDettaglioAnalisiBi> drillThrough(AnalisiBi analisiBi, Map<Colonna, Object[]> detailFilter,
			Colonna colonnaMisura, int page, int sizeOfPage) {
		logger.debug("--> Enter drillThrough");
		start("drillThrough");
		List<RigaDettaglioAnalisiBi> result = null;
		try {
			result = businessIntelligenceService.drillThrough(analisiBi, detailFilter, colonnaMisura, page, sizeOfPage);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("drillThrough");
		}
		logger.debug("--> Exit drillThrough ");
		return result;
	}

	@Override
	public AnalisiBIResult eseguiAnalisi(AnalisiBi analisiBi) {
		logger.debug("--> Enter caricaDataWareHouseModel");
		start("eseguiAnalisi");
		AnalisiBIResult analisiBiResult = null;
		try {
			analisiBiResult = businessIntelligenceService.eseguiAnalisi(analisiBi);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("eseguiAnalisi");
		}
		logger.debug("--> Exit eseguiAnalisi ");
		return analisiBiResult;
	}

	@Override
	public AnalisiBi salvaAnalisi(AnalisiBi analisiBi) {
		logger.debug("--> Enter salvaAnalisi");
		start("salvaAnalisi");
		AnalisiBi result = null;
		try {
			result = businessIntelligenceService.salvaAnalisi(analisiBi);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaAnalisi");
		}
		logger.debug("--> Exit salvaAnalisi ");
		return result;
	}

	@Override
	public DashBoard salvaDashBoard(DashBoard dashBoard) {
		logger.debug("--> Enter salvaDashBoard");
		start("salvaDashBoard");
		DashBoard result = null;
		try {
			result = businessIntelligenceService.salvaDashBoard(dashBoard);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaDashBoard");
		}
		logger.debug("--> Exit salvaDashBoard ");
		return result;
	}

	/**
	 * @param businessIntelligenceService
	 *            The businessIntelligenceService to set.
	 */
	public void setBusinessIntelligenceService(BusinessIntelligenceService businessIntelligenceService) {
		this.businessIntelligenceService = businessIntelligenceService;
	}

}
