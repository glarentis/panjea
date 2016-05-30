package it.eurotn.panjea.lotti.rich.commands;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.StampaCommand;

import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.util.RcpSupport;

public class OpenVerificheGiacenzaLottiCommand extends StampaCommand {
	private String idCommand;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public OpenVerificheGiacenzaLottiCommand() {
		super("openVerificheGiacenzaLottiCommand", "openVerificheGiacenzaLottiCommand");
	}

	/**
	 * @return Returns the idCommand.
	 */
	public String getIdCommand() {
		return idCommand;
	}

	@Override
	protected Map<Object, Object> getParametri() {
		AziendaCorrente azienda = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
		Map<Object, Object> parameters = new HashMap<Object, Object>();
		parameters.put("descAzienda", azienda.getDenominazione());
		parameters.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
		return parameters;
	}

	@Override
	protected String getReportName() {
		return "Verifiche giacenza lotti";
	}

	@Override
	protected String getReportPath() {
		return "Lotti/Verifiche/giacenzeLotti";
	}

	/**
	 * @param idCommand
	 *            The idCommand to set.
	 */
	public void setIdCommand(String idCommand) {
		this.idCommand = idCommand;
		setId(idCommand);
	}

}
