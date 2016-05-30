package it.eurotn.panjea.ordini.manager.documento.evasione;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DistintaCaricoVerificaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.DistintaCaricoVerificaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DistintaCaricoVerificaManager")
public class DistintaCaricoVerificaManagerBean implements DistintaCaricoVerificaManager {
    private static final Logger LOGGER = Logger.getLogger(DistintaCaricoVerificaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDao;

    @Override
    public DatiDistintaCaricoVerifica caricaDatiVerifica(Date dataInizioTrasporto) {
        DatiDistintaCaricoVerifica datiDistintaCaricoVerifica = new DatiDistintaCaricoVerifica(dataInizioTrasporto);
        datiDistintaCaricoVerifica = caricaOrdiniInevasi(datiDistintaCaricoVerifica, dataInizioTrasporto);
        datiDistintaCaricoVerifica = caricaRigheInevase(datiDistintaCaricoVerifica, dataInizioTrasporto);
        datiDistintaCaricoVerifica = caricaOrdiniInProduzione(datiDistintaCaricoVerifica);
        datiDistintaCaricoVerifica = caricaRigheInProduzione(datiDistintaCaricoVerifica);
        datiDistintaCaricoVerifica = caricaOrdiniInProduzioneConData(datiDistintaCaricoVerifica, dataInizioTrasporto);
        datiDistintaCaricoVerifica = caricaRigheInProduzioneConData(datiDistintaCaricoVerifica, dataInizioTrasporto);
        datiDistintaCaricoVerifica = caricaMagazzinoEvasiConData(datiDistintaCaricoVerifica, dataInizioTrasporto);
        datiDistintaCaricoVerifica = caricaRigheMagazzinoEvaseConData(datiDistintaCaricoVerifica, dataInizioTrasporto);
        return datiDistintaCaricoVerifica;
    }

    private DatiDistintaCaricoVerifica caricaMagazzinoEvasiConData(
            DatiDistintaCaricoVerifica datiDistintaCaricoVerifica, Date dataInizioTrasporto) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append(
                "select count(*) from maga_area_magazzino am where am.tipoGenerazione=1 and am.dataRegistrazione=:dataInizioTrasporto");
        SQLQuery query = panjeaDao.prepareNativeQuery(sb.toString());
        dataInizioTrasporto = DateUtils.truncate(dataInizioTrasporto, Calendar.DATE);
        query.setParameter("dataInizioTrasporto", dataInizioTrasporto);
        int ordinievavasiConData = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setOrdiniEvasiConData(ordinievavasiConData);
        return datiDistintaCaricoVerifica;
    }

    private DatiDistintaCaricoVerifica caricaOrdiniInevasi(DatiDistintaCaricoVerifica datiDistintaCaricoVerifica,
            Date dataInizioTrasporto) {
        LOGGER.debug("--> Enter caricaOrdiniInevasi");
        StringBuilder sb = new StringBuilder(1000);
        sb.append("select count(*) from ordi_area_ordine areaordine0_  ");
        sb.append("inner join docu_documenti doc on doc.id=areaordine0_.documento_id ");
        sb.append("inner join docu_tipi_documento td on td.id=doc.tipo_documento_id ");
        sb.append("where ( ");
        sb.append("   select ");
        sb.append("   if ");
        sb.append("   ( ");
        sb.append(
                "      sum(if(rigaOrd.qta-coalesce((select sum(rm.qta*rm.moltQtaOrdine) from maga_righe_magazzino rm where rm.rigaOrdineCollegata_Id = rigaOrd.id),0)>0,1,0))> 0, ");
        sb.append("      0, ");
        sb.append("      1 ");
        sb.append("   ) ");
        sb.append("   from ordi_righe_ordine rigaOrd ");
        sb.append("   where rigaOrd.areaOrdine_id = areaordine0_.id ");
        sb.append("   and rigaOrd.evasioneForzata = 0 ");
        sb.append(") =0 and td.tipoEntita=0");
        SQLQuery query = panjeaDao.prepareNativeQuery(sb.toString());

        int ordiniInvevasi = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setOrdiniInevasi(ordiniInvevasi);

        sb.append(" and areaordine0_.dataInizioTrasporto=:dataInizioTrasporto");
        query = panjeaDao.prepareNativeQuery(sb.toString());
        dataInizioTrasporto = DateUtils.truncate(dataInizioTrasporto, Calendar.DATE);
        query.setParameter("dataInizioTrasporto", dataInizioTrasporto);
        int ordiniInvevasiConData = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setOrdiniInevasiConData(ordiniInvevasiConData);
        LOGGER.debug("--> Exit caricaOrdiniInevasi");
        return datiDistintaCaricoVerifica;
    }

    private DatiDistintaCaricoVerifica caricaOrdiniInProduzione(DatiDistintaCaricoVerifica datiDistintaCaricoVerifica) {
        LOGGER.debug("--> Enter caricaOrdiniInProduzione");
        SQLQuery query = panjeaDao.prepareNativeQuery(
                "select count(*) from (select areaOrdine_id from ordi_riga_distinta_carico rdc inner join ordi_righe_ordine ro on ro.id=rdc.rigaArticolo_id group by ro.areaOrdine_id ) a");
        datiDistintaCaricoVerifica.setOrdiniInProduzione(((BigInteger) query.list().get(0)).intValue());
        LOGGER.debug("--> Exit caricaOrdiniInProduzione");
        return datiDistintaCaricoVerifica;
    }

