package it.eurotn.panjea.bi.rich.editors.analisi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.hssf.HssfPivotTableUtils;
import com.jidesoft.hssf.HssfTableFormat;
import com.jidesoft.hssf.HssfTableUtils;
import com.jidesoft.hssf.HssfTableUtils.CellValueConverter;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.swing.JideSplitPane;

import it.eurotn.panjea.bi.rich.editors.analisi.converter.NumberPivotContext;
import it.eurotn.panjea.bi.rich.editors.analisi.converter.NumberPivotConverter;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiPivotDataModel;
import it.eurotn.panjea.bi.rich.editors.analisi.style.ExcelPivotCellStyleProvider;
import it.eurotn.panjea.bi.rich.editors.analisi.style.PivotCellStyleProvider;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Gestisce la visualizzazione delle query di business.<br/>
 *
 * @author giangi
 *
 */
public class AnalisiBiPivotTablePage extends AbstractDialogPage implements IPageLifecycleAdvisor, IEditorCommands {

    /**
     * This class converts objects not handled by the HSSF converter.
     */
    public class ExportCellValueConverter extends HssfTableUtils.DefaultCellValueConverter {
        @Override
        public Object convert(JTable table, Object value, int rowIndex, int columnIndex) {
            if (value instanceof Number || value instanceof Date) {
                return value;
            } else {
                // se il valore da esportare non è una data o un numero allora passo attraverso il
                // converter.
                ConverterContext converterCtx = null;
                PivotField[] datafields = ((AnalisiBiPivotDataModel) pivotTablePane.getPivotDataModel()).getFields();
                for (PivotField pivotField : datafields) {
                    if (pivotField.getTitle().equals(table.getColumnName(columnIndex))) {
                        converterCtx = pivotField.getConverterContext();
                        break;
                    }
                }

                if (value != null) {
                    return ObjectConverterManager.toString(value, value.getClass(), converterCtx);
                } else {
                    return value;
                }
            }
        }

        @Override
        public int getDataFormat(JTable table, Object value, int rowIndex, int columnIndex) {
            if (value instanceof Number) {
                PivotField[] datafields = ((AnalisiBiPivotDataModel) pivotTablePane.getPivotDataModel()).getFields();

                for (PivotField pivotField : datafields) {
                    if (pivotField.getTitle().equals(table.getColumnName(columnIndex))) {
                        ConverterContext converterCtx = pivotField.getConverterContext();

                        if (converterCtx != null) {
                            if ((converterCtx instanceof NumberPivotContext)) {
                                int numeroDecimali = ((NumberPivotContext) converterCtx).getNumeroDecimali();
                                return ((HSSFWorkbook) table.getClientProperty("HssfTableUtils.HSSFWorkbook"))
                                        .createDataFormat()
                                        .getFormat(NumberPivotConverter.DECIMAL_FORMAT[numeroDecimali].toPattern());
                            } else {
                                return ((HSSFWorkbook) table.getClientProperty("HssfTableUtils.HSSFWorkbook"))
                                        .createDataFormat().getFormat("TEXT");
                            }
                        }
                        return super.getDataFormat(table, value, rowIndex, columnIndex);
                    }
                }
                return super.getDataFormat(table, value, rowIndex, columnIndex);
            } else if (value instanceof Date) {
                return 0xe; // use m/d/yyyy
            }
            return super.getDataFormat(table, value, rowIndex, columnIndex);
        }
    }

    public static final String PAGE_ID = "analisiBiPivotTablePage";
    private AnalisiBiPivotTablePane pivotTablePane;
    private AnalisiPivotChooserFieldsPanel fieldChooser;

    /**
     * Costruttore.
     */
    public AnalisiBiPivotTablePage() {
        super(PAGE_ID);
        pivotTablePane = new AnalisiBiPivotTablePane();
        // Registro i converter generici del pivot
        ObjectConverterManager.registerConverter(int.class, new NumberPivotConverter(), new NumberPivotContext());
        ObjectConverterManager.registerConverter(Integer.class, new NumberPivotConverter(), new NumberPivotContext());
        ObjectConverterManager.registerConverter(double.class, new NumberPivotConverter(), new NumberPivotContext());
        ObjectConverterManager.registerConverter(Double.class, new NumberPivotConverter(), new NumberPivotContext());
        ObjectConverterManager.registerConverter(BigDecimal.class, new NumberPivotConverter(),
                new NumberPivotContext());
    }

