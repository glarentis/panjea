package it.eurotn.panjea.beniammortizzabili.rich.forms.simulazione;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.PoliticheCalcoloCivilisticheWizardPage;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;

public class PoliticheCalcoloCivilisticheWizardForm extends PanjeaAbstractForm implements Memento {

	public static final String FORM_ID = "politicheCalcoloCivilisticheWizardForm";
	public static final String FORMMODEL_ID = "politicheCalcoloCivilisticheWizardFormModel";

	private PoliticheCalcoloCivilisticheWizardPage politicheCalcoloCivilistichePage = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param formModel
	 *            formModel della simulazione di riferimento.
	 */
	public PoliticheCalcoloCivilisticheWizardForm(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		politicheCalcoloCivilistichePage = new PoliticheCalcoloCivilisticheWizardPage((Simulazione) getFormObject());

		addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				politicheCalcoloCivilistichePage.updateTableData((Simulazione) evt.getNewValue());
			}
		});

		return politicheCalcoloCivilistichePage.getControl();

	}

	@Override
	public void restoreState(Settings settings) {
		politicheCalcoloCivilistichePage.restoreState(settings);

	}

	@Override
	public void saveState(Settings settings) {
		politicheCalcoloCivilistichePage.saveState(settings);
	}

}
