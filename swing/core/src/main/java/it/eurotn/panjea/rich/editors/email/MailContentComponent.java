package it.eurotn.panjea.rich.editors.email;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideScrollPane;

import it.eurotn.panjea.rich.bd.DmsBD;
import it.eurotn.panjea.rich.bd.IDmsBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;

public class MailContentComponent extends JPanel {

    private class OpenMailCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public OpenMailCommand() {
            super("OpenMailCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            if (emlFileContent != null && Desktop.isDesktopSupported()) {
                try {
                    Path tempFile = Files.createTempFile("email", ".eml");
                    tempFile = Files.write(tempFile, emlFileContent, StandardOpenOption.WRITE);
                    Desktop.getDesktop().open(tempFile.toFile());
                } catch (Exception e1) {
                    LOGGER.error("--> errore durante il downlad dell'allegato eml.", e1);
                    new MessageDialog("Attenzione", e1.getMessage()).showDialog();
                } finally {
                    PanjeaSwingUtil.unlockScreen();
                }
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
        }

    }

    private static final Logger LOGGER = Logger.getLogger(MailContentComponent.class);

    private static final long serialVersionUID = 6101850916715816988L;

    private IDmsBD dmsBD;

    private HTMLEditorPane contentEditorText;
    private JLabel daLabel;
    private JLabel alabel;
    private JLabel oggettoLabel;

    private AllegatiMailComponent allegatiMailComponent;

    private OpenMailCommand openMailCommand;

    private long documentId;

    private MimeMessage mimeMessage;

    private byte[] emlFileContent;

    /**
     * Costruttore.
     */
    public MailContentComponent() {
        super(new BorderLayout());
        this.dmsBD = RcpSupport.getBean(DmsBD.BEAN_ID);
        GuiStandardUtils.attachBorder(this);

        initControl();
    }

    private void initControl() {
        FormLayout layout = new FormLayout("right:p,4dlu,fill:250dlu:grow,right:p",
                "p,4dlu,p,4dlu,p,4dlu,fill:p:grow,4dlu,fill:50dlu,4dlu");
        PanelBuilder builder = new PanelBuilder(layout);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Da", cc.xy(1, 1));
        daLabel = new JLabel();
        Font font = daLabel.getFont();
        daLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        builder.add(daLabel, cc.xy(3, 1));
        openMailCommand = new OpenMailCommand();
        builder.add(openMailCommand.createButton(), cc.xywh(4, 1, 1, 5));

        builder.addLabel("A", cc.xy(1, 3));
        alabel = new JLabel();
        alabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        builder.add(alabel, cc.xyw(3, 3, 2));

        builder.addLabel("Oggetto", cc.xy(1, 5));
        oggettoLabel = new JLabel();
        oggettoLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        builder.add(oggettoLabel, cc.xyw(3, 5, 2));

        contentEditorText = new HTMLEditorPane();
        contentEditorText.setReadOnly(true);
        contentEditorText.getMenuBar().setVisible(false);
        contentEditorText.getFormatToolBar().setVisible(false);
        builder.add(contentEditorText, cc.xyw(1, 7, 4));

        allegatiMailComponent = new AllegatiMailComponent();
        builder.add(new JideScrollPane(allegatiMailComponent), cc.xyw(1, 9, 4));

        add(builder.getPanel(), BorderLayout.CENTER);
    }

    /**
     * Carica il contenuto della mail e lo visualizza.
     *
     * @param id
     *            id documento
     */
    public void load(Long id) {
        reset();

        if (id == null) {
            return;
        }

        this.documentId = id;

        emlFileContent = dmsBD.getContentByte(documentId);
        try {
            mimeMessage = new MimeMessage(null, new ByteArrayInputStream(emlFileContent));

            openMailCommand.setEnabled(true);

            daLabel.setText(((InternetAddress) mimeMessage.getFrom()[0]).getAddress());
            alabel.setText(((InternetAddress) mimeMessage.getRecipients(RecipientType.TO)[0]).getAddress());
            oggettoLabel.setText(mimeMessage.getSubject());

            MimeMultipart content = (MimeMultipart) mimeMessage.getContent();
            contentEditorText.setText((String) content.getBodyPart(0).getContent());

            allegatiMailComponent.load(mimeMessage);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del testo della mail.", e);
            contentEditorText.setText("Errore durante il caricamento del testo della mail.");
        }
    }

    private void reset() {
        mimeMessage = null;

        openMailCommand.setEnabled(false);

        daLabel.setText("");
        alabel.setText("");
        oggettoLabel.setText("");

        contentEditorText.setText("");

        allegatiMailComponent.load(mimeMessage);
    }

}
