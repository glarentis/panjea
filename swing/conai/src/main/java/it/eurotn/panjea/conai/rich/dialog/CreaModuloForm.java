package it.eurotn.panjea.conai.rich.dialog;

import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione.PERIODICITA;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.FileChooser.FileChooserMode;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class CreaModuloForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "creaModuloForm";

	private Integer[] mesi = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	private JComponent mesiControl = null;
	private JLabel periodo = null;
	private ArrayList<String> trimestre = null;
	private JComponent trimestreControl = null;
	private SettingsManager settingsManager = null;

	/**
	 * Costruttore.
	 */
	public CreaModuloForm() {
		super(PanjeaFormModelHelper.createFormModel(new ConaiParametriCreazione(), false, FORM_ID), FORM_ID);

		trimestre = new ArrayList<String>();

		trimestre.add("1");
		trimestre.add("2");
		trimestre.add("3");
		trimestre.add("4");

		// mesi.add(getMessageSource().getMessage(GENNAIO, new Object[] {},
		// Locale.getDefault()));

		settingsManager = RcpSupport.getBean("settingManagerLocal");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref, 10dlu, right:pref,4dlu,left:pref, default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, t");
		builder.setRow(1);
		builder.nextRow();
		builder.addPropertyAndLabel("materiale");
		builder.nextRow();
		builder.setComponentAttributes("f, t");
		builder.addLabel("pathCreazione");
		builder.addBinding(bf.createBoundFileChooseBinding("pathCreazione", false, FileChooserMode.FOLDER), 3);
		builder.nextRow();
		builder.setComponentAttributes("l, t");
		builder.addPropertyAndLabel("data");
		builder.nextRow();

		builder.addPropertyAndLabel("indirizzoFatturazione");
		builder.nextRow();

		builder.addHorizontalSeparator("Referente", 2, 6);
		builder.nextRow();

		builder.addPropertyAndLabel("referente");
		builder.nextRow();
		builder.addPropertyAndLabel("telReferente");
		builder.nextRow();
		builder.addPropertyAndLabel("faxReferente");
		builder.nextRow();
		builder.addPropertyAndLabel("emailReferente");
		builder.nextRow();

		builder.addPropertyAndLabel("anno", 5, 3);
		builder.nextRow();
		builder.addLabel("periodicita", 5);
		builder.addBinding(bf.createEnumRadioButtonBinding(PERIODICITA.class, "periodicita"), 7);
		builder.nextRow();
		periodo = builder.addLabel("periodo", 5, 7);
		Binding mesiBinding = bf.createBoundList("mese", new ValueHolder(mesi), null, null);
		mesiControl = builder.addBinding(mesiBinding, 7, 7, 1, 4);
		((JList) mesiControl).setCellRenderer(new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				Integer meseInt = ((Integer) value) + 1;
				String meseString = "mese." + meseInt.toString();
				setText(getMessageSource().getMessage(meseString, new Object[] {}, Locale.getDefault()));
				return this;
			}
		});

		trimestreControl = builder.addBinding(bf.createBoundList("trimestre", new ValueHolder(trimestre), null, null),
				7, 7, 1, 4);

		getFormModel().getValueModel("periodicita").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				periodo.setVisible(PERIODICITA.ANNUALE != getValueModel("periodicita").getValue());
				mesiControl.setVisible(getValueModel("periodicita").getValue() == PERIODICITA.MENSILE);
				trimestreControl.setVisible(getValueModel("periodicita").getValue() == PERIODICITA.TRIMESTRALE);
			}
		});
		mesiControl.setVisible(false);
		periodo.setVisible(false);
		trimestreControl.setVisible(false);
		return builder.getPanel();
	}

	/**
	 * Carica se presenti i settings per le proprieta' di
	 * ConaiParametriCreazione.
	 */
	public void loadConaiParametriCreazione() {
		String path = "";
		String referente = "";
		String telReferente = "";
		String faxReferente = "";
		String emailReferente = "";
		String indirizzoFatturazione = "";

		try {
			settingsManager.getUserSettings().load();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			indirizzoFatturazione = settingsManager.getUserSettings().getString(FORM_ID + ".indirizzoFatturazione");
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			path = settingsManager.getUserSettings().getString(FORM_ID + ".pathCreazione");
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			referente = settingsManager.getUserSettings().getString(FORM_ID + ".referente");
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			telReferente = settingsManager.getUserSettings().getString(FORM_ID + ".telReferente");
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			faxReferente = settingsManager.getUserSettings().getString(FORM_ID + ".faxReferente");
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			emailReferente = settingsManager.getUserSettings().getString(FORM_ID + ".emailReferente");
		} catch (SettingsException e) {
			// non faccio nulla
		}

		getFormModel().getValueModel("indirizzoFatturazione").setValue(indirizzoFatturazione);
		getFormModel().getValueModel("pathCreazione").setValue(path);
		getFormModel().getValueModel("referente").setValue(referente);
		getFormModel().getValueModel("telReferente").setValue(telReferente);
		getFormModel().getValueModel("faxReferente").setValue(faxReferente);
		getFormModel().getValueModel("emailReferente").setValue(emailReferente);
	}

	/**
	 * Salva nei settings le proprieta' di ConaiParametriCreazione.
	 */
	public void saveConaiParametriCreazione() {
		String indirizzoFatturazione = (String) getFormModel().getValueModel("indirizzoFatturazione").getValue();
		String path = (String) getFormModel().getValueModel("pathCreazione").getValue();
		String referente = (String) getFormModel().getValueModel("referente").getValue();
		String telReferente = (String) getFormModel().getValueModel("telReferente").getValue();
		String faxReferente = (String) getFormModel().getValueModel("faxReferente").getValue();
		String emailReferente = (String) getFormModel().getValueModel("emailReferente").getValue();

		try {
			settingsManager.getUserSettings().setString(FORM_ID + ".indirizzoFatturazione", indirizzoFatturazione);
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			settingsManager.getUserSettings().setString(FORM_ID + ".pathCreazione", path);
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			settingsManager.getUserSettings().setString(FORM_ID + ".referente", referente);
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			settingsManager.getUserSettings().setString(FORM_ID + ".telReferente", telReferente);
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			settingsManager.getUserSettings().setString(FORM_ID + ".faxReferente", faxReferente);
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			settingsManager.getUserSettings().setString(FORM_ID + ".emailReferente", emailReferente);
		} catch (SettingsException e) {
			// non faccio nulla
		}
		try {
			settingsManager.getUserSettings().save();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
