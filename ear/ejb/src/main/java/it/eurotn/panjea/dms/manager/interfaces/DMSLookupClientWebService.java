package it.eurotn.panjea.dms.manager.interfaces;

import javax.ejb.Local;

import com.logicaldoc.panjea.webservice.PanjeaClient;
import com.logicaldoc.webservice.auth.AuthClient;
import com.logicaldoc.webservice.document.DocumentClient;
import com.logicaldoc.webservice.folder.FolderClient;
import com.logicaldoc.webservice.search.SearchClient;
import com.logicaldoc.webservice.security.SecurityClient;

@Local
public interface DMSLookupClientWebService {

    /**
     *
     * @return client ricerca per l'autenticazione
     */
    AuthClient creaAuthClient();

    /**
     *
     * @return client webservice per i documenti
     */
    DocumentClient creaDocumentService();

    /**
     *
     * @return client webservice per i folder
     */
    FolderClient creaFolderService();

    /**
     *
     * @return client webservice per panjea
     */
    PanjeaClient creaPanjeaService();

    /**
     *
     * @return client ricerca per i documenti
     */
    SearchClient creaSearchService();

    /**
     *
     * @return client ricerca per la sicurezza
     */
    SecurityClient creaSecurityClient();

}
