package it.eurotn.panjea.contabilita.rich.editors.fatturato;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class FatturatoContabilitaForm extends PanjeaAbstractForm {

	/**
	 * Costruttore.
	 */
	public FatturatoContabilitaForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaFatturato(), false, "fatturatoContabilitaForm"));
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:default,4dlu,60dlu,130dlu",
				"10dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("annoCompetenza", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("periodo", 1, 4, 2, 1);
		builder.nextRow();
		builder.addPropertyAndLabel("tipoEntitaFatturazione", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("fatturatoPerSedi", 1);
		builder.nextRow();

		return builder.getPanel();
	}
}
