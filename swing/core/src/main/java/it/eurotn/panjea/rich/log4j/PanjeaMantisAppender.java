package it.eurotn.panjea.rich.log4j;

import it.eurotn.panjea.rich.editors.update.PanjeaServer;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.security.JecPrincipal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.mantisbt.connect.IMCSession;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.axis.MCSession;
import org.mantisbt.connect.model.IIssue;
import org.mantisbt.connect.model.IProject;
import org.springframework.richclient.application.Application;

public class PanjeaMantisAppender extends AppenderSkeleton {

	private static final String HTTP_EUROTN_MANTISCONNECT = "http://eurotn.it/eurotnbgtrk/api/soap/mantisconnect.php";

	private static final String MANTIS_PROJECT_MAIL = "Mail";
	private static final String MANTIS_CATEGORY_PROJECT_MAIL = "errori";

	private IMCSession session = null;
	private Long projectMailId = null;
	private PanjeaServer panjeaServer = null;

	@Override
	public void activateOptions() {
		URL url = null;
		try {
			url = new URL(HTTP_EUROTN_MANTISCONNECT);
			session = new MCSession(url, "panjea", "europasw");

			IProject projectMail = session.getProject(MANTIS_PROJECT_MAIL);
			projectMailId = projectMail.getId();
		} catch (MalformedURLException e) {
			LogLog.error("MalformedURLException occured while initializing mantis log4j appender.");
		} catch (MCException e) {
			LogLog.error("MCException occured while initializing mantis log4j appender.");
		}
	}

	@Override
	protected void append(LoggingEvent arg0) {
		IIssue issue = null;
		try {
			if (projectMailId == null) {
				LogLog.warn("Mantis server non raggiungibile.");
				return;
			}
			String[] s = arg0.getThrowableStrRep();

			StringBuilder sbuf = new StringBuilder();
			if (s != null) {
				for (String element : s) {
					sbuf.append(element);
					sbuf.append(Layout.LINE_SEP);
				}
			}

			String summary = getSummary();
			String description = sbuf.toString();

			// NPE Mail projectMailId può arrivare null, il metodo createIssue prende un Long invece di long
			issue = createIssue(projectMailId, MANTIS_CATEGORY_PROJECT_MAIL, summary, description);
		} catch (Exception e) {
			LogLog.error("Exception nella creazione dell'issue da spedire a Mantis.");
		}
		try {
			if (issue != null) {
				session.addIssue(issue);
			}
		} catch (MCException e) {
			LogLog.error("MCException occured while adding the Issue in mantis log4j appender.");
		}
	}

	@Override
	public void close() {

	}

	/**
	 * Create Mantis Issue.
	 * 
	 * @param projectId
	 *            projectId
	 * @param projectCategory
	 *            projectCategory
	 * @param summary
	 *            summary
	 * @param description
	 *            description
	 * @return IIssue
	 */
	private IIssue createIssue(Long projectId, String projectCategory, String summary, String description) {
		IIssue issue = null;
		try {
			issue = session.newIssue(projectId);

			issue.setSummary(summary);
			issue.setDescription(description);
			issue.setCategory(projectCategory);
		} catch (MCException e) {
			LogLog.error("MCException occured while creating bug in log4j mantis appender.");
		}
		return issue;
	}

	/**
	 * @return recupera PanjeaServer dall'application context
	 */
	private PanjeaServer getPanjeaServer() {
		if (panjeaServer == null) {
			panjeaServer = (PanjeaServer) Application.instance().getApplicationContext().getBean("panjeaServer");
		}
		return panjeaServer;
	}

	/**
	 * @return le properties dove è salvata la versione di panjea
	 */
	private Properties getPanjeaVersionProperties() {
		return getPanjeaServer().getApplicationProperties();
	}

	/**
	 * @return The summary of the mantis issue "Panjea error # codAzienda # username # panjeaVersion
	 */
	private String getSummary() {
		StringBuilder summary = new StringBuilder("Panjea error");
		if (Application.isLoaded()) {
			JecPrincipal jecPrincipal = PanjeaSwingUtil.getUtenteCorrente();
			String user = jecPrincipal != null ? jecPrincipal.getUserName() : "";
			String codAz = jecPrincipal != null ? jecPrincipal.getCodiceAzienda() : "";
			Properties versionProperties = getPanjeaVersionProperties();
			String version = "";
			if (versionProperties != null) {
				version = (String) versionProperties.get("versione");
			}
			summary = summary.append(" #");
			summary = summary.append(codAz);
			summary = summary.append(" #");
			summary = summary.append(user);
			summary = summary.append(" #");
			summary = summary.append(version);
		}
		return summary.toString();
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

}
