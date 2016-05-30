package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.editors.documento.StampaAreeDocumentoCommand;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.report.ReportManager;

import java.awt.BorderLayout;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.text.NumberFormatter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

public class ContabilizzazioneAreeMagazzinoTablePage extends AbstractTablePageEditor<AreaMagazzinoRicerca> implements
InitializingBean, PropertyChangeListener {

	private class CambiaAnnoContabileCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public CambiaAnnoContabileCommand() {
			super("cambiaAnnoContabileCommand");
			RcpSupport.configure(this);
		}

		@Override
		public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
				CommandButtonConfigurer buttonConfigurer) {
			AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
			return button;
		}

		@Override
		protected void doExecuteCommand() {
			InputApplicationDialog dialog = new InputApplicationDialog("Selezione anno contabile", (Window) null);
			dialog.setInputLabelMessage("Anno contabile");

			JSpinner annoSpinner = new JSpinner(
					new SpinnerNumberModel(aziendaCorrente.getAnnoContabile().intValue(), aziendaCorrente
							.getAnnoContabile().intValue() - 1, aziendaCorrente.getAnnoContabile().intValue(), 1));
			JFormattedTextField txt = ((JSpinner.NumberEditor) annoSpinner.getEditor()).getTextField();
			((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
			dialog.setInputField(annoSpinner);
			dialog.setFinishAction(new Closure() {

				@Override
				public Object call(Object paramObject) {
					parametriFormModel.getValueModel("annoContabile").setValue(paramObject);
					return null;
				}
			});
			dialog.showDialog();
			dialog = null;
		}

	}

	private class CercaAreeMagazzinoCommand extends ActionCommand {

		public static final String COMMAND_ID = "searchCommand";

		/**
		 * Costruttore.
		 *
		 */
		public CercaAreeMagazzinoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			if (parametriFormModel.isCommittable()) {

				ParametriContabilizzazioneWrapper parametri = (ParametriContabilizzazioneWrapper) parametriFormModel
						.getFormObject();

				List<AreaMagazzinoRicerca> aree = magazzinoContabilizzazioneBD.caricaAreeMAgazzinoDaContabilizzare(
						parametri.getTipoGenerazione(), parametri.getAnno());

				setRows(aree);
			}
		}

	}

	private class ContabilizzaAreeMagazzinoCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public void postExecution(ActionCommand command) {

			contabilizzazioneProgressBar.setVisible(false);
			contabilizzazioneProgressLabel.setVisible(false);

			// aggiorno le aree magazzino dopo la contabilizzazione
			getCercaAreeMagazzinoCommand().execute();
		}

		@Override
		public boolean preExecution(ActionCommand command) {

			List<AreaMagazzinoRicerca> aree = getTable().getSelectedObjects();

			if (!aree.isEmpty()) {
				command.addParameter(ContabilizzaAreeMagazzinoCommand.PARAM_AREE_DA_CONTABILIZZARE, aree);

				parametriFormModel.commit();
				int annoContabile = ((ParametriContabilizzazioneWrapper) parametriFormModel.getFormObject())
						.getAnnoContabile();
				command.addParameter(ContabilizzaAreeMagazzinoCommand.PARAM_ANNO_CONTABILE, annoContabile);

				contabilizzazioneProgressBar.setVisible(true);
				contabilizzazioneProgressLabel.setVisible(true);
			}

			return !aree.isEmpty();
		}
	}

	private class OpenAreaMagazzinoEditorCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {

			command.addParameter(OpenAreaMagazzinoEditorCommand.PARAM_AREA_MAGAZZINO_RICERCA, getTable()
					.getSelectedObject());

			return true;
		}

	}

	public static final String PAGE_ID = "contabilizzazioneAreeMagazzinoTablePage";

	public static final String LABAL_CONTABILIZZAZIONE = PAGE_ID + ".labelContabilizzazione";

	private FormModel parametriFormModel;

	private CercaAreeMagazzinoCommand cercaAreeMagazzinoCommand;
	private ContabilizzaAreeMagazzinoCommand contabilizzaAreeMagazzinoCommand;
	private OpenAreaMagazzinoEditorCommand openAreaMagazzinoEditorCommand;
	private CambiaAnnoContabileCommand cambiaAnnoContabileCommand;

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	private StampaAreeDocumentoCommand stampaAreeDocumentoCommand;

	private ActionCommandInterceptor stampaAreeMagazzinoCommandInterceptor;
	private ReportManager reportManager;

	private JProgressBar contabilizzazioneProgressBar = new JProgressBar();

	private JLabel contabilizzazioneProgressLabel = getComponentFactory().createLabel(LABAL_CONTABILIZZAZIONE);

	private AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);

	/**
	 * Costruttore.
	 *
	 */
	protected ContabilizzazioneAreeMagazzinoTablePage() {
		super(PAGE_ID, new ContabilizzazioneAreeMagazzinoTableModel());
		getTable().setPropertyCommandExecutor(getOpenAreaMagazzinoEditorCommand());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ContabilizzazioneAreeMagazzinoMessageDelegate messageDelegate = RcpSupport
				.getBean(ContabilizzazioneAreeMagazzinoMessageDelegate.DELEGATE_ID);
		messageDelegate.addPropertyChangeListener(this);
	}

	/**
	 * @return the cambiaAnnoContabileCommand
	 */
	public CambiaAnnoContabileCommand getCambiaAnnoContabileCommand() {
		if (cambiaAnnoContabileCommand == null) {
			cambiaAnnoContabileCommand = new CambiaAnnoContabileCommand();
		}

		return cambiaAnnoContabileCommand;
	}

	/**
	 * @return the cercaAreeMagazzinoCommand
	 */
	public CercaAreeMagazzinoCommand getCercaAreeMagazzinoCommand() {
		if (cercaAreeMagazzinoCommand == null) {
			cercaAreeMagazzinoCommand = new CercaAreeMagazzinoCommand();
		}

		return cercaAreeMagazzinoCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getContabilizzaAreeMagazzinoCommand(), getStampaAreeDocumentoCommand() };
	}

	/**
	 * @return the contabilizzaAreeMagazzinoCommand
	 */
	public ContabilizzaAreeMagazzinoCommand getContabilizzaAreeMagazzinoCommand() {
		if (contabilizzaAreeMagazzinoCommand == null) {
			contabilizzaAreeMagazzinoCommand = new ContabilizzaAreeMagazzinoCommand();
			contabilizzaAreeMagazzinoCommand.addCommandInterceptor(new ContabilizzaAreeMagazzinoCommandInterceptor());
		}

		return contabilizzaAreeMagazzinoCommand;
	}

	@Override
	public JComponent getFooterControl() {

		JPanel footerPanel = getComponentFactory().createPanel(new BorderLayout(10, 10));

		contabilizzazioneProgressLabel.setVisible(false);
		footerPanel.add(contabilizzazioneProgressLabel, BorderLayout.WEST);

		contabilizzazioneProgressBar.setStringPainted(true);
		contabilizzazioneProgressBar.setVisible(false);
		footerPanel.add(contabilizzazioneProgressBar, BorderLayout.CENTER);

		return footerPanel;
	}

	@Override
	public JComponent getHeaderControl() {
		parametriFormModel = PanjeaFormModelHelper.createFormModel(new ParametriContabilizzazioneWrapper(), false,
				"parametriContabilizzazioneFormModel");

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) Application
				.services().getService(BindingFactoryProvider.class)).getBindingFactory(parametriFormModel);
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:50dlu, 10dlu, right:pref,4dlu,fill:default, 10dlu,right:pref,default:grow,right:pref,left:pref",
				"4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r,c");
		builder.setRow(2);
		builder.addPropertyAndLabel("anno", 1);
		// builder.addBinding(bf.createBinding(Integer.class, "anno"), 3);
		builder.addPropertyAndLabel("tipoGenerazione", 5);
		builder.addComponent(getCercaAreeMagazzinoCommand().createButton(), 9);
		builder.addComponent(getCambiaAnnoContabileCommand().createButton(), 11);
		builder.addBinding(bf.createBoundLabel("annoContabile"), 12);
		builder.nextRow();

		return GuiStandardUtils.attachBorder(builder.getPanel(), BorderFactory.createEmptyBorder(0, 15, 0, 15));
	}

	/**
	 * @return the openAreaMagazzinoEditorCommand
	 */
	public OpenAreaMagazzinoEditorCommand getOpenAreaMagazzinoEditorCommand() {
		if (openAreaMagazzinoEditorCommand == null) {
			openAreaMagazzinoEditorCommand = new OpenAreaMagazzinoEditorCommand();
			openAreaMagazzinoEditorCommand.addCommandInterceptor(new OpenAreaMagazzinoEditorCommandInterceptor());
		}

		return openAreaMagazzinoEditorCommand;
	}

	/**
	 *
	 * @return command
	 */
	public StampaAreeDocumentoCommand getStampaAreeDocumentoCommand() {
		if (stampaAreeDocumentoCommand == null) {
			stampaAreeDocumentoCommand = new StampaAreeDocumentoCommand(this.reportManager);
			stampaAreeMagazzinoCommandInterceptor = new ActionCommandInterceptorAdapter() {

				@Override
				public boolean preExecution(ActionCommand command) {
					List<AreaMagazzino> areeMagazzinoDaStampare = new ArrayList<AreaMagazzino>();

					for (AreaMagazzinoRicerca areaMagazzinoRicerca : getTable().getSelectedObjects()) {
						AreaMagazzino areaMagazzinoStampa = new AreaMagazzino();
						areaMagazzinoStampa.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
						areaMagazzinoStampa.setTipoAreaMagazzino(areaMagazzinoRicerca.getTipoAreaMagazzino());
						areaMagazzinoStampa.setDocumento(areaMagazzinoRicerca.getDocumento());
						areeMagazzinoDaStampare.add(areaMagazzinoStampa);
					}
					stampaAreeDocumentoCommand.addParameter(StampaAreeDocumentoCommand.PARAM_AREE_DA_STAMPARE,
							areeMagazzinoDaStampare);
					return !getTable().getSelectedObjects().isEmpty();
				}
			};
			stampaAreeDocumentoCommand.addCommandInterceptor(stampaAreeMagazzinoCommandInterceptor);
		}

		return stampaAreeDocumentoCommand;
	}

	@Override
	public Collection<AreaMagazzinoRicerca> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

		if (ContabilizzazioneAreeMagazzinoMessageDelegate.MESSAGE_CHANGE.equals(event.getPropertyName())) {
			final AbstractStateDescriptor stateDescriptor = (AbstractStateDescriptor) event.getNewValue();

			Runnable task = new Runnable() {
				@Override
				public void run() {
					// contabilizzazioneProgressBar.setVisible(stateDescriptor.getCurrentJobOperation()
					// != stateDescriptor
					// .getTotalJobOperation());
					contabilizzazioneProgressBar.setMaximum(stateDescriptor.getTotalJobOperation());
					contabilizzazioneProgressBar.setValue(stateDescriptor.getCurrentJobOperation());
					contabilizzazioneProgressBar.setString(stateDescriptor.getCurrentJobOperation() + " / "
							+ stateDescriptor.getTotalJobOperation());
				}
			};
			if (SwingUtilities.isEventDispatchThread()) {
				task.run();
			} else {
				SwingUtilities.invokeLater(task);
			}
		} else {
			super.propertyChange(event);
		}
	}

	@Override
	public Collection<AreaMagazzinoRicerca> refreshTableData() {
		return null;
	}

	/**
	 * @param contabilizzaAreeMagazzinoCommand
	 *            the contabilizzaAreeMagazzinoCommand to set
	 */
	public void setContabilizzaAreeMagazzinoCommand(ContabilizzaAreeMagazzinoCommand contabilizzaAreeMagazzinoCommand) {
		this.contabilizzaAreeMagazzinoCommand = contabilizzaAreeMagazzinoCommand;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoContabilizzazioneBD
	 *            the magazzinoContabilizzazioneBD to set
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

	/**
	 * @param reportManager
	 *            the reportManager to set
	 */
	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}
}
