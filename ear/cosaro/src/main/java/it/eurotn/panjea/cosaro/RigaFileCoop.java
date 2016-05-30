package it.eurotn.panjea.cosaro;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "cosa_riga_file_coop")
public class RigaFileCoop extends EntityBase {
	private static final long serialVersionUID = 4641481271319140846L;
	private String uuid;
	private String text;
	private Integer numeroRigaPezzi;
	private Integer numeroRigaColli;
	private Integer numeroRiga;
	private String numeroOrdine;
	private Date dataOrdine;
	private Double qta;
	private String pezziEvasi;
	private String colliEvasi;
	private String numeroBolla;
	private String dataBolla;
	private String lottoFornitore;
	private String tipoRiga;
	private Double moltTara;

	@Transient
	private String codiceArticoloCosaro;

	@Transient
	private String codiceArticoloCoop;

	@Override
	public int compareTo(EntityBase obj) {
		RigaFileCoop rigaDaComparare = (RigaFileCoop) obj;
		if (uuid.equals(rigaDaComparare.getUuid())) {
			return numeroRiga.compareTo(rigaDaComparare.getNumeroRiga());
		}
		return uuid.compareTo(rigaDaComparare.getUuid());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		RigaFileCoop other = (RigaFileCoop) obj;
		if (numeroRiga == null) {
			if (other.numeroRiga != null) {
				return false;
			}
		} else if (!numeroRiga.equals(other.numeroRiga)) {
			return false;
		}
		if (uuid == null) {
			if (other.uuid != null) {
				return false;
			}
		} else if (!uuid.equals(other.uuid)) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @return chiave della riga
	 */
	public String getChiave() {
		return uuid + "#" + numeroRiga;
	}

	/**
	 * @return the codiceArticoloCoop
	 */
	public String getCodiceArticoloCoop() {
		if (codiceArticoloCoop == null) {
			if (StringUtils.isBlank(text) || text.length() == 67) {
				codiceArticoloCoop = ""; // sono su una riga testata
			} else {
				codiceArticoloCoop = text.substring(16, 30);
			}
		}

		return codiceArticoloCoop;
	}

	/**
	 * @return the codiceArticoloCosaro
	 */
	public String getCodiceArticoloCosaro() {
		return codiceArticoloCosaro;
	}

	/**
	 * @return Returns the colliEvasi.
	 */
	public String getColliEvasi() {
		return colliEvasi;
	}

	/**
	 * @return Returns the dataBolla.
	 */
	public String getDataBolla() {
		return dataBolla;
	}

	/**
	 * @return Returns the dataOrdine.
	 */
	public Date getDataOrdine() {
		return dataOrdine;
	}

	/**
	 * @return Returns the lottoFornitore.
	 */
	public String getLottoFornitore() {
		return lottoFornitore;
	}

	/**
	 * @return Returns the moltTara.
	 */
	public Double getMoltTara() {
		return moltTara;
	}

	/**
	 * @return Returns the numeroBolla.
	 */
	public String getNumeroBolla() {
		return numeroBolla;
	}

	/**
	 * @return Returns the numeroOrdine.
	 */
	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	/**
	 * @return Returns the numeroRiga.
	 */
	public Integer getNumeroRiga() {
		return numeroRiga;
	}

	/**
	 * @return Returns the numeroRigaColli.
	 */
	public Integer getNumeroRigaColli() {
		return numeroRigaColli;
	}

	/**
	 * @return Returns the numeroRigaPezzi.
	 */
	public Integer getNumeroRigaPezzi() {
		return numeroRigaPezzi;
	}

	/**
	 * @return Returns the pezziEvasi.
	 */
	public String getPezziEvasi() {
		return pezziEvasi;
	}

	/**
	 * @return Returns the qta.
	 */
	public Double getQta() {
		if (qta == null) {
			return 0.0;
		}
		return qta;
	}

	/**
	 *
	 * @return tara per la rendicontazione.
	 */
	public Integer getTara() {
		if (moltTara == null) {
			return 0;
		}
		BigDecimal moltTaraDecimal = BigDecimal.valueOf(moltTara).multiply(new BigDecimal(1000));
		return new BigDecimal(colliEvasi).multiply(moltTaraDecimal).intValue();
	}

	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}

