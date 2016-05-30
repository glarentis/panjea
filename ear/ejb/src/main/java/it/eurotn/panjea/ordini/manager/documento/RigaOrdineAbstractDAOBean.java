package it.eurotn.panjea.ordini.manager.documento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces.RigaDocumentoManager;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticoloComponente;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineDAO;
import it.eurotn.panjea.ordini.manager.sqlbuilder.RigheOrdineQueryBuilder;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.rigaordine.builder.dto.RigaOrdineDTOBuilder;
import it.eurotn.panjea.ordini.util.rigaordine.builder.dto.RigaOrdineDTOFactoryBuilder;
import it.eurotn.panjea.ordini.util.rigaordine.builder.dto.RigaOrdineDTOResult;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * Classe che gestisce la persistenza di una riga ordini.<br/>
 * Deve essere fatto un refactoring per creare diverse classi che ereditano<br/>
 * e gestiscano le varie tipologie di righe
 *
 * @author giangi
 * @version 1.0, 20/feb/2012
 *
 */
public abstract class RigaOrdineAbstractDAOBean implements RigaOrdineDAO {
    private static Logger logger = Logger.getLogger(RigaOrdineAbstractDAOBean.class);

    @EJB
    protected RigaDocumentoManager rigaDocumentoManager;

    @EJB
    protected PanjeaDAO panjeaDAO;

    @EJB
    protected DistintaBaseManager distintaBaseManager;

    @IgnoreDependency
    @EJB
    protected AreaOrdineManager areaOrdineManager;

    @Override
    public RigaArticolo associaConfigurazioneDistintaARigaOrdine(RigaArticolo rigaArticolo,
            ConfigurazioneDistinta configurazioneDistintaDaAssociare) {
        logger.debug("--> Enter associaConfigurazioneDistintaARigaOrdine");

        rigaArticolo.setConfigurazioneDistinta(configurazioneDistintaDaAssociare);
        RigaArticolo rigaOrdineSalvata = (RigaArticolo) salvaRigaOrdine(rigaArticolo);
        // evito lazy
        if (rigaOrdineSalvata.getConfigurazioneDistinta() != null) {
            rigaOrdineSalvata.getConfigurazioneDistinta().getNome();
        }
        // Il dao porta il documento allo stato provvisorio
        // passo dal dao per reimpostare lo stato perchè non ho cambiato nulla
        if (rigaArticolo.getAreaOrdine().getStato() == StatoAreaOrdine.CONFERMATO) {
            AreaOrdine ao = rigaOrdineSalvata.getAreaOrdine();
            ao.setStatoAreaOrdine(StatoAreaOrdine.CONFERMATO);
            ao.getDatiValidazioneRighe().valida("automatico");
            try {
                ao = panjeaDAO.save(ao);
                rigaOrdineSalvata.setAreaOrdine(ao);
            } catch (DAOException e) {
                logger.error("-->errore nel salvare l'areaOrdine con satto confermato", e);
                throw new RuntimeException(e);
            }
        }

        // Cancello tutte le distinte personalizzate non legate ad una riga ordine
        StringBuilder sb = new StringBuilder(150);
        sb.append("delete  d.* ");
        sb.append("from maga_distinte_configurazione  d ");
        sb.append("left join ordi_righe_ordine  ro on ro.configurazioneDistinta_id=d.id ");
        sb.append("where d.TIPO_CONFIGURAZIONE='P' ");
        sb.append("and ro.id is null ");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.executeUpdate();

        logger.debug("--> Exit associaConfigurazioneDistintaARigaOrdine");
        return rigaOrdineSalvata;
    }

