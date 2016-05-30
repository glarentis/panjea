package it.eurotn.panjea.magazzino.rich.forms.articolo;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ArticoloNoteForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "articoloNoteForm";

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formModel
	 */
	public ArticoloNoteForm(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:default:grow", "fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.addBinding(bf.createBoundTextArea("note"));

		return builder.getPanel();
	}

}
