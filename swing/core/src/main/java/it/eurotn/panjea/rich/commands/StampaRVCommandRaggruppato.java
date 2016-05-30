package it.eurotn.panjea.rich.commands;

public class StampaRVCommandRaggruppato extends StampaRVCommand {

	public static final String COMMAND_ID = "stampaRVCommandRaggruppato";

	/**
	 * Costruttore.
	 */
	public StampaRVCommandRaggruppato() {
		super("stampaRVCommandRaggruppato");
	}

	@Override
	protected String getReportName() {
		return "Richiesta di versamento ragg";
	}

	@Override
	protected String getReportNamePath() {
		return "RichiestaVersamentoRateRaggruppate";
	}

}
