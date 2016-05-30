package it.eurotn.panjea.fatturepa.solutiondoc.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Documento", propOrder = { "tipo", "nomeFile", "idSdi", "data" })
public class Documento {

    @XmlElement(name = "Tipo", required = true)
    private String tipo;

    @XmlElement(name = "NomeFile", required = true)
    private String nomeFile;

    @XmlElement(name = "IdSdi", required = true)
    private String idSdi;

    @XmlElement(name = "DataSdi")
    protected Date data;

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the idSdi
     */
    public String getIdSdi() {
        return idSdi;
    }

    /**
     * @return the nomeFile
     */
    public String getNomeFile() {
        return nomeFile;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param idSdi
     *            the idSdi to set
     */
    public void setIdSdi(String idSdi) {
        this.idSdi = idSdi;
    }

    /**
     * @param nomeFile
     *            the nomeFile to set
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    /**
     * @param tipo
     *            the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
