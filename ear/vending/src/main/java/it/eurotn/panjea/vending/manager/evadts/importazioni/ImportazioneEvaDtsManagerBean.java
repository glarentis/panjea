package it.eurotn.panjea.vending.manager.evadts.importazioni;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.logicaldoc.webservice.document.DocumentClient;
import com.logicaldoc.webservice.document.WSDocument;
import com.logicaldoc.webservice.folder.FolderClient;
import com.logicaldoc.webservice.folder.WSFolder;

import it.eurotn.panjea.dms.manager.interfaces.DMSLookupClientWebService;
import it.eurotn.panjea.dms.manager.interfaces.DMSSecurityManager;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.evadts.exception.ImportazioneEvaDtsException;
import it.eurotn.panjea.vending.manager.evadts.importazioni.interfaces.ImportazioneEvaDtsManager;
import it.eurotn.panjea.vending.manager.evadts.importazioni.interfaces.ImportazioniEvaDtsRifornimentoManager;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.interfaces.RilevazioniEvaDtsManager;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.ImportazioneEvaDtsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ImportazioneEvaDtsManager")
public class ImportazioneEvaDtsManagerBean implements ImportazioneEvaDtsManager {

    private static final Logger LOGGER = Logger.getLogger(ImportazioneEvaDtsManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DMSLookupClientWebService dmsLookupClientWebService;
    @EJB
    private DMSSecurityManager dmsSecurityManager;

    @EJB
    private RilevazioniEvaDtsManager rilevazioniEvaDtsManager;

    @EJB
    private ImportazioniEvaDtsRifornimentoManager importazioniEvaDtsRifornimentoManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @Resource
    private SessionContext sessionContext;

    /**
     * @return codiceAzienda
     */
    protected String getCodiceAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    private WSFolder getTrasmissioniFolder(String sid) {
        FolderClient folderService = dmsLookupClientWebService.creaFolderService();
        WSFolder folder = null;
        try {
            WSFolder rootFolder = folderService.getDefaultWorkspace(sid);
            Calendar calNow = Calendar.getInstance();
            String datePath = DateFormatUtils.format(calNow, "yyyy/MM/dd");
            folder = folderService.createPath(sid, rootFolder.getId(),
                    getCodiceAzienda() + "/vending/palmari/ricezioni/" + datePath + "/evadts");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder;
    }

    @Override
    public ImportazioneFileEvaDtsResult importaEvaDts(String fileName, byte[] fileContent,
            EvaDtsImportFolder evaDtsImportFolder) {
        return importaEvaDts(fileName, fileContent, evaDtsImportFolder, false);
    }

    @Override
    public ImportazioneFileEvaDtsResult importaEvaDts(String fileName, byte[] fileContent,
            EvaDtsImportFolder evaDtsImportFolder, boolean forzaImportazione) {
        LOGGER.debug("--> Enter importaEvaDts");

        // preparo tutte le query prima
        EnumMap<ImportazioniQueryType, Query> queries = initImportQuery();

        FileImportazioneEvaDtsReader reader = new FileImportazioneEvaDtsReader(fileName, fileContent,
                evaDtsImportFolder);

        ImportazioneFileEvaDtsResult result = new ImportazioneFileEvaDtsResult();
        result.setFilePath(evaDtsImportFolder.getFolder());
        result.setFileName(fileName);
        result.setEvaDtsImportFolder(evaDtsImportFolder);

        RilevazioneEvaDts[] rilevazioniImport;
        try {
            rilevazioniImport = reader.getRilevazioniEvaDts();
        } catch (ImportazioneEvaDtsException e) {
            LOGGER.error("--> Errore durante la lettura delle rilevazioni.", e);
            result.aggiungiErrore(null, e.getMessage());
            return result;
        }

        List<AreaRifornimento> rifornimentiGiaAssegnati = new ArrayList<>();
        List<RilevazioneEvaDts> rilevazioniOk = new ArrayList<>();
        List<RilevazioneEvaDts> rilevazioniKo = new ArrayList<>();
        for (int i = 0; i < rilevazioniImport.length; i++) {
            if (rilevazioniImport[i].isEmpty()) {
                result.aggiungiErrore(i, "Rilevazione non valida, non tutti i campi obbligatori sono avvalorati");
                rilevazioniKo.add(rilevazioniImport[i]);
                continue; // escludo le importazioni che non sono valide
            }
            AreaRifornimento rifornimento;
            try {
                rifornimento = importazioniEvaDtsRifornimentoManager.getAreaRifornimento(rilevazioniImport[i],
                        evaDtsImportFolder.getFieldIDContent(), queries, rifornimentiGiaAssegnati);
                if (rifornimento == null) {
                    throw new ImportazioneEvaDtsException(
                            "Impossibile recuperare il rifornimento da associare alla rilevazione.");
                }
                rifornimentiGiaAssegnati.add(rifornimento);
                rilevazioniImport[i].setAreaRifornimento(rifornimento);
                rilevazioniOk.add(rilevazioniImport[i]);
            } catch (ImportazioneEvaDtsException e) {
                LOGGER.error("--> Errore durante l'elaborazione della rilevazione.", e);
                result.aggiungiErrore(i, e.getMessage());
                rilevazioniKo.add(rilevazioniImport[i]);
            }
        }
        result.setNumeroRilevazioniPresenti(rilevazioniOk.size() + rilevazioniKo.size());
        result.setImportazioneForzata(forzaImportazione);

        salvaImportazione(forzaImportazione, rilevazioniOk, rilevazioniKo, reader.getFile(), fileName);

        LOGGER.debug("--> Exit importaEvaDts");
        return result;
    }

    private EnumMap<ImportazioniQueryType, Query> initImportQuery() {
        EnumMap<ImportazioniQueryType, Query> map = new EnumMap<>(ImportazioniQueryType.class);

        Query query = panjeaDAO.prepareQuery(
                "select distinct dist.id as id, dist.codice as codice from Distributore dist where dist.codice = :paramCodice",
                Distributore.class, null);
        map.put(ImportazioniQueryType.DISTRIBUTORE_BY_CODICE, query);

        query = panjeaDAO.prepareQuery(
                "select distinct dist.id as id, dist.codice as codice from Distributore dist where dist.datiVending.idEvaDts = :paramIdEvaDts",
                Distributore.class, null);
        map.put(ImportazioniQueryType.DISTRIBUTORE_BY_IDEVADTS, query);

        // @formatter:off
        query = panjeaDAO.prepareQuery(
                "select distinct inst.id as id "
                + "from RigaInstallazione ri inner join ri.installazione inst "
                + "where ri.tipoMovimento = 'RITIRO' "
                + "and inst.articolo = :paramIdDistributore "
                + "order by ri.areaInstallazione.documento.dataDocumento desc",
                Installazione.class, null);
        // @formatter:on
        query.setMaxResults(1);
        map.put(ImportazioniQueryType.INSTALLAZIONE_ULTIMO_RITIRO, query);

        // @formatter:off
        query = panjeaDAO.prepareQuery(
                "select distinct ar.id as id, ar.version as version "
                + "from AreaRifornimento ar inner join ar.installazione inst left join ar.rilevazioneEvaDts ril "
                + "where ar.datiVendingArea.dataInizioIntervento = :paramData "
                + "and inst.id = :paramIdInstallazione "
                + "and ril is null",
                AreaRifornimento.class, null);
        // @formatter:on
        map.put(ImportazioniQueryType.RIF_INSTALLAZIONE_E_DATA, query);

        // @formatter:off
        query = panjeaDAO.prepareQuery(
                "select distinct ar.id as id, ar.version as version, ar.datiVendingArea.dataInizioIntervento as datiVendingArea$dataInizioIntervento "
                + "from AreaRifornimento ar inner join ar.installazione inst left join ar.rilevazioneEvaDts ril "
                + "where ar.datiVendingArea.dataInizioIntervento = :paramData "
                + "and inst.id = :paramIdInstallazione "
                + "and (ar.incasso >= :paramIncassoMin and ar.incasso <= :paramIncassoMax) "
                + "and ril is null "
                + "order by ar.datiVendingArea.dataInizioIntervento ",
                AreaRifornimento.class, null);
        // @formatter:on
        map.put(ImportazioniQueryType.RIF_INCASSO_SIMILE, query);

        // @formatter:off
        query = panjeaDAO.prepareQuery(
                "select distinct ar.id as id, ar.version as version "
                + "from AreaRifornimento ar inner join ar.installazione inst left join ar.rilevazioneEvaDts ril "
                + "where inst.id = :paramIdInstallazione "
                + "and (ar.incasso is null or ar.incasso = 0) "
                + "and (ar.datiVendingArea.dataInizioIntervento >= :paramDataMin and ar.datiVendingArea.dataInizioIntervento <= :paramDataMax) "
                + "and ril is null "
                + "order by ar.datiVendingArea.dataInizioIntervento asc",
                AreaRifornimento.class, null);
        // @formatter:on
        map.put(ImportazioniQueryType.RIF_SENZA_INCASSO, query);

        return map;
    }

    private void pubblica(String fileName, File fileDaPubblicare) {
        try {
            String sid = dmsSecurityManager.login();
            WSFolder folder = getTrasmissioniFolder(sid);
            WSDocument wsDoc = new WSDocument();
            wsDoc.setId(0L);
            wsDoc.setFolderId(folder.getId());
            wsDoc.setTitle(fileName);
            wsDoc.setFileName(fileName);
            DocumentClient documentiService = dmsLookupClientWebService.creaDocumentService();
            documentiService.create(sid, wsDoc, new DataHandler(new FileDataSource(fileDaPubblicare)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void salvaImportazione(boolean forzaImportazione, List<RilevazioneEvaDts> rilevazioniOk,
            List<RilevazioneEvaDts> rilevazioniKo, File file, String fileName) {

        if (rilevazioniKo.isEmpty() || forzaImportazione) {
            for (RilevazioneEvaDts rilevazioneEvaDts : rilevazioniOk) {
                // se è stato creato un nuovo rifornimento lo salvo
                if (rilevazioneEvaDts.getAreaRifornimento().isNew()) {
                    try {
                        AreaMagazzino am = areaMagazzinoManager
                                .salvaAreaMagazzino(rilevazioneEvaDts.getAreaRifornimento().getAreaMagazzino(), true);
                        rilevazioneEvaDts.getAreaRifornimento().setAreaMagazzino(am);
                        AreaRifornimento rif = panjeaDAO.save(rilevazioneEvaDts.getAreaRifornimento());
                        rilevazioneEvaDts.setAreaRifornimento(rif);
                    } catch (Exception e) {
                        LOGGER.error("--> errore durante il salvataggio del rifornimento.", e);
                    }
                }
                rilevazioniEvaDtsManager.salva(rilevazioneEvaDts);
            }
            if (CollectionUtils.isNotEmpty(rilevazioniOk)) {
                pubblica(fileName, file);
            }
        }

        // se forzo l'importazione e ci sono delle rilevazioni non valide le metto su un file di errore e la pubblico
        if (!rilevazioniKo.isEmpty() && forzaImportazione) {
            List<String> lines = new ArrayList<>();
            for (RilevazioneEvaDts rilevazione : rilevazioniKo) {
                lines.addAll(rilevazione.getRigheFileImport());
            }

            File fileKo = null;
            try {
                fileKo = File.createTempFile("letturaEvaDtsOk", ".tmp");
                Path fileKoPath = Paths.get(fileKo.getAbsolutePath());
                Files.write(fileKoPath, lines, StandardCharsets.UTF_8);

                pubblica(fileName + ".err", fileKo);
            } catch (Exception e) {
                LOGGER.error("--> Errore durante il salvataggio del file contenente le rilevazioni con errori "
                        + fileName + ".err", e);
            }
        }
    }

}
