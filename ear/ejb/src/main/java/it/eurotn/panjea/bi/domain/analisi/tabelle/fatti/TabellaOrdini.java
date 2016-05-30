package it.eurotn.panjea.bi.domain.analisi.tabelle.fatti;

import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasureFunction;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaFatti;

import java.math.BigDecimal;

public class TabellaOrdini extends TabellaFatti {
	private static final long serialVersionUID = 3668257538868165843L;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public TabellaOrdini() {
		super("ordini", "vista", null, "ordini");
		colonne.putAll(ColumnMeasure.creaColonneAggregate("Qta ord.", double.class, this, "Qta ord.", null, null));

		StringBuilder sb = new StringBuilder();

		// sum(ordini.`Qta ord.` -coalesce((select sum(coalesce(rm.qtaMagazzino,0)) from maga_righe_magazzino rm where
		// rm.rigaOrdineCollegata_Id=ordini.idRigaOrdine),0)) as `Qta inevasa`,

		sb.append("(select if(ro.evasioneForzata=0,0,if(ro.qtaMagazzino - coalesce(sum(rm.moltQtaOrdine * rm.qtaMagazzino),0) > 0,ro.qtaMagazzino - coalesce(sum(rm.moltQtaOrdine * rm.qtaMagazzino),0),0)) as `Qta_forzata_tmp` ");
		sb.append("from maga_righe_magazzino rm ");
		sb.append("inner join ordi_righe_ordine ro on ro.id=rm.rigaOrdineCollegata_Id ");
		sb.append("where rm.rigaOrdineCollegata_Id=`ordini`.idRigaOrdine ) ");
		Colonna col = new ColumnMeasureFunction(sb.toString(), double.class, this, "Qta forzata", null, 2);
		colonne.put(col.getKey(), col);

		sb = new StringBuilder();
		sb.append("(select if ");
		sb.append("( ro.qtaMagazzino - if(ro.evasioneForzata = 0,coalesce(sum(rm.moltQtaOrdine * rm.qtaMagazzino),0),ro.qta) > 0, ");
		sb.append("   ro.qtaMagazzino - if(ro.evasioneForzata = 0,coalesce(sum(rm.moltQtaOrdine * rm.qtaMagazzino),0),ro.qta),   0) as `Qta_inevasa_tmp` ");
		sb.append("from maga_righe_magazzino rm ");
		sb.append("inner join ordi_righe_ordine ro on ro.id=rm.rigaOrdineCollegata_Id ");
		sb.append("where rm.rigaOrdineCollegata_Id=`ordini`.idRigaOrdine ");
		sb.append("and ro.evasioneForzata = 0)");
		col = new ColumnMeasureFunction(sb.toString(), double.class, this, "Qta inevasa", null, 2);
		colonne.put(col.getKey(), col);

		colonne.putAll(ColumnMeasure.creaColonneAggregate("GG cons. prevista", BigDecimal.class, this,
				"GG cons. prevista", null, null));

		colonne.putAll(ColumnMeasure.creaColonneAggregate("ImportoNetto", BigDecimal.class, this, "Importo netto",
				null, 2, null));
		colonne.putAll(ColumnMeasure.creaColonneAggregate("Importo", BigDecimal.class, this, "Importo", null, 2, null));
		colonne.putAll(ColumnMeasure.creaColonneAggregate("ImportoScontato", BigDecimal.class, this,
				"Importo scontato", null, 2, null));
	}

}
