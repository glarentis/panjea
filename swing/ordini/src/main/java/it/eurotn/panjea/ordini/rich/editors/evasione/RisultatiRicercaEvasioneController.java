package it.eurotn.panjea.ordini.rich.editors.evasione;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ReflectiveVisitorHelper;

import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.IndexReferenceRow;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.StatoRiga;
import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.CarrelloEvasioneOrdiniTableModel;
import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.CarrelloEvasioneOrdiniTablePage;
import it.eurotn.util.PanjeaEJBUtil;

public class RisultatiRicercaEvasioneController extends MouseAdapter implements KeyListener {

    public class Visit {

        private int rowSelected;

        /**
         * Recupera lo stato di un riga raggruppata in base allo stato delle righe contenute.<br/>
         * 
         * 
         * @param rowGroup
         *            riga raggrupata
         * @return stato della riga gruppo. Se almeno una riga è selezionabile il gruppo diventa selezionabile.
         */
        private StatoRiga getStatoRigaRaggruppata(DefaultGroupRow rowGroup) {
            StatoRiga statoRiga = StatoRiga.NEL_CARRELLO;
            for (final Object indexRow : rowGroup.getChildren()) {
                if (indexRow instanceof DefaultGroupRow) {
                    statoRiga = getStatoRigaRaggruppata((DefaultGroupRow) indexRow);
                    if (statoRiga == StatoRiga.SELEZIONABILE) {
                        break;
                    }
                } else {
                    final RigaDistintaCarico rigaEvasione = risultatiRicercaEvasioneModel
                            .getObject(((IndexReferenceRow) indexRow).getRowIndex());
                    statoRiga = rigaEvasione.getStato();
                    if (statoRiga == StatoRiga.SELEZIONABILE) {
                        break;
                    }
                }
            }
            return statoRiga;
        }

        /**
         * 
         * @param rowGroup
         *            riga raggruppata selezionata
         */
        private void selezionaRighe(DefaultGroupRow rowGroup) {
            StatoRiga stato = getStatoRigaRaggruppata(rowGroup);
            switch (stato) {
            case NEL_CARRELLO:
                stato = StatoRiga.SELEZIONABILE;
                break;
            case SELEZIONABILE:
                stato = StatoRiga.NEL_CARRELLO;
                break;
            default:
                stato = StatoRiga.SELEZIONABILE;
            }

            for (final Object indexRow : rowGroup.getChildren()) {
                if (indexRow instanceof DefaultGroupRow) {
                    selezionaRighe((DefaultGroupRow) indexRow);
                } else {
                    final RigaDistintaCarico rigaEvasione = risultatiRicercaEvasioneModel
                            .getObject(((IndexReferenceRow) indexRow).getRowIndex());
                    rigaEvasione.setStato(stato);
                    sincronizzaRiga(rigaEvasione);
                }
            }
        }

        /**
         * @param rowSelected
         *            The rowSelected to set.
         */
        public void setRowSelected(int rowSelected) {
            this.rowSelected = rowSelected;
        }

        /**
         * 
         * @param forzata
         *            valore selezionato
         */
        public void visit(Boolean forzata) {
            final RigaDistintaCarico rigaEvasione = risultatiRicercaEvasioneModel.getObject(
                    TableModelWrapperUtils.getActualRowAt(tableRisultatiRicercaEvasione.getModel(), rowSelected));
            rigaEvasione.setForzata(!rigaEvasione.getForzata());
        }

        /**
         * 
         * @param row
         *            rigaselezionata
         */
        public void visit(DefaultGroupRow row) {
            selezionaRighe(row);
        }

        /**
         * 
         * @param stato
         *            stato della riga selezionata
         */
        public void visit(StatoRiga stato) {
            final RigaDistintaCarico rigaEvasione = risultatiRicercaEvasioneModel.getObject(
                    TableModelWrapperUtils.getActualRowAt(tableRisultatiRicercaEvasione.getModel(), rowSelected));
            switch (stato) {
            case NEL_CARRELLO:
                stato = StatoRiga.SELEZIONABILE;
                break;
            case SELEZIONABILE:
                stato = StatoRiga.NEL_CARRELLO;
                break;
            default:
                stato = StatoRiga.SELEZIONABILE;
            }
            rigaEvasione.setStato(stato);
            sincronizzaRiga(rigaEvasione);
        }
    }

    private final CarrelloEvasioneOrdiniTableModel carrelloEvasioneModel;
    private final CarrelloEvasioneOrdiniTablePage carrelloEvasioneOrdiniTablePage;
    private final DefaultGroupTableModel groupTableModel;
    private long lastEvent;
    private final RisultatiRicercaEvasioneTableModel risultatiRicercaEvasioneModel;

    private final RisultatiRicercaEvasioneTablePage risultatiRicercaEvasioneTablePage;
    private final GroupTable tableRisultatiRicercaEvasione;
    private final Visit visitor;
    private final ReflectiveVisitorHelper visitorHelper;

