package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import org.apache.commons.lang3.StringUtils;

@Embeddable
public class XMLFatturaPA implements Serializable {

    private static final long serialVersionUID = 8662421412587633515L;

    /**
     * Contiene l'XML creato con tutti i dati inseriti dall'utente. La differenza con la proprietà xmlFattura stà nel
     * fatto che quando l'utente cancella l'xml questo viene mantenuto e quando successivamente viene ricreato verranno
     * utilizzati i suoi dati per avvalorare tutti i campi non obbligatori che sono presenti. Questo è utile ad esempio
     * per tutti i blocchi ( Causali, bollo virtuale, dati contratto, ecc...) che altrimenti dovrebbero essere
     * reinseriti ogni volta che si cancella l'xml.
     */
    @Lob
    private String xmlFatturaOriginale;

    @Lob
    private String xmlFattura;

    @Column(length = 5)
    private String progressivoInvio;

    @Column(length = 30)
    private String xmlFileName;

    private boolean xmlFirmato;

    @Column(length = 60)
    private String xmlFileNameFirmato;

    {
        xmlFirmato = false;
    }

    /**
     * @return the progressivoInvio
     */
    public String getProgressivoInvio() {
        return progressivoInvio;
    }

    /**
     * @return the xmlFattura
     */
    public String getXmlFattura() {
        return xmlFattura;
    }

    /**
     * @return the xmlFatturaOriginale
     */
    public String getXmlFatturaOriginale() {
        return xmlFatturaOriginale;
    }

    /**
     * @return the xmlFileName
     */
    public String getXmlFileName() {
        return xmlFileName;
    }

    /**
     * @return the xmlFileNameFirmato
     */
    public String getXmlFileNameFirmato() {
        return xmlFileNameFirmato;
    }

    /**
     * @return <code>true</code> se il file xml è presente
     */
    public boolean isPresent() {
        return !StringUtils.isBlank(xmlFileName);
    }

    /**
     * @return the xmlFirmato
     */
    public boolean isXmlFirmato() {
        return xmlFirmato;
    }

    /**
     * @param progressivoInvio
     *            the progressivoInvio to set
     */
    public void setProgressivoInvio(String progressivoInvio) {
        this.progressivoInvio = progressivoInvio;
    }

    /**
     * @param xmlFattura
     *            the xmlFattura to set
     */
    public void setXmlFattura(String xmlFattura) {
        this.xmlFattura = xmlFattura;
    }

    /**
     * @param xmlFatturaOriginale
     *            the xmlFatturaOriginale to set
     */
    public void setXmlFatturaOriginale(String xmlFatturaOriginale) {
        this.xmlFatturaOriginale = xmlFatturaOriginale;
    }

    /**
     * @param xmlFileName
     *            the xmlFileName to set
     */
    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    /**
     * @param xmlFileNameFirmato
     *            the xmlFileNameFirmato to set
     */
    public void setXmlFileNameFirmato(String xmlFileNameFirmato) {
        this.xmlFileNameFirmato = xmlFileNameFirmato;
    }

    /**
     * @param xmlFirmato
     *            the xmlFirmato to set
     */
    public void setXmlFirmato(boolean xmlFirmato) {
        this.xmlFirmato = xmlFirmato;
    }
}
