package it.eurotn.rich.binding.list;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author leonardo
 */
public class ListInsertableBinding<T> extends TableEditableBinding<T> {

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
            AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
            button.setName(formModel.getId() + "." + COMMAND_ID);
            return button;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void doExecuteCommand() {

            if (insertableObjectTableForm.getFormModel().isCommittable()) {
                T riga = ((InsertableElementWrapper<T>) insertableObjectTableForm.getFormModel().getFormObject())
                        .getElement();
                ConversionExecutor converter = getConversionService().getConversionExecutor(riga.getClass(),
                        elementClass);
                riga = (T) converter.execute(riga);

                // controllo se posso inserire la riga
                boolean canInsert = riga != null && riga instanceof EntityBase && ((EntityBase) riga).getId() != null;

                if (canInsert) {
                    Collection<Object> righe = (Collection<Object>) getValueModel().getValue();

                    righe.add(riga);
                    getValueModel().setValue(PanjeaSwingUtil.cloneObject(righe));
                    insertableObjectTableForm.getNewFormObjectCommand().execute();
                    insertableObjectTableForm.grabFocus();
                }
            }
        }
    }

    private class DefaultElementWrapper implements InsertableElementWrapper<T> {

        private T element;

        /**
         * Costruttore.
         */
        public DefaultElementWrapper() {
            super();
        }

        /**
         * @return the element
         */
        @Override
        public T getElement() {
            if (element == null) {
                element = createElementNewInstance();
            }
            return element;
        }

        /**
         * @param element
         *            the element to set
         */
        @Override
        public void setElement(T element) {
            this.element = element;
        }

    }

    private class InsertableObjectTableForm extends PanjeaAbstractForm implements Focussable {

        private SearchTextField searchTextField = null;
        private SearchPanel searchPanel = null;

        /**
         * Costruttore.
         */
        public InsertableObjectTableForm() {
            super(PanjeaFormModelHelper.createBeanFormModel(new DefaultElementWrapper(), false,
                    "insertableObjectTableFormModel"), "insertableObjectTableForm");
        }

        @Override
        protected JComponent createFormControl() {
            final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
            FormLayout layout = new FormLayout("10dlu,left:default, 5dlu, 15dlu", "4dlu,default,2dlu");
            FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered()
            builder.setLabelAttributes("r, c");
            builder.setComponentAttributes("f, f");

            builder.setRow(2);
            Binding searchTextBinding = bf.createBoundSearchText("element", renderedProperties, elementClass);
            searchPanel = ((SearchPanel) searchTextBinding.getControl());
            searchTextField = searchPanel.getTextFields().get(renderedProperties[0]);
            searchTextField.setColumns(30);
            if (renderedProperties.length > 1) {
                SearchTextField searchTextField2 = searchPanel.getTextFields().get(renderedProperties[1]);
                searchTextField2.setColumns(14);
            }
            builder.addBinding(searchTextBinding, 2, 2);
            builder.addComponent(getAggiungiRigaCommand().createButton(), 4, 2);
            new PanjeaFormGuard(getFormModel(), getAggiungiRigaCommand());
            return builder.getPanel();
        }

        @Override
        protected Object createNewObject() {
            return new DefaultElementWrapper();
        }

        @Override
        public void grabFocus() {
            if (searchTextField != null) {
                searchTextField.getTextField().requestFocusInWindow();
            }
        }

    }

    private InsertableObjectTableForm insertableObjectTableForm = null;
    private ActionCommand aggiungiRigaCommand = null;
    private Class<T> elementClass = null;
    private String[] renderedProperties = null;

    /**
     * Costruttore.
     *
     * @param formModelBase
     *            formModel
     * @param collectionFormPropertyPath
     *            formPropertyPath
     * @param collectionClass
     *            requiredSourceClass
     * @param tableModel
     *            tableModel
     * @param renderedProperties
     *            renderedProperties
     */
    public ListInsertableBinding(final FormModel formModelBase, final String collectionFormPropertyPath,
            final Class<?> collectionClass, final DefaultBeanTableModel<T> tableModel,
            final String[] renderedProperties) {
        super(formModelBase, collectionFormPropertyPath, collectionClass, tableModel);
        this.elementClass = tableModel.getClassObj();
        this.renderedProperties = renderedProperties;
    }

    /**
     * @return una nuova istanza di element
     */
    private T createElementNewInstance() {
        try {
            return elementClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected JComponent doBindControl() {
        JPanel panel = new JPanel(new BorderLayout());
        insertableObjectTableForm = new InsertableObjectTableForm();
        panel.add(insertableObjectTableForm.getControl(), BorderLayout.NORTH);
        panel.add(super.doBindControl(), BorderLayout.CENTER);
        return panel;
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

    @Override
    protected void readOnlyChanged() {
        if (insertableObjectTableForm != null) {
            insertableObjectTableForm.getFormModel().setReadOnly(isReadOnly());
            getAggiungiRigaCommand().setEnabled(!isReadOnly());
        }
        super.readOnlyChanged();
    }
}
