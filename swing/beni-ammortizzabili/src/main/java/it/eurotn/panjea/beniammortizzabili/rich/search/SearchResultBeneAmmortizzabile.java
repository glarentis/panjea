/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.commands.StampaEtichettaBeniCommand;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

/**
 * 
 * @author Aracno
 * @version 1.0, 29/set/06
 * 
 */
public class SearchResultBeneAmmortizzabile extends AbstractTableSearchResult<BeneAmmortizzabileLite> implements
		InitializingBean {

	private static Logger logger = Logger.getLogger(SearchResultBeneAmmortizzabile.class);

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private Map<String, Object> param;
	private String id;
	private String idNewBeneAmmortizzabileCommand;
	private StampaEtichettaBeniCommand stampaEtichettaBeniCommand;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(beniAmmortizzabiliBD, "beniAmmortizzabiliBD must be set");
		org.springframework.util.Assert.notNull(idNewBeneAmmortizzabileCommand,
				"idNewBeneAmmortizzabileCommand must be set");
	}

	@Override
	protected BeneAmmortizzabileLite doDelete(BeneAmmortizzabileLite objToRemove) {
		final BeneAmmortizzabileLite beneAmmortizzabileResult = objToRemove;
		beniAmmortizzabiliBD.cancellaBeneAmmortizzabile(beneAmmortizzabileResult);
		return beneAmmortizzabileResult;
	}

	@Override
	protected String[] getColumnPropertyNames() {
		return new String[] { BeneAmmortizzabile.PROP_CODICE, BeneAmmortizzabile.PROP_DESCRIZIONE,
				BeneAmmortizzabile.PROP_FORNITORE, BeneAmmortizzabile.PROP_ANNO_ACQUISTO,
				BeneAmmortizzabile.PROP_MATRICOLA_AZIENDALE, BeneAmmortizzabile.PROP_MATRICOLA_FORNITORE };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getRefreshCommand() };
		// Ai comandi di default aggiung il nuovoCommand
		abstractCommands = (AbstractCommand[]) PanjeaSwingUtil.resizeArray(abstractCommands,
				abstractCommands.length + 2);
		abstractCommands[abstractCommands.length - 2] = (AbstractCommand) getActiveWindow().getCommandManager()
				.getCommand(idNewBeneAmmortizzabileCommand);
		abstractCommands[abstractCommands.length - 1] = getStampaEtichettaBeniCommand();
		return abstractCommands;
	}

	@Override
	protected Collection<BeneAmmortizzabileLite> getData(Map<String, Object> parameters) {
		this.param = parameters;
		return beniAmmortizzabiliBD.ricercaBeniAmmortizzabili(parameters);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	protected Class<BeneAmmortizzabileLite> getObjectsClass() {
		return BeneAmmortizzabileLite.class;
	}

	@Override
	protected Map<String, Object> getParameters() {
		return param;
	}

	/**
	 * @return the stampaEtichettaBeniCommand
	 */
	public StampaEtichettaBeniCommand getStampaEtichettaBeniCommand() {
		if (stampaEtichettaBeniCommand == null) {
			stampaEtichettaBeniCommand = new StampaEtichettaBeniCommand();
			stampaEtichettaBeniCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {

				@Override
				public boolean preExecution(ActionCommand command) {
					List<BeneAmmortizzabileLite> selectedObjs = getSelectedObjects();
					command.addParameter(StampaEtichettaBeniCommand.PARAM_BENI, selectedObjs);
					return selectedObjs != null && !selectedObjs.isEmpty();
				}
			});
		}

		return stampaEtichettaBeniCommand;
	}

	@Override
	public Object reloadObject(BeneAmmortizzabileLite object) {
		BeneAmmortizzabileLite beneAmmortizzabile = object;
		BeneAmmortizzabile beneAmmortizzabileResult = beniAmmortizzabiliBD.caricaBeneAmmortizzabile(beneAmmortizzabile);
		logger.debug("--> BeneAmmortizzabile caricato: " + beneAmmortizzabileResult.getId());
		return beneAmmortizzabileResult;
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param idNewBeneAmmortizzabileCommand
	 *            the idNewBeneAmmortizzabileCommand to set
	 */
	public void setIdNewBeneAmmortizzabileCommand(String idNewBeneAmmortizzabileCommand) {
		this.idNewBeneAmmortizzabileCommand = idNewBeneAmmortizzabileCommand;
	}
}
