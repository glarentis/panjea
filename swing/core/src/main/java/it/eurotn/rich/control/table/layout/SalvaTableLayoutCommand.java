package it.eurotn.rich.control.table.layout;

import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SalvaTableLayoutCommand extends ApplicationWindowAwareCommand {

    private class SaveDialog extends TitledApplicationDialog {

        public static final String SAVE_LAYOUT_DIALOG_TITLE = "saveTableLayout.save.title";
        public static final String SAVE_LAYOUT_DIALOG_MESSAGE = "saveTableLayout.save.message";

        private JTextField nomeTextField;
        private JCheckBox globalCheckBox;
        private String layoutName;

        /**
         * Costruttore.
         *
         * @param layoutName
         */
        public SaveDialog(String layoutName) {
            super();
            this.layoutName = layoutName;
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
            if (!StringUtils.isEmpty(layoutName)) {
                nomeTextField.setText(layoutName);
            }
            builder.add(nomeTextField, cc.xy(3, 1));
            builder.nextRow();

            globalCheckBox = new JCheckBox("layout condiviso");
            builder.add(globalCheckBox, cc.xy(3, 2));
            builder.nextRow();

            return builder.getPanel();
        }

        @Override
        protected void onCancel() {
            super.onCancel();

            layoutSalvato = null;
        }

        @Override
        protected boolean onFinish() {

            String layoutName = nomeTextField.getText();
            if (layoutName == null || layoutName.length() == 0) {
                return false;
            }

            TableLayout layout = new TableLayout();
            layout.setChiave(layoutManager.getTableWidgetId());
            layout.setGlobal(globalCheckBox.isSelected());
            layout.setName(layoutName);

            layoutSalvato = layoutManager.salva(layout);

            return true;
        }

    }

    public static final String LAYOUT_NAME = "layoutName";

    public static final String COMMAND_ID = "saveTableLayoutCommand";

    private DefaultTableLayoutManager layoutManager = null;

    private AbstractLayout layoutSalvato = null;

    /**
     * Costruttore.
     *
     * @param layoutManager
     *            layout manager
     */
    public SalvaTableLayoutCommand(final DefaultTableLayoutManager layoutManager) {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        this.layoutManager = layoutManager;
    }

    @Override
    protected void doExecuteCommand() {
        String layoutName = (String) getParameter(LAYOUT_NAME);
        SaveDialog dialog = new SaveDialog(layoutName);
        dialog.showDialog();
    }

    /**
     * @return Returns the layoutSalvato.
     */
    public AbstractLayout getLayoutSalvato() {
        return layoutSalvato;
    }

    /**
     * @param layoutManager
     *            the layoutManager to set
     */
    public void setLayoutManager(DefaultTableLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

}
