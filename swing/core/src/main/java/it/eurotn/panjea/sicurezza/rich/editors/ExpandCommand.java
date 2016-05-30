package it.eurotn.panjea.sicurezza.rich.editors;

import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.tree.TreeUtils;

public class ExpandCommand extends ActionCommand {

    private static final String ESPANDI_COMMAND = ".expandCommand";
    private static final String EXPAND_STATE = "expand";
    private static final String COLLAPSE_STATE = "collapse";
    private final CommandFaceDescriptor expandDescriptor;
    private final CommandFaceDescriptor collapseDescriptor;

    public static final String PARAM_TREE = "paramTree";

    private boolean collapse;

    private CheckBoxTree checkBoxTree;

    /**
     * Costruttore.
     * 
     * @param collapse
     *            stato inizale del comando
     * @param checkBoxTree
     *            tree da gestire
     */
    public ExpandCommand(final boolean collapse, final CheckBoxTree checkBoxTree) {
        super(ESPANDI_COMMAND);
        RcpSupport.configure(this);
        this.checkBoxTree = checkBoxTree;
        this.collapse = collapse;

        Icon toExpandIcon = RcpSupport.getIcon(EXPAND_STATE + ".icon");
        Icon toCollapseIcon = RcpSupport.getIcon(COLLAPSE_STATE + ".icon");
        collapseDescriptor = new CommandFaceDescriptor(null, toExpandIcon, null);
        expandDescriptor = new CommandFaceDescriptor(null, toCollapseIcon, null);
        if (collapse) {
            setFaceDescriptor(collapseDescriptor);
        } else {
            setFaceDescriptor(expandDescriptor);

        }
    }

    @Override
    protected void doExecuteCommand() {
        collapse = !collapse;

        if (collapse) {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) checkBoxTree.getModel().getRoot();

            Enumeration<?> children = root.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
                checkBoxTree.collapsePath(new TreePath(childNode.getPath()));
            }
        } else {
            TreeUtils.expandAll(checkBoxTree, true);
        }

        if (getFaceDescriptor().equals(collapseDescriptor)) {
            setFaceDescriptor(expandDescriptor);
        } else {
            setFaceDescriptor(collapseDescriptor);
        }
    }
}