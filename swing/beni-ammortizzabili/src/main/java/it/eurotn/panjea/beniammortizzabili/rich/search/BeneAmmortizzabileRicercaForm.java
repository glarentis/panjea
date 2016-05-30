/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author Aracno
 * @version 1.0, 29/set/06
 * 
 */
public class BeneAmmortizzabileRicercaForm extends PanjeaAbstractForm {

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 * @param formId
	 *            id del form
	 */
	public BeneAmmortizzabileRicercaForm(final FormModel formModel, final String formId) {
		super(formModel, formId);
	}

	@Override
	protected JComponent createFormControl() {
		TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		((JTextField) builder.add("codiceBene", "colSpan=1 align=left")[1]).setColumns(10);
		((JTextField) builder.add("descrizione", "colSpan=1 align=left")[1]).setColumns(50);
		builder.row();
		builder.add("stato");
		builder.row();
		((JTextField) builder.add("annoAcquisto", "colSpan=1 align=left")[1]).setColumns(10);
		builder.row();
		builder.add("beneDiProprieta");
		builder.add("beneInLeasing");
		builder.row();
		return builder.getForm();
	}

}
