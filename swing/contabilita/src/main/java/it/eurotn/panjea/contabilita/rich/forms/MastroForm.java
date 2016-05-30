/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

/**
 * 
 * 
 * @author fattazzo
 * @version 1.0, 16/apr/07
 * 
 */
public class MastroForm extends PanjeaAbstractForm {

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

	private static Logger logger = Logger.getLogger(MastroForm.class);

	private static final String FORM_ID = "mastroForm";

	private PluginManager pluginManager;

	private CentroCostoPropertyChange centroCostoPropertyChange;

	/**
	 * Costruttore.
	 * 
	 * @param mastro
	 *            mastro
	 */
	public MastroForm(final Mastro mastro) {
		super(PanjeaFormModelHelper.createFormModel(mastro, false, FORM_ID), FORM_ID);
		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form del mastro");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(3);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(30);
		builder.row();
		if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
			builder.row();
			builder.add("soggettoCentroCosto", "align=left");
			builder.row();
			builder.add(bf.createBoundSearchText("centroCosto", null));

			centroCostoPropertyChange = new CentroCostoPropertyChange();
			getFormModel().getValueModel("soggettoCentroCosto").addValueChangeListener(centroCostoPropertyChange);
			getFormModel().addPropertyChangeListener(centroCostoPropertyChange);
		}

		return builder.getForm();
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
