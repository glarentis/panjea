package it.eurotn.panjea.mrp.rich;

import java.util.Calendar;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;

public class OpenMrpFromOrderCommand extends ActionCommand {
    private static final String ID = "openMrpResultCommand";

    /**
     * Costruttore;
     */
    public OpenMrpFromOrderCommand() {
        super(ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        AreaOrdineFullDTO areaOrdineFull = (AreaOrdineFullDTO) getParameter(
                FormsBackedTabbedDialogPageEditor.FORM_OBJECT_PROPERTY);
        ParametriMrpRisultato parametri = new ParametriMrpRisultato();
        parametri.setAreaOrdine(areaOrdineFull.getAreaOrdine());
        parametri.setDataInizio(org.apache.commons.lang3.time.DateUtils
                .addDays(Calendar.getInstance().getTime(), -30));
        parametri.setEseguiCalcoloSuApertura(true);
        LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
        Application.instance().getApplicationContext().publishEvent(event);
    }
}
