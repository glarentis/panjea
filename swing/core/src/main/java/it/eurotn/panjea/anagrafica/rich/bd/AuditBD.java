package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.anagrafica.service.interfaces.AuditService;
import it.eurotn.panjea.audit.envers.RevInf;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class AuditBD extends AbstractBaseBD implements IAuditBD {

	public static final String BEAN_ID = "auditBD";

	private static Logger logger = Logger.getLogger(AuditBD.class);
	private AuditService auditService;

	@Override
	public void cancellaAuditPrecedente(Date data) {
		logger.debug("--> Enter cancellaAuditPrecedente");
		start("cancellaAuditPrecedente");
		try {
			auditService.cancellaAuditPrecedente(data);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaAuditPrecedente");
		}
		logger.debug("--> Exit cancellaAuditPrecedente ");
	}

	@Override
	public Integer caricaNumeroRevInf() {
		logger.debug("--> Enter caricaNumeroRevInf");
		start("caricaNumeroRevInf");
		Integer result = null;
		try {
			result = auditService.caricaNumeroRevInf();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaNumeroRevInf");
		}
		logger.debug("--> Exit caricaNumeroRevInf ");
		return result;
	}

	/**
	 * @return Returns the auditService.
	 */
	public AuditService getAuditService() {
		return auditService;
	}

	@Override
	public Map<RevInf, List<Object>> getVersioni(Object auditBean) {
		logger.debug("--> Enter getVersioni");
		start("getVersioni");
		Map<RevInf, List<Object>> result = null;
		try {
			result = auditService.getVersioni(auditBean);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("getVersioni");
		}
		logger.debug("--> Exit getVersioni ");
		return result;
	}

	/**
	 * @param auditService
	 *            The auditService to set.
	 */
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}

}
