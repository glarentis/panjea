package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.pagamenti.service.interfaces.FlussoCBIDownload;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;
import it.eurotn.rich.dialog.MessageAlert;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

public class GeneraFlussoCommand extends ActionCommand {

	private class GenerazioneMessageAlert extends MessageAlert {

		/**
		 * Construttore di default per la finestra alert.
		 * 
		 * @param message
		 *            messaggio da visualizzare
		 */
		public GenerazioneMessageAlert(final Message message) {
			super(message);
		}

		@Override
		protected void applyAlertPreferences() {
			super.applyAlertPreferences();
			getAlert().setTimeout(0);
			getAlert().setTransient(true);
		}

		/**
		 * Chiude il dialogo di alert.
		 */
		@Override
		public void closeAlert() {
			getAlert().hidePopup();
		}
	}

	private static final String COMMAND_ID = "generaFlussoCommand";

	protected FlussoCBIDownload flussoCBIDownload;
	protected ITesoreriaBD tesoreriaBD;
	private AreaChiusure areaChiusure = null;

	/**
	 * costruttore di default.
	 * 
	 * @param flussoCBIDownload
	 *            manager per creare il flusso
	 * @param tesoreriaBD
	 *            bd per la tesoreria
	 */
	public GeneraFlussoCommand(final FlussoCBIDownload flussoCBIDownload, final ITesoreriaBD tesoreriaBD) {
		super(COMMAND_ID);
		setSecurityControllerId(AreaTesoreriaPage.PAGE_ID + ".controller");
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);
		// this.flussoCBIDownload = flussoCBIDownload;
		this.tesoreriaBD = tesoreriaBD;
		this.flussoCBIDownload = RcpSupport.getBean("flussoCBIDownload");
	}

	@Override
	protected void doExecuteCommand() {
		AsyncWorker.post(new AsyncTask() {
			private GenerazioneMessageAlert messageAlert;

			@Override
			public void failure(Throwable arg0) {
				logger.error("errore nella generazione del file flusso bancario", arg0);
				if (arg0 instanceof PanjeaRuntimeException && arg0.getCause() != null
						&& arg0.getCause() instanceof RapportoBancarioPerFlussoAssenteException) {
					throw (PanjeaRuntimeException) arg0;
				}
				if (messageAlert != null) {
					messageAlert.closeAlert();
				}
				Message message = new DefaultMessage("Esportazione del flusso non riuscita.\n" + arg0.getMessage(),
						Severity.ERROR);
				new MessageAlert(message).showAlert();
			}

			@Override
			public Object run() throws Exception {
				final JFileChooser fileChooser = new JFileChooser();

				fileChooser.setDialogTitle("Seleziona il file per l'esportazione");
				fileChooser.showOpenDialog(Application.instance().getActiveWindow().getControl());

				File localFile = fileChooser.getSelectedFile();
				if (localFile == null) {
					throw new RuntimeException("File selezionato non valido");
				}
				FileOutputStream file = null;

				Message message = new DefaultMessage("Esportazione del flusso  in corso...\n Generazione del file "
						+ localFile.getAbsolutePath(), Severity.INFO);
				messageAlert = new GenerazioneMessageAlert(message);
				messageAlert.showAlert();
				String pathFile = null;
				// genero il flusso sul server.
				try {
					pathFile = tesoreriaBD.generaFlusso(areaChiusure.getDocumento().getId());
				} catch (Exception e) {
					throw e;
				}
				// creo il file locale
				file = new FileOutputStream(localFile);
				file.write(flussoCBIDownload.getData(pathFile));
				file.close();
				return null;
			}

			@Override
			public void success(Object arg0) {
				if (messageAlert != null) {
					messageAlert.closeAlert();
				}
			}
		});
	}

	/**
	 * @param areaChiusure
	 *            the areaTesoreriaFullDTO to set
	 */
	public void setAreaChiusure(AreaChiusure areaChiusure) {
		this.areaChiusure = areaChiusure;
	}
}
