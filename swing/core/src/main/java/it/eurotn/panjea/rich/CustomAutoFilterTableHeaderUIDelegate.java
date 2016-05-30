package it.eurotn.panjea.rich;

/*
 * @(#)CustomAutoFilterTableHeaderUIDelegate.java 7/20/2012
 *
 * Copyright 2002 - 2012 JIDE Software Inc. Al
 */

import javax.swing.CellRendererPane;
import javax.swing.table.JTableHeader;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.filter.CustomFilterEditor;
import com.jidesoft.filter.FilterFactoryManager;
import com.jidesoft.plaf.basic.BasicAutoFilterTableHeaderUIDelegate;

public class CustomAutoFilterTableHeaderUIDelegate extends BasicAutoFilterTableHeaderUIDelegate {

    /**
     * CustomAutoFilterTableHeaderUIDelegate per aggiungere il supporto dei filtri custom per la tabella dove essi non
     * gestiscono di default classi diverse dal tipo della colonna.<br>
     * 
     * @see Chiamare queste righe per registrarlo:<br>
     *      <br>
     *      <code>UIManager.getDefaults().put("TableHeader.autoFilterTableHeaderUIDelegate",CustomAutoFilterTableHeaderUIDelegate.class.getName());</code>
     *      <code>UIManager.getDefaults().put("TableHeader.groupTableHeaderUIDelegate",CustomAutoFilterTableHeaderUIDelegate.class.getName());</code>
     * 
     * @param header
     *            JTableHeader
     * @param rendererPane
     *            CellRendererPane
     */
    public CustomAutoFilterTableHeaderUIDelegate(final JTableHeader header, final CellRendererPane rendererPane) {
        super(header, rendererPane);
    }

    @Override
    protected CustomFilterEditor createCustomFilterEditor(FilterFactoryManager filterFactoryManager, Class<?> type,
            ConverterContext converterContext, Object[] possibleValues) {

        return new CustomFilterEditor(filterFactoryManager, type, converterContext, possibleValues) {
            @Override
            protected Object getValidValueForClass(Class expectedClass, Object value) {
                if (expectedClass == Integer.class && value instanceof String) {
                    return value;
                }
                return super.getValidValueForClass(expectedClass, value);
            }
        };
    }
}
