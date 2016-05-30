package it.eurotn.panjea.magazzino.manager.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.domain.ConaiEsenzione;
import it.eurotn.panjea.conai.domain.RigaConaiComponente;
import it.eurotn.panjea.conai.manager.interfaces.ConaiManager;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.lotti.manager.verifica.interfaces.LottiVerificaManager;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoVerificaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.CodiceArticoloEntitaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces.RigaDocumentoManager;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoControlloSchedeArticolo;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.RigheMagazzinoDTOResult;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto.RigaMagazzinoDTOBuilder;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto.RigaMagazzinoDTOFactoryBuilder;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto.RigaMagazzinoDTOResult;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * Classe che gestisce la persistenza di una riga magazzino.<br/>
 * Deve essere fatto un refactoring per creare diverse classi che ereditano<br/>
 * e gestiscano le varie tipologie di righe
 *
 * @author giangi
 * @version 1.0, 20/feb/2012
 *
 */
public abstract class RigaMagazzinoAbstractDAOBean implements RigaMagazzinoDAO {

    protected static Logger logger = Logger.getLogger(RigaMagazzinoAbstractDAOBean.class);

    @IgnoreDependency
    @EJB
    protected AreaMagazzinoCancellaManager areaMagazzinoCancellaManager;

    @EJB
    protected RigaDocumentoManager rigaDocumentoManager;

    @EJB
    protected PanjeaDAO panjeaDAO;

    @EJB
    protected ArticoloManager articoloManager;

    @EJB
    protected LottiManager lottiManager;

    @EJB
    private LottiVerificaManager lottiVerificaManager;

    @EJB
    protected DistintaBaseManager distintaBaseManager;

    @IgnoreDependency
    @EJB
    protected ConaiManager conaiManager;

    @IgnoreDependency
    @EJB
    protected AreaMagazzinoVerificaManager areaMagazzinoVerificaManager;

    @EJB
    @IgnoreDependency
    protected MagazzinoControlloSchedeArticolo magazzinoControlloSchedeArticolo;

    @EJB
    private CodiceArticoloEntitaManager codiceArticoloEntitaManager;

    @Override
    public final AreaMagazzino cancellaRigaMagazzino(RigaMagazzino rigaMagazzino) {
        logger.debug("--> Enter cancellaRigaMagazzino");

        if (rigaMagazzino instanceof RigaArticolo) {
            RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzino;
            magazzinoControlloSchedeArticolo.checkInvalidaSchedeArticolo(rigaArticolo);
        }

        AreaMagazzino areaMagazzino = removeRigaMagazzino(rigaMagazzino);

        logger.debug("--> Exit cancellaRigaMagazzino");
        return areaMagazzino;
    }

