/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 */
package it.eurotn.rich.report.jasperserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.axis.client.Call;
import org.apache.log4j.Logger;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.OperationResult;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Request;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.ws.xml.Marshaller;

/**
 * 
 * @author gtoffoli
 */
public class WSClient {
    static Logger logger = Logger.getLogger(WSClient.class);

    private JServer server = null;

    private String webservicesUri = null; // "http://127.0.0.1:8080/axis2/services/repository-ws-1.0";

    private ManagementService managementService = null;

    private Marshaller marshaller = new Marshaller();
    private Unmarshaller unmarshaller;
    private String cachedServerVersion;

    public WSClient(JServer server) throws Exception {
        this.server = server;
        unmarshaller = new Unmarshaller();
        URL url;
        try {
            url = new URL(server.getUrl());
        } catch (MalformedURLException e1) {
            throw new Exception(e1);
        }

        setWebservicesUri(url.toString());
    }

    /**
     * Add or Modify a resource. Return the updated ResourceDescriptor
     * 
     */
    public ResourceDescriptor addOrModifyResource(ResourceDescriptor descriptor, File inputFile) throws Exception {
        return modifyReportUnitResource(null, descriptor, inputFile);
    }

    public void delete(ResourceDescriptor descriptor) throws Exception {
        delete(descriptor, null);
    }

