package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

public class ParametriRicercaSituazioneEPForm extends PanjeaAbstractForm {

	protected class CheckChangeListener implements PropertyChangeListener {

		private final ParametriRicercaSituazioneEPForm this$0;

		/**
		 * Costruttore.
		 */
		public CheckChangeListener() {
			this$0 = ParametriRicercaSituazioneEPForm.this;
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

	private static final String FORM_ID = "parametriRicercaSituazioneEPForm";
	private AziendaCorrente aziendaCorrente = null;
	private ParametriRicercaSituazioneEP parametriRicercaSituazioneEP = null;
	private JDateChooser jDateChooser = null;

	private PeriodoParametriChangeListener periodoParametriChangeListener = null;

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaSituazioneEP
	 *            parametri ricerca per la situazione economica patrimoniale
	 * @param aziendaCorrente
	 *            azienda corrente
	 */
	public ParametriRicercaSituazioneEPForm(final ParametriRicercaSituazioneEP parametriRicercaSituazioneEP,
			final AziendaCorrente aziendaCorrente) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaSituazioneEP, false, FORM_ID), FORM_ID);
		this.aziendaCorrente = aziendaCorrente;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:80dlu,4dlu,left:default, 10dlu, right:80dlu,4dlu,left:default, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		((JTextField) builder.addPropertyAndLabel("annoCompetenza")[1]).setColumns(4);
		builder.nextRow();

		JPanel panel = (JPanel) builder.addPropertyAndLabel("dataRegistrazione", 1, 4, 9, 1)[1];
		jDateChooser = (JDateChooser) panel.getComponents()[1];
		builder.nextRow();

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
			builder.addLabel("centroCosto", 1);

			Binding bindingCentroCosto = bf.createBoundSearchText("centroCosto", null);
			builder.addBinding(bindingCentroCosto, 3, 6, 8, 1);
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
		builder.addBinding(bindingStato, 3, 10, 9, 1);

		periodoParametriChangeListener = new PeriodoParametriChangeListener();
		periodoParametriChangeListener.setPeriodoValueModel(getValueModel("dataRegistrazione"));

		addFormValueChangeListener("annoCompetenza", periodoParametriChangeListener);
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		parametriRicercaSituazioneEP = new ParametriRicercaSituazioneEP();
		Set<StatoAreaContabile> stati = new HashSet<StatoAreaContabile>();
		stati.add(StatoAreaContabile.CONFERMATO);
		stati.add(StatoAreaContabile.VERIFICATO);
		parametriRicercaSituazioneEP.setStatiAreaContabile(stati);
		parametriRicercaSituazioneEP.setAnnoCompetenza(aziendaCorrente.getAnnoContabile());
		parametriRicercaSituazioneEP.getDataRegistrazione().setDataIniziale(aziendaCorrente.getDataInizioEsercizio());
		parametriRicercaSituazioneEP.getDataRegistrazione().setDataFinale(aziendaCorrente.getDataFineEsercizio());

		return parametriRicercaSituazioneEP;
	}

	/**
	 * Indica a quale component dare il focus all'apertura del form.
	 */
	public void requestFocusForData() {
		PanjeaSwingUtil.giveFocusToComponent(new Component[] { jDateChooser });
	}
}
