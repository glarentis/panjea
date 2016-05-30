package it.eurotn.panjea.rich.editors.update;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.rich.editors.update.step.AbstractStepPanel;
import it.eurotn.panjea.rich.editors.update.step.ArrowStepPanel;
import it.eurotn.panjea.rich.editors.update.step.PointStepPanel;
import it.eurotn.panjea.rich.editors.update.step.PointStepPanel.PointType;
import it.eurotn.panjea.rich.editors.update.step.StepUpdate;
import it.eurotn.rich.command.JECCommandGroup;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.ui.tabbedui.VerticalLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

import foxtrot.ConcurrentWorker;
import foxtrot.Task;

public class UpdateEditor extends AbstractEditor implements PropertyChangeListener, InitializingBean {

	private class CancelUpdateCommand extends ActionCommand {

		private static final String COMMAND_ID = "cancelUpdateCommand";

		/**
		 * Costruttore.
		 */
		public CancelUpdateCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			setEnabled(false);
			try {
				ConcurrentWorker.post(new Task() {

					@Override
					public Object run() throws Exception {
						if (timerTaskUpdate != null) {
							timerTaskUpdate.cancel();
						}
						panjeaServer.cancel();
						return null;
					}
				});
			} catch (Exception e) {
				logger.error("-->errore nel cancellare l'aggiornamento", e);
				throw new PanjeaRuntimeException(e);
			}

