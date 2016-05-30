/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.settings;

import it.eurotn.panjea.magazzino.rich.bd.IPosSettingsBD;
import it.eurotn.panjea.pos.domain.PosSettings;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class PosSettingsPage extends FormBackedDialogPageEditor {

	private class RefreshCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 */
		public RefreshCommand() {
			super("refreshCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			loadData();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(REFRESH_COMMAND_ID);
		}

	}

	public static final String PAGE_ID = "posSettingsPage";
	public static final String REFRESH_COMMAND_ID = "refreshCommand";

	private IPosSettingsBD posSettingsBD;

	private RefreshCommand refreshCommand;

	/**
	 * Costruttore.
	 * 
	 */
	public PosSettingsPage() {
		super(PAGE_ID, new PosSettingsForm());
	}

	@Override
	protected Object doSave() {
		PosSettings posSettings = (PosSettings) getBackingFormPage().getFormObject();
		posSettings = posSettingsBD.salvaPosSettings(posSettings);
		return posSettings;
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
		PosSettings posSettings = posSettingsBD.caricaPosSettings();
		setFormObject(posSettings);
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

	@Override
	public void setFormObject(Object object) {
		PosSettings posSettings = posSettingsBD.caricaPosSettings();
		super.setFormObject(posSettings);
	}

	/**
	 * @param posSettingsBD
	 *            the posSettingsBD to set
	 */
	public void setPosSettingsBD(IPosSettingsBD posSettingsBD) {
		this.posSettingsBD = posSettingsBD;
	}

}
