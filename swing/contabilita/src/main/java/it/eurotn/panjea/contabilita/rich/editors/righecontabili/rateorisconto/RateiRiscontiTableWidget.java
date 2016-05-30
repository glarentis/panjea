package it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto;

import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;

public class RateiRiscontiTableWidget extends JideTableWidget {

	/**
	 *
	 * @param id
	 *            id modello
	 * @param tableModel
	 *            table modello
	 */
	@SuppressWarnings("unchecked")
	public RateiRiscontiTableWidget(final String id, final RigaRateoRiscontoModel tableModel) {
		super(id, tableModel);
		setTableType(TableType.HIERARCHICAL);
		setHierarchicalTableComponentFactory(new HierarchicalTableComponentFactory() {

			@Override
			public Component createChildComponent(HierarchicalTable paramHierarchicalTable, Object paramObject,
					int paramInt) {
				RigheRateoRiscontoAnnoTable childComponent = new RigheRateoRiscontoAnnoTable();
				JScrollPane sp = new JScrollPane(childComponent.getTable());
				if (paramObject != null) {
					List<RigaRiscontoAnno> rateiRiscontiAnnuali = (List<RigaRiscontoAnno>) paramObject;
					childComponent.setRows(rateiRiscontiAnnuali);
					sp.setPreferredSize(new Dimension(500, (19 * (rateiRiscontiAnnuali.size()) + 50)));
				}
				sp.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
				return sp;
			}

			@Override
			public void destroyChildComponent(HierarchicalTable paramHierarchicalTable, Component paramComponent,
					int paramInt) {
			}
		});
	}
}
