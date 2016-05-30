package it.eurotn.rich.report;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

import it.eurotn.panjea.report.EmptyReportException;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.pm.IAziendaCorrente;
import it.eurotn.rich.report.exception.JecMaxPagesGovernatorException;
import it.eurotn.rich.report.exception.JecReportException;
import it.eurotn.rich.report.exception.ReportNonTrovatoException;
import it.eurotn.rich.report.jasperserver.JServer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.fill.JRTemplatePrintImage;
import net.sf.jasperreports.renderers.BatikRenderer;

public class ReportManager {

    private class ResourceDescriptorComparator implements Comparator<ResourceDescriptor> {

        @Override
        public int compare(ResourceDescriptor o1, ResourceDescriptor o2) {
            // rimuovo il path iniziale che si riferisce a PRIVATE o CUSTOM per verificare se le
            // risolrse sono le stesse
            String uriO1 = o1.getUriString();
            String uriO2 = o2.getUriString();

            uriO1 = StringUtils.replace(uriO1, "/" + getPathCodiceAzienda() + PRIVATE_PATH, "");
            uriO2 = StringUtils.replace(uriO2, "/" + getPathCodiceAzienda() + PRIVATE_PATH, "");
            uriO1 = StringUtils.replace(uriO1, "/" + getPathCodiceAzienda() + CUSTOM_PATH, "");
            uriO2 = StringUtils.replace(uriO2, "/" + getPathCodiceAzienda() + CUSTOM_PATH, "");
            uriO1 = StringUtils.replace(uriO1, DEFAULT_PATH, "");
            uriO2 = StringUtils.replace(uriO2, DEFAULT_PATH, "");

            return uriO1.compareTo(uriO2);
        }

    }

    public enum ResourceType {
        PRIVATA(PRIVATE_PATH), PERSONALIZZATA(CUSTOM_PATH), STANDARD(DEFAULT_PATH);

        private String path;

        /**
         * Costruttore.
         *
         * @param path
         *            path
         */
        private ResourceType(final String path) {
            this.path = path;
        }

        /**
         * @return the path
         */
        public String getPath() {
            return path;
        }
    }

    private static Logger logger = Logger.getLogger(ReportManager.class);

    public static final String MAX_PAGES_GOVERNOR_EXCEPTION = "net.sf.jasperreports.governors.MaxPagesGovernorException";
    public static final String BEAN_ID = "reportManager";
    private static final String DEFAULT_PATH = "/Standard/";
    private static final String CUSTOM_PATH = "/Personalizzate/";
    public static final String PRIVATE_PATH = "/Private/";

    private static final String BASE_HOME_URL = "/jasperserver/home.html";

    // login parameters
    private static final String PARAMETER_USERNAME = "j_username";
    private static final String PARAM_PASSWORD = "j_password";
    private static final String PARAM_OUTPUT_FORMAT = "RUN_OUTPUT_FORMAT";

    private static final String SERVICE_LOGIN = "/login";
    private static final String SERVICE_REPORT = "/report";

    private static final String SERVICE_RESOURCE = "/resource";

    private static final String RESPONSE_BODY_PROPERTY_UUID = "uuid";

    private static final String RESPONSE_BODY_PROPERTY_TOTAL_PAGE = "totalPages";

    // SERVER PARAMETERS
    private static final String SCHEME = "http";

    private static final int PORT = 8080;

    public static final String[] EXPORT_TYPE = new String[] { "pdf", "xls", "rtf", "csv", "odt", "txt", "docx", "ods",
            "xlsx" };

    private JServer server;

    private String aziendaStampa = ""; // se in debug indica quale repository

    // utilizzare per la stampa
    private IAziendaCorrente aziendaCorrente;

    protected HttpRequestBase httpReq;

    private CloseableHttpClient httpClient;

    private BasicCookieStore cookieStore;

    private Map<String, Set<String>> reportsNameCache;

    {
        reportsNameCache = new HashMap<String, Set<String>>();
    }

    private int statusCode;

