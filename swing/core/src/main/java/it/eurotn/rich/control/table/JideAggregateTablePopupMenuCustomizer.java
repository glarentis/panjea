/**
 * 
 */
package it.eurotn.rich.control.table;

import it.eurotn.panjea.anagrafica.domain.Importo;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.JTableHeader;

import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.AggregateTablePopupMenuCustomizer;
import com.jidesoft.pivot.PivotField;

/**
 * @author fattazzo
 * 
 */
public class JideAggregateTablePopupMenuCustomizer extends AggregateTablePopupMenuCustomizer {

    @Override
    public void customizePopupMenu(final JTableHeader paramJTableHeader, JPopupMenu paramJPopupMenu, int paramInt) {
        super.customizePopupMenu(paramJTableHeader, paramJPopupMenu, paramInt);

        for (Component child : paramJPopupMenu.getComponents()) {
            if (child instanceof JMenuItem && CONTEXT_MENU_SET_SUBTOTAL.equals(child.getName())) {
                for (Component child2 : ((JMenu) child).getPopupMenu().getComponents()) {
                    if (child2 instanceof JCheckBoxMenuItem && CONTEXT_MENU_AUTOMATIC.equals(child2.getName())) {
                        String text = ((JCheckBoxMenuItem) child2).getText();

                        // come azione di default la classe importo se viene selezionato il subtotal automatico viene
                        // impostata a "conta". Per forzare la somma cerco l'azione della totalizzazione automatica, la
                        // eseguo e successivamente forzo per tutte le classi Importo il summary type 0 (somma).
                        final Action defaultAction = ((JCheckBoxMenuItem) child2).getAction();
                        Action newAction = new AbstractAction() {
                            private static final long serialVersionUID = 4922292794876426141L;

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                defaultAction.actionPerformed(e);

                                AggregateTable aggregateTable = ((AggregateTable) paramJTableHeader.getTable());
                                PivotField[] fileds = aggregateTable.getAggregateTableModel().getPivotDataModel()
                                        .getFields();
                                for (int i = 0; i < fileds.length; i++) {
                                    if (Importo.class.equals(fileds[i].getType())) {
                                        fileds[i].setSummaryType(0);
                                    }
                                }
                            }
                        };
                        ((JCheckBoxMenuItem) child2).setAction(newAction);
                        ((JCheckBoxMenuItem) child2).setText(text);
                    }
                }
            }
        }
    }
}
