package it.eurotn.panjea.magazzino.rich.editors.raggruppamentoArticoli;

import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class RigaRaggruppamentoArticoliForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "rigaRaggruppamentoArticoliForm";
	private JFormattedTextField qtaFormattedTextField;

	/**
	 * Costruttore.
	 */
	public RigaRaggruppamentoArticoliForm() {
		super(PanjeaFormModelHelper.createFormModel(new RigaRaggruppamentoArticoli(), false, FORM_ID + "Model"),
				FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,4dlu,fill:pref", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);
		builder.addLabel("articolo");
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(25);
		builder.nextRow();
		builder.addLabel("qta");
		qtaFormattedTextField = (JFormattedTextField) builder.addBinding(
				bf.createBoundFormattedTextField("qta", getFactory()), 3);
		qtaFormattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		installNumerDecimaliQtaPropertyChange();
		return builder.getPanel();
	}

	/**
	 * 
	 * @return factory per la formattazione qtaDecimanli
	 */
	private DefaultFormatterFactory getFactory() {
		Integer numeroDecimaliQt = (Integer) getFormModel().getValueModel("articolo.numeroDecimaliQta").getValue();
		if (numeroDecimaliQt == null) {
			numeroDecimaliQt = 6;
		}
		DefaultNumberFormatterFactory f = new DefaultNumberFormatterFactory("#,##0", numeroDecimaliQt, Double.class);
		return f;
	}

	/**
	 * Installa il listener sulla proprietà "rigaArticolo.numeroDecimaliQta".
	 */
	private void installNumerDecimaliQtaPropertyChange() {
		getFormModel().getValueModel("articolo.numeroDecimaliQta").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				DefaultFormatterFactory factory = getFactory();
				qtaFormattedTextField.setFormatterFactory(factory);
			}

		});
	}
}
