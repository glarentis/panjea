package it.eurotn.panjea.cauzioni.rich.editors.situazionecauzioni;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.magazzino.rich.search.ArticoloSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaSituazioneCauzioniForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaSituazioneCauzioniForm";

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriRicercaSituazioneCauzioniForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaSituazioneCauzioni(), false, FORM_ID), FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text dell'entità mi selezioni solo clienti e
		// fornitori
		List<TipoEntita> tipiEntita = new ArrayList<TipoEntita>();
		tipiEntita.add(TipoEntita.CLIENTE);
		tipiEntita.add(TipoEntita.FORNITORE);

		ValueModel tipiEntitaValueModel = new ValueHolder(tipiEntita);
		DefaultFieldMetadata tipiEntitaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaValueModel), List.class, true, null);
		getFormModel().add("tipiEntita", tipiEntitaValueModel, tipiEntitaData);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,right:pref,4dlu,left:pref", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("periodo", 1, 2);

		builder.addLabel("entita", 1, 4);
		Binding entiBinding = bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" },
				new String[] { "tipiEntita" }, new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY });
		SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(entiBinding, 3, 4);
		searchPanelEntita.getTextFields().get("codice").setColumns(5);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(20);

		builder.addLabel("articolo", 5, 4);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" },
						new String[] { "entita" }, new String[] { ArticoloSearchObject.ENTITA_KEY }), 7, 4);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(20);

		return builder.getPanel();
	}

}
