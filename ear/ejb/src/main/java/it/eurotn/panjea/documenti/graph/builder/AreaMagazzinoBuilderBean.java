package it.eurotn.panjea.documenti.graph.builder;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.AreaMagazzinoNode;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
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

@Stateless(mappedName = "Panjea.AreaMagazzinoBuilderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaMagazzinoBuilderBean")
public class AreaMagazzinoBuilderBean extends AbstractAreaDocumentoBuilderBean {

	private static Logger logger = Logger.getLogger(AreaMagazzinoBuilderBean.class);

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public AreaDocumentoNode createNode(DocumentoNode node) {

		StringBuilder sb = new StringBuilder();
		sb.append("select am.dataRegistrazione as dataRegistrazione, ");
		sb.append("se.codice as codiceSedeEntita, ");
		sb.append("sa.indirizzo as indirizzoSedeEntita, ");
		sb.append("sa.descrizione as descrizioneSedeEntita, ");
		sb.append("sa.datiGeografici.localita.descrizione as localitaSedeEntita, ");
		sb.append("am.statoAreaMagazzino as statoAreaMagazzino ");
		sb.append("from AreaMagazzino am inner join am.documento doc left join doc.sedeEntita se left join se.sede sa ");
		sb.append("where doc.id = :paramIdDocumento ");

		AreaMagazzinoNode areaMagazzinoNode = null;

		try {
			Query query = panjeaDAO.prepareQuery(sb.toString());
			((QueryImpl) query).getHibernateQuery().setResultTransformer(
					Transformers.aliasToBean((AreaMagazzinoNode.class)));

			query.setParameter("paramIdDocumento", node.getIdDocumento());

			areaMagazzinoNode = (AreaMagazzinoNode) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.info("Area magazzino non trovata, la lascio a null");
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'area magazzino.", e);
			throw new RuntimeException("errore durante il caricamento dell'area magazzino.", e);
		}

		return areaMagazzinoNode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Documento, String> getNextDocumentiCollegati(DocumentoNode documentoNode) {

		// documenti provenienti da aree magazzino collegate
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct doc.id as idDocumento ");
		sb.append("from RigaArticolo ra  inner join ra.areaMagazzino.documento doc ");
		sb.append("where ra.areaMagazzinoCollegata.documento.id = :paramIdDocumento ");

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
				Documento documento = createGruppoDocumento(result.size(), "Documenti di magazzino");
				mapResult.put(documento, "fatturazione");
			} else {
				for (Integer idDocumento : result) {
					Documento documento = new Documento();
					documento.setId(idDocumento);

					mapResult.put(documento, "fatturazione");
				}
			}
		}

		// documenti provenienti da pagamenti effettuati
		sb = new StringBuilder();
		sb.append("select distinct docCh.id ");
		sb.append("from Pagamento pag inner join pag.areaChiusure ach inner join ach.documento docCh inner join pag.rata rata inner join rata.areaRate ar inner join ar.documento docAr ");
		sb.append("where docAr.id = :paramIdDocumento");

		query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdDocumento", documentoNode.getIdDocumento());

		result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dei documenti di pagamento collegati", e);
		}

		if (result != null) {
			if (MAX_DOCUMENT_SIZE_NODE < result.size()) {
				Documento documento = createGruppoDocumento(result.size(), "Documenti di pagamento");
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

	@SuppressWarnings("unchecked")
	@Override
	public Map<Documento, String> getPreviousDocumentiCollegati(DocumentoNode documentoNode) {

		// documenti provenienti da ordini
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct docAo.id ");
		sb.append("from RigaArticolo ra inner join ra.areaMagazzino am inner join am.documento doc inner join ra.areaOrdineCollegata ao inner join ao.documento docAo ");
		sb.append("where doc.id = :paramIdDocumento");

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
				mapResult.put(documento, "evasione");
			} else {
				for (Integer idDocumento : result) {
					Documento documento = new Documento();
					documento.setId(idDocumento);

					mapResult.put(documento, "evasione");
				}
			}
		}

		// documenti provenienti da fatturazioni
		sb = new StringBuilder();
		sb.append("select distinct docAMC.id ");
		sb.append("from RigaArticolo ra inner join ra.areaMagazzino am inner join am.documento doc inner join ra.areaMagazzinoCollegata amc inner join amc.documento docAMC ");
		sb.append("where doc.id = :paramIdDocumento");

		query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdDocumento", documentoNode.getIdDocumento());

		result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dei documenti collegati", e);
		}

		if (result != null) {
			if (MAX_DOCUMENT_SIZE_NODE < result.size()) {
				Documento documento = createGruppoDocumento(result.size(), "Documenti di magazzino");
				mapResult.put(documento, "fatturazione");
			} else {
				for (Integer idDocumento : result) {
					Documento documento = new Documento();
					documento.setId(idDocumento);

					mapResult.put(documento, "fatturazione");
				}
			}
		}

		return mapResult;
	}

}
