package it.eurotn.panjea.anagrafica.classedocumento.manager.interfaces;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;

import java.util.List;

import javax.ejb.Local;

/**
 * 
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
@Local
public interface ClasseTipoDocumentoManager {

	/**
	 * 
	 * @return lista delle classi da poter assegnare al tipo documento.
	 */
	List<IClasseTipoDocumento> caricaClassiTipoDocumento();
}
