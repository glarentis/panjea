package it.eurotn.panjea.ordini.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.IRigaComponente;
import it.eurotn.util.PanjeaEJBUtil;

@Entity(name = "RigaArticoloComponenteOrdine")
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

	@Override
	protected it.eurotn.panjea.magazzino.domain.RigaArticolo creaIstanzaRigaMagazzino() {
		return new it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta();
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
	public it.eurotn.panjea.magazzino.domain.RigaArticolo initRigaArticoloMagazzino(
			it.eurotn.panjea.magazzino.domain.RigaArticolo rigaArticolo) {
		rigaArticolo = super.initRigaArticoloMagazzino(rigaArticolo);
		// Trasformo le righe componenti ordine in componenti magazzino
		Set<IRigaArticoloDocumento> componentiMagazzino = new HashSet<IRigaArticoloDocumento>();
		for (IRigaArticoloDocumento rigaOrdineComponente : rigaArticolo.getComponenti()) {
			it.eurotn.panjea.magazzino.domain.RigaArticoloComponente rigaMagazzinoComponente = new it.eurotn.panjea.magazzino.domain.RigaArticoloComponente();
			RigaArticoloComponente rigaArticoloComponenteOrdine = (RigaArticoloComponente) rigaOrdineComponente;
			RigaArticoloComponente rigaClonata = PanjeaEJBUtil.cloneObject(rigaArticoloComponenteOrdine);
			rigaClonata.setRigaPadre(null);
			PanjeaEJBUtil.copyProperties(rigaMagazzinoComponente, rigaClonata);
			rigaMagazzinoComponente.setId(null);
			rigaMagazzinoComponente.setVersion(0);
			rigaMagazzinoComponente.setAttributi(null);
			rigaMagazzinoComponente.setComponenti(new HashSet<IRigaArticoloDocumento>());
			rigaMagazzinoComponente.setRigaPadre(rigaArticolo);
			rigaMagazzinoComponente.setRigaOrdineCollegata(this);
			componentiMagazzino.add(rigaMagazzinoComponente);
		}
		rigaArticolo.setComponenti(componentiMagazzino);
		return rigaArticolo;
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
