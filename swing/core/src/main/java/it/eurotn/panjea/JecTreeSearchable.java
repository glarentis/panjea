package it.eurotn.panjea;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.TreeSearchable;

/**
 * @author fattazzo
 *
 */
public class JecTreeSearchable extends TreeSearchable {

    private FormModel formModel;

    /**
     * Costruttore.
     *
     * @param paramJTree
     *            tree
     * @param formModel
     *            form model
     */
    public JecTreeSearchable(final JTree paramJTree, final FormModel formModel) {
        super(paramJTree);
        this.formModel = formModel;
        setRecursive(true);
        setRepeats(true);
    }

    @Override
    protected String convertElementToString(Object paramObject) {
        if (paramObject instanceof TreePath) {
            Object localObject = ((TreePath) paramObject).getLastPathComponent();
            Object nodeObj = ((DefaultMutableTreeNode) localObject).getUserObject();

            if (nodeObj instanceof String) {
                String message = RcpSupport.getMessage((String) nodeObj);
                if (message.isEmpty()) {
                    message = formModel.getFieldFace((String) nodeObj).getLabelInfo().getText();
                }
                return message;
            } else {
                return ObjectConverterManager.toString(nodeObj);
            }
        }
        if (paramObject != null) {
            return paramObject.toString();
        }
        return "";
    }

}
