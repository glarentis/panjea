package it.eurotn.panjea.rich.editors.stampe.nuovo;

import it.eurotn.panjea.rich.bd.ILayoutStampeBD;
import it.eurotn.panjea.rich.bd.LayoutStampeBD;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.util.RcpSupport;

public class NuovoLayoutStampaCommand extends ApplicationWindowAwareCommand {

    public static final String COMMAND_ID = "aggiungiLayoutStampaCommand";

    private ILayoutStampeBD layoutStampeBD;

    {
        layoutStampeBD = RcpSupport.getBean(LayoutStampeBD.BEAN_ID);
    }

    private LayoutStampa layoutStampa;

    /**
     * Costruttore.
     */
    public NuovoLayoutStampaCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        layoutStampa = null;

        LayoutStampaPM layoutStampaPM = new LayoutStampaPM();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText && contents != null) {
            try {
                String clipboardContent = (String) contents.getTransferData(DataFlavor.stringFlavor);
                if (clipboardContent.startsWith("layout-")) {
                    layoutStampaPM.setReportName(clipboardContent.replaceAll("layout-", ""));

                    // cancello dalla clipboard il contenuto dopo aver aggiunto il layout
                    StringSelection stringSelection = new StringSelection("");
                    clipboard.setContents(stringSelection, null);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                logger.error("-->errore nel recuperare il contenuto della clipboard", ex);
            }
        }
        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(
                new NuovoLayoutStampaForm(layoutStampaPM), null) {

            @Override
            protected String getTitle() {
                return "Abilita nuovo layout generico";
            }

            @Override
            protected boolean onFinish() {
                Form nuovoLayoutForm = ((FormBackedDialogPage) getDialogPage()).getBackingFormPage();

                if (nuovoLayoutForm.getFormModel().isCommittable()) {
                    // salvo il nuovo layout per il tipo area
                    LayoutStampaPM layoutStampaPM = (LayoutStampaPM) nuovoLayoutForm.getFormModel().getFormObject();
                    layoutStampa = layoutStampeBD.aggiungiLayoutStampa(null, layoutStampaPM.getReportName(), null,
                            null);
                }
                return nuovoLayoutForm.getFormModel().isCommittable();
            }
        };
        dialog.setTitlePaneTitle("<html>Seleziona il layout di stampa generico</html>");
        dialog.showDialog();
    }

    /**
     * @return Returns the layoutStampa.
     */
    public LayoutStampa getLayoutStampa() {
        return layoutStampa;
    }

}
