package it.eurotn.panjea.beniammortizzabili.rich.editors.settings;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeniSettings;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class BeniAmmortizzabiliSettingsPage extends FormBackedDialogPageEditor {

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
			BeniAmmortizzabiliSettingsPage.this.loadData();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(PAGE_ID + ".refreshCommand");
		}

	}

	public static final String PAGE_ID = "beniAmmortizzabiliSettingsPage";

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;
	private RefreshCommand refreshCommand;

	/**
	 * Costruttore.
	 */
	public BeniAmmortizzabiliSettingsPage() {
		super(PAGE_ID, new BeniAmmortizzabiliSettingsForm());
	}

	@Override
	protected Object doSave() {
		BeniSettings beniSettings = (BeniSettings) getForm().getFormObject();
		beniSettings = beniAmmortizzabiliBD.salvaBeniSettings(beniSettings);
		return beniSettings;
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
		BeniSettings beniSettings = beniAmmortizzabiliBD.caricaBeniSettings();
		setFormObject(beniSettings);
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
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	public void setFormObject(Object object) {
		BeniSettings beniSettings = beniAmmortizzabiliBD.caricaBeniSettings();
		super.setFormObject(beniSettings);
	}

}
