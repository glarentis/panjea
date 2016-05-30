/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.noteautomatiche;

import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.rich.bd.INoteAutomaticheBD;
import it.eurotn.panjea.anagrafica.rich.editors.noteautomatiche.NotaAutomaticaTipoDocForm.TutteClassiTipiDocumento;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 * 
 */
public class NotaAutomaticaTipoDocPage extends FormBackedDialogPageEditor {

	private INoteAutomaticheBD noteAutomaticheBD;

	/**
	 * Costruttore.
	 */
	public NotaAutomaticaTipoDocPage() {
		super("notaAutomaticaTipoDocPage", new NotaAutomaticaTipoDocForm());
	}

	@Override
	protected Object doDelete() {
		NotaAutomatica notaAutomatica = (NotaAutomatica) getBackingFormPage().getFormObject();
		noteAutomaticheBD.cancellaNotaAutomatica(notaAutomatica);
		return notaAutomatica;
	}

	@Override
	protected Object doSave() {
		NotaAutomatica notaAutomatica = (NotaAutomatica) getBackingFormPage().getFormObject();
		if (TutteClassiTipiDocumento.class.getName().equals(notaAutomatica.getClasseTipoDocumento())) {
			notaAutomatica.setClasseTipoDocumentoInstance(null);
		}
		notaAutomatica = noteAutomaticheBD.salvaNotaAutomatica(notaAutomatica);

		return notaAutomatica;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public void refreshData() {

	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof NotaAutomatica && ((NotaAutomatica) object).getId() != null) {
			Integer id = ((NotaAutomatica) object).getId();
			object = noteAutomaticheBD.caricaNotaAutomatica(id);
		}
		super.setFormObject(object);
	}

	/**
	 * @param noteAutomaticheBD
	 *            the noteAutomaticheBD to set
	 */
	public void setNoteAutomaticheBD(INoteAutomaticheBD noteAutomaticheBD) {
		this.noteAutomaticheBD = noteAutomaticheBD;
	}

}
