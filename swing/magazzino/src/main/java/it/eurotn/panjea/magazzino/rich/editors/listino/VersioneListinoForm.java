/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author fattazzo
 * 
 */
public class VersioneListinoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "versioneListinoForm";

	/**
	 * Costruttore.
	 * 
	 */
	public VersioneListinoForm() {
		super(PanjeaFormModelHelper.createFormModel(new VersioneListino(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colSpec=right:pref");
		builder.row();
		builder.add("dataVigore", "align=left");
		builder.row();
		return builder.getForm();
	}

}
