package it.eurotn.panjea.auvend.rich.editors.importazioni.fatture;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

public class VerificaimportazioneFattureCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "verificaImportazioneFattureCommand";

	private final ImportazioneFattureTablePage page;
	private final IAuVendBD auVendBD;

	/**
	 * Costruttore.
	 * 
	 * @param page
	 *            pagina di importazione delle fatture
	 * @param auVendBD
	 *            auVendBD
	 */
	public VerificaimportazioneFattureCommand(final ImportazioneFattureTablePage page, final IAuVendBD auVendBD) {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.page = page;
		this.auVendBD = auVendBD;
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		java.util.List<Deposito> listDepositi = this.page.getDepositiSedeSelezionata();
		java.util.List<String> listCodiciDepositi = new ArrayList<String>();

		for (Deposito deposito : listDepositi) {
			listCodiciDepositi.add(deposito.getCodice());
		}

		Map<String, StatisticaImportazione> map = auVendBD.verificaFatture(listCodiciDepositi,
				this.page.getDataFineImportazione());

		this.page.aggiungiStatisticheImportazione(map);
	}

}