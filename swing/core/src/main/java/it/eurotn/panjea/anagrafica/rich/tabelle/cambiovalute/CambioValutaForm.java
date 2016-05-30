package it.eurotn.panjea.anagrafica.rich.tabelle.cambiovalute;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;

import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class CambioValutaForm extends PanjeaAbstractForm {
	public static final String FORM_ID = "cambioValutaForm";

	private JFormattedTextField prezzoControl;

	/**
	 * Costruttore.
	 */
	public CambioValutaForm() {
		super(PanjeaFormModelHelper.createFormModel(new CambioValuta(), false, FORM_ID + "Model"), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.add("data", "align=left");
		builder.row();
		prezzoControl = (JFormattedTextField) builder.add(bf.createBoundFormattedTextField("tasso", getFactory(5)),
				"align=left")[1];
		prezzoControl.setColumns(6);
		return builder.getForm();
	}

	/**
	 * @param numeroDecimali
	 *            numeroDecimali
	 * @return DefaultFormatterFactory
	 */
	private DefaultFormatterFactory getFactory(Integer numeroDecimali) {
		DefaultFormatterFactory factory = new DefaultNumberFormatterFactory("###,###,###,##0", numeroDecimali,
				BigDecimal.class);
		return factory;
	}

}
