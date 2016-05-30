package it.eurotn.panjea.ordini.manager.documento;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistintaBase;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticoloComponente;
import it.eurotn.panjea.ordini.domain.RigaArticoloDistinta;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineProduzioneStampaManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.AreaProduzioneFullDTOStampa;
import it.eurotn.panjea.ordini.util.RigaOrdineProduzioneDTOStampa;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.AreaOrdineProduzioneStampaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaOrdineProduzioneStampaManager")
public class AreaOrdineProduzioneStampaManagerBean implements AreaOrdineProduzioneStampaManager {

    public static final Logger LOGGER = Logger.getLogger(AreaOrdineProduzioneStampaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DistintaBaseManager distintaBaseManager;

    @EJB
    private RigaOrdineManager rigaOrdineManager;

    @EJB
    private AreaOrdineManager areaOrdineManager;

    private List<AreaOrdine> caricaAreaOrdineCliente(AreaOrdine areaOrdine) {
        StringBuilder sb = new StringBuilder();
        sb.append("select rocli.areaOrdine_id ");
        sb.append("from ordi_righe_ordine roprod ");
        sb.append("inner join ordi_righe_ordine_ordi_righe_ordine roro on roro.ordi_righe_ordine_id=roprod.id ");
        sb.append("inner join ordi_righe_ordine rocli on roro.righeOrdineCollegate_id=rocli.id ");
        sb.append("where roprod.areaOrdine_id=");
        sb.append(areaOrdine.getId());
        sb.append(" group by rocli.areaOrdine_id");

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());

        @SuppressWarnings("unchecked")
        List<Integer> resultList = query.list();
        List<AreaOrdine> areaOrdineCliente = new ArrayList<AreaOrdine>();
        if (resultList != null && !resultList.isEmpty()) {
            for (Integer id : resultList) {
                areaOrdineCliente.add(panjeaDAO.loadLazy(AreaOrdine.class, id));
            }
        }
        return areaOrdineCliente;
    }

    @Override
    public AreaProduzioneFullDTOStampa caricaAreaOrdineProduzioneDTOStampa(AreaOrdineFullDTO areaOrdineFullDTO,
            List<RigaOrdine> righeOrdine) {

        List<AreaOrdine> areeOrdineCliente = caricaAreaOrdineCliente(areaOrdineFullDTO.getAreaOrdine());
        Map<RigaOrdine, List<FaseLavorazioneArticolo>> fasiPerRiga = caricaFasiLavorazioneOrdineStampa(righeOrdine);
        Map<RigaOrdine, List<Componente>> componentiPerRiga = caricaComponentiDistintaBasePerStampa(righeOrdine);
        // idArticolo, qtaOrdintataFornitore
        Map<Integer, Double> ordinatoFornitorePerArticoloOrdine = caricaOrdinatoFornitorePerArticoloOrdinePerStampa(
                areeOrdineCliente);

        Map<Integer, Double> ordinatoFornitorePerArticolo = caricaOrdinatoFornitorePerArticoloPerStampa();

        // idRigaOrdine,qtaImpegnataCliente
        Map<Integer, Double> impegnatoClientePerRiga = caricaImpegnatoClientePerRigaPerStampa(
                areaOrdineFullDTO.getAreaOrdine());
        // idRigaOrdine,dataConsegna
        Map<Integer, Date> ordinatoFornitoreDataConsegna = caricaOrdinatoFornitorePerRigaPerStampa(
                areaOrdineFullDTO.getAreaOrdine());

        AreaProduzioneFullDTOStampa fullDTOStampa = new AreaProduzioneFullDTOStampa(areaOrdineFullDTO.getAreaOrdine(),
                areeOrdineCliente, areaOrdineFullDTO.getAreaRate(), righeOrdine, fasiPerRiga, componentiPerRiga,
                impegnatoClientePerRiga, ordinatoFornitorePerArticoloOrdine, ordinatoFornitoreDataConsegna,
                ordinatoFornitorePerArticolo);

        List<RigaOrdineProduzioneDTOStampa> carichiProduzione = caricaCarichiProduzionePerStampa(
                areaOrdineFullDTO.getAreaOrdine());
        fullDTOStampa.setCarichiProduzione(carichiProduzione);

        List<RigaOrdineProduzioneDTOStampa> righeOrdineClienteStampa = caricaRigheOrdineClientePerProduzioneStampa(
                fullDTOStampa);
        fullDTOStampa.setRigheOrdineCliente(righeOrdineClienteStampa);
        return fullDTOStampa;
    }

