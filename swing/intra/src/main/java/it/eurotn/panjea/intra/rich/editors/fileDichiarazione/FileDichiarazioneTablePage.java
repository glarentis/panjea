package it.eurotn.panjea.intra.rich.editors.fileDichiarazione;

import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import javax.swing.JFileChooser;

import org.apache.commons.io.IOUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class FileDichiarazioneTablePage extends AbstractTablePageEditor<FileDichiarazione> {

	private class DeleteCommand extends AbstractDeleteCommand {

		/**
		 *
		 * Costruttore.
		 *
		 */
		public DeleteCommand() {
			super("deleteCommand");
		}

		@Override
		public Object onDelete() {
			FileDichiarazione file = getTable().getSelectedObject();
			if (file != null) {
				intraBD.cancellaFileDichiarazioni(file.getId());
			}
			return file;
		}

	}

	private class EsportaFileCommand extends ActionCommand {

		/**
		 *
		 * Costruttore.
		 *
		 */
		public EsportaFileCommand() {
			super("esportaCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			FileDichiarazione fileDichiarazione = getTable().getSelectedObject();
			if (fileDichiarazione != null) {
				JFileChooser fileChooser = new JFileChooser(PanjeaSwingUtil.getHome().toFile());
				File fileToExport = new File(fileDichiarazione.getNome());
				fileChooser.setSelectedFile(fileToExport);
				int returnVal = fileChooser.showSaveDialog(Application.instance().getActiveWindow().getControl());

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileChooser.getSelectedFile();
					try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
						IOUtils.write(fileDichiarazione.getContent(), fileOutputStream);
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
	}

	private class ModificaCommand extends ActionCommand {
		/**
		 *
		 * Costruttore.
		 *
		 */
		public ModificaCommand() {

		}

		@Override
		protected void doExecuteCommand() {
			FileDichiarazione fileDichiarazione = getTable().getSelectedObject();
			if (fileDichiarazione != null) {
				InputApplicationDialog dialog = new InputApplicationDialog(fileDichiarazione, "nome");
				dialog.setInputLabelMessage("Nome del file");
				dialog.showDialog();
				fileDichiarazione = intraBD.aggiornaNomeFileDichiarazione(fileDichiarazione.getId(), dialog
						.getInputValue().toString());
				getTable().replaceRowObject(fileDichiarazione, fileDichiarazione, null);

			}
		}

	}

	private IIntraBD intraBD;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public FileDichiarazioneTablePage() {
		super("fileDichiarazioneTablePage",
				new String[] { "nome", "stato", "dataCreazione", "dichiarazioniDescrizione" }, FileDichiarazione.class);
		getTable().setPropertyCommandExecutor(new ModificaCommand());
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRefreshCommand(), new DeleteCommand(), new EsportaFileCommand() };
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	public Collection<FileDichiarazione> loadTableData() {
		return intraBD.caricaFileDichiarazioni();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public Collection<FileDichiarazione> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}

}
