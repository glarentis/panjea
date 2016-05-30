package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.panjea.manutenzioni.rich.bd.ManutenzioniBD;

public class CreaAreaMagazzinoCommand extends ActionCommand {
    public static final String PARAM_ID_AREA_INSTALLAZIONE_KEY = "idAreaInstallazione";
    private IManutenzioniBD manutenzioniBD;

    private Integer idAreaMagazzinoCreata;

    /**
     * Costruttore
     */
    public CreaAreaMagazzinoCommand() {
        super("creaAreaMagazzinoCommand");
        RcpSupport.configure(this);
        manutenzioniBD = RcpSupport.getBean(ManutenzioniBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {
        idAreaMagazzinoCreata = null;

        int idInstallazione = (int) getParameter(PARAM_ID_AREA_INSTALLAZIONE_KEY);
        idAreaMagazzinoCreata = manutenzioniBD.creaAreaMagazzino(idInstallazione);
    }

    /**
     * @return the idAreaMagazzinoCreata
     */
    public final Integer getIdAreaMagazzinoCreata() {
        return idAreaMagazzinoCreata;
    }

}
