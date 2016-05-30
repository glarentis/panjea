package it.eurotn.rich.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.ListSearchable;
import com.jidesoft.swing.SearchableUtils;

import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.ParametriRicercaDTO;
import it.eurotn.panjea.rich.bd.IParametriRicercaBD;
import it.eurotn.panjea.rich.bd.ParametriRicercaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class ParametriRicercaCommand extends ActionCommand
        implements KeyListener, MouseListener, PropertyChangeListener {

    private class CancellaParametriCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public CancellaParametriCommand() {
            super("deleteCommand");
            RcpSupport.configure(this);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void doExecuteCommand() {
            IParametriRicercaBD parametriRicercaBD = RcpSupport.getBean(ParametriRicercaBD.BEAN_ID);
            ParametriRicercaDTO selectedElement = (ParametriRicercaDTO) list.getSelectedValue();
            if (selectedElement != null) {
                parametriRicercaBD.cancellaParametro(
                        (Class<? extends AbstractParametriRicerca>) formModel.getFormObject().getClass(),
                        selectedElement.getNome());
                int indexSelected = list.getSelectedIndex();
                ((DefaultListModel) list.getModel()).remove(indexSelected);
                if (list.getModel().getSize() > 0) {
                    list.getSelectionModel().setSelectionInterval(indexSelected, indexSelected);
                }
            }
        }

    }

    private class CellRender extends DefaultListCellRenderer {

        private static final long serialVersionUID = -4889445662612515438L;

        /*
         * (non-Javadoc)
         *
         * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object,
         * int, boolean, boolean)
         */
        @Override
        public Component getListCellRendererComponent(JList listControl, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(listControl, value, index, isSelected,
                    cellHasFocus);
            ParametriRicercaDTO parametro = (ParametriRicercaDTO) value;
            label.setText(parametro.getNome());
            setIcon(RcpSupport.getIcon((parametro.isGlobal()) ? "tableLayoutGlobal" : "tableLayoutUser"));
            return label;
        }

    }

    private class SalvaParametriCommand extends ActionCommand {
        /**
         * Costruttore.
         */
        public SalvaParametriCommand() {
            super("saveCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            new SaveDialog().showDialog();
        }
    }

    private class SaveDialog extends TitledApplicationDialog {

        public static final String SAVE_LAYOUT_DIALOG_TITLE = "saveTableLayout.save.title";
        public static final String SAVE_LAYOUT_DIALOG_MESSAGE = "saveTableLayout.save.message";

        private JTextField nomeTextField;
        private JCheckBox globalCheckBox;

        /**
         * Costruttore.
         */
        public SaveDialog() {
            super();
            setTitle(RcpSupport.getMessage(SAVE_LAYOUT_DIALOG_TITLE));
            setTitlePaneTitle(RcpSupport.getMessage(SAVE_LAYOUT_DIALOG_MESSAGE));
            setDescription("");
        }

        @Override
        protected JComponent createTitledDialogContentPane() {

            FormLayout layout = new FormLayout("left:pref, 5dlu, fill:pref:grow", "default,default,default");
            PanelBuilder builder = new PanelBuilder(layout);
            builder.setDefaultDialogBorder();
            CellConstraints cc = new CellConstraints();

            builder.addLabel("Nome", cc.xy(1, 1));
            nomeTextField = new JTextField();
            builder.add(nomeTextField, cc.xy(3, 1));
            builder.nextRow();

            globalCheckBox = new JCheckBox("ricerca condivisa");
            builder.add(globalCheckBox, cc.xy(3, 2));
            builder.nextRow();

            return builder.getPanel();
        }

        @Override
        protected boolean onFinish() {

            String ricercaName = nomeTextField.getText();
            if (ricercaName == null || ricercaName.length() == 0) {
                return false;
            }

            AbstractParametriRicerca parametro = (AbstractParametriRicerca) formModel.getFormObject();
            parametro.setNome(ricercaName);
            parametro.setGlobal(globalCheckBox.isSelected());

            // se ho una labella salvo anche il nome del layout
            if (risultatiPage != null) {
                parametro.setNomeLayoutTabella(
                        risultatiPage.getTable().getTableLayoutView().getCurrentLayout().getName());
            }

            IParametriRicercaBD parametriRicercaBD = RcpSupport.getBean(ParametriRicercaBD.BEAN_ID);
            parametriRicercaBD.salvaParametro(parametro);
            return true;
        }

    }

    private AbstractButton buttonCommand;

    private final FormModel formModel;

    private AbstractTablePageEditor<?> risultatiPage;

    private JList list;

    private JidePopup popup;

    private ListSearchable listSearchable;

    /**
     *
     * Costruttore.
     *
     * @param parametriPage
     *            pagina dei parametri
     * @param risultatiPage
     *            pagina dei risultati
     */
    public ParametriRicercaCommand(final FormBackedDialogPageEditor parametriPage,
            final AbstractTablePageEditor<?> risultatiPage) {
        super("parametriRicercaCommand");
        this.risultatiPage = risultatiPage;

        this.formModel = parametriPage.getForm().getFormModel();
        RcpSupport.configure(this);
    }

    /**
     * Costruttore.
     *
     * @param formModel
     *            formModel contenente un oggetto che deriva da {@link AbstractParametriRicerca}
     * @param risultatiPage
     *            pagina dei risultati
     */
    public ParametriRicercaCommand(final FormModel formModel, final AbstractTablePageEditor<?> risultatiPage) {
        super("parametriRicercaCommand");
        this.formModel = formModel;
        this.risultatiPage = risultatiPage;
        RcpSupport.configure(this);
    }

    @Override
    public void attach(AbstractButton button) {
        this.buttonCommand = button;
        super.attach(button);
    }

    @Override
    public void attach(AbstractButton button, CommandButtonConfigurer configurer) {
        this.buttonCommand = button;
        super.attach(button, configurer);
    }

    @Override
    public void attach(AbstractButton button, String faceDescriptorId, CommandButtonConfigurer configurer) {
        this.buttonCommand = button;
        super.attach(button, faceDescriptorId, configurer);
    }

    /**
     *
     * @return pannello centrale contenente la lista dei parametri da poter caricare.
     */
    private JList createCommandList() {
        list = getComponentFactory().createList();
        IParametriRicercaBD parametriBD = RcpSupport.getBean(ParametriRicercaBD.BEAN_ID);
        @SuppressWarnings("unchecked")
        List<ParametriRicercaDTO> parametri = parametriBD
                .caricaParametri((Class<? extends AbstractParametriRicerca>) formModel.getFormObject().getClass());
        DefaultListModel model = new DefaultListModel();
        for (ParametriRicercaDTO parametroRicerca : parametri) {
            model.addElement(parametroRicerca);
        }
        list.setModel(model);
        list.setBackground(UIManager.getColor("Panel.background"));
        list.setVisibleRowCount(10);
        list.addKeyListener(this);
        list.addMouseListener(this);
        list.setCellRenderer(new CellRender());
        listSearchable = new ListSearchable(list) {
            @Override
            protected String convertElementToString(Object obj) {
                return ((ParametriRicercaDTO) obj).getNome();
            }
        };
        return list;
    }

    @Override
    protected void doExecuteCommand() {
        popup = new JidePopup();
        if (formModel.isCommittable()) {
            formModel.commit();
        }
        popup.addPropertyChangeListener("visible", this);
        popup.setLayout(new BorderLayout());
        popup.getContentPane().add(new JScrollPane(createCommandList()), BorderLayout.CENTER);
        JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        commandPanel.add(new SalvaParametriCommand().createButton());
        commandPanel.add(new CancellaParametriCommand().createButton());
        popup.getContentPane().add(commandPanel, BorderLayout.SOUTH);
        popup.setResizable(true);
        popup.setMovable(false);
        popup.setOwner(buttonCommand);
        popup.setPopupBorder(BorderFactory.createLineBorder(new Color(100, 30, 106)));
        popup.setDefaultFocusComponent(list);
        popup.setReturnFocusToOwner(false);
        popup.getContentPane().setPreferredSize(new Dimension(300, 250));
        if (list.getModel().getSize() > 0) {
            list.setSelectedIndex(0);
        }
        popup.showPopup();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            loadSelectedElement();
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    /**
     * carica l'elemento selezionato.
     */
    private void loadSelectedElement() {
        ParametriRicercaDTO selectedElement = (ParametriRicercaDTO) list.getSelectedValue();
        if (selectedElement == null) {
            return;
        }
        IParametriRicercaBD parametriRicercaBD = RcpSupport.getBean(ParametriRicercaBD.BEAN_ID);
        @SuppressWarnings("unchecked")
        AbstractParametriRicerca parametro = parametriRicercaBD.caricaParametro(
                (Class<? extends AbstractParametriRicerca>) formModel.getFormObject().getClass(),
                selectedElement.getNome());
        parametro.setId(null);
        parametro.setVersion(null);
        formModel.setFormObject(parametro);

        // se ho una labella carico il layout associato ai parametri
        if (risultatiPage != null) {
            // applico il layout associato ai parametri, quello di default se non esiste
            TableLayout layoutToApply = risultatiPage.getTable().getTableLayoutView().getLayoutManager()
                    .getDefaultLayout();
            if (parametro.getNomeLayoutTabella() != null) {
                TableLayout layout = risultatiPage.getTable().getTableLayoutView().getLayoutManager()
                        .getLayout(parametro.getNomeLayoutTabella());
                layoutToApply = layout != null ? layout : layoutToApply;
            }
            risultatiPage.getTable().getTableLayoutView().getLayoutManager().applica(layoutToApply);
        }

        popup.hidePopup();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        loadSelectedElement();
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!((Boolean) evt.getNewValue())) {
            SearchableUtils.uninstallSearchable(listSearchable);
        }
    }

}
