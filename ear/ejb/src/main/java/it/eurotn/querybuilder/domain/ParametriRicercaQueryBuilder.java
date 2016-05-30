package it.eurotn.querybuilder.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;

@Entity
@Table(name = "para_ricerca_query_builder")
public class ParametriRicercaQueryBuilder extends AbstractParametriRicerca {

    private static final long serialVersionUID = -984260837237547063L;

    @Column(length = 4000)
    private String xmlProperties;

    @Column(length = 200)
    private String managedClassName;

    // Proprietà transiente utilizzata sul salvataggio per generare l'xml delle proprietà
    @Transient
    private List<ProprietaEntity> proprietaDaSalvare;

    /**
     * @return the managedClassName
     */
    public String getManagedClassName() {
        return managedClassName;
    }

    /**
     * @return the proprietaDaSalvare
     */
    public List<ProprietaEntity> getProprietaDaSalvare() {
        return proprietaDaSalvare;
    }

    /**
     * @return the xmlProperties
     */
    public String getXmlProperties() {
        return xmlProperties;
    }

    /**
     * @param managedClassName
     *            the managedClassName to set
     */
    public void setManagedClassName(String managedClassName) {
        this.managedClassName = managedClassName;
    }

    /**
     * @param proprietaDaSalvare
     *            the proprietaDaSalvare to set
     */
    public void setProprietaDaSalvare(List<ProprietaEntity> proprietaDaSalvare) {
        this.proprietaDaSalvare = proprietaDaSalvare;
    }

    /**
     * @param xmlProperties
     *            the xmlProperties to set
     */
    public void setXmlProperties(String xmlProperties) {
        this.xmlProperties = xmlProperties;
    }

}
