package it.eurotn.panjea.fatturepa.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AreaNotificaFatturaPA;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.service.interfaces.FatturePAService;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.panjea.fatturepa.util.AreaNotificaFatturaPADTO;
import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

/**
 * @author fattazzo
 *
 */
public class FatturePABD extends AbstractBaseBD implements IFatturePABD {

    public static final String BEAN_ID = "fatturePABD";

    private static final Logger LOGGER = Logger.getLogger(FatturePABD.class);

    private FatturePAService fatturePAService;

    @Override
    public void cancellaXMLFatturaPA(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter cancellaXMLFatturaPA");
        start("cancellaXMLFatturaPA");

        try {
            fatturePAService.cancellaXMLFatturaPA(idAreaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaXMLFatturaPA");
        }
        LOGGER.debug("--> Exit cancellaXMLFatturaPA ");
    }

    @Override
    public AreaMagazzinoFatturaPA caricaAreaMagazzinoFatturaPA(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter caricaAreaMagazzinoFatturaPA");
        start("caricaAreaMagazzinoFatturaPA");

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = null;
        try {
            areaMagazzinoFatturaPA = fatturePAService.caricaAreaMagazzinoFatturaPA(idAreaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaMagazzinoFatturaPA");
        }
        LOGGER.debug("--> Exit caricaAreaMagazzinoFatturaPA ");
        return areaMagazzinoFatturaPA;
    }

    @Override
    public AreaNotificaFatturaPA caricaAreaNotificaFatturaPA(Integer id) {
        LOGGER.debug("--> Enter caricaAreaNotificaFatturaPA");
        start("caricaAreaNotificaFatturaPA");

        AreaNotificaFatturaPA areaNotificaFatturaPA = null;
        try {
            areaNotificaFatturaPA = fatturePAService.caricaAreaNotificaFatturaPA(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaNotificaFatturaPA");
        }
        LOGGER.debug("--> Exit caricaAreaNotificaFatturaPA ");
        return areaNotificaFatturaPA;
    }

    @Override
    public List<AreaNotificaFatturaPADTO> caricaAreaNotificheFatturaPA(Integer idAreaMagazzinoFatturaPA) {
        LOGGER.debug("--> Enter caricaAreaNotificheFatturaPA");
        start("caricaAreaNotificheFatturaPA");

        List<AreaNotificaFatturaPADTO> notifiche = null;
        try {
            notifiche = fatturePAService.caricaAreaNotificheFatturaPA(idAreaMagazzinoFatturaPA);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaNotificheFatturaPA");
        }
        LOGGER.debug("--> Exit caricaAreaNotificheFatturaPA ");
        return notifiche;
    }

    @Override
    public IFatturaElettronicaType caricaFatturaElettronicaType(String xmlContent) {
        LOGGER.debug("--> Enter caricaFatturaElettronicaType");
        start("caricaFatturaElettronicaType");
        IFatturaElettronicaType fatturaElettronicaType = null;
        try {
            fatturaElettronicaType = fatturePAService.caricaFatturaElettronicaType(xmlContent);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFatturaElettronicaType");
        }
        LOGGER.debug("--> Exit caricaFatturaElettronicaType ");
        return fatturaElettronicaType;
    }

    @Override
    public AreaMagazzinoFatturaPA creaXMLFattura(Integer idAreaMagazzino) throws XMLCreationException {
        LOGGER.debug("--> Enter creaXMLFattura");
        start("creaXMLFatture");

        AreaMagazzinoFatturaPA result = null;
        try {
            result = fatturePAService.creaXMLFattura(idAreaMagazzino);
        } catch (XMLCreationException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaXMLFatture");
        }
        LOGGER.debug("--> Exit creaXMLFattura");
        return result;
    }

    @Override
    public AreaMagazzinoFatturaPA creaXMLFatturaPA(Integer idAreaMagazzino,
            IFatturaElettronicaType fatturaElettronicaType) throws XMLCreationException {
        LOGGER.debug("--> Enter creaXMLFatturaPA");
        start("creaXMLFatturaPA");

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = null;
        try {
            areaMagazzinoFatturaPA = fatturePAService.creaXMLFatturaPA(idAreaMagazzino, fatturaElettronicaType);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaXMLFatturaPA");
        }
        LOGGER.debug("--> Exit creaXMLFatturaPA ");
        return areaMagazzinoFatturaPA;
    }

    @Override
    public byte[] downloadXMLFirmato(String fileName) {
        LOGGER.debug("--> Enter downloadXMLFirmato");
        start("downloadXMLFirmato");

        byte[] file = null;
        try {
            file = fatturePAService.downloadXMLFirmato(fileName);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("downloadXMLFirmato");
        }
        LOGGER.debug("--> Exit downloadXMLFirmato ");
        return file;
    }

    @Override
    public void invioSdiFatturaPA(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter invioSdiFatturaPA");
        start("invioSdiFatturaPA");
        try {
            fatturePAService.invioSdiFatturaPA(idAreaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("invioSdiFatturaPA");
        }
        LOGGER.debug("--> Exit invioSdiFatturaPA ");
    }

    @Override
    public List<AreaMagazzinoFatturaPARicerca> ricercaFatturePA(ParametriRicercaFatturePA parametri) {
        LOGGER.debug("--> Enter ricercaFatturePA");
        start("ricercaFatturePA");

        List<AreaMagazzinoFatturaPARicerca> aree = null;
        try {
            aree = fatturePAService.ricercaFatturePA(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaFatturePA");
        }
        LOGGER.debug("--> Exit ricercaFatturePA ");
        return aree;
    }

    @Override
    public AreaMagazzinoFatturaPA salvaAreaMagazzinoFatturaPA(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA) {
        LOGGER.debug("--> Enter salvaAreaMagazzinoFatturaPA");
        start("salvaAreaMagazzinoFatturaPA");

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPASalvata = null;
        try {
            areaMagazzinoFatturaPASalvata = fatturePAService.salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAreaMagazzinoFatturaPA");
        }
        LOGGER.debug("--> Exit salvaAreaMagazzinoFatturaPA ");
        return areaMagazzinoFatturaPASalvata;
    }

    @Override
    public void salvaFatturaPAComeInviata(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter salvaFatturaPAComeInviata");
        start("salvaFatturaPAComeInviata");
        try {
            fatturePAService.salvaFatturaPAComeInviata(idAreaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaFatturaPAComeInviata");
        }
        LOGGER.debug("--> Exit salvaFatturaPAComeInviata ");
    }

    @Override
    public void salvaXMLFatturaFirmato(Integer idAreaMagazzino, byte[] xmlContent, String xmlFileName) {
        LOGGER.debug("--> Enter salvaXMLFatturaFirmato");
        start("salvaXMLFatturaFirmato");
        try {
            fatturePAService.salvaXMLFatturaFirmato(idAreaMagazzino, xmlContent, xmlFileName);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaXMLFatturaFirmato");
        }
        LOGGER.debug("--> Exit salvaXMLFatturaFirmato ");
    }

    /**
     * @param fatturePAService
     *            the fatturePAService to set
     */
    public void setFatturePAService(FatturePAService fatturePAService) {
        this.fatturePAService = fatturePAService;
    }

}
