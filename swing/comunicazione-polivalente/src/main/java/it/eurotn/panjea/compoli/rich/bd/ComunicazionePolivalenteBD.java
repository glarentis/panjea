package it.eurotn.panjea.compoli.rich.bd;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.contabilita.domain.EntitaCointestazione;
import it.eurotn.panjea.contabilita.service.interfaces.ComunicazionePolivalenteService;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author fattazzo
 *
 */
public class ComunicazionePolivalenteBD extends AbstractBaseBD implements IComunicazionePolivalenteBD {

    public static final String BEAN_ID = "comunicazionePolivalenteBD";

    private static final Logger LOGGER = Logger.getLogger(ComunicazionePolivalenteBD.class);

    private ComunicazionePolivalenteService comunicazionePolivalenteService;

    @Override
    public void cancellaEntitaCointestazione(Integer id) {
        LOGGER.debug("--> Enter cancellaEntitaCointestazione");
        start("cancellaEntitaCointestazione");
        try {
            comunicazionePolivalenteService.cancellaEntitaCointestazione(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaEntitaCointestazione");
        }
        LOGGER.debug("--> Exit cancellaEntitaCointestazione ");
    }

    @Override
    public List<DocumentoSpesometro> caricaDocumenti(ParametriCreazioneComPolivalente params) {
        LOGGER.debug("--> Enter caricaDocumenti");
        start("caricaDocumenti");
        List<DocumentoSpesometro> documenti = new ArrayList<>();
        try {
            documenti = comunicazionePolivalenteService.caricaDocumenti(params);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDocumenti");
        }
        LOGGER.debug("--> Exit caricaDocumenti ");
        return documenti;
    }

    @Override
    public List<EntitaCointestazione> caricaEntitaCointestazione() {
        LOGGER.debug("--> Enter caricaEntitaCointestazione");
        start("caricaEntitaCointestazione");

        List<EntitaCointestazione> result = null;
        try {
            result = comunicazionePolivalenteService.caricaEntitaCointestazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntitaCointestazione");
        }
        LOGGER.debug("--> Exit caricaEntitaCointestazione ");
        return result;
    }

    @Override
    public List<EntitaCointestazione> caricaEntitaCointestazioneByAreaContabile(Integer idAreaContabile) {
        LOGGER.debug("--> Enter caricaEntitaCointestazioneByAreaContabile");
        start("caricaEntitaCointestazioneByAreaContabile");

        List<EntitaCointestazione> entita = null;
        try {
            entita = comunicazionePolivalenteService.caricaEntitaCointestazioneByAreaContabile(idAreaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntitaCointestazioneByAreaContabile");
        }
        LOGGER.debug("--> Exit caricaEntitaCointestazioneByAreaContabile ");
        return entita;
    }

    @Override
    public EntitaCointestazione caricaEntitaCointestazioneById(Integer id) {
        LOGGER.debug("--> Enter caricaEntitaCointestazioneById");
        start("caricaEntitaCointestazioneById");

        EntitaCointestazione object = null;
        try {
            object = comunicazionePolivalenteService.caricaEntitaCointestazioneById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntitaCointestazioneById");
        }
        LOGGER.debug("--> Exit caricaEntitaCointestazioneById ");
        return object;
    }

    @Override
    public byte[] genera(ParametriCreazioneComPolivalente params) {
        LOGGER.debug("--> Enter genera");
        start("genera");
        byte[] file = null;
        try {
            file = comunicazionePolivalenteService.genera(params);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("genera");
        }
        LOGGER.debug("--> Exit genera ");
        return file;
    }

    @Override
    public EntitaCointestazione salvaEntitaCointestazione(EntitaCointestazione entitaCointestazione) {
        LOGGER.debug("--> Enter salvaEntitaCointestazione");
        start("salvaEntitaCointestazione");

        EntitaCointestazione object = null;
        try {
            object = comunicazionePolivalenteService.salvaEntitaCointestazione(entitaCointestazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaEntitaCointestazione");
        }
        LOGGER.debug("--> Exit salvaEntitaCointestazione ");
        return object;
    }

    /**
     * @param comunicazionePolivalenteService
     *            the comunicazionePolivalenteService to set
     */
    public void setComunicazionePolivalenteService(ComunicazionePolivalenteService comunicazionePolivalenteService) {
        this.comunicazionePolivalenteService = comunicazionePolivalenteService;
    }
}
