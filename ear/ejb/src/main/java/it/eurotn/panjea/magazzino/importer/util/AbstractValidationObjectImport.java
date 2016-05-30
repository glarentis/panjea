/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.util;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author fattazzo
 * 
 */
public abstract class AbstractValidationObjectImport implements Serializable {

	private static final long serialVersionUID = 9202455528099449101L;

	private String validationMessage;

	/**
	 * @return the validationMessageHtml
	 */
	public String getValidationMessageHtml() {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isBlank(validationMessage)) {
			sb = new StringBuilder("<HTML><b>Errori riscontrati:<b><ul>");
			String[] messages = StringUtils.split(validationMessage, "#");
			for (String msg : messages) {
				sb.append("<li>");
				sb.append(msg);
				sb.append("</li>");
			}
			sb.append("</ul></HTML>");
		}

		return sb.toString();
	};

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return StringUtils.isBlank(validationMessage);
	};

	/**
	 * @param validationMessage
	 *            the validationMessage to set
	 */
	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	/**
	 * Valida l'oggetto importato
	 */
	public abstract void valida();

}
