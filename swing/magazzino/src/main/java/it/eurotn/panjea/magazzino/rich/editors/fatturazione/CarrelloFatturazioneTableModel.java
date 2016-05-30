/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleModel;

/**
 * @author fattazzo
 * 
 */
public class CarrelloFatturazioneTableModel extends DefaultBeanTableModel<AreaMagazzinoLitePM> implements StyleModel {

	private static final long serialVersionUID = 6236746647686716949L;

	protected static final Color DOWN = new Color(204, 0, 0);

	private static final CellStyle ALERT_CELL_STYLE;

	private final Calendar calendarDataRiferimento = Calendar.getInstance();
	private final Calendar calendarDataArea = Calendar.getInstance();

	static {
		ALERT_CELL_STYLE = new CellStyle();
		ALERT_CELL_STYLE.setText(null);
		ALERT_CELL_STYLE.setToolTipText(null);
		ALERT_CELL_STYLE.setBorder(null);
		ALERT_CELL_STYLE.setIcon(null);
		ALERT_CELL_STYLE.setFontStyle(-1);
		ALERT_CELL_STYLE.setForeground(Color.WHITE);
		ALERT_CELL_STYLE.setBackground(DOWN);
	}

	/**
	 * Costruttore.
	 */
	public CarrelloFatturazioneTableModel() {
		super("CarrelloFatturazioneTableModel",
				new String[] { "areaMagazzinoLite.documento.tipoDocumento.codice",
						"areaMagazzinoLite.documento.tipoDocumento.descrizione", "areaMagazzinoLite.dataRegistrazione",
						"areaMagazzinoLite.documento.codice", "areaMagazzinoLite.documento.entita",
						"areaMagazzinoLite.documento.sedeEntita.sede.descrizione",
						"areaMagazzinoLite.documento.dataDocumento" }, AreaMagazzinoLitePM.class);
	}

	@Override
	public CellStyle getCellStyleAt(int row, int column) {
		AreaMagazzinoLitePM area = getElementAt(row);

		calendarDataArea.setTime(area.getAreaMagazzinoLite().getDocumento().getDataDocumento());

		if (calendarDataRiferimento.getTimeInMillis() == 0
				|| calendarDataArea.getTime().after(calendarDataRiferimento.getTime())
				|| calendarDataArea.get(Calendar.YEAR) != calendarDataRiferimento.get(Calendar.YEAR)) {
			return ALERT_CELL_STYLE;
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellStyleOn() {
		return true;
	}

	/**
	 * @param dataRiferimento
	 *            data di riferimento per il controllo
	 */
	public void setDataRiferimento(Date dataRiferimento) {
		if (dataRiferimento == null) {
			calendarDataRiferimento.setTimeInMillis(0);
		} else {
			calendarDataRiferimento.setTime(dataRiferimento);
		}
	}

}
