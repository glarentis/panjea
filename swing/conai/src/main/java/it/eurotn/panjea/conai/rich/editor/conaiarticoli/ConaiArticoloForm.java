package it.eurotn.panjea.conai.rich.editor.conaiarticoli;

import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ConaiArticoloForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "conaiArticoloForm";

	/**
	 * Costruttore.
	 */
	public ConaiArticoloForm() {
		super(PanjeaFormModelHelper.createFormModel(new ConaiArticolo(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref, 10dlu, right:pref,4dlu,left:pref",
				"10dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, t");

		Binding articoloBinding = bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" });

		builder.nextRow();
		builder.setRow(2);
		builder.addLabel("articolo");
		SearchPanel articoloSearchPanel = (SearchPanel) builder.addBinding(articoloBinding, 3, 2, 3, 1);
		articoloSearchPanel.getTextFields().get("codice").setColumns(5);
		articoloSearchPanel.getTextFields().get("descrizione").setColumns(23);
		builder.nextRow();

		builder.addHorizontalSeparator(7);
		builder.nextRow();

		builder.addLabel("listiniConai");
		RigaConaiListinoInserimentoForm rigaInserimentoForm = new RigaConaiListinoInserimentoForm();
		Binding listiniConaiTableBinding = bf.createTableBinding("listiniConai", 190, new RigaConaiListinoTableModel(),
				rigaInserimentoForm);
		builder.addBinding(listiniConaiTableBinding, 3);

		builder.addLabel("esenzioni", 5);
		RigaConaiEsenzioneInserimentoForm conaiEsenzioneInserimentoForm = new RigaConaiEsenzioneInserimentoForm();
		Binding esenzioniTableBinding = bf.createTableBinding("esenzioni", 210, new RigaConaiEsenzioneTableModel(),
				conaiEsenzioneInserimentoForm);
		builder.addBinding(esenzioniTableBinding, 7);
		builder.nextRow();

		return builder.getPanel();
	}
}