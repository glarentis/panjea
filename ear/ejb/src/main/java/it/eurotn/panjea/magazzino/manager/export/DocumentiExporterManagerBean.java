package it.eurotn.panjea.magazzino.manager.export;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.exporter.interfaces.RendicontazioneExporter;
import it.eurotn.panjea.magazzino.manager.export.interfaces.DocumentiExporterManager;
import it.eurotn.panjea.magazzino.service.exception.DocumentiExporterException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.DocumentiExporterManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentiExporterManager")
public class DocumentiExporterManagerBean implements DocumentiExporterManager {

	@Resource
	private SessionContext context;

	@Override
	public List<byte[]> esportaDocumenti(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) throws DocumentiExporterException {

		List<byte[]> result;

		RendicontazioneExporter rendicontazioneExporter = (RendicontazioneExporter) context.lookup(tipoEsportazione
				.getJndiName());

		result = rendicontazioneExporter.export(areeMagazzino, tipoEsportazione, parametri);

		return result;
	}
}
