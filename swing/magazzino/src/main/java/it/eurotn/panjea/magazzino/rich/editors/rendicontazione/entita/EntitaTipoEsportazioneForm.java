package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.entita;

import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class EntitaTipoEsportazioneForm extends PanjeaAbstractForm {

	/**
	 * Costruttore.
	 */
	public EntitaTipoEsportazioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new EntitaTipoEsportazione(), false, "entitaTipoEsportazioneForm"));
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:default, 4dlu, left:default", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());

		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("documentoExporter", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("tipoEsportazione", new String[] { "nome" }), 3);
		searchPanel.getTextFields().get("nome").setColumns(10);
		builder.nextRow();

		builder.addLabel("entita", 1);
		JComponent components = builder.addBinding(
				bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 3);
		((SearchPanel) components).getTextFields().get("codice").setColumns(5);
		((SearchPanel) components).getTextFields().get("anagrafica.denominazione").setColumns(23);

		return builder.getPanel();
	}

}
