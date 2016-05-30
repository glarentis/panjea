package it.eurotn.panjea.aton.rich.statusBarItem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.Timer;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.security.event.authentication.AbstractAuthenticationEvent;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.status.LabelStatusBarItem;

import it.eurotn.panjea.aton.rich.bd.IAtonBD;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.security.JecPrincipal;

/**
 * @author leonardo
 */
public class OrdiniDaImportareStatusBarItem extends LabelStatusBarItem implements ApplicationListener {

    private class CheckMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent evt) {
            super.mouseClicked(evt);
            if (MouseEvent.BUTTON1 == evt.getButton()) {
                ParametriRicercaOrdiniImportati parametriRicercaOrdiniImportati = new ParametriRicercaOrdiniImportati();
                parametriRicercaOrdiniImportati.setProvenienza(EProvenienza.TUTTI);
                LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaOrdiniImportati);
                Application.instance().getApplicationContext().publishEvent(event);
            } else if (MouseEvent.BUTTON3 == evt.getButton()) {
                ActionCommand command = RcpSupport.getCommand("atonImportCommand");
                command.execute();
            }
        }

    }

    private class CheckOrdiniListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            checkOrdini();
        }
    }

    private static final long serialVersionUID = -4926608394168995628L;
    private static final Color COLOR_ORDINI_PRESENTI_BLINK = Color.ORANGE;
    private Timer timerCheck;
    private IAtonBD atonBD;
    private CheckMouseListener checkMouseListener = null;
    private MessageAlert messageAlert = null;

    /**
     * Costruttore.
     */
    public OrdiniDaImportareStatusBarItem() {
        super();
        setIcon(null);
        setToolTipText("Nessun ordine rilevato.");
        setText("");
        setOpaque(true);
        messageAlert = null;
    }

    /**
     * Aggiorna i controlli in accordo al risultato della verifica degli ordini presenti da importare.
     */
    private void checkOrdini() {
        // Lo swingInteceptor me lo porta fuori dall' edt
        int[] numDocumenti;
        try {
            numDocumenti = atonBD.verificaOrdiniDaImportare();
            if (numDocumenti[0] != 0 || numDocumenti[1] != 0) {
                setVisible(true);
                setIcon(RcpSupport.getIcon("menu.gruppo.aton.onSale.icon"));
                setBackground(COLOR_ORDINI_PRESENTI_BLINK);
                setText(numDocumenti[0] + " FILE, " + numDocumenti[1] + " ORDINI");
                setToolTipText("Check ogni minuto");
            } else {
                nascondi();
            }
        } catch (Exception e1) {
            setVisible(true);
            setBackground(Color.RED);
            if (Application.isLoaded()
                    && (messageAlert == null || (messageAlert != null && !messageAlert.isVisible()))) {
                messageAlert = new MessageAlert(new DefaultMessage(e1.getMessage()));
                messageAlert.addCloseCommandVisible();
                messageAlert.showAlert();
            }
            setText("!");
        }
    }

    @Override
    protected void configureLabel(JLabel arg0) {
        super.configureLabel(arg0);
        ((JLabel) getComponent()).setOpaque(true);
    }

    @Override
    public String getItemName() {
        return "PanjeaUpdateStatusBarItem";
    }

    /**
     * Nasconde i componenti del bar item.
     */
    private void nascondi() {
        setVisible(false);
        setIcon(null);
        Color stdColor = Color.GRAY;
        setBackground(stdColor);
        setText("NESSUN ORDINE RILEVATO");
        setToolTipText("");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (checkMouseListener == null) {
            checkMouseListener = new CheckMouseListener();
            addMouseListener(checkMouseListener);
        }

        if (event instanceof AuthenticationSuccessEvent) {
            // Su loginEvent non ho le authority, devo trovare i permessi ciclando sulle authority
            JecPrincipal jecPrincipal = (JecPrincipal) ((AbstractAuthenticationEvent) event).getAuthentication();
            String[] ruoli = jecPrincipal.getPermissions();
            if (ArrayUtils.contains(ruoli, "atonimportazione") && ArrayUtils.contains(ruoli, "gestioneBackOrder")) {
                startCheck();
            } else {
                nascondi();
                stopCheck();
            }
        }
    }

    /**
     * @param atonBD
     *            the atonBD to set
     */
    public void setAtonBD(IAtonBD atonBD) {
        this.atonBD = atonBD;
    }

    /**
     * Inizio il timer per verificare gli ordini pronti.
     */
    private void startCheck() {
        if (timerCheck == null) {
            timerCheck = new Timer(60000, new CheckOrdiniListener());
            timerCheck.setInitialDelay(1000);
        }
        timerCheck.start();
    }

    /**
     * ferma il timer di controllo.
     */
    private void stopCheck() {
        if (timerCheck != null) {
            timerCheck.stop();
        }
    }

}
