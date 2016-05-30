package it.eurotn.panjea.fatturepa.rich.commands;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.AreaFatturaElettronicaType;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.AreaFatturaElettronicaTypeDialog;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

public class OpenAreaFatturaElettronicaTypeCommand extends ActionCommand {

    private static final Logger LOGGER = Logger.getLogger(OpenAreaFatturaElettronicaTypeCommand.class);

    public static final String PARAM_AREA_MAGAZZINO = "paramAreaMagazzino";

    private IFatturePABD fatturePABD;

    /**
     * Costruttore.
     */
    public OpenAreaFatturaElettronicaTypeCommand() {
        super("openAreaFatturaElettronicaTypeCommand");
        RcpSupport.configure(this);

        this.fatturePABD = RcpSupport.getBean(FatturePABD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        AreaMagazzino areaMagazzino = (AreaMagazzino) getParameter(PARAM_AREA_MAGAZZINO, null);
        if (areaMagazzino == null || !areaMagazzino.isFatturaPA()) {
            return;
        }

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePABD.caricaAreaMagazzinoFatturaPA(areaMagazzino.getId());

        if (areaMagazzinoFatturaPA != null) {
            if (!areaMagazzinoFatturaPA.getXmlFattura().isPresent()) {
                try {
                    areaMagazzinoFatturaPA = fatturePABD.creaXMLFattura(areaMagazzino.getId());
                } catch (XMLCreationException e1) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("--> Errore durante la creazione dell'xml.", e1);
                    }
                    MessageDialog dialog = new MessageDialog("ATTENZIONE",
                            new DefaultMessage(e1.getFormattedMessage(), Severity.ERROR));
                    dialog.showDialog();
                    return;
                }
            }
            IFatturaElettronicaType fatturaElettronicaType = fatturePABD
                    .caricaFatturaElettronicaType(areaMagazzinoFatturaPA.getXmlFattura().getXmlFattura());

            AreaFatturaElettronicaType areaFatturaElettronicaType = new AreaFatturaElettronicaType(
                    areaMagazzinoFatturaPA, fatturaElettronicaType);
            new AreaFatturaElettronicaTypeDialog(areaFatturaElettronicaType) {

                @Override
                public void onXMLFatturaCreated(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA) {
                    // non faccio niente
                }

            }.showDialog();
        }
    }

}
