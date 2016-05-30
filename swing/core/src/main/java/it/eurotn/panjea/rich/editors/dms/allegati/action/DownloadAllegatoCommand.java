package it.eurotn.panjea.rich.editors.dms.allegati.action;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.MessageDialog;

import com.jidesoft.list.ImagePreviewList;

import it.eurotn.panjea.rich.bd.IDmsBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class DownloadAllegatoCommand extends ActionAllegatoCommand {

    private static final Logger LOGGER = Logger.getLogger(DownloadAllegatoCommand.class);

    @Override
    public void execute(IDmsBD dmsBD, Long idDocument, ImagePreviewList list) {
        if (Desktop.isDesktopSupported()) {
            try {
                PanjeaSwingUtil.lockScreen("Download in corso");
                Desktop.getDesktop().open(dmsBD.getContent(idDocument));
            } catch (Exception e1) {
                LOGGER.error("--> errore durante il download dell'allegato", e1);
                new MessageDialog("Attenzione", e1.getMessage()).showDialog();
            } finally {
                PanjeaSwingUtil.unlockScreen();
            }
        }
    }

    @Override
    public boolean isInButtonArea(Rectangle cellBounds, Point clickPoint) {
        Rectangle downloadButtonBounds = new Rectangle(cellBounds.x + cellBounds.width - 22,
                cellBounds.y + cellBounds.height - 22, 16, 16);
        return downloadButtonBounds.contains(clickPoint);
    }

}
