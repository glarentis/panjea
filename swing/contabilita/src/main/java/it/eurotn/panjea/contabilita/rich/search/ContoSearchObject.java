/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.search;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * SearchObject Conto.
 * 
 * @author adriano
 * @version 1.0, 11/giu/07
 */
public class ContoSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(ContoSearchObject.class);

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	private static final String SEARCHOBJECT_ID = "contoSearchObject";

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<? extends Object> getData(String arg0, String arg1) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		/* ricerca nei parametri il valore per SottoTipoConto */
		SottotipoConto sottoTipoConto = (SottotipoConto) parameters.get("sottoTipoConto");
		List<Conto> conti = contabilitaAnagraficaBD.ricercaConti(arg1, sottoTipoConto);
		if (logger.isDebugEnabled()) {
			logger.debug("--> getData restituisce risultati # " + conti.size());
		}
		logger.debug("--> Exit getData");
		return conti;
	}

	@Override
	public String getId() {
		return SEARCHOBJECT_ID;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            The contabilitaAnagraficaBD to set.
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

}
