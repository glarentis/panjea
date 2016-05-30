package it.eurotn.panjea.sicurezza.service;

import it.eurotn.panjea.settings.SettingsServerMBean;
import it.eurotn.panjea.sicurezza.domain.UtenteCollegato;
import it.eurotn.panjea.sicurezza.license.exception.MassimoNumerUtentiCollegati;
import it.eurotn.panjea.sicurezza.service.interfaces.UtentiMBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.jboss.annotation.ejb.Depends;
import org.jboss.annotation.ejb.Service;

/**
 *
 * Gestisce la lista di utenti collegati tramite un mbean(singleton).<br/>
 * Controlla che non si superino gli utenti attivi contemporaneamente configurati nel contratto.
 *
 * @author giangi
 * @version 1.0, 19/feb/2015
 *
 */
@Service(objectName = "panjea:service=Utenti")
@Depends({ "panjea:service=Settings" })
public class UtentiBean implements UtentiMBean {
	private static final String TIMER_NAME = "timerCheckUtenti";
	private static final long TIMER_INTERVAL = 60_000;
	private static final long TIMEOUT_MIN = 5;

	@Resource
	private TimerService timerService;
	private List<UtenteCollegato> utenti;

	@EJB
	private SettingsServerMBean settingsServer;

	@Resource
	private SessionContext sessionContext;

	@Override
	public void aggiungiUtente(UtenteCollegato utente) throws MassimoNumerUtentiCollegati {
		if (settingsServer.getAziendaSettings(utente.getAzienda()).canAddUser(utenti, utente)) {
			if (!utenti.contains(utente)) {
				utenti.add(utente);
			}
		} else {
			throw new MassimoNumerUtentiCollegati();
		}
	}

	@Override
	public void cheakAlive(UtenteCollegato utente) throws MassimoNumerUtentiCollegati {
		int indexFind = utenti.indexOf(utente);
		if (indexFind > -1) {
			utenti.get(indexFind).checkAlive();
		} else {
			aggiungiUtente(utente);
		}
	}

	@Override
	public void create() throws Exception {
		// Leggo il file properties criptato
		utenti = Collections.synchronizedList(new ArrayList<UtenteCollegato>());
	}

	@Override
	public void destroy() {
	}

	@Override
	public List<UtenteCollegato> getUtentiCollegati() {
		return utenti;
	}

	@Override
	public void rimuoviUtente(UtenteCollegato utente) {
		utenti.remove(utente);
	}

	@Override
	public void start() throws Exception {
		timerService.createTimer(TIMER_INTERVAL, TIMER_INTERVAL, TIMER_NAME);
	}

	@Override
	public void stop() {
		for (Object obj : timerService.getTimers()) {
			Timer timer = (Timer) obj;
			if (TIMER_NAME.equals(timer.getInfo())) {
				timer.cancel();
			}
		}
	}

	/**
	 * Rimuove i client che hanno sforato il timeout.
	 *
	 * @param timer
	 *            timer
	 */
	@Timeout
	public void timeoutHandler(Timer timer) {
		Iterator<UtenteCollegato> iterator = utenti.listIterator();
		while (iterator.hasNext()) {
			if (iterator.next().getMinutiUltimoAggiornamento() > TIMEOUT_MIN) {
				iterator.remove();
			}
		}
	}
}
