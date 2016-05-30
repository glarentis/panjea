package it.eurotn.panjea.ordini.rich.editors.righeinserimento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;

public class GeneraRigheOrdineCommand extends ActionCommand {

    public static final String PARAM_RIGHE_INSERIMENTO = "paramRigheInserimento";
    public static final String PARAM_AREA_ORDINE = "paramAreaOrdine";

    private IOrdiniDocumentoBD ordiniDocumentoBD;

    private boolean righeGenerate = false;

    /**
     * Costruttore.
     */
    public GeneraRigheOrdineCommand() {
        super("generaRigheOrdineCommand");
        RcpSupport.configure(this);

        this.ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        righeGenerate = false;

        @SuppressWarnings("unchecked")
        List<RigaOrdineInserimento> righeInserimento = (List<RigaOrdineInserimento>) getParameter(
                PARAM_RIGHE_INSERIMENTO, null);
        AreaOrdine areaOrdine = (AreaOrdine) getParameter(PARAM_AREA_ORDINE, null);

        if (areaOrdine == null || righeInserimento == null) {
            return;
        }

        List<RigaOrdineInserimento> righeDaGenerare = new ArrayList<>();
        // prendo solo le righe che hanno qta o un attributo con valore diverso da 0
        for (RigaOrdineInserimento riga : righeInserimento) {
            if (!riga.isEmpty()) {
                righeDaGenerare.add(riga);
            }
        }

        if (!righeDaGenerare.isEmpty()) {
            ordiniDocumentoBD.generaRigheOrdine(righeDaGenerare, areaOrdine);
            righeGenerate = true;
        }
    }

    /**
     * @return the righeGenerate
     */
    public boolean isRigheGenerate() {
        return righeGenerate;
    }

}