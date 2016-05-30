package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.util.RigaTestataDTO;

public class ApriDocumentoCollegatoCommand extends ApplicationWindowAwareCommand {

    private final RigheMagazzinoTablePage righeMagazzinoTablePage;

    /**
     * Apre l'area collegata della riga testata selezionata.
     * 
     * @param righeMagazzinoTablePage
     *            pagina delle righe magazzino
     */
    public ApriDocumentoCollegatoCommand(final RigheMagazzinoTablePage righeMagazzinoTablePage) {
        super("Apri documento collegato");
        // setVisible(false);
        RcpSupport.configure(this);
        setIcon(RcpSupport.getIcon(LivelloRigaMagazzinoCellRenderer.RIGA_TESTATA_ICON));
        this.righeMagazzinoTablePage = righeMagazzinoTablePage;
    }

    @Override
    public void doExecuteCommand() {
        if (righeMagazzinoTablePage.getTable().getSelectedObject() instanceof RigaTestataDTO) {
            RigaTestataDTO rigaTestata = (RigaTestataDTO) righeMagazzinoTablePage.getTable().getSelectedObject();

            LifecycleApplicationEvent event = new OpenEditorEvent(rigaTestata.getAreaDocumentoCollegata());
            Application.instance().getApplicationContext().publishEvent(event);
        }
    }
}