/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.contabilita.rich.pm.RigaContoCentroCostoConfronto;
import it.eurotn.panjea.contabilita.rich.pm.RigaContoConfronto;
import it.eurotn.panjea.contabilita.rich.pm.RigaMastroConfronto;
import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;

import java.math.BigDecimal;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author leonardo
 */
public class RigheBilancioConfrontoTableModel extends DefaultTreeTableModel {

	/**
	 * Costruttore.
	 * 
	 * @param node
	 *            root node
	 */
	public RigheBilancioConfrontoTableModel(final TreeTableNode node) {
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
			return BigDecimal.class;// saldo 1 per
		case 5:
			return BigDecimal.class;// saldo 2 per
		case 6:
			return BigDecimal.class;// scostamento
		case 7:
			return BigDecimal.class;// scostamento %
		default:
			return super.getColumnClass(column);
		}
	}

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(final int column) {
		return RcpSupport.getMessage("RigheBilancioConfrontoTableColumn" + column);
	}

	@Override
	public Object getValueAt(Object node, int column) {
		Object object = ((DefaultMutableTreeTableNode) node).getUserObject();
		if (object instanceof RigaMastroConfronto) {
			RigaMastroConfronto mastro = (RigaMastroConfronto) object;
			switch (column) {
			case 0:
				return mastro.getMastroCodice();
			case 1:
				return mastro.getMastroDescrizione();
			case 4:
				return mastro.getSaldo();
			case 5:
				return mastro.getSaldo2();
			case 6:
				return mastro.getDifferenzaSaldi();
			case 7:
				return mastro.getPercentualeSaldi();
			default:
				return "";
			}
		} else if (object instanceof RigaContoConfronto) {
			RigaContoConfronto conto = (RigaContoConfronto) object;
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
				return conto.getSaldo();
			case 5:
				return conto.getSaldo2();
			case 6:
				return conto.getDifferenzaSaldi();
			case 7:
				return conto.getPercentualeSaldi();
			default:
				return "";
			}
		} else if (object instanceof RigaContoCentroCostoConfronto) {
			RigaContoCentroCostoConfronto centroCosto = (RigaContoCentroCostoConfronto) object;
			switch (column) {
			case 0:
				return centroCosto.getCentroCostoCodice();
			case 1:
				return centroCosto.getCentroCostoDescrizione();
			case 4:
				return centroCosto.getSaldo();
			case 5:
				return centroCosto.getSaldo2();
			case 6:
				return centroCosto.getDifferenzaSaldi();
			case 7:
				return centroCosto.getPercentualeSaldi();
			default:
				return "";
			}
		} else if (object instanceof SaldoContoConfronto) {
			SaldoContoConfronto sottoConto = (SaldoContoConfronto) object;
			switch (column) {
			case 0:
				return sottoConto.getSottoContoCodice();
			case 1:
				return sottoConto.getSottoContoDescrizione();
			case 4:
				return sottoConto.getSaldo();
			case 5:
				return sottoConto.getSaldo2();
			case 6:
				return sottoConto.getDifferenzaSaldi();
			case 7:
				return sottoConto.getPercentualeSaldi();
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
