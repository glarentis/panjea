/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author fattazzo
 * 
 */
@Entity
@DiscriminatorValue("B_IO")
public abstract class AbstractBeanIOImporter extends AbstractByteArrayImporter {

	private static final long serialVersionUID = -5275704956527903588L;

	public static final String STREAM_NAME = "documenti";

	private String xmlTemplatePath;

	/**
	 * @return the xmlTemplatePath
	 */
	public String getXmlTemplatePath() {
		return xmlTemplatePath;
	}

	/**
	 * @param xmlTemplatePath
	 *            the xmlTemplatePath to set
	 */
	public void setXmlTemplatePath(String xmlTemplatePath) {
		this.xmlTemplatePath = xmlTemplatePath;
	}

}
