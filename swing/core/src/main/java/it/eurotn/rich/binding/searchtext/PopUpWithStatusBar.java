package it.eurotn.rich.binding.searchtext;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.swing.JideBoxLayout;

/**
 * Popup del componente di ricerca, è composto principalmente dai risultati di una ricerca sotto forma di tabella e da
 * una status bar che può essere aggiunta a seconda dell'esigenza.
 * 
 * @author Leonardo
 */
public class PopUpWithStatusBar extends JidePopup {

    private static final long serialVersionUID = 8561789445946903956L;
    private Component owner;
    private LabelStatusBarItem labelStatusBarItem;
    private boolean closingPopup = false;
    /**
     * @uml.property name="table"
     * @uml.associationEnd
     */
    private TableSearchText table;

    private JLabel lblStatus;

    /**
     * 
     * @param tableSearchText
     *            tabella da visualizzare
     * @param paramOwner
     *            owner
     */
    public PopUpWithStatusBar(final TableSearchText tableSearchText, final Component paramOwner) {
        super();
        setName("searchPopupResult");
        this.table = tableSearchText;
        setOwner(paramOwner);
        setMovable(false);
        getContentPane().setLayout(new BorderLayout());
        setResizable(true);
        setDetached(false);
        getContentPane().add(table.getComponent(), BorderLayout.CENTER);
        getContentPane().add(getPopUpStatusBar(), BorderLayout.SOUTH);
        // int size = 0;
        // Enumeration<TableColumn> columns = table.getTable().getColumnModel().getColumns();
        // while (columns.hasMoreElements()) {
        // TableColumn tableColumn = columns.nextElement();
        // size += tableColumn.getWidth();
        // }
        // setPreferredPopupSize(new Dimension(size + 10, 200));
        // setPreferredSize(new Dimension(size + 10, 200));
    }

    /**
     * 
     * @return Status bar per la popup risultati.
     */
    private JComponent getPopUpStatusBar() {
        JPanel popupStatus = new com.jidesoft.status.StatusBar();
        labelStatusBarItem = new LabelStatusBarItem() {
            private static final long serialVersionUID = -2485037480787854020L;

            @Override
            protected void configureLabel(javax.swing.JLabel jlabel) {
                lblStatus = jlabel;
                if (owner instanceof SearchTextField) {
                    SearchTextField searchTextField = (SearchTextField) owner;
                    // label usata solo per i test
                    lblStatus.setName(searchTextField.getTextField().getName() + "Status");
                }

            };
        };
        popupStatus.add(labelStatusBarItem, JideBoxLayout.VARY);
        popupStatus.setName("searchPanelTest");
        return popupStatus;
    }

    @Override
    public void hidePopup() {
        super.hidePopup();
    }

    @Override
    public void hidePopupImmediately(boolean flag) {
        closingPopup = true;
        try {
            table.saveState(null);
            super.hidePopupImmediately(flag);
        } finally {
            closingPopup = false;
            ((SearchTextField) owner).disableShowPopUpImmediatly();
        }
    }

    /**
     * @return Returns the closingPopup.
     */
    public boolean isClosingPopup() {
        return closingPopup;
    }

    /**
     * @param component
     *            owner
     * @uml.property name="owner"
     */
    @Override
    public void setOwner(Component component) {
        super.setOwner(component);
        owner = component;
        if (component instanceof SearchTextField && lblStatus != null) {
            SearchTextField searchTextField = (SearchTextField) component;
            // label usata solo per i test
            lblStatus.setName(searchTextField.getTextField().getName() + "Status");
        }
    }

    /**
     * Metodo per aggiornare il testo visualizzato sulla status bar della popup.
     * 
     * @param text
     *            il testo da visualizzare
     */
    public void setText(String text) {
        labelStatusBarItem.setText(text);
    }

    @Override
    public void showPopup() {
        super.showPopup(owner);
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }

    @Override
    public void showPopup(Component arg0) {
        super.showPopup(owner);
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }

    @Override
    public void showPopup(Insets arg0) {
        super.showPopup(arg0);
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }

    @Override
    public void showPopup(Insets arg0, Component arg1) {
        super.showPopup(arg0, owner);
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }

    @Override
    public void showPopup(int arg0) {
        super.showPopup(arg0);
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }

    @Override
    public void showPopup(int arg0, Component arg1) {
        super.showPopup(arg0, getActualOwner());
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }

    @Override
    public void showPopup(int arg0, int arg1) {
        super.showPopup(arg0, arg1);
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }

    @Override
    public void showPopup(int arg0, int arg1, Component arg2) {
        super.showPopup(arg0, arg1, owner);
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }

    @Override
    protected void showPopupImmediately() {
        ((SearchTextField) owner).enableShowPopUpImmediatly();
        table.restoreState(null);
        super.showPopupImmediately();
        if (_window != null && _window.getResizable() != null) {
            _window.getResizable().setResizeCornerSize(100);
        }
    }
}
