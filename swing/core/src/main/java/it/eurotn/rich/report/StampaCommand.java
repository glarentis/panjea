package it.eurotn.rich.report;

import it.eurotn.panjea.anagrafica.domain.ParametriMail;

import java.awt.event.InputEvent;
import java.util.Map;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public abstract class StampaCommand extends ActionCommand {

	private static final String COMMAND_ID = "printCommand";

	/**
	 *
	 * Costruttore.
	 *
	 * @param idController
	 *            id del controller
	 */
	public StampaCommand(final String idController) {
		this(COMMAND_ID, idController);
	}

	/**
	 *
	 * Costruttore.
	 *
	 * @param idCommand
	 *            id del comando
	 * @param idController
	 *            id del controller
	 */
	public StampaCommand(final String idCommand, final String idController) {
		super(idCommand);
		setSecurityControllerId(idController);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		JecReport report = new JecReport(getReportPath(), getParametri());
		report.setReportName(getReportName());
		report.setParametriMail(getParametriMail());
		if (getParameter(MODIFIERS_PARAMETER_KEY) == null) {
			report.setShowPrintDialog(true);
		} else {
			int key = (int) getParameter(MODIFIERS_PARAMETER_KEY);
			report.setShowPrintDialog((key & InputEvent.CTRL_MASK) != 0);
		}
		report.execute();
	}

	/**
	 *
	 * @return numero di copie da stampare
	 */
	protected int getNumberOfCopies() {
		return 1;
	}

	/**
	 *
	 * @return parametri da passare al report
	 */
	protected abstract Map<Object, Object> getParametri();

	/**
	 *
	 * @return parametri di default per una eventuale spedizione tramite mail del report.<br/>
	 *         Di default setta solamente il nome del report come nome dell'allegato.
	 */
	protected ParametriMail getParametriMail() {
		ParametriMail parametriMail = new ParametriMail();
		parametriMail.setNomeAllegato(getReportName().replace(" ", "").replace("/", "-").replace("\\", "-"));
		parametriMail.setOggetto(getReportName());
		parametriMail.setNota("report " + getReportName());
		return parametriMail;
	}

	/**
	 * Nome della stampante da utilizzare solo in caso di stampa diretta.
	 *
	 * @return nome della stampante
	 */
	protected String getPrinterName() {
		return null;
	}

	/**
	 *
	 * @return nome del report da visualizzare nel titolo dell'editor
	 */
	protected abstract String getReportName();

	/**
	 *
	 * @return path del report in jasperServer
	 */
	protected abstract String getReportPath();
}
