package it.eurotn.panjea.centricosto.rich.forms;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.springframework.binding.form.ConfigurableFormModel;
import org.springframework.binding.form.support.DefaultFormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.BufferedCollectionValueModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ListBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;

public class CentroCostoForm extends PanjeaAbstractForm {

	private class SottoContoRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 497293379853562031L;

		private Icon sottoContoIcon = RcpSupport.getIcon(SottoConto.class.getName());

		@Override
		public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1) {
			super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
			setText(ObjectConverterManager.toString(obj));
			setIcon(sottoContoIcon);
			return this;
		}
	}

	private static final String FORM_ID = "centroCostoForm";
	private static final String FORM_MODEL_ID = "centroCostoFormModel";

	/**
	 * Costruttore.
	 */
	public CentroCostoForm() {
		super(PanjeaFormModelHelper.createFormModel(new CentroCosto(), false, FORM_MODEL_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:default",
				"2dlu,default,2dlu,default,2dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);
		builder.addPropertyAndLabel("codice");
		builder.nextRow();
		builder.addPropertyAndLabel("descrizione");
		builder.nextRow();
		builder.setLabelAttributes("r, t");
		builder.addLabel("sottoConti");
		JScrollPane scrollPane = getComponentFactory().createScrollPane(getSottoContiBinding().getControl());
		builder.addComponent(scrollPane, 3);
		return builder.getPanel();
	}

	/**
	 * 
	 * @return binding per i sottoconti
	 */
	private Binding getSottoContiBinding() {
		final ConfigurableFormModel formModel = (getFormModel());
		ValueModel valueModel = formModel.getValueModel("sottoConti");
		valueModel = new BufferedCollectionValueModel(
				(((DefaultFormModel) formModel).getFormObjectPropertyAccessStrategy())
						.getPropertyValueModel("sottoConti"),
				formModel.getFieldMetadata("sottoConti").getPropertyType());
		formModel.add("sottoConti", valueModel);
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		ListBinding binding = (ListBinding) bf.createBoundList("sottoConti", valueModel.getValue());
		binding.setRenderer(new SottoContoRenderer());
		return binding;
	}
}
