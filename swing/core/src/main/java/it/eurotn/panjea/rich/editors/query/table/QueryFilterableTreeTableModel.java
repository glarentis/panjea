package it.eurotn.panjea.rich.editors.query.table;

import javax.swing.table.TableModel;

import com.jidesoft.grid.FilterableTreeTableModel;

import it.eurotn.panjea.rich.editors.query.ProprietaEntityRow;
import it.eurotn.panjea.rich.editors.query.table.filtri.ProprietaConOperatoreFilter;
import it.eurotn.panjea.rich.editors.query.table.filtri.ProprietaVisualizzateFilter;

public class QueryFilterableTreeTableModel extends FilterableTreeTableModel<ProprietaEntityRow> {

    private static final long serialVersionUID = 5341399839629374101L;

    private ProprietaConOperatoreFilter proprietaConOperatoreFilter = new ProprietaConOperatoreFilter();
    private ProprietaVisualizzateFilter proprietaVisualizzateFilter = new ProprietaVisualizzateFilter();

    /**
     * Costruttore.
     *
     * @param paramTableModel
     *            table model
     */
    public QueryFilterableTreeTableModel(final TableModel paramTableModel) {
        super(paramTableModel);

        setKeepAllChildren(false);
        addFilter(1, proprietaVisualizzateFilter);
        addFilter(2, proprietaConOperatoreFilter);
        setAndMode(false);
        setFiltersApplied(false);
        setCacheEnabled(true);
    }

    private void setFiltersEnable(boolean enable) {
        for (FilterItem filterItem : getFilterItems()) {
            filterItem.setEnabled(enable);
        }
        proprietaVisualizzateFilter.setEnabled(enable);
        proprietaConOperatoreFilter.setEnabled(enable);
    }

    @Override
    public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
        // Molto brutto, setto per riferimento! Non richiamo la setValueAt per 2 motivi:
        // 1. se ho il model filtrato una volta applicato il valore ci mette 2 ore per riapplicare i filtri
        // 2. non posso disabilitare i filtri perchè il TableModelWrapperUtils non riesce a darmi l'indice della riga
        // originale
        if (isFiltersApplied()) {
            ProprietaEntityRow row = (ProprietaEntityRow) getRowAt(paramInt1);

            setFiltersApplied(false);
            setFiltersEnable(false);
            row.setValueAt(paramObject, paramInt2);
            setFiltersEnable(true);
            setFiltersApplied(true);
        } else {
            super.setValueAt(paramObject, paramInt1, paramInt2);
        }
    }
}
