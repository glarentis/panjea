package it.eurotn.panjea.tesoreria.rich.editors.preference;

import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.preferences.GeneralSettingsPM;
import it.eurotn.panjea.preferences.PanjeaPreferencesManager;
import it.eurotn.panjea.tesoreria.domain.TesoreriaSettings;
import it.eurotn.panjea.tesoreria.manager.LettoreAssegniManager;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

public class TesoreriaSettingsPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(TesoreriaSettingsPage.class);
	private static final String PAGE_ID = "tesoreriaSettingsPage";

	private SettingsManager settingsManager = null;
	private ITesoreriaBD tesoreriaBD;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public TesoreriaSettingsPage() {
		super(PAGE_ID, new TesoreriaSettingsForm());
	}

	@Override
	protected Object doSave() {
		TesoreriaSettings tesoreriaSettings = (TesoreriaSettings) getForm().getFormObject();
		try {
			getSettingsManagerLocal().getUserSettings().setBoolean(GeneralSettingsPM.LETTURA_ASSEGNI,
					tesoreriaSettings.isLetturaAssegni());
			getSettingsManagerLocal().getUserSettings().save();
		} catch (Exception e) {
			logger.error("-->errore nel salvare i settings per la lettura assegno", e);
		}
		return tesoreriaBD.salva(tesoreriaSettings);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
		return abstractCommands;
	}

	/**
	 * 
	 * @return SettingManager
	 */
	private SettingsManager getSettingsManagerLocal() {
		if (settingsManager == null) {
			PanjeaPreferencesManager panjeaPreferencesManager = (PanjeaPreferencesManager) getApplication()
					.getApplicationContext().getBean("preferenceManager", PanjeaPreferencesManager.class);
			settingsManager = panjeaPreferencesManager.getSettingsManagerLocal();
		}
		return settingsManager;
	}

	/**
	 * @return Returns the tesoreriaBD.
	 */
	public ITesoreriaBD getTesoreriaBD() {
		return tesoreriaBD;
	}

	@Override
	public void loadData() {
		TesoreriaSettings tesoreriaSettings = tesoreriaBD.caricaSettings();
		try {
			tesoreriaSettings.setLetturaAssegni(getSettingsManagerLocal().getUserSettings().getBoolean(
					GeneralSettingsPM.LETTURA_ASSEGNI));
		} catch (SettingsException e) {
			logger.error("-->errore nel caricare i setttings per la lettura assegni", e);
			tesoreriaSettings.setLetturaAssegni(false);
		}
		setFormObject(tesoreriaSettings);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param documentiBD
	 *            The documentiBD to set.
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		((TesoreriaSettingsForm) getForm()).setDocumentiBD(documentiBD);
	}

	/**
	 * @param lettoreAssegniManager
	 *            the lettoreAssegniManager to set
	 */
	public void setLettoreAssegniManager(LettoreAssegniManager lettoreAssegniManager) {
		((TesoreriaSettingsForm) getForm()).setLettoreAssegniManager(lettoreAssegniManager);
	}

	/**
	 * @param tesoreriaBD
	 *            The tesoreriaBD to set.
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}
}
