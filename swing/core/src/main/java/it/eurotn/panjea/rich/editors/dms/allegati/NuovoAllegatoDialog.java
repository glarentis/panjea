package it.eurotn.panjea.rich.editors.dms.allegati;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.rich.bd.IDmsBD;

public class NuovoAllegatoDialog extends ConfirmationDialog {

    private class SelezionaFileAllegatoCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public SelezionaFileAllegatoCommand() {
            super("selezionaFileAllegatoCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fc.showOpenDialog(Application.instance().getActiveWindow().getControl());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                fileAllegatoTextField.setText(file.getAbsolutePath());
                nomeAllegatoTextField.setText(FilenameUtils.getBaseName(file.getName()));
            }

        }

    }

    protected JTextField fileAllegatoTextField = new JTextField();

    protected JTextField nomeAllegatoTextField = new JTextField();

    protected JTextField directoryTextField = new JTextField();

    protected SelezionaFileAllegatoCommand selezionaFileAllegatoCommand;

    protected IDmsBD dmsBD;

    protected AllegatoDMS allegato;

    private String defaultFolder;

    /**
     * Costruttore.
     *
     * @param dmsBD
     *            bd
     * @param allegato
     *            attributi dell'allegato
     * @param defaultFolder
     *            cartella predefinita dove pubblicare l'allegato. Se null viene impostata alla
     *            "Altro" impostato nelle settings
     */
    public NuovoAllegatoDialog(final IDmsBD dmsBD, final AllegatoDMS allegato, final String defaultFolder) {
        super("Nuovo allegato", "_");
        this.dmsBD = dmsBD;
        this.allegato = allegato;
        this.defaultFolder = ObjectUtils.defaultIfNull(defaultFolder, dmsBD.caricaDmsSettings().getAltroFolder());
        setPreferredSize(new Dimension(600, 200));
        this.selezionaFileAllegatoCommand = new SelezionaFileAllegatoCommand();
    }

    @Override
    protected JComponent createDialogContentPane() {
        JPanel msgPanel = getComponentFactory()
                .createPanel(new FormLayout("right:pref, 4dlu, fill:90dlu, fill:default:grow,left:20dlu",
                        "20dlu, default,4dlu, default,4dlu, default"));
        CellConstraints cc = new CellConstraints();

        msgPanel.add(new JLabel("File"), cc.xy(1, 2));
        msgPanel.add(fileAllegatoTextField, cc.xyw(3, 2, 2));
        msgPanel.add(selezionaFileAllegatoCommand.createButton(), cc.xy(5, 2));

        msgPanel.add(new JLabel("Nome allegato"), cc.xy(1, 4));
        msgPanel.add(nomeAllegatoTextField, cc.xy(3, 4));

        msgPanel.add(new JLabel("Directory"), cc.xy(1, 6));
        msgPanel.add(directoryTextField, cc.xyw(3, 6, 2));

        directoryTextField.setText(defaultFolder);
        directoryTextField.setEditable(false);

        GuiStandardUtils.attachBorder(msgPanel);
        return msgPanel;
    }

    @Override
    protected String getCancelCommandId() {
        return "cancelCommand";
    }

    @Override
    protected String getFinishCommandId() {
        return "okCommand";
    }

    @Override
    protected void onConfirm() {
        dmsBD.pubblicaAllegato(directoryTextField.getText(), fileAllegatoTextField.getText(),
                nomeAllegatoTextField.getText(), allegato);
    }

}