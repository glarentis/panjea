/**
 *
 */
package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.panjea.tesoreria.util.SituazioneRata.StatoCarrello;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JecAggregateTable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

/**
 * @author fattazzo
 * 
 */
public class RicercaRateController extends MouseAdapter implements KeyListener {

	/**
	 * Costruttore.
	 */
	private final class PagamentoRataChiusaDialog extends ConfirmationDialog {

		private StatoCarrello statoCarrelloResult;

		/**
		 * Costruttore.
		 * 
		 * @param title
		 *            titolo
		 * @param message
		 *            messaggio
		 */
		private PagamentoRataChiusaDialog(final String title, final String message) {
			super(title, message);
			setCloseAction(CloseAction.HIDE);
		}

		/**
		 * @return the statoCarrelloResult
		 */
		public StatoCarrello getStatoCarrelloResult() {
			return statoCarrelloResult;
		}

		@Override
		protected void onCancel() {
			statoCarrelloResult = StatoCarrello.SELEZIONABILE;
			super.onCancel();
		}

		@Override
		protected void onConfirm() {
			statoCarrelloResult = StatoCarrello.AGGIUNTO_CARRELLO;

		}

	}

	private RisultatiRicercaRatePage risultatiRicercaRatePage;
	private JecAggregateTable<SituazioneRata> rateTable;

	private CarrelloPagamentiTablePage carrelloPagamentiTablePage;

	private long lastEvent;

	private DefaultBeanTableModel<SituazioneRata> rateTableModel;

	private PagamentoRataChiusaDialog pagamentoRataChiusaDialog;

	{
		pagamentoRataChiusaDialog = new PagamentoRataChiusaDialog(
				RcpSupport.getMessage("pagamentoRataChiusa.ask.title"),
				RcpSupport.getMessage("pagamentoRataChiusa.ask.message"));
	}

	/**
	 * Costruttore.
	 * 
	 * @param risultatiRicercaRatePage
	 *            pagina dei risultati ricerca rate
	 * @param carrelloPagamentiTablePage
	 *            pagina del carrello
	 */
	@SuppressWarnings("unchecked")
	public RicercaRateController(final RisultatiRicercaRatePage risultatiRicercaRatePage,
			final CarrelloPagamentiTablePage carrelloPagamentiTablePage) {
		super();
		this.risultatiRicercaRatePage = risultatiRicercaRatePage;
		this.carrelloPagamentiTablePage = carrelloPagamentiTablePage;

		this.rateTable = (JecAggregateTable<SituazioneRata>) this.risultatiRicercaRatePage.getTable().getTable();
		rateTable.addMouseListener(this);
		rateTable.addKeyListener(this);

		rateTableModel = (DefaultBeanTableModel<SituazioneRata>) TableModelWrapperUtils.getActualTableModel(rateTable
				.getModel());
	}

	/**
	 * Aggiunge al carrello tutte le situazioni rata dato l'indice dei risultati ricerca.
	 * 
	 * @param indiciRighe
	 *            indici riga
	 */
	private void aggiungiSituazioniRata(int[] indiciRighe) {
		for (Integer riga : indiciRighe) {
			SituazioneRata situazioneRata = rateTableModel.getObject(riga);
			StatoCarrello statoCarrelloCorrente = situazioneRata.getStatoCarrello();
			StatoCarrello statoCarrello = getNuovoStatoCarrello(situazioneRata);

			rateTableModel.getObject(riga).setStatoCarrello(statoCarrello);
			boolean result = carrelloPagamentiTablePage.updateSituazioneRata(situazioneRata);
			// interrompo l'inserimento a causa, ad esempio, di un importo di blocco impostato nel carrello
			// in questo modo il messaggio di impossibilità di inserimento viene mostrato una sola volta
			if (!result) {
				situazioneRata.setStatoCarrello(statoCarrelloCorrente);
				break;
			}
			rateTable.repaint();
		}
	}

	/**
	 * Restituisce il nuovo stato per la situazione rata.
	 * 
	 * @param situazioneRata
	 *            situazione rata
	 * @return nuovo stato
	 */
	private StatoCarrello getNuovoStatoCarrello(SituazioneRata situazioneRata) {
		StatoCarrello statoCarrello = situazioneRata.getStatoCarrello();
		switch (situazioneRata.getStatoCarrello()) {
		case SELEZIONABILE:
			if (situazioneRata.getStatoCarrelloFromStatoRata() == StatoCarrello.NON_SELEZIONABILE) {
				String title;
				String message;
				ApplicationDialog dialog;
				switch (situazioneRata.getStatoRata()) {
				case IN_RIASSEGNAZIONE:
					title = RcpSupport.getMessage("pagamentoDataScadenzaRataNonValida.ask.title");
					message = RcpSupport.getMessage("pagamentoDataScadenzaRataNonValida.ask.message");
					dialog = new MessageDialog(title, message);
					dialog.showDialog();
					statoCarrello = StatoCarrello.SELEZIONABILE;
					break;
				case RIEMESSA:
					statoCarrello = StatoCarrello.SELEZIONABILE;
					break;
				default:
					pagamentoRataChiusaDialog.showDialog();
					statoCarrello = pagamentoRataChiusaDialog.getStatoCarrelloResult();
					break;
				}
			} else {
				statoCarrello = StatoCarrello.AGGIUNTO_CARRELLO;
			}
			break;
		case DA_AGGIUNGERE:
			statoCarrello = StatoCarrello.SELEZIONABILE;
			break;
		case AGGIUNTO_CARRELLO:
			statoCarrello = StatoCarrello.SELEZIONABILE;
			break;
		default:
			throw new UnsupportedOperationException("stato carrello non valido " + situazioneRata.getStatoCarrello());
		}

		return statoCarrello;
	}

	@Override
	public void keyPressed(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == 32) {
			int rowIndex = rateTable.getSelectedRow();
			int columnIndex = rateTable.getSelectedColumn();
			if (columnIndex == -1 || rowIndex == -1) {
				return;
			}
			if (rateTable.getActualColumn(columnIndex) == 0) {
				int[] indiciRighe = rateTable.getActualRowsAt(rowIndex, columnIndex);
				aggiungiSituazioniRata(indiciRighe);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent mouseevent) {
		if (mouseevent.getWhen() == lastEvent) {
			return;
		} else {
			lastEvent = mouseevent.getWhen();
		}
		if (rateTable.checkColumn(mouseevent, 0)) {
			int[] indiciRighe = rateTable.getActualRowsAt(rateTable.rowAtPoint(mouseevent.getPoint()),
					rateTable.columnAtPoint(mouseevent.getPoint()));
			aggiungiSituazioniRata(indiciRighe);
		}
	}
}
