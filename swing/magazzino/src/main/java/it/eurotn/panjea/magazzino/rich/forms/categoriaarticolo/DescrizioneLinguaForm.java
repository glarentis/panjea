/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo;

import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.image.IconSource;

/**
 * 
 * @author adriano
 * @version 1.0, 13/nov/2008
 * 
 */
public class DescrizioneLinguaForm extends PanjeaAbstractForm {
	private static Logger logger = Logger.getLogger(DescrizioneLinguaForm.class);
	public static final String FORM_ID = "DescrizioneLinguaForm";
	public static final String FORMMODEL_ID = "DescrizioneLinguaFormModel";

	/**
	 * Costruttore.
	 * 
	 * @param parentFormModel
	 *            form model della categoria
	 * @param descrizioneLingua
	 *            descrizioneLingua
	 */
	public DescrizioneLinguaForm(final FormModel parentFormModel, final IDescrizioneLingua descrizioneLingua) {
		super(PanjeaFormModelHelper.createFormModel(descrizioneLingua, false,
				FORM_ID + "Model" + descrizioneLingua.getCodiceLingua()), FORM_ID + descrizioneLingua.getCodiceLingua());
	}

	/**
	 * Restituisce la stringa passata come parametro con la prima lettera maiuscola e le rimanenti minuscole.
	 * 
	 * @param word
	 *            stringa da formattare
	 * @return stringa formattata
	 */
	private String capitalizeWord(String word) {

		String firstLetter = word.substring(0, 1); // Get first letter
		String remainder = word.substring(1); // Get remainder of word.
		String capitalized = firstLetter.toUpperCase() + remainder.toLowerCase();

		return capitalized;
	}

	@Override
	protected JComponent createFormControl() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) getService(BindingFactoryProvider.class))
				.getBindingFactory(this.getFormModel());
		logger.debug("--> Enter createFormControl");
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		IDescrizioneLingua descrizioneLingua = (IDescrizioneLingua) getFormModel().getFormObject();
		Locale locale = new Locale(descrizioneLingua.getCodiceLingua());
		JLabel label = new JLabel(capitalizeWord(locale.getDisplayLanguage()), SwingConstants.TRAILING);
		label.setName(descrizioneLingua.getCodiceLingua());
		IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
		label.setIcon(iconSource.getIcon(descrizioneLingua.getCodiceLingua()));
		label.setHorizontalTextPosition(SwingConstants.LEFT);
		builder.getLayoutBuilder().cell(label, "align=right colSpec=90dlu");
		builder.getLayoutBuilder().cell("colSpec=4dlu");
		Binding binding = bf.createBinding("descrizione");
		builder.getLayoutBuilder().cell(binding.getControl(), "valign=top");
		logger.debug("--> Exit createFormControl");

		JComponent component = builder.getForm();
		component.setBorder(null);

		getValueModel("descrizione").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				logger.debug("Cambio descrizione per lingua "
						+ ((IDescrizioneLingua) getFormObject()).getCodiceLingua() + " form id: " + getId());
			}
		});

		return component;
	}
}
