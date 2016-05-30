package it.eurotn.panjea.anagrafica.mail;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang3.ObjectUtils;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.control.mail.FileListCellRenderer;
import it.eurotn.rich.dialog.AbstractFilterSelectionDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.report.editor.export.AbstractExportCommand;

public class ParametriMailForm extends PanjeaAbstractForm {

    private class AddAttachmentCommand extends ApplicationWindowAwareCommand {

        /**
         * Costruttore.
         */
        public AddAttachmentCommand() {
            super("addAttachmentCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(Application.instance().getActiveWindow().getControl());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                @SuppressWarnings("unchecked")
                Map<String, String> attachments = (Map<String, String>) getFormModel().getValueModel("attachments")
                        .getValue();
                attachments.put(file.getAbsolutePath(), file.getName());
                getFormModel().getValueModel("attachments").setValue(attachments);
                refreshAttachmentsListModel();
            }

        }
    }

    private class ChooseEmailDialogCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public ChooseEmailDialogCommand() {
            super("chooseEmailDialogCommand");
            RcpSupport.configure(this);
        }

        /**
         * Crea un destinatario mail per la riga di rubrica indicata.
         *
         * @param rubricaDTO
         *            riga della rubrica
         * @return destinatario creato
         */
        private Destinatario creaDestinatario(RubricaDTO rubricaDTO) {
            final Destinatario destinatario = new Destinatario();
            destinatario.setEntita(rubricaDTO.getEntita());

            // se non sono su una riga di entità ( sede o contatto ) oltre all'entità vado a salvare anche
            // il codice e denominazione nel destinatario
            if (!Entita.class.isAssignableFrom(rubricaDTO.getRowClass())) {
                destinatario.setNome(rubricaDTO.getCodice() + " - " + rubricaDTO.getDenominazione());
            }

            destinatario.setEmail(panjeaMailClient.getEMailValida(rubricaDTO.getEmail(), rubricaDTO.getIndirizzoPEC()));

            return destinatario;
        }

