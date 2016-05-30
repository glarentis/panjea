package it.eurotn.panjea.onroad.rich.commands;

import it.eurotn.panjea.onroad.rich.bd.IOnRoadBD;
import it.eurotn.panjea.onroad.rich.exporter.ArticoliExporter;
import it.eurotn.panjea.onroad.rich.exporter.AssortimentoArticoliExporter;
import it.eurotn.panjea.onroad.rich.exporter.ClientiExporter;
import it.eurotn.panjea.onroad.rich.exporter.CondizExporter;
import it.eurotn.panjea.onroad.rich.exporter.ExportLabel;
import it.eurotn.panjea.onroad.rich.exporter.RateExporter;
import it.eurotn.panjea.onroad.rich.exporter.TabelleExporter;
import it.eurotn.rich.command.OpenEditorCommand;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

public class ExportCommand extends OpenEditorCommand {

	private class DialogEsportazione extends TitledApplicationDialog {

		private static final String DIALOG_ID = "esportazioneOnRoadDialog";

		private ExportLabel esportazioneClienti;
		private ExportLabel esportazioneArticoli;
		private ExportLabel esportazioneAttributi;
		private ExportLabel esportazioneIva;
		private ExportLabel esportazioneRate;
		private ExportLabel esportazioneTabelle;
		private ExportLabel esportazioneCondiz;
		private ExportLabel esportazionePagame;
		private ExportLabel esportazioneAssortimentoArticoli;
		private ExportLabel esportazioneClientiCessionari;

		// private ExportLabel esportazioneUm;
		// private ExportLabel esportazioneGiacenze;
		// private ExportLabel esportazioneUtenti;

		/**
		 * Costruttore.
		 *
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

			esportazioneIva = new ExportLabel("esportazioneIva");
			panel.add(esportazioneIva);

			esportazioneArticoli = new ExportLabel("esportazioneArticoli");
			panel.add(esportazioneArticoli);

			esportazioneAttributi = new ExportLabel("esportazioneAttributi");
			panel.add(esportazioneAttributi);

			esportazioneAssortimentoArticoli = new ExportLabel("esportazioneAssortimento");
			panel.add(esportazioneAssortimentoArticoli);

			esportazioneClienti = new ExportLabel("esportazioneClienti");
			panel.add(esportazioneClienti);

			esportazioneClientiCessionari = new ExportLabel("esportazioneConces");
			panel.add(esportazioneClientiCessionari);

			esportazioneCondiz = new ExportLabel("esportazioneCondiz");
			panel.add(esportazioneCondiz);

			esportazionePagame = new ExportLabel("esportazionePagame");
			panel.add(esportazionePagame);

			esportazioneRate = new ExportLabel("esportazioneRate");
			panel.add(esportazioneRate);

			esportazioneTabelle = new ExportLabel("esportazioneTabelle");
			panel.add(esportazioneTabelle);

			// esportazioneGiacenze = new ExportLabel("esportazioneGiacenze");
			// panel.add(esportazioneGiacenze);

			// esportazioneUtenti = new ExportLabel("esportazioneUtenti");
			// panel.add(esportazioneUtenti);

			// esportazioneUm = new ExportLabel("Unità di misura");
			// panel.add(esportazioneUm);

			return panel;
		}

		@Override
		protected boolean onFinish() {
			getFinishCommand().setEnabled(false);
			// esporto

			ClientiExporter clientiExporter = new ClientiExporter(onRoadBD, esportazioneClienti,
					esportazioneClientiCessionari);
			ArticoliExporter articoliExporter = new ArticoliExporter(onRoadBD, esportazioneArticoli,
					esportazioneAttributi);
			AssortimentoArticoliExporter assortimentoArticoliExporter = new AssortimentoArticoliExporter(onRoadBD,
					esportazioneAssortimentoArticoli);
			RateExporter rateExporter = new RateExporter(onRoadBD, esportazioneRate);
			TabelleExporter tabelleExporter = new TabelleExporter(onRoadBD, esportazioneTabelle, esportazioneIva,
					esportazionePagame);
			CondizExporter condizExporter = new CondizExporter(onRoadBD, esportazioneCondiz);

			clientiExporter.start();
			articoliExporter.start();
			assortimentoArticoliExporter.start();
			condizExporter.start();
			rateExporter.start();
			tabelleExporter.start();

			return false;
		}

	}

	private IOnRoadBD onRoadBD;

	@Override
	protected void doExecuteCommand() {
		new DialogEsportazione().showDialog();
	}

	/**
	 * @param onRoadBD
	 *            The onRoadBD to set.
	 */
	public void setOnRoadBD(IOnRoadBD onRoadBD) {
		this.onRoadBD = onRoadBD;
	}
}
