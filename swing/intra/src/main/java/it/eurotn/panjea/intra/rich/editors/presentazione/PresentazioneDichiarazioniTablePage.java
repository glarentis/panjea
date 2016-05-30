package it.eurotn.panjea.intra.rich.editors.presentazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.commons.io.IOUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class PresentazioneDichiarazioniTablePage extends AbstractTablePageEditor<DichiarazioniIntraPM> {

	private class GeneraDichiarazioneCommand extends ActionCommand {
		/**
		 *
		 * Costruttore.
		 *
		 */
		public GeneraDichiarazioneCommand() {
			super("generaFileDichiarazioneCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}
			List<Integer> dichiarazioni = new ArrayList<Integer>();
			for (DichiarazioniIntraPM dichiarazioniIntraPM : getTable().getRows()) {
				if (dichiarazioniIntraPM.getSelezionata()) {
					dichiarazioni.add(dichiarazioniIntraPM.getDichiarazioneIntra().getId());
				}
			}
			intraBD.generaFileEsportazione(dichiarazioni, true);
			PresentazioneDichiarazioniTablePage.this.loadData();
		}

	}

	private class GeneraScambiCEECommand extends ActionCommand {
		/**
		 *
		 * Costruttore.
		 *
		 */
		public GeneraScambiCEECommand() {
			super("generaScambiCEECommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}
			List<Integer> dichiarazioni = new ArrayList<Integer>();
			for (DichiarazioniIntraPM dichiarazioniIntraPM : getTable().getRows()) {
				if (dichiarazioniIntraPM.getSelezionata()) {
					dichiarazioni.add(dichiarazioniIntraPM.getDichiarazioneIntra().getId());
				}
			}
			FileDichiarazione fileDichiarazione = intraBD.generaFileEsportazione(dichiarazioni, false);
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

	private class PresentaDichiarazioneCommand extends ActionCommand {

		/**
		 *
		 * Costruttore.
		 *
		 */
		public PresentaDichiarazioneCommand() {
			super("presentaDichiarazioneCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}
			List<Integer> dichiarazioni = new ArrayList<Integer>();
			for (DichiarazioniIntraPM dichiarazioniIntraPM : getTable().getRows()) {
				if (dichiarazioniIntraPM.getSelezionata()) {
					dichiarazioni.add(dichiarazioniIntraPM.getDichiarazioneIntra().getId());
				}
			}
			intraBD.spedisciFileEsportazione(dichiarazioni);
		}

	}

	private class PropertyCommand extends ActionCommand {

		@Override
		protected void doExecuteCommand() {
			DichiarazioniIntraPM dichiarazioneIntraPM = getTable().getSelectedObject();
			if (dichiarazioneIntraPM != null) {
				DichiarazioneIntra dichiarazione = intraBD.caricaDichiarazioneIntra(dichiarazioneIntraPM
						.getDichiarazioneIntra().getId());
				LifecycleApplicationEvent event = new OpenEditorEvent(dichiarazione);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}

	}

	private IIntraBD intraBD;

	/**
	 *
	 * Costruttore.
	 *
	 */
	protected PresentazioneDichiarazioniTablePage() {
		super("presentazioneDichiarazioniTablePage", new PresentazioneDichiarazioniTableModel());
		getTable().setPropertyCommandExecutor(new PropertyCommand());
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { new GeneraScambiCEECommand(), new PresentaDichiarazioneCommand(),
				new GeneraDichiarazioneCommand() };
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	public Collection<DichiarazioniIntraPM> loadTableData() {
		List<DichiarazioneIntra> dichiarazioniDaPresentare = intraBD.caricaDichiarazioniIntraDaPresentare();
		List<DichiarazioniIntraPM> dichiarazioniPM = new ArrayList<DichiarazioniIntraPM>();
		for (DichiarazioneIntra dichiarazioneIntra : dichiarazioniDaPresentare) {
			dichiarazioniPM.add(new DichiarazioniIntraPM(dichiarazioneIntra));
		}
		return dichiarazioniPM;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public Collection<DichiarazioniIntraPM> refreshTableData() {
		return null;
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
