/**
 *
 */
package it.eurotn.panjea.protocolli.rich.editor.tabelle;

import java.awt.Window;
import java.util.Calendar;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import it.eurotn.panjea.protocolli.domain.ProtocolloAnno;
import it.eurotn.panjea.protocolli.rich.bd.IProtocolliBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

/**
 * @author fattazzo
 *
 */
public class ProtocolliAnnoTablePage extends AbstractTablePageEditor<ProtocolloAnno> {

    private class NuovoAnnoPerProtocolliCommand extends ApplicationWindowAwareCommand {

        private static final String COMMAND_ID = "nuovoAnnoPerProtocolliCommand";

        /**
         * Costruttore.
         */
        public NuovoAnnoPerProtocolliCommand() {
            super(COMMAND_ID);
            setSecurityControllerId("protocolloAnnoPage.controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            int anno = Calendar.getInstance().get(Calendar.YEAR) + 1;
            SpinnerModel model = new SpinnerNumberModel(anno, anno - 5, anno + 5, 1);
            JSpinner annoSpinner = new JSpinner(model);

            InputApplicationDialog dialog = new InputApplicationDialog("Nuovo anno", ((Window) null));
            dialog.setInputField(annoSpinner);
            dialog.setInputLabelMessage("anno");
            dialog.setFinishAction(new Closure() {

                @Override
                public Object call(Object paramObject) {
                    Integer anno = (Integer) paramObject;
                    protocolliBD.creaProtocolliPerAnno(anno);
                    refreshData();
                    return null;
                }
            });
            dialog.showDialog();
        }
    }

    private class NuovoAnnoPerProtocolloCommand extends ApplicationWindowAwareCommand {

        private static final String COMMAND_ID = "nuovoAnnoPerProtocolloCommand";

        /**
         * Costruttore.
         */
        public NuovoAnnoPerProtocolloCommand() {
            super(COMMAND_ID);
            setSecurityControllerId("protocolloAnnoPage.controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ProtocolloAnno protocolloAnno = getTable().getSelectedObject();

            if (protocolloAnno != null) {
                ProtocolloAnno protocolloAnnoNew = (ProtocolloAnno) PanjeaSwingUtil.cloneObject(protocolloAnno);
                protocolloAnnoNew.setAnno(null);
                protocolloAnnoNew.setId(null);
                protocolloAnnoNew.setValore(new Integer(0));
                protocolloAnnoNew.setVersion(null);

                getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).preSetFormObject(protocolloAnnoNew);
                getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).setFormObject(protocolloAnnoNew);
                getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).postSetFormObject(protocolloAnnoNew);
                getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).grabFocus();
            }
        }
    }

    private static final String PAGE_ID = "protocolliAnnoTablePage";

    private IProtocolliBD protocolliBD = null;

    private CommandGroup nuovoAnnoCommandGroup;
    private NuovoAnnoPerProtocolloCommand nuovoAnnoPerProtocolloCommand;
    private NuovoAnnoPerProtocolliCommand nuovoAnnoPerProtocolliCommand;

    /**
     * Costruttore.
     */
    protected ProtocolliAnnoTablePage() {
        super(PAGE_ID, new String[] { "anno", "protocollo", "valore" }, ProtocolloAnno.class);
        getTable().setAggregatedColumns(new String[] { "anno" });
    }

    /**
     * Command group per creare su un nuovo anno un protocollo o tutti i protocolli.
     *
     * @return {@link CommandGroup}
     */
    private CommandGroup createNuovoAnnoCommandGroup() {
        nuovoAnnoCommandGroup = new CommandGroup("nuovoAnnoProtocolloCommandGroup");
        RcpSupport.configure(nuovoAnnoCommandGroup);

        nuovoAnnoCommandGroup.add(getNuovoAnnoPerProtocolloCommand());
        nuovoAnnoCommandGroup.add(getNuovoAnnoPerProtocolliCommand());

        return nuovoAnnoCommandGroup;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { createNuovoAnnoCommandGroup() };
    }

    @Override
    public JComponent getControl() {
        JComponent control = super.getControl();

        ((ActionCommand) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).getEditorSaveCommand())
                .addCommandInterceptor(new ActionCommandInterceptor() {

                    @Override
                    public void postExecution(ActionCommand arg0) {
                        ProtocolloAnno protocolloAnnoSalvato = (ProtocolloAnno) getEditPages()
                                .get(EditFrame.DEFAULT_OBJECT_CLASS_NAME).getPageObject();

                        // MAIL: eseguo la refresh e riseleziono il record per evitare la Concurrent
                        // presente nella
                        // versione 0.5.0
                        refreshData();

                        getTable().selectRowObject(protocolloAnnoSalvato, null);
                        getTable().scrollToSelectedRow();
                    }

                    @Override
                    public boolean preExecution(ActionCommand arg0) {
                        return true;
                    }
                });

        return control;
    }

    /**
     * @return gettter of nuovoAnnoPerProtocolliCommand
     */
    private NuovoAnnoPerProtocolliCommand getNuovoAnnoPerProtocolliCommand() {
        if (nuovoAnnoPerProtocolliCommand == null) {
            nuovoAnnoPerProtocolliCommand = new NuovoAnnoPerProtocolliCommand();
        }

        return nuovoAnnoPerProtocolliCommand;
    }

    /**
     * @return gettter of nuovoAnnoPerProtocolloCommand
     */
    private NuovoAnnoPerProtocolloCommand getNuovoAnnoPerProtocolloCommand() {
        if (nuovoAnnoPerProtocolloCommand == null) {
            nuovoAnnoPerProtocolloCommand = new NuovoAnnoPerProtocolloCommand();
        }

        return nuovoAnnoPerProtocolloCommand;
    }

    @Override
    public Collection<ProtocolloAnno> loadTableData() {
        return protocolliBD.caricaProtocolliAnno(null, null);
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public Collection<ProtocolloAnno> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        // Non utilizzato, carico tutti i protocolli anno
    }

    /**
     * @param protocolliBD
     *            the protocolliBD to set
     */
    public void setProtocolliBD(IProtocolliBD protocolliBD) {
        this.protocolliBD = protocolliBD;
    }

}
