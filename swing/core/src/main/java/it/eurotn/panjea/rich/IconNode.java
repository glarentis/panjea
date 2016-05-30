package it.eurotn.panjea.rich;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.springframework.richclient.command.ActionCommand;

class IconNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 3066962170830659512L;
    protected Icon icon;
    protected String iconName;
    private ActionCommand command;

    /**
     * Costruttore.
     */
    public IconNode() {
        this(null);
    }

    /**
     * Costruttore.
     *
     * @param userObject
     *            an Object provided by the user that constitutes the node's data
     */
    public IconNode(final Object userObject) {
        this(userObject, true, null);
    }

    /**
     * Costruttore.
     *
     * @param userObject
     *            an Object provided by the user that constitutes the node's data
     * @param allowsChildren
     *            if true, the node is allowed to have child nodes -- otherwise, it is always a leaf node
     * @param icon
     *            icona
     */
    public IconNode(final Object userObject, final boolean allowsChildren, final Icon icon) {
        super(userObject, allowsChildren);
        this.icon = icon;
    }

    /**
     * @return comando legato al nodo
     */
    public ActionCommand getCommand() {
        return command;
    }

    /**
     * @return icona del nodo
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @return icon name
     */
    public String getIconName() {
        if (iconName != null) {
            return iconName;
        } else {
            String str = userObject.toString();
            int index = str.lastIndexOf(".");
            if (index != -1) {
                return str.substring(++index);
            } else {
                return null;
            }
        }
    }

    /**
     * @param command
     *            comando legato al nodo
     */
    public void setCommand(ActionCommand command) {
        this.command = command;
    }

    /**
     * @param icon
     *            icona del nodo
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * @param name
     *            icon name
     */
    public void setIconName(String name) {
        iconName = name;
    }
}