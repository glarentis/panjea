package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Color;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.NavigableTableModel;
import com.jidesoft.grid.StyleModel;

/**
 * TreeTableModel dove vengono descritti: il numero delle colonne; i tipi per ogni colonna e la proprieta' da
 * visualizzare per ogni colonna.
 *
 * @author Leonardo
 */
public class RisultatiRicercaRateTableModel extends DefaultBeanTableModel<SituazioneRata> implements StyleModel,
NavigableTableModel {

	private static final long serialVersionUID = -17487279545300254L;

	private static final String[] DEFAULT_COLS = new String[] { "statoCarrello", "statoRata", "rata.dataScadenza",
		"rata.importo", "rata.importo.codiceValuta", "rata.tipoPagamento", "residuoRata", "rata.numeroRata",
		"documento.totale", "documento.dataDocumento", "documento.tipoDocumento", "documento.codice", "entita",
		"protocollo", "rata.importo.tassoDiCambio", "rata.note", "rata.areaRate.codicePagamento",
		"documento.sedeEntita", "documento.sedeEntita.zonaGeografica" };
	private static final String[] AGENTE_COLS = new String[] { "agente" };

	protected static final Color RATA_PASSIVA_COLOR = new Color(204, 0, 0);

	private boolean compensazione;

	private CellStyle cellStyle;

	{
		compensazione = Boolean.FALSE;

		cellStyle = new CellStyle();
	}

	/**
	 * Costruttore.
	 *
	 * @param modelId
	 *            id del modello
	 */
	public RisultatiRicercaRateTableModel(final String modelId) {
		super(
				modelId,
				((PluginManager) RcpSupport.getBean("pluginManager")).isPresente(PluginManager.PLUGIN_AGENTI) ? PanjeaSwingUtil
						.concatArray(DEFAULT_COLS, AGENTE_COLS) : DEFAULT_COLS, SituazioneRata.class);
	}

	@Override
	public CellStyle getCellStyleAt(int row, int column) {
		SituazioneRata situazioneRata = getElementAt(row);
		cellStyle.setForeground(Color.BLACK);
		if (situazioneRata.getRata().getAreaRate().getTipoAreaPartita().getTipoPartita() == TipoPartita.PASSIVA) {
			cellStyle.setForeground(RATA_PASSIVA_COLOR);
		}
		return cellStyle;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		return null;
	}

	@Override
	public boolean isCellStyleOn() {
		return compensazione;
	}

	/**
	 * @return the compensazione
	 */
	public boolean isCompensazione() {
		return compensazione;
	}

	@Override
	public boolean isNavigableAt(int row, int col) {
		return col == 0;
	}

	@Override
	public boolean isNavigationOn() {
		return true;
	}

	/**
	 * @param compensazione
	 *            the compensazione to set
	 */
	public void setCompensazione(boolean compensazione) {
		this.compensazione = compensazione;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (getValueAt(row, col) != value) {
			super.setValueAt(value, row, col);
			// fireTableCellUpdated(row, col);
		}
	}
}
