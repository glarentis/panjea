/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.omaggio;

import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 */
public class OmaggioForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "omaggioForm";
	private SearchPanel searchPanelCodiceIva;

	/**
	 * Costruttore.
	 */
	public OmaggioForm() {
		super(PanjeaFormModelHelper.createFormModel(new Omaggio(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref, 10dlu, right:pref,4dlu,left:pref",
				"10dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, t");
		builder.nextRow();
		builder.setRow(2);

		JComboBox comboBox = (JComboBox) builder.addPropertyAndLabel("tipoOmaggio")[1];
		// i tipi omaggio sono 4 e non sono modificabili, vengono generati automaticamente al caricamento dei dati, non
		// posso quindi cambiare il tipo omaggio
		comboBox.setFocusable(false);

		builder.nextRow();

		Binding bindingCodIva = bf.createBoundSearchText("codiceIva", new String[] { "codice" });
		builder.addLabel("codiceIva");
		searchPanelCodiceIva = (SearchPanel) builder.addBinding(bindingCodIva, 3);
		searchPanelCodiceIva.getTextFields().get("codice").setColumns(5);
		builder.nextRow();

		builder.addPropertyAndLabel("descrizionePerStampa");
		builder.nextRow();

		return builder.getPanel();
	}

	/**
	 * Request focus per il campo codiceIva.
	 */
	public void requestFocusOnCodiceIva() {
		if (searchPanelCodiceIva != null && searchPanelCodiceIva.getTextFields().get("codice") != null) {
			searchPanelCodiceIva.getTextFields().get("codice").getTextField().requestFocusInWindow();
		}
	}

}
