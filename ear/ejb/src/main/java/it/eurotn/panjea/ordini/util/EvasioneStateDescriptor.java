package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;

public class EvasioneStateDescriptor extends AbstractStateDescriptor {

	private static final long serialVersionUID = -7833899096823042957L;

	public static final int STATE_INSERIMENTO_RIGHE_EVASIONE = 2;
	public static final int STATE_VERIFICA_CARRELLO = 3;

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
	public EvasioneStateDescriptor(final int state, final int totalJobOperation, final int currentJobOperation) {
		super(state, totalJobOperation, currentJobOperation);
	}

}