    private DatiDistintaCaricoVerifica caricaOrdiniInProduzioneConData(
            DatiDistintaCaricoVerifica datiDistintaCaricoVerifica, Date dataInizioTrasporto) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("select count(*) from ");
        sb.append("(select ");
        sb.append("   ao.id ");
        sb.append("   from ordi_riga_distinta_carico rdc ");
        sb.append("   inner join ordi_righe_ordine ro on ro.id=rdc.rigaArticolo_id ");
        sb.append("   inner join  ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append("   where ao.dataInizioTrasporto=:dataInizioTrasporto ");
        sb.append("   group by ro.areaOrdine_id ");
        sb.append(") a ");
        SQLQuery query = panjeaDao.prepareNativeQuery(sb.toString());
        dataInizioTrasporto = DateUtils.truncate(dataInizioTrasporto, Calendar.DATE);
        query.setParameter("dataInizioTrasporto", dataInizioTrasporto);
        int ordiniInProduzioneConData = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setOrdiniInProduzioneConData(ordiniInProduzioneConData);
        return datiDistintaCaricoVerifica;
    }

    private DatiDistintaCaricoVerifica caricaRigheInevase(DatiDistintaCaricoVerifica datiDistintaCaricoVerifica,
            Date dataInizioTrasporto) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("select ");
        sb.append("count(ro.id) ");
        sb.append("from ordi_area_ordine ao ");
        sb.append("inner join ordi_righe_ordine ro on ro.areaOrdine_id=ao.id ");
        sb.append("inner join docu_documenti doc on doc.id=ao.documento_id ");
        sb.append("inner join docu_tipi_documento td on td.id=doc.tipo_documento_id ");
        sb.append("where td.tipoEntita=0 ");
        sb.append(
                "and (ro.qta-coalesce((select sum(rm.qta*rm.moltQtaOrdine) from maga_righe_magazzino rm where rm.rigaOrdineCollegata_Id = ro.id),0)>0 and ro.evasioneForzata=false) ");
        SQLQuery query = panjeaDao.prepareNativeQuery(sb.toString());

        int righeInevase = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setRigheInevase(righeInevase);

        sb.append(" and ao.dataInizioTrasporto=:dataInizioTrasporto");
        query = panjeaDao.prepareNativeQuery(sb.toString());
        dataInizioTrasporto = DateUtils.truncate(dataInizioTrasporto, Calendar.DATE);
        query.setParameter("dataInizioTrasporto", dataInizioTrasporto);
        int righeInevaseConData = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setRigheInevaseConData(righeInevaseConData);
        return datiDistintaCaricoVerifica;
    }

    private DatiDistintaCaricoVerifica caricaRigheInProduzione(DatiDistintaCaricoVerifica datiDistintaCaricoVerifica) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        sb.append("(select ");
        sb.append("   ro.id ");
        sb.append("   from ordi_riga_distinta_carico rdc ");
        sb.append("   inner join ordi_righe_ordine ro on ro.id=rdc.rigaArticolo_id ");
        sb.append("   inner join  ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append(") a ");
        SQLQuery query = panjeaDao.prepareNativeQuery(sb.toString());
        int righeInProduzione = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setRigheInProduzione(righeInProduzione);
        return datiDistintaCaricoVerifica;
    }

    private DatiDistintaCaricoVerifica caricaRigheInProduzioneConData(
            DatiDistintaCaricoVerifica datiDistintaCaricoVerifica, Date dataInizioTrasporto) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        sb.append("(select ");
        sb.append("   ro.id ");
        sb.append("   from ordi_riga_distinta_carico rdc ");
        sb.append("   inner join ordi_righe_ordine ro on ro.id=rdc.rigaArticolo_id ");
        sb.append("   inner join  ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
        sb.append("   where ao.dataInizioTrasporto=:dataInizioTrasporto and rdc.qtaEvasa>:qtaEvasa");
        sb.append(") a ");

        SQLQuery query = panjeaDao.prepareNativeQuery(sb.toString());
        dataInizioTrasporto = DateUtils.truncate(dataInizioTrasporto, Calendar.DATE);
        query.setParameter("dataInizioTrasporto", dataInizioTrasporto);
        query.setParameter("qtaEvasa", Integer.MIN_VALUE);
        int righeInProduzioneConData = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setRigheInProduzioneConData(righeInProduzioneConData);

        query.setParameter("qtaEvasa", 0);
        int righeInProduzionePronteConData = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setRigheInProduzionePronteConData(righeInProduzionePronteConData);
        return datiDistintaCaricoVerifica;
    }

    private DatiDistintaCaricoVerifica caricaRigheMagazzinoEvaseConData(
            DatiDistintaCaricoVerifica datiDistintaCaricoVerifica, Date dataInizioTrasporto) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append(
                "select count(*) from maga_area_magazzino am inner join maga_righe_magazzino rm on rm.areaMagazzino_id=am.id  where am.tipoGenerazione=1 and TIPO_RIGA='A' and rm.rigaOrdineCollegata_Id is not null and am.dataRegistrazione=:dataInizioTrasporto");
        SQLQuery query = panjeaDao.prepareNativeQuery(sb.toString());
        dataInizioTrasporto = DateUtils.truncate(dataInizioTrasporto, Calendar.DATE);
        query.setParameter("dataInizioTrasporto", dataInizioTrasporto);
        int ordinievavasiConData = ((BigInteger) query.list().get(0)).intValue();
        datiDistintaCaricoVerifica.setRigheEvase(ordinievavasiConData);
        return datiDistintaCaricoVerifica;
    }

}
