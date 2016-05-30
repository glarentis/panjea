package it.eurotn.panjea.preventivi.rich.forms.areapreventivo;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo.STATO_PREVENTIVO;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.Calendar;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaAreaPreventivoForm extends PanjeaAbstractForm implements Focussable {

	private AziendaCorrente aziendaCorrente;

	public static final String FORM_ID = "parametriRicercaAreaPreventivoForm";

	public static final String FORMMODEL_ID = "parametriRicercaAreaPreventivoFormModel";

	private IPreventiviBD preventiviBD;

	private JComponent dataComponent;

	/**
	 * Costruttore.
	 *
	 * @param parametriRicercaAreaOrdine
	 *            parametri da gestire
	 */
	public ParametriRicercaAreaPreventivoForm(final ParametriRicercaAreaPreventivo parametriRicercaAreaOrdine) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaAreaOrdine, false, FORMMODEL_ID), FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text degli agenti mi selezioni solo agenti e
		// non altri tipi entità
		TipoEntita tipiEntitaAgente = TipoEntita.AGENTE;

		ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaAgente);
		DefaultFieldMetadata tipiEntitaAgenteData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipiEntitaAgente", tipiEntitaAgenteValueModel, tipiEntitaAgenteData);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:100dlu,20dlu,right:pref,4dlu,left:100dlu,20dlu,right:pref,4dlu,left:default,20dlu,fill:default:grow",
				"default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");

		((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);
		builder.addBinding(
				bf.createEnumRadioButtonBinding(STATO_PREVENTIVO.class, "statoPreventivo", JList.HORIZONTAL_WRAP), 4,
				1, 6, 1);
		builder.nextRow();

		dataComponent = (JComponent) builder.addPropertyAndLabel("dataDocumento", 1, 3, 9, 1)[1].getComponent(1);
		builder.nextRow();

		builder.addLabel("numeroDocumentoIniziale", 1);
		builder.addBinding(bf.createBoundCodice("numeroDocumentoIniziale", true, false), 3);
		builder.addLabel("numeroDocumentoFinale", 5);
		builder.addBinding(bf.createBoundCodice("numeroDocumentoFinale", true, false), 7);
		builder.nextRow();

		builder.addLabel("entita");
		SearchPanel searchEntita = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 3, 7, 9, 1);
		searchEntita.getTextFields().get("codice").setColumns(5);
		searchEntita.getTextFields().get("anagrafica.denominazione").setColumns(15);

		builder.nextRow();
		builder.addLabel("utente");
		SearchPanel searchUtente = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("utente", new String[] { "userName", "nome" }), 3, 9, 9, 1);
		searchUtente.getTextFields().get("userName").setColumns(5);
		searchUtente.getTextFields().get("nome").setColumns(15);

		builder.addLabel("statiAreaPreventivo", 1, 13);
		Binding bindingStatoAreaPreventivo = bf.createBoundEnumCheckBoxList("statiAreaPreventivo",
				StatoAreaPreventivo.class, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addBinding(bindingStatoAreaPreventivo, 3, 13, 8, 1);

		List<TipoAreaPreventivo> tipiAreePreventivo = preventiviBD.caricaTipiAreaPreventivo("tipoDocumento.codice",
				null, true);
		builder.addBinding(bf.createBoundCheckBoxTree("tipiAreaPreventivo",
				new String[] { "tipoDocumento.classeTipoDocumento" }, new ValueHolder(tipiAreePreventivo)), 13, 1, 1, 7);

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

	@Override
	public ParametriRicercaAreaPreventivo createNewObject() {
		logger.debug("--> Enter createNewObject");
		ParametriRicercaAreaPreventivo parametriRicercaAreaOrdine = new ParametriRicercaAreaPreventivo();
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, aziendaCorrente.getAnnoMagazzino());
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		parametriRicercaAreaOrdine.getDataDocumento().setDataIniziale(calendar.getTime());
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		parametriRicercaAreaOrdine.getDataDocumento().setDataFinale(calendar.getTime());
		parametriRicercaAreaOrdine.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
		logger.debug("--> Exit createNewObject");
		return parametriRicercaAreaOrdine;
	}

	@Override
	public void grabFocus() {
		dataComponent.requestFocusInWindow();
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}

}
