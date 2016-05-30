package it.eurotn.panjea.rich.bd;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;

import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.dms.service.interfaces.DMSService;
import it.eurotn.panjea.rich.logicaldoc.LogicalDocSwingTransferFile;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class DmsBD extends AbstractBaseBD implements IDmsBD {

    private static final Logger LOGGER = Logger.getLogger(DmsBD.class);

    public static final String BEAN_ID = "dmsBD";

    private DMSService dmsService;

    @Override
    public DmsSettings caricaDmsSettings() {
        LOGGER.debug("--> Enter caricaDmsSettings");
        start("caricaDmsSettings");

        DmsSettings dmsSettings = null;
        try {
            dmsSettings = dmsService.caricaDmsSettings();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDmsSettings");
        }
        LOGGER.debug("--> Exit caricaDmsSettings ");
        return dmsSettings;
    }

    @Override
    public void deleteAllegato(long idAllegato) {
        LOGGER.debug("--> Enter deleteDocument");
        start("deleteDocument");
        try {
            dmsService.deleteAllegato(idAllegato);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("deleteDocument");
        }
        LOGGER.debug("--> Exit deleteDocument ");
    }

    @Override
    public List<WSDocument> getAllegati(AllegatoDMS allegatoDms) throws DMSLoginException {
        LOGGER.debug("--> Enter getAllegati");
        start("getAllegati");
        List<WSDocument> result = null;
        try {
            result = dmsService.getAllegati(allegatoDms);
        } catch (DMSLoginException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("getAllegati");
        }
        LOGGER.debug("--> Exit getAllegati ");
        return result;
    }

    @Override
    public byte[] getAllegatoTile(long idAllegato) {
        LOGGER.debug("--> Enter getDocumentTile");
        start("getDocumentTile");
        byte[] result = null;
        try {
            result = dmsService.getAllegatoTile(idAllegato);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("getDocumentTile");
        }
        LOGGER.debug("--> Exit getDocumentTile ");
        return result;
    }

    @Override
    public File getContent(long idDoc) {
        DmsSettings dmsSettings = caricaDmsSettings();

        LogicalDocSwingTransferFile logicalDocSwingTransferFile = new LogicalDocSwingTransferFile(dmsSettings);
        return logicalDocSwingTransferFile.getContent(idDoc);
    }

    @Override
    public byte[] getContentByte(long idDoc) {
        DmsSettings dmsSettings = caricaDmsSettings();

        LogicalDocSwingTransferFile logicalDocSwingTransferFile = new LogicalDocSwingTransferFile(dmsSettings);
        return logicalDocSwingTransferFile.getContentByte(idDoc);
    }

    @Override
    public void pubblicaAllegato(String folderPath, String filePath, String title) {
        pubblicaAllegato(folderPath, filePath, title, null);
    }

    @Override
    public void pubblicaAllegato(String folderPath, String filePath, String title, AllegatoDMS allegatoDms) {
        LOGGER.debug("--> Enter pubblicaAllegato");
        try {

            PanjeaSwingUtil.lockScreen("Pubblicazione in corso");
            DmsSettings dmsSettings = caricaDmsSettings();
            LogicalDocSwingTransferFile logicalDocSwingTransferFile = new LogicalDocSwingTransferFile(dmsSettings);

            String clsid = logicalDocSwingTransferFile.putContent(filePath);
            WSDocument document = new WSDocument();
            document.setPath(folderPath);
            document.setTitle(title);

            Path path = Paths.get(filePath);
            // controllo inserito solamente perchè altrimenti find bugs segnalava una possibile NPE
            // se tutti e 3 i
            // controlli non sono verificati
            if (document != null && path != null && path.getFileName() != null) {
                document.setFileName(path.getFileName().toString());
                if (allegatoDms == null) {
                    dmsService.addAllegato(clsid, document);
                } else {
                    dmsService.addAllegato(clsid, document, allegatoDms);
                }

            }
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            PanjeaSwingUtil.unlockScreen();
        }
        LOGGER.debug("--> Exit pubblicaAllegato");
    }

    @Override
    public DmsSettings salvaDmsSettings(DmsSettings dmsSettings) {
        LOGGER.debug("--> Enter salvaDmsSettings");
        start("salvaDmsSettings");

        DmsSettings dmsSettingsSalvato = null;
        try {
            dmsSettingsSalvato = dmsService.salvaDmsSettings(dmsSettings);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaDmsSettings");
        }
        LOGGER.debug("--> Exit salvaDmsSettings ");
        return dmsSettingsSalvato;
    }

    /**
     * @param dmsService
     *            the dmsService to set
     */
    public void setDmsService(DMSService dmsService) {
        this.dmsService = dmsService;
    }

}
