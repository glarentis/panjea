package it.eurotn.panjea.sicurezza.service;

import it.eurotn.panjea.sicurezza.domain.UtenteCollegato;
import it.eurotn.panjea.sicurezza.license.exception.MassimoNumerUtentiCollegati;
import it.eurotn.panjea.sicurezza.service.interfaces.ConnessioneUtenteService;
import it.eurotn.panjea.sicurezza.service.interfaces.UtentiMBean;
import it.eurotn.security.JecPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.ConnessioneUtenteService")
@SecurityDomain("PanjeaLoginModule")
@RemoteBinding(jndiBinding = "Panjea.ConnessioneUtenteService")
public class ConnessioneUtenteServiceBean implements ConnessioneUtenteService {

	@EJB
	private UtentiMBean utentiManager;

	@Resource
	private SessionContext sessionContext;

	@Override
	public void aggiungiUtente(String macAddress) throws MassimoNumerUtentiCollegati {
		UtenteCollegato utente = new UtenteCollegato(getJecPrincipal().getName(), macAddress, getJecPrincipal()
				.getCodiceAzienda());
		utentiManager.aggiungiUtente(utente);
	}

	@Override
	public void checkAlive(String macAddress) throws MassimoNumerUtentiCollegati {
		UtenteCollegato utente = new UtenteCollegato(getJecPrincipal().getName(), macAddress, getJecPrincipal()
				.getCodiceAzienda());
		utentiManager.cheakAlive(utente);

	}

	/**
	 * Restituisce il principal loggato.
	 *
	 * @return principal
	 */
	private JecPrincipal getJecPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	@Override
	public void rimuoviUtente(String macAddress) {
		UtenteCollegato utente = new UtenteCollegato(getJecPrincipal().getName(), macAddress, getJecPrincipal()
				.getCodiceAzienda());
		utentiManager.rimuoviUtente(utente);

	}

}
