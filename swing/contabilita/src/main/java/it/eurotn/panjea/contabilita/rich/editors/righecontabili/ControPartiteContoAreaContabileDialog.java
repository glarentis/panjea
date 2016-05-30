/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.ETipologiaContoControPartita;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti.ETipoRicercaSottoConto;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.dialog.TitledApplicationDialog;

import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableModelWrapperUtils;

/**
 * Dialog che gestisce la contropartita di tipologia CONTO e tutte le sue contropartite SOTTOCONTO.
 * 
 * @author fattazzo
 */
public class ControPartiteContoAreaContabileDialog extends TitledApplicationDialog {

	/**
	 * Estendo il TableModel standard delle contropartite sovrascrivendo il metodo isCellEditable perchè in questo caso
	 * le contropartite di tipologia SOTTOCONTO devono avere la cella importo e note editabile.
	 * 
	 * @author fattazzo
	 * 
	 */
	private class ControPartiteContoAreaContabileTableModel extends ControPartiteAreaContabileTableModel {

		private static final long serialVersionUID = -6537918470199037026L;

		/**
		 * Costruttore.
		 */
		public ControPartiteContoAreaContabileTableModel() {
			super(ControPartiteContoAreaContabileDialog.DIALOG_ID);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			switch (column) {
			case 1:
				return true;
			case 2:
				return true;
			default:
				return false;
			}
		}

	}

	/**
	 * Renderer applicato alla cella che contiene la descrizione della contropartita. Non posso utilizzare il renderer
	 * it.eurotn.panjea.contabilita.rich.editors.tabelle.contropartiteprimanota.DescrizioneTableCellRenderer perchè non
	 * devo visualizzare l'icona e la descrizione del sottoconto non deve essere indentata.
	 * 
	 * @author fattazzo
	 * 
	 */
	public class DescrizioneTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 4394812747704713757L;

		/**
		 * Costruttore.
		 */
		public DescrizioneTableCellRenderer() {
			super();
		}

