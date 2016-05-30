package it.eurotn.panjea.magazzino.rich.forms.articolo;

import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class CodiceArticoloEntitaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "codiceArticoloEntitaForm";

	/**
	 * Costruttore di default, inizializza un nuovo CodiceArticoloEntita.
	 */
	public CodiceArticoloEntitaForm() {
		super(PanjeaFormModelHelper.createFormModel(new CodiceArticoloEntita(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:90dlu,fill:120dlu", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("entita", 1);
		builder.addBinding(getEntitaBinding(), 3, 2, 2, 1);
		builder.nextRow();
		builder.addPropertyAndLabel("codice", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("descrizione", 1, 6, 2);
		builder.nextRow();
		builder.addPropertyAndLabel("barCode", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("barCode2", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("leadTime", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("qtaMinimaOrdinabile", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("lottoRiordino", 1);
		builder.nextRow();

		builder.nextRow();
		builder.addPropertyAndLabel("ggSicurezza", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("consegnaContoTerzi", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("entitaPrincipale", 1);
		builder.nextRow();

		return builder.getPanel();
	}

	/**
	 *
	 * @return Binding per l'entità
	 */
	private Binding getEntitaBinding() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		Binding bindingEntita = bf.createBoundSearchText("entita",
				new String[] { "codice", "anagrafica.denominazione" });
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("codice").setColumns(5);
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("anagrafica.denominazione").setColumns(25);
		return bindingEntita;
	}
}
