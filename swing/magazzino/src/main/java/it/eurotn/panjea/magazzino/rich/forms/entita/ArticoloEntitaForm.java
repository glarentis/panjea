package it.eurotn.panjea.magazzino.rich.forms.entita;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ArticoloEntitaForm extends PanjeaAbstractForm {

	private class ArticoloPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				return;
			}

			Articolo articolo = (Articolo) getFormModel().getValueModel(
					"articolo").getValue();
			// se pulisco l'elemento dalla searchtext (F8), l'articolo che ricevo è null
			if (articolo != null) {
				getFormModel().getValueModel("codice").setValue(
						articolo.getCodice());
			}
		}

	}

	public static final String FORM_ID = "articoloEntitaForm";

	private ArticoloPropertyChange articoloPropertyChange;

	/**
	 * Costruttore.
	 */
	public ArticoloEntitaForm() {
		super(PanjeaFormModelHelper.createFormModel(new CodiceArticoloEntita(),
				false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default",
				"4dlu,default, 4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
		// new
		// FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.addLabel("articolo", 1, 2);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articolo", new String[] { "codice",
						"descrizione" }, ArticoloLite.class), 3, 2);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(35);

		builder.addPropertyAndLabel("codice", 1, 4);

		builder.addPropertyAndLabel("leadTime", 1, 6);

		articoloPropertyChange = new ArticoloPropertyChange();
		getFormModel().getValueModel("articolo").addValueChangeListener(
				articoloPropertyChange);

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		return super.createNewObject();
	}

	@Override
	public void dispose() {

		getFormModel().getValueModel("articolo").removeValueChangeListener(
				articoloPropertyChange);
		articoloPropertyChange = null;

		super.dispose();

	}
}
