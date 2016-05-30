package it.eurotn.panjea.anagrafica.documenti.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.codice.generator.CodiceProtocollo;
import it.eurotn.codice.generator.interfaces.ProtocolloAnnoGenerator;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.exception.DataProtocolloNonValidaException;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.DocumentiServiceBean;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDocumento;
import it.eurotn.panjea.dms.manager.interfaces.DMSAllegatoManager;
import it.eurotn.panjea.dms.manager.interfaces.DMSSettingsManager;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.AreaPreventivoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.settings.SettingsServerMBean;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.security.JecPrincipal;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.DocumentiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentiManager")
public class DocumentiManagerBean implements DocumentiManager {

    private static Logger logger = Logger.getLogger(DocumentiServiceBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    @IgnoreDependency
    private AreaContabileManager areaContabileManager;

    @EJB
    @IgnoreDependency
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    @IgnoreDependency
    private AreaTesoreriaManager areaTesoreriaManager;

    @EJB
    @IgnoreDependency
    private AreaOrdineManager areaOrdineManager;

    @EJB(beanName = "DocumentoProtocolloAnnoGeneratorBean")
    private ProtocolloAnnoGenerator protocolloAnnoGenerator;

    @EJB
    @IgnoreDependency
    private AreaPreventivoManager areaPreventivoManager;

    @EJB(beanName = "DMSAllegatoManagerBean")
    private DMSAllegatoManager dmsAllegatoManager;

    @EJB
    private DMSSettingsManager dmsSettingsManager;

    @EJB
    private SettingsServerMBean settingsServer;

    @Override
    public void cancellaDocumento(Documento documento) {
        logger.debug("--> Enter cancellaDocumento");
        try {
            panjeaDAO.delete(documento);
            protocolloAnnoGenerator.restoreCodice(documento);
        } catch (Exception e) {
            logger.error("--> Errore nel cancellare il documento " + documento, e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaDocumento");
    }

    @Override
    public List<Object> caricaAreeDocumento(Integer idDocumento) {
        logger.debug("--> Exit caricaAreeLiteByAreaPartita");

        Documento documento = panjeaDAO.loadLazy(Documento.class, idDocumento);

        List<Object> listAree = new ArrayList<Object>();

        // carico l'area contabile
        AreaContabileLite areaContabile = areaContabileManager.caricaAreaContabileLiteByDocumento(documento);
        if (areaContabile != null) {
            listAree.add(areaContabile);
        }

        // carico l'area magazzino
        AreaMagazzino areaMagazzino = areaMagazzinoManager.caricaAreaMagazzinoByDocumento(documento);
        if (areaMagazzino != null) {
            listAree.add(areaMagazzino);
        }

        // carico l'area tesoreria
        AreaTesoreria areaTesoreria = areaTesoreriaManager.caricaAreaTesoreria(documento);
        if (areaTesoreria != null && areaTesoreria.getId() != null) {
            listAree.add(areaTesoreria);
        }

        // carico l'area ordine
        AreaOrdine areaOrdine = areaOrdineManager.caricaAreaOrdineByDocumento(documento);
        if (areaOrdine != null) {
            listAree.add(areaOrdine);
        }

        // carico l'area preventivo
        AreaPreventivo areaPreventivo = areaPreventivoManager.caricaAreaPreventivoByDocumento(documento);
        if (areaPreventivo != null) {
            listAree.add(areaPreventivo);
        }

        logger.debug("--> Exit caricaAreeLiteByAreaPartita");
        return listAree;
    }

    // @SuppressWarnings("unchecked")
    // @Override
    // public List<Documento> caricaDocumentiCollegati(Documento documento, boolean isDestinazione)
    // {
    // logger.debug("--> Enter caricaDocumentiCollegati");
    // StringBuilder queryLink = new StringBuilder();
    // queryLink.append("select ds.id as id, ");
    // queryLink.append("ds.codice as codice, ");
    // queryLink.append("ds.dataDocumento as dataDocumento, ");
    // queryLink.append("td.id as idTipoDocumento, ");
    // queryLink.append("td.codice as codiceTipoDocumento, ");
    // queryLink.append("td.descrizione as descrizioneTipoDocumento, ");
    // queryLink.append("e.id as idEntita, ");
    // queryLink.append("e.codice as codiceEntita, ");
    // queryLink.append("a.denominazione as denominazioneAnagrafica, ");
    // queryLink.append("ds.totale as totale ");
    // queryLink.append("from LinkDocumento l ");
    // if (isDestinazione) {
    // queryLink.append("join l.documentoOrigine ds ");
    // queryLink.append("join ds.tipoDocumento td ");
    // queryLink.append("join ds.entita e ");
    // queryLink.append("join e.anagrafica a ");
    // queryLink.append("where l.documentoDestinazione=:paramDoc ");
    // } else {
    // queryLink.append("join l.documentoDestinazione ds ");
    // queryLink.append("join ds.tipoDocumento td ");
    // queryLink.append("join ds.entita e ");
    // queryLink.append("join e.anagrafica a ");
    // queryLink.append("where l.documentoOrigine=:paramDoc ");
    // }
    // Query query = panjeaDAO.prepareQuery(queryLink.toString());
    // ((QueryImpl)
    // query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((Documento.class)));
    //
    // query.setParameter("paramDoc", documento);
    // List<Documento> documenti = query.getResultList();
    // logger.debug("--> Exit caricaDocumentiCollegati " + documenti.size());
    // return documenti;
    // }

    @Override
    public Documento caricaDocumento(int idDocumento) {
        logger.debug("--> Enter caricaDocumento");
        return caricaDocumento(idDocumento, false);
    }

    @Override
    public Documento caricaDocumento(int idDocumento, boolean initDocumentiCollegati) {
        try {
            Documento documento2 = panjeaDAO.load(Documento.class, idDocumento);
            if (initDocumentiCollegati) {
                Hibernate.initialize(documento2.getDocumentiDestinazione());
                Hibernate.initialize(documento2.getDocumentiOrigine());
            }
            logger.debug("--> Exit caricaDocumento");
            return documento2;
        } catch (ObjectNotFoundException e) {
            logger.error("--> documento non trovato. ID cercato=" + idDocumento);
            throw new RuntimeException("--> documento non trovato. ID cercato=" + idDocumento, e);
        }
    }

    @Override
    public int caricaNumeroAreeCollegate(Documento documento) {
        String sqlAc = new StringBuilder("select ac.id from AreaContabile ac where ac.documento.id=:paramIdDocumento")
                .toString();
        String sqlAm = new StringBuilder("select am.id from AreaMagazzino am where am.documento.id=:paramIdDocumento")
                .toString();
        String sqlAt = new StringBuilder("select at.id from AreaTesoreria at where at.documento.id=:paramIdDocumento")
                .toString();
        String sqlAo = new StringBuilder("select ao.id from AreaOrdine ao where ao.documento.id=:paramIdDocumento")
                .toString();
        String sqlAp = "select ap.id from AreaPreventivo ap where ap.documento.id=:paramIdDocumento";

        String[] stringQueries = new String[] { sqlAc, sqlAm, sqlAt, sqlAo, sqlAp };
        int numeroAree = 0;
        for (String string : stringQueries) {
            Query query = panjeaDAO.prepareQuery(string);
            query.setParameter("paramIdDocumento", documento.getId());
            @SuppressWarnings("unchecked")
            List<Integer> resultIdAreeDocumento = query.getResultList();
            if (resultIdAreeDocumento.size() > 0) {
                numeroAree++;
            }
        }

        // per l'area installazione faccio una query sql perchè il plugin potrebbe non esserci mentre invece nel
        // database trovo comunque la tabella
        String sqlInst = "select inst.id from manu_area_installazioni inst where inst.documento_id = :paramIdDocumento";
        SQLQuery query = panjeaDAO.prepareNativeQuery(sqlInst);
        query.setParameter("paramIdDocumento", documento.getId());
        @SuppressWarnings("unchecked")
        List<Integer> result = query.list();
        if (result.size() > 0) {
            numeroAree++;
        }

        return numeroAree;
    }

    /**
     *
     * @return codiceAzienda
     */
    private String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * @return principal corrente
     */
    private JecPrincipal getPrincipal() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal;

    }

    @Override
    public boolean isDocumentoUnivoco(Documento documento) {
        logger.debug("--> Enter isDocumentoUnivoco");
        List<Documento> list = ricercaDocumentiByChiaveDominio(documento);
        if (documento.getId() != null) {
            list.remove(documento);
        }
        if (list.size() > 0) {
            logger.warn("--> Trovato documento con chiavi di dominio corrispondenti " + list.get(0));
            return false;
        } else {
            logger.debug("--> Exit isAreaContabileUnivoca");
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Documento> ricercaDocumenti(ParametriRicercaDocumento parametriRicercaDocumento) {
        logger.debug("--> Enter caricaDocumentiContratto");
        StringBuilder billy = new StringBuilder();
        billy.append("select ");
        billy.append("d.id as id, ");
        billy.append("d.version as version, ");
        billy.append("d.codice as codice, ");
        billy.append("d.dataDocumento as dataDocumento, ");
        billy.append("td.id as idTipoDocumento, ");
        billy.append("td.codice as codiceTipoDocumento, ");
        billy.append("td.descrizione as descrizioneTipoDocumento, ");
        billy.append("e.id as idEntita, ");
        billy.append("e.codice as codiceEntita, ");
        billy.append("a.denominazione as denominazioneAnagrafica, ");
        billy.append("d.totale as totale ");
        billy.append("from Documento d ");
        billy.append("inner join d.tipoDocumento td ");
        billy.append("inner join d.entita e ");
        billy.append("inner join e.anagrafica a ");
        billy.append("where d.codiceAzienda=:paramCodiceAzienda  ");

        Map<String, Object> valueParametri = new TreeMap<String, Object>();
        EntitaLite entita = parametriRicercaDocumento.getEntita();

        if (entita != null) {
            billy.append("and d.entita.id=:paramIdEntita ");
            valueParametri.put("paramIdEntita", entita.getId());
        }
        if (parametriRicercaDocumento.getIdContratto() != null) {
            billy.append("and d.contrattoSpesometro.id=:paramIdContratto ");
            valueParametri.put("paramIdContratto", parametriRicercaDocumento.getIdContratto());
        }
        if (parametriRicercaDocumento.getCodice() != null) {
            billy.append("and str(d.codice) like :paramCodiceDocumento ");
            valueParametri.put("paramCodiceDocumento", parametriRicercaDocumento.getCodice() + "%");
        }
        if (parametriRicercaDocumento.getDataDocumento() != null) {
            billy.append("and str(d.dataDocumento) like :paramDataDocumento ");
            valueParametri.put("paramDataDocumento", "%" + parametriRicercaDocumento.getDataDocumento() + "%");
        }
        billy.append("order by d.dataDocumento, d.codice ");

        Query query = panjeaDAO.prepareQuery(billy.toString());
        ((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((Documento.class)));

        query.setParameter("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
        for (Entry<String, Object> entry : valueParametri.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Documento> documenti = query.getResultList();
        logger.debug("--> Exit caricaDocumentiContratto " + documenti.size());
        return documenti;
    }

    /**
     * @param documento
     *            documento da cercare tramite la chiave di dominio (entità,codice,dataDocumento,tipoDocumento)
     * @return lista di documenti trovati
     */
    @SuppressWarnings("unchecked")
    private List<Documento> ricercaDocumentiByChiaveDominio(Documento documento) {
        logger.debug("--> Enter ricercaDocumenti");
        StringBuffer queryStringHql = new StringBuffer(
                " from Documento d where d.codiceAzienda = :paramCodiceAzienda ");
        Map<String, Object> paramHql = new HashMap<String, Object>();
        paramHql.put("paramCodiceAzienda", documento.getCodiceAzienda());
        // verifica tutti gli attributi interessati di documento ignorando
        // che questi siano a null
        queryStringHql.append(" and d.codice.codice = :paramCodice ");
        paramHql.put("paramCodice", documento.getCodice().getCodice());

        if (documento.getTipoDocumento().getRegistroProtocollo() != null) {
            queryStringHql.append(" and d.tipoDocumento.registroProtocollo = :paramProtocollo ");
            paramHql.put("paramProtocollo", documento.getTipoDocumento().getRegistroProtocollo());
        } else {
            queryStringHql.append(" and d.tipoDocumento = :paramIdTipoDocumento ");
            paramHql.put("paramIdTipoDocumento", documento.getTipoDocumento());
        }

        switch (documento.getTipoDocumento().getTipoEntita()) {
        case CLIENTE:
            // se è cliente sono documenti che emetto e devono essere valutati
            // univoci senza considerare l'entità
            queryStringHql.append(" and YEAR(d.dataDocumento) = :paramAnno ");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(documento.getDataDocumento());
            paramHql.put("paramAnno", calendar.get(Calendar.YEAR));
            break;
        case FORNITORE:
            queryStringHql.append(" and d.entita = :paramEntita ");
            paramHql.put("paramEntita", documento.getEntita());

            queryStringHql.append(" and d.dataDocumento = :paramDataDocumento ");
            paramHql.put("paramDataDocumento", documento.getDataDocumento());
            break;
        default:
            // se non è cliente o fornitore l'entità deve essere a null
            queryStringHql.append(" and d.entita is null ");

            queryStringHql.append(" and d.dataDocumento = :paramDataDocumento ");
            paramHql.put("paramDataDocumento", documento.getDataDocumento());
            break;
        }

        if (TipoEntita.BANCA.equals(documento.getTipoDocumento().getTipoEntita())) {
            queryStringHql.append(" and d.rapportoBancarioAzienda = :paramRapportoBancario ");
            paramHql.put("paramRapportoBancario", documento.getRapportoBancarioAzienda());
        } else {
            queryStringHql.append(" and d.rapportoBancarioAzienda is null ");
        }

        Query query = panjeaDAO.prepareQuery(queryStringHql.toString());
        for (String parameterName : paramHql.keySet()) {
            query.setParameter(parameterName, paramHql.get(parameterName));
        }
        List<Documento> documenti;
        try {
            documenti = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile ottenere la list di Documenti ", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit ricercaDocumenti");
        return documenti;
    }

    @Override
    public Documento salvaDocumento(Documento documento) throws DocumentoDuplicateException {
        logger.debug("--> Enter salvaDocumento");
        Documento documento2 = null;
        if (documento.isNew()) {
            documento.setCodiceAzienda(getPrincipal().getCodiceAzienda());
        }

        documento2 = salvaDocumentoNoCheck(documento);

        if (!isDocumentoUnivoco(documento2)) {
            context.setRollbackOnly();
            logger.warn(
                    "--> Area contabile non univoca secondo le chiavi di dominio data doc, num. doc, tipo doc, entita'");
            throw new DocumentoDuplicateException("Documento duplicato ", documento);
        }
        logger.debug("--> Exit salvaDocumento");
        return documento2;
    }

    @Override
    public Documento salvaDocumentoNoCheck(Documento documento) {

        // carico il vecchio documento se esiste per poi usarlo per verificare i cambiamenti con
        // quello eventualmente
        // presente nella gestione documentale
        Documento docOld = null;
        if (!documento.isNew()) {
            // ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(documento);
            Query queryOldValue = panjeaDAO.prepareQuery("select doc from Documento doc where id = :paramID");
            queryOldValue.setParameter("paramID", documento.getId());

            try {
                docOld = (Documento) panjeaDAO.getSingleResult(queryOldValue);
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(docOld);
            } catch (Exception e) {
                docOld = null;
            }
        }

        Documento documentoSalvato = null;

        // se il documento non ha un codice lo genero automaticamente
        if (documento.getCodice().isEmpty()) {
            CodiceProtocollo codice = protocolloAnnoGenerator.nextCodice(documento);
            documento.getCodice().setCodice(codice.getCodice());
            documento.setValoreProtocollo(codice.getValoreProtocollo());
        }

        verificaDataDocumentoValoreProtocollo(documento);

        try {
            documentoSalvato = panjeaDAO.save(documento);

            // verifico se devo aggiornare il documento presente nella gestione documentale
            if (settingsServer.isDmsEnable()) {
                if (docOld != null
                        && (documentoSalvato.getDataDocumento().compareTo(docOld.getDataDocumento()) != 0
                                || !Objects.equals(documentoSalvato.getEntita(), docOld.getEntita())
                                || !Objects.equals(documentoSalvato.getCodice().getCodice(),
                                        docOld.getCodice().getCodice())
                        || !Objects.equals(documentoSalvato.getTipoDocumento(), docOld.getTipoDocumento()))) {
                    AllegatoDocumento allegatoDocumento = new AllegatoDocumento(docOld, getCodiceAzienda());
                    List<WSDocument> allegati = dmsAllegatoManager.getAllegati(allegatoDocumento);

                    if (allegati != null && !allegati.isEmpty()) {
                        allegatoDocumento = new AllegatoDocumento(documentoSalvato, getCodiceAzienda());
                        String folder = dmsSettingsManager.caricaDmsSettings().getFolder(documentoSalvato);
                        dmsAllegatoManager.updateDocuments(allegati, allegatoDocumento, folder,
                                documentoSalvato.getDataDocumento());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("--> errore in salva documento ", e);
            throw new RuntimeException(e);
        }
        return documentoSalvato;
    }

    private void verificaDataDocumentoValoreProtocollo(Documento documento) throws DataProtocolloNonValidaException {

        StringBuilder sb = new StringBuilder(200);
        sb.append("select doc.dataDocumento as dataDoc, ");
        sb.append("   doc.codice as docDoc, ");
        sb.append("   doc.valoreProtocollo as valProtDoc, ");
        sb.append("   prot.codice as docProt, ");
        sb.append("   prot.descrizione as descProt ");
        sb.append(
                "from docu_documenti doc inner join docu_tipi_documento tipoDoc on doc.tipo_documento_id = tipoDoc.id ");
        sb.append("                 inner join code_protocolli prot on tipoDoc.registroProtocollo = prot.codice ");
        sb.append("where tipoDoc.id = :paramTipoDoc ");
        sb.append("  and doc.valoreProtocollo < :paramValoreProtocollo ");
        sb.append("  and YEAR(doc.dataDocumento) = :paramAnnoDocumento ");
        sb.append("order by doc.valoreProtocollo desc ");
        sb.append("limit 1 ");

        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.addScalar("dataDoc");
        query.addScalar("docDoc");
        query.addScalar("valProtDoc");
        query.addScalar("docProt");
        query.addScalar("descProt");
        query.setParameter("paramTipoDoc", documento.getTipoDocumento().getId());
        query.setParameter("paramValoreProtocollo", documento.getValoreProtocollo());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(documento.getDataDocumento());
        query.setParameter("paramAnnoDocumento", calendar.get(Calendar.YEAR));

        Object[] result = (Object[]) query.uniqueResult();

        if (result != null && documento.getDataDocumento().before((Date) result[0])) {
            Date dataDoc = (Date) result[0];
            String numDoc = (String) result[1];
            Integer valoreProtocolloDoc = (Integer) result[2];
            String codiceProtocollo = (String) result[3];
            String descrizioneProtocollo = (String) result[4];

            throw new DataProtocolloNonValidaException(documento.getDataDocumento(), documento.getValoreProtocollo(),
                    dataDoc, numDoc, valoreProtocolloDoc, codiceProtocollo, descrizioneProtocollo, null);
        }
    }

}
