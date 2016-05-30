package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.OnRoadUTENTIExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadUTENTIExporter")
public class OnRoadUTENTIExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.OnRoadUTENTIExporter";

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void esporta() throws FileCreationException {

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("utenti", getFileForExport());

		@SuppressWarnings("unchecked")
		List<AgenteLite> agenti = panjeaDAO.prepareQuery("select a from AgenteLite a").getResultList();
		for (AgenteLite agente : agenti) {
			out.write(agente);
		}
		out.flush();
		out.close();
	}
}
