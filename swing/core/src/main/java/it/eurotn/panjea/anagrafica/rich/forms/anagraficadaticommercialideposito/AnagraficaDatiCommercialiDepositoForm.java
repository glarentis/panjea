package it.eurotn.panjea.anagrafica.rich.forms.anagraficadaticommercialideposito;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class AnagraficaDatiCommercialiDepositoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "anagraficaDatiCommercialiDepositoForm";

	/**
	 * Costruttore.
	 * 
	 */
	public AnagraficaDatiCommercialiDepositoForm() {
		super(PanjeaFormModelHelper.createFormModel(new Deposito(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		return new JPanel();
	}

}
