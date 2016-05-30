package it.eurotn.panjea.ordini.rich.forms.righeordine.componenti;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.DefaultExpandableRow;

public class RigaArticoloComponenteOrdineRow extends DefaultExpandableRow {
	private RigaArticolo rigaArticoloComponente;

	/**
	 *
	 * @param rigaArticoloComponente
	 *            rigaArticoloComponente
	 */
	public RigaArticoloComponenteOrdineRow(final RigaArticolo rigaArticoloComponente) {
		super();
		this.rigaArticoloComponente = rigaArticoloComponente;
		setExpandable(true);
	}

	@Override
	public Class<?> getCellClassAt(int column) {
		switch (column) {
		case 0:
			return ArticoloLite.class;
		case 1:
			return Double.class;
		case 2:
			return Date.class;
		case 3:
			return Date.class;
		default:
			return null;
		}
	}

	@Override
	public List<?> getChildren() {
		List<RigaArticoloComponenteOrdineRow> children = new ArrayList<RigaArticoloComponenteOrdineRow>(
				rigaArticoloComponente.getComponenti().size());
		for (it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento iRigaArticoloDocumento : rigaArticoloComponente
				.getComponenti()) {
			children.add(new RigaArticoloComponenteOrdineRow((RigaArticolo) iRigaArticoloDocumento));
		}
		return children;
	}

	@Override
	public int getChildrenCount() {
		return rigaArticoloComponente.getComponenti().size();
	}

	/**
	 * @return Returns the rigaArticoloComponente.
	 */
	public RigaArticolo getRigaArticoloComponente() {
		return rigaArticoloComponente;
	}

	@Override
	public Object getValueAt(int column) {
		switch (column) {
		case 0:
			return rigaArticoloComponente.getArticolo();
		case 1:
			return rigaArticoloComponente.getQta();
		case 2:
			return rigaArticoloComponente.getDataProduzione();
		case 3:
			return rigaArticoloComponente.getDataConsegna();
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int columnIndex) {
		return columnIndex > 1;
	}

	@Override
	public void rowUpdated() {
		System.out.println("DEBUG:RigaArticoloComponenteOrdineRow->rowUpdated:");
		super.rowUpdated();
	}

	@Override
	public void setValueAt(Object paramObject, int column) {
		switch (column) {
		case 2:
			rigaArticoloComponente.setDataProduzione((Date) paramObject);
			break;
		case 3:
			rigaArticoloComponente.setDataConsegna((Date) paramObject);
			break;
		default:
			break;
		}
		IOrdiniDocumentoBD bd = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
		rigaArticoloComponente = (RigaArticolo) bd.salvaRigaOrdine(rigaArticoloComponente);
	}
}