package it.eurotn.panjea.spedizioni.rich.editors.vettore;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.spedizioni.rich.bd.ISpedizioniBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.MessageDialog;

public class DatiSpedizioneVettorePage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "datiSpedizioneVettorePage";

	private Entita entita;

	private ISpedizioniBD spedizioniBD;

	public DatiSpedizioneVettorePage() {
		super(PAGE_ID, new DatiSpedizioneVettoreForm());
	}

	@Override
	protected Object doSave() {
		Entita entitaDaSalvare = ((Entita) getBackingFormPage().getFormObject());
		Entita entitaResult = null;
		try {
			entitaResult = spedizioniBD.salvaEntita(entitaDaSalvare);
		} catch (AnagraficheDuplicateException e) {
			e.printStackTrace();
		}

		getBackingFormPage().setFormObject(entitaResult);

		return entitaResult;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
		return commands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return null;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		boolean initializePage = true;
		if (entita.isNew()) {
			initializePage = false;
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
					Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage("entita.null.messageDialog.message",
					new Object[] { messageSourceAccessor.getMessage(entita.getDomainClassName(), new Object[] {},
							Locale.getDefault()) }, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	public void refreshData() {

	}

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);
		this.entita = (Entita) object;
	}

	/**
	 * @param spedizioniBD
	 *            the spedizioniBD to set
	 */
	public void setSpedizioniBD(ISpedizioniBD spedizioniBD) {
		this.spedizioniBD = spedizioniBD;
	}

}
