package it.eurotn.panjea.conai.rich.editor.conaiarticoli;

import it.eurotn.panjea.conai.domain.ConaiListino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class RigaConaiListinoInserimentoForm extends AbstractFocussableForm {

	/**
	 * Costruttore.
	 */
	public RigaConaiListinoInserimentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ConaiListino(), false, "conaiListino"));
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,right:pref,4dlu,fill:80 dlu,12dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("l,c");
		builder.addProperty("dataInizio");
		builder.addProperty("dataFine", 3);
		builder.addProperty("prezzo", 5);
		builder.addComponent(new JLabel("€"), 6);
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ConaiListino listino = new ConaiListino();
		return listino;
	}
}
