package it.eurotn.panjea.rich.commands;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Calendar;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;

import it.eurotn.panjea.utils.FileDownloader;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class TeamviewerCommand extends ApplicationWindowAwareCommand {

    private static Logger logger = Logger.getLogger(TeamviewerCommand.class);

    public static final String COMMAND_ID = "teamviewerCommand";
    private static final String FILE_NAME = "teamviewer.exe";
    private long startClick = 0;

    @Override
    protected void doExecuteCommand() {
        long seconds = (Calendar.getInstance().getTimeInMillis() - startClick) / 1000;
        if (startClick != 0 && seconds < 4) {
            return;
        }
        startClick = Calendar.getInstance().getTimeInMillis();
        Path pathExe = PanjeaSwingUtil.getHome().resolve(FILE_NAME);

        boolean launch = true;
        if (!Files.exists(pathExe, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
            StringBuilder sb = new StringBuilder("http://").append(PanjeaSwingUtil.getServerUrl())
                    .append(":8080/panjea/");
            try {
                setEnabled(false);
                launch = FileDownloader.download(sb.toString(), FILE_NAME, pathExe);
                if (!launch) {
                    MessageDialog dialog = new MessageDialog("Errore download file",
                            new DefaultMessage(
                                    "Errore nell'avvio dell'assistenza remota.\n Controllare il file di log per ulteriori informazioni",
                                    Severity.ERROR));
                    dialog.showDialog();
                }
            } catch (Exception ex) {
                logger.error("-->errore nello scaricare teamviewer", ex);
                setEnabled(true);
            }
        }

        if (launch) {
            ProcessBuilder pb = null;
            if (SystemUtils.IS_OS_LINUX) {
                pb = new ProcessBuilder("wine", pathExe.toString());
            } else {
                pb = new ProcessBuilder(pathExe.toString());
            }
            try {
                Process process = pb.start();
                process.waitFor();
            } catch (Exception e) {
                logger.error("-->errore ", e);
                MessageDialog dialog = new MessageDialog("Errore nel lanciare teamviewer", e.getMessage());
                dialog.showDialog();
            }
        }
    }
}
