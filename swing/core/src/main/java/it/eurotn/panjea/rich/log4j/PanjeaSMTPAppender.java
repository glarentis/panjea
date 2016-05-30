/**
 *
 */
package it.eurotn.panjea.rich.log4j;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.rich.bd.IPreferenceBD;
import it.eurotn.panjea.rich.editors.update.PanjeaServer;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.security.JecPrincipal;

import java.util.Date;
import java.util.Properties;

import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.richclient.application.Application;

/**
 * SMTP appender per log4j che spedisce una mail usando come soggetto denominazione azienda e utente corrente, mentre
 * come corpo della mail stampa lo stack trace dell'exception sollevata.<br>
 * Le chiavi da definire per la configurazione dell'invio email con i valori di default (se non sono definiti nelle
 * preference), sono le seguenti:
 * <ul>
 * <li>log4j.appender.MAIL.To=panjea@eurotn.it
 * <li>log4j.appender.MAIL.From=panjea@eurotn.it
 * <li>log4j.appender.MAIL.SMTPHost=mail.eurotn.it
 * </ul>
 * 
 * @author Leonardo
 */
public class PanjeaSMTPAppender extends SMTPAppender {

	public static final String LOG4J_MAIL_TO = "log4j.appender.MAIL.To";
	public static final String LOG4J_MAIL_FROM = "log4j.appender.MAIL.From";
	public static final String LOG4J_MAIL_SMTP_HOST = "log4j.appender.MAIL.SMTPHost";
	private IPreferenceBD preferenceBD = null;
	private PanjeaServer panjeaServer = null;

	@Override
	public void activateOptions() {
		try {
			Preference fromP = getPreferenceBD().caricaPreference(LOG4J_MAIL_FROM);
			String from = fromP.getValore();
			if (from != null && !from.equals("")) {
				setFrom(from);
			}
		} catch (Exception e) {
		}

		try {
			Preference toP = getPreferenceBD().caricaPreference(LOG4J_MAIL_TO);
			String to = toP.getValore();
			if (to != null && !to.equals("")) {
				setTo(to);
			}
		} catch (Exception e) {
		}
		try {
			Preference smtpP = getPreferenceBD().caricaPreference(LOG4J_MAIL_SMTP_HOST);
			String smtp = smtpP.getValore();
			if (smtp != null && !smtp.equals("")) {
				setSMTPHost(smtp);
			}
		} catch (Exception e) {
		}
		super.activateOptions();
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
	 * @return PreferenceBD
	 */
	private IPreferenceBD getPreferenceBD() {
		if (preferenceBD == null) {
			preferenceBD = (IPreferenceBD) Application.instance().getApplicationContext().getBean("preferenceBD");
		}
		return preferenceBD;
	}

	/**
	 * http://lajosd.blogspot.com/2009/09/log4j-smtpappender-exception-info-in.html.
	 * 
	 * @author ldomaniczky
	 */
	@Override
	protected void sendBuffer() {
		// Note: this code already owns the monitor for this
		// appender. This frees us from needing to synchronize on 'cb'.
		try {
			MimeBodyPart part = new MimeBodyPart();
			StringBuffer sbuf = new StringBuffer();
			String t = layout.getHeader();
			if (t != null) {
				sbuf.append(t);
			}
			int len = cb.length();
			for (int i = 0; i < len; i++) {
				// sbuf.append(MimeUtility.encodeText(layout.format(cb.get())));
				LoggingEvent event = cb.get();
				// setting the subject
				if (i == 0) {
					String sbj = getSubject();
					if (Application.isLoaded()) {
						JecPrincipal jecPrincipal = PanjeaSwingUtil.getUtenteCorrente();
						String user = jecPrincipal != null ? jecPrincipal.getUserName() : "";
						String codAz = jecPrincipal != null ? jecPrincipal.getCodiceAzienda() : "";
						Properties versionProperties = getPanjeaVersionProperties();
						String version = "";
						if (versionProperties != null) {
							version = (String) versionProperties.get("versione");
						}
						sbj = sbj + " # " + codAz + " # " + user + " # " + version;
					}
					msg.setSubject(sbj);
				}
				sbuf.append(layout.format(event));
				if (layout.ignoresThrowable()) {
					String[] s = event.getThrowableStrRep();
					if (s != null) {
						for (String element : s) {
							sbuf.append(element);
							sbuf.append(Layout.LINE_SEP);
						}
					}
				}
			}
			t = layout.getFooter();
			if (t != null) {
				sbuf.append(t);
			}
			part.setContent(sbuf.toString(), layout.getContentType());
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(part);
			msg.setContent(mp);
			msg.setSentDate(new Date());
			Transport.send(msg);
		} catch (Exception e) {
			LogLog.error("Error occured while sending e-mail notification.");
		}
	}
}
