package it.eurotn.panjea.ordini.manager.documento;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.iva.util.IImponibiliIvaQueryExecutor;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.Totalizzatore;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.TotalizzazioneNormale;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.ordini.manager.sqlbuilder.GiacenzaOrdineQueryBuilder;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine.STATO_ORDINE;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 */
@Stateless(mappedName = "Panjea.AreaOrdineManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaOrdineManager")
public class AreaOrdineManagerBean implements AreaOrdineManager {

    /**
     *
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
         */
        public void setSql(String sql) {
            this.sql = sql;
        }
    }

    private static Logger logger = Logger.getLogger(AreaOrdineManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DocumentiManager documentiManager;

    @EJB
    private IImponibiliIvaQueryExecutor imponibiliIvaQueryExecutor;

    @EJB
    private ITotalizzatoriQueryExecutor totalizzatoriQueryExecutor;

    @EJB
    private AreaIvaManager areaIvaManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private PrezzoArticoloCalculator prezzoArticoloCalculator;

    @EJB
    @IgnoreDependency
    private RigaOrdineManager rigaOrdineManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private DepositiManager depositiManager;

    @SuppressWarnings("unchecked")
    @Override
    public void aggiungiVariazione(Integer idAreaOrdine, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
        logger.debug("--> Enter aggiungiVariazione");
        Query query = panjeaDAO
                .prepareQuery("select riga from RigaArticoloOrdine riga where riga.areaOrdine=" + idAreaOrdine);
        List<RigaArticolo> righe = null;
        try {
            righe = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore nel caricare le righe articolo dell'ordine ", e);
            throw new RuntimeException("-->errore nel caricare le righe articolo dell'ordine", e);
        }
        for (RigaArticolo rigaArticolo : righe) {

            PoliticaPrezzo politicaPrezzo = calcolaPoliticaPrezzo(rigaArticolo);
            rigaArticolo.setPoliticaPrezzo(politicaPrezzo);
            rigaArticolo = (RigaArticolo) variazioneScontoStrategy.applicaVariazione(rigaArticolo, variazione,
                    tipoVariazioneScontoStrategy);
            rigaArticolo = (RigaArticolo) variazioneProvvigioneStrategy.applicaVariazione(rigaArticolo, percProvvigione,
                    tipoVariazioneProvvigioneStrategy);
            try {
                panjeaDAO.save(rigaArticolo);
            } catch (DAOException e) {
                logger.error("-->errore nel salvare la riga dell'ordine " + rigaArticolo, e);
                throw new RuntimeException("-->errore nel caricare la riga dell'ordine " + rigaArticolo, e);
            }
        }
        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setId(idAreaOrdine);
        areaOrdine = caricaAreaOrdine(areaOrdine);
        areaOrdine.setStatoAreaOrdine(StatoAreaOrdine.PROVVISORIO);
        areaOrdine.getDatiValidazioneRighe().invalida();
        salvaAreaOrdine(areaOrdine);
        logger.debug("--> Exit aggiungiVariazione");
    }

    @Override
    public AreaOrdine bloccaOrdine(AreaOrdine areaOrdine, boolean blocca) {
        logger.debug("--> Enter bloccaOrdine");

        try {
            areaOrdine = panjeaDAO.load(AreaOrdine.class, areaOrdine.getId());

            if (areaOrdine.getRighe().isEmpty()) {
                return areaOrdine;
            }

            // se l'area ordine è in stato provvisorio devo prima confermarla e poi bloccarla
            if (areaOrdine.getStatoAreaOrdine() == StatoAreaOrdine.PROVVISORIO) {
                AreaPartite areaPartite = areaRateManager.caricaAreaRate(areaOrdine.getDocumento());
                areaOrdine = validaRigheOrdine(areaOrdine, areaPartite, true);
            }

            areaOrdine.bloccaOrdine(blocca);
            areaOrdine = panjeaDAO.save(areaOrdine);
        } catch (ObjectNotFoundException e) {
            logger.error("-->errore. AreaOrdine non trovata. id cercato " + areaOrdine.getId(), e);
            throw new RuntimeException(e);
        } catch (DAOException e) {
            logger.error("-->errore nel salvare l'area ordine dopo averla bloccata", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit bloccaOrdine");
        return areaOrdine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void calcolaGiacenzaRigheOrdine(Integer idAreaOrdine) {
        logger.debug("--> Enter calcolaGiacenzaRigheOrdine");

        Deposito depositoPrincipale = depositiManager.caricaDepositoPrincipale();

        List<Integer> idArticoli = null;
        try {
            String queryArticoli = "select distinct ro.articolo_id from ordi_righe_ordine ro where ro.articolo_id is not null and ro.areaOrdine_id = "
                    + idAreaOrdine;
            idArticoli = panjeaDAO.getResultList(panjeaDAO.getEntityManager().createNativeQuery(queryArticoli));

            GiacenzaOrdineQueryBuilder giacenzaQueryBuilder = new GiacenzaOrdineQueryBuilder(idAreaOrdine);
            String query = giacenzaQueryBuilder.getSqlString(idArticoli, depositoPrincipale.getId(),
                    Calendar.getInstance().getTime());

            panjeaDAO.executeQuery(panjeaDAO.getEntityManager().createNativeQuery(query));
            panjeaDAO.getEntityManager().flush();
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit calcolaGiacenzaRigheOrdine");
    }

    /**
     * Calcola la politica prezzo della riga di riferimento.
     *
     * @param rigaArticolo
     *            riga articolo
     * @return politica prezzo
     */
    private PoliticaPrezzo calcolaPoliticaPrezzo(RigaArticolo rigaArticolo) {
        ArticoloLite articolo = rigaArticolo.getArticolo();
        AreaOrdine areaOrdine = rigaArticolo.getAreaOrdine();
        Integer idListino = null;
        Integer idListinoAlternativo = null;
        Integer idSedeEntita = null;
        Integer idTipoMezzo = null;

        if (areaOrdine.getListino() != null) {
            idListino = areaOrdine.getListino().getId();
        }

        if (areaOrdine.getListinoAlternativo() != null) {
            idListinoAlternativo = areaOrdine.getListinoAlternativo().getId();
        }

        if (areaOrdine.getDocumento().getSedeEntita() != null) {
            idSedeEntita = areaOrdine.getDocumento().getSedeEntita().getId();
        }

        AreaRate areaRate = areaRateManager.caricaAreaRate(rigaArticolo.getAreaOrdine().getDocumento());
        CodicePagamento codicePagamento = areaRate.getCodicePagamento();
        BigDecimal scontoCommerciale = null;
        if (codicePagamento != null) {
            scontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
        }

        ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articolo.getId(),
                areaOrdine.getDocumento().getDataDocumento(), idListino, idListinoAlternativo, null, idSedeEntita, null,
                null, ProvenienzaPrezzo.LISTINO, idTipoMezzo, null, articolo.getProvenienzaPrezzoArticolo(),
                areaOrdine.getDocumento().getTotale().getCodiceValuta(), null, scontoCommerciale);
        return prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);
    }

    @Override
    public AreaOrdine caricaAreaOrdine(AreaOrdine areaOrdine) {
        logger.debug("--> Enter caricaAreaOrdine");
        AreaOrdine areaOrdineCaricata = null;
        try {
            areaOrdineCaricata = panjeaDAO.load(AreaOrdine.class, areaOrdine.getId());
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore ObjectNotFoundException in caricaAreaOrdine", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaAreaOrdine");
        return areaOrdineCaricata;
    }

    @Override
    public AreaOrdine caricaAreaOrdineByDocumento(Documento documento) {
        logger.debug("--> Enter caricaAreaOrdineByDocumento");
        Query query = panjeaDAO.prepareNamedQuery("AreaOrdine.ricercaByDocumento");
        query.setParameter("paramIdDocumento", documento.getId());
        AreaOrdine areaOrdine = null;
        try {
            areaOrdine = (AreaOrdine) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            return areaOrdine;
        } catch (DAOException e) {
            logger.error("--> errore nel caricare l'area ordine per il documento " + documento.getId(), e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaAreaOrdineByDocumento");
        return areaOrdine;
    }

    @Override
    public AreaOrdine checkInvalidaAreaMagazzino(AreaOrdine areaOrdine) {
        if ((areaOrdine.getStatoAreaOrdine() == StatoAreaOrdine.CONFERMATO)
                || (areaOrdine.getStatoAreaOrdine() == StatoAreaOrdine.BLOCCATO)) {
            areaOrdine.setStatoAreaOrdine(StatoAreaOrdine.PROVVISORIO);
            try {
                areaOrdine.getDatiValidazioneRighe().invalida();
                areaOrdine = panjeaDAO.save(areaOrdine);
            } catch (Exception e) {
                logger.error("--> errore nel salvare il documento durante l'invalidazione dell'area contabile", e);
                throw new RuntimeException(
                        "--> errore nel salvare il documento durante l'invalidazione dell'area contabile", e);
            }
        }
        return areaOrdine;
    }

    @Override
    public Integer copiaOrdine(Integer idAreaOrdine) {

        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setId(idAreaOrdine);
        areaOrdine = caricaAreaOrdine(areaOrdine);
        areaOrdine.getRighe().size();

        AreaOrdine areaOrdineClone = (AreaOrdine) areaOrdine.clone();
        areaOrdineClone.setEvaso(false);
        Set<RigaOrdine> righeOrdineClone = areaOrdineClone.getRighe();

        areaOrdineClone.setRighe(new TreeSet<RigaOrdine>());

        // salvo l'area ordine
        areaOrdineClone = salvaAreaOrdine(areaOrdineClone);

        // salvo le righe ordine
        for (RigaOrdine rigaOrdine : righeOrdineClone) {
            rigaOrdine.setAreaOrdine(areaOrdineClone);
            // Cambio anche agli attributi il riferimento alla riga.
            if (rigaOrdine instanceof RigaArticolo) {
                RigaArticolo rigaArticolo = (RigaArticolo) rigaOrdine;
                List<AttributoRiga> attributi = rigaArticolo.getAttributi();
                rigaArticolo.setAttributi(new ArrayList<AttributoRiga>());
                for (AttributoRiga attributo : attributi) {
                    attributo.setId(null);
                    attributo.setVersion(null);
                    attributo.setRigaArticolo(rigaArticolo);
                    rigaArticolo.getAttributi().add(attributo);
                }
            }
            rigaOrdineManager.getDao(rigaOrdine).salvaRigaOrdine(rigaOrdine);
        }

        AreaRate areaRate = areaRateManager.caricaAreaRate(areaOrdine.getDocumento());

        // salvo l'area rate se è present
        if (areaRate.getId() != null) {
            AreaRate areaRateClone = (AreaRate) areaRate.clone();
            areaRateClone.setDocumento(areaOrdineClone.getDocumento());
            areaRateManager.salvaAreaRate(areaRateClone);
        }

        return areaOrdineClone.getId();
    }

    /**
     * @return codice azienda corrente
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * @return principal loggato
     */
    private JecPrincipal getJecPrincipal() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal());
    }

    /**
     * @param areaOrdine
     *            areaordine da ordinare
     */
    private void ordinaRighe(AreaOrdine areaOrdine) {
        // Le righe note automatiche devono sempre andare in fondo
        // ma non vengono ricreate ogni volta come le righe automatiche
        // quindi le metto all'ultimo posto del documento
        StringBuilder sb = new StringBuilder("update ordi_righe_ordine set ordinamento=");
        sb.append(Double.MAX_VALUE);
        sb.append(" where TIPO_RIGA='N' and rigaAutomatica=1 and areaOrdine_id=");
        sb.append(areaOrdine.getId());

        SqlExecuter executer = new SqlExecuter();
        executer.setSql(sb.toString());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);

        executer.setSql("set @row_number=0");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);

        sb = new StringBuilder(
                "update ordi_righe_ordine set ordinamento=@row_number:=@row_number+1000 where areaOrdine_id=");
        sb.append(areaOrdine.getId());
        sb.append(" order by ordinamento");
        executer.setSql(sb.toString());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
    }

    @Override
    public void ricalcolaPrezziOrdine(Integer idAreaOrdine) {

        // carico l'area ordine
        AreaOrdine areaOrdine;
        try {
            areaOrdine = panjeaDAO.load(AreaOrdine.class, idAreaOrdine);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dell'area ordine.", e);
            throw new RuntimeException("errore durante il caricamento dell'area ordine.", e);
        }

        // carica l'area rate per avere il codice pagamento
        AreaRate areaRate = areaRateManager.caricaAreaRate(areaOrdine.getDocumento());

        List<RigaOrdine> righeOrdine = rigaOrdineManager.getDao().caricaRigheOrdine(areaOrdine);

        // ricalcolo i prezzi se la riga non è chiusa
        for (RigaOrdine rigaOrdine : righeOrdine) {
            if (rigaOrdine instanceof RigaArticolo && !((RigaArticolo) rigaOrdine).isEvasa()) {
                rigaOrdineManager.ricalcolaPrezziRigaArticolo((RigaArticolo) rigaOrdine, areaRate.getCodicePagamento());
            }
        }

        // valido le righe ordine senza cambiare lo stato per lanciare la totalizzazione del
        // documento
        validaRigheOrdine(areaOrdine, areaRate, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaOrdineRicerca> ricercaAreeOrdine(ParametriRicercaAreaOrdine parametriRicercaAreaOrdine) {
        logger.debug("--> Enter ricercaAreeOrdine");

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("a.id as idAreaOrdine, ");
        sb.append("a.evaso as evaso, ");
        sb.append("a.tipoAreaOrdine.id as idTipoAreaOrdine, ");
        sb.append("a.documento.codiceAzienda as azienda, ");
        sb.append("a.documento.id as idDocumento, ");
        sb.append("a.documento.tipoDocumento.id as idTipoDocumento, ");
        sb.append("a.documento.entita.id as idEntita, ");
        sb.append("a.dataRegistrazione as dataRegistrazione, ");
        sb.append("a.statoAreaOrdine as statoAreaOrdine, ");
        sb.append("a.documento.tipoDocumento.tipoEntita as tipoEntita, ");
        sb.append("a.documento.entita.codice as codiceEntita, ");
        sb.append("a.documento.entita.anagrafica.denominazione as denominazioneEntita, ");
        sb.append("a.documento.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append("a.documento.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append("a.documento.dataDocumento as dataDocumento, ");
        sb.append("a.documento.codice as codiceDocumento, ");
        sb.append("a.documento.totale.importoInValutaAzienda as totaleDocumento, ");
        sb.append("a.depositoOrigine.codice as codiceDepositoOrigine, ");
        sb.append("a.depositoOrigine.descrizione as descrizioneDepositoOrigine, ");
        sb.append(" a.areaOrdineNote.noteTestata as note, ");
        sb.append("sed.id as idSede, ");
        sb.append("sed.descrizione as descrizioneSede, ");
        sb.append("sed.indirizzo as indirizzoSede, ");
        sb.append("loc.descrizione as descrizioneLocalitaSede, ");
        sb.append("a.documento.sedeEntita.codice as codiceSede, ");
        sb.append("a.riferimentiOrdine.numeroOrdine as numeroOrdineCliente ");
        sb.append(
                " from AreaOrdine a left join a.areaOrdineNote left join a.documento.sedeEntita.sede sed  left join a.documento.entita left join a.documento.entita.anagrafica left join sed.datiGeografici.localita loc ");
        sb.append(" where a.documento.codiceAzienda = :codiceAzienda  ");

        if (parametriRicercaAreaOrdine.getDataDocumentoFinale() != null
                && parametriRicercaAreaOrdine.getDataDocumentoIniziale() != null) {
            sb.append(
                    " and a.documento.dataDocumento >= :dataDocumentoIniziale and a.documento.dataDocumento <= :dataDocumentoFinale ");
        }

        if (!StringUtils.isEmpty(parametriRicercaAreaOrdine.getNumeroDocumento())) {
            sb.append(" and a.documento.codice.codice like :numeroDocumento ");
        }

        if (!parametriRicercaAreaOrdine.getNumeroDocumentoIniziale().isEmpty()) {
            sb.append(" and a.documento.codice.codiceOrder >= :numeroDocumentoIniziale ");
        }

        if (parametriRicercaAreaOrdine.getDataCreazioneTimeStamp() != null) {
            sb.append(" and a.dataCreazioneTimeStamp=:dataCreazioneTimeStamp");
        }

        if (!parametriRicercaAreaOrdine.getNumeroDocumentoFinale().isEmpty()) {
            sb.append(" and a.documento.codice.codiceOrder <= :numeroDocumentoFinale");
        }

        if (parametriRicercaAreaOrdine.getAnnoCompetenza() != null
                && parametriRicercaAreaOrdine.getAnnoCompetenza() != -1) {
            sb.append(" and a.annoMovimento=:annoCompetenza ");
        }
        if (parametriRicercaAreaOrdine.getEntita() != null) {
            sb.append(" and (a.documento.entita = :entita) ");
        }

        if (parametriRicercaAreaOrdine.getAgente() != null) {
            sb.append(" and (a.agente = :agente) ");
        }

        if ((parametriRicercaAreaOrdine.getTipiAreaOrdine() != null)
                && (parametriRicercaAreaOrdine.getTipiAreaOrdine().size() > 0)) {
            sb.append(" and (a.tipoAreaOrdine in (:tipiAreaOrdine)) ");
        }
        if (parametriRicercaAreaOrdine.getUtente() != null) {
            sb.append(" and (a.userInsert = :codiceUtente) ");
        }
        if ((parametriRicercaAreaOrdine.getStatiAreaOrdine() != null)
                && (parametriRicercaAreaOrdine.getStatiAreaOrdine().size() > 0)) {
            sb.append(" and (a.statoAreaOrdine in (:statiAreaOrdine)) ");
        }

        if (parametriRicercaAreaOrdine.getNumeroOrdineCliente() != null
                && !parametriRicercaAreaOrdine.getNumeroOrdineCliente().isEmpty()) {
            sb.append(" and a.riferimentiOrdine.numeroOrdine=:numeroOrdineCliente");
        }

        if (parametriRicercaAreaOrdine.getStatoOrdine() == null) {
            parametriRicercaAreaOrdine.setStatoOrdine(STATO_ORDINE.TUTTI);
        }
        switch (parametriRicercaAreaOrdine.getStatoOrdine()) {
        case EVASO:
            sb.append(" and (a.evaso = true) ");
            break;
        case NON_EVASO:
            sb.append(" and (a.evaso = false) ");
            break;

        default:
            // non filtro per stato dell'ordine
            break;
        }

        parametriRicercaAreaOrdine.setCodiceAzienda(getAzienda());

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((AreaOrdineRicerca.class)));

        ((QueryImpl) query).getHibernateQuery().setProperties(parametriRicercaAreaOrdine);
        if (!StringUtils.isEmpty(parametriRicercaAreaOrdine.getNumeroDocumento())) {
            query.setParameter("numeroDocumento", parametriRicercaAreaOrdine.getNumeroDocumento());
        }
        if (!parametriRicercaAreaOrdine.getNumeroDocumentoIniziale().isEmpty()) {
            query.setParameter("numeroDocumentoIniziale",
                    parametriRicercaAreaOrdine.getNumeroDocumentoIniziale().getCodiceOrder());
        }
        if (!parametriRicercaAreaOrdine.getNumeroDocumentoFinale().isEmpty()) {
            query.setParameter("numeroDocumentoFinale",
                    parametriRicercaAreaOrdine.getNumeroDocumentoFinale().getCodiceOrder());
        }
        List<AreaOrdineRicerca> result = Collections.emptyList();

        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore nella ricerca documento", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit ricercaAreeOrdine");
        return result;
    }

    @Override
    public AreaOrdine salvaAreaOrdine(AreaOrdine areaOrdine) {
        logger.debug("--> Enter salvaAreaOrdine");

        // allinea l'attributo tipoDocumento di Documento con il tipoDocumento
        // di TipoAreaOrdine
        areaOrdine.getDocumento().setTipoDocumento(areaOrdine.getTipoAreaOrdine().getTipoDocumento());

        if (areaOrdine.getDocumento().isNew()) {
            // inizializza il codiceAzienda di documento
            areaOrdine.getDocumento().setCodiceAzienda(getAzienda());
        }
        // save di documento
        Documento documento;
        try {
            documento = documentiManager.salvaDocumento(areaOrdine.getDocumento());
        } catch (DocumentoDuplicateException e) {
            logger.error("--> errore DocumentoDuplicateException in salvaAreaOrdine", e);
            throw new RuntimeException(e);
        }
        areaOrdine.setDocumento(documento);

        try {
            if (areaOrdine.getAreaOrdineNote().isEmpty()) {
                areaOrdine.setAreaOrdineNote(null);
            }
            areaOrdine = panjeaDAO.save(areaOrdine);
        } catch (Exception e) {
            logger.error("--> errore nel salvare l'area ordine", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaAreaOrdine");
        return areaOrdine;
    }

    @Override
    public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
        logger.debug("--> Enter spostaRighe");
        // Il metodo deve essere performante. genero update dei soli campi interessati
        try {
            // recupero l'ordinamento base e l'area associata. Se idDest esiste, altrimenti
            // significa che le righe
            // devono essere messe in fondo al doc.
            Double ordinamentoBase = 50000000.0;
            if (idDest != null) {
                Query queryOrdinamentoBase = panjeaDAO
                        .prepareQuery("select ro.ordinamento from RigaOrdine ro where ro.id=:idDest");
                queryOrdinamentoBase.setParameter("idDest", idDest);
                ordinamentoBase = (Double) panjeaDAO.getSingleResult(queryOrdinamentoBase);
            }

            // Riordino gli id in base all'ordinamento originale, carico anche l'id dell'
            // areamagazzino
            // per avere l'id dell'area da riordinare
            Query queryIdRiordinati = panjeaDAO.prepareQuery(
                    "select ro.id,ro.areaOrdine.id from RigaOrdine ro where ro.id in(:ids) order by ro.ordinamento desc");
            queryIdRiordinati.setParameter("ids", righeDaSpostare);
            @SuppressWarnings("unchecked")
            List<Object[]> idRiordinati = panjeaDAO.getResultList(queryIdRiordinati);

            Query queryAggiornamento = panjeaDAO.prepareQuery(
                    "update RigaOrdine ro set ro.ordinamento=:ordinamento,ro.version=ro.version+1 where ro.id=:idRiga");
            for (Object[] idRiga : idRiordinati) {
                ordinamentoBase--;
                queryAggiornamento.setParameter("ordinamento", ordinamentoBase);
                queryAggiornamento.setParameter("idRiga", idRiga[0]);
                panjeaDAO.executeQuery(queryAggiornamento);
            }
            AreaOrdine areaOrdine = new AreaOrdine();
            areaOrdine.setId((Integer) idRiordinati.get(0)[1]);
            ordinaRighe(areaOrdine);
        } catch (DAOException e) {
            logger.error("-->errore nell'impostare l'ordinamento delle righe", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit spostaRighe");
    }

    @Override
    public AreaOrdine totalizzaDocumento(AreaOrdine areaOrdine, AreaPartite areaPartite) {
        imponibiliIvaQueryExecutor.setAreaDocumento(areaOrdine);
        imponibiliIvaQueryExecutor.setQueryString("RigaArticoloOrdine.caricaImponibiliIva");
        List<RigaIva> righeIva = areaIvaManager.generaRigheIva(imponibiliIvaQueryExecutor, areaOrdine.getDocumento(),
                areaOrdine.isAddebitoSpeseIncasso(), null, areaPartite);

        totalizzatoriQueryExecutor.setAreaDocumento(areaOrdine);
        totalizzatoriQueryExecutor.setQueryString("RigaArticoloOrdine.caricaByTipo");

        Totalizzatore totalizzatore = new TotalizzazioneNormale();

        Documento documentoTotalizzato = totalizzatore.totalizzaDocumento(areaOrdine.getDocumento(),
                areaOrdine.getTotaliArea(), totalizzatoriQueryExecutor, righeIva);
        areaOrdine.setDocumento(documentoTotalizzato);

        return areaOrdine;
    }

    @Override
    public AreaOrdine validaRigheOrdine(AreaOrdine areaOrdine, AreaPartite areaPartite, boolean cambioStato) {
        logger.debug("--> Enter confermaRigheMagazzino");

        logger.debug("--> Totalizzo il documento");
        areaOrdine = totalizzaDocumento(areaOrdine, areaPartite);

        if (cambioStato) {
            if (areaOrdine.getTipoAreaOrdine().getTipoDocumento().getTipoEntita() != TipoEntita.AZIENDA
                    && (areaOrdine.getDocumento().getEntita().getBloccoSede().isBlocco()
                            || areaOrdine.getDocumento().getSedeEntita().getBloccoSede().isBlocco())) {
                areaOrdine.setStatoAreaOrdine(StatoAreaOrdine.BLOCCATO);
            } else {
                areaOrdine.setStatoAreaOrdine(StatoAreaOrdine.CONFERMATO);
            }

            // setto l'area validata
            areaOrdine.getDatiValidazioneRighe().valida(getJecPrincipal().getUserName());
        }

        AreaOrdine areaOrdineSave = null;
        try {
            areaOrdineSave = salvaAreaOrdine(areaOrdine);
            ordinaRighe(areaOrdineSave);
        } catch (Exception e) {
            logger.error("--> errore nel salvare l'area ordine " + areaOrdineSave, e);
            throw new RuntimeException(e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("--> area ordine salvata " + areaOrdineSave);
        }
        logger.debug("--> Exit validaRigheOrdine");
        return areaOrdineSave;
    }
}
