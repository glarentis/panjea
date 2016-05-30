/**
 * 
 */
package it.eurotn.panjea.report;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author fattazzo
 * 
 */
public class ReportManager {

	// login parameters
	private static final String PARAMETER_USERNAME = "j_username";
	private static final String PARAM_PASSWORD = "j_password";
	private static final String PARAM_OUTPUT_FORMAT = "RUN_OUTPUT_FORMAT";

	private static final String USER_NAME = "jasperadmin";
	private static final String PASSWORD = "jasperadmin";

	private static final String SERVICE_LOGIN = "/login";
	private static final String SERVICE_REPORT = "/report";
	private static final String SERVICE_RESOURCE = "/resource";

	private static final String RESPONSE_BODY_PROPERTY_UUID = "uuid";
	private static final String RESPONSE_BODY_PROPERTY_TOTAL_PAGE = "totalPages";

	// SERVER PARAMETERS
	private static final String SCHEME = "http";
	private static final String HOST = System.getProperty("jboss.bind.address");
	private static final int PORT = 8080;

	private int statusCode;

	private org.apache.commons.httpclient.HttpClient client;

	/**
	 * Costruttore.
	 */
	public ReportManager() {
		super();
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
	 * Restituisce l'url del path richiesto.
	 * 
	 * @param path
	 *            path
	 * @return url
	 * @throws Exception .
	 */
	private String createURL(String path) throws Exception {
		String url;
		url = new URL(SCHEME, HOST, PORT, "/jasperserver/rest" + path).toString();
		return url;
	}

	/**
	 * Converte il document xml in string.
	 * 
	 * @param xml
	 *            document da convertire
	 * @return valore in stringa
	 * @throws Exception .
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
			InputStream is = getReportContent(reportUuid);
			OutputStream os = new FileOutputStream(savePath);
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			is.close();
			os.close();
		} catch (EmptyReportException e) {
			throw e;
		} catch (Exception e) {
			System.err.println("Errore: " + e);
		}
	}

	/**
	 * Restituisce lo stream del report con uuid indicato.
	 * 
	 * @param reportUuid
	 *            uuid
	 * @return straem
	 * @throws Exception .
	 */
	private InputStream getReportContent(String reportUuid) throws Exception {

		String reportFileURL = createURL(SERVICE_REPORT) + "/" + reportUuid + "?file=report";
		GetMethod getMethodFile = new GetMethod(reportFileURL);

		// Send GET request to download the report
		statusCode = client.executeMethod(getMethodFile);

		// Check correct report process
		if (statusCode != 200) {
			System.out.println("Downlaod failed: " + getMethodFile.getStatusLine());
		}

		// Getting the report body
		InputStream is = getMethodFile.getResponseBodyAsStream();
		return is;
	}

	/**
	 * Esegue il login su jasper.
	 * 
	 * @throws Exception .
	 */
	private void loginToJasper() throws Exception {
		client = new org.apache.commons.httpclient.HttpClient();

		// Setting Login URL in a POST method
		String loginURL = createURL(SERVICE_LOGIN);
		PostMethod postMethod = new PostMethod(loginURL);

		// Set authentication parameters
		postMethod.addParameter(PARAMETER_USERNAME, USER_NAME);
		postMethod.addParameter(PARAM_PASSWORD, PASSWORD);

		// Send POST with login request
		statusCode = client.executeMethod(postMethod);

		// Check correct login process
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("Login failed: " + postMethod.getStatusLine());
			return;
		}
	}

	/**
	 * Restitusce il resurce descriptor della risorsa specificata.
	 * 
	 * @param reportPath
	 *            risorsa
	 * @return resource descriptor
	 * @throws Exception .
	 */
	private Document retrieveResourceDescriptor(String reportPath) throws Exception {
		String resourceURL = createURL(SERVICE_RESOURCE) + reportPath;
		GetMethod getMethod = new GetMethod(resourceURL);

		// Send GET request for descriptor
		statusCode = client.executeMethod(getMethod);

		// Check correct descriptor process
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("Descriptor failed: " + getMethod.getStatusLine());
		}

		// Get the response body as String
		String descriptorSource = getMethod.getResponseBodyAsString();

		// Transform descriptor from String into XML Document
		Document descriptorXML = stringToDom(descriptorSource);

		return descriptorXML;
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
	 * @throws Exception .
	 */
	@SuppressWarnings("deprecation")
	private String runReport(String reportPath, Map<String, String> params, String outPutFormat) throws Exception {
		Document descriptorXML = retrieveResourceDescriptor(reportPath);

		// Use this method if you need to send parameters to the report
		// These are added to the XML Document
		for (Entry<String, String> entry : params.entrySet()) {
			addParameter(descriptorXML, entry.getKey(), entry.getValue());
		}

		// Settting PUT method to run report, the url contains the RUN_OUTPUT_FORMAT parameter
		StringBuilder reportURL = new StringBuilder();
		reportURL.append(createURL(SERVICE_REPORT));
		reportURL.append(reportPath);
		reportURL.append("?");
		reportURL.append(PARAM_OUTPUT_FORMAT);
		reportURL.append("=");
		reportURL.append(outPutFormat);
		PutMethod putMethod = new PutMethod(reportURL.toString());

		// Setting the request Body. The descriptor XML Document is transform to String and add to the request body.
		putMethod.setRequestBody(domToString(descriptorXML));

		// Send PUT request to execute the report.
		statusCode = client.executeMethod(putMethod);

		// Check correct report process
		if (statusCode != 201) {
			System.out.println("Report failed: " + putMethod.getStatusLine());
		}

		// Get the response body
		String reportSource = putMethod.getResponseBodyAsString();

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
	 * Converte la string in Document.
	 * 
	 * @param xmlSource
	 *            string
	 * @return Document
	 * @throws Exception .
	 */
	private Document stringToDom(String xmlSource) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		StringReader sr = new StringReader(xmlSource);
		InputSource is = new InputSource(sr);
		return builder.parse(is);
	}

}
