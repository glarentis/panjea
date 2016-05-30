package it.eurotn.panjea.rate.rich.bd;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;

public interface ICalendariRateBD {

	/**
	 * Cancella una {@link CalendarioRate}.
	 * 
	 * @param calendarioRate
	 *            calendario da cancellare
	 */
	void cancellaCalendarioRate(CalendarioRate calendarioRate);

	/**
	 * Cancella una {@link RigaCalendarioRate}.
	 * 
	 * @param rigaCalendarioRate
	 *            riga da cancellare
	 */
	void cancellaRigaCalendarioRate(RigaCalendarioRate rigaCalendarioRate);

	/**
	 * Carica un {@link CalendarioRate}.
	 * 
	 * @param calendarioRate
	 *            calendario da caricare
	 * @param loadLazy
	 *            <code>true</code> carica in sessione tutte le proprietà lazy del calendario
	 * @return calendario caricato
	 */
	@AsyncMethodInvocation
	CalendarioRate caricaCalendarioRate(CalendarioRate calendarioRate, boolean loadLazy);

	/**
	 * Carica tutti i calendari esistenti.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return calendari caricati
	 */
	@AsyncMethodInvocation
	List<CalendarioRate> caricaCalendariRate(String fieldSearch, String valueSearch);

	/**
	 * Carica tutti i calendari rate aziendali.
	 * 
	 * @return calendari caricati
	 */
	@AsyncMethodInvocation
	List<CalendarioRate> caricaCalendariRateAzienda();

	/**
	 * Carica tutti i calendari rate configurati per il cliente.
	 * 
	 * @param clienteLite
	 *            cliente
	 * @return calendari
	 */
	@AsyncMethodInvocation
	List<CalendarioRate> caricaCalendariRateCliente(ClienteLite clienteLite);

	/**
	 * Carica la lista dei clienti che non possono essere aggiungi al calendario in base alle categorie rata.
	 * 
	 * @param categorieRateDaScludere
	 *            categorie rata
	 * @return clienti
	 */
	@AsyncMethodInvocation
	List<ClienteLite> caricaClientiNonDisponibiliPerCalendario(List<CategoriaRata> categorieRateDaScludere);

	/**
	 * Carica tutte le righe per il calendario rate specificato.
	 * 
	 * @param calendarioRate
	 *            calendario
	 * @return righe caricate
	 */
	@AsyncMethodInvocation
	List<RigaCalendarioRate> caricaRigheCalendarioRate(CalendarioRate calendarioRate);

	/**
	 * Salva un calendario rate.
	 * 
	 * @param calendarioRate
	 *            calendario da salvare
	 * @return calendario salvato
	 */
	CalendarioRate salvaCalendarioRate(CalendarioRate calendarioRate);

	/**
	 * Salva una {@link RigaCalendarioRate}.
	 * 
	 * @param rigaCalendarioRate
	 *            riga da salvare
	 * @return riga salvata
	 */
	RigaCalendarioRate salvaRigaCalendarioRate(RigaCalendarioRate rigaCalendarioRate);
}
