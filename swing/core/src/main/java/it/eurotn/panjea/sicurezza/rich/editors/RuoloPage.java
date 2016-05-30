/**
 *
 */
package it.eurotn.panjea.sicurezza.rich.editors;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Permesso;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.rich.commands.DuplicaRuoloCommand;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author Leonardo
 * 
 */
public class RuoloPage extends FormBackedDialogPageEditor {

    private static Logger logger = Logger.getLogger(RuoloPage.class);
    private static final String ID_PAGE = "ruoloPage";

    private ISicurezzaBD sicurezzaBD = null;

    private PermessiCheckBoxTree permessiCheckBoxTree = null;
    private List<Permesso> listAvailablePermessi = null;

    private DuplicaRuoloCommand duplicaRuoloCommand = null;

    private ExpandCommand expandCommand;

    /**
     * Costruttore.
     * 
     * @param form
     *            form che la pagina gestisce
     */
    public RuoloPage(final PanjeaAbstractForm form) {
        super(ID_PAGE, form);
        form.getFormModel().addPropertyChangeListener("readOnly", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (permessiCheckBoxTree != null) {
                    permessiCheckBoxTree.setCheckBoxEnabled(!((Boolean) evt.getNewValue()).booleanValue());
                }
            }
        });
    }

    @Override
    public JComponent createControl() {
        loadAvailablePermessi();

        JComponent componentRuoloForm = super.createControl();
        // GuiStandardUtils.attachBorder( componentRuoloForm );
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        // aggiungo il form del ruolo
        panel.add(componentRuoloForm, BorderLayout.NORTH);
        // aggiungo la tree dei permessi
        panel.add(createPermessiControl(), BorderLayout.WEST);

        return panel;
    }

    /**
     * Crea i controlli per la gestione dei permessi associati al ruolo.
     * 
     * @return controlli creati
     */
    private JComponent createPermessiControl() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        permessiCheckBoxTree = new PermessiCheckBoxTree(new DefaultTreeModel(root));
        permessiCheckBoxTree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (!getForm().getFormModel().isReadOnly()) {
                    Set<Permesso> permessi = new TreeSet<Permesso>();
                    permessi.addAll(permessiCheckBoxTree.getPermessiSelezionati());
                    getForm().getValueModel("permessi").setValue(permessi);
                }
            }
        });

        JPanel buttonPanel = getComponentFactory().createPanel(new BorderLayout());
        buttonPanel.add(getExpandCommand().createButton(), BorderLayout.WEST);

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(RcpSupport.getMessage("permessi")));
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(getComponentFactory().createScrollPane(permessiCheckBoxTree), BorderLayout.CENTER);

        return panel;
    }

    @Override
    protected Object doSave() {
        logger.debug("---> doSave()");

        Ruolo ruoloDaSalvare = (Ruolo) getBackingFormPage().getFormModel().getFormObject();
        ruoloDaSalvare.getPermessi().clear();
        ruoloDaSalvare.getPermessi().addAll(permessiCheckBoxTree.getPermessiSelezionati());

        Ruolo ruoloSalvato = sicurezzaBD.salvaRuolo(ruoloDaSalvare);
        return ruoloSalvato;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                getDuplicaRuoloCommand(), toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand() };
        return abstractCommands;
    }

    /**
     * @return the duplicaRuoloCommand
     */
    private DuplicaRuoloCommand getDuplicaRuoloCommand() {
        if (duplicaRuoloCommand == null) {
            duplicaRuoloCommand = (DuplicaRuoloCommand) getActiveWindow().getCommandManager()
                    .getCommand("duplicaRuoloCommand");
            duplicaRuoloCommand.setRuoloPage(this);
        }
        return duplicaRuoloCommand;
    }

    /**
     * @return the expandCommand
     */
    public ExpandCommand getExpandCommand() {
        if (expandCommand == null) {
            expandCommand = new ExpandCommand(true, permessiCheckBoxTree);
        }

        return expandCommand;
    }

    /**
     * @return the sicurezzaBD
     */
    public ISicurezzaBD getSicurezzaBD() {
        return sicurezzaBD;
    }

    /**
     * Carica la lista dei permessi disponibili.
     */
    private void loadAvailablePermessi() {
        listAvailablePermessi = sicurezzaBD.caricaPermessi();
    }

    @Override
    public void loadData() {
        Ruolo ruolo = (Ruolo) getForm().getFormObject();
        ArrayList<Permesso> listRuoloPermessi = new ArrayList<Permesso>(ruolo.getPermessi());

        if (permessiCheckBoxTree != null) {
            permessiCheckBoxTree.createNodes(listAvailablePermessi, listRuoloPermessi);
        }
        getDuplicaRuoloCommand().setEnabled(!ruolo.isNew());
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public boolean onUndo() {
        boolean undo = super.onUndo();
        refreshData();
        return undo;
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void setFormObject(Object object) {
        super.setFormObject(object);

    }

    /**
     * @param sicurezzaBD
     *            the sicurezzaBD to set
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }
}
