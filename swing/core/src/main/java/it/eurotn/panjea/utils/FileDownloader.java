package it.eurotn.panjea.utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

import org.apache.log4j.Logger;

public final class FileDownloader {

    private static final Logger LOGGER = Logger.getLogger(FileDownloader.class);

    private static final int BUFFER_SIZE = 4096;

    private FileDownloader() {
    }

    /**
     *
     * @param urlServer
     *            url del server
     * @param fileName
     *            nome del file da scaricare
     * @param pathToSave
     *            path dove salvare il file (incluso il nome del file)
     * @return true se il file è stato scaricato.
     */
    public static boolean download(String urlServer, String fileName, Path pathToSave) {
        boolean result = false;
        try {
            URL url = new URL(new StringBuilder(urlServer).append(fileName).toString());
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // opens input stream from the HTTP connection
                try (InputStream inputStream = httpConn.getInputStream();
                        FileOutputStream outputStream = new FileOutputStream(pathToSave.toString())) {

                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead = -1;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.close();
                    result = true;
                }
            } else {
                LOGGER.error("-->errore . Il server di aggiornamento ha risposto " + responseCode,
                        new RuntimeException());
            }
        } catch (Exception ex) {
            LOGGER.error("-->errore nello scaricare il file", new RuntimeException(ex));
        }
        return result;
    }
}