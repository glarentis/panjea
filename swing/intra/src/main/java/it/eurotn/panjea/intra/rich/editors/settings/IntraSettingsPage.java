package it.eurotn.panjea.intra.rich.editors.settings;

import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;

import org.apache.commons.io.IOUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class IntraSettingsPage extends FormBackedDialogPageEditor {
	private class ImportaCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public ImportaCommand() {
			super("importaDatiIntraArticoliCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			JFileChooser fileChooser = new JFileChooser(PanjeaSwingUtil.getHome().toFile());
			int returnVal = fileChooser.showOpenDialog(Application.instance().getActiveWindow().getControl());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				java.io.File file = fileChooser.getSelectedFile();
				try (FileInputStream fis = new FileInputStream(file)) {
					byte[] byteToSend = IOUtils.toByteArray(fis);
					String log = intraBD.associaNomenclatura(byteToSend);
					String pathLog = file.getParent();
					File fileLog = new File(pathLog + "/associazionelog.txt");
					fileLog.delete();
					FileOutputStream stream = new FileOutputStream(fileLog);
					IOUtils.write(log, stream);
					stream.close();
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().open(fileLog);
					} else {
						MessageDialog dialog = new MessageDialog("Log", "Aprire il file " + fileLog.getAbsolutePath()
								+ " per visualizzare il log");
						dialog.showDialog();
					}
				} catch (FileNotFoundException e1) {
					logger.error("-->errore nell'aprire il file", e1);
					DefaultMessage message = new DefaultMessage(e1.getMessage().replace("/", "\\"), Severity.ERROR);
					MessageDialog dialog = new MessageDialog("Errore", message);
					dialog.showDialog();
				} catch (IOException e) {
					logger.error("-->errore nell'aprire il file", e);
					DefaultMessage message = new DefaultMessage(e.getCause().getMessage(), Severity.ERROR);
					MessageDialog dialog = new MessageDialog("Errore", message);
					dialog.showDialog();
				}
			}
		}
	}

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
			IntraSettingsPage.this.loadData();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			button.setName(REFRESH_COMMAND_ID);
		}

	}

	public static final String PAGE_ID = "intraSettingsPage";
	public static final String REFRESH_COMMAND_ID = PAGE_ID + ".refreshCommand";

	private IIntraBD intraBD = null;
	private RefreshCommand refreshCommand = null;

	/**
	 * Costruttore.
	 */
	public IntraSettingsPage() {
		super(PAGE_ID, new IntraSettingsForm());
	}

	@Override
	protected Object doSave() {
		IntraSettings intraSettings = (IntraSettings) getForm().getFormObject();
		intraSettings = intraBD.salvaIntraSettings(intraSettings);
		return intraSettings;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(), getRefreshCommand(),
				new ImportaCommand() };
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
		setFormObject(null);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public void refreshData() {
		loadData();
	}

	@Override
	public void setFormObject(Object object) {
		IntraSettings magazzinoSettings = intraBD.caricaIntraSettings();
		super.setFormObject(magazzinoSettings);
	}

	/**
	 * @param intraBD
	 *            the intraBD to set
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}

}
