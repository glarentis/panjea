package it.eurotn.panjea.stampe.service;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.classedocumento.manager.interfaces.ClasseTipoDocumentoManager;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.panjea.stampe.manager.interfaces.LayoutStampeManager;
import it.eurotn.panjea.stampe.service.interfaces.LayoutStampeService;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.LayoutStampeService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.LayoutStampeService")
public class LayoutStampeServiceBean implements LayoutStampeService {

	@EJB
	private LayoutStampeManager layoutStampeManager;

	@EJB
	private ClasseTipoDocumentoManager classeTipoDocumentoManager;

	@Resource
	private SessionContext context;

	@Override
	public LayoutStampa aggiungiLayoutStampa(ITipoAreaDocumento tipoAreaDocumento, String reportName,
			EntitaLite entita, SedeEntita sedeEntita) {
		return layoutStampeManager.aggiungiLayoutStampa(tipoAreaDocumento, reportName, entita, sedeEntita);
	}

	@Override
	public void cancellaLayoutStampa(LayoutStampa layoutStampa) {
		layoutStampeManager.cancellaLayoutStampa(layoutStampa);
	}

	@Override
	public List<IClasseTipoDocumento> caricaClassiTipoDocumento() {
		return classeTipoDocumentoManager.caricaClassiTipoDocumento();
	}

	@Override
	public List<LayoutStampaDocumento> caricaLayoutStampaBatch(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
			SedeEntita sedeEntita) {
		return layoutStampeManager.caricaLayoutStampaBatch(tipoAreaDocumento, entita, sedeEntita);
	}

	@Override
	public List<LayoutStampaDocumento> caricaLayoutStampaPerDocumenti() {
		return layoutStampeManager.caricaLayoutStampaPerDocumenti();
	}

	@Override
	public List<LayoutStampa> caricaLayoutStampe() {
		return layoutStampeManager.caricaLayoutStampe();
	}

	@Override
	public List<LayoutStampaDocumento> caricaLayoutStampe(Integer idEntita) {
		return layoutStampeManager.caricaLayoutStampe(idEntita);
	}

	@Override
	public List<LayoutStampaDocumento> caricaLayoutStampe(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
			SedeEntita sedeEntita) {
		return layoutStampeManager.caricaLayoutStampe(tipoAreaDocumento, entita, sedeEntita);
	}

	@Override
	public LayoutStampa caricaLayoutStampe(String reportName) {
		return layoutStampeManager.caricaLayoutStampe(reportName);
	}

	@Override
	public List<ITipoAreaDocumento> caricaTipoAree(String classeTipoDocumento) {
		return layoutStampeManager.caricaTipoAree(classeTipoDocumento);
	}

	@Override
	public LayoutStampa salvaLayoutStampa(LayoutStampa layoutStampa) {
		return layoutStampeManager.salvaLayoutStampa(layoutStampa);
	}

	@Override
	public List<LayoutStampaDocumento> setLayoutAsDefault(LayoutStampaDocumento layoutStampa) {
		return layoutStampeManager.setLayoutAsDefault(layoutStampa);
	}

	@Override
	public List<LayoutStampaDocumento> setLayoutForInvioMail(LayoutStampaDocumento layoutStampa) {
		return layoutStampeManager.setLayoutForInvioMail(layoutStampa);
	}

	@Override
	public List<LayoutStampaDocumento> setLayoutForUsoInterno(LayoutStampaDocumento layoutStampa, boolean usoInterno) {
		return layoutStampeManager.setLayoutForUsoInterno(layoutStampa, usoInterno);
	}
}
