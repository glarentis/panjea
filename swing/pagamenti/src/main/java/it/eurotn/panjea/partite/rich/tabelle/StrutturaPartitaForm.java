package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class StrutturaPartitaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "strutturaPartiteForm";

	/**
	 * Costruttore.
	 * 
	 * @param strutturaPartita
	 *            struttura partita
	 */
	public StrutturaPartitaForm(final StrutturaPartita strutturaPartita) {
		super(PanjeaFormModelHelper.createFormModel(new StrutturaPartita(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,default,default,default,default,default,default,default, fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("descrizione");
		builder.nextRow();

		JTextField textGgFisso = (JTextField) builder.addPropertyAndLabel("giornoFisso")[1];
		textGgFisso.setColumns(4);
		JTextField textGgPostScadenza = (JTextField) builder.addPropertyAndLabel("ggPostScadenza", 5)[1];
		textGgPostScadenza.setColumns(4);
		builder.nextRow();

		builder.addLabel("categoriaRata");
		builder.addBinding(bf.createBoundSearchText("categoriaRata", new String[] { "descrizione" }), 3);
		builder.addPropertyAndLabel("stampaRV", 5);
		builder.nextRow();
		builder.addPropertyAndLabel("tipoPagamento");
		builder.nextRow();
		builder.addPropertyAndLabel("tipoStrategiaDataScadenza");
		builder.nextRow();

		return builder.getPanel();
	}

}
