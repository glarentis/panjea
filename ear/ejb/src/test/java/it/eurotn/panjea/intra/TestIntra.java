package it.eurotn.panjea.intra;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.Before;
import org.junit.Test;

import it.eurotn.panjea.anagrafica.TestAnagrafica;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.service.interfaces.IntraService;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

public class TestIntra {

    private static final String PATH_FILE_SERVIZI = "/home/giangi/Temp/servizi.txt";

    private InitialContext ic;

    private IntraService intraService;

    @Test
    public void esportaDichiarazione() {
        List<Integer> d = new ArrayList<Integer>();
        d.add(9);
        try {
            FileDichiarazione result = intraService.generaFileEsportazione(d, false);
            IOUtils.write(result.getContent(), System.out);
        } catch (PreferenceNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("java.security.auth.login.config",
                TestAnagrafica.class.getClassLoader().getResource("auth.conf").getPath());

        String username = "europa#SALFRA#Panjea#it";
        String credential = "pnj_adm";
        char[] password = credential.toCharArray();
        BasicConfigurator.configure();
        FileInputStream is = new FileInputStream(
                TestIntra.class.getClassLoader().getResource("log4j.properties").getPath());
        Properties logProperties = new Properties();
        logProperties.load(is);
        BasicConfigurator.resetConfiguration();
        PropertyConfigurator.configure(logProperties);
        UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, password);
        LoginContext loginContext;
        try {
            loginContext = new LoginContext("other", passwordHandler);
            loginContext.login();
        } catch (LoginException e) {
            fail(e.getMessage());
        }

        ic = new InitialContext();
        intraService = (IntraService) ic.lookup("Panjea.IntraService");
    }

    @Test
    public void testGeneraAreaIntra() {
        try {

            AreaMagazzino fakeAM = new AreaMagazzino();
            fakeAM.setId(485);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGeneraFileEsportazione() {

        List<Integer> id = new ArrayList<>();
        id.add(6);
        try {
            FileDichiarazione ret = intraService.generaFileEsportazione(id, true);
        } catch (PreferenceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testImportazioneNomenclatura() {
        try {
            // Open the file that is the first
            // command line parameter
            File file = new File("/home/giangi/Temp/nomenc.txt");
            // Get the object of DataInputStream
            InputStream is = new FileInputStream(file);

            // Get the size of the file
            long length = file.length();

            // You cannot create an array using a long type.
            // It needs to be an int type.
            // Before converting to an int type, check
            // to ensure that file is not larger than Integer.MAX_VALUE.
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new RuntimeException("Could not completely read file " + file.getName());
            }

            // Close the input stream and return bytes
            is.close();
            intraService.importaNomenclatura(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testImportazioneServizi() {
        try {
            File file = new File(PATH_FILE_SERVIZI);
            // Get the object of DataInputStream
            InputStream is = new FileInputStream(file);

            // Get the size of the file
            long length = file.length();

            // You cannot create an array using a long type.
            // It needs to be an int type.
            // Before converting to an int type, check
            // to ensure that file is not larger than Integer.MAX_VALUE.
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new RuntimeException("Could not completely read file " + file.getName());
            }

            // Close the input stream and return bytes
            is.close();
            intraService.importaServizi(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
