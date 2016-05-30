package it.eurotn.panjea.beniammortizzabili.rich.forms.simulazione;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.PoliticheCalcoloFiscaliWizardPage;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;

public class PoliticheCalcoloFiscaliWizardForm extends PanjeaAbstractForm implements Memento {

	public static final String FORM_ID = "politicheCalcoloFiscaliWizardForm";
	public static final String FORMMODEL_ID = "politicheCalcoloFiscaliWizardFormModel";

	private PoliticheCalcoloFiscaliWizardPage politicheCalcoloFiscaliPage = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param formModel
	 *            formModel della simulazione di riferimento.
	 */
	public PoliticheCalcoloFiscaliWizardForm(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		politicheCalcoloFiscaliPage = new PoliticheCalcoloFiscaliWizardPage((Simulazione) getFormObject());

		addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				politicheCalcoloFiscaliPage.updateTableData((Simulazione) evt.getNewValue());
			}
		});

		return politicheCalcoloFiscaliPage.getControl();

	}

	@Override
	public void restoreState(Settings settings) {
		politicheCalcoloFiscaliPage.restoreState(settings);
	}

	@Override
	public void saveState(Settings settings) {
		politicheCalcoloFiscaliPage.saveState(settings);

	}

}
