package it.eurotn.panjea.contabilita.rich.editors.righecontabili.verificaCentriCosto;

import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.rich.editors.righecontabili.RigheContabiliTableModel;

import java.awt.Color;

import javax.swing.BorderFactory;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.HierarchicalTableModel;
import com.jidesoft.grid.StyleTableModel;

public class RigheContabiliCentriDiCostoTableModel extends RigheContabiliTableModel implements HierarchicalTableModel,
StyleTableModel {
	private static final long serialVersionUID = 8103831970318817465L;
	public static final CellStyle INVALID_STYLE = new CellStyle();
	private boolean modificato;

	static {
		INVALID_STYLE.setBorder(BorderFactory.createLineBorder(Color.RED));
	}

	/**
	 * Costruttore.
	 */
	public RigheContabiliCentriDiCostoTableModel() {
		super("righeContabiliCentriDiCostoTableModel");
		modificato = false;
	}

	@Override
	public CellStyle getCellStyleAt(int paramInt1, int paramInt2) {
		if (!getElementAt(paramInt1).isValid()) {
			return INVALID_STYLE;
		}
		return null;
	}

	@Override
	public Object getChildValueAt(int paramInt) {
		return getElementAt(paramInt).getRigheCentroCosto();
	}

	@Override
	public boolean hasChild(int paramInt) {
		return true;
	}

	@Override
	public boolean isCellStyleOn() {
		return true;
	}

	@Override
	public boolean isExpandable(int paramInt) {
		return getElementAt(paramInt).getConto().isSoggettoCentroCosto();
	}

	@Override
	public boolean isHierarchical(int paramInt) {
		return getElementAt(paramInt).getConto().isSoggettoCentroCosto();
	}

	/**
	 *
	 * @return true se è stata chiamata la setObject
	 */
	public boolean isModificato() {
		return modificato;
	}

	@Override
	public void setObject(RigaContabile object) {
		super.setObject(object);
		modificato = true;
	}

}
