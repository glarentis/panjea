package it.eurotn.panjea.compoli.rich.commands;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.compoli.rich.editors.entitacointestate.EntitaCointestateDialog;
import it.eurotn.panjea.contabilita.domain.AreaContabile;

public class AggiungiEntitaCointestateCommand extends ActionCommand {

    public static final String PARAM_AREA_CONTABILE = "paramAreaContabile";

    private EntitaCointestateDialog entitaCointestateDialog = new EntitaCointestateDialog();

    /**
     * Costruttore.
     */
    public AggiungiEntitaCointestateCommand() {
        super("aggiungiEntitaCointestateCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        AreaContabile areaContabile = (AreaContabile) getParameter(PARAM_AREA_CONTABILE, null);
        if (areaContabile == null || areaContabile.isNew()) {
            return;
        }

        entitaCointestateDialog.setAreaContabile(areaContabile);
        entitaCointestateDialog.showDialog();

    }

}
