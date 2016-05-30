package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class SedeAziendaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "sedeAziendaForm";

	/**
	 * Costruttore.
	 * 
	 * @param sedeAzienda
	 *            sede azienda
	 */
	public SedeAziendaForm(final SedeAzienda sedeAzienda) {
		super(PanjeaFormModelHelper.createFormModel(sedeAzienda, false, FORM_ID + "Model"), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"70dlu,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, right:pref,4dlu,fill:default:grow",
				"3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_DESCRIZIONE, 1, 2, 5);
		builder.nextRow();
		builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_INDIRIZZO, 1, 4, 5);
		builder.nextRow();

		DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf.createDatiGeograficiBinding(
				"sede.datiGeografici", "right:70dlu");
		builder.addBinding(bindingDatiGeografici, 1, 6, 7, 1);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_TELEFONO, 1)[1]).setColumns(11);
		((JTextField) builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_FAX, 5)[1]).setColumns(11);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, 1)[1]).setColumns(20);
		builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_WEB, 5);
		builder.nextRow();

		SedeAzienda sedeAzienda = (SedeAzienda) this.getFormObject();
		if (sedeAzienda.getTipoSede() != null && !sedeAzienda.getTipoSede().isSedePrincipale()) {
			builder.addLabel("tipoSede", 1);
			builder.addBinding(bf.createBoundSearchText("tipoSede", new String[] { null }), 3);
			builder.nextRow();

			builder.addPropertyAndLabel("abilitato", 1);
		} else {
			builder.addLabel("tipoSede", 1);
			builder.addBinding(bf.createBoundLabel("tipoSede.descrizione"), 3);
		}
		builder.nextRow();

		return builder.getPanel();
	}

}
