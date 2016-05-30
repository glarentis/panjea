/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author fattazzo
 * 
 */
public class FormuleTrasformazioneCategoriaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "formuleTrasformazioneCategoriaForm";

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model della categoria
	 */
	public FormuleTrasformazioneCategoriaForm(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();

		return builder.getForm();
	}

}
