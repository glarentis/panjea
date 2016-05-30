package it.eurotn.panjea.contabilita.rich.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.rich.editors.documento.AbstractEliminaDocumentoCommand;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;

/**
 * it.eurotn.panjea.contabilita.rich.commands.EliminaAreaContabileCommand Command per la cancellazioene di un'area
 * contabile.
 *
 * @author Leonardo
 * @version 1.0, 15/apr/08
 */
public class EliminaAreaContabileCommand extends AbstractEliminaDocumentoCommand {

    private static final Logger LOGGER = Logger.getLogger(EliminaAreaContabileCommand.class);

    public static final String COMMAND_ID = "eliminaAreaContabileCommand";
    public static final String PARAM_AREA_CONTABILE = "areaContabileParam";
    public static final String PARAM_AREA_MAGAZZINO = "areaMagazzinoParam";

    public static final String PARAM_AREA_TESORERIA = "areaTesoreriaParam";
    private IContabilitaBD contabilitaBD = null;
    private List<Integer> idAreeContabili;

    /**
     * Costruttore.
     *
     * @param pageId
     *            id del command
     */
    public EliminaAreaContabileCommand(final String pageId) {
        super(pageId + ".controller");
        setEnabled(true);
    }

    @Override
    public Object doDelete(boolean deleteAreeCollegate) {
        LOGGER.debug("--> Enter doDelete");
        AreaContabile areaContabile = (AreaContabile) getParameter(PARAM_AREA_CONTABILE, null);

        List<AreaContabileFullDTO> areeDaCancellareFullDto = new ArrayList<AreaContabileFullDTO>();
        if (areaContabile == null) {
            for (Integer idArea : idAreeContabili) {
                areeDaCancellareFullDto.add(contabilitaBD.caricaAreaContabileFullDTO(idArea));
            }

            contabilitaBD.cancellaAreeContabili(idAreeContabili, deleteAreeCollegate);
        } else {
            if (areaContabile.getId() != null) {
                try {
                    areeDaCancellareFullDto.add(contabilitaBD.caricaAreaContabileFullDTO(areaContabile.getId()));
                    contabilitaBD.cancellaAreaContabile(areaContabile, deleteAreeCollegate, deleteAreeCollegate);
                } catch (AreeCollegatePresentiException e) {
                    // non deve arrivare
                    throw new RuntimeException(e);
                }
            }
        }

        if (deleteAreeCollegate) {
            for (AreaContabileFullDTO areaContabileFullDTO : areeDaCancellareFullDto) {
                if (areaContabileFullDTO.isAreaMagazzinoPresente()) {
                    AreaMagazzino areaMagazzino = new AreaMagazzino();
                    areaMagazzino.setId(areaContabileFullDTO.getAreaMagazzinoLite().getId());
                    areaMagazzino.setVersion(areaContabileFullDTO.getAreaMagazzinoLite().getVersion());
                    PanjeaLifecycleApplicationEvent deleteMagaEvent = new PanjeaLifecycleApplicationEvent(
                            LifecycleApplicationEvent.DELETED, areaMagazzino);
                    Application.instance().getApplicationContext().publishEvent(deleteMagaEvent);
                }
                if (areaContabileFullDTO.getAreaTesoreria() != null) {
                    PanjeaLifecycleApplicationEvent deleteAreaCollegataEvent = new PanjeaLifecycleApplicationEvent(
                            LifecycleApplicationEvent.DELETED, areaContabileFullDTO.getAreaTesoreria());
                    Application.instance().getApplicationContext().publishEvent(deleteAreaCollegataEvent);
                }
            }
        }

        return areeDaCancellareFullDto.isEmpty() ? null : areeDaCancellareFullDto;
    }

    /**
     * @return the contabilitaBD
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
    }

    /**
     * @return the idAreeContabili to get
     */
    public List<Integer> getIdAreeContabili() {
        return idAreeContabili;
    }

    @Override
    protected boolean isAreaCollegataPresente() {
        // La tesoreria non viene considerata come area collegata presente perchè viene SEMPRE cancellata assieme
        return (Boolean) getParameter(PARAM_AREA_MAGAZZINO, true);
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    /**
     * @param idAreeContabili
     *            the idAreeContabili to set
     */
    public void setIdAreeContabili(List<Integer> idAreeContabili) {
        this.idAreeContabili = idAreeContabili;
    }

}
