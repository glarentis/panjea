package it.eurotn.rich.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.dialog.AlertMessageAreaPane;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.alert.Alert;
import com.jidesoft.alert.AlertGroup;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.swing.PaintPanel;
import com.jidesoft.utils.PortingUtils;

import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * Classe wrapper che visualizza un alert di un messaggio. Se il testo del messaggio contiene un "\n" viene fatta la
 * split del testo e la prima stringa ottenuta verra' utitlizzata come titolo del messaggio dell'alert e messa in
 * grassetto mentre la parte rimanente verra' utilizzata come corpo del messaggio
 *
 * @author Fattazzo
 *
 */
public class MessageAlert {

    static class CloseAlertCommand extends ActionCommand {

        private final MessageAlert messageAlert;

        /**
         * Costruttore di default.
         *
         * @param alert
         *            l'alert da chiudere
         */
        public CloseAlertCommand(final MessageAlert alert) {
            super("closeAlertCommand");
            RcpSupport.configure(this);
            this.messageAlert = alert;
        }

        @Override
        protected void doExecuteCommand() {
            messageAlert.closeAlert();
        }

    }

    public static final String ALERT_GROUP_ID = "alertGroup";

    protected final CloseAlertCommand closeAlertCommand = new CloseAlertCommand(this);

    protected final Message message;
    private AlertMessageAreaPane messageAreaPane;
    protected final Alert alert;
    private final AlertGroup alertGroup;

    private List<ActionCommand> commands;

    /**
     * costruisce un dialogo che mostra un messaggio in popup.<br/>
     * imposta il timeout di default a 5 sec
     *
     * @param message
     *            messaggio da visualizzare
     */
    public MessageAlert(final Message message) {
        this(message, 5000, null);
    }

    /**
     * Costruisce un dialogo che mostra un messaggio in popup.
     *
     * @param message
     *            messaggio da visualizzare
     * @param timeout
     *            tempo dopo il quale il popup si chiude.
     */
    public MessageAlert(final Message message, final int timeout) {
        this(message, timeout, null);
    }

    /**
     * costruisce un dialogo che mostra un messaggio in popup.
     *
     * @param message
     *            messaggio da visualizzare
     * @param timeout
     *            tempo dopo il quale il popup si chiude.
     * @param commands
     *            i commands da visualizzare nel message alert
     */
    public MessageAlert(final Message message, final int timeout, final List<ActionCommand> commands) {
        super();
        this.message = message;
        this.alert = new Alert();
        alert.setTimeout(timeout);
        alert.setTransient(timeout > 0);

        // recupero l'alterGroup dai bean dell'applicazione
        this.alertGroup = (AlertGroup) Application.instance().getApplicationContext().getBean(ALERT_GROUP_ID);
        this.commands = commands;
        alertGroup.add(alert);
    }

    /**
     * Aggiunge il pulsante per chiudere l'alert.
     */
    public void addCloseCommandVisible() {
        if (commands == null) {
            commands = new ArrayList<ActionCommand>();
        }
        commands.add(closeAlertCommand);
    }

    /**
     * Applica le preferenze di visualizzazione dell'alert.
     */
    protected void applyAlertPreferences() {
        // preferenze generali dell'alert
        alert.setResizable(true);
        alert.setMovable(true);
        alert.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));

        // animazione di entrata
        CustomAnimation showAnimation = new CustomAnimation();
        showAnimation.setEffect(CustomAnimation.EFFECT_ZOOM);
        showAnimation.setSpeed(CustomAnimation.SPEED_MEDIUM);
        showAnimation.setSmoothness(CustomAnimation.SMOOTHNESS_ROUGH);
        showAnimation.setFunctionFade(CustomAnimation.FUNC_LINEAR);

        showAnimation.setType(CustomAnimation.TYPE_ENTRANCE);

        showAnimation.setDirection(CustomAnimation.RIGHT_UP);
        showAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        alert.setShowAnimation(showAnimation);

        // animazione di uscita
        CustomAnimation hideAnimation = new CustomAnimation();
        hideAnimation.setEffect(CustomAnimation.EFFECT_ZOOM);
        hideAnimation.setSpeed(CustomAnimation.SPEED_MEDIUM);
        hideAnimation.setSmoothness(CustomAnimation.SMOOTHNESS_ROUGH);
        hideAnimation.setFunctionFade(CustomAnimation.FUNC_LINEAR);

        hideAnimation.setType(CustomAnimation.TYPE_EXIT);

        hideAnimation.setDirection(CustomAnimation.RIGHT_UP);
        hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());

        alert.setHideAnimation(hideAnimation);

        // aggiungo l'alert all'alertGroup
        alertGroup.add(alert);
    }

    /**
     * Chiude il dialogo.
     */
    public void closeAlert() {
        PanjeaSwingUtil.runInEventDispatcherThread(new Runnable() {

            @Override
            public void run() {
                getAlert().hidePopup();
                getAlert().setVisible(false);
            }
        });
    }

    /**
     *
     * @return controlli per il messaggio.
     */
    protected JComponent createControl() {

        JComponent componentArea = getComponent();
        // setto tutti i compomenti trasparenti
        componentArea.setOpaque(false);
        for (Component component : componentArea.getComponents()) {
            if (component instanceof JComponent) {
                ((JComponent) component).setOpaque(false);
            }
        }
        PaintPanel panel = new PaintPanel(new BorderLayout());
        panel.add(componentArea, BorderLayout.CENTER);
        GuiStandardUtils.attachDialogBorder(componentArea);
        panel.setOpaque(true);
        panel.setBackgroundPaint(new GradientPaint(0, 0, new Color(231, 229, 224), 0, panel.getPreferredSize().height,
                new Color(212, 208, 200)));
        if (getCommands() != null) {
            JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            commandPanel.setOpaque(false);
            for (ActionCommand command : getCommands()) {
                commandPanel.add(command.createButton());
            }
            panel.add(commandPanel, BorderLayout.SOUTH);
        }
        return panel;
    }

    /**
     *
     * @return Alert
     */
    protected Alert getAlert() {
        return alert;
    }

    /**
     * @return close alert command
     */
    public ActionCommand getCloseAlertCommand() {
        return closeAlertCommand;
    }

    /**
     * @return the commands
     */
    public List<ActionCommand> getCommands() {
        return commands;
    }

    /**
     * @return componente contenuti nell'alert
     */
    protected JComponent getComponent() {
        messageAreaPane = new AlertMessageAreaPane();
        messageAreaPane.setMessage(message);
        return messageAreaPane.getControl();
    }

    /**
     * @return true se visibile, false altrimenti
     */
    public boolean isVisible() {
        return getAlert() != null && getAlert().isVisible();
    }

    /**
     * @param commands
     *            the commands to set
     */
    public void setCommands(List<ActionCommand> commands) {
        this.commands = commands;
    }

    /**
     * Visualizza l'alert.
     */
    public void showAlert() {
        this.alert.getContentPane().setLayout(new BorderLayout());
        JComponent control = createControl();
        this.alert.getContentPane().add(control, BorderLayout.CENTER);
        this.alert.getContentPane().setPreferredSize(new Dimension(400, 150));
        // applico le preferenze di visualizzazione del message alert.
        applyAlertPreferences();
        alertGroup.add(this.alert);
        this.alert.showPopup(SwingConstants.SOUTH_EAST);
    }
}
