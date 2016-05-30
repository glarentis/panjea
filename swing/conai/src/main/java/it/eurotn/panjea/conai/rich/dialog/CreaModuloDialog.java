package it.eurotn.panjea.conai.rich.dialog;

import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.rich.bd.IConaiBD;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.editor.export.EsportazioneStampaMessageAlert;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class CreaModuloDialog extends PanjeaTitledPageApplicationDialog {

	private IConaiBD conaiBD;

	/**
	 * Costruttore.
	 */
	public CreaModuloDialog() {
		super(new CreaModuloForm(), null);
	}

	@Override
	protected Dimension getPreferredSize() {
		return new Dimension(800, 350);
	}

	@Override
	protected void onAboutToShow() {
		FormBackedDialogPage page = (FormBackedDialogPage) getDialogPage();
		CreaModuloForm creaModuloForm = (CreaModuloForm) page.getBackingFormPage();

		// carica i dati impostati nel properties locale del cliente
		creaModuloForm.loadConaiParametriCreazione();
		super.onAboutToShow();
	}

	@Override
	protected boolean onFinish() {
		FormBackedDialogPage page = (FormBackedDialogPage) getDialogPage();
		CreaModuloForm creaModuloForm = (CreaModuloForm) page.getBackingFormPage();
		creaModuloForm.getFormModel().commit();
		ConaiParametriCreazione parametri = (ConaiParametriCreazione) creaModuloForm.getFormObject();
		try {
			File fileExport = new File(parametri.getPathCreazione() + File.separatorChar
					+ parametri.getMateriale().name() + ".pdf");
			FileOutputStream fos = new FileOutputStream(fileExport);
			fos.write(conaiBD.generaModulo(parametri));
			fos.close();
			EsportazioneStampaMessageAlert alert = new EsportazioneStampaMessageAlert();
			alert.showAlert();
			alert.finishExport(fileExport);

			// salva i dati impostati nel properties locale del cliente
			creaModuloForm.saveConaiParametriCreazione();
		} catch (FileNotFoundException e) {
			String testoMessaggio = RcpSupport.getMessage("", "percorsoFileNonValido", "message",
					new Object[] { parametri.getPathCreazione() });
			Message messaggio = new DefaultMessage(testoMessaggio, Severity.ERROR);
			String title = RcpSupport.getMessage("", "percorsoFileNonValido", "title");
			MessageDialog messageDialog = new MessageDialog(title, messaggio);
			messageDialog.showDialog();
			return false;
		} catch (IOException e) {
			throw new PanjeaRuntimeException(e);
		}
		return true;
	}

	/**
	 * @param conaiBD
	 *            The conaiBD to set.
	 */
	public void setConaiBD(IConaiBD conaiBD) {
		this.conaiBD = conaiBD;
	}

}
