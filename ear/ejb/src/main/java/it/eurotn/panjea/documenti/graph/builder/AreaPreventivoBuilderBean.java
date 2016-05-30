package it.eurotn.panjea.documenti.graph.builder;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.AreaPreventivoNode;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
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

@Stateless(mappedName = "Panjea.AreaPreventivoBuilderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaPreventivoBuilderBean")
public class AreaPreventivoBuilderBean extends AbstractAreaDocumentoBuilderBean {

	private static Logger logger = Logger.getLogger(AreaPreventivoBuilderBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public AreaDocumentoNode createNode(DocumentoNode node) {

		StringBuilder sb = new StringBuilder();
		sb.append("select ap.dataRegistrazione as dataRegistrazionePreventivo, ");
		sb.append("se.codice as codiceSedeEntita, ");
		sb.append("sa.indirizzo as indirizzoSedeEntita, ");
		sb.append("sa.descrizione as descrizioneSedeEntita, ");
		sb.append("sa.datiGeografici.localita.descrizione as localitaSedeEntita, ");
		sb.append("ap.statoAreaPreventivo as statoAreaPreventivo ");
		sb.append("from AreaPreventivo ap inner join ap.documento doc left join doc.sedeEntita se left join se.sede sa ");
		sb.append("where doc.id = :paramIdDocumento ");

		AreaPreventivoNode areaPreventivoNode = null;

		try {
			Query query = panjeaDAO.prepareQuery(sb.toString());
			((QueryImpl) query).getHibernateQuery().setResultTransformer(
					Transformers.aliasToBean((AreaPreventivoNode.class)));

			query.setParameter("paramIdDocumento", node.getIdDocumento());

			areaPreventivoNode = (AreaPreventivoNode) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.info("Area preventivo non trovata, la lascio a null");
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'area preventivo.", e);
			throw new RuntimeException("errore durante il caricamento dell'area preventivo.", e);
		}

		return areaPreventivoNode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Documento, String> getNextDocumentiCollegati(DocumentoNode documentoNode) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct doc.id as idDocumento ");
		sb.append("from RigaArticoloOrdine ra  inner join ra.areaOrdine.documento doc ");
		sb.append("where ra.areaPreventivoCollegata.documento.id = :paramIdDocumento ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdDocumento", documentoNode.getIdDocumento());

		List<Integer> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dei documenti collegati", e);
		}

		Map<Documento, String> mapResult = new HashMap<Documento, String>();
		if (result != null) {
			if (MAX_DOCUMENT_SIZE_NODE < result.size()) {
				Documento documento = createGruppoDocumento(result.size(), "Ordini");
				mapResult.put(documento, "generazione");
			} else {
				for (Integer idDocumento : result) {
					Documento documento = new Documento();
					documento.setId(idDocumento);

					mapResult.put(documento, "generazione");
				}
			}
		}

		return mapResult;
	}

	@Override
	public Map<Documento, String> getPreviousDocumentiCollegati(DocumentoNode documentoNode) {
		return new HashMap<Documento, String>();
	}

}
