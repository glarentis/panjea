package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.onroad.domain.ChiaveCondizRiga;
import it.eurotn.panjea.onroad.domain.DatiEsportazioneContratti;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.OnRoadCHIAVIExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadCHIAVIExporter")
public class OnRoadCHIAVIExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.OnRoadCHIAVIExporter";

	@Override
	public void esporta() throws FileCreationException {
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("chiavi", getFileForExport());

		List<ChiaveCondizRiga> chiavi = DatiEsportazioneContratti.getChiavi();
		Set<ChiaveCondizRiga> keys = new LinkedHashSet<ChiaveCondizRiga>();
		for (ChiaveCondizRiga chiave : chiavi) {
			keys.add(chiave);
		}
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(DatiEsportazioneContratti.TIPORECORD_LISTINO);

		chiaveCondizRiga.setDescrizione("ListinoCliente");
		chiaveCondizRiga.setNumeroCampi(2);
		chiaveCondizRiga.setSequenzaRicerca(DatiEsportazioneContratti.TIPORECORD_LISTINO);

		chiaveCondizRiga.setNomeTabella1("CLIENT");
		chiaveCondizRiga.setNomeCampo1("CodiceListino");

		chiaveCondizRiga.setNomeTabella2("ARTICO");
		chiaveCondizRiga.setNomeCampo2("CodiceArticolo");

		chiaveCondizRiga.setGotoT(null);
		chiaveCondizRiga.setGotoF(null);

		keys.add(chiaveCondizRiga);
		for (ChiaveCondizRiga chiave : keys) {
			if (chiave != null) {
				out.write(chiave);
			}
		}

		out.flush();
		out.close();
	}

}
