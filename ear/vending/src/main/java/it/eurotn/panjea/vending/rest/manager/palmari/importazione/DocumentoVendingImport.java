package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;

public class DocumentoVendingImport extends DocumentoImport {
    private static final long serialVersionUID = 1406530182783246182L;

    private Installazione installazione;
    private Operatore operatore;

    /**
     * @return Returns the installazione.
     */
    public Installazione getInstallazione() {
        return installazione;
    }

    /**
     * @return Returns the operatore.
     */
    public Operatore getOperatore() {
        return operatore;
    }

    /**
     * @param installazione
     *            The installazione to set.
     */
    public void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
    }

    /**
     * @param operatore
     *            The operatore to set.
     */
    public void setOperatore(Operatore operatore) {
        this.operatore = operatore;
    }

}
