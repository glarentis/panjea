package it.eurotn.rich.control;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideButton;

/**
 * SplitButton class that provides a drop down menu when the right side arrow is clicked. Written by
 * Edward Scholl (edscholl@atwistedweb.com)- use as you wish, but a acknowlegement would be
 * appreciated if you use this...
 *
 * @author Edward Scholl
 */
public class SplitButton extends JideButton implements ActionListener {

    private static final long serialVersionUID = 4263969136159148331L;

    private JButton mainButton;
    private JButton dropDownButton;

    private JidePopup dropDownMenu;

    /**
     * Default Constructor that creates a blank button with a down facing arrow.
     */
    public SplitButton() {
        this(" ");
    }

    /**
     * Passes in the button to use in the left hand side, with the specified orientation for the
     * arrow on the right hand side.
     *
     * @param mainButton
     *            JButton
     * @param orientation
     *            int
     */
    public SplitButton(final JButton mainButton, final int orientation) {
        super();
        this.mainButton = mainButton;
        this.mainButton.setBorder(new EmptyBorder(0, 5, 0, 0));
        // this.dropDownButton = new BasicArrowButton(orientation, UIManager.getColor("control"),
        // UIManager.getColor("controlShadow"), Color.BLACK,
        // UIManager.getColor("controlLtHighlight"));
        this.dropDownButton = new JideButton(RcpSupport.getIcon("arrow-down"));
        dropDownButton.setBorder(new EmptyBorder(0, 2, 0, 7));
        dropDownButton.addActionListener(this);

        this.setBorderPainted(false);

        this.setLayout(new BorderLayout());
        this.setMargin(new Insets(-3, -3, -3, -3));

        this.add(mainButton, BorderLayout.CENTER);
        this.add(dropDownButton, BorderLayout.EAST);
    }

    /**
     * Creates a button with the specified text and a down facing arrow.
     *
     * @param text
     *            String
     */
    public SplitButton(final String text) {
        this(new JideButton(text), SwingConstants.SOUTH);
    }

    /**
     * Creates a button with the specified text and a arrow in the specified direction.
     *
     * @param text
     *            String
     * @param orientation
     *            int
     */
    public SplitButton(final String text, final int orientation) {
        this(new JideButton(text), orientation);
    }

    /**
     * action listener for the arrow button- shows / hides the popup menu.
     *
     * @param e
     *            ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if (this.dropDownMenu == null || !this.dropDownMenu.isEnabled()) {
            return;
        }
        if (!dropDownMenu.isPopupVisible()) {
            showPopup();
        } else {
            dropDownMenu.hidePopup();
        }
    }

    /**
     * adds a action listener to this button (actually to the left hand side button, and any left
     * over surrounding space. the arrow button will not be affected.
     *
     * @param al
     *            ActionListener
     */
    @Override
    public void addActionListener(ActionListener al) {
        this.mainButton.addActionListener(al);
    }

    /**
     * gets the drop down button (with the arrow).
     *
     * @return JButton
     */
    public JButton getDropDownButton() {
        return dropDownButton;
    }

    /**
     * returns the main (left hand side) button.
     *
     * @return JButton
     */
    public JButton getMainButton() {
        return mainButton;
    }

    /**
     * gets the drop down menu.
     *
     * @return JPopupMenu
     */
    public JidePopup getMenu() {
        return dropDownMenu;
    }

    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        mainButton.setEnabled(enable);
        dropDownButton.setEnabled(enable);
        dropDownMenu.setEnabled(enable);
    }

    /**
     * Sets the popup menu to show when the arrow is clicked.
     *
     * @param menu
     *            JPopupMenu
     */
    public void setMenu(JidePopup menu) {
        this.dropDownMenu = menu;
    }

    /**
     * Visualizza il popup.
     */
    public void showPopup() {
        dropDownMenu.setOwner(mainButton);
        dropDownMenu.setResizable(false);
        dropDownMenu.setMovable(false);
        dropDownMenu.showPopup();
    }
}
