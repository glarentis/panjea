package it.eurotn.panjea.magazzino.rich.editors.articolo.clone;

import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.rich.editors.articolo.AttributiArticoloTableModel;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.Set;

import javax.swing.JComponent;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

import com.jgoodies.forms.layout.FormLayout;

public class CloneArticoloForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "ArticoloCloneForm";
	public static final String FORMMODEL_ID = "ArticoloCloneFormModel";
	private TableEditableBinding<AttributoArticolo> bindingAttributi;

	/**
	 *
	 * @param parameter
	 *            articoloClone
	 */
	public CloneArticoloForm(final CloneArticoloParameter parameter) {
		super(PanjeaFormModelHelper.createFormModel(parameter, false, FORMMODEL_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:40dlu, right:pref, right:pref,fill:180dlu:g",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,150dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanelNumbered());
		CloneArticoloParameter parameter = (CloneArticoloParameter) getFormObject();
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("nuovoCodice", 1, 2, 4, 1);
		builder.nextRow();
		builder.addPropertyAndLabel("nuovaDescrizione", 1, 4, 4, 1);
		builder.nextRow();
		if (parameter.getArticolo().isDistinta()) {
			builder.addPropertyAndLabel("copyDistinta");
			builder.nextRow();
		}
		builder.addPropertyAndLabel("copyListino");
		builder.addPropertyAndLabel("azzeraPrezziListino", 4);
		builder.nextRow();

		AttributiArticoloTableModel attributiArticoloTableModel = new AttributiArticoloTableModel();
		bindingAttributi = new TableEditableBinding<AttributoArticolo>(getFormModel(), "attributiArticolo", Set.class,
				attributiArticoloTableModel);
		builder.addBinding(bindingAttributi, 1, 10, 6, 1);
		SettingsManager manager = (SettingsManager) Application.services().getService(SettingsManager.class);
		try {
			bindingAttributi.getTableWidget().restoreState(manager.getUserSettings());
		} catch (SettingsException e) {
			logger.error("-->errore nel ripristinare lo stato della tabella attributiArticolo", e);
		}
		return builder.getPanel();
	}

}