    private List<RigaOrdineProduzioneDTOStampa> caricaCarichiProduzionePerStampa(AreaOrdine areaOrdine) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("d.dataDocumento as dataEvasione, ");
        sb.append("d.codice as codiceDocumentoEvasione ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("inner join maga_articoli art on art.id=ro.articolo_id ");
        sb.append("left join maga_righe_magazzino rm on ro.id=rm.rigaOrdineCollegata_Id ");
        sb.append("left join maga_area_magazzino am on am.id=rm.areaMagazzino_id ");
        sb.append("left join docu_documenti d on d.id=am.documento_id ");
        sb.append("where ro.areaOrdine_id= ");
        sb.append(areaOrdine.getId());
        sb.append(" and (ro.TIPO_RIGA='D' or ro.TIPO_RIGA='A')");
        sb.append(" group by d.id");

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean((RigaOrdineProduzioneDTOStampa.class)));
        query.addScalar("dataEvasione");
        query.addScalar("codiceDocumentoEvasione");

        @SuppressWarnings("unchecked")
        List<RigaOrdineProduzioneDTOStampa> resultList = query.list();
        return resultList;
    }

    @SuppressWarnings("unchecked")
    private Componente caricaComponenteByConfigurazioneDistinta(Integer idArticolo, Integer idConfigurazioneArticolo) {
        Query query = panjeaDAO.prepareNamedQuery("Componente.caricaByConfigurazioneDistinta");
        query.setParameter("idArticolo", idArticolo);
        query.setParameter("idConfigurazioneDistinta", idConfigurazioneArticolo);
        List<Componente> listResult = new ArrayList<Componente>();
        try {
            listResult = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento dei componenti della configurazione distinta", e);
            throw new GenericException("Errore durante il caricamento dei componenti della configurazione distinta", e);
        }

        Componente componente = null;
        if (!listResult.isEmpty()) {
            componente = listResult.get(0);
        }

        return componente;
    }

    private Map<RigaOrdine, List<Componente>> caricaComponentiDistintaBasePerStampa(List<RigaOrdine> righeOrdine) {
        Map<RigaOrdine, List<Componente>> componentiDistintaPerRiga = new HashMap<>();
        for (RigaOrdine rigaOrdine : righeOrdine) {
            if (rigaOrdine instanceof RigaArticoloDistinta) {
                Articolo articolo = ((RigaArticolo) rigaOrdine).getArticolo().creaProxyArticolo();
                Set<Componente> componenti = new HashSet<>();
                try {
                    componenti = distintaBaseManager.caricaComponenti(articolo);
                } catch (DistintaCircolareException e) {
                    throw new GenericException(e);
                }
                componentiDistintaPerRiga.put(rigaOrdine, new ArrayList<Componente>(componenti));
            }
        }
        return componentiDistintaPerRiga;
    }

    private Map<RigaOrdine, List<FaseLavorazioneArticolo>> caricaFasiLavorazioneOrdineStampa(
            List<RigaOrdine> righeOrdine) {
        Map<RigaOrdine, List<FaseLavorazioneArticolo>> fasiLavorazionePerRiga = new HashMap<>();

        for (RigaOrdine rigaOrdine : righeOrdine) {
            if (rigaOrdine instanceof RigaArticoloDistinta) {
                RigaArticoloDistinta rigaDistinta = (RigaArticoloDistinta) rigaOrdine;
                ConfigurazioneDistinta configurazioneDistinta = rigaDistinta.getConfigurazioneDistinta();
                if (configurazioneDistinta == null) {
                    configurazioneDistinta = new ConfigurazioneDistintaBase(
                            rigaDistinta.getArticolo().creaProxyArticolo());
                }
                Componente componente = null;
                if (!(configurazioneDistinta instanceof ConfigurazioneDistintaBase)) {
                    componente = caricaComponenteByConfigurazioneDistinta(rigaDistinta.getArticolo().getId(),
                            configurazioneDistinta.getId());
                }
                Set<FaseLavorazioneArticolo> fasiLavorazione = distintaBaseManager
                        .caricaFasiLavorazione(configurazioneDistinta, componente);
                fasiLavorazionePerRiga.put(rigaOrdine, new ArrayList<FaseLavorazioneArticolo>(fasiLavorazione));
            } else if (rigaOrdine instanceof RigaArticoloComponente) {
                RigaArticoloComponente rigaComponente = (RigaArticoloComponente) rigaOrdine;
                ConfigurazioneDistinta configurazioneDistinta = rigaComponente.getRigaDistintaCollegata()
                        .getConfigurazioneDistinta();
                if (configurazioneDistinta == null) {
                    configurazioneDistinta = new ConfigurazioneDistintaBase(
                            rigaComponente.getArticolo().creaProxyArticolo());
                }
                Componente componente = null;
                if (!(configurazioneDistinta instanceof ConfigurazioneDistintaBase)) {
                    componente = caricaComponenteByConfigurazioneDistinta(rigaComponente.getArticolo().getId(),
                            configurazioneDistinta.getId());
                }
                Set<FaseLavorazioneArticolo> fasiLavorazione = distintaBaseManager
                        .caricaFasiLavorazione(configurazioneDistinta, componente);
                fasiLavorazionePerRiga.put(rigaOrdine, new ArrayList<FaseLavorazioneArticolo>(fasiLavorazione));
            }
        }
        return fasiLavorazionePerRiga;
    }

    private Map<Integer, Double> caricaImpegnatoClientePerRigaPerStampa(AreaOrdine areaOrdineProduzione) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("ro.articolo_id, ");
        sb.append("sum(coalesce(ro.qtaR,ro.qta)-coalesce(rm.qtaMagazzino,0)) as qtaR ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("inner join ordi_area_ordine ao on ao.id = ro.areaOrdine_id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id = ao.tipoAreaOrdine_id ");
        sb.append("left join maga_righe_magazzino rm on ro.id=rm.rigaOrdineCollegata_Id ");
        sb.append("where ((ro.TIPO_RIGA='C' and ro.rigaPadre_id is null) or (ro.TIPO_RIGA='D')) ");
        sb.append("and tao.ordineProduzione = true ");
        sb.append("and rm.id is null ");
        sb.append(" group by ro.articolo_id ");

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());
        Map<Integer, Double> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.list();
        for (Object[] res : resultList) {
            map.put((Integer) res[0], (Double) res[1]);
        }
        return map;
    }

    private Map<Integer, Double> caricaOrdinatoFornitorePerArticoloOrdinePerStampa(List<AreaOrdine> areeOrdineCliente) {
        Map<Integer, Double> map = new HashMap<>();

        String idAreeCliente = "";
        if (!CollectionUtils.isEmpty(areeOrdineCliente)) {
            for (AreaOrdine areaOrdine : areeOrdineCliente) {
                idAreeCliente = idAreeCliente.concat(areaOrdine.getId().toString()).concat(",");
            }
            idAreeCliente = StringUtils.chop(idAreeCliente);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("rofor.articolo_id, ");
        sb.append(
                "sum(CASE WHEN rofor.qta-coalesce(rm.qtaMagazzino,0) >= 0 THEN rofor.qta-coalesce(rm.qtaMagazzino,0) ELSE 0 END) ");
        sb.append("from ordi_righe_ordine rocli ");
        sb.append("left join ordi_righe_ordine_ordi_righe_ordine roro on roro.righeOrdineCollegate_id=rocli.id ");
        sb.append("left join ordi_righe_ordine rofor on roro.ordi_righe_ordine_id=rofor.id ");
        sb.append("left join ordi_area_ordine aoc on aoc.id=rofor.areaOrdine_id ");
        sb.append("left join docu_documenti docc on docc.id=aoc.documento_id ");
        sb.append("left join docu_tipi_documento tdc on tdc.id=docc.tipo_documento_id ");
        sb.append("left join maga_righe_magazzino rm on rofor.id=rm.rigaOrdineCollegata_Id ");
        sb.append("where tdc.tipoEntita=1 ");
        if (!idAreeCliente.isEmpty()) {
            sb.append(" and rocli.areaOrdine_id in (");
            sb.append(idAreeCliente);
            sb.append(") ");
        }
        sb.append("group by rocli.id,rofor.articolo_id ");

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.list();
        for (Object[] res : resultList) {
            map.put((Integer) res[0], (Double) res[1]);
        }
        return map;
    }

    private Map<Integer, Double> caricaOrdinatoFornitorePerArticoloPerStampa() {
        Map<Integer, Double> map = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("select idArticolo, SUM(qta) ");
        sb.append("from( select ");
        sb.append("rofor.id as idRiga, ");
        sb.append("rofor.articolo_id as idArticolo, ");
        sb.append(
                "(CASE WHEN rofor.qta-coalesce(sum(rm.qtaMagazzino),0) >= 0 THEN rofor.qta-coalesce(sum(rm.qtaMagazzino),0) ELSE 0 END) as qta ");
        sb.append("from ordi_righe_ordine rofor ");
        sb.append("left join ordi_area_ordine aoc on aoc.id=rofor.areaOrdine_id ");
        sb.append("left join docu_documenti docc on docc.id=aoc.documento_id ");
        sb.append("left join docu_tipi_documento tdc on tdc.id=docc.tipo_documento_id ");
        sb.append("left join maga_righe_magazzino rm on rofor.id=rm.rigaOrdineCollegata_Id ");
        sb.append("where tdc.tipoEntita=1 and rofor.evasioneForzata = FALSE ");
        sb.append("group by rofor.id) as queryQta ");
        sb.append("group by idArticolo ");

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.list();
        for (Object[] res : resultList) {
            map.put((Integer) res[0], (Double) res[1]);
        }
        return map;
    }

    private Map<Integer, Date> caricaOrdinatoFornitorePerRigaPerStampa(AreaOrdine areaOrdine) {
        Map<Integer, Date> map = new HashMap<>();
        if (areaOrdine == null) {
            return map;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("ropc.id, ");
        sb.append("max(rofor.dataConsegna) ");
        sb.append("from ordi_righe_ordine rocli ");
        sb.append("left join ordi_righe_ordine_ordi_righe_ordine roro on roro.righeOrdineCollegate_id=rocli.id ");
        sb.append("left join ordi_righe_ordine rofor on roro.ordi_righe_ordine_id=rofor.id ");
        sb.append("left join ordi_area_ordine aoc on aoc.id=rofor.areaOrdine_id ");
        sb.append("left join docu_documenti docc on docc.id=aoc.documento_id ");
        sb.append("left join docu_tipi_documento tdc on tdc.id=docc.tipo_documento_id ");
        sb.append(
                "left join ordi_righe_ordine_ordi_righe_ordine righeordin1_ on rocli.id =righeordin1_.righeOrdineCollegate_id ");
        sb.append("left join ordi_righe_ordine rop on rop.id=righeordin1_.ordi_righe_ordine_id ");
        sb.append("left join ordi_righe_ordine ropc on ropc.rigaDistintaCollegata_id=rop.id ");
        sb.append("where rop.areaOrdine_id=");
        sb.append(areaOrdine.getId());
        sb.append(" and tdc.tipoEntita=1 and rofor.articolo_id = ropc.articolo_id ");
        sb.append("group by rofor.articolo_id ");

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.list();
        for (Object[] res : resultList) {
            map.put((Integer) res[0], (Date) res[1]);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private List<RigaOrdineProduzioneDTOStampa> caricaRigheOrdineClientePerProduzioneStampa(
            AreaProduzioneFullDTOStampa fullDTOStampa) {

        List<RigaOrdineProduzioneDTOStampa> resultList = new ArrayList<>();
        List<AreaOrdine> areeOrdineCliente = fullDTOStampa.getAreaOrdineCliente();
        if (areeOrdineCliente == null || areeOrdineCliente.isEmpty()) {
            return resultList;
        }

        String idAreeCliente = "";
        for (AreaOrdine areaCliente : areeOrdineCliente) {
            idAreeCliente = idAreeCliente.concat(areaCliente.getId().toString()).concat(",");
        }
        idAreeCliente = StringUtils.chop(idAreeCliente);

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("art.codice as codice, ");
        sb.append("ro.descrizione as descrizione, ");
        sb.append("ro.unitaMisura as unitaMisura, ");
        sb.append("ro.qta as qta, ");
        sb.append("rm.qta as qtaEvasione, ");
        sb.append("ro.dataConsegna as dataConsegna, ");
        sb.append("d.dataDocumento as dataEvasione, ");
        sb.append("d.codice as codiceDocumentoEvasione, ");
        sb.append("ro.areaOrdine_id as idAreaOrdineCliente ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("inner join maga_articoli art on art.id=ro.articolo_id ");
        sb.append("left join maga_righe_magazzino rm on ro.id=rm.rigaOrdineCollegata_Id ");
        sb.append("left join maga_area_magazzino am on am.id=rm.areaMagazzino_id ");
        sb.append("left join docu_documenti d on d.id=am.documento_id ");
        sb.append("where ro.areaOrdine_id in (");
        sb.append(idAreeCliente);
        sb.append(") and art.id in (");
        sb.append("select articolo_id from ordi_righe_ordine where areaOrdine_id=");
        sb.append(fullDTOStampa.getAreaOrdine().getId());
        sb.append(")");

        SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean((RigaOrdineProduzioneDTOStampa.class)));
        query.addScalar("codice");
        query.addScalar("descrizione");
        query.addScalar("unitaMisura");
        query.addScalar("qta");
        query.addScalar("qtaEvasione");
        query.addScalar("dataConsegna");
        query.addScalar("dataEvasione");
        query.addScalar("codiceDocumentoEvasione");
        query.addScalar("idAreaOrdineCliente");

        resultList = query.list();
        return resultList;
    }
}