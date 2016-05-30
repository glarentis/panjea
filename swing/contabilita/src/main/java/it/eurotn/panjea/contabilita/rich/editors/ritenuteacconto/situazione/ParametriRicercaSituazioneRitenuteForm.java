/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.ritenuteacconto.situazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Page dei parametri ricerca documenti contabili.
 *
 * @author Leonardo
 */
public class ParametriRicercaSituazioneRitenuteForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(ParametriRicercaSituazioneRitenuteForm.class);
	private static final String FORM_ID = "parametriRicercaSituazioneRitenuteForm";

	/**
	 * Default constructor.
	 *
	 */
	public ParametriRicercaSituazioneRitenuteForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriSituazioneRitenuteAcconto(), false, FORM_ID), FORM_ID);

		// Aggiungo il value model che mi servirà solamente nella search text delle entità
		// per cercare solo i fornitori
		ValueModel entitaFornitoriValueModel = new ValueHolder(TipoEntita.FORNITORE);
		DefaultFieldMetadata entitaFornitoriMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaFornitoriValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipoEntita", entitaFornitoriValueModel, entitaFornitoriMetaData);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "2dlu,default,2dlu,default,2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
		// new
		// FormDebugPanel()
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("periodo", 1, 4);
		builder.nextRow();
		builder.addLabel("fornitore", 1);
		Binding bindingEntita = bf.createBoundSearchText("fornitore", new String[] { "codice",
		"anagrafica.denominazione" }, new String[] { "tipoEntita" },
		new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }, EntitaLite.class);
		SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
		searchPanel.getTextFields().get("codice").setColumns(5);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
		builder.addBinding(bindingEntita, 3);

		return builder.getPanel();
	}

}
