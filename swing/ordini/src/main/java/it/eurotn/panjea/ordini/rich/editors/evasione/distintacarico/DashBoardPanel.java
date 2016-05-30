package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellSpan;
import com.jidesoft.grid.CellSpanTable;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SpanTableModel;
import com.jidesoft.grid.StyleTableModel;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.steema.teechart.drawing.Color;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.ordini.manager.documento.evasione.DatiDistintaCaricoVerifica;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

@SuppressWarnings("serial")
public class DashBoardPanel extends JPanel {

    private class ControlliTableModel extends AbstractTableModel
            implements StyleTableModel, SpanTableModel, ContextSensitiveTableModel {
        private CellStyle boldStyle;
        private CellStyle redStyle;
        private Object[][] data;
        private DatiDistintaCaricoVerifica dati;

        /**
         * Costruttore
         *
         */
        public ControlliTableModel() {
            boldStyle = new CellStyle();
            boldStyle.setFontStyle(Font.BOLD);
            boldStyle.setBackground(new Color(224, 224, 235));

            redStyle = new CellStyle();
            redStyle.setFontStyle(Font.BOLD);
            redStyle.setForeground(Color.RED);
        }

        /**
         *
         * @param dataInizioTrasporto
         *            data inizio trasp.
         */
        public void carica(Date dataInizioTrasporto) {
            IOrdiniDocumentoBD ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
            dati = ordiniDocumentoBD.caricaDatiVerifica(dataInizioTrasporto);
            data = new Object[5][8];
            data[0][0] = "";
            data[1][0] = "ORDINI";
            data[2][0] = "IN PRODUZIONE";
            data[3][0] = "";
            data[4][0] = "NON ELABORATI";

            data[0][1] = "";
            data[1][1] = dati.getOrdiniInevasi();
            data[2][1] = dati.getOrdiniInProduzione();
            data[3][1] = "";
            data[4][1] = dati.getOrdiniNonElaborati();

            data[0][2] = "";
            data[1][2] = dati.getRigheInevase();
            data[2][2] = dati.getRigheInProduzione();
            data[3][2] = "";
            data[4][2] = dati.getRigheNonElaborate();

            data[0][3] = "DATA INIZIO TRASP.";
            data[1][3] = "ORDINI.";
            data[2][3] = "IN PRODUZIONE";
            data[3][3] = "EVASI";
            data[4][3] = "NON ELABORATI";

            data[0][4] = dati.getDataInizioTrasporto();
            data[1][4] = dati.getOrdiniInevasiConData();
            data[2][4] = dati.getOrdiniInProduzioneConData();
            data[3][4] = dati.getOrdiniEvasiConData();
            data[4][4] = dati.getOrdiniNonElaboratiConData();

            data[0][5] = "";
            data[1][5] = dati.getRigheInevaseConData();
            data[2][5] = dati.getRigheInProduzioneConData();
            data[3][5] = dati.getRigheEvase();
            data[4][5] = dati.getRigheNonElaborateConData();

            data[2][6] = "";
            data[2][7] = dati.getRigheInProduzionePronteConData();
            fireTableDataChanged();
        }

        @Override
        public Class<?> getCellClassAt(int rowIndex, int columnIndex) {
            if (columnIndex == 4 && rowIndex == 0) {
                return Date.class;
            } else if (columnIndex != 0 && columnIndex != 3) {
                return Integer.class;
            }
            return null;
        }

        @Override
        public CellSpan getCellSpanAt(int rowIndex, int columnIndex) {
            if (rowIndex == 0 && columnIndex == 4) {
                return new CellSpan(0, 4, 1, 3);
            }
            return null;
        }

        @Override
        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0 || columnIndex == 3) {
                return boldStyle;
            }

            if (dati == null) {
                return null;
            }

