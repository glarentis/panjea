package it.eurotn.rich.control.table.layout;

import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.services.TableLayoutCache;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.springframework.richclient.application.Application;

public class DefaultTableLayoutManager {

    protected JideTableWidget<?> tableWidget;

    private TableLayoutCache tableLayoutCache;

    private TableLayout defaultLayout;

    private List<PropertyChangeListener> listeners;

    /**
     * Costruttore.
     *
     * @param jideTableWidget
     *            table widget di riferimento
     */
    public DefaultTableLayoutManager(final JideTableWidget<?> jideTableWidget) {
        super();
        this.tableWidget = jideTableWidget;
        listeners = new ArrayList<PropertyChangeListener>();
    }

    /**
     * Aggiunge un listener chiamato sull'applicazione del layout.
     *
     * @param propertyChangeListener
     *            listener
     */
    public void addLayoutListener(PropertyChangeListener propertyChangeListener) {
        listeners.add(propertyChangeListener);
    }

    /**
     * Applica il layout specificato.
     *
     * @param layout
     *            layout da applicare
     */
    public void applica(TableLayout layout) {
        if (defaultLayout == null && layout == null) {
            // non ho nessun layout da applicare
            return;
        }

        AbstractLayout layoutToApply = defaultLayout;
        if (layout != null) {
            layoutToApply = layout;
        }
        if (layoutToApply.getData() != null) {
            InputStream inputStream = new ByteArrayInputStream(layoutToApply.getData().getBytes());
            this.tableWidget.loadLayout(inputStream);
            this.tableWidget.setNumberRowVisible(layout.getVisualizzaNumeriRiga());
            for (PropertyChangeListener listener : listeners) {
                listener.propertyChange(new PropertyChangeEvent(this, "layout", null, layoutToApply));
            }
        }
    }

    /**
     * Cancella il layout specificato.
     *
     * @param layout
     *            layout da cancellare
     * @return <code>true</code> se il layout è stato cancellato
     */
    public boolean cancella(TableLayout layout) {

        if (isReadOnly()) {
            return false;
        }

        if (layout.getName() == null) {
            return false;
        }

        getTableLayoutCache().cancella(layout);

        return true;
    }

    /**
     * @return Returns the defaultLayout.
     */
    public TableLayout getDefaultLayout() {
        if (defaultLayout == null) {
            defaultLayout = new TableLayout();
            defaultLayout.setChiave(this.tableWidget.getId());
            defaultLayout.setGlobal(false);
            defaultLayout.setEstendiColonne(false);
            defaultLayout.setVisualizzaNumeriRiga(true);
        }
        return defaultLayout;
    }

    /**
     * Carica tutti i layout per la table widget. NB: il layout di default non viene restituito.
     *
     * @return lista di layout presenti
     */
    public TableLayout getLayout(String name) {

        List<TableLayout> layouts = getLayouts();

        for (TableLayout tableLayout : layouts) {
            if (tableLayout.getName() != null && tableLayout.getName().equals(name)) {
                return tableLayout;
            }
        }

        return null;
    }

    /**
     * Carica tutti i layout per la table widget. NB: il layout di default non viene restituito.
     *
     * @return lista di layout presenti
     */
    public List<TableLayout> getLayouts() {

        List<TableLayout> layouts = getTableLayoutCache().caricaTableLayout(this.tableWidget.getId());

        List<TableLayout> tableLayouts = new ArrayList<TableLayout>();
        tableLayouts.addAll(layouts);
        // cerco il layout di default
        for (TableLayout layout : layouts) {
            if (layout.getUltimoCaricato() != null
                    && layout.getUltimoCaricato().contains(PanjeaSwingUtil.getUtenteCorrente().getUserName())) {
                defaultLayout = layout;
                break;
            }
        }
        // rimuovo il layout di default se è quello standard senza nome
        if (defaultLayout != null && defaultLayout.getName() == null) {
            tableLayouts.remove(defaultLayout);
        }

        return tableLayouts;
    }

    /**
     * @return the tableLayoutCache
     */
    public TableLayoutCache getTableLayoutCache() {
        if (tableLayoutCache == null) {
            tableLayoutCache = (TableLayoutCache) Application.services().getService(TableLayoutCache.class);
        }

        return tableLayoutCache;
    }

    /**
     * @return the tableWidget
     */
    public JideTableWidget<?> getTableWidget() {
        return tableWidget;
    }

    /**
     * @return id table widget
     */
    public String getTableWidgetId() {
        return this.tableWidget.getId();
    }

    /**
     *
     * @return false se il layoutManager può salvare/cancellare il layout
     */
    public boolean isReadOnly() {
        return false;
    }

    /**
     * Salva il layout della table widget.
     *
     * @param layout
     *            layout da salvare
     * @return layout salvato
     */
    public AbstractLayout salva(TableLayout layout) {

        if (isReadOnly()) {
            return null;
        }

        OutputStream dataLayout = new ByteArrayOutputStream();

        ((ITable<?>) this.tableWidget.getTable()).saveLayout(dataLayout);

        layout.setData(dataLayout.toString());

        // se salvo il layout di default lo assegno al risultato perchè avrà
        // l'id settato (quello di default nuovo no)
        // refactoring per una soluzione meno spartana.
        boolean riassegna = layout == defaultLayout;
        layout = getTableLayoutCache().salva(layout);
        if (riassegna) {
            defaultLayout = layout;
        }
        return layout;
    }

    /**
     * @param tableWidget
     *            the tableWidget to set
     */
    public void setTableWidget(JideTableWidget<?> tableWidget) {
        this.tableWidget = tableWidget;
    }

}
