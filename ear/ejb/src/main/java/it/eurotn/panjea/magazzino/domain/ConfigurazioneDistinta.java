package it.eurotn.panjea.magazzino.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;

@Entity
@Audited
@Table(name = "maga_distinte_configurazione")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_CONFIGURAZIONE", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("A")
public class ConfigurazioneDistinta extends EntityBase {

    private static final long serialVersionUID = 1421984127073455430L;

    private String nome;
    // articolo al quale è collegata la configurazione
    @ManyToOne(fetch = FetchType.LAZY)
    private Articolo distinta;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "configurazioneDistinta")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy(value = "ordinamento")
    private Set<Componente> componenti;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "configurazioneDistinta")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy(value = "ordinamento")
    private Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().getCanonicalName().startsWith(this.getClass().getCanonicalName())) {
            return false;
        }
        ConfigurazioneDistinta other = (ConfigurazioneDistinta) obj;
        if (getId() != null && other.getId() != null && getId().equals(other.getId())) {
            return true;
        }
        if (nome == null) {
            if (other.nome != null) {
                return false;
            }
        } else if (!nome.equals(other.nome)) {
            return false;
        }
        return true;
    }

    /**
     * @return Returns the componenti.
     */
    public Set<Componente> getComponenti() {
        return componenti;
    }

    /**
     * @return Returns the distinta.
     */
    public Articolo getDistinta() {
        return distinta;
    }

    /**
     * @return Returns the fasiLavorazioneArticolo.
     */
    public Set<FaseLavorazioneArticolo> getFasiLavorazioneArticolo() {
        return fasiLavorazioneArticolo;
    }

    /**
     * @return Returns the nome.
     */
    public String getNome() {
        return nome;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    public boolean isConfigurazioneBase() {
        return false;
    }

    /**
     * @param componenti
     *            The componenti to set.
     */
    public void setComponenti(Set<Componente> componenti) {
        this.componenti = componenti;
    }

    /**
     * @param distinta
     *            The distinta to set.
     */
    public void setDistinta(Articolo distinta) {
        this.distinta = distinta;
    }

    /**
     * @param fasiLavorazioneArticolo
     *            The fasiLavorazioneArticolo to set.
     */
    public void setFasiLavorazioneArticolo(Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo) {
        this.fasiLavorazioneArticolo = fasiLavorazioneArticolo;
    }

    /**
     * @param nome
     *            The nome to set.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "ConfigurazioneDistinta [nome=" + nome + "]";
    }

}
