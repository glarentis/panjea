package it.eurotn.panjea.bi.rich.editors.chart;

import com.jidesoft.pivot.PivotField;

public class ChartCategory {
	private String categoryName;

	public ChartCategory(PivotField pivotField) {
	}

	@Override
	public String toString() {
		return categoryName;
	}
}
