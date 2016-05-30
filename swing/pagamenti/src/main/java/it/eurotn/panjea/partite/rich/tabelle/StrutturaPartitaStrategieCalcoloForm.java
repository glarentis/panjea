package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartita;
import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartitaConti;
import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartitaFormula;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.rich.tabelle.righestruttura.StrategiaCalcoloPartitaComponent;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

public class StrutturaPartitaStrategieCalcoloForm extends PanjeaAbstractForm implements PropertyChangeListener {

	private static Logger logger = Logger.getLogger(StrutturaPartitaStrategieCalcoloForm.class.getName());
	private static final String FORM_ID = "strutturaPartiteStrategieCalcoloForm";
	private RefreshableValueHolder strategieValueHolder1;
	private RefreshableValueHolder strategieValueHolder2;

	/**
	 * Costruttore.
	 */
	public StrutturaPartitaStrategieCalcoloForm() {
		super(PanjeaFormModelHelper.createFormModel(new StrutturaPartita(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		initForm();

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,default,default,default,default,default,default,default, fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addHorizontalSeparator("stategieCalcoloPartita", 8);
		builder.nextRow();
		builder.addLabel("primaStrategiaCalcoloPartita", 1, 17);
		builder.addBinding(
				bf.createBoundComboBox("primaStrategiaCalcoloPartita", strategieValueHolder1, "codiceStrategia"), 3, 17);
		builder.addLabel("secondaStrategiaCalcoloPartita", 5, 17);
		builder.addBinding(
				bf.createBoundComboBox("secondaStrategiaCalcoloPartita", strategieValueHolder2, "codiceStrategia"), 7,
				17);
		builder.nextRow();

		builder.addComponent(
				getStrategiaCalcoloPartitaComponent("primaStrategiaCalcoloPartita", strategieValueHolder1), 1, 18, 3, 1);
		builder.addComponent(
				getStrategiaCalcoloPartitaComponent("secondaStrategiaCalcoloPartita", strategieValueHolder2), 5, 18, 3,
				1);

		return builder.getPanel();
	}

	/**
	 * Crea il componente per la strategia di calcolo.
	 * 
	 * @param property
	 *            property
	 * @param valueHolder
	 *            valueHolder
	 * @return componente creato
	 */
	private JComponent getStrategiaCalcoloPartitaComponent(String property, ValueHolder valueHolder) {
		ValueModel vm = this.getFormModel().getValueModel(property);
		JComponent component = new StrategiaCalcoloPartitaComponent(valueHolder, vm, getFormModel()).getControl();
		return component;
	}

	/**
	 * Inizializza i componenti del form.
	 */
	private void initForm() {
		logger.debug("--> Enter initForm");
		addFormObjectChangeListener(this);

		strategieValueHolder1 = new RefreshableValueHolder(new Closure() {

			@Override
			public Object call(Object arg0) {
				logger.debug("--> Enter call per strategieValueHolder1");
				StrategiaCalcoloPartita[] strategie = new StrategiaCalcoloPartita[2];
				strategie[0] = new StrategiaCalcoloPartitaFormula();
				strategie[1] = new StrategiaCalcoloPartitaConti();
				logger.debug("--> La strategia nel formModel è "
						+ StrutturaPartitaStrategieCalcoloForm.this.getFormModel()
								.getValueModel("primaStrategiaCalcoloPartita").getValue());
				if (StrutturaPartitaStrategieCalcoloForm.this.getFormModel()
						.getValueModel("primaStrategiaCalcoloPartita").getValue() != null) {
					for (int i = 0; i < strategie.length; i++) {
						String codiceStrategia = ((StrategiaCalcoloPartita) (StrutturaPartitaStrategieCalcoloForm.this
								.getFormModel().getValueModel("primaStrategiaCalcoloPartita").getValue())).getCodiceStrategia();
						if (strategie[i].getCodiceStrategia().equals(codiceStrategia)) {
							strategie[i] = (StrategiaCalcoloPartita) StrutturaPartitaStrategieCalcoloForm.this
									.getFormModel().getValueModel("primaStrategiaCalcoloPartita").getValue();
						}
					}
				}
				logger.debug("--> Exit call");
				return strategie;
			}
		}, false, false);

		strategieValueHolder2 = new RefreshableValueHolder(new Closure() {

			@Override
			public Object call(Object arg0) {
				logger.debug("--> Enter call per strategieValueHolder2");
				StrategiaCalcoloPartita[] strategie = new StrategiaCalcoloPartita[2];
				strategie[0] = new StrategiaCalcoloPartitaFormula();
				strategie[1] = new StrategiaCalcoloPartitaConti();

				if (StrutturaPartitaStrategieCalcoloForm.this.getFormModel()
						.getValueModel("secondaStrategiaCalcoloPartita").getValue() != null) {
					for (int i = 0; i < strategie.length; i++) {
						String codiceStrategia = ((StrategiaCalcoloPartita) (StrutturaPartitaStrategieCalcoloForm.this
								.getFormModel().getValueModel("secondaStrategiaCalcoloPartita").getValue())).getCodiceStrategia();
						if (strategie[i].getCodiceStrategia().equals(codiceStrategia)) {
							strategie[i] = (StrategiaCalcoloPartita) StrutturaPartitaStrategieCalcoloForm.this
									.getFormModel().getValueModel("secondaStrategiaCalcoloPartita").getValue();
						}
					}
				}
				logger.debug("--> Exit call");
				return strategie;
			}
		}, false, false);

		logger.debug("--> Exit initForm");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange per la proprietà " + evt.getPropertyName());
		strategieValueHolder1.refresh();
		strategieValueHolder2.refresh();
		logger.debug("--> Exit propertyChange");
	}
}
