package it.eurotn.panjea.anagrafica.domain.lite;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;

/**
 * Oggetto di dominio Lite dell'oggetto {@link Anagrafica}.
 *
 * @author adriano
 * @version 1.0, 05/giu/07
 */
@Entity
@Audited
@Table(name = "anag_anagrafica")
public class AnagraficaLite extends EntityBase {

  private static Logger logger = Logger.getLogger(AnagraficaLite.class);
  private static final long serialVersionUID = 410643567796683555L;

  @Index(name = "azienda")
  @Column(name = "codice_azienda", length = 20)
  private java.lang.String codiceAzienda;

  @Column(name = "denominazione", length = 100)
  private java.lang.String denominazione;

  @Column(name = "partita_iva", length = 25)
  private java.lang.String partiteIVA;

  @Column(name = "codice_fiscale", length = 25)
  private java.lang.String codiceFiscale;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinColumn(name = "sede_anagrafica_id")
  private SedeAnagrafica sedeAnagrafica;

  @Transient
  private String codice;

  /**
   * @return the codice
   */
  public String getCodice() {
    if (codice == null) {
      codice = codiceFiscale;
      if (partiteIVA != null) {
        codice = partiteIVA;
      }
    }
    return codice;
  }

  /**
   * @return Returns the codiceAzienda.
   */
  public java.lang.String getCodiceAzienda() {
    return codiceAzienda;
  }

  /**
   * @return Returns the codiceFiscale.
   */
  public java.lang.String getCodiceFiscale() {
    return codiceFiscale;
  }

  /**
   * @return Returns the denominazione.
   * @uml.property name="denominazione"
   */
  public java.lang.String getDenominazione() {
    return denominazione;
  }

  /**
   * @return Returns the partiteIVA.
   * @uml.property name="partiteIVA"
   */
  public java.lang.String getPartiteIVA() {
    return partiteIVA;
  }

  /**
   * @return partita iva intra.
   */
  public String getPartiteIVAIntra() {
    String partitaIvaIntra = partiteIVA;

    try {
      if (getSedeAnagrafica() != null && getSedeAnagrafica().getDatiGeografici() != null
          && getSedeAnagrafica().getDatiGeografici().getNazione() != null) {
        Nazione nazione = getSedeAnagrafica().getDatiGeografici().getNazione();
        if (!"IT".equalsIgnoreCase(nazione.getCodice()) && nazione.isIntra()) {
          partitaIvaIntra = nazione.getCodice() + partiteIVA;
        }
      }
    } catch (Exception e) {
      logger.error(
          "-->errore nel caricare la partitaIvaIntra. Restituirsco la piva dell'anagrafica", e);
      partitaIvaIntra = partiteIVA;
    }

    return partitaIvaIntra;
  }

  /**
   * @return the sedeAnagrafica
   * @uml.property name="sedeAnagrafica"
   */
  public SedeAnagrafica getSedeAnagrafica() {
    return sedeAnagrafica;
  }

  /**
   * @param codice
   *          the codice to set
   */
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   * @param codiceAzienda
   *          The codiceAzienda to set.
   * @uml.property name="codiceAzienda"
   */
  public void setCodiceAzienda(java.lang.String codiceAzienda) {
    this.codiceAzienda = codiceAzienda;
  }

  /**
   * @param codiceFiscale
   *          The codiceFiscale to set.
   * @uml.property name="codiceFiscale"
   */
  public void setCodiceFiscale(java.lang.String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
    codice = null;
  }

  /**
   * @param denominazione
   *          The denominazione to set.
   * @uml.property name="denominazione"
   */
  public void setDenominazione(java.lang.String denominazione) {
    this.denominazione = denominazione;
  }

  /**
   * @param partiteIVA
   *          The partiteIVA to set.
   * @uml.property name="partiteIVA"
   */
  public void setPartiteIVA(java.lang.String partiteIVA) {
    this.partiteIVA = partiteIVA;
    codice = null;
  }

  /**
   * @param sedeAnagrafica
   *          the sedeAnagrafica to set
   * @uml.property name="sedeAnagrafica"
   */
  public void setSedeAnagrafica(SedeAnagrafica sedeAnagrafica) {
    this.sedeAnagrafica = sedeAnagrafica;
  }

}
