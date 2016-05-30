package it.eurotn.panjea.contabilita.util;

public class ContabilizzazioneStateDescriptor extends AbstractStateDescriptor {

	private static final long serialVersionUID = 453192108718927279L;

	public static final int STATE_CONTABILIZZAZIONE = 0;

	/**
	 * Costruttore.
	 * 
	 * @param totalJobOperation
	 *            operazioni totale
	 * @param currentJobOperation
	 *            operazione corrente
	 */
	public ContabilizzazioneStateDescriptor(final int totalJobOperation, final int currentJobOperation) {
		super(STATE_CONTABILIZZAZIONE, totalJobOperation, currentJobOperation);
	}

}
