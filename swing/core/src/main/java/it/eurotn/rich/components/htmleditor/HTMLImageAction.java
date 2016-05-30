package it.eurotn.rich.components.htmleditor;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.axis.encoding.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

import net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction;

public class HTMLImageAction extends HTMLTextEditAction {

    public class ImageFileFilter extends FileFilter implements java.io.FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }

            String ext = FilenameUtils.getExtension(file.getAbsolutePath());
            if (StringUtils.indexOfAny(ext, new String[] { "jpeg", "jpg", "bmp", "gif", "png" }) == 0) {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "Immagini disponibili";
        }

    }

    private static final Logger LOGGER = Logger.getLogger(HTMLImageAction.class);

    private static final long serialVersionUID = 1L;

    /**
     * Costruttore.
     */
    public HTMLImageAction() {
        super("Immagine");
        putValue("SmallIcon", RcpSupport.getIcon("image"));

        putValue("ShortDescription", "Immagine");
    }

    /**
     * Esegue l'encode 64 dell'immagine.
     *
     * @param imageFile
     *            file dell'immagine
     * @return rappresentazione base64
     */
    public static String encodeImageToString(File imageFile) {

        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            BufferedImage img = ImageIO.read(imageFile);
            ImageIO.write(img, FilenameUtils.getExtension(imageFile.getAbsolutePath()), bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            LOGGER.error("-->errore durante la conversione dell'immagine in base64", e);
        }
        return imageString;
    }

    /**
     * Restituisce il file dell'immagine da inserire.
     *
     * @return file dell'immagine, <code>null</code> se non è stata selezionata
     */
    private File getImageFile() {
        File imageFile = null;

        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ImageFileFilter());
        int res = fc.showOpenDialog(null);
        try {
            if (res == JFileChooser.APPROVE_OPTION) {
                imageFile = fc.getSelectedFile();
            }
        } catch (Exception e) {
            LOGGER.error("-->errore durante la selezione dell'immagine", e);
        }
        return imageFile;
    }

    @Override
    protected void sourceEditPerformed(ActionEvent event, JEditorPane editor) {
        editor.replaceSelection("<br>\n");
    }

    @Override
    protected void wysiwygEditPerformed(ActionEvent event, JEditorPane editor) {
        File imageFile = getImageFile();

        if (imageFile != null) {
            String imageFileExt = FilenameUtils.getExtension(imageFile.getAbsolutePath());

            StringBuilder sb = new StringBuilder(300);
            sb.append("<img src=\"data:image/");
            sb.append(imageFileExt);
            sb.append(";base64,");
            sb.append(encodeImageToString(imageFile));
            sb.append("\">");

            HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
            HTMLDocument doc = (HTMLDocument) editor.getDocument();
            try {
                kit.insertHTML(doc, editor.getCaretPosition(), sb.toString(), 0, 0, HTML.Tag.IMG);
            } catch (Exception ex) {
                LOGGER.error("-->errore durante l'inserimento del tag IMG", ex);
            }
        }
    }
}