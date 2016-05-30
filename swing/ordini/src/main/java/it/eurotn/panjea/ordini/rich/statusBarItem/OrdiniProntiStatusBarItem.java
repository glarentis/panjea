package it.eurotn.panjea.ordini.rich.statusBarItem;

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
import org.springframework.richclient.util.RcpSupport;
import org.springframework.security.event.authentication.AbstractAuthenticationEvent;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.status.LabelStatusBarItem;

import it.eurotn.panjea.ordini.domain.OrdiniSettings;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.rich.bd.AnagraficaOrdiniBD;
import it.eurotn.panjea.ordini.rich.bd.IAnagraficaOrdiniBD;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.security.JecPrincipal;

public class OrdiniProntiStatusBarItem extends LabelStatusBarItem implements ApplicationListener {

    private class CheckMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (MouseEvent.BUTTON1 == e.getButton()) {
                LifecycleApplicationEvent event = new OpenEditorEvent(new DistintaCarico(null));
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }

    }

    private class CheckOrdiniListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Lo swingInteceptor me lo porta fuori dall' edt
            Integer numDocumenti;
            try {
                numDocumenti = ordiniDocumentoBD.verificaNumeroOrdiniDaEvadere();
                if (numDocumenti > 0) {
                    setVisible(true);
                    setBackground(COLOR_ORDINI_PRESENTI_BLINK);
                    setText(numDocumenti + " ORDINI DA EVADERE");
                    setToolTipText("Numero ordini da evadere " + numDocumenti);
                } else {
                    hideStatusBarItem();
                }
            } catch (Exception e1) {
                setVisible(true);
                setText("*");
            }
        }
    }

    private static final long serialVersionUID = -298536902370573906L;

    private static final Color COLOR_ORDINI_PRESENTI_BLINK = Color.YELLOW;

    private Timer timerCheck;

    private IOrdiniDocumentoBD ordiniDocumentoBD;

    private CheckMouseListener checkMouseListener = null;

    /**
     * Costruttore.
     */
    public OrdiniProntiStatusBarItem() {
        super();

        // noUpdateIcon = RcpSupport.getIcon("noUpdateAvailableIcon");
        // updateIcon = RcpSupport.getIcon("updateAvailableIcon");

        // setIcon(noUpdateIcon);
        setToolTipText("Nessun ordine da evadere.");
        setText("");
        setOpaque(true);
    }

    @Override
    protected void configureLabel(JLabel jlabel) {
        super.configureLabel(jlabel);
        ((JLabel) getComponent()).setOpaque(true);
    }

    @Override
    public String getItemName() {
        return "PanjeaUpdateStatusBarItem";
    }

    /**
     * @return Returns the ordiniDocumentoBD.
     */
    public IOrdiniDocumentoBD getOrdiniDocumentoBD() {
        return ordiniDocumentoBD;
    }

    /**
     * Hides the status bar item.
     */
    private void hideStatusBarItem() {
        setVisible(false);
        Color stdColor = Color.GRAY;
        setBackground(stdColor);
        setText("NESSUN ORDINE DA EVADERE");
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

            IAnagraficaOrdiniBD anagraficaOrdiniBD = RcpSupport.getBean(AnagraficaOrdiniBD.BEAN_ID);
            OrdiniSettings ordiniSettings = anagraficaOrdiniBD.caricaOrdiniSettings();

            /**
             * Sull'evento di login non ho l'oggetto nel context. Devo quindi recuperarlo
             * dall'evento
             */
            JecPrincipal jecPrincipal = (JecPrincipal) ((AbstractAuthenticationEvent) event).getAuthentication();
            String[] ruoli = jecPrincipal.getPermissions();
            if (ArrayUtils.contains(ruoli, "evasione") && ordiniSettings.isCreazioneMissioniAbilitata()) {
                startCheck();
            } else {
                stopCheck();
                hideStatusBarItem();
            }
        }
    }

    /**
     * @param ordiniDocumentoBD
     *            The ordiniDocumentoBD to set.
     */
    public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
        this.ordiniDocumentoBD = ordiniDocumentoBD;
    }

    /**
     * Inizio il timer per verificare gli ordini pronti.
     */
    private void startCheck() {
        if (timerCheck == null) {
            timerCheck = new Timer(60000, new CheckOrdiniListener());
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
