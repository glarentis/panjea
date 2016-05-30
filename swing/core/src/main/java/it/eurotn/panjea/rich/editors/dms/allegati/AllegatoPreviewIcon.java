package it.eurotn.panjea.rich.editors.dms.allegati;

import javax.swing.ImageIcon;

import com.jidesoft.list.DefaultPreviewImageIcon;
import com.logicaldoc.webservice.document.WSDocument;

public class AllegatoPreviewIcon extends DefaultPreviewImageIcon {

    private WSDocument document;

    /**
     * Costruttore.
     *
     * @param imageIcon
     *            image
     * @param document
     *            document
     */
    public AllegatoPreviewIcon(final ImageIcon imageIcon, final WSDocument document) {
        super(imageIcon, document.getTitle());
        this.document = document;
    }

    /**
     * @return the document
     */
    public WSDocument getDocument() {
        return document;
    }

}
