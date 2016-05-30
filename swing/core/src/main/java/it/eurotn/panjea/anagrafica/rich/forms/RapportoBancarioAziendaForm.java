/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.forms;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.domain.RapportoBancario;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * form per gestire un rapporto bancario.
 * 
 * @author giangi
 * @version 1.0, 17/nov/06
 */
public class RapportoBancarioAziendaForm extends PanjeaAbstractForm {

	/**
	 * Inner class per la gestione della variazione dell'attributo banca.
	 */
	private class BancaChangeListeners implements PropertyChangeListener {

		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			// verifica congruenza tra la nuova banca e la filiale all'interno
			// del form model
			Banca bancaNew = (Banca) evt.getNewValue();
			Filiale filiale = (Filiale) getFormModel().getValueModel("filiale").getValue();
			if (bancaNew == null || filiale == null || bancaNew.equals(filiale.getBanca())) {
				// la banca corrisponde
				return;
			}
			// azzera l'attributo filiale
			getFormModel().getValueModel("filiale").setValue(null);
		}

	}

	private class EnableChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {

			labelIBAN.setEnabled(true);
			labelITBBAN.setEnabled(true);
		}
	}

	private final PluginManager pluginManager;

	private static final String FORM_ID = "rapportoBancarioAziendaForm";

	private JComponent labelIBAN;
	private JComponent labelITBBAN;

	/**
	 * Default constructor.
	 */
	public RapportoBancarioAziendaForm() {
		super(PanjeaFormModelHelper.createFormModel(new RapportoBancarioAzienda(), false, FORM_ID + "Model"), FORM_ID);
		// Aggiungo il value model che mi servirà solamente nella search text
		// delle entità
		// per cercare solo le entità abilitate
		ValueModel sottocontoAbilitatoInRicercaValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata sottocontoAbilitatoMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(sottocontoAbilitatoInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("sottocontoAbilitatoInRicerca", sottocontoAbilitatoInRicercaValueModel,
				sottocontoAbilitatoMetaData);

		this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:100dlu, 10dlu, left:default,4dlu,left:default, 10dlu, right:pref,4dlu,left:default,left:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel(RapportoBancario.PROP_DESCRIZIONE, 1, 2);
		builder.addPropertyAndLabel(RapportoBancario.PROP_ABILITATO, 5, 2);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("codicePaese", 1)[1]).setColumns(2);
		((JTextField) builder.addPropertyAndLabel("checkDigit", 5)[1]).setColumns(2);
		((JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_CIN, 9)[1]).setColumns(2);
		builder.nextRow();

		Binding bindingBanca = bf.createBoundSearchText("banca", new String[] { Banca.PROP_CODICE,
				Banca.PROP_DESCRIZIONE });
		builder.addLabel("banca", 1);
		SearchPanel searchPanelBanca = (SearchPanel) builder.addBinding(bindingBanca, 3, 6, 5, 1);
		searchPanelBanca.getTextFields().get(Banca.PROP_CODICE).setColumns(5);
		searchPanelBanca.getTextFields().get(Banca.PROP_DESCRIZIONE).setColumns(18);
		builder.nextRow();

		Binding bindingFiliale = bf.createBoundSearchText("filiale", new String[] { Filiale.PROP_CODICE,
				Filiale.PROP_INDIRIZZO }, new String[] { "banca" }, new String[] { Banca.REF });
		builder.addLabel("filiale", 1);
		SearchPanel searchPanelFiliale = (SearchPanel) builder.addBinding(bindingFiliale, 3, 8, 5, 1);
		searchPanelFiliale.getTextFields().get(Filiale.PROP_CODICE).setColumns(5);
		searchPanelFiliale.getTextFields().get(Filiale.PROP_INDIRIZZO).setColumns(18);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_NUMERO, 1)[1]).setColumns(12);
		builder.nextRow();

		builder.addLabel("iban", 1);
		labelIBAN = builder.addBinding(bf.createBoundLabel("iban"), 3, 12, 2, 1);
		builder.addLabel("itbban", 5);
		labelITBBAN = builder.addBinding(bf.createBoundLabel("itbban"), 7, 12, 5, 1);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_NOTE, 1, 14)[1]).setColumns(40);
		((JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_BIC, 5, 14)[1]).setColumns(11);
		builder.nextRow();

		// TODO personalizzare la search object per caricare eslusivamente il
		// rapporto bancario di regolamentazione
		Binding bindingRapportoBancario = bf.createBoundSearchText("rapportoBancarioRegolamentazione", new String[] {
				"numero", "descrizione" });
		builder.addLabel("rapportoBancarioRegolamentazione", 1, 16);
		SearchPanel searchPanelRapportoBancario = (SearchPanel) builder
				.addBinding(bindingRapportoBancario, 3, 16, 5, 1);
		searchPanelRapportoBancario.getTextFields().get("numero").setColumns(5);
		searchPanelRapportoBancario.getTextFields().get("descrizione").setColumns(18);

		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("percAnticippoFatture", 1, 18)[1]).setColumns(5);
		((JTextField) builder.addPropertyAndLabel("percIvaAnticipoFatture", 5, 18)[1]).setColumns(5);
		builder.nextRow();

		builder.addPropertyAndLabel(RapportoBancario.PROP_DATA_APERTURA, 1, 20);
		builder.addPropertyAndLabel(RapportoBancario.PROP_DATA_CHIUSURA, 5, 20);
		((JTextField) builder.addPropertyAndLabel("giorniBanca", 9, 20)[1]).setColumns(3);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("spesePresentazione", 1, 22)[1]).setColumns(6);
		((JTextField) builder.addPropertyAndLabel("speseInsoluto", 5, 22)[1]).setColumns(6);
		((JTextField) builder.addPropertyAndLabel("speseDistinta", 9, 22)[1]).setColumns(6);
		builder.nextRow();

		builder.addHorizontalSeparator("Associazione per creazione rate", 1, 12);

		builder.setLabelAttributes("r, t");
		builder.setComponentAttributes("f, t");

		Binding bindingStatoTipiPagamenti = bf.createBoundEnumCheckBoxList(RapportoBancario.PROP_DEFAULT_PAGAMENTI,
				TipoPagamento.class, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addLabel(RapportoBancario.PROP_DEFAULT_PAGAMENTI, 1, 26);
		builder.addBinding(bindingStatoTipiPagamenti, 3, 26, 10, 1);
		builder.nextRow();

		builder.addLabel("fornitori", 1, 28);
		FornitoreRapportoBancarioInserimentoForm inserimentoForm = new FornitoreRapportoBancarioInserimentoForm();
		DefaultBeanTableModel<SedeEntita> fornitoriTableModel = new FornitoreRapportoBancarioTableModel();
		Binding fornitoriBinding = bf.createTableBinding("sediEntita", 350, fornitoriTableModel, inserimentoForm);
		JComponent fornitoriControl = fornitoriBinding.getControl();
		fornitoriControl.setPreferredSize(new Dimension(370, 120));
		builder.addComponent(fornitoriControl, 3, 28, 10, 1);

		builder.nextRow();
		builder.setLabelAttributes("r, c");

		if (pluginManager.isPresente(PluginManager.PLUGIN_CONTABILITA)) {
			builder.addHorizontalSeparator("Conto rapporto bancario", 1, 12);

			builder.nextRow();
			builder.addLabel("sottoConto", 1, 32);
			Binding contoBinding = bf.createBoundSearchText("sottoConto", new String[] { "descrizione" },
					new String[] { "sottocontoAbilitatoInRicerca" }, new String[] { "filtroSottocontoAbilitato" },
					"it.eurotn.panjea.contabilita.rich.search.SottoContoSearchTextField");
			builder.addBinding(contoBinding, 3, 32);

			builder.addLabel("sottoContoEffettiAttivi", 5, 32);
			Binding contoEffettiAttiviBinding = bf.createBoundSearchText("sottoContoEffettiAttivi",
					new String[] { "descrizione" }, new String[] { "sottocontoAbilitatoInRicerca" },
					new String[] { "filtroSottocontoAbilitato" },
					"it.eurotn.panjea.contabilita.rich.search.SottoContoSearchTextField");
			builder.addBinding(contoEffettiAttiviBinding, 7, 32, 5, 1);
			builder.nextRow();

			builder.addLabel("sottoContoAnticipoFatture", 1, 34);
			Binding contoAnticipiBinding = bf.createBoundSearchText("sottoContoAnticipoFatture",
					new String[] { "descrizione" }, new String[] { "sottocontoAbilitatoInRicerca" },
					new String[] { "filtroSottocontoAbilitato" },
					"it.eurotn.panjea.contabilita.rich.search.SottoContoSearchTextField");
			builder.addBinding(contoAnticipiBinding, 3, 34);
		}

		labelIBAN.setEnabled(true);
		labelITBBAN.setEnabled(true);
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new EnableChangeListener());

		addFormValueChangeListener("banca", new BancaChangeListeners());
		return builder.getPanel();
	}
}