            if (columnIndex == 2 && rowIndex == 4 && dati.getRigheNonElaborate() > 0) {
                return redStyle;
            }
            if (columnIndex == 5 && rowIndex == 4 && dati.getRigheNonElaborateConData() > 0) {
                return redStyle;
            }
            return null;
        }

        @Override
        public int getColumnCount() {
            return 8;
        }

        @Override
        public ConverterContext getConverterContextAt(int rowIndex, int columnIndex) {
            return null;
        }

        @Override
        public EditorContext getEditorContextAt(int rowIndex, int columnIndex) {
            return null;
        }

        @Override
        public int getRowCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (data == null) {
                return "";
            }
            return data[rowIndex][columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return rowIndex == 0 && columnIndex == 4;
        }

        @Override
        public boolean isCellSpanOn() {
            return true;
        }

        @Override
        public boolean isCellStyleOn() {
            return true;
        }

        @Override
        public void setValueAt(Object valore, int rowIndex, int columnIndex) {
            if (rowIndex == 0 && columnIndex == 4) {
                data[0][4] = valore;
            }
            carica((Date) valore);
        }

    }

    private class TableMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            int row = table.rowAtPoint(event.getPoint());
            int col = table.columnAtPoint(event.getPoint());

            if (row == 3 && col > 3) {
                ParametriRicercaAreaMagazzino parametri = new ParametriRicercaAreaMagazzino();
                parametri.getDataRegistrazione().setTipoPeriodo(TipoPeriodo.DATE);
                Date dataAreaMagazzino = DateUtils.truncate((Date) table.getValueAt(0, 4), Calendar.DATE);
                parametri.getDataRegistrazione().setDataIniziale(dataAreaMagazzino);
                parametri.setAnnoCompetenza(null);
                parametri.getDataRegistrazione().setDataFinale(dataAreaMagazzino);
                parametri.getDataDocumento().setTipoPeriodo(TipoPeriodo.NESSUNO);
                Set<TipoGenerazione> gen = new HashSet<>();
                gen.add(TipoGenerazione.EVASIONE);
                parametri.setTipiGenerazione(gen);
                parametri.setStatiAreaMagazzino(new HashSet<AreaMagazzino.StatoAreaMagazzino>(
                        Arrays.asList(AreaMagazzino.StatoAreaMagazzino.values())));
                parametri.setEffettuaRicerca(true);
                Application.instance().getApplicationContext().publishEvent(new OpenEditorEvent(parametri));
            }
            if (row == 4 && col == 2) {
                ParametriRicercaEvasione parametri = new ParametriRicercaEvasione();
                parametri.getDataRegistrazione().setTipoPeriodo(TipoPeriodo.NESSUNO);
                parametri.setTipoEntita(TipoEntita.CLIENTE);
                parametri.setEvadiOrdini(true);
                parametri.setEffettuaRicerca(true);
                Application.instance().getApplicationContext().publishEvent(new OpenEditorEvent(parametri));
            }
            if (row == 4 && col == 5) {
                ParametriRicercaEvasione parametri = new ParametriRicercaEvasione();
                parametri.getDataRegistrazione().setTipoPeriodo(TipoPeriodo.NESSUNO);
                parametri.setEvadiOrdini(true);
                parametri.setEffettuaRicerca(true);
                parametri.setDataInizioTrasporto(DateUtils.truncate((Date) table.getValueAt(0, 4), Calendar.DATE));
                Application.instance().getApplicationContext().publishEvent(new OpenEditorEvent(parametri));
            }
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            table.setCursor(Cursor.getDefaultCursor());
            int row = table.rowAtPoint(event.getPoint());
            int col = table.columnAtPoint(event.getPoint());
            if (row == 4 && (col == 2 || col == 5) || (row == 3 && col > 3)) {
                table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
    }

    private CellSpanTable table;

    /**
     * Costruttore.
     */
    public DashBoardPanel() {
        setLayout(new BorderLayout());
        table = new CellSpanTable(new ControlliTableModel());
        table.setAutoResizeMode(JideTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(40);
        table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(40);
        table.getColumnModel().getColumn(6).setPreferredWidth(40);
        table.getColumnModel().getColumn(7).setPreferredWidth(40);
        table.setTableStyleProvider(new RowStripeTableStyleProvider());
        table.setGridColor(Color.lightGray);
        table.setShowVerticalLines(false);
        table.setRowSelectionAllowed(false);
        table.setFocusable(false);
        table.setAutoSelectTextWhenStartsEditing(true);
        table.setAutoStartCellEditing(true);
        table.addMouseListener(new TableMouseAdapter());
        table.addMouseMotionListener(new TableMouseAdapter());
        add(table, BorderLayout.CENTER);
    }

    /**
     * Carica i dati di verifica
     */
    public void carica() {
        ((ControlliTableModel) table.getModel()).carica(Calendar.getInstance().getTime());
    }
}
