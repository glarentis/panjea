/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.commands;

import it.eurotn.panjea.anagrafica.rich.exceptions.DownloadFileExchangeRateException;
import it.eurotn.panjea.rich.bd.IValutaBD;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Comando incaricato di scaricare il file xml contenente i tassi di cambio delle valute e chiamare il metodo per
 * aggiornare i valori.
 *
 * @author fattazzo
 *
 */
public class AggiornaTassiCommand extends ApplicationWindowAwareCommand {

	private static Logger logger = Logger.getLogger(AggiornaTassiCommand.class);

	// URL for XML file containing EGB's daily exchange rates
	private final String ecbRatesURL = "http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml";

	public static final String COMMAND_ID = "aggiornaTassiCommand";

	private IValutaBD valutaBD;

	/**
	 * Costruttore.
	 *
	 */
	public AggiornaTassiCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		URL ecbRates = null;
		try {
			ecbRates = new URL(ecbRatesURL);
		} catch (MalformedURLException e1) {
			throw new DownloadFileExchangeRateException("Connection/Open Error: " + e1.getMessage());
		}
		try (InputStreamReader in = new InputStreamReader(ecbRates.openStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			valutaBD.aggiornaTassi(out.toByteArray());
		} catch (IOException e) {
			logger.error("Connection/Open Error: " + e.getMessage());
			throw new DownloadFileExchangeRateException("Connection/Open Error: " + e.getMessage());
		}
	}

	/**
	 * @param valutaBD
	 *            The valutaBD to set.
	 */
	public void setValutaBD(IValutaBD valutaBD) {
		this.valutaBD = valutaBD;
	}

}