    /**
     * Costruttore.
     * 
     * @param risultatiRicercaEvasioneTablePage
     *            risultatiRicercaEvasioneTablePage
     * @param carrelloEvasioneOrdiniTablePage
     *            carrelloEvasioneOrdiniTablePage
     */
    public RisultatiRicercaEvasioneController(final RisultatiRicercaEvasioneTablePage risultatiRicercaEvasioneTablePage,
            final CarrelloEvasioneOrdiniTablePage carrelloEvasioneOrdiniTablePage) {
        super();
        this.risultatiRicercaEvasioneTablePage = risultatiRicercaEvasioneTablePage;
        this.carrelloEvasioneOrdiniTablePage = carrelloEvasioneOrdiniTablePage;

        risultatiRicercaEvasioneTablePage.setController(this);
        carrelloEvasioneOrdiniTablePage.setController(this);

        this.tableRisultatiRicercaEvasione = (GroupTable) risultatiRicercaEvasioneTablePage.getTable().getTable();
        tableRisultatiRicercaEvasione.addMouseListener(this);
        tableRisultatiRicercaEvasione.addKeyListener(this);
        risultatiRicercaEvasioneModel = (RisultatiRicercaEvasioneTableModel) TableModelWrapperUtils
                .getActualTableModel(tableRisultatiRicercaEvasione.getModel());
        groupTableModel = (DefaultGroupTableModel) TableModelWrapperUtils
                .getActualTableModel(tableRisultatiRicercaEvasione.getModel(), DefaultGroupTableModel.class);
        carrelloEvasioneModel = (CarrelloEvasioneOrdiniTableModel) TableModelWrapperUtils
                .getActualTableModel(carrelloEvasioneOrdiniTablePage.getTable().getTable().getModel());
        visitorHelper = new ReflectiveVisitorHelper();
        visitor = new Visit();
    }

    /**
     * Aggiorna lo stato delle righe.
     */
    public void aggiornaStatoRighe() {
        final List<RigaDistintaCarico> righe = risultatiRicercaEvasioneModel.getObjects();
        risultatiRicercaEvasioneTablePage.getTable().getRows();
        final List<RigaDistintaCarico> righeCarrello = carrelloEvasioneOrdiniTablePage.getTable().getRows();

        for (final RigaDistintaCarico rigaCarrello : righeCarrello) {
            for (final RigaDistintaCarico rigaDistintaCarico : righe) {
                if (rigaCarrello.getRigaArticolo().equals(rigaDistintaCarico.getRigaArticolo())) {
                    rigaDistintaCarico.setStato(StatoRiga.NEL_CARRELLO);
                    break;
                }
            }
        }
        risultatiRicercaEvasioneModel.fireTableDataChanged();
    }

    /**
     * Aggiunge una lista di righe invece di una singola.
     * 
     * @param righe
     *            righe
     */
    public void aggiungiRighe(List<RigaDistintaCarico> righe) {
        final List<RigaDistintaCarico> righeDaAggiungere = new ArrayList<>();
        for (final RigaDistintaCarico rigaDistintaCarico : righe) {
            rigaDistintaCarico.setStato(StatoRiga.NEL_CARRELLO);

            final RigaDistintaCarico rigaCarrello = getRigaCarrelloDaAggiungere(rigaDistintaCarico);
            if (rigaCarrello != null) {
                righeDaAggiungere.add(rigaCarrello);
            }
        }
        carrelloEvasioneOrdiniTablePage.getTable().addRows(righeDaAggiungere, null);
    }

    /**
     * Restituisce la riga da aggiungere al carrello o null.
     * 
     * @param rigaEvasione
     *            rigaEvasione
     * @return rigaEvasione con qta Evasa aggiornata o null
     */
    private RigaDistintaCarico getRigaCarrelloDaAggiungere(RigaDistintaCarico rigaEvasione) {
        // sincronizzo la vista dei risultati
        final int indexRigaEvasioneRisultatiRicerca = risultatiRicercaEvasioneModel.getObjects().indexOf(rigaEvasione);
        if (indexRigaEvasioneRisultatiRicerca != -1) {
            risultatiRicercaEvasioneModel.getObjects().get(indexRigaEvasioneRisultatiRicerca)
                    .setStato(rigaEvasione.getStato());
        }

        // sincronizzo riga
        switch (rigaEvasione.getStato()) {
        case SELEZIONABILE:
            return null;
        case NEL_CARRELLO:
            // Se è già nel carrello non lo inserisco
            if (carrelloEvasioneModel.getObjects().indexOf(rigaEvasione) < 0) {
                final RigaDistintaCarico rigaDaSincronizzare = PanjeaEJBUtil.cloneObject(rigaEvasione);
                rigaDaSincronizzare
                        .setGestioneGiacenza(risultatiRicercaEvasioneTablePage.getGestioneCalcoloQtaDaEvadere());
                return rigaDaSincronizzare;
            }
            break;
        default:
            throw new UnsupportedOperationException("Stato non definito per la riga di evasione");
        }
        return null;
    }

