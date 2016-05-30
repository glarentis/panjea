package it.eurotn.panjea.aton.exporter;

import it.eurotn.panjea.aton.domain.wrapper.SituazioneRataAton;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.util.List;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.PARTITExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PARTITExporter")
public class PARTITExporter extends AbstractDataExporter implements DataExporter {
	public static final String BEAN_NAME = "Panjea.PARTITExporter";

	@EJB
	private RateManager rateManager;

	@Override
	public void esporta() throws FileCreationException {
		ParametriRicercaRate parametri = new ParametriRicercaRate();
		parametri.setStatiRata(new TreeSet<StatoRata>());
		parametri.getStatiRata().addAll(
				java.util.Arrays.asList(new StatoRata[] { StatoRata.APERTA, StatoRata.PAGAMENTO_PARZIALE,
						StatoRata.RIEMESSA, StatoRata.IN_RIASSEGNAZIONE }));
		List<SituazioneRata> situazioneRate = rateManager.ricercaRate(parametri);

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("situazioneRata", getFileForExport());

		for (SituazioneRata situazioneRata : situazioneRate) {
			out.write(new SituazioneRataAton(situazioneRata));
		}
		out.flush();
		out.close();
	}
}
