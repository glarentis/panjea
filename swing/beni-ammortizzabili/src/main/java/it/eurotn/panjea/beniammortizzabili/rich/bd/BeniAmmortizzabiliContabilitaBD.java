/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.bd;

import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili2.service.interfaces.BeniAmmortizzabiliContabilitaService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;

/**
 * @author fattazzo
 *
 */
public class BeniAmmortizzabiliContabilitaBD extends AbstractBaseBD implements IBeniAmmortizzabiliContabilitaBD {

	public static final String BEAN_ID = "beniAmmortizzabiliContabilitaBD";

	private static Logger logger = Logger.getLogger(BeniAmmortizzabiliContabilitaBD.class);

	private BeniAmmortizzabiliContabilitaService beniAmmortizzabiliContabilitaService;

	@Override
	public void confermaAreeContaibliSimulazione(Integer idSimulazione) {
		logger.debug("--> Enter confermaAreeContaibliSimulazione");
		start("confermaAreeContaibliSimulazione");
		try {
			beniAmmortizzabiliContabilitaService.confermaAreeContaibliSimulazione(idSimulazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("confermaAreeContaibliSimulazione");
		}
		logger.debug("--> Exit confermaAreeContaibliSimulazione ");
	}

	@Override
	public void creaAreeContabili(Integer idSimulazione) throws SottocontiBeniNonValidiException {
		logger.debug("--> Enter creaAreeContabili");
		start("creaAreeContabili");
		try {
			beniAmmortizzabiliContabilitaService.creaAreeContabili(idSimulazione);
		} catch (SottocontiBeniNonValidiException e1) {
			throw e1;
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaAreeContabili");
		}
		logger.debug("--> Exit creaAreeContabili ");
	}

	/**
	 * @param beniAmmortizzabiliContabilitaService
	 *            the beniAmmortizzabiliContabilitaService to set
	 */
	public void setBeniAmmortizzabiliContabilitaService(
			BeniAmmortizzabiliContabilitaService beniAmmortizzabiliContabilitaService) {
		this.beniAmmortizzabiliContabilitaService = beniAmmortizzabiliContabilitaService;
	}
}
