package it.eurotn.panjea.rich.commands;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.rich.bd.DmsBD;
import it.eurotn.panjea.rich.bd.IDmsBD;

public class OpenLogicalDocUrlCommand extends ApplicationWindowAwareCommand {

    private IDmsBD bd;

    /**
     * Costruttore.
     */
    public OpenLogicalDocUrlCommand() {
        super("openLogicalDocUrlCommand");
    }

    @Override
    protected void doExecuteCommand() {
        try {
            // LifecycleApplicationEvent event = new OpenEditorEvent(new LogicalDocUrl());
            // Application.instance().getApplicationContext().publishEvent(event);

            bd = RcpSupport.getBean(DmsBD.BEAN_ID);
            String url = bd.caricaDmsSettings().getServiceUrl();
            if (Desktop.isDesktopSupported() && Desktop.getDesktop() != null) {
                // Windows
                Desktop.getDesktop().browse(new URI(url));
            } else {
                // Linux ( tento di aprire il link con firefox )
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("/usr/bin/firefox -new-window " + url);
            }
        } catch (Exception e) {
            new MessageDialog("ATTENZIONE", new DefaultMessage("Sistema operativo non supportato.", Severity.INFO))
                    .showDialog();
        }
    }

    /**
     * @return Returns the bd.
     */
    public IDmsBD getBd() {
        return bd;
    }

    /**
     * @param bd
     *            The bd to set.
     */
    public void setBd(IDmsBD bd) {
        this.bd = bd;
    }

}
