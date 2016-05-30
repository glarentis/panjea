package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.renderer.DepositoLiteListCellRenderer;
import it.eurotn.panjea.magazzino.rich.renderer.TipoAreaMagazzinoListCellRenderer;
import it.eurotn.panjea.rich.dialogs.PanjeaFilterListSelectionDialog;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.swing.JTable;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.selection.dialog.FilterListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;

public class AreaOrdineEvasioneTablePage extends AbstractTablePageEditor<AreaOrdinePM> {

	private class SelectRowListener extends MouseAdapter implements KeyListener {

		private JTable table;
		private long lastEvent;

		/**
		 * Costruttore.
		 *
		 * @param table
		 *            tabella da agganciare al listener
		 */
		public SelectRowListener(final JTable table) {
			super();
			this.table = table;
			table.addMouseListener(this);
			table.addKeyListener(this);
		}

		private void cambiaDepositoDestinazione() {
			if (getTable().getSelectedObject().getTipoAreaEvasione().getTipoMovimento() != TipoMovimento.TRASFERIMENTO) {
				MessageDialog dialog = new MessageDialog("Impossibile cambiare deposito",
						"il documento di destinazione non è un trasferimento");
				dialog.showDialog();
				return;
			}
			if (getTable().getSelectedObject().getTipoAreaEvasione().isDepositoDestinazioneBloccato()) {
				return;
			}

			IAnagraficaBD anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
			List<Deposito> depositiAll = anagraficaBD.caricaDepositi();
			EntitaDocumento entita = getTable().getSelectedObject().getEntita();
			DepositoLite depositoOrigine = getTable().getSelectedObject().getDepositoOrigine();
			// Filtro per i depositi aziendali o del cliente selezionato
			List<DepositoLite> depositi = new ArrayList<>();
			for (Deposito deposito : depositiAll) {
				if (!Objects.equals(deposito.creaLite(), depositoOrigine)
						&& deposito.getCodiceAzienda().equalsIgnoreCase(
								PanjeaSwingUtil.getUtenteCorrente().getCodiceAzienda())) {
					if (deposito.getEntita() == null || entita.getId().equals(deposito.getEntita().getId())) {
						depositi.add(deposito.getDepositoLite());
					}
				}
			}

			FilterList<DepositoLite> raggruppamentiFilterList = new FilterList<DepositoLite>(
					GlazedLists.eventList(depositi));
			FilterListSelectionDialog selectionDialog = new PanjeaFilterListSelectionDialog(
					"Seleziona il deposito di destinazione per l'evasione", Application.instance().getActiveWindow()
							.getControl(), raggruppamentiFilterList) {
				@Override
				protected void onSelect(Object selection) {
					if (getTable().getSelectedObject() != null) {
						DepositoLite depositoDestinazione = (DepositoLite) selection;
						getTable().getSelectedObject().setDepositoDestinazione(depositoDestinazione);
					}
				}
			};
			selectionDialog.setRenderer(new DepositoLiteListCellRenderer());
			selectionDialog.setFilterator(GlazedLists.textFilterator("codice"));
			selectionDialog.showDialog();
		}

		/**
		 * Cambia il tipo documento alla riga.
		 *
		 * @param rowIndexConvert
		 *            riga selezionata
		 */
		private void cambiaTipoDocumento(final int rowIndexConvert) {
			List<TipoAreaMagazzino> tipiAreaMagazzinoCliente = getTipiAreaMagazzinoCliente();
			FilterList<TipoAreaMagazzino> raggruppamentiFilterList = new FilterList<TipoAreaMagazzino>(
					GlazedLists.eventList(tipiAreaMagazzinoCliente));
			FilterListSelectionDialog selectionDialog = new PanjeaFilterListSelectionDialog(
					"Seleziona il tipo documento di destinazione per l'evasione", Application.instance()
					.getActiveWindow().getControl(), raggruppamentiFilterList) {
				@Override
				protected void onSelect(Object selection) {
					if (getTable().getSelectedObject() != null) {
						TipoAreaMagazzino tipoAreaSelezionata = (TipoAreaMagazzino) selection;
						getTable().getSelectedObject().setTipoDocumentoEvasione(tipoAreaSelezionata);
						if (tipoAreaSelezionata.getTipoMovimento() == TipoMovimento.TRASFERIMENTO) {
							IMagazzinoDocumentoBD magazzinoDocumentoBD = RcpSupport
									.getBean(MagazzinoDocumentoBD.BEAN_ID);
							// Ricarico l'area per caricare un eventuale deposito di destinazione predefinito sul
							// tipoAreaMagazzino.
							// Il deposito non viene caricato quando carica tutta la lista di tipoAreaMagazzino.
							tipoAreaSelezionata = magazzinoDocumentoBD.caricaTipoAreaMagazzino(tipoAreaSelezionata
									.getId());
							getTable().getSelectedObject().setDepositoDestinazione(
									tipoAreaSelezionata.getDepositoDestinazione());
						} else {
							getTable().getSelectedObject().setDepositoDestinazione(null);
						}
					}
				}
			};
			selectionDialog.setRenderer(new TipoAreaMagazzinoListCellRenderer(true));
			selectionDialog.setFilterator(GlazedLists.textFilterator("tipoDocumento"));
			selectionDialog.showDialog();
		}

		/**
		 *
		 * @return Tipi area magazzino abbinati a tipo entità cliente.
		 */
		private List<TipoAreaMagazzino> getTipiAreaMagazzinoCliente() {
			IMagazzinoDocumentoBD magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
			List<TipoAreaMagazzino> tipiAreaMagazzino = magazzinoDocumentoBD.caricaTipiAreaMagazzino(
					"tipoDocumento.codice", null, false);

			List<TipoAreaMagazzino> tipiAreaMagazzinoCliente = new ArrayList<TipoAreaMagazzino>();
			for (TipoAreaMagazzino tipo : tipiAreaMagazzino) {
				if (tipo.getTipoDocumento().getTipoEntita() == TipoEntita.CLIENTE
						|| tipo.getTipoMovimento() == TipoMovimento.TRASFERIMENTO) {
					tipiAreaMagazzinoCliente.add(tipo);
				}
			}
			return tipiAreaMagazzinoCliente;
		}

		@Override
		public void keyPressed(KeyEvent keyevent) {
		}

		@Override
		public void keyReleased(KeyEvent keyevent) {
			if (keyevent.getKeyCode() == 32) {
				int rowIndex = table.getSelectedRow();
				if (rowIndex == -1) {
					return;
				}
				int rowIndexConver = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));
				if (table.getSelectedColumn() == 5) {
					cambiaDepositoDestinazione();
				} else {
					cambiaTipoDocumento(rowIndexConver);
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
			int rowIndexConvert = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));
			if (table.getSelectedColumn() == 5) {
				cambiaDepositoDestinazione();
			} else {
				cambiaTipoDocumento(rowIndexConvert);
			}
		}
	}

	private static final String PAGE_ID = "areaOrdineEvasioneTablePage";
	private List<AreaOrdinePM> ordini;

	/**
	 * Costruttore.
	 */
	protected AreaOrdineEvasioneTablePage() {
		super(PAGE_ID, new AreaOrdinePMTableModel(PAGE_ID));
		new SelectRowListener(getTable().getTable());
	}

	@Override
	public Collection<AreaOrdinePM> loadTableData() {
		return ordini;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<AreaOrdinePM> refreshTableData() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setFormObject(Object object) {
		ordini = (List<AreaOrdinePM>) object;
	}

}
