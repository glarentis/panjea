package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.magazzino.domain.AttributoRiga;

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

public class AttributiRigaArticoloBinder extends AbstractBinder {

	/**
	 * Costruttoer di default.
	 */
	public AttributiRigaArticoloBinder() {
		super(AttributoRiga.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected JComponent createControl(Map context) {
		JPanel panel = getComponentFactory().createPanel();
		return panel;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Binding doBind(JComponent component, FormModel formModel, String property, Map context) {
		return new AttributiRigaArticoloBinding(component, formModel, property, List.class);
	}

	// @SuppressWarnings("unchecked")
	// public void propertyChange(PropertyChangeEvent evt) {
	// Set<AttributoRiga> attributi = (Set<AttributoRiga>) getFormModel().getValueModel("attributi").getValue();
	// ComponentFactory cf = getComponentFactory();
	// rootPanel.removeAll();
	// for (AttributoRiga attributoRiga : attributi) {
	// JTextField controlloAttributo = cf.createTextField();
	// controlloAttributo.setColumns(5);
	// controlloAttributo.setText(new Double(attributoRiga.getValore()).toString());
	// controlloAttributo.setEditable(attributoRiga.isUpdatable());
	// // formComponentInterceptorFactory
	// FormComponentInterceptorFactory formComponentInterceptorFactory = (FormComponentInterceptorFactory) Application
	// .services().getService(FormComponentInterceptorFactory.class);
	//
	// JLabel nomeAttributo = cf.createLabelFor(attributoRiga.getTipoAttributo().getNome(), controlloAttributo);
	// nomeAttributo.setText(nomeAttributo.getText() + "["
	// + attributoRiga.getTipoAttributo().getUnitaMisura().getCodice() + "]");
	// rootPanel.add(nomeAttributo);
	// rootPanel.add(controlloAttributo);
	// }
	// }
}
