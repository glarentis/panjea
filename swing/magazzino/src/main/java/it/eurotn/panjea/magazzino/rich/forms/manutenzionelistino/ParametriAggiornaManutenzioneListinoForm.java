package it.eurotn.panjea.magazzino.rich.forms.manutenzionelistino;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriAggiornaManutenzioneListinoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriAggiornaManutenzioneListinoForm";

	/**
	 * Default constructor.
	 */
	public ParametriAggiornaManutenzioneListinoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriAggiornaManutenzioneListino(), false, FORM_ID),
				FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:default,10dlu,right:pref,4dlu,fill:default", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");

		builder.addLabel("versioneListino", 1);
		Binding bindVersioneListino = bf.createBoundSearchText("versioneListino", new String[] { "listino.codice",
				"listino.descrizione", "codice" });
		SearchPanel searchPanelVersioneListino = (SearchPanel) builder.addBinding(bindVersioneListino, 3);
		searchPanelVersioneListino.getTextFields().get("listino.codice").setColumns(8);
		searchPanelVersioneListino.getTextFields().get("listino.descrizione").setColumns(10);
		searchPanelVersioneListino.getTextFields().get("codice").setColumns(5);

		return builder.getPanel();
	}

}
