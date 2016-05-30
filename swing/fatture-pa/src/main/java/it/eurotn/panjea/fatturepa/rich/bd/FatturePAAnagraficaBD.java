package it.eurotn.panjea.fatturepa.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.domain.TipoRegimeFiscale;
import it.eurotn.panjea.fatturepa.service.interfaces.FatturePAAnagraficaService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author fattazzo
 *
 */
public class FatturePAAnagraficaBD extends AbstractBaseBD implements IFatturePAAnagraficaBD {

    public static final String BEAN_ID = "fatturePAAnagraficaBD";

    private static final Logger LOGGER = Logger.getLogger(FatturePAAnagraficaBD.class);

    private FatturePAAnagraficaService fatturePAAnagraficaService;

    @Override
    public AziendaFatturaPA caricaAziendaFatturaPA() {
        LOGGER.debug("--> Enter caricaAziendaFatturaPA");
        start("caricaAziendaFatturaPA");

        AziendaFatturaPA aziendaFatturaPA = null;
        try {
            aziendaFatturaPA = fatturePAAnagraficaService.caricaAziendaFatturaPA();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAziendaFatturaPA");
        }
        LOGGER.debug("--> Exit caricaAziendaFatturaPA ");
        return aziendaFatturaPA;
    }

    @Override
    public FatturaPASettings caricaFatturaPASettings() {
        LOGGER.debug("--> Enter caricaFatturaPASettings");
        start("caricaFatturaPASettings");

        FatturaPASettings fatturaPASettings = null;
        try {
            fatturaPASettings = fatturePAAnagraficaService.caricaFatturaPASettings();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFatturaPASettings");
        }
        LOGGER.debug("--> Exit caricaFatturaPASettings ");
        return fatturaPASettings;
    }

    @Override
    public List<TipoRegimeFiscale> caricaTipiRegimiFiscali() {
        LOGGER.debug("--> Enter caricaTipiRegimiFiscali");
        start("caricaTipiRegimiFiscali");

        List<TipoRegimeFiscale> regimi = null;
        try {
            regimi = fatturePAAnagraficaService.caricaTipiRegimiFiscali();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiRegimiFiscali");
        }
        LOGGER.debug("--> Exit caricaTipiRegimiFiscali ");
        return regimi;
    }

    @Override
    public String checkMailForTest() {
        LOGGER.debug("--> Enter checkMailForTest");
        start("checkMailForTest");

        String result = null;
        try {
            result = fatturePAAnagraficaService.checkMailForTest();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("checkMailForTest");
        }
        LOGGER.debug("--> Exit checkMailForTest ");
        return result;
    }

    @Override
    public AziendaFatturaPA salvaAziendaFatturaPA(AziendaFatturaPA aziendaFatturaPA) {
        LOGGER.debug("--> Enter salvaAziendaFatturaPA");
        start("salvaAziendaFatturaPA");
        AziendaFatturaPA aziendaFatturaPASalvata = null;
        try {
            aziendaFatturaPASalvata = fatturePAAnagraficaService.salvaAziendaFatturaPA(aziendaFatturaPA);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAziendaFatturaPA");
        }
        LOGGER.debug("--> Exit salvaAziendaFatturaPA ");
        return aziendaFatturaPASalvata;
    }

    @Override
    public FatturaPASettings salvaFatturaPASettings(FatturaPASettings fatturaPaSettings) {
        LOGGER.debug("--> Enter salvaFatturaPASettings");
        start("salvaFatturaPASettings");

        FatturaPASettings fatturaPASettingsSalvate = null;
        try {
            fatturaPASettingsSalvate = fatturePAAnagraficaService.salvaFatturaPASettings(fatturaPaSettings);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaFatturaPASettings");
        }
        LOGGER.debug("--> Exit salvaFatturaPASettings ");
        return fatturaPASettingsSalvate;
    }

    /**
     * @param fatturePAAnagraficaService
     *            the fatturePAAnagraficaService to set
     */
    public void setFatturePAAnagraficaService(FatturePAAnagraficaService fatturePAAnagraficaService) {
        this.fatturePAAnagraficaService = fatturePAAnagraficaService;
    }

}
