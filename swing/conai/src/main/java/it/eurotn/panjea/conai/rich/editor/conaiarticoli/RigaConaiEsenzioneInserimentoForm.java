package it.eurotn.panjea.conai.rich.editor.conaiarticoli;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.conai.domain.ConaiEsenzione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class RigaConaiEsenzioneInserimentoForm extends AbstractFocussableForm {

	/**
	 * Costruttore.
	 */
	public RigaConaiEsenzioneInserimentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ConaiEsenzione(), false, "conaiEsenzione"));

		// Aggiungo al formModel il value model dinamico per il tipo entità cliente
		ValueModel tipoEntitaValueModel = new ValueHolder(TipoEntita.CLIENTE);
		DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				tipoEntitaValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipoEntita", tipoEntitaValueModel, metaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,right:pref,4dlu,fill:80dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("l,c");

		builder.addBinding(getEntitaBinding(bf), 1);

		builder.addBinding(bf.createBoundPercentageText("percentualeEsenzione"), 3);
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ConaiEsenzione listino = new ConaiEsenzione();
		return listino;
	}

	/**
	 * Crea e restituisce il SearchTextBinding per l' entita.
	 * 
	 * @param bf
	 *            il binding factory
	 * @return Binding
	 */
	private Binding getEntitaBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingEntita = bf.createBoundSearchText("entita",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
		SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
		searchPanel.getTextFields().get("codice").setColumns(5);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(15);
		return bindingEntita;
	}
}
