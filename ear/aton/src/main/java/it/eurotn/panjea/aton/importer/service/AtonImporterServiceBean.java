package it.eurotn.panjea.aton.importer.service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.aton.importer.manager.interfaces.ImportaOrdini;
import it.eurotn.panjea.aton.importer.service.interfaces.AtonImporterService;
import it.eurotn.panjea.aton.importer.service.interfaces.DolcelitAtonImporterService;
import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdiniManager;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.AtonImporterService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AtonImporterService")
public class AtonImporterServiceBean implements AtonImporterService {

    @Resource
    private SessionContext sessionContext;

    private static Logger logger = Logger.getLogger(AtonImporterServiceBean.class);

    @EJB
    protected PanjeaDAO panjeaDAO;
    @EJB
    protected ImportaOrdini importaOrdini;

    @EJB
    protected DolcelitAtonImporterService dolcelitAtonImporterService;

    @EJB
    private ImportazioneOrdiniManager importazioneOrdiniManager;

    /**
     * @return codice azienda
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void importa() throws ImportException {
        logger.debug("--> Enter importa");
        try {
            String dir = importaOrdini.getAtonImportDir();
            if (dir.isEmpty()) {
                return;
            }
            String prefix = importaOrdini.getAtonPrefixImport();
            File file = new File(dir);
            String fileName = prefix + "TESTAT";
            String[] fileTestate = file.list(new PrefixFileFilter(fileName, IOCase.SENSITIVE));
            if (fileTestate == null) {
                return;
            }
            for (String pathFile : fileTestate) {
                List<OrdineImportato> ordiniDaCalcolare = importaOrdini.importa(dir + pathFile);
                ParametriRicercaOrdiniImportati parametri = new ParametriRicercaOrdiniImportati();
                parametri.setProvenienza(EProvenienza.ATON);
                for (OrdineImportato ordineImportato : ordiniDaCalcolare) {
                    parametri.setNumeroOrdine(Integer.parseInt(ordineImportato.getNumero()));
                    importazioneOrdiniManager.aggiornaPrezziDeterminatiOrdiniImportati(parametri);
                    // ALLA FINE...DOBBIAMO METTERLO..CAZZOOOOOOOOO
                    if ("dolcelit".equalsIgnoreCase(getAzienda())) {
                        // Carico ogni riga che ho creato e imposto le modifiche appositamente per dolcelit...'na
                        // bellezza.
                        dolcelitAtonImporterService.importa(ordineImportato);
                    }
                }

                File fileTestata = new File(dir + pathFile);
                pathFile = pathFile.replace("TESTAT", "RIGHE");
                File fileRighe = new File(dir + pathFile);
                pathFile = pathFile.replace("RIGHE", "NOTE");
                File fileNote = new File(dir + pathFile);
                fileTestata.delete();
                fileRighe.delete();
                fileNote.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit importa");
    }

    @Override
    public int[] verifica() {
        int[] rows = new int[] { 0, 0 };
        String dir = importaOrdini.getAtonImportDir();
        if (!dir.isEmpty()) {
            String prefix = importaOrdini.getAtonPrefixImport();
            File file = new File(dir);
            String fileName = prefix + "TESTAT";
            String[] fileTestate = file.list(new PrefixFileFilter(fileName, IOCase.SENSITIVE));
            if (fileTestate != null) {
                rows[0] = fileTestate.length;
            }
        }

        Query query = panjeaDAO.prepareQuery("select count(o.id) from OrdineImportato o ");
        try {
            Long result = (Long) panjeaDAO.getSingleResult(query);
            rows[1] = result.intValue();
        } catch (Exception e) {
            rows[1] = -1;
        }

        return rows;
    }
}