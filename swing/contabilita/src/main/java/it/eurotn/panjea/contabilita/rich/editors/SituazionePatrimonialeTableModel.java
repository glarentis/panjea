/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.contabilita.domain.SottoConto;
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
public class SituazionePatrimonialeTableModel extends DefaultTreeTableModel {

	/**
	 * SituazionePatrimonialeTableModel.
	 * 
	 * @param node
	 *            tree table node
	 */
	public SituazionePatrimonialeTableModel(final TreeTableNode node) {
		super(node);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return TreeTableModel.class; // tree
		case 1:
			return String.class; // cod. mastro/conto/sottoconto
		case 2:
			return String.class; // desc. mastro/conto/sottoconto
		case 3:
			return BigDecimal.class; // Saldo Attivita
		case 4:
			return String.class;
		case 5:
			return TreeTableModel.class; // cod mastro/conto/sottoconto
		case 6:
			return String.class; // desc. mastro/conto/sottoconto
		case 7:
			return BigDecimal.class; // Saldo Passivita'
		default:
			throw new UnsupportedOperationException("La colonna " + column + " non esiste");
		}
	}

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(final int column) {
		return RcpSupport.getMessage("SituazioneEPTableColumn" + column);
	}

	@Override
	public Object getValueAt(Object node, int column) {
		Object object = ((DefaultMutableTreeTableNode) node).getUserObject();

		if (object instanceof RigaMastro) {
			RigaMastro mastro = (RigaMastro) object;

			switch (column) {
			case 0:
				return "";
			case 1:
				return mastro.getSaldo().signum() == 1 ? mastro.getMastroCodice() : "";
			case 2:
				return mastro.getSaldo().signum() == 1 ? mastro.getMastroDescrizione() : "";
			case 3:
				return mastro.getSaldo().signum() == 1 ? mastro.getSaldo().abs() : "";
			case 4:
				return "";
			case 5:
				return mastro.getSaldo().signum() == -1 ? mastro.getMastroCodice() : "";
			case 6:
				return mastro.getSaldo().signum() == -1 ? mastro.getMastroDescrizione() : "";
			case 7:
				return mastro.getSaldo().signum() == -1 ? mastro.getSaldo().abs() : "";
			default:
				return "";
			}
		} else if (object instanceof RigaConto) {
			RigaConto conto = (RigaConto) object;

			switch (column) {
			case 0:
				return "";
			case 1:
				return conto.getSaldo().signum() == 1 ? conto.getContoCodice() : "";
			case 2:
				return conto.getSaldo().signum() == 1 ? conto.getContoDescrizione() : "";
			case 3:
				return conto.getSaldo().signum() == 1 ? conto.getSaldo().abs() : "";
			case 4:
				return "";
			case 5:
				return conto.getSaldo().signum() == -1 ? conto.getContoCodice() : "";
			case 6:
				return conto.getSaldo().signum() == -1 ? conto.getContoDescrizione() : "";
			case 7:
				return conto.getSaldo().signum() == -1 ? conto.getSaldo().abs() : "";
			default:
				return "";
			}
		} else if (object instanceof RigaContoCentroCosto) {
			RigaContoCentroCosto conto = (RigaContoCentroCosto) object;
			switch (column) {
			case 0:
				return "";
			case 1:
				return conto.getSaldo().signum() == 1 ? conto.getCentroCostoCodice() : "";
			case 2:
				return conto.getSaldo().signum() == 1 ? conto.getCentroCostoDescrizione() : "";
			case 3:
				return conto.getSaldo().signum() == 1 ? conto.getSaldo().abs() : "";
			case 4:
				return "";
			case 5:
				return conto.getSaldo().signum() == -1 ? conto.getCentroCostoCodice() : "";
			case 6:
				return conto.getSaldo().signum() == -1 ? conto.getCentroCostoDescrizione() : "";
			case 7:
				return conto.getSaldo().signum() == -1 ? conto.getSaldo().abs() : "";
			default:
				return "";
			}
		} else if (object instanceof SaldoConto) {
			SaldoConto sottoConto = (SaldoConto) object;
			if ((sottoConto == null) || (SottoConto.DEFAULT_CODICE.equals(sottoConto.getSottoContoCodice()))) {
				return "";
			}

			switch (column) {
			case 0:
				return "";
			case 1:
				return sottoConto.getSaldo().signum() == 1 ? sottoConto.getSottoContoCodice() : "";
			case 2:
				return sottoConto.getSaldo().signum() == 1 ? sottoConto.getSottoContoDescrizione() : "";
			case 3:
				return sottoConto.getSaldo().signum() == 1 ? sottoConto.getSaldo().abs() : "";
			case 4:
				return "";
			case 5:
				return sottoConto.getSaldo().signum() == -1 ? sottoConto.getSottoContoCodice() : "";
			case 6:
				return sottoConto.getSaldo().signum() == -1 ? sottoConto.getSottoContoDescrizione() : "";
			case 7:
				return sottoConto.getSaldo().signum() == -1 ? sottoConto.getSaldo().abs() : "";
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