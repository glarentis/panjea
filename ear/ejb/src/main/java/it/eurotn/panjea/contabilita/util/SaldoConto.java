package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class SaldoConto implements Serializable, Comparable<SaldoConto> {

	private static final long serialVersionUID = -8097780613747232111L;

	private String centroCostoCodice;
	private String centroCostoDescrizione;
	private Integer centroCostoId;

	private String contoCodice;
	private String contoDescrizione;
	private Integer contoId;

	private String mastroCodice;
	private String mastroDescrizione;
	private Integer mastroId;

	private Integer sottoContoId;
	private String sottoContoCodice;
	private String sottoContoDescrizione;

	private Conto.SottotipoConto sottoTipoConto; // Lo utilizzo per capire di che tipo è un sotto conto
	private Conto.TipoConto tipoConto;

	private BigDecimal importoDare;
	private BigDecimal importoAvere;

	private BigDecimal importoCentriCostoDare;
	private BigDecimal importoCentriCostoAvere;

	{
		this.importoDare = BigDecimal.ZERO;
		this.importoAvere = BigDecimal.ZERO;
		this.importoCentriCostoDare = BigDecimal.ZERO;
		this.importoCentriCostoAvere = BigDecimal.ZERO;
	}

	private List<SaldoConto> centriCosto;

	/**
	 * Costruttore di default.
	 */
	public SaldoConto() {
		super();
	}

	/**
	 * Costruttore per costruire una {@link SaldoConto} da una {@link RigaContabile} .
	 *
	 * @param rigaContabile
	 *            rigaContabile
	 */
	public SaldoConto(final RigaContabile rigaContabile) {
		this.mastroCodice = rigaContabile.getConto().getConto().getMastro().getCodice();
		this.mastroDescrizione = rigaContabile.getConto().getConto().getMastro().getDescrizione();
		this.mastroId = rigaContabile.getConto().getConto().getMastro().getId();
		this.contoCodice = rigaContabile.getConto().getConto().getCodice();
		this.contoDescrizione = rigaContabile.getConto().getConto().getDescrizione();
		this.contoId = rigaContabile.getConto().getConto().getId();
		this.sottoContoCodice = rigaContabile.getConto().getCodice();
		this.sottoContoDescrizione = rigaContabile.getConto().getDescrizione();
		this.sottoContoId = rigaContabile.getConto().getId();
		this.tipoConto = rigaContabile.getConto().getConto().getTipoConto();
		this.sottoTipoConto = rigaContabile.getConto().getConto().getSottotipoConto();
		this.importoDare = rigaContabile.getImportoDare();
		this.importoAvere = rigaContabile.getImportoAvere();
	}

	/**
	 *
	 * @param saldoConto
	 *            saldo conto con i dati iniziali
	 */
	public SaldoConto(final SaldoConto saldoConto) {
		this.mastroCodice = saldoConto.getMastroCodice();
		this.mastroDescrizione = saldoConto.getMastroDescrizione();
		this.mastroId = saldoConto.getMastroId();
		this.contoCodice = saldoConto.getContoCodice();
		this.contoDescrizione = saldoConto.getContoDescrizione();
		this.contoId = saldoConto.getContoId();
		this.sottoContoCodice = saldoConto.getSottoContoCodice();
		this.sottoContoDescrizione = saldoConto.getSottoContoDescrizione();
		this.sottoContoId = saldoConto.getSottoContoId();
		this.tipoConto = saldoConto.getTipoConto();
		this.sottoTipoConto = saldoConto.getSottoTipoConto();
	}

	/**
	 * Costruisce un saldoConto partendo dal sotto conto. Gli importi sono a zero.
	 *
	 * @param sottoConto
	 *            sottoConto
	 */
	public SaldoConto(final SottoConto sottoConto) {
		this.mastroCodice = sottoConto.getConto().getMastro().getCodice();
		this.mastroDescrizione = sottoConto.getConto().getMastro().getDescrizione();
		this.mastroId = sottoConto.getConto().getMastro().getId();
		this.contoCodice = sottoConto.getConto().getCodice();
		this.contoDescrizione = sottoConto.getConto().getDescrizione();
		this.contoId = sottoConto.getConto().getId();
		this.sottoContoCodice = sottoConto.getCodice();
		this.sottoContoDescrizione = sottoConto.getDescrizione();
		this.sottoContoId = sottoConto.getId();
		this.tipoConto = sottoConto.getConto().getTipoConto();
		this.sottoTipoConto = sottoConto.getConto().getSottotipoConto();
	}

	/**
	 * @param saldoContoCentroCosto
	 *            saldo conto centro costo da aggiungere
	 */
	public void aggiungiCentroCosto(SaldoConto saldoContoCentroCosto) {
		if (centriCosto == null) {
			centriCosto = new ArrayList<SaldoConto>();
		}
		centriCosto.add(saldoContoCentroCosto);
	}

	/**
	 * Aggiunge un importo all'avere.
	 *
	 * @param valore
	 *            valore da aggiungere
	 */
	public void aggiungiImportoAvere(BigDecimal valore) {
		if (valore != null) {
			importoAvere = importoAvere.add(valore);
		}
	}

	/**
	 * Aggiunge un importo al dare.
	 *
	 * @param valore
	 *            valore da aggiungere
	 */
	public void aggiungiImportoDare(BigDecimal valore) {
		if (valore != null) {
			importoDare = importoDare.add(valore);
		}
	}

	@Override
	public int compareTo(SaldoConto saldoConto2) {
		return getSottoContoCodiceCompleto().compareTo(saldoConto2.getSottoContoCodiceCompleto());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SaldoConto other = (SaldoConto) obj;
		if (centroCostoId == null) {
			if (other.centroCostoId != null) {
				return false;
			}
		} else if (!centroCostoId.equals(other.centroCostoId)) {
			return false;
		}
		if (sottoContoId == null) {
			if (other.sottoContoId != null) {
				return false;
			}
		} else if (!sottoContoId.equals(other.sottoContoId)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the centriCosto
	 */
	public List<SaldoConto> getCentriCosto() {
		return centriCosto;
	}

	/**
	 * @return the centroCostoCodice
	 */
	public String getCentroCostoCodice() {
		return centroCostoCodice;
	}

	/**
	 * @return the centroCostoDescrizione
	 */
	public String getCentroCostoDescrizione() {
		return centroCostoDescrizione;
	}

	/**
	 * @return the centroCostoId
	 */
	public Integer getCentroCostoId() {
		return centroCostoId;
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (!(obj instanceof SaldoConto)) {
	// return false;
	// }
	// SaldoConto saldoConto2 = (SaldoConto) obj;
	// return sottoContoId.equals(saldoConto2.getSottoContoId());
	// }

	/**
	 * @return contoCodice
	 */
	public String getContoCodice() {
		return contoCodice;
	}

	/**
	 * @return contoDescrizione
	 */
	public String getContoDescrizione() {
		return contoDescrizione;
	}

	/**
	 * @return Returns the contoId.
	 */
	public Integer getContoId() {
		return contoId;
	}

	/**
	 * @return Returns the importoAvere.
	 */
	public BigDecimal getImportoAvere() {
		return importoAvere;
	}

	/**
	 * @return the importoCentriCostoAvere
	 */
	public BigDecimal getImportoCentriCostoAvere() {
		return importoCentriCostoAvere;
	}

	/**
	 * @return the importoCentriCostoDare
	 */
	public BigDecimal getImportoCentriCostoDare() {
		return importoCentriCostoDare;
	}

	/**
	 * @return Returns the importoDare.
	 */
	public BigDecimal getImportoDare() {
		return importoDare;
	}

	/**
	 * @return mastroCodice
	 */
	public String getMastroCodice() {
		return mastroCodice;
	}

	/**
	 * @return mastroDescrizione
	 */
	public String getMastroDescrizione() {
		return mastroDescrizione;
	}

	/**
	 * @return Returns the mastroId.
	 */
	public Integer getMastroId() {
		return mastroId;
	}

	/**
	 * @return saldo
	 */
	public BigDecimal getSaldo() {
		return importoDare.subtract(importoAvere);
	}

	/**
	 * @return Returns the sottoContoCodice.
	 */
	public String getSottoContoCodice() {
		return sottoContoCodice;
	}

	/**
	 * Restituisce il codice del sottoconto così formattato. : <br/>
	 * MMM.CCC.SSSSSSS dove :<br/>
	 * MMM = Codice Mastro <br/>
	 * CCC = Codice Conto <br/>
	 * SSSSSS = Codice Sottoconto<br/>
	 *
	 * @return codice del sottoconto composto da codiceMastro + codiceConto + codiceSottoConto
	 */
	public String getSottoContoCodiceCompleto() {
		StringBuffer codiceSottoConto = new StringBuffer();
		if (sottoContoCodice == null) {
			sottoContoCodice = "";
		}
		if (sottoContoCodice.equals("")) {
			return sottoContoCodice;
		}
		codiceSottoConto = codiceSottoConto.append(getMastroCodice()).append(".").append(getContoCodice()).append(".")
				.append(getSottoContoCodice());
		if (centroCostoCodice != null) {
			codiceSottoConto = codiceSottoConto.append(".").append(getCentroCostoCodice());
		}
		return codiceSottoConto.toString();
	}

	/**
	 * @return Returns the sottoContoCodiceDescrizione.
	 */
	public String getSottoContoDescrizione() {
		return sottoContoDescrizione;
	}

	/**
	 * @return sottoContoId
	 */
	public Integer getSottoContoId() {
		return sottoContoId;
	}

	/**
	 * @return sottoTipoConto
	 */
	public Conto.SottotipoConto getSottoTipoConto() {
		return sottoTipoConto;
	}

	/**
	 * @return tipoConto
	 */
	public Conto.TipoConto getTipoConto() {
		return tipoConto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centroCostoId == null) ? 0 : centroCostoId.hashCode());
		result = prime * result + ((sottoContoId == null) ? 0 : sottoContoId.hashCode());
		return result;
	}

	/**
	 * @return se la riga è un centro di costo
	 */
	public boolean isCentriCostoPresenti() {
		return centriCosto != null;
	}

	// @Override
	// public int hashCode() {
	// if (sottoContoId != null) {
	// String hashStr = this.getClass().getName() + ":" + sottoContoId;
	//
	// return hashStr.hashCode();
	// }
	// return super.hashCode();
	// }

	/**
	 * @return true se il conto è un conto cliente o fornitore
	 */
	public boolean isClienteFornitore() {
		return sottoTipoConto == SottotipoConto.CLIENTE || sottoTipoConto == SottotipoConto.FORNITORE;
	}

	/**
	 * @return true se l'importo di controllo è uguale all' importo.
	 */
	public boolean isTotaleCentriCostoValido() {
		if (!isCentriCostoPresenti()) {
			return true;
		}
		importoCentriCostoAvere = BigDecimal.ZERO;
		importoCentriCostoDare = BigDecimal.ZERO;
		for (SaldoConto centroCosto : centriCosto) {
			importoCentriCostoAvere = importoCentriCostoAvere.add(centroCosto.getImportoAvere());
			importoCentriCostoDare = importoCentriCostoDare.add(centroCosto.getImportoDare());
		}

		int compareAvere = importoAvere.compareTo(importoCentriCostoAvere);
		int compareDare = importoDare.compareTo(importoCentriCostoDare);
		return compareAvere == 0 && compareDare == 0;
	}

	/**
	 * Gira i conti Dare e Avere<br/>
	 * . <b>NB.</b>Negare gli importi significa girare i conti.<br/>
	 */
	public void negate() {
		BigDecimal importoTemp = importoDare.add(BigDecimal.ZERO);
		importoDare = importoAvere;
		importoAvere = importoTemp;
	}

	/**
	 * @param centriCosto
	 *            the centriCosto to set
	 */
	public void setCentriCosto(List<SaldoConto> centriCosto) {
		this.centriCosto = centriCosto;
	}

	/**
	 * @param centroCostoCodice
	 *            the centroCostoCodice to set
	 */
	public void setCentroCostoCodice(String centroCostoCodice) {
		this.centroCostoCodice = centroCostoCodice;
	}

	/**
	 * @param centroCostoDescrizione
	 *            the centroCostoDescrizione to set
	 */
	public void setCentroCostoDescrizione(String centroCostoDescrizione) {
		this.centroCostoDescrizione = centroCostoDescrizione;
	}

	/**
	 * @param centroCostoId
	 *            the centroCostoId to set
	 */
	public void setCentroCostoId(Integer centroCostoId) {
		this.centroCostoId = centroCostoId;
	}

	/**
	 * @param contoCodice
	 *            the contoCodice to set
	 */
	public void setContoCodice(String contoCodice) {
		this.contoCodice = contoCodice;
	}

	/**
	 * @param contoDescrizione
	 *            the contoDescrizione to set
	 */
	public void setContoDescrizione(String contoDescrizione) {
		this.contoDescrizione = contoDescrizione;
	}

	/**
	 * @param contoId
	 *            the contoId to set
	 */
	public void setContoId(Integer contoId) {
		this.contoId = contoId;
	}

	/**
	 * @param importoAvere
	 *            the importoAvere to set
	 */
	public void setImportoAvere(BigDecimal importoAvere) {
		this.importoAvere = importoAvere;
	}

	/**
	 * @param importoCentriCostoAvere
	 *            the importoCentriCostoAvere to set
	 */
	public void setImportoCentriCostoAvere(BigDecimal importoCentriCostoAvere) {
		this.importoCentriCostoAvere = importoCentriCostoAvere;
	}

	/**
	 * @param importoCentriCostoDare
	 *            the importoCentriCostoDare to set
	 */
	public void setImportoCentriCostoDare(BigDecimal importoCentriCostoDare) {
		this.importoCentriCostoDare = importoCentriCostoDare;
	}

	/**
	 * @param importoDare
	 *            the importoDare to set
	 */
	public void setImportoDare(BigDecimal importoDare) {
		this.importoDare = importoDare;
	}

	/**
	 * @param mastroCodice
	 *            the mastroCodice to set
	 */
	public void setMastroCodice(String mastroCodice) {
		this.mastroCodice = mastroCodice;
	}

	/**
	 * @param mastroDescrizione
	 *            the mastroDescrizione to set
	 */
	public void setMastroDescrizione(String mastroDescrizione) {
		this.mastroDescrizione = mastroDescrizione;
	}

	/**
	 * @param mastroId
	 *            the mastroId to set
	 */
	public void setMastroId(Integer mastroId) {
		this.mastroId = mastroId;
	}

	/**
	 * @param sottoContoCodice
	 *            the sottoContoCodice to set
	 */
	public void setSottoContoCodice(String sottoContoCodice) {
		this.sottoContoCodice = sottoContoCodice;
	}

	/**
	 * @param sottoContoDescrizione
	 *            the sottoContoDescrizione to set
	 */
	public void setSottoContoDescrizione(String sottoContoDescrizione) {
		this.sottoContoDescrizione = sottoContoDescrizione;
	}

	/**
	 * @param sottoContoId
	 *            the sottoContoId to set
	 */
	public void setSottoContoId(Integer sottoContoId) {
		this.sottoContoId = sottoContoId;
	}

	/**
	 * @param sottoTipoConto
	 *            the sottoTipoConto to set
	 */
	public void setSottoTipoConto(Conto.SottotipoConto sottoTipoConto) {
		this.sottoTipoConto = sottoTipoConto;
	}

	/**
	 * @param tipoConto
	 *            the tipoConto to set
	 */
	public void setTipoConto(Conto.TipoConto tipoConto) {
		this.tipoConto = tipoConto;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SottoContoDTO[");
		buffer.append(" mastroCodice = ").append(mastroCodice);
		buffer.append(" mastroDescrizione = ").append(mastroDescrizione);
		buffer.append(" contoCodice = ").append(contoCodice);
		buffer.append(" contoDescrizione = ").append(contoDescrizione);
		buffer.append(" contoId = ").append(contoId);
		buffer.append(" sottoContoCodice = ").append(sottoContoCodice);
		buffer.append(" sottoContoDescrizione = ").append(sottoContoDescrizione);
		buffer.append(" sottoContoId = ").append(sottoContoId);
		buffer.append(" tipoConto = ").append(tipoConto);
		buffer.append(" importoAvere = ").append(getImportoAvere());
		buffer.append(" importoDare = ").append(getImportoDare());
		buffer.append(" saldo = ").append(getSaldo());
		buffer.append("]");
		return buffer.toString();
	}
}
