package it.eurotn.panjea.contabilita.interceptor;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

import java.text.DecimalFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class EntitaAggiornaInterceptor {

	/**
	 * @uml.property name="pianoContiManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected PianoContiManager pianoContiManager;
	/**
	 * @uml.property name="entitaManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected EntitaManager entitaManager;
	/**
	 * @uml.property name="panjeaMessage"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaMessage panjeaMessage;

	/**
	 * Aggiorna il sottoConto dell'entità.
	 * 
	 * @param entita
	 *            entita con i dati per aggiornare il sottoconto
	 * @param sottoConto
	 *            sottoconto da aggiornare
	 */
	private void aggiornaSottoContoEntita(Entita entita, SottoConto sottoConto) {
		sottoConto.setDescrizione(entita.getAnagrafica().getDenominazione());
		sottoConto.setAbilitato(entita.isAbilitato());
		sottoConto.setCodice(new DecimalFormat("000000").format(entita.getCodice().longValue()));
		pianoContiManager.salvaSottoConto(sottoConto);
	}

	/**
	 * Associa all'entita' il conto. Non se e' un cliente potenziale.
	 * 
	 * @param ctx
	 *            ctx
	 * @return Object
	 * @throws Exception
	 *             Exception
	 */
	@AroundInvoke
	public Object associaContoEntita(InvocationContext ctx) throws Exception {
		Entita entita = (Entita) ctx.getParameters()[0];
		String denominazionePrecedente = entita.getAnagrafica().getDenominazionePrecedente();
		Integer codicePrecedente = entita.getCodicePrecedente();
		boolean abilitatoEntita = entita.isAbilitato();
		entita = (Entita) ctx.proceed();

		AnagraficaLite anagraficaLite = new AnagraficaLite();
		anagraficaLite.setId(entita.getAnagrafica().getId());
		List<EntitaLite> entitaAnag = entitaManager.caricaEntita(anagraficaLite);

		// Solo se cambio la denominazione dovrei aggiornare tutte le entità associate all'anagrafica ma per comodità
		// ciclo su di esse e aggiorno tutto
		for (EntitaLite entitaLite : entitaAnag) {
			boolean aggiornaSottoconto = false;

			SottotipoConto sottotipoConto = Conto.getSottoTipoConto(entitaLite.creaProxyEntita());

			if (sottotipoConto != null) {

				// carico il sottoconto con codice nuovo e non con codice precedente per non alterare la storia del
				// conto
				SottoConto sottoConto = pianoContiManager.caricaSottoContoPerEntita(sottotipoConto,
						entitaLite.getCodice());
				if (sottoConto.isNew()) {
					// devo creare il sotto conto
					creaSottoContoEntita(entitaLite.creaProxyEntita());

					// se non sto inserendo una nuova entità controllo il sottoconto del codice precedente
					if (codicePrecedente != null) {
						// se esiste il sottoconto precedente e arrivo in questo interceptor significa che il sottoconto
						// con
						// codice precedente esiste e non è utilizzato, quindi lo cancello.
						// se il conto è utilizzato in qlc documento, viene mostrato un messaggio all'utente
						// dell'impossibilità
						// di cambiare il codice a causa di documenti collegati
						SottoConto sottoContoPrecedente = pianoContiManager.caricaSottoContoPerEntita(sottotipoConto,
								codicePrecedente);
						if (!sottoContoPrecedente.isNew()) {
							cancellaSottoContoPrecedente(sottoContoPrecedente);
						}
					}
				} else {
					if ((denominazionePrecedente != null && (!entitaLite.getAnagrafica().getDenominazione()
							.equals(denominazionePrecedente)))) {
						aggiornaSottoconto = true;
					}
					if ((codicePrecedente != null && (!entitaLite.getCodice().equals(codicePrecedente)))) {
						aggiornaSottoconto = true;
					}
					if (abilitatoEntita != sottoConto.isAbilitato()) {
						aggiornaSottoconto = true;
					}
					if (aggiornaSottoconto) {
						aggiornaSottoContoEntita(entitaLite.creaProxyEntita(), sottoConto);

					}
				}
			}
		}

		return entita;
	}

	/**
	 * Cancella il sottoconto scelto.
	 * 
	 * @param sottoContoPrecedente
	 *            il sottoconto da cancellare
	 */
	private void cancellaSottoContoPrecedente(SottoConto sottoContoPrecedente) {
		try {
			pianoContiManager.cancellaSottoConto(sottoContoPrecedente);
		} catch (Exception e) {
			// non faccio nulla in caso di errore sulla cancellazione del sottoconto
		}
	}

	/**
	 * Crea un sottoconto per l'entità.
	 * 
	 * @param entita
	 *            entita
	 */
	protected void creaSottoContoEntita(Entita entita) {
		try {
			SottoConto sottoConto = pianoContiManager.creaSottoContoPerEntita(entita);
			if (sottoConto != null) {
				panjeaMessage.send(sottoConto, "contoEntitaAssociati");
			} else {
				panjeaMessage.send(entita, "contoEntitaNonAssociati");
			}
		} catch (ContabilitaException e) {
			throw new RuntimeException(e);
		}
	}
}
