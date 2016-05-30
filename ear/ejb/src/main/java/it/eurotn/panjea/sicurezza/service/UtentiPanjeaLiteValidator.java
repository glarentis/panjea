package it.eurotn.panjea.sicurezza.service;

import it.eurotn.panjea.sicurezza.domain.UtenteCollegato;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.List;

public class UtentiPanjeaLiteValidator implements UtentiValidator {

	@Override
	public boolean canAddUser(List<UtenteCollegato> utenti, UtenteCollegato utente) {
		if (utenti.contains(utente)) {
			return true;
		}
		if (utenti.size() == 0) {
			return true;
		}
		if (utenti.size() >= 2) {
			return false;
		}

		// Se ne ho solamente uno controllo se è locale
		String localAddress = PanjeaEJBUtil.getMacAddress();
		boolean utentePresenteLocale = localAddress.equals(utenti.get(0).getMcAddress());
		boolean utenteDaAggiungereLocale = localAddress.equals(utente.getMcAddress());

		return utentePresenteLocale ^ utenteDaAggiungereLocale;
	}
}
