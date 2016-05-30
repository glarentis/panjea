package it.eurotn.panjea.rich.editors.dms.allegati.action;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.URI;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.MessageDialog;

import com.jidesoft.list.ImagePreviewList;

import it.eurotn.panjea.rich.bd.IDmsBD;

public class OpenLinkAllegato extends ActionAllegatoCommand {

    private static final Logger LOGGER = Logger.getLogger(OpenLinkAllegato.class);

    @Override
    public void execute(IDmsBD dmsBD, Long idDocument, ImagePreviewList list) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop()
                        .browse(new URI(dmsBD.caricaDmsSettings().getServiceUrl() + "?docId=" + idDocument));
            } catch (Exception e1) {
                LOGGER.error("--> errore durante l'apertura del link dell'allegato", e1);
                new MessageDialog("Attenzione", e1.getMessage()).showDialog();
            }
        }
    }

    @Override
    public boolean isInButtonArea(Rectangle cellBounds, Point clickPoint) {
        Rectangle bounds = new Rectangle(cellBounds.x + cellBounds.width - 42, cellBounds.y + cellBounds.height - 22,
                16, 16);
        return bounds.contains(clickPoint);
    }

}
