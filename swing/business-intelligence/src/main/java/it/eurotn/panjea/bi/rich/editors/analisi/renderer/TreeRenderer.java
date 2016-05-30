package it.eurotn.panjea.bi.rich.editors.analisi.renderer;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.springframework.richclient.image.EmptyIcon;
import org.springframework.richclient.image.IconSize;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotDataModel;
import com.jidesoft.pivot.PivotField;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Tabella;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaDimensione;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaFatti;
import it.eurotn.panjea.rich.components.CompoundIcon;

@SuppressWarnings("serial")
public class TreeRenderer extends DefaultTreeCellRenderer {

    private static Map<String, Icon> iconsTabelle = new HashMap<>();

    static {
        iconsTabelle.put(TabellaDimensione.class.getName(), RcpSupport.getIcon(TabellaDimensione.class.getName()));
        iconsTabelle.put(TabellaFatti.class.getName(), RcpSupport.getIcon(TabellaFatti.class.getName()));
        iconsTabelle.put("DIMENSIONI", RcpSupport.getIcon(TabellaDimensione.class.getName()));
        iconsTabelle.put("MISURE", RcpSupport.getIcon(TabellaFatti.class.getName()));
    }

    private IPivotDataModel pivotDataModel;

    private void configureColonnaDimensioneRender(PivotField pivotField, JLabel label) {
        label.setText(pivotField.getTitle());
        label.setIcon(getIcon(pivotField));
    }

    private void configureColonnaMisuraRenderer(ColumnMeasure misura, JLabel label) {
        label.setText(misura.getTitle());
        label.setIcon(getIcon(misura));
    }

    Icon createDisableIcon(ImageIcon icon) {
        return new ImageIcon(GrayFilter.createDisabledImage((icon.getImage())));
    }

    private Icon getIcon(ColumnMeasure misura) {
        final String[] funzioni = AnalisiBIDomain.FUNZIONI_AGGREGAZIONE;
        final Icon[] icons = new Icon[funzioni.length];
        for (int i = 0; i < funzioni.length; i++) {
            final String funzione = funzioni[i];
            icons[i] = new EmptyIcon(IconSize.SMALL);
            String key = misura.getKey();
            int endString = key.indexOf(misura.getTabella().getAlias()) - 1;
            if (endString < 0) {
                endString = 0;
            }
            final String functionToReplace = key.substring(0, endString);
            key = key.replace(functionToReplace, funzione);
            if (pivotDataModel.getField(key) != null) {
                icons[i] = RcpSupport.getIcon(funzione);
            }
        }
        return new CompoundIcon(icons);
    }

    private Icon getIcon(PivotField pivotField) {
        Icon rowIcon = RcpSupport.getIcon("addrow.icon");
        if (pivotField.getAreaType() != PivotDataModel.AREA_ROW) {
            rowIcon = createDisableIcon((ImageIcon) rowIcon);
        }

        Icon colIcon = RcpSupport.getIcon("addcolumn.icon");
        if (pivotField.getAreaType() != PivotDataModel.AREA_COLUMN) {
            colIcon = createDisableIcon((ImageIcon) colIcon);
        }

        Icon filterIcon = RcpSupport.getIcon("visualizzaFiltroCommand.icon");
        if (pivotField.getAreaType() != PivotDataModel.AREA_FILTER) {
            filterIcon = createDisableIcon((ImageIcon) filterIcon);
        }

        return new CompoundIcon(colIcon, rowIcon, filterIcon);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object,
     * boolean, boolean, boolean, int, boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        final JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                hasFocus);
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        final Object userObject = node.getUserObject();
        if (userObject instanceof Tabella) {
            label.setIcon(iconsTabelle.get(userObject.getClass().getSuperclass().getName()));
            label.setText(((Tabella) userObject).getTitle());
        } else if (userObject instanceof ColumnMeasure) {
            final ColumnMeasure misura = (ColumnMeasure) userObject;
            configureColonnaMisuraRenderer(misura, label);

        } else if (userObject instanceof Colonna) {
            configureColonnaDimensioneRender(pivotDataModel.getField(((Colonna) userObject).getKey()), label);
        } else if (userObject instanceof String) {
            label.setIcon(iconsTabelle.get(userObject));
        }

        // label.setIcon(RcpSupport.getIcon(Articolo.class.getName()));

        return label;
    }

    /**
     * @param pivotDataModel
     *            The pivotDataModel to set.
     */
    public void setPivotDataModel(IPivotDataModel pivotDataModel) {
        this.pivotDataModel = pivotDataModel;
    }

}
