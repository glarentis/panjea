/**
 * 
 */
package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.partite.rich.search.TipoAreaPartitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno.StatoAssegno;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 * 
 */
public class ParametriRicercaAssegniForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaAssegniForm";
	public static final String FORMMODEL_ID = "parametriRicercaAssegniFormModel";

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAssegniForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaAssegni(), false, FORMMODEL_ID), FORM_ID);

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
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("dataDocumento");
		builder.nextRow();

		builder.addLabel("tipoAreaPartita", 1);
		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaPartita", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" }, new String[] { "escludiTipiAreaPartiteDistinta" },
				new String[] { TipoAreaPartitaSearchObject.PARAM_ESCLUDI_TIPIAREAPARTITE_DISTINTA });
		SearchPanel searchTextTipoAreaCont = (SearchPanel) builder.addBinding(bindingTipoDoc, 3);
		searchTextTipoAreaCont.getTextFields().get("tipoDocumento.codice").setColumns(5);
		searchTextTipoAreaCont.getTextFields().get("tipoDocumento.descrizione").setColumns(18);
		builder.nextRow();

		builder.addLabel("entita", 1);
		JComponent components = builder.addBinding(bf.createBoundSearchText("entita", new String[] { "codice",
				"anagrafica.denominazione" }, new String[] { "tipiEntita" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }), 3);
		((SearchPanel) components).getTextFields().get("codice").setColumns(5);
		((SearchPanel) components).getTextFields().get("anagrafica.denominazione").setColumns(23);
		builder.nextRow();

		builder.addLabel("statiAssegno", 1);
		Binding bindingStato = bf.createBoundEnumCheckBoxList("statiAssegno", StatoAssegno.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addBinding(bindingStato, 3);
		builder.nextRow();

		return builder.getPanel();
	}

}
