package it.eurotn.panjea.agenti.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.DatiRitenutaAccontoEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;

@Entity
@Audited
@DiscriminatorValue("A")
@NamedQueries({
        @NamedQuery(name = "Agente.caricaSenzaCapoarea", query = " select agente from Agente agente where agente.id not in (select agentiConCapo.id from Agente capoArea inner join capoArea.agenti agentiConCapo where capoArea.capoArea = true) and agente.capoArea = false ") })
public class Agente extends Entita {
    public enum TipoMandato {
        MONOMANDATARIO, PLURIMANDATARIO
    }

    private static final long serialVersionUID = -699663566604282962L;

    private static int ordine = 1;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
    @JoinColumn(name = "capoarea_id")
    @AuditJoinTable(name = "agente_agente_aud")
    private Set<Agente> agenti;

    private Boolean capoArea;

    private Boolean fatturazioneAgente;

    private TipoMandato tipoMandato;

    @Embedded
    private DatiRitenutaAccontoEntita datiRitenutaAcconto;

    /**
     * Costruttore.
     */
    public Agente() {
        super();
        capoArea = false;
        fatturazioneAgente = false;
        agenti = new HashSet<Agente>();
    }

    /**
     * @return Returns the agenti.
     */
    public Set<Agente> getAgenti() {
        return agenti;
    }

    /**
     * @return the datiRitenutaAcconto
     */
    public DatiRitenutaAccontoEntita getDatiRitenutaAcconto() {
        if (datiRitenutaAcconto == null) {
            datiRitenutaAcconto = new DatiRitenutaAccontoEntita();
        }
        return datiRitenutaAcconto;
    }

    @Override
    public int getOrdine() {
        return ordine;
    }

    @Override
    public TipoEntita getTipo() {
        return TipoEntita.AGENTE;
    }

    /**
     * @return the tipoMandato
     */
    public TipoMandato getTipoMandato() {
        return tipoMandato;
    }

    /**
     * @return Returns the capoArea.
     */
    public Boolean isCapoArea() {
        return capoArea;
    }

    /**
     * @return Returns the fatturazioneAgente.
     */
    public Boolean isFatturazioneAgente() {
        if (fatturazioneAgente == null) {
            return Boolean.FALSE;
        }
        return fatturazioneAgente;
    }

    /**
     * @param agenti
     *            The agenti to set.
     */
    public void setAgenti(Set<Agente> agenti) {
        this.agenti = agenti;

        if (isNew()) {
            return;
        }
        for (Agente agente : agenti) {
            // non so perchè mi toglie l'azienda quando salvo l'agente capo area. Boh...la risetto
            agente.getAnagrafica().setCodiceAzienda(this.getAnagrafica().getCodiceAzienda());
        }
    }

    /**
     * @param capoArea
     *            The capoArea to set.
     */
    public void setCapoArea(Boolean capoArea) {
        this.capoArea = capoArea;
        if (!capoArea) {
            agenti = new HashSet<Agente>();
        }
    }

    /**
     * @param datiRitenutaAcconto
     *            the datiRitenutaAcconto to set
     */
    public void setDatiRitenutaAcconto(DatiRitenutaAccontoEntita datiRitenutaAcconto) {
        this.datiRitenutaAcconto = datiRitenutaAcconto;
    }

    /**
     * @param fatturazioneAgente
     *            The fatturazioneAgente to set.
     */
    public void setFatturazioneAgente(Boolean fatturazioneAgente) {
        this.fatturazioneAgente = fatturazioneAgente;
    }

    /**
     * @param tipoMandato
     *            the tipoMandato to set
     */
    public void setTipoMandato(TipoMandato tipoMandato) {
        this.tipoMandato = tipoMandato;
    }
}