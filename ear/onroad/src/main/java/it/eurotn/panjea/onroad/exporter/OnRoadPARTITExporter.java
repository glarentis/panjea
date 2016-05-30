package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.onroad.domain.wrapper.SituazioneRataOnRoad;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.OnRoadPARTITExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadPARTITExporter")
public class OnRoadPARTITExporter extends AbstractDataExporter implements DataExporter {
	public static final String BEAN_NAME = "Panjea.OnRoadPARTITExporter";

	@EJB
	private RateManager rateManager;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@Override
	public void esporta() throws FileCreationException {
		ParametriRicercaRate parametri = new ParametriRicercaRate();
		parametri.setStatiRata(new TreeSet<StatoRata>());
		parametri.getStatiRata().addAll(
				Arrays.asList(new StatoRata[] { StatoRata.APERTA, StatoRata.PAGAMENTO_PARZIALE }));
		List<SituazioneRata> situazioneRate = rateManager.ricercaRate(parametri);

		List<TipoAreaPartita> tipiAreaPartita = tipiAreaPartitaManager
				.caricaTipiAreaPartitaGenerazioneRate(TipoPartita.ATTIVA);

		Map<Integer, TipoDocumento> tipi = new HashMap<Integer, TipoDocumento>();
		for (TipoAreaPartita tipoAreaPartita : tipiAreaPartita) {
			tipi.put(tipoAreaPartita.getTipoDocumento().getId(), tipoAreaPartita.getTipoDocumento());
		}

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("situazioneRata", getFileForExport());

		for (SituazioneRata situazioneRata : situazioneRate) {
			SituazioneRataOnRoad situazioneOnRoad = new SituazioneRataOnRoad(situazioneRata);
			situazioneOnRoad.getDocumento().setTipoDocumento(
					tipi.get(situazioneOnRoad.getDocumento().getTipoDocumento().getId()));
			out.write(situazioneOnRoad);
		}
		out.flush();
		out.close();
	}
}
