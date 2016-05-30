package it.eurotn.panjea.conai.rich.editor.articolo;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiTipoImballo;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.text.DefaultFormatterFactory;

import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

public class ConaiComponenteForm extends PanjeaAbstractForm {

	public class MaterialeChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			getTipiImballo().refresh();
		}

	}

	public static final String FORM_ID = "conaiComponenteForm";
	private RefreshableValueHolder tipiImballoValueHolder;
	private MaterialeChangeListener materialeChangeListener;

	/**
	 * Costruttore.
	 */
	public ConaiComponenteForm() {
		super(PanjeaFormModelHelper.createFormModel(new ConaiComponente(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,fill:120dlu,10dlu,right:pref,4dlu,left:pref,10dlu,fill:default:grow", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);
		builder.addPropertyAndLabel("materiale", 1);
		builder.nextRow();
		builder.addLabel("tipoImballo");
		builder.addBinding(bf.createBoundComboBox("tipoImballo", getTipiImballo()), 3);
		builder.nextRow();
		builder.addLabel("pesoUnitario", 1);
		builder.addBinding(bf.createBoundFormattedTextField("pesoUnitario", getFactory(6)), 3);
		builder.nextRow();
		builder.addLabel("pesoUnitarioKG", 1);
		builder.addBinding(bf.createBoundFormattedTextField("pesoUnitarioKG", getFactory(3)), 3);
		materialeChangeListener = new MaterialeChangeListener();
		getValueModel("materiale").addValueChangeListener(materialeChangeListener);
		return builder.getPanel();
	}

	/**
	 * @param numeroDecimali
	 *            numeroDecimali
	 * @return DefaultFormatterFactory
	 */
	private DefaultFormatterFactory getFactory(Integer numeroDecimali) {
		DefaultFormatterFactory factory = new it.eurotn.panjea.util.DefaultNumberFormatterFactory("###,###,###,##0",
				numeroDecimali, BigDecimal.class);
		return factory;
	}

	/**
	 * @return restituisce la lista di TipoImballo per materiale
	 */
	private RefreshableValueHolder getTipiImballo() {
		if (tipiImballoValueHolder == null) {
			tipiImballoValueHolder = new RefreshableValueHolder(new Closure() {

				@Override
				public Object call(Object argument) {
					ConaiMateriale materiale = (ConaiMateriale) getValue("materiale");
					if (materiale == null) {
						return new ConaiTipoImballo[0];
					}
					return materiale.getTipiImballo();
				}
			});
			tipiImballoValueHolder.refresh();
		}
		return tipiImballoValueHolder;
	}
}
