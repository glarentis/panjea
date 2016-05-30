package it.eurotn.panjea.vending.rich.editors.importazioneevadts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.bd.VendingAnagraficaBD;

public class ImportaFileEvaDtsCommand extends ActionCommand {

    private static final Logger LOGGER = Logger.getLogger(ImportaFileEvaDtsCommand.class);

    private JTree treeLogImport;

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore.
     *
     * @param treeLogImport
     *            tree
     */
    public ImportaFileEvaDtsCommand(final JTree treeLogImport) {
        super("importaFileEvaDtsCommand");
        RcpSupport.configure(this);

        this.treeLogImport = treeLogImport;
        this.vendingAnagraficaBD = RcpSupport.getBean(VendingAnagraficaBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        treeLogImport.removeAll();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        treeLogImport.setModel(new DefaultTreeModel(rootNode));

        List<EvaDtsImportFolder> folders = vendingAnagraficaBD.caricaVendingSettings().getImportFolder();

        Map<EvaDtsImportFolder, File[]> mapFiles = new HashMap<>();
        for (EvaDtsImportFolder folder : folders) {
            File importFolder = new File(folder.getFolder());
            if (importFolder.exists() && importFolder.isDirectory()) {
                File[] fileList = importFolder.listFiles();
                mapFiles.put(folder, fileList);
            }
        }

        for (Entry<EvaDtsImportFolder, File[]> entry : mapFiles.entrySet()) {
            DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(entry.getKey().getFolder());
            rootNode.add(folderNode);

            for (File file : entry.getValue()) {
                String fileName = FilenameUtils.getName(file.getPath());

                byte[] fileContent;
                try {
                    fileContent = FileUtils.readFileToByteArray(file);
                } catch (IOException e1) {
                    DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(fileName);
                    folderNode.add(fileNode);

                    DefaultMutableTreeNode errorNode = new DefaultMutableTreeNode(
                            "Errore durante la lettura del file " + fileName);
                    fileNode.add(errorNode);

                    LOGGER.error("--> errore durante la lettura del file " + fileName, e1);
                    throw new PanjeaRuntimeException("Errore durante la lettura del file " + fileName);
                }

                new ImportFileTask(fileName, fileContent, entry.getKey(), folderNode, null, treeLogImport, false)
                        .execute();

            }
        }
    }

}