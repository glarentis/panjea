package it.eurotn.panjea.magazzino.manager.sqlbuilder;

import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

public class InventarioArticoloQueryBuilder extends GiacenzaQueryBuilder {

	private String codiceAzienda;

	/**
	 *
	 * Costruttore.
	 *
	 * @param codiceAzienda
	 *            codice azienda
	 *
	 */
	public InventarioArticoloQueryBuilder(final String codiceAzienda) {
		super();
		this.codiceAzienda = codiceAzienda;
	}

	@Override
	public String getSqlString(Integer idArticolo, Integer idDeposito, Date data) {
		return getSqlString(new ArrayList<Integer>(), idDeposito, data);
	}

	@Override
	public String getSqlString(List<Integer> idArticoli, Integer idDeposito, Date data) {
		StringBuilder sb = new StringBuilder(2000);
		sb.append("INSERT INTO maga_inventari_articolo ");
		sb.append(" select null, UNIX_TIMESTAMP(NOW()),'europa',0,");
		sb.append(PanjeaEJBUtil.addQuote(DateFormatUtils.format(data, "yyyy-MM-dd")));
		sb.append(",giac.giacenza,giac.idArticolo,");
		sb.append(idDeposito);
		sb.append(",null ");
		sb.append(" from (");
		sb.append(super.getSqlString(new ArrayList<Integer>(), idDeposito, data));
		sb.append(") as giac");

		return sb.toString();
	}

}
