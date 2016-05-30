package it.eurotn.panjea.mail;

import java.io.Serializable;

import org.apache.commons.io.FilenameUtils;

public class Attachment implements Serializable {

    private static final long serialVersionUID = -5041305227072538175L;

    private byte[] content;

    private String name;

    private String extension;

    /**
     * Costruttore.
     *
     * @param content
     *            content
     * @param name
     *            nome
     */
    public Attachment(byte[] content, String name) {
        super();
        this.content = content;
        this.name = name;
        this.extension = FilenameUtils.getExtension(name);
        if (extension.isEmpty()) {
            this.extension = "txt";
        }
    }

    /**
     * @return the content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}
