package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.onroad.domain.wrapper.AssortimentoSedeCessionarioOnRoad;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.OnRoadCONCESExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadCONCESExporter")
public class OnRoadCONCESExporter extends OnRoadAssortimenoCessionariExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.OnRoadCONCESExporter";

	@Override
	public void esporta() throws FileCreationException {
		List<AssortimentoSedeCessionarioOnRoad> assortimentiSede = caricaAssortimentiSedi();

		Map<String, AssortimentoSedeCessionarioOnRoad> assortimenti = new HashMap<String, AssortimentoSedeCessionarioOnRoad>();
		for (AssortimentoSedeCessionarioOnRoad assort : assortimentiSede) {
			String key = assort.getCodiceDestinatario() + "#" + assort.getCodiceAssortimento();
			assortimenti.put(key, assort);
		}

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("conces", getFileForExport());

		for (AssortimentoSedeCessionarioOnRoad assortimentoClienteCessionario : assortimenti.values()) {
			out.write(assortimentoClienteCessionario);
		}
		out.flush();
		out.close();
	}
}
