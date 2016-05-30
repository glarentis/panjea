package it.eurotn.panjea.ordini.rich.editors.evasione.carrello.sostituzione;

import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.ArticoloSostituzione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ArticoloSostituzioneForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "articoloSostituzioneForm";
	private static final String FORMMODEL_ID = "articoloSostituzioneFormModel";

	/**
	 * Costruttore.
	 */
	public ArticoloSostituzioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new ArticoloSostituzione(), false, FORMMODEL_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r,c");

		builder.addLabel("articolo", 1);
		Binding bindingArticolo = bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" });
		builder.addBinding(bindingArticolo, 3);

		return builder.getPanel();
	}
}