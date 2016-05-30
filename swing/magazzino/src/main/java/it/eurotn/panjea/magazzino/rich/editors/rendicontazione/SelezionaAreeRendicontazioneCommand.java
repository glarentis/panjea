package it.eurotn.panjea.magazzino.rich.editors.rendicontazione;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class SelezionaAreeRendicontazioneCommand extends ApplicationWindowAwareCommand {

	public static final String SELECT_COMMAND_ID = "selezionaTuttoCommand";
	public static final String UNSELECT_COMMAND_ID = "deselezionaTuttoCommand";

	public static final String PARAM_RENDICONTAZIONE_TABLE_PAGE = "paramRendicontazioneTablePage";

	private boolean select = true;

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 *            id del comando
	 * @param select
	 *            indica se il command deve selezionare o deselezionare tutte le rate
	 */
	public SelezionaAreeRendicontazioneCommand(final String commandId, final boolean select) {
		super(commandId);
		this.select = select;
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		RisultatiRicercaRendicontazioneTablePage tablePage = (RisultatiRicercaRendicontazioneTablePage) getParameter(
				PARAM_RENDICONTAZIONE_TABLE_PAGE, null);

		if (tablePage != null) {

			for (AreaMagazzinoRicerca areaMagazzinoRicerca : tablePage.getTable().getRows()) {
				areaMagazzinoRicerca.setSelezionata(select);
			}

			tablePage.getTable().getTable().repaint();
		}
	}

}