    @Override
    public AreaOrdine cancellaRigaOrdine(RigaOrdine rigaOrdine) {
        logger.debug("--> Enter cancellaRigaOrdine");

        try {
            panjeaDAO.delete(rigaOrdine);
        } catch (Exception e) {
            logger.error("--> errore in cancellaRigaOrdine.", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit cancellaRigaOrdine");
        return areaOrdineManager.checkInvalidaAreaMagazzino(rigaOrdine.getAreaOrdine());
    }

    @Override
    public RigaArticolo caricaQtaAttrezzaggio(RigaArticolo rigaArticolo,
            ConfigurazioneDistinta configurazioneDistinta) {
        ArticoloLite distinta = rigaArticolo.getArticolo();        
        ((Session)panjeaDAO.getEntityManager().getDelegate()).evict(rigaArticolo);
        if (rigaArticolo instanceof RigaArticoloComponente) {
            RigaArticoloComponente rac = (RigaArticoloComponente) rigaArticolo;
            distinta = rac.getRigaDistintaCollegata().getArticolo();
            configurazioneDistinta = rac.getRigaDistintaCollegata().getConfigurazioneDistinta();
            RigaArticolo rigaPadre = rac.getRigaPadre();
            if (rigaPadre != null) {
                distinta = rigaPadre.getArticolo();
            }
        }

        double qtaAttrezzaggio = distintaBaseManager.caricaQtaAttrezzaggioComponenti(distinta,
                rigaArticolo.getArticolo(), configurazioneDistinta);
        // sull'ordine non devo preoccuparmi di formule sulla qtaMagazzino
        rigaArticolo.setQtaAttrezzaggio(qtaAttrezzaggio);

        return rigaArticolo;
    }

    @Override
    public RigaOrdine caricaRigaOrdine(RigaOrdine rigaOrdine) {
        logger.debug("--> Enter caricaRigaOrdine");
        RigaOrdine rigaOrdineResult = null;
        try {
            rigaOrdineResult = panjeaDAO.load(RigaOrdine.class, rigaOrdine.getId());
            // Init dell'area ordine lazy
            rigaOrdineResult.getAreaOrdine().getVersion();
            // Init delle righe lotti se previste dall'articolo
            if (rigaOrdineResult instanceof RigaArticolo) {
                ((RigaArticolo) rigaOrdineResult).getAttributi().size();
                // evito lazy
                if (((RigaArticolo) rigaOrdineResult).getConfigurazioneDistinta() != null) {
                    ((RigaArticolo) rigaOrdineResult).getConfigurazioneDistinta().getId();
                }
            }
        } catch (ObjectNotFoundException e) {
            logger.error("--> riga ordine non trovata ", e);
        }
        logger.debug("--> Exit caricaRigaOrdine");
        return rigaOrdineResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaOrdine> caricaRigheOrdine(AreaOrdine areaOrdine) {
        logger.debug("--> Enter caricaRigheOrdine");

        RigaOrdine rigaOrdine = null;

        Query query = panjeaDAO.prepareNamedQuery("RigaOrdine.caricaByAreaOrdine");
        query.setParameter("paramAreaOrdine", areaOrdine.getId());

        List<Object[]> resultQuery = new ArrayList<Object[]>();
        resultQuery = query.getResultList();

        // uso un set perchè la query che carica le righe ordine non riesce a fare la distinct
        // a causa delle 2 subselect per il calcolo delle qta
        Set<RigaOrdine> righeOrdine = new HashSet<RigaOrdine>();

        for (Object[] objects : resultQuery) {

            rigaOrdine = (RigaOrdine) objects[0];

            // se la riga ordine è una riga articolo setto la quantità evasa nel campo transiente.
            if (rigaOrdine.getDomainClassName().equals(RigaArticolo.class.getName())) {

                Double qtaEvasa = (Double) objects[1];
                if (qtaEvasa == null) {
                    qtaEvasa = new Double(0);
                }
                ((RigaArticolo) rigaOrdine).setQtaEvasa(qtaEvasa);

            }

            righeOrdine.add(rigaOrdine);
        }

        List<RigaOrdine> resultList = new ArrayList<RigaOrdine>();
        resultList.addAll(righeOrdine);
        // devo riordinarli perche' l'ordine viene perso quando si riorganizza per il calcolo delle
        // quantita'
        // ordino per area e ordinamento
        Collections.sort(resultList, new Comparator<RigaOrdine>() {

            @Override
            public int compare(RigaOrdine o1, RigaOrdine o2) {
                if (o1.getAreaOrdine().getId().compareTo(o2.getAreaOrdine().getId()) >= 0) {
                    if (o1.getOrdinamento() >= o2.getOrdinamento()) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    if (o1.getOrdinamento() >= o2.getOrdinamento()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }

        });
        logger.debug("--> Exit caricaRigheOrdine");
        return resultList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaOrdineDTO> caricaRigheOrdineDTO(AreaOrdine areaOrdine) {
        Integer entitaId = null;
        if (areaOrdine.getDocumento().getEntita() != null && areaOrdine.getDocumento().getEntita().getId() != null) {
            entitaId = areaOrdine.getDocumento().getEntita().getId();
        }

        String query = RigheOrdineQueryBuilder.getSQLRigheOrdine(areaOrdine.getId(), entitaId);

        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(query);
        SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
        sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaOrdineDTOResult.class));
        List<RigaOrdineDTOResult> righeOrdineDTOResults = null;
        try {
            sqlQuery.addScalar("id");
            sqlQuery.addScalar("idAreaOrdine");
            sqlQuery.addScalar("tipoRiga");
            sqlQuery.addScalar("idArticolo");
            sqlQuery.addScalar("codiceArticolo");
            sqlQuery.addScalar("codiceArticoloEntita");
            sqlQuery.addScalar("descrizioneRiga");
            sqlQuery.addScalar("idArticoloPadre");
            sqlQuery.addScalar("codiceArticoloPadre");
            sqlQuery.addScalar("descrizioneArticoloPadre");
            sqlQuery.addScalar("codiceArticoloPadreEntita");
            sqlQuery.addScalar("numeroDecimaliPrezzo");
            sqlQuery.addScalar("codiceValutaPrezzoUnitario");
            sqlQuery.addScalar("importoInValutaPrezzoUnitario");
            sqlQuery.addScalar("importoInValutaAziendaPrezzoUnitario");
            sqlQuery.addScalar("tassoDiCambioPrezzoUnitario");
            sqlQuery.addScalar("qtaRiga");
            sqlQuery.addScalar("codiceValutaPrezzoNetto");
            sqlQuery.addScalar("importoInValutaPrezzoNetto");
            sqlQuery.addScalar("importoInValutaAziendaPrezzoNetto");
            sqlQuery.addScalar("tassoDiCambioPrezzoNetto");
            sqlQuery.addScalar("evasioneForzata");
            sqlQuery.addScalar("variazione1");
            sqlQuery.addScalar("variazione2");
            sqlQuery.addScalar("variazione3");
            sqlQuery.addScalar("variazione4");
            sqlQuery.addScalar("dataConsegna");
            sqlQuery.addScalar("codiceTipoDocumentoCollegato");
            sqlQuery.addScalar("idAreaPreventivoCollegata");
            sqlQuery.addScalar("codiceValutaPrezzoTotale");
            sqlQuery.addScalar("importoInValutaPrezzoTotale");
            sqlQuery.addScalar("importoInValutaAziendaPrezzoTotale");
            sqlQuery.addScalar("tassoDiCambioPrezzoTotale");
            sqlQuery.addScalar("qtaChiusa");
            sqlQuery.addScalar("rigaNota", Hibernate.STRING);
            sqlQuery.addScalar("noteRiga", Hibernate.STRING);
            sqlQuery.addScalar("livello");
            sqlQuery.addScalar("rigaAutomatica");

            righeOrdineDTOResults = sqlQuery.list();
        } catch (Exception e) {
            logger.error("--> errore in caricaRigheOrdine per area ordine con id " + areaOrdine.getId(), e);
            throw new RuntimeException(e);
        }

        List<RigaOrdineDTO> righeOrdineDTO = convertResultToDTO(righeOrdineDTOResults);

        return righeOrdineDTO;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaOrdine> caricaRigheOrdineStampa(AreaOrdine areaOrdine) {

        List<RigaOrdine> righeOrdine = caricaRigheOrdine(areaOrdine);
        for (Iterator<RigaOrdine> iterator = righeOrdine.iterator(); iterator.hasNext();) {
            RigaOrdine rigaOrdine = iterator.next();            
            ((Session)panjeaDAO.getEntityManager().getDelegate()).evict(rigaOrdine);
            if (rigaOrdine instanceof RigaArticoloComponente) {
                RigaArticoloComponente riga = (RigaArticoloComponente) rigaOrdine;
                if (riga.getRigaPadre() != null) {
                    iterator.remove();
                }
            }

        }

        TipoEntita tipoEntitaAreaOrdine = areaOrdine.getDocumento().getTipoDocumento().getTipoEntita();
        if (!TipoEntita.CLIENTE.equals(tipoEntitaAreaOrdine) && !TipoEntita.FORNITORE.equals(tipoEntitaAreaOrdine)) {
            return righeOrdine;
        }

        // carico i codici entità degli articoli e aggiorno le righe ordine caricate
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct ro.articolo.id,cae ");
        sb.append("from RigaArticoloOrdine ro, CodiceArticoloEntita cae ");
        sb.append(
                "where ro.areaOrdine.id = :idAreaOrdine and cae.articolo = ro.articolo and cae.entita.id = :idEntita ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("idAreaOrdine", areaOrdine.getId());
        query.setParameter("idEntita", areaOrdine.getDocumento().getEntita().getId());

        List<Object[]> result = new ArrayList<Object[]>();
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dei codice articolo entità per l'area ordine", e);
            throw new RuntimeException("errore durante il caricamento dei codice articolo entità per l'area ordine", e);
        }

        Map<Integer, CodiceArticoloEntita> codiciArticolo = new HashMap<Integer, CodiceArticoloEntita>();
        for (Object[] objects : result) {
            codiciArticolo.put((Integer) objects[0], (CodiceArticoloEntita) objects[1]);
        }

        for (RigaOrdine rigaOrdine : righeOrdine) {
            if (rigaOrdine instanceof RigaArticolo) {
                CodiceArticoloEntita codiceArticoloEntita = codiciArticolo
                        .get(((RigaArticolo) rigaOrdine).getArticolo().getId());
                if (codiceArticoloEntita != null) {
                    ((RigaArticolo) rigaOrdine).setCodiceArticoloEntita(codiceArticoloEntita.getCodice());
                    ((RigaArticolo) rigaOrdine).setDescrizioneArticoloEntita(codiceArticoloEntita.getDescrizione());
                    ((RigaArticolo) rigaOrdine).setBarCodeEntita(codiceArticoloEntita.getBarCode());
                }
            }
        }

        return righeOrdine;
    }

    /**
     * Si preoccupa di convertire ed eventualmente raggruppare eventuali righe dove l'articolo padre
     * e il prezzo sono gli stessi.<br>
     * Nota che per il raggruppamento vengono unite le righe contigue con padre e prezzo uguali.
     *
     * @param righeBuilder
     *            la lista di RigaOrdineDTOBuilder da scorrere e raggruppare
     * @return List<RigaOrdineDTO>
     */
    private List<RigaOrdineDTO> convertResultToDTO(List<RigaOrdineDTOResult> righeBuilder) {
        Map<String, RigaOrdineDTO> righeComposte = new HashMap<String, RigaOrdineDTO>();
        List<RigaOrdineDTO> result = new ArrayList<RigaOrdineDTO>();

        RigaOrdineDTOFactoryBuilder factoryBuilder = new RigaOrdineDTOFactoryBuilder();

        for (RigaOrdineDTOResult rigaOrdineDTOResult : righeBuilder) {
            RigaOrdineDTOBuilder dtoBuilder = factoryBuilder.getBuilder(rigaOrdineDTOResult);
            dtoBuilder.fillResult(rigaOrdineDTOResult, result, righeComposte);
        }
        return result;
    }

    @Override
    public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        RigaArticolo rigaArticolo = (RigaArticolo) rigaDocumentoManager.creaRigaArticoloDocumento(new RigaArticolo(),
                parametriCreazioneRigaArticolo);
        return rigaArticolo;
    }

    @Override
    public RigaOrdine salvaRigaOrdine(RigaOrdine rigaOrdine) {
        logger.debug("--> Enter salvaRigaOrdine");

        RigaOrdine rigaOrdineSalvata = null;
        rigaOrdineSalvata = salvaRigaOrdineNoCheck(rigaOrdine);
        AreaOrdine areaOrdine = areaOrdineManager.checkInvalidaAreaMagazzino(rigaOrdineSalvata.getAreaOrdine());
        rigaOrdineSalvata.setAreaOrdine(areaOrdine);

        logger.debug("--> Exit salvaRigaOrdine");
        return rigaOrdineSalvata;
    }

    @Override
    public RigaOrdine salvaRigaOrdineNoCheck(RigaOrdine rigaOrdine) {
        logger.debug("--> Enter salvaRigaOrdine ");
        RigaOrdine rigaOrdineResult = null;
        try {
            rigaOrdineResult = panjeaDAO.save(rigaOrdine);
            rigaOrdineResult.getAreaOrdine().getVersion();
        } catch (Exception e) {
            logger.error("--> errore nel salvare la rigaOrdine", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaRigaOrdine ");
        return rigaOrdineResult;
    }
}
