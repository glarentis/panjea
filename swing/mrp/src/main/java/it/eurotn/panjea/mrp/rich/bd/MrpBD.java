package it.eurotn.panjea.mrp.rich.bd;

import it.eurotn.panjea.magazzino.exception.FormulaMrpCalcoloArticoloException;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.service.interfaces.MrpService;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class MrpBD extends AbstractBaseBD implements IMrpBD {
	private static Logger logger = Logger.getLogger(MrpBD.class);
	private MrpService mrpService;

	@Override
	@AsyncMethodInvocation
	public void calcolaMrp(int numBucket, Date startDate, Integer idAreaOrdine) {
		logger.debug("--> Enter calcolaMrp");
		start("calcolaMrp");
		try {
			mrpService.calcolaMrp(numBucket, startDate, idAreaOrdine);
		} catch (Exception e) {
			// Controllo se ho una FormulaException, in caso la rigiro
			if (e.getCause() != null && e.getCause().getCause() != null
					&& e.getCause().getCause().getCause() instanceof FormulaMrpCalcoloArticoloException) {
				throw ((FormulaMrpCalcoloArticoloException) e.getCause().getCause().getCause());
			}
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("calcolaMrp");
		}
		logger.debug("--> Exit calcolaMrp ");

	}

	@Override
	public void cancellaRigheRisultati(List<RisultatoMrpFlat> selectedObjects) {
		logger.debug("--> Enter cancellaRigheRisultati");
		start("cancellaRigheRisultati");
		try {
			mrpService.cancellaRigheRisultati(selectedObjects);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaRigheRisultati");
		}
		logger.debug("--> Exit cancellaRigheRisultati ");

	}

	@Override
	public List<RisultatoMrpFlat> caricaRisultatoMrp(ParametriMrpRisultato parametriMrpRisultato) {
		logger.debug("--> Enter caricaRisultatoMrp");
		start("caricaRisultatoMrp");
		List<RisultatoMrpFlat> result = null;
		try {
			result = mrpService.caricaRisultatoMrp(parametriMrpRisultato);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRisultatoMrp");
		}
		logger.debug("--> Exit caricaRisultatoMrp ");
		return result;
	}

	@Override
	@AsyncMethodInvocation
	public Long generaOrdini(Integer[] idRisultatiMrp) {
		logger.debug("--> Enter generaOrdini");
		Long timeStmapCreazione = null;
		start("generaOrdini");
		try {
			timeStmapCreazione = mrpService.generaOrdini(idRisultatiMrp);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("generaOrdini");
		}
		logger.debug("--> Exit generaOrdini ");
		return timeStmapCreazione;
	}

	/**
	 * @return Returns the mrpService.
	 */
	public MrpService getMrpService() {
		return mrpService;
	}

	@Override
	public List<RisultatoMrpFlat> salvaRigheRisultato(List<RisultatoMrpFlat> righeMrpFlat) {
		logger.debug("--> Enter salvaRigheRisultato");
		start("salvaRigheRisultato");
		List<RisultatoMrpFlat> result = null;
		try {
			result = mrpService.salvaRigheRisultato(righeMrpFlat);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaRigheRisultato");
		}
		logger.debug("--> Exit salvaRigheRisultato ");
		return result;
	}

	/**
	 * @param mrpService
	 *            The mrpService to set.
	 */
	public void setMrpService(MrpService mrpService) {
		this.mrpService = mrpService;
	}

	@Override
	public void svuotaRigheRisultati() {
		logger.debug("--> Enter svuotaRigheRisultati");
		start("svuotaRigheRisultati");
		try {
			mrpService.svuotaRigheRisultati();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("svuotaRigheRisultati");
		}
		logger.debug("--> Exit svuotaRigheRisultati ");

	}
}
