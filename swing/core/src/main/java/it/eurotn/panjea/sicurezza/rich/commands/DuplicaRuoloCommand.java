package it.eurotn.panjea.sicurezza.rich.commands;

import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.rich.editors.RuoloPage;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.springframework.richclient.command.ActionCommand;

public class DuplicaRuoloCommand extends ActionCommand {

    private RuoloPage ruoloPage;

    /**
     * Costruttore.
     */
    public DuplicaRuoloCommand() {
        super("duplicaRuoloCommand");
    }

    @Override
    protected void doExecuteCommand() {
        Ruolo ruolo = (Ruolo) ruoloPage.getBackingFormPage().getFormObject();
        Ruolo ruoloDuplicato = (Ruolo) PanjeaSwingUtil.cloneObject(ruolo);
        ruoloDuplicato.setDescrizione(null);
        ruoloDuplicato.setCodice(null);
        ruoloDuplicato.setVersion(null);
        ruoloDuplicato.setId(null);

        ruoloPage.setFormObject(ruoloDuplicato);

    }

    /**
     * @return the ruoloPage
     */
    public RuoloPage getRuoloPage() {
        return ruoloPage;
    }

    /**
     * @param ruoloPage
     *            the ruoloPage to set
     */
    public void setRuoloPage(RuoloPage ruoloPage) {
        this.ruoloPage = ruoloPage;
    }

}
