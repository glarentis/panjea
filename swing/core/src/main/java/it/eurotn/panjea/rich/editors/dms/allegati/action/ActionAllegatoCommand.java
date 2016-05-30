package it.eurotn.panjea.rich.editors.dms.allegati.action;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.list.ImagePreviewList;

import it.eurotn.panjea.rich.bd.IDmsBD;

public abstract class ActionAllegatoCommand {
    /**
     *
     * @param idButton
     *            id button
     * @return component per il command
     */
    public static JComponent creaComponente(String idButton) {
        return new JLabel(RcpSupport.getIcon(idButton));
    }

    /**
     * Esegue il comando
     *
     * @param dmsBD
     *            bd
     * @param idDocument
     *            documento
     * @param list
     *            lista
     */
    public abstract void execute(final IDmsBD dmsBD, final Long idDocument, final ImagePreviewList list);

    /**
     *
     * @param cellBounds
     *            margini della cella
     * @param clickPoint
     *            coordinate click del mouse
     * @return true se il click è stato effettuato nell'area
     */
    public abstract boolean isInButtonArea(Rectangle cellBounds, Point clickPoint);

}
