/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.panjea.anagrafica.classedocumento;

import java.util.List;

/**
 * 
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public interface IClasseTipoDocumento {

	String TIPO_AREA_IVA = "TipoAreaIva";
	String TIPO_CARATTERISTICA_NOTA_CREDITO = "notaDiCredito";
	String TIPO_CARATTERISTICA_INTRA = "gestioneIntra";
	String TIPO_CARATTERISTICA_LOTTI = "gestioneLotti";

	/**
	 * @return tipi aree gestite
	 */
	List<String> getTipiAree();

	/**
	 * @return tipi caratteristiche gestite
	 */
	List<String> getTipiCaratteristiche();

}
