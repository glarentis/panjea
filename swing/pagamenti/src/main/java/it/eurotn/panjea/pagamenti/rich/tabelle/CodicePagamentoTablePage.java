package it.eurotn.panjea.pagamenti.rich.tabelle;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.rich.bd.IAnagraficaPagamentiBD;
import it.eurotn.panjea.rate.rich.bd.IRateBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.Dimension;
import java.util.Collection;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

public class CodicePagamentoTablePage extends AbstractTablePageEditor<CodicePagamento> {

	/**
	 * Command per simulare la generazione di rate da un codice pagamento scelto.
	 */
	private class SimulaRateCommand extends ActionCommand {

		public static final String COMMAND_ID = "simulaRateCommand";

		/**
		 * Costruttore di default.
		 */
		public SimulaRateCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		public void doExecuteCommand() {
			final SimulazioneRateTablePage dialogPage = new SimulazioneRateTablePage(rateBD, getTable()
					.getSelectedObject());
			TitledPageApplicationDialog applicationDialog = new TitledPageApplicationDialog(dialogPage) {

				@Override
				protected boolean onFinish() {
					dialogPage.dispose();
					return true;
				}
			};
			applicationDialog.setPreferredSize(new Dimension(700, 300));
			applicationDialog.setCallingCommand(this);
			applicationDialog.showDialog();
		}

	}

	private static final String PAGE_ID = "codicePagamentoTablePage";

	private IAnagraficaPagamentiBD anagraficaPagamentiBD;
	private IRateBD rateBD;

	private AbstractCommand simulaRateCommand;

	/**
	 * Costruttore.
	 */
	protected CodicePagamentoTablePage() {
		super(PAGE_ID, new String[] { "codicePagamento", "descrizione", "tipologiaPartita", "abilitato" },
				CodicePagamento.class);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getSimulaRateCommand() };
	}

	/**
	 * @return Returns the rateBD.
	 */
	public IRateBD getRateBD() {
		return rateBD;
	}

	/**
	 * 
	 * @return command per aprire il dialogo della simulazione creazione rate
	 */
	private AbstractCommand getSimulaRateCommand() {
		if (simulaRateCommand == null) {
			simulaRateCommand = new SimulaRateCommand();
		}
		return simulaRateCommand;
	}

	@Override
	public Collection<CodicePagamento> loadTableData() {
		return anagraficaPagamentiBD.caricaCodiciPagamento(null, TipoRicercaCodicePagamento.TUTTO, true);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<CodicePagamento> refreshTableData() {
		return null;
	}

	/**
	 * @param anagraficaPagamentiBD
	 *            anagraficaPagamentiBD to set
	 */
	public void setAnagraficaPagamentiBD(IAnagraficaPagamentiBD anagraficaPagamentiBD) {
		this.anagraficaPagamentiBD = anagraficaPagamentiBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param rateBD
	 *            The rateBD to set.
	 */
	public void setRateBD(IRateBD rateBD) {
		this.rateBD = rateBD;
	}

}
