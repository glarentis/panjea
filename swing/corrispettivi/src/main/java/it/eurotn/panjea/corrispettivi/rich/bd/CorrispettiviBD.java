package it.eurotn.panjea.corrispettivi.rich.bd;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.TotaliCodiceIvaDTO;
import it.eurotn.panjea.corrispettivi.service.interfaces.CorrispettiviService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class CorrispettiviBD extends AbstractBaseBD implements ICorrispettiviBD {

    private static final Logger LOGGER = Logger.getLogger(CorrispettiviBD.class);

    public static final String BEAN_ID = "corrispettiviBD";

    private CorrispettiviService corrispettiviService;

    @Override
    public void cancellaCorrispettivoLinkTipoDocumento(Integer id) {
        LOGGER.debug("--> Enter cancellaCorrispettivoLinkTipoDocumento");
        start("cancellaCorrispettivoLinkTipoDocumento");
        try {
            corrispettiviService.cancellaCorrispettivoLinkTipoDocumento(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCorrispettivoLinkTipoDocumento");
        }
        LOGGER.debug("--> Exit cancellaCorrispettivoLinkTipoDocumento ");
    }

    @Override
    public CalendarioCorrispettivo caricaCalendarioCorrispettivo(int anno, int mese, TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaCalendarioCorrispettivo");
        start("caricaCalendarioCorrispettivo");

        CalendarioCorrispettivo calendarioCorrispettivo = null;
        try {
            calendarioCorrispettivo = corrispettiviService.caricaCalendarioCorrispettivo(anno, mese, tipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCalendarioCorrispettivo");
        }
        LOGGER.debug("--> Exit caricaCalendarioCorrispettivo ");
        return calendarioCorrispettivo;
    }

    @Override
    public List<CorrispettivoLinkTipoDocumento> caricaCorrispettiviLinkTipoDocumento() {
        LOGGER.debug("--> Enter caricaCorrispettiviLinkTipoDocumento");
        start("caricaCorrispettiviLinkTipoDocumento");

        List<CorrispettivoLinkTipoDocumento> result = null;
        try {
            result = corrispettiviService.caricaCorrispettiviLinkTipoDocumento();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCorrispettiviLinkTipoDocumento");
        }
        LOGGER.debug("--> Exit caricaCorrispettiviLinkTipoDocumento ");
        return result;
    }

    @Override
    public CorrispettivoLinkTipoDocumento caricaCorrispettivoLinkTipoDocumentoById(Integer id) {
        LOGGER.debug("--> Enter caricaCorrispettivoLinkTipoDocumentoById");
        start("caricaCorrispettivoLinkTipoDocumentoById");

        CorrispettivoLinkTipoDocumento object = null;
        try {
            object = corrispettiviService.caricaCorrispettivoLinkTipoDocumentoById(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCorrispettivoLinkTipoDocumentoById");
        }
        LOGGER.debug("--> Exit caricaCorrispettivoLinkTipoDocumentoById ");
        return object;
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentoCorrispettivi() {
        LOGGER.debug("--> Enter caricaTipiDocumentoCorrispettivi");
        start("caricaTipiDocumentoCorrispettivi");

        List<TipoDocumento> tipiDocumento = null;
        try {
            tipiDocumento = corrispettiviService.caricaTipiDocumentoCorrispettivi();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiDocumentoCorrispettivi");
        }
        LOGGER.debug("--> Exit caricaTipiDocumentoCorrispettivi ");
        return tipiDocumento;
    }

    @Override
    public List<TotaliCodiceIvaDTO> caricaTotaliCalendarioCorrispettivi(
            CalendarioCorrispettivo calendarioCorrispettivo) {
        LOGGER.debug("--> Enter caricaTotaliCalendarioCorrispettivi");
        start("caricaTotaliCalendarioCorrispettivi");

        List<TotaliCodiceIvaDTO> totali = null;
        try {
            totali = corrispettiviService.caricaTotaliCalendarioCorrispettivi(calendarioCorrispettivo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTotaliCalendarioCorrispettivi");
        }
        LOGGER.debug("--> Exit caricaTotaliCalendarioCorrispettivi ");
        return totali;
    }

    @Override
    public CalendarioCorrispettivo creaDocumenti(CalendarioCorrispettivo calendarioCorrispettivo) {
        LOGGER.debug("--> Enter creaDocumenti");
        start("creaDocumenti");

        CalendarioCorrispettivo calendarioCorrispettivoAggiornato = null;
        try {
            calendarioCorrispettivoAggiornato = corrispettiviService.creaDocumenti(calendarioCorrispettivo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaDocumenti");
        }
        LOGGER.debug("--> Exit creaDocumenti ");
        return calendarioCorrispettivoAggiornato;
    }

    @Override
    public void importa(Date data) {
        LOGGER.debug("--> Enter importa");
        start("importa");
        try {
            corrispettiviService.importa(data);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("importa");
        }
        LOGGER.debug("--> Exit importa ");
    }

    @Override
    public Corrispettivo salvaCorrispettivo(Corrispettivo corrispettivo) {
        LOGGER.debug("--> Enter salvaCorrispettivo");
        start("salvaCorrispettivo");

        Corrispettivo corrispettivoSalvato = null;
        try {
            corrispettivoSalvato = corrispettiviService.salvaCorrispettivo(corrispettivo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCorrispettivo");
        }
        LOGGER.debug("--> Exit salvaCorrispettivo ");
        return corrispettivoSalvato;
    }

    @Override
    public CorrispettivoLinkTipoDocumento salvaCorrispettivoLinkTipoDocumento(
            CorrispettivoLinkTipoDocumento corrispettivoLinkTipoDocumento) {
        LOGGER.debug("--> Enter salvaCorrispettivoLinkTipoDocumento");
        start("salvaCorrispettivoLinkTipoDocumento");

        CorrispettivoLinkTipoDocumento object = null;
        try {
            object = corrispettiviService.salvaCorrispettivoLinkTipoDocumento(corrispettivoLinkTipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCorrispettivoLinkTipoDocumento");
        }
        LOGGER.debug("--> Exit salvaCorrispettivoLinkTipoDocumento ");
        return object;
    }

    /**
     * @param corrispettiviService
     *            the corrispettiviService to set
     */
    public void setCorrispettiviService(CorrispettiviService corrispettiviService) {
        this.corrispettiviService = corrispettiviService;
    }
}
