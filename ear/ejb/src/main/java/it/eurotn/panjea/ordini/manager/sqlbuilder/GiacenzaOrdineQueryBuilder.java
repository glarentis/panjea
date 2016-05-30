package it.eurotn.panjea.ordini.manager.sqlbuilder;

import it.eurotn.panjea.magazzino.manager.sqlbuilder.GiacenzaQueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Aggiorna le righe di un ordine con la giacenza al momento della stampa.
 *
 * @author giangi
 * @version 1.0, 16/dic/2011
 *
 */
public class GiacenzaOrdineQueryBuilder extends GiacenzaQueryBuilder {

	private final Integer idOrdine;

	/**
	 * Costruttore.
	 *
	 * @param idOrdine
	 *            id dell'ordine
	 */
	public GiacenzaOrdineQueryBuilder(final Integer idOrdine) {
		super();
		this.idOrdine = idOrdine;
	}

	@Override
	public String getSqlString(Integer idArticolo, Integer idDeposito, Date data) {

		List<Integer> idArticoli = new ArrayList<Integer>();
		if(idArticolo != null) {
			idArticoli.add(idArticolo);
		}
		return getSqlString(idArticoli, idDeposito, data);
	}

	@Override
	public String getSqlString(List<Integer> idArticoli, Integer idDeposito, Date data) {
		StringBuilder sb = new StringBuilder();

		sb.append("update ordi_righe_ordine ");
		sb.append("inner join (");
		sb.append(super.getSqlString(idArticoli, idDeposito, data));
		sb.append(") as giac on ordi_righe_ordine.articolo_id= giac.idArticolo set ordi_righe_ordine.giacenza=giac.giacenza where ordi_righe_ordine.areaOrdine_id= ");
		sb.append(idOrdine);

		return sb.toString();
	}
}
