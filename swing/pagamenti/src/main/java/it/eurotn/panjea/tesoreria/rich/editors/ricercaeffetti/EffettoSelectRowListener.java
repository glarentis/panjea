package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti;

import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto.StatoCarrello;
import it.eurotn.rich.control.table.ITable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JTable;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;

public class EffettoSelectRowListener extends MouseAdapter implements KeyListener {
	private JTable table;
	private long lastEvent;
	private int[] colonneAbilitatePerSelezione;

	{
		colonneAbilitatePerSelezione = new int[] {};
	}

	/**
	 * Costruttore.
	 * 
	 * @param table
	 *            tabella da agganciare al listener
	 */
	public EffettoSelectRowListener(final AggregateTable table) {
		super();
		this.table = table;
		table.addMouseListener(this);
		table.addKeyListener(this);
	}

	/**
	 * Restituisce lo stato del carrello in base allo stato della situazione effetto.
	 * 
	 * @param situazioneEffetto
	 *            situazioneEffetto
	 * @return stato
	 */
	private StatoCarrello getStatoCarrello(SituazioneEffetto situazioneEffetto) {
		StatoCarrello statoCarrello;

		switch (situazioneEffetto.getStatoCarrello()) {
		case SELEZIONABILE:
			if (situazioneEffetto.getStatoCarrelloFromStatoEffetto() == StatoCarrello.NON_SELEZIONABILE) {
				statoCarrello = StatoCarrello.NON_SELEZIONABILE;
			} else {
				statoCarrello = StatoCarrello.DA_AGGIUNGERE;
			}
			break;
		case DA_AGGIUNGERE:
			statoCarrello = StatoCarrello.SELEZIONABILE;
			break;
		case AGGIUNTO_CARRELLO:
			// non faccio nulla
			statoCarrello = StatoCarrello.AGGIUNTO_CARRELLO;
			break;
		case NON_SELEZIONABILE:
			// non faccio nulla
			statoCarrello = StatoCarrello.NON_SELEZIONABILE;
			break;
		default:
			throw new UnsupportedOperationException("stato carrello non valido " + situazioneEffetto.getStatoCarrello());
		}

		return statoCarrello;
	}

	@Override
	public void keyPressed(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == 32) {
			int rowIndex = table.getSelectedRow();
			int columnIndex = table.getSelectedColumn();
			int actualColumn = ((ITable<?>) table).getActualColumn(columnIndex);
			if (Arrays.asList(colonneAbilitatePerSelezione).contains(actualColumn)) {
				RisultatiRicercaEffettiTableModel tableEffettiModel = (RisultatiRicercaEffettiTableModel) TableModelWrapperUtils
						.getActualTableModel(table.getModel());
				ITable<?> iTable = (ITable<?>) table;
				int[] righe = iTable.getActualRowsAt(rowIndex, columnIndex);
				for (Integer riga : righe) {
					SituazioneEffetto situazioneEffetto = tableEffettiModel.getObject(riga);
					situazioneEffetto.setStatoCarrello(getStatoCarrello(situazioneEffetto));
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent keyevent) {
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

		RisultatiRicercaEffettiTableModel tableEffettiModel = (RisultatiRicercaEffettiTableModel) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		ITable<?> iTable = (ITable<?>) table;
		for (int actualIndexColumn : colonneAbilitatePerSelezione) {
			if (iTable.checkColumn(mouseevent, actualIndexColumn)) {
				int[] righe = iTable.getActualRowsAt(table.rowAtPoint(mouseevent.getPoint()),
						table.columnAtPoint(mouseevent.getPoint()));
				for (Integer riga : righe) {
					SituazioneEffetto situazioneEffetto = tableEffettiModel.getObject(riga);
					situazioneEffetto.setStatoCarrello(getStatoCarrello(situazioneEffetto));
				}
			}
			break;
		}
		table.repaint();
	}

	/**
	 * 
	 * @param paramColonneAbilitatePerSelezione
	 *            colonne da abilitare per selezionare l'effetto
	 */
	public void setEnabledRow(int[] paramColonneAbilitatePerSelezione) {
		this.colonneAbilitatePerSelezione = paramColonneAbilitatePerSelezione;
	}
}
