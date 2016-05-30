package it.eurotn.panjea.magazzino.util;

import it.eurotn.panjea.contabilita.util.AbstractStateDescriptor;

public class FatturazioneStateDescriptor extends AbstractStateDescriptor {

    private static final long serialVersionUID = 7267094520962812832L;

    public static final int STATE_CREAZIONE_MOVIMENTI = 1;

    /**
     * Costruttore.
     * 
     * @param totalJobOperation
     *            operazioni totale
     * @param currentJobOperation
     *            operazione corrente
     */
    public FatturazioneStateDescriptor(final int totalJobOperation, final int currentJobOperation) {
        super(STATE_CREAZIONE_MOVIMENTI, totalJobOperation, currentJobOperation);
    }

    @Override
    public boolean isDone() {
        // in conferma dei documenti l'operazione è terminata solamente
        // se ho un errore (currentJonOperation==-1), in caso l'operazione non termina
        // perchè devo contabilizzare.
        return currentJobOperation == -1;
    }

}
