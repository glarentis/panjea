package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.logicaldoc.webservice.document.DocumentClient;
import com.logicaldoc.webservice.document.WSDocument;
import com.logicaldoc.webservice.folder.FolderClient;
import com.logicaldoc.webservice.folder.WSFolder;

import it.eurotn.panjea.dms.manager.interfaces.DMSLookupClientWebService;
import it.eurotn.panjea.dms.manager.interfaces.DMSSecurityManager;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.ImportazioneException;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.DocumentiImportazioneManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.PalmariImportaManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.PalmariImportaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PalmariImportaManager")
public class PalmariImportaManagerBean implements PalmariImportaManager {

    @EJB
    private DMSLookupClientWebService dmsLookupClientWebService;
    @EJB
    private DMSSecurityManager dmsSecurityManager;

    @EJB
    private DocumentiImportazioneManager documentiImportazioneManager;

    @Resource
    private SessionContext sessionContext;

    /**
     * @return codiceAzienda
     */
    protected String getCodiceAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    private WSFolder getTrasmissioniFolder(String sid) {
        FolderClient folderService = dmsLookupClientWebService.creaFolderService();
        WSFolder folder = null;
        try {
            WSFolder rootFolder = folderService.getDefaultWorkspace(sid);
            Calendar calNow = Calendar.getInstance();
            String datePath = DateFormatUtils.format(calNow, "yyyy/MM/dd");
            folder = folderService.createPath(sid, rootFolder.getId(),
                    getCodiceAzienda() + "/vending/palmari/ricezioni/" + datePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder;
    }

    @Override
    public String importa(String codiceOperatore, byte[] contenutoFile) throws ImportazioneException {
        File tmpFile = null;
        String result = "";
        try {
            tmpFile = File.createTempFile("000" + codiceOperatore, ".xml");
            IOUtils.write(contenutoFile, new FileOutputStream(tmpFile));

            pubblica(codiceOperatore, tmpFile);
            ImportazioneXml xml = new ImportazioneXml(contenutoFile);
            try {
                result = documentiImportazioneManager.importa(codiceOperatore, xml);
            } catch (ImportazioneException e) {
                sessionContext.setRollbackOnly();
                throw e;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tmpFile != null) {
                tmpFile.delete();
            }
        }
        return result;
    }

    private void pubblica(String codiceOperatore, File fileDaPubblicare) {
        try {
            String sid = dmsSecurityManager.login();
            WSFolder folder = getTrasmissioniFolder(sid);
            WSDocument wsDoc = new WSDocument();
            wsDoc.setId(0L);
            wsDoc.setFolderId(folder.getId());
            wsDoc.setTitle(codiceOperatore + ".xml");
            wsDoc.setFileName(codiceOperatore + ".xml");
            // wsDoc.setCoverage("coverage");
            // wsDoc.setCustomId("yyyyyyyy");
            DocumentClient documentiService = dmsLookupClientWebService.creaDocumentService();
            documentiService.create(sid, wsDoc, new DataHandler(new FileDataSource(fileDaPubblicare)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
