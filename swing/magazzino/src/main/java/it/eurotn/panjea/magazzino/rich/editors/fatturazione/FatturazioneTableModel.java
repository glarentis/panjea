/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;

import java.util.Date;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class FatturazioneTableModel extends DefaultTreeTableModel {

	/**
	 * Costruttore.
	 * 
	 * @param root
	 *            root node
	 */
	public FatturazioneTableModel(final TreeTableNode root) {
		super(root);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return TreeTableModel.class; // date registrazione
		case 1:
			return Date.class; // data documento
		case 2:
			return String.class; // numero documento
		case 3:
			return EntitaLite.class; // entità
		case 4:
			return SedeEntita.class; // sede entita
		default:
			return super.getColumnClass(column);
		}
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(final int column) {
		return RcpSupport.getMessage(RisultatiRicercaFatturazioneTablePage.PAGE_ID + "Column" + column);
	}

	@Override
	public Object getValueAt(Object node, int column) {
		Object object = ((DefaultMutableTreeTableNode) node).getUserObject();

		if (object instanceof TipoDocumento) {
			TipoDocumento tipoDocumento = (TipoDocumento) object;

			switch (column) {
			case 0:
				return tipoDocumento.getCodice() + " - " + tipoDocumento.getDescrizione();
			case 1:
				return null;
			default:
				return "";
			}

		} else {
			if (object instanceof AreaMagazzinoLitePM) {
				AreaMagazzinoLitePM areaMagazzinoLitePM = (AreaMagazzinoLitePM) object;

				switch (column) {
				case 0:
					return areaMagazzinoLitePM.getAreaMagazzinoLite().getDataRegistrazione();
				case 1:
					return areaMagazzinoLitePM.getAreaMagazzinoLite().getDocumento().getDataDocumento();
				case 2:
					return areaMagazzinoLitePM.getAreaMagazzinoLite().getDocumento().getCodice().getCodice();
				case 3:
					return areaMagazzinoLitePM.getAreaMagazzinoLite().getDocumento().getEntita();
				case 4:
					return areaMagazzinoLitePM.getAreaMagazzinoLite().getDocumento().getSedeEntita();
				default:
					return super.getValueAt(node, column);
				}
			} else {
				if (object instanceof RigaArticoloLite) {
					RigaArticoloLite rigaArticoloLite = (RigaArticoloLite) object;

					switch (column) {
					case 0:
						return rigaArticoloLite.getArticolo().getDescrizione();
					default:
						return "";
					}
				}
			}
		}

		return super.getValueAt(node, column);
	}

	@Override
	public boolean isCellEditable(Object node, int column) {
		return false;
	}
}
