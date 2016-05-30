package it.eurotn.panjea.anagrafica.rich.editors.tabelle.riepilogodatibancari;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;

public class SostituisciDatiBancariCommand extends ActionCommand {

    public static final String PARAM_RAPPORTI_BANCARI = "paramRapportiBancari";

    private SostituzioneDialog sostituzioneDialog = new SostituzioneDialog();

    /**
     * Costruttore.
     */
    public SostituisciDatiBancariCommand() {
        super("sostituisciDatiBancariCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        @SuppressWarnings("unchecked")
        List<RapportoBancarioSedeEntita> rapporti = (List<RapportoBancarioSedeEntita>) getParameter(
                PARAM_RAPPORTI_BANCARI, null);
        if (CollectionUtils.isEmpty(rapporti)) {
            return;
        }

        sostituzioneDialog.setRapporti(rapporti);
        sostituzioneDialog.showDialog();

    }

}
