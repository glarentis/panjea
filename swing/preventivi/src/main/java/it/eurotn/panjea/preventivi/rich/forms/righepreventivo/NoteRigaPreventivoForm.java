package it.eurotn.panjea.preventivi.rich.forms.righepreventivo;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;

import org.springframework.binding.form.FormModel;

public class NoteRigaPreventivoForm extends AbstractNoteRigaForm {

	public static final String FORM_ID = "noteRigaForm";

	/**
	 * 
	 * @param formModel
	 *            formModel
	 */
	public NoteRigaPreventivoForm(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected Documento getDocumento() {
		RigaArticolo rigaArticolo = (RigaArticolo) getFormObject();
		return rigaArticolo.getAreaPreventivo().getDocumento();
	}

}
