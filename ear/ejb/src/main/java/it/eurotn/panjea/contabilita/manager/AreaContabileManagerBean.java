/**
 *
 */
package it.eurotn.panjea.contabilita.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.CodiceProtocollo;
import it.eurotn.codice.generator.interfaces.ProtocolloAnnoGenerator;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.exception.DataProtocolloNonValidaException;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.TipoDocumentoManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaContabileRateiRisconti;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileVerificaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StatoAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces.RateoRiscontoManager;
import it.eurotn.panjea.contabilita.manager.rigacontabilebuider.EntitaRigaContabileBuilder;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.RigheContabiliNonValidiException;
import it.eurotn.panjea.contabilita.service.exception.StatoAreaContabileNonValidoException;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.FatturatoDTO;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author Leonardo list roles: visualizzaAreaContabile, modificaAreaContabile, cancellaAreaContabile,
 *         visualizzaTipoAreaContabile, amministraGiornale, amministraRegistroIva,
 */
@Stateless(name = "Panjea.AreaContabileManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaContabileManager")
public class AreaContabileManagerBean implements AreaContabileManager {

    private static Logger logger = Logger.getLogger(AreaContabileManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private RateoRiscontoManager rateoRiscontoManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB(mappedName = "Panjea.DocumentiManager")
    private DocumentiManager documentiManager;

    @EJB
    private StatoAreaContabileManager statoAreaContabileManager;

    @EJB
    private AreaContabileVerificaManager areaContabileVerificaManager;

    @EJB
    private TipiAreaContabileManager tipiAreaContabileManager;

    @EJB
    @IgnoreDependency
    private AreaRateManager areaRateManager;

    @EJB
    @IgnoreDependency
    private TipoDocumentoManager tipoDocumentoManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private PianoContiManager pianoContiManager;

    @EJB
    private RitenutaAccontoContabilitaManager ritenutaAccontoContabilitaManager;

    @EJB
    @IgnoreDependency
    private StrutturaContabileManager strutturaContabileManager;

    @IgnoreDependency
    @EJB(beanName = "AreaContabileProtocolloAnnoGeneratorBean")
    private ProtocolloAnnoGenerator protocolloAnnoGenerator;

    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void aggiornaAreeContabiliPerGiornale(Map<AreaContabileDTO, Integer> mapAreeContabili)
            throws ContabilitaException {
        logger.debug("--> Enter aggiornaAreeContabiliPerGiornale");

        StringBuilder sb = new StringBuilder();
        sb.append(
                "update cont_area_contabile ac inner join cont_tipi_area_contabile tac on ac.tipoAreaContabile_id = tac.id ");
        sb.append("						inner join docu_tipi_documento tipoDoc on tipoDoc.id = tac.tipoDocumento_id ");
        sb.append("set ac.numeroPaginaGiornale = :paramNumeroPagina, ");
        sb.append(
                "    ac.statoAreaContabile = if(tipoDoc.righeIvaEnable=false or (tipoDoc.righeIvaEnable=true and ac.stampatoSuRegistro=true),1,ac.statoAreaContabile) ");
        sb.append("where ac.id = :paramIdArea ");

        Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());

        for (Entry<AreaContabileDTO, Integer> entry : mapAreeContabili.entrySet()) {
            query.setParameter("paramIdArea", entry.getKey().getId());
            query.setParameter("paramNumeroPagina", entry.getValue());

            query.executeUpdate();
        }
        logger.debug("--> Exit aggiornaAreeContabiliPerGiornale");
    }

