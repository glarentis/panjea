package it.eurotn.panjea.documenti.graph.node.loader;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.builder.AbstractAreaDocumentoBuilderBean;
import it.eurotn.panjea.documenti.graph.node.AreaAccreditoNode;
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
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaDocumentoNodeLoaderAC")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaDocumentoNodeLoaderAC")
public class AreaAccreditoNodeLoader implements AreaDocumentoNodeLoader {

	private static Logger logger = Logger.getLogger(AreaAccreditoNodeLoader.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public AreaDocumentoNode caricaAreaDocumentoNode(Integer idDocumento) {
		// Visto che il metodo viene chiamato dal builder dell'area tesoreria, sò che ho un'area accredito per il
		// documento passato ma siccome il node non visualizza nessuna proprietà dell'area, per prestazioni non la
		// carico nemmeno e restituisco il nodo creato
		return new AreaAccreditoNode();
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
		sb.append("select docad.id ");
		sb.append("from AreaDistintaBancaria ad inner join ad.documento docad inner join ad.areaEffettiCollegata aeff inner join aeff.effetti eff inner join eff.areaAccredito aacc ");
		sb.append("where aacc.id = :paramAreaAccreditoId");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramAreaAccreditoId", idAreaDocumento);

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
				mapResult.put(documento, "utilizzo accredito");
			} else {
				for (Integer idDocumento : result) {
					Documento documentoResult = new Documento();
					documentoResult.setId(idDocumento);

					mapResult.put(documentoResult, "utilizzo accredito");
				}
			}
		}

		return mapResult;
	}
}
