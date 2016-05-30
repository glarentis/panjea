package it.eurotn.panjea.magazzino.manager;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.dms.exception.DmsException;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoArticolo;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoEntita;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoPanjea;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoTipoDocumento;
import it.eurotn.panjea.dms.manager.interfaces.DMSAttributiManager;
import it.eurotn.panjea.dms.mdb.AttributoAggiornamentoDTO.TipoAttributo;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.AbstractDataWarehouseMovimentiMagazzinoSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.AbstractDataWarehouseMovimentiMagazzinoSqlBuilder.TipoFiltro;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseArticoliSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseCaricoDistintaSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseCaricoSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseCaricoTrasferimentoSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseCaricoValoreSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseDepositiSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseFatturatoSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseScaricoDistintaSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseScaricoSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseScaricoTrasferimentoSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseScaricoValoreSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseSediEntitaSqlBuilder;
import it.eurotn.panjea.magazzino.manager.interfaces.DataWarehouseManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.DataWarehouseManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DataWarehouseManager")
public class DataWarehouseManagerBean implements DataWarehouseManager {

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
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

    private static final Logger LOGGER = Logger.getLogger(DataWarehouseManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DMSAttributiManager dmsAttributiManager;

    /**
     * Cancella i movimenti dal datawarehouse per la sincronizzazione.
     *
     * @param dateInMillisecond
     *            data utilizzata per aggiornare.
     * @return true se la tabella è stata ricreata, false se sono state eseguite solamente le cancellazioni.
     */
    private boolean cancellaMovimenti(Long dateInMillisecond) {

        boolean result = false;
        // se il primo movimento modificato ha una data successiva a quella
        // richiesta
        // significa che devo reinserire tutti i documenti. Quindi eseguo
        // direttamente il truncate della tabella
        StringBuilder sb = new StringBuilder();
        sb.append("select TIMESTAMP ");
        sb.append("from maga_area_magazzino ");
        sb.append("  order by timeStamp LIMIT 1 ");
        Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        BigInteger firstTimeStamp = null;
        try {
            firstTimeStamp = (BigInteger) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // se non ho risultati non faccio nulla (tabele aree magazzino
            // vuota)
            LOGGER.warn("nessuna area magazzino trovata", e);
        } catch (Exception e) {
            throw new GenericException("errore nel cancellare i mov dal datawarehouse", e);
        }
        if (firstTimeStamp == null) {
            firstTimeStamp = new BigInteger(Long.toString(Long.MAX_VALUE));
        }

        if (firstTimeStamp.compareTo(new BigInteger(dateInMillisecond.toString())) > 0) {
            creaTabellaMovimentazione();
            result = true;
        } else {
            // cancello le righe che non sono state cancellate dal datawarehouse
            // o che hanno lo stato del documento a provvisorio o fatturazione
            sb = new StringBuilder();
            sb.append("delete dw from dw_movimentimagazzino dw ");
            sb.append("inner join maga_righe_magazzino rm on dw.idRiga=rm.id ");
            sb.append("left join maga_area_magazzino am on am.id=rm.areaMagazzino_id ");
            sb.append("where rm.id is null or am.statoAreaMagazzino=1 or am.statoAreaMagazzino=3 ");
            SqlExecuter sqlExecuter = new SqlExecuter();
            sqlExecuter.setSql(sb.toString());
            ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

            // Cancello le righe che devo andare a ricreare
            sb = new StringBuilder();
            sb.append("delete dw from dw_movimentimagazzino dw ");
            sb.append("inner join maga_area_magazzino am on am.id=dw.areaMagazzino_id ");
            sb.append("where am.timeStamp>=");
            sb.append(dateInMillisecond);
            sqlExecuter.setSql(sb.toString());
            ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
        }
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void cancellaMovimenti(String uuid) {
        Query query = panjeaDAO.getEntityManager().createNativeQuery(
                "delete dw from dw_movimentimagazzino dw inner join maga_area_magazzino area on dw.areaMagazzino_id = area.id where area.uUIDContabilizzazione = :uuid");
        query.setParameter("uuid", uuid);
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new DmsException(e);
        }
    }

    /**
     * Crea la tabella dei movimenti.
     */
    private void creaTabellaMovimentazione() {
        SqlExecuter sqlExecuter = new SqlExecuter();
        sqlExecuter.setSql("drop table if exists dw_movimentimagazzino;");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        StringBuilder sb = new StringBuilder();
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
        sb.append("CREATE TABLE `dw_movimentimagazzino` ( ");
        sb.append("	`codiceAzienda` CHAR(10) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci', ");
        sb.append("	`tipoDocumentoCodice` CHAR(18) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci', ");
        sb.append("	`tipoDocumentoDescrizione` CHAR(60) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci', ");
        sb.append("	`classeTipoDocumento` CHAR(81) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci', ");
        sb.append("	`notaCreditoEnable` BIT(1) NULL DEFAULT NULL, ");
        sb.append("	`dataDocumento` DATE NOT NULL, ");
        sb.append("	`numeroDocumento` CHAR(30) NULL DEFAULT NULL, ");
        sb.append("	`numeroDocumentoOrder` CHAR(60) NULL DEFAULT NULL, ");
        sb.append("	`dataRegistrazione` DATE NOT NULL, ");
        sb.append("	`deposito_id` INT(11) NOT NULL, ");
        sb.append("	`listinoAlternativo` CHAR(20) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci', ");
        sb.append("	`listino` CHAR(20) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci', ");
        sb.append("	`sedeEntita_id` INT(11) NULL DEFAULT NULL, ");
        sb.append("	`tipoOperazione` TINYINT(4) NULL DEFAULT NULL, ");
        sb.append("	`addebitoSpeseIncasso` BIT(1) NULL DEFAULT NULL, ");
        sb.append("	`idRiga` INT(11) NOT NULL, ");
        sb.append("	`importoCarico` DECIMAL(19,6) NULL DEFAULT NULL, ");
        sb.append("	`importoScarico` DECIMAL(19,6) NULL DEFAULT NULL, ");
        sb.append("	`qtaCarico` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`qtaScarico` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`articoloLibero` BIT(1) NULL DEFAULT NULL, ");
        sb.append("	`unitaMisura` CHAR(10) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci', ");
        sb.append("	`sezioneTipoMovimento` CHAR(15) NULL DEFAULT NULL, ");
        sb.append("	`tipoMovimento` TINYINT(4) NULL DEFAULT NULL, ");
        sb.append("	`articolo_id` INT(11) NOT NULL, ");
        sb.append("	`areaMagazzino_id` INT(11) NULL DEFAULT NULL, ");
        sb.append("	`qtaCaricoAltro` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`qtaScaricoAltro` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`importoCaricoAltro` DECIMAL(19,6) NULL DEFAULT NULL, ");
        sb.append("	`importoScaricoAltro` DECIMAL(19,6) NULL DEFAULT NULL, ");
        sb.append("	`tipoMovimentoOriginale` TINYINT(4) NULL DEFAULT NULL, ");
        sb.append("	`qtaMagazzinoCarico` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`qtaMagazzinoScarico` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`qtaMagazzinoCaricoAltro` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`qtaMagazzinoScaricoAltro` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`annoMovimento` SMALLINT(6) NOT NULL, ");
        sb.append("	`qtaFatturatoCarico` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`qtaFatturatoScarico` DOUBLE NULL DEFAULT NULL, ");
        sb.append("	`importoFatturatoCarico` DECIMAL(19,6) NULL DEFAULT NULL, ");
        sb.append("	`importoFatturatoScarico` DECIMAL(19,6) NULL DEFAULT NULL, ");
        sb.append("	`sezioneTipoMovimentoValore` TINYINT(4) NULL DEFAULT NULL, ");
        sb.append("	`tipoDocumentoId` INT(11) NULL DEFAULT NULL, ");
        sb.append("	`agente_Id` INT(11) NULL DEFAULT NULL, ");
        sb.append("	`percProvvigione` DECIMAL(5,2) NULL DEFAULT NULL, ");
        sb.append("	`importoProvvigione` DECIMAL(19,6) NULL DEFAULT NULL, ");
        sb.append("	`omaggio` BIT(1) NULL DEFAULT b'0', ");
        sb.append("	`sedeVettore_id` INT(11) NULL DEFAULT NULL, ");
        sb.append("	`pesoLordo` DECIMAL(8,2) NULL DEFAULT NULL, ");
        sb.append("	`volume` DECIMAL(8,2) NULL DEFAULT NULL, ");
        sb.append("	`numeroColli` INT(11) NULL DEFAULT NULL, ");
        sb.append("	`aspettoEsteriore` VARCHAR(40) NULL DEFAULT NULL, ");
        sb.append("	`causaleTrasporto` VARCHAR(40) NULL DEFAULT NULL, ");
        sb.append("	`tipoPorto` VARCHAR(30) NULL DEFAULT NULL, ");
        sb.append("	`trasportoCura` VARCHAR(45) NULL DEFAULT NULL, ");
        sb.append("	`baseProvv` DECIMAL(19,6) NULL DEFAULT NULL ");
        sb.append(") ");
        sb.append("COMMENT='InnoDB' ");
        sb.append("COLLATE='utf8_general_ci' ");
        sb.append("ROW_FORMAT=COMPRESSED ");
        if (isMysqlVersionNext50()) {
            sb.append("PARTITION BY RANGE (YEAR(dataRegistrazione)) ");
            sb.append("(PARTITION base VALUES LESS THAN (0) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2008 VALUES LESS THAN (2009) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2009 VALUES LESS THAN (2010) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2010 VALUES LESS THAN (2011) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2011 VALUES LESS THAN (2012) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2012 VALUES LESS THAN (2013) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2013 VALUES LESS THAN (2014) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2014 VALUES LESS THAN (2015) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2015 VALUES LESS THAN (2016) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2016 VALUES LESS THAN (2017) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2017 VALUES LESS THAN (2018) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2018 VALUES LESS THAN (2019) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2019 VALUES LESS THAN (2020) ENGINE = InnoDB, ");
            sb.append(" PARTITION P2020 VALUES LESS THAN (2021) ENGINE = InnoDB, ");
            sb.append(" PARTITION POTHER VALUES LESS THAN MAXVALUE ENGINE = InnoDB)");
        } else {
            sb.append(" ENGINE=Innodb ");
        }
        sqlExecuter.setSql(sb.toString());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
    }

    /**
     * Esegue i vari builder per inserire i movimenti nel datawarehouse.
     *
     * @param sqlbuilder
     *            sqlBuilder per costruire la query di inserimento
     * @param dateInMillisecond
     *            data in millisecondi per l' inserimento
     */
    private void executeBuilder(AbstractDataWarehouseMovimentiMagazzinoSqlBuilder sqlbuilder, Long dateInMillisecond) {

        String sql = sqlbuilder.getSql();
        sql = sql.replaceAll(":dateUpdate", dateInMillisecond.toString());

        SqlExecuter executer = new SqlExecuter();
        executer.setSql(sql);
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
    }

    /**
     *
     * @return codice dell'azienda loggata.
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     *
     * @return true se la versione di mysql è dopo la 5.0
     */
    private boolean isMysqlVersionNext50() {
        Query query = panjeaDAO.getEntityManager().createNativeQuery("select version()");
        String version = (String) query.getSingleResult();
        return !version.startsWith("5.0");
    }

    /**
     * Reindicizza la tabella dei movimenti.
     */
    private void reindexMovimenti() {
        StringBuilder sb = new StringBuilder("");
        sb.append("alter table dw_movimentimagazzino ");
        sb.append("  add PRIMARY KEY (`deposito_id`,`idRiga`, `dataRegistrazione`), ");
        sb.append("  add KEY `data` (`dataRegistrazione`), ");
        sb.append("  add KEY `articolo` (`articolo_id`), ");
        sb.append("  add KEY `tipoMovimento` (`tipoMovimento`), ");
        sb.append("  add KEY `sede_entita` (`sedeEntita_id`), ");
        sb.append("  add KEY `areaMagazzino` (`areaMagazzino_id`), ");
        sb.append("  add KEY `articoloDeposito` (`articolo_id`,`deposito_id`), ");
        sb.append("  add KEY `agente_id` (`agente_id`) ");

        SqlExecuter sqlExecuter = new SqlExecuter();
        sqlExecuter.setSql(sb.toString());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
    }

    @Override
    public void sincronizzaAnagrafiche() {
        LOGGER.debug("--> Sincronizzazione articoli");
        sincronizzaArticoli();

        LOGGER.debug("--> Sincronizzazione clienti");
        sincronizzaSediEntita();

        LOGGER.debug("--> Sincronizzazione depositi");
        sincronizzaDepositi();
    }

    /**
     * Cancella i dati della tabella dw_articoli e li reinserisce.
     */
    private void sincronizzaArticoli() {

        DataWarehouseArticoliSqlBuilder sqlBuilder = new DataWarehouseArticoliSqlBuilder(
                it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseArticoliSqlBuilder.TipoFiltro.NESSUNO);

        SqlExecuter sqlExecuter = new SqlExecuter();
        sqlExecuter.setSql(sqlBuilder.getSqlDelete());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter.setSql(sqlBuilder.getSqlInsert());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter.setSql(sqlBuilder.getSqlInserisciArticoloNonPresente(getAzienda()));
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
    }

    /**
     * Sincronizza gli attributi del DMS che fanno riferimento agli articoli.
     */
    private void sincronizzaArticoliDMS() {
        // Sincronizzo gli articoli nel dms
        // Li carico tutti e spedisco il messaggio per aggiornare/inserire
        try {
            Query query = panjeaDAO.prepareNamedQuery("Articolo.caricaCodiceDescrizione");
            @SuppressWarnings("unchecked")
            List<Object[]> datiArticoli = panjeaDAO.getResultList(query);
            List<AttributoAllegatoPanjea> attributi = new ArrayList<>(100);
            for (Object[] datiArticolo : datiArticoli) {
                attributi.add(new AttributoAllegatoArticolo((String) datiArticolo[0], (String) datiArticolo[1],
                        (int) datiArticolo[2], getAzienda()));
                if (attributi.size() == 100) {
                    dmsAttributiManager.aggiornaAttributi(TipoAttributo.ARTICOLO, attributi);
                    attributi = new ArrayList<>();
                }
                LOGGER.debug("--> messaggio spedito");
            }
            if (attributi.size() > 0) {
                dmsAttributiManager.aggiornaAttributi(TipoAttributo.ARTICOLO, attributi);
            }
        } catch (Exception e) {
            LOGGER.error("-->errore nello spedire il messaggio per l'aggiornamento in batch degli articoli dms", e);
            throw new GenericException(
                    "-->errore nello spedire il messaggio per l'aggiornamento in batch degli articoli dms", e);
        }
    }

    /**
     * Cancella i dati della tabella dw_depositi e li reinserisce.
     */
    private void sincronizzaDepositi() {
        DataWarehouseDepositiSqlBuilder sqlBuilder = new DataWarehouseDepositiSqlBuilder(
                it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseDepositiSqlBuilder.TipoFiltro.NESSUNO);

        SqlExecuter sqlExecuter = new SqlExecuter();
        sqlExecuter.setSql(sqlBuilder.getSqlDelete());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter.setSql(sqlBuilder.getSqlInsert());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
    }

    @Override
    public void sincronizzaDimensionedata() {
        SqlExecuter sqlExecuter = new SqlExecuter();
        sqlExecuter.setSql("truncate dw_dimensionedata");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.YEAR, -5);

        for (int i = 1; i < 4001; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "insert into dw_dimensionedata (DATA_ID,ANNO,QUADRIMESTRE,QUADRIM_NOME,MESE,MESE_NOME,SETTIMANA,GIORNO_SETTIMANA,GIORNO_SETTIMANA_NOME,GIORNO,contatore) ");
            sb.append("VALUES ");

            // elaboro i campi della data
            Date date = cal.getTime();
            int anno = cal.get(Calendar.YEAR);
            int mese = cal.get(Calendar.MONTH);
            int quadrimestre = (mese / 3) + 1;
            String quadName = "Q" + quadrimestre;
            String meseName = dateFormatSymbols.getShortMonths()[mese];

            int settimanaAnno = cal.get(Calendar.WEEK_OF_YEAR);
            int giornoSettimana = cal.get(Calendar.DAY_OF_WEEK);
            String giornoSettimanaName = dateFormatSymbols.getShortWeekdays()[giornoSettimana];
            int giorno = cal.get(Calendar.DAY_OF_MONTH);

            sb.append("('");
            sb.append(dateFormat.format(date));
            sb.append("',");
            sb.append(anno);
            sb.append(",");
            sb.append(quadrimestre);
            sb.append(",'");
            sb.append(quadName);
            sb.append("',");
            sb.append(mese + 1);
            sb.append(",'");
            sb.append(meseName.toUpperCase());
            sb.append("',");
            sb.append(settimanaAnno);
            sb.append(",");
            sb.append(giornoSettimana);
            sb.append(",'");
            sb.append(giornoSettimanaName.toUpperCase());
            sb.append("',");
            sb.append(giorno);
            sb.append(",");
            sb.append(i);
            sb.append(");");

            cal.add(Calendar.DATE, 1);

            sqlExecuter.setSql(sb.toString());
            ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 7200)
    public void sincronizzaDMS() {

        LOGGER.debug("--> Sincronizzazione articoli dms");
        sincronizzaArticoliDMS();

        LOGGER.debug("--> Sincronizzazione entità dms");
        sincronizzaEntitaDMS();

        LOGGER.debug("--> Sincronizzazione tipi documento dms");
        sincronizzaTipiDocumentoDMS();
    }

