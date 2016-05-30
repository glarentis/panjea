package it.eurotn.panjea.rich.editors.webbrowser;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import javafx.scene.web.WebEngine;

public class LogicalDocUrl extends PanjeaUrl {

    public LogicalDocUrl() {
        setIndirizzo("http://localhost:8280");
    }

    @Override
    public String getDisplayName() {
        return "LogicalDoc";
    }

    /**
     * Carica i dati di accesso da mantis.
     */
    public void init() {
        setUserName(PanjeaSwingUtil.getUtenteCorrente().getUserName());
        setPassword((String) PanjeaSwingUtil.getUtenteCorrente().getCredentials());
        setUserNameControl("isc_V");
        setPasswordControl("isc_X");
        setFormName("isc_S");
    }

    @Override
    public void setEngine(WebEngine engine) {
        init();
        super.setEngine(engine);
    }

}
