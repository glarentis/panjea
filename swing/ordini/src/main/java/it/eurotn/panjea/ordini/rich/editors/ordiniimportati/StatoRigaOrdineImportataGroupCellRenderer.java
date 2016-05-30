package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.navigationloader.NavigatioLoaderContextSensitiveCellRenderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.IndexReferenceRow;
import com.jidesoft.grid.TableModelWrapperUtils;

public class StatoRigaOrdineImportataGroupCellRenderer extends NavigatioLoaderContextSensitiveCellRenderer {
	private static final long serialVersionUID = 3131358058774840111L;

	public static final EditorContext STATO_RIGA_ORDINE_IMPORTATA_CONTEXT = new EditorContext(
			"STATO_RIGA_ORDINE_IMPORTATA_CONTEXT");

	public static final String KEY_ICON_SELEZIONABILE = "false";
	public static final String KEY_ICON_SELEZIONATA = "true";
	private static final Icon ICON_SELEZIONABILE = RcpSupport.getIcon(KEY_ICON_SELEZIONABILE);
	private static final Icon ICON_SELEZIONATA = RcpSupport.getIcon(KEY_ICON_SELEZIONATA);
	private static final Icon ICON_STATO_INIZIALE_BLOCCATO = RcpSupport
			.getIcon("it.eurotn.panjea.ordini.domain.documento.AreaOrdine$StatoAreaOrdine#BLOCCATO");
	private static final Icon ICON_STATO_INIZIALE_CONFERMATO = RcpSupport
			.getIcon("it.eurotn.panjea.ordini.domain.documento.AreaOrdine$StatoAreaOrdine#CONFERMATO");

	@Override
	public int getNumberOfExtraIcons() {
		// blocco ordine e selezione ordine
		return 2;
	}

	/**
	 * Recupera lo stato di generazione dell'ordine.<br/>
	 * 
	 * 
	 * @param rowGroup
	 *            riga raggrupata
	 * @param tableModel
	 *            table model base
	 * @return stato iniziale di creazione dell'ordine della riga . True l'ordine verrà creato bloccato, False
	 *         confermato , null ho vari stati fra vari ordini
	 */
	private Boolean getStatoBloccatoOrdine(DefaultBeanTableModel<RigaOrdineImportata> tableModel,
			DefaultGroupRow rowGroup) {
		Boolean rigaBloccata = null;
		Boolean first = true;
		for (Object indexRow : rowGroup.getChildren()) {
			if (rigaBloccata == null && !first) {
				break;
			}
			if (indexRow instanceof DefaultGroupRow) {
				Boolean statoCorrente = getStatoBloccatoOrdine(tableModel, (DefaultGroupRow) indexRow);
				if (first) {
					rigaBloccata = statoCorrente;
					first = false;
				} else if (!rigaBloccata.equals(statoCorrente)) {
					rigaBloccata = null;
				}
			} else {
				RigaOrdineImportata rigaImportata = tableModel.getObject(((IndexReferenceRow) indexRow).getRowIndex());
				if (first) {
					rigaBloccata = rigaImportata.getOrdine().isBloccaOrdine();
					first = false;
				} else if (!rigaBloccata.equals(rigaImportata.getOrdine().isBloccaOrdine())) {
					rigaBloccata = null;
				}
			}
		}
		return rigaBloccata;
	}

	/**
	 * Recupera lo stato del blocco in base alblocco delle righe contenute.<br/>
	 * 
	 * 
	 * @param rowGroup
	 *            riga raggrupata
	 * @param tableModel
	 *            table model base
	 * @return blocco della riga . Se almeno una riga è bloccata il bruppo risulta bloccato
	 */
	private boolean getStatoBloccoRaggruppata(DefaultBeanTableModel<RigaOrdineImportata> tableModel,
			DefaultGroupRow rowGroup) {
		boolean rigaBloccata = true;
		for (Object indexRow : rowGroup.getChildren()) {
			if (indexRow instanceof DefaultGroupRow) {
				rigaBloccata = getStatoBloccoRaggruppata(tableModel, (DefaultGroupRow) indexRow);
				if (!rigaBloccata) {
					break;
				}
			} else {
				RigaOrdineImportata rigaImportata = tableModel.getObject(((IndexReferenceRow) indexRow).getRowIndex());
				rigaBloccata = rigaImportata.getOrdine().isSedeBloccata();
				if (!rigaImportata.getOrdine().isSedeBloccata()) {
					break;
				}
			}
		}
		return rigaBloccata;
	}

