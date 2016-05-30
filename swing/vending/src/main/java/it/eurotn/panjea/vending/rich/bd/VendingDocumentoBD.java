package it.eurotn.panjea.vending.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.service.interfaces.OrdiniDocumentoService;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.RigaLetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.arearifornimento.ParametriRicercaAreeRifornimento;
import it.eurotn.panjea.vending.manager.evadts.importazioni.ImportazioneFileEvaDtsResult;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.ParametriRicercaRilevazioniEvaDts;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.RisultatiChiusuraLettureDTO;
import it.eurotn.panjea.vending.service.interfaces.VendingDocumentoService;

public class VendingDocumentoBD extends AbstractBaseBD implements IVendingDocumentoBD {

    public static final String BEAN_ID = "vendingDocumentoBD";

    private static final Logger LOGGER = Logger.getLogger(VendingDocumentoBD.class);

    private MagazzinoDocumentoService magazzinoDocumentoService;

    private OrdiniDocumentoService ordiniDocumentoService;

    private VendingDocumentoService vendingDocumentoService;

    @Override
    public AreaRifornimento aggiornaDatiInstallazione(AreaRifornimento areaRifornimento, Installazione installazione) {
        LOGGER.debug("--> Enter aggiornaDatiInstallazione");
        start("aggiornaDatiInstallazione");

        AreaRifornimento areaRifornimentoAggiornata = null;
        try {
            areaRifornimentoAggiornata = vendingDocumentoService.aggiornaDatiInstallazione(areaRifornimento,
                    installazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaDatiInstallazione");
        }
        LOGGER.debug("--> Exit aggiornaDatiInstallazione ");
        return areaRifornimentoAggiornata;
    }

    @Override
    public AreaRifornimento aggiornaDistributore(AreaRifornimento areaRifornimento, Integer idDistributore) {
        LOGGER.debug("--> Enter aggiornaArticolo");
        start("aggiornaArticolo");
        AreaRifornimento areaRifornimentoAggiornata = null;
        try {
            areaRifornimentoAggiornata = vendingDocumentoService.aggiornaDistributore(areaRifornimento, idDistributore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaArticolo");
        }
        LOGGER.debug("--> Exit aggiornaArticolo ");
        return areaRifornimentoAggiornata;
    }

    @Override
    public AreaRifornimento aggiornaEntita(AreaRifornimento areaRifornimento, Integer idEntita) {
        LOGGER.debug("--> Enter aggiornaEntita");
        start("aggiornaEntita");

        AreaRifornimento areaRifornimentoAggiornata = null;
        try {
            areaRifornimentoAggiornata = vendingDocumentoService.aggiornaEntita(areaRifornimento, idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaEntita");
        }
        LOGGER.debug("--> Exit aggiornaEntita ");
        return areaRifornimentoAggiornata;
    }

    @Override
    public AreaRifornimento aggiornaSedeEntita(AreaRifornimento areaRifornimento, SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter aggiornaSedeEntita");
        start("aggiornaSedeEntita");

        AreaRifornimento areaRifornimentoAggiornata = null;
        try {
            areaRifornimentoAggiornata = vendingDocumentoService.aggiornaSedeEntita(areaRifornimento, sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaSedeEntita");
        }
        LOGGER.debug("--> Exit aggiornaSedeEntita ");
        return areaRifornimentoAggiornata;
    }

    @Override
    public void cancellaAreaRifornimento(Integer id) {
        LOGGER.debug("--> Enter cancellaAreaRifornimento");
        start("cancellaAreaRifornimento");
        try {
            vendingDocumentoService.cancellaAreaRifornimento(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreaRifornimento");
        }
        LOGGER.debug("--> Exit cancellaAreaRifornimento ");
    }

    @Override
    public void cancellaLetturaSelezionatrice(Integer id) {
        LOGGER.debug("--> Enter cancellaLetturaSelezionatrice");
        start("cancellaLetturaSelezionatrice");
        try {
            vendingDocumentoService.cancellaLetturaSelezionatrice(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaLetturaSelezionatrice");
        }
        LOGGER.debug("--> Exit cancellaLetturaSelezionatrice ");
    }

    @Override
    public void cancellaRilevazioneEvaDts(Integer id) {
        LOGGER.debug("--> Enter cancellaRilevazioneEvaDts");
        start("cancellaRilevazioneEvaDts");
        try {
            vendingDocumentoService.cancellaRilevazioneEvaDts(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRilevazioneEvaDts");
        }
        LOGGER.debug("--> Exit cancellaRilevazioneEvaDts ");
    }

    @Override
    public List<AreaRifornimento> caricaAreaRifornimento() {
        LOGGER.debug("--> Enter caricaAreaRifornimento");
        start("caricaAreaRifornimento");

        List<AreaRifornimento> result = null;
        try {
            result = vendingDocumentoService.caricaAreaRifornimento();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaRifornimento");
        }
        LOGGER.debug("--> Exit caricaAreaRifornimento ");
        return result;
    }

    @Override
    public AreaRifornimento caricaAreaRifornimentoById(Integer id) {
        LOGGER.debug("--> Enter caricaAreaRifornimentoById");
        start("caricaAreaRifornimentoById");

        AreaRifornimento object = null;
        try {
            object = vendingDocumentoService.caricaAreaRifornimentoById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaRifornimentoById");
        }
        LOGGER.debug("--> Exit caricaAreaRifornimentoById ");
        return object;
    }

    @Override
    public LetturaSelezionatrice caricaLetturaSelezionatriceById(Integer id) {
        LOGGER.debug("--> Enter caricaLetturaSelezionatriceById");
        start("caricaLetturaSelezionatriceById");

        LetturaSelezionatrice lettura = null;
        try {
            lettura = vendingDocumentoService.caricaLetturaSelezionatriceById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaLetturaSelezionatriceById");
        }
        LOGGER.debug("--> Exit caricaLetturaSelezionatriceById ");
        return lettura;
    }

    @Override
    public RilevazioneEvaDts caricaRilevazioneEvaDtsById(Integer id) {
        LOGGER.debug("--> Enter caricaRilevazioneEvaDtsById");
        start("caricaRilevazioneEvaDtsById");

        RilevazioneEvaDts object = null;
        try {
            object = vendingDocumentoService.caricaRilevazioneEvaDtsById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRilevazioneEvaDtsById");
        }
        LOGGER.debug("--> Exit caricaRilevazioneEvaDtsById ");
        return object;
    }

    @Override
    public List<RilevazioneEvaDts> caricaRilevazioniEvaDts() {
        LOGGER.debug("--> Enter caricaRilevazioniEvaDts");
        start("caricaRilevazioniEvaDts");

        List<RilevazioneEvaDts> result = null;
        try {
            result = vendingDocumentoService.caricaRilevazioniEvaDts();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRilevazioniEvaDts");
        }
        LOGGER.debug("--> Exit caricaRilevazioniEvaDts ");
        return result;
    }

    @Override
    public RisultatiChiusuraLettureDTO chiudiLettureSelezionatrice(List<LetturaSelezionatrice> letture) {
        LOGGER.debug("--> Enter chiudiLettureSelezionatrice");
        start("chiudiLettureSelezionatrice");

        RisultatiChiusuraLettureDTO risultati = null;
        try {
            risultati = vendingDocumentoService.chiudiLettureSelezionatrice(letture);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("chiudiLettureSelezionatrice");
        }
        LOGGER.debug("--> Exit chiudiLettureSelezionatrice ");
        return risultati;
    }

    @Override
    public ImportazioneFileEvaDtsResult importaEvaDts(String fileName, byte[] fileContent,
            EvaDtsImportFolder evaDtsImportFolder) {
        LOGGER.debug("--> Enter importaEvaDts");
        start("importaEvaDts");

        ImportazioneFileEvaDtsResult result = null;
        try {
            result = vendingDocumentoService.importaEvaDts(fileName, fileContent, evaDtsImportFolder);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("importaEvaDts");
        }

        LOGGER.debug("--> Exit importaEvaDts ");
        return result;
    }

    @Override
    public ImportazioneFileEvaDtsResult importaEvaDts(String fileName, byte[] fileContent,
            EvaDtsImportFolder evaDtsImportFolder, boolean forzaImportazione) {
        LOGGER.debug("--> Enter importaEvaDts");
        start("importaEvaDts");

        ImportazioneFileEvaDtsResult result = null;
        try {
            result = vendingDocumentoService.importaEvaDts(fileName, fileContent, evaDtsImportFolder,
                    forzaImportazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("importaEvaDts");
        }

        LOGGER.debug("--> Exit importaEvaDts ");
        return result;
    }

    @Override
    public List<AreaRifornimento> ricercaAreeRifornimento(ParametriRicercaAreeRifornimento parametri) {
        LOGGER.debug("--> Enter ricercaAreeRifornimento");
        start("ricercaAreeRifornimento");

        List<AreaRifornimento> aree = null;
        try {
            aree = vendingDocumentoService.ricercaAreeRifornimento(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaAreeRifornimento");
        }
        LOGGER.debug("--> Exit ricercaAreeRifornimento ");
        return aree;
    }

    @Override
    public List<LetturaSelezionatrice> ricercaLettureSelezionatrice(Integer idLettura) {
        return vendingDocumentoService.ricercaLettureSelezionatrice(idLettura);
    }

    @Override
    public List<RigaLetturaSelezionatrice> ricercaRigheLetturaSelezionatrice(Integer progressivo) {
        LOGGER.debug("--> Enter ricercaRigheLetturaSelezionatrice");
        start("ricercaRigheLetturaSelezionatrice");

        List<RigaLetturaSelezionatrice> righe = null;
        try {
            righe = vendingDocumentoService.ricercaRigheLetturaSelezionatrice(progressivo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaRigheLetturaSelezionatrice");
        }
        LOGGER.debug("--> Exit ricercaRigheLetturaSelezionatrice ");
        return righe;
    }

    @Override
    public List<RilevazioneEvaDts> ricercaRilevazioniEvaDts(ParametriRicercaRilevazioniEvaDts parametri) {
        LOGGER.debug("--> Enter ricercaRilevazioniEvaDts");
        start("ricercaRilevazioniEvaDts");

        List<RilevazioneEvaDts> rilevazioni = null;
        try {
            rilevazioni = vendingDocumentoService.ricercaRilevazioniEvaDts(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaRilevazioniEvaDts");
        }
        LOGGER.debug("--> Exit ricercaRilevazioniEvaDts ");
        return rilevazioni;
    }

    @Override
    public AreaMagazzinoFullDTO salvaAreaMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate,
            Object areaRifornimento, boolean forzaSalvataggio) throws DocumentoAssenteBloccaException,
            DocumentoAssenteAvvisaException, DocumentiEsistentiPerAreaMagazzinoException {

        AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoService.salvaAreaMagazzino(areaMagazzino,
                areaRate, forzaSalvataggio);
        if (areaRifornimento != null) {
            ((AreaRifornimento) areaRifornimento).setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
            AreaRifornimento areaRifornimentoSave = vendingDocumentoService
                    .salvaAreaRifornimento((AreaRifornimento) areaRifornimento);
            areaMagazzinoFullDTO.setAreaRifornimento(areaRifornimentoSave);
        }

        return areaMagazzinoFullDTO;
    }

    @Override
    public AreaOrdineFullDTO salvaAreaOrdine(AreaOrdine areaOrdine, AreaRate areaRate, Object areaRifornimento) {
        AreaOrdineFullDTO areaOrdineFullDTO = ordiniDocumentoService.salvaAreaOrdine(areaOrdine, areaRate);
        if (areaRifornimento != null) {
            ((AreaRifornimento) areaRifornimento).setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
            AreaRifornimento areaRifornimentoSave = vendingDocumentoService
                    .salvaAreaRifornimento((AreaRifornimento) areaRifornimento);
            areaOrdineFullDTO.setAreaRifornimento(areaRifornimentoSave);
        }

        return areaOrdineFullDTO;

    }

    @Override
    public LetturaSelezionatrice salvaLetturaSelezionatrice(LetturaSelezionatrice letturaSelezionatrice) {
        LOGGER.debug("--> Enter salvaLetturaSelezionatrice");
        start("salvaLetturaSelezionatrice");
        LetturaSelezionatrice lettura = null;
        try {
            lettura = vendingDocumentoService.salvaLetturaSelezionatrice(letturaSelezionatrice);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaLetturaSelezionatrice");
        }
        LOGGER.debug("--> Exit salvaLetturaSelezionatrice ");
        return lettura;
    }

    @Override
    public RilevazioneEvaDts salvaRilevazioneEvaDts(RilevazioneEvaDts rilevazioneEvaDts) {
        LOGGER.debug("--> Enter salvaRilevazioneEvaDts");
        start("salvaRilevazioneEvaDts");

        RilevazioneEvaDts object = null;
        try {
            object = vendingDocumentoService.salvaRilevazioneEvaDts(rilevazioneEvaDts);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRilevazioneEvaDts");
        }
        LOGGER.debug("--> Exit salvaRilevazioneEvaDts ");
        return object;
    }

    /**
     * @param magazzinoDocumentoService
     *            the magazzinoDocumentoService to set
     */
    public void setMagazzinoDocumentoService(MagazzinoDocumentoService magazzinoDocumentoService) {
        this.magazzinoDocumentoService = magazzinoDocumentoService;
    }

    /**
     * @param ordiniDocumentoService
     *            The ordiniDocumentoService to set.
     */
    public void setOrdiniDocumentoService(OrdiniDocumentoService ordiniDocumentoService) {
        this.ordiniDocumentoService = ordiniDocumentoService;
    }

    /**
     * @param vendingDocumentoService
     *            the vendingDocumentoService to set
     */
    public void setVendingDocumentoService(VendingDocumentoService vendingDocumentoService) {
        this.vendingDocumentoService = vendingDocumentoService;
    }
}
