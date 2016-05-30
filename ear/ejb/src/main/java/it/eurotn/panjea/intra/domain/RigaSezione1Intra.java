package it.eurotn.panjea.intra.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("1")
public class RigaSezione1Intra extends RigaSezioneIntra {
	private static final long serialVersionUID = -7582072716826315745L;
	@ManyToOne
	private Nomenclatura nomenclatura;
	private Long massaNetta;
	private Long massaSupplementare;
	private String um;

	@Column(precision = 19, scale = 0)
	private BigDecimal valoreStatisticoEuro;

	private GruppoCondizioneConsegna gruppoCondizioneConsegna;
	private String paese;
	private String provincia;
	private String paeseOrigineArticolo;
	private ModalitaTrasporto modalitaTrasporto;

	/**
	 * @return Returns the gruppoCondizioneConsegna.
	 */
	public GruppoCondizioneConsegna getGruppoCondizioneConsegna() {
		return gruppoCondizioneConsegna;
	}

	/**
	 * @return Returns the massaNetta.
	 */
	public Long getMassaNetta() {
		return massaNetta;
	}

	/**
	 * @return the massaSupplementare
	 */
	public Long getMassaSupplementare() {
		return massaSupplementare;
	}

	/**
	 * @return Returns the modalitaTrasporto.
	 */
	public ModalitaTrasporto getModalitaTrasporto() {
		return modalitaTrasporto;
	}

	/**
	 * @return Returns the nomenclatura.
	 */
	public Nomenclatura getNomenclatura() {
		return nomenclatura;
	}

	/**
	 * @return Returns the paese.
	 */
	public String getPaese() {
		return paese;
	}

	/**
	 * @return Returns the paeseOrigineArticolo.
	 */
	public String getPaeseOrigineArticolo() {
		return paeseOrigineArticolo;
	}

	/**
	 * @return Returns the provincia.
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @return the um
	 */
	public String getUm() {
		return um;
	}

	/**
	 * @return Returns the valoreStatisticoEuro.
	 */
	public BigDecimal getValoreStatisticoEuro() {
		return valoreStatisticoEuro;
	}

	@Override
	public void negateImporti() {
		super.negateImporti();
		valoreStatisticoEuro = valoreStatisticoEuro.negate();
	}

	/**
	 * @param gruppoCondizioneConsegna
	 *            The gruppoCondizioneConsegna to set.
	 */
	public void setGruppoCondizioneConsegna(GruppoCondizioneConsegna gruppoCondizioneConsegna) {
		this.gruppoCondizioneConsegna = gruppoCondizioneConsegna;
		if (gruppoCondizioneConsegna != null) {
			getCrc().update(gruppoCondizioneConsegna.ordinal());
		}
	}

	/**
	 * Set della massa calcolata dalle righe bene intra (BigDecimal) per avvalorare la massaNetta della riga sezione
	 * (Long).
	 * 
	 * @param massa
	 *            il BigDecimal da impostare come massa
	 */
	public void setMassaCalcolata(BigDecimal massa) {
		if (massa != null) {
			setMassaNetta(massa.longValue());
		}
	}

	/**
	 * @param massaNetta
	 *            The massaNetta to set.
	 */
	public void setMassaNetta(Long massaNetta) {
		this.massaNetta = massaNetta;
	}

	/**
	 * @param massaSupplementare
	 *            the massaSupplementare to set
	 */
	public void setMassaSupplementare(Long massaSupplementare) {
		this.massaSupplementare = massaSupplementare;
	}

	/**
	 * @param modalitaTrasporto
	 *            The modalitaTrasporto to set.
	 */
	public void setModalitaTrasporto(ModalitaTrasporto modalitaTrasporto) {
		this.modalitaTrasporto = modalitaTrasporto;
		if (modalitaTrasporto != null) {
			getCrc().update(modalitaTrasporto.name());
		}
	}

	/**
	 * @param nomenclatura
	 *            The nomenclatura to set.
	 */
	public void setNomenclatura(Nomenclatura nomenclatura) {
		this.nomenclatura = nomenclatura;
		if (nomenclatura != null) {
			getCrc().update(nomenclatura);
		}
	}

	/**
	 * @param paese
	 *            The paese to set.
	 */
	public void setPaese(String paese) {
		this.paese = paese;
	}

	/**
	 * @param paeseOrigineArticolo
	 *            The paeseOrigineArticolo to set.
	 */
	public void setPaeseOrigineArticolo(String paeseOrigineArticolo) {
		this.paeseOrigineArticolo = paeseOrigineArticolo;
	}

	/**
	 * @param provincia
	 *            The provincia to set.
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 * @param um
	 *            The um to set.
	 */
	public void setUm(String um) {
		this.um = um;
		getCrc().update(um);
	}

	/**
	 * @param valoreStatisticoEuro
	 *            The valoreStatisticoEuro to set.
	 */
	public void setValoreStatisticoEuro(BigDecimal valoreStatisticoEuro) {
		this.valoreStatisticoEuro = valoreStatisticoEuro;
	}

	@Override
	public void sottrai(RigaSezioneIntra rigaSezioneIntra) {
		super.sottrai(rigaSezioneIntra);
		if (rigaSezioneIntra instanceof RigaSezione1Intra) {
			valoreStatisticoEuro = valoreStatisticoEuro.subtract(((RigaSezione1Intra) rigaSezioneIntra)
					.getValoreStatisticoEuro());
		}
	}
}
