package it.eurotn.panjea.rich.editors.dms.allegati.action;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.DefaultListModel;

import org.springframework.richclient.dialog.ConfirmationDialog;

import com.jidesoft.list.ImagePreviewList;

import it.eurotn.panjea.rich.bd.IDmsBD;

public class CancellaAllegatoCommand extends ActionAllegatoCommand {

    @Override
    public void execute(final IDmsBD dmsBD, final Long idDocument, final ImagePreviewList list) {
        ConfirmationDialog dialog = new ConfirmationDialog("Conferma cancellazione",
                "Vuoi cancellare l'allegato selezionato?") {

            @Override
            protected void onConfirm() {
                dmsBD.deleteAllegato(idDocument);
                ((DefaultListModel<?>) list.getModel()).remove(list.getSelectedIndex());

            }
        };
        dialog.showDialog();
    }

    @Override
    public boolean isInButtonArea(Rectangle cellBounds, Point clickPoint) {
        Rectangle deleteBounds = new Rectangle(cellBounds.x + cellBounds.width - 65,
                cellBounds.y + cellBounds.height - 22, 16, 16);
        return deleteBounds.contains(clickPoint);
    }

}
