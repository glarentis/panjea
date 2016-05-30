package it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.StampaCommand;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.util.RcpSupport;

public class StampaInventarioArticoloCommand extends StampaCommand {

	public static final String COMMAND_ID = "stampaInventarioArticoloCommand";

	public static final String PARAM_DATA = "paramData";
	public static final String PARAM_DEPOSITO = "paramDeposito";
	public static final String PARAM_STAMPA_GIACENZE_A_ZERO = "paramStampaGiacenzeAZero";

	private AziendaCorrente aziendaCorrente;

	private Date data;

	private Integer idDeposito;

	private Boolean stampaGiacenzeAZero;

	/**
	 * Costruttore.
	 *
	 */
	public StampaInventarioArticoloCommand() {
		super(COMMAND_ID);
		aziendaCorrente = RcpSupport.getBean("aziendaCorrente");
	}

	@Override
	protected void doExecuteCommand() {

		data = (Date) getParameter(PARAM_DATA);
		idDeposito = (Integer) getParameter(PARAM_DEPOSITO);
		stampaGiacenzeAZero = (Boolean) getParameter(PARAM_STAMPA_GIACENZE_A_ZERO);

		super.doExecuteCommand();
	}

	@Override
	protected Map<Object, Object> getParametri() {

		HashMap<Object, Object> parametri = new HashMap<Object, Object>();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		parametri.put("data", dateFormat.format(data));
		parametri.put("idDeposito", idDeposito);
		parametri.put("caricaGiacenzeAZero", stampaGiacenzeAZero);
		parametri.put("azienda", aziendaCorrente.getCodice());
		parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
		parametri.put("descAzienda", aziendaCorrente.getDenominazione());

		return parametri;
	}

	@Override
	protected String getReportName() {
		return "Stampa preparazione inventario";
	}

	@Override
	protected String getReportPath() {
		return "Magazzino/PreparazioneInventario";
	}

}
