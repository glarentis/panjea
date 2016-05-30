/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.CustomEnumListRenderer;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

/**
 * 
 * 
 * @author fattazzo
 * @version 1.0, 16/apr/07
 * 
 */
public class ContoForm extends PanjeaAbstractForm implements PropertyChangeListener {

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

	private static Logger logger = Logger.getLogger(ContoForm.class);

	private static final String FORM_ID = "contoForm";

	private JComboBox comboSottoTipoConto;

	private Binding sottotipoContoBinding;

	private PluginManager pluginManager;

	private CentroCostoPropertyChange centroCostoPropertyChange;

	/**
	 * Costruttore.
	 * 
	 * @param conto
	 *            conto da gestire
	 */
	public ContoForm(final Conto conto) {
		super(PanjeaFormModelHelper.createFormModel(conto, false, FORM_ID), FORM_ID);
		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	/**
	 * Bind della proprietà tipo conto.
	 * 
	 * @param tipoConto
	 *            tipo conto
	 */
	private void createComboBoxBinding(TipoConto tipoConto) {
		if (tipoConto == null) {
			sottotipoContoBinding = ((PanjeaSwingBindingFactory) getBindingFactory()).createBoundComboBox(
					"sottotipoConto", getListForAll());
		} else {
			switch (tipoConto) {
			case PATRIMONIALE:
				sottotipoContoBinding = ((PanjeaSwingBindingFactory) getBindingFactory()).createBoundComboBox(
						"sottotipoConto", getListForPatrimoniale());
				break;
			case ECONOMICO:
				sottotipoContoBinding = ((PanjeaSwingBindingFactory) getBindingFactory()).createBoundComboBox(
						"sottotipoConto", getListForEconomico());
				break;
			case ORDINE:
				sottotipoContoBinding = ((PanjeaSwingBindingFactory) getBindingFactory()).createBoundComboBox(
						"sottotipoConto", getListForORDINE());
				break;
			default:
				sottotipoContoBinding = ((PanjeaSwingBindingFactory) getBindingFactory()).createBoundComboBox(
						"sottotipoConto", getListForAll());
				break;
			}
		}
		MessageSourceAccessor messageSourceAccessor = getMessages();
		((ComboBoxBinding) sottotipoContoBinding).setRenderer(new CustomEnumListRenderer(messageSourceAccessor));
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
		builder.add("tipoConto", "align=left");
		builder.row();
		comboSottoTipoConto = (JComboBox) builder.add("sottotipoConto", "align=left")[1];
		valorizzaComboSottoTipoConto(((Conto) getFormObject()).getTipoConto());
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

		// mi registro al listener perche' i valori della proprieta' sottotipoConto sono
		// legati alla proprietà tipoConto
		this.addFormValueChangeListener("tipoConto", this);

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

	private ValueHolder getListForAll() {
		List<SottotipoConto> list = new ArrayList<SottotipoConto>();
		ValueHolder holder = new ValueHolder();
		holder.setValue(list);
		return holder;
	}

	private ValueHolder getListForEconomico() {
		List<SottotipoConto> list = new ArrayList<SottotipoConto>();
		list.add(SottotipoConto.COSTO);
		list.add(SottotipoConto.RICAVO);
		ValueHolder holder = new ValueHolder();
		holder.setValue(list);
		return holder;
	}

	private ValueHolder getListForORDINE() {
		List<SottotipoConto> list = new ArrayList<SottotipoConto>();
		ValueHolder holder = new ValueHolder();
		holder.setValue(list);
		return holder;
	}

	private ValueHolder getListForPatrimoniale() {
		List<SottotipoConto> list = new ArrayList<SottotipoConto>();
		list.add(SottotipoConto.VUOTO);
		list.add(SottotipoConto.CLIENTE);
		list.add(SottotipoConto.FORNITORE);
		ValueHolder holder = new ValueHolder();
		holder.setValue(list);
		return holder;
	}

	@Override
	public void propertyChange(PropertyChangeEvent prop) {
		logger.debug("--> Entro nel property change della proprieta': " + prop);

		valorizzaComboSottoTipoConto((TipoConto) prop.getNewValue());
	}

	private void valorizzaComboSottoTipoConto(TipoConto tipoConto) {

		// setto a null il valore della proprieta' sottotipoConto prima di cambiare il tipoConto
		getValueModel("sottotipoConto").setValue(null);

		if (tipoConto == null) {
			createComboBoxBinding(null);
		} else {
			switch (tipoConto) {
			case PATRIMONIALE:
				createComboBoxBinding(TipoConto.PATRIMONIALE);
				break;
			case ECONOMICO:
				createComboBoxBinding(TipoConto.ECONOMICO);
				break;
			case ORDINE:
				createComboBoxBinding(TipoConto.ORDINE);
				break;
			default:
				createComboBoxBinding(null);
				break;
			}
		}
		comboSottoTipoConto.setModel(((JComboBox) sottotipoContoBinding.getControl()).getModel());
		// se ci sono valori nella combo viene selezionato il primo. vedi bug 146
		// "ins/upd conto: sottotipo conto non sempre richiesto"
		if (comboSottoTipoConto.getModel().getSize() > 0) {
			comboSottoTipoConto.setSelectedIndex(0);
		}
	}

}
