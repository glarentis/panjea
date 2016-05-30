package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.OnRoadUNIVENExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadUNIVENExporter")
public class OnRoadUNIVENExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.OnRoadUNIVENExporter";

	@EJB
	private AnagraficaTabelleManager anagraficaTabelleManager;

	@Override
	public void esporta() throws FileCreationException {
		List<UnitaMisura> result = anagraficaTabelleManager.caricaUnitaMisura();
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("um", getFileForExport());

		for (UnitaMisura um : result) {
			out.write(um);
		}
		out.flush();
		out.close();
	}

}
