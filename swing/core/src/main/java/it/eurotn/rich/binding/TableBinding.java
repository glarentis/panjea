package it.eurotn.rich.binding;

import java.awt.Dimension;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;

import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.support.CustomBinding;
import org.springframework.richclient.util.Assert;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JideTableWidget;

public class TableBinding<T> extends CustomBinding {

    private class AggiungiRigaCommand extends ActionCommand {

        public static final String COMMAND_ID = "aggiungiCommand";

        /**
         * Costruttore.
         */
        public AggiungiRigaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            setLabel("");
        }

        @Override
        public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                CommandButtonConfigurer buttonConfigurer) {
            final AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
            button.setName(formModel.getId() + "." + COMMAND_ID);
            return button;
        }

        @Override
        protected void doExecuteCommand() {

            if (formInsert.getFormModel().isCommittable()) {
                Object riga = formInsert.getFormModel().getFormObject();
                final ConversionExecutor converter = getConversionService().getConversionExecutor(riga.getClass(),
                        tableModel.getClassObj());
                riga = converter.execute(riga);

                // controllo se posso inserire la riga
                final boolean canInsert = riga != null;

                if (canInsert) {
                    @SuppressWarnings("unchecked")
                    final Collection<Object> righe = (Collection<Object>) getValueModel().getValue();

                    righe.add(riga);
                    getValueModel().setValue(PanjeaSwingUtil.cloneObject(righe));
                    formInsert.getNewFormObjectCommand().execute();
                    formInsert.grabFocus();
                }
            }
        }
    }

    private class RimuoviRigheCommand extends ActionCommand {

        public static final String COMMAND_ID = "deleteCommand";

        /**
         * Costruttore.
         */
        public RimuoviRigheCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            setLabel("");
        }

        @Override
        public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                CommandButtonConfigurer buttonConfigurer) {
            final AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
            button.setName(formModel.getId() + "." + COMMAND_ID);
            return button;
        }

        @Override
        protected void doExecuteCommand() {

            final List<T> righeDaCancellare = tableWidget.getSelectedObjects();

            if (righeDaCancellare != null && !righeDaCancellare.isEmpty()) {

                @SuppressWarnings("unchecked")
                final Collection<T> righe = (Collection<T>) getValueModel().getValue();
                righe.removeAll(righeDaCancellare);
                getValueModel().setValue(PanjeaSwingUtil.cloneObject(righe));
            }
        }
    }

    public static final String LABEL_RIGHE_LOTTI_NOT_VALID = "labelRigheLotti.notValid";

    public static final String LABEL_RIGHE_LOTTI_VALID = "labelRigheLotti.valid";

    private ActionCommand aggiungiRigaCommand;

    private AbstractFocussableForm formInsert;
    private final String formPropertyPath;
    private ActionCommand rimuoviRigheCommand;
    private DefaultBeanTableModel<T> tableModel;
    private JideTableWidget<T> tableWidget;

    private final int width;

    /**
     * Costruttore.
     *
     * @param formModel
     *            formModel legato al binding
     * @param formPropertyPath
     *            path della proprietà bindata
     * @param dluWidth
     *            larghezza del binding
     */
    public TableBinding(final FormModel formModel, final String formPropertyPath, final int dluWidth) {
        super(formModel, formPropertyPath, Collection.class);
        this.width = dluWidth;
        this.formPropertyPath = formPropertyPath;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected JComponent doBindControl() {
        Assert.notNull(tableModel);
        if (tableWidget == null) {
            tableWidget = new JideTableWidget(tableModel.getModelId(), tableModel);
        }
        if (formInsert != null) {
            final JTableHeader header = ((ITable<T>) tableWidget.getTable()).getTableHeader(tableWidget.getTable());
            header.setPreferredSize(new Dimension(0, 2));
            header.setVisible(false);
            header.setBorder(BorderFactory.createEmptyBorder());
            tableWidget.getTable().setTableHeader(header);
        }
        tableWidget.getTable().setShowGrid(false);
        tableWidget.getTable().setShowVerticalLines(false);

        FormLayout layout;
        if (width == 0) {
            layout = new FormLayout("p:grow,left:11dlu", "top:default,default:none");
        } else {
            layout = new FormLayout("fill:" + width + "dlu,left:11dlu", "top:default,default:none");
        }
        final PanelBuilder builder = new PanelBuilder(layout);

        final CellConstraints cc = new CellConstraints();
        if (formInsert != null) {
            builder.add(formInsert.getControl());
            builder.add(getAggiungiRigaCommand().createButton(),
                    cc.rc(1, 2, CellConstraints.BOTTOM, CellConstraints.LEFT));
            new PanjeaFormGuard(formInsert.getFormModel(), getAggiungiRigaCommand());
            builder.nextRow();
        }
        final JScrollPane tableScrollPane = getComponentFactory().createScrollPane(tableWidget.getTable());
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        builder.add(tableScrollPane);
        if (formInsert != null) {
            builder.add(getRimuoviRigheCommand().createButton(),
                    cc.rc(2, 2, CellConstraints.TOP, CellConstraints.LEFT));
        }
        builder.nextRow();

        if (getFormModel().getValueModel(formPropertyPath).getValue() != null) {
            tableWidget.setRows((Collection<T>) getFormModel().getValueModel(formPropertyPath).getValue());
        }

        return builder.getPanel();
    }

    @Override
    protected void enabledChanged() {

    }

    /**
     * @return command per aggiungere la riga nel formInsert
     */
    public ActionCommand getAggiungiRigaCommand() {
        if (aggiungiRigaCommand == null) {
            aggiungiRigaCommand = new AggiungiRigaCommand();
        }
        return aggiungiRigaCommand;
    }

    /**
     * @return commend per rimuovere la riga selezionata
     */
    public ActionCommand getRimuoviRigheCommand() {
        if (rimuoviRigheCommand == null) {
            rimuoviRigheCommand = new RimuoviRigheCommand();
        }

        return rimuoviRigheCommand;
    }

    /**
     * @return the tableWidget
     */
    public JideTableWidget<T> getTableWidget() {
        return tableWidget;
    }

    @Override
    protected void readOnlyChanged() {
        if (formInsert != null) {
            formInsert.getFormModel().setReadOnly(isReadOnly());
            getAggiungiRigaCommand().setEnabled(!isReadOnly());
            getRimuoviRigheCommand().setEnabled(!isReadOnly());
        }
        if (tableWidget != null) {
            tableWidget.setEditable(!isReadOnly());
        }
    }

    /**
     *
     * @param formInsert
     *            form per l'inserimento di una riga
     */
    public void setFormInsert(AbstractFocussableForm formInsert) {
        this.formInsert = formInsert;
    }

    /**
     *
     * @param tableModel
     *            tableModel legato alla tabella
     */
    public void setTableModel(DefaultBeanTableModel<T> tableModel) {
        this.tableModel = tableModel;
    }

    /**
     *
     * @param tableWidget
     *            widget delal tabella creato precedentemente
     */
    public void setTableWidget(JideTableWidget<T> tableWidget) {
        this.tableWidget = tableWidget;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void valueModelChanged(Object obj) {
        if (formInsert != null) {
            formInsert.getNewFormObjectCommand().execute();
        }
        tableWidget.setRows((Collection<T>) obj);
    }
}
