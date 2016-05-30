package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.codice.CodiceBinder.CodicePanel;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Form dei componenti per l'input dei criteri di ricerca di {@link Documento} <br>
 * Il Form contiene una table contenente i risultati della ricerca.
 *
 * @author adriano
 * @version 1.0, 10/set/2008
 */
public class SelezioneDocumentoForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(SelezioneDocumentoForm.class);
	private static final String FORM_ID = "selezioneDocumentoForm";

	/**
	 * Form per la selezione di un documento.
	 */
	public SelezioneDocumentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new Documento(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default",
				"2dlu,default,2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);
		builder.addPropertyAndLabel("dataDocumento");
		builder.addLabel("tipoDocumento", 5);
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoDocumento", new String[] { "codice", "descrizione" });
		SearchPanel searchPanelTipoDocumento = (SearchPanel) builder.addBinding(bindingTipoDoc, 7);
		searchPanelTipoDocumento.getTextFields().get("codice").setColumns(5);
		searchPanelTipoDocumento.getTextFields().get("descrizione").setColumns(15);
		builder.nextRow();

		builder.addLabel("entita", 1);
		Binding bindingEntita = bf.createBoundSearchText("entita",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoDocumento.tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
		SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(bindingEntita, 3);
		searchPanelEntita.getTextFields().get("codice").setColumns(5);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(15);

		// per mettere come label nr.doc invece di codice devo recuperare a mano
		// la label, cambiare propertyPath non e' sufficiente.
		Binding bindingCodice = bf.createBoundCodice("codice", true, false);
		String msgNrDoc = getMessage("documento.codice");
		JLabel labelNrDoc = getComponentFactory().createLabel(msgNrDoc);
		getFormModel().getFieldFace("codice").configure(labelNrDoc);
		CodicePanel codicePanel = (CodicePanel) bindingCodice.getControl();
		codicePanel.getTextFieldCodice().setColumns(6);
		builder.addComponent(labelNrDoc, 5);
		builder.addComponent(codicePanel, 7);
		builder.nextRow();
		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

}
