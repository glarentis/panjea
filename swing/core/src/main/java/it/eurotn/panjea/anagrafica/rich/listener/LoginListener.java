/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.listener;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.statusBarItem.AziendaStatusBarItem;
import it.eurotn.panjea.anagrafica.rich.statusBarItem.UserStatusBarItem;
import it.eurotn.panjea.rich.login.ConnessioneUtente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.richclient.security.ClientSecurityEvent;
import org.springframework.richclient.security.LoginEvent;
import org.springframework.richclient.security.LogoutEvent;

/**
 * Si occupa di gestire tutti gli eventi di Login e Logout dell'applicazione.
 *
 * @author fattazzo
 * @version 1.0, 14/nov/07
 *
 */
public class LoginListener implements ApplicationListener {

	private AziendaCorrente aziendaCorrente;
	private AziendaStatusBarItem aziendaStatusBarItem;
	private UserStatusBarItem userStatusBarItem;
	private ConnessioneUtente connessioneUtente;

	private Logger logger = Logger.getLogger(LoginListener.class);

	/**
	 * @return Returns the connessioneUtente.
	 */
	public ConnessioneUtente getConnessioneUtente() {
		return connessioneUtente;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof ClientSecurityEvent) {
			logger.debug("---> SecurityEvent catched");
			if (applicationEvent instanceof LoginEvent) {
				logger.debug("---> Intercettato evento di login, carico l'azienda corrente");
				// init dell'azienda sul login
				aziendaCorrente.initialize();
				aziendaStatusBarItem.setText(aziendaCorrente.getDenominazione());
				userStatusBarItem.setText(PanjeaSwingUtil.getUtenteCorrente().getUserName());
			}

			if (applicationEvent instanceof LogoutEvent) {
				logger.debug("---> Intercettato evento di logout, azienda corrente null");
				connessioneUtente.logout();
				// reset dell'azienda sul logout
				aziendaCorrente.reset();
				aziendaStatusBarItem.setText("");
				userStatusBarItem.setText("");
			}
		}
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param aziendaStatusBarItem
	 *            the aziendaStatusBarItem to set
	 */
	public void setAziendaStatusBarItem(AziendaStatusBarItem aziendaStatusBarItem) {
		this.aziendaStatusBarItem = aziendaStatusBarItem;
	}

	/**
	 * @param connessioneUtente
	 *            The connessioneUtente to set.
	 */
	public void setConnessioneUtente(ConnessioneUtente connessioneUtente) {
		this.connessioneUtente = connessioneUtente;
	}

	/**
	 * @param userStatusBarItem
	 *            the userStatusBarItem to set
	 */
	public void setUserStatusBarItem(UserStatusBarItem userStatusBarItem) {
		this.userStatusBarItem = userStatusBarItem;
	}

}
