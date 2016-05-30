package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancioConfronto;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

/**
 * 
 * @author Leonardo
 */
public class ParametriRicercaBilancioConfrontoForm extends PanjeaAbstractForm {

	protected class CheckChangeListener implements PropertyChangeListener {

		private ParametriRicercaBilancioConfrontoForm this$0;

		/**
		 * Costruttore.
		 */
		public CheckChangeListener() {
			this$0 = ParametriRicercaBilancioConfrontoForm.this;
		}

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			logger.debug("-->CAMBIATA LA PROPRIETA'" + arg0.getPropertyName());

			Boolean stampaConto = (Boolean) this$0.getFormModel().getValueModel("stampaConto").getValue();
			Boolean stampaSottoConto = (Boolean) this$0.getFormModel().getValueModel("stampaSottoConto").getValue();

			if (!stampaConto) {
				this$0.getFormModel().getValueModel("stampaSottoConto").setValueSilently(new Boolean(false), null);
				this$0.getFormModel().getValueModel("stampaClienti").setValueSilently(new Boolean(false), null);
				this$0.getFormModel().getValueModel("stampaFornitori").setValueSilently(new Boolean(false), null);
			}

			if (!stampaSottoConto) {
				this$0.getFormModel().getValueModel("stampaClienti").setValueSilently(new Boolean(false), null);
				this$0.getFormModel().getValueModel("stampaFornitori").setValueSilently(new Boolean(false), null);
			}

			this$0.getFormModel().getFieldMetadata("stampaSottoConto").setEnabled(stampaConto);
			this$0.getFormModel().getFieldMetadata("stampaClienti").setEnabled(stampaSottoConto);
			this$0.getFormModel().getFieldMetadata("stampaFornitori").setEnabled(stampaSottoConto);
		}
	}

	private static final String FORM_ID = "parametriRicercaBilancioConfrontoForm";
	private AziendaCorrente aziendaCorrente = null;
	private ParametriRicercaBilancioConfronto parametriRicercaBilancioConfronto = null;
	private JDateChooser jDateChooser = null;

	private PeriodoParametriChangeListener periodoParametriChangeListener = null;

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaBilancio
	 *            parametriricerca bilancio confronto per eseguire la ricerca
	 * @param aziendaCorrente
	 *            azienda corrente
	 */
	public ParametriRicercaBilancioConfrontoForm(final ParametriRicercaBilancio parametriRicercaBilancio,
			final AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaBilancio, false, FORM_ID), FORM_ID);
		this.aziendaCorrente = aziendaCorrente;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:130dlu,4dlu,left:default, 10dlu, right:100dlu,4dlu,left:default, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		// binding per il periodo 1
		Binding bindingDa1 = bf.createBinding("dataRegistrazione");
		JComponent c1 = bindingDa1.getControl();
		jDateChooser = (JDateChooser) c1.getComponents()[1];

		// binding per il periodo 2
		Binding bindingDa2 = bf.createBinding("dataRegistrazione2");
		JComponent c3 = bindingDa2.getControl();

		((JTextField) builder.addPropertyAndLabel("annoCompetenza", 1)[1]).setColumns(4);
		((JTextField) builder.addPropertyAndLabel("annoCompetenza2", 9)[1]).setColumns(4);
		builder.nextRow();

		JLabel per1 = getComponentFactory().createLabel("");
		JLabel per2 = getComponentFactory().createLabel("");

		getFormModel().getFieldFace("periodoIniziale").configure(per1);
		getFormModel().getFieldFace("periodoFinale").configure(per2);

		builder.addComponent(per1, 1);
		builder.addComponent(c1, 3, 4, 6, 1);
		builder.addComponent(per2, 9);
		builder.addComponent(c3, 11);
		builder.nextRow();

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
			builder.addLabel("centroCosto", 1);

			Binding bindingCentroCosto = bf.createBoundSearchText("centroCosto", null);
			builder.addBinding(bindingCentroCosto, 3, 6, 3, 1);
			builder.nextRow();
		}

		builder.addPropertyAndLabel("stampaClienti", 1);
		builder.addPropertyAndLabel("stampaFornitori", 5);
		builder.addPropertyAndLabel("stampaCentriCosto", 9);

		builder.nextRow();
		// CheckBox per lo stato
		Binding bindingStato = bf.createBoundEnumCheckBoxList("statiAreaContabile", StatoAreaContabile.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP);
		builder.addLabel("statiAreaContabile", 1);
		builder.addBinding(bindingStato, 3, 10, 8, 1);

		periodoParametriChangeListener = new PeriodoParametriChangeListener();
		periodoParametriChangeListener.setPeriodoValueModel(getValueModel("dataRegistrazione"));

		addFormValueChangeListener("annoCompetenza", periodoParametriChangeListener);

		builder.nextRow();
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		parametriRicercaBilancioConfronto = new ParametriRicercaBilancioConfronto();
		Set<StatoAreaContabile> stati = new HashSet<StatoAreaContabile>();
		stati.add(StatoAreaContabile.CONFERMATO);
		stati.add(StatoAreaContabile.VERIFICATO);
		parametriRicercaBilancioConfronto.setStatiAreaContabile(stati);
		parametriRicercaBilancioConfronto.setAnnoCompetenza(aziendaCorrente.getAnnoContabile());
		parametriRicercaBilancioConfronto.getDataRegistrazione().setDataIniziale(
				aziendaCorrente.getDataInizioEsercizio());
		parametriRicercaBilancioConfronto.getDataRegistrazione().setDataFinale(aziendaCorrente.getDataFineEsercizio());

		parametriRicercaBilancioConfronto.setAnnoCompetenza2(aziendaCorrente.getAnnoContabile() - 1);
		Calendar calda2 = Calendar.getInstance();
		calda2.setTime(aziendaCorrente.getDataInizioEsercizio());
		calda2.add(Calendar.YEAR, -1);
		Calendar cala2 = Calendar.getInstance();
		cala2.setTime(aziendaCorrente.getDataFineEsercizio());
		cala2.add(Calendar.YEAR, -1);

		parametriRicercaBilancioConfronto.getDataRegistrazione2().setDataIniziale(calda2.getTime());
		parametriRicercaBilancioConfronto.getDataRegistrazione2().setDataFinale(cala2.getTime());

		return parametriRicercaBilancioConfronto;
	}

	/**
	 * Indica a quale component dare il focus all'apertura del form.
	 */
	public void requestFocusForData() {
		PanjeaSwingUtil.giveFocusToComponent(new Component[] { jDateChooser });
	}
}
