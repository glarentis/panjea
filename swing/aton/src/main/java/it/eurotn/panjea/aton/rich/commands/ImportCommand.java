package it.eurotn.panjea.aton.rich.commands;

import it.eurotn.panjea.aton.rich.bd.IAtonBD;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.rich.command.OpenEditorCommand;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ImportCommand extends OpenEditorCommand {

	private class ImportAtonDialog extends TitledApplicationDialog {

		private static final String DIALOG_ID = "importazioneAtonDialog";

		private JProgressBar progressBar;

		/**
		 * Costruttore.
		 * 
		 */
		public ImportAtonDialog() {
			setId(DIALOG_ID);
			RcpSupport.configure(this, DIALOG_ID);
			setTitlePaneTitle(getMessage(DIALOG_ID + ".titlePane"));
		}

		@Override
		protected JComponent createTitledDialogContentPane() {
			JPanel panel = getComponentFactory().createPanel(new BorderLayout());

			progressBar = new JProgressBar(SwingConstants.HORIZONTAL);

			panel.add(progressBar, BorderLayout.CENTER);

			return panel;
		}

		@Override
		protected String getCancelCommandId() {
			return "cancelOrdiniAtonCommand";
		}

		@Override
		protected Object[] getCommandGroupMembers() {
			return (new AbstractCommand[] { getFinishCommand(), getCancelCommand() });
		}

		@Override
		protected String getFinishCommandId() {
			return "ImportaOrdiniAtonCommand";
		}

		@Override
		protected void onAboutToShow() {
		}

		@Override
		protected boolean onFinish() {

			openOrdiniEditor = Boolean.FALSE;

			progressBar.setIndeterminate(true);
			getFinishCommand().setEnabled(false);
			try {
				atonBD.importa();
				openOrdiniEditor = Boolean.TRUE;
			} finally {
				progressBar.setIndeterminate(false);
				getFinishCommand().setEnabled(true);
			}

			return true;
		}

	}

	private IAtonBD atonBD;

	private boolean openOrdiniEditor;

	@Override
	protected void doExecuteCommand() {
		new ImportAtonDialog().showDialog();

		if (openOrdiniEditor) {
			ParametriRicercaOrdiniImportati parametri = new ParametriRicercaOrdiniImportati();
			parametri.setProvenienza(EProvenienza.ATON);
			LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	/**
	 * @param atonBD
	 *            the atonBD to set
	 */
	public void setAtonBD(IAtonBD atonBD) {
		this.atonBD = atonBD;
	}

}