		@Override
		public Component getTableCellRendererComponent(JTable paramTable, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(paramTable, value, isSelected, hasFocus, row,
					column);

			ControPartiteAreaContabileTableModel model = (ControPartiteAreaContabileTableModel) TableModelWrapperUtils
					.getActualTableModel(paramTable.getModel());

			int actualRow = TableModelWrapperUtils.getActualRowAt(paramTable.getModel(), row);

			if (actualRow == -1) {
				return label;
			}

			ControPartita controPartita = model.getObject(actualRow);

			// visualizzo il codice del sottoconto invece della descrizione della contropartita
			if (controPartita.getAvere() != null && controPartita.getAvere().getId() != -1) {
				label.setText(controPartita.getAvere().getSottoContoCodice() + " - "
						+ controPartita.getAvere().getDescrizione());
			} else {
				label.setText(controPartita.getDare().getSottoContoCodice() + " - "
						+ controPartita.getDare().getDescrizione());
			}

			return label;
		}
	}

	private static final String DIALOG_ID = "controPartiteContoAreaContabileDialog";
	private static final String FORMATIMPORTO = ValutaAzienda.MASCHERA_VALUTA_GENERICA;
	private final List<ControPartita> listControPartite;

	private Conto conto;

	private boolean dare;

	private JideTableWidget<ControPartita> table;

	private JFormattedTextField importoContoField;

	private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;
	private boolean finish = false;

	private static final String DIALOG_TITLE_SUM_IMPORTI = DIALOG_ID + ".title.sum.importi";

	private static final String DIALOG_MESSAGE_SUM_IMPORTI = DIALOG_ID + ".message.sum.importi";

	/**
	 * Costruttore di default.
	 * 
	 * @param list
	 *            Il primo oggetto della lista è la contropartita di tipologia CONTO, gli altri eventuali oggetti sono
	 *            tutte le contropartite di tipologia SOTTOCONTO già create in precedenza.
	 */
	public ControPartiteContoAreaContabileDialog(List<ControPartita> list) {
		super();
		this.contabilitaAnagraficaBD = (IContabilitaAnagraficaBD) Application.instance().getApplicationContext()
				.getBean("contabilitaAnagraficaBD");
		this.listControPartite = list;
		setPreferredSize(new Dimension(500, 300));
		setTitle(getMessage(DIALOG_ID + ".title"));
		setTitlePaneTitle(getMessage(DIALOG_ID + ".pane.title"));
		setDescription("");

		// dalla lista prendo il primo elemento che rappresenta la contropartita di tipologia SOTTOCONTO
		// e memorizzo il conto che rappresenta e se questo è il dare o in avere per evitare ogni volta
		// di fare i controlli
		ControPartita controPartitaConto = list.get(0);
		if (controPartitaConto.getContoAvere() != null && controPartitaConto.getContoAvere().getId() != -1) {
			this.conto = controPartitaConto.getContoAvere();
			this.dare = false;
		} else {
			this.conto = controPartitaConto.getContoDare();
			this.dare = true;
		}
	}

	/**
	 * Crea i controlli relativi alla contropartita conto.
	 * 
	 * @param controPartita
	 *            contro paprtita
	 * @return componente creato
	 */
	private JComponent createContoComponent(ControPartita controPartita) {
		// creo un pannello con un titolo contenente il codice del conto
		JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBorder(BorderFactory.createTitledBorder(getMessage(Conto.class.getName()) + " - "
				+ conto.getContoCodice() + " " + conto.getDescrizione()));
		panel.add(getComponentFactory().createLabel(getMessage("importo")));

		// inserisco il controllo per variare eventualmente l'importo della contropartita CONTO
		NumberFormatter iNumberFormatter = new NumberFormatter();

		DecimalFormat iDecimalFormat = (DecimalFormat) NumberFormat.getNumberInstance();
		iDecimalFormat.applyPattern("###,###,###,###.00");

		iNumberFormatter.setFormat(iDecimalFormat);

		iNumberFormatter.setAllowsInvalid(false);
		iNumberFormatter.setValueClass(BigDecimal.class);
		iNumberFormatter.setCommitsOnValidEdit(true);

		DefaultFormatterFactory factory = new DefaultFormatterFactory();
		factory.setDefaultFormatter(iNumberFormatter);
		factory.setDisplayFormatter(iNumberFormatter);

		// DecimalFormat format = new DecimalFormat(FORMATIMPORTO);
		// format.setParseBigDecimal(true);
		importoContoField = new JFormattedTextField(factory);
		importoContoField.setValue(controPartita.getImporto());
		importoContoField.setColumns(10);
		importoContoField.setHorizontalAlignment(SwingConstants.RIGHT);
		importoContoField.selectAll();
		panel.add(importoContoField);
		return panel;
	}

	/**
	 * Carica la lista di tutti i sottoconti relativi al conto della contropartita e ne crea per ognuno una
	 * contropartita di tipologia SOTTOCONTO. Infine restituisce una lista di tutte le controparitte generate.
	 * 
	 * @return lista di {@link ControPartita}
	 */
	private List<ControPartita> createControPartiteSottoConti() {
		ParametriRicercaSottoConti parametriRicercaSottoConti = new ParametriRicercaSottoConti();
		parametriRicercaSottoConti.setSottotipoConto(null);
		parametriRicercaSottoConti.setTipoRicercaSottoConto(ETipoRicercaSottoConto.CODICE);
		parametriRicercaSottoConti.setValoreDaRicercare(conto.getContoCodice());

		// eseguo la ricerca di tutti i sottoconti del conto della contropartita
		List<SottoConto> listSottoConti = contabilitaAnagraficaBD.ricercaSottoConti(parametriRicercaSottoConti);

		int ordineControPartitaSottoConto = listControPartite.get(0).getOrdine().intValue();
		Map<SottoConto, ControPartita> mapControPartiteSottoconti = new HashMap<SottoConto, ControPartita>();

		// per ogni sottoconto creo una contropartita di tipologia SOTTOCONTO e li metto in una mappa con chiave
		// sottoconto e valore contropartita
		for (SottoConto sottoConto : listSottoConti) {
			ordineControPartitaSottoConto++;
			ControPartita controPartitaSottoConto = new ControPartita();
			if (dare) {
				controPartitaSottoConto.setDare(sottoConto);
			} else {
				controPartitaSottoConto.setAvere(sottoConto);
			}
			controPartitaSottoConto.setCodiceIva(listControPartite.get(0).getCodiceIva());
			controPartitaSottoConto.setEntita(listControPartite.get(0).getEntita());
			controPartitaSottoConto.setImporto(BigDecimal.ZERO);
			controPartitaSottoConto.setTipoDocumento(listControPartite.get(0).getTipoDocumento());
			controPartitaSottoConto.setTipologiaContoControPartita(ETipologiaContoControPartita.SOTTOCONTO);
			controPartitaSottoConto.setOrdine(ordineControPartitaSottoConto);

			mapControPartiteSottoconti.put(sottoConto, controPartitaSottoConto);
		}

		// creata la mappa vado a sovrascrivere i valori delle eventuali contropartite di tipo conto già
		// precedentemente create.
		for (ControPartita controPartita : listControPartite) {
			if (controPartita.getTipologiaContoControPartita() == ETipologiaContoControPartita.SOTTOCONTO) {
				if (dare) {
					mapControPartiteSottoconti.put(controPartita.getDare(), controPartita);
				} else {
					mapControPartiteSottoconti.put(controPartita.getAvere(), controPartita);
				}
			}
		}

		List<ControPartita> list = new ArrayList<ControPartita>();
		list.addAll(mapControPartiteSottoconti.values());

		// ordino la lista delle contropartite di tipologia SOTTOCONTO secondo il codice del sottoconto
		Collections.sort(list, new Comparator<ControPartita>() {

			@Override
			public int compare(ControPartita o1, ControPartita o2) {
				if (dare) {
					return o1.getDare().getSottoContoCodice().compareTo(o2.getDare().getSottoContoCodice());
				} else {
					return o1.getAvere().getSottoContoCodice().compareTo(o2.getAvere().getSottoContoCodice());
				}
			}
		});

		return list;
	}

	/**
	 * Crea i controlli per gestire le contropartite di tipologia SOTTOCONTO.
	 * 
	 * @param list
	 * @return
	 */
	private JComponent createSottoContiComponent(List<ControPartita> list) {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(getMessage(SottoConto.class.getName())));
		panel.add(getComponentFactory().createScrollPane(createTable()), BorderLayout.CENTER);
		return panel;
	}

	private JComponent createTable() {

		table = new JideTableWidget<ControPartita>(DIALOG_ID, new ControPartiteContoAreaContabileTableModel());
		((SortableTable) table.getTable()).setSortable(false);
		table.setRows(createControPartiteSottoConti());
		table.getTable().getColumnModel().getColumn(0).setCellRenderer(new DescrizioneTableCellRenderer());

		return table.getComponent();
	}

	@Override
	protected JComponent createTitledDialogContentPane() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(createContoComponent(listControPartite.get(0)), BorderLayout.NORTH);
		panel.add(createSottoContiComponent(listControPartite), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Restituisce una lista dove il primo elemento è la contropartita di tipologia CONTO e gli altri sono tutte le
	 * contropartite di tipologia SOTTOCONTO con l'importo diverso da zero.
	 * 
	 * @return
	 */
	public List<ControPartita> getControPartiteConImporto() {
		List<ControPartita> list = table.getRows();

		List<ControPartita> listControPartiteConImporto = new ArrayList<ControPartita>();
		listControPartiteConImporto.add(this.listControPartite.get(0));
		for (ControPartita controPartita : list) {
			if (controPartita.getImporto() != null && !controPartita.getImporto().equals(BigDecimal.ZERO)) {
				listControPartiteConImporto.add(controPartita);
			}
		}

		return listControPartiteConImporto;
	}

	public boolean isFinish() {
		return finish;
	}

	@Override
	protected void onCancel() {
		finish = false;
		super.onCancel();
	}

	@Override
	protected boolean onFinish() {
		BigDecimal totaleSottoConti = BigDecimal.ZERO;

		// sommo tutti gli importi della contropartite di tipologia SOTTOCONTO
		List<ControPartita> list = table.getRows();
		for (ControPartita controPartita : list) {
			if (controPartita.getImporto() != null && !controPartita.getImporto().equals(BigDecimal.ZERO)) {
				totaleSottoConti = totaleSottoConti.add(controPartita.getImporto());
			}
		}

		ControPartita controPartitaConto = listControPartite.get(0);

		// commito il valore della formattedTextField ( nel caso in cui ci fosse ancora il focus )
		try {
			importoContoField.commitEdit();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		controPartitaConto.setImporto((BigDecimal) importoContoField.getValue());

		if (controPartitaConto.getImporto() == null) {
			// in questo caso va bene perchè il conto non aveva importo. Setto il totale della
			// contropartite SOTTOCONTO alla contropartita CONTO
			controPartitaConto.setImporto(totaleSottoConti);
			finish = true;
		} else {
			if (controPartitaConto.getImporto().compareTo(totaleSottoConti) == 0) {
				// se l'importo della contropartita CONTO corrisponde alla somma va bene
				finish = true;
			} else {
				// se non corrisponde non chiudo la dialog
				String title = getMessage(DIALOG_TITLE_SUM_IMPORTI);

				Format format = new DecimalFormat(FORMATIMPORTO);
				String message = getMessage(DIALOG_MESSAGE_SUM_IMPORTI,
						new Object[] { format.format(controPartitaConto.getImporto()), format.format(totaleSottoConti),
								format.format(controPartitaConto.getImporto().subtract(totaleSottoConti)) });
				MessageDialog dialog = new MessageDialog(title, message);
				dialog.showDialog();
				finish = false;
			}
		}

		return finish;
	}
}
