package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

public class ParametriRicercaBilancioForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriRicercaBilancioForm";
	private AziendaCorrente aziendaCorrente = null;
	private ParametriRicercaBilancio parametriRicercaBilancio = null;
	private JDateChooser jDateChooser = null;
	private PeriodoParametriChangeListener periodoParametriChangeListener = null;

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaBilancio
	 *            parametri ricerca bilancio
	 * @param aziendaCorrente
	 *            azienda corrente
	 */
	public ParametriRicercaBilancioForm(final ParametriRicercaBilancio parametriRicercaBilancio,
			final AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaBilancio, false, FORM_ID), FORM_ID);
		this.aziendaCorrente = aziendaCorrente;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:80dlu,4dlu,left:default, 10dlu, right:pref,4dlu,left:default,10dlu, right:pref,4dlu,left:default, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);
		builder.nextRow();

		JPanel panel = (JPanel) builder.addPropertyAndLabel("dataRegistrazione", 1, 4, 9, 1)[1];
		jDateChooser = (JDateChooser) panel.getComponents()[1];
		builder.nextRow();

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		boolean isCentriCostoPluginPresente = pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI);
		if (isCentriCostoPluginPresente) {
			builder.addLabel("centroCosto", 1);

			Binding bindingCentroCosto = bf.createBoundSearchText("centroCosto", null);
			builder.addBinding(bindingCentroCosto, 3, 6, 6, 1);
			builder.nextRow();
		}

		builder.addPropertyAndLabel("stampaConti", 1);
		builder.addPropertyAndLabel("stampaClienti", 5);
		builder.addPropertyAndLabel("stampaFornitori", 9);
		builder.nextRow();

		builder.addPropertyAndLabel("visualizzaSaldiCFAzero", 1);
		if (isCentriCostoPluginPresente) {
			builder.addPropertyAndLabel("stampaCentriCosto", 5);
		}
		builder.nextRow();

		periodoParametriChangeListener = new PeriodoParametriChangeListener();
		periodoParametriChangeListener.setPeriodoValueModel(getValueModel("dataRegistrazione"));

		// CheckBox per lo stato
		Binding bindingStato = bf.createBoundEnumCheckBoxList("statiAreaContabile", StatoAreaContabile.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addLabel("statiAreaContabile", 1);
		builder.addBinding(bindingStato, 3, 12, 9, 1);
		builder.nextRow();

		addFormValueChangeListener("annoCompetenza", periodoParametriChangeListener);

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		parametriRicercaBilancio = new ParametriRicercaBilancio();
		Set<StatoAreaContabile> stati = new HashSet<StatoAreaContabile>();
		stati.add(StatoAreaContabile.CONFERMATO);
		stati.add(StatoAreaContabile.VERIFICATO);
		parametriRicercaBilancio.setStatiAreaContabile(stati);
		parametriRicercaBilancio.setAnnoCompetenza(aziendaCorrente.getAnnoContabile());
		parametriRicercaBilancio.getDataRegistrazione().setDataIniziale(aziendaCorrente.getDataInizioEsercizio());
		parametriRicercaBilancio.getDataRegistrazione().setDataFinale(aziendaCorrente.getDataFineEsercizio());
		return parametriRicercaBilancio;
	}

	/**
	 * Indica a quale component dare il focus all'apertura del form.
	 */
	public void requestFocusForData() {
		PanjeaSwingUtil.giveFocusToComponent(new Component[] { jDateChooser });
	}
}
