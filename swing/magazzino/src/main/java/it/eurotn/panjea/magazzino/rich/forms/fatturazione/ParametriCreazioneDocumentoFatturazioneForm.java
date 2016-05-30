/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.fatturazione;

import it.eurotn.panjea.magazzino.util.ParametriCreazioneDocumentoFatturazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Parametri per la creazione del documento di fatturazione.
 * 
 * @author fattazzo
 * 
 */
public class ParametriCreazioneDocumentoFatturazioneForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriCreazioneDocumentoFatturazioneForm";

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriCreazioneDocumentoFatturazioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneDocumentoFatturazione(), false, FORM_ID),
				FORM_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();

		builder.add("dataDocumento", "align=left");
		builder.add("note", "align=left");
		builder.row();

		return builder.getForm();
	}
}
