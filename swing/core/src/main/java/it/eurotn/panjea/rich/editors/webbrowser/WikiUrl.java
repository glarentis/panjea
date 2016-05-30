package it.eurotn.panjea.rich.editors.webbrowser;

import javafx.scene.web.WebEngine;

public class WikiUrl extends PanjeaUrl {

    /**
     * Costruttore.
     */
    public WikiUrl() {
        setIndirizzo("http://www.eurotn.it/wiki/tiki-index.php?page=PanjeaDoc_Index");
    }

    @Override
    public String getDisplayName() {
        return "Wiki";
    }

    /**
     * Carica i dati di accesso da mantis.
     */
    public void init() {
        setUserName("cliente");
        setPassword("cliente");
        setUserNameControl("user");
        setPasswordControl("pass");
        setFormName("loginbox");
    }

    @Override
    public void setEngine(WebEngine engine) {
        init();
        super.setEngine(engine);
    }
}
