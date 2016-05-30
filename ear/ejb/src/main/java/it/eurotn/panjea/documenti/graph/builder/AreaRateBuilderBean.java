package it.eurotn.panjea.documenti.graph.builder;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.AreaRateNode;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaRateBuilderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaRateBuilderBean")
public class AreaRateBuilderBean extends AbstractAreaDocumentoBuilderBean {

	private static Logger logger = Logger.getLogger(AreaRateBuilderBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public AreaDocumentoNode createNode(DocumentoNode node) {

		StringBuilder sb = new StringBuilder();
		sb.append("select codPag.codicePagamento, ");
		sb.append("			codPag.descrizione ");
		sb.append("from AreaRate ar inner join ar.documento doc left join ar.codicePagamento codPag ");
		sb.append("where doc.id = :paramIdDocumento ");

		AreaRateNode areaRateNode = null;

		try {
			Query query = panjeaDAO.prepareQuery(sb.toString());

			query.setParameter("paramIdDocumento", node.getIdDocumento());

			Object[] areaResult = (Object[]) panjeaDAO.getSingleResult(query);

			if (areaResult != null && areaResult.length != 0) {
				areaRateNode = new AreaRateNode();
				String codicePagamento = (String) areaResult[0];
				String descrizionePagamento = (String) areaResult[1];
				areaRateNode.setCodiceCodicePagamento(codicePagamento);
				areaRateNode.setDescrizioneCodicePagamento(descrizionePagamento);

				// carico le rate se esistono
				sb = new StringBuilder();
				sb.append("select rata.numeroRata, ");
				sb.append("			rata.importo.importoInValuta, ");
				sb.append("			rata.importo.codiceValuta, ");
				sb.append("			rata.dataScadenza ");
				sb.append("from Rata rata inner join rata.areaRate ar inner join ar.documento doc ");
				sb.append("where doc.id = :paramIdDocumento ");

				query = panjeaDAO.prepareQuery(sb.toString());

				query.setParameter("paramIdDocumento", node.getIdDocumento());

				@SuppressWarnings("unchecked")
				List<Object[]> rateResult = panjeaDAO.getResultList(query);

				List<Rata> rate = new ArrayList<Rata>();
				for (Object[] object : rateResult) {

					Integer numeroRata = (Integer) object[0];
					BigDecimal importo = (BigDecimal) object[1];
					String codiceValuta = (String) object[2];
					Date dataScadenza = (Date) object[3];

					Rata rata = new Rata();
					rata.setNumeroRata(numeroRata);
					Importo importoRata = new Importo();
					importoRata.setImportoInValuta(importo);
					importoRata.setCodiceValuta(codiceValuta);
					rata.setImporto(importoRata);
					rata.setDataScadenza(dataScadenza);

					rate.add(rata);
				}

				areaRateNode.setRate(rate);
			}
		} catch (ObjectNotFoundException e) {
			logger.info("Area rate non trovata, la lascio a null");
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'area rate.", e);
			throw new RuntimeException("errore durante il caricamento dell'area rate.", e);
		}

		return areaRateNode;
	}

	@Override
	public Map<Documento, String> getNextDocumentiCollegati(DocumentoNode documentoNode) {
		return new HashMap<Documento, String>();
	}

	@Override
	public Map<Documento, String> getPreviousDocumentiCollegati(DocumentoNode documentoNode) {
		return new HashMap<Documento, String>();
	}

}
