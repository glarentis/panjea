package it.eurotn.panjea.magazzino.rich.editors.articolo;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.HyperlinkTableCellEditorRenderer;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.swing.JideButton;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 *
 * Render per la giacenza
 *
 * @author giangi
 * @version 1.0, 29/dic/2014
 *
 */
public class GiacenzaCellRenderer extends HyperlinkTableCellEditorRenderer {
    private static final long serialVersionUID = -2093710302456791755L;
    public static final EditorContext GIACENZA_ARTICOLO_CONTEXT = new EditorContext("GIACENZA_ARTICOLO_CONTEXT");

    /**
     * Costruttore.
     */
    public GiacenzaCellRenderer() {

        setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JideButton source = (JideButton) event.getSource();
                IMagazzinoDocumentoBD magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
                IMagazzinoAnagraficaBD magazzinoAnagraficaDocumentoBD = RcpSupport
                        .getBean(MagazzinoAnagraficaBD.BEAN_ID);
                ArticoloLite articolo = magazzinoAnagraficaDocumentoBD
                        .caricaArticoloLite((Integer) source.getClientProperty("idArticolo"));
                StatisticheArticolo statisticheArticolo = magazzinoDocumentoBD
                        .caricaStatisticheArticolo(articolo.creaProxyArticolo());
                StatisticaDialog dialog = new StatisticaDialog(statisticheArticolo, null, articolo,
                        magazzinoDocumentoBD);
                dialog.showDialog();
            }
        });
    }

    @Override
    public void configureTableCellEditorRendererComponent(JTable paramJTable, Component paramComponent,
            boolean paramBoolean1, Object paramObject, boolean paramBoolean2, boolean paramBoolean3, int paramInt1,
            int paramInt2) {
        super.configureTableCellEditorRendererComponent(paramJTable, paramComponent, paramBoolean1, paramObject,
                paramBoolean2, paramBoolean3, paramInt1, paramInt2);
        JideButton button = (JideButton) paramComponent;
        button.setButtonStyle(3);
        Font font = button.getFont().deriveFont(18);
        button.setFont(font);
        button.setHorizontalAlignment(SwingConstants.RIGHT);
        @SuppressWarnings("unchecked")
        DefaultBeanTableModel<ArticoloRicerca> tableModel = (DefaultBeanTableModel<ArticoloRicerca>) TableModelWrapperUtils
                .getActualTableModel(paramJTable.getModel());

        int actualRow = TableModelWrapperUtils.getActualRowAt(paramJTable.getModel(), paramInt1);
        if (actualRow != -1) {
            ArticoloRicerca articolo = tableModel.getObject(actualRow);
            button.putClientProperty("idArticolo", articolo.getId());
            if (articolo.getGiacenza() != null) {
                String giacenzaFormat = new DecimalFormat(
                        "#,###,###,##0." + StringUtils.repeat("0", articolo.getNumeroDecimaliQta()))
                                .format(articolo.getGiacenza());
                button.setText(giacenzaFormat);
            } else {
                button.setText("");
            }
        }
    }
}