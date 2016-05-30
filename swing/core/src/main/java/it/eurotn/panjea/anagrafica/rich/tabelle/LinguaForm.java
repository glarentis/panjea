package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class LinguaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "linguaForm";

	/**
	 * Costruttore.
	 * 
	 * @param lingua
	 *            {@link Lingua}
	 */
	public LinguaForm(final Lingua lingua) {
		super(PanjeaFormModelHelper.createFormModel(lingua, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();

		Set<String> lingue = new TreeSet<String>();
		for (Locale locale : Locale.getAvailableLocales()) {
			lingue.add(locale.getLanguage() + " - " + locale.getDisplayLanguage());
		}
		ComboBoxBinding localeBinding = (ComboBoxBinding) bf.createBoundComboBox("codice", lingue);
		builder.add(localeBinding);
		builder.row();

		return builder.getForm();
	}

}
