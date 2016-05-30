package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.datiaccompagnatori.DatiAccompagnatoriDialog;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

public class ModificaAreaMagazzinoCommand extends ActionCommand {

    public static final String AREA_MAGAZZINO_ID_PARAM = "areaMagazzinoIdParam";

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    /**
     * Costruttore.
     */
    public ModificaAreaMagazzinoCommand() {
        super("modificaAreaMagazzinoCommand");
        RcpSupport.configure(this);

        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        Integer idAreaMagazzino = (Integer) getParameter(AREA_MAGAZZINO_ID_PARAM, null);
        if (idAreaMagazzino == null) {
            return;
        }

        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(idAreaMagazzino);
        AreaMagazzinoFullDTO areaMagazzinoDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);

        SortedSet<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatoriRichiesti = new TreeSet<DatoAccompagnatorioMagazzinoMetaData>(
                areaMagazzinoDTO.getAreaMagazzino().getTipoAreaMagazzino().getDatiAccompagnatoriMetaData());

        if (!datiAccompagnatoriRichiesti.isEmpty()) {
            DatiAccompagnatoriDialog datiAccompagnatoriDialog = new DatiAccompagnatoriDialog(areaMagazzinoDTO);
            datiAccompagnatoriDialog.showDialog();
            areaMagazzinoDTO = datiAccompagnatoriDialog.getAreaMagazzinoFullDTO();

            try {
                magazzinoDocumentoBD.salvaAreaMagazzino(areaMagazzinoDTO.getAreaMagazzino(), null, true);
            } catch (Exception e) {
                new PanjeaRuntimeException(e);
            }
        }
    }

}
