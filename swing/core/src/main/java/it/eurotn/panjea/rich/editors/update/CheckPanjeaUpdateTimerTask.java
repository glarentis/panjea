package it.eurotn.panjea.rich.editors.update;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimerTask;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;

public class CheckPanjeaUpdateTimerTask extends TimerTask {

    private PanjeaServer panjeaServer = null;

    private MessageAlert messageAlert = null;

    /**
     * Restiuisce un messageAlert con il messaggio scelto che non si chiude automaticamente.
     *
     * @param message
     *            il messaggio da mostrare nell'alert
     * @return MessageAlert
     */
    private MessageAlert getMessageAlert(Message message) {
        messageAlert = new MessageAlert(message, 0);
        ActionCommand mantisCommand = RcpSupport.getCommand("mantisCommand");
        ActionCommand updateCommand = RcpSupport.getCommand("updateCommand");
        List<ActionCommand> commands = new ArrayList<ActionCommand>();
        commands.add(updateCommand);
        commands.add(mantisCommand);
        commands.add(messageAlert.getCloseAlertCommand());

        messageAlert.setCommands(commands);
        return messageAlert;
    }

    /**
     * @return the panjeaServer
     */
    public PanjeaServer getPanjeaServer() {
        return panjeaServer;
    }

    @Override
    public void run() {
        if (PanjeaSwingUtil.hasPermission("aggiornamento")) {
            Properties updatedProperties = panjeaServer.isUpdateAvailable();
            // se ho il properties significa che ho trovato un aggiornamento disponibile
            if (updatedProperties != null) {
                showMessageAlert(updatedProperties);
            }
        }
    }

    /**
     * @param panjeaServer
     *            the panjeaServer to set
     */
    public void setPanjeaServer(PanjeaServer panjeaServer) {
        this.panjeaServer = panjeaServer;
    }

    /**
     * Visualizza se non già visibile l'alert per avvertire l'utente della presenza di un
     * aggiornamento.
     *
     * @param updatedProperties
     *            il properties dell'utente con le informazioni dell'aggiornamento
     */
    private void showMessageAlert(Properties updatedProperties) {
        if (messageAlert != null && messageAlert.isVisible()) {
            return;
        }
        String aziende = updatedProperties.getProperty("aziende");
        String versioneDisponibile = updatedProperties.getProperty("updateVersion");
        String buildid = updatedProperties.getProperty("buildid");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dataAggiornamento = null;
        try {
            Date dateBuildId = dateFormat.parse(buildid);
            dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dataAggiornamento = dateFormat.format(dateBuildId);
        } catch (ParseException e) {
            dataAggiornamento = "n.p.";
        }

        StringBuffer messageStringBuffer = new StringBuffer();
        messageStringBuffer.append("Nuova versione disponibile per:<br>");
        messageStringBuffer.append("<b>");
        messageStringBuffer.append(aziende.toUpperCase(Locale.getDefault()));
        messageStringBuffer.append("</b><br>");
        messageStringBuffer.append("<b>v");
        messageStringBuffer.append(versioneDisponibile);
        messageStringBuffer.append("</b> del <b>");
        messageStringBuffer.append(dataAggiornamento);
        messageStringBuffer.append("</b>");

        Message message = new DefaultMessage(messageStringBuffer.toString());
        messageAlert = getMessageAlert(message);
        messageAlert.showAlert();
    }

}
