/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.importazioni;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.list.ComboBoxListModel;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.importer.util.RigaImport;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author fattazzo
 *
 */
public class ImportazioniAMTablePage extends AbstractTablePageEditor<DocumentoImport> {

    private class CaricaDocumentiCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public CaricaDocumentiCommand() {
            super("caricaDocumentiCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            File fileImport = new File(fileImportTextField.getText());
            if (StringUtils.isBlank(fileImportTextField.getText()) || !fileImport.exists() || !fileImport.isFile()) {
                MessageDialog dialog = new MessageDialog("Attenzione",
                        "Specificare un file valido per l'importazione prima di continuare.");
                dialog.showDialog();
                return;
            }
            refreshData();
        }

    }

    private class ChooseFileImportActionCommand extends ActionCommand {

        private SettingsManager settingsManager = (SettingsManager) Application.services()
                .getService(SettingsManager.class);

        /**
         * Costruttore.
         */
        public ChooseFileImportActionCommand() {
            super("chooseFileImportActionCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            JFileChooser fileChooser = new JFileChooser(getImportDirectory());
            // fileChooser.setSelectedFile(new File(userDir + File.separator + fileName));
            int returnVal = fileChooser.showSaveDialog(Application.instance().getActiveWindow().getControl());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                fileImportTextField.setText(file.getAbsolutePath());

                // aggiorno la directory di salvataggio del tipo operazione
                try {
                    settingsManager.getUserSettings().setString(
                            "importazioniAreeMagazzino." + (String) importersComboBox.getSelectedItem(),
                            file.getParent());
                    settingsManager.getUserSettings().save();
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                                "--> Errore durante il salvataggio delle settings dell'importazione area magazzino.");
                    }
                }
            }
        }

        /**
         * Restituisce la directory di import del file. La directory viene recuperata dal file
         * .properties di panjea, se questa non è settata viene restituita la home dell'utente.
         *
         * @return directory di esportazione
         */
        private String getImportDirectory() {
            String dirExp = null;
            try {
                dirExp = settingsManager.getUserSettings()
                        .getString("importazioniAreeMagazzino." + (String) importersComboBox.getSelectedItem());
            } catch (SettingsException e) {
                logger.error("-->errore durante il caricamento delle settings", e);
            }

            if (dirExp == null || dirExp.isEmpty()) {
                dirExp = PanjeaSwingUtil.getHome().toString();
            }

            return dirExp;
        }

    }

    private class ComponentiHierarchicalTableComponentFactory implements HierarchicalTableComponentFactory {

        private JideTableWidget<RigaImport> tableWidget;

        @Override
        public Component createChildComponent(HierarchicalTable arg0, Object arg1, int arg2) {
            tableWidget = new JideTableWidget<RigaImport>("tableComponentiWidget", new ImportazioniAMRigaTableModel());
            tableWidget.getTable().getColumnModel().getColumn(0).setCellRenderer(new ValidazioneImportCellRenderer());
            tableWidget.getTable().getTableHeader().setReorderingAllowed(false);
            @SuppressWarnings("unchecked")
            List<RigaImport> righe = (List<RigaImport>) arg1;
            tableWidget.setRows(righe);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JLabel(new EmptyIcon(32, 16)), BorderLayout.WEST);
            panel.add(new JScrollPane(tableWidget.getTable()), BorderLayout.CENTER);
            return panel;
        }

        @Override
        public void destroyChildComponent(HierarchicalTable arg0, Component arg1, int arg2) {
            tableWidget.dispose();
        }
    }

    private class ImportaDocumentiCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public ImportaDocumentiCommand() {
            super("importaDocumentiCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            // il file è valido se:
            // 1. è stato specificato un file da importare ed esiste
            File fileImport = new File(fileImportTextField.getText());
            if (StringUtils.isBlank(fileImportTextField.getText()) || !fileImport.exists() || !fileImport.isFile()) {
                MessageDialog dialog = new MessageDialog("Attenzione",
                        "Specificare un file valido per l'importazione prima di continuare.");
                dialog.showDialog();
                return;
            }

            // 2. se non ci sono documenti o almeno 1 non è valido non importo
            List<DocumentoImport> documenti = getTable().getRows();
            boolean allValid = !documenti.isEmpty();
            for (DocumentoImport documentoImport : documenti) {
                allValid = allValid && documentoImport.isValid();
            }

            // se non ci sono documenti o almeno 1 non è valido non importo
            if (!allValid) {
                MessageDialog dialog = new MessageDialog("Attenzione",
                        "Nessun documento da importare o documenti non validi presenti.\n Impossibile continuare l'importazione.");
                dialog.showDialog();
                return;
            }

            try {
                List<AreaMagazzinoRicerca> areeCreate = magazzinoDocumentoBD.importaDocumenti(documenti,
                        (String) importersComboBox.getSelectedItem());
                ParametriRicercaAreaMagazzino parametri = new ParametriRicercaAreaMagazzino();
                parametri.setEffettuaRicerca(true);
                // parametri.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
                parametri.setAreeMagazzino(areeCreate);
                LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
                Application.instance().getApplicationContext().publishEvent(event);
            } finally {
                getTable().setRows(Collections.<DocumentoImport> emptyList());
                fileImportTextField.setText("");

                // sposto il file importato in una cartella di backup
                try {
                    File dirDest = new File(fileImport.getParentFile().getPath() + File.separator + "backup");
                    if (!dirDest.exists()) {
                        dirDest.mkdir();
                    }
                    FileUtils
                            .moveFile(fileImport,
                                    new File(fileImport.getParentFile().getPath() + File.separator
                                            + "backup" + File.separator + new SimpleDateFormat("yyyyMMdd_hhmmss")
                                                    .format(Calendar.getInstance().getTime())
                                            + "_" + fileImport.getName()));
                } catch (IOException e) {
                    MessageDialog dialog = new MessageDialog("Attenzione",
                            "Errore nel copiare il file importato nella cartella di backup.");
                    dialog.showDialog();
                }
            }
        }

    }

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private JComboBox<String> importersComboBox;
    private JTextField fileImportTextField;

    private CaricaDocumentiCommand caricaDocumentiCommand;
    private ImportaDocumentiCommand importaDocumentiCommand;
    private ChooseFileImportActionCommand chooseFileImportActionCommand;

    {
        importersComboBox = new JComboBox<String>();
        caricaDocumentiCommand = new CaricaDocumentiCommand();
        importaDocumentiCommand = new ImportaDocumentiCommand();
        chooseFileImportActionCommand = new ChooseFileImportActionCommand();
    }

    /**
     * Costruttore.
     *
     * @param pageId
     */
    protected ImportazioniAMTablePage() {
        super("importazioniAMTablePage", new ImportazioniAMDocumentoTableModel());
        getTable().setTableType(TableType.HIERARCHICAL);
        getTable().setHierarchicalTableComponentFactory(new ComponentiHierarchicalTableComponentFactory());
        ((HierarchicalTable) getTable().getTable()).setSingleExpansion(false);
        ((HierarchicalTable) getTable().getTable()).expandAllRows();
        getTable().getTable().getColumnModel().getColumn(0).setCellRenderer(new ValidazioneImportCellRenderer());

    }

    @SuppressWarnings("unchecked")
    @Override
    public JComponent getHeaderControl() {

        ComboBoxModel<String> model = new ComboBoxListModel();
        importersComboBox.setModel(model);

        fileImportTextField = getComponentFactory().createTextField();

        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:80dlu,fill:170dlu,left:pref,10dlu,left:pref,10dlu,left:pref",
                "4dlu,default,2dlu,default");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Tipo importazione", cc.xy(1, 2));
        builder.add(importersComboBox, cc.xy(3, 2));
        builder.addLabel("File", cc.xy(1, 4));
        builder.add(fileImportTextField, cc.xyw(3, 4, 2));
        builder.add(chooseFileImportActionCommand.createButton(), cc.xy(5, 4));

        builder.add(caricaDocumentiCommand.createButton(), cc.xy(7, 4));
        builder.add(importaDocumentiCommand.createButton(), cc.xy(9, 4));

        JPanel panel = builder.getPanel();
        GuiStandardUtils.attachBorder(panel);

        return panel;
    }

    @Override
    public void grabFocus() {
        importersComboBox.requestFocusInWindow();
    }

    @Override
    public Collection<DocumentoImport> loadTableData() {
        return null;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public Collection<DocumentoImport> refreshTableData() {

        String codiceImporter = (String) importersComboBox.getSelectedItem();
        byte[] fileImport = null;
        try {
            fileImport = FileUtils.readFileToByteArray(new File(fileImportTextField.getText()));
        } catch (IOException e) {
            fileImport = null;
        }

        return magazzinoDocumentoBD.caricaDocumenti(codiceImporter, fileImport);
    }

    @Override
    public void setFormObject(Object object) {
        List<String> importers = magazzinoDocumentoBD.caricaImporter();
        ((ComboBoxListModel) importersComboBox.getModel()).clear();
        ((ComboBoxListModel) importersComboBox.getModel()).addAll(importers);
    }

    /**
     * @param magazzinoDocumentoBD
     *            the magazzinoDocumentoBD to set
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

}
