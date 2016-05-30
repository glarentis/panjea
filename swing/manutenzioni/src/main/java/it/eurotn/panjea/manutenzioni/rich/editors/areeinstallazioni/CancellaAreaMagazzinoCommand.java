package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.command.AbstractDeleteCommand;

public class CancellaAreaMagazzinoCommand extends AbstractDeleteCommand {

    public static final String AREA_MAGAZZINO_ID_PARAM = "areaMagazzinoIdParam";

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private Integer idAreaMagazzino = null;

    /**
     * Costruttore.
     */
    public CancellaAreaMagazzinoCommand() {
        super("cancellaAreaMagazzinoCommand");
        setId("cancellaAreaMagazzinoCommand");
        RcpSupport.configure(this);

        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        idAreaMagazzino = (Integer) getParameter(AREA_MAGAZZINO_ID_PARAM, null);

        super.doExecuteCommand();
    }

    @Override
    protected String getConfirmMessageKey() {
        return "cancellaAreaMagazzinoCommand.message.confirm.delete";
    }

    @Override
    protected String getConfirmTitleKey() {
        return "cancellaAreaMagazzinoCommand.title.confirm.delete";
    }

    /**
     * @return the idAreaMagazzino
     */
    public final Integer getIdAreaMagazzino() {
        return idAreaMagazzino;
    }

    @Override
    public Object onDelete() {

        if (idAreaMagazzino == null) {
            return null;
        }

        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(idAreaMagazzino);
        AreaMagazzinoFullDTO areaMagazzinoDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);

        try {
            magazzinoDocumentoBD.cancellaAreaMagazzino(areaMagazzinoDTO.getAreaMagazzino(), false, false);
            idAreaMagazzino = null;
        } catch (Exception e) {
            new PanjeaRuntimeException(e);
        }
        return areaMagazzino;
    }

}
