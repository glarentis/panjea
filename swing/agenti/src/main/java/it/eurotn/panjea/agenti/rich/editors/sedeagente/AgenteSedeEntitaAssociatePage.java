/**
 * 
 */
package it.eurotn.panjea.agenti.rich.editors.sedeagente;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.editors.entita.sedicollegate.SediEntitaAssociatePage;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

import java.util.Collection;

/**
 * @author fattazzo
 * 
 */
public class AgenteSedeEntitaAssociatePage extends SediEntitaAssociatePage {

	@Override
	protected void doDelete(RiepilogoSedeEntitaDTO riepilogoSedeEntitaDTO) {
		SedeEntita sedeEntita = getAnagraficaBD().caricaSedeEntita(riepilogoSedeEntitaDTO.getSedeEntita().getId());
		sedeEntita.setAgente(null);
		try {
			getAnagraficaBD().salvaSedeEntita(sedeEntita);
		} catch (SedeEntitaPrincipaleAlreadyExistException e) {
			// non mi preoccupo dell'eccezione perchè cambio solo l'agente sulla
			// sede e non il tipo sede
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void doSave(SedeEntita sedeEntita) {
		sedeEntita.setAgente((AgenteLite) getEntita().getEntitaLite());
		try {
			getAnagraficaBD().salvaSedeEntita(sedeEntita);
		} catch (SedeEntitaPrincipaleAlreadyExistException e) {
			// non mi preoccupo dell'eccezione perchè cambio solo l'agente sulla
			// sede e non il tipo sede
			throw new RuntimeException(e);
		}
	}

	@Override
	public Collection<RiepilogoSedeEntitaDTO> loadTableData() {
		return getAnagraficaBD().caricaRiepilogoSediEntitaByAgente((AgenteLite) getEntita().getEntitaLite());
	}

}
