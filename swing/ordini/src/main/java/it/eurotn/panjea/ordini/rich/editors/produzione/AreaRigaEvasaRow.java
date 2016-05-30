package it.eurotn.panjea.ordini.rich.editors.produzione;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import com.jidesoft.grid.DefaultExpandableRow;

public class AreaRigaEvasaRow extends DefaultExpandableRow {

	private AreaMagazzino areaMagazzino;

	/**
	 * Costruttore.
	 *
	 * @param areaMagazzino area magazzino
	 */
	public AreaRigaEvasaRow(final AreaMagazzino areaMagazzino) {
		super();
		this.areaMagazzino = areaMagazzino;
	}

	@Override
	public Class<?> getCellClassAt(int column) {
		switch (column) {
		case 0:
			return Documento.class;
		default:
			return Double.class;
		}
	}

	@Override
	public Object getValueAt(int column) {
		switch (column) {
		case 0:
			return areaMagazzino.getDocumento();
		default:
			return null;
		}
	}

}
