package it.eurotn.panjea.vending.rich.editors.casse.situazione;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableColumnGroup;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.SituazioneCassa;
import it.eurotn.rich.control.table.JideTableWidget;

public class SituazioneCassaTable extends JideTableWidget<SituazioneCassa> {

    private DecimalFormat decimalFormat = new DecimalFormat("##0.00€");

    private JLabel totaleInizialeLabel;
    private JLabel totaleEntrateLabel;
    private JLabel totaleUsciteLabel;
    private JLabel totaleFinaleLabel;

    /**
     * Costruttore.
     */
    public SituazioneCassaTable() {
        super("situazioneCassaTable", new SituazioneCassaTableModel());

        JideTable table = (JideTable) getTable();
        table.setNestedTableHeader(true);
        ((NestedTableHeader) table.getTableHeader()).setUseNativeHeaderRenderer(true);

        NestedTableHeader header = (NestedTableHeader) table.getTableHeader();

        TableColumnGroup firstGroup = new TableColumnGroup("");
        firstGroup.add(getTable().getColumnModel().getColumn(0));
        firstGroup.add(getTable().getColumnModel().getColumn(1));
        header.addColumnGroup(firstGroup);

        TableColumnGroup pezziGroup = new TableColumnGroup("Pezzi");
        pezziGroup.add(getTable().getColumnModel().getColumn(2));
        pezziGroup.add(getTable().getColumnModel().getColumn(3));
        pezziGroup.add(getTable().getColumnModel().getColumn(4));
        pezziGroup.add(getTable().getColumnModel().getColumn(5));
        header.addColumnGroup(pezziGroup);

        TableColumnGroup valoreGroup = new TableColumnGroup("Valore");
        valoreGroup.add(getTable().getColumnModel().getColumn(6));
        valoreGroup.add(getTable().getColumnModel().getColumn(7));
        valoreGroup.add(getTable().getColumnModel().getColumn(8));
        valoreGroup.add(getTable().getColumnModel().getColumn(9));
        header.addColumnGroup(valoreGroup);

        getTable().getTableHeader().setReorderingAllowed(false);

        SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getModel(), SortableTableModel.class);
        sortableTableModel.setSortable(false);
    }

    @Override
    public JComponent getComponent() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(super.getComponent(), BorderLayout.CENTER);

        JPanel totaliPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        totaliPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 5));

        totaleInizialeLabel = new JLabel();
        totaliPanel.add(totaleInizialeLabel);

        totaleEntrateLabel = new JLabel();
        totaliPanel.add(totaleEntrateLabel);

        totaleUsciteLabel = new JLabel();
        totaliPanel.add(totaleUsciteLabel);

        totaleFinaleLabel = new JLabel();
        totaliPanel.add(totaleFinaleLabel);
        rootPanel.add(totaliPanel, BorderLayout.NORTH);

        return rootPanel;
    }

    /**
     * @param cassa
     *            cassa
     */
    public void setCassa(Cassa cassa) {
        setRows(cassa.getSituazioneCassa());

        BigDecimal totaleIniziale = cassa.getMovimentoApertura() != null ? cassa.getMovimentoApertura().getTotale()
                : BigDecimal.ZERO;
        totaleInizialeLabel.setText("<html>Iniziale: <b>" + decimalFormat.format(totaleIniziale) + "</b></html>");

        BigDecimal totaleEntrate = BigDecimal.ZERO;
        BigDecimal totaleUscite = BigDecimal.ZERO;
        for (MovimentoCassa mov : cassa.getAltriMovimenti()) {
            totaleEntrate = totaleEntrate.add(mov.getTotaleEntrate());
            totaleUscite = totaleUscite.add(mov.getTotaleUscite());
        }
        totaleEntrateLabel.setText("<html>Entrate: <b>" + decimalFormat.format(totaleEntrate) + "</b></html>");
        totaleUsciteLabel.setText("<html>Uscite: <b>" + decimalFormat.format(totaleUscite) + "</b></html>");

        BigDecimal totale = totaleIniziale.add(totaleEntrate).subtract(totaleUscite);
        totaleFinaleLabel.setText("<html>Finale: <b>" + decimalFormat.format(totale) + "</b></html>");
    }

}
