package it.eurotn.panjea.magazzino.rich.editors.articolo.statistiche;

import it.eurotn.panjea.magazzino.util.StatisticaArticolo;

import com.jidesoft.pivot.PivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.pivot.Values;

public class DisponibilitaPivotDataModel extends PivotDataModel {

	/**
	 *
	 * @param statisticaArticolo
	 *            statistica articolo
	 */
	public DisponibilitaPivotDataModel(final StatisticaArticolo statisticaArticolo) {
		super(new DisponibilitaTableModel(statisticaArticolo.getDisponibilita(), statisticaArticolo.getGiacenza()));
		getField("ANNO").setAreaType(PivotField.AREA_COLUMN);
		getField("ANNO").setAreaIndex(0);
		getField("ANNO").setSortByAscending(true);

		getField("MESE").setAreaType(PivotField.AREA_COLUMN);
		getField("MESE").setAreaIndex(1);
		getField("MESE").setConverterContext(it.eurotn.panjea.rich.converter.MeseConverter.CONTEXT);

		getField("DATA").setAreaType(PivotField.AREA_COLUMN);
		getField("DATA").setAreaIndex(2);

		getField("FABB").setAreaType(PivotField.AREA_DATA);
		getField("CAR").setAreaType(PivotField.AREA_DATA);
		getField("FABB INC").setAreaType(PivotField.AREA_DATA);
		getField("CAR INC").setAreaType(PivotField.AREA_DATA);
		getField("DISP").setAreaType(PivotField.AREA_DATA);
		setCellStyleProvider(new DisponibilitaCellStyleProvider());

		getField("DATA").setSortByAscending(true);
	}

	@Override
	protected Object calculateStatistics(PivotField pivotField, Values arg1, Values arg2, int arg3, Object[] arg4) {
		// Se sono su una colonna incrementale ritorno il massimo (qundi l'ultomo valore nell'array)
		if (pivotField.getName().endsWith("INC")) {
			double max = (Double) arg4[0];
			for (int j = 1; j < arg4.length; ++j) {
				if ((Double) arg4[j] > max) {
					max = (Double) arg4[j];
				}
			}
			return max;
		} else if ("DISP".equals(pivotField.getName())) {
			if (arg4.length > 1) {
				return "";
			}
		}
		return super.calculateStatistics(pivotField, arg1, arg2, arg3, arg4);
	}
}
