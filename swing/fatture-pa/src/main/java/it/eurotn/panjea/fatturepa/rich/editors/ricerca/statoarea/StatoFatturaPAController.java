package it.eurotn.panjea.fatturepa.rich.editors.ricerca.statoarea;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.fatturepa.rich.editors.ricerca.RisultatiRicercaFatturePATablePage;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JecAggregateTable;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 * @author fattazzo
 *
 */
public class StatoFatturaPAController extends MouseAdapter {

    private RisultatiRicercaFatturePATablePage risultatiRicercaFatturePATablePage;

    private long lastEvent;

    private JecAggregateTable<AreaMagazzinoFatturaPARicerca> areaFatturaTable;
    private DefaultBeanTableModel<AreaMagazzinoFatturaPARicerca> areaFatturaTableModel;

    private AreaFatturaPANotificheTablePage tablePage = new AreaFatturaPANotificheTablePage();

    /**
     * Costruttore.
     *
     * @param risultatiRicercaFatturePATablePage
     *            pagina dei movimenti
     */
    @SuppressWarnings("unchecked")
    public StatoFatturaPAController(final RisultatiRicercaFatturePATablePage risultatiRicercaFatturePATablePage) {
        super();
        this.risultatiRicercaFatturePATablePage = risultatiRicercaFatturePATablePage;

        this.areaFatturaTable = (JecAggregateTable<AreaMagazzinoFatturaPARicerca>) this.risultatiRicercaFatturePATablePage
                .getTable().getTable();
        areaFatturaTable.addMouseListener(this);

        areaFatturaTableModel = (DefaultBeanTableModel<AreaMagazzinoFatturaPARicerca>) TableModelWrapperUtils
                .getActualTableModel(areaFatturaTable.getModel());
    }

    @Override
    public void mousePressed(MouseEvent mouseevent) {
        if (mouseevent.getWhen() == lastEvent) {
            return;
        } else {
            lastEvent = mouseevent.getWhen();
        }

        int rowIndex = areaFatturaTable.rowAtPoint(mouseevent.getPoint());
        int columnIndex = areaFatturaTable.columnAtPoint(mouseevent.getPoint());
        Rectangle rectSelezione = ((ITable<?>) areaFatturaTable).getIconCellRect(rowIndex, columnIndex);

        if (areaFatturaTable.checkColumn(mouseevent, 9) && rectSelezione.contains(mouseevent.getPoint())) {
            int[] indiciRighe = areaFatturaTable.getActualRowsAt(areaFatturaTable.rowAtPoint(mouseevent.getPoint()),
                    areaFatturaTable.columnAtPoint(mouseevent.getPoint()));

            showStatiAreaMagazzinoFatturaPA(indiciRighe);
        }
    }

    /**
     * Visualizza il dialog per la visualizzazione e modifica degli stati.
     *
     * @param indiciRighe
     *            indici riga
     */
    private void showStatiAreaMagazzinoFatturaPA(int[] indiciRighe) {
        for (final Integer riga : indiciRighe) {
            final AreaMagazzinoFatturaPARicerca areaMagazzinoFatturaPARicerca = areaFatturaTableModel.getObject(riga);

            switch (areaMagazzinoFatturaPARicerca.getStato()) {
            case CONFERMATO:
            case FORZATO:
                tablePage = new AreaFatturaPANotificheTablePage();
                tablePage.setFormObject(areaMagazzinoFatturaPARicerca.getIdAreaMagazzino());
                tablePage.loadData();
                tablePage.setTitle(ObjectConverterManager.toString(areaMagazzinoFatturaPARicerca.getDocumento(),
                        Documento.class, null));
                PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(tablePage) {

                    @Override
                    protected boolean isMessagePaneVisible() {
                        return false;
                    }

                    @Override
                    protected boolean onFinish() {
                        risultatiRicercaFatturePATablePage.refreshData();
                        return true;
                    }
                };
                dialog.setPreferredSize(new Dimension(1000, 700));
                dialog.showDialog();
                tablePage.dispose();
                tablePage = null;
                break;
            default:
                // non faccio niente perchè lo stato non è valido
                break;
            }
        }
    }
}
