/**
 * 
 */
package it.eurotn.panjea.agenti.rich.editors.settings;

import it.eurotn.panjea.agenti.domain.AgentiSettings;
import it.eurotn.panjea.agenti.domain.BaseProvvigionaleStrategy;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class AgentiSettingsForm extends PanjeaAbstractForm {

	private class StrategyPropertyChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			AgentiSettings agentiSettings = (AgentiSettings) getFormObject();
			BaseProvvigionaleStrategy strategy = agentiSettings.getBaseProvvigionaleSettings()
					.getBaseProvvigionaleStrategy();

			boolean listinoVisible = strategy != null && strategy == BaseProvvigionaleStrategy.LISTINO;

			listinoLabel.setVisible(listinoVisible);
			listinoSearch.setVisible(listinoVisible);

			if (!listinoVisible) {
				getFormModel().getValueModel("baseProvvigionaleSettings.listino").setValue(null);
			}
		}

	}

	public static final String FORM_ID = "agentiSettingsForm";

	private JLabel listinoLabel;
	private JComponent listinoSearch;

	private StrategyPropertyChangeListener strategyPropertyChangeListener;

	/**
	 * Costruttore.
	 */
	public AgentiSettingsForm() {
		super(PanjeaFormModelHelper.createFormModel(new AgentiSettings(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addPropertyAndLabel("baseProvvigionaleSettings.baseProvvigionaleStrategy", 1);
		builder.nextRow();
		listinoLabel = builder.addLabel("listino", 1);
		Binding bindingListino = bf.createBoundSearchText("baseProvvigionaleSettings.listino",
				new String[] { "codice" });
		((SearchPanel) bindingListino.getControl()).getTextFields().get("codice").setColumns(15);
		listinoSearch = builder.addBinding(bindingListino, 3);

		addFormObjectChangeListener(getStrategyPropertyChangeListener());
		getValueModel("baseProvvigionaleSettings.baseProvvigionaleStrategy").addValueChangeListener(
				getStrategyPropertyChangeListener());

		return builder.getPanel();
	}

	@Override
	public void dispose() {
		removeFormObjectChangeListener(getStrategyPropertyChangeListener());
		getValueModel("baseProvvigionaleSettings.baseProvvigionaleStrategy").removeValueChangeListener(
				getStrategyPropertyChangeListener());

		strategyPropertyChangeListener = null;

		super.dispose();
	}

	/**
	 * @return the strategyPropertyChangeListener
	 */
	private StrategyPropertyChangeListener getStrategyPropertyChangeListener() {
		if (strategyPropertyChangeListener == null) {
			strategyPropertyChangeListener = new StrategyPropertyChangeListener();
		}

		return strategyPropertyChangeListener;
	}

}
