/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.rich.editors.ricercaarticoli.RicercaAvanzataArticoliPage;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo.CustomFilter;

import java.awt.Dimension;
import java.util.List;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author leonardo
 *
 */
public class RicercaAvanzataArticoliCommand extends ActionCommand {

	public static final String COMMAND_ID = "ricercaAvanzataArticoliCommand";
	private List<ArticoloRicerca> articoliSelezionati = null;

	public static final String CUSTOM_FILTER_PARAM = "customFilterParam";

	/**
	 * Costruttore.
	 */
	public RicercaAvanzataArticoliCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	/**
	 * Costruttore.
	 *
	 * @param commandId
	 *            l'id del command
	 */
	public RicercaAvanzataArticoliCommand(final String commandId) {
		super(commandId);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		final RicercaAvanzataArticoliPage ricercaArticoliPage = new RicercaAvanzataArticoliPage();
		CustomFilter customFilterParam = (CustomFilter) getParameter(CUSTOM_FILTER_PARAM, null);
		ricercaArticoliPage.setCustomFilter(customFilterParam);

		RicercaAvanzataArticoliDialog dialog = new RicercaAvanzataArticoliDialog(ricercaArticoliPage);
		dialog.setPreferredSize(new Dimension(900, 450));
		dialog.showDialog();

		articoliSelezionati = dialog.getArticoliSelezionati();
	}

	/**
	 * @return the articoliSelezionati
	 */
	public List<ArticoloRicerca> getArticoliSelezionati() {
		return articoliSelezionati;
	}

}
