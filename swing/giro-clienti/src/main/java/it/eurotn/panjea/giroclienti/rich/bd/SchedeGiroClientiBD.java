package it.eurotn.panjea.giroclienti.rich.bd;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.service.interfaces.SchedeGiroClientiService;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClientiDTO;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class SchedeGiroClientiBD extends AbstractBaseBD implements ISchedeGiroClientiBD {

    private static final Logger LOGGER = Logger.getLogger(SchedeGiroClientiBD.class);

    public static final String BEAN_ID = "schedeGiroClientiBD";

    private SchedeGiroClientiService schedeGiroClientiService;

    @Override
    public void cancellaSchede(Utente utente) {
        LOGGER.debug("--> Enter cancellaSchede");
        start("cancellaSchede");
        try {
            schedeGiroClientiService.cancellaSchede(utente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaSchede");
        }
        LOGGER.debug("--> Exit cancellaSchede ");

    }

    @Override
    public Date[] caricaDateSchedaSettimanale(Integer idUtente) {
        LOGGER.debug("--> Enter caricaDateSchedaSettimanale");
        start("caricaDateSchedaSettimanale");

        Date[] date = null;
        try {
            date = schedeGiroClientiService.caricaDateSchedaSettimanale(idUtente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDateSchedaSettimanale");
        }
        LOGGER.debug("--> Exit caricaDateSchedaSettimanale ");
        return date;
    }

    @Override
    public RigaGiroCliente caricaRigaGiroCliente(Integer idRiga) {
        LOGGER.debug("--> Enter caricaRigaGiroCliente");
        start("caricaRigaGiroCliente");

        RigaGiroCliente rigaGiroCliente = null;
        try {
            rigaGiroCliente = schedeGiroClientiService.caricaRigaGiroCliente(idRiga);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaGiroCliente");
        }
        LOGGER.debug("--> Exit caricaRigaGiroCliente ");
        return rigaGiroCliente;
    }

    @Override
    public List<RigaGiroCliente> caricaRigheGiroCliente(Giorni giorno, Utente utente) {
        LOGGER.debug("--> Enter caricaRigheGiroCliente");
        start("caricaRigheGiroCliente");

        List<RigaGiroCliente> righe = null;
        try {
            righe = schedeGiroClientiService.caricaRigheGiroCliente(giorno, utente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheGiroCliente");
        }
        LOGGER.debug("--> Exit caricaRigheGiroCliente ");
        return righe;
    }

    @Override
    public SchedaGiroClientiDTO caricaSchedaSettimana(Integer idUtente, Giorni giorno) {
        LOGGER.debug("--> Enter caricaSchedaSettimana");
        start("caricaSchedaSettimana");

        SchedaGiroClientiDTO schedaDTO = null;
        try {
            schedaDTO = schedeGiroClientiService.caricaSchedaSettimana(idUtente, giorno);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSchedaSettimana");
        }
        LOGGER.debug("--> Exit caricaSchedaSettimana ");
        return schedaDTO;
    }

    @Override
    public void creaAreaOrdineGiroCliente(Integer idRigaGiroCliente) {
        LOGGER.debug("--> Enter creaAreaOrdineGiroCliente");
        start("creaAreaOrdineGiroCliente");

        try {
            schedeGiroClientiService.creaAreaOrdineGiroCliente(idRigaGiroCliente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaAreaOrdineGiroCliente");
        }
        LOGGER.debug("--> Exit creaAreaOrdineGiroCliente ");
    }

    @Override
    public RigaGiroCliente salvaRigaGiroCliente(RigaGiroCliente rigaGiroCliente) {
        LOGGER.debug("--> Enter salvaRigaGiroCliente");
        start("salvaRigaGiroCliente");

        RigaGiroCliente rigaGiroClienteSalvata = null;
        try {
            rigaGiroClienteSalvata = schedeGiroClientiService.salvaRigaGiroCliente(rigaGiroCliente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaGiroCliente");
        }
        LOGGER.debug("--> Exit salvaRigaGiroCliente ");
        return rigaGiroClienteSalvata;

    }

    /**
     * @param schedeGiroClientiService
     *            the schedeGiroClientiService to set
     */
    public void setSchedeGiroClientiService(SchedeGiroClientiService schedeGiroClientiService) {
        this.schedeGiroClientiService = schedeGiroClientiService;
    }

}
