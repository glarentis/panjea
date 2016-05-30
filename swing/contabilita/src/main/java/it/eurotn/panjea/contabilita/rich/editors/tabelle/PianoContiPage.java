/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.PopupMenuMouseListener;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.report.StampaCommand;

/**
 * Pagina che gestisce la visualizzazione e gestione di mastri, conti e sottoconti.
 *
 * @author fattazzo,Leonardo
 * @version 1.0, 13/apr/07
 */
public class PianoContiPage extends AbstractTreeTableDialogPageEditor implements IPageEditor {

    private class CancellaContoCommand extends ActionCommand {

        DefaultMutableTreeTableNode node;

        public CancellaContoCommand() {
            super(PAGE_ID + CANCELLA_CONTO_COMMAND_ID);
            setVisible(false);
        }

        @Override
        public void attach(AbstractButton button) {
            super.attach(button);
            button.setName("cancellaContoCommand");
        }

        @Override
        protected void doExecuteCommand() {
            if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
                return;
            } else {
                node = getSelectedNode();
                // controllo per bug selezione su treeTable
                if (!(node.getUserObject() instanceof Conto)) {
                    return;
                }
                node = getSelectedNode();
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                        getMessage("abstractObjectTable.delete.confirm.title"),
                        getMessage("abstractObjectTable.delete.confirm.message")) {

                    @Override
                    protected void onConfirm() {
                        contabilitaAnagraficaBD.cancellaConto((Conto) CancellaContoCommand.this.node.getUserObject());

                        DefaultTreeTableModel myModel = (DefaultTreeTableModel) getTreeTable().getTreeTableModel();
                        DefaultMutableTreeTableNode toBeDeletedNode = CancellaContoCommand.this.node;

                        // find out which node to select next
                        DefaultMutableTreeTableNode parentNode = (DefaultMutableTreeTableNode) toBeDeletedNode
                                .getParent();

                        int currIdx = myModel.getIndexOfChild(parentNode, toBeDeletedNode);
                        TreeTableNode nextToBeSelected = null;
                        if (parentNode.getChildCount() > currIdx) {
                            nextToBeSelected = parentNode.getChildAt(currIdx);
                        }
                        if (nextToBeSelected == null && currIdx > 0) {
                            nextToBeSelected = parentNode.getChildAt(currIdx + 1);
                        } else {
                            nextToBeSelected = toBeDeletedNode.getParent();
                        }
                        myModel.removeNodeFromParent(toBeDeletedNode);
                        getTreeTable().getTreeSelectionModel()
                                .setSelectionPath(new TreePath(myModel.getPathToRoot(nextToBeSelected)));

                    }
                };
                confirmationDialog.setPreferredSize(new Dimension(250, 50));
                confirmationDialog.setResizable(false);
                confirmationDialog.showDialog();
            }
        }
    }

    private class CancellaMastroCommand extends ActionCommand {

        DefaultMutableTreeTableNode node;

        public CancellaMastroCommand() {
            super(PAGE_ID + CANCELLA_MASTRO_COMMAND_ID);
            setVisible(false);
        }

        @Override
        protected void doExecuteCommand() {
            if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
                return;
            } else {
                node = getSelectedNode();
                // controllo per bug selezione su treeTable
                if (!(node.getUserObject() instanceof Mastro)) {
                    return;
                }
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                        getMessage("abstractObjectTable.delete.confirm.title"),
                        getMessage("abstractObjectTable.delete.confirm.message")) {

                    @Override
                    protected void onConfirm() {
                        contabilitaAnagraficaBD
                                .cancellaMastro((Mastro) CancellaMastroCommand.this.node.getUserObject());

                        DefaultTreeTableModel myModel = (DefaultTreeTableModel) getTreeTable().getTreeTableModel();
                        DefaultMutableTreeTableNode toBeDeletedNode = CancellaMastroCommand.this.node;// (
                        // DefaultMutableTreeTableNode
                        // ) selp.
                        // getLastPathComponent
                        // ();

                        // find out which node to select next
                        DefaultMutableTreeTableNode parentNode = (DefaultMutableTreeTableNode) toBeDeletedNode
                                .getParent();

                        int currIdx = myModel.getIndexOfChild(parentNode, toBeDeletedNode);
                        TreeTableNode nextToBeSelected = null;
                        if (parentNode.getChildCount() > currIdx) {
                            nextToBeSelected = parentNode.getChildAt(currIdx);
                        }
                        if (nextToBeSelected == null && currIdx > 0) {
                            nextToBeSelected = parentNode.getChildAt(currIdx + 1);
                        } else {
                            nextToBeSelected = toBeDeletedNode.getParent();
                        }
                        myModel.removeNodeFromParent(toBeDeletedNode);
                        getTreeTable().getTreeSelectionModel()
                                .setSelectionPath(new TreePath(myModel.getPathToRoot(nextToBeSelected)));

                    }
                };
                confirmationDialog.setPreferredSize(new Dimension(250, 50));
                confirmationDialog.setResizable(false);
                confirmationDialog.showDialog();
            }
        }
    }

    private class CancellaSottoContoCommand extends ActionCommand {

        private DefaultMutableTreeTableNode node;

        public CancellaSottoContoCommand() {
            super(PAGE_ID + CANCELLA_SOTTOCONTO_COMMAND_ID);
            setVisible(false);
        }

        @Override
        protected void doExecuteCommand() {
            if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
                return;
            } else {
                node = getSelectedNode();
                // controllo per bug selezione su treeTable
                if (!(node.getUserObject() instanceof SottoConto)) {
                    return;
                }
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                        getMessage("abstractObjectTable.delete.confirm.title"),
                        getMessage("abstractObjectTable.delete.confirm.message")) {

                    @Override
                    protected void onConfirm() {
                        contabilitaAnagraficaBD
                                .cancellaSottoConto((SottoConto) CancellaSottoContoCommand.this.node.getUserObject());

                        DefaultTreeTableModel myModel = (DefaultTreeTableModel) getTreeTable().getTreeTableModel();
                        DefaultMutableTreeTableNode toBeDeletedNode = CancellaSottoContoCommand.this.node;// (
                        // DefaultMutableTreeTableNode
                        // ) selp.
                        // getLastPathComponent
                        // ();

                        // find out which node to select next
                        DefaultMutableTreeTableNode parentNode = (DefaultMutableTreeTableNode) toBeDeletedNode
                                .getParent();

                        int currIdx = myModel.getIndexOfChild(parentNode, toBeDeletedNode);
                        TreeTableNode nextToBeSelected = null;
                        if (parentNode.getChildCount() > currIdx) {
                            nextToBeSelected = parentNode.getChildAt(currIdx);
                        }
                        if (nextToBeSelected == null && currIdx > 0) {
                            nextToBeSelected = parentNode.getChildAt(currIdx + 1);
                        } else {
                            nextToBeSelected = toBeDeletedNode.getParent();
                        }
                        myModel.removeNodeFromParent(toBeDeletedNode);
                        getTreeTable().getTreeSelectionModel()
                                .setSelectionPath(new TreePath(myModel.getPathToRoot(nextToBeSelected)));
                    }
                };
                confirmationDialog.setPreferredSize(new Dimension(250, 50));
                confirmationDialog.setResizable(false);
                confirmationDialog.showDialog();
            }
        }
    }

    private class NuovoContoCommand extends ActionCommand {

        public NuovoContoCommand() {
            super(PAGE_ID + NUOVO_CONTO_COMMAND_ID);
            setVisible(false);
        }

        @Override
        protected void doExecuteCommand() {
            if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
                return;
            } else {
                DefaultMutableTreeTableNode node = getSelectedNode();

                Conto conto = new Conto();
                conto.setMastro((Mastro) node.getUserObject());

                ContoTitledPageApplicationDialog dialog = new ContoTitledPageApplicationDialog(conto,
                        contabilitaAnagraficaBD);
                dialog.setCloseAction(CloseAction.HIDE);
                dialog.setModal(true);
                dialog.showDialog();

                if (dialog.getContoSalvato().getId() != null) {
                    DefaultMutableTreeTableNode nuovoNode = new DefaultMutableTreeTableNode(dialog.getContoSalvato());
                    DefaultTreeTableModel model = ((DefaultTreeTableModel) getTreeTable().getTreeTableModel());
                    model.insertNodeInto(nuovoNode, node, node.getChildCount());
                }
            }

        }
    }

    private class NuovoMastroCommand extends ActionCommand {

        public NuovoMastroCommand() {
            super(PAGE_ID + NUOVO_MASTRO_COMMAND_ID);
        }

        @Override
        protected void doExecuteCommand() {
            Mastro mastro = new Mastro();

            MastroTitledPageApplicationDialog dialog = new MastroTitledPageApplicationDialog(mastro,
                    contabilitaAnagraficaBD);
            dialog.setCloseAction(CloseAction.HIDE);
            dialog.setModal(true);
            dialog.showDialog();

            if (dialog.getMastroSalvato().getId() != null) {
                DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) getTreeTable().getTreeTableModel()
                        .getRoot();
                int initialChildCount = node.getChildCount();
                DefaultTreeTableModel model = ((DefaultTreeTableModel) getTreeTable().getTreeTableModel());
                DefaultMutableTreeTableNode nuovoNode = new DefaultMutableTreeTableNode(dialog.getMastroSalvato());
                model.insertNodeInto(nuovoNode, node, node.getChildCount());
                // risetto il rootnode aggiornato solo nel caso in cui il
                // rootnode non ha figli
                // ci deve essere un bug che non mi visualizza il primo elemento
                // inserito (vedi bug 406)
                if (initialChildCount == 0) {
                    model.setRoot(node);
                }
            }

        }
    }

    private class NuovoSottoContoCommand extends ActionCommand {

        public NuovoSottoContoCommand() {
            super(PAGE_ID + NUOVO_SOTTOCONTO_COMMAND_ID);
            setVisible(false);
        }

        @Override
        public void attach(AbstractButton button) {
            super.attach(button);
            button.setName("nuovoSottoContoCommand");
        }

        @Override
        protected void doExecuteCommand() {
            if (getTreeTable().getTreeSelectionModel().getSelectionPath() == null) {
                return;
            } else {
                DefaultMutableTreeTableNode node = getSelectedNode();
                if (node.getUserObject() instanceof Conto) {
                    SottoConto sottoConto = new SottoConto();
                    sottoConto.setConto((Conto) node.getUserObject());

                    SottoContoTitledPageApplicationDialog dialog = new SottoContoTitledPageApplicationDialog(sottoConto,
                            contabilitaAnagraficaBD);
                    dialog.setCloseAction(CloseAction.HIDE);
                    dialog.setModal(true);
                    dialog.showDialog();

                    if (dialog.getSottoContoSalvato().getId() != null) {
                        DefaultMutableTreeTableNode nuovoNode = new DefaultMutableTreeTableNode(
                                dialog.getSottoContoSalvato());
                        DefaultTreeTableModel model = ((DefaultTreeTableModel) getTreeTable().getTreeTableModel());
                        model.insertNodeInto(nuovoNode, node, node.getChildCount());
                    }
                }
            }

        }
    }

    private class PianoContiTreeTablePopupMenuMouseListener extends PopupMenuMouseListener {

        @Override
        protected JPopupMenu getPopupMenu() {
            return createPopupMenu();
        }
    }

    private class PianoDeiContiTreeTableModel extends DefaultTreeTableModel {

        public PianoDeiContiTreeTableModel(final TreeTableNode node) {
            super(node);
        }

        @Override
        public Class<?> getColumnClass(int column) {
            switch (column) {
            case 0:
                return TreeTableModel.class;
            case 1:
                return String.class;
            case 2:
                return Enum.class;
            case 3:
                return Enum.class;
            case 4:
                return Boolean.class;
            case 5:
                return Enum.class;
            default:
                return super.getColumnClass(column);
            }
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public String getColumnName(final int column) {
            return getMessage("PianoContiTableColonna" + column);
        }

        @Override
        public Object getValueAt(Object node, int column) {
            Object object = ((DefaultMutableTreeTableNode) node).getUserObject();

            if (column == 0) {
                return super.getValueAt(node, column);
            } else {
                if (object instanceof Mastro) {
                    Mastro mastro = (Mastro) object;

                    switch (column) {
                    case 1:
                        return mastro.getDescrizione();
                    default:
                        return "";
                    }
                } else {
                    if (object instanceof Conto) {
                        Conto conto = (Conto) object;

                        switch (column) {
                        case 1:
                            return conto.getDescrizione();
                        case 2:
                            return conto.getTipoConto();
                        case 3:
                            return conto.getSottotipoConto();
                        default:
                            return "";
                        }
                    } else {
                        if (object instanceof SottoConto) {
                            SottoConto sottoConto = (SottoConto) object;

                            switch (column) {
                            case 1:
                                return sottoConto.getDescrizione();
                            case 4:
                                return sottoConto.getFlagIRAP();
                            case 5:
                                return sottoConto.getClassificazioneConto();
                            default:
                                return "";
                            }
                        }
                    }
                }

            }

            return "";
        }

        @Override
        public boolean isCellEditable(Object arg0, int arg1) {
            return false;
        }
    }

    private class ProprietaContoCommand extends ActionCommand {

        private DefaultMutableTreeTableNode node = null;

        public ProprietaContoCommand() {
            super(PAGE_ID + PROPRIETA_CONTO_COMMAND_ID);
            setVisible(false);
        }

        @Override
        public void attach(AbstractButton button) {
            super.attach(button);
            button.setName("proprietaContoCommand");
        }

        @Override
        protected void doExecuteCommand() {
            // controllo per bug selezione su treeTable
            if (node == null || (node != null && !(node.getUserObject() instanceof Conto))) {
                return;
            }

            ContoTitledPageApplicationDialog dialog = new ContoTitledPageApplicationDialog((Conto) node.getUserObject(),
                    contabilitaAnagraficaBD);
            dialog.setCloseAction(CloseAction.HIDE);
            dialog.showDialog();

            Conto contoSalvato = dialog.getContoSalvato();
            node.setUserObject(contoSalvato);

            @SuppressWarnings("unchecked")
            Enumeration<DefaultMutableTreeTableNode> children = (Enumeration<DefaultMutableTreeTableNode>) node
                    .children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeTableNode nodeChild = children.nextElement();
                ((SottoConto) nodeChild.getUserObject()).setSoggettoCentroCosto(contoSalvato.isSoggettoCentroCosto());
                ((SottoConto) nodeChild.getUserObject()).setCentroCosto(contoSalvato.getCentroCosto());
            }

            dialog = null;
        }

        public void setNode(DefaultMutableTreeTableNode node) {
            this.node = node;
        }
    }

    private class ProprietaMastroCommand extends ActionCommand {

        private DefaultMutableTreeTableNode node = null;

        public ProprietaMastroCommand() {
            super(PAGE_ID + PROPRIETA_MASTRO_COMMAND_ID);
            setVisible(false);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void doExecuteCommand() {
            // controllo per bug selezione su treeTable
            if (node == null || (node != null && !(node.getUserObject() instanceof Mastro))) {
                return;
            }

            MastroTitledPageApplicationDialog dialog = new MastroTitledPageApplicationDialog(
                    (Mastro) node.getUserObject(), contabilitaAnagraficaBD);
            dialog.setCloseAction(CloseAction.HIDE);
            dialog.showDialog();

            Mastro mastroSalvato = dialog.getMastroSalvato();
            node.setUserObject(mastroSalvato);

            Enumeration<DefaultMutableTreeTableNode> children = (Enumeration<DefaultMutableTreeTableNode>) node
                    .children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeTableNode nodeChild = children.nextElement();
                ((Conto) nodeChild.getUserObject()).setSoggettoCentroCosto(mastroSalvato.isSoggettoCentroCosto());
                ((Conto) nodeChild.getUserObject()).setCentroCosto(mastroSalvato.getCentroCosto());
                Enumeration<DefaultMutableTreeTableNode> childrenConto = (Enumeration<DefaultMutableTreeTableNode>) nodeChild
                        .children();
                while (childrenConto.hasMoreElements()) {
                    DefaultMutableTreeTableNode nodeSottocontoChild = childrenConto.nextElement();
                    ((SottoConto) nodeSottocontoChild.getUserObject())
                            .setSoggettoCentroCosto(mastroSalvato.isSoggettoCentroCosto());
                    ((SottoConto) nodeSottocontoChild.getUserObject()).setCentroCosto(mastroSalvato.getCentroCosto());
                }
            }
            dialog = null;
        }

        public void setNode(DefaultMutableTreeTableNode node) {
            this.node = node;
        }
    }

    private class ProprietaSottoContoCommand extends ActionCommand {

        private DefaultMutableTreeTableNode node = null;

        public ProprietaSottoContoCommand() {
            super(PAGE_ID + PROPRIETA_SOTTOCONTO_COMMAND_ID);
            setVisible(false);
        }

        @Override
        protected void doExecuteCommand() {
            // controllo per bug selezione su treeTable
            if (node == null || (node != null && !(node.getUserObject() instanceof SottoConto))) {
                return;
            }

            SottoContoTitledPageApplicationDialog dialog = new SottoContoTitledPageApplicationDialog(
                    (SottoConto) node.getUserObject(), contabilitaAnagraficaBD);
            dialog.setCloseAction(CloseAction.HIDE);
            dialog.showDialog();

            node.setUserObject(dialog.getSottoContoSalvato());
            dialog = null;
        }

        public void setNode(DefaultMutableTreeTableNode node) {
            this.node = node;
        }
    }

    public class StampaPianoDeiContiCommand extends StampaCommand {
        private static final String CONTROLLER_ID = "printCommandListino";

        /**
         * Costruttore.
         */
        public StampaPianoDeiContiCommand() {
            super(CONTROLLER_ID);
        }

        @Override
        protected Map<Object, Object> getParametri() {
            HashMap<Object, Object> params = new HashMap<Object, Object>();
            params.put("utenteCorrente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
            params.put("denominazioneAzienda", aziendaCorrente.getDenominazione());
            return params;
        }

        @Override
        protected String getReportName() {
            return "Piano dei Conti";
        }

        @Override
        protected String getReportPath() {
            return "Contabilita/Anagrafica/pianoDeiConti";
        }
    }

    private static Logger logger = Logger.getLogger(PianoContiPage.class);

    private static final String PAGE_ID = "pianoContiPage";

    private static final String NUOVO_MASTRO_COMMAND_ID = ".nuovoMastroCommand";
    private static final String CANCELLA_MASTRO_COMMAND_ID = ".cancellaMastroCommand";
    private static final String PROPRIETA_MASTRO_COMMAND_ID = ".proprietaMastroCommand";
    private static final String NUOVO_CONTO_COMMAND_ID = ".nuovoContoCommand";

    private static final String CANCELLA_CONTO_COMMAND_ID = ".cancellaContoCommand";

    private static final String PROPRIETA_CONTO_COMMAND_ID = ".proprietaContoCommand";
    private static final String NUOVO_SOTTOCONTO_COMMAND_ID = ".nuovoSottoContoCommand";
    private static final String CANCELLA_SOTTOCONTO_COMMAND_ID = ".cancellaSottoContoCommand";

    private static final String PROPRIETA_SOTTOCONTO_COMMAND_ID = ".proprietaSottoContoCommand";
    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;
    private AziendaCorrente aziendaCorrente = null;

    private JPopupMenu popUpMenu;
    private JECCommandGroup nuovoCommandGroup;
    private ActionCommand stampaCommand;
    private NuovoMastroCommand nuovoMastroCommand = null;
    private CancellaMastroCommand cancellaMastroCommand = null;
    private ProprietaMastroCommand proprietaMastroCommand = null;

    private NuovoContoCommand nuovoContoCommand = null;
    private CancellaContoCommand cancellaContoCommand = null;
    private ProprietaContoCommand proprietaContoCommand = null;

    private NuovoSottoContoCommand nuovoSottoContoCommand = null;
    private CancellaSottoContoCommand cancellaSottoContoCommand = null;
    private ProprietaSottoContoCommand proprietaSottoContoCommand = null;

    /**
     * Costruttore.
     */
    public PianoContiPage() {
        super(PAGE_ID);
        createCommand();
    }

    @Override
    protected void configureTreeTable(JXTreeTable treeTable) {
        treeTable.setRootVisible(false);
        treeTable.addMouseListener(new PianoContiTreeTablePopupMenuMouseListener());
    }

    private void createCommand() {
        nuovoMastroCommand = new NuovoMastroCommand();
        cancellaMastroCommand = new CancellaMastroCommand();
        proprietaMastroCommand = new ProprietaMastroCommand();

        nuovoContoCommand = new NuovoContoCommand();
        cancellaContoCommand = new CancellaContoCommand();
        proprietaContoCommand = new ProprietaContoCommand();

        nuovoSottoContoCommand = new NuovoSottoContoCommand();
        cancellaSottoContoCommand = new CancellaSottoContoCommand();
        proprietaSottoContoCommand = new ProprietaSottoContoCommand();
    }

    private JPopupMenu createPopupMenu() {
        if (popUpMenu == null) {
            JECCommandGroup commandGroup;
            final CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            commandGroup = new JECCommandGroup("toolbarPianoContiPage");

            nuovoCommandGroup = new JECCommandGroup(PAGE_ID + ".nuovoCommand");
            c.configure(nuovoCommandGroup);

            nuovoCommandGroup.add(c.configure(nuovoMastroCommand));
            nuovoCommandGroup.add(c.configure(nuovoContoCommand));
            nuovoCommandGroup.add(c.configure(nuovoSottoContoCommand));
            commandGroup.add(nuovoCommandGroup);

            commandGroup.add(c.configure(cancellaMastroCommand));
            commandGroup.add(c.configure(proprietaMastroCommand));

            commandGroup.add(c.configure(cancellaContoCommand));
            commandGroup.add(c.configure(proprietaContoCommand));

            commandGroup.add(c.configure(cancellaSottoContoCommand));
            commandGroup.add(c.configure(proprietaSottoContoCommand));

            popUpMenu = commandGroup.createPopupMenu();
        }
        return popUpMenu;
    }

    private DefaultMutableTreeTableNode createTreeNode(List<SottoConto> listSottoconti) {
        // root su cui aggiungere i mastri
        DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();

        Mastro currentMastro = null;
        Mastro oldMastro = new Mastro();

        Conto currentConto = null;
        Conto oldConto = new Conto();

        DefaultMutableTreeTableNode nodeMastro = null;
        DefaultMutableTreeTableNode nodeConto = null;
        DefaultMutableTreeTableNode nodeSottoConto = null;
        // scorro i sottoconti
        for (SottoConto sottoConto : listSottoconti) {

            // il mastro di questo sottoconto ? currentMastro
            currentMastro = sottoConto.getConto().getMastro();

            // ################################ mastro
            // ############################################
            // se current mastro ? uguale a old mastro non serve aggiungere il
            // mastro perch? gi? presente,
            // ? possibile accederci tramite oldMastro
            if (!currentMastro.getId().equals(oldMastro.getId())) {
                // current mastro è diverso da old mastro quindi devo aggiungere il mastro al root
                // node di mastri
                nodeMastro = new DefaultMutableTreeTableNode(currentMastro);
                rootNode.add(nodeMastro);
            }

            // ################################ conto
            // ############################################
            // il conto corrente da verificare ? quello del sottoconto corrente
            currentConto = sottoConto.getConto();

            // se old conto ? uguale a current conto del sottoconto corrente
            // allora:
            if (!currentConto.getId().equals(oldConto.getId())) {
                // se old conto e current conto sono diversi devo aggiungere current conto all' node
                // mastro
                if (!currentConto.isDefault()) {
                    nodeConto = new DefaultMutableTreeTableNode(currentConto);
                    nodeMastro.add(nodeConto);
                }
            }

            // escludere i sottoconti default e
            // quelli con tipoconto patrimoniale con sottotipoconto
            // cliente/fornitore
            if (!(sottoConto.isDefault() || (TipoConto.PATRIMONIALE.equals(currentConto.getTipoConto())
                    && (SottotipoConto.CLIENTE.equals(currentConto.getSottotipoConto())
                            || (SottotipoConto.FORNITORE.equals(currentConto.getSottotipoConto())))))) {
                nodeSottoConto = new DefaultMutableTreeTableNode(sottoConto);
                nodeConto.add(nodeSottoConto);
                if (sottoConto.isSoggettoCentroCosto()) {
                    ((Conto) nodeConto.getUserObject()).setSoggettoCentroCosto(sottoConto.isSoggettoCentroCosto());
                    ((Conto) nodeConto.getUserObject()).setCentroCosto(sottoConto.getCentroCosto());
                    ((Mastro) nodeMastro.getUserObject()).setSoggettoCentroCosto(sottoConto.isSoggettoCentroCosto());
                    ((Mastro) nodeMastro.getUserObject()).setCentroCosto(sottoConto.getCentroCosto());
                }
            }

            // aggiorno i riferimenti di old mastro e old conto per il
            // sottoconto successivo
            oldMastro = (Mastro) PanjeaSwingUtil.cloneObject(currentMastro);
            oldConto = (Conto) PanjeaSwingUtil.cloneObject(currentConto);
        }

        return rootNode;
    }

    @Override
    protected TreeTableModel createTreeTableModel() {
        return new PianoDeiContiTreeTableModel(createTreeNode(new ArrayList<SottoConto>()));
    }

    @Override
    public AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getExpandCommand(),
                new StampaPianoDeiContiCommand() };
        return abstractCommands;
    }

    /**
     * @return Returns the contabilitaBD.
     */
    public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
        return contabilitaAnagraficaBD;
    }

    @Override
    public String getPageEditorId() {
        return this.PAGE_ID;
    }

    @Override
    public Object getPageObject() {
        return null;
    }

    @Override
    public String getPageSecurityEditorId() {
        return null;
    }

    @Override
    protected TreeCellRenderer getTreeCellRender() {
        return new DefaultTreeCellRenderer() {

            private static final long serialVersionUID = -8813682511205137384L;

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
                JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
                // setto l'icona
                if (node.getUserObject() != null) {
                    c.setIcon(iconSource.getIcon(node.getUserObject().getClass().getName()));
                }

                if (node.getUserObject() instanceof Mastro) {
                    c.setText(((Mastro) node.getUserObject()).getCodice());
                } else {
                    if (node.getUserObject() instanceof Conto) {
                        c.setText(((Conto) node.getUserObject()).getCodice());
                    } else {
                        if (node.getUserObject() instanceof SottoConto) {
                            c.setText(((SottoConto) node.getUserObject()).getCodice());
                        }
                    }
                }
                return c;
            }
        };
    }

    @Override
    public void grabFocus() {
        getControl().requestFocusInWindow();
    }

    @Override
    public boolean isCommittable() {
        return false;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void loadData() {
        List<SottoConto> conti = contabilitaAnagraficaBD.ricercaSottoContiOrdinati();
        updateTreeModel(createTreeNode(conti));
    }

    @Override
    public Object onDelete() {
        return null;
    }

    @Override
    public ILock onLock() {
        return null;
    }

    @Override
    public void onNew() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public boolean onSave() {
        return true;
    }

    @Override
    public boolean onUndo() {
        return true;
    }

    @Override
    protected void openSelectedNode(DefaultMutableTreeTableNode node) {
        if (node.getUserObject() instanceof Mastro) {
            PianoContiPage.logger.debug("--> Selezionato il mastro");
            proprietaMastroCommand.setNode(node);
            proprietaMastroCommand.execute();
        } else {
            if (node.getUserObject() instanceof Conto) {
                PianoContiPage.logger.debug("--> Selezionato il conto");
                proprietaContoCommand.setNode(node);
                proprietaContoCommand.execute();
            } else {
                if (node.getUserObject() instanceof SottoConto) {
                    PianoContiPage.logger.debug("--> Selezionato il Sottoconto");
                    proprietaSottoContoCommand.setNode(node);
                    proprietaSottoContoCommand.execute();
                }
            }
        }
    }

    @Override
    public void refreshData() {
    }

    @Override
    protected void selectionChanged(DefaultMutableTreeTableNode node) {
        if (node != null) {
            updateCommand(node.getUserObject());
            updateSelectedNode(node);
        } else {
            updateCommand(null);
        }

    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param contabilitaAnagraficaBD
     *            The contabilitaBD to set.
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
    }

    @Override
    public void unLock() {
    }

    private void updateCommand(Object object) {
        if (popUpMenu == null) {
            createPopupMenu();
        }
        nuovoCommandGroup.setEnabled(false);

        nuovoMastroCommand.setVisible(false);
        cancellaMastroCommand.setVisible(false);
        proprietaMastroCommand.setVisible(false);

        nuovoContoCommand.setVisible(false);
        cancellaContoCommand.setVisible(false);
        proprietaContoCommand.setVisible(false);

        nuovoSottoContoCommand.setVisible(false);
        cancellaSottoContoCommand.setVisible(false);
        proprietaSottoContoCommand.setVisible(false);

        if (object == null) {
            nuovoCommandGroup.setEnabled(true);
            nuovoMastroCommand.setVisible(true);

        } else {
            if (object instanceof Mastro) {
                nuovoCommandGroup.setEnabled(true);

                nuovoMastroCommand.setVisible(true);
                cancellaMastroCommand.setVisible(true);
                proprietaMastroCommand.setVisible(true);

                nuovoContoCommand.setVisible(true);
            } else {
                if (object instanceof Conto) {
                    nuovoCommandGroup.setEnabled(true);

                    cancellaContoCommand.setVisible(true);
                    proprietaContoCommand.setVisible(true);

                    nuovoSottoContoCommand.setVisible(true);
                    nuovoSottoContoCommand.setEnabled(true);

                    // regola di dominio: se il tipo conto del conto ?
                    // PATRIMONIALE e il tipo sottoconto ?
                    // diverso da VUOTO non devo far inserire un sottoconto
                    if ((((Conto) object).getTipoConto() == TipoConto.PATRIMONIALE)
                            && (((Conto) object).getSottotipoConto() != SottotipoConto.VUOTO)) {
                        nuovoSottoContoCommand.setEnabled(false);
                    }
                } else {
                    if (object instanceof SottoConto) {
                        cancellaSottoContoCommand.setVisible(true);
                        proprietaSottoContoCommand.setVisible(true);
                    }
                }
            }
        }
    }

    /**
     * Imposta il nodo selezionato al cambio della selezione; questo metodo si occupa di impostare
     * il nodo al relativo command (M/C/S).
     *
     * @param node
     *            il nodo da impostare al relativo command a seconda che sia mastro, conto o
     *            sottoconto
     */
    private void updateSelectedNode(DefaultMutableTreeTableNode node) {
        if (node.getUserObject() instanceof Mastro) {
            PianoContiPage.logger.debug("--> Selezionato il mastro");
            proprietaMastroCommand.setNode(node);
        } else {
            if (node.getUserObject() instanceof Conto) {
                PianoContiPage.logger.debug("--> Selezionato il conto");
                proprietaContoCommand.setNode(node);
            } else {
                if (node.getUserObject() instanceof SottoConto) {
                    PianoContiPage.logger.debug("--> Selezionato il Sottoconto");
                    proprietaSottoContoCommand.setNode(node);
                }
            }
        }
    }

    private void updateTreeModel(DefaultMutableTreeTableNode rootNode) {
        setTableData(rootNode);
        if (getTreeTable().getRowCount() > 0) {
            getExpandCommand().setEnabled(true);
        } else {
            getExpandCommand().setEnabled(false);
        }
    }
}
