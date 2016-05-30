/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.panjea.contabilita.rich.rules;

import it.eurotn.panjea.rich.rules.ConfrontoPropertiesConstraint;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.rules.closure.BinaryConstraint;

/**
 * Estende confrontoPropertiesConstraint per verificare in ordine un campo intero che rappresenta un anno "aaaa" e un
 * campo data (java.util.Date) secondo la BinaryConstraint passata.
 * 
 * @author Leonardo
 */
public class ConfrontoDataAnnoConstraint extends ConfrontoPropertiesConstraint {

	private static Logger logger = Logger.getLogger(ConfrontoDataAnnoConstraint.class);

	private MessageSource messageSource;

	/**
	 * Costruttore.
	 * 
	 * @param dateProperty
	 *            nome proprietà della data
	 * @param beanPropertyExpression
	 *            beanPropertyExpression
	 * @param annoProperty
	 *            anno di confronto
	 */
	public ConfrontoDataAnnoConstraint(final String dateProperty, final BinaryConstraint beanPropertyExpression,
			final String annoProperty) {
		super(dateProperty, beanPropertyExpression, annoProperty);

		messageSource = (MessageSource) ApplicationServicesLocator.services().getService(MessageSource.class);
	}

	@Override
	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		logger.debug("--> domainObjectAccessStrategy " + domainObjectAccessStrategy);
		Object data = domainObjectAccessStrategy.getPropertyValue(getPropertyName());
		Object anno = domainObjectAccessStrategy.getPropertyValue(getOtherPropertyName());

		logger.debug("--> data " + data);
		logger.debug("--> anno " + anno);

		// se l'anno e' sotto forma di stringa allora eseguo un parseInt
		if (anno instanceof String) {
			if (((String) anno).equals("")) {
				// se il valore anno String e' vuoto evito di fare il parse che mi
				// lancia una exception.
				return false;
			}
			try {
				anno = Integer.parseInt((String) anno);
			} catch (Exception e) {
				logger.error("--> Errore nel parese della stringa anno in int anno", e);
				// non c'e' un valore valido per il campo anno quindi non passo la validazione
				// ritornando false
				return false;
			}
		}

		boolean testResult = false;
		if (anno != null && data != null) {
			Date date = (Date) data;
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			int annoDate = cal.get(Calendar.YEAR);
			// getConstraint usa la binaryConstraint passata per eseguire il test
			testResult = getConstraint().test(annoDate, anno);
		}
		logger.debug("--> testResult per la verifica di date " + testResult);
		return testResult;
	}

	@Override
	public String toString() {
		String annoData = messageSource.getMessage("anno.label", new Object[] {}, Locale.getDefault());
		String annoConfronto = messageSource.getMessage(getOtherPropertyName(), new Object[] {}, Locale.getDefault());
		return annoData + getConstraint().toString() + annoConfronto;
	}
}
