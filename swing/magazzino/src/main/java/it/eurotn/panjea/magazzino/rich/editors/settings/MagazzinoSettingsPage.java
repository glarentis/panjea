package it.eurotn.panjea.magazzino.rich.editors.settings;

import it.eurotn.panjea.magazzino.domain.AddebitoDichiarazioneIntentoSettings;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.SogliaAddebitoDichiarazioneSettings;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

public class MagazzinoSettingsPage extends FormBackedDialogPageEditor {

	private class RefreshCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public RefreshCommand() {
			super("refreshCommand");
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(REFRESH_COMMAND_ID);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			MagazzinoSettingsPage.this.loadData();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			button.setName(REFRESH_COMMAND_ID);
		}

	}

	public static final String PAGE_ID = "magazzinoSettingsPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private RefreshCommand refreshCommand = null;

	public static final String REFRESH_COMMAND_ID = PAGE_ID + ".refreshCommand";

	/**
	 * Costruttore.
	 */
	public MagazzinoSettingsPage() {
		super(PAGE_ID, new MagazzinoSettingsForm());
	}

	@Override
	protected Object doSave() {
		MagazzinoSettings magazzinoSettings = (MagazzinoSettings) getForm().getFormObject();
		String numeratoreBarCode = magazzinoSettings.getNumeratoreBarCode();
		if (numeratoreBarCode != null) {
			String[] numeratoreSplit = numeratoreBarCode.split(" - ");
			magazzinoSettings.setNumeratoreBarCode(numeratoreSplit[0]);
		}
		for (AddebitoDichiarazioneIntentoSettings addebitoDichSetting : magazzinoSettings
				.getAddebitiDichiarazioneIntento()) {
			addebitoDichSetting.setMagazzinoSettings(magazzinoSettings);
		}
		for (SogliaAddebitoDichiarazioneSettings sogliaDichSetting : magazzinoSettings.getSogliaAddebitoDichiarazione()) {
			sogliaDichSetting.setMagazzinoSettings(magazzinoSettings);
		}
		magazzinoSettings = magazzinoAnagraficaBD.salvaMagazzinoSettings(magazzinoSettings);
		return magazzinoSettings;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(), getRefreshCommand() };
		return abstractCommands;
	}

	/**
	 * @return refreshCommand
	 */
	public ActionCommand getRefreshCommand() {
		if (this.refreshCommand == null) {
			this.refreshCommand = new RefreshCommand();
		}

		return this.refreshCommand;
	}

	@Override
	public void loadData() {
		MagazzinoSettings contabilitaSettings = magazzinoAnagraficaBD.caricaMagazzinoSettings();
		setFormObject(contabilitaSettings);
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
		MagazzinoSettings magazzinoSettings = magazzinoAnagraficaBD.caricaMagazzinoSettings();
		super.setFormObject(magazzinoSettings);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
