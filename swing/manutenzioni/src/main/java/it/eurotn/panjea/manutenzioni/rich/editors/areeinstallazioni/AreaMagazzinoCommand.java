package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;

public class AreaMagazzinoCommand extends ActionCommand {
    private static final String COMMAND_ID = "areaMagazzinoCommand";

    public static final String AREA_MAGAZZINO_ID_PARAM = "areaMagazzinoIdParam";

    /**
     * Costruttore.
     */
    public AreaMagazzinoCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        Integer idAreaMagazzino = (Integer) getParameter(AREA_MAGAZZINO_ID_PARAM, null);
        if (idAreaMagazzino == null) {
            return;
        }

        IMagazzinoDocumentoBD magazzinoBd = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
        AreaMagazzino am = new AreaMagazzino();
        am.setId(idAreaMagazzino);
        LifecycleApplicationEvent event = new OpenEditorEvent(magazzinoBd.caricaAreaMagazzinoFullDTO(am));
        Application.instance().getApplicationContext().publishEvent(event);
    }

}