			getUpdateCommand().setEnabled(true);
		}
	}

	private class StartUpdateCommand extends ActionCommand {
		private static final String COMMAND_ID = "startUpdateCommand";

		/**
		 * Costruttore.
		 */
		public StartUpdateCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (!panjeaServer.checkEuroTnServerConnection()) {
				new MessageDialog("Attenzione!",
						"Server di aggiornamento di Panjea non disponibile, riprovare più tardi!").showDialog();
				return;
			}

			setEnabled(false);
			cancelUpdateCommand.setEnabled(true);
			DelayUpdateChooserDialog dialog = new DelayUpdateChooserDialog();
			dialog.setCloseAction(CloseAction.HIDE);
			dialog.showDialog();

			final long delay = dialog.getDelay();
			dialog = null;

			// annullo l'aggiornamento quindi esco senza fare nulla
			if (delay == -1) {
				setEnabled(true);
				cancelUpdateCommand.setEnabled(false);
				return;
			}

			panjeaServer.notifyApplicationUpdate(delay);

			if (currentStepIndex == StepUpdate.WAITING.ordinal()) {
				try {
					logArea.setText("");
					UpdateTask task = new UpdateTask();
					timerTaskUpdate = new Timer();
					timerTaskUpdate.schedule(task, delay * 1000);
				} catch (Exception e) {
					logger.error("-->errore nel lanciare  l'aggiornamento", e);
					throw new PanjeaRuntimeException(e);
				}
			}
		}
	}

	private class StatusTask extends TimerTask {
		@Override
		public void run() {
			final String stato = panjeaServer.getServerStatus();
			try {
				SwingUtilities.invokeAndWait(new Runnable() {

					@Override
					public void run() {
						if (factory.isControlCreated()) {
							servicesStatusPanel.update(stato);
							servicesStatusPanel.updateVersion(panjeaServer.getApplicationProperties());
						}
					}
				});
			} catch (Exception e) {
				logger.error("-->errore nel recuperare lo stato del server", e);
				throw new PanjeaRuntimeException(e);
			}
		}
	}

	private class UpdateTask extends TimerTask {
		@Override
		public void run() {
			try {
				panjeaServer.update();
			} catch (Exception e) {
				logger.error("-->errore nell'update", e);
			}
		}
	}

	private final AbstractControlFactory factory = new AbstractControlFactory() {

		@Override
		public JComponent createControl() {
			JPanel topPanel = new JPanel(new BorderLayout(10, 20));
			rootPanel = new JPanel();

			JPanel topCommandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
			JECCommandGroup commandGroup = new JECCommandGroup();
			commandGroup.add(getUpdateCommand());
			commandGroup.add(getCancelUpdateCommand());
			topCommandPanel.add(commandGroup.createToolBar());
			topPanel.add(topCommandPanel, BorderLayout.NORTH);

			JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			statusPanel.add(createStepsPanel());
			servicesStatusPanel = new ServicesStatusPanel();
			statusPanel.add(servicesStatusPanel);
			topPanel.add(statusPanel, BorderLayout.WEST);

			logArea = new JTextArea("");
			logArea.setVisible(false);
			logArea.setBackground(UIManager.getDefaults().getColor("Panel.background"));

			rootPanel.setLayout(new BorderLayout());
			rootPanel.add(topPanel, BorderLayout.NORTH);

			scrollPaneLog = new JScrollPane(logArea);
			scrollPaneLog.setBorder(BorderFactory.createEmptyBorder());
			rootPanel.add(scrollPaneLog, BorderLayout.CENTER);

			return rootPanel;
		}

	};

	private StartUpdateCommand startUpdateCommand;
	private CancelUpdateCommand cancelUpdateCommand;

	private JPanel rootPanel;

	private JTextArea logArea;

	private int currentStepIndex;

	private PanjeaServer panjeaServer;
	private List<AbstractStepPanel> stepsPanel;

	private JLabel statusLabel;

	private ServicesStatusPanel servicesStatusPanel;
	private JComponent scrollPaneLog;

	private Timer timerTaskUpdate;
	private Timer timerStatus;

	@Override
	public void afterPropertiesSet() throws Exception {
		panjeaServer.addPropertyChangeListener(this);
	}

	@Override
	public void componentClosed() {
		super.componentClosed();
		if (timerStatus != null) {
			timerStatus.cancel();
		}
		if (timerTaskUpdate != null) {
			timerTaskUpdate.cancel();
		}
	}

	/**
	 * @return pannello contenente i controlli per gli step di aggiornamento
	 */
	private JPanel createStepsPanel() {

		stepsPanel = new ArrayList<AbstractStepPanel>();

		JPanel updatePanel = new JPanel(new GridLayout(1, 5));

		JPanel cell2Panel = new JPanel(new VerticalLayout());
		AbstractStepPanel abstractStepPanel = new ArrowStepPanel(new StepUpdate[] { StepUpdate.UPDATE_DOWNLOAD },
				SwingConstants.RIGHT);
		stepsPanel.add(abstractStepPanel);
		cell2Panel.add(abstractStepPanel);
		abstractStepPanel = new ArrowStepPanel(
				new StepUpdate[] { StepUpdate.CHECKSUM_UPLOAD, StepUpdate.UPDATE_VERSION }, SwingConstants.LEFT);
		stepsPanel.add(abstractStepPanel);
		cell2Panel.add(abstractStepPanel);

		JPanel cell3Panel = new JPanel(new VerticalLayout());
		abstractStepPanel = new ArrowStepPanel(
				new StepUpdate[] { StepUpdate.CHECKSUM_PREPARE, StepUpdate.UPDATE_UPLOAD }, SwingConstants.RIGHT);
		stepsPanel.add(abstractStepPanel);
		cell3Panel.add(abstractStepPanel);
		abstractStepPanel = new ArrowStepPanel(new StepUpdate[] { StepUpdate.CHECKSUM_DOWNLOAD }, SwingConstants.LEFT);
		stepsPanel.add(abstractStepPanel);
		cell3Panel.add(abstractStepPanel);

		abstractStepPanel = new PointStepPanel(
				new StepUpdate[] { StepUpdate.CHECKSUM_UPLOAD, StepUpdate.UPDATE_VERSION }, PointType.SERVER_EUROPA);
		stepsPanel.add(abstractStepPanel);
		updatePanel.add(abstractStepPanel);
		updatePanel.add(cell2Panel);
		abstractStepPanel = new PointStepPanel(new StepUpdate[] { StepUpdate.CHECKSUM_PREPARE,
				StepUpdate.CHECKSUM_DOWNLOAD, StepUpdate.CHECKSUM_UPLOAD, StepUpdate.UPDATE_DOWNLOAD,
				StepUpdate.UPDATE_UPLOAD, StepUpdate.UPDATE_VERSION }, PointType.CLIENT);
		stepsPanel.add(abstractStepPanel);
		updatePanel.add(abstractStepPanel);
		updatePanel.add(cell3Panel);
		abstractStepPanel = new PointStepPanel(
				new StepUpdate[] { StepUpdate.CHECKSUM_PREPARE, StepUpdate.APPLY_UPDATE }, PointType.SERVER_CLIENT);
		stepsPanel.add(abstractStepPanel);
		updatePanel.add(abstractStepPanel);

		JPanel rootUpdatePanel = new JPanel(new BorderLayout());
		rootUpdatePanel.add(updatePanel, BorderLayout.CENTER);
		statusLabel = new JLabel();
		Font font = statusLabel.getFont();
		statusLabel.setFont(new Font(font.getName(), Font.BOLD, font.getSize() + 4));
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rootUpdatePanel.add(statusLabel, BorderLayout.SOUTH);

		return rootUpdatePanel;
	}

	@Override
	public void dispose() {
		super.dispose();
		panjeaServer.removePropertyChangeListener(this);
	}

	/**
	 * @return command per la cancellazione dell'update.
	 */
	private ActionCommand getCancelUpdateCommand() {
		if (cancelUpdateCommand == null) {
			cancelUpdateCommand = new CancelUpdateCommand();
		}
		cancelUpdateCommand.setEnabled(false);
		return cancelUpdateCommand;
	}

	@Override
	public JComponent getControl() {
		return factory.getControl();
	}

	@Override
	public String getId() {
		return "updateEditor";
	}

	/**
	 * Restituisce la descrizione delle step.
	 *
	 * @param step
	 *            step
	 * @return descrizione
	 */
	public String getStepDescription(StepUpdate step) {
		return RcpSupport.getMessage(step.name());
	}

	/**
	 * @return comando di aggiornamento
	 */
	ActionCommand getUpdateCommand() {
		if (startUpdateCommand == null) {
			startUpdateCommand = new StartUpdateCommand();
		}
		return startUpdateCommand;
	}

	@Override
	public void initialize(Object editorObject) {
		timerStatus = new Timer();
		StatusTask loopStatus = new StatusTask();
		timerStatus.schedule(loopStatus, 0, 2500);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if (!factory.isControlCreated()) {
			logger.debug("se non ho ancora creato i controlli esco senza far nulla");
			return;
		}
		if (evt.getPropertyName().equals(PanjeaServer.PROPERTY_STEP)) {
			if (!SwingUtilities.isEventDispatchThread()) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {

						@Override
						public void run() {
							StepUpdate newStep = (StepUpdate) evt.getNewValue();
							updateStepsPanels(newStep);
							statusLabel.setText(getStepDescription(newStep));
						}
					});
				} catch (Exception e) {
					logger.error("-->errore nel cambiare lo stato dell'aggiornamento", e);
					throw new PanjeaRuntimeException(e);
				}
			} else {
				StepUpdate newStep = (StepUpdate) evt.getNewValue();
				updateStepsPanels(newStep);
				statusLabel.setText(getStepDescription(newStep));
			}
		}
		if (evt.getPropertyName().equals(PanjeaServer.PROPERTY_BYTE_TRASMETTI)) {
			for (AbstractStepPanel stepPanel : stepsPanel) {
				if (stepPanel instanceof ArrowStepPanel) {
					((ArrowStepPanel) stepPanel).updateByte(Long.parseLong(evt.getNewValue().toString()));
				}
			}
		}

		if (evt.getPropertyName().equals(PanjeaServer.PROPERTY_LOG)) {
			if (!SwingUtilities.isEventDispatchThread()) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							logArea.append(evt.getNewValue().toString());
							logArea.setCaretPosition(logArea.getText().length() - 1);
						}
					});
				} catch (Exception e) {
					logger.error("-->errore nell'aggiornare il log del server", e);
					throw new PanjeaRuntimeException(e);
				}
			} else {
				logArea.append(evt.getNewValue().toString());
				logArea.setCaretPosition(logArea.getText().length() - 1);
			}
		}
	}

	@Override
	public void save(ProgressMonitor arg0) {
	}

	/**
	 * @param panjeaServer
	 *            the panjeaServer to set
	 */
	public void setPanjeaServer(PanjeaServer panjeaServer) {
		this.panjeaServer = panjeaServer;
	}

	/**
	 * Aggiorna i pannelli degli step con lo step passato come parametro.
	 *
	 * @param step
	 *            ste di riferimento
	 */
	private void updateStepsPanels(StepUpdate step) {

		for (AbstractStepPanel abstractStepPanel : stepsPanel) {
			abstractStepPanel.update(step);
		}

		switch (step) {
		case WAITING:
			startUpdateCommand.setEnabled(true);
			cancelUpdateCommand.setEnabled(false);
			break;
		case CHECKSUM_PREPARE:
			logArea.setVisible(false);
			scrollPaneLog.setBorder(BorderFactory.createEmptyBorder());
			break;

		case NOT_UPDATE_FILE:
			MessageDialog dialogDisabled = new MessageDialog("Aggiornamenti non presenti!!",
					"Non ci sono aggiornamenti...");
			dialogDisabled.showDialog();
			break;

		case UPDATE_FILE_CORRUPTED:
			MessageDialog dialogCorrupted = new MessageDialog("Errore aggiornamento!!", "File scaricato non valido!");
			dialogCorrupted.showDialog();
			break;

		case APPLY_UPDATE:
			cancelUpdateCommand.setEnabled(false);
			logArea.setVisible(true);
			scrollPaneLog.setBorder(BorderFactory.createTitledBorder("LOG AGGIORNAMENTO"));
			break;
		default:
			break;
		}
	}

}
