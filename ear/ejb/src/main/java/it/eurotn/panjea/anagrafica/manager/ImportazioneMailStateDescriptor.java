package it.eurotn.panjea.anagrafica.manager;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;

public class ImportazioneMailStateDescriptor extends AbstractStateDescriptor {

    private static final long serialVersionUID = 453192108718927279L;

    public static final int STATE_MAIL = 99;

    /**
     * Costruttore.
     * 
     * @param totalJobOperation
     *            operazioni totale
     * @param currentJobOperation
     *            operazione corrente
     */
    public ImportazioneMailStateDescriptor(final int totalJobOperation, final int currentJobOperation) {
        super(STATE_MAIL, totalJobOperation, currentJobOperation);
    }

}
