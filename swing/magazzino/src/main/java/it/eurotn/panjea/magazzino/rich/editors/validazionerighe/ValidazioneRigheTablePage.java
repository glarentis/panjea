package it.eurotn.panjea.magazzino.rich.editors.validazionerighe;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideCellEditorAdapter;
import com.jidesoft.grid.JideTable;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.validazionerighe.commands.RulesValidationCommand;
import it.eurotn.panjea.magazzino.rulesvalidation.AbstractRigaArticoloRulesValidation;
import it.eurotn.panjea.magazzino.rulesvalidation.RigaMagazzinoPrezzoDeterminatoDiversoRulesValidation;
import it.eurotn.panjea.magazzino.rulesvalidation.RigaMagazzinoPrezzoZeroRulesValidation;
import it.eurotn.panjea.magazzino.rulesvalidation.RigaMagazzinoProvvisoriaRulesValidation;
import it.eurotn.panjea.magazzino.rulesvalidation.RigaMagazzinoQuantitaZeroRulesValidation;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRegoleValidazioneRighe;
import it.eurotn.rich.command.GlueActionCommand;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.command.SeparatorActionCommand;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class ValidazioneRigheTablePage extends AbstractTablePageEditor<RigaArticoloLite> {

    private class ConfermaImportiCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "confermaImportiCommand";

        /**
         * Costruttore.
         */
        public ConfermaImportiCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            if (getTable().getTable().getCellEditor() != null) {
                getTable().getTable().getCellEditor().stopCellEditing();
            }

            List<RigaArticoloLite> listRighe = new ArrayList<RigaArticoloLite>();

            Message message = new DefaultMessage("Validazione righe in corso...", Severity.INFO);
            final MessageAlert messageAlert = new MessageAlert(message, 0);
            try {
                messageAlert.showAlert();
                for (RigaArticoloLite rigaArticoloLite : getTable().getRows()) {
                    rigaArticoloLite.getPrezzoUnitario().calcolaImportoValutaAzienda(6);
                    if (rigaArticoloLite.getPrezzoUnitario().getImportoInValuta().compareTo(BigDecimal.ZERO) != 0) {
                        listRighe.add(rigaArticoloLite);
                    }
                }

                this.setEnabled(false);
                magazzinoDocumentoBD.modificaPrezziRigheNonValidePerFatturazione(listRighe);
            } finally {
                messageAlert.closeAlert();
                this.setEnabled(true);
            }
            getRefreshCommand().execute();
        }

    }

    public class RefreshCommand extends ApplicationWindowAwareCommand {

        private static final String REFRESH_COMMAND = "refreshCommand";

        /**
         * Costruttore.
         */
        public RefreshCommand() {
            super(REFRESH_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            refreshData();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + "." + REFRESH_COMMAND);
        }

    }

    public static final String PAGE_ID = "validazioneRigheTablePage";

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private ParametriRegoleValidazioneRighe parametriRegoleValidazioneRighe;

    private RulesValidationCommand prezzoRulesValidationCommand;
    private RulesValidationCommand quantitaRulesValidationCommand;
    private RulesValidationCommand prezzoDeterminatoCommand;
    private RulesValidationCommand documentoProvvisorioCommand;
    private RefreshCommand refreshCommand;
    private ConfermaImportiCommand confermaImportiCommand;

    /**
     * Costruttore.
     */
    protected ValidazioneRigheTablePage() {
        super(PAGE_ID, new ValidazioneRigheTableModel());

        ((JideTable) getTable().getTable()).addCellEditorListener(new JideCellEditorAdapter() {

            @Override
            public void editingStopped(ChangeEvent arg0) {
                super.editingStopped(arg0);
                // Se modifico l'importo e la colonna è aggregata devo rifare un aggregate
                // per vedere le modifiche.
                int[] colonneAggregate = ((AggregateTable) getTable().getTable()).getAggregateTableModel()
                        .getAggregatedColumns();
                for (int i = 0; i < colonneAggregate.length; i++) {
                    if (colonneAggregate[i] == ValidazioneRigheTableModel.COL_IMPORTO) {
                        ((AggregateTable) getTable().getTable()).getAggregateTableModel().aggregate();
                        break;
                    }
                }
            }
        });

        getTable().setPropertyCommandExecutor(new ActionCommandExecutor() {

            @Override
            public void execute() {
                // apro l'area solo della prima riga
                AreaMagazzino areaMagazzino = new AreaMagazzino();
                areaMagazzino.setId(getTable().getSelectedObject().getAreaMagazzino().getId());
                LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzino);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        });

    }

    /**
     * @return button bar con le regole di validazione.
     */
    private JComponent getButtonBar() {
        JComponent panel = getCommandGroup().createToolBar();
        panel.setBorder(GuiStandardUtils.createTopAndBottomBorder(3));
        return panel;
    }

    /**
     * @return toolbar con i command standard.
     */
    public AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getConfermaImportiCommand(),
                GlueActionCommand.getInstance(), getDocumentoProvvisorioCommand(), getPrezzoRulesValidationCommand(),
                getQuantitaRulesValidationCommand(), getPrezzoDeterminatoCommand(), getRefreshCommand() };

        return abstractCommands;
    }

    /**
     * @return toolbar.
     */
    private CommandGroup getCommandGroup() {
        JECCommandGroup commandGroup = new JECCommandGroup();
        if (getCommand() != null) {
            for (int i = 0; i < getCommand().length; i++) {
                if (getCommand()[i] instanceof GlueActionCommand) {
                    commandGroup.addGlue();
                } else if (getCommand()[i] instanceof SeparatorActionCommand) {
                    commandGroup.addSeparator();
                } else {
                    commandGroup.add(getCommand()[i]);
                }
            }
        } else {
            commandGroup = null;
        }
        return commandGroup;
    }

    /**
     * @return command per confermare gli importi
     */
    public ConfermaImportiCommand getConfermaImportiCommand() {
        if (confermaImportiCommand == null) {
            confermaImportiCommand = new ConfermaImportiCommand();
        }

        return confermaImportiCommand;
    }

    /**
     * @return command per selezionare la regola di documento provvisorio
     */
    public RulesValidationCommand getDocumentoProvvisorioCommand() {
        if (documentoProvvisorioCommand == null) {
            documentoProvvisorioCommand = new RulesValidationCommand(new RigaMagazzinoProvvisoriaRulesValidation());
            documentoProvvisorioCommand.addPropertyChangeListener("selected", this);
        }
        return documentoProvvisorioCommand;
    }

    @Override
    public JComponent getHeaderControl() {
        return getButtonBar();
    }

    /**
     * @return command per la regola del prezzo determinato
     */
    public RulesValidationCommand getPrezzoDeterminatoCommand() {
        if (prezzoDeterminatoCommand == null) {
            prezzoDeterminatoCommand = new RulesValidationCommand(
                    new RigaMagazzinoPrezzoDeterminatoDiversoRulesValidation());
            prezzoDeterminatoCommand.addPropertyChangeListener("selected", this);
        }

        return prezzoDeterminatoCommand;
    }

    /**
     * @return command per la regola del prezzo a zero
     */
    public RulesValidationCommand getPrezzoRulesValidationCommand() {
        if (prezzoRulesValidationCommand == null) {
            prezzoRulesValidationCommand = new RulesValidationCommand(new RigaMagazzinoPrezzoZeroRulesValidation());
            prezzoRulesValidationCommand.addPropertyChangeListener("selected", this);
        }
        return prezzoRulesValidationCommand;
    }

    /**
     * @return command per la regola della qta a zero
     */
    public RulesValidationCommand getQuantitaRulesValidationCommand() {
        if (quantitaRulesValidationCommand == null) {
            quantitaRulesValidationCommand = new RulesValidationCommand(new RigaMagazzinoQuantitaZeroRulesValidation());
            quantitaRulesValidationCommand.addPropertyChangeListener("selected", this);
        }

        return quantitaRulesValidationCommand;
    }

    @Override
    public RefreshCommand getRefreshCommand() {
        if (refreshCommand == null) {
            refreshCommand = new RefreshCommand();
        }

        return refreshCommand;
    }

    /**
     * Carica le righe non valide in base alle regole di validazione impostate.
     *
     * @return lista di righe articolo caricate
     */
    private List<RigaArticoloLite> getRigheNonValide() {
        List<RigaArticoloLite> listaRigheNonValide = new ArrayList<RigaArticoloLite>();

        if (this.parametriRegoleValidazioneRighe != null) {
            listaRigheNonValide = magazzinoDocumentoBD.applicaRegoleValidazione(this.parametriRegoleValidazioneRighe);
        }

        return listaRigheNonValide;
    }

    @Override
    public Collection<RigaArticoloLite> loadTableData() {
        return getRigheNonValide();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        if (parametriRegoleValidazioneRighe.getRegole().contains(getPrezzoRulesValidationCommand().getRule())) {
            getPrezzoRulesValidationCommand().setSelected(true);
        }

        if (parametriRegoleValidazioneRighe.getRegole().contains(getQuantitaRulesValidationCommand().getRule())) {
            getQuantitaRulesValidationCommand().setSelected(true);
        }

        if (parametriRegoleValidazioneRighe.getRegole().contains(getPrezzoDeterminatoCommand().getRule())) {
            getPrezzoDeterminatoCommand().setSelected(true);
        }

        if (parametriRegoleValidazioneRighe.getRegole().contains(getDocumentoProvvisorioCommand().getRule())) {
            getDocumentoProvvisorioCommand().setSelected(true);
        }

        getRefreshCommand().setEnabled(false);
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void processTableData(Collection<RigaArticoloLite> results) {
        super.processTableData(results);

        getRefreshCommand().setEnabled(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        getRefreshCommand().setEnabled(true);
    }

    @Override
    public Collection<RigaArticoloLite> refreshTableData() {
        this.parametriRegoleValidazioneRighe.setRegole(new ArrayList<AbstractRigaArticoloRulesValidation>());
        if (getPrezzoRulesValidationCommand().isSelected()) {
            this.parametriRegoleValidazioneRighe.addToRegole(getPrezzoRulesValidationCommand().getRule());
        }
        if (getQuantitaRulesValidationCommand().isSelected()) {
            this.parametriRegoleValidazioneRighe.addToRegole(getQuantitaRulesValidationCommand().getRule());
        }
        if (getPrezzoDeterminatoCommand().isSelected()) {
            this.parametriRegoleValidazioneRighe.addToRegole(getPrezzoDeterminatoCommand().getRule());
        }
        if (getDocumentoProvvisorioCommand().isSelected()) {
            this.parametriRegoleValidazioneRighe.addToRegole(getDocumentoProvvisorioCommand().getRule());
        }

        return loadTableData();
    }

    @Override
    public void restoreState(Settings arg0) {

    }

    @Override
    public void saveState(Settings arg0) {

    }

    @Override
    public void setFormObject(Object object) {
        this.parametriRegoleValidazioneRighe = (ParametriRegoleValidazioneRighe) object;
    }

    /**
     * @param magazzinoDocumentoBD
     *            the magazzinoDocumentoBD to set
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

}
