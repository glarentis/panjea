/**
 *
 */
package it.eurotn.panjea.rich.forms;

import java.io.IOException;
import java.util.Locale;

import javax.swing.JComponent;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.security.LoginDetails;
import org.springframework.richclient.security.LoginForm;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.security.Authentication;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.JecPrincipalSpring;
import it.eurotn.security.JecPrincipal;

/**
 * Login form che contiene i campi username, password e i command login es exit.
 *
 * @author Leonardo
 */
public class PanjeaLoginForm extends LoginForm {

    private JComponent usernameField;

    /**
     * Costruisce la form aggiungendo campo username, password e i pulsanti di login e exit.
     *
     * @return componenti del form
     */
    @Override
    protected JComponent createFormControl() {
        final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder formBuilder = new TableFormBuilder(bf);

        formBuilder.row();

        usernameField = formBuilder.add(LoginDetails.PROPERTY_USERNAME)[1];
        formBuilder.row();

        formBuilder.addPasswordField(LoginDetails.PROPERTY_PASSWORD);
        formBuilder.row();

        ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        formBuilder.add(bf.createBoundComboBox("azienda", sicurezzaBD.caricaAziendeDeployate()));
        formBuilder.row();

        // Creo la combo della locale
        Locale[] lingue = new Locale[2];
        lingue[0] = Locale.ITALY;
        lingue[1] = Locale.GERMANY;
        ComboBoxBinding localeBinding = (ComboBoxBinding) bf.createBoundComboBox("locale", lingue, "displayName");
        formBuilder.add(localeBinding);

        return formBuilder.getForm();
    }

    /**
     *
     * @return loginDetails con l'ultima azienda e lingua avvalorate.
     */
    @Override
    protected PanjeaLoginDetails createLoginDetails() {
        PanjeaLoginDetails loginDetail = new PanjeaLoginDetails();
        loginDetail.setLocale(Locale.getDefault());
        SettingsManager manager = (SettingsManager) ApplicationServicesLocator.services()
                .getService(SettingsManager.class);
        String ultimaAziendaLoggata = null;
        String ultimoUtenteLoggato = null;
        try {
            ultimaAziendaLoggata = manager.getUserSettings().getString("ultimaAziendaLoggata");
            ultimoUtenteLoggato = manager.getUserSettings().getString("ultimoUtenteLoggato");
            loginDetail.setAzienda(ultimaAziendaLoggata);
            loginDetail.setUsername(ultimoUtenteLoggato);
        } catch (SettingsException e1) {
            logger.error("--> errore ne l recupero dell'ultima azienda loggata", e1);
        }
        return loginDetail;
    }

    @Override
    public Authentication getAuthentication() {
        PanjeaLoginDetails loginDetails = (PanjeaLoginDetails) getFormObject();
        String name = loginDetails.getUsername();
        String lingua = loginDetails.getLocale().getLanguage();
        String azienda = loginDetails.getAzienda();
        Authentication jecPrincipal = new JecPrincipalSpring(name + "#" + azienda + "#" + lingua);
        ((JecPrincipal) jecPrincipal).setCredentials(loginDetails.getPassword());
        return jecPrincipal;
    }

    /**
     * Richiama il focus per il campo di username all'avvio.
     *
     * @return true or false
     */
    @Override
    public boolean requestFocusInWindow() {
        return usernameField.requestFocusInWindow();
    }

    /**
     * Salva nei settings locali gli ultimi dati inseriti del login;<br/>
     * il metodo deve essere chiamato una volta autenticato l'utente correttamente.
     */
    public void saveSettings() {
        PanjeaLoginDetails loginDetails = (PanjeaLoginDetails) getFormObject();
        String azienda = loginDetails.getAzienda();
        String userName = loginDetails.getUsername();
        SettingsManager manager = (SettingsManager) ApplicationServicesLocator.services()
                .getService(SettingsManager.class);
        try {
            manager.getUserSettings().setString("ultimaAziendaLoggata", azienda);
            manager.getUserSettings().setString("ultimoUtenteLoggato", userName);
            manager.getUserSettings().save();
        } catch (SettingsException e) {
            logger.error("Errore nel settare la property ultimaAziendaLoggata con value " + azienda, e);
        } catch (IOException e) {
            logger.error("Errore nel salvare la property ultimaAziendaLoggata con value " + azienda, e);
        }
    }
}
