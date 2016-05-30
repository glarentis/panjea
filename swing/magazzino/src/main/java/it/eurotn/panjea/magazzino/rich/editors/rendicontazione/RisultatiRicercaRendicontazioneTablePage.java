package it.eurotn.panjea.magazzino.rich.editors.rendicontazione;

import it.eurotn.panjea.magazzino.rich.bd.DocumentiExporterBD;
import it.eurotn.panjea.magazzino.rich.bd.IDocumentiExporterBD;
import it.eurotn.panjea.magazzino.rich.editors.areamagazzino.RisultatiRicercaAreaMagazzinoTablePage;
import it.eurotn.panjea.magazzino.service.exception.DocumentiExporterException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.file.FileTransport;
import it.eurotn.panjea.rich.file.FileTransportFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.AggregateTable;

public class RisultatiRicercaRendicontazioneTablePage extends RisultatiRicercaAreaMagazzinoTablePage {

	private class EsportaDocumentiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "esportaDocumentiCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public EsportaDocumentiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			if (!getTable().isEmpty()) {
				IDocumentiExporterBD documentiExporterBD = RcpSupport.getBean(DocumentiExporterBD.BEAN_ID);
				List<byte[]> flussi;
				try {
					// esporto solo le aree selezionate
					List<AreaMagazzinoRicerca> areeDaEsportare = new ArrayList<AreaMagazzinoRicerca>();
					for (AreaMagazzinoRicerca areaMagazzinoRicerca : getTable().getRows()) {
						if (areaMagazzinoRicerca.isSelezionata()) {
							areeDaEsportare.add(areaMagazzinoRicerca);
						}
					}
					if (areeDaEsportare.isEmpty()) {
						return;
					}

					// Richiedo eventuali dati configurati nel template
					Map<String, Object> parametri = null;
					if (parametriRicercaAreaMagazzino.getTipoEsportazione().isRichiediParametri()) {
						ParametriRichiestaDialog dialogParametri = new ParametriRichiestaDialog(
								parametriRicercaAreaMagazzino.getTipoEsportazione());
						dialogParametri.showDialog();
						parametri = dialogParametri.getParametri();
						dialogParametri = null;
						if (parametri == null) {
							return;
						}
					}

					flussi = documentiExporterBD.esportaDocumenti(areeDaEsportare,
							parametriRicercaAreaMagazzino.getTipoEsportazione(), parametri);

					FileTransport fileTransport = FileTransportFactory.getInstance().getTransport(
							parametriRicercaAreaMagazzino.getTipoEsportazione().getTipoSpedizione());
					boolean sendResult = fileTransport.send(flussi, parametriRicercaAreaMagazzino.getTipoEsportazione()
							.getDatiSpedizione(), parametri);

					String message = "";
					Severity severity = Severity.INFO;
					if (sendResult) {
						message = "Esportazione terminata con successo.";
					} else {
						message = "Esportazione fallita";
						severity = Severity.ERROR;
					}
					new MessageDialog("ATTENZIONE", new DefaultMessage(message, severity)).showDialog();
				} catch (DocumentiExporterException e1) {
					DocumentiExporterExceptionDialog dialog = new DocumentiExporterExceptionDialog(e1);
					dialog.showDialog();
				} catch (Exception e) {
					PanjeaSwingUtil.checkAndThrowException(e);
				}

			}
		}

	}

	private class SelezioneCommandInterceptor extends ActionCommandInterceptorAdapter {
		@Override
		public boolean preExecution(ActionCommand command) {
			command.addParameter(SelezionaAreeRendicontazioneCommand.PARAM_RENDICONTAZIONE_TABLE_PAGE,
					RisultatiRicercaRendicontazioneTablePage.this);

			return super.preExecution(command);
		}
	}

	public static final String PAGE_ID = "risultatiRicercaRendicontazioneTablePage";

	private EsportaDocumentiCommand esportaDocumentiCommand;

	private SelezionaAreeRendicontazioneCommand deselezionaTuttoCommand;
	private SelezionaAreeRendicontazioneCommand selezionaTuttoCommand;

	private SelezioneCommandInterceptor selezioneCommandInterceptor;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaRendicontazioneTablePage() {
		super(PAGE_ID, new RisultatiRicercaRendicontazioneTableModel());
		((AggregateTable) getTable().getTable()).getAggregateTableModel().getField(0).setAllowedAsRowField(false);
		getTable().getTable().addMouseListener(new SelectRowListener(getTable().getTable()));
		selezioneCommandInterceptor = new SelezioneCommandInterceptor();
	}

	@Override
	public void dispose() {

		selezionaTuttoCommand.removeCommandInterceptor(selezioneCommandInterceptor);
		deselezionaTuttoCommand.removeCommandInterceptor(selezioneCommandInterceptor);

		super.dispose();
	}

	@Override
	public AbstractCommand[] getCommands() {
		AbstractCommand[] commands = new AbstractCommand[] { getSelezionaTuttoCommand(), getDeselezionaTuttoCommand(),
				getGestisciRigheNonValideCommand(), getOpenAreaMagazzinoEditor(), getStampaAreeMagazzinoCommand(),
				getEsportaDocumentiCommand() };

		return commands;
	}

	/**
	 * @return comando per deselezionare tutte le rate.
	 */
	public SelezionaAreeRendicontazioneCommand getDeselezionaTuttoCommand() {
		if (deselezionaTuttoCommand == null) {
			deselezionaTuttoCommand = new SelezionaAreeRendicontazioneCommand(
					SelezionaAreeRendicontazioneCommand.UNSELECT_COMMAND_ID, false);
			deselezionaTuttoCommand.addCommandInterceptor(selezioneCommandInterceptor);
		}

		return deselezionaTuttoCommand;
	}

	/**
	 * @return the esportaDocumentiCommand
	 */
	public EsportaDocumentiCommand getEsportaDocumentiCommand() {
		if (esportaDocumentiCommand == null) {
			esportaDocumentiCommand = new EsportaDocumentiCommand();
		}

		return esportaDocumentiCommand;
	}

	/**
	 * @return comando per selezionare tutte le rate.
	 */
	public SelezionaAreeRendicontazioneCommand getSelezionaTuttoCommand() {
		if (selezionaTuttoCommand == null) {
			selezionaTuttoCommand = new SelezionaAreeRendicontazioneCommand(
					SelezionaAreeRendicontazioneCommand.SELECT_COMMAND_ID, true);
			selezionaTuttoCommand.addCommandInterceptor(selezioneCommandInterceptor);
		}

		return selezionaTuttoCommand;
	}
}
