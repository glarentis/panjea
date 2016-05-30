package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import java.awt.BorderLayout;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.components.JideLookAndFeelConfigurer;
import com.jidesoft.status.StatusBarItem;
import com.jidesoft.utils.PortingUtils;

/**
 * 
 * Elemento della status bar che incrementa/decrementa le font della gui.
 * 
 * @author giangi
 * @version 1.0, 16/ago/2010
 * 
 */
public class ChangeFontSizeStatusBarItem extends StatusBarItem {

    private class DecrementFontSizeCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public DecrementFontSizeCommand() {
            super("decrementFontSizeCommand");
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            JideLookAndFeelConfigurer conf = RcpSupport.getBean("lookAndFeelConfigurer");
            conf.decrementFontSize();
        }
    }

    private class IncrementFontSizeCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public IncrementFontSizeCommand() {
            super("incrementFontSizeCommand");
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            JideLookAndFeelConfigurer conf = RcpSupport.getBean("lookAndFeelConfigurer");
            conf.incrementFontSize();
        }
    }

    private static final long serialVersionUID = 2251019230383768027L;

    /**
     * Costruttore.
     */
    public ChangeFontSizeStatusBarItem() {
        AbstractButton incrementCommand = new IncrementFontSizeCommand().createButton();
        AbstractButton decrementCommand = new DecrementFontSizeCommand().createButton();
        PortingUtils.removeButtonBorder(decrementCommand);
        PortingUtils.removeButtonBorder(incrementCommand);
        setLayout(new BorderLayout());
        add(decrementCommand, BorderLayout.LINE_START);
        add(incrementCommand, BorderLayout.CENTER);
    }

    @Override
    public String getItemName() {
        return "changeFontSizeStatusBarItem";
    }

    @Override
    public int getPreferredWidth() {
        return 50;
    }
}
