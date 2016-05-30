package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import java.awt.Dimension;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.table.EditFrameChangeSelectionConstraint;

public class RigheMagazzinoEditFrameChangeSelectionConstraint extends EditFrameChangeSelectionConstraint {

    @Override
    public boolean test(IPageEditor page) {

        if (page.getPageObject() instanceof RigaArticolo && !((RigaArticolo) page.getPageObject()).isLottiValid()) {
            String title = RcpSupport.getMessage("rigaArticolo.lotti.title.error.validation");
            Message message = new DefaultMessage(RcpSupport.getMessage("rigaArticolo.lotti.message.error.validation"),
                    Severity.WARNING);
            MessageDialog messageDialog = new MessageDialog(title, message);
            messageDialog.setPreferredSize(new Dimension(250, 50));
            messageDialog.setResizable(false);
            messageDialog.showDialog();
            return false;
        } else {
            return super.test(page);
        }
    }

}
