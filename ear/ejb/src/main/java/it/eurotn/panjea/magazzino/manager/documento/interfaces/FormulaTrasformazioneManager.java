package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.exception.FormuleLinkateException;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface FormulaTrasformazioneManager {

	/**
	 * Cancella una {@link FormulaTrasformazione}.
	 * 
	 * @param formulaTrasformazione
	 *            formula da cancellare
	 */
	void cancellaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione);

	/**
	 * Carica una {@link FormulaTrasformazione}.
	 * 
	 * @param formulaTrasformazione
	 *            formula da caricare
	 * @return formula caricata
	 */
	FormulaTrasformazione caricaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione);

	/**
	 * Carica tutte le formule di trsformazione.
	 * 
	 * @param codice
	 *            valore da ricercare
	 * @return formule caricate
	 */
	List<FormulaTrasformazione> caricaFormuleTrasformazione(String codice);

	/**
	 * Salva una {@link FormulaTrasformazione}.
	 * 
	 * @param formulaTrasformazione
	 *            formula da salvare
	 * @return formula salvata
	 */
	FormulaTrasformazione salvaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione);

	/**
	 * Verifica che le fomule non abbiano riferimenti circolari in base al tipo attributo.
	 * 
	 * @param tipoAttributo
	 *            tipo attributo di riferimento
	 * @param formulaTrasformazione
	 *            formula del tipo attributo di riferimento
	 * @param formuleTipiAttributo
	 *            mappa contenente i tipi attributo e formula associata
	 * @throws FormuleLinkateException
	 *             FormuleLinkateException
	 */
	void verificaFormuleLinkate(TipoAttributo tipoAttributo, FormulaTrasformazione formulaTrasformazione,
			Map<TipoAttributo, FormulaTrasformazione> formuleTipiAttributo) throws FormuleLinkateException;

}
