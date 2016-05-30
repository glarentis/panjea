package it.eurotn.panjea.anagrafica.documenti.domain;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;

import java.util.Map;

/**
 * Interfaccia implementata da un'area del documento.
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public interface IAreaDocumento extends IDefProperty {

	/**
	 *
	 * @return variabili avvalorate per l'area documento
	 */
	Map<String, Object> fillVariables();

	/**
	 *
	 * @return documento legato all'area
	 */
	Documento getDocumento();

	/**
	 *
	 * @return stato dell'area documento.
	 */
	IStatoDocumento getStato();

	/**
	 * @return the statoSpedizione
	 */
	StatoSpedizione getStatoSpedizione();

	/**
	 *
	 * @return tipoAreaDocumento
	 */
	ITipoAreaDocumento getTipoAreaDocumento();

	/**
	 *
	 * @param documento
	 *            documento da associare all'area
	 */
	void setDocumento(Documento documento);

	/**
	 * @param statoSpedizione
	 *            the statoSpedizione to set
	 */
	void setStatoSpedizione(StatoSpedizione statoSpedizione);

	/**
	 *
	 * @param tipoAreaDocumento
	 *            tipoAreaDocumento da associare all'area
	 */
	void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento);

}
