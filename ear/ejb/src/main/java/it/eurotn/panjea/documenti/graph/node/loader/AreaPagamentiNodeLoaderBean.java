package it.eurotn.panjea.documenti.graph.node.loader;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.documenti.graph.builder.AbstractAreaDocumentoBuilderBean;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.AreaPagamentiNode;
import it.eurotn.panjea.documenti.graph.node.loader.interfaces.AreaDocumentoNodeLoader;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

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

@Stateless(mappedName = "Panjea.AreaDocumentoNodeLoaderPA")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaDocumentoNodeLoaderPA")
public class AreaPagamentiNodeLoaderBean implements AreaDocumentoNodeLoader {

	private static Logger logger = Logger.getLogger(AreaPagamentiNodeLoaderBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public AreaDocumentoNode caricaAreaDocumentoNode(Integer idDocumento) {

		StringBuilder sb = new StringBuilder();
		sb.append("select pag.importo.importoInValuta as importoInValuta, ");
		sb.append("			pag.importo.codiceValuta as codiceValuta, ");
		sb.append("			rata.dataScadenza as dataScadenzaRata, ");
		sb.append("			rata.numeroRata as numeroRata ");
		sb.append("from Pagamento pag inner join pag.rata rata inner join pag.areaChiusure ac inner join ac.documento doc ");
		sb.append("where doc.id = :paramIdDocumento ");

		AreaPagamentiNode areaPagamentiNode = null;

		try {
			Query query = panjeaDAO.prepareQuery(sb.toString());

			query.setParameter("paramIdDocumento", idDocumento);

			@SuppressWarnings("unchecked")
			List<Object[]> result = panjeaDAO.getResultList(query);

			if (result != null && !result.isEmpty()) {
				areaPagamentiNode = new AreaPagamentiNode();
				List<Pagamento> pagamenti = new ArrayList<Pagamento>();
				for (Object[] object : result) {

					BigDecimal importo = (BigDecimal) object[0];
					String codiceValuta = (String) object[1];
					Date dataScadenza = (Date) object[2];
					Integer numeroRata = (Integer) object[3];

					Pagamento pagamento = new Pagamento();
					Importo importoPag = new Importo();
					importoPag.setImportoInValuta(importo);
					importoPag.setCodiceValuta(codiceValuta);
					pagamento.setImporto(importoPag);
					pagamento.setDataCreazione(dataScadenza);
					Rata rata = new Rata();
					rata.setNumeroRata(numeroRata);
					pagamento.setRata(rata);

					pagamenti.add(pagamento);
				}
				areaPagamentiNode.setPagamenti(pagamenti);
			}
		} catch (ObjectNotFoundException e) {
			logger.info("Area rate non trovata, la lascio a null");
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'area rate.", e);
			throw new RuntimeException("errore durante il caricamento dell'area rate.", e);
		}

		return areaPagamentiNode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Documento, String> getNextDocumentiCollegati(Integer idAreaDocumento) {
		Map<Documento, String> mapResult = new HashMap<Documento, String>();

		StringBuilder sb = new StringBuilder();
		sb.append("select distinct docAcc.id ");
		sb.append("from Pagamento pag inner join pag.areaChiusure ach inner join pag.areaAcconto aacc inner join aacc.documento docAcc ");
		sb.append("where ach.id = :paramAreaChiusureId");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramAreaChiusureId", idAreaDocumento);

		List<Integer> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dei documenti di acconto collegati", e);
		}

		if (result != null) {
			if (AbstractAreaDocumentoBuilderBean.MAX_DOCUMENT_SIZE_NODE < result.size()) {
				Documento documento = AbstractAreaDocumentoBuilderBean.createGruppoDocumento(result.size(),
						"Documenti di pagamento");
				mapResult.put(documento, "acconto");
			} else {
				for (Integer idDocumento : result) {
					Documento documentoResult = new Documento();
					documentoResult.setId(idDocumento);

					mapResult.put(documentoResult, "acconto");
				}
			}
		}
		return mapResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Documento, String> getPreviousDocumentiCollegati(Integer idAreaDocumento) {
		Map<Documento, String> mapResult = new HashMap<Documento, String>();

		StringBuilder sb = new StringBuilder();
		sb.append("select distinct docAr.id ");
		sb.append("from Pagamento pag inner join pag.areaChiusure ach inner join pag.rata rata inner join rata.areaRate ar inner join ar.documento docAr ");
		sb.append("where ach.id = :paramIdDocumento");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdDocumento", idAreaDocumento);

		List<Integer> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dei documenti di pagamento collegati", e);
		}

		if (result != null) {
			if (AbstractAreaDocumentoBuilderBean.MAX_DOCUMENT_SIZE_NODE < result.size()) {
				Documento documento = AbstractAreaDocumentoBuilderBean.createGruppoDocumento(result.size(),
						"Documenti di pagamento");
				mapResult.put(documento, "pagamento");
			} else {
				for (Integer idDocumento : result) {
					Documento documento = new Documento();
					documento.setId(idDocumento);

					mapResult.put(documento, "pagamento");
				}
			}
		}

		return mapResult;
	}

}
