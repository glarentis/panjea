package it.eurotn.panjea.rich.editors.email;

import java.awt.Desktop;
import java.awt.FlowLayout;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

import javax.mail.internet.MimeMessage;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.mail.Attachment;
import it.eurotn.panjea.mail.EmailMessage;
import it.eurotn.panjea.mail.exception.EmailException;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class AllegatiMailComponent extends JPanel {

    private class AttachmentCommand extends ActionCommand {

        private Attachment attachment;

        /**
         * Costruttore.
         *
         * @param attachment
         *            allegato
         */
        public AttachmentCommand(final Attachment attachment) {
            super();
            this.attachment = attachment;
        }

        @Override
        protected void doExecuteCommand() {
            if (Desktop.isDesktopSupported()) {
                try {
                    PanjeaSwingUtil.lockScreen("Download in corso");
                    Path tempFile = Files.createTempFile(FilenameUtils.getBaseName(attachment.getName()),
                            "." + attachment.getExtension());
                    tempFile = Files.write(tempFile, attachment.getContent(), StandardOpenOption.WRITE);
                    Desktop.getDesktop().open(tempFile.toFile());
                } catch (Exception e1) {
                    LOGGER.error("--> errore durante il downlad dell'allegato.", e1);
                    new MessageDialog("Attenzione", e1.getMessage()).showDialog();
                } finally {
                    PanjeaSwingUtil.unlockScreen();
                }
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);

            button.setText(StringUtils.abbreviateMiddle(attachment.getName(), "...", 20));

            String ext = StringUtils.defaultIfBlank(attachment.getExtension(), "no");
            Icon icon = RcpSupport.getIcon(ext + "32");
            button.setIcon(ObjectUtils.defaultIfNull(icon, RcpSupport.getIcon("no32")));

            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
        }

    }

    private static final Logger LOGGER = Logger.getLogger(AllegatiMailComponent.class);

    private static final long serialVersionUID = -1563408325907740105L;

    /**
     * Costruttore.
     */
    public AllegatiMailComponent() {
        super(new FlowLayout(FlowLayout.LEFT, 10, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
    }

    /**
     * Visualizza tutti gli allegati presenti nel messaggio.
     *
     * @param message
     *            messagigo
     */
    public void load(MimeMessage message) {

        try {
            removeAll();
            loadAttachments(message);
        } catch (EmailException e) {
            LOGGER.error("--> errore durante il caricamento degli allegati", e);
        } finally {
            repaint();
        }
    }

    private void loadAttachments(MimeMessage message) throws EmailException {

        if (message != null) {
            EmailMessage emailMessage = new EmailMessage(message);
            Collection<Attachment> attachments = emailMessage.getAttachements();
            for (Attachment attachment : attachments) {
                add(new AttachmentCommand(attachment).createButton());
            }
        }
    }
}
