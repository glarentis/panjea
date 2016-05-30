/**
 *
 */
package it.eurotn.panjea.rich;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.DockingLayoutManager;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.image.ImageSource;

import com.jidesoft.spring.richclient.docking.JideApplicationWindow;

/**
 * Classe che genera la system tray e il suo menu per panjea.
 * 
 * @author fattazzo
 * @version 1.0, 20/apr/07
 * 
 */
public class PanjeaSystemTray {
    private Logger logger = Logger.getLogger(PanjeaSystemTray.class);

    private TrayIcon trayIcon;
    private SystemTray tray;
    private boolean isConfirmed = false;
    private final ApplicationWindow applicationWindow;

    /**
     * Costruttore.
     * 
     * @param applicationWindow
     *            applicationWindow
     */
    public PanjeaSystemTray(final ApplicationWindow applicationWindow) {
        super();
        this.applicationWindow = applicationWindow;

        if (SystemTray.isSupported()) {
            createSystemTray();
        }
    }

    /**
     * @return menu creato
     */
    private PopupMenu createMenuSystemTray() {

        PopupMenu menu = new PopupMenu();
        // Aggiungo il menu "Esci"
        MenuItem menuItem;
        menuItem = new MenuItem("Esci da PanJea - " + PanjeaSwingUtil.getUtenteCorrente().getCodiceAzienda());
        menuItem.getAccessibleContext().setAccessibleDescription("Chiude l'applicazione");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                        messageSource.getMessage("panjea.application.title.confirm.exit", new Object[] {},
                                Locale.getDefault()),
                        messageSource.getMessage("panjea.application.message.confirm.exit", new Object[] {},
                                Locale.getDefault())) {

                    @Override
                    protected void onConfirm() {
                        isConfirmed = true;
                    }
                };
                confirmationDialog.setPreferredSize(new Dimension(250, 50));
                confirmationDialog.setResizable(false);
                confirmationDialog.showDialog();

                if (isConfirmed) {
                    DockingLayoutManager.saveLayout(((JideApplicationWindow) applicationWindow).getDockingManager(),
                            applicationWindow.getPage().getId());

                    // chiudo l'applicazione
                    Application.instance().close(true, 0);
                }

            }
        });
        menu.add(menuItem);
        return menu;
    }

    /**
     * Crea la trayIcon.
     */
    private void createSystemTray() {
        tray = SystemTray.getSystemTray();
        // carico l'immagine della systemtray
        ImageSource imageSource = (ImageSource) Application.services().getService(ImageSource.class);
        Image trayImage = imageSource.getImage("trayIcon.image");

        trayIcon = new TrayIcon(trayImage, Application.instance().getName());
        trayIcon.setPopupMenu(createMenuSystemTray());
        trayIcon.setImageAutoSize(true);

        // se viene cliccata l'immagine visualizzo o nascondo l'applicazione nella systemtray
        trayIcon.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                applicationWindow.getControl().setVisible(!applicationWindow.getControl().isVisible());

                // se visualizzo l'applicazione la metto a tutto schermo altrimenti non lo fa
                if (applicationWindow.getControl().isVisible()) {
                    applicationWindow.getControl().setExtendedState(Frame.MAXIMIZED_BOTH);
                }
            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            logger.error("--> errore impossibile installare la trayIcon", e);
        }
    }
}
