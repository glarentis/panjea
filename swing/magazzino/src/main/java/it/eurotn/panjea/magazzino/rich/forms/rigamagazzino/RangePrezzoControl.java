package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

import com.jidesoft.alert.Alert;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.StyleModel;

/**
 * Visualizza il range dei prezzi per la rigaArticolo corrente. La lista dei prezzi viene visualizzata solamente per
 * qualche secondo.
 * 
 * @author giangi
 */
public class RangePrezzoControl {

	/**
	 * Property change che nasconde il component se visibile.
	 */
	private class HideControlPropertyChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			hide();
		}
	}

	public class MouseRangePrezzoControlListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			// super.mousePressed(e);
			try {
				int range = table.getTableHeader().columnAtPoint(e.getPoint());
				if (range > 0) {
					Double value = ScaglioneListino.MAX_SCAGLIONE;
					if (range < table.getTableHeader().getColumnModel().getColumnCount() - 1) {
						value = Double.parseDouble(table.getColumnModel().getColumn(range).getHeaderValue().toString());
					}
					((IRigaArticoloDocumento) formModel.getFormObject()).applicaPoliticaPrezzo(value);
					Importo prezzoUnitario = (Importo) formModel.getValueModel(getPrezzoUnitarioPropertyName())
							.getValue();
					formModel.getValueModel(getPrezzoUnitarioPropertyName()).setValue(prezzoUnitario.clone());
					formModel.getValueModel("variazione1").setValue(formModel.getValueModel("variazione1").getValue());
					formModel.getValueModel("variazione2").setValue(formModel.getValueModel("variazione2").getValue());
					formModel.getValueModel("variazione3").setValue(formModel.getValueModel("variazione3").getValue());
					formModel.getValueModel("variazione4").setValue(formModel.getValueModel("variazione4").getValue());
				}
			} catch (Exception ex) {
				System.err.println("AHHOOO" + e);
			}
		}
	}

	@SuppressWarnings("serial")
	private class PoliticaPrezzoTableModel extends AbstractTableModel implements StyleModel, ContextSensitiveTableModel {
		private PoliticaPrezzo politicaPrezzo;
		private Double[] scaglioniArrays;
		private BigDecimal[] prezziArrays;
		private Sconto[] scontiArrays;

		/**
		 * 
		 * Costruttore.
		 * 
		 * @param politicaPrezzo
		 *            politica prezzo da visualizzare.
		 */
		public PoliticaPrezzoTableModel(final PoliticaPrezzo politicaPrezzo) {
			super();
			formModel.getValueModel("qta").addValueChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					fireTableDataChanged();
				}
			});
			this.politicaPrezzo = politicaPrezzo;

			// ciclo sugli scaglioni di prezzo solamente, gli sconti sono inclusi
			scaglioniArrays = politicaPrezzo.getPrezzi().keySet().toArray(new Double[0]);
			prezziArrays = new BigDecimal[scaglioniArrays.length];
			scontiArrays = new Sconto[scaglioniArrays.length];
			for (int i = 0; i < scaglioniArrays.length; i++) {
				RisultatoPrezzo<BigDecimal> risultatoPrezzo = politicaPrezzo.getPrezzi().getRisultatoPrezzo(
						scaglioniArrays[i]);
				if (risultatoPrezzo != null) {
					prezziArrays[i] = risultatoPrezzo.getValue();
				} else if (risultatoPrezzo == null && i == 0) {
					prezziArrays[i] = BigDecimal.ZERO;
				} else if (risultatoPrezzo == null && i > 0) {
					prezziArrays[i] = prezziArrays[i - 1];
				}

				RisultatoPrezzo<Sconto> risultatoSconti = politicaPrezzo.getSconti().getRisultatoPrezzo(
						scaglioniArrays[i]);
				if (risultatoSconti != null) {
					scontiArrays[i] = risultatoSconti.getValue();
				} else if (risultatoSconti == null && i == 0) {
					Sconto sconto = new Sconto();
					scontiArrays[i] = sconto;
				} else if (risultatoSconti == null && i > 0) {
					scontiArrays[i] = scontiArrays[i - 1];
				}
			}
		}

		@Override
		public Class<?> getCellClassAt(int row, int column) {
			if (column > 0) {
				return BigDecimal.class;
			}
			return String.class;
		}

		@Override
		public CellStyle getCellStyleAt(int row, int column) {
			if (column == 0) {
				return STYLE_ROW_CAPTION;
			}
			double qta = (Double) RangePrezzoControl.this.formModel.getValueModel("qta").getValue();
			Double scaglione = scaglioniArrays[column - 1];
			Double scaglionePrecedente = column == 1 ? 0 : scaglioniArrays[column - 2];
			if (row == 5) {
				STYLE_PREZZO_NETTO.setFontStyle(0);
				if (qta <= scaglione && qta > scaglionePrecedente) {
					STYLE_PREZZO_NETTO.setFontStyle(1);
				}
				return STYLE_PREZZO_NETTO;
			}

			if (qta <= scaglione && qta > scaglionePrecedente) {
				return STYLE_SELEZIONATO;
			}
			return null;
		}

		@Override
		public int getColumnCount() {
			if (politicaPrezzo == null) {
				return 0;
			}
			return scaglioniArrays.length + 1;
		}

		@Override
		public String getColumnName(int column) {
			if (column == 0) {
				return "";
			}
			Double scaglione = scaglioniArrays[column - 1];
			if (scaglione == 999999999) {
				return ScaglioneListino.MAX_SCAGLIONE_LABEL;
			}
			return scaglione.toString();
		}

		@Override
		public ConverterContext getConverterContextAt(int row, int column) {
			if (row == 0 || row >= 5) {
				Importo tmp = (Importo) formModel.getValueModel("prezzoNetto").getValue();
				if (tmp != null) {
					NUMBER_PREZZO_CONTEXT.setPostfisso(tmp.getCodiceValuta());
				}
				NUMBER_PREZZO_CONTEXT.setUserObject(formModel.getValueModel("numeroDecimaliPrezzo").getValue());
				return NUMBER_PREZZO_CONTEXT;
			}
			if (column > 0 && row > 0 && row < 5) {
				return SCONTO_CONVERTER_CONTEXT;
			}
			return null;
		}

		@Override
		public EditorContext getEditorContextAt(int paramInt1, int paramInt2) {
			return null;
		}

		@Override
		public int getRowCount() {
			return 7;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column == 0) {
				return HEADER_NAME[row];
			}
			switch (row) {
			case 0:
				// Prezzo
				return prezziArrays[column - 1];
			case 1:
				return scontiArrays[column - 1].getSconto1();
			case 2:
				return scontiArrays[column - 1].getSconto2();
			case 3:
				return scontiArrays[column - 1].getSconto3();
			case 4:
				return scontiArrays[column - 1].getSconto4();
			case 5:
				return politicaPrezzo.getPrezzoNetto(scaglioniArrays[column - 1],
						((CodiceIva) formModel.getValueModel("codiceIva").getValue()).getPercApplicazione());
			case 6:
				Double qta = (Double) formModel.getValueModel("qta").getValue();
				qta = qta == null ? 0.0 : qta;
				return politicaPrezzo
						.getPrezzoNetto(scaglioniArrays[column - 1],
								((CodiceIva) formModel.getValueModel("codiceIva").getValue()).getPercApplicazione())
						.multiply(BigDecimal.valueOf(qta.longValue())).setScale(2, RoundingMode.HALF_UP);
			default:
				return null;
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public boolean isCellStyleOn() {
			return true;
		}
	}

	/**
	 * Property change che nasconde o visualizza il component.
	 */
	private class RangePrezzoPropertyChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Boolean enabled = new Boolean(evt.getNewValue().toString());
			if (enabled && !RangePrezzoControl.this.formModel.isReadOnly()) {
				show();
			} else {
				hide();
			}
		}
	}

	private static final String[] HEADER_NAME = new String[] { "PREZZO", "V1", "V2", "V3", "V4", "NETTO", "TOTALE" };
	private static Logger logger = Logger.getLogger(RangePrezzoControl.class);
	private static final NumberWithDecimalConverterContext NUMBER_PREZZO_CONTEXT = new NumberWithDecimalConverterContext(
			6, "€");
	private static final NumberWithDecimalConverterContext SCONTO_CONVERTER_CONTEXT = new NumberWithDecimalConverterContext(
			2, "%", false);

	private final FormModel formModel;

	private JTable table;

	private static final CellStyle STYLE_SCAGLIONI = new CellStyle();
	private static final CellStyle STYLE_SELEZIONATO = new CellStyle();
	private static final CellStyle STYLE_ROW_CAPTION = new CellStyle();
	private static final CellStyle STYLE_PREZZO_NETTO = new CellStyle();

	private Alert alert;
	private JComponent controlOwner;
	private MouseRangePrezzoControlListener listener;

	static {
		STYLE_SCAGLIONI.setFontStyle(1);
		STYLE_SCAGLIONI.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		STYLE_SCAGLIONI.setBackground(new Color(237, 243, 254));

		STYLE_ROW_CAPTION.setFontStyle(1);
		STYLE_ROW_CAPTION.setBackground(Color.LIGHT_GRAY);

		STYLE_PREZZO_NETTO.setFontStyle(1);
		STYLE_PREZZO_NETTO.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
		STYLE_PREZZO_NETTO.setBackground(new Color(237, 243, 254));

		STYLE_SELEZIONATO.setFontStyle(1);
	}

	/**
	 * Costruttore di default.
	 * 
	 * @param formModel
	 *            form model su cui registrarsi ai cambiamenti per agire sui controlli.
	 * @param controlOwner
	 *            controllo da associare al popup come owner
	 */
	public RangePrezzoControl(final FormModel formModel, final JComponent controlOwner) {
		super();
		this.formModel = formModel;
		this.controlOwner = controlOwner;
		this.listener = new MouseRangePrezzoControlListener();
		// Se viene abilitata la proprietà politicaPrezzo visualizzo la lista
		this.formModel.getFieldMetadata("politicaPrezzo").addPropertyChangeListener(FormModel.ENABLED_PROPERTY,
				new RangePrezzoPropertyChangeListener());
		// Se rendo il formModel in solaLettura (salvo) nascondo il pannello
		this.formModel.addPropertyChangeListener(FormModel.READONLY_PROPERTY, new HideControlPropertyChangeListener());
		table = new CellStyleTable();

		// Font fontHeader = table.getTableHeader().getFont().deriveFont(Font.BOLD);
		// table.getTableHeader().setFont(fontHeader);
		// table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		table.setName("rangePrezzoControlTable");
		table.setAutoResizeMode(JideTable.AUTO_RESIZE_ALL_COLUMNS);

		alert = new Alert();
		alert.setName("rangePrezzoControlAlert");
		alert.setFocusable(false);
		JScrollPane pane = new JScrollPane(table);
		alert.getContentPane().add(pane);
	}

	/**
	 * @return La property da impostare per cambiare il prezzo unitario.
	 */
	protected String getPrezzoUnitarioPropertyName() {
		return "prezzoUnitarioReale";
	}

	/**
	 * nasconde il popup.
	 */
	public void hide() {
		table.removeMouseListener(listener);
		alert.hidePopupImmediately();
	}

	/**
	 * Metodo che rende visibile il componente.
	 */
	public void show() {
		logger.debug("--> Enter show");
		PoliticaPrezzoTableModel model = new PoliticaPrezzoTableModel((PoliticaPrezzo) formModel.getValueModel(
				"politicaPrezzo").getValue());
		table.setModel(model);
		table.setPreferredSize(new Dimension((180 + (100 * model.getColumnCount() - 1)), 110));
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		Point pt = controlOwner.getLocationOnScreen();
		alert.showPopup(pt.x, pt.y + controlOwner.getHeight());
		table.getTableHeader().addMouseListener(new MouseRangePrezzoControlListener());
		logger.debug("--> Exit show");
	}
}
