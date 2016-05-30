/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.forms;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.domain.RapportoBancario;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form per gestire un rapporto bancario.
 * 
 * @author giangi
 * @version 1.0, 17/nov/06
 * 
 */
public class RapportoBancarioSedeEntitaForm extends PanjeaAbstractForm {

	/**
	 * inner class per la gestione della variazione dell'attributo banca.
	 */
	private class BancaChangeListeners implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
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

	private static final String FORM_ID = "rapportoBancarioAziendaForm";
	private JComponent labelIBAN;
	private JComponent labelITBBAN;

	/**
	 * Costruttore.
	 */
	public RapportoBancarioSedeEntitaForm() {
		super(PanjeaFormModelHelper.createFormModel(new RapportoBancarioSedeEntita(), false, FORM_ID), FORM_ID);
	}

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formmodel
	 * @param formId
	 *            id del form
	 */
	public RapportoBancarioSedeEntitaForm(final FormModel formModel, final String formId) {
		super(formModel, formId);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		JTextField textFieldDesc = (JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_DESCRIZIONE, 1)[1];
		textFieldDesc.setColumns(30);
		builder.addPropertyAndLabel(RapportoBancario.PROP_ABILITATO, 5);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("codicePaese", 1)[1]).setColumns(2);
		((JTextField) builder.addPropertyAndLabel("checkDigit", 5)[1]).setColumns(2);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_CIN, 1)[1]).setColumns(2);
		builder.nextRow();

		builder.addLabel("banca", 1);
		SearchPanel searchPanelBanca = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("banca", new String[] { Banca.PROP_CODICE, Banca.PROP_DESCRIZIONE }), 3);
		searchPanelBanca.getTextFields().get(Banca.PROP_CODICE).setColumns(6);
		builder.nextRow();

		builder.addLabel("filiale", 1);
		SearchPanel searchPanelFiliale = (SearchPanel) builder.addBinding(bf.createBoundSearchText("filiale",
				new String[] { Filiale.PROP_CODICE, Filiale.PROP_INDIRIZZO }, new String[] { "banca" },
				new String[] { Banca.REF }), 3);
		searchPanelFiliale.getTextFields().get(Filiale.PROP_CODICE).setColumns(5);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_NUMERO, 1)[1]).setColumns(11);
		builder.nextRow();

		builder.addLabel("iban", 1);
		labelIBAN = builder.addBinding(bf.createBoundLabel("iban"), 3);
		builder.nextRow();

		builder.addLabel("itbban", 1);
		labelITBBAN = builder.addBinding(bf.createBoundLabel("itbban"), 3);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_NOTE, 1)[1]).setColumns(40);
		((JTextField) builder.addPropertyAndLabel(RapportoBancario.PROP_BIC, 5)[1]).setColumns(11);
		builder.nextRow();

		Binding bindingStatoTipiPagamenti = bf.createBoundEnumCheckBoxList(RapportoBancario.PROP_DEFAULT_PAGAMENTI,
				TipoPagamento.class, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addLabel(RapportoBancario.PROP_DEFAULT_PAGAMENTI, 1);
		builder.addBinding(bindingStatoTipiPagamenti, 3, 20, 10, 1);
		builder.nextRow();

		addFormValueChangeListener("banca", new BancaChangeListeners());

		labelIBAN.setEnabled(true);
		labelITBBAN.setEnabled(true);
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new EnableChangeListener());
		return builder.getPanel();
	}

}
