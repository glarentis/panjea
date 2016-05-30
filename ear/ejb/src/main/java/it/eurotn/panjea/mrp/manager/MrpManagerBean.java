package it.eurotn.panjea.mrp.manager;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.ConflittoMrp;
import it.eurotn.panjea.mrp.domain.OrdiniClienteCalcolo;
import it.eurotn.panjea.mrp.domain.OrdiniFornitoreCalcolo;
import it.eurotn.panjea.mrp.domain.OrdiniProduzioneCalcolo;
import it.eurotn.panjea.mrp.domain.RisultatoMRP;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.MrpManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.MrpManager")
@PermitAll
public class MrpManagerBean implements MrpManager {

    private static Logger logger = Logger.getLogger(MrpManagerBean.class);

    @Resource
    private EJBContext context;

    @EJB
    private GiacenzaManager giacenzaManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DepositiManager depositiManager;

    @Override
    public List<Giacenza> calcolaGiacenzeFlat(int idDeposito, Date data) {
        DepositoLite deposito = new DepositoLite();
        deposito.setId(idDeposito);
        return giacenzaManager.calcolaGiacenzeFlat(deposito, data);
    }

    @Override
    public void cancellaRigheRisultati(List<RisultatoMrpFlat> selectedObjects) {
        logger.debug("--> Enter cancellaRigheRisultati");
        Query queryUpdate = panjeaDAO.prepareQuery("update RisultatoMRP r set r.distinta=null where r.distinta=:riga");
        for (RisultatoMrpFlat risultatoMrpFlat : selectedObjects) {
            RisultatoMRP risultati = panjeaDAO.loadLazy(RisultatoMRP.class, risultatoMrpFlat.getId());
            try {
                queryUpdate.setParameter("riga", risultati);
                panjeaDAO.executeQuery(queryUpdate);
                panjeaDAO.delete(risultati);
            } catch (DAOException e) {
                logger.error("-->errore nel cancellare il risultato mrp " + risultati.getId(), e);
                throw new RuntimeException("-->errore nel cancellare il risultato mrp " + risultati.getId(), e);
            }
        }
        logger.debug("--> Exit cancellaRigheRisultati");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArticoloAnagrafica> caricaArticoloAnagrafica() {
        StringBuilder sb = new StringBuilder(500);
        // la prima query prende solo articoli con fornitore abituale
        sb.append("select ");
        sb.append(
                "art.id as idArticolo,ent.id as idFornitore,art.numeroDecimaliQta as numDecimali,art.tipoAreaOrdine_id as idTipoDocumento,artent.leadTime as leadTime,artent.ggSicurezza ,artent.qtaMinimaOrdinabile as minOrdinabile,artent.lottoRiordino as lottoRiordino, art.distinta as distinta ");
        sb.append("from maga_articoli art ");
        sb.append("inner join maga_codici_articolo_entita artent on art.id=artent.articolo_id ");
        sb.append("inner join anag_entita ent on ent.id=artent.entita_id ");
        sb.append("where artent.entitaPrincipale=true and ent.TIPO_ANAGRAFICA='F' and art.mrp = true ");
        sb.append("union ");
        sb.append(
                "select art.id as idArticolo,null as idFornitore,art.numeroDecimaliQta as numDecimali,art.tipoAreaOrdine_id as idTipoDocumento,art.leadTime as leadTime,0 as ggSicurezza,0 as minOrdinabile,0 as lottoRiordino, art.distinta as distinta ");
        sb.append("from maga_articoli art ");
        sb.append("left join maga_codici_articolo_entita artent on art.id=artent.articolo_id ");
        sb.append("left join anag_entita ent on ent.id=artent.entita_id and ent.TIPO_ANAGRAFICA='F' ");
        sb.append("where art.mrp = true ");
        sb.append("group by art.id having(sum(case when artent.entitaPrincipale = true then 1 else 0 end) = 0) ");

        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString(), ArticoloAnagrafica.class);

        query.addScalar("idArticolo", Hibernate.INTEGER);
        query.addScalar("idFornitore", Hibernate.INTEGER);
        query.addScalar("numDecimali", Hibernate.INTEGER);
        query.addScalar("idTipoDocumento", Hibernate.INTEGER);
        query.addScalar("leadTime", Hibernate.INTEGER);
        query.addScalar("ggSicurezza", Hibernate.INTEGER);
        query.addScalar("minOrdinabile", Hibernate.DOUBLE);
        return query.list();
    }

    @Override
    public List<Object[]> caricaBomBase() {
        StringBuilder sb = new StringBuilder(500);
        sb.append("select c.configurazioneDistinta_id as idConfigurazioneDistinta, ");
        sb.append("c.distinta_id as idDistinta, ");
        sb.append("c.articolo_id  as idArticolo, ");
        sb.append("CASE WHEN c.formula<>'' THEN c.formula ELSE cat.formulaPredefinitaComponente END  as formula, ");
        sb.append("c.id as idComponente, ");
        sb.append("c.componenteConfigurazione_id as idConfigurazioneComponente, ");
        sb.append("GROUP_CONCAT(COALESCE(ta.codice,0)) as codiciAttributi, ");
        sb.append("GROUP_CONCAT(COALESCE(aa.valore,0)) as valoriAttributi, ");
        sb.append(
                "ifnull((select sum(fasi.qtaAttrezzaggio) from maga_fasi_lavorazione_articolo fasi where fasi.articolo_id=c.distinta_id),0) as qtaAttrezzaggioDistinta, ");
        sb.append(
                "ifnull((select sum(fasi.qtaAttrezzaggio) from maga_fasi_lavorazione_articolo fasi where fasi.articolo_id=c.articolo_id),0)+c.qtaAttrezzaggio as qtaAttrezzaggioArticolo ");
        sb.append("from maga_componente c ");
        sb.append("inner join maga_articoli a on c.articolo_id=a.id ");
        sb.append("inner join maga_categorie cat on cat.id=a.categoria_id ");
        sb.append("left join maga_attributi_articoli aa on aa.articolo_id=a.id ");
        sb.append("left join maga_tipo_attributo ta on ta.id=aa.tipoAttributo_id ");
        sb.append("where c.configurazioneDistinta_id is null ");
        sb.append("group by c.distinta_id,c.articolo_id ");
        sb.append("order by c.distinta_id ");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());

        query.addScalar("idConfigurazioneDistinta", Hibernate.INTEGER);
        query.addScalar("idDistinta", Hibernate.INTEGER);
        query.addScalar("idArticolo", Hibernate.INTEGER);
        query.addScalar("formula", Hibernate.STRING);
        query.addScalar("idComponente", Hibernate.INTEGER);
        query.addScalar("idConfigurazioneComponente", Hibernate.INTEGER);
        query.addScalar("codiciAttributi", Hibernate.STRING);
        query.addScalar("valoriAttributi", Hibernate.STRING);
        query.addScalar("qtaAttrezzaggioDistinta", Hibernate.DOUBLE);
        query.addScalar("qtaAttrezzaggioArticolo", Hibernate.DOUBLE);

