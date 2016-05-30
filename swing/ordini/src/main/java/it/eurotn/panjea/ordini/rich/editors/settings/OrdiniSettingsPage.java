package it.eurotn.panjea.ordini.rich.editors.settings;

import it.eurotn.panjea.ordini.domain.OrdiniSettings;
import it.eurotn.panjea.ordini.rich.bd.IAnagraficaOrdiniBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class OrdiniSettingsPage extends FormBackedDialogPageEditor {

	private class RefreshCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public RefreshCommand() {
			super("refreshCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			OrdiniSettingsPage.this.loadData();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(PAGE_ID + ".refreshCommand");
		}

	}

	public static final String PAGE_ID = "ordiniSettingsPage";

	private IAnagraficaOrdiniBD anagraficaOrdiniBD;
	private RefreshCommand refreshCommand;

	/**
	 * Costruttore.
	 */
	public OrdiniSettingsPage() {
		super(PAGE_ID, new OrdiniSettingsForm());
	}

	@Override
	protected Object doSave() {
		OrdiniSettings ordiniSettings = (OrdiniSettings) getForm().getFormObject();
		ordiniSettings = anagraficaOrdiniBD.salvaOrdiniSettings(ordiniSettings);
		return ordiniSettings;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(), getRefreshCommand() };
		return abstractCommands;
	}

	/**
	 *
	 * @return command refresh.
	 */
	public ActionCommand getRefreshCommand() {
		if (this.refreshCommand == null) {
			this.refreshCommand = new RefreshCommand();
		}

		return this.refreshCommand;
	}

	@Override
	public void loadData() {
		OrdiniSettings ordiniSettings = anagraficaOrdiniBD.caricaOrdiniSettings();
		setFormObject(ordiniSettings);
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
		loadData();
	}

	/**
	 * @param anagraficaOrdiniBD
	 *            the anagraficaOrdiniBD to set
	 */
	public void setAnagraficaOrdiniBD(IAnagraficaOrdiniBD anagraficaOrdiniBD) {
		this.anagraficaOrdiniBD = anagraficaOrdiniBD;
	}

	@Override
	public void setFormObject(Object object) {
		OrdiniSettings ordiniSettings = anagraficaOrdiniBD.caricaOrdiniSettings();
		super.setFormObject(ordiniSettings);
	}

}
