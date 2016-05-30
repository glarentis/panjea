package it.eurotn.panjea.bi.rich.editors.analisi;

import java.awt.Container;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.pivot.DataTable;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.pivot.PivotTablePane;
import com.jidesoft.pivot.RunningSummary;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.AnalisiValueSelected;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;
import it.eurotn.panjea.bi.domain.analisi.RunningSummaryBILayout;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiPivotDataModel;

public class AnalisiBiPivotTablePane extends PivotTablePane {

    /**
     * @author fattazzo
     *
     */
    private final class DataTableSelectionListener implements ListSelectionListener {
        private final DataTable dataTable;

        /**
         * Costruttore.
         *
         * @param dataTable
         *            table
         */
        private DataTableSelectionListener(final DataTable dataTable) {
            this.dataTable = dataTable;
        }

        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                int[] selectionCell = new int[] { dataTable.getSelectedRow(), dataTable.getSelectedColumn() };
                firePropertyChange(AnalisiBiEditorController.SELECTION_EVENT, null, selectionCell);
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(AnalisiBiPivotTablePane.class);

    private static final long serialVersionUID = -5626124374096026555L;

    private DataTableSelectionListener dataTableSelectionListener;

    /**
     *
     * Costruttore.
     *
     */
    public AnalisiBiPivotTablePane() {
        super();
        setShowFilterIcon(true);
        setAutoscrolls(true);
        setPlainHeaderTables(true);
        setFlatLayout(true);
        setShrinkDataFieldArea(true);
        setFieldChooserVisible(false);
    }

    /**
     * applica il layout contenuto nell'analisi. Il layout è comprensivo dei filtri
     *
     * @param analisiBi
     *            analisi con il layout da applicare
     */
    public void applyLayout(AnalisiBi analisiBi) {
        // ripulisco il data model esistente creandone uno nuovo
        AnalisiBiPivotDataModel dataModel = new AnalisiBiPivotDataModel(getPivotDataModel().getDataSource());
        setPivotDataModel(dataModel);
        fieldsUpdated();

        AnalisiBILayout analisiBILayout = analisiBi.getAnalisiLayout();
        getPivotDataModel().setShowGrandTotalForColumn(analisiBILayout.isTotalForColumn());
        getPivotDataModel().setShowGrandTotalForRow(analisiBILayout.isTotalForRow());

        for (PivotField pivotField : getPivotDataModel().getFields()) {
            if (analisiBILayout.getFields().containsKey(pivotField.getName())) {
                FieldBILayout fieldLayout = analisiBILayout.getFields().get(pivotField.getName());
                updatePivotField(fieldLayout, pivotField);
            }
        }

        Map<String, AnalisiValueSelected> filtri = analisiBi.getFiltri();
        AnalisiBiPersistenceUtil.applicaFiltri(getPivotDataModel(), filtri);
        fieldsUpdated();
    }

    private int[] convertStringToIntArray(String string) {
        if (string == null) {
            return null;
        }
        String[] strings = string.split(",");
        final int[] ints = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            ints[i] = Integer.parseInt(strings[i].trim());
        }
        return ints;
    }

    @Override
    protected DataTable createDataTable() {
        final DataTable dataTable = super.createDataTable();
        dataTableSelectionListener = new DataTableSelectionListener(dataTable);
        dataTable.getSelectionModel().addListSelectionListener(dataTableSelectionListener);
        return dataTable;
    }

    // @Override
    // protected FieldBox createFieldBox(PivotField paramPivotField, boolean paramBoolean1, boolean
    // paramBoolean2) {
    // FieldBox fieldBox = super.createFieldBox(paramPivotField, paramBoolean1, paramBoolean2);
    //
    // Colonna column = AnalisiBIDomain.getColonne().get(paramPivotField.getName());
    //
    // fieldBox.getField().setName(column.getKey());
    // fieldBox.getField().setTitle(column.getTitle());
    // boolean misureAllowed = (column instanceof ColumnMeasure);
    // fieldBox.getField().setAllowedAsDataField(misureAllowed);
    // fieldBox.getField().setAllowedAsColumnField(!misureAllowed);
    // fieldBox.getField().setAllowedAsRowField(!misureAllowed);
    //
    // if (column instanceof ColumnMeasure && !(column instanceof ColumnMeasureFunction)) {
    // ColumnMeasure colonna = (ColumnMeasure) AnalisiBIDomain.getColonna(column.getKey());
    // fieldBox.setIcon(RcpSupport.getIcon(colonna.getFunzioneAggregazione()));
    // fieldBox.setIconTextGap(0);
    // fieldBox.setToolTipText(column.getTitle());
    // if (paramPivotField.getAreaType() == PivotField.AREA_NOT_ASSIGNED) {
    // fieldBox.getField().setTitle("");
    // fieldBox.setText("");
    // }
    // }
    // return fieldBox;
    // }

