package it.eurotn.panjea.rich;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationDescriptor;
import org.springframework.richclient.application.config.ApplicationLifecycleAdvisor;

public class PanjeaApplication extends Application {
    private String[] args;

    /**
     * Costruttore.
     * 
     * @param descriptor
     *            descrittore dell'applicazione
     * @param advisor
     *            lifecycleAdvisor per l'applicazione
     */
    public PanjeaApplication(final ApplicationDescriptor descriptor, final ApplicationLifecycleAdvisor advisor) {
        super(descriptor, advisor);
    }

    /**
     * 
     * @return nome dell'azienda impostata nel command line. vuota se non è stata impostata
     */
    public String getAziendaCommandLine() {
        if (args.length == 0) {
            return "";
        }
        return args[0];
    }

    /**
     * @param args
     *            The args to set.
     */
    public void setArgs(String[] args) {
        this.args = args;
    }

}
