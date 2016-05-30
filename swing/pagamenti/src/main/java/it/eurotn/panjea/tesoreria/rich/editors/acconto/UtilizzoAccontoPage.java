package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;
import it.eurotn.rich.control.table.style.FocusCellStyle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;
import org.springframework.richclient.util.GuiStandardUtils;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.pivot.AggregateTable;

public class UtilizzoAccontoPage extends AbstractDialogPage implements Memento {

	private class UtilizzoAccontoCellStyleProvider extends DefaultCellStyleProvider {

		private final CellStyle errorCellStyle = new CellStyle();
		private final CellStyle validCellStyle = new CellStyle();

		/**
		 * Costruttore.
		 */
		public UtilizzoAccontoCellStyleProvider() {
			super();
			errorCellStyle.setBackground(new Color(255, 99, 99));
			validCellStyle.setBackground(new Color(99, 255, 99));
		}

		@Override
		public CellStyle getCellStyleAt(TableModel tablemodel, int row, int column) {

			BigDecimal residuoRata = (BigDecimal) tablemodel.getValueAt(row, 6);
			BigDecimal accontoRata = (BigDecimal) tablemodel.getValueAt(row, 5);

			if (residuoRata.compareTo(BigDecimal.ZERO) < 0) {
				return errorCellStyle;
			} else if (accontoRata.compareTo(BigDecimal.ZERO) > 0) {
				return validCellStyle;
			} else {
				return super.getCellStyleAt(tablemodel, row, column);
			}
		}

	}

	public static final String PAGE_ID = "utilizzoAccontoPage";

	private final ITesoreriaBD tesoreriaBD;
	private final AreaAcconto areaAcconto;

	private BigDecimal residuo = BigDecimal.ZERO;
	private final DecimalFormat decimalFormat = new DecimalFormat("Residuo: #,##0.00;Residuo: -#,##0.00");
	private JLabel residuoLabel = null;

	private JideTableWidget<SituazioneRataUtilizzoAcconto> table;
	private UtilizzoAccontoTableModel tableModel;

	/**
	 * Costruttore.
	 * 
	 * @param tesoreriaBD
	 *            tesoreriaBD
	 * @param areaAcconto
	 *            area acconto
	 */
	protected UtilizzoAccontoPage(final ITesoreriaBD tesoreriaBD, final AreaAcconto areaAcconto) {
		super(PAGE_ID);
		this.tesoreriaBD = tesoreriaBD;
		this.areaAcconto = areaAcconto;
		this.residuo = this.areaAcconto.getResiduo();
	}

	@Override
	protected JComponent createControl() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(createHeaderComponent(), BorderLayout.NORTH);
		rootPanel.add(createTableComponent(), BorderLayout.CENTER);
		return rootPanel;
	}

	/**
	 * Crea i componenti che vanno posizionati sopra la tabella.
	 * 
	 * @return componenti creati
	 */
	private JComponent createHeaderComponent() {
		JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT));

		residuoLabel = new JLabel(decimalFormat.format(residuo));
		Font f = residuoLabel.getFont();
		// bold
		residuoLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
		panel.add(residuoLabel);

		GuiStandardUtils.attachBorder(panel);
		return panel;
	}

	/**
	 * Crea i componenti per la tabella.
	 * 
	 * @return componenti creati
	 */
	private JComponent createTableComponent() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());

		tableModel = new UtilizzoAccontoTableModel(PAGE_ID + ".tableModel") {

			private static final long serialVersionUID = -7037378286081746330L;

			@Override
			public void setValueAt(Object editedValue, int row, int column) {
				super.setValueAt(editedValue, row, column);
				if (column == 5) {
					updateResiduoTotale();
				}
			}
		};

		table = new JideTableWidget<SituazioneRataUtilizzoAcconto>(PAGE_ID + "table", tableModel);

		((AggregateTable) table.getTable()).getAggregateTableModel().setCellStyleProvider(
				new UtilizzoAccontoCellStyleProvider());
		((CellStyleTable) table.getTable()).setFocusCellStyle(new FocusCellStyle());

		panel.add(table.getComponent(), BorderLayout.CENTER);

		table.setRows(getSituazioniRata());

		return panel;
	}

	/**
	 * @return lista delle {@link SituazioneRataUtilizzoAcconto}.
	 */
	public List<SituazioneRataUtilizzoAcconto> getSituazioneRataUtilizzoAcconto() {
		return tableModel.getObjects();
	}

	/**
	 * Restituisce la lista di rate che possono essere pagate con l'acconto.
	 * 
	 * @return rate
	 */
	private List<SituazioneRataUtilizzoAcconto> getSituazioniRata() {
		EntitaLite entitaLite = areaAcconto.getDocumento().getEntita();
		TipoPartita tipoPartita = ((entitaLite instanceof ClienteLite) ? TipoPartita.ATTIVA : TipoPartita.PASSIVA);

		List<SituazioneRata> rate = tesoreriaBD.caricaSituazioneRateDaUtilizzarePerAcconto(entitaLite.getId(),
				tipoPartita);

		List<SituazioneRataUtilizzoAcconto> listResult = new ArrayList<SituazioneRataUtilizzoAcconto>();
		for (SituazioneRata situazioneRata : rate) {
			listResult.add(new SituazioneRataUtilizzoAcconto(situazioneRata));
		}
		return listResult;
	}

	/**
	 * @return indica se l'importo dell'acconto è stato assegnato in maniera valida.
	 */
	public boolean isImportoAccontoValido() {
		boolean importoValido = true;
		if (residuo.compareTo(BigDecimal.ZERO) < 0) {
			importoValido = false;
		}
		for (SituazioneRataUtilizzoAcconto situazioneRataUtilizzoAcconto : tableModel.getObjects()) {
			if (situazioneRataUtilizzoAcconto.getImportoResiduo().compareTo(BigDecimal.ZERO) < 0) {
				importoValido = false;
				break;
			}
		}
		return importoValido;
	}

	@Override
	public void restoreState(Settings settings) {
		table.restoreState(settings);

	}

	@Override
	public void saveState(Settings settings) {
		table.saveState(settings);
	}

	/**
	 * Aggiorna la label del residuo totale con il valore calcolato dalle righe.
	 */
	private void updateResiduoTotale() {
		BigDecimal accontoUtilizzato = BigDecimal.ZERO;
		for (SituazioneRataUtilizzoAcconto situazioneRataUtilizzoAcconto : tableModel.getObjects()) {
			accontoUtilizzato = accontoUtilizzato.add(situazioneRataUtilizzoAcconto.getImportoAcconto());
		}
		residuo = areaAcconto.getResiduo().subtract(accontoUtilizzato);
		residuoLabel.setText(decimalFormat.format(residuo));

		if (residuo.compareTo(BigDecimal.ZERO) < 0) {
			residuoLabel.setForeground(Color.RED);
		} else {
			residuoLabel.setForeground(Color.BLACK);
		}
	}
}
