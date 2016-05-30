package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;

public abstract class AbstractStateDescriptor implements Serializable {

	private static final long serialVersionUID = -3784913011664882735L;

	private int state;

	protected int totalJobOperation;

	protected int currentJobOperation;

	/**
	 * Costruttore.
	 * 
	 * @param state
	 *            stato dell'operazione
	 * @param totalJobOperation
	 *            operazioni totale
	 * @param currentJobOperation
	 *            operazione corrente
	 */
	public AbstractStateDescriptor(final int state, final int totalJobOperation, final int currentJobOperation) {
		super();
		this.state = state;
		this.totalJobOperation = totalJobOperation;
		this.currentJobOperation = currentJobOperation;
	}

	/**
	 * @return the currentJobOperation
	 */
	public int getCurrentJobOperation() {
		return currentJobOperation;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @return the totalJobOperation
	 */
	public int getTotalJobOperation() {
		return totalJobOperation;
	}

	/**
	 * 
	 * @return true se l'operazione è terminata oppure se il current job ha avuto un errore (segnalato con
	 *         currentjob==-1)
	 */
	public boolean isDone() {
		return (getTotalJobOperation() == getCurrentJobOperation());
	}
}