    @Override
    public void aggiornaAreeContabiliPerRegistroIva(GiornaleIva giornaleIva,
            Map<AreaContabileDTO, Integer> mapAreeContabili) {
        logger.debug("--> Enter aggiornaAreeContabiliPerRegistroIva");
        try {
            Set<AreaContabileDTO> keysAree = mapAreeContabili.keySet();

            AreaContabile areaContabile;
            for (AreaContabileDTO areaContabileDTO : keysAree) {

                areaContabile = caricaAreaContabile(areaContabileDTO.getId());

                // il numero pagina giornale e' solo per il libro giornale per il registro iva c'e' la proprieta'
                // stampato su registro.
                areaContabile.setStampatoSuRegistro(true);
                areaContabile.setAnnoIva(giornaleIva.getAnno());

                // se ho stampato il giornale posso portare a verificato lo stato documento
                if (areaContabile.getNumeroPaginaGiornale() != null
                        && areaContabile.getNumeroPaginaGiornale().intValue() != 0) {
                    areaContabile.setStatoAreaContabile(StatoAreaContabile.VERIFICATO);
                }

                try {
                    // passo per l'entityManager per evitare l'history e il check sul codice nel salvataggio
                    // dell'areacontabile
                    panjeaDAO.getEntityManager().merge(areaContabile);
                    panjeaDAO.getEntityManager().flush();
                } catch (Exception e) {
                    logger.debug("--> Errore nel salvataggio di area contabile " + areaContabileDTO.getId(), e);
                    throw new ContabilitaException(
                            "--> Errore nel salvataggio di area contabile " + areaContabileDTO.getId(), e);
                }
            }
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio dell'area contabile", e);
            throw new RuntimeException("--> Errore durante il salvataggio dell'area contabile", e);
        }
        logger.debug("--> Exit aggiornaAreeContabiliPerRegistroIva");
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void aggiornaRigheContabiliPerGiornale(Map<RigaContabileDTO, List<Integer>> mapRigheContabili)
            throws ContabilitaException {
        logger.debug("--> Enter aggiornaRigheContabiliPerGiornale");

        Set<RigaContabileDTO> keysRighe = mapRigheContabili.keySet();

        Query query = panjeaDAO.prepareNamedQuery("RigaContabile.aggiornaLibroGiornale");
        for (Object element : keysRighe) {
            RigaContabileDTO rigaContabileDTO = (RigaContabileDTO) element;
            query.setParameter("paramPaginaGiornale", mapRigheContabili.get(rigaContabileDTO).get(0));
            query.setParameter("paramNumeroRigaGiornale", mapRigheContabili.get(rigaContabileDTO).get(1));
            query.setParameter("paraRigaContabileId", rigaContabileDTO.getId());

            try {
                panjeaDAO.executeQuery(query);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        logger.debug("--> Exit aggiornaRigheContabiliPerGiornale");
    }

    @Override
    public AreaContabile caricaAreaContabile(Integer id) {
        logger.debug("--> Enter caricaAreaContabile");
        try {
            AreaContabile areaContabile = panjeaDAO.load(AreaContabile.class, id);
            logger.debug("--> Exit caricaAreaContabile");
            return areaContabile;
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore impossibile recuperare AreaContabile identificata da " + id, e);
            throw new RuntimeException("Impossibile recuperare AreaContabile identificata da " + id, e);
        }
    }

    @Override
    public AreaContabile caricaAreaContabileByChiaveImportazione(String chiaveImportazione)
            throws ContabilitaException {
        logger.debug("--> Enter ricercaAreaContabilePerChiaveImportazione");
        Query query = panjeaDAO.prepareQuery(
                " from AreaContabile b where  b.documento.codiceAzienda = :paramCodiceAzienda and b.chiaveImportazione = :paramChiaveImportazione ");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramChiaveImportazione", chiaveImportazione);
        AreaContabile areaContabile = null;
        try {
            areaContabile = (AreaContabile) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            logger.debug(
                    "--> AreaContabile identificata da chiaveImportazione " + chiaveImportazione + " non trovata ");
        } catch (DAOException e) {
            logger.error("--> errore, impossibile ottenere AreaContabile per chiaveImportazione " + chiaveImportazione
                    + " e azienda  " + getAzienda(), e);
            throw new ContabilitaException(e);
        }

        logger.debug("--> Exit ricercaAreaContabilePerChiaveImportazione");
        return areaContabile;
    }

    @Override
    public AreaContabile caricaAreaContabileByDocumento(Integer idDocumento) {
        logger.debug("--> Enter caricAreaContabileByDocumento");
        Query query = panjeaDAO.prepareNamedQuery("AreaContabile.ricercaAreaByDocumento");
        query.setParameter("paramIdDocumento", idDocumento);

        AreaContabile areaContabile = null;
        try {
            areaContabile = (AreaContabile) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException ex) {
            logger.warn("--> Nessuna area contabile trovata per il documento " + idDocumento);
        } catch (DAOException ex) {
            logger.error("--> Errore durante la ricerca dell'area contabile.", ex);
            throw new RuntimeException("Errore durante la ricerca dell'area contabile.", ex);
        }
        logger.debug("--> Exit caricAreaContabileByDocumento");
        return areaContabile;
    }

    @Override
    public AreaContabileLite caricaAreaContabileLiteByDocumento(Documento documento) {
        Query query = panjeaDAO.prepareNamedQuery("AreaContabileLite.ricercaAreaByDocumento");
        query.setParameter("paramIdDocumento", documento.getId());

        AreaContabileLite areaContabileLite = null;
        try {
            areaContabileLite = (AreaContabileLite) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException ex) {
            logger.warn("--> Nessuna area contabile trovata per il documento " + documento.getId());
        } catch (DAOException ex) {
            logger.error("--> Errore durante la ricerca dell'area contabile.", ex);
            throw new RuntimeException("Errore durante la ricerca dell'area contabile.", ex);
        }
        logger.debug("--> Exit caricAreaContabileByDocumento");
        return areaContabileLite;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AreaContabile> caricaDocumentiContabili() throws ContabilitaException {
        logger.debug("--> Enter caricaDocumentiContabili");
        Map<String, Object> valueParametri = new TreeMap<String, Object>();
        StringBuffer queryHQL = new StringBuffer(" select ac from AreaContabile ac inner join ac.documento d ");
        StringBuffer whereHQL = new StringBuffer(" where d.codiceAzienda = :paramCodiceAzienda ");
        JecPrincipal jecPrincipal = getPrincipal();
        valueParametri.put("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());
        Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString());
        Set<String> set = valueParametri.keySet();
        for (String key : set) {
            query.setParameter(key, valueParametri.get(key));
        }
        List<AreaContabile> list;
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore in ricercaDocumentiContabili", e);
            throw new ContabilitaException("Impossibile recuperare la lista di AreaContabile ", e);
        }
        logger.debug("--> Exit ricercaDocumentiContabili");
        return list;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FatturatoDTO> caricaFatturato(Integer annoCompetenza, Date dataRegistrazioneIniziale,
            Date dataRegistrazioneFinale, TipoEntita tipoEntita) {
        logger.debug("--> Enter caricaFatturato");

        List<FatturatoDTO> fatturato = new ArrayList<FatturatoDTO>();

        StringBuilder sb = new StringBuilder();
        sb.append("select ent.id as entitaId, ");
        sb.append("	  ent.codice as entitaCodice, ");
        sb.append("	  anag.denominazione as entitaDenominazione, ");
        sb.append("	  sum(rigaIva.importoInValutaAziendaImponibile) as totaleImponibile, ");
        sb.append("	  sum(rigaIva.importoInValutaAziendaImposta) as totaleIVA, ");
        sb.append(
                "	  (select count(doc2.id) from cont_aree_iva ai2 inner join cont_area_contabile ac2 on ac2.documento_id=ai2.documento_id ");
        sb.append(
                "					   					   inner join docu_documenti doc2 on doc2.id=ai2.documento_id ");
        sb.append("					   					   inner join anag_entita ent2 on ent2.id=doc2.entita_id ");
        sb.append(
                "						where ent2.id=ent.id and ac2.annoMovimento = :annoCompetenza and ac2.dataRegistrazione >= :paramDataIniziale and ac2.dataRegistrazione <= :paramDataFinale and ac2.statoAreaContabile in (0,1) ");
        sb.append(
                "                                      and exists ( select id from cont_righe_iva rigaIva2 where rigaIva2.areaIva_id=ai2.id)) as numeroDocumenti ");
        sb.append("from cont_righe_iva rigaIva inner join cont_aree_iva ai on ai.id=rigaIva.areaIva_id ");
        sb.append("					   inner join cont_area_contabile ac on ac.documento_id=ai.documento_id ");
        sb.append("					   inner join docu_documenti doc on doc.id=ai.documento_id ");
        sb.append("					   inner join anag_entita ent on ent.id=doc.entita_id ");
        sb.append("					   inner join anag_anagrafica anag on anag.id=ent.anagrafica_id ");
        sb.append(
                "where ac.annoMovimento = :annoCompetenza and ac.dataRegistrazione >= :paramDataIniziale and ac.dataRegistrazione <= :paramDataFinale and ac.statoAreaContabile in (0,1) and ent.TIPO_ANAGRAFICA = :paramTipoEntita ");
        sb.append("group by ent.id ");
        sb.append("order by anag.denominazione");

        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(sb.toString());
        SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
        sqlQuery.setParameter("annoCompetenza", annoCompetenza);
        sqlQuery.setParameter("paramDataIniziale", dataRegistrazioneIniziale);
        sqlQuery.setParameter("paramDataFinale", dataRegistrazioneFinale);
        switch (tipoEntita) {
        case CLIENTE:
            sqlQuery.setParameter("paramTipoEntita", "C");
            break;
        case FORNITORE:
            sqlQuery.setParameter("paramTipoEntita", "F");
            break;
        default:
            throw new RuntimeException("Tipo entità non prevista.");
        }
        sqlQuery.setResultTransformer(Transformers.aliasToBean(FatturatoDTO.class));

        try {
            sqlQuery.addScalar("entitaId");
            sqlQuery.addScalar("entitaCodice");
            sqlQuery.addScalar("entitaDenominazione");
            sqlQuery.addScalar("totaleImponibile");
            sqlQuery.addScalar("totaleIVA");
            sqlQuery.addScalar("numeroDocumenti", Hibernate.INTEGER);
            fatturato = sqlQuery.list();
        } catch (Exception e) {
            logger.error("--> errore in caricaFatturato", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit caricaFatturato");
        return fatturato;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FatturatoDTO> caricaFatturatoPerSede(Integer annoCompetenza, Date dataRegistrazioneIniziale,
            Date dataRegistrazioneFinale, TipoEntita tipoEntita) {

        logger.debug("--> Enter caricaFatturatoPerSede");

        List<FatturatoDTO> fatturato = new ArrayList<FatturatoDTO>();

        StringBuilder sb = new StringBuilder();
        sb.append("select distinct ent.id as entitaId, ");
        sb.append("	  ent.codice as entitaCodice, ");
        sb.append("	  anag.denominazione as entitaDenominazione, ");
        sb.append("	  se.id as sedeEntitaId, ");
        sb.append("	  se.codice as sedeEntitaCodice, ");
        sb.append("	  sa.descrizione as sedeEntitaDescrizione, ");
        sb.append("	  sa.indirizzo as sedeEntitaIndirizzo, ");
        sb.append("	  sum(rigaIva.importoInValutaAziendaImponibile) as totaleImponibile, ");
        sb.append("	  sum(rigaIva.importoInValutaAziendaImposta) as totaleIVA, ");
        sb.append(
                "	  (select count(doc2.id) from cont_aree_iva ai2 inner join cont_area_contabile ac2 on ac2.documento_id=ai2.documento_id ");
        sb.append(
                "					   					   inner join docu_documenti doc2 on doc2.id=ai2.documento_id ");
        sb.append(
                "					   					   inner join anag_sedi_entita se2 on se2.id=doc2.sedeEntita_id ");
        sb.append("					   					   inner join anag_entita ent2 on ent2.id=se2.entita_id ");
        sb.append(
                "						where se2.id=se.id and ac2.annoMovimento = :annoCompetenza and ac2.dataRegistrazione >= :paramDataIniziale and ac2.dataRegistrazione <= :paramDataFinale and ac2.statoAreaContabile in (0,1) ");
        sb.append(
                "                                      and exists ( select id from cont_righe_iva rigaIva2 where rigaIva2.areaIva_id=ai2.id)) as numeroDocumenti ");
        sb.append("from cont_righe_iva rigaIva inner join cont_aree_iva ai on ai.id=rigaIva.areaIva_id ");
        sb.append("					   inner join cont_area_contabile ac on ac.documento_id=ai.documento_id ");
        sb.append("					   inner join docu_documenti doc on doc.id=ai.documento_id ");
        sb.append("					   inner join anag_sedi_entita se on se.id=doc.sedeEntita_id ");
        sb.append("					   inner join anag_sedi_anagrafica sa on se.sede_anagrafica_id=sa.id ");
        sb.append("					   inner join anag_entita ent on ent.id=se.entita_id ");
        sb.append("					   inner join anag_anagrafica anag on anag.id=ent.anagrafica_id ");
        sb.append(
                "where ac.annoMovimento = :annoCompetenza and ac.dataRegistrazione >= :paramDataIniziale and ac.dataRegistrazione <= :paramDataFinale and ac.statoAreaContabile in (0,1) and ent.TIPO_ANAGRAFICA = :paramTipoEntita ");
        sb.append("group by se.id ");
        sb.append("order by anag.denominazione ");

        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(sb.toString());
        SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
        sqlQuery.setParameter("annoCompetenza", annoCompetenza);
        sqlQuery.setParameter("paramDataIniziale", dataRegistrazioneIniziale);
        sqlQuery.setParameter("paramDataFinale", dataRegistrazioneFinale);
        switch (tipoEntita) {
        case CLIENTE:
            sqlQuery.setParameter("paramTipoEntita", "C");
            break;
        case FORNITORE:
            sqlQuery.setParameter("paramTipoEntita", "F");
            break;
        default:
            throw new RuntimeException("Tipo entità non prevista.");
        }
        sqlQuery.setResultTransformer(Transformers.aliasToBean(FatturatoDTO.class));

        try {
            sqlQuery.addScalar("entitaId");
            sqlQuery.addScalar("entitaCodice");
            sqlQuery.addScalar("entitaDenominazione");
            sqlQuery.addScalar("sedeEntitaId");
            sqlQuery.addScalar("sedeEntitaCodice");
            sqlQuery.addScalar("sedeEntitaDescrizione");
            sqlQuery.addScalar("sedeEntitaIndirizzo");
            sqlQuery.addScalar("totaleImponibile");
            sqlQuery.addScalar("totaleIVA");
            sqlQuery.addScalar("numeroDocumenti", Hibernate.INTEGER);
            fatturato = sqlQuery.list();
        } catch (Exception e) {
            logger.error("--> errore in caricaFatturatoPerSede", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit caricaFatturatoPerSede");
        return fatturato;
    }

    @Override
    public RigaContabile caricaRigaContabile(Integer id) throws ContabilitaException {
        logger.debug("--> Enter caricaRigaContabile");
        try {
            RigaContabile rigaContabile = panjeaDAO.load(RigaContabile.class, id);
            rigaContabile.getRigheCentroCosto().size();
            rigaContabile.getAreaContabile().getDocumento().getId();
            rigaContabile.getRigheRateoRisconto().size();
            for (RigaRateoRisconto rateoRisconto : rigaContabile.getRigheRateoRisconto()) {
                for (RigaRiscontoAnno rigaRiscontoAnno : rateoRisconto.getRateiRiscontiAnno()) {
                    if (rigaRiscontoAnno.getRigaContabile() != null) {
                        rigaRiscontoAnno.getRigaContabile().getAreaContabile().getDocumento().getId();
                    }
                }
            }

            if (rigaContabile instanceof RigaContabileRateiRisconti) {
                ((RigaContabileRateiRisconti) rigaContabile).setDocumentiRiscontiCollegati(rateoRiscontoManager
                        .caricaDocumentiCollegatiRisconto((RigaContabileRateiRisconti) rigaContabile));
            }

            logger.debug("--> Exit caricaRigaContabile");
            return rigaContabile;
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore impossibile recuperare RigaContabile identificata da " + id, e);
            throw new ContabilitaException("Impossibile recuperare RigaContabile identificata da " + id, e);
        }
    }

    @Override
    public RigaContabile caricaRigaContabileLazy(Integer id) {
        logger.debug("--> Enter caricaRigaContabileLazy");
        try {
            RigaContabile rigaContabile = panjeaDAO.load(RigaContabile.class, id);
            logger.debug("--> Exit caricaRigaContabile");
            return rigaContabile;
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore impossibile recuperare RigaContabile identificata da " + id, e);
            throw new RuntimeException("Impossibile recuperare RigaContabile identificata da " + id, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RigaContabile> caricaRigheContabili(Integer idAreaContabile) {
        logger.debug("--> Enter caricaRigheContabili");
        Query query = panjeaDAO.prepareNamedQuery("RigaContabile.caricaRigheByArea");
        query.setParameter("paramIdAreaContabile", idAreaContabile);
        List<RigaContabile> list = query.getResultList();
        logger.debug("--> Exit caricaRigheContabili");
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RigaContabile> caricaRigheContabiliPerSottoConto(Integer idAreaContabile, SottoConto sottoConto) {
        logger.debug("--> Enter caricaRigheContabiliPerSottoConto");
        Query query = panjeaDAO.prepareNamedQuery("RigaContabile.caricaRigheBySottoConto");
        query.setParameter("paramIdAreaContabile", idAreaContabile);
        query.setParameter("paramIdSottoConto", sottoConto.getId());
        List<RigaContabile> list = query.getResultList();
        logger.debug("--> Exit caricaRigheContabiliPerSottoConto");
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TipoDocumento> caricaTipiDocumentoByTipoRegistro(TipoRegistro tipoRegistro) {
        logger.debug("--> Enter caricaTipiDocumentoByTipoRegistro");
        List<TipoDocumento> list = new ArrayList<TipoDocumento>();

        try {
            Query query = panjeaDAO.prepareNamedQuery("TipoAreaContabile.caricaTipoDocumentoByTipoRegistro");
            query.setParameter("paramCodiceAzienda", getAzienda());
            query.setParameter("paramTipoRegistro", tipoRegistro);

            list = panjeaDAO.getResultList(query);
        } catch (Exception ex) {
            logger.error("--> Errore durante il caricamento dei tipi documento per tipo registro.", ex);
            throw new RuntimeException("Errore durante il caricamento dei tipi documento per tipo registro.", ex);
        }

        logger.debug("--> Exit caricaTipiDocumentoByTipoRegistro");
        return list;
    }

    /**
     * Metodo che determina se e' gia' stata creata una area contabile verificando i seguenti attributi come chiave di
     * dominio:.<br/>
     * <ul>
     * <li>codiceAzienda</li>
     * <li>resistroIva</li>
     * <li>annoMovimento</li>
     * <li>codice (numero protocollo)</li>
     * <li>registroProtocollo solo se l'area iva è presente</li>
     * </ul>
     * 
     * @param areaContabile
     *            l'area contabile di cui verificare l'esistenza
     * @throws AreaContabileDuplicateException
     *             esiste già un area contabile allegata al documento
     */
    @SuppressWarnings("unchecked")
    private void checkAreaContabileUnivoca(AreaContabile areaContabile) throws AreaContabileDuplicateException {
        logger.debug("--> Enter isAreaContabileUnivoca");
        if (areaContabile.getTipoAreaContabile().getTipoDocumento().isRigheIvaEnable()) {
            StringBuffer queryHql = new StringBuffer(
                    " from AreaContabile ac where ac.documento.codiceAzienda = :paramCodiceAzienda and ac.tipoAreaContabile.registroIva.id = :paramIdRegistroIva and ac.annoMovimento = :paramAnnoMovimento ");
            Map<String, Object> paramHql = new HashMap<String, Object>();
            Integer idRegistroIva = null;
            if (areaContabile.getTipoAreaContabile().getRegistroIva() != null) {
                idRegistroIva = areaContabile.getTipoAreaContabile().getRegistroIva().getId();
            }
            paramHql.put("paramCodiceAzienda", areaContabile.getDocumento().getCodiceAzienda());
            paramHql.put("paramIdRegistroIva", idRegistroIva);
            paramHql.put("paramAnnoMovimento", areaContabile.getAnnoMovimento());
            if (areaContabile.getCodice().getCodice() != null) {
                queryHql.append(" and ac.codice.codice = :paramCodice ");
                paramHql.put("paramCodice", areaContabile.getCodice().getCodice());
            }
            if ((areaContabile.getTipoAreaContabile().getRegistroProtocollo() != null)
                    && (!"".equals(areaContabile.getTipoAreaContabile().getRegistroProtocollo()))) {
                if (areaContabile.getTipoAreaContabile().getRegistroProtocolloCollegato() != null) {
                    queryHql.append(
                            " and (ac.tipoAreaContabile.registroProtocollo = :paramRegistroProtocollo or ac.tipoAreaContabile.registroProtocolloCollegato = :paramRegistroProtocolloCollegato) ");
                    paramHql.put("paramRegistroProtocollo",
                            areaContabile.getTipoAreaContabile().getRegistroProtocollo());
                    paramHql.put("paramRegistroProtocolloCollegato",
                            areaContabile.getTipoAreaContabile().getRegistroProtocolloCollegato());
                } else {
                    queryHql.append(" and ac.tipoAreaContabile.registroProtocollo = :paramRegistroProtocollo ");
                    paramHql.put("paramRegistroProtocollo",
                            areaContabile.getTipoAreaContabile().getRegistroProtocollo());
                }

            }
            logger.debug("--> preparata query hql da eseguire per l'univocita codice " + queryHql.toString());
            Query query = panjeaDAO.prepareQuery(queryHql.toString());
            for (String paramQuery : paramHql.keySet()) {
                query.setParameter(paramQuery, paramHql.get(paramQuery));
            }
            List<AreaContabile> list = null;
            try {
                list = panjeaDAO.getResultList(query);
            } catch (DAOException e) {
                logger.error("--> errore, impossibile ottenere la list di AreaContabile ", e);
                throw new RuntimeException(e);
            }

            // delle aree contabili trovate rilancio l'eccezzione solo se i protocolli utilizzati sono gli stessi.
            // Questo perchè 2 tipi documento posso avere, per la chiava di dominio, lo stesso registro protocollo.
            String registroArea = areaContabile.getTipoAreaContabile().getRegistroProtocolloUtilizzato();
            if (registroArea == null) {
                registroArea = "";
            }
            for (AreaContabile areaContabileTrovata : list) {
                String registroAreaTrovata = areaContabileTrovata.getTipoAreaContabile()
                        .getRegistroProtocolloUtilizzato();
                if (registroAreaTrovata == null) {
                    registroAreaTrovata = "";
                }
                if (!areaContabileTrovata.equals(areaContabile) && registroAreaTrovata.equals(registroArea)) {
                    throw new AreaContabileDuplicateException("Area contabile duplicata", areaContabileTrovata);
                }
            }
        }
    }

    /**
     * Verifica se tra le 2 aree contabili è cambiata l'entità.
     * 
     * @param areaContabileOld
     *            vecchia area contabile
     * @param areaContabileNew
     *            nuova area contabile
     * @return <code>true</code> se l'entità è cambiata
     */
    private boolean checkCambioEntita(AreaContabile areaContabileOld, AreaContabile areaContabileNew) {
        return areaContabileOld != null && areaContabileNew.getDocumento().getEntita() != null
                && !areaContabileNew.getDocumento().getEntita().equals(areaContabileOld.getDocumento().getEntita());
    }

    /**
     * Verifica e invalida se necessario le aree presenti e i documenti contabili collegati.
     * 
     * @param areaContabile
     *            area contabile da invalidare
     * @return areaContabile la nuova area contabile aggiornata
     * @throws ContabilitaException
     *             errore generico
     */
    @Override
    public AreaContabile checkInvalidaAreeCollegate(AreaContabile areaContabile) throws ContabilitaException {
        // controllo l'area contabile
        if (areaContabile.isValidRigheContabili()) {
            areaContabile = invalidaAreaContabile(areaContabile,
                    areaContabile.getStatoAreaContabile().compareTo(StatoAreaContabile.CONFERMATO) == 0);
        }

        // invalida se le condizioni sono soddisfatte il libro giornale non invalido la parte iva perche' una modifica
        // sulle righe iva non tocca la parte iva
        areaContabileVerificaManager.invalidaLibroGiornale(null, areaContabile);

        // se l'areaPartite e' presente mi preoccupo di invalidarla
        areaContabileVerificaManager.invalidaAreaRate(areaContabile);

        return areaContabile;
    }

    /**
     * @return codice azienda corrente
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    /**
     * 
     * @return utentecorrente
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) context.getCallerPrincipal();
    }

    /**
     * 
     * @param righe
     *            righe di riferimento
     * @return saldo delle righe contabili
     */
    private BigDecimal getSaldoRigheContabili(List<RigaContabile> righe) {
        logger.debug("--> Enter getSaldoRigheContabili");
        BigDecimal saldo = BigDecimal.ZERO;
        for (RigaContabile rigaContabile : righe) {
            saldo = saldo.add(rigaContabile.getImportoDare()).subtract(rigaContabile.getImportoDare());
        }
        logger.debug("--> Exit getSaldoRigheContabili");
        return saldo;

    }

    @Override
    public AreaContabile invalidaAreaContabile(AreaContabile areaContabile) {
        return invalidaAreaContabile(areaContabile, true);
    }

    @Override
    public AreaContabile invalidaAreaContabile(AreaContabile areaContabile, boolean changeConfermatoToProvvisorio) {
        logger.debug("--> Enter invalidaAreaContabile");
        // porto lo stato dell'area in PROVVISORIO
        AreaContabile areaContabileNew = null;
        if (changeConfermatoToProvvisorio) {
            try {
                areaContabileNew = statoAreaContabileManager.cambioStatoDaConfermatoInProvvisorio(areaContabile);
            } catch (StatoAreaContabileNonValidoException ex) {
                logger.error("--> Errore durante il cambio della stato dell'area contabile.", ex);
                throw new RuntimeException("Errore durante il cambio della stato dell'area contabile.", ex);
            }
        } else {
            areaContabileNew = areaContabile;
        }
        // invalido le righe contabili
        areaContabileNew.setValidDataRigheContabili(null);
        areaContabileNew.setValidUtenteRigheContabili(null);
        areaContabileNew.setValidRigheContabili(false);
        try {
            areaContabileNew = panjeaDAO.save(areaContabileNew);
        } catch (Exception ex) {
            logger.error("--> Errore durante il salvataggio dell'area contabile.", ex);
            throw new RuntimeException("Errore durante il salvataggio dell'area contabile.", ex);
        }
        logger.debug("--> Exit invalidaAreaContabile");
        return areaContabileNew;
    }

    @Override
    public boolean isAreaPresente(Integer idDocumento) throws AnagraficaServiceException {
        logger.debug("--> Enter isAreaPresente");
        Query query = panjeaDAO.prepareNamedQuery("AreaContabile.ricercaByDocumento");
        query.setParameter("paramIdDocumento", idDocumento);
        Integer id = null;
        try {
            id = (Integer) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            logger.debug("--> Exit isAreaPresente");
            return false;
        } catch (DAOException e) {
            logger.error("--> errore getSingleResult ", e);
            throw new AnagraficaServiceException("Impossibile recuperare AreaContabile da Documento", e);
        }
        logger.debug("--> Exit caricaAreaContabilePerDocumento");
        return (id != null);
    }

    @Override
    public boolean isRigheContabiliPresenti(AreaContabile areaContabile) throws ContabilitaException {
        logger.debug("--> Enter isRigheContabiliPresenti");

        Query query = panjeaDAO.prepareNamedQuery("RigaContabile.numeroRigheByArea");
        query.setParameter("paramIdAreaContabile", areaContabile.getId());

        Long numeroRighe = null;
        try {
            numeroRighe = (Long) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento del numero delle righe contabili.", e);
            throw new ContabilitaException("Errore durante il caricamento del numero delle righe contabili.", e);
        }

        logger.debug("--> Exit isRigheContabiliPresenti");
        return !(numeroRighe == null || numeroRighe.intValue() == 0);
    }

    @Override
    public boolean isTipoAreaPresente(Integer idTipoDocumento) throws AnagraficaServiceException {
        logger.debug("--> Enter isTipoAreaPresente");
        Query query = panjeaDAO.prepareNamedQuery("TipoAreaContabile.caricaByTipoDocumento");
        query.setParameter("paramId", idTipoDocumento);
        TipoAreaContabile tipoAreaContabile = null;
        try {
            tipoAreaContabile = (TipoAreaContabile) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            logger.debug("--> Exit isTipoAreaPresente");
            return false;
        } catch (DAOException e) {
            logger.error("--> errore getSingleResult ", e);
            throw new AnagraficaServiceException("Impossibile recuperare AreaContabile da Documento", e);
        }
        logger.debug("--> Exit isTipoAreaPresente");
        return (tipoAreaContabile != null);
    }

    @Override
    public List<AreaContabileDTO> ricercaAreaContabilePerLiquidazione(Integer anno) throws TipoDocumentoBaseException {
        logger.debug("--> Enter ricercaAreaContabilePerLiquidazione");
        // carico il tipo documento per la liquidazioni non creo una query perche' i tipi documenti sono pochi
        List<TipoDocumentoBase> tipoDocumento = tipiAreaContabileManager.caricaTipiDocumentoBase();
        TipoDocumento tipoDocumentoLiquidazione = null;
        for (TipoDocumentoBase tipoDocumentoBase : tipoDocumento) {
            if (tipoDocumentoBase.getTipoOperazione() == TipoOperazioneTipoDocumento.LIQUIDAZIONE_IVA) {
                tipoDocumentoLiquidazione = tipoDocumentoBase.getTipoAreaContabile().getTipoDocumento();
                break;
            }
        }

        if (tipoDocumentoLiquidazione == null) {
            logger.warn("--> ");
            throw new TipoDocumentoBaseException(
                    new String[] { "Tipo operazione " + TipoOperazioneTipoDocumento.LIQUIDAZIONE_IVA.name() });
        }

        // imposto i parametri per la ricerca
        ParametriRicercaMovimentiContabili parametriRicerca = new ParametriRicercaMovimentiContabili();
        parametriRicerca.getTipiDocumento().add(tipoDocumentoLiquidazione);

        Calendar calendar = Calendar.getInstance();
        calendar.set(anno, 0, 01);
        Date inizioAnno = calendar.getTime();

        calendar.set(anno, 11, 31);
        Date fineAnno = calendar.getTime();

        parametriRicerca.getDataDocumento().setDataIniziale(inizioAnno);
        parametriRicerca.getDataDocumento().setDataFinale(fineAnno);

        parametriRicerca.getStatiAreaContabile().add(StatoAreaContabile.CONFERMATO);
        parametriRicerca.getStatiAreaContabile().add(StatoAreaContabile.PROVVISORIO);
        parametriRicerca.getStatiAreaContabile().add(StatoAreaContabile.SIMULATO);
        parametriRicerca.getStatiAreaContabile().add(StatoAreaContabile.VERIFICATO);

        parametriRicerca.setEscludiMovimentiStampati(false);

        List<RigaContabileDTO> righeAreeContabiliLiquidazione = ricercaControlloAreeContabili(parametriRicerca);

        // Raggruppo per areaDocumento
        if (righeAreeContabiliLiquidazione == null) {
            righeAreeContabiliLiquidazione = new ArrayList<RigaContabileDTO>();
        }

        Map<Integer, AreaContabileDTO> mapAreeContabili = new HashMap<Integer, AreaContabileDTO>();
        for (RigaContabileDTO rigaContabileDTO : righeAreeContabiliLiquidazione) {
            mapAreeContabili.put(rigaContabileDTO.getAreaContabileDTO().getId(),
                    rigaContabileDTO.getAreaContabileDTO());
        }

        Collection<AreaContabileDTO> areeContabiliDTO = mapAreeContabili.values();
        // la collection che mi arriav da Values non è serializzabile. Metto tutto in un arrayList
        List<AreaContabileDTO> areeContabiliDTOList = new ArrayList<AreaContabileDTO>(areeContabiliDTO);
        logger.debug("--> Exit ricercaAreaContabilePerLiquidazione");
        return areeContabiliDTOList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RigaContabileDTO> ricercaControlloAreeContabili(ParametriRicercaMovimentiContabili parametriRicerca) {
        logger.debug("--> Enter ricercaControlloAreeContabili");
        Map<String, Object> valueParametri = new TreeMap<String, Object>();

        StringBuffer queryHQL = new StringBuffer(" select new it.eurotn.panjea.contabilita.util.RigaContabileDTO( ");
        queryHQL.append(
                " r.areaContabile.id, r.areaContabile.statoAreaContabile, r.areaContabile.annoMovimento, r.areaContabile.documento.dataDocumento, r.areaContabile.dataRegistrazione, ");
        queryHQL.append(" r.areaContabile.documento.codice, r.areaContabile.codice, ");
        queryHQL.append(
                " r.areaContabile.tipoAreaContabile.tipoDocumento.codice, r.areaContabile.tipoAreaContabile.tipoDocumento.descrizione, ");
        queryHQL.append(
                " r.areaContabile.note, r.areaContabile.numeroPaginaGiornale, r.id, r.conto.conto.mastro.codice, r.conto.conto.codice, r.conto.codice, r.conto.descrizione, r.note, r.importoDare, r.importoAvere,r.contoInsert, r.areaContabile.documento.totale,r.areaContabile.tipoAreaContabile.registroProtocollo ) ");
        queryHQL.append(" from RigaContabile r ");
        StringBuffer whereHQL = new StringBuffer(
                " where r.areaContabile.documento.codiceAzienda = :paramCodiceAzienda  ");
        JecPrincipal jecPrincipal = getPrincipal();
        // condizioni obbligatorie
        valueParametri.put("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());

        // filtro data registrazione
        if (parametriRicerca.getDataRegistrazione().getDataIniziale() != null) {
            whereHQL.append(" and (r.areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
            valueParametri.put("paramDaDataRegistrazione",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataRegistrazione().getDataIniziale()));
        }
        if (parametriRicerca.getDataRegistrazione().getDataFinale() != null) {
            whereHQL.append(" and (r.areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
            valueParametri.put("paramADataRegistrazione",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataRegistrazione().getDataFinale()));
        }

        // filtro anno
        if (!parametriRicerca.getAnnoCompetenza().equals("")) {
            whereHQL.append(" and r.areaContabile.annoMovimento = :paramAnnoMovimento ");
            valueParametri.put("paramAnnoMovimento", new Integer(parametriRicerca.getAnnoCompetenza()));
        }

        // filtro data documento
        if (parametriRicerca.getDataDocumento().getDataIniziale() != null) {
            whereHQL.append(" and (r.areaContabile.documento.dataDocumento >= :paramDaDataDocumento) ");
            valueParametri.put("paramDaDataDocumento",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataDocumento().getDataIniziale()));
        }
        if (parametriRicerca.getDataDocumento().getDataFinale() != null) {
            whereHQL.append(" and (r.areaContabile.documento.dataDocumento <= :paramADataDocumento) ");
            valueParametri.put("paramADataDocumento",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataDocumento().getDataFinale()));
        }
        // filtro numero documento
        if (!parametriRicerca.getDaNumeroDocumento().isEmpty()) {
            whereHQL.append(" and (r.areaContabile.documento.codice.codiceOrder >= :paramDaNumeroDocumento) ");
            valueParametri.put("paramDaNumeroDocumento", parametriRicerca.getDaNumeroDocumento().getCodiceOrder());
        }
        if (!parametriRicerca.getANumeroDocumento().isEmpty()) {
            whereHQL.append(" and (r.areaContabile.documento.codice.codiceOrder <= :paramANumeroDocumento) ");
            valueParametri.put("paramANumeroDocumento", parametriRicerca.getANumeroDocumento().getCodiceOrder());
        }
        // filtro totale documento
        if (parametriRicerca.getTotale() != null) {
            whereHQL.append(" and (r.areaContabile.documento.totale.importoInValutaAzienda = :paramTotale) ");
            valueParametri.put("paramTotale", parametriRicerca.getTotale());
        }

        // filtro per stampa giornale
        if (parametriRicerca.isFiltraAbilitatiStampaGiornale()) {
            whereHQL.append(" and (r.areaContabile.tipoAreaContabile.stampaGiornale = true) ");
        }

        // filtro tipi documento
        List<TipoDocumento> tipiDocumento = new ArrayList<TipoDocumento>();
        try {
            tipiDocumento = tipoDocumentoManager.caricaTipiDocumento("codice", null, false);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei tipidocumento. ", e);
            throw new RuntimeException("Errore durante il caricamento dei tipidocumento. ", e);
        }
        if ((parametriRicerca.getTipiDocumento() != null) && (parametriRicerca.getTipiDocumento().size() != 0)
                && parametriRicerca.getTipiDocumento().size() != tipiDocumento.size()) {
            if (parametriRicerca.getTipiDocumento().size() == 1) {
                whereHQL.append(" and r.areaContabile.documento.tipoDocumento = :paramTipiDocumento ");
            } else {
                whereHQL.append(" and r.areaContabile.documento.tipoDocumento in (:paramTipiDocumento) ");
            }
            valueParametri.put("paramTipiDocumento", parametriRicerca.getTipiDocumento());
        }

        // filtro Stato documento
        if ((parametriRicerca.getStatiAreaContabile() != null) && (parametriRicerca.getStatiAreaContabile().size() != 0)
                && (StatoAreaContabile.values().length != parametriRicerca.getStatiAreaContabile().size())) {
            if (parametriRicerca.getStatiAreaContabile().size() == 1) {
                whereHQL.append(" and r.areaContabile.statoAreaContabile = :paramStatoDocumento ");
            } else {
                whereHQL.append(" and r.areaContabile.statoAreaContabile in (:paramStatoDocumento) ");
            }
            valueParametri.put("paramStatoDocumento", parametriRicerca.getStatiAreaContabile());
        }

        if (parametriRicerca.getEscludiMovimentiStampati().booleanValue()) {
            whereHQL.append(" and r.areaContabile.numeroPaginaGiornale = 0 ");
        }
        whereHQL.append(" order by r.areaContabile.dataRegistrazione, r.areaContabile.id, r.ordinamento,r.id");
        Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString());
        Set<String> set = valueParametri.keySet();
        for (String key : set) {
            Object value = valueParametri.get(key);
            if (value instanceof Date) {
                Date valueDate = (Date) value;
                query.setParameter(key, valueDate, TemporalType.DATE);
            } else {
                query.setParameter(key, valueParametri.get(key));
            }
        }

        List<RigaContabileDTO> listResult = new ArrayList<RigaContabileDTO>();
        try {
            List<RigaContabileDTO> righeContabili = new ArrayList<RigaContabileDTO>();

            righeContabili = panjeaDAO.getResultList(query);

            if (righeContabili == null) {
                righeContabili = new ArrayList<RigaContabileDTO>();
            }

            // ricerco le aree contabili senza righe solo se non devo filtrare quelli che vanno stampati a giornali.
            if (!parametriRicerca.isFiltraAbilitatiStampaGiornale()) {
                List<RigaContabileDTO> areeSenzaRighe = ricercaControlloAreeContabiliSenzaRighe(parametriRicerca);

                if (areeSenzaRighe == null) {
                    areeSenzaRighe = new ArrayList<RigaContabileDTO>();
                }
                listResult.addAll(areeSenzaRighe);
            }

            listResult.addAll(righeContabili);
        } catch (Exception e) {
            logger.error("--> errore, impossibile eseguire l'interrogazione di ricercaControlloDocumenti ", e);
            throw new RuntimeException("Impossibile eseguire l'interrogazione per Controllo Documenti ", e);
        }
        logger.debug("--> Exit ricercaControlloAreeContabili");
        return listResult;
    }

    /**
     * Ricerca tutte le aree contabili che non hanno righe contabili.
     * 
     * @param parametriRicerca
     *            parametri per la ricerca delle righe contabili
     * @return lista di aree contabili senza righe
     * @throws ContabilitaException
     *             exception generica
     */
    @SuppressWarnings("unchecked")
    private List<RigaContabileDTO> ricercaControlloAreeContabiliSenzaRighe(
            ParametriRicercaMovimentiContabili parametriRicerca) throws ContabilitaException {
        logger.debug("--> Enter ricercaControlloAreeContabiliSenzaRighe");
        Map<String, Object> valueParametri = new TreeMap<String, Object>();

        StringBuffer queryHQL = new StringBuffer(" select new it.eurotn.panjea.contabilita.util.RigaContabileDTO( ");
        queryHQL.append(
                " a.id, a.statoAreaContabile, a.annoMovimento, a.documento.dataDocumento, a.dataRegistrazione, ");
        queryHQL.append(" a.documento.codice, a.codice, ");
        queryHQL.append(" a.tipoAreaContabile.tipoDocumento.codice, a.tipoAreaContabile.tipoDocumento.descrizione, ");
        queryHQL.append(" a.note, a.numeroPaginaGiornale, a.documento.totale, 'FAKE' ) ");
        queryHQL.append(" from RigaContabile r RIGHT JOIN r.areaContabile a ");

        StringBuffer whereHQL = new StringBuffer(" where a.documento.codiceAzienda = :paramCodiceAzienda  ");
        whereHQL.append(" and r.id is null ");
        JecPrincipal jecPrincipal = getPrincipal();
        // condizioni obbligatorie
        valueParametri.put("paramCodiceAzienda", jecPrincipal.getCodiceAzienda());

        // filtro data registrazione
        if (parametriRicerca.getDataRegistrazione().getDataIniziale() != null) {
            whereHQL.append(" and (r.areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
            valueParametri.put("paramDaDataRegistrazione",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataRegistrazione().getDataIniziale()));
        }
        if (parametriRicerca.getDataRegistrazione().getDataFinale() != null) {
            whereHQL.append(" and (r.areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
            valueParametri.put("paramADataRegistrazione",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataRegistrazione().getDataFinale()));
        }

        // filtro anno
        if (!parametriRicerca.getAnnoCompetenza().equals("")) {
            whereHQL.append(" and a.annoMovimento = :paramAnnoMovimento ");
            valueParametri.put("paramAnnoMovimento", new Integer(parametriRicerca.getAnnoCompetenza()));
        }

        // filtro data documento
        if (parametriRicerca.getDataDocumento().getDataIniziale() != null) {
            whereHQL.append(" and (a.documento.dataDocumento >= :paramDaDataDocumento) ");
            valueParametri.put("paramDaDataDocumento",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataDocumento().getDataIniziale()));
        }
        if (parametriRicerca.getDataDocumento().getDataFinale() != null) {
            whereHQL.append(" and (a.documento.dataDocumento <= :paramADataDocumento) ");
            valueParametri.put("paramADataDocumento",
                    PanjeaEJBUtil.getDateTimeToZero(parametriRicerca.getDataDocumento().getDataFinale()));
        }
        // filtro numero documento
        if (!parametriRicerca.getDaNumeroDocumento().isEmpty()) {
            whereHQL.append(" and (a.documento.codice.codiceOrder >= :paramDaNumeroDocumento) ");
            valueParametri.put("paramDaNumeroDocumento", parametriRicerca.getDaNumeroDocumento().getCodiceOrder());
        }
        if (!parametriRicerca.getANumeroDocumento().isEmpty()) {
            whereHQL.append(" and (a.documento.codice.codiceOrder <= :paramANumeroDocumento) ");
            valueParametri.put("paramANumeroDocumento", parametriRicerca.getANumeroDocumento().getCodiceOrder());
        }
        // filtro totale documento
        if (parametriRicerca.getTotale() != null) {
            whereHQL.append(" and (a.documento.totale.importoInValutaAzienda = :paramTotale) ");
            valueParametri.put("paramTotale", parametriRicerca.getTotale());
        }

        // filtro tipi documento
        List<TipoDocumento> tipiDocumento = new ArrayList<TipoDocumento>();
        try {
            tipiDocumento = tipoDocumentoManager.caricaTipiDocumento("codice", null, false);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei tipidocumento. ", e);
            throw new RuntimeException("Errore durante il caricamento dei tipidocumento. ", e);
        }
        if ((parametriRicerca.getTipiDocumento() != null) && (parametriRicerca.getTipiDocumento().size() != 0)
                && parametriRicerca.getTipiDocumento().size() != tipiDocumento.size()) {
            if (parametriRicerca.getTipiDocumento().size() == 1) {
                whereHQL.append(" and a.documento.tipoDocumento = :paramTipiDocumento ");
            } else {
                whereHQL.append(" and a.documento.tipoDocumento in (:paramTipiDocumento) ");
            }
            valueParametri.put("paramTipiDocumento", parametriRicerca.getTipiDocumento());
        }

        // filtro Stato documento
        if ((parametriRicerca.getStatiAreaContabile() != null) && (parametriRicerca.getStatiAreaContabile().size() != 0)
                && (StatoAreaContabile.values().length != parametriRicerca.getStatiAreaContabile().size())) {
            if (parametriRicerca.getStatiAreaContabile().size() == 1) {
                whereHQL.append(" and a.statoAreaContabile = :paramStatoDocumento ");
            } else {
                whereHQL.append(" and a.statoAreaContabile in (:paramStatoDocumento) ");
            }
            valueParametri.put("paramStatoDocumento", parametriRicerca.getStatiAreaContabile());
        }

        if (parametriRicerca.getEscludiMovimentiStampati().booleanValue()) {
            whereHQL.append(" and a.numeroPaginaGiornale = 0 ");
        }
        whereHQL.append(" order by a.dataRegistrazione, a.id");
        Query query = panjeaDAO.prepareQuery(queryHQL.toString() + whereHQL.toString());
        Set<String> set = valueParametri.keySet();
        for (String key : set) {
            Object value = valueParametri.get(key);
            if (value instanceof Date) {
                Date valueDate = (Date) value;
                query.setParameter(key, valueDate, TemporalType.DATE);
            } else {
                query.setParameter(key, valueParametri.get(key));
            }
        }
        List<RigaContabileDTO> righeContabili = new ArrayList<RigaContabileDTO>();
        try {
            righeContabili = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile eseguire l'interrogazione di ricercaControlloDocumenti ", e);
            throw new ContabilitaException("Impossibile eseguire l'interrogazione per Controllo Documenti ", e);
        }

        logger.debug("--> Exit ricercaControlloAreeContabiliSenzaRighe");
        return righeContabili;
    }

    @Override
    public AreaContabile salvaAreaContabile(AreaContabile areaContabile, boolean eseguiRollBack)
            throws ContabilitaException, AreaContabileDuplicateException, DocumentoDuplicateException {
        logger.debug("--> Enter salvaAreaContabile");

        // carico se id!=null l'entita' alla versione attuale prima di essere
        // salvata
        AreaContabile areaContabileOld = null;
        if (areaContabile.getId() != null) {
            // carico l'areaContabile e istanzio un nuovo oggetto per averne uno staccato dalla session che non mi si
            // aggiorna quando salvo.
            AreaContabile areaTmp = caricaAreaContabile(areaContabile.getId());
            try {
                areaContabileOld = areaTmp.clone();
            } catch (CloneNotSupportedException e) {
                logger.error("--> errore in salvaAreaContabile", e);
            }
        }

        boolean cambioEntita = checkCambioEntita(areaContabileOld, areaContabile);

        // se cambia l'entità vado ad aggiornare i valori della ritenuta d'acconto
        if (areaContabile.isNew() || cambioEntita) {
            areaContabile = ritenutaAccontoContabilitaManager.assegnaDatiRitenutaAccontoAreaContabile(areaContabile);
        }

        AreaContabile areaContabileSalvata = salvaAreaContabileNoCheck(areaContabile);
        try {
            checkAreaContabileUnivoca(areaContabileSalvata);
        } catch (AreaContabileDuplicateException e) {
            logger.error(
                    "--> Area contabile non univoca secondo le chiavi di dominio data doc, num. doc, tipo doc, entita'");
            if (eseguiRollBack) {
                context.setRollbackOnly();
            }
            throw e;
        }

        // crea se necessario l'areaIva dall'areaContabile salvata e se sono soddisfatte le condizioni invalida la parte
        // iva
        areaContabileVerificaManager.checkCreaInvalidaAreaIva(areaContabileOld, areaContabileSalvata);

        // invalida l'areaContabile se sono soddifatte le condizioni, in caso di aggiornamento di alcune proprietà della
        // testata per problema di referenze circolari anche segnando i bean con
        // @IgnoreDependency
        // e per il fatto che il metodo e' in questo manager devo chiamarlo da qui
        if (areaContabileVerificaManager.isTotaliDiversi(areaContabileOld, areaContabileSalvata)) {
            areaContabileSalvata = invalidaAreaContabile(areaContabileSalvata,
                    areaContabileSalvata.getStatoAreaContabile().equals(AreaContabile.StatoAreaContabile.CONFERMATO));
            // invalida l'area partite dato che la validazione dell'area partite si esegue
            // tot doc = tot rate partite
            areaContabileVerificaManager.invalidaAreaRate(areaContabileSalvata);
        }

        if (areaContabileVerificaManager.isDateDocumentoDiverse(areaContabileOld, areaContabileSalvata)) {
            areaContabileVerificaManager.invalidaAreaRate(areaContabileSalvata);
            areaContabileSalvata = invalidaAreaContabile(areaContabileSalvata,
                    areaContabileSalvata.getStatoAreaContabile().equals(AreaContabile.StatoAreaContabile.CONFERMATO));
        }

        // invalida se le condizioni sono soddisfatte il giornale e il registro iva
        areaContabileVerificaManager.checkInvalidaDocumentiContabilita(areaContabileOld, areaContabileSalvata);

        // Se ho cambiato entità devo cambiare anche il conto nella riga del cliente
        if (cambioEntita) {
            EntitaRigaContabileBuilder entitaRigaContabileBuilder = new EntitaRigaContabileBuilder(pianoContiManager,
                    null, null, aziendeManager);
            SottoConto contoClienteOld;
            SottoConto contoCliente;
            try {
                contoClienteOld = entitaRigaContabileBuilder.getSottoConto(areaContabileOld, "");
                contoCliente = entitaRigaContabileBuilder.getSottoConto(areaContabile, "");
            } catch (ContoEntitaAssenteException e) {
                throw new RuntimeException(e);
            } catch (ContoRapportoBancarioAssenteException e) {
                throw new RuntimeException(e);
            }
            // carico la riga con il vecchio sottoconto
            List<RigaContabile> righeContabili = caricaRigheContabiliPerSottoConto(areaContabile.getId(),
                    contoClienteOld);
            for (RigaContabile rigaContabile : righeContabili) {
                BigDecimal importo = rigaContabile.getImporto();
                if (rigaContabile.getContoAvere() != null && rigaContabile.getContoAvere().getId() != -1) {
                    rigaContabile.setContoAvere(contoCliente);
                } else {
                    rigaContabile.setContoDare(contoCliente);
                }
                // cambiando il conto si azzera l'importo. Lo risetto.
                rigaContabile.setImporto(importo);
                salvaRigaContabile(rigaContabile);
            }

            strutturaContabileManager.creaRigheContabiliAutomatiche(areaContabile, righeContabili.size());
        }
        logger.debug("--> Exit salvaAreaContabile");
        return areaContabileSalvata;
    }

    @Override
    public AreaContabile salvaAreaContabileNoCheck(AreaContabile areaContabile)
            throws ContabilitaException, AreaContabileDuplicateException, DocumentoDuplicateException {
        logger.debug("--> Enter salvaAreaContabileNoCheck");
        Documento documento;
        logger.debug("--> salvataggio documento ");

        // allinea l'attributo tipoDocumento in Documento con il valore di tipoAreaContabile
        areaContabile.getDocumento().setTipoDocumento(areaContabile.getTipoAreaContabile().getTipoDocumento());
        documento = documentiManager.salvaDocumento(areaContabile.getDocumento());
        areaContabile.setDocumento(documento);

        // imposto dal tipoAreaContabile che il protocollo deve essere uguale al numero documento solo nel caso in cui
        // non ho impostato un codice,lascio quindi la possibilità dell'override manuale
        if (areaContabile.getTipoAreaContabile().getTipoDocumento().isRigheIvaEnable()
                && areaContabile.getTipoAreaContabile().isProtocolloLikeNumDoc()) {
            areaContabile.setCodice(documento.getCodice());
            areaContabile.setValoreProtocollo(documento.getValoreProtocollo());
        }

        verificaDataRegistrazioneValoreProtocollo(areaContabile);

        // se ho gestione diversa da normale su tipoAreaContabile devo avere un protocollo e registro iva collegati e
        // quindi codice e prefisso rispettivi sull'areaContabile
        if (!areaContabile.getTipoAreaContabile().getGestioneIva().equals(GestioneIva.NORMALE)
                && StringUtils.isEmpty(areaContabile.getCodiceCollegato().getCodice())) {
            CodiceProtocollo codice = protocolloAnnoGenerator.nextCodice(areaContabile,
                    areaContabile.getTipoAreaContabile().getRegistroProtocolloCollegato());
            areaContabile.getCodiceCollegato().setCodice(codice.getCodice());
            areaContabile.setValoreProtocolloCollegato(codice.getValoreProtocollo());
        }

        AreaContabile areaContabileSalvata = null;
        try {
            // se il documento non ha un codice lo genero automaticamente
            if (areaContabile.getCodice().isEmpty()) {
                CodiceProtocollo codice = protocolloAnnoGenerator.nextCodice(areaContabile);
                areaContabile.getCodice().setCodice(codice.getCodice());
                areaContabile.setValoreProtocollo(codice.getValoreProtocollo());
            }

            areaContabileSalvata = panjeaDAO.save(areaContabile);
            // HACK l'areaContabileSalvata mantiene il documento alla versione precedente, risetto quindi il documento
            // salvato
            // il problema probabilmente e' legato al fatto che il salvataggio del documento e il salvataggio dell'area
            // contabile vengono eseguiti su due DAO diversi e quindi uno non ha consapevolezza delle modifiche
            // dell'altro.
            areaContabileSalvata.setDocumento(documento);
        } catch (Exception e) {
            logger.error("--> errore in salvataggio area contabile", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaAreaContabileNoCheck");
        return areaContabileSalvata;
    }

    @Override
    public RigaContabile salvaRigaContabile(RigaContabile rigaContabile) {
        logger.debug("--> Enter salvaRigaContabile");
        try {
            if (rigaContabile.isRateiRiscontiAttivi()) {
                rigaContabile = rateoRiscontoManager.salvaRigaContabileRateoRisconto(rigaContabile);
            }
            RigaContabile rigaContabileSalvata = salvaRigaContabileNoCheck(rigaContabile);
            AreaContabile areaContabile = rigaContabileSalvata.getAreaContabile();

            AreaContabile areaContabileAggiornata = checkInvalidaAreeCollegate(areaContabile);

            rigaContabileSalvata.setAreaContabile(areaContabileAggiornata);
            rigaContabileSalvata.getRigheCentroCosto().size();
            rigaContabileSalvata.getAreaContabile().getDocumento().getId();
            logger.debug("--> Exit salvaRigaContabile");
            return rigaContabileSalvata;
        } catch (Exception e) {
            logger.error("--> errore in salvataggio riga contabile ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RigaContabile salvaRigaContabileNoCheck(RigaContabile rigaContabile) {
        try {
            logger.debug("--> Enter salvaRigaContabile");
            RigaContabile rigaContabileSalvata = panjeaDAO.save(rigaContabile);
            logger.debug("--> Exit salvaRigaContabile");
            return rigaContabileSalvata;
        } catch (Exception e) {
            logger.error("--> errore in salvataggio riga contabile ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AreaContabile validaRigheContabili(AreaContabile areaContabile)
            throws ContabilitaException, RigheContabiliNonValidiException {
        return validaRigheContabili(areaContabile, true);
    }

    @Override
    public AreaContabile validaRigheContabili(AreaContabile areaContabile, boolean controllaRigheValide)
            throws ContabilitaException, RigheContabiliNonValidiException {
        logger.debug("--> Enter validaRigheContabili");

        /* verifica la quadratura delle righe contabili */
        List<RigaContabile> righeContabili = caricaRigheContabili(areaContabile.getId());
        // devo associare le righe all'area contabile altrimenti perdo il collegamento parent-child e al salvataggio
        // viene generato un errore
        Set<RigaContabile> righeSet = new HashSet<RigaContabile>(righeContabili);
        areaContabile.setRigheContabili(righeSet);
        // Controllo che tutte le righe siano valide
        if (controllaRigheValide) {
            for (RigaContabile rigaContabile : righeContabili) {
                if (!rigaContabile.isValid()) {
                    throw new RigheContabiliNonValidiException("Centri di costo non validi");
                }
            }
        }

        boolean squadraturaRigheContabili = (getSaldoRigheContabili(righeContabili).compareTo(BigDecimal.ZERO) == 0);
        areaContabile.setSquadratoRigheContabili(squadraturaRigheContabili);
        areaContabile.setValidRigheContabili(true);
        areaContabile.setValidUtenteRigheContabili(getPrincipal().getUserName());
        areaContabile.setValidDataRigheContabili(Calendar.getInstance().getTime());

        /*
         * verifica della presenza di una AreaPartita per questo documento. In caso non esistesse effettua il passaggio
         * di stato da PROVVISORIO a CONFERMATO. Viene effettuato il cambio di stato da PROVVISORIO a CONFERMATO solo se
         * il tipo operazione in tipo area partita è diversa da GENERA
         */
        AreaRate areaRate = areaRateManager.caricaAreaRate(areaContabile.getDocumento());
        try {
            if (!TipoAreaPartita.TipoOperazione.GENERA.equals(areaRate.getTipoAreaPartita().getTipoOperazione())) {
                /* cambio stato all'area contabile in CONFERMATO */
                areaContabile = statoAreaContabileManager.cambioStatoPerConfermaRigheContabili(areaContabile);
            }
            areaContabile = panjeaDAO.save(areaContabile);

        } catch (StatoAreaContabileNonValidoException e) {
            logger.error("--> errore, impossibile variare lo stato al documento  ", e);
            throw new RuntimeException("Impossibile eseguire la variazione dello stato del documento ", e);
        } catch (Exception e) {
            logger.error("--> errore nel salvare l'areaContabile ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit validaRigheContabili");
        return areaContabile;
    }

    private void verificaDataRegistrazioneValoreProtocollo(AreaContabile areaContabile)
            throws DataProtocolloNonValidaException {

        StringBuilder sb = new StringBuilder(200);
        sb.append("select doc.dataDocumento as dataDoc, ");
        sb.append("   doc.codice as docDoc, ");
        sb.append("   ac.valoreProtocollo as valProtDoc, ");
        sb.append("   prot.codice as docProt, ");
        sb.append("   prot.descrizione as descProt, ");
        sb.append("   ac.dataRegistrazione as dataRegistrazione ");
        sb.append("from cont_area_contabile ac inner join docu_documenti doc on ac.documento_id = doc.id ");
        sb.append(
                "                    inner join cont_tipi_area_contabile tipoAc on ac.tipoAreaContabile_id = tipoAc.id ");
        sb.append(
                "                        inner join code_protocolli prot on tipoAc.registroProtocollo = prot.codice ");
        sb.append("where tipoAc.id = :paramTipoAc ");
        sb.append("  and ac.valoreProtocollo < :paramValoreProtocollo ");
        sb.append("  and YEAR(ac.dataRegistrazione) = :paramAnnoDataReg ");
        sb.append("order by ac.valoreProtocollo desc ");
        sb.append("limit 1 ");

        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.addScalar("dataDoc");
        query.addScalar("docDoc");
        query.addScalar("valProtDoc");
        query.addScalar("docProt");
        query.addScalar("descProt");
        query.addScalar("dataRegistrazione");
        query.setParameter("paramTipoAc", areaContabile.getTipoAreaContabile().getId());
        query.setParameter("paramValoreProtocollo", areaContabile.getValoreProtocollo());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(areaContabile.getDataRegistrazione());
        query.setParameter("paramAnnoDataReg", calendar.get(Calendar.YEAR));

        Object[] result = (Object[]) query.uniqueResult();

        if (result != null && areaContabile.getDataRegistrazione().before((Date) result[5])) {
            Date dataDoc = (Date) result[0];
            String numDoc = (String) result[1];
            Integer valoreProtocolloDoc = (Integer) result[2];
            String codiceProtocollo = (String) result[3];
            String descrizioneProtocollo = (String) result[4];
            Date dataRegistrazione = (Date) result[5];

            throw new DataProtocolloNonValidaException(areaContabile.getDataRegistrazione(),
                    areaContabile.getValoreProtocollo(), dataDoc, numDoc, valoreProtocolloDoc, codiceProtocollo,
                    descrizioneProtocollo, dataRegistrazione);
        }
    }
}
