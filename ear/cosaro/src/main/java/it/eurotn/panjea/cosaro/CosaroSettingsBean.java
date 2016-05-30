package it.eurotn.panjea.cosaro;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

@Stateless(name = "Panjea.CosaroSettings")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.CosaroSettings")
public class CosaroSettingsBean implements CosaroSettings {

    private static final Logger LOGGER = Logger.getLogger(CosaroSettingsBean.class);

    public static final String PEZZI_ATTRIBUTO = "pezzi";
    public static final String COLLI_ATTRIBUTO = "colli";
    public static final String CONFEZIONI_ATTRIBUTO = "conf";
    public static final String PESO_MIN_ATTRIBUTO = "pesoMin";
    public static final String TARA_PRODOTTO_ATTRIBUTO = "taraProd";
    public static final String TARA_CARTONE_ATTRIBUTO = "taraCarton";
    public static final String PEZZI_CARTONE_ATTRIBUTO = "pzCartone";
    public static final String BAR_CODE_CARTONE = "bcCartone";
    public static final String PESO_MAX_ATTRIBUTO = "pesoMax";

    private static final String CLIENTE_COOP = "clienteCoop";
    private static final String CLIENTE_UNICOMM = "clienteUnicomm";
    private static final String COSARO_IMPORTA_PRODUZIONE_GAMMA_MEAT = "cosaroImportaProduzioneGammaMeat";
    public static final String COSARO_DIR_PRODUZIONE = "cosaroDirProduzione";
    public static final String COSARO_DIR_EVASIONE = "cosaroDirEvasione";
    public static final String COSARO_DIR_IMPORT = "cosaroDirImport";

    public static final String TIPO_DOCUMENTO_UNICOMM = "tipoDocumentoUnicomm";
    public static final String TIPO_DOCUMENTO_COOP = "tipoDocumentoCoop";

    public static final String COSARO_TIPO_DOC_PRODUZIONE = "cosaroTipoDocProduzione";

    public static final String COSARO_PATH_FILE_EXPORT_BILANCE = "cosaroFileExport";

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;

    @EJB
    private PreferenceService preferenceService;

    @Override
    public String caricaCodiceCoop() {
        return caricaPreference(CosaroSettingsBean.CLIENTE_COOP);
    }

    @Override
    public String caricaCodiceTipoDocumentoCoop() {
        return caricaPreference(TIPO_DOCUMENTO_COOP);
    }

    @Override
    public String caricaCodiceTipoDocumentoUnicomm() {
        return caricaPreference(TIPO_DOCUMENTO_UNICOMM);
    }

    @Override
    public String caricaCodiceUnicomm() {
        return caricaPreference(CosaroSettingsBean.CLIENTE_UNICOMM);
    }

    @Override
    public String caricaFilePathBilanceExport() {
        return caricaPreference(COSARO_PATH_FILE_EXPORT_BILANCE);
    }

    /**
     * Carica una chiave dalle preference. Se la chiave non esiste viene spedito um messaggio sulla
     * queue.
     *
     * @param key
     *            chiave delle preference
     * @return valore della preference. null se non trovato.
     */
    private String caricaPreference(String key) {
        String result = null;
        try {
            result = preferenceService.caricaPreference(key).getValore();
        } catch (PreferenceNotFoundException e) {
            LOGGER.error("--> Errore ricerca preference con key " + key, e);
            panjeaMessage.send("Nelle preferenze generali manca la chiave  " + key,
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
        }
        return result;
    }

    @Override
    public TipoAreaMagazzino caricaTamProduzione() {
        String codiceTipoDocumento = caricaPreference(COSARO_TIPO_DOC_PRODUZIONE);
        TipoAreaMagazzino tam = null;

        // Setto il tipo documento
        List<TipoAreaMagazzino> tipiAreaMagazzino = tipiAreaMagazzinoManager
                .caricaTipiAreaMagazzino("tipoDocumento.codice", null, false);
        for (TipoAreaMagazzino tipoAreaMagazzino : tipiAreaMagazzino) {
            if (tipoAreaMagazzino.getTipoDocumento().getCodice().equals(codiceTipoDocumento)) {
                tam = tipoAreaMagazzino;
                break;
            }
        }

        if (tam == null) {
            LOGGER.error("-->errore nel trovare il tipo documento settato nei settings " + codiceTipoDocumento);
            throw new RuntimeException("Tipo documento con codice " + codiceTipoDocumento + " non trovato");
        }
        return tam;
    }

    @Override
    public File getPreferenceDir(String key) {
        String folderWatchPath = caricaPreference(key);
        File folderWatch = new File(folderWatchPath);
        if (!folderWatch.exists() || !folderWatch.isDirectory()) {
            // Errore
            panjeaMessage.send("La cartella " + folderWatch.getAbsolutePath() + " non esiste",
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            return null;
        }
        return folderWatch;
    }

    @Override
    public StreamFactory getStreamTemplate(String templateFileName) {
        // Recupero la cartella dove sono i file di template
        // Recupero la cartella dove esportare i file
        StreamFactory factory = null;
        try {
            String fileNamePath = caricaPreference(templateFileName);
            String templateFile = new StringBuilder(fileNamePath).append(templateFileName).toString();
            factory = StreamFactory.newInstance();
            factory.load(templateFile);
        } catch (Exception e) {
            LOGGER.error("-->errore ", e);
            throw new RuntimeException(
                    "Errore nel caricare il template per l'importazione del file associato alla chiave "
                            + templateFileName,
                    e);
        }
        return factory;
    }

    @Override
    public boolean isGammaMeatEnable() {
        String importaProduzione = caricaPreference(COSARO_IMPORTA_PRODUZIONE_GAMMA_MEAT);
        return new Boolean(importaProduzione).booleanValue();
    }

}
