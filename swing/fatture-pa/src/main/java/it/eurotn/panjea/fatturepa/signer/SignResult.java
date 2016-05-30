package it.eurotn.panjea.fatturepa.signer;

public class SignResult {

    private String fileToSign;

    private String fileSigned;

    private String signLog;

    /**
     * Costruttore.
     *
     */
    public SignResult() {
        super();
    }

    /**
     * @return the fileSigned
     */
    public String getFileSigned() {
        return fileSigned;
    }

    /**
     * @return the fileToSign
     */
    public String getFileToSign() {
        return fileToSign;
    }

    /**
     * @return the signLog
     */
    public String getSignLog() {
        return signLog;
    }

    /**
     * @param fileSigned
     *            the fileSigned to set
     */
    public void setFileSigned(String fileSigned) {
        this.fileSigned = fileSigned;
    }

    /**
     * @param fileToSign
     *            the fileToSign to set
     */
    public void setFileToSign(String fileToSign) {
        this.fileToSign = fileToSign;
    }

    /**
     * @param signLog
     *            the signLog to set
     */
    public void setSignLog(String signLog) {
        this.signLog = signLog;
    }

}
