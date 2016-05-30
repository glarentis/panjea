package it.eurotn.panjea.anagrafica.util;

import java.io.Serializable;

public class PanjeaUpdateDescriptor implements Serializable {

	public enum UpdateOperation {
		START, END, ABORT
	}

	private static final long serialVersionUID = -8899420524474584114L;

	private String utente;

	private long delayUpdate;

	private UpdateOperation updateOperation;

	public static final String MESSAGE_SELECTOR = "PanjeaUpdateMessageSelector";

	/**
	 * Costruttore.
	 * 
	 * @param utente
	 *            utente che lancia il descriptor
	 * @param delayUpdate
	 *            tempo ( in secondi ) di ritardo
	 * @param updateOperation
	 *            operazione
	 */
	public PanjeaUpdateDescriptor(final String utente, final long delayUpdate, final UpdateOperation updateOperation) {
		super();
		this.utente = utente;
		this.delayUpdate = delayUpdate;
		this.updateOperation = updateOperation;
	}

	/**
	 * @return the delayUpdate
	 */
	public long getDelayUpdate() {
		return delayUpdate;
	}

	/**
	 * @return the updateOperation
	 */
	public UpdateOperation getUpdateOperation() {
		return updateOperation;
	}

	/**
	 * @return the utente
	 */
	public String getUtente() {
		return utente;
	}

}
