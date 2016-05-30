/**
 *
 */
package it.eurotn.panjea.magazzino.rich.commands.analisi;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 *
 */
public class ApriAnalisiArticoloEntitaCommand extends ActionCommand {

    public static final String PARAM_ID_ARTICOLO = "paramIdArticolo";
    public static final String PARAM_ID_ENTITA = "paramIdEntita";

    private static final String COMMAND_ID = "apriAnalisiArticoloEntitaCommand";

    private AnalisiArticoloAlert alert;

    /**
     * Costruttore.
     */
    public ApriAnalisiArticoloEntitaCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        Integer idArticolo = (Integer) getParameter(PARAM_ID_ARTICOLO, null);
        Integer idEntita = (Integer) getParameter(PARAM_ID_ENTITA, null);

        alert.setIdArticolo(idArticolo);
        alert.setIdEntita(idEntita);
        alert.showPopup();
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
        super.onButtonAttached(button);
        button.setFocusable(false);
        alert = new AnalisiArticoloAlert(button);
    }

}
