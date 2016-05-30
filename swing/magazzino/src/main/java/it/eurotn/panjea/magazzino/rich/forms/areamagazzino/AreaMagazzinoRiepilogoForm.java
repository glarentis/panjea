package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextArea;

public class AreaMagazzinoRiepilogoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "areaMagazzinoRiepilogoForm";

	/**
	 * Costruttore.
	 * 
	 */
	public AreaMagazzinoRiepilogoForm() {
		super(PanjeaFormModelHelper.createFormModel(new AreaMagazzinoFullDTO(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		return new JTextArea("");
	}

}
