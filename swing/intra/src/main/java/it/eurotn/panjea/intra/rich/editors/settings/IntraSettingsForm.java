package it.eurotn.panjea.intra.rich.editors.settings;

import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class IntraSettingsForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "intraSettingsForm";

	/**
	 * Costruttore di default.
	 */
	public IntraSettingsForm() {
		super(PanjeaFormModelHelper.createFormModel(new IntraSettings(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default, fill:default:grow", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("tipoPeriodo");
		builder.nextRow();

		builder.addPropertyAndLabel("codiceUA");
		builder.nextRow();

		builder.addPropertyAndLabel("sezioneDoganale");
		builder.nextRow();

		builder.addPropertyAndLabel("progressivoSede");
		builder.nextRow();

		builder.addPropertyAndLabel("codiceFiscaleSpedizionere");
		builder.nextRow();

		JTextField percValStatisticoField = (JTextField) builder.addPropertyAndLabel("percValoreStatistico")[1];
		percValStatisticoField.setColumns(3);

		return builder.getPanel();
	}

}
