package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AreaAccontoManager extends IAreaTesoreriaDAO {

	/**
	 * Carica tutti gli acconti che possono essere utilizzati per pagare in automatico.
	 * 
	 * @param entita
	 *            entita di riferimento
	 * @param codiceValuta
	 *            codice della valuta di riferimento
	 * @return acconti caricati
	 */
	List<AreaAcconto> caricaAccontiAutomaticiPerPagamenti(EntitaLite entita, String codiceValuta);

	/**
	 * Carica i {@link DocumentoAcconto} in base ai {@link ParametriRicercaDocumentiAcconti}.
	 * 
	 * @param parametriRicercaAcconti
	 *            parametri di ricerca
	 * @return acconti caricati
	 */
	List<AreaAcconto> caricaAreaAcconti(ParametriRicercaAcconti parametriRicercaAcconti);

	/**
	 * 
	 * @param areaAcconto
	 *            areaAcconto per la quale caricare i pagamenti
	 * @return pagamenti legati all'acconto
	 */
	List<Pagamento> caricaPagamentiByAreaAcconto(AreaAcconto areaAcconto);

	/**
	 * Salva un {@link DocumentoAcconto}.
	 * 
	 * @param areaAcconto
	 *            acconto da salvare
	 * @return acconto salvato
	 * @throws TipoDocumentoBaseException
	 *             ecceziona sollevata se non viene trovato il tipo documento base per l'acconto fornitore o cliente
	 */
	AreaAcconto salvaAcconto(AreaAcconto areaAcconto) throws TipoDocumentoBaseException;
}