    /**
     * Sincronizza gli attributi dms che fanno riferimento alle entità.
     */
    private void sincronizzaEntitaDMS() {

        Query query = panjeaDAO.prepareQuery(
                "select e.codice,e.anagrafica.denominazione,e.class,e.id from Entita e where e.abilitato=true");
        try {
            List<AttributoAllegatoPanjea> attributi = new ArrayList<>();
            @SuppressWarnings("unchecked")
            List<Object[]> results = panjeaDAO.getResultList(query);
            for (Object[] entitaArray : results) {
                EntitaDocumento entita = new EntitaDocumento();
                entita.setTipoEntita((String) entitaArray[2]);
                entita.setCodice((Integer) entitaArray[0]);
                entita.setDescrizione((String) entitaArray[1]);
                entita.setId((Integer) entitaArray[3]);
                AttributoAllegatoEntita att = new AttributoAllegatoEntita(entita, getAzienda());
                attributi.add(att);

                if (attributi.size() % 100 == 0) {
                    dmsAttributiManager.aggiornaAttributi(TipoAttributo.ENTITA, attributi);
                    attributi.clear();
                }
            }

            if (!attributi.isEmpty()) {
                dmsAttributiManager.aggiornaAttributi(TipoAttributo.ENTITA, attributi);
            }
        } catch (DAOException e) {
            LOGGER.error("-->errore nel sincronizzare l'entita nel dms", e);
            throw new GenericException("-->errore nel sincronizzare l'entita nel dms", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void sincronizzaMovimenti(Date dataIniziale) {
        Calendar calendarDataIniziale = Calendar.getInstance();
        calendarDataIniziale.setTime(dataIniziale);
        Long dateInMillisecond = calendarDataIniziale.getTimeInMillis();

        boolean fullReindex = cancellaMovimenti(dateInMillisecond);
        // Carico qta e valore
        executeBuilder(new DataWarehouseCaricoSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);
        // Carico valore
        executeBuilder(new DataWarehouseCaricoValoreSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);
        // Scarico qta e valore
        executeBuilder(new DataWarehouseScaricoSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);
        // Scarico valore
        executeBuilder(new DataWarehouseScaricoValoreSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);
        // Traferimento
        executeBuilder(new DataWarehouseCaricoTrasferimentoSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);
        executeBuilder(new DataWarehouseScaricoTrasferimentoSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);
        // Fatturato
        executeBuilder(new DataWarehouseFatturatoSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);
        // carico produzione
        executeBuilder(new DataWarehouseCaricoDistintaSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);
        executeBuilder(new DataWarehouseScaricoDistintaSqlBuilder(TipoFiltro.TIMESTAMP), dateInMillisecond);

        // Carico le righe nella tabella dello storico
        // SqlExecuter sqlExecuter = new SqlExecuter();
        // sqlExecuter.setSql("insert into dw_movimentimagazzino select * from
        // dw_movimentimagazzino_storico");
        // ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        // Se ho eseguito il truncate della tabella faccio un reindex
        if (fullReindex) {
            reindexMovimenti();
        }

    }

    /**
     * Sincronizza la tabella dw_sedientita cancellando tutti i dati e reinserendoli.
     */
    private void sincronizzaSediEntita() {
        DataWarehouseSediEntitaSqlBuilder sqlBuilder = new DataWarehouseSediEntitaSqlBuilder(
                it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseSediEntitaSqlBuilder.TipoFiltro.NESSUNO);
        SqlExecuter sqlExecuter = new SqlExecuter();
        sqlExecuter.setSql(sqlBuilder.getSqlDelete());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter.setSql(sqlBuilder.getSql());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter.setSql(sqlBuilder.getSqlSedeNonPresente(getAzienda()));
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
    }

    private void sincronizzaTipiDocumentoDMS() {
        Query query = panjeaDAO.prepareQuery("select td.codice,td.descrizione,td.id from TipoDocumento td");
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> result = panjeaDAO.getResultList(query);
            List<AttributoAllegatoPanjea> atts = new ArrayList<>();
            for (Object[] att : result) {
                atts.add(new AttributoAllegatoTipoDocumento((String) att[0], (String) att[1], (Integer) att[2],
                        getAzienda()));
            }
            dmsAttributiManager.aggiornaAttributi(TipoAttributo.TIPO_DOC, atts);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare la lista dei tipi documento", e);
            throw new GenericException("-->errore nel caricare la lista dei tipi documento", e);
        }
    }
}
