package it.eurotn.panjea.mrp.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpOrdiniGenerator;
import it.eurotn.panjea.mrp.util.RigheOrdineDaGenerare;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RiferimentiOrdine.ModalitaRicezione;
import it.eurotn.panjea.ordini.exception.TipoAreaOrdineAssenteException;
import it.eurotn.panjea.ordini.manager.documento.interfaces.TipiAreaOrdineManager;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdiniManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.KeyFromValueProvider;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.MrpOrdiniGeneratorBean")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.MrpOrdiniGeneratorBean")
@PermitAll
public class MrpOrdiniGeneratorBean implements MrpOrdiniGenerator {

    private static Logger logger = Logger.getLogger(MrpOrdiniGeneratorBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private EJBContext context;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @EJB
    private TipiAreaOrdineManager tipiAreaOrdineManager;

    @EJB
    private ImportazioneOrdiniManager impoOrdiniManager;

    @EJB
    private MrpManager mrpManager;

    /**
     * @param rigaOrdineDaGenerare
     *            rigaOrdineDaGenerare con i parametri per la ricerca dell'area ordine
     * @return AreaOrdine caricata o null
     */
    @SuppressWarnings("unchecked")
    private AreaOrdine caricaAreaOrdine(RigheOrdineDaGenerare rigaOrdineDaGenerare) {
        StringBuffer sb = new StringBuffer(400);
        sb.append("select a.id as id,a.version as version ");
        sb.append("from AreaOrdine a ");
        sb.append("join a.documento doc ");
        sb.append("join a.depositoOrigine dep ");
        sb.append("join doc.tipoDocumento td ");
        sb.append("where doc.entita.id=:paramIdEntita ");
        sb.append("and dep.id=:paramIdDeposito ");
        sb.append("and doc.dataDocumento=:paramDataDocumento ");
        sb.append("and a.statoAreaOrdine=:paramStatoAreaOrdine ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((AreaOrdine.class)));
        query.setParameter("paramIdEntita", rigaOrdineDaGenerare.getIdFornitore());
        query.setParameter("paramIdDeposito", rigaOrdineDaGenerare.getIdDeposito());
        query.setParameter("paramDataDocumento", rigaOrdineDaGenerare.getDataDocumento());
        query.setParameter("paramStatoAreaOrdine", StatoAreaOrdine.BLOCCATO);

        AreaOrdine primaAreaOrdine = null;
        try {
            List<AreaOrdine> aree = panjeaDAO.getResultList(query);
            if (aree.size() > 0) {
                primaAreaOrdine = aree.get(0);
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return primaAreaOrdine;
    }

    /**
     * @param idRisultatiMrp
     *            lista di risultati per cui trovare le righe fornitore o null per tutte le righe
     * @return List<RigheOrdineDaGenerare>
     */
    private List<RigheOrdineDaGenerare> caricaRigheOrdineFornitoreDaGenerare(
            Integer[] idRisultatiMrp) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("select ");
        sb.append("ra.id as id, ");
        sb.append("ra.idArticolo as idArticolo, ");
        sb.append(
                "(select count(*) from mrp_risultati mrp where ra.id=mrp.distinta_id)>0 as distinta, ");
        sb.append("ra.idDeposito as idDeposito, ");
        sb.append("ra.idFornitore as idFornitore, ");
        sb.append("ra.idTipoDocumento as idTipoDocumento, ");
        sb.append("null as idRigaDistinta, ");
        sb.append("ra.dataConsegna as dataConsegna, ");
        sb.append("ra.dataDocumento as dataDocumento, ");
        sb.append("sum(ra.qtaPor) qta, ");
        sb.append("sum(ra.qtaR) as qtaR, ");
        sb.append("group_concat(ra.idRigaOrdine) righeDaCollegare, ");
        sb.append("null as idConfigurazioneDistinta, ");
        sb.append("null as idComponente, ");
        sb.append("null as formulaComponente, ");
        sb.append("false as nuovoOrdine, ");
        sb.append("false as rilasciaOrdine, ");
        sb.append("ordiniDaCollegare as ordiniDaCollegare ");
        sb.append("from mrp_risultati ra ");
        sb.append("inner join maga_articoli a on ra.idArticolo=a.id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=ra.idTipoDocumento ");
        sb.append("inner join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
        sb.append("where (ra.qtaPor>0)");
        sb.append("and td.tipoEntita=1 ");
        sb.append("and ra.idFornitore<>0 ");
        if (ArrayUtils.isNotEmpty(idRisultatiMrp)) {
            sb.append("and ra.id in (");
            sb.append(StringUtils.join(idRisultatiMrp, ","));
            sb.append(") ");
        }
        sb.append(
                "group by ra.idArticolo,ra.idDeposito,ra.idFornitore,dataConsegna,dataDocumento ");
        sb.append("having distinta=0 ");
        sb.append("order by ra.distinta_id asc ");

        return getRigheOrdineDaGenerare(sb.toString());
    }

    /**
     * @param idRisultatiMrp
     *            lista di risultati per cui trovare le righe produzione o null per tutte le righe
     * @return List<RigheOrdineDaGenerare>
     */
    private List<RigheOrdineDaGenerare> caricaRigheOrdineProduzioneDaGenerare(
            Integer[] idRisultatiMrp) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("select ");
        sb.append("ra.id as id, ");
        sb.append("ra.idArticolo as idArticolo, ");
        sb.append(
                "((select count(*) from mrp_risultati mrp where ra.id=mrp.distinta_id)>0 or a.distinta) as distinta,");
        sb.append("ra.idDeposito as idDeposito, ");
        sb.append("ra.idFornitore as idFornitore, ");
        sb.append("ra.idTipoDocumento as idTipoDocumento, ");
        sb.append("ra.distinta_id as idRigaDistinta, ");
        sb.append("ra.dataConsegna as dataConsegna, ");
        sb.append("ra.dataDocumento as dataDocumento, ");
        sb.append("ra.qtaPor as qta, ");
        sb.append("ra.qtaR as qtaR, ");
        sb.append("ra.idRigaOrdine righeDaCollegare, ");
        sb.append("ra.idConfigurazioneDistinta as idConfigurazioneDistinta, ");
        sb.append("ra.idComponente as idComponente, ");
        sb.append("ra.formula as formulaComponente, ");
        sb.append("false as nuovoOrdine, ");
        sb.append("false as rilasciaOrdine, ");
        sb.append("ordiniDaCollegare as ordiniDaCollegare ");
        sb.append("from mrp_risultati ra ");
        sb.append("inner join maga_articoli a on ra.idArticolo=a.id ");
        sb.append(
                "where ((ra.distinta_id is null and a.distinta=true) or ra.distinta_id is not null) ");
        if (ArrayUtils.isNotEmpty(idRisultatiMrp)) {
            sb.append("and ra.id in (");
            sb.append(StringUtils.join(idRisultatiMrp, ","));
            sb.append(") ");
        }
        sb.append("order by ra.distinta_id asc");

        return getRigheOrdineDaGenerare(sb.toString());
    }

    private void collegaOrdiniFornitoriPresenti() {
    	//Metto idRigaORdine <>0 per togliere le righe di sottoscorta
        Query query = panjeaDAO.prepareQuery(
                "select r.idRigaOrdine,r.ordiniDaCollegare from RisultatoMRP r where r.userInsert=:userInsert and r.ordiniDaCollegare<>'' and r.idRigaOrdine <> 0 order by r.ordinamento");
        query.setParameter("userInsert", getUtente());
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> ordiniDaCollegare = panjeaDAO.getResultList(query);
            // Devo mantenere un set per non aggiungere duplicati. Devo però anche mantenere
            // lìordine con cui li salvo quindi utilizzo una lista per salvare ed il set
            // per verificare se ho duplicati
            for (Object[] ordine : ordiniDaCollegare) {
                int idRigaOrdine = (int) ordine[0];
                String[] righeOrdinefornitoriPresenti = ((String) ordine[1]).split(",");
                for (String rigaOrdinePresente : righeOrdinefornitoriPresenti) {
                    RigaArticolo rigaOrdine = panjeaDAO.loadLazy(RigaArticolo.class,
                            Integer.parseInt(rigaOrdinePresente));
                    System.out.println(idRigaOrdine);
                    rigaOrdine.addRigaOrdineCollegata(
                            panjeaDAO.load(RigaOrdine.class, idRigaOrdine));
                }
                // panjeaDAO.save(rigaOrdine);
            }
        } catch (DAOException e) {
            logger.error("Errore nel recuperare le righe da collegare", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long generaOrdini(Integer[] idRisultatiMrp) {
        collegaOrdiniFornitoriPresenti();
        List<RigheOrdineDaGenerare> righeOrdineFornitore = caricaRigheOrdineFornitoreDaGenerare(
                idRisultatiMrp);
        List<RigheOrdineDaGenerare> righeOrdineProduzione = caricaRigheOrdineProduzioneDaGenerare(
                idRisultatiMrp);
        Long timeStampCreazione = generaOrdini(righeOrdineFornitore, righeOrdineProduzione);
        raggruppaRighe(timeStampCreazione);
        return timeStampCreazione;
    }

    /**
     * Genera ordini dalla lista di righe ordine da generare.
     *
     * @param righeOrdineFornitore
     *            righeOrdineFornitore
     * @param righeOrdineProduzione
     *            righeOrdineProduzione
     *
     * @return timeStampCreazione
     */
    private Long generaOrdini(List<RigheOrdineDaGenerare> righeOrdineFornitore,
            List<RigheOrdineDaGenerare> righeOrdineProduzione) {

        // Cache dei tipiAreaOrdine.
        List<TipoAreaOrdine> tipiAreaOrdineCache = tipiAreaOrdineManager.caricaTipiAreaOrdine(null,
                null, false);
        Map<Integer, TipoAreaOrdine> tipiAreaOrdine = new HashMap<Integer, TipoAreaOrdine>();
        for (TipoAreaOrdine tipoAreaOrdine : tipiAreaOrdineCache) {
            tipiAreaOrdine.put(tipoAreaOrdine.getId(), tipoAreaOrdine);
        }

        // ----------------------------- ORDINI FORNITORE
        // ----------------------------------------
        Map<String, OrdineImportato> ordiniDaGenerareFornitore = new HashMap<String, OrdineImportato>();
        Map<String, RigaOrdineImportata> righeDaGenerareFornitore = new HashMap<String, RigaOrdineImportata>();
        for (RigheOrdineDaGenerare rigaMrp : righeOrdineFornitore) {
            String keyOrdine = getKeyOrdine(rigaMrp, false);
            String keyRiga = keyOrdine + "§" + getKeyRiga(rigaMrp, false);

            OrdineImportato ordineImportato = ObjectUtils
                    .defaultIfNull(ordiniDaGenerareFornitore.get(keyOrdine), new OrdineImportato());
            TipoAreaOrdine tipoAreaOrdineFornitore = tipiAreaOrdine
                    .get(rigaMrp.getIdTipoDocumento());

            if (tipoAreaOrdineFornitore == null) {
                throw new TipoAreaOrdineAssenteException();
            }

            ordineImportato = valorizzaOrdineImportato(ordineImportato, rigaMrp,
                    tipoAreaOrdineFornitore);

            RigaOrdineImportata rigaDaGenerare = ObjectUtils.defaultIfNull(
                    righeDaGenerareFornitore.get(keyRiga), new RigaOrdineImportata());
            rigaDaGenerare = valorizzaRigaDaImportare(rigaDaGenerare, rigaMrp, ordineImportato,
                    tipoAreaOrdineFornitore);
            ordineImportato.addRiga(rigaDaGenerare);

            ordiniDaGenerareFornitore.put(keyOrdine, ordineImportato);
            righeDaGenerareFornitore.put(keyRiga, rigaDaGenerare);
        }
        logger.debug("ordini fornitore " + ordiniDaGenerareFornitore.size());

        // ----------------------------- ORDINI PRODUZIONE
        // ----------------------------------------
        Map<String, OrdineImportato> ordiniDaGenerareProduzione = new HashMap<String, OrdineImportato>();
        Map<String, RigaOrdineImportata> righeDaGenerareProduzione = new HashMap<String, RigaOrdineImportata>();

        // variabile per gestire le distinte in modo da aggiungere i componenti
        // di essa come righe dello stesso ordine
        // importato
        Map<Integer, OrdineImportato> ordiniDistinta = new HashMap<Integer, OrdineImportato>();

        // variabile per gestire le righe distinta in modo da aggiungere le
        // righe componente
        Map<Integer, RigaOrdineImportata> distinte = new HashMap<Integer, RigaOrdineImportata>();

        for (RigheOrdineDaGenerare rigaOrdineDaGenerare : righeOrdineProduzione) {
            String keyOrdine = getKeyOrdine(rigaOrdineDaGenerare, true);
            String keyRiga = keyOrdine + "§" + getKeyRiga(rigaOrdineDaGenerare, true);

            OrdineImportato ordineImportato = ObjectUtils.defaultIfNull(
                    ordiniDaGenerareProduzione.get(keyOrdine), new OrdineImportato());
            TipoAreaOrdine tipoAreaOrdineProduzione = tipiAreaOrdine
                    .get(rigaOrdineDaGenerare.getIdTipoDocumento());

            if (tipoAreaOrdineProduzione == null) {
                throw new TipoAreaOrdineAssenteException();
            }

            RigaOrdineImportata rigaDaGenerare = null;
            OrdineImportato ordineDistintaImportato = null;
            RigaOrdineImportata rigaOrdineComponentePerProduzione = null;

            // per ogni articolo che sia distinta o componente (ma non distinta)
            // l'ordine in
            // accordo con la rigaOrdineDaGenereare
            // if (rigaOrdineDaGenerare.isDistinta()) {
            if (rigaOrdineDaGenerare.getIdRigaDistinta() == null
                    && rigaOrdineDaGenerare.isDistinta() && (rigaOrdineDaGenerare.getQta() > 0)) {
                ordineImportato = valorizzaOrdineImportato(ordineImportato, rigaOrdineDaGenerare,
                        tipoAreaOrdineProduzione);

                rigaDaGenerare = ObjectUtils.defaultIfNull(righeDaGenerareProduzione.get(keyRiga),
                        new RigaOrdineImportata());
                rigaDaGenerare = valorizzaRigaDaImportare(rigaDaGenerare, rigaOrdineDaGenerare,
                        ordineImportato, tipoAreaOrdineProduzione);

                // aggiungo le righe con id come chiave per poter aggiungere poi
                // le righe componente sull' ordine
                // produzione associato
                // (distinte.get(rigaOrdineDaGenerare.getIdRigaDistinta()))
                // Map<Integer, RigaOrdineImportata> righePerId = new
                // HashMap<Integer, RigaOrdineImportata>();
                Map<Integer, RigaOrdineImportata> righePerId = new HashMap<>(
                        ordineImportato.getRighe());
                righePerId.put(rigaDaGenerare.getId(), rigaDaGenerare);
                ordineImportato.setRighe(righePerId);

                ordiniDaGenerareProduzione.put(keyOrdine, ordineImportato);
                righeDaGenerareProduzione.put(keyRiga, rigaDaGenerare);
                ordiniDistinta.put(rigaOrdineDaGenerare.getId(), ordineImportato);
                distinte.put(rigaOrdineDaGenerare.getId(), rigaDaGenerare);
            }

            // devo aver già processato le distinte altrimenti non la trovo, qui
            // processo i componenti
            if (rigaOrdineDaGenerare.getIdRigaDistinta() != null) {
                ordineDistintaImportato = ordiniDistinta
                        .get(rigaOrdineDaGenerare.getIdRigaDistinta());
                // Se non ho la distinta elaborata significa che ho cancellato
                // la riga della distinta dai risultati.
                // Quindi non elaboro il componente.
                if (ordineDistintaImportato != null) {
                    rigaOrdineComponentePerProduzione = ObjectUtils.defaultIfNull(
                            righeDaGenerareProduzione.get(keyRiga), new RigaOrdineImportata());
                    rigaOrdineComponentePerProduzione = valorizzaRigaDaImportare(
                            rigaOrdineComponentePerProduzione, rigaOrdineDaGenerare,
                            ordineDistintaImportato, tipoAreaOrdineProduzione);

                    // Map<Integer, RigaOrdineImportata> righePerIdDistinta =
                    // new HashMap<Integer,
                    // RigaOrdineImportata>();
                    Map<Integer, RigaOrdineImportata> righeEsistentiDistinta = ordineDistintaImportato
                            .getRighe();

                    RigaOrdineImportata rigaOrdineDistinta = righeEsistentiDistinta
                            .get(rigaOrdineComponentePerProduzione.getIdRigaDistinta());
                    if (rigaOrdineDistinta == null) {
                        rigaOrdineDistinta = distinte
                                .get(rigaOrdineComponentePerProduzione.getIdRigaDistinta());
                    }
                    if (rigaOrdineDistinta != null) {
                        rigaOrdineDistinta.addRigaComponente(rigaOrdineComponentePerProduzione);
                    }
                    if (rigaOrdineDistinta == null || rigaOrdineDaGenerare.isDistinta()) {
                        Map<Integer, RigaOrdineImportata> righePerId = new HashMap<Integer, RigaOrdineImportata>(
                                righeEsistentiDistinta);
                        righePerId.put(rigaOrdineComponentePerProduzione.getId(),
                                rigaOrdineComponentePerProduzione);
                        ordineDistintaImportato.setRighe(righePerId);
                    }

                    if (rigaOrdineDaGenerare.isDistinta()) {
                        ordiniDistinta.put(rigaOrdineDaGenerare.getId(), ordineDistintaImportato);
                        distinte.put(rigaOrdineDaGenerare.getId(),
                                rigaOrdineComponentePerProduzione);
                    }
                    righeDaGenerareProduzione.put(keyRiga, rigaOrdineComponentePerProduzione);
                }
            }
        }
        logger.debug("ordini produzione " + ordiniDaGenerareProduzione.size());
        if (logger.isDebugEnabled()) {
            for (OrdineImportato oi : ordiniDaGenerareProduzione.values()) {
                for (RigaOrdineImportata ro : oi.getRighe().values()) {
                    logger.debug("RIGA " + ro.getArticolo().getCodice() + " : " + ro.getQta());
                }
            }
        }
        ArrayList<OrdineImportato> results = new ArrayList<OrdineImportato>(
                ordiniDaGenerareFornitore.values());
        results.addAll(ordiniDaGenerareProduzione.values());
        Long timeStampCreazione = impoOrdiniManager.confermaOrdiniImportati(results);

        mrpManager.svuotaRigheRisultati();

        return timeStampCreazione;
    }

    /**
     * Devo sapere se produzione perchè una riga può essere distinta anche se non ha un articolo
     * distinta (configurazioni), ma per una riga produzione devo creare la chiave senza il
     * fornitore.
     *
     * @param rigaOrdineDaGenerare
     *            riga per la quale calcolare la chiave
     * @param produzione
     *            se produzione o fornitore
     * @return chiave per raggruppare gli ordini. idFornitore,deposito e dataDocumento.
     */
    private String getKeyOrdine(RigheOrdineDaGenerare rigaOrdineDaGenerare, boolean produzione) {
        StringBuilder sb = new StringBuilder(200);
        Integer idFornitore = 0;
        if (!produzione) {
            idFornitore = rigaOrdineDaGenerare.getIdFornitore();
        }
        // per evitare avere un ordine produzione per rigaOrdinte cliente,
        // aggiungo l'id della distinta principale
        // (rigaOrdineDaGenerare.getIdRigaDistinta() == null)
        if (produzione && rigaOrdineDaGenerare.getIdRigaDistinta() == null) {
            sb = sb.append(rigaOrdineDaGenerare.getIdArticolo()).append("#");
            sb = sb.append(rigaOrdineDaGenerare.getIdConfigurazioneDistinta()).append("#");
        }
        sb = sb.append(idFornitore).append("#");
        sb = sb.append(rigaOrdineDaGenerare.getIdDeposito()).append("#");
        sb = sb.append(rigaOrdineDaGenerare.getDataDocumento()).append("#");
        sb = sb.append(rigaOrdineDaGenerare.isNuovoOrdine()).append("#");

        if (rigaOrdineDaGenerare.isNuovoOrdine()) {
            sb = sb.append(rigaOrdineDaGenerare.isRilasciaOrdine());
        } else {
            sb = sb.append("false");
        }
        return sb.toString();
    }

    private String getKeyRiga(RigheOrdineDaGenerare rigaOrdineDaGenerare, boolean produzione) {
        StringBuilder sb = new StringBuilder(200);
        sb = sb.append(rigaOrdineDaGenerare.getIdArticolo()).append("#");
        if (produzione) {
            sb = sb.append(rigaOrdineDaGenerare.getDataDocumento()).append("#");
        } else {
            sb = sb.append(rigaOrdineDaGenerare.getDataConsegna()).append("#");
        }
        return sb.toString();
    }

    /**
     * Carica le righe ordine da generare data una query sql.
     *
     * @param sql
     *            l'sql da eseguire per avere una lista di RigheOrdineDaGenerare
     * @return List<RigheOrdineDaGenerare>
     */
    private List<RigheOrdineDaGenerare> getRigheOrdineDaGenerare(String sql) {
        SQLQuery query = panjeaDAO.prepareNativeQuery(sql, RigheOrdineDaGenerare.class);

        query.addScalar("id", Hibernate.INTEGER);
        query.addScalar("idArticolo", Hibernate.INTEGER);
        query.addScalar("idRigaDistinta", Hibernate.INTEGER);
        query.addScalar("idDeposito", Hibernate.INTEGER);
        query.addScalar("idFornitore", Hibernate.INTEGER);
        query.addScalar("idTipoDocumento", Hibernate.INTEGER);
        query.addScalar("dataConsegna", Hibernate.DATE);
        query.addScalar("dataDocumento", Hibernate.DATE);
        query.addScalar("qta", Hibernate.DOUBLE);
        query.addScalar("qtaR", Hibernate.DOUBLE);
        query.addScalar("righeDaCollegare", Hibernate.STRING);
        query.addScalar("distinta", Hibernate.BOOLEAN);
        query.addScalar("nuovoOrdine", Hibernate.BOOLEAN);
        query.addScalar("rilasciaOrdine", Hibernate.BOOLEAN);
        query.addScalar("idConfigurazioneDistinta", Hibernate.INTEGER);
        query.addScalar("idComponente", Hibernate.INTEGER);
        query.addScalar("formulaComponente", Hibernate.STRING);
        query.addScalar("ordiniDaCollegare", Hibernate.STRING);

        @SuppressWarnings("unchecked")
        List<RigheOrdineDaGenerare> righeOrdineDaGenerare = query.list();
        return righeOrdineDaGenerare;
    }

    private String getUtente() {
        return ((JecPrincipal) context.getCallerPrincipal()).getUserName();
    }

    /**
     * Raggruppa le righe degli ordini fornitori creati per idArticolo e considera la
     * qtaMinimaOrdinabile
     *
     * @param timeStampCreazione
     */
    private void raggruppaRighe(Long timeStampCreazione) {
        Query query = panjeaDAO.prepareQuery(
                "select ro  from RigaArticoloOrdine ro inner join ro.areaOrdine ao where ao.dataCreazioneTimeStamp=:paramTimeStamp and ao.tipoAreaOrdine.ordineProduzione=false order by ao.id,ro.articolo.id,ro.timeStamp");
        query.setParameter("paramTimeStamp", timeStampCreazione);
        try {
            panjeaDAO.getEntityManager().flush();
            @SuppressWarnings("unchecked")
            List<RigaArticolo> righeOrdiniGenerate = panjeaDAO.getResultList(query);
            if (righeOrdiniGenerate.size() == 0) {
                return;
            }

            Query queryQtaMin = panjeaDAO.prepareQuery(
                    "select c.qtaMinimaOrdinabile,c.lottoRiordino from CodiceArticoloEntita c where c.articolo.id=:idArticolo and c.entita.id=:idFornitore");

            Map<String, List<RigaArticolo>> righeRaggruppate = PanjeaEJBUtil.listToMap(
                    righeOrdiniGenerate, new KeyFromValueProvider<RigaArticolo, String>() {

                        @Override
                        public String keyFromValue(RigaArticolo elem) {
                            return elem.getAreaOrdine().getId() + "#" + elem.getArticolo().getId();
                        }
                    });

            for (List<RigaArticolo> righeArticolo : righeRaggruppate.values()) {
                Iterator<RigaArticolo> iteratoRighe = righeArticolo.iterator();
                RigaArticolo rigaArticolo = iteratoRighe.next();

                for (; iteratoRighe.hasNext();) {
                    RigaArticolo rigaArticoloDaRagg = iteratoRighe.next();
                    rigaArticolo.setQtaMagazzino(rigaArticolo.getQta());
                    rigaArticolo.setQtaR(rigaArticoloDaRagg.getQtaR() + rigaArticolo.getQtaR());
                    rigaArticolo.setQta(rigaArticolo.getQtaR());
                    rigaArticolo.getRigheOrdineCollegate()
                            .addAll(rigaArticoloDaRagg.getRigheOrdineCollegate());
                    panjeaDAO.delete(rigaArticoloDaRagg);
                }

                queryQtaMin.setParameter("idArticolo", rigaArticolo.getArticolo().getId());
                queryQtaMin.setParameter("idFornitore",
                        rigaArticolo.getAreaOrdine().getDocumento().getEntita().getId());
                double qtaMinOrd = 0.0;
                try {
                    @SuppressWarnings("unchecked")
                    List<Object[]> qtasResult = panjeaDAO.getResultList(queryQtaMin);
                    if (qtasResult.size() > 0) {
                        qtaMinOrd = (double) qtasResult.get(0)[0];
                    }
                } catch (Exception e) {
                    logger.error("-->errore nel trovare la qta minima impostata per art:"
                            + rigaArticolo.getArticolo().getId());
                    throw new RuntimeException(e);
                }
                if (rigaArticolo.getQtaR() < qtaMinOrd) {
                    rigaArticolo.setQta(qtaMinOrd);
                    rigaArticolo.setQtaMagazzino(qtaMinOrd);
                }
                panjeaDAO.save(rigaArticolo);
            }

            // Controllo la qta
        } catch (DAOException e) {
            logger.error("-->errore Errore nel recuperare le righe generate per raggrupparle", e);
            throw new RuntimeException("Errore nel recuperare le righe generate per raggrupparle",
                    e);
        }

    }

    /**
     * Valorizza l'ordine importato in accordo con i parametri ricevuti.
     *
     * @param ordineImportato
     *            ordineImportato
     * @param rigaOrdineDaGenerare
     *            rigaOrdineDaGenerare
     * @param tipoAreaOrdine
     *            tipoAreaOrdine
     * @return OrdineImportato
     */
    private OrdineImportato valorizzaOrdineImportato(OrdineImportato ordineImportato,
            RigheOrdineDaGenerare rigaOrdineDaGenerare, TipoAreaOrdine tipoAreaOrdine) {
        if (!rigaOrdineDaGenerare.isNuovoOrdine()) {
            AreaOrdine areaOrdineCaricata = caricaAreaOrdine(rigaOrdineDaGenerare);
            ordineImportato.setAreaOrdine(areaOrdineCaricata);
        }

        if (rigaOrdineDaGenerare.getIdFornitore() != null
                && rigaOrdineDaGenerare.getIdFornitore() != 0) {
            FornitoreLite fornitore = panjeaDAO.loadLazy(FornitoreLite.class,
                    rigaOrdineDaGenerare.getIdFornitore());
            SedeEntita sedeFornitore = null;
            try {
                // Non avendo impostato in hibernate l'accesso ai campi tramite
                // proprietà ma con i field
                // il getId inizializza l'oggetto. Lo evito passando dal proxy.
                sedeFornitore = sediEntitaManager
                        .caricaSedePrincipaleEntita(PanjeaEJBUtil.getLazyId(fornitore));
            } catch (AnagraficaServiceException e) {
                logger.error("-->errore nel caricare la sede principale dell'anagrafica", e);
                throw new RuntimeException(e);
            }
            ordineImportato.setEntita(fornitore);
            ordineImportato.setSedeEntita(sedeFornitore);
        }

        DepositoLite deposito = panjeaDAO.loadLazy(DepositoLite.class,
                rigaOrdineDaGenerare.getIdDeposito());

        ordineImportato.setModalitaRicezione(ModalitaRicezione.MRP);

        ordineImportato.setTipoAreaOrdine(tipoAreaOrdine);
        ordineImportato.setDeposito(deposito);
        if (ordineImportato.getTipoAreaOrdine().isOrdineProduzione()) {
            ordineImportato.setData(new Date());
        } else {
            ordineImportato.setData(rigaOrdineDaGenerare.getDataDocumento());
        }
        ordineImportato.setBloccaOrdine(!rigaOrdineDaGenerare.isRilasciaOrdine());

        return ordineImportato;
    }

    /**
     * Crea ed avvalora la RigaOrdineImportata in accordo con i parametri ricevuti.
     *
     * @param rigaOrdineImportata
     *            rigaOrdineImportata
     * @param rigaMrp
     *            rigaOrdineDaGenerare
     * @param ordineImportato
     *            ordineImportato
     * @param tipoAreaOrdine
     *            tipoAreaOrdine
     * @return RigaOrdineImportata
     */
    private RigaOrdineImportata valorizzaRigaDaImportare(RigaOrdineImportata rigaOrdineImportata,
            RigheOrdineDaGenerare rigaMrp, OrdineImportato ordineImportato,
            TipoAreaOrdine tipoAreaOrdine) {

        if (rigaOrdineImportata.getArticolo() == null) {
            ArticoloLite articolo = panjeaDAO.loadLazy(ArticoloLite.class, rigaMrp.getIdArticolo());
            rigaOrdineImportata.setArticolo(articolo);
            rigaOrdineImportata.setNumeroRiga(ordineImportato.getRighe().size() + 1);
            rigaOrdineImportata.setProvenienzaPrezzo(ProvenienzaPrezzo.LISTINO);
            rigaOrdineImportata.setOrdine(ordineImportato);
            rigaOrdineImportata.setIdDistintaConfigurazione(rigaMrp.getIdConfigurazioneDistinta());
            rigaOrdineImportata.setDistinta(rigaMrp.isDistinta());
            rigaOrdineImportata.setFormulaComponente(rigaMrp.getFormulaComponente());
            rigaOrdineImportata.setDataConsegna(rigaMrp.getDataConsegna());
            rigaOrdineImportata.setDataProduzione(rigaMrp.getDataDocumento());
            rigaOrdineImportata.setId(rigaMrp.getId());
            rigaOrdineImportata.setIdRigaDistinta(rigaMrp.getIdRigaDistinta());
        } else {
            System.out.println(rigaOrdineImportata);
        }

        BigDecimal qta = BigDecimal.valueOf(rigaMrp.getQta());
        BigDecimal qtaR = BigDecimal.valueOf(rigaMrp.getQtaR());

        // if (!StringUtils.isBlank(rigaMrp.getOrdiniDaCollegare())) {
        // String[] idStringRighe = rigaMrp.getOrdiniDaCollegare().split(",");
        // //int[] id = new int[idStringRighe.length];
        // for (int i = 0; i < idStringRighe.length; i++) {
        // //id[i] = Integer.parseInt(idStringRighe[i]);
        // int idRigaOrdineFornitoreDaColl= Integer.parseInt(idStringRighe[i]);
        // panjeaDAO.loadLazy(rigaMrp.getid, primaryKey)
        // }
        // }

        if (rigaOrdineImportata.getQta() != null) {
            qta = qta.add(BigDecimal.valueOf(rigaOrdineImportata.getQta()));
        }
        if (rigaOrdineImportata.getQtaR() != null) {
            qtaR = qtaR.add(BigDecimal.valueOf(rigaOrdineImportata.getQtaR()));
        }

        int[] idRigheDaCollegare = rigaMrp.getIdRigheDaCollegare();
        if (rigaOrdineImportata.getIdRigheDaCollegare() != null) {
            idRigheDaCollegare = ArrayUtils.addAll(idRigheDaCollegare,
                    rigaOrdineImportata.getIdRigheDaCollegare());
        }

        rigaOrdineImportata.setIdRigheDaCollegare(idRigheDaCollegare);
        rigaOrdineImportata.setQta(qta.doubleValue());
        rigaOrdineImportata.setQtaR(qtaR.doubleValue());

        return rigaOrdineImportata;
    }
}
