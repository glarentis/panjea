package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Gestisce la costruzione della CategorieratePage.
 * 
 * @author vittorio
 * @version 1.0, 30/apr/08
 */
public class CategoriaRateForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "categoriaRateForm";

	/**
	 * Costruttore.
	 * 
	 * @param categoriaRata
	 *            categoria rata
	 */
	public CategoriaRateForm(final CategoriaRata categoriaRata) {
		super(PanjeaFormModelHelper.createFormModel(categoriaRata, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();

		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(40);
		builder.row();
		builder.add("tipoCategoria", "align=left");

		builder.row();
		return builder.getForm();
	}

}
