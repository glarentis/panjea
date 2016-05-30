package it.eurotn.panjea.agenti.rich.editors.entita;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.editors.entita.anagrafica.AnagraficaForm;
import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoEnasarco;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableBinding;

public class AgenteForm extends AnagraficaForm {

	private class CapoAreaChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent propertychangeevent) {
			Boolean capoArea = (Boolean) getValueModel("capoArea").getValue();
			if (capoArea != null) {
				separator.setVisible(capoArea);
				agenteTableBinding.getControl().setVisible(capoArea);
			}
		}

	}

	private JComponent separator;
	private TableBinding<?> agenteTableBinding;

	private final PluginManager pluginManager;

	/**
	 * Costruttore.
	 *
	 * @param anagraficaBD
	 *            anagrafica
	 */
	public AgenteForm(final IAnagraficaBD anagraficaBD) {
		super(new Agente(), anagraficaBD);
		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		super.createFormControl();

		if (pluginManager.isPresente(PluginManager.PLUGIN_CONTABILITA)) {
			builder.nextRow();
			builder.addHorizontalSeparator("Dati per ritenute d'acconto", 11);
			builder.nextRow();

			builder.addLabel("datiRitenutaAcconto.causaleRitenutaAcconto", 1, 54);
			ComboBoxBinding bindingCausale = (ComboBoxBinding) bf.createBoundComboBox(
					"datiRitenutaAcconto.causaleRitenutaAcconto",
					new ValueHolder(anagraficaBD.caricaCausaliRitenuteAcconto()), "descrizione");
			CausaleRitenutaAcconto causaleRitenutaAccontoEmpty = new CausaleRitenutaAcconto();
			causaleRitenutaAccontoEmpty.setDescrizione(" Nessuna");
			bindingCausale.setEmptySelectionValue(causaleRitenutaAccontoEmpty);
			builder.addBinding(bindingCausale, 3, 54, 9, 1);
			builder.nextRow();

			builder.addLabel("datiRitenutaAcconto.contributoPrevidenziale", 1, 56);
			ComboBoxBinding bindingContributo = (ComboBoxBinding) bf.createBoundComboBox(
					"datiRitenutaAcconto.contributoPrevidenziale",
					new ValueHolder(anagraficaBD.caricaContributiEnasarco()), "codice");
			ContributoPrevidenziale contributoPrevidenzialeEmpty = new ContributoEnasarco();
			contributoPrevidenzialeEmpty.setCodice(" Nessuno");
			bindingContributo.setEmptySelectionValue(contributoPrevidenzialeEmpty);
			builder.addBinding(bindingContributo, 3, 56, 2, 1);

			builder.addPropertyAndLabel("tipoMandato", 9);
		}

		builder.setRow(58);
		builder.addHorizontalSeparator(11);
		builder.nextRow();
		builder.addPropertyAndLabel("fatturazioneAgente", 1);
		builder.nextRow();
		builder.addPropertyAndLabel("capoArea", 1);
		builder.nextRow();
		separator = getComponentFactory().createLabeledSeparator("Agenti");
		builder.addComponent(separator, 1, 66, 8, 1);
		AgenteCollegatoForm agenteCollegatoForm = new AgenteCollegatoForm(anagraficaBD, this);
		builder.setComponentAttributes("l,t");
		agenteTableBinding = (TableBinding<?>) bf.createTableBinding("agenti", 100, new AgentiCollegatiTableModel(),
				agenteCollegatoForm);
		builder.addBinding(agenteTableBinding, 3, 70, 5, 1);
		installListener();
		return builder.getPanel();
	}

	/**
	 * installa i listener per il form.
	 */
	private void installListener() {
		CapoAreaChangeListener listener = new CapoAreaChangeListener();
		getValueModel("capoArea").addValueChangeListener(listener);
		listener.propertyChange(null);
	}
}
