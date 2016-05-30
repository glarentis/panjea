package it.eurotn.panjea.magazzino.rich.editors.raggruppamentoArticoli;

import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class RaggruppamentoArticoliForm extends PanjeaAbstractForm {
	public static final String FORM_ID = "scontoForm";

	/**
	 * Costruttore.
	 */
	public RaggruppamentoArticoliForm() {
		super(PanjeaFormModelHelper.createFormModel(new RaggruppamentoArticoli(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,4dlu,fill:pref:grow", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);
		builder.addPropertyAndLabel("descrizione");
		return builder.getPanel();
	}

}
