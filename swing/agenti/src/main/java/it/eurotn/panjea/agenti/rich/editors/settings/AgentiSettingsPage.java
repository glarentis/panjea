/**
 * 
 */
package it.eurotn.panjea.agenti.rich.editors.settings;

import it.eurotn.panjea.agenti.domain.AgentiSettings;
import it.eurotn.panjea.agenti.rich.bd.IAgentiSettingsBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class AgentiSettingsPage extends FormBackedDialogPageEditor {

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
			AgentiSettingsPage.this.loadData();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			button.setName(REFRESH_COMMAND_ID);
		}

	}

	public static final String PAGE_ID = "agentiSettingsPage";
	public static final String REFRESH_COMMAND_ID = PAGE_ID + ".refreshCommand";

	private IAgentiSettingsBD agentiSettingsBD;

	private RefreshCommand refreshCommand;

	/**
	 * Costruttore.
	 */
	public AgentiSettingsPage() {
		super(PAGE_ID, new AgentiSettingsForm());
	}

	@Override
	protected Object doSave() {
		AgentiSettings agentiSettings = (AgentiSettings) getBackingFormPage().getFormObject();
		agentiSettings = agentiSettingsBD.salvaAgentiSettings(agentiSettings);
		return agentiSettings;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(), getRefreshCommand() };
		return abstractCommands;
	}

	/**
	 * @return the refreshCommand
	 */
	private RefreshCommand getRefreshCommand() {
		if (refreshCommand == null) {
			refreshCommand = new RefreshCommand();
		}

		return refreshCommand;
	}

	@Override
	public void loadData() {
		AgentiSettings agentiSettings = agentiSettingsBD.caricaAgentiSettings();
		setFormObject(agentiSettings);
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
	 * @param agentiSettingsBD
	 *            the agentiSettingsBD to set
	 */
	public void setAgentiSettingsBD(IAgentiSettingsBD agentiSettingsBD) {
		this.agentiSettingsBD = agentiSettingsBD;
	}

	@Override
	public void setFormObject(Object object) {
		AgentiSettings agentiSettings = agentiSettingsBD.caricaAgentiSettings();
		super.setFormObject(agentiSettings);
	}

}
