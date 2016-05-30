package it.eurotn.panjea.cosaro.rendicontazione;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.exporter.RendicontazioneWriter;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.TracciabilitaRendicontazioneExporter.Coop")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TracciabilitaRendicontazioneExporter.Coop")
public class CoopTracciabilitaRendicontazioneExporterBean extends AbstractCoopRendicontazioneExporterBean {

	@Override
	public List<byte[]> export(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) {

		List<Object> rendicontazioneBeans = new ArrayList<Object>();

		Set<String> uuiToExport = getRigheToExport(areeMagazzino);

		StringBuilder sb = new StringBuilder();
		sb.append("select " + PanjeaEJBUtil.addQuote(tipoEsportazione.getCodiceCliente()) + " as codiceFornitoreCoop, ");
		sb.append("		   rfc.numeroBolla as numeroBolla, ");
		sb.append("		   rfc.dataBolla as dataBolla, ");
		sb.append("		   codArtForn.codice as codiceArticoloCoop, ");
		sb.append("		   codArtForn.barcode2 as barCodeCoop, ");
		sb.append("		   codArtForn.descrizione as descrizioneArticolo, ");
		sb.append("		   sum(rfc.numeroRigaColli) as colli, ");
		sb.append("		   sum(rfc.numeroRigaPezzi) as pezzi, ");
		sb.append("		   sum(rm.qta) as quantita, ");
		sb.append("		   lotto.codice as lotto ");
		sb.append("from maga_area_magazzino am inner join maga_righe_magazzino rm on am.id = rm.areaMagazzino_id ");
		sb.append("														inner join maga_righe_lotti rl on rm.id = rl.rigaArticolo_id ");
		sb.append("														inner join maga_lotti lotto on lotto.id = rl.lotto_id ");
		sb.append("														inner join docu_documenti doc on doc.id = am.documento_id ");
		sb.append("														inner join maga_codici_articolo_entita codArtForn on codArtForn.articolo_id = rm.articolo_id and codArtForn.entita_id = doc.entita_id ");
		sb.append("														inner join ordi_righe_ordine roc on rm.rigaOrdineCollegata_Id = roc.id ");
		sb.append("														inner join ordi_area_ordine ao on ao.id = roc.areaOrdine_id ");
		sb.append("														left join cosa_riga_file_coop rfc on rfc.numeroOrdine = ao.numeroOrdine ");
		sb.append("																					  and rfc.dataOrdine = ao.dataOrdine ");
		sb.append("																					  and rfc.numeroRigaColli = roc.numeroRiga ");
		sb.append("where rfc.uuid = :paramUUID ");
		sb.append("group by rm.articolo_id,lotto.codice ");
		sb.append("order by rfc.numeroRiga");
		Query queryFile = panjeaDAO.prepareSQLQuery(sb.toString(),
				CoopTracciabilitaRigaRendicontazioneBeanExporter.class, null);
		SQLQuery querySQL = ((SQLQuery) ((QueryImpl) queryFile).getHibernateQuery());
		querySQL.addScalar("codiceFornitoreCoop");
		querySQL.addScalar("numeroBolla");
		querySQL.addScalar("dataBolla");
		querySQL.addScalar("codiceArticoloCoop");
		querySQL.addScalar("barCodeCoop");
		querySQL.addScalar("descrizioneArticolo");
		querySQL.addScalar("colli", Hibernate.INTEGER);
		querySQL.addScalar("pezzi", Hibernate.INTEGER);
		querySQL.addScalar("quantita", Hibernate.DOUBLE);
		querySQL.addScalar("lotto");

		for (String uuidFile : uuiToExport) {
			queryFile.setParameter("paramUUID", uuidFile);
			try {
				@SuppressWarnings("unchecked")
				List<Object> righeTracciabilita = panjeaDAO.getResultList(queryFile);
				rendicontazioneBeans.addAll(righeTracciabilita);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		RendicontazioneWriter writer = new RendicontazioneWriter();
		List<byte[]> flussi = new ArrayList<byte[]>();
		flussi.add(writer.write(tipoEsportazione.getTemplate(), rendicontazioneBeans));

		return flussi;
	}
}
