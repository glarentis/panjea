package it.eurotn.panjea.cauzioni.service;

import it.eurotn.panjea.cauzioni.manager.interfaces.CauzioniManager;
import it.eurotn.panjea.cauzioni.service.interfaces.CauzioniService;
import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniEntitaDTO;

import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.CauzioniService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.CauzioniService")
public class CauzioniServiceBean implements CauzioniService {

	@EJB
	private CauzioniManager cauzioniManager;

	@Override
	public List<MovimentazioneCauzioneDTO> caricaMovimentazioneCauzioniArticolo(Set<Integer> idEntita,
			Set<Integer> idSedeEntita, Set<Integer> idArticolo) {
		return cauzioniManager.caricaMovimentazioneCauzioniArticolo(idEntita, idSedeEntita, idArticolo);
	}

	@Override
	public List<SituazioneCauzioniDTO> caricaSituazioneCauzioni(ParametriRicercaSituazioneCauzioni parametri) {
		return cauzioniManager.caricaSituazioneCauzioni(parametri);
	}

	@Override
	public List<SituazioneCauzioniEntitaDTO> caricaSituazioneCauzioniEntita(Integer idEntita, boolean raggruppamentoSedi) {
		return cauzioniManager.caricaSituazioneCauzioniEntita(idEntita, raggruppamentoSedi);
	}
}
