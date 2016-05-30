package it.eurotn.panjea.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.panjea.stampe.service.interfaces.LayoutStampeService;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class LayoutStampeBD extends AbstractBaseBD implements ILayoutStampeBD {

    public static final String BEAN_ID = "layoutStampeBD";

    private static final Logger LOGGER = Logger.getLogger(LayoutStampeBD.class);

    private LayoutStampeService layoutStampeService;

    /**
     * Costruttore.
     */
    public LayoutStampeBD() {
        super();
    }

    @Override
    public LayoutStampa aggiungiLayoutStampa(ITipoAreaDocumento tipoAreaDocumento, String reportName, EntitaLite entita,
            SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter aggiungiLayoutStampa");
        start("aggiungiLayoutStampa");
        LayoutStampa layoutStampa = null;
        try {
            layoutStampa = layoutStampeService.aggiungiLayoutStampa(tipoAreaDocumento, reportName, entita, sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiungiLayoutStampa");
        }
        LOGGER.debug("--> Exit aggiungiLayoutStampa ");
        return layoutStampa;
    }

    @Override
    public void cancellaLayoutStampa(LayoutStampa layoutStampa) {
        LOGGER.debug("--> Enter cancellaLayoutStampa");
        start("cancellaLayoutStampa");
        try {
            layoutStampeService.cancellaLayoutStampa(layoutStampa);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaLayoutStampa");
        }
        LOGGER.debug("--> Exit cancellaLayoutStampa ");
    }

    @Override
    public List<IClasseTipoDocumento> caricaClassiTipoDocumento() {
        LOGGER.debug("--> Enter caricaClassiTipoDocumento");
        start("caricaClassiTipoDocumento");
        List<IClasseTipoDocumento> classiTipoDoc = null;
        try {
            classiTipoDoc = layoutStampeService.caricaClassiTipoDocumento();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaClassiTipoDocumento");
        }
        LOGGER.debug("--> Exit caricaClassiTipoDocumento ");
        return classiTipoDoc;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampaBatch(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
            SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter caricaLayoutStampaBatch");
        start("caricaLayoutStampaBatch");
        List<LayoutStampaDocumento> layouts = null;
        try {
            layouts = layoutStampeService.caricaLayoutStampaBatch(tipoAreaDocumento, entita, sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaLayoutStampaBatch");
        }
        LOGGER.debug("--> Exit caricaLayoutStampaBatch ");
        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampaPerDocumenti() {
        LOGGER.debug("--> Enter caricaLayoutStampaPerDocumenti");
        start("caricaLayoutStampaPerDocumenti");
        List<LayoutStampaDocumento> result = null;
        try {
            result = layoutStampeService.caricaLayoutStampaPerDocumenti();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaLayoutStampaPerDocumenti");
        }
        LOGGER.debug("--> Exit caricaLayoutStampaPerDocumenti ");
        return result;
    }

    @Override
    public List<LayoutStampa> caricaLayoutStampe() {
        LOGGER.debug("--> Enter caricaLayoutStampe");
        start("caricaLayoutStampe");
        List<LayoutStampa> layouts = null;
        try {
            layouts = layoutStampeService.caricaLayoutStampe();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaLayoutStampe");
        }
        LOGGER.debug("--> Exit caricaLayoutStampe ");
        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampe(Integer idEntita) {
        LOGGER.debug("--> Enter caricaLayoutStampe");
        start("caricaLayoutStampe");
        List<LayoutStampaDocumento> layouts = null;
        try {
            layouts = layoutStampeService.caricaLayoutStampe(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaLayoutStampe");
        }
        LOGGER.debug("--> Exit caricaLayoutStampe ");
        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampe(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
            SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter caricaLayoutStampe");
        start("caricaLayoutStampe");
        List<LayoutStampaDocumento> layoutCaricati = null;
        try {
            layoutCaricati = layoutStampeService.caricaLayoutStampe(tipoAreaDocumento, entita, sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaLayoutStampe");
        }
        LOGGER.debug("--> Exit caricaLayoutStampe ");
        return layoutCaricati;
    }

    @Override
    public LayoutStampa caricaLayoutStampe(String reportName) {
        LOGGER.debug("--> Enter caricaLayoutStampe");
        start("caricaLayoutStampe");
        LayoutStampa result = null;
        try {
            result = layoutStampeService.caricaLayoutStampe(reportName);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaLayoutStampe");
        }
        LOGGER.debug("--> Exit caricaLayoutStampe ");
        return result;
    }

    @Override
    public List<ITipoAreaDocumento> caricaTipoAree(String classeTipoDocumento) {
        LOGGER.debug("--> Enter caricaTipoAree");
        start("caricaTipoAree");
        List<ITipoAreaDocumento> tipiAree = null;
        try {
            tipiAree = layoutStampeService.caricaTipoAree(classeTipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAree");
        }
        LOGGER.debug("--> Exit caricaTipoAree ");
        return tipiAree;
    }

    @Override
    public LayoutStampa salvaLayoutStampa(LayoutStampa layoutStampa) {
        LOGGER.debug("--> Enter salvaLayoutStampa");
        start("salvaLayoutStampa");
        LayoutStampa layoutStampaSalvata = null;
        try {
            layoutStampaSalvata = layoutStampeService.salvaLayoutStampa(layoutStampa);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaLayoutStampa");
        }
        LOGGER.debug("--> Exit salvaLayoutStampa ");
        return layoutStampaSalvata;
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutAsDefault(LayoutStampaDocumento layoutStampa) {
        LOGGER.debug("--> Enter setLayoutAsDefault");
        start("setLayoutAsDefault");
        List<LayoutStampaDocumento> layouts = null;
        try {
            layouts = layoutStampeService.setLayoutAsDefault(layoutStampa);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("setLayoutAsDefault");
        }
        LOGGER.debug("--> Exit setLayoutAsDefault ");
        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutForInvioMail(LayoutStampaDocumento layoutStampa) {
        LOGGER.debug("--> Enter setLayoutForInvioMail");
        start("setLayoutForInvioMail");

        List<LayoutStampaDocumento> layouts = null;
        try {
            layouts = layoutStampeService.setLayoutForInvioMail(layoutStampa);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("setLayoutForInvioMail");
        }
        LOGGER.debug("--> Exit setLayoutForInvioMail ");
        return layouts;
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutForUsoInterno(LayoutStampaDocumento layoutStampa, boolean usoInterno) {
        LOGGER.debug("--> Enter setLayoutForUsoInterno");
        start("setLayoutForUsoInterno");

        List<LayoutStampaDocumento> layouts = null;
        try {
            layouts = layoutStampeService.setLayoutForUsoInterno(layoutStampa, usoInterno);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("setLayoutForUsoInterno");
        }
        LOGGER.debug("--> Exit setLayoutForUsoInterno ");
        return layouts;
    }

    /**
     * @param layoutStampeService
     *            The layoutStampeService to set.
     */
    public void setLayoutStampeService(LayoutStampeService layoutStampeService) {
        this.layoutStampeService = layoutStampeService;
    }

}