        @SuppressWarnings("unchecked")
        List<Object[]> result = query.list();
        return result;
    }

    @Override
    public List<Object[]> caricaBomConfigurazioni(Set<Integer> configurazioniUtilizzate) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("select ");
        sb.append("c.configurazioneDistinta_id as idConfigurazioneDistinta, ");
        sb.append("COALESCE(c1.articolo_id,c.distinta_id) as idDistinta, ");
        sb.append("c.articolo_id as idArticolo, ");
        sb.append("CASE WHEN c.formula<>'' THEN c.formula ELSE cat.formulaPredefinitaComponente END as formula, ");
        sb.append("c.id as idComponente, ");
        sb.append("c.componenteConfigurazione_id as idConfigurazioneComponente, ");
        sb.append("GROUP_CONCAT(COALESCE(ta.codice,0)) as codiciAttributi, ");
        sb.append("GROUP_CONCAT(COALESCE(aa.valore,0)) as valoriAttributi, ");
        sb.append(
                "ifnull((select sum(fasi.qtaAttrezzaggio) from maga_fasi_lavorazione_articolo fasi where fasi.configurazioneDistinta_id=c.configurazioneDistinta_id and fasi.articolo_id is null and fasi.componente_id is null),0) as qtaAttrezzaggioDistinta, ");
        sb.append(
                "ifnull((select sum(fasi.qtaAttrezzaggio) from maga_fasi_lavorazione_articolo fasi where fasi.configurazioneDistinta_id=c.configurazioneDistinta_id and fasi.componente_id=c.id),0)+c.qtaAttrezzaggio as qtaAttrezzaggioArticolo ");
        sb.append("from maga_componente c ");
        sb.append("inner join maga_articoli a on c.articolo_id=a.id ");
        sb.append("inner join maga_categorie cat on cat.id=a.categoria_id ");
        sb.append("left join maga_componente c1 on c.componenteConfigurazione_id=c1.id ");
        sb.append("left join maga_attributi_articoli aa on aa.articolo_id=a.id ");
        sb.append("left join maga_tipo_attributo ta on ta.id=aa.tipoAttributo_id ");
        sb.append("where c.configurazioneDistinta_id in (");
        sb.append(StringUtils.join(configurazioniUtilizzate.iterator(), ","));
        sb.append(") ");
        sb.append("group by c.distinta_id,c.articolo_id ");
        sb.append("order by c.configurazioneDistinta_id,c.componenteConfigurazione_id,c.id");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.addScalar("idConfigurazioneDistinta", Hibernate.INTEGER);
        query.addScalar("idDistinta", Hibernate.INTEGER);
        query.addScalar("idArticolo", Hibernate.INTEGER);
        query.addScalar("formula", Hibernate.STRING);
        query.addScalar("idComponente", Hibernate.INTEGER);
        query.addScalar("idConfigurazioneComponente", Hibernate.INTEGER);
        query.addScalar("codiciAttributi", Hibernate.STRING);
        query.addScalar("valoriAttributi", Hibernate.STRING);
        query.addScalar("qtaAttrezzaggioDistinta", Hibernate.DOUBLE);
        query.addScalar("qtaAttrezzaggioArticolo", Hibernate.DOUBLE);
        @SuppressWarnings("unchecked")
        List<Object[]> result = query.list();
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrdiniClienteCalcolo> caricaOrdiniClienteCalcolo(Date dataInizio, Date dataFine) {
        logger.debug("--> Enter caricaOrdiniClienteCalcolo");
        final String endDateString = new SimpleDateFormat("yyyyMMdd").format(dataFine);
        final String startDateString = new SimpleDateFormat("yyyyMMdd").format(dataInizio);
        StringBuilder sb = new StringBuilder(1500);
        sb.append("select ");
        sb.append("rop.id as idRigaOrdineProduzione, ");
        sb.append("rop.articolo_id as idArticolo, ");
        sb.append("rop.qta-coalesce(tabEvasione.qtaEvasa,0) as qta, ");
        sb.append("ao.depositoOrigine_id as idDeposito, ");
        sb.append("rop.dataProduzione as dataProduzione, ");
        sb.append("rop.dataConsegna as dataConsegna ");
        sb.append("from ordi_righe_ordine rop ");
        sb.append("inner join ordi_area_ordine ao on ao.id=rop.areaOrdine_id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=ao.tipoAreaOrdine_id ");
        sb.append("left join ( ");
        sb.append("   select rigamaga.rigaOrdineCollegata_Id as roe_id ,coalesce(sum(rigamaga.qta),0) as qtaEvasa ");
        sb.append("   from maga_righe_magazzino as rigamaga    group by rigaOrdineCollegata_Id ");
        sb.append(")  as tabEvasione on tabEvasione.roe_id=rop.id ");
        sb.append("where ");
        sb.append("rop.TIPO_RIGA in ('A',  'C') ");
        sb.append("and ao.statoAreaOrdine>0 ");
        sb.append("and (coalesce(tabEvasione.qtaEvasa,0) < rop.qta) and rop.evasioneForzata=false ");
        sb.append("and tao.ordineProduzione=true ");
        sb.append("and ao.dataRegistrazione between ").append(startDateString).append(" and ").append(endDateString);
        sb.append(" order by null ");
        List<String> alias = new ArrayList<>();
        alias.add("idRigaOrdineProduzione");
        alias.add("idArticolo");
        alias.add("qta");
        alias.add("idDeposito");
        alias.add("idArticolo");
        alias.add("dataProduzione");
        alias.add("dataConsegna");
        Query query = panjeaDAO.prepareQuery(sb.toString(), OrdiniProduzioneCalcolo.class, alias);
        List<OrdiniClienteCalcolo> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaOrdiniClienteCalcolo");
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrdiniFornitoreCalcolo> caricaOrdiniFornitoreCalcolo(Date dataInizio, Date dataFine) {
        logger.debug("--> Enter caricaOrdiniFornitoreCalcolo");
        Date dataInizioFiltro = DateUtils.addYears(dataInizio, -1);
        final String inizioFiltroDateString = new SimpleDateFormat("yyyyMMdd").format(dataInizioFiltro);
        final String endDateString = new SimpleDateFormat("yyyyMMdd").format(dataFine);
        String startDateString = new SimpleDateFormat("yyyyMMdd").format(dataInizio);
        StringBuilder sb = new StringBuilder(2500);
        sb.append("select ");
        sb.append("rof.id as idRigaOrdineFornitore, ");
        sb.append("rof.articolo_id as idArticolo, ");
        sb.append("ao.depositoOrigine_id as idDeposito, ");
        sb.append("IF(rof.qta-coalesce(tabEvasione.qtaEvasa,0)<0,0,rof.qta-coalesce(tabEvasione.qtaEvasa,0)) as qta, ");
        sb.append("rof.dataConsegna, ");
        sb.append("DATEDIFF(rof.dataConsegna,'");
        sb.append(startDateString);
        sb.append("') as diffData, ");
        sb.append(
                "(select CONVERT(concat(group_concat(aud.REV)) USING 'utf8') from ordi_righe_ordine_ordi_righe_ordine rcoll inner join ordi_righe_ordine roc on roc.id=rcoll.righeOrdineCollegate_id inner join ordi_area_ordine aoc on aoc.id=roc.areaOrdine_id inner join ordi_tipi_area_ordine taoc on taoc.id=aoc.tipoAreaOrdine_id inner join docu_documenti docc on docc.id=aoc.documento_id inner join docu_tipi_documento tdc on tdc.id=docc.tipo_documento_id and tdc.tipoEntita=0 inner join ordi_righe_ordine_ordi_righe_ordine rcollp on rcollp.righeOrdineCollegate_id=roc.id inner join ordi_righe_ordine rop on rop.id=rcollp.ordi_righe_ordine_id inner join ordi_area_ordine aop on aop.id=rop.areaOrdine_id inner join ordi_tipi_area_ordine taop on taop.id=aop.tipoAreaOrdine_id and taop.ordineProduzione=true inner join ordi_righe_ordine rocomp on rocomp.rigaDistintaCollegata_id=rop.id left join ordi_righe_ordine_ordi_righe_ordine_aud aud on aud.ordi_righe_ordine_id=rcollp.ordi_righe_ordine_id and aud.righeOrdineCollegate_id=rcollp.righeOrdineCollegate_id where rcoll.ordi_righe_ordine_id=rof.id and rocomp.articolo_id=rof.articolo_id ) as ordiniProduzioneComponentiREV, ");
        sb.append(
                "(select CONVERT(concat(group_concat(rocomp.id)) USING 'utf8') from ordi_righe_ordine_ordi_righe_ordine rcoll inner join ordi_righe_ordine roc on roc.id=rcoll.righeOrdineCollegate_id inner join ordi_area_ordine aoc on aoc.id=roc.areaOrdine_id inner join ordi_tipi_area_ordine taoc on taoc.id=aoc.tipoAreaOrdine_id inner join docu_documenti docc on docc.id=aoc.documento_id inner join docu_tipi_documento tdc on tdc.id=docc.tipo_documento_id and tdc.tipoEntita=0 inner join ordi_righe_ordine_ordi_righe_ordine rcollp on rcollp.righeOrdineCollegate_id=roc.id inner join ordi_righe_ordine rop on rop.id=rcollp.ordi_righe_ordine_id inner join ordi_area_ordine aop on aop.id=rop.areaOrdine_id inner join ordi_tipi_area_ordine taop on taop.id=aop.tipoAreaOrdine_id and taop.ordineProduzione=true inner join ordi_righe_ordine rocomp on rocomp.rigaDistintaCollegata_id=rop.id left join ordi_righe_ordine_ordi_righe_ordine_aud aud on aud.ordi_righe_ordine_id=rcollp.ordi_righe_ordine_id and aud.righeOrdineCollegate_id=rcollp.righeOrdineCollegate_id where rcoll.ordi_righe_ordine_id=rof.id and rocomp.articolo_id=rof.articolo_id order by aud.REV) as ordiniProduzioneComponenti, ");
        sb.append(
                "(select CONVERT(concat(group_concat(aud.REV)) USING 'utf8') from ordi_righe_ordine_ordi_righe_ordine rcoll left join ordi_righe_ordine rop on rop.id=rcoll.righeOrdineCollegate_id left join ordi_area_ordine aop on aop.id=rop.areaOrdine_id left join ordi_tipi_area_ordine taop on taop.id=aop.tipoAreaOrdine_id left join docu_documenti docp on docp.id=aop.documento_id left join docu_tipi_documento tdp on tdp.id=docp.tipo_documento_id left join ordi_righe_ordine_ordi_righe_ordine_aud aud on aud.ordi_righe_ordine_id=rcoll.ordi_righe_ordine_id and aud.righeOrdineCollegate_id=rcoll.righeOrdineCollegate_id where rcoll.ordi_righe_ordine_id=rof.id and taop.ordineProduzione=true ) as ordiniProduzioneDistinteREV, ");
        sb.append(
                "(select CONVERT(concat(group_concat(rop.id)) USING 'utf8') from ordi_righe_ordine_ordi_righe_ordine rcoll left join ordi_righe_ordine rop on rop.id=rcoll.righeOrdineCollegate_id left join ordi_area_ordine aop on aop.id=rop.areaOrdine_id left join ordi_tipi_area_ordine taop on taop.id=aop.tipoAreaOrdine_id left join docu_documenti docp on docp.id=aop.documento_id left join docu_tipi_documento tdp on tdp.id=docp.tipo_documento_id left join ordi_righe_ordine_ordi_righe_ordine_aud aud on aud.ordi_righe_ordine_id=rcoll.ordi_righe_ordine_id and aud.righeOrdineCollegate_id=rcoll.righeOrdineCollegate_id where rcoll.ordi_righe_ordine_id=rof.id and taop.ordineProduzione=true order by aud.REV) as ordiniProduzioneDistinte, ");
        sb.append(
                "( select CONVERT(concat(group_concat(aud.REV)) USING 'utf8') from ordi_righe_ordine_ordi_righe_ordine rcoll left join ordi_righe_ordine rop on rop.id=rcoll.righeOrdineCollegate_id left join ordi_area_ordine aop on aop.id=rop.areaOrdine_id left join ordi_tipi_area_ordine taop on taop.id=aop.tipoAreaOrdine_id left join docu_documenti docp on docp.id=aop.documento_id left join docu_tipi_documento tdp on tdp.id=docp.tipo_documento_id left join ordi_righe_ordine_ordi_righe_ordine_aud aud on aud.ordi_righe_ordine_id=rcoll.ordi_righe_ordine_id and aud.righeOrdineCollegate_id=rcoll.righeOrdineCollegate_id where rcoll.ordi_righe_ordine_id=rof.id and tdp.tipoEntita=0 and rop.articolo_id=rof.articolo_id order by aud.REV ) as ordiniClientiREV, ");
        sb.append("( ");
        sb.append("   select ");
        sb.append("   CONVERT(concat(group_concat(rop.id)) USING 'utf8') ");
        sb.append("   from ordi_righe_ordine_ordi_righe_ordine rcoll ");
        sb.append("   left join ordi_righe_ordine rop on rop.id=rcoll.righeOrdineCollegate_id ");
        sb.append("   left join ordi_area_ordine aop on aop.id=rop.areaOrdine_id ");
        sb.append("   left join ordi_tipi_area_ordine taop on taop.id=aop.tipoAreaOrdine_id ");
        sb.append("   left join docu_documenti docp on docp.id=aop.documento_id ");
        sb.append("   left join docu_tipi_documento tdp on tdp.id=docp.tipo_documento_id ");
        sb.append(
                "   left join ordi_righe_ordine_ordi_righe_ordine_aud aud on aud.ordi_righe_ordine_id=rcoll.ordi_righe_ordine_id ");
        sb.append("   and aud.righeOrdineCollegate_id=rcoll.righeOrdineCollegate_id ");
        sb.append("   where rcoll.ordi_righe_ordine_id=rof.id ");
        sb.append("   and tdp.tipoEntita=0 ");
        sb.append("   and rop.articolo_id=rof.articolo_id ");
        sb.append("   order by aud.REV ");
        sb.append(") ");
        sb.append("as ordiniClienti ");
        sb.append("from ordi_righe_ordine rof ");
        sb.append("inner join ordi_area_ordine ao on ao.id=rof.areaOrdine_id ");
        sb.append("inner join docu_documenti docf on docf.id=ao.documento_id ");
        sb.append("inner join docu_tipi_documento tdf on tdf.id=docf.tipo_documento_id ");
        sb.append("left join ");
        sb.append("( ");
        sb.append("   select ");
        sb.append("   rigamaga.rigaOrdineCollegata_Id as roe_id , ");
        sb.append("   coalesce(sum(rigamaga.qta),0) as qtaEvasa ");
        sb.append("   from maga_righe_magazzino as rigamaga ");
        sb.append("   group by rigaOrdineCollegata_Id ");
        sb.append(") ");
        sb.append("as tabEvasione on tabEvasione.roe_id=rof.id ");
        sb.append("where  ");
        sb.append("rof.evasioneForzata=false ");
        sb.append("and ao.statoAreaOrdine>0 ");
        sb.append("and (rof.TIPO_RIGA = 'D' or (rof.TIPO_RIGA = 'A' and tdf.tipoEntita=1))");
        sb.append("and rof.dataConsegna  between '").append(inizioFiltroDateString).append("' and '")
                .append(endDateString).append("' ");
        sb.append("order by null ");

        List<OrdiniFornitoreCalcolo> result = null;
        try {
            SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
            query.addScalar("idDeposito");
            query.addScalar("idRigaOrdineFornitore");
            query.addScalar("ordiniClienti");
            query.addScalar("idArticolo");
            query.addScalar("qta");
            query.addScalar("dataConsegna");
            query.addScalar("diffData", Hibernate.INTEGER);
            query.addScalar("ordiniProduzioneComponentiREV", Hibernate.STRING);
            query.addScalar("ordiniProduzioneComponenti", Hibernate.STRING);
            query.addScalar("ordiniProduzioneDistinteREV", Hibernate.STRING);
            query.addScalar("ordiniProduzioneDistinte", Hibernate.STRING);
            query.addScalar("ordiniClientiREV", Hibernate.STRING);
            query.addScalar("ordiniClienti", Hibernate.STRING);
            query.setResultTransformer(Transformers.aliasToBean(OrdiniFornitoreCalcolo.class));
            result = query.list();
        } catch (Exception e) {
            logger.error("-->errore nel calcolare gli ordini forntirore/produzione calcolo", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaOrdiniFornitoreCalcolo con n risultati " + result.size());
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RisultatoMRPArticoloBucket> caricaRigheClienteDaEvadere(Date dataInizio, Date dataFine) {
        final String endDateString = new SimpleDateFormat("yyyyMMdd").format(dataFine);
        final String startDateString = new SimpleDateFormat("yyyyMMdd").format(dataInizio);
        StringBuilder sb = new StringBuilder(1500);
        sb.append("select 1 as tipoRiga, ");
        sb.append("ro.areaOrdine_id as idOrdine, ");
        sb.append("ro.id as idRigaOrdine, ");
        sb.append("ro.articolo_id as idArticolo, ");
        sb.append("ro.qtaMagazzino as qtaR, ");
        sb.append("rod.qtaMagazzino-sum(coalesce(rm.qtaMagazzino,0)) as qtaDistintaEvasa, ");
        sb.append("ro.dataConsegna as dataConsegna, ");
        sb.append("ro.dataProduzione as dataProduzione, ");
        sb.append("d.dataDocumento as dataDocumento, ");
        sb.append("ro.configurazioneDistinta_id as idConfigurazioneDistinta, ");
        sb.append("ao.depositoOrigine_id as idDeposito, ");
        sb.append("GROUP_CONCAT(ta.codice) AS codiciAttributo, ");
        sb.append("GROUP_CONCAT(ar.valore) AS valoriAttributo, ");
        sb.append("coalesce(roc.areaOrdine_id,rod.areaOrdine_id)  as idOrdineCliente ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("left join maga_righe_magazzino rm on ro.rigaDistintaCollegata_id=rm.rigaOrdineCollegata_Id ");
        sb.append("left join ordi_righe_ordine rod on rod.id=ro.rigaDistintaCollegata_id ");
        sb.append("left join ordi_attributi_riga ar on ar.rigaArticolo_id=ro.id ");
        sb.append("left join maga_tipo_attributo ta on ta.id=ar.tipoAttributo_id ");
        sb.append("left join ordi_righe_ordine_ordi_righe_ordine rcoll on rcoll.ordi_righe_ordine_id=rod.id ");
        sb.append("left join ordi_righe_ordine roc on roc.id=rcoll.righeOrdineCollegate_id ");
        sb.append("inner join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=ao.tipoAreaOrdine_id ");
        sb.append("inner join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
        sb.append("inner join docu_documenti d on d.id=ao.documento_id ");
        sb.append("inner join maga_articoli art on art.id=ro.articolo_id ");
        sb.append("where ro.TIPO_RIGA='C' and ro.rigaPadre_id is null ");
        sb.append("and art.mrp=true ");
        sb.append("and ao.statoAreaOrdine>0 ");
        sb.append("and ro.evasioneForzata=false ");
        sb.append("and ro.dataConsegna>='");
        sb.append(startDateString);
        sb.append("' and ro.dataConsegna<='");
        sb.append(endDateString).append("' ");
        sb.append("group by ro.id having sum(coalesce(rm.qtaMagazzino,0))=0 ");
        sb.append(" union ");
        sb.append("select ");
        sb.append("2 as tipoRiga,");
        sb.append("ro.areaOrdine_id as idOrdine, ");
        sb.append("ro.id as idRigaOrdine, ");
        sb.append("ro.articolo_id as idArticolo, ");
        sb.append("ro.qta-sum(coalesce(rm.qtaMagazzino,0)) as qtaR, ");
        sb.append(" 0 as qtaDistintaEvasa, ");
        sb.append("ro.dataConsegna as dataConsegna, ");
        sb.append("ro.dataProduzione as dataProduzione, ");
        sb.append("d.dataDocumento as dataDocumento, ");
        sb.append("ro.configurazioneDistinta_id as idConfigurazioneDistinta, ");
        sb.append("ao.depositoOrigine_id as idDeposito, ");
        sb.append("GROUP_CONCAT(ta.codice) AS codiciAttributo,");
        sb.append("GROUP_CONCAT(ar.valore) AS valoriAttributo, ");
        sb.append("coalesce(roc.areaOrdine_id,ro.areaOrdine_id) as idOrdineCliente ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("left join maga_righe_magazzino rm on ro.id=rm.rigaOrdineCollegata_Id ");
        sb.append("left join ordi_attributi_riga ar on ar.rigaArticolo_id=ro.id ");
        sb.append("left join maga_tipo_attributo ta on ta.id=ar.tipoAttributo_id ");
        sb.append("left join ordi_righe_ordine_ordi_righe_ordine rcoll on rcoll.ordi_righe_ordine_id=ro.id ");
        sb.append("left join ordi_righe_ordine roc on roc.id=rcoll.righeOrdineCollegate_id ");
        sb.append("inner join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=ao.tipoAreaOrdine_id ");
        sb.append("inner join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
        sb.append("inner join docu_documenti d on d.id=ao.documento_id ");
        sb.append("inner join maga_articoli art on art.id=ro.articolo_id ");
        sb.append("where ro.TIPO_RIGA='D' ");
        sb.append("and art.mrp=true ");
        sb.append("and ao.statoAreaOrdine>0 ");
        sb.append("and ro.evasioneForzata=false ");
        sb.append("and ro.dataConsegna>='");
        sb.append(startDateString);
        sb.append("' and ro.dataConsegna<='");
        sb.append(endDateString).append("' ");
        sb.append(" group by ro.id");
        sb.append(" having qtaR>0 ");
        sb.append("union ");
        sb.append("select ");
        sb.append("0 as tipoRiga,");
        sb.append("ro.areaOrdine_id as idOrdine, ");
        sb.append("ro.id as idRigaOrdine, ");
        sb.append("ro.articolo_id as idArticolo, ");
        sb.append("ro.qta+ifnull(ro.qtaMagazzinoTolleranza,0)-sum(coalesce(rm.qtaMagazzino,0)) as qtaR, ");
        sb.append(" 0 as qtaDistintaEvasa, ");
        sb.append("coalesce(ro.dataFineProduzione,ro.dataConsegna) as dataConsegna, ");
        sb.append("ro.dataProduzione as dataProduzione, ");
        sb.append("null as dataDocumento, ");
        sb.append("ro.configurazioneDistinta_id as idConfigurazioneDistinta, ");
        sb.append("ao.depositoOrigine_id as idDeposito, ");
        sb.append("GROUP_CONCAT(ta.codice) AS codiciAttributo,");
        sb.append("GROUP_CONCAT(ar.valore) AS valoriAttributo, ");
        sb.append("ro.areaOrdine_id as idOrdineCliente ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("left join maga_righe_magazzino rm on ro.id=rm.rigaOrdineCollegata_Id ");
        sb.append("left join ordi_attributi_riga ar on ar.rigaArticolo_id=ro.id ");
        sb.append("left join maga_tipo_attributo ta on ta.id=ar.tipoAttributo_id ");
        sb.append("inner join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=ao.tipoAreaOrdine_id ");
        sb.append("inner join docu_documenti d on d.id=ao.documento_id ");
        sb.append("inner join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
        sb.append("inner join maga_articoli art on art.id=ro.articolo_id ");
        sb.append("where td.tipoEntita=0 ");
        sb.append("and ro.TIPO_RIGA ='A' ");
        sb.append("and art.mrp=true ");
        sb.append("and ao.statoAreaOrdine>0 ");
        sb.append("and ro.evasioneForzata=false ");
        sb.append("and ro.dataConsegna>='");
        sb.append(startDateString);
        sb.append("' and ro.dataConsegna<='");
        sb.append(endDateString).append("' ");
        sb.append(" group by ro.id ");
        sb.append(" having qtaR>0 ");

        QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        query.getHibernateQuery().setResultTransformer(Transformers.aliasToBean((RisultatoMRPArticoloBucket.class)));
        SQLQuery sqlQuery = (SQLQuery) query.getHibernateQuery();
        sqlQuery.addScalar("tipoRiga", Hibernate.INTEGER);
        sqlQuery.addScalar("idOrdine");
        sqlQuery.addScalar("idRigaOrdine");
        sqlQuery.addScalar("idArticolo");
        sqlQuery.addScalar("qtaR");
        sqlQuery.addScalar("dataConsegna", Hibernate.DATE);
        sqlQuery.addScalar("dataProduzione", Hibernate.DATE);
        sqlQuery.addScalar("dataDocumento", Hibernate.DATE);
        sqlQuery.addScalar("idDeposito");
        sqlQuery.addScalar("idConfigurazioneDistinta");
        sqlQuery.addScalar("codiciAttributo", Hibernate.STRING);
        sqlQuery.addScalar("valoriAttributo", Hibernate.STRING);
        sqlQuery.addScalar("idOrdineCliente");

        List<RisultatoMRPArticoloBucket> result = null;
        try {
            result = sqlQuery.list();
        } catch (Exception e) {
            System.err.println(e);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RisultatoMRPArticoloBucket> caricaRigheProduzionePresenti(Date dataInizio, Date dataFine) {
        String endDateString = new SimpleDateFormat("yyyyMMdd").format(dataFine);
        String startDateString = new SimpleDateFormat("yyyyMMdd").format(dataInizio);
        StringBuilder sb = new StringBuilder(1500);

        // ordini produzione, i componenti
        sb.append("select ");
        sb.append("ro.id as idRigaOrdine, ");
        sb.append("ro.articolo_id as idArticolo, ");
        sb.append("coalesce(ro.qtaR,ro.qta)-coalesce(rm.qtaMagazzino,0) as qtaR, ");
        sb.append("ro.dataConsegna as dataConsegna, ");
        sb.append("ro.dataProduzione as dataProduzione, ");
        sb.append("d.dataDocumento as dataDocumento, ");
        sb.append("ro.configurazioneDistinta_id as idConfigurazioneDistinta, ");
        sb.append("ao.depositoOrigine_id as idDeposito, ");
        sb.append("GROUP_CONCAT(ta.codice) AS codiciAttributo,");
        sb.append("GROUP_CONCAT(ar.valore) AS valoriAttributo ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("left join maga_righe_magazzino rm on ro.id=rm.rigaOrdineCollegata_Id ");
        sb.append("left join ordi_attributi_riga ar on ar.rigaArticolo_id=ro.id ");
        sb.append("left join maga_tipo_attributo ta on ta.id=ar.tipoAttributo_id ");
        sb.append("inner join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=ao.tipoAreaOrdine_id ");
        sb.append("inner join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
        sb.append("inner join docu_documenti d on d.id=ao.documento_id ");
        sb.append("inner join maga_articoli art on art.id=ro.articolo_id ");
        // per evitare di caricare le distinte multilivello come componente e
        // come distinta
        // carico i componenti che non sono distinta e le distinte
        sb.append(
                "where ((ro.TIPO_RIGA='C' and ro.dataProduzione is null and art.distinta=false) or (ro.TIPO_RIGA='D'))");
        sb.append("and art.mrp=true ");
        sb.append("and ao.statoAreaOrdine>0 ");
        sb.append("and ro.evasioneForzata=false ");
        sb.append("and ro.qta-coalesce(rm.qta,0)>0 ");
        sb.append("and ro.dataConsegna>='");
        sb.append(startDateString);
        sb.append("' and ro.dataConsegna<='");
        sb.append(endDateString);
        sb.append("' group by ro.id");

        QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        query.getHibernateQuery().setResultTransformer(Transformers.aliasToBean((RisultatoMRPArticoloBucket.class)));
        SQLQuery sqlQuery = (SQLQuery) query.getHibernateQuery();
        sqlQuery.addScalar("idRigaOrdine");
        sqlQuery.addScalar("idArticolo");
        sqlQuery.addScalar("qtaR");
        sqlQuery.addScalar("dataConsegna", Hibernate.DATE);
        sqlQuery.addScalar("dataProduzione", Hibernate.DATE);
        sqlQuery.addScalar("dataDocumento", Hibernate.DATE);
        sqlQuery.addScalar("idDeposito");
        sqlQuery.addScalar("idConfigurazioneDistinta");
        sqlQuery.addScalar("codiciAttributo", Hibernate.STRING);
        sqlQuery.addScalar("valoriAttributo", Hibernate.STRING);

        List<RisultatoMRPArticoloBucket> result = null;
        try {
            result = sqlQuery.list();
        } catch (Exception e) {
            System.err.println(e);
        }

        return result;
    }

    @Override
    public List<RisultatoMrpFlat> caricaRisultatoMrp(ParametriMrpRisultato parametri) {
        try {
            StringBuilder sb = new StringBuilder(1000);
            sb.append("select ");
            sb.append("ris.id, ");
            sb.append("roc.TIPO_RIGA, ");
            sb.append("art.id as articoloId, ");
            sb.append("art.codice as articoloCodice, ");
            sb.append("art.distinta as articoloDistinta, ");
            sb.append("art.numeroDecimaliQta as articoloNumeroDecimaliQta, ");
            sb.append("art.descrizioneLinguaAziendale as articoloDescrizione, ");
            sb.append("ris.idConfigurazioneDistinta as configurazioneDistintaId, ");
            sb.append("confdist.nome as configurazioneDistintaNome, ");
            sb.append("dep.id as depositoId, ");
            sb.append("dep.codice as depositoCodice, ");
            sb.append("dep.descrizione as depositoDescrizione, ");
            sb.append("ent.id as fornitoreId, ");
            sb.append("ent.codice as fornitoreCodice, ");
            sb.append("anag.denominazione as fornitoreDenominazione, ");
            sb.append("ris.dataConsegna as dataConsegna, ");
            sb.append("ris.dataDocumento as dataDocumento, ");
            sb.append("ris.qtaPor as qtaCalcolata, ");
            sb.append("ris.qtaR as qtaR, ");
            sb.append("ris.qtaInArrivo as qtaInArrivo, ");
            sb.append("ris.minOrdinabile as minOrdinabile, ");
            sb.append("ris.lottoRiordino as lottoRiordino, ");
            sb.append("ris.leadTime as leadTime, ");
            sb.append("ris.giacenza as giacenza, ");
            sb.append("ris.disponibilitaDopoUtilizzo as disponibilitaDopoUtilizzo, ");
            sb.append("ris.disponibilita as disponibilita, ");
            sb.append("ris.scorta as scorta, ");
            sb.append("ris.minOrdinabile as minOrdinabile, ");
            sb.append("ris.ordinamento as ordinamento, ");
            sb.append("td.id as tipoDocumentoId, ");
            sb.append("td.codice as tipoDocumentoCodice, ");
            sb.append("td.descrizione as tipoDocumentoDescrizione, ");
            sb.append("ris.idTipoDocumento as tipoAreaOrdineDaCreareId, ");
            sb.append("docc.id as ordineId, ");
            sb.append("docc.codice as ordineCodice, ");
            sb.append("docc.dataDocumento as ordineData, ");
            sb.append("tdc.id as tipoDocumentoOrdineId, ");
            sb.append("tdc.descrizione as tipoDocumentoOrdineDescrizione, ");
            sb.append("tdc.codice as tipoDocumentoOrdineCodice, ");
            sb.append("taoc.ordineProduzione as ordineProduzione, ");
            sb.append("aoc.id as idAreaOrdine, ");
            sb.append("roc.id as idRigaOrdine, ");
            sb.append("IF(roc.TIPO_RIGA='A',roc.ordinamento,0) as numRiga, ");
            sb.append("dist.id as distintaId, ");
            sb.append("dist.descrizioneLinguaAziendale as distintaDescrizione, ");
            sb.append("dist.codice as distintaCodice, ");
            sb.append("ris.distinta_id ");
            sb.append("from mrp_risultati ris ");
            sb.append("inner join maga_articoli art on art.id=ris.idArticolo ");
            sb.append("inner join anag_depositi dep on dep.id=ris.idDeposito ");
            sb.append("left join anag_entita ent on ent.id=ris.idFornitore ");
            sb.append("left join anag_anagrafica anag on ent.anagrafica_id=anag.id ");
            sb.append("left join ordi_tipi_area_ordine tao on tao.id=ris.idTipoDocumento ");
            sb.append("left join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
            sb.append("left join ordi_righe_ordine roc on roc.id=ris.idRigaOrdine ");
            sb.append("left join ordi_area_ordine aoc on aoc.id=roc.areaOrdine_id ");
            sb.append("left join docu_documenti docc on docc.id=aoc.documento_id ");
            sb.append("left join ordi_tipi_area_ordine taoc on taoc.id=aoc.tipoAreaOrdine_id ");
            sb.append("left join docu_tipi_documento tdc on tdc.id=taoc.tipoDocumento_id ");
            sb.append("left join anag_entita entc on entc.id=docc.entita_id ");
            sb.append("left join ordi_righe_ordine rod on rod.id=roc.rigaDistintaCollegata_id ");
            sb.append("left join maga_distinte_configurazione confdist on confdist.id=ris.idConfigurazioneDistinta ");
            sb.append("left join mrp_risultati risDistinte on risDistinte.id=ris.idDistintaRisultato ");
            sb.append("left join maga_articoli dist on dist.id=risDistinte.idArticolo ");
            sb.append("where ris.userInsert=").append(getUtente()).append(" ");

            if (parametri.isEscludiOrdinati()) {
                sb.append(" and ris.qtaPor>0");
            }

            SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString(), RisultatoMrpFlat.class);
            query.addScalar("id");
            query.addScalar("articoloId");
            query.addScalar("articoloCodice");
            query.addScalar("articoloDescrizione");
            query.addScalar("articoloDistinta");
            query.addScalar("articoloNumeroDecimaliQta");
            query.addScalar("configurazioneDistintaId");
            query.addScalar("configurazioneDistintaNome");
            query.addScalar("depositoId");
            query.addScalar("depositoCodice");
            query.addScalar("depositoDescrizione");
            query.addScalar("fornitoreId");
            query.addScalar("fornitoreCodice");
            query.addScalar("fornitoreDenominazione");
            query.addScalar("dataConsegna", Hibernate.DATE);
            query.addScalar("dataDocumento", Hibernate.DATE);
            query.addScalar("qtaCalcolata");
            query.addScalar("qtaR");
            query.addScalar("qtaInArrivo");
            query.addScalar("leadTime");
            query.addScalar("giacenza");
            query.addScalar("disponibilita");
            query.addScalar("disponibilitaDopoUtilizzo");
            query.addScalar("scorta");
            query.addScalar("minOrdinabile");
            query.addScalar("ordinamento");
            query.addScalar("lottoRiordino");
            query.addScalar("tipoDocumentoId");
            query.addScalar("tipoDocumentoCodice");
            query.addScalar("tipoDocumentoDescrizione");
            query.addScalar("tipoAreaOrdineDaCreareId");
            query.addScalar("ordineId");
            query.addScalar("ordineCodice");
            query.addScalar("ordineData");
            query.addScalar("tipoDocumentoOrdineId");
            query.addScalar("tipoDocumentoOrdineDescrizione");
            query.addScalar("tipoDocumentoOrdineCodice");
            query.addScalar("ordineProduzione");
            query.addScalar("idAreaOrdine", Hibernate.INTEGER);
            query.addScalar("idRigaOrdine", Hibernate.INTEGER);
            query.addScalar("numRiga", Hibernate.INTEGER);
            query.addScalar("distintaId");
            query.addScalar("distintaDescrizione");
            query.addScalar("distintaCodice");
            @SuppressWarnings("unchecked")
            final List<RisultatoMrpFlat> result = query.list();

            // posso avere dei conflitti fra utenti se calcolo lo stesso
            // articolo per ordini diversi. Cerco eventuali
            // vari conflitti
            sb = new StringBuilder();
            sb.append("select ");
            sb.append("rother.userInsert as user, ");
            sb.append("rother.timeStamp as timeStamp, ");
            sb.append("distinta.codice as codiceDistinta, ");
            sb.append("distinta.descrizioneLinguaAziendale as descrizioneDistinta, ");
            sb.append("rother.idArticolo as idArticolo, ");
            sb.append("ro.numeroRiga as numeroRiga, ");
            sb.append("doc.codice as documentoCodice, ");
            sb.append("doc.dataDocumento as documentoData, ");
            sb.append("ent.codice as entitaCodice, ");
            sb.append("anag.denominazione as entitaDenominazione ");
            sb.append("from mrp_risultati r ");
            sb.append("inner join mrp_risultati rother on rother.idArticolo=r.idArticolo ");
            sb.append("inner join ordi_righe_ordine ro on ro.id=rother.idRigaOrdine ");
            sb.append("inner join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
            sb.append("inner join docu_documenti doc on ao.documento_id=doc.id ");
            sb.append("inner join anag_entita ent on ent.id=doc.entita_id ");
            sb.append("inner join anag_anagrafica anag on anag.id=ent.anagrafica_id ");
            sb.append("inner join maga_articoli distinta on distinta.id=rother.distinta_id ");
            sb.append("where r.userInsert=");
            sb.append(getUtente());
            sb.append("and rother.userInsert<>");
            sb.append(getUtente());
            query = panjeaDAO.prepareNativeQuery(sb.toString(), ConflittoMrp.class);
            query.addScalar("user");
            query.addScalar("timeStamp");
            query.addScalar("codiceDistinta");
            query.addScalar("descrizioneDistinta");
            query.addScalar("idArticolo");
            query.addScalar("numeroRiga");
            query.addScalar("documentoCodice");
            query.addScalar("documentoData");
            query.addScalar("entitaCodice");
            query.addScalar("entitaDenominazione");
            @SuppressWarnings("unchecked")
            List<ConflittoMrp> conflitti = query.list();

            Map<Integer, ConflittoMrp> conflittiMap = new HashMap<>();
            for (ConflittoMrp conflittoMrp : conflitti) {
                conflittiMap.put(conflittoMrp.getIdArticolo(), conflittoMrp);
            }

            for (RisultatoMrpFlat risultatoMrpFlat : result) {
                ConflittoMrp conflittoArticolo = conflittiMap.get(risultatoMrpFlat.getArticolo().getId());
                if (conflittoArticolo != null) {
                    risultatoMrpFlat.addConflitto(conflittoArticolo);
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("-->errore nel caricare i risultati dell MRP", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> getIdDepositi() {
        List<Deposito> depositi = depositiManager.caricaDepositi();
        List<Integer> result = new ArrayList<>();
        for (Deposito deposito : depositi) {
            result.add(deposito.getId());
        }
        return result;
    }

    private String getUtente() {
        return "'" + ((JecPrincipal) context.getCallerPrincipal()).getUserName() + "' ";
    }

    @Override
    public List<RisultatoMrpFlat> salvaRigheRisultato(List<RisultatoMrpFlat> righeMrpFlat) {
        logger.debug("--> Enter salvaRigaRisultato");
        for (RisultatoMrpFlat rigaRisultatoMrpFlat : righeMrpFlat) {
            try {
                RisultatoMRP risultato = panjeaDAO.load(RisultatoMRP.class, rigaRisultatoMrpFlat.getId());
                Integer idFornitore = null;
                if (rigaRisultatoMrpFlat.getFornitore() != null
                        && rigaRisultatoMrpFlat.getFornitore().getId() != null) {
                    idFornitore = rigaRisultatoMrpFlat.getFornitore().getId();
                }
                risultato.setIdFornitore(idFornitore);
                risultato.setDataDocumento(rigaRisultatoMrpFlat.getDataDocumento());
                risultato.setDataConsegna(rigaRisultatoMrpFlat.getDataConsegna());
                risultato.setIdTipoDocumento(rigaRisultatoMrpFlat.getTipoAreaOrdineDaGenerare() == null ? null
                        : rigaRisultatoMrpFlat.getTipoAreaOrdineDaGenerare().getId());
                risultato.setQtaPor(ObjectUtils.defaultIfNull(rigaRisultatoMrpFlat.getQtaCalcolata(), 0.0));
                panjeaDAO.save(risultato);
            } catch (DAOException e) {
                logger.error("-->errore nel salvare il risultato " + rigaRisultatoMrpFlat, e);
                throw new RuntimeException("-->errore nel salvare il risultato " + rigaRisultatoMrpFlat, e);
            }
        }
        logger.debug("--> Exit salvaRigaRisultato");
        return righeMrpFlat;
    }

    @Override
    public void salvaRisultatoMRP(List<RisultatoMRPArticoloBucket> risultati) {
        int counter = 0;
        svuotaRigheRisultati();

        Map<UUID, RisultatoMRP> risultatiSalvati = new HashMap<>();
        for (RisultatoMRPArticoloBucket risultato : risultati) {
            try {
                RisultatoMRP risultatoMrp = ObjectUtils.defaultIfNull(risultatiSalvati.get(risultato.getUuid()),
                        new RisultatoMRP(risultato));
                counter++;
                if (risultato.getRisultatoArticoloDistinta() != null) {
                    RisultatoMRP distinta = ObjectUtils.defaultIfNull(
                            risultatiSalvati.get(risultato.getRisultatoArticoloDistinta().getUuid()),
                            new RisultatoMRP(risultato.getRisultatoArticoloDistinta()));
                    if (distinta.isNew()) {
                        counter++;
                        distinta = panjeaDAO.saveWithoutFlush(distinta);
                        risultatiSalvati.put(risultato.getRisultatoArticoloDistinta().getUuid(), distinta);
                    }
                    risultatoMrp.setDistinta(distinta);
                }
                risultatoMrp = panjeaDAO.saveWithoutFlush(risultatoMrp);
                risultatiSalvati.put(risultato.getUuid(), risultatoMrp);
                if (risultato.getRisultatoArticoloDistinta() != null) {
                    risultatiSalvati.put(risultato.getRisultatoArticoloDistinta().getUuid(),
                            risultatoMrp.getDistinta());
                }
                if (counter % 40 == 0) {
                    panjeaDAO.getEntityManager().flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            panjeaDAO.getEntityManager().flush();
        }
        // L'ordine a produzione già presente non ha l'id della distinta. quindi
        // aggiorno i riferimenti della
        // distinta nei risultati mrp in un batch
        SQLQuery updateDistintaQuery = panjeaDAO
                .prepareNativeQuery("update mrp_risultati set idDistintaRisultato=distinta_id");
        updateDistintaQuery.executeUpdate();

        StringBuilder sb = new StringBuilder(2000);
        sb.append("update  mrp_risultati ");
        sb.append("inner join ( ");
        sb.append("select ");
        sb.append("coalesce(mrp_distinta.id,mrp.id) as distinta_id, ");
        sb.append("mrp.id ");
        sb.append("from mrp_risultati mrp ");
        sb.append("left join ordi_righe_ordine ro on ro.id=mrp.idRigaOrdine and ro.TIPO_RIGA<>'A' ");
        sb.append("inner join maga_articoli art on art.id=ro.articolo_id ");
        sb.append("left join ordi_righe_ordine ro_distinta on ro_distinta.id=ro.rigaDistintaCollegata_id ");
        sb.append(
                "left join mrp_risultati mrp_distinta on mrp_distinta.idRigaOrdine=ro_distinta.id) as mrpProduzione ");
        sb.append("on mrpProduzione.id=mrp_risultati.id ");
        sb.append("set mrp_risultati.idDistintaRisultato=mrpProduzione.distinta_id ");
        updateDistintaQuery = panjeaDAO.prepareNativeQuery(sb.toString());

        updateDistintaQuery.executeUpdate();
        // Ora aggiorno i riferimenti distinta degli articoli degli ordini
        // cliente
        updateDistintaQuery = panjeaDAO.prepareNativeQuery(
                "update mrp_risultati set idDistintaRisultato=id where idDistintaRisultato is null");
        updateDistintaQuery.executeUpdate();

        // Setto la data documento degli ordini fornitori uguale ad oggi
        sb = new StringBuilder(1000);
        sb.append("update mrp_risultati ");
        sb.append("inner join ordi_tipi_area_ordine tao on tao.id=mrp_risultati.idTipoDocumento ");
        sb.append("inner join docu_tipi_documento td on td.id=tao.tipoDocumento_id ");
        sb.append("set mrp_risultati.dataDocumento=now() ");
        sb.append("where td.tipoEntita=1 ");
        panjeaDAO.prepareNativeQuery(sb.toString()).executeUpdate();

    }

    @Override
    public void svuotaRigheRisultati() {
        logger.debug("--> Enter cancellaRigheRisultati");
        Query queryUpdate = panjeaDAO
                .prepareQuery("update RisultatoMRP r set r.distinta=null where r.userInsert=" + getUtente());
        try {
            panjeaDAO.executeQuery(queryUpdate);
            // Se non ho altre righe faccio una truncate per ottimizzare la
            // tabella
            SQLQuery countQuery = panjeaDAO
                    .prepareNativeQuery(" select count(*) from mrp_risultati r where r.userInsert<>" + getUtente());
            BigInteger numeroRecord = (BigInteger) countQuery.list().get(0);
            SQLQuery queryDelete = panjeaDAO.prepareNativeQuery("truncate mrp_risultati");
            if (!numeroRecord.equals(BigInteger.ZERO)) {
                queryDelete = panjeaDAO
                        .prepareNativeQuery("delete from mrp_risultati where mrp_risultati.userInsert=" + getUtente());
            }
            queryDelete.executeUpdate();
        } catch (DAOException e) {
            logger.error("Errore nel cancellare i risultati mrp", e);
            throw new RuntimeException("Errore nel cancellare i risultati mrp", e);
        }
        logger.debug("--> Exit cancellaRigheRisultati");
    }
}