/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * @author fattazzo
 * 
 */
@Entity
@DiscriminatorValue("BYTE")
public abstract class AbstractByteArrayImporter extends AbstractImporter {

	private static final long serialVersionUID = -3554540100138931883L;

	@Transient
	private byte[] byteArray;

	/**
	 * @return the byteArray
	 */
	public byte[] getByteArray() {
		return byteArray;
	}

	/**
	 * @param byteArray
	 *            the byteArray to set
	 */
	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

}
