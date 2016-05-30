package it.eurotn.panjea.contabilita.manager.rigacontabilebuider;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IRigaContabileBuilder {

	/**
	 * Crea una riga contabile in base ai parametri specificati.
	 * 
	 * @param strutturaContabile
	 *            struttura contabile
	 * @param areaContabile
	 *            area contabile
	 * @param mapSC
	 *            mappa contenente le variabili e i relativi valori per il calcolo delle formule definite sulla
	 *            struttura contabile
	 * @param ordinamentoRiga
	 *            ordinamento della riga generata
	 * @param controPartite
	 *            contro partite
	 * @param rigaAutomatica
	 *            <code>true</code> per creare una riga automatica
	 * @return {@link RigaContabile} generata
	 * @throws FormulaException
	 *             sollevata in caso di errore durante il calcolo della formula legata alla struttura contabile
	 * @throws ContabilitaException
	 *             eccezione generica di contabilità
	 * @throws ContoEntitaAssenteException
	 *             sollevata se per l'entità non esiste un sottoconto
	 * @throws ContoRapportoBancarioAssenteException
	 *             ContoRapportoBancarioAssenteException
	 */
	List<RigaContabile> creaRigheContabili(StrutturaContabile strutturaContabile, AreaContabile areaContabile,
			Map<String, BigDecimal> mapSC, long ordinamentoRiga, List<ControPartita> controPartite,
			boolean rigaAutomatica) throws FormulaException, ContabilitaException, ContoEntitaAssenteException,
			ContoRapportoBancarioAssenteException;
}
