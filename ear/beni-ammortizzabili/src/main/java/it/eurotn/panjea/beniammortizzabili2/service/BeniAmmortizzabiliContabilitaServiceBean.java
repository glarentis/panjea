/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.service;

import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliDaSimulazionePresentiException;
import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili2.manager.contabilita.interfaces.BeniAmmortizzabiliContabilitaManager;
import it.eurotn.panjea.beniammortizzabili2.service.interfaces.BeniAmmortizzabiliContabilitaService;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.RigheContabiliNonValidiException;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.BeniAmmortizzabiliContabilitaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.BeniAmmortizzabiliContabilitaService")
public class BeniAmmortizzabiliContabilitaServiceBean implements BeniAmmortizzabiliContabilitaService {

	@EJB
	private BeniAmmortizzabiliContabilitaManager beniAmmortizzabiliContabilitaManager;

	@Override
	public void confermaAreeContaibliSimulazione(Integer idSimulazione) throws ContabilitaException,
	RigheContabiliNonValidiException {
		beniAmmortizzabiliContabilitaManager.confermaAreeContaibliSimulazione(idSimulazione);
	}

	@Override
	@TransactionTimeout(value = 7200)
	public void creaAreeContabili(Integer idSimulazione) throws AreeContabiliDaSimulazionePresentiException,
			SottocontiBeniNonValidiException {
		beniAmmortizzabiliContabilitaManager.creaAreeContabili(idSimulazione);
	}

}
