package it.eurotn.panjea.documenti.graph.builder;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.builder.interfaces.AreaDocumentoBuilder;
import it.eurotn.panjea.documenti.graph.builder.interfaces.DocumentoBuilder;
import it.eurotn.panjea.documenti.graph.builder.interfaces.NodeBuilder;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.NodeBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.NodeBuilder")
public class NodeBuilderBean implements NodeBuilder {

	@EJB(beanName = "DocumentoBuilderBean")
	private DocumentoBuilder documentoBuilder;

	@EJB(beanName = "AreaMagazzinoBuilderBean")
	private AreaDocumentoBuilder areaMagazzinoBuilder;

	@EJB(beanName = "AreaOrdineBuilderBean")
	private AreaDocumentoBuilder areaOrdineBuilder;

	@EJB(beanName = "AreaRateBuilderBean")
	private AreaDocumentoBuilder areaRateBuilder;

	@EJB(beanName = "AreaTeroreriaBuilderBean")
	private AreaDocumentoBuilder areaTesoreriaBuilder;

	@EJB(beanName = "AreaPreventivoBuilderBean")
	private AreaDocumentoBuilder areaPreventivoBuilder;

	private static Logger logger = Logger.getLogger(NodeBuilderBean.class);

	/**
	 * Crea il nodo che contiene tutte le proprietà del documento e delle sue aree.
	 *
	 * @param idDocumento
	 *            id del documento
	 * @param nodesMediator
	 *            nodesMediator
	 * @return nodo creato
	 */
	private DocumentoNode createAllDocumentoNode(Documento documento, HashMap<Integer, DocumentoNode> nodesMediator) {

		// carico le proprietà del documento
		DocumentoNode node = documentoBuilder.createNode(documento);
		node = createLinkNodes(node);

		nodesMediator.put(node.getIdDocumento(), node);

		Map<Documento, String> nextDocumenti = new HashMap<Documento, String>();
		Map<Documento, String> previousDocumenti = new HashMap<Documento, String>();

		for (AreaDocumentoBuilder builder : getBuilders()) {
			AreaDocumentoNode nodeArea = builder.createNode(node);
			if (nodeArea != null) {
				node.getAreeDocumentoNode().add(nodeArea);
				nextDocumenti.putAll(builder.getNextDocumentiCollegati(node));
				previousDocumenti.putAll(builder.getPreviousDocumentiCollegati(node));
			}
		}

		for (Entry<Documento, String> entry : nextDocumenti.entrySet()) {

			DocumentoNode nodeNext = nodesMediator.get(entry.getKey().getId());
			if (nodeNext == null) {
				nodeNext = createAllDocumentoNode(entry.getKey(), nodesMediator);
			}
			node.getNextNodes().put(nodeNext, entry.getValue());
		}

		for (Entry<Documento, String> entry : previousDocumenti.entrySet()) {

			DocumentoNode nodePrev = nodesMediator.get(entry.getKey().getId());
			if (nodePrev == null) {
				nodePrev = createAllDocumentoNode(entry.getKey(), nodesMediator);
			}
			node.getPreviousNodes().put(nodePrev, entry.getValue());
		}

		return node;
	}

	/**
	 * Crea il nodo e ricorsivamente tutti i nodi successivi.
	 *
	 * @param idDocumento
	 *            nodo documento da creare
	 * @param nodesMediator
	 *            nodesMediator
	 * @return nodo creato
	 */
	private DocumentoNode createAllNextDocumentoNode(Documento documento, HashMap<Integer, DocumentoNode> nodesMediator) {
		// carico le proprietà del documento
		DocumentoNode node = documentoBuilder.createNode(documento);

		nodesMediator.put(node.getIdDocumento(), node);

		Map<Documento, String> nextDocumenti = new HashMap<Documento, String>();

		for (AreaDocumentoBuilder builder : getBuilders()) {
			AreaDocumentoNode nodeArea = builder.createNode(node);
			if (nodeArea != null) {
				node.getAreeDocumentoNode().add(nodeArea);
				nextDocumenti.putAll(builder.getNextDocumentiCollegati(node));
			}
		}

		for (Entry<Documento, String> entry : nextDocumenti.entrySet()) {

			DocumentoNode nodeNext = nodesMediator.get(entry.getKey().getId());
			if (nodeNext == null) {
				nodeNext = createAllNextDocumentoNode(entry.getKey(), nodesMediator);
			}
			node.getNextNodes().put(nodeNext, entry.getValue());
		}

		return node;
	}

