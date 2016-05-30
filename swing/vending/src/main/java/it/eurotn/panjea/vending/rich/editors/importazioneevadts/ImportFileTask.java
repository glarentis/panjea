package it.eurotn.panjea.vending.rich.editors.importazioneevadts;

import java.io.File;
import java.util.Map.Entry;

import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.richclient.tree.TreeUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.manager.evadts.importazioni.ImportazioneFileEvaDtsResult;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;
import it.eurotn.panjea.vending.rich.bd.VendingDocumentoBD;

public class ImportFileTask extends SwingWorker<ImportazioneFileEvaDtsResult, Void> {

    private EvaDtsImportFolder folderSettings;
    private String fileName;
    private byte[] fileContent;
    private boolean forceImport;
    private DefaultMutableTreeNode folderNode;

    private IVendingDocumentoBD vendingDocumentoBD;
    private JTree tree;
    private DefaultMutableTreeNode fileNode;

    /**
     * Costruttore.
     *
     * @param fileName
     *            nome file
     * @param fileContent
     *            contenuto del file
     * @param folderSettings
     *            folder settings
     * @param folderNode
     *            nodo della folder di riferimento
     * @param fileNode
     *            nodo del file se presente
     * @param tree
     *            tree
     * @param forceImport
     *            forza l'importazione del file
     */
    public ImportFileTask(final String fileName, final byte[] fileContent, final EvaDtsImportFolder folderSettings,
            final DefaultMutableTreeNode folderNode, final DefaultMutableTreeNode fileNode, final JTree tree,
            final boolean forceImport) {
        super();
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.folderSettings = folderSettings;
        this.folderNode = folderNode;
        this.fileNode = fileNode;
        this.tree = tree;
        this.forceImport = forceImport;
        this.vendingDocumentoBD = RcpSupport.getBean(VendingDocumentoBD.BEAN_ID);
    }

    @Override
    protected ImportazioneFileEvaDtsResult doInBackground() throws Exception {
        ImportazioneFileEvaDtsResult result = vendingDocumentoBD.importaEvaDts(fileName, fileContent, folderSettings,
                forceImport);

        if (forceImport || MapUtils.isEmpty(result.getErrorImport())) {
            FileUtils.forceDelete(new File(folderSettings.getFolder() + File.separator + fileName));
        }

        return result;
    }

    @Override
    protected void done() {
        try {
            ImportazioneFileEvaDtsResult result = get();

            if (fileNode == null) {
                fileNode = new DefaultMutableTreeNode(result);
                folderNode.add(fileNode);
            } else {
                fileNode.setUserObject(result);
                fileNode.removeAllChildren();
            }

            if (result.getNumeroRilevazioniPresenti() > 0) {

                String message = "<html>" + result.getNumeroRilevazioniPresenti()
                        + " rilevazioni presenti nel file. <b>" + result.getRilevazioniImportate()
                        + " importate.</b></html>";
                DefaultMutableTreeNode rilevazioniNode = new DefaultMutableTreeNode(message);
                fileNode.add(rilevazioniNode);
            }
            for (Entry<Integer, String> error : result.getErrorImport().entrySet()) {
                String errorMsg = error.getKey() != null ? "Rilevazione " + error.getKey() + ": " : "";
                errorMsg += error.getValue();
                DefaultMutableTreeNode errorNode = new DefaultMutableTreeNode(errorMsg);
                fileNode.add(errorNode);
            }

            ((DefaultTreeModel) tree.getModel()).reload();
            TreeUtils.expandAll(tree, true);
        } catch (Exception e) {
            throw new PanjeaRuntimeException("Errore durante l'importazione del file.", e);
        }
    }
}