    /***
     *
     * Costruttore.
     *
     */
    public ReportManager() {
        cookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    /**
     * Adds node like <parameter name ="exampleParm1" >value< /parameter>.
     *
     * @param doc
     *            document
     * @param name
     *            nome nodo
     * @param value
     *            valore CDATA
     **/
    private void addParameter(Document doc, String name, String value) {
        Element element = doc.getDocumentElement();
        Element node = doc.createElement("parameter");
        node.setAttribute("name", name);
        CDATASection cdata = doc.createCDATASection(value);
        node.appendChild(cdata);
        element.appendChild(node);
    }

    /**
     *
     * @return utente loggato.
     */
    private Utente caricaUtenteCorrente() {
        ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
        return utente;
    }

    /**
     * Svuota la cache dei reports.
     */
    public void clearReportsNameCache() {
        reportsNameCache.clear();
    }

    /**
     * Crea una cartella nella posizione specificata.
     *
     * @param folderName
     *            nome delle nuova cartella
     * @param parentFolderPath
     *            parent folder
     */
    public void createFolder(String folderName, String parentFolderPath) {
        ResourceDescriptor resourceDescriptor = new ResourceDescriptor();
        resourceDescriptor.setWsType(ResourceDescriptor.TYPE_FOLDER);
        resourceDescriptor.setParentFolder("/" + getPathCodiceAzienda() + parentFolderPath);
        resourceDescriptor.setUriString("/" + getPathCodiceAzienda() + parentFolderPath + "/" + folderName);
        resourceDescriptor.setName(folderName);
        resourceDescriptor.setDescription(folderName);
        resourceDescriptor.setLabel(folderName);
        resourceDescriptor.setResourceType(ResourceDescriptor.TYPE_FOLDER);
        resourceDescriptor.setHasData(false);
        resourceDescriptor.setIsNew(true);
        try {
            server.getWSClient().addOrModifyResource(resourceDescriptor, null);
        } catch (Exception e) {
            logger.error("--> errore nel creare la resource " + resourceDescriptor.getUriString(), e);
            throw new RuntimeException();
        }
    }

    /**
     * Restituisce l'url del path richiesto.
     *
     * @param path
     *            path
     * @return url
     * @throws Exception
     *             .
     */
    private String createURL(String path) throws Exception {
        String url;
        url = new URL(SCHEME, getServerAddress(), PORT, "/jasperserver/rest" + path).toString();
        return url;
    }

    /**
     * Cancella una immagine.
     *
     * @param pathImage
     *            percorso dell'immagine
     */
    public void deleteImage(String pathImage) {
        deleteResouce(pathImage, ResourceDescriptor.TYPE_IMAGE);
    }

    /**
     * Cancella una resource.
     *
     * @param resourcePath
     *            percorso della risorsa
     * @param resourceType
     *            tipo della risorsa
     */
    private void deleteResouce(String resourcePath, String resourceType) {
        ResourceDescriptor resourceDescriptor = new ResourceDescriptor();
        resourceDescriptor.setWsType(resourceType);
        resourceDescriptor.setUriString(resourcePath);
        try {
            server.getWSClient().delete(resourceDescriptor);
        } catch (Exception e) {
            logger.error("--> errore nel cancellare la resource " + resourcePath, e);
            throw new RuntimeException();
        }
    }

    /**
     * Converte il document xml in string.
     *
     * @param xml
     *            document da convertire
     * @return valore in stringa
     * @throws Exception
     *             .
     */
    private String domToString(Document xml) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(xml), new StreamResult(writer));
        return writer.getBuffer().toString();
    }

    /**
     * Esegue l'export del report nel path indicato.
     *
     * @param params
     *            parametri del report
     * @param reportPath
     *            report da esportare
     * @param savePath
     *            path di salvataggio
     * @param outPutFormat
     *            formato di esportazione
     * @throws EmptyReportException
     *             rilanciata se il report generato non contiene pagine
     */
    public void exportReport(Map<String, String> params, String reportPath, String savePath, String outPutFormat)
            throws EmptyReportException {
        try {
            loginToJasper();

            String reportUuid = runReport(reportPath, params, outPutFormat);

            // Getting the report body
            try (InputStream is = getReportContent(reportUuid); OutputStream os = new FileOutputStream(savePath);) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                is.close();
                os.close();
            }
        } catch (Exception e) {
            logger.error("--> Errore durante l'esportazione del report  " + reportPath, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica se la cartella esiste.
     *
     * @param parentFolderPath
     *            percordo della cartella
     * @param folderName
     *            nome della cartella da ricercare
     * @param resourceType
     *            tipo risorsa
     * @return <code>true</code> se esiste, <code>false</code> altrimenti
     */
    public boolean folderExist(String parentFolderPath, String folderName, ResourceType resourceType) {
        Set<ResourceDescriptor> risorse = listResources(parentFolderPath, ResourceDescriptor.TYPE_FOLDER);

        ResourceDescriptor resourceDescriptorResult = null;

        if (!risorse.isEmpty()) {
            for (ResourceDescriptor resourceDescriptor : risorse) {
                if (StringUtils.contains(resourceDescriptor.getUriString(), resourceType.getPath())
                        && resourceDescriptor.getName().equals(folderName)) {
                    resourceDescriptorResult = resourceDescriptor;
                    break;
                }
            }
        }
        return resourceDescriptorResult != null;
    }

    /**
     *
     * @return azienda impostata per le stampe (se settata)
     */
    public String getAziendaStampa() {
        return aziendaStampa;
    }

    /**
     * @return Codice dell'azienda loggata
     */
    public String getCodiceAzienda() {
        return aziendaCorrente.getCodice();
    }

    /**
     * @param reportPath
     *            path "generico" del report.
     * @return reportPath con il path completo per i report customizzati
     */
    private String getCustomPath(String reportPath) {
        return "/" + getPathCodiceAzienda() + CUSTOM_PATH + reportPath;
    }

    /**
     * @param reportPath
     *            path "generico" del report.
     * @return reportPath con il path completo per i report di default
     */
    public String getDefaultPath(String reportPath) {
        return DEFAULT_PATH + reportPath;
    }

    /**
     * Recupera tutte le immagini contenute nella cartella specificata.
     *
     * @param parentFolderPath
     *            cartella di riferimento
     * @return lista di immagini caricate
     */
    public Set<ImageIcon> getImages(String parentFolderPath) {
        Set<ResourceDescriptor> risorse = listResources(parentFolderPath, ResourceDescriptor.TYPE_IMAGE);

        Set<ImageIcon> immagini = new HashSet<ImageIcon>();

        if (!risorse.isEmpty()) {
            for (ResourceDescriptor imageDescriptor : risorse) {

                File imageFile = null;
                try {
                    imageFile = File.createTempFile("tmpFileReport", "");
                } catch (IOException e1) {
                    logger.error("--> Errore durante la creazione del file temporameo", e1);
                }
                imageFile.deleteOnExit();

                try {
                    server.getWSClient().get(imageDescriptor, imageFile);
                } catch (Exception e) {
                    logger.error("--> Errore durante il caricamento dell'immagine " + imageDescriptor.getUriString(),
                            e);
                }

                Image image = Toolkit.getDefaultToolkit().getImage(imageFile.getAbsolutePath());
                ImageIcon imageIcon = new ImageIcon(image);
                imageIcon.setDescription(imageDescriptor.getName());
                immagini.add(imageIcon);

            }
        }

        return immagini;
    }

    /**
     * Restituisce il path del codice azienda.
     *
     * @return path dell'azienda
     */
    public String getPathCodiceAzienda() {
        if (!aziendaStampa.isEmpty()) {
            return aziendaStampa;
        } else {
            return getCodiceAzienda().toLowerCase();
        }
    }

    /**
     *
     * @param reportPath
     *            path "generico" del report.
     * @return reportPath con il path completo per i report privati del cliente
     */
    private String getPrivatePath(String reportPath) {
        String privPath = "/" + getPathCodiceAzienda() + PRIVATE_PATH + reportPath;
        // se non ho report path tolgo l'ultimo "/"
        if (StringUtils.isBlank(reportPath)) {
            privPath = StringUtils.left(privPath, privPath.length() - 1);
        }
        return privPath;
    }

    /**
     * Restituisce lo stream del report con uuid indicato.
     *
     * @param reportUuid
     *            uuid
     * @return straem
     * @throws Exception
     *             .
     */
    private InputStream getReportContent(String reportUuid) throws Exception {

        String reportFileURL = createURL(SERVICE_REPORT) + "/" + reportUuid + "?file=report";
        HttpGet method = new HttpGet(reportFileURL);

        // Send GET request for descriptor
        CloseableHttpResponse response = httpClient.execute(method);
        try {
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Downlaod failed: " + response.getStatusLine());
            }
            // Getting the report body
            HttpEntity entity = response.getEntity();
            return entity.getContent();
        } catch (Exception e) {
            logger.error("-->errore nel recuperare il contenuto del report", e);
        } finally {
            response.close();
        }
        return null;
    }

    /**
     * @param reportPath
     *            path completo del report. Deve essere passato anche il prefisso per i vari "casi"
     *            (standard,privato,personalizzato)
     * @return ResourceDescriptor per il report. Il ResourceDescriptor
     */
    @SuppressWarnings("unchecked")
    private ResourceDescriptor getResourceDescriptor(String reportPath) {
        String[] token = reportPath.split("/");
        String reportName = token[token.length - 1];

        reportPath = reportPath.replace("/" + reportName, "");

        if (logger.isDebugEnabled()) {
            logger.debug("--> reportPath:" + reportPath);
        }
        // Carico le risorse dalla cartella e vedo se contiene la reportUnit che
        // cerco
        ResourceDescriptor rd = new ResourceDescriptor();
        rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
        rd.setUriString(reportPath);
        List<ResourceDescriptor> customResources;
        try {
            customResources = server.getWSClient().list(rd);
        } catch (Exception e) {
            logger.error(
                    "-->errore nel caricare la lista dei report da jasperServer. Resource descriprot della cartella "
                            + rd,
                    e);
            throw new RuntimeException(e);
        }
        for (ResourceDescriptor resourceDescriptor : customResources) {
            if (ResourceDescriptor.TYPE_REPORTUNIT.equals(resourceDescriptor.getWsType())) {
                if (reportName.equals(resourceDescriptor.getName())
                        || reportName.equals(resourceDescriptor.getLabel())) {
                    return resourceDescriptor;
                }
            }
        }
        return null;
    }

    /**
     *
     * @return JServer
     */
    public JServer getServer() {
        return server;
    }

    /**
     *
     * @return indirizzo del server di stampa. L'indirizzo è l'url compoleto
     *         (es:http://localhost:8080/jasperServer,
     */
    public String getServerAddress() {
        String indirizzo = caricaUtenteCorrente().getDatiJasperServer().getUrl();
        if (indirizzo == null) {
            indirizzo = getServerAddressDefault();
        }
        return indirizzo;
    }

    /**
     *
     * @return indirizzo del server di jasperserver calcolato in base all'indirizzo di webstart
     *         dell'applicazione. Se non disponibile uso localhost.
     */
    private String getServerAddressDefault() {
        return PanjeaSwingUtil.getServerUrl();
    }

    /**
     *
     * @return url
     */
    public String getServerBaseAddress() {
        URI uri;
        try {
            uri = URIUtils.createURI(SCHEME, getServerAddress(), PORT, "/jasperserver/", null, null);
        } catch (Exception e) {
            logger.error("-->errore nel creare l'url per la home", e);
            throw new RuntimeException(e);
        }
        return uri.toString();
    }

    /**
     *
     * @return url della pagina home con i dati di accesso.
     */
    public String getServerHomeAddress() {
        URI uri;
        try {
            uri = URIUtils.createURI(SCHEME, getServerAddress(), PORT, BASE_HOME_URL, null, null);
        } catch (Exception e) {
            logger.error("-->errore nel creare l'url per la home", e);
            throw new RuntimeException(e);
        }
        return uri.toString();
    }

    public String getTemplateJrxml(String reportPath, String templateName) throws Exception {

        ResourceDescriptor rd = new ResourceDescriptor();
        rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
        rd.setUriString(reportPath);
        List<ResourceDescriptor> templates = server.getWSClient().list(rd);
        for (ResourceDescriptor resourceDescriptor : templates) {
            if (resourceDescriptor.getWsType().equals(ResourceDescriptor.TYPE_JRXML)
                    && resourceDescriptor.getName().equals(templateName)) {
                try {
                    File templateFile = File.createTempFile("tmpFileReport", "");
                    server.getWSClient().get(resourceDescriptor, templateFile);
                    String templateContent = new String(Files.readAllBytes(templateFile.toPath()));
                    templateFile.delete();
                    return templateContent;
                } catch (IOException e) {
                    logger.error("--> Errore durante la creazione del file temporameo", e);
                    throw new RuntimeException(e);
                }
            }
        }
        return "";

    }

    /**
     * Verifica se esiste l'immagine nella cartella specificata.
     *
     * @param folderPath
     *            parent folder
     * @param imageName
     *            nome dell'immagine
     * @param resourceType
     *            tipo della risorsa
     * @return <code>true</code> se esiste, <code>false</code> altrimenti
     */
    public boolean imageExist(String folderPath, String imageName, ResourceType resourceType) {

        boolean existImg = false;

        String resource = resourceType.getPath();

        Set<ResourceDescriptor> risorse = listResources(folderPath, ResourceDescriptor.TYPE_IMAGE);
        for (ResourceDescriptor resourceDescriptor : risorse) {
            if (StringUtils.contains(resourceDescriptor.getUriString(), resource)
                    && resourceDescriptor.getName().equals(imageName)) {
                existImg = true;
                break;
            }
        }

        return existImg;
    }

    /**
     * Restituisce una lista contenente tutti gli uri delle immagini presenti.
     *
     * @param folderPath
     *            paren folder
     * @return lista caricata
     */
    public Set<String> listImages(String folderPath) {

        Set<ResourceDescriptor> risorse = listResources(folderPath, ResourceDescriptor.TYPE_IMAGE);
        HashSet<String> imageName = new HashSet<String>();

        for (ResourceDescriptor resourceDescriptor : risorse) {
            imageName.add(resourceDescriptor.getUriString());
        }

        return imageName;

    }

    /**
     * Restituisce tutti i {@link ResourceDescriptor} di tipo reportUnit<br/>
     * in un determinato path.<br/>
     * Aggiunge tutti i report sia customizzati che di default.<br/>
     *
     * @param folderPath
     *            parent folder
     * @return lista caricata
     */
    public Set<String> listReport(String folderPath) {

        Set<String> tmp = new HashSet<String>();

        if (reportsNameCache.containsKey(folderPath)) {
            tmp = reportsNameCache.get(folderPath);
        } else {
            Set<ResourceDescriptor> risorse = listResources(folderPath, ResourceDescriptor.TYPE_REPORTUNIT);
            for (ResourceDescriptor resourceDescriptor : risorse) {
                tmp.add(resourceDescriptor.getLabel());
            }
            reportsNameCache.put(folderPath, tmp);
        }

        // restituisco un copia del set altrimenti se viene modificato ne risulterà modificato anche
        // quello nemma cache
        Set<String> reportName = new HashSet<String>();
        for (String string : tmp) {
            reportName.add(string);
        }

        return reportName;
    }

    /**
     * Carica tutte le risorse del tipo specificato delle cartella.
     *
     * @param folderPath
     *            parent folder
     * @param resourceType
     *            tipo delle risorse caricate
     * @return lista delle risorse caricate
     */
    @SuppressWarnings("unchecked")
    private Set<ResourceDescriptor> listResources(String folderPath, String resourceType) {

        ResourceDescriptor rd = new ResourceDescriptor();
        rd.setWsType(ResourceDescriptor.TYPE_FOLDER);

        Set<ResourceDescriptor> resources = new TreeSet<ResourceDescriptor>(new ResourceDescriptorComparator());

        rd.setUriString(getPrivatePath(folderPath));
        List<ResourceDescriptor> privateResources = new ArrayList<ResourceDescriptor>();
        try {
            privateResources = server.getWSClient().list(rd);
        } catch (Exception e) {
            logger.error("--> errore nel recuperare le resource per il path " + getPrivatePath(folderPath), e);
        }
        for (ResourceDescriptor resourceDescriptor : privateResources) {
            if (resourceDescriptor.getWsType().equals(resourceType)) {
                resources.add(resourceDescriptor);
            }
        }

        rd.setUriString(getCustomPath(folderPath));
        List<ResourceDescriptor> customResources = new ArrayList<ResourceDescriptor>();
        try {
            customResources = server.getWSClient().list(rd);
        } catch (Exception e) {
            logger.error("--> errore nel recuperare le resources per il path " + getCustomPath(folderPath), e);
        }
        for (ResourceDescriptor resourceDescriptor : customResources) {
            if (resourceDescriptor.getWsType().equals(resourceType)) {
                resources.add(resourceDescriptor);
            }
        }

        rd.setUriString(getDefaultPath(folderPath));
        List<ResourceDescriptor> defaultResources = new ArrayList<ResourceDescriptor>();
        try {
            defaultResources = server.getWSClient().list(rd);
        } catch (Exception e) {
            logger.error("--> errore nel recuperare le resource per il path " + getDefaultPath(folderPath), e);
        }
        for (ResourceDescriptor resourceDescriptor : defaultResources) {
            if (resourceDescriptor.getWsType().equals(resourceType)) {
                resources.add(resourceDescriptor);
            }
        }

        return resources;
    }

    /**
     *
     * @return lista delle stampanti installate nel sistema. Ordinate per nome, esclusa la stampante
     *         predefinita che viene inserita come primo elemento
     */
    public List<String> listStampanti() {
        List<String> stampanti = new ArrayList<String>();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService serviceDefault = PrintServiceLookup.lookupDefaultPrintService();

        for (PrintService printer : printServices) {
            if (serviceDefault != printer) {
                stampanti.add(printer.getName());
            }
        }
        Collections.sort(stampanti);
        stampanti.add(0, serviceDefault.getName());
        return stampanti;
    }

    /**
     * Esegue il login su jasper.
     *
     * @throws Exception
     *             .
     */
    private void loginToJasper() throws Exception {
        String loginURL = createURL(SERVICE_LOGIN);
        HttpEntityEnclosingRequestBase httpost = new HttpPost(loginURL);

        // Setting Login URL in a POST method

        // Set authentication parameters
        Utente utente = caricaUtenteCorrente();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair(PARAMETER_USERNAME, utente.getDatiJasperServer().getUsername()));
        nvps.add(new BasicNameValuePair(PARAM_PASSWORD, utente.getDatiJasperServer().getPassword()));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        CloseableHttpResponse response2 = httpClient.execute(httpost);

        // Check correct login process
        if (response2.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            System.out.println("Login failed: " + response2.getStatusLine());
            response2.close();
            return;
        }
    }

    public ResourceDescriptor putReportUnit(String parentPath, String name, String descrizione, String mainJrxml)
            throws Exception {

        deleteResouce(parentPath + "/" + name, ResourceDescriptor.TYPE_REPORTUNIT);
        File resourceFile = null;
        ResourceDescriptor rd = new ResourceDescriptor();
        rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
        rd.setName(name);
        rd.setLabel(name);
        rd.setDescription(descrizione);
        rd.setParentFolder(parentPath);
        rd.setUriString(parentPath + "/" + name);
        rd.setIsNew(true);
        rd.setHasData(false);
        rd.setResourceProperty(ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS, true);

        ResourceDescriptor tmpDataSourceDescriptor = new ResourceDescriptor();
        tmpDataSourceDescriptor.setWsType(ResourceDescriptor.TYPE_DATASOURCE);
        tmpDataSourceDescriptor.setReferenceUri("/Generale/DataSource/panjeajndi");
        tmpDataSourceDescriptor.setIsReference(true);
        rd.getChildren().add(tmpDataSourceDescriptor);

        ResourceDescriptor jrxmlDescriptor = new ResourceDescriptor();
        jrxmlDescriptor.setWsType(ResourceDescriptor.TYPE_JRXML);
        jrxmlDescriptor.setName("test_jrxml");
        jrxmlDescriptor.setLabel("Main jrxml");
        jrxmlDescriptor.setDescription("Main jrxml");
        jrxmlDescriptor.setIsNew(true);
        jrxmlDescriptor.setHasData(true);
        jrxmlDescriptor.setMainReport(true);
        rd.getChildren().add(jrxmlDescriptor);

        resourceFile = File.createTempFile("panjeaAnalisi", ".jrxml");
        try (FileWriter fileWriter = new FileWriter(resourceFile)) {
            fileWriter.write(mainJrxml);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        ResourceDescriptor result = server.getWSClient().addOrModifyResource(rd, resourceFile);

        ResourceDescriptor newRd = new ResourceDescriptor();
        newRd.setWsType(ResourceDescriptor.TYPE_INPUT_CONTROL);
        newRd.setIsReference(true);
        newRd.setReferenceUri("/Standard/InputControl/Anagrafica/azienda");
        newRd.setIsNew(true);
        newRd.setUriString(rd.getUriString() + "/<controls>");
        server.getWSClient().modifyReportUnitResource(result.getUriString(), newRd, null);

        newRd = new ResourceDescriptor();
        newRd.setWsType(ResourceDescriptor.TYPE_INPUT_CONTROL);
        newRd.setIsReference(true);
        newRd.setReferenceUri("/Standard/InputControl/Analisi/Anni");
        newRd.setIsNew(true);
        newRd.setUriString(rd.getUriString() + "/<controls>");

        server.getWSClient().modifyReportUnitResource(result.getUriString(), newRd, null);
        resourceFile.delete();
        return result;
    }

    /**
     * Verifica se esiste il report specificato nella directory richiesta.
     *
     * @param folderPath
     *            directory di riferimento
     * @param reportName
     *            nome del repost da cercare
     * @return <code>true</code> se il report esiste
     */
    public boolean reportExist(String folderPath, String reportName) {

        Set<String> reports = listReport(folderPath);

        return reports != null && reports.contains(reportName);
    }

    /**
     * Restitusce il resurce descriptor della risorsa specificata.
     *
     * @param reportPath
     *            risorsa
     * @return resource descriptor
     * @throws Exception
     *             .
     */
    private Document retrieveResourceDescriptor(String reportPath) throws Exception {

        String resourceURL = createURL(SERVICE_RESOURCE) + reportPath;
        HttpGet method = new HttpGet(resourceURL);

        // Send GET request for descriptor
        Document descriptorXML = null;
        try (CloseableHttpResponse response = httpClient.execute(method)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Downlaod failed: " + response.getStatusLine());
            }
            // Getting the report body
            HttpEntity entity = response.getEntity();
            String documentoString = IOUtils.toString(entity.getContent());
            EntityUtils.consume(entity);
            // Transform descriptor from String into XML Document
            descriptorXML = stringToDom(documentoString);
        } catch (Exception e) {
            logger.error("-->errore nel recuperare il contenuto del report", e);
            System.err.println(e);
        }
        return descriptorXML;
    }

    /**
     * Crea un oggetto jasperReport. Cerca prima il report nella cartella personalizzata e poi nella
     * default
     *
     * @param reportPath
     *            path del report compreso il nome. la "/" della root viene aggiunga nel metodo
     * @param parameters
     *            parametri per il report.
     * @return jasperPrint con il report eseguito.
     * @throws JecReportException
     *             rilanciata se la generazione del report non è andata a buon fine
     */
    @SuppressWarnings({ "rawtypes", "deprecation" })
    public JasperPrint runReport(String reportPath, java.util.Map parameters) throws JecReportException {
        if (logger.isDebugEnabled()) {
            logger.debug("--> Enter runReport. reportPath " + reportPath);
        }
        // Verifico se il report esiste nella cartella personalizzata.
        // Recupero il nome del file
        try {
            ResourceDescriptor rd = getResourceDescriptor(getPrivatePath(reportPath));
            if (rd == null) {
                rd = getResourceDescriptor(getCustomPath(reportPath));
            }
            if (rd == null) {
                rd = getResourceDescriptor(getDefaultPath(reportPath));
            }
            if (rd == null) {
                throw new ReportNonTrovatoException(reportPath);
            }

            JasperPrint report = server.getWSClient().runReport(rd, parameters);
            report.getPropertiesMap().setProperty("reportPath", reportPath);
            // Per qualche motivo dal passaggio dal Server al client certe
            // configurazioni (es Baumenner)per
            // l'xml per batik
            // toglie la stringa xmlnd (probabilmente l'antivitus.) Sono
            // costretto a rimetterla se non
            // esiste.
            if (reportPath.contains("/Magazzino/Etichette")) {
                for (Object jtTemplate : ((JRBasePrintPage) report.getPages().get(0)).getElements()) {
                    if (jtTemplate instanceof JRTemplatePrintImage) {
                        if (((JRTemplatePrintImage) jtTemplate).getRenderer() instanceof BatikRenderer) {
                            BatikRenderer renderer = (BatikRenderer) ((JRTemplatePrintImage) jtTemplate).getRenderer();
                            Field currentBeanField = BatikRenderer.class.getDeclaredField("svgText");
                            currentBeanField.setAccessible(true);
                            String svgText = (String) (currentBeanField.get(renderer));
                            if (!svgText.contains("xmlns")) {
                                svgText = svgText.replace("<svg", "<svg xmlns=\"http://www.w3.org/2000/svg\"");
                                currentBeanField.set(renderer, svgText);
                            }
                        }
                    }
                }
            }
            return report;
        } catch (ReportNonTrovatoException rnfe) {
            // logger.error("-->errore nell'eseguire il report", rnfe);
            // showException("Il layout del report non è stato trovato sul server. Percorso " +
            // reportPath);
            throw rnfe;
        } catch (Exception e) {
            if (e.getMessage().indexOf(ReportManager.MAX_PAGES_GOVERNOR_EXCEPTION) != -1) {
                throw new JecMaxPagesGovernatorException(reportPath);
            } else {
                // showException(message);
                logger.error("-->errore nell'eseguire il report", e);
                throw new JecReportException(reportPath);
            }
        }
    }

    /**
     * Esegue il report spedificato restituendo l'uuid.
     *
     * @param reportPath
     *            path del report da eseguire
     * @param params
     *            parametri del report
     * @param outPutFormat
     *            formato di generazione del report
     * @return uuid del report generato
     * @throws Exception
     *             .
     */
    private String runReport(String reportPath, Map<String, String> params, String outPutFormat) throws Exception {
        Document descriptorXML = retrieveResourceDescriptor(reportPath);

        // Use this method if you need to send parameters to the report
        // These are added to the XML Document
        for (Entry<String, String> entry : params.entrySet()) {
            // if (!entry.getKey().toLowerCase().equals("data")) {
            addParameter(descriptorXML, entry.getKey(), entry.getValue());
            // }
        }

        // Settting PUT method to run report, the url contains the RUN_OUTPUT_FORMAT parameter
        StringBuilder reportURL = new StringBuilder();
        reportURL.append(createURL(SERVICE_REPORT));
        reportURL.append(reportPath);
        reportURL.append("?");
        reportURL.append(PARAM_OUTPUT_FORMAT);
        reportURL.append("=");
        reportURL.append(outPutFormat);

        HttpPost httpPut = new HttpPost(reportURL.toString());
        // Setting the request Body. The descriptor XML Document is transform to String and add to
        // the request body.
        httpPut.setEntity(new StringEntity(domToString(descriptorXML), Consts.UTF_8));
        // putMethod.setRequestBody(domToString(descriptorXML));

        // Send PUT request to execute the report.
        // Send GET request for descriptor
        CloseableHttpResponse response = httpClient.execute(httpPut);

        // Check correct descriptor process
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            System.out.println("Descriptor failed: " + response.getStatusLine());
        }
        // Check correct report process
        if (statusCode != 201) {
            System.out.println("Report failed: " + response.getStatusLine());
        }

        HttpEntity resultEntity = response.getEntity();

        // Get the response body
        String reportSource = IOUtils.toString(resultEntity.getContent());
        EntityUtils.consume(resultEntity);

        // Transform report information into XML Document.
        Document reportXML = stringToDom(reportSource);

        // Extrac from XML Document the report's page count
        NodeList nodes = reportXML.getElementsByTagName(RESPONSE_BODY_PROPERTY_TOTAL_PAGE);
        String reportTotalPage = nodes.item(0).getTextContent();

        if ("0".equals(reportTotalPage)) {
            throw new EmptyReportException();
        }

        // Extrac from XML Document the report's UUID
        nodes = reportXML.getElementsByTagName(RESPONSE_BODY_PROPERTY_UUID);
        String reportUuid = nodes.item(0).getTextContent();

        return reportUuid;
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(IAziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * Setta l'azienda di stampa. Utilizzata per poter utilizzare i report di un'azienda diversa
     * dall'azienda corrente.
     *
     * @param aziendaStampa
     *            azienda per la stampa
     */
    public void setAziendaStampa(String aziendaStampa) {
        clearReportsNameCache();
        this.aziendaStampa = aziendaStampa;
    }

    /**
     * Server di jasper.
     *
     * @param server
     *            dati del server di jasper
     */
    public void setServer(JServer server) {
        this.server = server;
    }

    private void showException(String errorMessage) {
        Message message = new DefaultMessage(errorMessage, Severity.ERROR);
        final MessageAlert messageAlert = new MessageAlert(message, 0);
        messageAlert.addCloseCommandVisible();
        messageAlert.showAlert();
    }

    /**
     * Converte la string in Document.
     *
     * @param xmlSource
     *            string
     * @return Document
     * @throws Exception
     *             .
     */
    private Document stringToDom(String xmlSource) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader sr = new StringReader(xmlSource);
        InputSource is = new InputSource(sr);
        return builder.parse(is);
    }

    /**
     * Salva una immagine nel percorso specificato.
     *
     * @param parentResourcePath
     *            persorso dell'immagine
     * @param resourceName
     *            nome immagine
     * @param resourceFile
     *            file immagine
     */
    public void uploadImage(String parentResourcePath, String resourceName, File resourceFile) {

        if (imageExist(parentResourcePath, resourceName, ResourceType.PRIVATA)) {
            deleteImage(getPrivatePath(parentResourcePath) + "/" + resourceName);
        }

        ResourceDescriptor uploadResourceDescriptor = new ResourceDescriptor();
        uploadResourceDescriptor.setResourceType(ResourceDescriptor.TYPE_IMAGE);
        uploadResourceDescriptor.setName(resourceName);
        uploadResourceDescriptor.setLabel(resourceName);
        uploadResourceDescriptor.setDescription(resourceName);
        uploadResourceDescriptor.setParentFolder(getPrivatePath(parentResourcePath));
        uploadResourceDescriptor.setUriString(uploadResourceDescriptor.getParentFolder() + "/" + resourceName);
        uploadResourceDescriptor.setWsType(ResourceDescriptor.TYPE_IMAGE);
        uploadResourceDescriptor.setHasData(true);
        uploadResourceDescriptor.setIsNew(true);

        try {
            server.getWSClient().addOrModifyResource(uploadResourceDescriptor, resourceFile);
        } catch (Exception e) {
            logger.error("--> errore nel salvare l'immagine " + resourceFile.getName(), e);
            throw new RuntimeException();
        }
    }
}