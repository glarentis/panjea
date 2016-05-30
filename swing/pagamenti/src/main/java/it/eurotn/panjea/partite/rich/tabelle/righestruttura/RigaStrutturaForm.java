package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.PercentageNumberBinder;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class RigaStrutturaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "rigaStrutturaForm";

	/**
	 * Costruttore di default.
	 * 
	 * @param rigaStrutturaPartite
	 *            la riga struttura partita da editare nel form
	 */
	public RigaStrutturaForm(final RigaStrutturaPartite rigaStrutturaPartite) {
		super(PanjeaFormModelHelper.createFormModel(rigaStrutturaPartite, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("numeroRata", "align=left")[1]).setColumns(3);
		((JTextField) builder.add("intervallo", "align=left")[1]).setColumns(3);
		builder.row();

		PercentageNumberBinder binder = new PercentageNumberBinder();
		binder.setNrOfDecimals(4);
		binder.setFormat("###,###,###,##0.00");
		((JTextField) builder.add(binder.bind(this.getFormModel(), "primaPercentuale", new HashMap<Object, Object>()),
				"align=left")[1]).setColumns(8);
		((JTextField) builder.add(
				binder.bind(this.getFormModel(), "secondaPercentuale", new HashMap<Object, Object>()), "align=left")[1])
				.setColumns(8);
		builder.row();

		return builder.getForm();
	}
}
