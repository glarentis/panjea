package it.eurotn.panjea.giroclienti.rich.editors.scheda.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.AbstractTableCellEditorRenderer;
import com.jidesoft.grid.CellRolloverSupport;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.JideButton;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.rich.bd.ISchedeGiroClientiBD;
import it.eurotn.panjea.giroclienti.rich.bd.SchedeGiroClientiBD;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class AreaOrdineGiroClienteCellEditorRenderer extends AbstractTableCellEditorRenderer
        implements CellRolloverSupport {

    private final class NewActionListener implements ActionListener {
        private final RigaGiroCliente rigaGiroCliente;

        /**
         * Costruttore.
         *
         * @param rigaGiroCliente
         *            riga giro
         */
        private NewActionListener(final RigaGiroCliente rigaGiroCliente) {
            this.rigaGiroCliente = rigaGiroCliente;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            righeSchedaGiroTable.selectRowObject(rigaGiroCliente, null);

            schedeGiroClientiBD.creaAreaOrdineGiroCliente(rigaGiroCliente.getId());

            RigaGiroCliente rigaGiroClienteSalvata = schedeGiroClientiBD.caricaRigaGiroCliente(rigaGiroCliente.getId());
            rigaGiroClienteSalvata = schedeGiroClientiBD.salvaRigaGiroCliente(rigaGiroClienteSalvata);

            AreaOrdine areaOrdineDaCaricare = null;
            // ricarica tutte le righe perchè se ho la stessa entità su più ore viene aggiornata per tutte
            // le sue righe
            List<RigaGiroCliente> righeGiro = schedeGiroClientiBD
                    .caricaRigheGiroCliente(rigaGiroClienteSalvata.getGiorno(), rigaGiroClienteSalvata.getUtente());
            for (RigaGiroCliente riga : righeGiro) {
                righeSchedaGiroTable.replaceRowObject(riga, riga, null);
                if (riga.getId().compareTo(rigaGiroClienteSalvata.getId()) == 0) {
                    areaOrdineDaCaricare = riga.getAreaOrdine();
                }
            }

            AreaOrdineFullDTO areaOrdineFullDTO = ordiniDocumentoBD.caricaAreaOrdineFullDTO(areaOrdineDaCaricare);
            areaOrdineFullDTO.setInserimentoRigheMassivo(true);
            LifecycleApplicationEvent event = new OpenEditorEvent(areaOrdineFullDTO);
            Application.instance().getApplicationContext().publishEvent(event);
        }
    }

    private final class OkActionListener implements ActionListener {
        private final RigaGiroCliente rigaGiroCliente;

        /**
         * Costruttore.
         *
         * @param rigaGiroCliente
         *            riga giro
         */
        private OkActionListener(final RigaGiroCliente rigaGiroCliente) {
            this.rigaGiroCliente = rigaGiroCliente;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            righeSchedaGiroTable.selectRowObject(rigaGiroCliente, null);

            RigaGiroCliente rigaGiroClienteSalvata = schedeGiroClientiBD.caricaRigaGiroCliente(rigaGiroCliente.getId());
            rigaGiroClienteSalvata.setEseguito(!rigaGiroClienteSalvata.isEseguito());
            rigaGiroClienteSalvata = schedeGiroClientiBD.salvaRigaGiroCliente(rigaGiroClienteSalvata);

            righeSchedaGiroTable.replaceRowObject(rigaGiroCliente, rigaGiroClienteSalvata, null);
        }
    }

    private final class OpenActionListener implements ActionListener {
        private final RigaGiroCliente rigaGiroCliente;

        /**
         * Costruttore.
         *
         * @param rigaGiroCliente
         *            riga giro
         */
        private OpenActionListener(final RigaGiroCliente rigaGiroCliente) {
            this.rigaGiroCliente = rigaGiroCliente;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            righeSchedaGiroTable.selectRowObject(rigaGiroCliente, null);

            RigaGiroCliente rigaGiroClienteSalvata = schedeGiroClientiBD.caricaRigaGiroCliente(rigaGiroCliente.getId());

            AreaOrdineFullDTO areaOrdineFullDTO = ordiniDocumentoBD
                    .caricaAreaOrdineFullDTO(rigaGiroClienteSalvata.getAreaOrdine());
            areaOrdineFullDTO.setInserimentoRigheMassivo(true);
            LifecycleApplicationEvent event = new OpenEditorEvent(areaOrdineFullDTO);
            Application.instance().getApplicationContext().publishEvent(event);
        }
    }

    private static final long serialVersionUID = -1559557761862573989L;

    public static final EditorContext CONTEXT = new EditorContext("areaOrdineGiroClienteCellEditorRenderer");

    public static final Icon ESEGUITO_ICON = RcpSupport.getIcon("confirmGiroCliente.icon");
    public static final Icon NON_ESEGUITO_ICON = RcpSupport.getIcon("confirmDisabledGiroCliente.icon");

    private ISchedeGiroClientiBD schedeGiroClientiBD;
    private IOrdiniDocumentoBD ordiniDocumentoBD;

    private RigheSchedaGiroTable righeSchedaGiroTable;

    /**
     * Costruttore.
     *
     * @param righeSchedaGiroTable
     *            table
     *
     */
    public AreaOrdineGiroClienteCellEditorRenderer(final RigheSchedaGiroTable righeSchedaGiroTable) {
        super();
        this.righeSchedaGiroTable = righeSchedaGiroTable;

        schedeGiroClientiBD = RcpSupport.getBean(SchedeGiroClientiBD.BEAN_ID);
        ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void configureTableCellEditorRendererComponent(final JTable table, Component editorRendererComponent,
            boolean forRenderer, Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {

        JButton newButton = (JButton) (((JPanel) editorRendererComponent).getComponent(0));
        JButton okButton = (JButton) (((JPanel) editorRendererComponent).getComponent(1));
        JButton openButton = (JButton) (((JPanel) editorRendererComponent).getComponent(2));

        final DefaultBeanTableModel innerModel = (DefaultBeanTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel());
        final int roxInner = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

        newButton.setVisible(roxInner != -1);
        okButton.setVisible(roxInner != -1);
        openButton.setVisible(roxInner != -1);

        newButton.setToolTipText("Crea l'ordine cliente");
        okButton.setToolTipText("");
        openButton.setToolTipText("");

        if (roxInner != -1) {
            final RigaGiroCliente rigaGiroCliente = (RigaGiroCliente) innerModel.getElementAt(roxInner);

            newButton.setVisible(!rigaGiroCliente.isEseguito());

            okButton.setIcon(rigaGiroCliente.isEseguito() ? ESEGUITO_ICON : NON_ESEGUITO_ICON);
            okButton.setToolTipText(
                    rigaGiroCliente.isEseguito() ? "Giro cliente eseguito" : "Giro cliente da eseguire");

            openButton.setVisible(rigaGiroCliente.getAreaOrdine() != null);
            openButton.setOpaque(false);
            String toolTip = "<html>Apri l'ordine cliente "
                    + ObjectConverterManager.toString(rigaGiroCliente.getAreaOrdine());
            if (rigaGiroCliente.getNumeroRigheOrdineInMissione() != 0) {
                toolTip = toolTip + "<br><b>" + rigaGiroCliente.getNumeroRigheOrdineInMissione()
                        + "</b> righe risultano attualmente in evasione.";
                openButton.setOpaque(true);
            }
            toolTip = toolTip + "</html>";
            openButton.setToolTipText(toolTip);

            if (!forRenderer) {
                newButton.addActionListener(new NewActionListener(rigaGiroCliente));
                okButton.addActionListener(new OkActionListener(rigaGiroCliente));
                openButton.addActionListener(new OpenActionListener(rigaGiroCliente));
            }
        }
        editorRendererComponent.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        editorRendererComponent.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
    }

    /**
     * Crea un button standard per il renderer/editor.
     *
     * @param idIcon
     *            id icona
     * @return button
     */
    private JButton createButton(String idIcon) {
        JButton button = new JideButton(RcpSupport.getIcon(idIcon));
        button.setOpaque(false);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    @Override
    public Component createTableCellEditorRendererComponent(JTable table, int row, int column) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 2, 0));
        panel.setOpaque(true);
        panel.add(createButton("newAreaOrdineGiroCliente.icon"));
        panel.add(createButton("confirmGiroCliente.icon"));
        JButton openButton = createButton("openAreaOrdineGiroCliente.icon");
        openButton.setBackground(Color.ORANGE);
        panel.add(openButton);
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isRollover(JTable table, MouseEvent event, int row, int column) {
        return true;
    }
}