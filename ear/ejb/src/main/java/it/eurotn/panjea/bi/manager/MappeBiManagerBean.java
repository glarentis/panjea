/**
 *
 */
package it.eurotn.panjea.bi.manager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.bi.exception.MappaNonPresenteException;
import it.eurotn.panjea.bi.manager.interfaces.MappeBiManager;
import it.eurotn.panjea.bi.util.Mappa;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.MappeBiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MappeBiManager")
public class MappeBiManagerBean implements MappeBiManager {

    private static Logger logger = Logger.getLogger(MappeBiManagerBean.class);

    private static final String DIR_PANJEA = "panjea";
    private static final String DIR_MAPS = "maps";

    @Override
    public Map<String, byte[]> caricaFilesMappa(final String nomeFileMappa)
            throws MappaNonPresenteException {
        logger.debug("--> Enter caricaFilesMappa");
        Map<String, byte[]> filesMappa = new HashMap<String, byte[]>();

        File dir = getMapsDir();

        File[] txtFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(nomeFileMappa);
            }
        });

        if (dir.length() == 0) {
            logger.error("--> errore la mappa " + nomeFileMappa + " non ha files");
            throw new MappaNonPresenteException(nomeFileMappa);
        }

        for (File fileMappa : txtFiles) {
            String fileName = fileMappa.getName();

            // escludo il file dell'immagine
            if (!fileName.endsWith("png")) {
                try {
                    filesMappa.put(fileName, Files.readAllBytes(
                            new File(getMapsDir() + File.separator + fileName).toPath()));
                } catch (IOException e) {
                    logger.error("--> errore durante la lettura del file " + fileName, e);
                    throw new RuntimeException("errore durante la lettura del file " + fileName, e);
                }
            }
        }

        logger.debug("--> Exit caricaFilesMappa");
        return filesMappa;
    }

    @Override
    public List<Mappa> caricaMappe() {
        logger.debug("--> Enter caricaMappe");

        List<Mappa> mappe = new ArrayList<Mappa>();

        // carico tutti i file .txt perchè contengono le definizioni della mappa
        File dir = getMapsDir();

        File[] txtFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".txt");
            }
        });

        for (File file : txtFiles) {
            try (InputStream sr = new BufferedInputStream(new FileInputStream(file));
                    BufferedReader br = new BufferedReader(new InputStreamReader(sr, "UTF8"))) {
                // nella prima riga del file txt trovo le info della cateogria, descrizione e field
                // gestiti
                String line = br.readLine();
                if (line != null) {
                    String categorie = line.split("#")[0];
                    System.err.println("categorie: " + categorie);
                    String descrizione = line.split("#")[1];
                    System.err.println("descrizione: " + descrizione);
                    String fields = line.split("#")[2];
                    System.err.println("fields: " + fields);
                    List<String> filedsList = Arrays.asList(fields.split(","));

                    int idx = file.getCanonicalPath().lastIndexOf(".");
                    String imageFile = file.getCanonicalPath().substring(0, idx + 1) + "png";
                    byte[] imageFileContent = Files.readAllBytes(new File(imageFile).toPath());

                    for (String categoria : categorie.split(",")) {
                        Mappa mappa = new Mappa(FilenameUtils.removeExtension(file.getName()),
                                categoria, descrizione, filedsList, imageFileContent);
                        mappe.add(mappa);
                    }
                }
            } catch (IOException e) {
                logger.error("--> errore durante il caricamento della definizione della mappa "
                        + file.getName(), e);
                throw new RuntimeException(
                        "errore durante il caricamento della definizione della mappa "
                                + file.getName(),
                        e);
            }
        }

        logger.debug("--> Exit caricaMappe");
        return mappe;
    }

    /**
     * Restituisce la directory che contiene le mappe disponibili.
     *
     * @return directory
     */
    private File getMapsDir() {
        String jbossConfDir = System.getProperty("jboss.server.config.url").replace("file:", "");

        String mapsDir = jbossConfDir + DIR_PANJEA + File.separator + DIR_MAPS;

        File dir = new File(mapsDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

}
