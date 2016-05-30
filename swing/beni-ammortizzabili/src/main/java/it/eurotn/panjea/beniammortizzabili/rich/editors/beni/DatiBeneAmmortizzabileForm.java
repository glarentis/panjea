/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.BorderLayout;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;
import org.springframework.rules.constraint.Constraint;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Aracno
 * @version 1.0, 02/ott/06
 */
public class DatiBeneAmmortizzabileForm extends PanjeaAbstractForm {

	private class CambioCodiceCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public CambioCodiceCommand() {
			super("newCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			InputApplicationDialog inputDialog = new InputApplicationDialog("Input Codice", (Window) null);
			Integer codice = (Integer) getFormModel().getValueModel("codice").getValue();
			inputDialog.setInputField(new JTextField(codice == null ? "" : codice.toString()));
			inputDialog.setInputLabelMessage("Codice");
			inputDialog.setInputConstraint(new Constraint() {

				@Override
				public boolean test(Object paramObject) {
					String objStr = (String) paramObject;
					if (paramObject == null || StringUtils.isNumeric(objStr)) {
						return true;
					}

					new MessageDialog("ATTENZIONE", "Il codice inserito deve essere numerico").showDialog();
					return false;
				}
			});
			inputDialog.setFinishAction(new Closure() {

				@Override
				public Object call(Object paramObject) {
					Integer newCodice = null;

					if (paramObject != null && !"".equals(((String) paramObject).trim())) {
						newCodice = new Integer((String) paramObject);
					}
					getFormModel().getValueModel("codice").setValue(newCodice);
					return null;
				}
			});
			inputDialog.showDialog();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setText("");
		}
	}

