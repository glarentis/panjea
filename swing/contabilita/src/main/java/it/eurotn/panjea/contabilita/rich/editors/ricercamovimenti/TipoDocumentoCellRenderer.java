package it.eurotn.panjea.contabilita.rich.editors.ricercamovimenti;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

/*
 * @(#)FilecellRenderer.java 6/29/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.SottoConto;

public class TipoDocumentoCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -7999672695165014551L;

    private Icon tipoDocumentoIcon;
    private Icon sottoContoIcon;

    {
        tipoDocumentoIcon = RcpSupport.getIcon(TipoDocumento.class.getName());
        sottoContoIcon = RcpSupport.getIcon(SottoConto.class.getName());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        int rowsIndex = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

        Font f = label.getFont();

        if (rowsIndex != -1) {
            ControlloMovimentiTableModel tableModel = (ControlloMovimentiTableModel) TableModelWrapperUtils
                    .getActualTableModel(table.getModel(), ControlloMovimentiTableModel.class);
            ControlloMovimentoRow controlloMovimentoRow = tableModel.getRowAt(rowsIndex);
            switch (controlloMovimentoRow.getRowType()) {
            case AREA:
                label.setIcon(tipoDocumentoIcon);
                label.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
                break;
            default:
                label.setIcon(sottoContoIcon);
                break;
            }
        }
        return label;
    }
}