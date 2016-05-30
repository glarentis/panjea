package it.eurotn.panjea.rich.editors.webbrowser;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import javafx.scene.web.WebEngine;

import org.springframework.richclient.util.RcpSupport;

public class MantisUrl extends PanjeaUrl {

    /**
     * Costruttore.
     */
    public MantisUrl() {
        setIndirizzo("http://eurotn.it/eurotnbgtrk/changelog_page.php");
    }

    @Override
    public String getDisplayName() {
        return "Gestione versioni";
    }

    /**
     * Carica i dati di accesso da mantis.
     */
    public void init() {
        ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
        setUserName(utente.getDatiBugTracker().getUsername());
        setPassword(utente.getDatiBugTracker().getPassword());
        setUserNameControl("username");
        setPasswordControl("password");
        setFormName("login_form");
    }

    @Override
    public void setEngine(WebEngine engine) {
        init();
        super.setEngine(engine);
    }
}
