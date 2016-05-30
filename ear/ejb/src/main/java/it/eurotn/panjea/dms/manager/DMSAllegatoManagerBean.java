package it.eurotn.panjea.dms.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.logicaldoc.webservice.document.WSDocument;
import com.logicaldoc.webservice.folder.FolderClient;
import com.logicaldoc.webservice.folder.WSFolder;
import com.logicaldoc.webservice.search.SearchClient;
import com.logicaldoc.webservice.search.WSSearchOptions;
import com.logicaldoc.webservice.search.WSSearchResult;

import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.exception.DmsException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.dms.manager.interfaces.DMSAllegatoManager;
import it.eurotn.panjea.dms.manager.interfaces.DMSLookupClientWebService;
import it.eurotn.panjea.dms.manager.interfaces.DMSSecurityManager;

@Stateless(mappedName = "Panjea.DMSAllegatoManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DMSAllegatoManagerBean")
public class DMSAllegatoManagerBean implements DMSAllegatoManager {

    private static final Logger LOGGER = Logger.getLogger(DMSAllegatoManagerBean.class);

    @EJB
    protected DMSSecurityManager securityManager;

    @EJB
    protected DMSLookupClientWebService lookupService;

    @Override
    public void addAllegato(String uuid, WSDocument documento) throws DMSLoginException {
        LOGGER.debug("--> Enter addDocument");
        String sid = securityManager.login();
        try {
            lookupService.creaPanjeaService().addDocument(sid, uuid, documento);
        } catch (Exception e) {
            LOGGER.error("-->errore nel pubblicare il documento " + documento.getFileName(), e);
            throw new DmsException(e);
        } finally {
            securityManager.logout(sid);
        }
        LOGGER.debug("--> Exit addDocument");
    }

    @Override
    public void addAllegato(String uuid, WSDocument documento, AllegatoDMS allegatoDms) throws DMSLoginException {
        LOGGER.debug("--> Enter addAllegato");
        String sid = securityManager.login();
        try {
            documento.setSourceDate(allegatoDms.getData());
            lookupService.creaPanjeaService().addDocumentConTemplate(sid, uuid, documento, allegatoDms.getTemplate(),
                    allegatoDms.getNamesReale(), allegatoDms.getValues());
        } catch (Exception e) {
            LOGGER.error("-->errore nel pubblicare il documento " + documento.getFileName(), e);
            throw new DmsException(e);
        } finally {
            securityManager.logout(sid);
        }
        LOGGER.debug("--> Exit addAllegato");
    }

    /**
     *
     * @param sid
     *            authenticazione
     * @param folderService
     *            service
     * @param idFolder
     *            idcartella per la quale costruire il path
     * @return path della cartella
     */
    private String creaPathPerFolder(String sid, FolderClient folderService, long idFolder) {
        WSFolder[] wsFolder = new WSFolder[0];
        try {
            wsFolder = folderService.getPath(sid, idFolder);
        } catch (Exception e) {
            LOGGER.error("-->errore nel recuperare il path per la fodler con id " + idFolder, e);
            throw new DmsException(e);
        }
        String path = "";
        if (wsFolder.length > 0) {
            for (WSFolder folder : wsFolder) {
                path = path.concat(folder.getName().equals(File.separator) ? folder.getName()
                        : File.separator.concat(folder.getName()));
            }
        }
        return path;
    }

    @Override
    public void deleteAllegato(long idDocumento) throws DMSLoginException {
        LOGGER.debug("--> Enter deleteDocument");
        String sid = securityManager.login();
        try {
            lookupService.creaDocumentService().delete(sid, idDocumento);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione del documento", e);
            throw new DmsException(e);
        } finally {
            securityManager.logout(sid);
        }
    }

    @Override
    public List<WSDocument> getAllegati(AllegatoDMS allegatoDms) throws DMSLoginException {
        String sid = securityManager.login();
        SearchClient searchClient = lookupService.creaSearchService();
        FolderClient folderService = lookupService.creaFolderService();

        WSSearchOptions option = allegatoDms.createSearchOption();
        WSSearchResult resultSearch = null;
        try {
            resultSearch = searchClient.find(sid, option);
        } catch (Exception e) {
            LOGGER.error("-->errore nel cercare i documenti", e);
            throw new DmsException(e);
        }

        List<WSDocument> documenti = new ArrayList<>();
        WSDocument[] documentiArray = resultSearch.getHits();
        if (documentiArray != null) {
            documenti = Arrays.asList(documentiArray);
        }

        // i path non sono presenti quindi me li carico usando una mappa come cache per non fare
        // tante chiamate quanti
        // sono i documenti trovati ( gli allegati dell'articolo se non cambio path di pubblicazione
        // nelle settings
        // saranno sempre in una directory)
        Map<Long, String> paths = new HashMap<Long, String>();
        for (WSDocument wsDocument : documenti) {
            String path = paths.get(wsDocument.getFolderId());
            if (path == null) {
                path = creaPathPerFolder(sid, folderService, wsDocument.getFolderId());
                paths.put(wsDocument.getFolderId(), path);
            }
            wsDocument.setPath(path);
        }
        securityManager.logout(sid);
        return documenti;
    }

    @Override
    public byte[] getAllegatoTile(long idDocumento) throws DMSLoginException {
        LOGGER.debug("--> Enter getDocumentTile con id " + idDocumento);
        String sid = securityManager.login();
        byte[] result = null;
        try {
            result = lookupService.creaPanjeaService().getDocumentTile(sid, idDocumento);
        } catch (Exception e) {
            LOGGER.error("-->errore nel cercare il tile per l'articolo", e);
            throw new DmsException(e);
        } finally {
            securityManager.logout(sid);
        }
        LOGGER.debug("--> Exit getDocumentTile");
        return result;
    }

    @Override
    public void updateDocuments(List<WSDocument> documentiDaAggiornare, AllegatoDMS allegatoDms, String folder,
            Date data) throws DMSLoginException {
        String sid = securityManager.login();
        try {
            String dataString = DateFormatUtils.format(data, "yyyy-MM-dd 12:00:00 Z");
            lookupService.creaPanjeaService().updateDocuments(sid,
                    documentiDaAggiornare.toArray(new WSDocument[documentiDaAggiornare.size()]),
                    allegatoDms.getTemplate(), allegatoDms.getNamesReale(), allegatoDms.getValues(), folder,
                    dataString);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione del documento", e);
            throw new DmsException(e);
        } finally {
            securityManager.logout(sid);
        }
    }

}
