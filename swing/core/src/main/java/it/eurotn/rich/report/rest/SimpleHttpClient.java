package it.eurotn.rich.report.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SimpleHttpClient {
    protected final CloseableHttpClient httpclient;
    protected final String serverName;

    public SimpleHttpClient(String serverName) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        this.serverName = serverName;
    }

    public void close() {
        try {
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doDeleteRequest(String url) {
        HttpDelete httpDelete = new HttpDelete(serverName + url);
        doRequest(httpDelete);
    }

    public String doGetRequest(String url) {
        HttpGet httpget = new HttpGet(serverName + url);
        return doRequest(httpget);
    }

    public String doPostRequest(String url, List<NameValuePair> parameters) {
        HttpPost httpost = new HttpPost(serverName + url);
        if (parameters != null) {
            HttpEntity requestEntity = new UrlEncodedFormEntity(parameters, Consts.UTF_8);
            httpost.setEntity(requestEntity);
        }
        return doRequest(httpost);
    }

    public String doPutMultiPartRequest(String url, Map<String, ContentBody> parts) {
        HttpPut httput = new HttpPut(serverName + url);
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        for (Entry<String, ContentBody> part : parts.entrySet()) {
            reqEntity.addPart(part.getKey(), part.getValue());
        }
        httput.setEntity(reqEntity.build());
        return doRequest(httput);
    }

    public String doPutRequest(String url, String body) {
        HttpPut httput = new HttpPut(serverName + url);
        if (body != null) {
            StringEntity requestEntity = new StringEntity(body, Consts.UTF_8);
            httput.setEntity(requestEntity);
        }
        return doRequest(httput);
    }

    public String doRequest(HttpRequestBase httpRequest) {
        HttpEntity responseEntity = null;
        try (CloseableHttpResponse httpResponse = httpclient.execute(httpRequest)) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String statusMessage = httpResponse.getStatusLine().getReasonPhrase();
            if (statusCode >= 300) {
                throw new HttpException("Failed with HTTP error code : " + statusCode + " " + statusMessage);
            }
            responseEntity = httpResponse.getEntity();
            String result = new String(EntityUtils.toString(responseEntity));
            if (responseEntity != null) {
                EntityUtils.consume(responseEntity);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel connettermi al server " + serverName, e);
        }
    }

    /**
     * Effettua una richiesta get all'url indicato e scarica la risposta nel file in destinazione. Il file viene creato
     * nuovo ed eventualmente sovrascritto.
     *
     * @param url
     * @param pathDestinazione
     * @throws IOException
     */
    public void downloadFileWithGetRequest(String url, String pathDestinazione) throws IOException {
        String esito = " OK";
        HttpGet httpget = null;
        InputStream instream = null;
        try {
            File file = new File(pathDestinazione);
            file.getParentFile().mkdirs();
            file.createNewFile();
            httpget = new HttpGet(serverName + url);
            HttpResponse response = httpclient.execute(httpget);
            writeResponceToFile(response, file);
        } catch (IOException ex) {
            esito = " IOException";
            throw ex;
        } catch (RuntimeException ex) {
            esito = " RuntimeException";
            throw ex;
        } finally {
            if (httpget != null) {
                httpget.abort();
            }
        }
    }

    private void writeResponceToFile(HttpResponse response, File file) throws IOException {
        HttpEntity entity = response.getEntity();
        InputStream instream = null;
        try {
            if (entity != null) {
                instream = entity.getContent();
                BufferedInputStream bis = new BufferedInputStream(instream);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                int inByte;
                while ((inByte = bis.read()) != -1) {
                    bos.write(inByte);
                }
                bis.close();
                bos.close();
            }
        } finally {
            if (instream != null) {
                instream.close();
            }
        }
    }
}
