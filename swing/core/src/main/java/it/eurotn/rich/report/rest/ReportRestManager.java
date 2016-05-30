package it.eurotn.rich.report.rest;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.ws.xml.Marshaller;
import com.jaspersoft.jasperserver.ws.xml.Unmarshaller;

/**
 * @author fattazzo
 *
 */
public class ReportRestManager {

    private static Logger logger = Logger.getLogger(ReportRestManager.class);

    // login parameters
    private static final String PARAM_OUTPUT_FORMAT = "RUN_OUTPUT_FORMAT";

    private static final String LOGIN_URL = "/rest/login";
    private static final String SERVICE_REPORT = "/report";
    private static final String SERVICE_RESOURCE = "/rest/resource/";
    private static final String SERVICE_RESOURCES = "/rest/resources/";

    private static final String RESPONSE_BODY_PROPERTY_UUID = "uuid";
    private ClientConfig config;
    private SimpleHttpClient restClient;
    private String urlRest;

    /**
     *
     * @param config
     *            configurazione del server per il collegamento
     */
    public ReportRestManager(final ClientConfig config) {
        this.config = config;
        urlRest = "http://" + config.getUrl() + ":" + config.getPort() + "/jasperserver";
        restClient = new SimpleHttpClient(urlRest);
    }

    public void deleteResource(String uriResource) {
        restClient.doDeleteRequest(SERVICE_RESOURCE + uriResource);
    }

    // /**
    // * Restituisce lo stream del report con uuid indicato.
    // *
    // * @param reportUuid
    // * uuid
    // * @return straem
    // * @throws Exception .
    // */
    // private InputStream getResourceAsInputStream(String reportUuid) throws Exception {
    //
    // String reportFileURL = createURL(SERVICE_REPORT) + "/" + reportUuid + "?file=report";
    // HttpGet getMethodFile = new HttpGet(reportFileURL);
    //
    // // Send GET request to download the report
    // HttpResponse response = client.execute(getMethodFile);
    //
    // // Check correct report process
    // if (response.getStatusLine().getStatusCode() != 200) {
    // System.out.println("Downlaod failed: " + response.getStatusLine().getStatusCode());
    // }
    //
    // // Getting the report body
    // InputStream is = response.getEntity().getContent();
    // return is;
    // }

    /**
     * Converte il document xml in string.
     *
     * @param xml
     *            document da convertire
     * @return valore in stringa
     */
    public String domToString(Document xml) {
        try {
            StringWriter writer;
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            writer = new StringWriter();
            transformer.transform(new DOMSource(xml), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (Exception e) {
            logger.error("-->errore nel trasformare il documento xml in stringa ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param uriResource
     *            uri della risorsa
     * @return true se esiste la risorsa
     */
    public boolean isResourcePresent(String uriResource) {
        try {
            restClient.doGetRequest(SERVICE_RESOURCE + uriResource);
            return true;
        } catch (RuntimeException ex) {
            if (ex.getCause().getMessage().contains("404 Not Found")) {
                return false;
            }
            logger.error("-->errore nel recuperare il resource descriptor della risorsa " + uriResource, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Esegue il login in jasperserver.
     *
     * @throws HttpException
     * @throws IOException
     * @throws ClientProtocolException
     * @throws Exception
     */
    public void login() {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("j_username", config.getUserName()));
        parameters.add(new BasicNameValuePair("j_password", config.getPassword()));
        restClient.doPostRequest(LOGIN_URL, parameters);
    }

    public void putResource(ResourceDescriptor rd) {
        Map<String, ContentBody> parts = new HashMap<String, ContentBody>();
        StringBody rdPart = new StringBody(new Marshaller().writeResourceDescriptor(rd), ContentType.TEXT_PLAIN);
        parts.put("ResourceDescriptor", rdPart);
        restClient.doPutMultiPartRequest(SERVICE_RESOURCE + rd.getUriString(), parts);
    }

    public void putResource(ResourceDescriptor rd, File file) {
        putResource(rd, file, rd.getUriString());
    }

    public void putResource(ResourceDescriptor rd, File file, String namePartAttachment) {
        Map<String, ContentBody> parts = new HashMap<String, ContentBody>();
        StringBody rdPart = new StringBody(new Marshaller().writeResourceDescriptor(rd), ContentType.TEXT_PLAIN);
        parts.put("ResourceDescriptor", rdPart);
        FileBody bin = new FileBody(file);
        parts.put(namePartAttachment, bin);
        restClient.doPutMultiPartRequest(SERVICE_RESOURCE + rd.getUriString(), parts);
    }

    /**
     *
     * @param pathTemplateFiles
     *            path del template
     * @return contenuto in formato testo della risorsa
     */
    public String retrieveContentResourceAsString(String pathTemplateFiles) {
        StringBuilder sbResourceURL = new StringBuilder(500).append(SERVICE_RESOURCE).append(pathTemplateFiles)
                .append("?fileData=true");
        return restClient.doGetRequest(sbResourceURL.toString());
    }

    /**
     * Restitusce il resurce descriptor della risorsa specificata.
     *
     * @param resourcePath
     *            risorsa
     * @return resource descriptor
     */
    public Document retrieveResource(String resourcePath) {
        Document descriptorXML = null;
        try {
            StringBuilder sbResourceURL = new StringBuilder(500).append(SERVICE_RESOURCE).append(resourcePath);
            String descriptorSource = restClient.doGetRequest(sbResourceURL.toString());
            descriptorXML = stringToDom(descriptorSource);
        } catch (Exception e) {
            logger.error("-->errore new recuperare la risorsa dal path " + resourcePath, e);
            throw new RuntimeException(e);
        }
        return descriptorXML;
    }

    /**
     *
     * @param resourcePath
     *            path della risorsa
     * @param type
     *            tipo di risorse
     * @return lista di resourceDescriptor contenuti nella risorsa
     */
    public List<ResourceDescriptor> retrieveResources(String resourcePath, String type) {
        List<ResourceDescriptor> documents = new ArrayList<ResourceDescriptor>();
        try {
            StringBuilder sbResourceURL = new StringBuilder(500).append(SERVICE_RESOURCES).append(resourcePath)
                    .append("?type=").append(type);
            String descriptorSource = restClient.doGetRequest(sbResourceURL.toString());
            Document descriptorXML = stringToDom(descriptorSource);
            NodeList fields = descriptorXML.getChildNodes().item(0).getChildNodes();
            documents = new ArrayList<ResourceDescriptor>();
            for (int i = 0; i < fields.getLength(); i++) {
                Node node = fields.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    documents.add(Unmarshaller.readResourceDescriptor((Element) node));
                }
            }
        } catch (Exception e) {
            logger.error("-->errore nel caricare la lista delle risorse " + resourcePath, e);
            e.printStackTrace();
        }
        return documents;
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

}
