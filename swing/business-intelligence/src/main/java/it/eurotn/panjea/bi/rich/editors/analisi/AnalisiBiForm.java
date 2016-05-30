/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.analisi;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.AutoCompletionComboBox;

/**
 * @author leonardo
 *
 */
public class AnalisiBiForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "conaiArticoloForm";
	private IBusinessIntelligenceBD businessIntelligenceBD;
	private AutoCompletionComboBox categorieComboBox;

	/**
	 * Costruttore.
	 *
	 * @param analisiBi
	 *            parametri di analisi
	 */
	public AnalisiBiForm(final AnalisiBi analisiBi) {
		super(PanjeaFormModelHelper.createFormModel(analisiBi, false, FORM_ID), FORM_ID);
	}

	/**
	 * Costruttore.
	 *
	 * @param analisiBi
	 *            analisiBi
	 * @param businessIntelligenceBD
	 *            businessIntelligenceBD
	 */
	public AnalisiBiForm(final AnalisiBi analisiBi, final IBusinessIntelligenceBD businessIntelligenceBD) {
		super(PanjeaFormModelHelper.createFormModel(analisiBi, false, FORM_ID), FORM_ID);
		this.businessIntelligenceBD = businessIntelligenceBD;
	}

	@Override
	public void commit() {
		String categoria = (String) categorieComboBox.getSelectedItem();
		getValueModel("categoria").setValue(categoria);
		super.commit();
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref:grow, default", "10dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, t");
		builder.nextRow();
		builder.setRow(2);

		Set<String> nomi = new HashSet<String>();
		if (businessIntelligenceBD != null) {
			List<AnalisiBi> analisi = businessIntelligenceBD.caricaListaAnalisi();
			for (AnalisiBi parametriAnalisi : analisi) {
				nomi.add(parametriAnalisi.getCategoria());
			}
		}

		categorieComboBox = new AutoCompletionComboBox(nomi.toArray());
		categorieComboBox.getEditor().selectAll();
		categorieComboBox.setStrict(false);
		builder.addLabel("categoria", 1);
		builder.addComponent(categorieComboBox, 3);
		AnalisiBi analisiBi = (AnalisiBi) getFormObject();
		categorieComboBox.setSelectedItem(analisiBi.getCategoria());
		builder.nextRow();
		// JTextField text = (JTextField) builder.addPropertyAndLabel("categoria")[1];
		// AutoCompletion a = new AutoCompletion(text, new ArrayList<String>(nomi));
		// a.setStrict(false);
		// ComboBoxBinding comboBoxBinding = (ComboBoxBinding) bf.createBoundComboBox("categoria", nomi);
		// builder.nextRow();

		builder.addPropertyAndLabel("nome");
		builder.nextRow();

		builder.addPropertyAndLabel("descrizione");
		builder.nextRow();

		return builder.getPanel();
	}
}
