package it.eurotn.panjea.contabilita.rich.editors.areacontabile;

import javax.swing.Icon;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonLabelInfo;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;

public class AreaMagazzinoCommand extends ActionCommand {

    private static final String COMMAND_ID = "areaMagazzinoCommand";
    private AreaMagazzinoLite areaMagazzinoLite;

    /**
     * Costruttore.
     */
    public AreaMagazzinoCommand() {
        super(COMMAND_ID);
        setSecurityControllerId(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(areaMagazzinoLite.getId());
        LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzino);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * @param areaMagazzinoLite
     *            the areaMagazzino to set
     */
    public void setAreaMagazzinoLite(AreaMagazzinoLite areaMagazzinoLite) {
        this.areaMagazzinoLite = areaMagazzinoLite;
        if (areaMagazzinoLite == null) {
            getFaceDescriptor().setCaption("");
            getFaceDescriptor().setIcon(null);
            setVisible(false);
        } else {
            // se ha rate non devo visualizzare il link al documento di pagamento
            CommandButtonLabelInfo labelInfo = new CommandButtonLabelInfo("");
            getFaceDescriptor().setLabelInfo(labelInfo);
            Icon icon = RcpSupport.getIcon(AreaMagazzino.class.getName());
            getFaceDescriptor().setIcon(icon);
            setVisible(true);
        }
    }
}