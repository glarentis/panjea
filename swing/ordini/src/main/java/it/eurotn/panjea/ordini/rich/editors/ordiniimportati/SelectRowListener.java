package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.rich.editors.verificaprezzo.ParametriCalcoloPrezziPM;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.IndexReferenceRow;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class SelectRowListener extends MouseAdapter implements KeyListener {

	private interface AbstractColumnClosure {
		/**
		 * Esegue il metodo per la colonna selezionata.
		 * 
		 * @param rigaOrdineImportata
		 *            riga ordine selezionata
		 * @param mouseEvent
		 *            evento del mouse
		 * @param cellRect
		 *            rettangolo della cella premuta.
		 */
		void execute(RigaOrdineImportata rigaOrdineImportata, MouseEvent mouseEvent, Rectangle cellRect);
	}

	private class NoteClosure implements AbstractColumnClosure {

		@Override
		public void execute(RigaOrdineImportata rigaOrdineImportata, MouseEvent mouseEvent, Rectangle cellRect) {
			String noteImportazioneOrdine = rigaOrdineImportata.getOrdine().getNoteImportazione();
			String noteOrdine = rigaOrdineImportata.getOrdine().getNote();
			String noteRiga = rigaOrdineImportata.getNote();

			boolean isEmpty = StringUtils.isEmpty(noteImportazioneOrdine) && StringUtils.isEmpty(noteOrdine)
					&& StringUtils.isEmpty(noteRiga);
			if (isEmpty) {
				return;
			}

			StringBuilder sb = new StringBuilder("<HTML>");
			sb.append(noteImportazioneOrdine);

			if (rigaOrdineImportata.getOrdine().getNote() != null) {
				sb.append("<b>NOTE ORDINE:</b>");
				sb.append(noteOrdine);
				sb.append("<BR>");
			}

			if (rigaOrdineImportata.getNote() != null) {
				sb.append("<b>NOTE RIGA:</b>");
				sb.append(noteRiga);
				sb.append("<BR>");
			}
			MessageDialog dialog = new MessageDialog("Note ordine/riga", new DefaultMessage(sb.toString()));
			dialog.showDialog();
		}
	}

	private class PrezzoTotaleClosure implements AbstractColumnClosure {

		/**
		 * Aggiorna il prezzo e sconti della riga di riferimento con quelli determinati.
		 * 
		 * @param rigaOrdine
		 *            riga di riferimento
		 */
		private void aggiornaPrezzoDeterminato(RigaOrdineImportata rigaOrdine) {
			boolean selezionata = rigaOrdine.isSelezionata();
			rigaOrdine.sostituisciPrezzoDeterminato();

			rigaOrdine = ordiniDocumentoBD.salvaRigaOrdineImportata(rigaOrdine).get(0);
			rigaOrdine.setSelezionata(selezionata);
			int index = SelectRowListener.this.tableModel.getObjects().indexOf(rigaOrdine);
			tableModel.setObject(rigaOrdine, index);
			tableModel.initDatiMancanti();

		}

		@Override
		public void execute(RigaOrdineImportata rigaOrdineImportata, MouseEvent mouseEvent, Rectangle cellRect) {

			// costruisco i rettangoli delle icone copia e verifica prezzo
			Rectangle rectVerifica = new Rectangle(cellRect.x, cellRect.y, 16, 16);
			Rectangle rectCopia = new Rectangle(cellRect.x + 16, cellRect.y, 16, 16);

			if ((rigaOrdineImportata.getPrezzoDeterminato().compareTo(BigDecimal.ZERO) != 0)
					&& rectCopia.contains(mouseEvent.getPoint())) {
				aggiornaPrezzoDeterminato(rigaOrdineImportata);
				importazioneOrdiniPage.restoreAllRows();
			}

			if (rectVerifica.contains(mouseEvent.getPoint())) {
				openVerificaPrezzoEditor(rigaOrdineImportata);
			}
		}

		/**
		 * Apre l'editor per la verifica prezzo.
		 * 
		 * @param rigaOrdine
		 *            riga ordine di riferimento
		 */
		private void openVerificaPrezzoEditor(RigaOrdineImportata rigaOrdine) {
			ParametriCalcoloPrezziPM parametriCalcolo = new ParametriCalcoloPrezziPM();
			parametriCalcolo.setArticolo(rigaOrdine.getArticolo());
			parametriCalcolo.setData(rigaOrdine.getOrdine().getData());

			if (rigaOrdine.getOrdine().getEntita() != null) {
				parametriCalcolo.setEntita(rigaOrdine.getOrdine().getEntita());
				parametriCalcolo.setSedeEntita(rigaOrdine.getOrdine().getSedeEntita());
			} else {
				parametriCalcolo.setEntita(new Cliente().getEntitaLite());
				parametriCalcolo.setSedeEntita(new SedeEntita());
			}

			if (rigaOrdine.getOrdine().getListino() != null) {
				parametriCalcolo.setListino(rigaOrdine.getOrdine().getListino());
			} else {
				parametriCalcolo.setListino(new Listino());
			}

			if (rigaOrdine.getOrdine().getListinoAlternativo() != null) {
				parametriCalcolo.setListinoAlternativo(rigaOrdine.getOrdine().getListinoAlternativo());
			} else {
				parametriCalcolo.setListinoAlternativo(new Listino());
			}

			if (rigaOrdine.getOrdine().getAgente() != null) {
				parametriCalcolo.setAgente(rigaOrdine.getOrdine().getAgente());
			}

			parametriCalcolo.setCodiceValuta(valutaAziendaCache.caricaValutaAziendaCorrente().getCodiceValuta());
			parametriCalcolo.setEffettuaRicerca(true);

			LifecycleApplicationEvent event = new OpenEditorEvent(parametriCalcolo);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	private IOrdiniDocumentoBD ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);

	private ImportazioneOrdiniPage importazioneOrdiniPage;
	private GroupTable table;
	private RigheImportazioneTableModel tableModel;
	private DefaultGroupTableModel groupTableModel;
	private long lastEvent;
	private HashMap<Integer, AbstractColumnClosure> columnExecuteMap;
	private ValutaAziendaCache valutaAziendaCache;

	/**
	 * Costruttore.
	 * 
	 * @param table
	 *            tabella da agganciare al listener
	 */
	public SelectRowListener(final ImportazioneOrdiniPage importazioneOrdiniPage) {
		super();
		this.importazioneOrdiniPage = importazioneOrdiniPage;
		valutaAziendaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
		this.table = (GroupTable) importazioneOrdiniPage.getTable().getTable();
		table.addMouseListener(this);
		table.addKeyListener(this);
		tableModel = (RigheImportazioneTableModel) TableModelWrapperUtils.getActualTableModel(table.getModel());
		groupTableModel = (DefaultGroupTableModel) TableModelWrapperUtils.getActualTableModel(table.getModel(),
				DefaultGroupTableModel.class);
		columnExecuteMap = new HashMap<Integer, AbstractColumnClosure>();
		columnExecuteMap.put(14, new PrezzoTotaleClosure());
		columnExecuteMap.put(9, new NoteClosure());
	}

	/**
	 * Cambia lo stato iniziale dell'ordine. Blocca tutti se ho diversi stati
	 * 
	 * @param rowGroup
	 *            rowGroup
	 * @param stato
	 *            corrente del gruppo. true bloccato false confermato null stati diversi
	 */
	private void cambiaStatoInizialeOrdine(DefaultGroupRow rowGroup, Boolean stato) {
		boolean statoDaSettare;
		if (stato == null) {
			statoDaSettare = true;
		} else {
			statoDaSettare = !stato;
		}
		for (Object indexRow : rowGroup.getChildren()) {
			if (indexRow instanceof DefaultGroupRow) {
				cambiaStatoInizialeOrdine((DefaultGroupRow) indexRow, stato);
			} else {
				RigaOrdineImportata rigaImportata = tableModel.getObject(((IndexReferenceRow) indexRow).getRowIndex());
				rigaImportata.getOrdine().setBloccaOrdine(statoDaSettare);
			}
		}
	}

	/**
	 * Recupera lo stato di generazione dell'ordine.<br/>
	 * 
	 * 
	 * @param rowGroup
	 *            riga raggrupata
	 * @return stato iniziale di creazione dell'ordine della riga . True l'ordine verrà creato bloccato, False
	 *         confermato , null ho vari stati fra vari ordini
	 */
	private Boolean getStatoBloccatoOrdine(DefaultGroupRow rowGroup) {
		Boolean rigaBloccata = null;
		Boolean first = true;
		for (Object indexRow : rowGroup.getChildren()) {
			if (rigaBloccata == null && !first) {
				break;
			}
			if (indexRow instanceof DefaultGroupRow) {
				Boolean statoCorrente = getStatoBloccatoOrdine((DefaultGroupRow) indexRow);
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
	 * @return blocco della riga . Se almeno una riga è bloccata il bruppo risulta bloccato
	 */
	private boolean getStatoBloccoRaggruppata(DefaultGroupRow rowGroup) {
		boolean rigaBloccata = true;
		for (Object indexRow : rowGroup.getChildren()) {
			if (indexRow instanceof DefaultGroupRow) {
				rigaBloccata = getStatoBloccoRaggruppata((DefaultGroupRow) indexRow);
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
	 * 
	 * @param rowGroup
	 *            riga raggrupata
	 * @return stato della riga gruppo. Se almeno una riga è selezionabile il gruppo diventa selezionabile.
	 */
	private boolean getStatoRigaRaggruppata(DefaultGroupRow rowGroup) {
		boolean statoRiga = true;
		for (Object indexRow : rowGroup.getChildren()) {
			if (indexRow instanceof DefaultGroupRow) {
				statoRiga = getStatoRigaRaggruppata((DefaultGroupRow) indexRow);
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
	public void keyPressed(KeyEvent keyevent) {
	}

	@Override
	public void keyReleased(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == 32) {
			int rowIndex = table.getSelectedRow();
			int columnIndex = table.getSelectedColumn();
			if (columnIndex == -1 || rowIndex == -1) {
				return;
			}
			int columnIndexConvert = table.convertRowIndexToModel(table.convertColumnIndexToModel(columnIndex));
			int rowIndexConver = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));
			Object value = table.getModel().getValueAt(rowIndexConver, columnIndexConvert);

			if (value instanceof DefaultGroupRow) {
				DefaultGroupRow groupRow = (DefaultGroupRow) value;
				boolean stato = getStatoRigaRaggruppata(groupRow);
				selezionaRighe(groupRow, !stato);
				groupTableModel.refresh();
				table.setColumnSelectionInterval(columnIndex, columnIndex);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent keyevent) {
	}

	@Override
	public void mouseClicked(MouseEvent mouseevent) {
		// Non so perchè l'evento viene chiamato due volte. (ved. MouseClicked
		// su eventMulticaster).
		// controllo che l'evento sia generato in tempi diversi

		if (mouseevent.getWhen() == lastEvent) {
			return;
		} else {
			lastEvent = mouseevent.getWhen();
		}

		int rowIndex = table.rowAtPoint(mouseevent.getPoint());
		int columnIndex = table.columnAtPoint(mouseevent.getPoint());
		int columnIndexConvert = table.convertRowIndexToModel(table.convertColumnIndexToModel(columnIndex));
		int rowIndexConvert = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));
		Object value = table.getModel().getValueAt(rowIndexConvert, columnIndexConvert);
		// trovo il rettangolo della cella
		Rectangle rec = table.getCellRect(rowIndex, columnIndex, false);
		if (value instanceof DefaultGroupRow) {
			// se premo su una riga raggruppata devo controllare di non aver premuto sull'icona per
			// espandere/raggruppare.
			DefaultGroupRow groupRow = (DefaultGroupRow) value;
			// Creo il rettangolo "utile" per la selezione
			Rectangle rectSelezione = new Rectangle(rec.x + (16 * (groupRow.getLevel() + 1)), rec.y, 16, 16);
			Rectangle rectBloccoOrdine = new Rectangle(rec.x + 16 + (16 * (groupRow.getLevel() + 1)), rec.y, 16, 16);

			if (rectSelezione.contains(mouseevent.getPoint())) {
				boolean stato = getStatoRigaRaggruppata(groupRow);
				selezionaRighe(groupRow, !stato);
				groupTableModel.refresh();
			}

			if (rectBloccoOrdine.contains(mouseevent.getPoint())) {
				Boolean bloccoOrdine = getStatoBloccatoOrdine(groupRow);
				if (bloccoOrdine != null) {
					cambiaStatoInizialeOrdine(groupRow, bloccoOrdine);
					groupTableModel.refresh();
				}
			}
		} else {

			int groupCols = groupTableModel.getGroupColumnCount();
			int colonna = columnIndexConvert + groupCols;

			RigaOrdineImportata rigaOrdineImportata = tableModel.getObject(TableModelWrapperUtils.getActualRowAt(
					table.getModel(), rowIndex));
			if (columnExecuteMap.containsKey(colonna)) {
				columnExecuteMap.get(colonna).execute(rigaOrdineImportata, mouseevent, rec);
			}
		}

		groupTableModel.refresh();
		table.setColumnSelectionInterval(columnIndex, columnIndex);
	}

	/**
	 * 
	 * @param rowGroup
	 *            riga raggruppata selezionata
	 * @param selezionata
	 *            stato da applicare
	 */
	private void selezionaRighe(DefaultGroupRow rowGroup, boolean selezionata) {
		if (selezionata) {
			if (getStatoBloccoRaggruppata(rowGroup) && !PanjeaSwingUtil.hasPermission("creaBackOrderBloccato")) {
				MessageDialog messageDialog = new MessageDialog("Permessi insufficienti",
						"Impossibile selezionare un'ordine con la sede bloccata.\n Permessi insufficienti");
				messageDialog.showDialog();
				return;
			}
		}
		for (Object indexRow : rowGroup.getChildren()) {
			if (indexRow instanceof DefaultGroupRow) {
				selezionaRighe((DefaultGroupRow) indexRow, selezionata);
			} else {
				RigaOrdineImportata rigaImportata = tableModel.getObject(((IndexReferenceRow) indexRow).getRowIndex());
				rigaImportata.setSelezionata(selezionata);
			}
		}
	}
}
