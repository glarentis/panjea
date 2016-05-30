package it.eurotn.panjea.documenti.graph.node.loader;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.builder.AbstractAreaDocumentoBuilderBean;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.AreaEffettiNode;
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
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaDocumentoNodeLoaderEF")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaDocumentoNodeLoaderEF")
public class AreaEffettiNodeLoader implements AreaDocumentoNodeLoader {

	private static Logger logger = Logger.getLogger(AreaEffettiNodeLoader.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public AreaDocumentoNode caricaAreaDocumentoNode(Integer idDocumento) {
		// Visto che il metodo viene chiamato dal builder dell'area tesoreria, sò che ho un'area effetti per il
		// documento passato ma siccome il node non visualizza nessuna proprietà dell'area, per prestazioni non la
		// carico nemmeno e restituisco il nodo creato
		return new AreaEffettiNode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Documento, String> getNextDocumentiCollegati(Integer idAreaDocumento) {

		Map<Documento, String> mapResult = new HashMap<Documento, String>();

		StringBuilder sb = new StringBuilder();
		sb.append("select distinct docad.id ");
		sb.append("from AreaDistintaBancaria ad inner join ad.documento docad inner join ad.areaEffettiCollegata aeff ");
		sb.append("where aeff.id = :paramAreaEffettiId");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramAreaEffettiId", idAreaDocumento);

		List<Integer> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dei documenti di accredito collegati", e);
		}

		if (result != null) {
			if (AbstractAreaDocumentoBuilderBean.MAX_DOCUMENT_SIZE_NODE < result.size()) {
				Documento documento = AbstractAreaDocumentoBuilderBean.createGruppoDocumento(result.size(),
						"Documenti di pagamento");
				mapResult.put(documento, "presentazione");
			} else {
				for (Integer idDocumento : result) {
					Documento documentoResult = new Documento();
					documentoResult.setId(idDocumento);

					mapResult.put(documentoResult, "presentazione");
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