    @Override
    protected void customizeFieldsPanel(Container paramContainer) {
        super.customizeFieldsPanel(paramContainer);
        // paramContainer.remove(getFieldChooserComboBoxPanel());

        // paramContainer.setLayout(new BorderLayout());
        // JPanel header = new JPanel(new VerticalLayout());
        // header.add(com.jgoodies.forms.factories.DefaultComponentFactory.getInstance()
        // .createSeparator("Filtri impostati", SwingConstants.CENTER));
        // JScrollPane headerScroll = new JScrollPane(filtriPane);
        // headerScroll.getViewport().setPreferredSize(new Dimension(100, 100));
        // header.add(headerScroll);
        // paramContainer.add(header, BorderLayout.NORTH);
        // paramContainer.add(new SimpleScrollPane(containerFiltri), BorderLayout.CENTER);
        setShowFilterButtonsOnMouseOver(true);
    }

    /**
     * Dispose del table pane.
     */
    public void dispose() {
        getDataTable().getSelectionModel().removeListSelectionListener(dataTableSelectionListener);
        dataTableSelectionListener = null;
    }

    /**
     * Salva lo stato del table pane sull'analisi.
     *
     * @param analisiBi
     *            analisi su cui salvare lo stato
     * @return analisi
     */
    public AnalisiBi saveAnalisi(AnalisiBi analisiBi) {

        analisiBi.setShowFieldList(isFieldChooserVisible());

        AnalisiBILayout analisiBILayout = new AnalisiBILayout();
        analisiBILayout.setTotalForColumn(getPivotDataModel().isShowGrandTotalForColumn());
        analisiBILayout.setTotalForRow(getPivotDataModel().isShowGrandTotalForRow());

        List<PivotField> fields = new ArrayList<PivotField>();
        fields.addAll(Arrays.asList(getPivotDataModel().getRowFields()));
        fields.addAll(Arrays.asList(getPivotDataModel().getColumnFields()));
        fields.addAll(Arrays.asList(getPivotDataModel().getDataFields()));
        fields.addAll(Arrays.asList(getPivotDataModel().getFilterFields()));

        for (PivotField pivotField : fields) {
            FieldBILayout fieldBILayout = new FieldBILayout(pivotField.getName(), pivotField.getTitle(),
                    pivotField.getPreferredWidth(), pivotField.getAreaType(), pivotField.getAreaIndex(),
                    pivotField.getModelIndex(), pivotField.isDuplicated(), pivotField.getSubtotalType(),
                    pivotField.getSubtotalTypeForRow(), pivotField.getSubtotalTypeForColumn(),
                    pivotField.getCustomSubtotals(), pivotField.getCustomSubtotalsForRow(),
                    pivotField.getCustomSubtotalsForColumn(), pivotField.isSeparateSubtotalSettings(),
                    pivotField.getSummaryType(), pivotField.getGrandTotalSummaryType(), pivotField.isAscending(),
                    pivotField.isHideOriginalData());
            if (pivotField.getRunningSummaryList() != null) {
                List<RunningSummaryBILayout> runningSummaryList = new ArrayList<RunningSummaryBILayout>();
                for (RunningSummary summary : pivotField.getRunningSummaryList()) {
                    String compareTo = null;
                    try {
                        compareTo = ObjectConverterManager.toString(summary.getCompareTo(),
                                Class.forName(summary.getCompareTo().getClass().getName()),
                                new ConverterContext("CompareTo"));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    RunningSummaryBILayout runningSummary = new RunningSummaryBILayout(summary.getRunningType(),
                            summary.getBaseOnField().getName(), summary.getRangeInField().getName(), compareTo,
                            "com.jidesoft.pivot.RunningSummary$b_");
                    runningSummaryList.add(runningSummary);
                }
                fieldBILayout.setRunningSummary(runningSummaryList);
            }

            analisiBILayout.addField(fieldBILayout);
        }

        analisiBi.setFiltri(AnalisiBiPersistenceUtil.creaFiltri(getPivotDataModel()));
        analisiBi.setAnalisiLayout(analisiBILayout);

        return analisiBi;
    }

    @Override
    public void setPivotDataModel(IPivotDataModel paramIPivotDataModel) {
        super.setPivotDataModel(paramIPivotDataModel);
    }

    private void updatePivotField(FieldBILayout fieldLayout, PivotField pivotField) {
        pivotField.setName(fieldLayout.getName());
        pivotField.setTitle(fieldLayout.getTitle());
        pivotField.setPreferredWidth(fieldLayout.getWidth());
        pivotField.setAreaType(fieldLayout.getAreaType());
        pivotField.setAreaIndex(fieldLayout.getAreaIndex());
        pivotField.setDuplicated(fieldLayout.isDuplicated());
        pivotField.setSubtotalType(fieldLayout.getSubtotalType());
        pivotField.setSubtotalTypeForRow(fieldLayout.getSubtotalTypeForRow());
        pivotField.setSubtotalTypeForColumn(fieldLayout.getSubtotalTypeForColumn());
        pivotField.setCustomSubtotals(convertStringToIntArray(fieldLayout.getSubtotal()));
        pivotField.setCustomSubtotalsForRow(convertStringToIntArray(fieldLayout.getSubtotalForRow()));
        pivotField.setCustomSubtotalsForColumn(convertStringToIntArray(fieldLayout.getSubtotalForColumn()));
        pivotField.setSeparateSubtotalSettings(fieldLayout.isSeparateSubtotalSettings());
        pivotField.setSummaryType(fieldLayout.getSummaryType());
        pivotField.setGrandTotalSummaryType(fieldLayout.getGrandTotalSummaryType());
        pivotField.setAscending(fieldLayout.isAscending());
        pivotField.setHideOriginalData(fieldLayout.isHideOriginal());

        if (fieldLayout.getRunningSummary() != null) {
            List<RunningSummary> runningSummarieList = new ArrayList<RunningSummary>();
            for (RunningSummaryBILayout summaryBILayout : fieldLayout.getRunningSummary()) {
                RunningSummary runningSummary = new RunningSummary();
                runningSummary.setRunningType(summaryBILayout.getRunningSummaryType());
                if (!StringUtils.isEmpty(summaryBILayout.getBaseOnField())) {
                    runningSummary.setBaseOnField(getPivotDataModel().getField(summaryBILayout.getBaseOnField()));
                }
                if (!StringUtils.isEmpty(summaryBILayout.getRangeInField())) {
                    runningSummary.setRangeInField(getPivotDataModel().getField(summaryBILayout.getRangeInField()));
                } else {
                    try {
                        // se il range in field non è configurato devo inserire il filed predefinito
                        // (totale) che è una
                        // inner class privata all'interno di RunningSummary. L'unico modo che ho è
                        // quello di
                        // istanziarmela con la reflection
                        Class<?> classA = RunningSummary.class.getDeclaredClasses()[1];
                        Constructor<?> constructor = classA.getDeclaredConstructors()[0];
                        constructor.setAccessible(true);
                        Object innerObject = constructor.newInstance();
                        runningSummary.setRangeInField((PivotField) innerObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Object object = null;

                try {
                    object = ObjectConverterManager.fromString(summaryBILayout.getCompareTo(),
                            Class.forName(summaryBILayout.getCompareToClassName()), new ConverterContext("CompareTo"));
                } catch (ClassNotFoundException e) {
                    LOGGER.error("-->errore durante il caricamento del valore del compate to del running summary", e);
                }
                runningSummary.setCompareTo(object);
                runningSummarieList.add(runningSummary);
            }
            pivotField.setRunningSummaryList(runningSummarieList);
        }
    }

}
