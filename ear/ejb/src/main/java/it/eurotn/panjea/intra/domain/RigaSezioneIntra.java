package it.eurotn.panjea.intra.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.util.CRC;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "intr_righe_dichiarazioni", uniqueConstraints = @UniqueConstraint(columnNames = { "progressivo",
		"dichiarazione_id", "sezione" }))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "sezione", discriminatorType = DiscriminatorType.INTEGER, length = 2)
@DiscriminatorValue("0")
public abstract class RigaSezioneIntra extends EntityBase {

	private static final long serialVersionUID = -6800289261098819220L;

	@ManyToOne
	private DichiarazioneIntra dichiarazione;

	private Integer progressivo;

	private String fornitoreStato;

	private String fornitorepiva;

	@Column(precision = 19, scale = 0)
	private BigDecimal importo;

	@Column(precision = 19, scale = 0)
	private BigDecimal importoInValuta;

	private boolean operazioneTriangolare;

	private NaturaTransazione naturaTransazione;

	@Transient
	private transient CRC crc;

	/**
	 * Costruttore.
	 */
	public RigaSezioneIntra() {
	}

	/**
	 *
	 * @param rigaSezioneIntra
	 *            riga da aggiungere.
	 */
	public void aggiungi(RigaSezioneIntra rigaSezioneIntra) {
		if (importo != null) {
			importo = importo.add(rigaSezioneIntra.getImporto());
		}
		if (importoInValuta != null) {
			importoInValuta = importoInValuta.add(rigaSezioneIntra.getImportoInValuta());
		}
	}

	/**
	 * @return long
	 */
	public long checkSum() {
		return crc.getValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RigaSezioneIntra other = (RigaSezioneIntra) obj;
		if (crc == null && other.crc == null) {
			return getId().equals(getId());
		}
		if (crc == null && other.crc != null) {
			return getId().equals(getId());
		} else if (crc != null && other.crc == null) {
			return getId().equals(getId());
		} else if (crc.getValue() != other.crc.getValue()) {
			return false;
		}
		return true;
	}

	/**
	 * @return crc dell'oggetto
	 */
	protected CRC getCrc() {
		if (crc == null) {
			crc = new CRC();
		}
		return crc;
	}

	/**
	 * @return Returns the dichiarazione.
	 */
	public DichiarazioneIntra getDichiarazione() {
		return dichiarazione;
	}

	/**
	 * @return Returns the fornitorepiva.
	 */
	public String getFornitorepiva() {
		return fornitorepiva;
	}

	/**
	 * @return Returns the fornitoreStato.
	 */
	public String getFornitoreStato() {
		return fornitoreStato;
	}

	/**
	 * @return Returns the importo.
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @return Returns the importoInValuta.
	 */
	public BigDecimal getImportoInValuta() {
		return importoInValuta;
	}

	/**
	 * @return Returns the importoInValuta.
	 */
	public BigDecimal getImportoInValutaExport() {
		if (importo == null && importoInValuta == null) {
			return BigDecimal.ZERO;
		}
		if (importo != null && importoInValuta != null && importo.compareTo(importoInValuta) == 0) {
			return BigDecimal.ZERO;
		}
		return importoInValuta;
	}

	/**
	 * @return Returns the naturaTransazione.
	 */
	public NaturaTransazione getNaturaTransazione() {
		return naturaTransazione;
	}

	/**
	 * @return codice della transazione dipendente dall'operazione triangolare.
	 */
	public String getNaturaTransazioneCodiceEsportazione() {
		if (naturaTransazione == null) {
			return null;
		}
		String result = naturaTransazione.getNumeric();
		if (operazioneTriangolare) {
			result = naturaTransazione.getAlfa();
		}
		return result;
	}

	/**
	 * @return Returns the progressivo.
	 */
	public Integer getProgressivo() {
		return progressivo;
	}

	/**
	 *
	 * @return tipoRecord in base al valore del discriminatore
	 */
	public Integer getTipoRecord() {
		if (getClass().getAnnotation(DiscriminatorValue.class) == null) {
			return null;
		}
		return Integer.valueOf(getClass().getAnnotation(DiscriminatorValue.class).value());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((crc == null) ? 0 : new Long(crc.getValue()).hashCode());
		return result;
	}

	/**
	 * @return Returns the operazioneTriangolare.
	 */
	public boolean isOperazioneTriangolare() {
		return operazioneTriangolare;
	}

	/**
	 * nega gli importi della riga.
	 */
	public void negateImporti() {
		if (importo != null) {
			importo = importo.negate();
		}
		if (importoInValuta != null) {
			importoInValuta = importoInValuta.negate();
		}
	}

	/**
	 * Arrotonda un bigdecimal a 1 se il valore è compreso tra 0 e 1.
	 *
	 * @param importoParam
	 *            l'importo da arrotondare
	 * @return l'importo arrotondato o l'importo di origine
	 */
	private BigDecimal roundToOne(BigDecimal importoParam) {
		if (importoParam != null) {
			if (importoParam.abs().compareTo(BigDecimal.ZERO) > 0 && importoParam.abs().compareTo(BigDecimal.ONE) < 0) {
				importoParam = importoParam.setScale(0, RoundingMode.UP);
			}
		}
		return importoParam;
	}

	/**
	 * @param dichiarazione
	 *            The dichiarazione to set.
	 */
	public void setDichiarazione(DichiarazioneIntra dichiarazione) {
		this.dichiarazione = dichiarazione;
	}

	/**
	 * @param fornitorepiva
	 *            The fornitorepiva to set.
	 */
	public void setFornitorepiva(String fornitorepiva) {
		this.fornitorepiva = fornitorepiva;
		getCrc().update(fornitorepiva);
	}

	/**
	 * @param fornitoreStato
	 *            The fornitoreStato to set.
	 */
	public void setFornitoreStato(String fornitoreStato) {
		this.fornitoreStato = fornitoreStato;
		getCrc().update(fornitoreStato);
	}

	/**
	 * @param importo
	 *            The importo to set.
	 */
	public void setImporto(BigDecimal importo) {
		importo = roundToOne(importo);
		this.importo = importo;

	}

	/**
	 * @param importoInValuta
	 *            The importoInValuta to set.
	 */
	public void setImportoInValuta(BigDecimal importoInValuta) {
		importoInValuta = roundToOne(importoInValuta);
		this.importoInValuta = importoInValuta;
	}

	/**
	 * @param naturaTransazione
	 *            The naturaTransazione to set.
	 */
	public void setNaturaTransazione(NaturaTransazione naturaTransazione) {
		this.naturaTransazione = naturaTransazione;
		if (naturaTransazione != null) {
			getCrc().update(naturaTransazione.ordinal());
		}
	}

	/**
	 * @param operazioneTriangolare
	 *            The operazioneTriangolare to set.
	 */
	public void setOperazioneTriangolare(boolean operazioneTriangolare) {
		this.operazioneTriangolare = operazioneTriangolare;
	}

	/**
	 * @param progressivo
	 *            The progressivo to set.
	 */
	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

	/**
	 *
	 * @param rigaSezioneIntra
	 *            riga da sottrarre
	 */
	public void sottrai(RigaSezioneIntra rigaSezioneIntra) {
		if (importo != null) {
			importo = importo.subtract(rigaSezioneIntra.getImporto());
		}
		if (importoInValuta != null) {
			importoInValuta = importoInValuta.subtract(rigaSezioneIntra.getImportoInValuta());
		}
	}
}
