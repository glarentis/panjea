package it.eurotn.panjea.ordini.rich.editors.produzione;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;

import com.jidesoft.grid.DefaultExpandableRow;

public class RigaDistintaEvasaRow extends DefaultExpandableRow {

	protected RigaArticolo rigaArticolo;

	/**
	 * Costruttore.
	 *
	 * @param rigaArticolo
	 *            riga distinta
	 */
	public RigaDistintaEvasaRow(final RigaArticolo rigaArticolo) {
		super();
		this.rigaArticolo = rigaArticolo;

		addRigheChild(rigaArticolo, this);
	}

	/**
	 * aggiunge una riga componente alla rigaDistinta
	 *
	 * @param rigaComponente
	 *            articolo
	 */
	public void addRigaComponente(RigaArticolo rigaComponente) {
		addChild(new RigaComponenteRow(rigaComponente));
	}

	private void addRigheChild(RigaArticolo riga, DefaultExpandableRow parentRow) {

		if (riga instanceof RigaArticoloDistinta) {
			for (IRigaArticoloDocumento componente : ((RigaArticoloDistinta) riga).getComponenti()) {

				if (componente instanceof RigaArticoloComponente) {
					parentRow.addChild(new RigaComponenteRow((RigaArticolo) componente));
				}
			}
		}
	}

	@Override
	public Class<?> getCellClassAt(int column) {
		switch (column) {
		case 0:
			return ArticoloLite.class;
		default:
			return Double.class;
		}
	}

	/**
	 * @return Returns the rigaArticolo.
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	@Override
	public Object getValueAt(int column) {
		switch (column) {
		case 0:
			return rigaArticolo.getArticolo();
		default:
			return rigaArticolo.getQta();
		}
	}

}
