/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.forms.ricercaeffetti;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.Effetto.StatoEffetto;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.components.ImportoTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Leonardo
 * 
 */
public class ParametriRicercaEffettiForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(ParametriRicercaEffettiForm.class);
	public static final String FORM_ID = "parametriRicercaEffettiForm";
	private AziendaCorrente aziendaCorrente = null;

	/**
	 * Costruttore.
	 * 
	 * @param aziendaCorrente
	 *            azienda corrente
	 */
	public ParametriRicercaEffettiForm(final AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaEffetti(), false, FORM_ID), FORM_ID);
		this.aziendaCorrente = aziendaCorrente;

		// aggiungo la finta proprietà tipiEntita per far si che la search text
		// dell'entità mi selezioni solo clienti e
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
		logger.debug("--> Creo controlli per il form");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, left:default,4dlu,left:default, 10dlu",
				"2dlu,default, 2dlu,default, 2dlu,default, 2dlu,default, 2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); //
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		// stato rata
		Binding bindingStato = bf.createBoundEnumCheckBoxList("statiEffetto", StatoEffetto.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addLabel("statiEffetto", 1);
		builder.addBinding(bindingStato, 3, 2, 3, 1);

		builder.nextRow();

		// ##################################### PRIMA RIGA: DATA VALUTA
		builder.addPropertyAndLabel("daDataValuta", 1);
		builder.addPropertyAndLabel("aDataValuta", 5);

		builder.nextRow();

		// ######################## SECONDA RIGA: IMPORTO
		builder.addLabel("daImporto", 1);
		ImportoTextField daImporto = (ImportoTextField) builder.addBinding(bf.createBoundImportoTextField("daImporto"),
				3);
		daImporto.setColumns(8);
		builder.addLabel("aImporto", 5);
		ImportoTextField aImporto = (ImportoTextField) builder
				.addBinding(bf.createBoundImportoTextField("aImporto"), 7);
		aImporto.setColumns(8);

		builder.nextRow();

		// ######################## TERZA RIGA:
		// entita'
		Binding bindingEntita = bf.createBoundSearchText("entita",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntita" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
		SearchPanel entSearchPanel = (SearchPanel) bindingEntita.getControl();
		entSearchPanel.getTextFields().get("codice").setColumns(5);
		entSearchPanel.getTextFields().get("anagrafica.denominazione").setColumns(18);
		builder.addLabel("entita", 1);
		builder.addBinding(bindingEntita, 3);

		// rapporto bancario azienda
		Binding bindingBanca = bf.createBoundSearchText("rapportoBancarioAzienda", new String[] { "numero",
				"descrizione" }, new String[] {}, new String[] {});
		SearchPanel bancaSearchPanel = (SearchPanel) bindingBanca.getControl();
		bancaSearchPanel.getTextFields().get("numero").setColumns(8);
		bancaSearchPanel.getTextFields().get("descrizione").setColumns(18);
		builder.addLabel("rapportoBancarioAzienda", 5);
		builder.addBinding(bindingBanca, 7);

		builder.nextRow();

		// ######################## QUARTA RIGA:
		// numero doc.
		builder.addLabel("numeroDocumento", 1);
		builder.addBinding(bf.createBoundCodice("numeroDocumento", true, false), 3);

		builder.nextRow();

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ParametriRicercaEffetti parametriRicercaEffetti = new ParametriRicercaEffetti();
		parametriRicercaEffetti.setDaDataValuta(aziendaCorrente.getDataInizioEsercizio());
		parametriRicercaEffetti.setADataValuta(aziendaCorrente.getDataFineEsercizio());
		parametriRicercaEffetti.getDaImporto().setCodiceValuta(aziendaCorrente.getCodiceValuta());
		parametriRicercaEffetti.getAImporto().setCodiceValuta(aziendaCorrente.getCodiceValuta());
		return parametriRicercaEffetti;
	}

}
