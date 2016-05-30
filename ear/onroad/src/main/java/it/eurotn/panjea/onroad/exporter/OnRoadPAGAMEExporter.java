package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.CodicePagamentoManager;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.OnRoadPAGAMEExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadPAGAMEExporter")
public class OnRoadPAGAMEExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.OnRoadPAGAMEExporter";

	@EJB
	private CodicePagamentoManager codicePagamentoManager;

	@Override
	public void esporta() throws FileCreationException {

		List<CodicePagamento> result = codicePagamentoManager.caricaCodiciPagamento(null,
				TipoRicercaCodicePagamento.TUTTO, false);

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("codPag", getFileForExport());

		for (CodicePagamento codicePagamento : result) {
			out.write(codicePagamento);
		}
		out.flush();
		out.close();
	}
}
