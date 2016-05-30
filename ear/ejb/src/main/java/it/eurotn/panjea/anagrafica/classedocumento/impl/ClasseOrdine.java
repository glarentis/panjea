package it.eurotn.panjea.anagrafica.classedocumento.impl;

import it.eurotn.panjea.anagrafica.classedocumento.manager.AbstractClasseTipoDocumento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class ClasseOrdine extends AbstractClasseTipoDocumento implements Serializable {
	private static final long serialVersionUID = -2602959339220726833L;

	/**
	 * Aree abilitate.
	 * 
	 * @return aree abilitatre per la classe
	 */
	@Override
	public List<String> getTipiAree() {
		List<String> list = new ArrayList<String>();
		list.add("it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine");
		list.add("it.eurotn.panjea.partite.domain.TipoAreaPartita");
		return list;
	}

	@Override
	public List<String> getTipiCaratteristiche() {
		List<String> list = new ArrayList<String>();
		return list;
	}

}
