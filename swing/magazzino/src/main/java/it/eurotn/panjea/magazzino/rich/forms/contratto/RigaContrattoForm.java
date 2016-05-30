/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.contratto;

import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.RigaContratto.TipoRiga;
import it.eurotn.panjea.magazzino.domain.RigaContrattoStrategiaPrezzo.TipoValore;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form che gestisce l'inserimento/modifica di una <code>RigaContratto</code>. In base al tipo riga gestito verranno
 * abilitati o disabilitati i campi necessari per l'inserimento/modifica.
 * 
 * @author fattazzo
 */
public class RigaContrattoForm extends PanjeaAbstractForm {

	private class NumeroDecimaliPrezzoChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFormModel().isReadOnly()) {
				logger.debug("--> Exit propertyChange. FormModel in sola lettura");
				return;
			}
			if ((Integer) getValueModel("numeroDecimaliPrezzo").getValue() != null) {
				Integer numeroDecimali = (Integer) getValueModel("numeroDecimaliPrezzo").getValue();
				formattedFieldValorePrezzo.setFormatterFactory(getFactory(numeroDecimali));
				BigDecimal prezzo = (BigDecimal) getValueModel("strategiaPrezzo.valorePrezzo").getValue();
				if (prezzo != null) {
					prezzo = prezzo.setScale(numeroDecimali, RoundingMode.HALF_UP);
				}
				getValueModel("strategiaPrezzo.valorePrezzo").setValue(prezzo);
			}
		}
	}

	private class RigaContrattoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			RigaContratto rigaContratto = (RigaContratto) evt.getNewValue();
			// Se la riga contratto e' nuova il tipo riga e' impostato dal
			// pulsante nuovo, altrimenti lo determino
			if (rigaContratto.getId() != null) {
				if (rigaContratto.getTutteLeCategorie()) {
					tipoRiga = TipoRiga.TUTTI;
				} else if (rigaContratto.getArticolo() != null) {
					tipoRiga = TipoRiga.ARTICOLO;
				} else {
					tipoRiga = TipoRiga.CATEGORIA;
				}
			}
			if (tipoRiga == TipoRiga.ARTICOLO) {
				getFormModel().getFieldFace("articolo").configure(labelEntita);
			} else if (tipoRiga == TipoRiga.CATEGORIA) {
				getFormModel().getFieldFace("categoria").configure(labelEntita);
			} else {
				labelEntita.setText("");
			}
			articoloControl.setVisible(tipoRiga == TipoRiga.ARTICOLO);
			categoriaControl.setVisible(tipoRiga == TipoRiga.CATEGORIA);

			Integer numeroDecimali = 0;
			if ((Integer) getValueModel("numeroDecimaliPrezzo").getValue() != null) {
				BigDecimal prezzo = (BigDecimal) getValueModel("strategiaPrezzo.valorePrezzo").getValue();
				numeroDecimali = (Integer) getValueModel("numeroDecimaliPrezzo").getValue();
				formattedFieldValorePrezzo.setFormatterFactory(getFactory(numeroDecimali));
				formattedFieldValorePrezzo.setValue(prezzo);
			}
		}
	}

	private class StrategiaPrezzoAbilitataChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Boolean strategiaPrezzoAbilitata = (Boolean) evt.getNewValue();
			if (strategiaPrezzoAbilitata != null) {
				getFormModel().getFieldMetadata("strategiaPrezzo.azionePrezzo").setEnabled(strategiaPrezzoAbilitata);
				getFormModel().getFieldMetadata("strategiaPrezzo.bloccoPrezzo").setEnabled(strategiaPrezzoAbilitata);
				getFormModel().getFieldMetadata("strategiaPrezzo.ignoraBloccoPrezzoPrecedente").setEnabled(
						strategiaPrezzoAbilitata);
				getFormModel().getFieldMetadata("strategiaPrezzo.quantitaSogliaPrezzo").setEnabled(
						strategiaPrezzoAbilitata);
				getFormModel().getFieldMetadata("strategiaPrezzo.tipoValorePrezzo")
						.setEnabled(strategiaPrezzoAbilitata);
				getFormModel().getFieldMetadata("numeroDecimaliPrezzo").setEnabled(strategiaPrezzoAbilitata);
				getFormModel().getFieldMetadata("strategiaPrezzo.valorePrezzo").setEnabled(strategiaPrezzoAbilitata);
			}
		}
	}

	private class StrategiaScontoAbilitataChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Boolean strategiaScontoAbilitata = (Boolean) evt.getNewValue();
			if (strategiaScontoAbilitata != null) {
				getFormModel().getFieldMetadata("strategiaSconto.azioneSconto").setEnabled(strategiaScontoAbilitata);
				getFormModel().getFieldMetadata("strategiaSconto.bloccoSconto").setEnabled(strategiaScontoAbilitata);
				getFormModel().getFieldMetadata("strategiaSconto.ignoraBloccoScontoPrecedente").setEnabled(
						strategiaScontoAbilitata);
				getFormModel().getFieldMetadata("strategiaSconto.quantitaSogliaSconto").setEnabled(
						strategiaScontoAbilitata);
				getFormModel().getFieldMetadata("strategiaSconto.sconto").setEnabled(strategiaScontoAbilitata);
			}
		}
	}

	private class TipoValoreChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFormModel().isReadOnly()) {
				logger.debug("--> Exit propertyChange. FormModel in sola lettura");
				return;
			}
			getValueModel("strategiaPrezzo.valorePrezzo").setValue(BigDecimal.ZERO);

			Integer numeroDecimali = (Integer) getValueModel("numeroDecimaliPrezzo").getValue();
			if (numeroDecimali != null) {
				formattedFieldValorePrezzo.setFormatterFactory(getFactory(numeroDecimali));
			}
		}
	}

	public static final String FORM_ID = "rigaContrattoForm";
	public static final String FORMMODEL_ID = "rigaContrattoFormModel";
	private static Logger logger = Logger.getLogger(RigaContrattoForm.class);
	private SearchPanel panelTextArticolo = null;
	private JComponent articoloControl = null;
	private JComponent categoriaControl = null;
	private JComponent[] numeroDecimaliComponent = null;
	private TipoRiga tipoRiga = null;
	private SearchPanel panelCategoria = null;
	private JLabel labelEntita = null;
	private JFormattedTextField formattedFieldValorePrezzo;

	/**
	 * Costruttore di default.
	 */
	public RigaContrattoForm() {
		super(PanjeaFormModelHelper.createFormModel(new RigaContratto(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,5dlu,fill:100dlu, 10dlu, right:pref,5dlu,default, default:grow",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		// ########### PRIMA RIGA: Articolo o categoria o nessuno se tutte le
		// categorie.
		Binding bindingArticolo = bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" });
		Binding bindingCategoria = bf.createBoundSearchText("categoriaCommercialeArticolo", new String[] { "codice" });

		articoloControl = bindingArticolo.getControl();
		panelTextArticolo = ((SearchPanel) articoloControl);
		panelTextArticolo.getTextFields().get("codice").setColumns(9);
		panelTextArticolo.getTextFields().get("descrizione").setColumns(20);
		categoriaControl = bindingCategoria.getControl();
		panelCategoria = ((SearchPanel) categoriaControl);
		panelCategoria.getTextFields().get("codice").setColumns(9);

		labelEntita = getComponentFactory().createLabel("");
		articoloControl.setVisible(false);
		categoriaControl.setVisible(false);

		builder.addComponent(labelEntita);
		builder.addComponent(articoloControl, 3, 2, 3, 1);
		builder.addComponent(categoriaControl, 3, 2, 3, 1);

		// ########## SEZIONE STRATEGIA PREZZO
		builder.nextRow();

		Binding bindingStrategiaPrezzoAbilitata = bf.createBinding("strategiaPrezzoAbilitata");
		builder.addBinding(bindingStrategiaPrezzoAbilitata, 1);
		builder.addHorizontalSeparator(getMessage("separator.strategiaPrezzo"), 3, 6);
		builder.nextRow();

		builder.addPropertyAndLabel("strategiaPrezzo.azionePrezzo");
		builder.nextRow();

		builder.addPropertyAndLabel("strategiaPrezzo.bloccoPrezzo", 1);
		builder.addPropertyAndLabel("strategiaPrezzo.ignoraBloccoPrezzoPrecedente", 5);
		builder.nextRow();

		JTextField qtaSogliaText = (JTextField) builder.addPropertyAndLabel("strategiaPrezzo.quantitaSogliaPrezzo")[1];
		qtaSogliaText.setColumns(10);
		builder.nextRow();

		builder.addPropertyAndLabel("strategiaPrezzo.tipoValorePrezzo", 1);
		numeroDecimaliComponent = builder.addPropertyAndLabel("numeroDecimaliPrezzo", 5);
		((JTextField) numeroDecimaliComponent[1]).setColumns(5);
		builder.nextRow();

		Binding createBoundFormattedTextField = bf.createBoundFormattedTextField("strategiaPrezzo.valorePrezzo",
				getFactory((Integer) getValueModel("numeroDecimaliPrezzo").getValue()));
		builder.addLabel("strategiaPrezzo.valorePrezzo");
		formattedFieldValorePrezzo = (JFormattedTextField) builder.addBinding(createBoundFormattedTextField, 3);
		formattedFieldValorePrezzo.setColumns(10);
		formattedFieldValorePrezzo.setHorizontalAlignment(SwingConstants.RIGHT);

		// ########## SEZIONE STRATEGIA SCONTO
		builder.nextRow();
		Binding bindingStrategiaScontoAbilitata = bf.createBinding("strategiaScontoAbilitata");
		builder.addBinding(bindingStrategiaScontoAbilitata, 1);
		builder.addHorizontalSeparator(getMessage("separator.strategiaSconto"), 3, 6);
		builder.nextRow();

		builder.addPropertyAndLabel("strategiaSconto.azioneSconto");
		builder.nextRow();

		builder.addPropertyAndLabel("strategiaSconto.bloccoSconto", 1);
		builder.addPropertyAndLabel("strategiaSconto.ignoraBloccoScontoPrecedente", 5);
		builder.nextRow();

		JTextField qtaSogliaScontoText = (JTextField) builder
				.addPropertyAndLabel("strategiaSconto.quantitaSogliaSconto")[1];
		qtaSogliaScontoText.setColumns(10);
		builder.nextRow();

		builder.addLabel("strategiaSconto.sconto");
		builder.addBinding(bf.createBoundSearchText("strategiaSconto.sconto", new String[] { "codice" }), 3);

		logger.debug("--> Exit createFormControl");
		updateVisibility();
		installPropertyChange();

		return builder.getPanel();
	}

	/**
	 * @param numeroDecimali
	 *            numeroDecimali
	 * @return DefaultFormatterFactory
	 */
	private DefaultFormatterFactory getFactory(Integer numeroDecimali) {
		String pattern = null;

		if (getValueModel("strategiaPrezzo.tipoValorePrezzo").getValue() == TipoValore.IMPORTO) {
			pattern = "###,###,###,##0";
		} else {
			pattern = "##0";
			numeroDecimali = new Integer(2);
		}
		DefaultFormatterFactory factory = new DefaultNumberFormatterFactory(pattern, numeroDecimali, BigDecimal.class);
		return factory;
	}

	/**
	 * @return the tipoRiga
	 */
	public TipoRiga getTipoRiga() {
		return tipoRiga;
	}

	/**
	 * Non uso il <code>DefaultController</code> sulla pagina perche' devo modificare il controllo sul form<br/>
	 * . e i formModelPropertyChange possono modificare solamente le prop del formModel
	 */
	private void installPropertyChange() {
		addFormObjectChangeListener(new RigaContrattoChangeListener());
		getValueModel("numeroDecimaliPrezzo").addValueChangeListener(new NumeroDecimaliPrezzoChangeListener());
		getValueModel("strategiaPrezzo.tipoValorePrezzo").addValueChangeListener(new TipoValoreChangeListener());
		getValueModel("strategiaPrezzoAbilitata").addValueChangeListener(new StrategiaPrezzoAbilitataChangeListener());
		getValueModel("strategiaScontoAbilitata").addValueChangeListener(new StrategiaScontoAbilitataChangeListener());
	}

	/**
	 * @param tipoRiga
	 *            the tipoRiga to set
	 */
	public void setTipoRiga(TipoRiga tipoRiga) {
		this.tipoRiga = tipoRiga;
	}

	/**
	 * Aggiorna la proprietà visible dei componenti in base al valore della proprietà strategiaPrezzo.tipoValorePrezzo.
	 */
	private void updateVisibility() {
		for (JComponent component : numeroDecimaliComponent) {
			component.setVisible(getValueModel("strategiaPrezzo.tipoValorePrezzo").getValue() == TipoValore.IMPORTO);
		}
	}
}
