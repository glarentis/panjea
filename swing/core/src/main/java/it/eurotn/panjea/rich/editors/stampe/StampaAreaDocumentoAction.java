package it.eurotn.panjea.rich.editors.stampe;

import it.eurotn.panjea.stampe.domain.LayoutStampa;

public interface StampaAreaDocumentoAction {
    /**
     *
     * @param layout
     *            layout da stampare
     * @param showPrintDialog
     *            visualizza il dialogo della stampante
     */
    void stampa(LayoutStampa layout, boolean showPrintDialog);
}