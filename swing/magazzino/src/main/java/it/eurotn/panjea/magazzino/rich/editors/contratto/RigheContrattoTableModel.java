package it.eurotn.panjea.magazzino.rich.editors.contratto;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class RigheContrattoTableModel extends DefaultBeanTableModel<RigaContratto> {

	private static final long serialVersionUID = 8763032563575634179L;

	/**
	 * costruiosce il table model per la riga contratto.
	 * 
	 * @param idPage
	 *            id della pagina da usare per I18N
	 */
	public RigheContrattoTableModel(final String idPage) {
		super(idPage, new String[] { "Articolo" }, RigaContratto.class);
	}

	@Override
	public Class<?> getCellClassAt(int row, int column) {
		RigaContratto rigaContratto = getObject(row);
		if (rigaContratto.getCategoriaCommercialeArticolo() != null
				&& rigaContratto.getCategoriaCommercialeArticolo().getId() != null) {
			return CategoriaCommercialeArticolo.class;
		}

		if (rigaContratto.getArticolo() != null && rigaContratto.getArticolo().getId() != null) {
			return ArticoloLite.class;
		}
		return Categoria.class;
	}

	@Override
	public Object getValueAt(int row, int column) {
		RigaContratto rigaContratto = getObject(row);
		if (rigaContratto.getTutteLeCategorie()) {
			Categoria categoria = new Categoria();
			categoria.setCodice("---TUTTI");
			categoria.setDescrizione("Tutte le categorie---");
			categoria.setId(20);
			return categoria;
		}

		if (rigaContratto.getArticolo() != null && rigaContratto.getArticolo().getId() != null) {
			return rigaContratto.getArticolo();
		}
		return rigaContratto.getCategoriaCommercialeArticolo();
	}
}
