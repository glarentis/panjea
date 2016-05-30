package it.eurotn.panjea.fatturepa.rich.editors.ricerca.xmlaction;

import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;

public final class DeleteXMLFatturaPAAction {

    /**
     * Costruttore.
     */
    private DeleteXMLFatturaPAAction() {
    }

    /**
     * Cancella l'XML e l'XML firmato dell'area magazzino. Il prograssivo legato all'XML creato viene mantenuto per
     * essere utilizzato su quello che verrà generato.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     */
    public static void cancellaXMLFatturaPA(final Integer idAreaMagazzino) {

        ConfirmationDialog confirmationDialog = new ConfirmationDialog("ATTENZIONE",
                "Cancellare i file XML presenti per il documento selezionato?") {

            @Override
            protected void onConfirm() {
                IFatturePABD fatturePABD = RcpSupport.getBean(FatturePABD.BEAN_ID);
                fatturePABD.cancellaXMLFatturaPA(idAreaMagazzino);
            }
        };
        confirmationDialog.showDialog();
    }
}
