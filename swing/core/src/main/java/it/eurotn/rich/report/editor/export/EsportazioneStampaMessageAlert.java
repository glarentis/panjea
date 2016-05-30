package it.eurotn.rich.report.editor.export;

import it.eurotn.rich.dialog.MessageAlert;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.image.IconSource;

public class EsportazioneStampaMessageAlert extends MessageAlert {

    private static Logger logger = Logger.getLogger(EsportazioneStampaMessageAlert.class);

    private JPanel rootPanel = new JPanel(new BorderLayout());

    private final JPanel infoPanel = new JPanel();
    private JLabel infoLabel = new JLabel("Esportazione della stampa in corso...");
    private final JLabel iconLabel = new JLabel();
    private JButton openFileButton = new JButton();
    private JButton openDirectoryButton = new JButton();
    private final JPanel buttonPanel = new JPanel(new GridLayout(0, 2));

    private final IconSource iconSource = (IconSource) ApplicationServicesLocator.services()
            .getService(IconSource.class);

    /**
     * Costruttore.
     */
    public EsportazioneStampaMessageAlert() {
        super(new DefaultMessage(""));

        openFileButton.setText("Apri file");
        openFileButton.setIcon(iconSource.getIcon("aprifile"));

        openDirectoryButton.setText("Apri cartella");
        openDirectoryButton.setIcon(iconSource.getIcon("apridirectory"));
    }

    /**
     * Costruttore.
     * 
     * @param text
     *            testo del messaggio
     */
    public EsportazioneStampaMessageAlert(final String text) {
        this();
        infoLabel = new JLabel(text);
    }

    @Override
    protected void applyAlertPreferences() {
        super.applyAlertPreferences();
        getAlert().setTimeout(0);
        getAlert().setTransient(false);
    }

    /**
     * Crea i componenti per il layout di errore.
     */
    private void createErrorLayout() {
        rootPanel.removeAll();

        infoLabel.setText("Errore durante l'esportazione. Controllare che il file non sia usato da un altro processo.");
        iconLabel.setIcon(getErrorIcon());
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        rootPanel.add(iconLabel, BorderLayout.LINE_START);

        infoPanel.setLayout(new BorderLayout());
        infoPanel.setOpaque(false);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));
        infoPanel.add(infoLabel, BorderLayout.NORTH);

        buttonPanel.setOpaque(false);
        openFileButton.setEnabled(false);
        openDirectoryButton.setEnabled(false);
        buttonPanel.add(openFileButton);
        buttonPanel.add(openDirectoryButton);

        infoPanel.add(buttonPanel, BorderLayout.SOUTH);

        rootPanel.add(infoPanel, BorderLayout.CENTER);
    }

    /**
     * Crea i componenti per il layout di fine esportazione.
     */
    private void createFinishLayout() {
        rootPanel.removeAll();

        iconLabel.setIcon(getInfoIcon());
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        rootPanel.add(iconLabel, BorderLayout.LINE_START);

        infoPanel.setLayout(new BorderLayout());
        infoPanel.setOpaque(false);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));
        infoPanel.add(infoLabel, BorderLayout.NORTH);

        buttonPanel.setOpaque(false);
        openFileButton.setText("Apri file");
        openFileButton.setIcon(iconSource.getIcon("aprifile"));
        openFileButton.setEnabled(true);
        openDirectoryButton.setText("Apri cartella");
        openDirectoryButton.setIcon(iconSource.getIcon("apridirectory"));
        openDirectoryButton.setEnabled(true);
        buttonPanel.add(openFileButton);
        buttonPanel.add(openDirectoryButton);

        infoPanel.add(buttonPanel, BorderLayout.SOUTH);

        rootPanel.add(infoPanel, BorderLayout.CENTER);
    }

    /**
     * Cambia il layout dell'alert per informare dell'errore.
     */
    public void errorExport() {

        createErrorLayout();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                EsportazioneStampaMessageAlert.this.getAlert().hidePopup();
            }
        }, 5000);
    }

    /**
     * Cambia il layout dell'alert per l'avvenuta esportazione fornendo le operazioni possibili sul file creato.
     * 
     * @param file
     *            file creato
     */
    public void finishExport(final File file) {

        infoLabel.setText("Esportazione generata con successo.");

        openFileButton.setAction(new AbstractAction() {

            private static final long serialVersionUID = -8831506215822009818L;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logger.debug("--> Apro il file: " + file.getName());

                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(file);
                            EsportazioneStampaMessageAlert.this.getAlert().hidePopup();
                        } catch (IOException e1) {
                            logger.error("-->errore ", e1);
                            EsportazioneStampaMessageAlert.this.getAlert().hidePopup();
                            new MessageDialog("Attenzione", e1.getMessage()).showDialog();
                        }
                    }
                } catch (Exception e2) {
                    logger.error("-->errore ", e2);
                    EsportazioneStampaMessageAlert.this.getAlert().hidePopup();
                    new MessageDialog("Attenzione", e2.getMessage()).showDialog();
                }
            }
        });

        openDirectoryButton.setAction(new AbstractAction() {

            private static final long serialVersionUID = -8831506215822009818L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(file.getParentFile());
                        EsportazioneStampaMessageAlert.this.getAlert().hidePopup();
                    } catch (IOException e1) {
                        logger.error("-->errore ", e1);
                        EsportazioneStampaMessageAlert.this.getAlert().hidePopup();
                        new MessageDialog("Attenzione", e1.getMessage()).showDialog();
                    }
                }
            }
        });

        createFinishLayout();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                EsportazioneStampaMessageAlert.this.getAlert().hidePopup();
            }
        }, 5000);
    }

    @Override
    protected JComponent getComponent() {

        iconLabel.setIcon(getInfoIcon());
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        rootPanel.add(iconLabel, BorderLayout.LINE_START);

        infoPanel.setLayout(new BorderLayout());
        infoPanel.setOpaque(false);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));
        infoPanel.add(infoLabel, BorderLayout.NORTH);

        buttonPanel.setOpaque(false);
        openFileButton.setEnabled(false);
        openDirectoryButton.setEnabled(false);
        buttonPanel.add(openFileButton);
        buttonPanel.add(openDirectoryButton);

        infoPanel.add(buttonPanel, BorderLayout.SOUTH);

        rootPanel.add(infoPanel, BorderLayout.CENTER);

        return rootPanel;
    }

    /**
     * Restituisce l'icona per il messaggio di errore.
     * 
     * @return icona
     */
    private Icon getErrorIcon() {
        return UIManager.getIcon("OptionPane.errorIcon");
    }

    /**
     * Restituisce l'icona per il messaggio di informazione.
     * 
     * @return icona
     */
    private Icon getInfoIcon() {
        return UIManager.getIcon("OptionPane.informationIcon");
    }
}
