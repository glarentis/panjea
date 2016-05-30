package it.eurotn.panjea.magazzino.service;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.interfaces.DocumentiExporterManager;
import it.eurotn.panjea.magazzino.service.exception.DocumentiExporterException;
import it.eurotn.panjea.magazzino.service.interfaces.DocumentiExporterService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.DocumentiExporterService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.DocumentiExporterService")
public class DocumentiExporterServiceBean implements DocumentiExporterService {

	@EJB
	protected DocumentiExporterManager documentiExporterManager;

	@Override
	public List<byte[]> esportaDocumenti(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) throws DocumentiExporterException {
		return documentiExporterManager.esportaDocumenti(areeMagazzino, tipoEsportazione, parametri);
	}

}
