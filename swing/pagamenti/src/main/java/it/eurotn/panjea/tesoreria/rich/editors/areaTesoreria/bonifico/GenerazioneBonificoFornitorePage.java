/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.bonifico;

import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author leonardo
 */
public class GenerazioneBonificoFornitorePage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "generazioneBonificoFornitorePage";

	/**
	 * Costruttore.
	 */
	public GenerazioneBonificoFornitorePage() {
		super(PAGE_ID, new GenerazioneBonificoFornitoreForm());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] {};
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

}
