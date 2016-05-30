package it.eurotn.rich.command.support;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.command.CommandGroupFactoryBean;
import org.springframework.richclient.command.support.SimpleGroupContainerPopulator;

import com.jidesoft.swing.ButtonStyle;
import com.jidesoft.swing.JideButton;

public class JECButtonStackGroupContainerPopulator extends SimpleGroupContainerPopulator {

    private List<Object> buttons = new ArrayList<Object>();

    /**
     *
     * Costruttore.
     */
    public JECButtonStackGroupContainerPopulator() {
        super(new JPanel());
    }

    /**
     * @see SimpleGroupContainerPopulator#add(Component)
     */
    @Override
    public void add(Component component) {
        buttons.add(component);
    }

    /**
     * @see SimpleGroupContainerPopulator#addSeparator()
     */
    @Override
    public void addSeparator() {
        buttons.add(CommandGroupFactoryBean.SEPARATOR_MEMBER_CODE);
    }

    /**
     *
     * @return button stack
     */
    public JComponent getButtonStack() {
        return (JComponent) getContainer();
    }

    @Override
    public void onPopulated() {
        JPanel panel = (JPanel) getContainer();
        panel.setLayout(new GridLayout(buttons.size(), 1));
        int length = buttons.size();
        for (int i = 0; i < length; i++) {
            Object obj = buttons.get(i);
            if (obj instanceof JideButton) {
                JideButton button = (JideButton) obj;
                button.setButtonStyle(ButtonStyle.HYPERLINK_STYLE);

                button.setOpaque(false);
                button.setPreferredSize(new Dimension(0, 20));
                button.setHorizontalAlignment(SwingConstants.LEFT);
                button.setRequestFocusEnabled(true);
                button.setFocusable(true);

                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            }
            panel.add((Component) obj);
        }
    }
}