	private JTextField codiceEntitaTextField;
	private CambioCodiceCommand cambioCodiceCommand;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formModel per il form
	 */
	public DatiBeneAmmortizzabileForm(final FormModel formModel) {
		super(formModel, "datiBeneAmmortizzabileForm");

		// Aggiungo il value model che mi servirà solamente nella search text
		// delle entità
		// per cercare i fornitori solamente
		ValueModel tipoEntitaValueModel = new ValueHolder(TipoEntita.FORNITORE);
		DefaultFieldMetadata tipoEntitaMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipoEntitaValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipoEntita", tipoEntitaValueModel, tipoEntitaMetaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:default,4dlu,100dlu, 10dlu, right:pref,4dlu,left:60dlu, 10dlu, right:pref,4dlu,100dlu, 10dlu, right:pref,4dlu,fill:default:grow",
				"3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,default,3dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		codiceEntitaTextField = (JTextField) builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_CODICE, 1)[1];
		codiceEntitaTextField.setColumns(15);
		cambioCodiceCommand = new CambioCodiceCommand();
		cambioCodiceCommand.setEnabled(false);
		builder.addComponent(cambioCodiceCommand.createButton(), 4);

		((JTextField) builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_CODICE_BENE_ESTERNO, 9)[1]).setColumns(15);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_DESCRIZIONE, 1, 4, 7);
		builder.nextRow();

		builder.addPropertyAndLabel("sottoSpecie", 1, 6, 7, 1);
		builder.nextRow();

		builder.addLabel("ubicazione", 1);
		builder.addBinding(bf.createBoundSearchText("ubicazione", new String[] { Ubicazione.PROP_DESCRIZIONE }), 3, 8,
				7, 1);
		builder.nextRow();

		builder.addLabel("benePadre", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("benePadre", new String[] { BeneAmmortizzabile.PROP_CODICE,
						BeneAmmortizzabile.PROP_DESCRIZIONE }), 3, 10, 7, 1);
		searchPanel.getTextFields().get(BeneAmmortizzabile.PROP_CODICE).setColumns(10);
		builder.nextRow();

		builder.addPropertyAndLabel("matricolaAziendale", 1);
		builder.addPropertyAndLabel("matricolaFornitore", 9);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_IND_AMMORTAMENTO, 1);
		builder.nextRow();

		builder.addPropertyAndLabel("eliminato", 1);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_ANNO_ACQUISTO, 1);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_NUMERO_REGISTRO, 1);
		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_NUMERO_PROTOCOLLO_ACQUISTO, 9);
		builder.nextRow();

		builder.addLabel("fornitore", 1);
		SearchPanel searchPanelFornitore = (SearchPanel) builder.addBinding(bf.createBoundSearchText("fornitore",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }), 3, 22, 7, 1);
		searchPanelFornitore.getTextFields().get("codice").setColumns(10);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_VALORE_ACQUISTO, 1);
		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_IMPORTO_FATTURA_ACQUISTO, 9);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO_SINGOLO, 1);
		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO, 9);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_DATA_INIZIO_AMMORTAMENTO, 1);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_ACQUISTATO_USATO, 1);
		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_BENE_MATERIALE, 9);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_FABBRICATO, 1);
		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_MANUTENZIONE, 9);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_STAMPA_SU_REGISTRI_BENI, 1);
		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_STAMPA_SU_REGISTRI_INVENTARI, 9);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_SOGGETTO_A_CONTRIBUTO, 1);
		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_BENE_DI_PROPRIETA, 9);
		builder.nextRow();

		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_BENE_IN_LEASING, 1);
		builder.addPropertyAndLabel(BeneAmmortizzabile.PROP_IND_TESTO_UNICO, 9);
		builder.nextRow();

		// add text area per attributo note
		Binding noteBinding = bf.createBoundTextArea(BeneAmmortizzabile.PROP_NOTE, 3, 3);

		JLabel labelNote = getComponentFactory().createLabel("");
		getFormModel().getFieldFace(BeneAmmortizzabile.PROP_NOTE).configure(labelNote);

		JTextArea textAreaNote = (JTextArea) noteBinding.getControl();

		JPanel notePanel = new JPanel(new BorderLayout());
		notePanel.add(getComponentFactory().createScrollPane(textAreaNote), BorderLayout.CENTER);
		// notePanel.setPreferredSize(new Dimension(200, 400));
		getComponentFactory().createTitledBorderFor(labelNote.getText(), notePanel);

		builder.addComponent(notePanel, 1, 40, 7, 1);

		// add text area per attributo note interne
		Binding noteInterneBinding = bf.createBoundTextArea(BeneAmmortizzabile.PROP_NOTE_INTERNE, 3, 3);

		JLabel labelNoteInterne = getComponentFactory().createLabel("");
		getFormModel().getFieldFace(BeneAmmortizzabile.PROP_NOTE_INTERNE).configure(labelNoteInterne);

		JTextArea textAreaNoteInterne = (JTextArea) noteInterneBinding.getControl();

		JPanel noteInternePanel = new JPanel(new BorderLayout());
		noteInternePanel.add(getComponentFactory().createScrollPane(textAreaNoteInterne), BorderLayout.CENTER);
		// noteInternePanel.setPreferredSize(new Dimension(200, 400));
		getComponentFactory().createTitledBorderFor(labelNoteInterne.getText(), noteInternePanel);

		builder.addComponent(noteInternePanel, 9, 40, 3, 1);

		getFormModel().getFieldMetadata(BeneAmmortizzabile.PROP_CODICE_BENE_ESTERNO).setReadOnly(true);
		getFormModel().getFieldMetadata(BeneAmmortizzabile.PROP_IMPORTO_SOGGETTO_AD_AMMORTAMENTO).setReadOnly(true);

		initListeners();

		return getComponentFactory().createScrollPane(builder.getPanel());
	}

	@Override
	protected String getCommitCommandFaceDescriptorId() {
		return getFormModel().getId() + ".save";
	}

	@Override
	protected String getNewFormObjectCommandId() {
		return getFormModel().getId() + ".new";
	}

	@Override
	protected String getRevertCommandFaceDescriptorId() {
		return getFormModel().getId() + ".revert";
	}

	/**
	 * Crea tutti i listeners.
	 */
	private void initListeners() {
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				codiceEntitaTextField.setEditable(false);
				cambioCodiceCommand.setEnabled(!((Boolean) evt.getNewValue()));
			}
		});
	}

}
