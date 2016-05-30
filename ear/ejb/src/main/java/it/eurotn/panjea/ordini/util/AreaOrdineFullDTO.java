package it.eurotn.panjea.ordini.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.rate.domain.AreaRate;

/**
 * Classe che raggruppa un ordine.
 *
 * @author giangi
 */
@AuditableProperties(properties = { "areaOrdine", "areaOrdine.documento", "areaRate" })
public class AreaOrdineFullDTO implements Serializable, IDefProperty {
    private static final long serialVersionUID = -4400822364158585122L;

    private AreaOrdine areaOrdine;
    private AreaRate areaRate;
    /**
     * Viene mappata come object perchè è su un plugin a parte e il crud viene gestito tramite
     * interceptor.
     */
    private Object areaRifornimento = null;

    private List<RigaOrdine> righeOrdine;

    private List<FaseLavorazioneArticolo> fasiLavorazione;

    /**
     * indicatore da utilizzare esclusivamente lato presentazione, per eseguire i controlli sui
     * componenti legati ad {@link AreaRate} <br>
     * e per la loro validazione. <br>
     * Viene valorizzato al momento del caricamento di {@link AreaMagazzinoFullDTO} controllando che
     * l'attributo {@link AreaRate} <br>
     * non sia istanziato vuoto
     */
    private boolean areaRateEnabled = false;
    private boolean inserimentoRigheMassivo = false;

    {
        areaOrdine = new AreaOrdine();
        areaRate = new AreaRate();
        righeOrdine = new ArrayList<RigaOrdine>();
        fasiLavorazione = new ArrayList<FaseLavorazioneArticolo>();
    }

    /**
     * Costruttore.
     */
    public AreaOrdineFullDTO() {
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
        if (getClass() != obj.getClass() && !obj.getClass().getName().equals(AreaOrdine.class.getName())) {
            return false;
        }

        AreaOrdine areaOrdineEquals = null;
        if (obj.getClass() == AreaOrdine.class) {
            areaOrdineEquals = (AreaOrdine) obj;
        } else {
            areaOrdineEquals = ((AreaOrdineFullDTO) obj).getAreaOrdine();
        }

        if (areaOrdineEquals == null) {
            return false;
        } else if (!areaOrdineEquals.equals(areaOrdine)) {
            return false;
        }
        return true;
    }

    /**
     * @return the areaOrdine
     */
    public AreaOrdine getAreaOrdine() {
        return areaOrdine;
    }

    /**
     * @return the areaRate
     */
    public AreaRate getAreaRate() {
        return areaRate;
    }

    /**
     * @return Returns the areaRifornimento.
     */
    public Object getAreaRifornimento() {
        return areaRifornimento;
    }

    @Override
    public String getDomainClassName() {
        return areaOrdine.getDomainClassName();
    }

    /**
     * @return the fasiLavorazione
     */
    public List<FaseLavorazioneArticolo> getFasiLavorazione() {
        return fasiLavorazione;
    }

    @Override
    public Integer getId() {
        return areaOrdine.getId();
    }

    /**
     * Restituisce una nuova {@link AreaOrdine} inizializzata dai valori della area ordine corrente.
     *
     * @return {@link AreaOrdine}
     */
    public AreaOrdineFullDTO getInitializedNewObject() {
        AreaOrdine areaOrdineNew = new AreaOrdine();
        areaOrdineNew.setTipoAreaOrdine(this.areaOrdine.getTipoAreaOrdine());
        areaOrdineNew.setDepositoOrigine(this.areaOrdine.getDepositoOrigine());
        areaOrdineNew.setDataRegistrazione(this.areaOrdine.getDataRegistrazione());

        AreaOrdineFullDTO areaOrdineFullDTO = new AreaOrdineFullDTO();
        areaOrdineFullDTO.setAreaOrdine(areaOrdineNew);
        areaOrdineFullDTO.setAreaRateEnabled(isAreaRateEnabled());

        return areaOrdineFullDTO;
    }

    /**
     * @return righeOrdine
     */
    public List<RigaOrdine> getRigheOrdine() {
        return righeOrdine;
    }

    @Override
    public Integer getVersion() {
        return areaOrdine.getVersion();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areaOrdine.getId() == null) ? 0 : areaOrdine.getId().hashCode());
        return result;
    }

    /**
     * @return Returns the areaArateEnabled.
     */
    public boolean isAreaRateEnabled() {
        return areaRateEnabled;
    }

    /**
     * @return true se i dati di importazione delle righe e della testata sono coerenti con i dati
     *         determinati per l'entità.
     */
    public boolean isDatiImportazioneCoerenti() {
        if (!getAreaOrdine().isDatiImportazioneCoerenti()) {
            return false;
        }

        for (RigaOrdine rigaOrdine : getRigheOrdine()) {
            if (rigaOrdine instanceof RigaArticolo && !((RigaArticolo) rigaOrdine).isDatiImportazioneCoerenti()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the inserimentoRigheMassivo
     */
    public boolean isInserimentoRigheMassivo() {
        return inserimentoRigheMassivo;
    }

    @Override
    public boolean isNew() {
        return areaOrdine.isNew();
    }

    /**
     * @param areaOrdine
     *            the areaOrdine to set
     */
    public void setAreaOrdine(AreaOrdine areaOrdine) {
        this.areaOrdine = areaOrdine;
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
     *            The areaRifornimento to set.
     */
    public void setAreaRifornimento(Object areaRifornimento) {
        this.areaRifornimento = areaRifornimento;
    }

    /**
     * @param fasiLavorazione
     *            the fasiLavorazione to set
     */
    public void setFasiLavorazione(List<FaseLavorazioneArticolo> fasiLavorazione) {
        this.fasiLavorazione = fasiLavorazione;
    }

    /**
     * @param inserimentoRigheMassivo
     *            the inserimentoRigheMassivo to set
     */
    public void setInserimentoRigheMassivo(boolean inserimentoRigheMassivo) {
        this.inserimentoRigheMassivo = inserimentoRigheMassivo;
    }

    /**
     * @param righeOrdine
     *            the righeOrdine to set
     */
    public void setRigheOrdine(List<RigaOrdine> righeOrdine) {
        this.righeOrdine = righeOrdine;
    }

}
