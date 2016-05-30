package it.eurotn.panjea.ordini.rich.forms.righeordine.componenti;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.TreeTableModel;

public class RigheArticoliComponentiDistintaTreeTableModel extends TreeTableModel<RigaArticoloComponenteOrdineRow> {

	private static final long serialVersionUID = -1930483679348136481L;

	private static ConverterContext numberQtaContext = new NumberWithDecimalConverterContext(0);

	/**
	 * @param righeComponenti
	 *            componenti del livello 0 della distinta
	 */
	public RigheArticoliComponentiDistintaTreeTableModel(final Set<IRigaArticoloDocumento> righeComponenti) {
		super();
		// List<ComponenteRow> result = new ArrayList<ComponenteRow>();
		// for (IRigaArticoloDocumento rigaComponenti : righeComponenti) {
		// result.add(creaRiga(righeComponenti));
		// }
		List<RigaArticoloComponenteOrdineRow> result = new ArrayList<RigaArticoloComponenteOrdineRow>();
		for (IRigaArticoloDocumento rigaArticoloDocumento : righeComponenti) {
			result.add(new RigaArticoloComponenteOrdineRow((RigaArticolo) rigaArticoloDocumento));
		}
		setOriginalRows(result);
	}

	// /**
	// *
	// * @param componenti
	// * componente che la riga wrappa
	// * @return Row che wrappa il componente.
	// */
	// public ComponenteRow creaRiga(Set<IRigaArticoloDocumento> componenti) {
	// ComponenteRow result = new RigaArticoloComponenteOrdineRow(componenti.);
	// for (Componente componenteFiglio : componenti.getArticolo().getComponenti()) {
	// result.addChild(creaRiga(componenteFiglio));
	// }
	// return result;
	// }

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Articolo";
		case 1:
			return "Quantità";
		case 2:
			return "Data prod.";
		case 3:
			return "Data consegna";
		default:
			return "na";
		}
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int col) {
		if (col == 1) {
			RigaArticoloComponenteOrdineRow rowAt = getRowAt(row);
			RigaArticolo rigaArticoloComponente = rowAt.getRigaArticoloComponente();
			Integer numeroDecimaliQta = rigaArticoloComponente.getNumeroDecimaliQta();
			numberQtaContext.setUserObject(numeroDecimaliQta);
			return numberQtaContext;
		}
		return super.getConverterContextAt(row, col);
	}
}
