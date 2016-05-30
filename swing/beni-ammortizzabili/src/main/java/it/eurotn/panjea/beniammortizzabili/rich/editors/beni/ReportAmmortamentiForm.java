/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.beniammortizzabili2.util.parametriricerca.ParametriRicercaBeni;
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
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author adriano
 * @version 1.0, 13/dic/06
 */
public class ReportAmmortamentiForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "reportAmmortamentiForm";

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaBeni
	 *            parametri di ricerca
	 */
	public ReportAmmortamentiForm(final ParametriRicercaBeni parametriRicercaBeni) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaBeni, false, FORM_ID), FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text dei fornitori mi selezioni solo
		// fornitori e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaFornitore = new ArrayList<TipoEntita>();
		tipiEntitaFornitore.add(TipoEntita.FORNITORE);

		ValueModel tipiEntitaFornitoriValueModel = new ValueHolder(tipiEntitaFornitore);
		DefaultFieldMetadata tipiEntitaFornitoriData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaFornitoriValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaFornitori", tipiEntitaFornitoriValueModel, tipiEntitaFornitoriData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		SearchPanel searchPanelFornitore = (SearchPanel) builder.add(bf.createBoundSearchText("fornitoreLite",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaFornitori" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class))[1];
		searchPanelFornitore.getTextFields().get("codice").setColumns(5);
		builder.row();
		builder.add(bf.createBoundCheckBox("visualizzaFigli"));
		builder.row();

		return builder.getForm();
	}

}
