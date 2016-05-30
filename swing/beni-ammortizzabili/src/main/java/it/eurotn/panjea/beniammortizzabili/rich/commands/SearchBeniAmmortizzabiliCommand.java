/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.search.RicercaBeniAmmortizzabiliDialog;
import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.dialog.ApplicationDialog;

/**
 * 
 * @author Aracno
 * @version 1.0, 29/set/06
 * 
 */
public class SearchBeniAmmortizzabiliCommand extends OpenEditorCommand {

	private static final String DIALOG_ID = "ricercaBeniAmmortizzabili";

	/**
	 * Mostra un nuovo dialogo di ricerca dell'entità scelta tramite la proprietà entita settata nel
	 * commands-context.xml.
	 */
	@Override
	protected void doExecuteCommand() {
		ApplicationDialog ricercaBeneDialog = new RicercaBeniAmmortizzabiliDialog(DIALOG_ID);
		ricercaBeneDialog.setCallingCommand(this);
		ricercaBeneDialog.showDialog();
	}
}
