/**
 * 
 */
package it.eurotn.panjea.auvend.rich.commands;

import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.util.ParametriRecuperoFatturazioneRifornimenti;

import java.util.Calendar;
import java.util.Date;

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
public class OpenRecuperoFatturazioneRifornimentiAuVendCommand extends ApplicationWindowAwareCommand {

	private static final String COMMAND_ID = "openRecuperoFatturazioneRifornimentiAuVendCommand";

	private Logger logger = Logger.getLogger(OpenRecuperoFatturazioneRifornimentiAuVendCommand.class);

	private String auVendBDBeanName;

	/**
	 * 
	 */
	public OpenRecuperoFatturazioneRifornimentiAuVendCommand() {
		super(COMMAND_ID);
	}

	@Override
	protected void doExecuteCommand() {
		Date dataFine;
		Date dataInizio;
		Date dataUltimaLettura;

		logger.debug("--> Enter doExecuteCommand");
		IAuVendBD auVendBD = RcpSupport.getBean(IAuVendBD.BEAN_ID);
		ParametriRecuperoFatturazioneRifornimenti parametri = new ParametriRecuperoFatturazioneRifornimenti();
		dataUltimaLettura = auVendBD.caricaLetturaFlussoFatturazioneRifornimenti()
				.getUltimaLetturaFlussoFatturazioneRifornimenti();
		// imposto la data di inizio al giorno successivo la data di ultima lettura del flusso
		Calendar calDataInizio = Calendar.getInstance();
		calDataInizio.setTime(dataUltimaLettura);
		calDataInizio.add(Calendar.DAY_OF_MONTH, 1);
		dataInizio = calDataInizio.getTime();
		parametri.setDataInizio(dataInizio);
		// imposto la data di fine
		// se la data d'inizio è nello stesso mese della data corrente, uso la data corrente come data fine
		// altrimenti imposto l'ultimo giorno del mese precedente
		Calendar calDataFine = Calendar.getInstance();
		if (calDataFine.get(Calendar.MONTH) == calDataInizio.get(Calendar.MONTH)) {
			dataFine = Calendar.getInstance().getTime();
		} else {
			calDataFine.add(Calendar.MONTH, -1);
			calDataFine.set(Calendar.DAY_OF_MONTH, calDataFine.getMaximum(Calendar.DAY_OF_MONTH));
			dataFine = calDataFine.getTime();
		}
		parametri.setDataFine(dataFine);
		LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit doExecuteCommand");
	}

	/**
	 * @return the auVendBDBeanName
	 */
	public String getAuVendBDBeanName() {
		return auVendBDBeanName;
	}

	/**
	 * @param auVendBDBeanName
	 *            the auVendBDBeanName to set
	 */
	public void setAuVendBDBeanName(String auVendBDBeanName) {
		this.auVendBDBeanName = auVendBDBeanName;
	}
}
