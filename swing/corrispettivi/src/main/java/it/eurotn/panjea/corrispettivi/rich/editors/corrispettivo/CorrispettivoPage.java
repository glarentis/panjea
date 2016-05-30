package it.eurotn.panjea.corrispettivi.rich.editors.corrispettivo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;

import org.apache.log4j.Logger;
import org.jfree.ui.tabbedui.VerticalLayout;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.AggregateTableHeader;

import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.RigaCorrispettivo;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JideTableWidget;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class CorrispettivoPage extends AbstractDialogPage {

    private static final Logger LOGGER = Logger.getLogger(CorrispettivoPage.class);

    public static final String PAGE_ID = "corrispettivoPage";
    public static final String CLOSE_PROPERTY_CHANGE = "closePropertyChange";
    private final Corrispettivo corrispettivo;
    private CorrispettivoForm corrispettivoForm;
    private JideTableWidget<RigaCorrispettivo> righeCorrispettivoTable;
    private JLabel dateLabel;

    private RigheCorrispettivoTableModel righeCorrispettivoTableModel;

    private JLabel labelImportoAssegnato;
    private JLabel labelImportoResiduo;

    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    /**
     * Costruttore.
     *
     * @param corrispettivo
     *            corrispettivo
     */
    public CorrispettivoPage(final Corrispettivo corrispettivo) {
        super(PAGE_ID);
        this.corrispettivo = corrispettivo;
    }

    @Override
    protected JComponent createControl() {
        LOGGER.debug("--> Enter createControl");

        corrispettivoForm = new CorrispettivoForm(corrispettivo);
        dateLabel = new JLabel(getFormatedGiorno(corrispettivo.getData()));
        dateLabel.setIcon(RcpSupport.getIcon(Date.class.getName()));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel topPanel = getComponentFactory().createPanel(new BorderLayout());
        topPanel.add(dateLabel, BorderLayout.NORTH);
        topPanel.add(corrispettivoForm.getControl(), BorderLayout.CENTER);
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(createRigheCorrispettivoControl(), BorderLayout.CENTER);

        panel.add(createSummaryComponent(), BorderLayout.SOUTH);

        // abilita o disabilita la tabella in base al totale del corrispettivo
        corrispettivoForm.addFormValueChangeListener("totale", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                righeCorrispettivoTable.getTable().setEnabled((arg0.getNewValue() != null));
                resetImportiAssegnati();

                CorrispettivoPage.this.setPageComplete(false);
                // setto sempre il totale del corrispettivo nella prima riga. Se il corrispettivo ha
                // solo 1 riga la pagina è completa
                if (corrispettivo.getRigheCorrispettivo() != null) {
                    if (corrispettivo.getRigheCorrispettivo().size() == 1) {
                        righeCorrispettivoTable.getRows().get(0).setImporto((BigDecimal) arg0.getNewValue());
                        CorrispettivoPage.this.setPageComplete(true);
                    } else if (corrispettivo.getRigheCorrispettivo().size() > 1) {
                        righeCorrispettivoTable.getRows().get(0).setImporto((BigDecimal) (arg0.getNewValue()));
                        CorrispettivoPage.this.setPageComplete(true);
                    }
                }

                updateSummary();
            }
        });
        CorrispettivoPage.this.setPageComplete(isImportiValidi());
        updateSummary();

        LOGGER.debug("--> Exit createControl");
        return panel;
    }

    /**
     * Crea i controlli delle righe corrispettivo.
     *
     * @return controlli creati
     */
    private JComponent createRigheCorrispettivoControl() {

        righeCorrispettivoTableModel = new RigheCorrispettivoTableModel();
        righeCorrispettivoTable = new JideTableWidget<RigaCorrispettivo>(PAGE_ID + ".table",
                righeCorrispettivoTableModel);
        // tabella non filtrabile
        JTableHeader header = ((ITable<?>) righeCorrispettivoTable.getTable())
                .getTableHeader(righeCorrispettivoTable.getTable());
        ((AggregateTableHeader) header).setAutoFilterEnabled(false);
        righeCorrispettivoTable.getTable().setTableHeader(header);

        righeCorrispettivoTable.setRows(corrispettivo.getRigheCorrispettivo());

        righeCorrispettivoTableModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent arg0) {
                CorrispettivoPage.this.setPageComplete(isImportiValidi());
                updateSummary();
            }
        });

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(righeCorrispettivoTable.getComponent(), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea il componente che visualizza l'importo assegnato e quello rimanente.
     *
     * @return componente creato
     */
    private JComponent createSummaryComponent() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        JPanel importiPanel = getComponentFactory().createPanel(new VerticalLayout());
        rootPanel.add(importiPanel, BorderLayout.EAST);

        JPanel impAssegnatiPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel labelImporti = getComponentFactory().createLabel("Importo assegnato:");
        labelImporti.setPreferredSize(new Dimension(150, 20));
        labelImporti.setHorizontalAlignment(SwingConstants.RIGHT);
        impAssegnatiPanel.add(labelImporti);
        labelImportoAssegnato = getComponentFactory().createLabel("");
        labelImportoAssegnato.setPreferredSize(new Dimension(80, 20));
        labelImportoAssegnato.setHorizontalAlignment(SwingConstants.RIGHT);
        Font font = labelImportoAssegnato.getFont();
        labelImportoAssegnato.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));

        impAssegnatiPanel.add(labelImportoAssegnato);
        importiPanel.add(impAssegnatiPanel);

        JPanel impResiduoPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel labelResiduo = getComponentFactory().createLabel("Importo residuo:");
        labelResiduo.setPreferredSize(new Dimension(150, 20));
        labelResiduo.setHorizontalAlignment(SwingConstants.RIGHT);
        impResiduoPanel.add(labelResiduo);
        labelImportoResiduo = getComponentFactory().createLabel("");
        labelImportoResiduo.setPreferredSize(new Dimension(80, 20));
        labelImportoResiduo.setHorizontalAlignment(SwingConstants.RIGHT);
        labelImportoResiduo.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
        impResiduoPanel.add(labelImportoResiduo);
        importiPanel.add(impResiduoPanel);

        return rootPanel;
    }

    /**
     *
     * @return then corrispettivo
     */
    public Corrispettivo getCorrispettivo() {
        Corrispettivo corrispettivoEdit = (Corrispettivo) corrispettivoForm.getFormObject();
        corrispettivoEdit.setRigheCorrispettivo(righeCorrispettivoTable.getRows());
        return corrispettivoEdit;
    }

    /**
     * @return the corrispettivoForm
     */
    public CorrispettivoForm getCorrispettivoForm() {
        return corrispettivoForm;
    }

    /**
     * Formatta la data per far vedere il giorno de la settimana.
     *
     * @param date
     *            data da formattare
     * @return data formattata
     */
    public String getFormatedGiorno(Date date) {
        Format formatter;
        formatter = new SimpleDateFormat("EEEE, d MMMM yyyy");
        return formatter.format(date);
    }

    /**
     * Restituisce il totale degli importi assegnati alle righe del corrispettivo.
     *
     * @return totale degli importi
     */
    private BigDecimal getImportoAssegnato() {

        BigDecimal totale = BigDecimal.ZERO;
        for (RigaCorrispettivo rigaCorrispettivo : righeCorrispettivoTable.getRows()) {
            if (rigaCorrispettivo.getImporto() != null) {
                totale = totale.add(rigaCorrispettivo.getImporto());
            }
        }

        return totale;
    }

    /**
     * Verifica che l'importo del corrispettivo e delle righe siano validi.
     *
     * @return <code>true</code> se validi
     */
    private boolean isImportiValidi() {
        boolean valid = false;

        if (corrispettivo.getTotale() != null && corrispettivo.getTotale().compareTo(BigDecimal.ZERO) != 0) {
            if (getImportoAssegnato().compareTo(corrispettivo.getTotale()) == 0) {
                valid = true;
            }
        }

        return valid;
    }

    /**
     * Azzera tutti gli importi delle righe del corrispettivo.
     */
    private void resetImportiAssegnati() {
        for (RigaCorrispettivo rigaCorrispettivo : righeCorrispettivoTable.getRows()) {
            rigaCorrispettivo.setImporto(BigDecimal.ZERO);
        }
        righeCorrispettivoTableModel.fireTableDataChanged();
    }

    /**
     * Aggiorna il sommario della tabella.
     */
    private void updateSummary() {

        BigDecimal totaleAssegnato = getImportoAssegnato();
        labelImportoAssegnato.setText(decimalFormat.format(totaleAssegnato));

        if (corrispettivo.getTotale() == null) {
            labelImportoResiduo.setText("nd.");
            labelImportoResiduo.setForeground(Color.RED);
        } else {
            BigDecimal residuo = corrispettivo.getTotale().subtract(totaleAssegnato);
            labelImportoResiduo.setText(decimalFormat.format(residuo));

            if (residuo.compareTo(BigDecimal.ZERO) < 0) {
                labelImportoResiduo.setForeground(Color.RED);
            } else {
                labelImportoResiduo.setForeground(Color.BLACK);
            }
        }
    }
}
