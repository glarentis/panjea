package it.eurotn.panjea.magazzino.service;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.magazzino.service.interfaces.ModuloPrezzoService;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ModuloPrezzoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ModuloPrezzoService")
public class ModuloPrezzoServiceBean implements ModuloPrezzoService {

	@EJB
	private PrezzoArticoloCalculator prezzoArticoloCalculator;

	@Override
	public PoliticaPrezzo calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
		return prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);
	}
}