    @Override
    protected JComponent createControl() {
        JideSplitPane mainPanel = new JideSplitPane();
        mainPanel.setOneTouchExpandable(true);
        mainPanel.setDividerSize(10);
        fieldChooser = new AnalisiPivotChooserFieldsPanel(pivotTablePane);
        mainPanel.add(fieldChooser);

        mainPanel.add(pivotTablePane);

        return mainPanel;
    }

    @Override
    public void dispose() {
        pivotTablePane.dispose();
        pivotTablePane = null;
    }

    /**
     * Esporta la pivot in un foglio di excel.
     *
     * @param fileName
     *            nome del file dove esportare i dati
     * @param nomeAnalisi
     *            nome dell'analisi che verrà usato come nome del sheet
     * @throws IOException
     *             errore sul file
     */
    public void esportaPivotPane(String fileName, final String nomeAnalisi) throws IOException {
        pivotTablePane.getPivotDataModel().setCellStyleProvider(new ExcelPivotCellStyleProvider());
        try {
            HssfPivotTableUtils.export(pivotTablePane, fileName, nomeAnalisi, false, new HssfTableFormat() {
                @Override
                public CellValueConverter getCellValueConverter() {
                    // return new HssfT ableUtils.DefaultCellValueConverter();
                    return new ExportCellValueConverter();
                }

                @Override
                public String getHeader() {
                    return nomeAnalisi;
                }

                @Override
                public boolean isAutoSizeColumns() {
                    return true;
                }

                @Override
                public boolean isFreezePanes() {
                    return true;
                }

                @Override
                public boolean isIncludeTableHeader() {
                    return false;
                }

                @Override
                public boolean isPrintFitToPage() {
                    return true;
                }

                @Override
                public boolean isPrintLandscape() {
                    return true;
                }
            });
        } catch (Exception e) {
            logger.error("-->errore nell'esporare la ricerca di business in oocalc", e);
            throw new PanjeaRuntimeException(e);
        } finally {
            pivotTablePane.getPivotDataModel().setCellStyleProvider(new PivotCellStyleProvider());
        }
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        return null;
    }

    /**
     *
     * @return il pivotDataModel associato a questa pagina
     */
    public IPivotDataModel getPivotDataModel() {
        return pivotTablePane.getPivotDataModel();
    }

    /**
     *
     * @return pivotTablePane contenuto nella pagina.
     */
    public AnalisiBiPivotTablePane getPivotTablePane() {
        return pivotTablePane;
    }

    /**
     *
     * @return true se il fieldChooser è visibile
     */
    public boolean isFieldChooserVisible() {
        return fieldChooser.isVisible();
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return false;
    }

    @Override
    public void postSetFormObject(Object object) {
        if (pivotTablePane.getDataTable() != null) {
            pivotTablePane.fieldsUpdated();
        }
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void restoreState(Settings arg0) {

    }

    @Override
    public void saveState(Settings arg0) {

    }

    /**
     * Setta la visibilità.
     *
     * @param visible
     *            visible
     */
    public void setFieldChooserVisible(boolean visible) {
        fieldChooser.setVisible(visible);
        // pivotTablePane.setFilterFieldAreaVisible(visible);
        // pivotTablePane.setRowFieldAreaVisible(visible);
        // pivotTablePane.setColumnFieldAreaVisible(visible);
        // pivotTablePane.setDataFieldAreaVisible(visible);
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof IPivotDataModel) {
            pivotTablePane.setPivotDataModel((IPivotDataModel) object);
            fieldChooser.setPivotDataModel((IPivotDataModel) object);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    /**
     * da il focus alla cella con le coordinate e la rende visibile.
     *
     * @param rowSelected
     *            riga Selezionata
     * @param columnSelected
     *            colonna selezionata
     */
    public void setSelection(int rowSelected, int columnSelected) {
        pivotTablePane.getDataTable().changeSelection(rowSelected, columnSelected, false, false);
    }

}