	/**
	 *
	 * @param dataConsegnaPrevista
	 *            data prevista di consegna richiesta durante la rendicontazione *
	 * @return testo da inserire nel file della rendicontazione.
	 */
	public String getTextRendicontazione(String dataConsegnaPrevista) {
		StringBuilder sb = new StringBuilder(text);
		if (sb.length() == 67) {
			sb = sb.replace(21, 31, dataConsegnaPrevista);
			sb = sb.replace(31, 33, "00"); // imposto i secondi della data prevista sempre a 0
			sb = sb.replace(33, 39, dataBolla == null ? "" : dataBolla);
			sb = sb.replace(39, 45, StringUtils.leftPad(numeroBolla == null ? "" : numeroBolla, 6, "0"));
		} else {
			sb = sb.replace(97, 106, StringUtils.leftPad(colliEvasi == null ? "" : colliEvasi, 9, "0"));
			sb = sb.replace(106, 115, StringUtils.leftPad(pezziEvasi == null ? "" : pezziEvasi, 9, "0"));
			BigDecimal qtaDecimal = BigDecimal.valueOf(qta);
			qtaDecimal = qtaDecimal.multiply(new BigDecimal(1000));
			sb = sb.replace(115, 124, StringUtils.leftPad(new Integer(qtaDecimal.intValue()).toString(), 9, "0"));
			String lottoFornitoreExport = StringUtils.leftPad(lottoFornitore == null ? "" : lottoFornitore, 7, "0");
			sb = sb.replace(124, 135,
					StringUtils.rightPad(lottoFornitoreExport == null ? "" : lottoFornitoreExport, 11));
			if (getTara() != 0) {
				sb = sb.replace(135, 142, StringUtils.leftPad(getTara().toString(), 7));
			}
			sb = sb.replace(142, 144, "00");
		}
		return sb.toString();
	}

	/**
	 * @return Returns the tipoRiga.
	 */
	public String getTipoRiga() {
		return tipoRiga;
	}

	/**
	 * @return Returns the uuid.
	 */
	public String getUuid() {
		return uuid;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((numeroRiga == null) ? 0 : numeroRiga.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/**
	 * @param codiceArticoloCoop
	 *            the codiceArticoloCoop to set
	 */
	public void setCodiceArticoloCoop(String codiceArticoloCoop) {
		this.codiceArticoloCoop = codiceArticoloCoop;
	}

	/**
	 * @param codiceArticoloCosaro
	 *            the codiceArticoloCosaro to set
	 */
	public void setCodiceArticoloCosaro(String codiceArticoloCosaro) {
		this.codiceArticoloCosaro = codiceArticoloCosaro;
	}

	/**
	 * @param colliEvasi
	 *            The colliEvasi to set.
	 */
	public void setColliEvasi(String colliEvasi) {
		this.colliEvasi = colliEvasi;
	}

	/**
	 * @param dataBolla
	 *            The dataBolla to set.
	 */
	public void setDataBolla(String dataBolla) {
		this.dataBolla = dataBolla;
	}

	/**
	 * @param dataOrdine
	 *            The dataOrdine to set.
	 */
	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	/**
	 * @param lottoFornitore
	 *            The lottoFornitore to set.
	 */
	public void setLottoFornitore(String lottoFornitore) {
		this.lottoFornitore = lottoFornitore;
	}

	/**
	 * @param moltiplicatore
	 *            The moltTara to set.
	 */
	public void setMoltTara(double moltiplicatore) {
		this.moltTara = moltiplicatore;
	}

	/**
	 * @param numeroBolla
	 *            The numeroBolla to set.
	 */
	public void setNumeroBolla(String numeroBolla) {
		this.numeroBolla = numeroBolla;
	}

	/**
	 * @param numeroOrdine
	 *            The numeroOrdine to set.
	 */
	public void setNumeroOrdine(String numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	/**
	 * @param numeroRiga
	 *            The numeroRiga to set.
	 */
	public void setNumeroRiga(Integer numeroRiga) {
		this.numeroRiga = numeroRiga;
	}

	/**
	 * @param numeroRigaColli
	 *            The numeroRigaColli to set.
	 */
	public void setNumeroRigaColli(Integer numeroRigaColli) {
		this.numeroRigaColli = numeroRigaColli;
	}

	/**
	 * @param numeroRigaPezzi
	 *            The numeroRigaPezzi to set.
	 */
	public void setNumeroRigaPezzi(Integer numeroRigaPezzi) {
		this.numeroRigaPezzi = numeroRigaPezzi;
	}

	/**
	 * @param pezziEvasi
	 *            The pezziEvasi to set.
	 */
	public void setPezziEvasi(String pezziEvasi) {
		this.pezziEvasi = pezziEvasi;
	}

	/**
	 * @param qta
	 *            The qta to set.
	 */
	public void setQta(Double qta) {
		this.qta = qta;
	}

	/**
	 * @param text
	 *            The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @param tipoRiga
	 *            The tipoRiga to set.
	 */
	public void setTipoRiga(String tipoRiga) {
		this.tipoRiga = tipoRiga;
	}

	/**
	 * @param uuid
	 *            The uuid to set.
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
