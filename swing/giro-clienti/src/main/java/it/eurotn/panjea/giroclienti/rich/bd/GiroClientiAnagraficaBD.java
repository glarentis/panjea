package it.eurotn.panjea.giroclienti.rich.bd;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.giroclienti.domain.GiroClientiSettings;
import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.domain.ModalitaCopiaGiroClienti;
import it.eurotn.panjea.giroclienti.service.interfaces.GiroClientiAnagraficaService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.util.Giorni;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class GiroClientiAnagraficaBD extends AbstractBaseBD implements IGiroClientiAnagraficaBD {

    private static final Logger LOGGER = Logger.getLogger(GiroClientiAnagraficaBD.class);

    public static final String BEAN_ID = "giroClientiAnagraficaBD";

    private GiroClientiAnagraficaService giroClientiAnagraficaService;

    @Override
    public void cancellaGiroSedeCliente(GiroSedeCliente giroSedeCliente) {
        LOGGER.debug("--> Enter cancellaGiroSedeClienteGiro");
        start("cancellaGiroSedeClienteGiro");
        try {
            giroClientiAnagraficaService.cancellaGiroSedeCliente(giroSedeCliente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaGiroSedeClienteGiro");
        }
        LOGGER.debug("--> Exit cancellaGiroSedeClienteGiro ");
    }

    @Override
    public void cancellaGiroSedeCliente(Integer idEntita) {
        LOGGER.debug("--> Enter cancellaGiroSedeClienteIdEntita");
        start("cancellaGiroSedeClienteIdEntita");
        try {
            giroClientiAnagraficaService.cancellaGiroSedeCliente(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaGiroSedeClienteIdEntita");
        }
        LOGGER.debug("--> Exit cancellaGiroSedeClienteIdEntita ");
    }

    @Override
    public void cancellaGiroSedeCliente(Integer idSedeEntita, Giorni giorno, Date ora) {
        LOGGER.debug("--> Enter cancellaGiroSedeCliente");
        start("cancellaGiroSedeCliente");
        try {
            giroClientiAnagraficaService.cancellaGiroSedeCliente(idSedeEntita, giorno, ora);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaGiroSedeCliente");
        }
        LOGGER.debug("--> Exit cancellaGiroSedeCliente ");
    }

    @Override
    public GiroClientiSettings caricaGiroClientiSettings() {
        LOGGER.debug("--> Enter caricaGiroClientiSettings");
        start("caricaGiroClientiSettings");

        GiroClientiSettings giroClientiSettings = null;
        try {
            giroClientiSettings = giroClientiAnagraficaService.caricaGiroClientiSettings();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaGiroClientiSettings");
        }
        LOGGER.debug("--> Exit caricaGiroClientiSettings ");
        return giroClientiSettings;
    }

    @Override
    public List<GiroSedeCliente> caricaGiroSedeCliente(Integer idEntita) {
        LOGGER.debug("--> Enter caricaGiroSedeCliente");
        start("caricaGiroSedeCliente");

        List<GiroSedeCliente> giro = null;
        try {
            giro = giroClientiAnagraficaService.caricaGiroSedeCliente(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaGiroSedeCliente");
        }
        LOGGER.debug("--> Exit caricaGiroSedeCliente ");
        return giro;
    }

    @Override
    public void copiaGiroSedeClienti(Integer idUtente, Giorni giorno, Integer idUtenteDestinazione,
            Giorni giornoDestinazione, ModalitaCopiaGiroClienti modalitaCopia) {
        LOGGER.debug("--> Enter copiaGiroSedeClienti");
        start("copiaGiroSedeClienti");
        try {
            giroClientiAnagraficaService.copiaGiroSedeClienti(idUtente, giorno, idUtenteDestinazione,
                    giornoDestinazione, modalitaCopia);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("copiaGiroSedeClienti");
        }
        LOGGER.debug("--> Exit copiaGiroSedeClienti ");

    }

    @Override
    public GiroClientiSettings salvaGiroClientiSettings(GiroClientiSettings giroClientiSettings) {
        LOGGER.debug("--> Enter salvaGiroClientiSettings");
        start("salvaGiroClientiSettings");

        GiroClientiSettings giroClientiSettingsSalvato = null;
        try {
            giroClientiSettingsSalvato = giroClientiAnagraficaService.salvaGiroClientiSettings(giroClientiSettings);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaGiroClientiSettings");
        }
        LOGGER.debug("--> Exit salvaGiroClientiSettings ");
        return giroClientiSettingsSalvato;
    }

    @Override
    public GiroSedeCliente salvaGiroSedeCliente(GiroSedeCliente giroSedeCliente) {
        LOGGER.debug("--> Enter salvaGiroSedeCliente");
        start("salvaGiroSedeClienteGiro");

        GiroSedeCliente giroSedeClienteSalvato = null;
        try {
            giroSedeClienteSalvato = giroClientiAnagraficaService.salvaGiroSedeCliente(giroSedeCliente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaGiroSedeClienteGiro");
        }
        LOGGER.debug("--> Exit salvaGiroSedeCliente ");
        return giroSedeClienteSalvato;
    }

    @Override
    public void salvaGiroSedeCliente(List<GiroSedeCliente> giri) {
        LOGGER.debug("--> Enter salvaGiroSedeCliente");
        start("salvaGiroSedeCliente");
        try {
            giroClientiAnagraficaService.salvaGiroSedeCliente(giri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaGiroSedeCliente");
        }
        LOGGER.debug("--> Exit salvaGiroSedeCliente ");
    }

    /**
     * @param giroClientiAnagraficaService
     *            the giroClientiAnagraficaService to set
     */
    public void setGiroClientiAnagraficaService(GiroClientiAnagraficaService giroClientiAnagraficaService) {
        this.giroClientiAnagraficaService = giroClientiAnagraficaService;
    }

}
