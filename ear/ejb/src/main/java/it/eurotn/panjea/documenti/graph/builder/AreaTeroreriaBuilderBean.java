package it.eurotn.panjea.documenti.graph.builder;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.AreaTesoreriaNode;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.node.loader.interfaces.AreaDocumentoNodeLoader;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaTeroreriaBuilderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaTeroreriaBuilderBean")
public class AreaTeroreriaBuilderBean extends AbstractAreaDocumentoBuilderBean {

	private static Logger logger = Logger.getLogger(AreaTeroreriaBuilderBean.class);

	@EJB
	private AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@Override
	public AreaDocumentoNode createNode(DocumentoNode node) {

		AreaTesoreriaNode areaTesoreriaNode = null;

		Query query = panjeaDAO
				.getEntityManager()
				.createNativeQuery(
						"select TIPO_PARTITA from part_area_partite where documento_id = :paramIdDocumento and TIPO_PARTITA <> 'RA'");
		query.setParameter("paramIdDocumento", node.getIdDocumento());

		String tipoArea = null;
		try {
			tipoArea = (String) panjeaDAO.getSingleResult(query);

			AreaDocumentoNodeLoader areaDocumentoNodeLoader = (AreaDocumentoNodeLoader) context
					.lookup(AreaDocumentoNodeLoader.LOADER_JNDI_NAME + tipoArea);

			if (areaDocumentoNodeLoader != null) {
				areaTesoreriaNode = (AreaTesoreriaNode) areaDocumentoNodeLoader.caricaAreaDocumentoNode(node
						.getIdDocumento());
			} else {
				areaTesoreriaNode = new AreaTesoreriaNode(tipoArea);
			}
		} catch (ObjectNotFoundException e1) {
			logger.info("Area tesoreria non trovata, la lascio a null");
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dell'area tesoreria.", e);
			throw new RuntimeException("errore durante il caricamento dell'area tesoreria.", e);
		}

		return areaTesoreriaNode;
	}

	@Override
	public Map<Documento, String> getNextDocumentiCollegati(DocumentoNode documentoNode) {

		Map<Documento, String> mapResult = new HashMap<Documento, String>();

		Query query = panjeaDAO
				.getEntityManager()
				.createNativeQuery(
						"select TIPO_PARTITA,id from part_area_partite where documento_id = :paramIdDocumento and TIPO_PARTITA <> 'RA'");
		query.setParameter("paramIdDocumento", documentoNode.getIdDocumento());

		Object[] tipoArea = null;
		try {
			tipoArea = (Object[]) panjeaDAO.getSingleResult(query);

			AreaDocumentoNodeLoader areaDocumentoNodeLoader = (AreaDocumentoNodeLoader) context
					.lookup(AreaDocumentoNodeLoader.LOADER_JNDI_NAME + tipoArea[0]);

			if (areaDocumentoNodeLoader != null) {
				mapResult.putAll(areaDocumentoNodeLoader.getNextDocumentiCollegati((Integer) tipoArea[1]));
			}
		} catch (ObjectNotFoundException e1) {
			logger.info("Area tesoreria non trovata, la lascio a null");
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dell'area tesoreria.", e);
			throw new RuntimeException("errore durante il caricamento dell'area tesoreria.", e);
		}

		return mapResult;
	}

	@Override
	public Map<Documento, String> getPreviousDocumentiCollegati(DocumentoNode documentoNode) {

		Map<Documento, String> mapResult = new HashMap<Documento, String>();

		Query query = panjeaDAO
				.getEntityManager()
				.createNativeQuery(
						"select TIPO_PARTITA,id from part_area_partite where documento_id = :paramIdDocumento and TIPO_PARTITA <> 'RA'");
		query.setParameter("paramIdDocumento", documentoNode.getIdDocumento());

		Object[] tipoArea = null;
		try {
			tipoArea = (Object[]) panjeaDAO.getSingleResult(query);

			AreaDocumentoNodeLoader areaDocumentoNodeLoader = (AreaDocumentoNodeLoader) context
					.lookup(AreaDocumentoNodeLoader.LOADER_JNDI_NAME + tipoArea[0]);

			if (areaDocumentoNodeLoader != null) {
				mapResult.putAll(areaDocumentoNodeLoader.getPreviousDocumentiCollegati((Integer) tipoArea[1]));
			}
		} catch (ObjectNotFoundException e1) {
			logger.info("Area tesoreria non trovata, la lascio a null");
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dell'area tesoreria.", e);
			throw new RuntimeException("errore durante il caricamento dell'area tesoreria.", e);
		}

		return mapResult;
	}
}
