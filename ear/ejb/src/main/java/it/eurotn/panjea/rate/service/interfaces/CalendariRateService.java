package it.eurotn.panjea.rate.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface CalendariRateService {

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
	List<CalendarioRate> caricaCalendariRate(String fieldSearch, String valueSearch);

	/**
	 * Carica tutti i calendari rate aziendali.
	 * 
	 * @return calendari caricati
	 */
	List<CalendarioRate> caricaCalendariRateAzienda();

	/**
	 * Carica tutti i calendari rate configurati per il cliente.
	 * 
	 * @param clienteLite
	 *            cliente
	 * @return calendari
	 */
	List<CalendarioRate> caricaCalendariRateCliente(ClienteLite clienteLite);

	/**
	 * Carica la lista dei clienti che non possono essere aggiungi al calendario in base alle categorie rata.
	 * 
	 * @param categorieRateDaScludere
	 *            categorie rata
	 * @return clienti
	 */
	List<ClienteLite> caricaClientiNonDisponibiliPerCalendario(List<CategoriaRata> categorieRateDaScludere);

	/**
	 * Carica tutte le righe per il calendario rate specificato.
	 * 
	 * @param calendarioRate
	 *            calendario
	 * @return righe caricate
	 */
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
