package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.AreaRate;

/**
 * DTO per il trasporto in una sola chiamata di {@link AreaMagazzino} , {@link AreaIva} ,
 * {@link AreaPartite} , {@link AreaContabileLite} .
 *
 * @author adriano
 * @version 1.0, 29/ago/2008
 */
@AuditableProperties(properties = { "areaMagazzino", "areaMagazzino.documento", "areaRate", "areaIva" })
public class AreaMagazzinoFullDTO implements Serializable, IDefProperty {

    private static final long serialVersionUID = -7565477996495638064L;

    private AreaMagazzino areaMagazzino;

    private AreaRate areaRate;

    private AreaIva areaIva;

    private AreaContabileLite areaContabileLite;

    private List<RigaIva> riepilogoIva;

    /**
     * Utilizzate e riempite solamente per la stampa del report.
     */
    private List<RigaMagazzino> righeMagazzino;

    /**
     * Agenti raggruppati nelle righe di magazzino.
     */
    private List<AgenteLite> agenti;

    /**
     * indicatore da utilizzare esclusivamente lato presentazione, per eseguire i controlli sui
     * componenti legati ad {@link AreaRate} <br>
     * e per la loro validazione. <br>
     * Viene valorizzato al momento del caricamento di {@link AreaMagazzinoFullDTO} controllando che
     * l'attributo {@link AreaRate} <br>
     * non sia istanziato vuoto
     */
    private boolean areaRateEnabled = false;

    private boolean areaContabileEnabled = false;

    /**
     * Viene mappata come object perchè è su un plugin a parte e il crud viene gestito tramite
     * interceptor.
     */
    private Object areaRifornimento = null;

    {
        areaMagazzino = new AreaMagazzino();
        areaRate = new AreaRate();
        areaIva = new AreaIva();
        righeMagazzino = new ArrayList<RigaMagazzino>();
        TipoAreaMagazzino tam = new TipoAreaMagazzino();
        tam.setGestioneVending(false);
        areaMagazzino.setTipoAreaMagazzino(tam);
    }

    /**
     * Costruttore.
     */
    public AreaMagazzinoFullDTO() {
        super();
    }

    /**
     * Equals basato sull'id dell'area Magazzino.<br/>
     * <b>NB.</b>Anche se viene testato con un {@link AreaMagazzino} torna true
     *
     * @param obj
     *            oggetto da testare
     * @return true se gli Id degli oggetti sono uguali
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass() && !obj.getClass().getName().equals(AreaMagazzino.class.getName())) {
            return false;
        }

        AreaMagazzino areaMagazzinoEquals = null;
        if (obj.getClass() == AreaMagazzino.class) {
            areaMagazzinoEquals = (AreaMagazzino) obj;
        } else {
            areaMagazzinoEquals = ((AreaMagazzinoFullDTO) obj).getAreaMagazzino();
        }

        if (areaMagazzinoEquals == null) {
            return false;
        } else if (!areaMagazzinoEquals.equals(areaMagazzino)) {
            return false;
        }
        return true;
    }

    /**
     * @return Returns the agenti.
     */
    public List<AgenteLite> getAgenti() {
        return agenti;
    }

    /**
     * @return Returns the areaContabileLite.
     */
    public AreaContabileLite getAreaContabileLite() {
        return areaContabileLite;
    }

    /**
     * @return Returns the areaIva.
     */
    public AreaIva getAreaIva() {
        return areaIva;
    }

    /**
     * @return Returns the areaMagazzino.
     */
    public AreaMagazzino getAreaMagazzino() {
        return areaMagazzino;
    }

    /**
     * @return the areaRate
     */
    public AreaRate getAreaRate() {
        return areaRate;
    }

    /**
     * @return the areaRifornimento
     */
    public final Object getAreaRifornimento() {
        return areaRifornimento;
    }

    @Override
    public String getDomainClassName() {
        return areaMagazzino.getDomainClassName();
    }

    @Override
    public Integer getId() {
        return areaMagazzino.getId();
    }

    /**
     * @return riepilogo iva
     */
    public List<RigaIva> getRiepilogoIva() {
        return riepilogoIva;
    }

