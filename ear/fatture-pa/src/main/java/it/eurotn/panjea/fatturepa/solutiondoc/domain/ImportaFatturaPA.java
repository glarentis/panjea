package it.eurotn.panjea.fatturepa.solutiondoc.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImportaFatturaPA", propOrder = { "documenti" })
public class ImportaFatturaPA {

    @XmlElement(name = "Documento", required = true)
    private List<Documento> documenti;

    {
        documenti = new ArrayList<Documento>();
    }

    /**
     * @return the documenti
     */
    public List<Documento> getDocumenti() {
        return documenti;
    }

    /**
     * @param documenti
     *            the documenti to set
     */
    public void setDocumenti(List<Documento> documenti) {
        this.documenti = documenti;
    }

}
