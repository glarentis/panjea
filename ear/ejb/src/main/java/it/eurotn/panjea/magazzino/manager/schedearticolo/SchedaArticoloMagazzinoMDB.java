package it.eurotn.panjea.magazzino.manager.schedearticolo;

import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.magazzino.domain.SchedaArticolo;
import it.eurotn.panjea.magazzino.domain.SchedaArticolo.StatoScheda;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoSchedeArticoloManager;
import it.eurotn.panjea.magazzino.util.SchedaArticoloDTO;
import it.eurotn.panjea.report.EmptyReportException;
import it.eurotn.panjea.report.ReportManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jboss.annotation.security.SecurityDomain;
import org.jboss.security.auth.callback.UsernamePasswordHandler;

@MessageDriven(name = "Panjea.SchedaArticoloMagazzinoMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/schedeArticoloMagazzino") })
@SecurityDomain("PanjeaLoginModule")
public class SchedaArticoloMagazzinoMDB implements MessageListener {

	private static final String SCHEDA_ARTICOLO_REPORT_PATH = "/Personalizzate/Magazzino/schedaArticolo";

	private static Logger logger = Logger.getLogger(SchedaArticoloMagazzinoMDB.class);

	@EJB
	private MagazzinoSchedeArticoloManager magazzinoSchedeArticoloManager;

	@EJB
	private PreferenceService preferenceService;

	/**
	 * Crea la scheda articolo.
	 *
	 * @param schedaArticoloDTO
	 *            parametri di creazione della scheda articolo
	 */
	private void creaSchedaArticolo(SchedaArticoloDTO schedaArticoloDTO) {
		SchedaArticolo schedaArticolo = magazzinoSchedeArticoloManager.caricaSchedaArticolo(
				schedaArticoloDTO.getAnno(), schedaArticoloDTO.getMese(), schedaArticoloDTO.getArticolo().getId());
		if (schedaArticoloDTO.getNote() != null) {
			schedaArticolo.getNote().add(schedaArticoloDTO.getNote());
		}

		switch (schedaArticolo.getStato()) {
		case STAMPATO:
			// in questo caso non faccio niente perchè la scheda è già stampata e valida
			break;
		case NON_VALIDO:
			schedaArticolo.setStato(StatoScheda.IN_ELABORAZIONE);
		default:
			// sia che la scheda articolo sia nuova o passata da non valido ad in elaborazione la salvo
			schedaArticolo = magazzinoSchedeArticoloManager.salvaSchedaArticolo(schedaArticolo);

			// creo il report
			String pathPDF = StampaSchedaArticoloBuilder.getStampeSchedaArticoloExportPath(getDirectoryExportPath(),
					schedaArticoloDTO.getAnno(), schedaArticoloDTO.getMese());
			File file = new File(pathPDF);
			file.mkdirs();
			pathPDF = pathPDF + File.separator
					+ StampaSchedaArticoloBuilder.getStampaSchedaArticoloFileName(schedaArticoloDTO.getArticolo());

			ReportManager reportManager = new ReportManager();
			try {
				Map<String, String> reportParams = new HashMap<String, String>();
				reportParams.put("azienda", schedaArticoloDTO.getAzienda());
				reportParams.put("mese", schedaArticoloDTO.getMese().toString());
				reportParams.put("anno", schedaArticoloDTO.getAnno().toString());
				reportParams.put("idArticolo", schedaArticoloDTO.getArticolo().getId().toString());

				reportManager.exportReport(reportParams, "/" + schedaArticoloDTO.getAzienda()
						+ SCHEDA_ARTICOLO_REPORT_PATH, pathPDF, StampaSchedaArticoloBuilder.OUTPUT_FORMAT);
			} catch (EmptyReportException e) {
				logger.info("Report scheda articolo vuoto per l'articolo " + schedaArticoloDTO.getArticolo().getId());
			}

			// aggiorno lo stato della scheda articolo
			schedaArticolo.setStato(StatoScheda.STAMPATO);
			schedaArticolo = magazzinoSchedeArticoloManager.salvaSchedaArticolo(schedaArticolo);
			break;
		}
	}

	/**
	 * Restituisce la directory di salvataggio delle stampe delle schede articolo.
	 *
	 * @return directory
	 */
	private String getDirectoryExportPath() {
		String baseDir = "";

		try {
			baseDir = preferenceService.caricaPreference(StampaSchedaArticoloBuilder.OUTPUT_BASE_PATH_KEY).getValore();
		} catch (PreferenceNotFoundException e) {
			logger.error("--> errore durante il caricamento della chiave "
					+ StampaSchedaArticoloBuilder.OUTPUT_BASE_PATH_KEY, e);
		}

		return baseDir;
	}

	/**
	 * Esegue il login.
	 *
	 * @param nomeAzienda
	 *            azienda
	 */
	private void login(String nomeAzienda) {
		String username = new StringBuilder("internalAdmin#").append(nomeAzienda).append("#IT").toString();
		String credential = "internalEuropaSw";
		UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, credential);
		LoginContext loginContext;
		try {
			loginContext = new LoginContext("PanjeaLoginModule", passwordHandler);
			loginContext.login();
		} catch (LoginException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Comment for onMessage.
	 *
	 * @param message
	 *            messaggio
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void onMessage(Message message) {
		logger.debug("--> Enter onMessage");

		SchedaArticoloDTO schedaArticoloDTO = null;

		/* recupero l'object message e rieffettua il login */
		Object messageObject = null;
		try {
			messageObject = ((ObjectMessage) message).getObject();

			if (messageObject == null || !(messageObject instanceof SchedaArticoloDTO)) {
				return;
			}

			schedaArticoloDTO = (SchedaArticoloDTO) messageObject;
			login(schedaArticoloDTO.getAzienda());

			creaSchedaArticolo(schedaArticoloDTO);

		} catch (Exception e) {
			logger.error("--> errore, impossibile recuperare l'object scheda articolo dalla coda ", e);
			throw new RuntimeException(e);
		} finally {
			try {
				message.acknowledge();
			} catch (JMSException e) {
				logger.error("--> errore durante l'acknowledge del messaggio");
			}
		}
	}

}
