package it.eurotn.panjea.magazzino.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.manager.sqlbuilder.GiacenzaQueryBuilder;
import it.eurotn.panjea.magazzino.util.DisponibilitaArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * Gestisce i vari metodi per il calcolo della giacenza di un articolo.
 *
 * @author adriano
 * @version 1.0, 03/set/07
 */
@Stateless(name = "Panjea.GiacenzaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GiacenzaManager")
public class GiacenzaManagerBean implements GiacenzaManager {

    private static Logger logger = Logger.getLogger(GiacenzaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @Resource
    private SessionContext context;

    @Override
    public Map<String, Map<Integer, List<DisponibilitaArticolo>>> calcolaDisponibilita(List<Articolo> articoli,
            Integer idDeposito, Date dataDisponibilita) {
        StringBuilder sb = new StringBuilder();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Se la riga è un componente metto in join la rigaordine della sua
        // distinta con la rigaMagazzino evasa della distinta.
        // Quindi se una distinta è evasa anche solo parzialmente i suoi
        // componenti possono essere considerati consumati.
        sb.append("select ");
        sb.append("dispRo.idArticolo, ");
        sb.append(
                "sum(if(dispRo.tipoRiga='A' and dispRo.tipoEntita=0,dispRo.qtaRo-coalesce(dispRo.qtaRm,0),0)) + sum(if(dispRo.tipoRiga='C' and dispRo.tipoEntita=2,dispRo.qtaRo-coalesce(dispRo.qtaRm,0),0)) as fabbisogno, ");
        sb.append(
                "sum(if(dispRo.tipoRiga='A' and dispRo.tipoEntita=1,dispRo.qtaRo-coalesce(dispRo.qtaRm,0),0)) + sum(if(dispRo.tipoRiga='A' and dispRo.tipoEntita=2,dispRo.qtaRo-coalesce(dispRo.qtaRm,0),0)) as carico, ");
        sb.append("dispRo.dataConsegna as dataConsegna, ");
        sb.append("dispRo.codiceDeposito as codiceDeposito, ");
        sb.append("dispRo.idDeposito as idDeposito ");
        sb.append("from ");
        sb.append("(select ");
        sb.append("ro.TIPO_RIGA as tipoRiga, ");
        sb.append("ro.articolo_id as idArticolo, ");
        sb.append("td.tipoEntita as tipoEntita, ");
        sb.append("max(ro.qta) as qtaRo, ");
        sb.append("sum(rm.qta) as qtaRm, ");
        sb.append("ro.dataConsegna as dataConsegna, ");
        sb.append("dep.codice as codiceDeposito, ");
        sb.append("dep.id as idDeposito ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("left join maga_righe_magazzino rm on ro.id=rm.rigaOrdineCollegata_Id ");
        sb.append("inner join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=ao.tipoAreaOrdine_id ");
        sb.append("inner join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
        sb.append("inner join docu_documenti doc on doc.id=ao.documento_id ");
        sb.append("inner join anag_depositi dep on dep.id=ao.depositoOrigine_id ");
        sb.append("where td.tipoEntita in (0,1,2) ");
        sb.append("and ro.TIPO_RIGA = 'A' ");
        sb.append(" and ro.evasioneForzata=false ");
        sb.append(" and ro.articolo_id in (");
        for (Articolo articolo : articoli) {
            sb.append(articolo.getId()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");

        if (idDeposito != null) {
            sb.append(" and dep.id=").append(idDeposito);
        }
        if (dataDisponibilita != null) {
            sb.append(" and ro.dataConsegna<='").append(df.format(dataDisponibilita)).append("' ");
        }
        sb.append(" group by ao.id,ro.id ");
        sb.append("union all ");
        sb.append("select ");
        sb.append("ro.TIPO_RIGA as tipoRiga, ");
        sb.append("ro.articolo_id as idArticolo, ");
        sb.append("td.tipoEntita as tipoEntita, ");
        sb.append("max(coalesce(rm.qta,ro.qta)) as qtaRo, ");
        sb.append("sum(rm.qta) as qtaRm, ");
        sb.append("ro.dataConsegna as dataConsegna, ");
        sb.append("dep.codice as codiceDeposito, ");
        sb.append("dep.id as idDeposito ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("inner join ordi_righe_ordine ropadre on ropadre.id=ro.rigaDistintaCollegata_id ");
        sb.append("left join maga_righe_magazzino rm on ropadre.id=rm.rigaOrdineCollegata_Id ");
        sb.append("inner join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=ao.tipoAreaOrdine_id ");
        sb.append("inner join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
        sb.append("inner join docu_documenti doc on doc.id=ao.documento_id ");
        sb.append("inner join anag_depositi dep on dep.id=ao.depositoOrigine_id ");
        sb.append("where td.tipoEntita in (0,1,2) ");
        sb.append("and ro.TIPO_RIGA ='C' ");
        sb.append(" and ro.evasioneForzata=false ");
        sb.append(" and ro.articolo_id in (");
        for (Articolo articolo : articoli) {
            sb.append(articolo.getId()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");

        if (idDeposito != null) {
            sb.append(" and dep.id=").append(idDeposito);
        }
        if (dataDisponibilita != null) {
            sb.append(" and ro.dataConsegna<='").append(df.format(dataDisponibilita)).append("' ");
        }
        sb.append(" group by ao.id,ro.id ");
        sb.append("order by null ");
        sb.append(") as dispRo ");
        sb.append("group by idDeposito,idArticolo,dataConsegna,tipoEntita ");
        sb.append("having fabbisogno + carico>0 ");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString(), DisponibilitaArticolo.class);
        query.addScalar("idArticolo");
        query.addScalar("fabbisogno");
        query.addScalar("carico");
        query.addScalar("dataConsegna");
        query.addScalar("codiceDeposito");
        query.addScalar("idDeposito");

        @SuppressWarnings("unchecked")
        List<DisponibilitaArticolo> dispGenerica = query.list();
        // mappa con chiave codiceDeposito,valore mappa con chiave articolo.
        Map<String, Map<Integer, List<DisponibilitaArticolo>>> dispPerDeposito = new HashMap<String, Map<Integer, List<DisponibilitaArticolo>>>();
        for (DisponibilitaArticolo disponibilitaArticolo : dispGenerica) {
            // Verifico se nella mappa principale ho il deposito. in caso lo
            // creo con una mappa vuota
            if (!dispPerDeposito.containsKey(disponibilitaArticolo.getCodiceDeposito())) {
                dispPerDeposito.put(disponibilitaArticolo.getCodiceDeposito(),
                        new HashMap<Integer, List<DisponibilitaArticolo>>());
            }
            if (!dispPerDeposito.get(disponibilitaArticolo.getCodiceDeposito())
                    .containsKey(disponibilitaArticolo.getIdArticolo())) {
                dispPerDeposito.get(disponibilitaArticolo.getCodiceDeposito())
                        .put(disponibilitaArticolo.getIdArticolo(), new ArrayList<DisponibilitaArticolo>());
            }

            List<DisponibilitaArticolo> listDispArticolo = dispPerDeposito
                    .get(disponibilitaArticolo.getCodiceDeposito()).get(disponibilitaArticolo.getIdArticolo());
            DisponibilitaArticolo ultimaDispArticolo = listDispArticolo.size() == 0 ? new DisponibilitaArticolo()
                    : listDispArticolo.get(listDispArticolo.size() - 1);
            disponibilitaArticolo.calcolaIncrementale(ultimaDispArticolo);
            listDispArticolo.add(disponibilitaArticolo);
            dispPerDeposito.get(disponibilitaArticolo.getCodiceDeposito()).put(disponibilitaArticolo.getIdArticolo(),
                    listDispArticolo);
        }
        return dispPerDeposito;
    }

    @Override
    public double calcolaDisponibilitaArticolo(Articolo articolo, Date dataConsegna, Integer idDeposito) {
        List<Articolo> articoli = new ArrayList<Articolo>();
        articoli.add(articolo);
        // Calcolo la giacenza
        DepositoLite deposito = new DepositoLite();
        deposito.setId(idDeposito);
        Giacenza giacenza = calcoloGiacenza(articolo.getArticoloLite(), Calendar.getInstance().getTime(), deposito,
                getAzienda());

        double disponibilita = giacenza.getGiacenza();
        Map<String, Map<Integer, List<DisponibilitaArticolo>>> dispDeposito = calcolaDisponibilita(articoli, idDeposito,
                dataConsegna);
        if (dispDeposito.size() != 0) {
            List<DisponibilitaArticolo> movimentidisponibilita = dispDeposito.entrySet().iterator().next().getValue()
                    .entrySet().iterator().next().getValue();
            // Prendo l'ultima della lista.
            disponibilita = movimentidisponibilita.get(movimentidisponibilita.size() - 1)
                    .getDisponibilita(giacenza.getGiacenza());
        }
        return disponibilita;
    }

    @Override
    public Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, Date data) {
        return calcolaGiacenze(depositoLite, null, data);
    }

    @Override
    public Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, List<Integer> articoli, Date data) {
        Map<ArticoloLite, Double> giacenze = new HashMap<ArticoloLite, Double>();
        List<Giacenza> results = calcolaGiacenzeFlat(depositoLite, articoli, data);
        for (Giacenza giacenza : results) {
            ArticoloLite articoloLite = new ArticoloLite();
            articoloLite.setId(giacenza.getIdArticolo());
            giacenze.put(articoloLite, giacenza.getGiacenza());
        }
        return giacenze;
    }

    @Override
    public List<Giacenza> calcolaGiacenzeFlat(DepositoLite depositoLite, Date data) {
        return calcolaGiacenzeFlat(depositoLite, null, data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Giacenza> calcolaGiacenzeFlat(DepositoLite depositoLite, List<Integer> articoli, Date data) {

        Integer idDeposito = depositoLite == null ? null : depositoLite.getId();

        List<Giacenza> giacenze = null;

        GiacenzaQueryBuilder giacenzaQueryBuilder = new GiacenzaQueryBuilder();

        QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(giacenzaQueryBuilder.getSqlString(articoli, idDeposito, data));
        query.getHibernateQuery().setResultTransformer(Transformers.aliasToBean((Giacenza.class)));
        SQLQuery sqlQuery = (SQLQuery) query.getHibernateQuery();
        sqlQuery.addScalar("idArticolo", Hibernate.INTEGER);
        sqlQuery.addScalar("giacenza", Hibernate.DOUBLE);
        sqlQuery.addScalar("scorta", Hibernate.DOUBLE);

        try {
            giacenze = sqlQuery.list();
        } catch (Exception e) {
            logger.error("-->errore durante il calcolo della giacenza.", e);
            throw new RuntimeException(e);
        }

        return giacenze;
    }

    @Override
    public Giacenza calcoloGiacenza(ArticoloLite articoloLite, Date data, DepositoLite depositoLite,
            String codiceAzienda) {
        logger.debug("--> Enter calcoloGiacenza");

        Integer idDeposito = depositoLite == null ? null : depositoLite.getId();
        Integer idArticolo = articoloLite == null ? null : articoloLite.getId();

        Giacenza giacenza = null;

        GiacenzaQueryBuilder giacenzaQueryBuilder = new GiacenzaQueryBuilder();
        Query query = panjeaDAO.prepareSQLQuery(giacenzaQueryBuilder.getSqlString(idArticolo, idDeposito, data),
                Giacenza.class, giacenzaQueryBuilder.getAlias());

        try {
            giacenza = (Giacenza) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            logger.error("-->errore durante il calcolo della giacenza.", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit calcoloGiacenza");
        return giacenza;
    }

    /**
     *
     * @return azienda corrente
     */
    private String getAzienda() {
        JecPrincipal principal = (JecPrincipal) sessionContext.getCallerPrincipal();
        return principal.getCodiceAzienda();
    }

}