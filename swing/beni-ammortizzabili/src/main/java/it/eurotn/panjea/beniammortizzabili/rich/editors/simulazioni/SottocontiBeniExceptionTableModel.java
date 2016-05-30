/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.SottocontiBeni;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Color;

import javax.swing.BorderFactory;

import org.apache.commons.lang3.ObjectUtils;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleTableModel;

/**
 * @author fattazzo
 *
 */
public class SottocontiBeniExceptionTableModel extends DefaultBeanTableModel<EntitaSottoContiPM> implements
		StyleTableModel {

	private static final long serialVersionUID = 3949973677988440084L;

	public static final CellStyle SOTTOCONTO_EREDITATO_STYLE = new CellStyle();

	static {
		SOTTOCONTO_EREDITATO_STYLE.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
	}

	/**
	 * Costruttore.
	 */
	public SottocontiBeniExceptionTableModel() {
		super("sottocontiBeniExceptionTableModel", new String[] { "entity", "sottocontiBeni.sottoContoAmmortamento",
				"sottocontiBeni.sottoContoFondoAmmortamento", "sottocontiBeni.sottoContoAmmortamentoAnticipato",
				"sottocontiBeni.sottoContoFondoAmmortamentoAnticipato" }, EntitaSottoContiPM.class);
	}

	@Override
	public CellStyle getCellStyleAt(int row, int column) {
		EntitaSottoContiPM pm = getElementAt(row);
		SottocontiBeni sottocontiOriginali = getSottoContiBeniEntity(pm);

		switch (column) {
		case 1:
			return ObjectUtils.equals(sottocontiOriginali.getSottoContoAmmortamento(), pm.getSottocontiBeni()
					.getSottoContoAmmortamento()) ? null : SOTTOCONTO_EREDITATO_STYLE;
		case 2:
			return ObjectUtils.equals(sottocontiOriginali.getSottoContoFondoAmmortamento(), pm.getSottocontiBeni()
					.getSottoContoFondoAmmortamento()) ? null : SOTTOCONTO_EREDITATO_STYLE;
		case 3:
			return ObjectUtils.equals(sottocontiOriginali.getSottoContoAmmortamentoAnticipato(), pm.getSottocontiBeni()
					.getSottoContoAmmortamentoAnticipato()) ? null : SOTTOCONTO_EREDITATO_STYLE;
		case 4:
			return ObjectUtils.equals(sottocontiOriginali.getSottoContoFondoAmmortamentoAnticipato(), pm
					.getSottocontiBeni().getSottoContoFondoAmmortamentoAnticipato()) ? null
					: SOTTOCONTO_EREDITATO_STYLE;
		default:
			return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int column) {
		if (column == 0 && getRowCount() > 0) {
			EntitaSottoContiPM pm = getElementAt(0);
			return pm.getEntityClass();
		}
		return super.getColumnClass(column);
	}

	private SottocontiBeni getSottoContiBeniEntity(EntitaSottoContiPM pm) {
		SottocontiBeni result = new SottocontiBeni();

		if (Specie.class.equals(pm.getEntityClass())) {
			result = ((Specie) pm.getEntity()).getSottocontiBeni();
		} else if (SottoSpecie.class.equals(pm.getEntityClass())) {
			result = ((SottoSpecie) pm.getEntity()).getSottocontiBeni();
		} else if (BeneAmmortizzabileLite.class.equals(pm.getEntityClass())) {
			result = ((BeneAmmortizzabileLite) pm.getEntity()).getSottocontiBeni();
		}

		return result;
	}

	@Override
	public boolean isCellStyleOn() {
		return true;
	}

}
