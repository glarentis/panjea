package it.eurotn.panjea.magazzino.rich.forms.categoriacontabile;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class CategoriaContabileDepositoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "categoriaContabileDepositoForm";

	/**
	 * Costruttore.
	 * 
	 * @param categoriaContabileDeposito
	 *            {@link CategoriaContabileDeposito}
	 */
	public CategoriaContabileDepositoForm(final CategoriaContabileDeposito categoriaContabileDeposito) {
		super(PanjeaFormModelHelper.createFormModel(categoriaContabileDeposito, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(30);
		builder.row();

		return builder.getForm();
	}

}
