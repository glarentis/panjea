package it.eurotn.panjea.magazzino.manager;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.panjea.magazzino.domain.RigaInventarioArticolo;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.GestioneInventarioArticoloDocumentoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.GestioneInventarioArticoloManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.manager.inventari.FileInventarioArticoliProcessor;
import it.eurotn.panjea.magazzino.manager.inventari.FileInventarioArticoliProcessorFactory;
import it.eurotn.panjea.magazzino.manager.inventari.ImportazioneArticoliInventarioQueryBuilder;
import it.eurotn.panjea.magazzino.manager.sqlbuilder.InventarioArticoloQueryBuilder;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.InventarioArticoloDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.XlsUtils;

@Stateless(name = "Panjea.GestioneInventarioArticoloManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GestioneInventarioArticoloManager")
public class GestioneInventarioArticoloManagerBean implements GestioneInventarioArticoloManager {

    private class SqlExecuter implements Work {
        private String sql;

        @Override
        public void execute(Connection connection) throws SQLException {
            CallableStatement updateStatements = connection.prepareCall(sql);
            updateStatements.executeUpdate();
            updateStatements.close();
        }

        /**
         * @param sql
         *            The sql to set.
         * @uml.property name="sql"
         */
        public void setSql(String sql) {
            this.sql = sql;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(GestioneInventarioArticoloManagerBean.class);

    private static Logger logger = Logger.getLogger(GestioneInventarioArticoloManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @EJB
    private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private ArticoloManager articoloManager;

    @EJB
    private GestioneInventarioArticoloDocumentoManager gestioneInventarioArticoloDocumentoManager;

    @EJB
    private AreaMagazzinoCancellaManager areaMagazzinoCancellaManager;

    @EJB
    private DepositiManager depositiManager;

    /**
     * Aggiorna gli importi delle righe delle aree di riferimento all'ultimo costo dell'articolo.
     *
     * @param areeMagazzino
     *            aree di riferimento
     * @param deposito
     *            deposito
     */
    private void aggiornaPrezziImportazione(List<AreaMagazzino> areeMagazzino, DepositoLite deposito) {

        ImportazioneArticoliInventarioQueryBuilder builder = new ImportazioneArticoliInventarioQueryBuilder(null, null,
                null);

        try {
            // aggiorno le righe delle aree magazzino interessate
            List<Integer> idAree = new ArrayList<Integer>();
            for (AreaMagazzino areaMagazzino : areeMagazzino) {
                idAree.add(areaMagazzino.getId());
            }
            String sqlUpdate = builder.getSqlUpdateAree(idAree, deposito.getId());

            SqlExecuter executer = new SqlExecuter();
            executer.setSql(sqlUpdate);
            ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
        } catch (Exception e) {
            logger.error("--> errore durante l'aggiornamento dei prezzi articoli", e);
            throw new RuntimeException("errore durante l'aggiornamento dei prezzi articoli", e);
        }
    }

    /**
     * Aggiorna gli importi delle righe delle aree di riferimento all'ultimo costo dell'articolo.
     *
     * @param areeMagazzino
     *            aree di riferimento
     * @param dataInventario
     *            data di riferimento
     * @param deposito
     *            deposito
     */
    private void aggiornaUltimoCosto(List<AreaMagazzino> areeMagazzino, Date dataInventario, DepositoLite deposito) {

        ParametriRicercaValorizzazione parametri = new ParametriRicercaValorizzazione();
        parametri.setConsideraGiacenzaZero(true);
        parametri.setData(dataInventario);
        MagazzinoValorizzazioneDepositoUltimoCostoAziendaManagerBean builder = new MagazzinoValorizzazioneDepositoUltimoCostoAziendaManagerBean();
        String sqlVal = builder.getSqlString(parametri, deposito.getId());

        // aggiorno le righe delle aree magazzino interessate
        List<Integer> idAree = new ArrayList<Integer>();
        for (AreaMagazzino areaMagazzino : areeMagazzino) {
            idAree.add(areaMagazzino.getId());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("update maga_righe_magazzino riga inner join (");
        sb.append(sqlVal);
        sb.append(") as val on riga.articolo_id = val.idArticolo ");
        sb.append("set riga.importoInValutaNetto = val.costo, ");
        sb.append(
                "    riga.importoInValutaAziendaNetto = round(val.costo/riga.tassoDiCambioNetto,riga.numeroDecimaliPrezzo), ");
        sb.append("    riga.importoInValuta = val.costo, ");
        sb.append("    riga.importoInValutaAzienda = round(val.costo/riga.tassoDiCambio,riga.numeroDecimaliPrezzo), ");
        sb.append("    riga.importoInValutaTotale = val.costo*riga.qta, ");
        sb.append("    riga.importoInValutaAziendaTotale = round(val.costo*riga.qta/riga.tassoDiCambioTotale,2) ");
        sb.append("where ");
        sb.append(" riga.areaMagazzino_id in ( ");
        sb.append(StringUtils.join(idAree, ","));
        sb.append(" )");

        try {
            SqlExecuter executer = new SqlExecuter();
            executer.setSql(sb.toString());
            ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void avvaloraGiacenzaRealeInventarioArticolo(Date data, DepositoLite deposito) {

        // carico l'inventario articolo
        List<InventarioArticolo> inventari = caricaInventarioArticolo(data, deposito, false);

        int countRigheForFlush = 0;

        // aggiorno la giacenza di ogni inventario
        for (InventarioArticolo inventarioArticolo : inventari) {

            // copio solo le quantità positive e gli inventari che non hanno delle rilevazioni
            if (new Double(0.0).compareTo(inventarioArticolo.getGiacenzaCalcolata()) < 0
                    && inventarioArticolo.getNumeroRighe() == 0) {
                RigaInventarioArticolo rigaInventarioArticolo = new RigaInventarioArticolo();
                rigaInventarioArticolo.setData(Calendar.getInstance().getTime());
                rigaInventarioArticolo.setInventarioArticolo(inventarioArticolo);
                rigaInventarioArticolo.setQuantita(BigDecimal.valueOf(inventarioArticolo.getGiacenzaCalcolata())
                        .subtract(inventarioArticolo.getGiacenzaReale()));

                try {
                    panjeaDAO.saveWithoutFlush(rigaInventarioArticolo);
                } catch (Exception e) {
                    logger.error("--> errore durante il salvataggio della riga inventario articolo", e);
                    throw new RuntimeException("errore durante il salvataggio della riga inventario articolo", e);
                }

                countRigheForFlush++;

                // faccio il flush ogni 200 aree create
                if (countRigheForFlush % 200 == 0) {
                    panjeaDAO.getEntityManager().flush();
                }
            }
        }
        panjeaDAO.getEntityManager().flush();
    }

    @Override
    public void cancellaInventarioArticolo(Date data, DepositoLite deposito) {
        logger.debug("--> Enter cancellaInventarioArticolo");

        gestioneInventarioArticoloDocumentoManager.cancellaInventarioArticolo(data, deposito);

        logger.debug("--> Exit cancellaInventarioArticolo");
    }

    @Override
    public List<Deposito> caricaDepositiPerInventari() {

        // carico tutti i depositi
        List<Deposito> depositi = depositiManager.caricaDepositi();

        // carico gli inventari presenti
        List<InventarioArticoloDTO> inventari = caricaInventariiArticoli();

        // tolgo i depositi già presenti
        for (InventarioArticoloDTO inventarioArticoloDTO : inventari) {
            Deposito deposito = inventarioArticoloDTO.getDeposito().creaProxy();
            depositi.remove(deposito);
        }

        // Rimuovo i depositi non abilitati
        CollectionUtils.filter(depositi, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                Deposito d = (Deposito) arg0;
                return d.getAttivo();
            }
        });

        return depositi;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InventarioArticoloDTO> caricaInventariiArticoli() {

        List<InventarioArticoloDTO> inventari = new ArrayList<InventarioArticoloDTO>();

        Query query = panjeaDAO.prepareNamedQuery("InventarioArticolo.caricaAllDTO");
        try {
            inventari = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento degli inventari.", e);
            throw new RuntimeException("Errore durante il caricamento degli inventari.", e);
        }

        return inventari;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InventarioArticolo> caricaInventarioArticolo(Date date, DepositoLite depositoLite,
            boolean caricaGiacenzeAZero) {

        List<InventarioArticolo> inventari = new ArrayList<InventarioArticolo>();

        StringBuilder sb = new StringBuilder();
        sb.append(
                "select distinct inv.id as id,inv.version as version,inv.data as data, art.id as idArticolo, art.codice as codiceArticolo, art.descrizioneLinguaAziendale as descrizioneArticolo,cat.id as idCategoria,cat.codice as codiceCategoria, cat.descrizioneLinguaAziendale as descrizioneCategoria, ");
        sb.append(
                "inv.giacenzaCalcolata as giacenzaCalcolata,count(righe.id) as numeroRighe, coalesce(sum(righe.quantita),0) as qtaRighe ");
        sb.append(
                "from InventarioArticolo inv inner join inv.articolo art inner join art.categoria cat left join inv.righeInventarioArticolo righe ");
        sb.append(
                "where inv.data = :data and inv.deposito.id = :deposito and (inv.giacenzaCalcolata != 0 or :tutteGiacenza = true) ");
        sb.append("group by inv.id ");
        sb.append("order by inv.articolo.codice");

        org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(InventarioArticolo.class));

        query.setParameter("data", date);
        query.setParameter("deposito", depositoLite.getId());
        query.setParameter("tutteGiacenza", caricaGiacenzeAZero);

        inventari = query.list();

        return inventari;
    }

    @Override
    public InventarioArticolo caricaInventarioArticolo(InventarioArticolo inventarioArticolo) {
        logger.debug("--> Enter caricaInventarioArticolo");

        InventarioArticolo inventarioArticoloSalvato = null;
        try {
            inventarioArticoloSalvato = panjeaDAO.load(InventarioArticolo.class, inventarioArticolo.getId());
        } catch (Exception e) {
            logger.error("--> errore durante il caricamanto dell'inventario articolo", e);
            throw new RuntimeException("errore durante il caricamanto dell'inventario articolo", e);
        }

        logger.debug("--> Exit caricaInventarioArticolo");
        return inventarioArticoloSalvato;
    }

    /**
     * Verifica che tutti i tipi documento per la generazione dell'inventario siano configurati (inventario, rettifiche
     * positiche e negative) e li restituisce.
     *
     * @return mappa contenente i tipi area magazzino per la generazione dei movimenti
     * @throws TipoDocumentoBaseException
     *             sollevata se almento un tipi documento non è configurato.
     */
    private Map<String, TipoAreaMagazzino> caricaTipiDocumentoPerGenerazioneInventrio()
            throws TipoDocumentoBaseException {

        Map<String, TipoAreaMagazzino> mapResult = new HashMap<String, TipoAreaMagazzino>();

        // verifica rettifiche positive
        TipoDocumentoBaseMagazzino rettificaPositiva;
        try {
            rettificaPositiva = tipiAreaMagazzinoManager
                    .caricaTipoDocumentoBase(TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA);
            mapResult.put(TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA.name(),
                    rettificaPositiva.getTipoAreaMagazzino());
        } catch (ObjectNotFoundException e) {
            rettificaPositiva = null;
        }

        // verifica rettifiche negative
        TipoDocumentoBaseMagazzino rettificaNegativa;
        try {
            rettificaNegativa = tipiAreaMagazzinoManager
                    .caricaTipoDocumentoBase(TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA);
            mapResult.put(TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA.name(),
                    rettificaNegativa.getTipoAreaMagazzino());
        } catch (ObjectNotFoundException e) {
            rettificaNegativa = null;
        }

        // verifica inventario
        TipoAreaMagazzino tipoAreaInventario = tipiAreaMagazzinoManager.caricaTipoAreaMagazzinoInventario();
        if (tipoAreaInventario != null) {
            mapResult.put("Inventario", tipoAreaInventario);
        }

        List<String> tipiDocAssenti = new ArrayList<String>();

        if (tipoAreaInventario == null) {
            tipiDocAssenti.add("Tipo documento inventario");
        }
        if (rettificaPositiva == null) {
            tipiDocAssenti.add("Tipo operazione " + TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA.name());
        }
        if (rettificaNegativa == null) {
            tipiDocAssenti.add("Tipo operazione " + TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA.name());
        }

        if (!tipiDocAssenti.isEmpty()) {
            throw new TipoDocumentoBaseException(tipiDocAssenti.toArray(new String[tipiDocAssenti.size()]));
        }

        return mapResult;
    }

    private AreaMagazzino creaAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino, Date dataMovimento,
            DepositoLite deposito, List<InventarioArticolo> inventariArticolo, boolean rettifica,
            List<Articolo> articoli) {
        AreaMagazzino areaMagazzino = gestioneInventarioArticoloDocumentoManager.creaAreaMagazzino(tipoAreaMagazzino,
                dataMovimento, deposito);

        String linguaAzienda = aziendeManager.caricaAzienda().getLingua();

        // creo le righe articolo con gli inventari articolo
        Articolo articoloRicerca = new Articolo();
        System.err.println("CREO LE RIGHE");
        for (InventarioArticolo inventarioArticolo : inventariArticolo) {
            articoloRicerca.setId(inventarioArticolo.getArticolo().getId());
            double qta = 0.0;
            if (rettifica) {
                qta = inventarioArticolo.getRettifica().abs().doubleValue();
            } else {
                qta = inventarioArticolo.getGiacenzaReale().doubleValue();
            }

            gestioneInventarioArticoloDocumentoManager.creaRigaArticolo(areaMagazzino,
                    articoli.get(articoli.indexOf(articoloRicerca)), qta, linguaAzienda);
        }

        System.err.println("FINE RIGHE");

        return areaMagazzino;
    }

    @Override
    public void creaInventariArticolo(Date data, List<DepositoLite> depositi) {

        try {
            InventarioArticoloQueryBuilder queryBuilder = new InventarioArticoloQueryBuilder(getAzienda());
            for (DepositoLite deposito : depositi) {
                String query = queryBuilder.getSqlString((Integer) null, deposito.getId(), data);

                panjeaDAO.executeQuery(panjeaDAO.getEntityManager().createNativeQuery(query));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public List<AreaMagazzinoRicerca> generaInventario(Date dataInventario, Date dataInventarioArticolo,
            DepositoLite deposito) throws TipoDocumentoBaseException {

        // verifico che tutti i tipi documento siano configurati
        Map<String, TipoAreaMagazzino> mapTipiAreee = caricaTipiDocumentoPerGenerazioneInventrio();

        // carico l'inventario articolo
        List<InventarioArticolo> inventarioArticolo = caricaInventarioArticolo(dataInventarioArticolo, deposito, true);

        Map<TipoOperazioneTipoDocumento, List<InventarioArticolo>> mapRettifiche = new HashMap<TipoOperazioneTipoDocumento, List<InventarioArticolo>>();
        mapRettifiche.put(TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA, new ArrayList<InventarioArticolo>());
        mapRettifiche.put(TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA, new ArrayList<InventarioArticolo>());

        List<InventarioArticolo> righeInventario = new ArrayList<InventarioArticolo>();

        for (InventarioArticolo inv : inventarioArticolo) {
            BigDecimal rettifica = inv.getRettifica();

            if (BigDecimal.ZERO.compareTo(rettifica) > 0) {
                List<InventarioArticolo> list = mapRettifiche.get(TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA);
                list.add(inv);
                mapRettifiche.put(TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA, list);
            } else if (BigDecimal.ZERO.compareTo(rettifica) < 0) {
                List<InventarioArticolo> list = mapRettifiche.get(TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA);
                list.add(inv);
                mapRettifiche.put(TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA, list);
            }

            if (BigDecimal.ZERO.compareTo(inv.getGiacenzaReale()) != 0) {
                righeInventario.add(inv);
            }
        }

        List<Articolo> articoli = articoloManager.caricaArticoliFull();

        List<AreaMagazzino> areeGenerate = new ArrayList<AreaMagazzino>();

        // calcolo la data delle rettifiche ( data inventario - 1 giorno )
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataInventario);
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        Date dataRettifiche = calendar.getTime();

        List<AreaMagazzinoRicerca> areeRicerca = new ArrayList<AreaMagazzinoRicerca>();

        try {
            AreaMagazzino area;
            // genero la rettifica positiva
            if (mapRettifiche.get(TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA).size() > 0) {
                TipoAreaMagazzino tipoAreaRettPositiva = mapTipiAreee
                        .get(TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA.name());
                List<InventarioArticolo> inventariRettPositive = mapRettifiche
                        .get(TipoOperazioneTipoDocumento.RETTIFICA_POSITIVA);
                area = creaAreaMagazzino(tipoAreaRettPositiva, dataRettifiche, deposito, inventariRettPositive, true,
                        articoli);
                areeGenerate.add(area);
            }

            // genero la rettifica negativa
            if (mapRettifiche.get(TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA).size() > 0) {
                TipoAreaMagazzino tipoAreaRettNegativa = mapTipiAreee
                        .get(TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA.name());
                List<InventarioArticolo> inventariRettNegative = mapRettifiche
                        .get(TipoOperazioneTipoDocumento.RETTIFICA_NEGATIVA);
                area = creaAreaMagazzino(tipoAreaRettNegativa, dataRettifiche, deposito, inventariRettNegative, true,
                        articoli);
                areeGenerate.add(area);
            }

            TipoAreaMagazzino tipoAreaInv = mapTipiAreee.get("Inventario");
            area = creaAreaMagazzino(tipoAreaInv, dataInventario, deposito, righeInventario, false, articoli);
            areeGenerate.add(area);

            // carico l'ultimo costo degli articoli per applicarlo alle righe delle
            // aree generate
            aggiornaUltimoCosto(areeGenerate, dataInventario, deposito);

            aggiornaPrezziImportazione(areeGenerate, deposito);

            // totalizzo le aree generate
            areeRicerca = gestioneInventarioArticoloDocumentoManager.totalizzaAreeMagazzino(areeGenerate);
            // cancello l'inventario articolo
            cancellaInventarioArticolo(dataInventarioArticolo, deposito);
        } catch (Exception e) {
            logger.error("-->errore durante la creazione dell'inventario", e);
            for (AreaMagazzino areaMagazzino : areeGenerate) {
                try {
                    areaMagazzinoCancellaManager.cancellaAreaMagazzino(areaMagazzino, true, true);
                } catch (DocumentiCollegatiPresentiException e1) {
                    // non faccio niente perchè forzo la cancellazione
                    if (logger.isDebugEnabled()) {
                        logger.debug("--> e1");
                    }
                } catch (AreeCollegatePresentiException e1) {
                    // non faccio niente perchè forzo la cancellazione
                    if (logger.isDebugEnabled()) {
                        logger.debug("--> e1");
                    }
                }
            }
        }

        return areeRicerca;
    }

    /**
     *
     * @return codice Azienda loggata
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) sessionContext.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> importaArticoliInventario(byte[] fileImportData, Integer idDeposito) {

        List<String> articoliMancanti = new ArrayList<String>();

        // creo il file temporaneo per l'importazione degli articoli
        String fileImportPath = "";
        try {
            File fileImport = null;

            XlsUtils xlsUtils = null;
            xlsUtils = new XlsUtils(fileImportData);
            if (xlsUtils.isWorkbook()) {
                fileImport = xlsUtils.getCSV("\t");
            } else {
                fileImport = File.createTempFile("fileImport" + UUID.randomUUID(), ".csv");
                FileUtils.writeByteArrayToFile(fileImport, fileImportData);
            }

            fileImportPath = fileImport.getAbsolutePath();

            // il file viene modificato per poter essere importato sempre nello stesso formato
            FileInventarioArticoliProcessor processor = FileInventarioArticoliProcessorFactory.getProcessor(fileImport);
            if (processor != null) {
                processor.process(fileImport);
            } else {
                throw new GenericException("File di importazione non valido.");
            }

            fileImportPath = fileImportPath.replaceAll("\\\\", "/");
            System.err.println("File import: " + fileImportPath);
        } catch (IOException e) {
            logger.error("--> errore durante la creazione del file di importazione.", e);
            throw new RuntimeException("errore durante la creazione del file di importazione.", e);
        }

        ImportazioneArticoliInventarioQueryBuilder builder = new ImportazioneArticoliInventarioQueryBuilder(
                fileImportPath, idDeposito, getPrincipal().getUserName());
        Collection<String> prepareSqls = builder.getSqlPrepareData();
        String sqlDrop = builder.getSqlDrop();
        String sqlArticoliMancanti = builder.getSqlArticoliInventarioMancanti();

        try {
            SqlExecuter executer = new SqlExecuter();
            for (String stringSQL : prepareSqls) {
                System.err.println("SQL: " + stringSQL);
                executer.setSql(stringSQL);
                ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
            }

            // verifico se ci sono articoli presenti nel file e non in panjea
            Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlArticoliMancanti);
            articoliMancanti = query.getResultList();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        } finally {
            // drop della tabella temporanea
            SqlExecuter executer = new SqlExecuter();
            executer.setSql(sqlDrop);
            ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
        }

        return articoliMancanti;
    }

    @Override
    public InventarioArticolo salvaInventarioArticolo(InventarioArticolo inventarioArticolo) {
        logger.debug("--> Enter salvaInventarioArticolo");

        try {
            inventarioArticolo = panjeaDAO.save(inventarioArticolo);
            if (inventarioArticolo.getArticolo().getCategoria() != null) {
                inventarioArticolo.getArticolo().getCategoria().getId();
            }
        } catch (DAOException e) {
            logger.error("--> errore durante il salvataggio dell'inventario articolo.", e);
            throw new RuntimeException("errore durante il salvataggio dell'inventario articolo.", e);
        }

        logger.debug("--> Exit salvaInventarioArticolo");
        return inventarioArticolo;
    }

}
