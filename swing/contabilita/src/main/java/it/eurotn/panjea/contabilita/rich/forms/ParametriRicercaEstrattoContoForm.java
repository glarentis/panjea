package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.rich.search.SottoContoSearchTextField;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form per visualizzare i parametri ricerca estratto conto.
 * 
 * @author leonardo
 */
public class ParametriRicercaEstrattoContoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriRicercaEstrattoContoForm";
	private AziendaCorrente aziendaCorrente = null;
	private final IDocumentiBD documentiBD;
	private SottoContoSearchTextField searchTextFieldSottoConto;
	private ParametriRicercaEstrattoConto parametriRicercaEstrattoConto = null;

	private PeriodoParametriChangeListener periodoParametriChangeListener = null;

	private List<TipoDocumento> tipiDocumento;

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaEstrattoConto
	 *            parametri
	 * @param aziendaCorrente
	 *            aziendaCorrente
	 * @param documentiBD
	 *            BD per i documenti contabili
	 */
	public ParametriRicercaEstrattoContoForm(final ParametriRicercaEstrattoConto parametriRicercaEstrattoConto,
			final AziendaCorrente aziendaCorrente, final IDocumentiBD documentiBD) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaEstrattoConto, false, FORM_ID), FORM_ID);
		this.aziendaCorrente = aziendaCorrente;
		this.documentiBD = documentiBD;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default, 10dlu, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default, fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);
		builder.nextRow();

		builder.setComponentAttributes("l, c");
		builder.addPropertyAndLabel("dataRegistrazione", 1, 4);
		builder.nextRow();

		builder.setComponentAttributes("f, c");
		searchTextFieldSottoConto = (SottoContoSearchTextField) ((SearchPanel) (builder.addPropertyAndLabel(
				"sottoConto", 1, 6)[1])).getTextFields().get("descrizione");
		builder.nextRow();

		builder.addComponent(searchTextFieldSottoConto.getLabelSottoConto(), 3, 8);
		builder.nextRow();

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
			builder.addLabel("centroCosto");

			Binding bindingCentroCosto = bf.createBoundSearchText("centroCosto", null);
			builder.addBinding(bindingCentroCosto, 3, 10);
			builder.nextRow();
		}

		builder.nextRow();

		// CheckBox per lo stato
		Binding bindingStato = bf.createBoundEnumCheckBoxList("statiAreaContabile", StatoAreaContabile.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addLabel("statiAreaContabile", 1, 12);
		builder.addBinding(bindingStato, 3, 12);

		builder.nextRow();
		builder.setComponentAttributes("f, f");
		List<TipoDocumento> tipiList = getTipiDocumento();
		Set<TipoDocumento> tipiSet = new HashSet<TipoDocumento>(tipiList);
		builder.addBinding(bf.createBoundCheckBoxTree("tipiDocumento", new String[] { "abilitato",
				"classeTipoDocumento" }, new ValueHolder(tipiSet)), 5, 2, 1, 12);
		builder.nextRow();

		// Aggiunta dei vari listener
		periodoParametriChangeListener = new PeriodoParametriChangeListener();
		periodoParametriChangeListener.setPeriodoValueModel(getValueModel("dataRegistrazione"));
		addFormValueChangeListener("annoCompetenza", periodoParametriChangeListener);

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		logger.debug("--> createNewObject per parametri estratto conto");
		parametriRicercaEstrattoConto = new ParametriRicercaEstrattoConto();
		parametriRicercaEstrattoConto.setAnnoCompetenza(this.aziendaCorrente.getAnnoContabile());
		parametriRicercaEstrattoConto.getDataRegistrazione().setDataIniziale(
				this.aziendaCorrente.getDataInizioEsercizio());
		parametriRicercaEstrattoConto.getDataRegistrazione().setDataFinale(this.aziendaCorrente.getDataFineEsercizio());
		parametriRicercaEstrattoConto.getStatiAreaContabile().add(AreaContabile.StatoAreaContabile.CONFERMATO);
		parametriRicercaEstrattoConto.getStatiAreaContabile().add(AreaContabile.StatoAreaContabile.VERIFICATO);
		parametriRicercaEstrattoConto.setNumeroTotaleTipiDocumento(getTipiDocumento().size());
		return parametriRicercaEstrattoConto;
	}

	/**
	 * 
	 * @return tipi Documenti
	 */
	private List<TipoDocumento> getTipiDocumento() {
		if (tipiDocumento == null) {
			tipiDocumento = documentiBD.caricaTipiDocumento("codice", null, true);
		}
		return tipiDocumento;
	}

	/**
	 * Controllo che riceve il focus.
	 */
	public void requestFocusForConto() {
		searchTextFieldSottoConto.getTextField().requestFocusInWindow();
	}
}
