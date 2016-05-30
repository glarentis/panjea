/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.entita.sedicollegate;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

import java.util.List;

/**
 * @author fattazzo
 * 
 */
public class VettoreSediEntitaAssociatePage extends SediEntitaAssociatePage {

	@Override
	protected void doDelete(RiepilogoSedeEntitaDTO riepilogoSedeEntitaDTO) {
		SedeEntita sedeEntita = getAnagraficaBD().caricaSedeEntita(riepilogoSedeEntitaDTO.getSedeEntita().getId());
		sedeEntita.setVettore(null);
		try {
			getAnagraficaBD().salvaSedeEntita(sedeEntita);
		} catch (SedeEntitaPrincipaleAlreadyExistException e) {
			// non mi preoccupo dell'eccezione perchè cambio solo
			// il vettore sulla sede e non il tipo sede
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void doSave(SedeEntita sedeEntita) {
		sedeEntita.setVettore((VettoreLite) getEntita().getEntitaLite());
		try {
			getAnagraficaBD().salvaSedeEntita(sedeEntita);
		} catch (SedeEntitaPrincipaleAlreadyExistException e) {
			// non mi preoccupo dell'eccezione perchè cambio solo il vettore
			// sulla sede e non il tipo sede
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<RiepilogoSedeEntitaDTO> loadTableData() {
		return getAnagraficaBD().caricaRiepilogoSediEntitaByVettore((VettoreLite) getEntita().getEntitaLite());
	}

}
