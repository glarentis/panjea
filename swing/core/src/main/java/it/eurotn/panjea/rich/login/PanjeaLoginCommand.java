package it.eurotn.panjea.rich.login;

import java.awt.Dimension;
import java.awt.Window;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.WindowUtils;

import com.sun.javafx.stage.EmbeddedWindow;

public class PanjeaLoginCommand extends ActionCommand {

    private JDialog dialog = null;

    private JFXPanel jfxPanel = new JFXPanel();

    /**
     * Costruttore.
     * 
     */
    public PanjeaLoginCommand() {
        Platform.setImplicitExit(false);
    }

    @Override
    protected void doExecuteCommand() {

        getDialog().setVisible(true);
    }

    /**
     * @return dialog da usare per il login
     */
    private JDialog getDialog() {
        if (dialog == null) {
            dialog = new JDialog((Window) null, "Login");
            dialog.setPreferredSize(new Dimension(450, 180));
            dialog.setResizable(true);
            dialog.setUndecorated(true);
            dialog.setModal(true);
        }
        jfxPanel = new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                jfxPanel.setScene(new PanjeaLoginPage().createScene());
                ((EmbeddedWindow) jfxPanel.getScene().getWindow()).addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST,
                        new javafx.event.EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent paramT) {
                        jfxPanel = null;
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                dialog.setVisible(false);
                            }
                        });
                    }
                });
            }
        });
        dialog.setContentPane(jfxPanel);
        dialog.pack();
        WindowUtils.centerOnParent(dialog, JOptionPane.getRootFrame());

        return dialog;
    }
}
