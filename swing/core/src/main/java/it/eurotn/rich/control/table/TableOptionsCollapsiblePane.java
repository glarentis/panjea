package it.eurotn.rich.control.table;

import java.awt.Color;

import javax.swing.BorderFactory;

import com.jidesoft.pane.CollapsiblePane;

public class TableOptionsCollapsiblePane extends CollapsiblePane {

    private static final long serialVersionUID = -6108909348618951316L;

    /**
     * Costruttore.
     * 
     */
    public TableOptionsCollapsiblePane() {
        super();
        setStyle(CollapsiblePane.DROPDOWN_STYLE);
        setEmphasized(true);
        setLayout(new org.jdesktop.swingx.VerticalLayout(4));
        collapse(false);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getContentPane().setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        setBackground(new Color(204, 204, 214));
    }
}
