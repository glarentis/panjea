package it.eurotn.panjea.tesoreria.rich.forms;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.rich.binding.codice.CodiceBinder.CodicePanel;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class AreaTesoreriaForm extends PanjeaAbstractForm {

	/**
	 * Listener che controlla le variazioni applicate a {@link TipoAreaPartita} e alla bisogna disabilita i controlli
	 * per la selezione dell'entita' e per la parte iva; viene chiamato da AreaContabile page nel metodo onNew() per
	 * inizializzare lo stato del form senza nessun tipo documento selezionato e comunque per allineare correttamente lo
	 * stato quando viene eseguito il newCommand.
	 * 
	 * @author Leonardo
	 * @version 1.0, 13/giu/07
	 */
	public class TipoAreaPartitePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			logger.debug("--> TipoDocumentoPropertyChange property " + evt.getPropertyName() + " value "
					+ evt.getNewValue());
			if (evt.getNewValue() != null) {

				TipoAreaPartita tipoAreaPartita = (TipoAreaPartita) evt.getNewValue();

				if ((tipoAreaPartita.getTipoDocumento().getTipoEntita() != null)) {
					if ((tipoAreaPartita.getTipoDocumento().getTipoEntita().equals(TipoEntita.CLIENTE))
							|| (tipoAreaPartita.getTipoDocumento().getTipoEntita().equals(TipoEntita.FORNITORE))) {
						bindingEntita.getControl().setVisible(true);
						bindingBanca.getControl().setVisible(false);
						// cEntita.setVisible(true);
						// cBanca.setVisible(false);
						getFormModel().getFieldFace("documento.entita").configure(labelEntita);
					} else if (tipoAreaPartita.getTipoDocumento().getTipoEntita().equals(TipoEntita.BANCA)) {
						bindingEntita.getControl().setVisible(false);
						bindingBanca.getControl().setVisible(true);
						getFormModel().getFieldFace("documento.rapportoBancarioAzienda").configure(labelEntita);
					} else {
						AreaTesoreriaForm.this.disableEntita();
					}
				} else {
					AreaTesoreriaForm.this.disableEntita();
				}
			} else {
				AreaTesoreriaForm.this.disableEntita();
			}
			// HACK richiamo la validate del form model per aggirare un problema sul codice binding o sul formmodel (?)
			// che non aggiorna le validation rules in accordo con il tipo documento scelto (protocollo obbligatorio)
			getFormModel().validate();
		}
	}

	private static Logger logger = Logger.getLogger(AreaTesoreriaForm.class);
	private static final String FORM_ID = "areaPartiteForm";
	private TipoAreaPartitePropertyChange tipoAreaPartitePropertyChange = null;
	private JLabel labelEntita = null;
	private Binding bindingEntita;
	private Binding bindingBanca;

	/**
	 * Costruttore.
	 */
	public AreaTesoreriaForm() {
		super(PanjeaFormModelHelper.createFormModel(new AreaTesoreria(), false, FORM_ID), FORM_ID);
		tipoAreaPartitePropertyChange = new TipoAreaPartitePropertyChange();
	}

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formModel
	 * @param formId
	 *            formId
	 */
	public AreaTesoreriaForm(final FormModel formModel, final String formId) {
		super(formModel, formId);
		tipoAreaPartitePropertyChange = new TipoAreaPartitePropertyChange();
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,left:pref,4dlu,fill:150dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanel());
		builder.setLabelAttributes("r,c");

		// ################################### : data documento
		builder.addPropertyAndLabel("documento.dataDocumento");

		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaPartita", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" }, null, null);
		builder.addLabel("tipoAreaPartita", 5);
		builder.addBinding(bindingTipoDoc, 7);
		SearchPanel componentTipoAreaPartita = (SearchPanel) bindingTipoDoc.getControl();
		componentTipoAreaPartita.getTextFields().get("tipoDocumento.codice").setColumns(5);
		componentTipoAreaPartita.getTextFields().get("tipoDocumento.descrizione").setColumns(18);
		builder.nextRow();

		// ################################### : entita', rapporto bancario
		bindingEntita = bf.createBoundSearchText("documento.entita", new String[] { "codice",
				"anagrafica.denominazione" }, new String[] { "tipoAreaPartita.tipoDocumento.tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
		SearchPanel searchPanelEntita = (SearchPanel) bindingEntita.getControl();
		searchPanelEntita.getTextFields().get("codice").setColumns(5);
		// searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(18);
		bindingBanca = bf.createBoundSearchText("documento.rapportoBancarioAzienda", new String[] { "numero",
				"descrizione" }, new String[] {}, new String[] {});

		SearchPanel bancaSearchPanel = (SearchPanel) bindingBanca.getControl();
		bancaSearchPanel.getTextFields().get("numero").setColumns(5);
		labelEntita = getComponentFactory().createLabel("");
		builder.addComponent(labelEntita, 1);
		builder.addBinding(bindingEntita, 3, 3, 5, 1);
		builder.addBinding(bindingBanca, 3, 3, 5, 1);
		bindingEntita.getControl().setVisible(false);
		bindingBanca.getControl().setVisible(false);
		builder.nextRow();

		// ################################### : num.doc.
		builder.addLabel("documento.codice");
		JComponent numDocumentoComponent = builder.addBinding(bf.createBoundCodice("documento.codice",
				"tipoAreaPartita.tipoDocumento.registroProtocollo", "documento.valoreProtocollo",
				"tipoAreaPartita.tipoDocumento.patternNumeroDocumento", null), 3);
		((CodicePanel) numDocumentoComponent).getTextFieldCodice().setColumns(10);

		// ################################### : TOTALE DOCUMENTO
		builder.addLabel("documento.totale", 5);
		builder.addBinding(bf.createBoundImportoTextField("documento.totale"), 7);
		builder.nextRow();
		// ################################### : SPESE INCASSO
		builder.addPropertyAndLabel("speseIncasso", 5);

		addFormValueChangeListener("tipoAreaPartita", tipoAreaPartitePropertyChange);
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				getFormModel().getFieldMetadata("documento.dataDocumento").setReadOnly(true);
				getFormModel().getFieldMetadata("tipoAreaPartita").setReadOnly(true);
				getFormModel().getFieldMetadata("documento.entita").setReadOnly(true);
				getFormModel().getFieldMetadata("documento.rapportoBancarioAzienda").setReadOnly(true);
				getFormModel().getFieldMetadata("documento.totale").setReadOnly(true);
				getFormModel().getFieldMetadata("speseIncasso").setReadOnly(true);
			}
		});

		return builder.getPanel();
	}

	/**
	 * Nasconde i controlli per la selezione dell'entita' e banca.
	 * 
	 * @param enabled
	 */
	private void disableEntita() {
		bindingEntita.getControl().setVisible(false);
		bindingBanca.getControl().setVisible(false);
		getValueModel("documento.entita").setValue(new ClienteLite());
		// cEntita.setVisible(false);
		// cBanca.setVisible(false);
		// getValueModel("documento.rapportoBancarioAzienda").setValue(new
		// RapportoBancarioAzienda());
		labelEntita.setText("");
	}

	/**
	 * @return the tipoAreaPartitePropertyChange
	 */
	public TipoAreaPartitePropertyChange getTipoAreaPartitePropertyChange() {
		return tipoAreaPartitePropertyChange;
	}

}
