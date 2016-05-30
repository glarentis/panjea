/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.sedimagazzino;

import it.eurotn.panjea.magazzino.domain.DichiarazioneIntento;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaGenerazioneDocumentoFatturazione;
import it.eurotn.panjea.magazzino.rich.search.SedeMagazzinoLiteSearchObject;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Gestisce tutti i dati che riguardano l'area di magazzino della sede.
 * 
 * @author fattazzo
 * 
 */
public class SedeMagazzinoForm extends PanjeaAbstractForm {

	private class DichiarazioneIntentoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			DichiarazioneIntento dichiarazioneIntento = (DichiarazioneIntento) getFormModel().getValueModel(
					"dichiarazioneIntento").getValue();

			if (dichiarazioneIntento == null || dichiarazioneIntento.getTesto() == null
					|| dichiarazioneIntento.getTesto().isEmpty()) {
				testoDichiarazioneIntentoButton.setToolTipText(null);
			} else {
				testoDichiarazioneIntentoButton.setToolTipText(dichiarazioneIntento.getTesto());
			}
		}

	}

	private class ReadOnlyPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = (ETipologiaCodiceIvaAlternativo) getFormModel()
					.getValueModel("tipologiaCodiceIvaAlternativo").getValue();

			// NPE mail per tipologiacodiceivaalternativo
			if ((!(Boolean) evt.getNewValue()) && tipologiaCodiceIvaAlternativo != null) {

				boolean readOnlyControlIva = tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.NESSUNO;
				boolean readOnlyControlDicIntento = tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.NESSUNO
						|| tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.ESENZIONE;

				getFormModel().getFieldMetadata("codiceIvaAlternativo").setReadOnly(readOnlyControlIva);
				getFormModel().getFieldMetadata("dichiarazioneIntento.dataScadenza").setReadOnly(
						readOnlyControlDicIntento);
				getFormModel().getFieldMetadata("dichiarazioneIntento.addebito").setReadOnly(readOnlyControlDicIntento);
			}

			if (testoDichiarazioneIntentoCommand != null && tipologiaCodiceIvaAlternativo != null) {
				testoDichiarazioneIntentoCommand
						.setEnabled(tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO);
			}
		}

	}

	private final class TestoDichiarazioneIntentoCommand extends ApplicationWindowAwareCommand {

		private class TestoDichiarazioneInputDialog extends InputApplicationDialog {

			private boolean confirmed = true;

			/**
			 * @return confirmed state of the input dialog
			 */
			public boolean isConfirmed() {
				return confirmed;
			}

			@Override
			protected void onCancel() {
				confirmed = false;
				super.onCancel();
			}

			@Override
			protected boolean onFinish() {
				String testo = (String) getInputValue();
				if (testo != null && testo.length() > 100) {
					new MessageDialog("ATTENZIONE", "Il testo non può superare i 100 caratteri. ( caratteri presenti "
							+ testo.length() + " )").showDialog();
					return false;
				}
				return super.onFinish();
			}

		}

		public static final String COMMAND_ID = "testoDichiarazioneIntendoCommand";

		/**
		 * Costruttore.
		 */
		private TestoDichiarazioneIntentoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (getFormModel().isReadOnly()) {
				return;
			}

			TestoDichiarazioneInputDialog inputDialog = new TestoDichiarazioneInputDialog();
			inputDialog.setTitle("Descrizione dichiarazione di intento");
			inputDialog.setInputField(new JTextField((String) getFormModel()
					.getValueModel("dichiarazioneIntento.testo").getValue()));
			inputDialog.setInputLabelMessage("Testo");
			inputDialog.setPreferredSize(new Dimension(400, 100));
			inputDialog.showDialog();

			if (inputDialog.isConfirmed()) {
				getFormModel().getValueModel("dichiarazioneIntento.testo").setValue(inputDialog.getInputValue());
			}
		}
	}

	private class TipologiaCodiceIvaChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = ((ETipologiaCodiceIvaAlternativo) evt
					.getNewValue());
			// NPE mail per tipologiacodiceivaalternativo
			if (tipologiaCodiceIvaAlternativo != null) {

				boolean readOnlyControlIva = tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.NESSUNO;
				boolean readOnlyControlDicIntento = tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.NESSUNO
						|| tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.ESENZIONE;

				getFormModel().getFieldMetadata("codiceIvaAlternativo").setReadOnly(readOnlyControlIva);
				getFormModel().getFieldMetadata("dichiarazioneIntento.dataScadenza").setReadOnly(
						readOnlyControlDicIntento);
				getFormModel().getFieldMetadata("dichiarazioneIntento.addebito").setReadOnly(readOnlyControlDicIntento);

				switch (tipologiaCodiceIvaAlternativo) {
				case NESSUNO:
					getFormModel().getValueModel("codiceIvaAlternativo").setValue(null);
					getFormModel().getValueModel("dichiarazioneIntento.testo").setValue("");
					getFormModel().getValueModel("dichiarazioneIntento.dataScadenza").setValue(null);
					break;
				case AGEVOLATA:
					getFormModel().getValueModel("dichiarazioneIntento.testo").setValue("");
					getFormModel().getValueModel("dichiarazioneIntento.dataScadenza").setValue(null);
					break;
				case ESENZIONE:
					getFormModel().getValueModel("dichiarazioneIntento.testo").setValue("");
					getFormModel().getValueModel("dichiarazioneIntento.dataScadenza").setValue(null);
					break;
				default:
					break;
				}
				testoDichiarazioneIntentoCommand
						.setEnabled(tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO);
			}
		}
	}

	private static Logger logger = Logger.getLogger(SedeMagazzinoForm.class);

	private static final String FORM_ID = "sedeMagazzinoForm";

	private PluginManager pluginManager = null;

	private TestoDichiarazioneIntentoCommand testoDichiarazioneIntentoCommand;
	private AbstractButton testoDichiarazioneIntentoButton;

	private DichiarazioneIntentoPropertyChange dichiarazioneIntentoPropertyChange = null;
	private ReadOnlyPropertyChange readOnlyPropertyChange = null;
	private TipologiaCodiceIvaChangeListener tipologiaCodiceIvaChangeListener = null;

	/**
	 * Costruttore di default.
	 */
	public SedeMagazzinoForm() {
		super(PanjeaFormModelHelper.createFormModel(new SedeMagazzino(), false, FORM_ID), FORM_ID);

		ValueModel sediRifatturazioneSeachValueModel = new ValueHolder(BigDecimal.ZERO);
		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				sediRifatturazioneSeachValueModel), Boolean.class, true, null);
		getFormModel().add("sediRifatturazioneSeach", sediRifatturazioneSeachValueModel, metaData);

		testoDichiarazioneIntentoCommand = new TestoDichiarazioneIntentoCommand();

		dichiarazioneIntentoPropertyChange = new DichiarazioneIntentoPropertyChange();

		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:90dlu,4dlu,left:default, right:53dlu, right:pref,4dlu,left:default, 10dlu, fill:default:grow",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addLabel("listino", 1);
		SearchPanel searchListino = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("listino", new String[] { "codice", "descrizione" }), 3, 2, 4, 1);
		searchListino.getTextFields().get("codice").setColumns(6);
		searchListino.getTextFields().get("descrizione").setColumns(27);
		builder.nextRow();

		builder.addLabel("listinoAlternativo", 1);
		SearchPanel searchListinoAlt = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("listinoAlternativo", new String[] { "codice", "descrizione" }), 3, 4, 4, 1);
		searchListinoAlt.getTextFields().get("codice").setColumns(6);
		searchListinoAlt.getTextFields().get("descrizione").setColumns(27);
		builder.nextRow();

		builder.addLabel("causaleTrasporto", 1);
		SearchPanel searchCausaleTrasp = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("causaleTrasporto", new String[] { "descrizione" }), 3);
		searchCausaleTrasp.getTextFields().get("descrizione").setColumns(15);

		builder.addLabel("trasportoCura", 5);
		SearchPanel searchTrasportoCura = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("trasportoCura", new String[] { "descrizione" }), 7);
		searchTrasportoCura.getTextFields().get("descrizione").setColumns(15);
		builder.nextRow();

		builder.addLabel("aspettoEsteriore", 1);
		SearchPanel searchAspettoEsteriore = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("aspettoEsteriore", new String[] { "descrizione" }), 3);
		searchAspettoEsteriore.getTextFields().get("descrizione").setColumns(15);

		builder.addLabel("tipoPorto", 5);
		SearchPanel searchTipoPorto = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("tipoPorto", new String[] { "descrizione" }), 7);
		searchTipoPorto.getTextFields().get("descrizione").setColumns(15);
		builder.nextRow();

		if (pluginManager.isPresente(PluginManager.PLUGIN_INTRA)) {
			builder.addPropertyAndLabel("modalitaTrasporto", 1);
			builder.nextRow();
		}

		builder.addLabel("categoriaContabileSedeMagazzino", 1);
		SearchPanel searchCatContSede = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("categoriaContabileSedeMagazzino", new String[] { "codice" }), 3);
		searchCatContSede.getTextFields().get("codice").setColumns(15);

		builder.addLabel("categoriaSedeMagazzino", 5);
		SearchPanel searchCatSedeMaga = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("categoriaSedeMagazzino", new String[] { "descrizione" }), 7);
		searchCatSedeMaga.getTextFields().get("descrizione").setColumns(15);
		builder.nextRow();

		builder.addLabel("sedePerRifatturazione", 1, 14);
		SearchPanel searchSedePerRifatt = (SearchPanel) builder.addBinding(bf.createBoundSearchText(
				"sedePerRifatturazione", new String[] { "sedeEntita.entita.anagrafica.denominazione",
						"sedeEntita.sede.descrizione" }, new String[] { "sediRifatturazioneSeach" },
				new String[] { SedeMagazzinoLiteSearchObject.PARAM_SEDI_RIFATTURAZIONE }), 3, 14, 4, 1);
		searchSedePerRifatt.getTextFields().get("sedeEntita.entita.anagrafica.denominazione").setColumns(6);
		searchSedePerRifatt.getTextFields().get("sedeEntita.sede.descrizione").setColumns(27);
		builder.nextRow();

		Binding bindingTipologiaGenerazioneDocumentoFatturazione = (bf.createEnumRadioButtonBinding(
				ETipologiaGenerazioneDocumentoFatturazione.class, "tipologiaGenerazioneDocumentoFatturazione",
				JList.HORIZONTAL_WRAP));
		builder.addLabel("tipologiaGenerazioneDocumentoFatturazione", 1);
		builder.addBinding(bindingTipologiaGenerazioneDocumentoFatturazione, 3);
		builder.nextRow();

		Binding bindingTipologiaCodiceIvaAlternativo = (bf.createEnumRadioButtonBinding(
				ETipologiaCodiceIvaAlternativo.class, "tipologiaCodiceIvaAlternativo", JList.HORIZONTAL_WRAP));
		builder.addLabel("tipologiaCodiceIvaAlternativo", 1);
		builder.addBinding(bindingTipologiaCodiceIvaAlternativo, 3, 18, 5, 1);

		builder.nextRow();

		builder.addLabel("codiceIvaAlternativo", 1);
		JPanel dicIntentoPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		dicIntentoPanel.setBorder(null);
		SearchPanel searchCodiceIva = (SearchPanel) bf.createBoundSearchText("codiceIvaAlternativo",
				new String[] { "codice" }).getControl();
		searchCodiceIva.getTextFields().get("codice").setColumns(6);
		dicIntentoPanel.add(searchCodiceIva);
		testoDichiarazioneIntentoButton = testoDichiarazioneIntentoCommand.createButton();
		dicIntentoPanel.add(testoDichiarazioneIntentoButton);
		dicIntentoPanel.add(getComponentFactory().createLabel("dichiarazioneIntento.dataScadenza"));
		dicIntentoPanel.add(bf.createBoundCalendar("dichiarazioneIntento.dataScadenza", "dd/MM/yy", "##/##/##")
				.getControl());
		dicIntentoPanel.add(getComponentFactory().createLabel("dichiarazioneIntento.addebito"));
		dicIntentoPanel.add(bf.createBoundCheckBox("dichiarazioneIntento.addebito").getControl());
		builder.addComponent(dicIntentoPanel, 2, 20, 6, 1);
		builder.nextRow();

		builder.addPropertyAndLabel("sedeDiRifatturazione", 1);
		builder.addPropertyAndLabel("raggruppamentoBolle", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("calcoloSpese", 1);
		builder.addPropertyAndLabel("stampaPrezzo", 5);
		builder.nextRow();

		installReadOnlyPropertyChange();

		getFormModel().getValueModel("dichiarazioneIntento").getValue();

		addFormObjectChangeListener(getDichiarazioneIntentoPropertyChange());
		getFormModel().getValueModel("dichiarazioneIntento.testo").addValueChangeListener(
				getDichiarazioneIntentoPropertyChange());

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

	@Override
	public void dispose() {
		getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, getReadOnlyPropertyChange());
		getFormModel().getValueModel("tipologiaCodiceIvaAlternativo").removeValueChangeListener(
				getTipologiaCodiceIvaChangeListener());
		removeFormObjectChangeListener(getDichiarazioneIntentoPropertyChange());
		getFormModel().getValueModel("dichiarazioneIntento.testo").removeValueChangeListener(
				getDichiarazioneIntentoPropertyChange());
		super.dispose();
	}

	/**
	 * @return the dichiarazioneIntentoPropertyChange
	 */
	public DichiarazioneIntentoPropertyChange getDichiarazioneIntentoPropertyChange() {
		if (dichiarazioneIntentoPropertyChange == null) {
			dichiarazioneIntentoPropertyChange = new DichiarazioneIntentoPropertyChange();
		}
		return dichiarazioneIntentoPropertyChange;
	}

	/**
	 * @return the readOnlyPropertyChange
	 */
	public ReadOnlyPropertyChange getReadOnlyPropertyChange() {
		if (readOnlyPropertyChange == null) {
			readOnlyPropertyChange = new ReadOnlyPropertyChange();
		}
		return readOnlyPropertyChange;
	}

	/**
	 * @return the tipologiaCodiceIvaChangeListener
	 */
	public TipologiaCodiceIvaChangeListener getTipologiaCodiceIvaChangeListener() {
		if (tipologiaCodiceIvaChangeListener == null) {
			tipologiaCodiceIvaChangeListener = new TipologiaCodiceIvaChangeListener();
		}
		return tipologiaCodiceIvaChangeListener;
	}

	/**
	 * installa i property change sul form.
	 */
	private void installReadOnlyPropertyChange() {
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, getReadOnlyPropertyChange());
		getFormModel().getValueModel("tipologiaCodiceIvaAlternativo").addValueChangeListener(
				getTipologiaCodiceIvaChangeListener());
	}

	/**
	 * @param pluginManager
	 *            the pluginManager to set
	 */
	public void setPluginManager(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}
}
