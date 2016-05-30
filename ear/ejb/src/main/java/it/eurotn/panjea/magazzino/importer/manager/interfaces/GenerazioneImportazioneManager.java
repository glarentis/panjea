/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface GenerazioneImportazioneManager {

	AreaMagazzino generaDocumento(DocumentoImport documentoImport, SedeEntita sedeEntita,
			SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO, DatiGenerazione datiGenerazione);
}