        @Override
        protected void doExecuteCommand() {
            AbstractFilterSelectionDialog chooseEmailDialog = RcpSupport.getBean("chooseEmailDialog");
            chooseEmailDialog.setFilter(((ParametriMail) getFormObject()).getFiltroRubrica());
            chooseEmailDialog.setOnSelectAction(new Closure() {

                @SuppressWarnings("unchecked")
                @Override
                public Object call(Object obj) {
                    RubricaDTO rubricaDTO = (RubricaDTO) obj;
                    if (!rubricaDTO.getEmail().isEmpty() || !rubricaDTO.getIndirizzoPEC().isEmpty()) {

                        Set<Destinatario> email = (Set<Destinatario>) getFormModel().getValueModel("destinatari")
                                .getValue();
                        email.add(creaDestinatario(rubricaDTO));
                        getFormModel().getValueModel("destinatari").setValue(email);

                        updateEmailListComponent();
                    }
                    return null;
                }
            });
            chooseEmailDialog.showDialog();
        }
    }

    private class DatiMailListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            DatiMail datiMail = (DatiMail) evt.getNewValue();
            String testo = (String) getValueModel("testo").getValue();

            int idxFirma = testo.indexOf(DatiMail.ID_DIV_FIRMA);
            if (idxFirma != -1) {
                // tolgo la firma precedente
                testo = testo.substring(0, idxFirma);
            } else {
                testo = testo.concat("<br/><br/><br/>");
            }
            testo = testo.concat(DatiMail.ID_DIV_FIRMA.concat(datiMail.getFirma()).concat("</div>"));

            getValueModel("testo").setValue(new String(testo));
            getValueModel("notificaLettura").setValue(datiMail.isNotificaLettura());
        }

    }

    private class ExportToggleCommand extends JideToggleCommand {

        private AbstractExportCommand exportCommand;

        /**
         * Costruttore.
         *
         * @param command
         *            export command
         */
        public ExportToggleCommand(final AbstractExportCommand command) {
            super(command.getId());
            RcpSupport.configure(this);
            this.exportCommand = command;
        }

        @Override
        public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                CommandButtonConfigurer configurer) {
            AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, configurer);
            button.setText(button.getToolTipText());

            return button;
        }

        /**
         * @return Returns the exportCommand.
         */
        public AbstractExportCommand getExportCommand() {
            return exportCommand;
        }
    }

    private class RemoveAttachmentCommand extends ApplicationWindowAwareCommand {

        /**
         * Costruttore.
         */
        public RemoveAttachmentCommand() {
            super("removeAttachmentCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            @SuppressWarnings("unchecked")
            List<String> attachments = (List<String>) getFormModel().getValueModel("attachments").getValue();
            attachments.removeAll(attachmentsList.getSelectedValuesList());
            getFormModel().getValueModel("attachments").setValue(attachments);
            refreshAttachmentsListModel();

        }
    }

    private AbstractExportCommand[] exportCommands;

    private List<ExportToggleCommand> exportToggleCommands;

    private ListaDestinatariEditableTableModel listaDestinatariEditableTableModel = null;

    private DefaultListModel<String> attachmentsListModel;
    private JList<String> attachmentsList;

    private PanjeaMailClient panjeaMailClient;

    private Utente utente;

    private DatiMailListener datiMailListener;

    /**
     * Costruttore.
     *
     * @param parametriMail
     *            parametri predefiniti della mail da settare nel form
     * @param exportCommands
     *            comandi per la scelta degli allegati
     * @param utente
     *            utente di riferimento dal quale selezionare gli account di spedizione
     */
    public ParametriMailForm(final ParametriMail parametriMail, final AbstractExportCommand[] exportCommands,
            final Utente utente) {
        super(PanjeaFormModelHelper.createFormModel(parametriMail, false, "parametriMailForm"));
        this.utente = utente;
        this.datiMailListener = new DatiMailListener();

        if (parametriMail.getDestinatari() == null) {
            parametriMail.setDestinatari(new TreeSet<Destinatario>());
        }
        this.exportCommands = ObjectUtils.defaultIfNull(exportCommands, new AbstractExportCommand[] {});
        this.exportToggleCommands = new ArrayList<ParametriMailForm.ExportToggleCommand>();

        this.panjeaMailClient = RcpSupport.getBean(PanjeaMailClient.BEAN_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:pref,  4dlu, left:pref,  fill:pref:grow,  left:pref,left:pref",
                "2dlu,default,12dlu,70dlu,default,2dlu,default,2dlu,default,2dlu,40dlu,2dlu,default,2dlu,default,2dlu,fill:pref:grow,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());

        builder.setLabelAttributes("r,t");

        builder.nextRow();
        builder.setRow(2);
        builder.addLabel("account", 1);
        builder.addBinding(bf.createBoundComboBox("datiMail", utente.getDatiMail(), "nomeAccount"), 3, 2, 2, 1);
        builder.nextRow();

        builder.addLabel("destinatari", 1, 4);
        builder.setComponentAttributes("f,f");
        listaDestinatariEditableTableModel = new ListaDestinatariEditableTableModel();
        TableEditableBinding<Destinatario> bindingDestinatari = new TableEditableBinding<Destinatario>(getFormModel(),
                "destinatari", Set.class, listaDestinatariEditableTableModel);

        JPanel panelDestinatari = (JPanel) bindingDestinatari.getControl();
        panelDestinatari.setPreferredSize(new Dimension(500, 100));
        builder.addBinding(bindingDestinatari, 3, 3, 2, 2);

        ChooseEmailDialogCommand chooseEmailDialogCommand = new ChooseEmailDialogCommand();
        builder.addComponent(chooseEmailDialogCommand.createButton(), 5, 4);

        builder.setLabelAttributes("r, c");
        builder.addPropertyAndLabel("oggetto", 1, 7, 2, 1);

        if (exportCommands != null && exportCommands.length > 0) {
            builder.addLabel("allegato", 1, 9);

            JPanel tipiAllegatiPanel = getComponentFactory().createPanel(new HorizontalLayout(5));
            for (int i = 0; i < exportCommands.length; i++) {
                AbstractExportCommand command = exportCommands[i];
                ExportToggleCommand exportToggleCommand = new ExportToggleCommand(command);
                exportToggleCommands.add(exportToggleCommand);
                tipiAllegatiPanel.add(exportToggleCommand.createButton());
            }
            builder.addComponent(tipiAllegatiPanel, 3, 9);
            builder.addProperty("nomeAllegato", 4, 9);
        }

        builder.setLabelAttributes("r,t");
        builder.addLabel("attachments", 1, 11);

        // non bindo la lista perchè altrimenti solo gli elementi selezionati poi li ritrovo nel value model mentre in
        // questo caso tutti gli allegati che vedo devono finire nella mail. Inoltre vedrei tutti i file
        // selezionati, cosa brutta da vedersi
        attachmentsListModel = new DefaultListModel<String>();
        attachmentsList = new JList<String>(attachmentsListModel);
        attachmentsList.setVisibleRowCount(-1);
        attachmentsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        attachmentsList.setCellRenderer(new FileListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(attachmentsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        builder.addComponent(scrollPane, 3, 11, 2, 4);
        refreshAttachmentsListModel();

        JPanel attachmentsPanel = getComponentFactory().createPanel(new VerticalLayout(2));
        attachmentsPanel.add(new AddAttachmentCommand().createButton());
        attachmentsPanel.add(new RemoveAttachmentCommand().createButton());
        builder.addComponent(attachmentsPanel, 5, 11);

        builder.setComponentAttributes("f,f");
        builder.addLabel("testo", 1, 17);

        Binding noteBinding = bf.createBoundHTMLEditor("testo");
        JScrollPane jScrollPane = new JScrollPane(noteBinding.getControl());
        noteBinding.getControl().setPreferredSize(new Dimension(400, 300));

        builder.addComponent(jScrollPane, 3, 17, 2, 1);

        builder.addPropertyAndLabel("notificaLettura", 1, 19);

        getValueModel("datiMail").addValueChangeListener(datiMailListener);

        JPanel panel = builder.getPanel();
        return panel;
    }

    @Override
    public void dispose() {
        getValueModel("datiMail").removeValueChangeListener(datiMailListener);
        datiMailListener = null;

        super.dispose();
    }

    /**
     * Restituisce i command da utilizzare per l'esportazione degli allegati.
     *
     * @return command
     */
    public List<AbstractExportCommand> getSelectedExportCommands() {
        List<AbstractExportCommand> commands = new ArrayList<AbstractExportCommand>();

        for (ExportToggleCommand toggleCommand : exportToggleCommands) {
            if (toggleCommand.isSelected()) {
                commands.add(toggleCommand.getExportCommand());
            }
        }

        return commands;
    }

    /**
     * Sincronizza il modello della lista degli attachments con quelli dell'oggetto del form.
     */
    private void refreshAttachmentsListModel() {
        attachmentsListModel.clear();

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) getFormModel().getValueModel("attachments").getValue();
        for (String attach : map.values()) {
            attachmentsListModel.addElement(attach);
        }
    }

    /**
     * Aggiorna la lista delle email in base al valore dei parametri mail del form.
     */
    @SuppressWarnings({ "unchecked" })
    private void updateEmailListComponent() {

        Set<Destinatario> email = ((Set<Destinatario>) getFormModel().getValueModel("destinatari").getValue());

        listaDestinatariEditableTableModel.setRows(email);
    }
}
