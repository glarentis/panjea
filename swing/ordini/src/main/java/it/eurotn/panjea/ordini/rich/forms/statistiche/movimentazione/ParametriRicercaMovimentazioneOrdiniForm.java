package it.eurotn.panjea.ordini.rich.forms.statistiche.movimentazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
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

public class ParametriRicercaMovimentazioneOrdiniForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaMovimentazioneOrdiniForm";
	public static final String FORMMODEL_ID = "parametriRicercaMovimentazioneOrdiniFormModel";
	private IOrdiniDocumentoBD ordiniDocumentoBD = null;
	private List<TipoAreaOrdine> tipiAreeAreaOrdine = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param parametriRicercaMovimentazione
	 *            parametri di ricerca iniziale
	 */
	public ParametriRicercaMovimentazioneOrdiniForm(final ParametriRicercaMovimentazione parametriRicercaMovimentazione) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaMovimentazione, false, FORMMODEL_ID), FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text degli agenti mi selezioni solo agenti e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaAgente = new ArrayList<TipoEntita>();
		tipiEntitaAgente.add(TipoEntita.AGENTE);

		ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaAgente);
		DefaultFieldMetadata tipiEntitaAgenteData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaAgente", tipiEntitaAgenteValueModel, tipiEntitaAgenteData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:pref, 4dlu,left:100dlu, 5dlu,left:pref, 4dlu,left:pref,5dlu,left:pref, 4dlu,left:pref,5dlu,4dlu,left:pref,5dlu,default:grow",
				"4dlu,default,4dlu,default, 4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r,c");

		builder.addPropertyAndLabel("dataRegistrazione", 1, 2, 9, 1);

		builder.addPropertyAndLabel("dataConsegna", 1, 4, 9, 1);

		builder.addLabel("articoloLite", 1, 6);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloLite", new String[] { "codice", "descrizione" }), 3, 6, 9, 1);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);

		builder.addLabel("depositoLite", 1, 8);
		SearchPanel searchPanelDeposito = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("depositoLite", new String[] { "codice", "descrizione" }), 3, 8, 9, 1);
		searchPanelDeposito.getTextFields().get("codice").setColumns(10);
		searchPanelDeposito.getTextFields().get("descrizione").setColumns(26);

		builder.addLabel("entitaLite", 1, 10);
		SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("entitaLite", new String[] { "codice", "anagrafica.denominazione" }), 3, 10,
				9, 1);
		searchPanelEntita.getTextFields().get("codice").setColumns(10);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(26);

		builder.addLabel("agente", 1, 12);
		SearchPanel searchPanelAgente = (SearchPanel) builder.addBinding(bf.createBoundSearchText("agente",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaAgente" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class), 3, 12, 9, 1);
		searchPanelAgente.getTextFields().get("codice").setColumns(10);
		searchPanelAgente.getTextFields().get("anagrafica.denominazione").setColumns(26);

		builder.addPropertyAndLabel("noteRiga", 1, 14, 5, 1);

		builder.addPropertyAndLabel("statoRiga", 9, 14);

		builder.addPropertyAndLabel("righeOmaggio", 14, 14);

		builder.addBinding(bf.createBoundCheckBoxTree("tipiAreaOrdine",
				new String[] { "tipoDocumento.classeTipoDocumento" }, new ValueHolder(getTipiAreaOrdine())), 13, 2, 4,
				12);
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
	private List<TipoAreaOrdine> getTipiAreaOrdine() {
		if (tipiAreeAreaOrdine == null) {
			tipiAreeAreaOrdine = ordiniDocumentoBD.caricaTipiAreaOrdine("tipoDocumento.codice", null, true);
		}
		return tipiAreeAreaOrdine;
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
