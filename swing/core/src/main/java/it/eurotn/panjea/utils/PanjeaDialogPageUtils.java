package it.eurotn.panjea.utils;

import javax.swing.JComponent;

import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.dialog.support.DialogPageUtils;

import it.eurotn.rich.editors.FormBackedMessagablePanel;

/**
 * Classe utility per gestire dialog pages. Fare riferimento a {@link DialogPageUtils}
 *
 * @author Leonardo
 *
 */
public class PanjeaDialogPageUtils extends DialogPageUtils {

    /**
     * Metodo per la creazione di un pannello Messagable legato alla dialogPage ricevuta come parametro.
     * 
     * @param dialogPage
     *            la dialogPage da legare al pannello Messagable
     * @return pannello creato
     */
    public static JComponent createFormBackedMessagablePanel(DialogPage dialogPage) {
        FormBackedMessagablePanel formBackedMessagablePanel = new FormBackedMessagablePanel();
        DialogPageUtils.addMessageMonitor(dialogPage, formBackedMessagablePanel);
        return formBackedMessagablePanel.getControl();
    }

}
