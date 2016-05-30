/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.panjea.anagrafica.classedocumento.impl;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.classedocumento.manager.AbstractClasseTipoDocumento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Leonardo
 */
public class ClasseFattura extends AbstractClasseTipoDocumento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public List<String> getTipiAree() {
		List<String> list = new ArrayList<String>();
		list.add("it.eurotn.panjea.contabilita.domain.TipoAreaContabile");
		list.add("it.eurotn.panjea.partite.domain.TipoAreaPartita");
		list.add("it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino");
		// aggiungo il tipoareaIva per specificare che questa
		// classeTipoDocumento puo' avere una gestione righe iva
		list.add(IClasseTipoDocumento.TIPO_AREA_IVA);
		return list;
	}

	@Override
	public List<String> getTipiCaratteristiche() {
		List<String> list = new ArrayList<String>();
		// tipo caratteristica Nota di credito
		list.add(IClasseTipoDocumento.TIPO_CARATTERISTICA_NOTA_CREDITO);
		list.add(IClasseTipoDocumento.TIPO_CARATTERISTICA_LOTTI);
		list.add(IClasseTipoDocumento.TIPO_CARATTERISTICA_INTRA);
		return list;
	}

}
