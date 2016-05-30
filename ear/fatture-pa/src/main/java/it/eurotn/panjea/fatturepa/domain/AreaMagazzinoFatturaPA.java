package it.eurotn.panjea.fatturepa.domain;

import java.math.BigInteger;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;

@Entity
@Audited
@Table(name = "ftpa_area_magazzino")
@NamedQueries({
        @NamedQuery(name = "AreaMagazzinoFatturaPA.caricaByIdAreaMagazzino", query = "select amfpa from AreaMagazzinoFatturaPA amfpa where amfpa.areaMagazzino.id = :paramIdAreaMagazzino") })
public class AreaMagazzinoFatturaPA extends EntityBase {

    private static final long serialVersionUID = -8060822796706502827L;

    @ManyToOne
    private AreaMagazzinoLite areaMagazzino;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "xmlFattura", column = @Column(name = "xmlFattura") ),
            @AttributeOverride(name = "progressivoInvio", column = @Column(name = "progressivoInvio", length = 5) ),
            @AttributeOverride(name = "xmlFileName", column = @Column(name = "xmlFileName", length = 30) ) })
    private XMLFatturaPA xmlFattura;

    /**
     * Denormalizzo ma mi è utile in tutte le query per filtrare tutti i documenti per notifica fattura. Rappresenta
     * l'ultima notifica temporale ricevuta.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private NotificaFatturaPA notificaCorrente;

    private BigInteger identificativoSDI;

    private boolean conservazioneSostitutivaEseguita;

    {
        xmlFattura = new XMLFatturaPA();
        conservazioneSostitutivaEseguita = false;
    }

    /**
     * @return the areaMagazzino
     */
    public AreaMagazzinoLite getAreaMagazzino() {
        return areaMagazzino;
    }

    /**
     * @return the identificativoSDI
     */
    public BigInteger getIdentificativoSDI() {
        return identificativoSDI;
    }

    /**
     * @return the notificaCorrente
     */
    public NotificaFatturaPA getNotificaCorrente() {
        return notificaCorrente;
    }

    /**
     * @return the xmlFattura
     */
    public XMLFatturaPA getXmlFattura() {
        if (xmlFattura == null) {
            xmlFattura = new XMLFatturaPA();
        }
        return xmlFattura;
    }

    /**
     * @return the conservazioneSostitutivaEseguita
     */
    public boolean isConservazioneSostitutivaEseguita() {
        return conservazioneSostitutivaEseguita;
    }

    /**
     * @param areaMagazzino
     *            the areaMagazzino to set
     */
    public void setAreaMagazzino(AreaMagazzinoLite areaMagazzino) {
        this.areaMagazzino = areaMagazzino;
    }

    /**
     * @param conservazioneSostitutivaEseguita
     *            the conservazioneSostitutivaEseguita to set
     */
    public void setConservazioneSostitutivaEseguita(boolean conservazioneSostitutivaEseguita) {
        this.conservazioneSostitutivaEseguita = conservazioneSostitutivaEseguita;
    }

    /**
     * @param identificativoSDI
     *            the identificativoSDI to set
     */
    public void setIdentificativoSDI(BigInteger identificativoSDI) {
        this.identificativoSDI = identificativoSDI;
    }

    /**
     * @param notificaCorrente
     *            the notificaCorrente to set
     */
    public void setNotificaCorrente(NotificaFatturaPA notificaCorrente) {
        this.notificaCorrente = notificaCorrente;
    }

    /**
     * @param xmlFattura
     *            the xmlFattura to set
     */
    public void setXmlFattura(XMLFatturaPA xmlFattura) {
        this.xmlFattura = xmlFattura;
    }

}