    /**
     * @return Returns the righeMagazzino.
     */
    public List<RigaMagazzino> getRigheMagazzino() {
        return righeMagazzino;
    }

    @Override
    public Integer getVersion() {
        return areaMagazzino.getVersion();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areaMagazzino.getId() == null) ? 0 : areaMagazzino.getId().hashCode());
        return result;
    }

    /**
     * Restituisce una nuova {@link AreaMagazzinoFullDTO} inizializzata dai valori della area
     * magazzino corrente. La data viene reimpostata nel createNewObject del form.
     *
     * @param areaMagazzinoFullDTO
     *            area magazzino di riferimento
     *
     */
    public void initializedNewObject(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
        if (areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino() != null
                && areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino().getDepositoOrigine() != null) {
            areaMagazzino.setDepositoOrigine(
                    areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino().getDepositoOrigine());
        } else {
            areaMagazzino.setDepositoOrigine(areaMagazzinoFullDTO.getAreaMagazzino().getDepositoOrigine());
        }
        areaMagazzino.setTipoAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino());
        areaRateEnabled = (areaMagazzinoFullDTO.isAreaRateEnabled());
    }

    /**
     * @return the areaContabileEnabled
     */
    public boolean isAreaContabileEnabled() {
        return areaContabileEnabled;
    }

    /**
     * @return Returns the areaArateEnabled.
     */
    public boolean isAreaRateEnabled() {
        return areaRateEnabled;
    }

    @Override
    public boolean isNew() {
        return areaMagazzino.isNew();
    }

    /**
     * @param agenti
     *            The agenti to set.
     */
    public void setAgenti(List<AgenteLite> agenti) {
        this.agenti = agenti;
    }

    /**
     * @param areaContabileEnabled
     *            the areaContabileEnabled to set
     */
    public void setAreaContabileEnabled(boolean areaContabileEnabled) {
        this.areaContabileEnabled = areaContabileEnabled;
    }

    /**
     * @param areaContabileLite
     *            The areaContabileLite to set.
     */
    public void setAreaContabileLite(AreaContabileLite areaContabileLite) {
        this.areaContabileLite = areaContabileLite;
    }

    /**
     * @param areaIva
     *            The areaIva to set.
     */
    public void setAreaIva(AreaIva areaIva) {
        this.areaIva = areaIva;
    }

    /**
     * @param areaMagazzino
     *            The areaMagazzino to set.
     */
    public void setAreaMagazzino(AreaMagazzino areaMagazzino) {
        this.areaMagazzino = areaMagazzino;
    }

    /**
     * @param areaRate
     *            the areaRate to set
     */
    public void setAreaRate(AreaRate areaRate) {
        this.areaRate = areaRate;
    }

    /**
     * @param areaRateEnabled
     *            The areaPartiteEnabled to set.
     */
    public void setAreaRateEnabled(boolean areaRateEnabled) {
        this.areaRateEnabled = areaRateEnabled;
    }

    /**
     * @param areaRifornimento
     *            the areaRifornimento to set
     */
    public final void setAreaRifornimento(Object areaRifornimento) {
        this.areaRifornimento = areaRifornimento;
    }

    /**
     * @param riepilogoIva
     *            the riepilogoIva to set
     */
    public void setRiepilogoIva(List<RigaIva> riepilogoIva) {
        this.riepilogoIva = riepilogoIva;
    }

    /**
     * @param righeMagazzino
     *            The righeMagazzino to set.
     */
    public void setRigheMagazzino(List<RigaMagazzino> righeMagazzino) {
        this.righeMagazzino = righeMagazzino;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome
     * attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {
        StringBuilder retValue = new StringBuilder();
        retValue.append("AreaMagazzinoFullDTO[ ").append(super.toString()).append(" areaMagazzino = ")
                .append(this.areaMagazzino != null ? this.areaMagazzino.getId() : null).append(" areaPartite = ")
                .append(this.areaRate != null ? this.areaRate.getId() : null).append(" areaIva = ")
                .append(this.areaIva != null ? this.areaIva.getId() : null).append(" ]");

        return retValue.toString();
    }

}
