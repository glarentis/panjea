package it.eurotn.panjea.magazzino.rich.forms.depositomagazzino;

import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class DepositoMagazzinoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "depositoMagazzinoForm";

	/**
	 * Costruttore.
	 * 
	 */
	public DepositoMagazzinoForm() {
		super(PanjeaFormModelHelper.createFormModel(new DepositoMagazzino(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.row();
		builder.setLabelAttributes("colSpec=right:pref");
		builder.add(bf.createBoundSearchText("categoriaContabileDeposito", new String[] { "codice" }), "align=left");
		builder.row();
		return builder.getForm();
	}

}
