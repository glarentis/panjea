package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import it.eurotn.panjea.anagrafica.util.PanjeaUpdateDescriptor;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.status.LabelStatusBarItem;

public class PanjeaUpdateStatusBarItem extends LabelStatusBarItem implements PropertyChangeListener {

    private class UpdateMessage extends DefaultMessage {

        private static final long serialVersionUID = 5908397577724117123L;

        /**
         * 
         * Costruttore.
         * 
         * @param text
         *            tempo di aggiornamento in formato testo
         */
        public UpdateMessage(final String text) {
            super("ATTENZIONE\n Panjea sarà aggiornato tra meno di " + text + " e l'applicazione verrà chisa.",
                    Severity.WARNING);
        }

    }

    private class UpdateTimerTask extends TimerTask {

        @Override
        public void run() {
            PanjeaSwingUtil.runInEventDispatcherThread(new Runnable() {

                @Override
                public void run() {

                    setText("Aggiornamento software tra " + formatSecondToTimer(counter));

                    // cambio i colori alla label
                    if (counter % 2 == 1) {
                        getComponent().setBackground(BGCOLOR);
                        getComponent().setForeground(FRCOLOR);
                    } else {
                        getComponent().setBackground(FRCOLOR);
                        getComponent().setForeground(BGCOLOR);
                    }

                    if (counter == 0) {

                        updateTimerTask.cancel();
                        setText(null);
                        setIcon(noUpdateIcon);
                        setToolTipText("Nessun aggiornamento programmato in corso.");
                        ((JLabel) getComponent()).setOpaque(false);
                        updateMessageAlert.closeAlert();

                        // se il contatore arriva a 0 e non sono io l'utente che ha lanciato l'aggiornamento, chiudo
                        // l'applicazione
                        if (!PanjeaSwingUtil.getUtenteCorrente().getUserName().equals(utente)) {
                            Application.instance().close(true, 0);
                        }
                    }

                    getComponent().repaint();

                    if (counter > 0) {
                        --counter;
                    }
                }
            });
        }

    }

    private static final long serialVersionUID = -298536902370573906L;

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");

    private long counter;

    private java.util.Timer timer;

    private Icon noUpdateIcon;
    private Icon updateIcon;

    private static final Color BGCOLOR = Color.RED; // UIManager.getColor("TextField.background");
    private static final Color FRCOLOR = Color.BLACK; // UIManager.getColor("TextField.foreground");

    private MessageAlert updateMessageAlert;

    private String utente;

    private UpdateTimerTask updateTimerTask;

    /**
     * Costruttore.
     * 
     */
    public PanjeaUpdateStatusBarItem() {
        super();
        PanjeaUpdateMessageDelegate messageDelegate = RcpSupport.getBean(PanjeaUpdateMessageDelegate.DELEGATE_ID);
        messageDelegate.addPropertyChangeListener(this);

        noUpdateIcon = RcpSupport.getIcon("noUpdateAvailableIcon");
        updateIcon = RcpSupport.getIcon("updateAvailableIcon");

        setIcon(noUpdateIcon);
        setToolTipText("Nessun aggiornamento programmato in corso.");
        setText("");

        updateTimerTask = new UpdateTimerTask();

        timer = new java.util.Timer();

        setOpaque(true);
    }

    @Override
    protected void configureLabel(JLabel jlabel) {
        super.configureLabel(jlabel);

        ((JLabel) getComponent()).setOpaque(true);
    }

    /**
     * Converte il formato testo ( HH:mm:ss ) il tempo espoesso in secondi.
     * 
     * @param timeInSecs
     *            tempo
     * @return testo
     */
    private String formatSecondToTimer(long timeInSecs) {
        if (timeInSecs >= 0) {
            long s = ((timeInSecs) % 60);
            long m = (((timeInSecs) / 60) % 60);
            long h = ((((timeInSecs) / 60) / 60) % 60);

            return DECIMAL_FORMAT.format(h) + ":" + DECIMAL_FORMAT.format(m) + ":" + DECIMAL_FORMAT.format(s);
        } else {
            return "";
        }
    }

    @Override
    public String getItemName() {
        return "PanjeaUpdateStatusBarItem";
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if (PanjeaUpdateMessageDelegate.MESSAGE_CHANGE.equals(event.getPropertyName())) {
            final PanjeaUpdateDescriptor stateDescriptor = (PanjeaUpdateDescriptor) event.getNewValue();

            counter = stateDescriptor.getDelayUpdate();
            if (counter == 0) {
                counter = 1;
            }
            utente = stateDescriptor.getUtente();

            switch (stateDescriptor.getUpdateOperation()) {
            case START:
                updateMessageAlert = new MessageAlert(new UpdateMessage(formatSecondToTimer(counter)), 5000);
                updateMessageAlert.showAlert();

                PanjeaSwingUtil.runInEventDispatcherThread(new Runnable() {
                    @Override
                    public void run() {
                        ((JLabel) getComponent()).setOpaque(true);
                        Font f = ((JLabel) getComponent()).getFont();
                        ((JLabel) getComponent()).setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
                        setIcon(updateIcon);
                        setToolTipText(null);
                    }
                });

                updateTimerTask = new UpdateTimerTask();
                timer.schedule(updateTimerTask, 0, 1000);
                break;
            default:
                PanjeaSwingUtil.runInEventDispatcherThread(new Runnable() {
                    @Override
                    public void run() {
                        setText(null);
                        setIcon(noUpdateIcon);
                        setToolTipText("Nessun aggiornamento programmato in corso.");
                        ((JLabel) getComponent()).setOpaque(false);

                        getComponent().repaint();
                    }
                });
                updateTimerTask.cancel();
                updateMessageAlert.closeAlert();
                break;
            }
        }
    }
}