	/**
	 * Crea il nodo e ricorsivamente tutti i nodi precedenti.
	 *
	 * @param idDocumento
	 *            nodo documento da creare
	 * @param nodesMediator
	 *            nodesMediator
	 * @return nodo creato
	 */
	private DocumentoNode createAllPreviousDocumentoNode(Documento documento,
			HashMap<Integer, DocumentoNode> nodesMediator) {
		// carico le proprietà del documento
		DocumentoNode node = documentoBuilder.createNode(documento);

		nodesMediator.put(node.getIdDocumento(), node);

		Map<Documento, String> previousDocumenti = new HashMap<Documento, String>();

		for (AreaDocumentoBuilder builder : getBuilders()) {
			AreaDocumentoNode nodeArea = builder.createNode(node);
			if (nodeArea != null) {
				node.getAreeDocumentoNode().add(nodeArea);
				previousDocumenti.putAll(builder.getPreviousDocumentiCollegati(node));
			}
		}

		for (Entry<Documento, String> entry : previousDocumenti.entrySet()) {

			DocumentoNode nodePrev = nodesMediator.get(entry.getKey().getId());
			if (nodePrev == null) {
				nodePrev = createAllPreviousDocumentoNode(entry.getKey(), nodesMediator);
			}
			node.getPreviousNodes().put(nodePrev, entry.getValue());
		}

		return node;
	}

	/**
	 * Crea il nodo del documento creando ricorsivamente tutiti nodi successivi e precedenti.
	 *
	 * @param idDocumento
	 *            nodo documento da creare
	 * @param nodesMediator
	 *            nodesMediator
	 * @return nodo creato
	 */
	private DocumentoNode createDocumentoNode(Documento documento, HashMap<Integer, DocumentoNode> nodesMediator) {

		// carico le proprietà del documento
		DocumentoNode node = documentoBuilder.createNode(documento);
		// aggiungo i documenti collegati
		node = createLinkNodes(node);

		nodesMediator.put(node.getIdDocumento(), node);

		Map<Documento, String> nextDocumenti = new HashMap<Documento, String>();
		Map<Documento, String> previousDocumenti = new HashMap<Documento, String>();

		for (AreaDocumentoBuilder builder : getBuilders()) {
			AreaDocumentoNode nodeArea = builder.createNode(node);
			if (nodeArea != null) {
				node.getAreeDocumentoNode().add(nodeArea);
				nextDocumenti.putAll(builder.getNextDocumentiCollegati(node));
				previousDocumenti.putAll(builder.getPreviousDocumentiCollegati(node));
			}
		}

		for (Entry<Documento, String> entry : nextDocumenti.entrySet()) {

			DocumentoNode nodeNext = nodesMediator.get(entry.getKey().getId());
			if (nodeNext == null) {
				nodeNext = createAllNextDocumentoNode(entry.getKey(), nodesMediator);
			}
			node.getNextNodes().put(nodeNext, entry.getValue());
		}

		for (Entry<Documento, String> entry : previousDocumenti.entrySet()) {

			DocumentoNode nodePrev = nodesMediator.get(entry.getKey().getId());
			if (nodePrev == null) {
				nodePrev = createAllPreviousDocumentoNode(entry.getKey(), nodesMediator);
			}
			node.getPreviousNodes().put(nodePrev, entry.getValue());
		}

		return node;
	}

	/**
	 * Crea i nodi per i documenti collegati.
	 *
	 * @param documentoNode
	 *            nodo di riferimento
	 * @return nodo aggiornato con i documenti collegati
	 */
	private DocumentoNode createLinkNodes(DocumentoNode documentoNode) {

		List<Integer> linkDocumenti = documentoBuilder.getLinkDocumentiDestinazione(documentoNode.getIdDocumento());
		for (Integer id : linkDocumenti) {
			// carico le proprietà del documento
			Documento documento = new Documento();
			documento.setId(id);
			DocumentoNode node = documentoBuilder.createNode(documento);

			for (AreaDocumentoBuilder builder : getBuilders()) {
				AreaDocumentoNode nodeArea = builder.createNode(node);
				if (nodeArea != null) {
					node.getAreeDocumentoNode().add(nodeArea);
				}
			}
			documentoNode.getLinkNodesDestinazione().add(node);
		}

		// carico i documenti di origine linkati
		linkDocumenti = documentoBuilder.getLinkDocumentiOrigine(documentoNode.getIdDocumento());
		for (Integer id : linkDocumenti) {
			// carico le proprietà del documento
			Documento documento = new Documento();
			documento.setId(id);
			DocumentoNode node = documentoBuilder.createNode(documento);
			// carico le proprietà dell'area magazzino
			for (AreaDocumentoBuilder builder : getBuilders()) {
				AreaDocumentoNode nodeArea = builder.createNode(node);
				if (nodeArea != null) {
					node.getAreeDocumentoNode().add(nodeArea);
				}
			}
			documentoNode.getLinkNodesOrigine().add(node);
		}

		return documentoNode;
	}

	@Override
	public DocumentoNode createNode(DocumentoGraph documentoGraph, boolean loadAllNodes) {
		logger.debug("--> Enter createNode");

		// creo il nodo del documento
		DocumentoNode node = null;

		if (!loadAllNodes) {
			node = createDocumentoNode(documentoGraph.getDocumento(), new HashMap<Integer, DocumentoNode>());
		} else {
			node = createAllDocumentoNode(documentoGraph.getDocumento(), new HashMap<Integer, DocumentoNode>());
		}

		return node;
	}

	/**
	 * @return the builders
	 */
	private AreaDocumentoBuilder[] getBuilders() {
		return new AreaDocumentoBuilder[] { areaMagazzinoBuilder, areaOrdineBuilder, areaPreventivoBuilder,
				areaRateBuilder, areaTesoreriaBuilder };
	}
}
