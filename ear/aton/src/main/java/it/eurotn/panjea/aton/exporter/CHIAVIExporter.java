package it.eurotn.panjea.aton.exporter;

import it.eurotn.panjea.aton.domain.ChiaveCondizRiga;
import it.eurotn.panjea.aton.domain.DatiEsportazioneContratti;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.CHIAVIExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CHIAVIExporter")
public class CHIAVIExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.CHIAVIExporter";

	@Override
	public void esporta() throws FileCreationException {
		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("chiavi", getFileForExport());

		List<ChiaveCondizRiga> chiavi = DatiEsportazioneContratti.getChiavi();

		// li passo in un set per eliminare i duplicati
		Set<ChiaveCondizRiga> keys = new LinkedHashSet<ChiaveCondizRiga>();
		for (ChiaveCondizRiga chiave : chiavi) {
			keys.add(chiave);
		}

		// chiave per listino
		ChiaveCondizRiga chiaveCondizRigaListino = new ChiaveCondizRiga();
		chiaveCondizRigaListino.setCodiceRicerca(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRigaListino.setDescrizione("ListinoCliente");
		chiaveCondizRigaListino.setNumeroCampi(2);
		chiaveCondizRigaListino.setSequenzaRicerca(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRigaListino.setNomeTabella1("T_CLIENTI");
		chiaveCondizRigaListino.setNomeCampo1("CodiceListino");
		chiaveCondizRigaListino.setNomeTabella2("T_ARTICOL");
		chiaveCondizRigaListino.setNomeCampo2("CodiceArticolo");
		chiaveCondizRigaListino.setGotoT(null);
		chiaveCondizRigaListino.setGotoF(null);

		keys.add(chiaveCondizRigaListino);

		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setArchivio("T_UNIVEN");
		chiaveCondizRiga.setCodiceRicerca("00");
		chiaveCondizRiga.setDescrizione("UNITA DI VENDITA");
		chiaveCondizRiga.setNumeroCampi(1);
		chiaveCondizRiga.setSequenzaRicerca("00");
		chiaveCondizRiga.setNomeTabella1("T_ARTICOL");
		chiaveCondizRiga.setNomeCampo1("GRUPPOUNITAVENDITA");

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
