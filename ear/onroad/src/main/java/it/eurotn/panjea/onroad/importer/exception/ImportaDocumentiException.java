/**
 *
 */
package it.eurotn.panjea.onroad.importer.exception;

import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.onroad.domain.DocumentoOnRoad;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leonardo
 */
public class ImportaDocumentiException extends Exception {

	private static final long serialVersionUID = 5961423945658231684L;

	private List<LottiException> lottiExceptions = null;
	private List<DocumentoOnRoad> documentiDuplicati = null;

	public ImportaDocumentiException() {
		super();
	}

	public ImportaDocumentiException(String message) {
		super(message);
	}

	public ImportaDocumentiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImportaDocumentiException(Throwable cause) {
		super(cause);
	}

	public boolean add(LottiException e) {
		return getLottiExceptions().add(e);
	}

	public boolean addDocumentoDuplicato(DocumentoOnRoad documentoOnRoad) {
		return getDocumentiDuplicati().add(documentoOnRoad);
	}

	/**
	 * @return the documentiDuplicati
	 */
	public List<DocumentoOnRoad> getDocumentiDuplicati() {
		if (documentiDuplicati == null) {
			documentiDuplicati = new ArrayList<DocumentoOnRoad>();
		}
		return documentiDuplicati;
	}

	/**
	 * @return the lottiExceptions
	 */
	public List<LottiException> getLottiExceptions() {
		if (lottiExceptions == null) {
			lottiExceptions = new ArrayList<LottiException>();
		}
		return lottiExceptions;
	}

	/**
	 * @return empty
	 */
	public boolean isEmpty() {
		return getLottiExceptions().isEmpty() && getDocumentiDuplicati().isEmpty();
	}

}
