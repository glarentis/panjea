package it.eurotn.panjea.beniammortizzabili.rich.forms.bene;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.FigliBeneAmmortizzabileTablePage;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.Application;

public class FigliBeneAmmortizzabileForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "figliBeneAmmortizzabileForm";

	private FigliBeneAmmortizzabileTablePage page;

	/**
	 * Costruttore di default.
	 * 
	 * @param formModel
	 *            form model
	 */
	public FigliBeneAmmortizzabileForm(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		page = (FigliBeneAmmortizzabileTablePage) Application.instance().getApplicationContext()
				.getBean("figliBeneAmmortizzabileTablePage");

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(page.getControl());

		// al cambio dell'oggetto del form carico i suoi figli
		addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				page.setFormObject(evt.getNewValue());
				page.loadData();
			}
		});
		page.setFormObject(getFormObject());
		page.loadData();

		return panel;
	}
}
