/**
 *
 */
package it.eurotn.panjea.sicurezza.rich.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.DatiMail.TipoConnessione;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.rich.commands.TestMailUtenteCommand;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author Leonardo
 */
public class UtenteDatiEmailForm extends PanjeaAbstractForm {

    private class AggiornaUtenteTestMailCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {

        }

        @Override
        public boolean preExecution(ActionCommand command) {
            testMailUtenteCommand.setUtente(utente);
            testMailUtenteCommand.setDatiMail((DatiMail) getFormObject());
            return true;
        }
    }

    private class AutenticazioneChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (evt.getNewValue() != null) {
                Boolean auth = (Boolean) evt.getNewValue();
                setEmailAuthenticationVisible(auth.booleanValue());

                if (getFormModel().isReadOnly()) {
                    return;
                }

                // se nascondo i campi annullo i valori
                if (!auth) {
                    getValueModel("utenteMail").setValue(null);
                    getValueModel("passwordMail").setValue(null);
                }
            }
        }

    }

    private class TipoConnessioneChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            if (evt.getNewValue() != null) {
                TipoConnessione tipoConnessione = (TipoConnessione) evt.getNewValue();
                int porta = tipoConnessione.getPorta();
                getValueModel("port").setValue(porta);
            }
        }
    }

    private static final String ID_FORM = "utenteDatiEmailForm";

    private TestMailUtenteCommand testMailUtenteCommand = null;

    private AggiornaUtenteTestMailCommandInterceptor aggiornaUtenteTestMailCommandInterceptor = null;

    private JComponent[] pwdField = null;
    private JComponent[] userField = null;

    private TipoConnessioneChangeListener tipoConnessioneChangeListener = null;
    private AutenticazioneChangeListener autenticazioneChangeListener = null;

    private Utente utente;

    /**
     * Costruttore di default.
     *
     */
    public UtenteDatiEmailForm() {
        super(PanjeaFormModelHelper.createFormModel(new DatiMail(), false, ID_FORM), ID_FORM);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:120dlu, 10dlu, right:pref,4dlu,left:120dlu, 10dlu, right:pref,4dlu,left:30dlu,10dlu,right:pref,4dlu,left:pref",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default:grow,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());

        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addPropertyAndLabel("nomeAccount", 1);
        builder.addPropertyAndLabel("predefinito", 5);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("server", 1)[1]).setColumns(20);
        builder.addPropertyAndLabel("tipoConnessione", 5);
        ((JTextField) builder.addPropertyAndLabel("port", 9)[1]).setColumns(7);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("email", 1)[1]).setColumns(20);
        builder.addPropertyAndLabel("emailDiRisposta", 5);
        builder.addPropertyAndLabel("pec", 9);
        builder.addPropertyAndLabel("auth", 13);
        builder.nextRow();

        userField = builder.addPropertyAndLabel("utenteMail", 1);
        ((JTextField) userField[1]).setColumns(20);
        pwdField = builder.addPasswordFieldAndLabel("passwordMail", 5);
        ((JTextField) pwdField[1]).setColumns(20);
        builder.addPropertyAndLabel("notificaLettura", 13);
        builder.nextRow();

        builder.setLabelAttributes("r, t");
        builder.addLabel("firma", 1);
        Binding createBoundHTMLEditor = bf.createBoundHTMLEditor("firma");

        builder.setComponentAttributes("f,f");
        JComponent htmlEditor = createBoundHTMLEditor.getControl();
        builder.addComponent(htmlEditor, 3, 10, 13, 1);
        builder.setLabelAttributes("r, t");
        builder.nextRow();

        JButton button = (JButton) getTestMailUtenteCommand().createButton();
        builder.addComponent(button, 11, 12, 5, 1);
        builder.nextRow();

        getValueModel("tipoConnessione").addValueChangeListener(getTipoConnessioneChangeListener());
        getValueModel("auth").addValueChangeListener(getAutenticazioneChangeListener());
        setEmailAuthenticationVisible(false);

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        DatiMail datiMail = new DatiMail();
        datiMail.setUtente(utente);
        return datiMail;
    }

    @Override
    public void dispose() {
        getTestMailUtenteCommand().removeCommandInterceptor(getAggiornaUtenteTestMailCommandInterceptor());
        getValueModel("tipoConnessione").removeValueChangeListener(getTipoConnessioneChangeListener());
        getValueModel("auth").removeValueChangeListener(getAutenticazioneChangeListener());
        tipoConnessioneChangeListener = null;
        autenticazioneChangeListener = null;
        super.dispose();
    }

    /**
     * @return AggiornaUtenteTestMailCommandInterceptor
     */
    private AggiornaUtenteTestMailCommandInterceptor getAggiornaUtenteTestMailCommandInterceptor() {
        if (aggiornaUtenteTestMailCommandInterceptor == null) {
            aggiornaUtenteTestMailCommandInterceptor = new AggiornaUtenteTestMailCommandInterceptor();
        }
        return aggiornaUtenteTestMailCommandInterceptor;
    }

    /**
     * @return AutenticazioneChangeListener
     */
    private AutenticazioneChangeListener getAutenticazioneChangeListener() {
        if (autenticazioneChangeListener == null) {
            autenticazioneChangeListener = new AutenticazioneChangeListener();
        }
        return autenticazioneChangeListener;
    }

    @Override
    protected String getCommitCommandFaceDescriptorId() {
        return this.getId() + ".save";
    }

    @Override
    protected String getNewFormObjectCommandId() {
        return this.getId() + ".new";
    }

    @Override
    protected String getRevertCommandFaceDescriptorId() {
        return this.getId() + ".revert";
    }

    /**
     * @return TestMailUtenteCommand
     */
    private ActionCommand getTestMailUtenteCommand() {
        if (testMailUtenteCommand == null) {
            testMailUtenteCommand = new TestMailUtenteCommand();
            testMailUtenteCommand.addCommandInterceptor(getAggiornaUtenteTestMailCommandInterceptor());
        }
        return testMailUtenteCommand;
    }

    /**
     * @return TipoConnessioneChangeListener
     */
    private TipoConnessioneChangeListener getTipoConnessioneChangeListener() {
        if (tipoConnessioneChangeListener == null) {
            tipoConnessioneChangeListener = new TipoConnessioneChangeListener();
        }
        return tipoConnessioneChangeListener;
    }

    /**
     * Nasconde/visualizza i componenti per l'autenticazione del server mail.
     *
     * @param visible
     *            true o false
     */
    private void setEmailAuthenticationVisible(boolean visible) {
        pwdField[0].setVisible(visible);
        pwdField[1].setVisible(visible);
        userField[0].setVisible(visible);
        userField[1].setVisible(visible);
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
