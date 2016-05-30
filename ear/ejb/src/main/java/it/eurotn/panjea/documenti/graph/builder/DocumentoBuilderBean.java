package it.eurotn.panjea.documenti.graph.builder;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.builder.interfaces.DocumentoBuilder;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.node.GroupDocumentoNode;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.DocumentoBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentoBuilder")
public class DocumentoBuilderBean implements DocumentoBuilder {

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	private static Logger logger = Logger.getLogger(DocumentoBuilderBean.class);

	@Override
	public DocumentoNode createNode(Documento documento) {
		logger.debug("--> Enter createNode");

		DocumentoNode node = null;

		if (documento.getId() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("select d.id as idDocumento, ");
			sb.append("d.codice.codice as codiceDocumento, ");
			sb.append("d.dataDocumento as dataDocumento, ");
			sb.append("td.descrizione as descrizioneTipoDocumento, ");
			sb.append("td.classeTipoDocumento as classeTipoDocumento, ");
			sb.append("d.totale.importoInValuta as importoInValuta,");
			sb.append("d.totale.codiceValuta as codiceValuta, ");
			sb.append("a.denominazione as descrizioneEntita, ");
			sb.append("e.codice as codiceEntita, ");
			sb.append("d.codiceAzienda as codiceAzienda, ");
			sb.append("rapp.descrizione as descrizioneRapportoBancario, ");
			sb.append("td.tipoEntita as tipoEntitaTipoDocumento, ");
			sb.append("td.colore as colore ");
			sb.append("from Documento d ");
			sb.append("inner join d.tipoDocumento td ");
			sb.append("left join d.entita e ");
			sb.append("left join e.anagrafica a ");
			sb.append("left join d.rapportoBancarioAzienda rapp ");
			sb.append("where d.id=:paramIdDocumento ");

			Query query = panjeaDAO.prepareQuery(sb.toString());
			((QueryImpl) query).getHibernateQuery().setResultTransformer(
					Transformers.aliasToBean((DocumentoNode.class)));

			query.setParameter("paramIdDocumento", documento.getId());

			node = (DocumentoNode) query.getSingleResult();
		} else {
			node = new GroupDocumentoNode(documento);
		}

		logger.debug("--> Exit createNode");
		return node;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getLinkDocumentiDestinazione(Integer idDocumento) {

		StringBuilder sb = new StringBuilder(
				"select ld.documentoDestinazione.id as id from LinkDocumento ld where  ld.documentoOrigine.id=:paramIdDocumento");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdDocumento", idDocumento);

		List<Integer> idDocumenti = new ArrayList<Integer>();
		try {
			idDocumenti = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei documenti collegati.", e);
			throw new RuntimeException("errore durante il caricamento dei documenti collegati.", e);
		}

		return idDocumenti;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getLinkDocumentiOrigine(Integer idDocumento) {

		StringBuilder sb = new StringBuilder();
		sb.append("select ld.documentoOrigine.id as id from LinkDocumento ld where  ld.documentoDestinazione.id=:paramIdDocumento");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdDocumento", idDocumento);

		List<Integer> idDocumenti = new ArrayList<Integer>();
		try {
			idDocumenti = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei documenti collegati.", e);
			throw new RuntimeException("errore durante il caricamento dei documenti collegati.", e);
		}

		return idDocumenti;
	}
}
