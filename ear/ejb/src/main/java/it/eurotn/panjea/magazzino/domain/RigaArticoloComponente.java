package it.eurotn.panjea.magazzino.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;

@Entity
@Audited
@DiscriminatorValue("C")
public class RigaArticoloComponente extends RigaArticolo implements IRigaComponente {

    private static final long serialVersionUID = -3539856023970932681L;

    @ManyToOne
    private ArticoloLite articoloDistinta;

    @Column
    private String formulaComponente;

    @ManyToOne
    private RigaArticolo rigaDistintaCollegata;

    @ManyToOne(fetch = FetchType.LAZY)
    private RigaArticolo rigaPadre;

    @Transient
    private Set<IRigaArticoloDocumento> righeComponenti;

    /**
     * Costruttore.
     */
    public RigaArticoloComponente() {
        setQta(0.0);
        setLivello(1);
        righeComponenti = new HashSet<IRigaArticoloDocumento>();
    }

    /**
     * @return Returns the articoloDistinta.
     */
    @Override
    public ArticoloLite getArticoloDistinta() {
        return articoloDistinta;
    }

    @Override
    public Set<IRigaArticoloDocumento> getComponenti() {
        return righeComponenti;
    }

    /**
     * @return the formulaComponente
     */
    @Override
    public String getFormulaComponente() {
        return formulaComponente;
    }

    /**
     * @return Returns the rigaDistintaCollegata.
     */
    @Override
    public RigaArticolo getRigaDistintaCollegata() {
        return rigaDistintaCollegata;
    }

    /**
     * @return the rigaPadre
     */
    public RigaArticolo getRigaPadre() {
        return rigaPadre;
    }

    @Override
    public boolean isAssegnazioneLottiAutomaticaAbilitata(boolean gestioneLottiAutomatici) {
        // se la riga è una RigaArticoloComponente controllo se il tipo
        // movimento è un carico a produzione perchè in
        // questo caso devo assegnare i lotti automatici
        boolean gestioneLottiTipoDocumento = false;
        TipoMovimento tipoMovimento = null;
        if (this.areaMagazzino != null && this.areaMagazzino.getTipoAreaMagazzino() != null
                && this.areaMagazzino.getTipoAreaMagazzino().getTipoMovimento() != null) {
            tipoMovimento = this.areaMagazzino.getTipoAreaMagazzino().getTipoMovimento();
            gestioneLottiTipoDocumento = this.areaMagazzino.getDocumento().getTipoDocumento().isGestioneLotti();
        }

        TipoLotto tipoLottoArticolo = (getArticolo() != null && getArticolo().getId() != null)
                ? getArticolo().getTipoLotto() : TipoLotto.NESSUNO;

        return tipoLottoArticolo != TipoLotto.NESSUNO && tipoMovimento != null
                && tipoMovimento == TipoMovimento.CARICO_PRODUZIONE && gestioneLottiTipoDocumento;
    }

    /**
     * @param articoloDistinta
     *            the articoloDistinta to set
     */
    @Override
    public void setArticoloDistinta(ArticoloLite articoloDistinta) {
        this.articoloDistinta = articoloDistinta;
    }

    @Override
    public void setComponenti(Set<IRigaArticoloDocumento> componenti) {
        this.righeComponenti = componenti;
    }

    /**
     * @param formulaComponente
     *            the formulaComponente to set
     */
    @Override
    public void setFormulaComponente(String formulaComponente) {
        this.formulaComponente = formulaComponente;
    }

    /**
     * @param rigaDistintaCollegata
     *            The rigaDistintaCollegata to set.
     */
    @Override
    public void setRigaDistintaCollegata(IRigaArticoloDocumento rigaDistintaCollegata) {
        this.rigaDistintaCollegata = (RigaArticolo) rigaDistintaCollegata;
    }

    /**
     * @param rigaPadre
     *            the rigaPadre to set
     */
    public void setRigaPadre(RigaArticolo rigaPadre) {
        this.rigaPadre = rigaPadre;
    }

}
