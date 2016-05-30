/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.contabilita.rich.pm.RigaConto;
import it.eurotn.panjea.contabilita.rich.pm.RigaContoCentroCosto;
import it.eurotn.panjea.contabilita.rich.pm.RigaMastro;
import it.eurotn.panjea.contabilita.util.SaldoConto;

import java.math.BigDecimal;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author leonardo
 */
public class RigheBilancioTableModel extends DefaultTreeTableModel {

	/**
	 * Costruttore.
	 * 
	 * @param node
	 *            root note
	 */
	public RigheBilancioTableModel(final TreeTableNode node) {
		super(node);
	}

	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return TreeTableModel.class;// cod mastro/conto/sottoconto
		case 1:
			return String.class;// desc. mastro/conto/sottoconto
		case 2:
			return Enum.class;// tipo conto
		case 3:
			return Enum.class;// sotto tipo conto
		case 4:
			return BigDecimal.class;// dare
		case 5:
			return BigDecimal.class;// avere
		case 6:
			return BigDecimal.class;// saldo
		default:
			return super.getColumnClass(column);
		}
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public String getColumnName(final int column) {
		return RcpSupport.getMessage("RigheBilancioTableColumn" + column);
	}

	@Override
	public Object getValueAt(Object node, int column) {
		Object object = ((DefaultMutableTreeTableNode) node).getUserObject();
		if (object instanceof RigaMastro) {
			RigaMastro mastro = (RigaMastro) object;
			switch (column) {
			case 0:
				return mastro.getMastroCodice();
			case 1:
				return mastro.getMastroDescrizione();
			case 4:
				return mastro.getImportoDare();
			case 5:
				return mastro.getImportoAvere();
			case 6:
				return mastro.getSaldo();
			default:
				return "";
			}
		} else if (object instanceof RigaConto) {
			RigaConto conto = (RigaConto) object;
			switch (column) {
			case 0:
				return conto.getContoCodice();
			case 1:
				return conto.getContoDescrizione();
			case 2:
				return conto.getTipoConto();
			case 3:
				return conto.getSottoTipoConto();
			case 4:
				return conto.getImportoDare();
			case 5:
				return conto.getImportoAvere();
			case 6:
				return conto.getSaldo();
			default:
				return "";
			}
		} else if (object instanceof RigaContoCentroCosto) {
			RigaContoCentroCosto conto = (RigaContoCentroCosto) object;
			switch (column) {
			case 0:
				return conto.getCentroCostoCodice();
			case 1:
				return conto.getCentroCostoDescrizione();
			case 2:
				return conto.getTipoConto();
			case 3:
				return conto.getSottoTipoConto();
			case 4:
				return conto.getImportoDare();
			case 5:
				return conto.getImportoAvere();
			case 6:
				return conto.getSaldo();
			default:
				return "";
			}
		} else if (object instanceof SaldoConto) {
			SaldoConto sottoConto = (SaldoConto) object;

			switch (column) {
			case 0:
				return sottoConto.getSottoContoCodice();
			case 1:
				return sottoConto.getSottoContoDescrizione();
			case 4:
				return sottoConto.getImportoDare();
			case 5:
				return sottoConto.getImportoAvere();
			case 6:
				return sottoConto.getSaldo();
			default:
				return "";
			}
		}
		return "";
	}

	@Override
	public boolean isCellEditable(Object arg0, int arg1) {
		return false;
	}
}
