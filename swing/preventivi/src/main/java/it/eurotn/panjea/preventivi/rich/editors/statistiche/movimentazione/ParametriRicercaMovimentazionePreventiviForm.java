package it.eurotn.panjea.preventivi.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaMovimentazionePreventiviForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaMovimentazionePreventiviForm";

	private IPreventiviBD preventiviBD = null;
	private List<TipoAreaPreventivo> tipiAreePreventivo = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param parametriRicercaMovimentazione
	 *            parametri di ricerca iniziale
	 */
	public ParametriRicercaMovimentazionePreventiviForm(
			final ParametriRicercaMovimentazione parametriRicercaMovimentazione) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaMovimentazione, false, FORM_ID), FORM_ID);

		List<TipoEntita> tipiEntitaClienti = new ArrayList<TipoEntita>();
		tipiEntitaClienti.add(TipoEntita.CLIENTE);
		tipiEntitaClienti.add(TipoEntita.CLIENTE_POTENZIALE);
		ValueModel tipiEntitaClientiValueModel = new ValueHolder(tipiEntitaClienti);
		DefaultFieldMetadata tipiEntitaCLientiData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaClientiValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaClienti", tipiEntitaClientiValueModel, tipiEntitaCLientiData);

		ValueModel entitaPotenzialiInRicercaValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata entitaPotenzialimetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaPotenzialiInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("entitaPotenzialiPerRicerca", entitaPotenzialiInRicercaValueModel, entitaPotenzialimetaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref, 4dlu,left:200dlu, 5dlu,default:grow",
				"4dlu,default,4dlu,default, 4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r,c");

		builder.addPropertyAndLabel("dataRegistrazione", 1, 2);

		builder.addPropertyAndLabel("dataConsegna", 1, 4);

		builder.addLabel("articoloLite", 1, 6);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloLite", new String[] { "codice", "descrizione" }), 3, 6);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);

		builder.addLabel("entitaLite", 1, 8);
		SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(bf.createBoundSearchText("entitaLite",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaClienti" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }), 3, 8);
		searchPanelEntita.getTextFields().get("codice").setColumns(10);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(26);

		builder.addPropertyAndLabel("statoRiga", 1, 10);

		builder.addBinding(bf.createBoundCheckBoxTree("tipiAreaPreventivo",
				new String[] { "tipoDocumento.classeTipoDocumento" }, new ValueHolder(getTipiAreaPreventivo())), 5, 2,
				1, 9);
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ParametriRicercaMovimentazione parametriRicercaMovimentazione = new ParametriRicercaMovimentazione();
		Calendar calendar = Calendar.getInstance();
		Periodo dataRegistrazione = new Periodo();
		dataRegistrazione.setTipoPeriodo(TipoPeriodo.DATE);
		dataRegistrazione.setDataFinale(calendar.getTime());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		dataRegistrazione.setDataIniziale(calendar.getTime());
		parametriRicercaMovimentazione.setDataRegistrazione(dataRegistrazione);
		return parametriRicercaMovimentazione;
	}

	/**
	 * Restituisco tutti i TipiAreaOrdine.
	 * 
	 * @return List<TipoAreaOrdine>
	 */
	private List<TipoAreaPreventivo> getTipiAreaPreventivo() {
		if (tipiAreePreventivo == null) {
			tipiAreePreventivo = preventiviBD.caricaTipiAreaPreventivo("tipoDocumento.codice", null, true);
		}
		return tipiAreePreventivo;
	}

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}

}
