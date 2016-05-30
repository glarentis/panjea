package it.eurotn.panjea.bi.rich.editors.analisi.commands;

import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;
import it.eurotn.rich.report.editor.export.EsportazioneStampaMessageAlert;

import java.io.File;

import javax.swing.JFileChooser;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

public class EsportaAnalisiCommand extends ApplicationWindowAwareCommand {
	class XLSFileFilter extends javax.swing.filechooser.FileFilter {
		@Override
		public boolean accept(File file) {

			if (file.isDirectory()) {
				return true;
			}

			String filename = file.getName();
			return filename.endsWith(".xls");
		}

		@Override
		public String getDescription() {
			return "*.xls";
		}
	}

	public static final String COMMAND_ID = "DWEsportaAnalisiCommand";

	private AnalisiBiEditorController analisiBiEditorController;
	private final EsportazioneStampaMessageAlert alert = new EsportazioneStampaMessageAlert();

	/**
	 * Costruttore.
	 *
	 * @param analisiBiEditorController
	 *            Istanza della controller dell'editor.
	 */
	public EsportaAnalisiCommand(final AnalisiBiEditorController analisiBiEditorController) {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
		this.analisiBiEditorController = analisiBiEditorController;
	}

	@Override
	protected void doExecuteCommand() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleziona il file per l'esportazione");
		fileChooser.addChoosableFileFilter(new XLSFileFilter());
		String nomeAnalisi = analisiBiEditorController.getAnalisiBi().getNome();
		File fileToExport = new File(System.getProperty("java.io.tmpdir") + "/" + nomeAnalisi);
		fileChooser.setSelectedFile(fileToExport);
		int returnVal = fileChooser.showSaveDialog(Application.instance().getActiveWindow().getControl());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			alert.showAlert();
			File localFile = fileChooser.getSelectedFile();
			File fileWithExt = new File(localFile.getPath());
			if (!localFile.getName().endsWith("xls")) {
				if (localFile.getName().endsWith("/.")) {
					fileWithExt = new File(localFile.getPath() + "xls");
				} else {
					fileWithExt = new File(localFile.getPath() + ".xls");
				}
			}
			try {
				analisiBiEditorController.esportaPivotPane(fileWithExt.getCanonicalPath());
			} catch (Exception e) {
				logger.error("--> errore nell'esportare l'analisi " + nomeAnalisi, e);
				alert.errorExport();
				throw new RuntimeException(e);
			}
			alert.finishExport(fileWithExt);
		}
	}
}