    /**
     * @return <code>true</code> se si stà calcondo le giacenze nel carrello
     */
    public boolean isGiacenzaCaluculatorRunning() {
        return carrelloEvasioneModel.getGiacenzaCalculator().isRunning();
    }

    @Override
    public void keyPressed(KeyEvent keyevent) {
    }

    @Override
    public void keyReleased(KeyEvent keyevent) {
        if (keyevent.getKeyCode() == 32) {
            if (carrelloEvasioneModel.getGiacenzaCalculator().isRunning()) {
                return;
            }
            final int rowIndex = tableRisultatiRicercaEvasione.getSelectedRow();
            final int columnIndex = tableRisultatiRicercaEvasione.getSelectedColumn();
            if (columnIndex == -1 || rowIndex == -1) {
                return;
            }
            final int columnIndexConvert = tableRisultatiRicercaEvasione
                    .convertRowIndexToModel(tableRisultatiRicercaEvasione.convertColumnIndexToModel(columnIndex));
            final int rowIndexConver = tableRisultatiRicercaEvasione
                    .convertRowIndexToModel(tableRisultatiRicercaEvasione.convertRowIndexToModel(rowIndex));
            final Object value = tableRisultatiRicercaEvasione.getModel().getValueAt(rowIndexConver,
                    columnIndexConvert);
            visitor.setRowSelected(tableRisultatiRicercaEvasione.getSelectedRow());
            visitorHelper.invokeVisit(visitor, value);
            groupTableModel.refresh();
            tableRisultatiRicercaEvasione.setColumnSelectionInterval(columnIndex, columnIndex);
        }
    }

    @Override
    public void keyTyped(KeyEvent keyevent) {
    }

    @Override
    public void mouseClicked(MouseEvent mouseevent) {
        if (isGiacenzaCaluculatorRunning()) {
            return;
        }

        // Non so perchè l'evento viene chiamato due volte. (ved. MouseClicked
        // su eventMulticaster).
        // controllo che l'evento sia generato in tempi diversi
        if (mouseevent.getWhen() == lastEvent) {
            return;
        } else {
            lastEvent = mouseevent.getWhen();
        }

        final int rowIndex = tableRisultatiRicercaEvasione.rowAtPoint(mouseevent.getPoint());
        final int columnIndex = tableRisultatiRicercaEvasione.columnAtPoint(mouseevent.getPoint());
        final int columnIndexConvert = tableRisultatiRicercaEvasione
                .convertRowIndexToModel(tableRisultatiRicercaEvasione.convertColumnIndexToModel(columnIndex));
        final int rowIndexConvert = tableRisultatiRicercaEvasione
                .convertRowIndexToModel(tableRisultatiRicercaEvasione.convertRowIndexToModel(rowIndex));
        final Object value = tableRisultatiRicercaEvasione.getModel().getValueAt(rowIndexConvert, columnIndexConvert);
        // se premo su una riga raggruppata devo controllare di non aver premuto sull'icona per espandere/raggruppare.
        // se la x è <13 sto clikkando sul + per decomprimere/comprimere l'albero.
        Rectangle rectSelezione = null;
        final Rectangle rec = tableRisultatiRicercaEvasione.getCellRect(rowIndex, columnIndex, false);
        if (value instanceof DefaultGroupRow) {
            final DefaultGroupRow groupRow = (DefaultGroupRow) value;
            // Creo il rettangolo "utile" per la selezione
            // trovo il rettangolo della cella
            rectSelezione = new Rectangle(rec.x + (16 * (groupRow.getLevel() + 1)), rec.y, 16, 16);
        } else {
            rectSelezione = new Rectangle(rec.x, rec.y, rec.width, 16);
        }
        if (rectSelezione.contains(mouseevent.getPoint())) {
            visitor.setRowSelected(rowIndex);
            visitorHelper.invokeVisit(visitor, value);
            groupTableModel.refresh();
            tableRisultatiRicercaEvasione.setColumnSelectionInterval(columnIndex, columnIndex);
        }
    }

    /**
     * Ricarica i risultati.
     */
    public void reloadRisultatiRicercaEvasioneTablePage() {
        this.risultatiRicercaEvasioneTablePage.loadData();
    }

    /**
     * Sincronizza una singola riga tra il carrello e i risultati.
     * 
     * @param rigaEvasione
     *            rigaEvasione
     */
    public void sincronizzaRiga(RigaDistintaCarico rigaEvasione) {
        final RigaDistintaCarico rigaCarrello = getRigaCarrelloDaAggiungere(rigaEvasione);
        if (rigaCarrello == null) {
            carrelloEvasioneOrdiniTablePage.getTable().removeRowObject(rigaEvasione);
        } else {
            carrelloEvasioneOrdiniTablePage.getTable().addRowObject(rigaCarrello, null);
        }
        risultatiRicercaEvasioneModel.fireTableDataChanged();
    }
}
