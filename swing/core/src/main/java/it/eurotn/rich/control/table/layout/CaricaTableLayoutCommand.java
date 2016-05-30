package it.eurotn.rich.control.table.layout;

import it.eurotn.panjea.anagrafica.domain.TableLayout;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class CaricaTableLayoutCommand extends ActionCommand {

    public static final String COMMAND_ID = "caricaTableLayoutCommand";

    private final DefaultTableLayoutManager layoutManager;
    private final TableLayout layout;

    /**
     * Costruttore.
     * 
     * @param layoutManager
     *            layout manager
     * @param layout
     *            layout
     */
    public CaricaTableLayoutCommand(final DefaultTableLayoutManager layoutManager, final TableLayout layout) {
        super();
        RcpSupport.configure(this);
        this.layoutManager = layoutManager;
        this.layout = layout;
    }

    @Override
    protected void doExecuteCommand() {
        layoutManager.applica(layout);
    }

    @Override
    public String getText() {
        return layout.getName();
    }

}