	/**
	 * Recupera lo stato di un riga raggruppata in base allo stato delle righe contenute.<br/>
	 * 
	 * @param rowGroup
	 *            riga raggrupata
	 * @param tableModel
	 *            table model base
	 * @return stato della riga gruppo. Se almeno una riga è selezionabile il gruppo diventa selezionabile.
	 */
	private boolean getStatoRigaRaggruppata(DefaultBeanTableModel<RigaOrdineImportata> tableModel,
			DefaultGroupRow rowGroup) {
		boolean statoRiga = true;
		for (Object indexRow : rowGroup.getChildren()) {
			if (indexRow instanceof DefaultGroupRow) {
				statoRiga = getStatoRigaRaggruppata(tableModel, (DefaultGroupRow) indexRow);
				if (!statoRiga) {
					break;
				}
			} else {
				RigaOrdineImportata rigaImportata = tableModel.getObject(((IndexReferenceRow) indexRow).getRowIndex());
				statoRiga = rigaImportata.isSelezionata();
				if (!rigaImportata.isSelezionata()) {
					break;
				}
			}
		}
		return statoRiga;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		DefaultBeanTableModel<RigaOrdineImportata> model = (DefaultBeanTableModel<RigaOrdineImportata>) TableModelWrapperUtils
				.getActualTableModel(table.getModel(), DefaultBeanTableModel.class);

		boolean selezionata = getStatoRigaRaggruppata(model, (DefaultGroupRow) value);
		boolean bloccata = getStatoBloccoRaggruppata(model, (DefaultGroupRow) value);
		Boolean statoInizialeBloccato = getStatoBloccatoOrdine(model, (DefaultGroupRow) value);

		JLabel labelRender = (JLabel) CellRendererManager.getRenderer(value.getClass()).getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);
		JLabel labelStatoSelezionata = new JLabel();
		JLabel labelStatoInizialeOrdine = new JLabel();
		JLabel labelStatoSelezioneRaggruppamento = new JLabel();

		labelStatoSelezionata.setToolTipText(null);
		if (bloccata) {
			labelStatoSelezionata.setForeground(Color.red);
		}

		if (statoInizialeBloccato == null) {
			// labelStatoInizialeOrdine.setEnabled(false);
			labelStatoInizialeOrdine.setIcon(ICON_STATO_INIZIALE_BLOCCATO);
		} else {
			labelStatoInizialeOrdine.setEnabled(true);
			if (statoInizialeBloccato) {
				labelStatoInizialeOrdine.setIcon(ICON_STATO_INIZIALE_BLOCCATO);
			} else {
				labelStatoInizialeOrdine.setIcon(ICON_STATO_INIZIALE_CONFERMATO);
			}
		}

		labelStatoSelezionata.setOpaque(false);
		labelStatoSelezionata.setText(labelRender.getText());
		labelStatoSelezionata.setIcon(labelRender.getIcon());

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new HorizontalLayout());
		panel.setBorder(null);

		Icon icon = ICON_SELEZIONABILE;
		if (selezionata) {
			icon = ICON_SELEZIONATA;
		}
		labelStatoSelezioneRaggruppamento.setIcon(icon);
		labelStatoSelezioneRaggruppamento.setOpaque(false);

		panel.add(labelStatoSelezioneRaggruppamento);
		panel.add(labelStatoInizialeOrdine);
		panel.add(labelStatoSelezionata);
		return panel;
	}
}
