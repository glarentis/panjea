package it.eurotn.panjea.bi.rich.editors.dashboard.commands;

import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.rich.bd.BusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.bd.JasperBIManager;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardCompositePage;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.report.rest.ClientConfig;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingWorker;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 *
 */
public class ExportToJrxmlCommand extends ApplicationWindowAwareCommand {

	private class DashboardForm extends PanjeaAbstractForm {

		private static final String FORM_ID = "analisiBiForm";
		private JasperBIManager biManager;

		/**
		 * Costruttore.
		 *
		 * @param datawarehouseBD
		 * @param dashBoard
		 */
		public DashboardForm(final DashBoard dashBoard) {
			super(PanjeaFormModelHelper.createFormModel(dashBoard, false, FORM_ID), FORM_ID);
			ClientConfig config = new ClientConfig();
			config.setPassword("jasperadmin");
			config.setUserName("jasperadmin");
			config.setUrl("localhost");
			config.setPort("8080");
			biManager = new JasperBIManager(config);
		}

		@Override
		protected JComponent createFormControl() {
			final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
			FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref:grow, default", "10dlu,default");
			FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
			builder.setLabelAttributes("r, t");
			builder.nextRow();
			builder.setRow(2);

			ValueHolder vhTemplate = new ValueHolder(retrieveTemplate());

			builder.addLabel("Template", 1);
			// builder.addBinding((bf.createBoundComboBox("pathTemplateJRXML", vhTemplate)), 3);
			builder.nextRow();
			builder.addLabel("InputControl", 1);
			builder.nextRow();
			// builder.addBinding(bf.createBoundShuttleList("inputControls", retrieveInputControl()), 1, 6, 3, 1);
			return builder.getPanel();
		}

		/**
		 * @return Returns the biManager.
		 */
		public JasperBIManager getBiManager() {
			return biManager;
		}

		private List<String> retrieveInputControl() {
			SwingWorker<List<String>, Void> work = new SwingWorker<List<String>, Void>() {
				@Override
				protected List<String> doInBackground() throws Exception {
					List<ResourceDescriptor> resources = biManager.retrieveInputControls();
					List<String> result = new ArrayList<String>();
					DashBoard dashboard = (DashBoard) getFormObject();
					for (ResourceDescriptor resource : resources) {
						String fieldControl = resource.getName().split("_")[1];
					}
					return result;
				}
			};
			work.execute();
			try {
				return work.get();
			} catch (Exception e) {
				logger.error("-->errore nel recuperare inputContrl dei report", e);
				throw new RuntimeException("-->errore nel recuperare inputControl dei report", e);
			}
		}

		private Set<String> retrieveTemplate() {
			SwingWorker<Set<String>, Void> work = new SwingWorker<Set<String>, Void>() {
				@Override
				protected Set<String> doInBackground() throws Exception {
					return biManager.retrieveTemplateFiles();
				}
			};
			work.execute();
			try {
				return work.get();
			} catch (Exception e) {
				logger.error("-->errore nel recuperare i template dei report", e);
				throw new RuntimeException("-->errore nel recuperare i template dei report", e);
			}
		}
	}

	private class InputControlIdRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 9031498388730439136L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel component = (JLabel) super
					.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			component.setText(value.toString());
			return component;
		}
	}

	public static final String COMMAND_ID = "dwExportToJrxmlCommand";
	private DashBoardCompositePage dashBoardPage;

	/**
	 * Costruttore di default.
	 *
	 * @param dashBoardPage
	 *            controller
	 */
	public ExportToJrxmlCommand(final DashBoardCompositePage dashBoardPage) {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
		this.dashBoardPage = dashBoardPage;
	}

	@Override
	protected void doExecuteCommand() {

		final DashboardForm analisiBiForm = new DashboardForm(dashBoardPage.getDashBoard());
		PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(analisiBiForm, Application
				.instance().getActiveWindow().getControl()) {
			@Override
			protected boolean onFinish() {
				analisiBiForm.commit();
				final DashBoard dashboard = (DashBoard) analisiBiForm.getFormObject();
				final JasperBIManager biManager = analisiBiForm.getBiManager();
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						try {
							String templateContent = biManager.retrieveContentTemplateFile("default");
							IBusinessIntelligenceBD bd = RcpSupport.getBean(BusinessIntelligenceBD.BEAN_ID);
							String mainJrxml = bd.creaJrxml(dashboard, templateContent);
							biManager.putAnalisi(dashboard.getNome(), dashboard.getCategoria(),
									dashboard.getDescrizione(), mainJrxml, new ArrayList<String>());
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
				};
				worker.execute();
				return true;
			}
		};
		boolean filtriVisible = dashBoardPage.isFitriVisible();
		if (filtriVisible) {
			dashBoardPage.visualizzaFiltri();
		}
		dashBoardPage.salva();
		dialog.showDialog();
		if (filtriVisible) {
			dashBoardPage.visualizzaFiltri();
		}
	}
}
