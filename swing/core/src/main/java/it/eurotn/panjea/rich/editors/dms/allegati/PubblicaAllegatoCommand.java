package it.eurotn.panjea.rich.editors.dms.allegati;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.rich.bd.IDmsBD;

public class PubblicaAllegatoCommand extends ActionCommand {

    private IDmsBD dmsBD;
    private AllegatoDMS allegato;
    private String defaultPath;

    /**
     * Costruttore.
     *
     * @param dmsBD
     *            bd
     */
    public PubblicaAllegatoCommand(final IDmsBD dmsBD) {
        super("pubblicaDocumentoCommand");
        this.dmsBD = dmsBD;
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        new NuovoAllegatoDialog(dmsBD, allegato, defaultPath).showDialog();
    }

    /**
     * @return Returns the defaultPath.
     */
    public String getDefaultPath() {
        return defaultPath;
    }

    /**
     * @param attributi
     *            The attributi to set.
     */
    public void setAttributi(AllegatoDMS attributi) {
        this.allegato = attributi;
    }

    /**
     * @param defaultPath
     *            The defaultPath to set.
     */
    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

}