    /**
     * Delete a resource and its contents Specify the reportUnitUri if you are deleting something inside this report
     * unit.
     * 
     */
    public void delete(ResourceDescriptor descriptor, String reportUnitUri) throws Exception {

        try {
            Request req = new Request();
            req.setOperationName("delete");
            req.setResourceDescriptor(descriptor);
            req.setLocale(getServer().getLocale());

            if (reportUnitUri != null && reportUnitUri.length() > 0) {
                req.getArguments().add(new Argument(Argument.MODIFY_REPORTUNIT, reportUnitUri));
            }

            String result = getManagementService().delete(marshaller.marshal(req));

            OperationResult or = (OperationResult) unmarshaller.unmarshal(result);

            if (or.getReturnCode() != 0) {
                throw new Exception(or.getReturnCode() + " - " + or.getMessage());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Export a resource using the "get" ws and save the resource in the file specified by the user... If the outputFile
     * is null, the argument "NO_ATTACHMENT" is added to the request in order of avoid the attachment trasmission.
     * 
     */
    public ResourceDescriptor get(ResourceDescriptor descriptor, File outputFile) throws Exception {
        return get(descriptor, outputFile, null);
    }

    /**
     * Export a resource using the "get" ws and save the resource in the file specified by the user... If the outputFile
     * is null, the argument "NO_ATTACHMENT" is added to the request in order of avoid the attachment trasmission.
     * 
     */
    public ResourceDescriptor get(ResourceDescriptor descriptor, File outputFile, java.util.List args)
            throws Exception {

        try {
            Request req = new Request();

            req.setOperationName("get");
            req.setResourceDescriptor(descriptor);
            req.setLocale(getServer().getLocale());

            if (args != null) {
                for (int i = 0; i < args.size(); ++i) {
                    Argument arg = (Argument) args.get(i);
                    req.getArguments().add(arg);
                }
            }

            if (outputFile == null) {
                req.getArguments().add(new Argument(Argument.NO_RESOURCE_DATA_ATTACHMENT, null));
            }

            String result = getManagementService().get(marshaller.marshal(req));

            OperationResult or = (OperationResult) unmarshaller.unmarshal(result);

            if (or.getReturnCode() != 0) {
                throw new Exception(or.getReturnCode() + " - " + or.getMessage());
            }

            Object[] resAtts = ((org.apache.axis.client.Stub) getManagementService()).getAttachments();
            if (resAtts != null && resAtts.length > 0 && outputFile != null) {
                java.io.InputStream is = ((org.apache.axis.attachments.AttachmentPart) resAtts[0]).getDataHandler()
                        .getInputStream();

                byte[] buffer = new byte[1024];
                OutputStream os = new FileOutputStream(outputFile);
                int bCount = 0;
                while ((bCount = is.read(buffer)) > 0) {
                    os.write(buffer, 0, bCount);
                }
                is.close();
                os.close();
            } else if (outputFile != null) {
                throw new Exception("Attachment not present!");
            }

            return (ResourceDescriptor) or.getResourceDescriptors().get(0);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {

        }

    }

    public ManagementService getManagementService() throws Exception {

        if (managementService == null) {
            ManagementServiceServiceLocator rsl = new ManagementServiceServiceLocator();
            managementService = rsl.getrepository(new java.net.URL(getWebservicesUri()));
            ((org.apache.axis.client.Stub) managementService).setUsername(getUsername());
            ((org.apache.axis.client.Stub) managementService).setPassword(getPassword());
            ((org.apache.axis.client.Stub) managementService).setMaintainSession(true);

        }

        return managementService;
    }

    public String getPassword() {
        return getServer().getPassword();
    }

    public JServer getServer() {
        return server;
    }

    public String getUsername() {
        return getServer().getUsername();
    }

    /**
     * It returns a list of resourceDescriptors.
     */
    public String getVersion() throws Exception {
        if (cachedServerVersion != null) {
            return cachedServerVersion;
        }

        Request req = new Request();

        req.setOperationName(Request.OPERATION_LIST);
        req.setResourceDescriptor(null);
        req.setLocale(getServer().getLocale());

        try {

            ManagementService ms = getManagementService();
            String reqXml = marshaller.marshal(req);
            // System.out.println("Executing list for version.." + new java.util.Date());
            // System.out.flush();
            String result = ms.list(reqXml);
            // System.out.println("Finished list for version.." + new java.util.Date());
            // System.out.flush();
            OperationResult or = (OperationResult) unmarshaller.unmarshal(result);

            if (or.getReturnCode() != 0) {
                throw new Exception(or.getReturnCode() + " - " + or.getMessage());
            }

            cachedServerVersion = or.getVersion();
            return cachedServerVersion;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
        }
    }

    public String getWebservicesUri() {
        return webservicesUri;
    }

    /**
     * It returns a list of resourceDescriptors.
     */
    public java.util.List list(ResourceDescriptor descriptor) throws Exception {
        Request req = new Request();

        req.setOperationName(Request.OPERATION_LIST);
        req.setResourceDescriptor(descriptor);
        req.setLocale(getServer().getLocale());

        StringWriter xmlStringWriter = new StringWriter();
        Marshaller.marshal(req, xmlStringWriter);

        return list(xmlStringWriter.toString());
    }

    /**
     * It returns a list of resourceDescriptors.
     */
    public java.util.List list(String xmlRequest) throws Exception {

        try {

            String result = getManagementService().list(xmlRequest);
            OperationResult or = (OperationResult) unmarshaller.unmarshal(result);

            if (or.getReturnCode() != 0) {
                throw new Exception(or.getReturnCode() + " - " + or.getMessage());
            }

            return or.getResourceDescriptors();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
        }
    }

    /**
     * List all datasources. It returns a list of resourceDescriptors.
     */
    public java.util.List listDatasources() throws Exception {

        Request req = new Request();

        req.setOperationName(Request.OPERATION_LIST);
        req.setResourceDescriptor(null);
        req.setLocale(getServer().getLocale());

        req.getArguments().add(new Argument(Argument.LIST_DATASOURCES, Argument.VALUE_TRUE));

        StringWriter xmlStringWriter = new StringWriter();
        Marshaller.marshal(req, xmlStringWriter);

        return list(xmlStringWriter.toString());
    }

    public ResourceDescriptor modifyReportUnitResource(String reportUnitUri, ResourceDescriptor descriptor,
            File inputFile) throws Exception {

        try {
            Request req = new Request();
            req.setOperationName("put");
            req.setLocale(getServer().getLocale());

            if (reportUnitUri != null && reportUnitUri.length() > 0) {
                req.getArguments().add(new Argument(Argument.MODIFY_REPORTUNIT, reportUnitUri));
            }

            ManagementService ms = getManagementService();

            // ManagementServiceServiceLocator rsl = new ManagementServiceServiceLocator();
            // ManagementService ms = rsl.getrepository(new java.net.URL( getWebservicesUri() ) );
            // ((org.apache.axis.client.Stub)ms).setUsername( getUsername() );
            // ((org.apache.axis.client.Stub)ms).setPassword( getPassword() );
            // ((org.apache.axis.client.Stub)ms).setMaintainSession( false );

            // attach the file...
            if (inputFile != null) {
                descriptor.setHasData(true);
                // ((org.apache.axis.client.Stub)getManagementService()).addAttachment( new DataHandler( new
                // FileDataSource(inputFile)));
                DataHandler attachmentFile = new DataHandler(new FileDataSource(inputFile));

                // Tell the stub that the message being formed also contains an attachment, and it is of type MIME
                // encoding.
                ((org.apache.axis.client.Stub) ms)._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT,
                        Call.ATTACHMENT_ENCAPSULATION_FORMAT_DIME);

                // Add the attachment to the message
                ((org.apache.axis.client.Stub) ms).addAttachment(attachmentFile);
            }

            req.setResourceDescriptor(descriptor);

            String result = ms.put(marshaller.marshal(req));

            OperationResult or = (OperationResult) unmarshaller.unmarshal(result);

            if (or.getReturnCode() != 0) {
                throw new Exception(or.getReturnCode() + " - " + or.getMessage());
            }

            return (ResourceDescriptor) or.getResourceDescriptors().get(0);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {

        }

    }

    public JasperPrint runReport(ResourceDescriptor descriptor, java.util.Map parameters) throws Exception {
        List args = new ArrayList(1);
        args.add(new Argument(Argument.RUN_OUTPUT_FORMAT, Argument.RUN_OUTPUT_FORMAT_JRPRINT));
        Map attachments = runReport(descriptor, parameters, args);

        FileContent content = null;
        if (attachments != null && !attachments.isEmpty()) {

            content = (FileContent) (attachments.values().toArray()[0]);
            // attachments.get("jasperPrint");

        }

        if (content == null) {
            throw new Exception("No JasperPrint");
        }

        InputStream is = new ByteArrayInputStream(content.getData());

        JasperPrint print = null;
        try {
            print = (JasperPrint) JRLoader.loadObject(is);
        } catch (Exception e) {
            System.err.println(e);
        }
        return print;
    }

    /**
     * This method run a report. The return is an OperationResult. If the result is succesfull, the message contains a
     * set of strings (one for each row) with the list of files attached complete of the relative path. I.e.
     * 
     * main_report.html images/logo1.jpg images/chartxyz.jpg
     * 
     * Arguments:
     * 
     * 
     * 
     * The request must contains the descriptor of the report to execute (only the URI is used). Arguments can be
     * attached to the descriptor as childs. Each argument is a ListItem, with the parameter name as Name and the object
     * rapresenting the value as Value.
     * 
     * Operation result Codes: 0 - Success 1 - Generic error
     * 
     */
    @SuppressWarnings({ "unchecked", "unchecked" })
    public Map runReport(ResourceDescriptor descriptor, java.util.Map parameters, List args) throws Exception {

        try {
            Request req = new Request();
            req.setOperationName("runReport");
            req.setLocale(getServer().getLocale());
            ResourceDescriptor newRUDescriptor = new ResourceDescriptor();
            newRUDescriptor.setUriString(descriptor.getUriString());
            for (Iterator i = parameters.keySet().iterator(); i.hasNext();) {
                String key = "" + i.next();
                Object value = parameters.get(key);
                if (value instanceof java.util.Collection) {
                    Iterator cIter = ((Collection) value).iterator();
                    while (cIter.hasNext()) {
                        String item = "" + cIter.next();
                        com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem l = new com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem(
                                key + "", item);
                        l.setIsListItem(true);
                        newRUDescriptor.getParameters().add(l);
                    }
                } else {
                    newRUDescriptor.getParameters()
                            .add(new com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem(key + "",
                                    parameters.get(key)));
                }
            }

            req.setResourceDescriptor(newRUDescriptor);
            req.getArguments().addAll(args);

            String result = getManagementService().runReport(marshaller.marshal(req));

            OperationResult or = (OperationResult) unmarshaller.unmarshal(result);

            if (or.getReturnCode() != 0) {
                throw new Exception(or.getReturnCode() + " - " + or.getMessage());
            }

            Map results = new HashMap();

            Object[] resAtts = ((org.apache.axis.client.Stub) getManagementService()).getAttachments();
            boolean attachFound = false;
            for (int i = 0; resAtts != null && i < resAtts.length; ++i) {
                attachFound = true;
                DataHandler actualDH = ((org.apache.axis.attachments.AttachmentPart) resAtts[i]).getDataHandler();
                String name = actualDH.getName(); // ((org.apache.axis.attachments.AttachmentPart)resAtts[i]).
                // getAttachmentFile();
                String contentId = ((org.apache.axis.attachments.AttachmentPart) resAtts[i]).getContentId();
                if (name == null) {
                    name = "attachment-" + i;
                }
                if (contentId == null) {
                    contentId = "attachment-" + i;
                }

                InputStream is = actualDH.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] data = new byte[1000];
                int bytesRead;

                while ((bytesRead = is.read(data)) != -1) {
                    bos.write(data, 0, bytesRead);
                }

                data = bos.toByteArray();

                String contentType = actualDH.getContentType();

                FileContent content = new FileContent();
                content.setData(data);
                content.setMimeType(contentType);
                content.setName(name);

                results.put(contentId, content);

            }
            if (!attachFound) {
                throw new Exception("Attachment not present!");
            }

            return results;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {

        }

    }

    public JasperPrint runReport(String reportPath, java.util.Map parameters) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("--> Enter runReport per il report " + reportPath);
        }

        ResourceDescriptor rd = new ResourceDescriptor();
        rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
        rd.setUriString(reportPath);
        JasperPrint jasperPrint = runReport(rd, parameters);
        if (logger.isDebugEnabled()) {
            logger.debug("--> Exit runReport con numero pagine " + jasperPrint.getPages().size());
        }
        return jasperPrint;
    }

    public void setManagementService(ManagementService managementService) {
        this.managementService = managementService;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public void setWebservicesUri(String webservicesUri) {
        this.webservicesUri = webservicesUri;
    }
}
