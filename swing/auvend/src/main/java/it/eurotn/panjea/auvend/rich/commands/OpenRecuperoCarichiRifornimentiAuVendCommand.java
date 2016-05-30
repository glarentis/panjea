package it.eurotn.panjea.auvend.rich.commands;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.util.ParametriRecuperoCarichiRifornimenti;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Command per l'apertura dell'editor per il recupero dei rifornimenti di AuVend.
 * 
 * @author adriano
 * @version 1.0, 13/gen/2009
 * 
 */
public class OpenRecuperoCarichiRifornimentiAuVendCommand extends ApplicationWindowAwareCommand {

	private static final String COMMAND_ID = "openRecuperoCarichiRifornimentiAuVendCommand";

	private Logger logger = Logger.getLogger(OpenRecuperoCarichiRifornimentiAuVendCommand.class);

	/**
	 * 
	 */
	public OpenRecuperoCarichiRifornimentiAuVendCommand() {
		super(COMMAND_ID);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		IAuVendBD auVendBD = RcpSupport.getBean(IAuVendBD.BEAN_ID);
		ParametriRecuperoCarichiRifornimenti parametri = new ParametriRecuperoCarichiRifornimenti();
		parametri.setDataFine(Calendar.getInstance().getTime());
		Map<Deposito, LetturaFlussoAuVend> letture = auVendBD.caricaLettureFlussoCarichi();
		Date ultimaLettura = null;
		// prendo la prima impostata.
		for (LetturaFlussoAuVend lettura : letture.values()) {
			ultimaLettura = lettura.getUltimaLetturaFlussoCarichi();
			if (ultimaLettura != null) {
				break;
			}
		}
		parametri.setDataInizio(ultimaLettura);
		LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit doExecuteCommand");
	}

}
