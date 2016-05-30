package it.eurotn.panjea.aton.exporter;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.AtonUNIVENExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AtonUNIVENExporter")
public class UNIVENExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.AtonUNIVENExporter";

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
