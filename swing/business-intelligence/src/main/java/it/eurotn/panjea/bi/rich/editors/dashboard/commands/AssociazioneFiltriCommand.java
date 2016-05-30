/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard.commands;

import it.eurotn.panjea.bi.domain.dashboard.DashBoardAnalisi;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DualListBinding;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.GuiStandardUtils;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.pivot.PivotField;

/**
 * @author fattazzo
 *
 */
public class AssociazioneFiltriCommand extends ActionCommand {

	private class AssociazioneFiltriForm extends PanjeaAbstractForm {

		private static final String FORM_ID = "associazioneFiltriForm";

		/**
		 * Costruttore.
		 *
		 * @param associazioneFiltriBi
		 *            associazione filtri
		 */
		public AssociazioneFiltriForm(final DashBoardAnalisi dashBoardAnalisi) {
			super(PanjeaFormModelHelper.createFormModel(dashBoardAnalisi, false, FORM_ID), FORM_ID);
		}

		@Override
		protected JComponent createFormControl() {
			final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
			FormLayout layout = new FormLayout("left:pref", "default,2dlu,default,10dlu,default,2dlu,default");
			FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanelNumbered());
			builder.setLabelAttributes("r, c");

			// builder.addPropertyAndLabel("applyFilter");
			// builder.nextRow();
			builder.addHorizontalSeparator("Campi da filtrare");
			builder.nextRow();
			List<String> pivotToFilter = new ArrayList<String>();
			for (PivotField pivotField : page.getPivotDataModel().getFilterFields()) {
				pivotToFilter.add(pivotField.getName());
			}
			for (PivotField pivotField : page.getPivotDataModel().getColumnFields()) {
				pivotToFilter.add(pivotField.getName());
			}
			for (PivotField pivotField : page.getPivotDataModel().getRowFields()) {
				pivotToFilter.add(pivotField.getName());
			}

			DualListBinding bindingDualListFiltriDaFiltrare = (DualListBinding) bf.createBoundShuttleList(
					"associazioneFiltriApplyFields", pivotToFilter);
			bindingDualListFiltriDaFiltrare.setRenderer(new FieldListCellRender());
			builder.addBinding(bindingDualListFiltriDaFiltrare, 1);
			builder.nextRow();

			builder.addHorizontalSeparator("Associazioni");
			builder.nextRow();
			DualListBinding bindingDualListFiltriDaAssociare = (DualListBinding) bf.createBoundShuttleList(
					"associazioneFiltriFields", getAnalisiFields());
			bindingDualListFiltriDaAssociare.setRenderer(new FieldListCellRender());
			builder.addBinding(bindingDualListFiltriDaAssociare, 1);

			JPanel panel = builder.getPanel();
			GuiStandardUtils.attachBorder(panel);
			return panel;
		}
	}

	@SuppressWarnings("serial")
	private class FieldListCellRender extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel component = (JLabel) super
					.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			PivotField field = page.getPivotDataModel().getField(value.toString());
			if (field != null) {
				component.setText(field.getTitle());
			}
			return component;
		}
	}

	private final DashBoardPage page;

	public static final String COMMAND_ID = "DWAssociazioneFiltriCommand";

	private AssociazioneFiltriForm associazioneFiltriForm;

	/**
	 * Costruttore.
	 *
	 * @param page
	 *            pagina legata al comando
	 */
	public AssociazioneFiltriCommand(final DashBoardPage page) {
		super(COMMAND_ID);
		this.page = page;
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		associazioneFiltriForm = new AssociazioneFiltriForm(page.getAnalisi());
		PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(associazioneFiltriForm, null) {

			@Override
			protected boolean isMessagePaneVisible() {
				return false;
			}

			@Override
			protected boolean onFinish() {
				associazioneFiltriForm.commit();
				return true;
			}
		};
		dialog.showDialog();
	}

	private Set<String> getAnalisiFields() {
		PivotField[] colFields = page.getPivotDataModel().getColumnFields();
		PivotField[] rowFields = page.getPivotDataModel().getRowFields();

		Set<String> results = new HashSet<String>();
		for (PivotField pivotField : colFields) {
			results.add(pivotField.getName());
		}
		for (PivotField pivotField : rowFields) {
			results.add(pivotField.getName());
		}

		return results;
	}

}
