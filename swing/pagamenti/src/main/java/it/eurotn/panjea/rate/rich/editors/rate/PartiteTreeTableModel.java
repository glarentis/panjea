package it.eurotn.panjea.rate.rich.editors.rate;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.util.Date;
import java.util.Locale;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

public class PartiteTreeTableModel extends DefaultTreeTableModel {

	private MessageSource messageSource = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param node
	 *            il treetablenode da cui creare il treetablemodel
	 */
	public PartiteTreeTableModel(final TreeTableNode node) {
		super(node);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return TreeTableModel.class; // stato rata e numero
		case 1:
			return Date.class; // data scadenza rata
		case 2:
			return Importo.class; // importo
		case 3:
			return Enum.class; // tipo pagamento
		default:
			return super.getColumnClass(column);
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	/*
	 * Il nome della colonna per avere accesso all'internazionalizzazione delle colonne il nome sarà:
	 * risultatiRicercaRatePageTableColumn0 risultatiRicercaRatePageTableColumn1
	 */
	@Override
	public String getColumnName(final int column) {
		return getMessage("partiteTreeTableModel" + column);
	}

	/**
	 * Restituisce il message per la chiave passata.
	 * 
	 * @param key
	 *            la chiave per recuperare il valore nei messages
	 * @return Il valore per la chiave richiesta
	 */
	private String getMessage(String key) {
		if (messageSource == null) {
			messageSource = (MessageSource) Application.services().getService(MessageSource.class);
		}
		return messageSource.getMessage(key, null, Locale.getDefault());
	}

	/*
	 * Il valore da visualizzare per ogni colonna diverso a seconda che sia una riga con un'area o una riga contabile
	 */
	@Override
	public Object getValueAt(Object node, int column) {
		Object object = ((DefaultMutableTreeTableNode) node).getUserObject();

		if (object instanceof Rata) {
			Rata rata = (Rata) object;
			switch (column) {

			case 0:
				return rata.getNumeroRata(); // stato rata e numero
			case 1:
				return rata.getDataScadenza(); // data scadenza rata
			case 2:
				return rata.getImporto(); // importo
			case 3:
				return rata.getTipoPagamento(); // tipo pagamento
			default:
				return super.getValueAt(node, column);
			}
		} else if (object instanceof Pagamento) {
			Pagamento pagamento = (Pagamento) object;
			switch (column) {
			case 0:
				return null; //
			case 1:
				return pagamento.getDataPagamento(); // data pagamento
			case 2:
				return pagamento.getImporto(); // importo
			case 3:
				return null; // Residuo ()

			default:
				return super.getValueAt(node, column);
			}
		}
		return super.getValueAt(node, column);
	}
}
