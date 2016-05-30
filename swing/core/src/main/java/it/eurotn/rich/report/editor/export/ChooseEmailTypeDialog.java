/**
 *
 */
package it.eurotn.rich.report.editor.export;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

/**
 * @author fattazzo
 *
 */
public class ChooseEmailTypeDialog extends ConfirmationDialog {

    private String email;
    private String emailPEC;
    private Closure selectMailClosure;

    private boolean pecUtenteAbilitata = false;

    /**
     * Costruttore.
     *
     * @param email
     *            indirizzo mail
     * @param emailPEC
     *            indirizzo PEC
     * @param selectMailClosure
     *            azione da eseguire alla scelta dell'indirizzo
     */
    public ChooseEmailTypeDialog(final String email, final String emailPEC, final Closure selectMailClosure) {
        super("ATTENZIONE", "Scelta email");
        this.email = email;
        this.emailPEC = emailPEC;
        this.selectMailClosure = selectMailClosure;

        ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
        pecUtenteAbilitata = utente.getDatiMailPredefiniti().isPec();
    }

    @Override
    protected JComponent createDialogContentPane() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 10));
        rootPanel.add(new JLabel("Selezionare l'indirizzo email di spedizione:"), BorderLayout.NORTH);

        JPanel emailPanel = getComponentFactory().createPanel(new VerticalLayout(5));
        emailPanel.add(new JLabel("<html><b>Indirizzo PEC: </b>" + emailPEC));
        emailPanel.add(new JLabel("<html><b>Indirizzo mail: </b>" + email));
        rootPanel.add(emailPanel, BorderLayout.CENTER);
        return rootPanel;
    }

    @Override
    protected String getCancelCommandId() {
        return "chooseEmailCommand";
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return super.getCommandGroupMembers();
    }

    @Override
    protected String getFinishCommandId() {
        return "choosePECCommand";
    }

    @Override
    protected void onCancel() {
        selectMailClosure.call(email);
        super.onCancel();
    }

    @Override
    protected void onConfirm() {
        selectMailClosure.call(emailPEC);
    }

    @Override
    protected void onWindowGainedFocus() {
        if (pecUtenteAbilitata) {
            getFinishCommand().requestFocusIn(getDialog());
        } else {
            getCancelCommand().requestFocusIn(getDialog());
        }
    }

    @Override
    protected void registerDefaultCommand() {
        // setto il comando predefinito in base al flag PEC della mail dell'utente
        if (pecUtenteAbilitata) {
            registerDefaultCommand(getFinishCommand());
        } else {
            registerDefaultCommand(getCancelCommand());
        }
    }

}
