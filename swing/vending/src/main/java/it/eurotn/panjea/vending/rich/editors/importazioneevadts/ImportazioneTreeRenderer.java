package it.eurotn.panjea.vending.rich.editors.importazioneevadts;

import java.awt.Color;
import java.awt.Component;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.commons.collections4.MapUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.manager.evadts.importazioni.ImportazioneFileEvaDtsResult;

public class ImportazioneTreeRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -5154697965369769634L;

    private Icon folderIcon = RcpSupport.getIcon("folder");

    private Icon resultcon = RcpSupport.getIcon("arrowRight");

    private Icon okIcon = RcpSupport.getIcon("importazione.ok.icon");
    private Icon warningIcon = RcpSupport.getIcon("importazione.warning.icon");
    private Icon errorIcon = RcpSupport.getIcon("importazione.error.icon");

    @Override
    public Color getBackground() {
        return null;
    }

    @Override
    public Color getBackgroundNonSelectionColor() {
        return null;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row,
                hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        switch (node.getDepth()) {
        case 0:
            label.setIcon(resultcon);
            break;
        case 1:
            label.setIcon(errorIcon);
            if (node.getUserObject() instanceof ImportazioneFileEvaDtsResult) {
                ImportazioneFileEvaDtsResult result = (ImportazioneFileEvaDtsResult) node.getUserObject();
                Map<Integer, String> errors = ((ImportazioneFileEvaDtsResult) node.getUserObject()).getErrorImport();

                label.setIcon(result.getRilevazioniImportate() > 0 || MapUtils.isEmpty(errors) ? okIcon
                        : result.getNumeroRilevazioniPresenti() == errors.size() ? errorIcon : warningIcon);
                label.setText("<html><b>" + result.getFileName() + "</b></html>");
            }
            break;
        case 2:
            label.setIcon(folderIcon);
            break;
        default:
            label.setIcon(folderIcon);
            break;
        }

        return label;
    }

}
