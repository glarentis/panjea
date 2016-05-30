package it.eurotn.panjea.documenti.graph.service;

import it.eurotn.panjea.documenti.graph.builder.interfaces.NodeBuilder;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.service.interfaces.DocumentiGraphService;
import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.DocumentiGraphService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.DocumentiGraphService")
public class DocumentiGraphServiceBean implements DocumentiGraphService {

	@EJB
	private NodeBuilder nodeBuilder;

	@Override
	public DocumentoNode createNode(DocumentoGraph documentoGraph, boolean loadAllNodes) {
		return nodeBuilder.createNode(documentoGraph, loadAllNodes);
	}

}
