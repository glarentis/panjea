package it.eurotn.panjea.spedizioni.rich.editors.vettore;

import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class DatiSpedizioneVettoreForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "datiSpedizioneVettoreForm";

	/**
	 * Costruttore.
	 * 
	 */
	public DatiSpedizioneVettoreForm() {
		super(PanjeaFormModelHelper.createFormModel(new Vettore(), false, FORM_ID + "Model"), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,50dlu,10dlu,right:pref,4dlu,50dlu", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.setRow(2);

		builder.addPropertyAndLabel("datiSpedizioni.codiceClienteMittenteItalia", 1);
		builder.addPropertyAndLabel("datiSpedizioni.codiceClienteMittenteEstero", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.codiceTariffaItalia", 1);
		builder.addPropertyAndLabel("datiSpedizioni.codiceTariffaEstero", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.tipoStampante", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.puntoOperativoPartenzaMerce", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.tipoServizioBolle", 1);
		builder.nextRow();

		builder.addHorizontalSeparator(7);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.numeratore", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.etichettePath", 1, 16, 5);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.nameFileEtichetteToExport", 1);
		builder.addPropertyAndLabel("datiSpedizioni.nameFileEtichetteToImport", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.pathFileTemplateEtichette", 1, 20, 5);
		builder.nextRow();

		builder.addHorizontalSeparator(7);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.numeroSerie", 1);
		builder.addPropertyAndLabel("datiSpedizioni.puntoOperativoArrivo", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.naturaMerceMittente", 1);
		builder.addPropertyAndLabel("datiSpedizioni.codiceTrattamentoMerce", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.pathFileRendiconto", 1, 28, 5);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.pathFileTemplateRendiconto", 1, 30, 5);
		builder.nextRow();

		builder.addHorizontalSeparator(7);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.tipoInvio", 1);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.indirizzoInvio", 1, 36, 5);
		builder.nextRow();

		builder.addPropertyAndLabel("datiSpedizioni.userInvio", 1);
		builder.addPropertyAndLabel("datiSpedizioni.passwordInvio", 5);
		builder.nextRow();

		return builder.getPanel();
	}

}
