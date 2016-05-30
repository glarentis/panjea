package it.eurotn.panjea.mail.service;

import java.io.File;

import javax.ejb.Local;

@Local
public interface JbossMailService {

	/**
	 * Invia una mail tramite il servizio mail di Jboss (configurazione mail-service.xml).
	 * 
	 * @param subject
	 *            subject
	 * @param text
	 *            text
	 * @param attachments
	 *            attachments
	 * @return true o false
	 */
	boolean send(String subject, String text, File[] attachments);

}
