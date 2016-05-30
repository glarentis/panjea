package it.eurotn.panjea.cosaro.importazione;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.cosaro.importazione.importer.ImporterCosaro;
import it.eurotn.panjea.mail.service.JbossMailService;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdiniManager;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

@Stateless(name = "Panjea.Cosaro.Importazione")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.Cosaro.Importazione")
public class ImportazioneCosaroServiceBean implements ImportazioneCosaroService {

    private static final Logger LOGGER = Logger.getLogger(ImportazioneCosaroServiceBean.class);

    @EJB(beanName = "Panjea.CosaroImporter.UNICOMM")
    private ImporterCosaro unicommImporter;

    @EJB(beanName = "Panjea.CosaroImporter.COOP")
    private ImporterCosaro coopImporter;

    @EJB
    protected PanjeaMessage panjeaMessage;

    @EJB
    private ImportazioneOrdiniManager importazioneOrdiniManager;

    @EJB
    private JbossMailService jbossMailService;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void importa(File fileToImport) {
        // Scan della cartella. Termino con la cancellazione di tutti i file presenti.
        // Le righe importate non hanno prezzo, li lascio sempre a zero
        if (!fileToImport.isDirectory() && fileToImport.exists()) {
            String backupDestination = "/backup/";
            try {
                // UNICOMM
                if (unicommImporter.test(fileToImport)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("--> File " + fileToImport.getAbsolutePath() + " di tipo unicomm...lo importo");
                    }
                    unicommImporter.importa(fileToImport);
                } else {
                    // COOP
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("--> File " + fileToImport.getAbsolutePath() + " di tipo coop...lo importo");
                    }
                    coopImporter.importa(fileToImport);
                }

                // questo lo faccio solo se l'importazione è andata a buon fine, faccio una catch
                // separata per mandare
                // dei messaggi diversi in caso di errore
                try {
                    ParametriRicercaOrdiniImportati parametri = new ParametriRicercaOrdiniImportati();
                    parametri.setProvenienza(EProvenienza.ATON);
                    importazioneOrdiniManager.aggiornaPrezziDeterminatiOrdiniImportati(parametri);
                } catch (Exception e) {
                    jbossMailService.send("Errore ricalcolo prezzi determinati degli ordini importati", e.getMessage(),
                            new File[] { fileToImport });

                    panjeaMessage.send(e.getMessage(), new String[] { "evasione" }, 5,
                            PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                }
            } catch (Exception e) {
                backupDestination = "/error/";
                jbossMailService.send("Errore importazione ordini", e.getMessage(), new File[] { fileToImport });

                panjeaMessage.send(e.getMessage(), new String[] { "evasione" }, 5,
                        PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            }

            try {
                File dirDest = new File(fileToImport.getParentFile().getPath() + backupDestination);
                if (!dirDest.exists()) {
                    dirDest.mkdir();
                }
                FileUtils.moveFile(fileToImport, new File(fileToImport.getParentFile().getPath() + backupDestination
                        + Calendar.getInstance().getTimeInMillis() + "_" + fileToImport.getName()));
            } catch (IOException e) {
                panjeaMessage.send("-->errore nel copiare il file delle bilance nella cartella di backup",
                        new String[] { "evasione" }, 8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                LOGGER.error("-->errore nel copiare il file delle bilace nella cartella di backup "
                        + fileToImport.getParentFile().getPath(), e);
            }
        }
    }

}
