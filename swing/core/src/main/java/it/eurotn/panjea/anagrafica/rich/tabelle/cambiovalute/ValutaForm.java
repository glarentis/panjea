package it.eurotn.panjea.anagrafica.rich.tabelle.cambiovalute;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.text.DefaultFormatterFactory;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ValutaForm extends PanjeaAbstractForm {

	/**
	 * Costruttore.
	 */
	public ValutaForm() {
		super(PanjeaFormModelHelper.createFormModel(new CambioValuta(), false, "valutaFormModel"), "valutaForm");
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:60dlu,10dlu,right:pref,4dlu,fill:40dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r,c");

		// builder.nextRow();
		// builder.setRow(2);

		Set<String> currencyCode = new TreeSet<String>();
		for (Locale locale : Locale.getAvailableLocales()) {
			if (!locale.getCountry().isEmpty()) {
				currencyCode.add(Currency.getInstance(locale).getCurrencyCode());
			}
		}
		AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
		currencyCode.remove(aziendaCorrente.getCodiceValuta());
		builder.addLabel("valuta.codiceValuta");
		builder.addBinding(bf.createBoundComboBox("valuta.codiceValuta", currencyCode), 3);
		builder.addPropertyAndLabel("valuta.simbolo", 5);
		builder.nextRow();
		builder.addPropertyAndLabel("valuta.numeroDecimali");
		builder.nextRow();
		builder.addPropertyAndLabel("data");
		builder.addLabel("tasso", 5);
		builder.addBinding(bf.createBoundFormattedTextField("tasso", getFactory(5)), 7);
		return builder.getPanel();
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
