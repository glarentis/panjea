package it.eurotn.panjea.magazzino.rich.forms.articolo;

import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.rich.editors.articolo.AttributiArticoloTableModel;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.util.Set;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

import com.jgoodies.forms.layout.FormLayout;

public class ArticoloAttributiForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(ArticoloAttributiForm.class);

	private static final String FORM_ID = "articoloAttributiForm";

	private TableEditableBinding<AttributoArticolo> bindingAttributi;

	/**
	 * Costruttore.
	 *
	 * @param formModel
	 *            formModel
	 */
	public ArticoloAttributiForm(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:default:grow", "fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		AttributiArticoloTableModel attributiArticoloTableModel = new AttributiArticoloTableModel();
		bindingAttributi = new TableEditableBinding<AttributoArticolo>(getFormModel(), "attributiArticolo", Set.class,
				attributiArticoloTableModel);
		builder.addBinding(bindingAttributi, 1, 1);
		SettingsManager manager = (SettingsManager) Application.services().getService(SettingsManager.class);
		try {
			bindingAttributi.getTableWidget().restoreState(manager.getUserSettings());
		} catch (SettingsException e) {
			logger.error("-->errore nel ripristinare lo stato della tabella attributiArticolo", e);
		}

		return builder.getPanel();
	}

	@Override
	public void dispose() {
		super.dispose();
		SettingsManager manager = (SettingsManager) Application.services().getService(SettingsManager.class);
		try {
			bindingAttributi.getTableWidget().saveState(manager.getUserSettings());
		} catch (SettingsException e) {
			logger.error("-->errore nel salvare lo stato della tabella attributiArticolo", e);
		}
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);
	}
}