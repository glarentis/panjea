package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ConfermaMovimentiDialog extends MessageDialog implements PropertyChangeListener {

	private class UpdateJobRunnable implements Runnable {
		private volatile AbstractStateDescriptor stateDescriptor;
		private final JProgressBar progressBar;

		/**
		 * 
		 * Costruttore.
		 * 
		 * @param progressBar
		 *            da aggiornare.
		 */
		public UpdateJobRunnable(final JProgressBar progressBar) {
			this.progressBar = progressBar;
		}

		@Override
		public void run() {
			if (stateDescriptor == null) {
				return;
			}
			progressBar.setMaximum(stateDescriptor.getTotalJobOperation());
			progressBar.setValue(stateDescriptor.getCurrentJobOperation());
			progressBar.setString(stateDescriptor.getCurrentJobOperation() + " / "
					+ stateDescriptor.getTotalJobOperation());

			closeEnable = stateDescriptor.isDone();
		}

		/**
		 * @param stateDescriptor
		 *            The stateDescriptor to set.
		 */
		public void setStateDescriptor(AbstractStateDescriptor stateDescriptor) {
			this.stateDescriptor = stateDescriptor;
		}
	};

	public static final String DIALOG_ID = "confermaMovimentiDialog";

	private JProgressBar confermaMovimentiProgressBar = new JProgressBar();

	private JProgressBar contabilizzazioneMovimentiProgressBar = new JProgressBar();

	private volatile boolean closeEnable = false;

	private UpdateJobRunnable confermaMovimentiTask;

	private UpdateJobRunnable contabilizzaMovimentiTask;

	private Map<String, UpdateJobRunnable> jobRunnables;

	/**
	 * Costruttore.
	 * 
	 */
	public ConfermaMovimentiDialog() {
		super(RcpSupport.getMessage(DIALOG_ID + ".title"), RcpSupport.getMessage(DIALOG_ID + ".message"));
		initControl();
	}

	@Override
	protected JComponent createDialogContentPane() {

		FormLayout layout = new FormLayout("right:default, fill:200dlu:grow",
				"default,10dlu,fill:20dlu,10dlu,fill:20dlu");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(super.createDialogContentPane(), cc.xyw(1, 1, 2));
		builder.addLabel("Conferma movimenti:", cc.xy(1, 3));
		builder.add(confermaMovimentiProgressBar, cc.xy(2, 3));
		builder.addLabel("Contabilizzazione movimenti:", cc.xy(1, 5));
		builder.add(contabilizzazioneMovimentiProgressBar, cc.xy(2, 5));

		closeEnable = false;

		contabilizzaMovimentiTask = new UpdateJobRunnable(contabilizzazioneMovimentiProgressBar);
		confermaMovimentiTask = new UpdateJobRunnable(confermaMovimentiProgressBar);

		jobRunnables = new HashMap<String, ConfermaMovimentiDialog.UpdateJobRunnable>(2);
		jobRunnables.put(ContabilizzazioneMovimentiInFatturazioneMessageDelegate.MESSAGE_CHANGE,
				contabilizzaMovimentiTask);
		jobRunnables.put(ConfermaMovimentiInFatturazioneMessageDelegate.MESSAGE_CHANGE, confermaMovimentiTask);

		return builder.getPanel();
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new Object[] {};
	}

	/**
	 * Inizializza tutti i controlli.
	 */
	private void initControl() {

		ConfermaMovimentiInFatturazioneMessageDelegate confermaMovimentimessageDelegate = RcpSupport
				.getBean(ConfermaMovimentiInFatturazioneMessageDelegate.DELEGATE_ID);
		confermaMovimentimessageDelegate.addPropertyChangeListener(this);
		ContabilizzazioneMovimentiInFatturazioneMessageDelegate contabilizzazioneMovimentiInFatturazioneMessageDelegate = RcpSupport
				.getBean(ContabilizzazioneMovimentiInFatturazioneMessageDelegate.DELEGATE_ID);
		contabilizzazioneMovimentiInFatturazioneMessageDelegate.addPropertyChangeListener(this);

		confermaMovimentiProgressBar.setStringPainted(true);
		confermaMovimentiProgressBar.setValue(0);
		confermaMovimentiProgressBar.setMaximum(0);
		confermaMovimentiProgressBar.setString("0/0");

		contabilizzazioneMovimentiProgressBar.setStringPainted(true);
		contabilizzazioneMovimentiProgressBar.setValue(0);
		contabilizzazioneMovimentiProgressBar.setMaximum(0);
		contabilizzazioneMovimentiProgressBar.setString("0/0");

	}

	@Override
	protected void onCancel() {
		if (closeEnable) {
			super.onCancel();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

		UpdateJobRunnable job = jobRunnables.get(event.getPropertyName());
		if (job == null) {
			throw new IllegalArgumentException(
					"il nome della proprietà non corrisponde a nessun nome valido. Nome ricevuto "
							+ event.getPropertyName());
		}
		job.setStateDescriptor((AbstractStateDescriptor) event.getNewValue());
		SwingUtilities.invokeLater(job);
	}

	/**
	 * @param closeEnable
	 *            the closeEnable to set
	 */
	public void setCloseEnable(boolean closeEnable) {
		this.closeEnable = closeEnable;
	}

}
