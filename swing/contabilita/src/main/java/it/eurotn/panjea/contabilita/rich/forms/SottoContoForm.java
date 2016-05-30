package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StileSottoConto;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.ColorComboBoxBinder;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * 
 * @author fattazzo
 * @version 1.0, 16/apr/07
 * 
 */
public class SottoContoForm extends PanjeaAbstractForm {

	private class CentroCostoPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {

				boolean enableComponent = !getFormModel().isReadOnly()
						&& ((Boolean) getFormModel().getValueModel("soggettoCentroCosto").getValue());

				getFormModel().getFieldMetadata("centroCosto").setReadOnly(!enableComponent);

				if (!enableComponent && !getFormModel().isReadOnly()) {
					getFormModel().getValueModel("centroCosto").setValue(null);
				}
			}
		}

	}

	private class StileAbilitatoPropertyChange implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent propertychangeevent) {
			boolean abilitato = (Boolean) propertychangeevent.getNewValue();
			getFormModel().getFieldMetadata("stileSaldo.importo").setEnabled(abilitato);
			getFormModel().getFieldMetadata("stileSaldo.condizione").setEnabled(abilitato);
			getFormModel().getFieldMetadata("stileSaldo.backGroundColor").setEnabled(abilitato);
		}
	}

	private static Logger logger = Logger.getLogger(SottoContoForm.class);

	private static final String FORM_ID = "sottoContoForm";
	private static final String FORMMODEL_ID = "sottoContoFormModel";

	private PluginManager pluginManager;

	private CentroCostoPropertyChange centroCostoPropertyChange;

	/**
	 * Costruttore.
	 * 
	 * @param sottoConto
	 *            .
	 */
	public SottoContoForm(final SottoConto sottoConto) {
		super(PanjeaFormModelHelper.createFormModel(sottoConto, false, FORMMODEL_ID), FORM_ID);
		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form del sottoconto");

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,4dlu,fill:default:grow", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		((JTextField) builder.addPropertyAndLabel("codice")[1]).setColumns(6);
		builder.nextRow();
		((JTextField) builder.addPropertyAndLabel("descrizione")[1]).setColumns(20);
		builder.nextRow();
		builder.addPropertyAndLabel("classificazioneConto");
		builder.nextRow();
		builder.addPropertyAndLabel("flagIRAP");
		if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
			builder.nextRow();
			builder.addPropertyAndLabel("soggettoCentroCosto");
			builder.nextRow();
			builder.addLabel("centroCosto");
			SearchPanel searchPanel = (SearchPanel) builder.addBinding(
					bf.createBoundSearchText("centroCosto", new String[] { "codice", "descrizione" }), 3);
			searchPanel.getTextFields().get("codice").setColumns(5);
			searchPanel.getTextFields().get("descrizione").setColumns(10);

			centroCostoPropertyChange = new CentroCostoPropertyChange();
			getFormModel().getValueModel("soggettoCentroCosto").addValueChangeListener(centroCostoPropertyChange);
			getFormModel().addPropertyChangeListener(centroCostoPropertyChange);

			builder.nextRow();
		}

		builder.nextRow();
		builder.addPropertyAndLabel("stileSaldo.abilitato");
		builder.nextRow();
		builder.addLabel("stileSaldo.backGroundColor");
		builder.addBinding(new ColorComboBoxBinder().bind(getFormModel(), "stileSaldo.backGroundColor",
				new HashMap<Object, Object>()), 3);
		builder.nextRow();
		builder.addLabel("stileSaldo.condizione");
		builder.addBinding(bf.createBoundComboBox("stileSaldo.condizione", StileSottoConto.ALL_CONDITION), 3);
		builder.nextRow();
		builder.addPropertyAndLabel("stileSaldo.importo");

		PropertyChangeListener stilePropertyChangeListener = new StileAbilitatoPropertyChange();
		getValueModel("stileSaldo.abilitato").addValueChangeListener(stilePropertyChangeListener);
		boolean abilitato = (Boolean) getValueModel("stileSaldo.abilitato").getValue();
		getFormModel().getFieldMetadata("stileSaldo.importo").setEnabled(abilitato);
		getFormModel().getFieldMetadata("stileSaldo.condizione").setEnabled(abilitato);
		getFormModel().getFieldMetadata("stileSaldo.backGroundColor").setEnabled(abilitato);
		return builder.getPanel();
	}

	@Override
	public void dispose() {
		super.dispose();

		if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
			getFormModel().getValueModel("soggettoCentroCosto").removeValueChangeListener(centroCostoPropertyChange);
			getFormModel().removePropertyChangeListener(centroCostoPropertyChange);
		}
	}

}
