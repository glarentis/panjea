package it.eurotn.panjea.anagrafica.rich.editors.azienda.deposito.stampa;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.Set;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriDepositoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriDepositoForm";

	private Set<String> reportDisponibili;

	/**
	 * Costruttore.
	 */
	public ParametriDepositoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriDeposito(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref, 4dlu,fill:180dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); //, new FormDebugPanelNumbered());

		builder.setLabelAttributes("r,c");


		builder.addLabel("stampa",1);

		Binding reportBinding = bf.createBoundComboBox("report", reportDisponibili);
		builder.addBinding(reportBinding, 3);
		builder.nextRow();

		builder.addPropertyAndLabel("periodo");

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ParametriDeposito parametriDeposito = new ParametriDeposito();
		parametriDeposito.setReport(reportDisponibili.iterator().next());
		return parametriDeposito;
	}

	/**
	 * @param reportDisponibili the reportDisponibili to set
	 */
	public void setReportDisponibili(Set<String> reportDisponibili) {
		this.reportDisponibili = reportDisponibili;
	}

}
