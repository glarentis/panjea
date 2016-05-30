package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.StampaCommand;

import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.util.RcpSupport;

public class StampaSimulazioneCommand extends StampaCommand {

	private Integer idSimulazione;

	public static final String COMMAND_ID = "stampaSimulazioneCommand";

	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private final AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 *
	 * @param paramBeniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public StampaSimulazioneCommand(final IBeniAmmortizzabiliBD paramBeniAmmortizzabiliBD) {
		super(COMMAND_ID);
		setSecurityControllerId(COMMAND_ID);
		RcpSupport.configure(this);
		this.beniAmmortizzabiliBD = paramBeniAmmortizzabiliBD;
		this.aziendaCorrente = (AziendaCorrente) Application.instance().getApplicationContext()
				.getBean("aziendaCorrente");
	}

	// @Override
	// protected Map<String, Closure> getActionHyperlinkExecutor() {
	// Closure closureBene = new Closure() {
	//
	// @Override
	// public Object call(Object argument) {
	//
	// @SuppressWarnings("unchecked")
	// List<JRPrintHyperlinkParameter> list = (List<JRPrintHyperlinkParameter>) argument;
	//
	// for (JRPrintHyperlinkParameter parametro : list) {
	//
	// if ("id".equals(parametro.getName())) {
	// BeneAmmortizzabile beneAmmortizzabile = new BeneAmmortizzabile();
	// beneAmmortizzabile.setId((Integer) parametro.getValue());
	// beneAmmortizzabile = beniAmmortizzabiliBD.caricaBeneAmmortizzabile(beneAmmortizzabile);
	// LifecycleApplicationEvent event = new OpenEditorEvent(beneAmmortizzabile);
	// Application.instance().getApplicationContext().publishEvent(event);
	// }
	// }
	//
	// return null;
	// }
	// };
	//
	// Map<String, Closure> mappa = new HashMap<String, Closure>();
	// mappa.put("it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile", closureBene);
	//
	// return mappa;
	// }

	@Override
	protected Map<Object, Object> getParametri() {
		HashMap<Object, Object> parametri = new HashMap<Object, Object>();
		parametri.put("simulazione", idSimulazione);
		parametri.put("descAzienda", aziendaCorrente.getDenominazione());
		parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
		return parametri;
	}

	@Override
	protected String getReportName() {
		return "Stampa simulazione beni";
	}

	@Override
	protected String getReportPath() {
		return "BeniAmmortizzabili/simulazione";
	}

	/**
	 *
	 * @param paramSimulazione
	 *            the simulazione to set
	 */
	public void setSimulazione(Simulazione paramSimulazione) {
		this.idSimulazione = paramSimulazione.getId();
	}

}