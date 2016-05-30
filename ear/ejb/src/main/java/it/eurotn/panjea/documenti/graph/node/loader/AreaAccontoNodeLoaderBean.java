package it.eurotn.panjea.documenti.graph.node.loader;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.node.AreaAccontoNode;
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

@Stateless(mappedName = "Panjea.AreaDocumentoNodeLoaderAA")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaDocumentoNodeLoaderAA")
public class AreaAccontoNodeLoaderBean implements AreaDocumentoNodeLoader {

	private static Logger logger = Logger.getLogger(AreaAccontoNodeLoaderBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public AreaDocumentoNode caricaAreaDocumentoNode(Integer idDocumento) {

		StringBuilder sb = new StringBuilder();
		sb.append("select aa.importoUtilizzato as importoUtilizzato, ");
		sb.append("			aa.note as note ");
		sb.append("from AreaAcconto aa inner join aa.documento doc ");
		sb.append("where doc.id = :paramIdDocumento ");

		AreaAccontoNode areaAccontoNode = null;

		try {
			Query query = panjeaDAO.prepareQuery(sb.toString());
			((QueryImpl) query).getHibernateQuery().setResultTransformer(
					Transformers.aliasToBean((AreaAccontoNode.class)));

			query.setParameter("paramIdDocumento", idDocumento);

			areaAccontoNode = (AreaAccontoNode) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.info("Area acconto non trovata, la lascio a null");
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'area acconto.", e);
			throw new RuntimeException("errore durante il caricamento dell'area acconto.", e);
		}

		return areaAccontoNode;
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
		sb.append("select distinct docCh.id ");
		sb.append(" from Pagamento pag inner join pag.areaChiusure ac inner join ac.documento docCh inner join pag.areaAcconto aacc ");
		sb.append("where aacc.id = :paramAreaAccontoId ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramAreaAccontoId", idAreaDocumento);

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

				mapResult.put(documentoResult, "utilizzo acconto");
			}
		}

		return mapResult;
	}

}
