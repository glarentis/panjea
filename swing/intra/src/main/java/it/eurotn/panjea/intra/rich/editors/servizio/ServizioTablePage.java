package it.eurotn.panjea.intra.rich.editors.servizio;

import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class ServizioTablePage extends AbstractTablePageEditor<Servizio> {

	private class ImportaCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public ImportaCommand() {
			super("importaServiziCommand");
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
					intraBD.importaServizi(byteToSend);
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

	public static final String PAGE_ID = "servizioTablePage";

	private IIntraBD intraBD;

	/**
	 * Costruttore.
	 */
	protected ServizioTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione" }, Servizio.class);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { new ImportaCommand() };
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Servizio> loadTableData() {
		return (Collection<Servizio>) intraBD.caricaServizi(Servizio.class, "codice", null);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public Collection<Servizio> refreshTableData() {
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
