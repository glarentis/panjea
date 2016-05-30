package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;

public class CreaDistintaCaricoCommand extends ApplicationWindowAwareCommand {
    public static final String PARAM_RIGHE = "paramRighe";

    public static final String PARAM_EVADI_DISTINTE = "paramEvadiDistinte";
    public static final String COMMAND_ID = "creaDistintaCaricoCommand";
    private IOrdiniDocumentoBD ordiniDocumentoBD = null;

    /**
     * 
     * Costruttore.
     * 
     * @param ordiniDocumentoBD
     *            bd del documento.
     */
    public CreaDistintaCaricoCommand(final IOrdiniDocumentoBD ordiniDocumentoBD) {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        this.ordiniDocumentoBD = ordiniDocumentoBD;
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("--> Enter doExecuteCommand");
        @SuppressWarnings("unchecked")
        // Wrappo in una lista perchè il table model contiene una glazed list
        // non serializzabile
        List<RigaDistintaCarico> righe = new ArrayList<>((List<RigaDistintaCarico>) getParameter(PARAM_RIGHE));
        ordiniDocumentoBD.creaDistintadiCarico(righe);
        logger.debug("--> Exit doExecuteCommand");
    }
}
