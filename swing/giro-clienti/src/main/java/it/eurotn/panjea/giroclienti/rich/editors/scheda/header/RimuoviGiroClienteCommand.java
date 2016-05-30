package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import java.awt.Dimension;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.rich.bd.GiroClientiAnagraficaBD;
import it.eurotn.panjea.giroclienti.rich.bd.IGiroClientiAnagraficaBD;

public class RimuoviGiroClienteCommand extends ActionCommand {

    private final class DeleteGiroDialog extends ConfirmationDialog {

        private final RigaGiroCliente rigaGiroCliente;

        /**
         * Costruttore.
         *
         * @param message
         *            messaggio
         * @param rigaGiroCliente
         *            riga giro
         */
        private DeleteGiroDialog(final String message, final RigaGiroCliente rigaGiroCliente) {
            super("Conferma cancellazione", message);
            this.rigaGiroCliente = rigaGiroCliente;
            setPreferredSize(new Dimension(650, 110));
        }

        @Override
        protected void onConfirm() {
            giroClientiAnagraficaBD.cancellaGiroSedeCliente(rigaGiroCliente.getSedeEntita().getId(),
                    rigaGiroCliente.getGiorno(), rigaGiroCliente.getOra());
            giroRimosso = true;
        }
    }

    public static final String PARAM_GIRO_DA_RIMUOVERE = "paramGiroDaRimuovere";

    private IGiroClientiAnagraficaBD giroClientiAnagraficaBD;

    private boolean giroRimosso = false;

    /**
     * Costruttore.
     */
    public RimuoviGiroClienteCommand() {
        super("rimuoviGiroClienteCommand");
        RcpSupport.configure(this);

        this.giroClientiAnagraficaBD = RcpSupport.getBean(GiroClientiAnagraficaBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        giroRimosso = false;

        final RigaGiroCliente rigaGiroCliente = (RigaGiroCliente) getParameter(PARAM_GIRO_DA_RIMUOVERE, null);

        if (rigaGiroCliente != null) {

            StringBuilder sbMessage = new StringBuilder(200);
            sbMessage.append("<html><br>Cancellare il giro del cliente <b>");
            sbMessage.append(ObjectConverterManager.toString(rigaGiroCliente.getSedeEntita().getEntita()));
            sbMessage.append("</b><br>per il giorno <b>");
            sbMessage.append(ObjectConverterManager.toString(rigaGiroCliente.getGiorno()));
            sbMessage.append("</b><br>delle ore <b>");
            sbMessage.append(DateFormatUtils.format(rigaGiroCliente.getOra(), "HH:mm"));
            sbMessage.append("</b>?<br>");

            new DeleteGiroDialog(sbMessage.toString(), rigaGiroCliente).showDialog();
        }
    }

    /**
     * @return the giroRimosso
     */
    public boolean isGiroRimosso() {
        return giroRimosso;
    }

}
