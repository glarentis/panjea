package it.eurotn.panjea.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.interfaces.MailService;
import it.eurotn.panjea.anagrafica.util.MailDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaMail;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author Leonardo
 *
 */
public class MailBD extends AbstractBaseBD implements IMailBD {

    private static final Logger LOGGER = Logger.getLogger(MailBD.class);

    public static final String BEAN_ID = "mailBD";

    private MailService mailService;

    @Override
    public List<MailDTO> caricaMails(ParametriRicercaMail parametriRicercaMail) {
        LOGGER.debug("--> Enter caricaMails");
        start("caricaMails");
        List<MailDTO> result = null;
        try {
            result = mailService.caricaMails(parametriRicercaMail);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMails");
        }
        LOGGER.debug("--> Exit caricaMails ");
        return result;
    }

    @Override
    public Integer caricaNumeroMailDaImportare() {
        LOGGER.debug("--> Enter caricaNumeroMailDaImportare");
        start("caricaNumeroMailDaImportare");
        Integer numeroMail = 0;
        try {
            numeroMail = mailService.caricaNumeroMailDaImportare();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaNumeroMailDaImportare");
        }
        LOGGER.debug("--> Exit caricaNumeroMailDaImportare ");
        return numeroMail;
    }

    @AsyncMethodInvocation
    @Override
    public void importaMail() {
        LOGGER.debug("--> Enter importaMail");
        start("importaMail");
        try {
            mailService.importaMail();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("importaMail");
        }
        LOGGER.debug("--> Exit importaMail ");

    }

    @Override
    public void salvaMail(byte[] emlFile, EntitaLite entita, boolean inviata) {
        LOGGER.debug("--> Enter salvaMail");

        try {
            mailService.salvaMail(emlFile, entita, inviata);
        } catch (Exception e) {
            LOGGER.error("---> salvare il Mail  ", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("--> Exit salvaMail");
    }

    /**
     * @param mailService
     *            The mailService to set.
     */
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
}
