package it.eurotn.panjea.dms.manager;

import java.io.IOException;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.logicaldoc.panjea.webservice.PanjeaClient;
import com.logicaldoc.webservice.auth.AuthClient;
import com.logicaldoc.webservice.document.DocumentClient;
import com.logicaldoc.webservice.folder.FolderClient;
import com.logicaldoc.webservice.search.SearchClient;
import com.logicaldoc.webservice.security.SecurityClient;

import it.eurotn.panjea.dms.exception.DmsException;
import it.eurotn.panjea.dms.manager.interfaces.DMSLookupClientWebService;
import it.eurotn.panjea.dms.manager.interfaces.DMSSettingsManager;

@Stateless(mappedName = "Panjea.DMSLookupClientWebService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DMSLookupClientWebService")
public class DMSLookupClientWebServiceBean implements DMSLookupClientWebService {
    private static final Logger LOGGER = Logger.getLogger(DMSLookupClientWebServiceBean.class);

    @EJB
    private DMSSettingsManager settingsManager;

    @Override
    public AuthClient creaAuthClient() {
        try {
            return new AuthClient(settingsManager.caricaDmsSettings().getServiceUrl() + "/services/Auth");
        } catch (IOException e) {
            LOGGER.error("-->errore nel creare il DocumentClient", e);
            throw new DmsException("-->errore nel creare il DocumentClient", e);
        }
    }

    @Override
    public DocumentClient creaDocumentService() {
        try {
            return new DocumentClient(settingsManager.caricaDmsSettings().getServiceUrl() + "/services/Document");
        } catch (IOException e) {
            LOGGER.error("-->errore nel creare il DocumentClient", e);
            throw new DmsException("-->errore nel creare il DocumentClient", e);
        }
    }

    @Override
    public FolderClient creaFolderService() {
        try {
            return new FolderClient(settingsManager.caricaDmsSettings().getServiceUrl() + "/services/Folder");
        } catch (IOException e) {
            LOGGER.error("-->errore nel creare il FolderClient", e);
            throw new DmsException("-->errore nel creare il FolderClient", e);
        }
    }

    @Override
    public PanjeaClient creaPanjeaService() {
        try {
            return new PanjeaClient(settingsManager.caricaDmsSettings().getServiceUrl() + "/services/Panjeaservice");
        } catch (IOException e) {
            LOGGER.error("-->errore nel creare il PanjeaClient", e);
            throw new DmsException("-->errore nel creare il PanjeaClient", e);
        }
    }

    @Override
    public SearchClient creaSearchService() {
        try {
            return new SearchClient(settingsManager.caricaDmsSettings().getServiceUrl() + "/services/Search");
        } catch (IOException e) {
            LOGGER.error("-->errore nel creare il SearchClient", e);
            throw new DmsException("-->errore nel creare il SearchClient", e);
        }
    }

    @Override
    public SecurityClient creaSecurityClient() {
        try {
            return new SecurityClient(settingsManager.caricaDmsSettings().getServiceUrl() + "/services/Security");
        } catch (IOException e) {
            LOGGER.error("-->errore nel creare il securityClient", e);
            throw new DmsException("-->errore nel creare il securityClient", e);
        }
    }
}