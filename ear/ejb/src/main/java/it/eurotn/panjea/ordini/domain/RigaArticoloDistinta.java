package it.eurotn.panjea.ordini.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.ordini.util.RigaArticoloDistintaDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.util.PanjeaEJBUtil;

@Entity(name = "RigaArticoloDistintaOrdine")
@Audited
@DiscriminatorValue("D")
@NamedQueries({
        @NamedQuery(name = "RigaArticoloDistintaOrdine.caricaComponenti", query = "select rc from RigaArticoloComponenteOrdine rc join fetch rc.areaOrdine where rc.rigaDistintaCollegata.id=:idDistinta and rc.rigaPadre is null order by rc.articolo.codice") })
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
    protected it.eurotn.panjea.magazzino.domain.RigaArticolo creaIstanzaRigaMagazzino() {
        return new it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta();
    }

    @Override
    protected RigaOrdineDTO creaIstanzaRigaOrdineDTO() {
        RigaArticoloDTO rigaArticoloDTO = new RigaArticoloDistintaDTO();
        rigaArticoloDTO.setCodice(getArticolo().getCodice());
        rigaArticoloDTO.setIdArticolo(getArticolo().getId());
        rigaArticoloDTO.setDescrizioneArticolo(getArticolo().getDescrizione());
        rigaArticoloDTO.setDescrizione(getArticolo().getDescrizione());
        Sconto sconto = new Sconto(getVariazione1(), getVariazione2(), getVariazione3(),
                getVariazione4());
        rigaArticoloDTO.setSconto(sconto);
        // la riga ordine non gestisce la qtaChiusa quindi la imposto a 0
        rigaArticoloDTO.setQtaChiusa(0.00);
        return rigaArticoloDTO;
    }

    @Override
    public it.eurotn.panjea.magazzino.domain.RigaArticolo creaRigaArticoloMagazzino() {
        it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta rigaArticolo = new it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta();
        initRigaArticoloMagazzino(rigaArticolo);
        return rigaArticolo;
    }

    @Override
    public Set<IRigaArticoloDocumento> getComponenti() {
        return righeComponenti;
    }

    @Override
    public it.eurotn.panjea.magazzino.domain.RigaArticolo initRigaArticoloMagazzino(
            it.eurotn.panjea.magazzino.domain.RigaArticolo rigaArticolo) {
        rigaArticolo = super.initRigaArticoloMagazzino(rigaArticolo);
        // Trasformo le righe componenti ordine in componenti magazzino
        Set<IRigaArticoloDocumento> componentiMagazzino = new HashSet<IRigaArticoloDocumento>();
        for (IRigaArticoloDocumento rigaOrdineComponente : getComponenti()) {
            if (rigaOrdineComponente.getQta().compareTo(0.0) == 0) {
                continue;
            }
            RigaArticoloComponente rigaMagazzinoComponente = new RigaArticoloComponente();
            // RigaArticoloComponente rigaArticoloComponenteOrdine = (RigaArticoloComponente)
            // rigaOrdineComponente;
            // rigaArticoloComponenteOrdine.setRigaPadre(null);
            PanjeaEJBUtil.copyProperties(rigaMagazzinoComponente, rigaOrdineComponente);
            rigaMagazzinoComponente.setId(null);
            rigaMagazzinoComponente.setVersion(0);
            rigaMagazzinoComponente.setAttributi(null);
            rigaMagazzinoComponente.setComponenti(new HashSet<IRigaArticoloDocumento>());
            rigaMagazzinoComponente.setRigaOrdineCollegata((RigaOrdine) rigaOrdineComponente);
            rigaMagazzinoComponente.setRigaDistintaCollegata(rigaArticolo);
            componentiMagazzino.add(rigaMagazzinoComponente);
        }
        rigaArticolo.setComponenti(componentiMagazzino);
        return rigaArticolo;
    }

    @Override
    public void setComponenti(Set<IRigaArticoloDocumento> componenti) {
        this.righeComponenti = componenti;
    }

    @Override
    public void setDataConsegna(Date dataConsegna) {
        super.setDataConsegna(dataConsegna);

        if (righeComponenti != null) {
            for (IRigaArticoloDocumento rigaDocumento : righeComponenti) {
                if (((RigaArticolo) rigaDocumento).getDataConsegna() == null) {
                    ((RigaArticolo) rigaDocumento).setDataConsegna(dataConsegna);
                }
            }
        }
    }

    @Override
    public void setDataProduzione(Date dataProduzione) {
        super.setDataProduzione(dataProduzione);

        if (righeComponenti != null) {
            for (IRigaArticoloDocumento rigaDocumento : righeComponenti) {
                if (((RigaArticolo) rigaDocumento).getDataProduzione() == null
                        && rigaDocumento.getArticolo().isDistinta()) {
                    ((RigaArticolo) rigaDocumento).setDataProduzione(dataProduzione);
                }
            }
        }
    }

    @Override
    public void setRigheOrdineCollegate(Set<RigaOrdine> righeOrdineCollegate) {
        super.setRigheOrdineCollegate(righeOrdineCollegate);
        if (righeComponenti != null) {
            for (IRigaArticoloDocumento rigaDocumento : righeComponenti) {
                ((RigaArticolo) rigaDocumento).setRigheOrdineCollegate(righeOrdineCollegate);
            }
        }
    }

}
