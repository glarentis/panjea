package it.eurotn.panjea.magazzino.rich.editors.schedearticolo;

import it.eurotn.panjea.magazzino.util.SituazioneSchedaArticoloDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class MeseSchedeArticoloCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -8421079767779397558L;

	public static final EditorContext GIACENZA_REALE_CONTEXT = new EditorContext("GIACENZA_REALE_CONTEXT", 6);

	private static final Icon SCHEDA_ARTICOLO_NON_CREATA_ICON;
	private static final Icon SCHEDA_ARTICOLO_NON_VALIDA_ICON;
	private static final Icon SCHEDA_ARTICOLO_STAMPATA_ICON;

	static {
		SCHEDA_ARTICOLO_NON_CREATA_ICON = RcpSupport.getIcon("schedaArticoloNonCreata.icon");
		SCHEDA_ARTICOLO_NON_VALIDA_ICON = RcpSupport.getIcon("schedaArticoloNonStampata.icon");
		SCHEDA_ARTICOLO_STAMPATA_ICON = RcpSupport.getIcon("schedaArticoloStampata.icon");
	}

	/**
	 * Costruttore.
	 * 
	 */
	public MeseSchedeArticoloCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<SituazioneSchedaArticoloDTO> tableModel = (DefaultBeanTableModel<SituazioneSchedaArticoloDTO>) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		label.setIcon(SCHEDA_ARTICOLO_NON_CREATA_ICON);
		label.setToolTipText(null);

		SituazioneSchedaArticoloDTO situazioneSchedaArticoloDTO = tableModel.getObject(actualRow);
		String mese = RcpSupport.getMessage("mese." + situazioneSchedaArticoloDTO.getMese());
		label.setText(mese);

		// se non ci sono articoli o se ci sono solo articoli stampati la scheda è valida
		boolean nessunArticolo = situazioneSchedaArticoloDTO.getArticoliNonValidi() == 0
				&& situazioneSchedaArticoloDTO.getArticoliRimanenti() == 0
				&& situazioneSchedaArticoloDTO.getArticoliStampati() == 0;

		boolean tuttiArticoliStampati = situazioneSchedaArticoloDTO.getArticoliNonValidi() == 0
				&& situazioneSchedaArticoloDTO.getArticoliRimanenti() == 0
				&& situazioneSchedaArticoloDTO.getArticoliStampati() > 0;

		boolean nonTuttiArticoliStampati = situazioneSchedaArticoloDTO.getArticoliStampati() > 0
				&& (situazioneSchedaArticoloDTO.getArticoliNonValidi() > 0 || situazioneSchedaArticoloDTO
						.getArticoliRimanenti() > 0);
		if (tuttiArticoliStampati || nessunArticolo) {
			label.setIcon(SCHEDA_ARTICOLO_STAMPATA_ICON);
		} else if (nonTuttiArticoliStampati) {
			label.setIcon(SCHEDA_ARTICOLO_NON_VALIDA_ICON);
		}

		return label;
	}
}
