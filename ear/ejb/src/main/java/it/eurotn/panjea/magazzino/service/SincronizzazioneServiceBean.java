package it.eurotn.panjea.magazzino.service;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.manager.sincronizzazione.interfaces.SincronizzazioneManager;
import it.eurotn.panjea.magazzino.service.interfaces.SincronizzazioneService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.SincronizzazioneService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.SincronizzazioneService")
public class SincronizzazioneServiceBean implements SincronizzazioneService {

	/**
	 * @uml.property name="sincronizzazioneManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected SincronizzazioneManager sincronizzazioneManager;

	@Override
	public String sincronizzaDDT(List<DatiGenerazione> datiGenerazione) {
		return sincronizzazioneManager.sincronizzaDDT(datiGenerazione);
	}

}
