package it.eurotn.panjea.rich.editors.dms.allegati;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.list.ImagePreviewPanel;
import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.rich.editors.dms.allegati.action.ActionAllegatoCommand;

public class AllegatiImagePreviewPanel extends ImagePreviewPanel {

    private static final long serialVersionUID = 751584715597120938L;
    private JPanel southPanel;

    private JLabel titleLabel;

    /**
     * Costruttore.
     */
    public AllegatiImagePreviewPanel() {
        super();
        setShowDetails(0);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3),
                BorderFactory.createLineBorder(Color.GRAY, 1)));
        add(getSouthPanel(), BorderLayout.SOUTH);
    }

    private JPanel getSouthPanel() {
        if (southPanel == null) {
            southPanel = new JPanel(
                    new FormLayout("3px,     left:222px,     3px,    20px,   3px,    20px,  3px,    20px,3px", "20px"));
            southPanel.setBorder(null);
            southPanel.setBackground(Color.LIGHT_GRAY);
            titleLabel = new JLabel();
            CellConstraints cc = new CellConstraints();
            southPanel.add(titleLabel, cc.xy(2, 1));

            southPanel.add(ActionAllegatoCommand.creaComponente("deleteAllegatoCommand.icon"), cc.xy(4, 1));
            southPanel.add(ActionAllegatoCommand.creaComponente("webCommand.icon"), cc.xy(6, 1));
            southPanel.add(ActionAllegatoCommand.creaComponente("downloadAllegatoCommand.icon"), cc.xy(8, 1));
        }

        return southPanel;
    }

    /**
     * @param document
     *            the document to set
     */
    public void setDocument(WSDocument document) {
        titleLabel.setText(document.getTitle());
        setImageTitle(document.getTitle());

        StringBuilder sb = new StringBuilder(200);
        sb.append("<br><b>Directory:</b>");
        sb.append(document.getPath());
        sb.append("<br><b>Pubblicato da:</b>");
        sb.append(document.getPublisher());
        sb.append("<br><b>Data:</b>");
        sb.append(document.getCreation());
        sb.append("<br><b>Versione:</b>");
        sb.append(document.getVersion());
        setImageDescription(sb.toString());
    }

}
