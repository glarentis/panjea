package it.eurotn.panjea.contabilita.rich.editors.areacontabile;

import javax.swing.Icon;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonLabelInfo;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.editors.acconto.AreaAccontoWrapper;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;

/**
 * Se l'area partite di questo documento gestisce dei pagamenti <br/>
 * il comando apre l'editor dell'area pagamenti.
 *
 * @author giangi
 *
 */
public class AreaTesoreriaCommand extends ActionCommand {

    private static final String COMMAND_ID = "areaPartiteCommand";

    private Documento documento;

    /**
     * Costruttore.
     */
    public AreaTesoreriaCommand() {
        super(COMMAND_ID);
        setSecurityControllerId(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        ITesoreriaBD tesoreriaBD = RcpSupport.getBean("tesoreriaBD");
        AreaTesoreria areaTesoreria = tesoreriaBD.caricaAreaTesoreria(documento);

        LifecycleApplicationEvent event = null;
        if (areaTesoreria instanceof AreaAcconto) {
            event = new OpenEditorEvent(new AreaAccontoWrapper((AreaAcconto) areaTesoreria));
        } else {
            event = new OpenEditorEvent(ParametriRicercaAreeTesoreria.creaParametriRicercaAreeTesoreria(areaTesoreria));
        }
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * @param areaTesoreriaPresente
     *            the areaTesoreriaPresente to set
     * @param paramDocumento
     *            documento dell'area tesoreria da caricare
     */
    public void setAreaTesoreriaPresente(boolean areaTesoreriaPresente, Documento paramDocumento) {
        this.documento = paramDocumento;
        if (!areaTesoreriaPresente) {
            getFaceDescriptor().setCaption("");
            getFaceDescriptor().setIcon(null);
            setVisible(false);
        } else {
            // se ha rate non devo visualizzare il link al documento di pagamento
            CommandButtonLabelInfo labelInfo = new CommandButtonLabelInfo("");
            getFaceDescriptor().setLabelInfo(labelInfo);
            Icon icon = RcpSupport.getIcon(AreaTesoreria.class.getName());
            getFaceDescriptor().setIcon(icon);
            setVisible(true);
        }
    }
}