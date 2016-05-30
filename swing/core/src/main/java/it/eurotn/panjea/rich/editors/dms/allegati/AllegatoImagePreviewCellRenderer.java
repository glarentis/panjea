package it.eurotn.panjea.rich.editors.dms.allegati;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.jidesoft.list.DefaultListModelWrapper;
import com.jidesoft.list.ImagePreviewList;
import com.jidesoft.list.ImagePreviewList.PreviewImageIcon;
import com.jidesoft.list.ImagePreviewPanel;
import com.logicaldoc.webservice.document.WSDocument;

public class AllegatoImagePreviewCellRenderer extends AllegatiImagePreviewPanel
        implements ListCellRenderer<ImagePreviewList.PreviewImageIcon> {

    private static final long serialVersionUID = 5389825171913806766L;

    /**
     * Costruttore.
     */
    public AllegatoImagePreviewCellRenderer() {
        super();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends PreviewImageIcon> list, PreviewImageIcon value,
            int index, boolean isSelected, boolean cellHasFocus) {

        boolean bool = DefaultListModelWrapper.c;
        if ((bool) || (list instanceof ImagePreviewList)) {
            ImagePreviewList localImagePreviewList = (ImagePreviewList) list;
            setHighlightBackground(localImagePreviewList.getHighlightBackground());
            setGridBackground(localImagePreviewList.getGridBackground());
            setGridForeground(localImagePreviewList.getGridForeground());
            if (!(bool) && value instanceof AllegatoPreviewIcon) {
                AllegatoPreviewIcon localObject = (AllegatoPreviewIcon) value;
                WSDocument doc = localObject.getDocument();

                return updatePreview(localObject.getImageIcon(), localObject.getSize(), false, cellHasFocus, doc);
            }
        }
        return null;
    }

    private ImagePreviewPanel updatePreview(ImageIcon icon, Dimension dimension, boolean isSelected, boolean hasFocus,
            WSDocument document) {
        setIcon(icon);
        setImageSize(dimension);
        setSelected(isSelected);
        setFocused(hasFocus);
        setDocument(document);
        return this;
    }

}
