package it.eurotn.panjea.magazzino.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaArticoloDistintaDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;

@Entity
@Audited
@DiscriminatorValue("D")
@NamedQueries({
        @NamedQuery(name = "RigaArticoloDistinta.caricaComponenti", query = "select rc from RigaArticoloComponente rc join fetch rc.areaMagazzino where rc.rigaDistintaCollegata.id=:idDistinta and rc.rigaPadre is null order by rc.articolo.codice") })
public class RigaArticoloDistinta extends RigaArticolo {

    private static final long serialVersionUID = 2447738885620912024L;

    @Transient
    private Set<IRigaArticoloDocumento> righeComponenti;

    /**
     * Costruttore.
     */
    public RigaArticoloDistinta() {
        righeComponenti = new HashSet<IRigaArticoloDocumento>();
    }

    @Override
    protected RigaMagazzinoDTO creaIstanzaRigaMagazzinoDTO() {
        RigaArticoloDTO rigaArticoloDTO = new RigaArticoloDistintaDTO();
        if (getArticolo() != null) {
            rigaArticoloDTO.setCodice(getArticolo().getCodice());
            rigaArticoloDTO.setIdArticolo(getArticolo().getId());
            rigaArticoloDTO.setDescrizioneArticolo(getArticolo().getDescrizione());
            rigaArticoloDTO.setDescrizione(getArticolo().getDescrizione());
        }
        Sconto sconto = new Sconto(getVariazione1(), getVariazione2(), getVariazione3(), getVariazione4());
        rigaArticoloDTO.setSconto(sconto);
        return rigaArticoloDTO;
    }

    @Override
    public Set<IRigaArticoloDocumento> getComponenti() {
        return righeComponenti;
    }

    @Override
    public void setComponenti(Set<IRigaArticoloDocumento> componenti) {
        this.righeComponenti = componenti;
    }

}
