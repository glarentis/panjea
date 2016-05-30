package it.eurotn.panjea.vending.rich.editors.importazioneevadts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.io.FileUtils;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.vending.manager.evadts.importazioni.ImportazioneFileEvaDtsResult;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ImportazioneEvaDtsPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class ForceImportFileCommand implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeLogImport.getLastSelectedPathComponent();
            if (selectedNode.getUserObject() instanceof ImportazioneFileEvaDtsResult) {
                ImportazioneFileEvaDtsResult result = (ImportazioneFileEvaDtsResult) selectedNode.getUserObject();
                if (!result.getErrorImport().isEmpty()
                        && result.getNumeroRilevazioniPresenti() != result.getErrorImport().size()) {

                    byte[] fileContent;
                    try {
                        fileContent = FileUtils.readFileToByteArray(
                                new File(result.getFilePath() + File.separator + result.getFileName()));
                    } catch (IOException e1) {
                        selectedNode.setUserObject(result.getFileName());
                        selectedNode.removeAllChildren();

                        DefaultMutableTreeNode errorNode = new DefaultMutableTreeNode(
                                "Errore durante la lettura del file " + result.getFileName());
                        selectedNode.add(errorNode);

                        throw new PanjeaRuntimeException("Errore durante la lettura del file " + result.getFileName(),
                                e1);
                    }

                    new ImportFileTask(result.getFileName(), fileContent, result.getEvaDtsImportFolder(),
                            (DefaultMutableTreeNode) selectedNode.getParent(), selectedNode, treeLogImport, true)
                                    .execute();
                }
            }
        }
    }

    private JTree treeLogImport = new JTree(new DefaultTreeModel(null));

    private JPopupMenu popupMenu;

    /**
     * Costruttore.
     */
    public ImportazioneEvaDtsPage() {
        super("importazioneEvaDtsPage");
        popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(RcpSupport.getMessage("forceImportFileEvaDtsCommand.label"),
                RcpSupport.getIcon("forceImportFileEvaDtsCommand.icon"));
        menuItem.addActionListener(new ForceImportFileCommand());
        popupMenu.add(menuItem);
    }

    @Override
    protected JComponent createControl() {

        FormLayout layout = new FormLayout("right:pref,fill:pref:grow",
                "4dlu,default,10dlu,default,2dlu,fill:pref:grow");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Risultato importazione:", cc.xyw(1, 4, 2));

        treeLogImport.setRootVisible(false);
        treeLogImport.setOpaque(false);
        treeLogImport.setCellRenderer(new ImportazioneTreeRenderer());
        treeLogImport.setFocusable(false);
        treeLogImport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {

                if (SwingUtilities.isRightMouseButton(event)) {
                    int row = treeLogImport.getClosestRowForLocation(event.getX(), event.getY());
                    treeLogImport.setSelectionRow(row);
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeLogImport
                            .getLastSelectedPathComponent();
                    if (selectedNode.getUserObject() instanceof ImportazioneFileEvaDtsResult) {
                        ImportazioneFileEvaDtsResult result = (ImportazioneFileEvaDtsResult) selectedNode
                                .getUserObject();
                        if (result.getRilevazioniImportate() == 0 && !result.getErrorImport().isEmpty()
                                && result.getNumeroRilevazioniPresenti() != result.getErrorImport().size()) {
                            popupMenu.show(treeLogImport, event.getX(), event.getY());
                        }
                    }
                }
            }
        });
        builder.add(getComponentFactory().createScrollPane(treeLogImport), cc.xyw(1, 6, 2));

        builder.add(new ImportaFileEvaDtsCommand(treeLogImport).createButton(), cc.xy(1, 2));

        JPanel panel = builder.getPanel();
        GuiStandardUtils.attachBorder(panel);

        return panel;
    }

    @Override
    public void dispose() {
        // Non utilizzato
    }

    @Override
    public void loadData() {
        // Non utilizzato
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        // Non utilizzato
    }

    @Override
    public void preSetFormObject(Object object) {
        // Non utilizzato
    }

    @Override
    public void refreshData() {
        // Non utilizzato
    }

    @Override
    public void restoreState(Settings arg0) {
        // Non utilizzato
    }

    @Override
    public void saveState(Settings arg0) {
        // Non utilizzato
    }

    @Override
    public void setFormObject(Object object) {
        // Non utilizzato
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        // Non utilizzato
    }
}