    @Override
    public void cancellaRigheAutomatiche(AreaMagazzino areaMagazzino) {

        Query query = panjeaDAO.prepareQuery(
                "delete RigaMagazzino r where r.areaMagazzino.id = :paramIdArea and r.rigaAutomatica = true");
        query.setParameter("paramIdArea", areaMagazzino.getId());

        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            logger.error("--> errore durante la cancellazione delle righe automatiche.", e);
            throw new RuntimeException("errore durante la cancellazione delle righe automatiche.", e);
        }
    }

    @Override
    public RigaArticolo caricaQtaAttrezzaggio(RigaArticolo rigaArticolo,
            ConfigurazioneDistinta configurazioneDistinta) {
        ArticoloLite distinta = rigaArticolo.getArticolo();
        if (rigaArticolo instanceof RigaArticoloComponente) {
            RigaArticoloComponente rac = (RigaArticoloComponente) rigaArticolo;
            distinta = rac.getRigaDistintaCollegata().getArticolo();
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
    public RigaMagazzino caricaRigaMagazzino(RigaMagazzino rigaMagazzino) {
        logger.debug("--> Enter caricaRigaMagazzino");
        RigaMagazzino rigaMagazzinoResult = null;
        try {
            rigaMagazzinoResult = panjeaDAO.load(RigaMagazzino.class, rigaMagazzino.getId());
            // Init dell'areamagazzino lazy
            // Hibernate.initialize(rigaMagazzinoResult.getAreaMagazzino());

            // Init delle righe lotti se previste dall'articolo
            if (rigaMagazzinoResult instanceof RigaArticolo) {
                Hibernate.initialize(((RigaArticolo) rigaMagazzinoResult).getAttributi());
                Hibernate.initialize(((RigaArticolo) rigaMagazzinoResult).getRigheLotto());
                Hibernate.initialize(((RigaArticolo) rigaMagazzinoResult).getRigheConaiComponente());
            }
        } catch (ObjectNotFoundException e) {
            logger.error("--> riga magazzino non trovata ", e);
        }
        logger.debug("--> Exit caricaRigaMagazzino");
        return rigaMagazzinoResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaArticolo> caricaRigheArticolo(AreaMagazzino areaMagazzino) {
        return (List<RigaArticolo>) caricaRigheMagazzino(areaMagazzino, true, false);
    }

    @Override
    public List<? extends RigaMagazzino> caricaRigheMagazzino(AreaMagazzino areaMagazzino) {
        return caricaRigheMagazzino(areaMagazzino, false, false);
    }

    /**
     *
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @param soloRigaArticolo
     *            indica se caricare o meno solamente le righe di classe {@link RigaArticolo}
     * @param fullJoin
     *            se <code>true</code> carica tutte le properietà lazy non standard della riga magazzino ( es: righe
     *            conai )
     * @return lista delle righe dell'area magazzino
     */
    @SuppressWarnings("unchecked")
    private List<? extends RigaMagazzino> caricaRigheMagazzino(AreaMagazzino areaMagazzino, boolean soloRigaArticolo,
            boolean fullJoin) {
        logger.debug("--> Enter caricaRigheMagazzino");

        RigaMagazzino rigaMagazzino = null;

        StringBuilder sb = new StringBuilder();

        sb.append("select ");
        sb.append("r, ");
        sb.append("(select sum(rc.qta) from RigaMagazzino rc where rc.rigaMagazzinoCollegata = r), ");
        sb.append("( ");
        sb.append("   select ");
        sb.append("   sum(rc2.qtaMagazzino) ");
        sb.append("   from RigaMagazzino rc2 ");
        sb.append("   where rc2.rigaMagazzinoCollegata = r ");
        sb.append(") ");
        if (fullJoin) {
            // devo caricare le descrizioni dell'omaggio con tipo uguale al tipo
            // della riga
            sb.append(",( ");
            sb.append(
                    "select o.descrizionePerStampa from Omaggio o where o.tipoOmaggio=r.tipoOmaggio and r.tipoOmaggio<>0 and r.tipoOmaggio<>4");
            sb.append(") ");
        }
        sb.append("from RigaMagazzino r ");
        sb.append("left join fetch r.articolo art ");
        sb.append("left join fetch r.attributi att ");
        // sb.append("left join fetch att.tipoAttributo tip ");
        // sb.append("left join fetch tip.unitaMisura ");
        sb.append("left join fetch r.categoriaContabileArticolo ");
        if (fullJoin) {
            sb.append("left join fetch r.righeLotto ");
            sb.append("left join fetch r.righeConaiComponente ");
        }
        sb.append("where r.areaMagazzino.id =:paramAreaMagazzino ");
        if (soloRigaArticolo) {
            sb.append(
                    "and r.class!=it.eurotn.panjea.magazzino.domain.RigaNota and r.class!=it.eurotn.panjea.magazzino.domain.RigaTestata ");
        }
        sb.append("order by r.ordinamento ");

        Query query = panjeaDAO.prepareQuery(sb.toString());

        query.setParameter("paramAreaMagazzino", areaMagazzino.getId());
        List<Object[]> resultQuery = new ArrayList<Object[]>();
        resultQuery = query.getResultList();

        // uso un set perchè la query che carica le righe magazzino non riesce a
        // fare la distinct
        // a causa delle 2 subselect per il calcolo delle qta
        Set<RigaMagazzino> righeMagazzino = new HashSet<RigaMagazzino>();

        for (Object[] objects : resultQuery) {

            rigaMagazzino = (RigaMagazzino) objects[0];

            // se la riga magazzino è una riga articolo setto la quantità chiusa
            // nel campo transiente.
            if (rigaMagazzino.getDomainClassName().equals(RigaArticolo.class.getName())) {

                Double qtaChiusa = (Double) objects[1];
                if (qtaChiusa == null) {
                    qtaChiusa = new Double(0);
                }
                ((RigaArticolo) rigaMagazzino).setQtaChiusa(qtaChiusa);

                Double qtaMagazzinoChiusa = (Double) objects[2];
                if (qtaMagazzinoChiusa == null) {
                    qtaMagazzinoChiusa = new Double(0);
                }
                ((RigaArticolo) rigaMagazzino).setQtaMagazzinoChiusa(qtaMagazzinoChiusa);

                if (fullJoin) {
                    String descrizioneOmaggio = (String) objects[3];
                    if (descrizioneOmaggio != null) {
                        ((RigaArticolo) rigaMagazzino).setNoteOmaggiPerStampa(descrizioneOmaggio);
                    }
                }
            }

            righeMagazzino.add(rigaMagazzino);
        }

        List<RigaMagazzino> resultList = new ArrayList<RigaMagazzino>();
        resultList.addAll(righeMagazzino);
        // devo riordinarli perche' l'ordine viene perso quando si riorganizza
        // per il calcolo delle quantita'
        // ordino per area e ordinamento
        Collections.sort(resultList, new Comparator<RigaMagazzino>() {

            @Override
            public int compare(RigaMagazzino o1, RigaMagazzino o2) {
                if (o1.getAreaMagazzino().getId().compareTo(o2.getAreaMagazzino().getId()) >= 0) {
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
        logger.debug("--> Exit caricaRigheMagazzino");
        return resultList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaMagazzino> caricaRigheMagazzino(AreaMagazzino areaMagazzino, Integer idArticolo) {
        logger.debug("--> Enter caricaRigheMagazzino");
        Query query = panjeaDAO.prepareNamedQuery("RigaMagazzino.caricaByAreaByArticolo");
        query.setParameter("paramIdAreaMagazzino", areaMagazzino.getId());
        query.setParameter("paramIdArticolo", idArticolo);
        List<RigaMagazzino> righeMagazzino = query.getResultList();
        logger.debug("--> Exit caricaRigheMagazzino");
        return righeMagazzino;
    }

    @SuppressWarnings("unchecked")
    @Override
    public RigheMagazzinoDTOResult caricaRigheMagazzinoDTO(AreaMagazzino areaMagazzino) {
        StringBuffer sb = new StringBuffer();

        Integer entitaId = null;
        if (areaMagazzino.getDocumento().getEntita() != null
                && areaMagazzino.getDocumento().getEntita().getId() != null) {
            entitaId = areaMagazzino.getDocumento().getEntita().getId();
        }

        sb.append("select ");
        sb.append("rigamagazz0_.TIPO_RIGA as tipoRiga, ");
        sb.append("rigamagazz0_.id as id, ");
        sb.append("rigamagazz0_.areaMagazzino_id as idAreaMagazzino, ");
        sb.append("articololi1_.id as idArticolo, ");
        sb.append("articololi1_.codice as codiceArticolo, ");
        sb.append("articololi1_.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("articololi1_.barCode as barcodeArticolo, ");
        sb.append("(select codiciarti3_.codice from maga_articoli articolo2_ ");
        sb.append(
                "left outer join maga_codici_articolo_entita codiciarti3_ on articolo2_.id=codiciarti3_.articolo_id ");
        sb.append("where codiciarti3_.entita_id=" + entitaId);
        sb.append(" and articololi1_.id=articolo2_.id limit 1) as codiceArticoloEntita, ");
        sb.append("articoloPadre.id as idArticoloPadre, ");
        sb.append("articoloPadre.codice as codiceArticoloPadre, ");
        sb.append("articoloPadre.descrizioneLinguaAziendale as descrizioneArticoloPadre, ");
        sb.append("(select codiciarti3_.codice from maga_articoli articoloPadre2 ");
        sb.append(
                "left outer join maga_codici_articolo_entita codiciarti3_ on articoloPadre2.id=codiciarti3_.articolo_id ");
        sb.append("where codiciarti3_.entita_id=" + entitaId);
        sb.append(" and articoloPadre.id=articoloPadre2.id) as codiceArticoloPadreEntita, ");
        sb.append("coalesce(rigamagazz0_.numeroDecimaliPrezzo,0) as numeroDecimaliPrezzo, ");
        sb.append("coalesce(rigamagazz0_.numeroDecimaliQta,0) as numeroDecimaliQta, ");
        sb.append("rigamagazz0_.descrizione as descrizioneRiga, ");
        sb.append("rigamagazz0_.codiceValuta as codiceValutaPrezzoUnitario, ");
        sb.append("rigamagazz0_.importoInValuta as importoInValutaPrezzoUnitario, ");
        sb.append("rigamagazz0_.importoInValutaAzienda as importoInValutaAziendaPrezzoUnitario, ");
        sb.append("rigamagazz0_.importoInValutaUnitarioImposta as importoInValutaUnitarioImposta, ");
        sb.append("rigamagazz0_.importoInValutaAziendaUnitarioImposta as importoInValutaAziendaUnitarioImposta, ");
        sb.append("rigamagazz0_.tassoDiCambio as tassoDiCambioPrezzoUnitario, ");
        sb.append("rigamagazz0_.qta as qtaRiga, ");
        sb.append("rigamagazz0_.codiceValutaNetto as codiceValutaPrezzoNetto, ");
        sb.append("rigamagazz0_.importoInValutaNetto as importoInValutaPrezzoNetto, ");
        sb.append("rigamagazz0_.importoInValutaAziendaNetto as importoInValutaAziendaPrezzoNetto, ");
        sb.append("rigamagazz0_.tassoDiCambioNetto as tassoDiCambioPrezzoNetto, ");
        sb.append("rigamagazz0_.ivata as ivata, ");
        sb.append("rigamagazz0_.strategiaTotalizzazioneDocumento as strategiaTotalizzazioneDocumento, ");
        sb.append("rigamagazz0_.variazione1 as variazione1, ");
        sb.append("rigamagazz0_.variazione2 as variazione2, ");
        sb.append("rigamagazz0_.variazione3 as variazione3, ");
        sb.append("rigamagazz0_.variazione4 as variazione4, ");
        sb.append("rigamagazz0_.codiceValutaTotale as codiceValutaPrezzoTotale, ");
        sb.append("rigamagazz0_.importoInValutaTotale as importoInValutaPrezzoTotale, ");
        sb.append("rigamagazz0_.importoInValutaAziendaTotale as importoInValutaAziendaPrezzoTotale, ");
        sb.append("rigamagazz0_.tassoDiCambioTotale as tassoDiCambioPrezzoTotale, ");
        sb.append("ifnull(rigamagazz0_.rigaAutomatica,0) as rigaAutomatica, ");
        sb.append("(select ");
        sb.append("ifnull(sum(rigamagazz4_.qta),0) ");
        sb.append("from ");
        sb.append("maga_righe_magazzino rigamagazz4_, ");
        sb.append("maga_righe_magazzino rigamagazz5_ ");
        sb.append("where ");
        sb.append("rigamagazz4_.rigaMagazzinoCollegata_id=rigamagazz5_.id ");
        sb.append("and rigamagazz4_.rigaMagazzinoCollegata_id=rigamagazz0_.id) as qtaChiusa, ");
        sb.append("rigamagazz0_.nota as rigaNota, ");
        sb.append("rigamagazz0_.noteRiga as noteRiga, ");
        sb.append("rigamagazz0_.livello as livello, ");
        sb.append("rigamagazz0_.codiceTipoDocumentoCollegato as codiceTipoDocumentoCollegato, ");
        sb.append("rigamagazz0_.areaOrdineCollegata_id as idAreaOrdineCollegata, ");
        sb.append("rigamagazz0_.areaMagazzinoCollegata_id as idAreaMagazzinoCollegata, ");
        sb.append("codIva.id as idCodiceIva, ");
        sb.append("codIva.codice as codiceCodiceIva,");
        sb.append("codIva.percApplicazione as percApplicazioneCodiceIva ");
        sb.append("from ");
        sb.append("maga_righe_magazzino rigamagazz0_ ");
        sb.append("left outer join maga_articoli articololi1_ on rigamagazz0_.articolo_id=articololi1_.id ");
        sb.append("left outer join maga_articoli articoloPadre on rigamagazz0_.articoloDistinta_id=articoloPadre.id ");
        sb.append("left outer join anag_codici_iva codIva on codIva.id = rigamagazz0_.codiceIva_id ");
        sb.append("where ");
        sb.append("rigamagazz0_.areaMagazzino_id=" + areaMagazzino.getId().intValue());
        sb.append(" order by rigamagazz0_.ordinamento,rigamagazz0_.id,articololi1_.codice ");

        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(sb.toString());
        SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
        sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaMagazzinoDTOResult.class));
        List<RigaMagazzinoDTOResult> righeMagazzinoDTOBuilder = null;
        try {

            sqlQuery.addScalar("tipoRiga");
            sqlQuery.addScalar("id");
            sqlQuery.addScalar("idAreaMagazzino");
            sqlQuery.addScalar("idArticolo");
            sqlQuery.addScalar("codiceArticolo");
            sqlQuery.addScalar("descrizioneArticolo");
            sqlQuery.addScalar("barcodeArticolo");
            sqlQuery.addScalar("codiceArticoloEntita");
            sqlQuery.addScalar("idArticoloPadre");
            sqlQuery.addScalar("codiceArticoloPadre");
            sqlQuery.addScalar("descrizioneArticoloPadre");
            sqlQuery.addScalar("codiceArticoloPadreEntita");
            sqlQuery.addScalar("numeroDecimaliPrezzo", Hibernate.INTEGER);
            sqlQuery.addScalar("numeroDecimaliQta", Hibernate.INTEGER);
            sqlQuery.addScalar("descrizioneRiga");
            sqlQuery.addScalar("codiceValutaPrezzoUnitario");
            sqlQuery.addScalar("importoInValutaPrezzoUnitario");
            sqlQuery.addScalar("importoInValutaAziendaPrezzoUnitario");
            sqlQuery.addScalar("importoInValutaUnitarioImposta");
            sqlQuery.addScalar("importoInValutaAziendaUnitarioImposta");
            sqlQuery.addScalar("tassoDiCambioPrezzoUnitario");
            sqlQuery.addScalar("qtaRiga");
            sqlQuery.addScalar("codiceValutaPrezzoNetto");
            sqlQuery.addScalar("importoInValutaPrezzoNetto");
            sqlQuery.addScalar("importoInValutaAziendaPrezzoNetto");
            sqlQuery.addScalar("tassoDiCambioPrezzoNetto");
            sqlQuery.addScalar("variazione1");
            sqlQuery.addScalar("variazione2");
            sqlQuery.addScalar("variazione3");
            sqlQuery.addScalar("variazione4");
            sqlQuery.addScalar("codiceValutaPrezzoTotale");
            sqlQuery.addScalar("importoInValutaPrezzoTotale");
            sqlQuery.addScalar("importoInValutaAziendaPrezzoTotale");
            sqlQuery.addScalar("tassoDiCambioPrezzoTotale");
            sqlQuery.addScalar("ivata", Hibernate.BOOLEAN);
            sqlQuery.addScalar("strategiaTotalizzazioneDocumento", Hibernate.INTEGER);
            sqlQuery.addScalar("qtaChiusa");
            sqlQuery.addScalar("rigaNota", Hibernate.STRING);
            sqlQuery.addScalar("noteRiga", Hibernate.STRING);
            sqlQuery.addScalar("rigaAutomatica", Hibernate.BOOLEAN);
            sqlQuery.addScalar("livello");
            sqlQuery.addScalar("codiceTipoDocumentoCollegato");
            sqlQuery.addScalar("idAreaMagazzinoCollegata");
            sqlQuery.addScalar("idAreaOrdineCollegata");
            sqlQuery.addScalar("idCodiceIva");
            sqlQuery.addScalar("codiceCodiceIva");
            sqlQuery.addScalar("percApplicazioneCodiceIva");

            righeMagazzinoDTOBuilder = sqlQuery.list();
        } catch (Exception e) {
            logger.error("--> errore in caricaRigheMagazzino per area magazzino con id " + areaMagazzino.getId(), e);
            throw new RuntimeException(e);
        }

        RigheMagazzinoDTOResult righeMagazzinoDTO = convertResultToDTO(righeMagazzinoDTOBuilder);

        return righeMagazzinoDTO;
    }

    @Override
    public List<? extends RigaMagazzino> caricaRigheMagazzinoStampa(AreaMagazzino areaMagazzino) {
        return caricaRigheMagazzino(areaMagazzino, false, true);
    }

    /**
     * Si preoccupa di convertire ed eventualmente raggruppare eventuali righe dove l'articolo padre e il prezzo sono
     * gli stessi.<br>
     * Nota che per il raggruppamento vengono unite le righe contigue con padre e prezzo uguali.
     *
     * @param righeBuilder
     *            la lista di RigaMagazzinoDTOBuilder da scorrere e raggruppare
     * @return List<RigaMagazzinoDTO>
     */
    private RigheMagazzinoDTOResult convertResultToDTO(List<RigaMagazzinoDTOResult> righeBuilder) {
        Map<String, RigaMagazzinoDTO> righeComposte = new HashMap<String, RigaMagazzinoDTO>();
        RigheMagazzinoDTOResult result = new RigheMagazzinoDTOResult();

        RigaMagazzinoDTOFactoryBuilder factoryBuilder = new RigaMagazzinoDTOFactoryBuilder();

        for (RigaMagazzinoDTOResult rigaMagazzinoDTOResult : righeBuilder) {
            RigaMagazzinoDTOBuilder dtoBuilder = factoryBuilder.getBuilder(rigaMagazzinoDTOResult);
            dtoBuilder.fillResult(rigaMagazzinoDTOResult, result, righeComposte);

            result.setNumeroDecimaliQta(
                    Math.max(result.getNumeroDecimaliQta(), rigaMagazzinoDTOResult.getNumeroDecimaliQta()));
            result.setNumeroDecimaliPrezzo(
                    Math.max(result.getNumeroDecimaliPrezzo(), rigaMagazzinoDTOResult.getNumeroDecimaliPrezzo()));
        }
        return result;
    }

    @Override
    public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        RigaArticolo rigaArticolo = (RigaArticolo) rigaDocumentoManager.creaRigaArticoloDocumento(new RigaArticolo(),
                parametriCreazioneRigaArticolo);
        Set<RigaConaiComponente> righeComponenteConai = creaRigheConaiComponente(rigaArticolo,
                parametriCreazioneRigaArticolo);
        rigaArticolo.setRigheConaiComponente(righeComponenteConai);

        if (parametriCreazioneRigaArticolo.getIdEntita() != null) {
            int idArticolo = rigaArticolo.getArticolo().getId();
            int idEntita = parametriCreazioneRigaArticolo.getIdEntita();
            CodiceArticoloEntita codiceEntita = codiceArticoloEntitaManager.caricaCodiceArticoloEntita(idArticolo,
                    idEntita);
            if (codiceEntita != null) {
                rigaArticolo.setCodiceEntita(codiceEntita.getCodice());
            }
        }

        return rigaArticolo;
    }

    @Override
    public Set<RigaConaiComponente> creaRigheConaiComponente(RigaArticolo riga,
            ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        Articolo articolo;
        try {
            articolo = panjeaDAO.load(Articolo.class, parametriCreazioneRigaArticolo.getIdArticolo());
        } catch (ObjectNotFoundException e) {
            logger.error("--> articolo non trovato: idArticolo " + parametriCreazioneRigaArticolo.getIdArticolo(), e);
            throw new RuntimeException(e);
        }

        Set<RigaConaiComponente> componenti = new HashSet<RigaConaiComponente>();
        if (articolo.hasComponentiConai() && parametriCreazioneRigaArticolo.isGestioneConai()) {
            Set<ConaiComponente> componentiConai = articolo.getComponentiConai();
            for (ConaiComponente conaiComponente : componentiConai) {
                ConaiMateriale materiale = conaiComponente.getMateriale();
                ConaiArticolo conaiArticolo = conaiManager.caricaArticoloConai(materiale);

                // devo recuperare l'esenzione a seconda dell'entità, verifico
                // l'entità per id quindi posso permettermi
                // di istanziare un cliente anche se l'entita potrebbe essere
                // diversa
                EntitaLite ent = new ClienteLite();
                ent.setId(parametriCreazioneRigaArticolo.getIdEntita());
                ConaiEsenzione esenzione = conaiArticolo.getEsenzione(ent);

                RigaConaiComponente rigaConaiComponente = new RigaConaiComponente();
                Integer numeroDecimali = conaiArticolo.getArticolo() == null ? 2
                        : conaiArticolo.getArticolo().getNumeroDecimaliQta();
                rigaConaiComponente.initialize(conaiComponente, numeroDecimali);
                rigaConaiComponente.setPercentualeEsenzione(
                        esenzione != null ? esenzione.getPercentualeEsenzione() : BigDecimal.ZERO);
                rigaConaiComponente.setRigaArticolo(riga);

                if (parametriCreazioneRigaArticolo.isNotaCredito()) {
                    rigaConaiComponente.setPesoUnitario(rigaConaiComponente.getPesoUnitario().negate());
                }

                rigaConaiComponente.calcolaEsenzione();
                componenti.add(rigaConaiComponente);
            }
        }
        return componenti;
    }

    /**
     * Invalida le schede articolo se necessario per la riga magazzino.
     *
     * @param rigaMagazzino
     *            riga
     */
    protected void invalidaSchedeArticolo(RigaMagazzino rigaMagazzino) {

        if (rigaMagazzino instanceof RigaArticolo) {
            RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzino;

            // se sto inserendo la riga vado a invalidare le schede per l'articolo de necessario
            if (rigaArticolo.isNew()) {
                magazzinoControlloSchedeArticolo.checkInvalidaSchedeArticolo(rigaArticolo);
            } else {
                // se l'oggetto è già in sessione devo toglierlo in modo da poter ricaricare la riga
                // old con i valori
                // originali non modificati
                if (((Session) panjeaDAO.getEntityManager().getDelegate()).contains(rigaArticolo)) {
                    ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rigaArticolo);
                }
                // carico la riga già presente
                RigaArticolo rigaArticoloOld = null;
                try {
                    rigaArticoloOld = panjeaDAO.load(RigaArticolo.class, rigaArticolo.getId());
                } catch (ObjectNotFoundException e) {
                    // non faccio niente perchè sono sicuro che la riga articolo è presente
                    rigaArticoloOld = null;
                }

                magazzinoControlloSchedeArticolo.checkInvalidaSchedeArticolo(rigaArticoloOld, rigaArticolo);
            }
        }

    }

    /**
     * Cancella la {@link RigaMagazzino}.
     *
     * @param rigaMagazzino
     *            riga da cancellare
     *
     * @return area Magazzino con lo stato aggiornato
     */
    protected AreaMagazzino removeRigaMagazzino(RigaMagazzino rigaMagazzino) {
        logger.debug("--> Enter removeRigaMagazzino");
        try {
            Class<?> clazz = null;
            if (rigaMagazzino instanceof HibernateProxy) {
                HibernateProxy proxy = (HibernateProxy) rigaMagazzino;
                clazz = proxy.getHibernateLazyInitializer().getPersistentClass();
            } else {
                clazz = rigaMagazzino.getClass();
            }
            rigaMagazzino = (RigaMagazzino) panjeaDAO.load(clazz, rigaMagazzino.getId());
            panjeaDAO.delete(rigaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore in cancellaRigaMagazzino", e);
            throw new RuntimeException(e);
        }

        // verifico i lotti solo se si sta cancellando un movimento di carico
        switch (rigaMagazzino.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento()) {
        case CARICO:
        case INVENTARIO:
        case TRASFERIMENTO:
        case CARICO_PRODUZIONE:
            try {
                lottiVerificaManager.verifica(rigaMagazzino, rigaMagazzino.getAreaMagazzino().getDepositoOrigine());
            } catch (Exception e) {
                throw new RuntimeException("eccezione dopo la cancellazione delle righe magazzino", e);
            }
        default:
            // non faccio niente perchè i movimenti di scarico liberano quantità
            // nel lotto.
        }

        lottiManager.cancellaLottiNonUtilizzati();
        logger.debug("--> Exit removeRigaMagazzino");
        return areaMagazzinoVerificaManager.checkInvalidaAreaMagazzino(rigaMagazzino.getAreaMagazzino());
    }

    @Override
    public final RigaMagazzino salvaRigaMagazzino(RigaMagazzino rigaMagazzino)
            throws RimanenzaLottiNonValidaException, RigheLottiNonValideException, QtaLottiMaggioreException {
        logger.debug("--> Enter salvaRigaMagazzino");

        invalidaSchedeArticolo(rigaMagazzino);

        RigaMagazzino rigaMagazzinoSalvata = saveRigaMagazzino(rigaMagazzino);

        logger.debug("--> Exit salvaRigaMagazzino");
        return rigaMagazzinoSalvata;
    }

    @Override
    public RigaMagazzino salvaRigaMagazzinoNoCheck(RigaMagazzino rigaMagazzino) {

        invalidaSchedeArticolo(rigaMagazzino);

        RigaMagazzino rigaMagazzinoSalvata = saveRigaMagazzinoNoCheck(rigaMagazzino);

        return rigaMagazzinoSalvata;
    }

    /**
     * Salva la riga magazzino.
     *
     * @param rigaMagazzino
     *            rigaMagazzino da salvare
     * @return riga magazzino salvata
     * @throws RimanenzaLottiNonValidaException
     *             rilanciata se non c'è una rimanenza nei lotti valida.
     * @throws RigheLottiNonValideException
     *             rilanciata se le righe lotti non sono valide
     * @throws QtaLottiMaggioreException
     *             rilanciata se la quantità assegnata ai lotti supera la quantità della riga articolo
     */
    protected RigaMagazzino saveRigaMagazzino(RigaMagazzino rigaMagazzino)
            throws RimanenzaLottiNonValidaException, RigheLottiNonValideException, QtaLottiMaggioreException {
        // // effettuare refactoring tramite visitor
        RigaMagazzino rigaMagazzinoSalvata = saveRigaMagazzinoNoCheck(rigaMagazzino);

        AreaMagazzino areaMagazzino = areaMagazzinoVerificaManager
                .checkInvalidaAreaMagazzino(rigaMagazzino.getAreaMagazzino());
        rigaMagazzinoSalvata.setAreaMagazzino(areaMagazzino);

        return rigaMagazzinoSalvata;
    }

    /**
     * Salva la riga magazzino.
     *
     * @param rigaMagazzino
     *            rigaMagazzino da salvare
     * @return riga magazzino salvata
     */
    protected RigaMagazzino saveRigaMagazzinoNoCheck(RigaMagazzino rigaMagazzino) {
        // // effettuare refactoring tramite visitor
        if (rigaMagazzino.getNotePerStampa(null) == null) {
            rigaMagazzino.setNoteRiga(null);
        }

        RigaMagazzino rigaMagazzinoSalvata = null;
        try {
            rigaMagazzinoSalvata = panjeaDAO.save(rigaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore nel salvare la rigaMagazzino ", e);
            throw new RuntimeException(e);
        }

        if (rigaMagazzino instanceof RigaArticolo) {
            String codiceEntita = ((RigaArticolo) rigaMagazzino).getCodiceEntita();
            ((RigaArticolo) rigaMagazzinoSalvata).setCodiceEntita(codiceEntita);
        }

        return rigaMagazzinoSalvata;
    }
}
