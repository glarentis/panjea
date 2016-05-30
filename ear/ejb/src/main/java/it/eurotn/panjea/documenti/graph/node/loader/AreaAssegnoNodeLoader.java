package it.eurotn.panjea.documenti.graph.node.loader;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.node.AreaAssegnoNode;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.loader.interfaces.AreaDocumentoNodeLoader;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaDocumentoNodeLoaderAS")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaDocumentoNodeLoaderAS")
public class AreaAssegnoNodeLoader implements AreaDocumentoNodeLoader {

	private static Logger logger = Logger.getLogger(AreaAssegnoNodeLoader.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public AreaDocumentoNode caricaAreaDocumentoNode(Integer idDocumento) {

		StringBuilder sb = new StringBuilder();
		sb.append("select aa.abi as abi, ");
		sb.append("			aa.cab as cab, ");
		sb.append("			aa.numeroAssegno as numeroAssegno ");
		sb.append("from AreaAssegno aa inner join aa.documento doc ");
		sb.append("where doc.id = :paramIdDocumento ");

		AreaAssegnoNode areaAssegnoNode = null;

		try {
			Query query = panjeaDAO.prepareQuery(sb.toString());
			((QueryImpl) query).getHibernateQuery().setResultTransformer(
					Transformers.aliasToBean((AreaAssegnoNode.class)));

			query.setParameter("paramIdDocumento", idDocumento);

			areaAssegnoNode = (AreaAssegnoNode) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.info("Area assegno non trovata, la lascio a null");
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'area assegno.", e);
			throw new RuntimeException("errore durante il caricamento dell'area assegno.", e);
		}

		return areaAssegnoNode;
	}

	@Override
	public Map<Documento, String> getNextDocumentiCollegati(Integer idAreaDocumento) {
		return new HashMap<Documento, String>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Documento, String> getPreviousDocumentiCollegati(Integer idAreaDocumento) {
		Map<Documento, String> mapResult = new HashMap<Documento, String>();

		StringBuilder sb = new StringBuilder();
		sb.append("select docAr.id ");
		sb.append("from Pagamento pag inner join pag.areaChiusure ach inner join ach.documento docCh inner join pag.rata rata inner join rata.areaRate ar inner join ar.documento docAr ");
		sb.append("where ach.id = :paramAreaAssegnoId");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramAreaAssegnoId", idAreaDocumento);

		List<Integer> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dei documenti di pagamento collegati", e);
		}

		if (result != null) {
			for (Integer idDocumento : result) {
				Documento documentoResult = new Documento();
				documentoResult.setId(idDocumento);

				mapResult.put(documentoResult, "utilizzo assegno");
			}
		}

		return mapResult;
	}
}
