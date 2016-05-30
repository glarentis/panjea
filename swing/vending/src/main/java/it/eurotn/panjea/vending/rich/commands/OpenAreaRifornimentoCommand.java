package it.eurotn.panjea.vending.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

public class OpenAreaRifornimentoCommand extends ApplicationWindowAwareCommand {

    /**
     * Costruttore.
     */
    public OpenAreaRifornimentoCommand() {
        super("openAreaRifornimentoCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        AreaMagazzino areaMagazzino = new AreaMagazzino();

        AreaMagazzinoFullDTO areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
        areaMagazzinoFullDTO.setAreaMagazzino(areaMagazzino);

        OpenEditorEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
