package it.eurotn.panjea.onroad.rich.commands;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.onroad.domain.wrapper.ClientiOnRoad;
import it.eurotn.panjea.onroad.domain.wrapper.DocumentiOnRoad;
import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.rich.command.OpenEditorCommand;

import java.awt.BorderLayout;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ImportCommand extends OpenEditorCommand {

	private class ImportOnRoadDialog extends TitledApplicationDialog {

		private static final String DIALOG_ID = "importazioneOnRoadDialog";

		private JProgressBar progressBar;
		private JTextArea logArea;

		/**
		 * Costruttore.
		 */
		public ImportOnRoadDialog() {
			setId(DIALOG_ID);
			RcpSupport.configure(this, DIALOG_ID);
			setTitlePaneTitle(getMessage(DIALOG_ID + ".titlePane"));
		}

		@Override
		protected JComponent createTitledDialogContentPane() {
			JPanel panel = getComponentFactory().createPanel(new BorderLayout());

			progressBar = new JProgressBar(SwingConstants.HORIZONTAL);

			panel.add(progressBar, BorderLayout.CENTER);
			logArea = getComponentFactory().createTextArea();
			logArea.setColumns(60);
			logArea.setRows(20);
			panel.add(logArea, BorderLayout.SOUTH);

			return panel;
		}

		@Override
		protected String getCancelCommandId() {
			return "cancelDocumentiOnRoadCommand";
		}

		@Override
		protected Object[] getCommandGroupMembers() {
			return (new AbstractCommand[] { getFinishCommand(), getCancelCommand() });
		}

		@Override
		protected String getFinishCommandId() {
			return "importaDocumentiOnRoadCommand";
		}

		@Override
		protected void onAboutToShow() {
		}

		@Override
		protected boolean onFinish() {

			openDocumentiEditor = Boolean.FALSE;

			progressBar.setIndeterminate(true);
			getFinishCommand().setEnabled(false);

			try {
				clientiOnRoad = onRoadBD.importaClienti();
				documentiOnRoad = onRoadBD.importaDocumenti();
			} finally {
				progressBar.setIndeterminate(false);
				getFinishCommand().setEnabled(true);
			}

			String logClienti = clientiOnRoad != null && clientiOnRoad.getLog() != null ? clientiOnRoad.getLog() : "";
			String logDocumenti = documentiOnRoad != null && documentiOnRoad.getLog() != null ? documentiOnRoad
					.getLog() : "";

			logArea.setText(logClienti + "\n\n" + logDocumenti);

			openDocumentiEditor = Boolean.TRUE;

			return false;
		}

	}

	private IOnRoadBD onRoadBD;
	private ClientiOnRoad clientiOnRoad = null;
	private DocumentiOnRoad documentiOnRoad = null;
	private boolean openDocumentiEditor;

	@Override
	protected void doExecuteCommand() {
		new ImportOnRoadDialog().showDialog();

		if (openDocumentiEditor) {
			ParametriRicercaAreaMagazzino parametri = new ParametriRicercaAreaMagazzino();
			Set<TipoGenerazione> tipiGenerazione = new HashSet<TipoGenerazione>();
			tipiGenerazione.add(TipoGenerazione.ATON);
			parametri.setTipiGenerazione(tipiGenerazione);
			parametri.setEffettuaRicerca(true);
			Periodo periodo = new Periodo();
			periodo.setTipoPeriodo(TipoPeriodo.OGGI);
			if (documentiOnRoad != null) {
				periodo.setTipoPeriodo(TipoPeriodo.DATE);
				periodo.setDataIniziale(documentiOnRoad.getDataInizio());
				periodo.setDataFinale(documentiOnRoad.getDataFine());
			}
			parametri.setDataDocumento(periodo);
			parametri.setDataRegistrazione(periodo);
			parametri.setAnnoCompetenza(Calendar.getInstance().get(Calendar.YEAR));
			LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	/**
	 * @param onRoadBD
	 *            the onRoadBD to set
	 */
	public void setOnRoadBD(IOnRoadBD onRoadBD) {
		this.onRoadBD = onRoadBD;
	}

}
