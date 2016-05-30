package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;

import javax.ejb.Local;

@Local
public interface DepositiMagazzinoManager {

	/**
	 * Cancella un deposito magazzino.
	 * 
	 * @param id
	 *            id del deposito
	 */
	void cancellaDepositoMagazzino(Integer id);

	/**
	 * Carica un deposito magazzino.
	 * 
	 * @param id
	 *            id del deposito da caricare
	 * @return deposito magazzino caricato
	 */
	DepositoMagazzino caricaDepositoMagazzino(Integer id);

	/**
	 * Carica il ceposito magazzino in base al deposito specificato.
	 * 
	 * @param deposito
	 *            deposito di riferimento
	 * @return deposito magazzino caricato
	 */
	DepositoMagazzino caricaDepositoMagazzinoByDeposito(Deposito deposito);

	/**
	 * Salva un <code>DepositoMagazzino</code>.
	 * 
	 * @param depositoMagazzino
	 *            <code>DepositoMagazzino</code> da salvare
	 * @return <code>DepositoMagazzino</code> salvato
	 */
	DepositoMagazzino salvaDepositoMagazzino(DepositoMagazzino depositoMagazzino);
}
