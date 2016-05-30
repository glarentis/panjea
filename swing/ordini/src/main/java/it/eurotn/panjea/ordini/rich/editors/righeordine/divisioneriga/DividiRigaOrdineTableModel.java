package it.eurotn.panjea.ordini.rich.editors.righeordine.divisioneriga;

import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

public class DividiRigaOrdineTableModel extends DefaultBeanEditableTableModel<RigaArticolo> {
	private static final long serialVersionUID = -4731935446484055406L;
	private double qtaTot;

	public DividiRigaOrdineTableModel(RigaArticolo rigaArticoloOriginale) {
		super("dividiRigaOrdineTableModel", new String[] { "qta", "dataConsegna" }, RigaArticolo.class);
		this.qtaTot = rigaArticoloOriginale.getQta();
		RigaArticolo rigaClone = (RigaArticolo) PanjeaSwingUtil.cloneObject(rigaArticoloOriginale);
		rigaClone.setQta(null);
		source.set(0, rigaClone);
	}

	@Override
	protected RigaArticolo createNewObject() {
		RigaArticolo riga = super.createNewObject();
		double qtaRimanente = qtaTot;
		for (RigaArticolo rigaArticolo : source) {
			qtaRimanente -= rigaArticolo.getQta();
		}
		riga.setQta(qtaRimanente);
		return riga;
	}

}
