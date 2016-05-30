package it.eurotn.panjea.aton.rich.commands;

import it.eurotn.panjea.aton.rich.bd.IAtonBD;
import it.eurotn.panjea.aton.rich.exporter.ArticoliExporter;
import it.eurotn.panjea.aton.rich.exporter.ClientiExporter;
import it.eurotn.panjea.aton.rich.exporter.CondizExporter;
import it.eurotn.panjea.aton.rich.exporter.ExportLabel;
import it.eurotn.panjea.aton.rich.exporter.GiacenzeExporter;
import it.eurotn.panjea.aton.rich.exporter.RateExporter;
import it.eurotn.panjea.aton.rich.exporter.TabelleExporter;
import it.eurotn.rich.command.OpenEditorCommand;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

public class ExportCommand extends OpenEditorCommand {

	private class DialogEsportazione extends TitledApplicationDialog {

		private static final String DIALOG_ID = "esportazioneAtonDialog";

		private ExportLabel esportazioneClienti;
		private ExportLabel esportazioneArticoli;
		private ExportLabel esportazioneAttributi;
		private ExportLabel esportazioneIva;
		private ExportLabel esportazioneRate;
		private ExportLabel esportazioneTabelle;
		private ExportLabel esportazioneGiacenze;
		private ExportLabel esportazioneUtenti;
		private ExportLabel esportazioneCondiz;
		private ExportLabel esportazioneUm;

		/**
		 * Costruttore.
		 */
		public DialogEsportazione() {
			setId(DIALOG_ID);
			RcpSupport.configure(this, DIALOG_ID);
			setPreferredSize(new Dimension(300, 200));
			setTitlePaneTitle(getMessage(DIALOG_ID + ".titlePane"));
		}

		@Override
		protected JComponent createTitledDialogContentPane() {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			esportazioneClienti = new ExportLabel("esportazioneClienti");
			panel.add(esportazioneClienti);

			esportazioneArticoli = new ExportLabel("esportazioneArticoli");
			panel.add(esportazioneArticoli);

			esportazioneAttributi = new ExportLabel("esportazioneAttributi");
			panel.add(esportazioneAttributi);

			esportazioneIva = new ExportLabel("esportazioneIva");
			panel.add(esportazioneIva);

			esportazioneRate = new ExportLabel("esportazioneRate");
			panel.add(esportazioneRate);

			esportazioneTabelle = new ExportLabel("esportazioneTabelle");
			panel.add(esportazioneTabelle);

			esportazioneGiacenze = new ExportLabel("esportazioneGiacenze");
			panel.add(esportazioneGiacenze);

			esportazioneUtenti = new ExportLabel("esportazioneUtenti");
			panel.add(esportazioneUtenti);

			esportazioneCondiz = new ExportLabel("esportazioneCondiz");
			panel.add(esportazioneCondiz);

			esportazioneUm = new ExportLabel("esportazioneUm");
			panel.add(esportazioneUm);
			return panel;
		}

		@Override
		protected boolean onFinish() {
			getFinishCommand().setEnabled(false);

			ClientiExporter clientiExporter = new ClientiExporter(atonBD, esportazioneClienti);
			ArticoliExporter articoliExporter = new ArticoliExporter(atonBD, esportazioneArticoli,esportazioneAttributi);
			//			AttributiExporter attributiExporter = new AttributiExporter(atonBD, esportazioneAttributi);
			//			CodiciIvaExporter codiciIvaExporter = new CodiciIvaExporter(atonBD, esportazioneIva);
			RateExporter rateExporter = new RateExporter(atonBD, esportazioneRate);
			TabelleExporter tabelleExporter = new TabelleExporter(atonBD, esportazioneTabelle,esportazioneUm,esportazioneUtenti,esportazioneIva);
			GiacenzeExporter giacenzeExporter = new GiacenzeExporter(atonBD, esportazioneGiacenze);
			//			UtenteExporter utenteExporter = new UtenteExporter(atonBD, esportazioneUtenti);
			CondizExporter condizExporter = new CondizExporter(atonBD, esportazioneCondiz);
			//UmExporter umExporter = new UmExporter(atonBD, esportazioneUm);

			clientiExporter.start();
			articoliExporter.start();
			//			attributiExporter.start();
			//			codiciIvaExporter.start();
			rateExporter.start();
			tabelleExporter.start();
			giacenzeExporter.start();
			//			utenteExporter.start();
			condizExporter.start();
			//umExporter.start();

			return false;
		}

	}

	private IAtonBD atonBD;

	@Override
	protected void doExecuteCommand() {
		new DialogEsportazione().showDialog();
	}

	/**
	 * @param atonBD
	 *            The atonBD to set.
	 */
	public void setAtonBD(IAtonBD atonBD) {
		this.atonBD = atonBD;
	}
}
