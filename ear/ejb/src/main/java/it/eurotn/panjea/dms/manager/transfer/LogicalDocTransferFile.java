package it.eurotn.panjea.dms.manager.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import javax.mail.internet.ContentDisposition;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import it.eurotn.panjea.sicurezza.domain.Utente;

public abstract class LogicalDocTransferFile {

    private static final Logger LOGGER = Logger.getLogger(LogicalDocTransferFile.class);

    /**
     * @param idDoc
     *            id documento logicaldoc
     * @return contenuto del file
     */
    public final File getContent(long idDoc) {
        LOGGER.debug("--> Enter getContent");

        StringBuilder sb = new StringBuilder(getLogicalDocUrl()).append("/download?docId=")
                .append(String.valueOf(idDoc));

        File file = null;
        try {
            Utente utente = getUtente();
            String password = StringUtils.rightPad(utente.getPassword(), 8);

            URL url = new URL(sb.toString());
            URLConnection uc = url.openConnection();
            String userpass = utente.getUserName() + ":" + password;
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            uc.setRequestProperty("Authorization", basicAuth);
            ContentDisposition contentDisposition = new ContentDisposition(uc.getHeaderField("Content-Disposition"));
            String fileName = MimeUtility.decodeText(contentDisposition.getParameter("filename"));
            file = File.createTempFile(FilenameUtils.getBaseName(fileName), "." + FilenameUtils.getExtension(fileName));
            try (InputStream in = uc.getInputStream(); OutputStream out = new FileOutputStream(file)) {
                IOUtils.copy(in, out);
            }
        } catch (IOException | ParseException e) {
            LOGGER.error("-->errore nel creare l'indirizzo per il download; " + sb.toString(), e);
            throw new RuntimeException(e);
        }
        LOGGER.debug("--> Exit getcontent");
        return file;
    }

    /**
     * @param idDoc
     *            id documento logicaldoc
     * @return contenuto del file
     */
    public final byte[] getContentByte(long idDoc) {
        LOGGER.debug("--> Enter getContent");

        StringBuilder sb = new StringBuilder(getLogicalDocUrl()).append("/download?docId=")
                .append(String.valueOf(idDoc));

        byte[] content = null;
        try {
            Utente utente = getUtente();
            String password = StringUtils.rightPad(utente.getPassword(), 8);

            URL url = new URL(sb.toString());
            URLConnection uc = url.openConnection();
            String userpass = utente.getUserName() + ":" + password;
            String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
            uc.setRequestProperty("Authorization", basicAuth);
            try (InputStream in = uc.getInputStream()) {
                content = IOUtils.toByteArray(in);
            }
        } catch (IOException e) {
            LOGGER.error("-->errore nel creare l'indirizzo per il download; " + sb.toString(), e);
            throw new RuntimeException(e);
        }
        LOGGER.debug("--> Exit getcontent");
        return content;
    }

    /**
     * @return url server logicaldoc
     */
    public abstract String getLogicalDocUrl();

    /**
     * @return utente di riferimento
     */
    public abstract Utente getUtente();

    private String putContent(AbstractContentBody body) {
        LOGGER.debug("--> Enter putContent");
        String keyUpload = UUID.randomUUID().toString();

        HttpPost post = new HttpPost(getLogicalDocUrl() + "/uploadpanjea?sid=" + keyUpload);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("downloadFile", body);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        LOGGER.debug("--> Exit putContent");
        return keyUpload;
    }

    /**
     * @param filePath
     *            nome del file da pubblicare
     * @return uuid utilizzato per questo upload
     */
    public final String putContent(String filePath) {
        LOGGER.debug("--> Enter putContent");

        File file = new File(filePath);
        FileBody fileBody = new FileBody(file, ContentType.APPLICATION_OCTET_STREAM, FilenameUtils.getName(filePath));

        return putContent(fileBody);
    }

    /**
     * @param fileName
     *            nome del file da pubblicare
     * @param fileContent
     *            file content
     * @return uuid utilizzato per questo upload
     */
    public final String putContent(String fileName, byte[] fileContent) {
        LOGGER.debug("--> Enter putContent");

        ByteArrayBody fileBody = new ByteArrayBody(fileContent, ContentType.APPLICATION_OCTET_STREAM, fileName);

        return putContent(fileBody);
    }
}
