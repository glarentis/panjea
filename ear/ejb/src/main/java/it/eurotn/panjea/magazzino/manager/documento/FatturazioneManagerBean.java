package it.eurotn.panjea.magazzino.manager.documento;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.OrdinamentoFatturazione;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.exception.SedeNonAppartieneAdEntitaException;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces.PreparaFatturazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.ConfermaMovimentoInFatturazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneDifferitaGenerator;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.querybuilder.CaricaAreeMagazzinoParametriRicercaFatturazioneQueryBuilder;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.rulesvalidation.FatturazioneRulesChecker;
import it.eurotn.panjea.magazzino.rulesvalidation.RigheRulesChecker;
import it.eurotn.panjea.magazzino.service.MagazzinoDocumentoServiceBean;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.panjea.magazzino.util.FatturazioneStateDescriptor;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(mappedName = "Panjea.FatturazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FatturazioneManager")
public class FatturazioneManagerBean implements FatturazioneManager {

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

    private static Logger logger = Logger.getLogger(FatturazioneManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManagerBean;

    @EJB(mappedName = "Panjea.FatturazioneDifferitaGenerator")
    private FatturazioneDifferitaGenerator fatturazioneDifferitaGenerator;

    @EJB(mappedName = "Panjea.RifatturazioneDifferitaGenerator")
    private FatturazioneDifferitaGenerator rifatturazioneDifferitaGenerator;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaMagazzinoCancellaManager areaMagazzinoCancellaManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    @IgnoreDependency
    private PreparaFatturazioneManager preparaFatturazioneManager;

    @EJB
    private ConfermaMovimentoInFatturazioneManager confermaMovimentoInFatturazioneManager;

    @EJB
    private PanjeaMessage panjeaMessage;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void cancellaMovimentiInFatturazione(String utente) {
        // carico tutte le aree magazzino che sono in fatturazione.
        // viene aperta una transazione specifica per il caricamento
        // in modo da mantenere il più piccole possibile le transazioni
        List<AreaMagazzino> areeMagazzinoInFatturazione = areaMagazzinoManager
                .caricaAreeMagazzinoByStato(StatoAreaMagazzino.INFATTURAZIONE);

        if (areeMagazzinoInFatturazione == null || areeMagazzinoInFatturazione.isEmpty()) {
            return;
        }

        // cancello le aree tramite sql senza passare dalla
        // areaMagazzinoCancellaManager.cancellaAreaMagazzino per
        // motivi di prestazione.
        // Posso farlo perchè il movimento in fatturazione non prevede utilizzo dei valori dei
        // protocolli, lotti, ecc...
        List<Integer> idDocumenti = new ArrayList<Integer>();
        for (AreaMagazzino areaMagazzino : areeMagazzinoInFatturazione) {
            if (Objects.equals(areaMagazzino.getDatiGenerazione().getUtente(), utente)) {
                idDocumenti.add(areaMagazzino.getDocumento().getId());
            }
        }

        // se non ho nessun movimento in fatturazione dell'utente esco
        if (idDocumenti.isEmpty()) {
            return;
        }

        String idDocumentiString = StringUtils.join(idDocumenti.toArray(), ",");

        SqlExecuter sqlExecuter = new SqlExecuter();

        // delete rate ( righe e area )
        sqlExecuter
                .setSql("delete rate.* from part_rate rate inner join part_area_partite ap on ap.id = rate.areaRate_id where ap.documento_id in ("
                        + idDocumentiString + ")");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter
                .setSql("delete ap.* from part_area_partite ap where ap.documento_id in (" + idDocumentiString + ")");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        // delete iva ( righe e area )
        sqlExecuter
                .setSql("delete rigaIva.* from cont_righe_iva rigaIva inner join cont_aree_iva ai on ai.id=rigaIva.areaIva_id where ai.documento_id in ("
                        + idDocumentiString + ")");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter.setSql("delete ai.* from cont_aree_iva ai where ai.documento_id in (" + idDocumentiString + ")");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        // delete magazzino ( righe e area )
        sqlExecuter
                .setSql("update maga_righe_magazzino rm inner join maga_area_magazzino am on am.id=rm.areaMagazzino_id set rm.rigaTestataMagazzinoCollegata_id = null where am.documento_id in ("
                        + idDocumentiString + ")");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter
                .setSql("delete rm.* from maga_righe_magazzino rm inner join maga_area_magazzino am on am.id=rm.areaMagazzino_id where am.documento_id in ("
                        + idDocumentiString + ")");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        sqlExecuter
                .setSql("delete am.* from maga_area_magazzino am where am.documento_id in (" + idDocumentiString + ")");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);

        // delete documenti
        sqlExecuter.setSql("delete doc.* from docu_documenti doc where doc.id in (" + idDocumentiString + ")");
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(sqlExecuter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoLite> caricaAreeMagazzino(ParametriRicercaFatturazione parametriRicercaFatturazione) {
        logger.debug("--> Enter caricaAreeMagazzino");

        Query query = panjeaDAO.prepareQuery(
                CaricaAreeMagazzinoParametriRicercaFatturazioneQueryBuilder.buildSql(parametriRicercaFatturazione));
        query = CaricaAreeMagazzinoParametriRicercaFatturazioneQueryBuilder.applyParameter(query,
                parametriRicercaFatturazione, getCodiceAzienda());

        List<RigaArticoloLite> listRigheTrovate = new ArrayList<RigaArticoloLite>();

        try {
            listRigheTrovate = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento delle aree magazzino per la fatturazione.", e);
            throw new RuntimeException("Errore durante il caricamento delle aree magazzino per la fatturazione.", e);
        }

        // Dalle righe recupero le areeMagazzino ed aggiungo le righe non valide
        Map<Integer, AreaMagazzinoLite> areeMagazzino = new HashMap<Integer, AreaMagazzinoLite>();
        RigheRulesChecker rulesChecker = new FatturazioneRulesChecker();

        for (RigaArticoloLite rigaArticoloLite : listRigheTrovate) {
            // Controllo se esiste l'area nella mappa, in caso la aggiungo
            if (!areeMagazzino.containsKey(rigaArticoloLite.getAreaMagazzino().getId())) {
                areeMagazzino.put(rigaArticoloLite.getAreaMagazzino().getId(), rigaArticoloLite.getAreaMagazzino());
            }
            if (!rulesChecker.check(rigaArticoloLite)) {
                // Se ho una riga non valida la aggiungo all'area
                AreaMagazzinoLite area = areeMagazzino.get(rigaArticoloLite.getAreaMagazzino().getId());
                area.addToRigheNonValide(rigaArticoloLite);
                areeMagazzino.put(rigaArticoloLite.getAreaMagazzino().getId(), area);
            }
        }

        // riodino le aree magazzino per tipo documento
        Comparator<AreaMagazzinoLite> areeComaparator = new Comparator<AreaMagazzinoLite>() {
            @Override
            public int compare(AreaMagazzinoLite o1, AreaMagazzinoLite o2) {
                return o1.getTipoAreaMagazzino().getTipoDocumento().getCodice()
                        .compareTo(o2.getTipoAreaMagazzino().getTipoDocumento().getCodice());
            }
        };
        EventList<AreaMagazzinoLite> eventList = new BasicEventList<AreaMagazzinoLite>();
        eventList.addAll(areeMagazzino.values());

        SortedList<AreaMagazzinoLite> sortedList = new SortedList<AreaMagazzinoLite>(eventList, areeComaparator);

        logger.debug("--> Exit caricaAreeMagazzino");
        return new ArrayList<AreaMagazzinoLite>(sortedList);
    }

    @Override
    public Date caricaDataMovimentiInFatturazione() {
        logger.debug("--> Enter caricaDataMovimentiInFatturazione");

        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.caricaDataFatturazioneMovimenti");
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        query.setParameter("paramUtente", ((JecPrincipal) context.getCallerPrincipal()).getUserName());

        Date dataGenerazione = null;

        try {
            dataGenerazione = (Date) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            logger.debug("--> Non ho nessun movimento in fatturazione temporanea. No prolema manara");
        } catch (DAOException e) {
            throw new RuntimeException("Errore durante il caricamento della data dei movimenti in fatturazione.", e);
        }

        logger.debug("--> Exit caricaDataMovimentiInFatturazione");
        return dataGenerazione;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DatiGenerazione> caricaDatiGenerazioneFatturazioneTemporanea() {
        logger.debug("--> Enter caricaDatiGenerazioneFatturazioniTemporanee");

        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.caricaBYDatiGenerazioneFatturazioneTemporanea");
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());

        List<DatiGenerazione> datiGenerazione = null;

        try {
            datiGenerazione = panjeaDAO.getResultList(query);
        } catch (ObjectNotFoundException e) {
            // non faccio niente, vuol dire solo che non ho una fatturazione
            // temporanea
            datiGenerazione = null;
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dei dati generazione per le fatturazioni temporanee", e);
            throw new RuntimeException(
                    " errore durante il caricamento dei dati generazione per le fatturazioni temporanee", e);
        }

        return datiGenerazione;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DatiGenerazione> caricaFatturazioni(int annoFatturazione) {
        logger.debug("--> Enter caricaFatturazioni");

        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.caricaBYDatiGenerazioneFatturazione");
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        query.setParameter("paramAnnoFatturazione", annoFatturazione);

        List<DatiGenerazione> listDatiGenerazione = new ArrayList<DatiGenerazione>();

        try {
            listDatiGenerazione = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento delle fatturazioni per l'anno " + annoFatturazione, e);
            throw new RuntimeException(
                    "Errore durante il caricamento delle fatturazioni per l'anno " + annoFatturazione, e);
        }

        logger.debug("--> Exit caricaFatturazioni");
        return listDatiGenerazione;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoLite> caricaMovimentiPerFatturazione(Date dataCreazione) {
        logger.debug("--> Enter caricaMovimentiPerFatturazione");

        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.caricaBYFatturazione");
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        query.setParameter("paramDataCreazione", dataCreazione);

        List<AreaMagazzinoLite> list = new ArrayList<AreaMagazzinoLite>();

        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei movimenti fatturati il " + dataCreazione, e);
            throw new RuntimeException("Errore durante il caricamento dei movimenti fatturati il " + dataCreazione, e);
        }

        logger.debug("--> Exit caricaMovimentiPerFatturazione");
        return list;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @SuppressWarnings("unchecked")
    @Override
    public List<MovimentoFatturazioneDTO> caricaMovimentPerFatturazione(Date dataCreazione, String utente) {
        logger.debug("--> Enter caricaMovimentiInFatturazione");

        StringBuffer hqlQuery = new StringBuffer(1000);
        hqlQuery.append("select am.id as idAreaMagazzino, ");
        hqlQuery.append("am.version as versionAreaMagazzino, ");
        hqlQuery.append("am.statoSpedizione as statoSpedizione, ");
        hqlQuery.append("am.statoAreaMagazzino as statoAreaMagazzino, ");
        hqlQuery.append("partite.id as idAreaRate, ");
        hqlQuery.append("partite.codicePagamento.id as idCodicePagamento, ");
        hqlQuery.append("partite.codicePagamento.codicePagamento as pagamentoCodice, ");
        hqlQuery.append("partite.codicePagamento.descrizione as pagamentoDescrizione, ");
        hqlQuery.append("am.tipoAreaMagazzino.id as idTipoAreaMagazzino, ");
        hqlQuery.append("tipoDocumento.id as idTipoDocumento, ");
        hqlQuery.append("doc.entita.id as entitaId, ");
        hqlQuery.append("doc.entita.version as entitaVersion, ");
        hqlQuery.append("doc.entita.codice as entitaCodice, ");
        hqlQuery.append("doc.entita.anagrafica.denominazione as entitaDenominazione, ");
        hqlQuery.append("doc.entita.anagrafica.sedeAnagrafica.indirizzoMail as indirizzoMail, ");
        hqlQuery.append("doc.entita.anagrafica.sedeAnagrafica.indirizzoPEC as indirizzoPec, ");
        hqlQuery.append("tipoDocumento.descrizione as tipoDocumentoDescrizione, ");
        hqlQuery.append("tipoDocumento.codice as tipoDocumentoCodice, ");
        hqlQuery.append("doc.codice as numeroDocumento, ");
        hqlQuery.append("doc.dataDocumento as dataDocumento, ");
        hqlQuery.append("doc.id as idDocumento, ");
        hqlQuery.append("sede.id as idSede, ");
        hqlQuery.append("sede.sede.descrizione as descrizioneSede, ");
        hqlQuery.append("sede.sede.indirizzo as indirizzoSede, ");
        hqlQuery.append("(select zona from ZonaGeografica zona where zona.id=am.idZonaGeografica) as zonaGeografica, ");
        hqlQuery.append("agente.id as idAgente, ");
        hqlQuery.append("agente.codice as codiceAgente, ");
        hqlQuery.append("agenteAnagrafica.denominazione as agenteDenominazione ");
        hqlQuery.append("from AreaMagazzino am join am.documento doc join doc.tipoDocumento tipoDocumento ");
        hqlQuery.append(
                "join doc.sedeEntita sede left join sede.agente agente left join agente.anagrafica agenteAnagrafica , AreaPartite partite ");
        hqlQuery.append("where partite.documento = doc and doc.codiceAzienda = :paramCodiceAzienda ");
        hqlQuery.append(" and am.datiGenerazione.utente = :paramUtente ");
        if (dataCreazione == null) {
            hqlQuery.append(" and am.statoAreaMagazzino = 3 ");
            hqlQuery.append("order by am.documento.codice.codiceOrder ");
        } else {
            hqlQuery.append(
                    " and am.datiGenerazione.tipoGenerazione=0 and am.datiGenerazione.dataCreazione=:paramDataCreazione ");
            hqlQuery.append(
                    "order by tipoDocumento.codice, am.documento.entita, am.documento.sedeEntita, partite.codicePagamento ");
        }

        Query query = panjeaDAO.prepareQuery(hqlQuery.toString(), MovimentoFatturazioneDTO.class, null);
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        query.setParameter("paramUtente", utente);
        if (dataCreazione != null) {
            query.setParameter("paramDataCreazione", dataCreazione);
        }

        List<MovimentoFatturazioneDTO> listMovimenti = new ArrayList<MovimentoFatturazioneDTO>();

        try {
            listMovimenti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei movimenti in fatturazione.", e);
            throw new RuntimeException("Errore durante il caricamento dei movimenti in fatturazione.", e);
        }

        logger.debug("--> Exit caricaMovimentiInFatturazione");
        return listMovimenti;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoDocumento> caricaTipiDocumentoDestinazioneFatturazione() {
        logger.debug("--> Enter caricaTipiDocumentoDestinazioneFatturazione");

        Query query = panjeaDAO.prepareNamedQuery("TipoAreaMagazzino.caricaTipiDocumentoDestinazioneFatturazione");
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());

        List<TipoDocumento> list = new ArrayList<TipoDocumento>();

        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei tipi documento di destinazione per la fatturzione", e);
            throw new RuntimeException(
                    "Errore durante il caricamento dei tipi documento di destinazione per la fatturzione", e);
        }

        logger.debug("--> Exit caricaTipiDocumentoDestinazioneFatturazione");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoDocumento> caricaTipiDocumentoPerFatturazione(TipoDocumento tipoDocumentoDiFatturazione) {
        logger.debug("--> Enter caricaTipiAreeMagazzinoPerFatturazione");

        Query query = panjeaDAO.prepareNamedQuery("TipoAreaMagazzino.caricaByTipoDocumentoFatturazione");
        query.setParameter("paramIdTipoDoc", tipoDocumentoDiFatturazione.getId());

        List<TipoDocumento> list = new ArrayList<TipoDocumento>();

        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei tipi documento per tipo documento da fatturare.", e);
            throw new RuntimeException(
                    "Errore durante il caricamento dei tipi documento per tipo documento da fatturare.", e);
        }

        logger.debug("--> Exit caricaTipiAreeMagazzinoPerFatturazione");
        return list;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public DatiGenerazione confermaMovimentiInFatturazione(List<AreaMagazzino> listMovimenti) {
        if (listMovimenti.isEmpty()) {
            return null;
        }
        DatiGenerazione datiGenerazione = listMovimenti.get(0).getDatiGenerazione();

        // memorizzo la data di creazione
        Calendar dataCreazione = Calendar.getInstance();
        dataCreazione.set(Calendar.MILLISECOND, 0);

        int totaleDocumenti = listMovimenti.size();
        int numDoc = 1;
        // lo step di pubblicazione sarà pari al 5% dei documenti da
        // contabilizzare in modo tale da evitare la pubblicazione sulla coda di
        // ogni singolo documento in caso di un numero elevato.
        int stepCoda = totaleDocumenti / 100 * 5;
        if (stepCoda < 1) {
            stepCoda = 1;
        }

        try {
            for (AreaMagazzino areaMagazzino : listMovimenti) {
                confermaMovimentoInFatturazioneManager.confermaMovimentoInFatturazione(areaMagazzino,
                        dataCreazione.getTime());

                if ((numDoc % stepCoda) == 0) {
                    FatturazioneStateDescriptor descriptor = new FatturazioneStateDescriptor(totaleDocumenti, numDoc);
                    panjeaMessage.send(descriptor,
                            MagazzinoDocumentoServiceBean.FATTURAZIONE_CONFERMA_MESSAGE_SELECTOR);
                }

                numDoc++;
            }

            FatturazioneStateDescriptor descriptor = new FatturazioneStateDescriptor(totaleDocumenti, totaleDocumenti);
            panjeaMessage.send(descriptor, MagazzinoDocumentoServiceBean.FATTURAZIONE_CONFERMA_MESSAGE_SELECTOR);
            datiGenerazione.setDataCreazione(dataCreazione.getTime());

        } catch (Exception e) {
            // lancio un evento con lo state descriptor con il job a -1, segnalo
            // così un errore un fase di conferma del documento
            FatturazioneStateDescriptor descriptor = new FatturazioneStateDescriptor(totaleDocumenti, -1);
            panjeaMessage.send(descriptor, MagazzinoDocumentoServiceBean.FATTURAZIONE_CONFERMA_MESSAGE_SELECTOR);
            throw new RuntimeException(e);
        }

        return datiGenerazione;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public void genera(List<AreaMagazzinoLite> areeDaFatturare, TipoDocumento tipoDocumentoDestinazione,
            Date dataDocumentoDestinazione, String noteFatturazione, SedeMagazzinoLite sedePerRifatturazione,
            String utente) throws RigaArticoloNonValidaException, SedePerRifatturazioneAssenteException,
                    SedeNonAppartieneAdEntitaException {
        logger.debug("--> Enter genera");

        if (areeDaFatturare == null || (areeDaFatturare != null && areeDaFatturare.size() == 0)) {
            logger.debug("--> Non ci sono aree, exit genera");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        // Cancello un eventuale fatturazione pendente (chiamo il metodo del
        // preparaFatturazioneManager che apre un transazione per eseguire la
        // query)
        preparaFatturazioneManager.cancellaMovimentiInFatturazione(utente);
        // Genero la fatturazione
        try {
            if (sedePerRifatturazione == null) {
                fatturazioneDifferitaGenerator.genera(areeDaFatturare, dataDocumentoDestinazione, noteFatturazione,
                        null, utente);
            } else {
                rifatturazioneDifferitaGenerator.genera(areeDaFatturare, dataDocumentoDestinazione, noteFatturazione,
                        sedePerRifatturazione, utente);
            }
        } catch (Exception e) {
            // Se ho una eccazione cancello gli eventuali movimenti generati
            // perchè non essendo in una transazione unica non viene fatto il
            // rollback
            preparaFatturazioneManager.cancellaMovimentiInFatturazione(utente);

            if (e instanceof RigaArticoloNonValidaException) {
                throw (RigaArticoloNonValidaException) e;
            } else if (e instanceof SedePerRifatturazioneAssenteException) {
                throw (SedePerRifatturazioneAssenteException) e;
            } else if (e instanceof SedeNonAppartieneAdEntitaException) {
                throw (SedeNonAppartieneAdEntitaException) e;
            } else {
                throw new RuntimeException(e);
            }

        }

        preparaFatturazioneManager.ordinaFatturazione(utente);

        logger.debug("--> Exit genera. Tempo di esecuzione: "
                + (Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()));
    }

    /**
     * @return codice azienda loggata
     */
    private String getCodiceAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    @Override
    public void impostaComeEsportato(DatiGenerazione datiGenerazione) {
        logger.debug("--> Enter impostaComeEsportato");
        Query query = panjeaDAO.prepareNamedQuery("AreaMagazzino.impostaComeEsportato");
        query.setParameter("paramDataGenerazione", datiGenerazione.getDataGenerazione());
        query.setParameter("paramDataCreazione", datiGenerazione.getDataCreazione());
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit impostaComeEsportato");
    }

    @Override
    public void ordinaFatturazioneCorrente(String utente) {
        MagazzinoSettings magazzinoSettings = magazzinoSettingsManagerBean.caricaMagazzinoSettings();
        OrdinamentoFatturazione ordinamentoFatturazione = magazzinoSettings.getOrdinamentoFatturazione();
        StringBuilder sb = new StringBuilder("update docu_documenti ");
        sb.append("inner join ");
        sb.append("( ");
        sb.append("select  @codice:=@codice-1 as contatore,idDocumento,utente from ( ");
        sb.append("   select doc.id as idDocumento,am.utente as utente ");
        sb.append("   from maga_area_magazzino am ");
        sb.append("   inner join docu_documenti doc on doc.id=am.documento_id ");
        sb.append("   inner join anag_entita ent on ent.id=doc.entita_id ");
        sb.append("   inner join anag_anagrafica anag on anag.id=ent.anagrafica_id ");
        sb.append("   inner join anag_sedi_entita se on se.id=doc.sedeEntita_id ");
        sb.append("   inner join anag_sedi_anagrafica sa on se.sede_anagrafica_id=sa.id");
        sb.append("   left join anag_entita entAgente on entAgente.id=se.agente_id ");
        sb.append("   left join anag_zone_geografiche zone on zone.id=se.zonaGeografica_id ");
        sb.append("   where am.statoAreaMagazzino=3 and doc.codiceAzienda=");
        sb.append(PanjeaEJBUtil.addQuote(getCodiceAzienda()));
        sb.append(" and am.utente = ");
        sb.append(PanjeaEJBUtil.addQuote(utente));
        sb.append(ordinamentoFatturazione.getSqlOrdinamento());
        sb.append("   ) derivata  join (SELECT @codice:=0 ) tabcontatore ");
        sb.append(") ");
        sb.append("docsub on docu_documenti.id=docsub.idDocumento ");
        sb.append("set docu_documenti.codice=CONCAT(docsub.contatore,' ',docsub.utente), ");
        // fillo il numero documento order mettendo il simbolo '-' e formattando di 10 il numero
        sb.append(
                " docu_documenti.codiceOrder=CONCAT(LEFT(docsub.contatore,1),LPAD(SUBSTRING(docsub.contatore,2),10,0),' ',docsub.utente) ");
        SqlExecuter executer = new SqlExecuter();
        executer.setSql(sb.toString());
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
    }

}
