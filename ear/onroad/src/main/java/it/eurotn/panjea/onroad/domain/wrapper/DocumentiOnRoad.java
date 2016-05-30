package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentiOnRoad implements Serializable {

	private static final long serialVersionUID = 6832764206211091073L;

	private int numeroDdt = 0;
	private int numeroFatture = 0;

	private int numeroCarichi = 0;
	private BigDecimal imponibileDdt;
	private BigDecimal imponibileFatture;

	private BigDecimal imponibileCarichi;
	private BigDecimal impostaDdt;
	private BigDecimal impostaFatture;

	private BigDecimal impostaCarichi;
	private int codiceMinDdt = 0;

	private int codiceMaxDdt = 0;
	private int codiceMinFatture = 0;

	private int codiceMaxFatture = 0;
	private int codiceMinCarichi = 0;

	private int codiceMaxCarichi = 0;
	private Date dataInizio = null;

	private Date dataFine = null;

	{
		impostaDdt = BigDecimal.ZERO;
		impostaFatture = BigDecimal.ZERO;
		impostaCarichi = BigDecimal.ZERO;

		imponibileDdt = BigDecimal.ZERO;
		imponibileFatture = BigDecimal.ZERO;
		imponibileCarichi = BigDecimal.ZERO;
	}

	/**
	 * Costruttore.
	 */
	public DocumentiOnRoad() {
		super();
	}

	/**
	 * Aggiungi un documento e quindi aggiorna le informazioni riepilogative relative all'importazione (numero carichi,
	 * numero fatture, numero ddt, totali relativi).
	 * 
	 * @param areaMagazzinoFullDTO
	 *            areaMagazzinoFullDTO
	 * @param tipoDocumentoOnRoad
	 *            il tipo documento per identificare i tre gruppi tipo documento onroad per il riepilogo
	 */
	public void addDocumento(AreaMagazzinoFullDTO areaMagazzinoFullDTO, String tipoDocumentoOnRoad) {
		// i tipi documento gestiti da onroad sono FT, NOT, NVA, DDT

		BigDecimal importoInValutaAzienda = areaMagazzinoFullDTO.getAreaMagazzino().getTotaleMerce()
				.getImportoInValutaAzienda();
		BigDecimal impostaInValutaAzienda = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getImposta() != null ? areaMagazzinoFullDTO
				.getAreaMagazzino().getDocumento().getImposta().getImportoInValutaAzienda()
				: BigDecimal.ZERO;

		setDataInizio(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getDataDocumento());
		setDataFine(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getDataDocumento());

		int codiceDocumento = 0;
		// prendo il primo numero che trovo nel codice documento
		Pattern pattern = Pattern.compile("([0-9]+)");
		Matcher matcher = pattern.matcher(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getCodice()
				.getCodice());
		if (matcher.find()) {
			codiceDocumento = Integer.parseInt(matcher.group(0));
		}

		if (tipoDocumentoOnRoad.equals("FT")) {
			numeroFatture++;
			imponibileFatture = imponibileFatture.add(importoInValutaAzienda);
			impostaFatture = impostaFatture.add(impostaInValutaAzienda);
			setCodiceMaxFatture(codiceDocumento);
			setCodiceMinFatture(codiceDocumento);
		} else if (tipoDocumentoOnRoad.equals("DDT")) {
			numeroCarichi++;
			imponibileCarichi = imponibileCarichi.add(importoInValutaAzienda);
			impostaCarichi = impostaCarichi.add(impostaInValutaAzienda);
			setCodiceMaxCarichi(codiceDocumento);
			setCodiceMinCarichi(codiceDocumento);
		} else {
			numeroDdt++;
			imponibileDdt = imponibileDdt.add(importoInValutaAzienda);
			impostaDdt = impostaDdt.add(impostaInValutaAzienda);
			setCodiceMaxDdt(codiceDocumento);
			setCodiceMinDdt(codiceDocumento);
		}
	}

	/**
	 * @return the codiceMaxCarichi
	 */
	public int getCodiceMaxCarichi() {
		return codiceMaxCarichi;
	}

	/**
	 * @return the codiceMaxDdt
	 */
	public int getCodiceMaxDdt() {
		return codiceMaxDdt;
	}

	/**
	 * @return the codiceMaxFatture
	 */
	public int getCodiceMaxFatture() {
		return codiceMaxFatture;
	}

	/**
	 * @return the codiceMinCarichi
	 */
	public int getCodiceMinCarichi() {
		return codiceMinCarichi;
	}

	/**
	 * @return the codiceMinDdt
	 */
	public int getCodiceMinDdt() {
		return codiceMinDdt;
	}

	/**
	 * @return the codiceMinFatture
	 */
	public int getCodiceMinFatture() {
		return codiceMinFatture;
	}

	/**
	 * @return the dataFine
	 */
	public Date getDataFine() {
		return dataFine;
	}

	/**
	 * @return the dataInizio
	 */
	public Date getDataInizio() {
		return dataInizio;
	}

	/**
	 * @return the imponibileCarichi
	 */
	public BigDecimal getImponibileCarichi() {
		return imponibileCarichi;
	}

	/**
	 * @return the imponibileDdt
	 */
	public BigDecimal getImponibileDdt() {
		return imponibileDdt;
	}

	/**
	 * @return the imponibileFatture
	 */
	public BigDecimal getImponibileFatture() {
		return imponibileFatture;
	}

	/**
	 * @return the impostaCarichi
	 */
	public BigDecimal getImpostaCarichi() {
		return impostaCarichi;
	}

	/**
	 * @return the impostaDdt
	 */
	public BigDecimal getImpostaDdt() {
		return impostaDdt;
	}

	/**
	 * @return the impostaFatture
	 */
	public BigDecimal getImpostaFatture() {
		return impostaFatture;
	}

	/**
	 * @return la stringa che riporta le informazioni relative ai documenti onroad importati
	 */
	public String getLog() {
		StringBuilder sb = new StringBuilder();
		sb.append("-------------------------------------------------------------------------------------------");
		sb.append("\n");
		sb.append("DOCUMENTI IMPORTATI: ");
		sb.append("\n");
		sb.append("\n");
		sb.append("D.D.T.: \t\t" + getNumeroCarichi() + "\t da " + getCodiceMinCarichi() + " a "
				+ getCodiceMaxCarichi());
		sb.append("\n");
		sb.append("NOTE DI CONSEGNA: \t" + getNumeroDdt() + "\t da " + getCodiceMinDdt() + " a " + getCodiceMaxDdt());
		sb.append("\n");
		sb.append("FATTURE: \t\t" + getNumeroFatture() + "\t da " + getCodiceMinFatture() + " a "
				+ getCodiceMaxFatture());
		sb.append("\n");
		sb.append("\n");
		sb.append("TOTALE DOCUMENTI: \t" + getNumeroDocumenti());
		sb.append("\n");
		sb.append("-------------------------------------------------------------------------------------------");
		sb.append("\n");
		sb.append("-------------------------------------------------------------------------------------------");
		sb.append("\n");

		sb.append("TOTALI: ");
		sb.append("\n");
		sb.append("\n");
		sb.append("D.D.T.: \t\t" + getImponibileCarichi() + "\t IVA " + getImpostaCarichi());
		sb.append("\n");
		sb.append("NOTE DI CONSEGNA: \t" + getImponibileDdt() + "\t IVA " + getImpostaDdt());
		sb.append("\n");
		sb.append("FATTURE: \t\t" + getImponibileFatture() + "\t IVA " + getImpostaFatture());
		sb.append("\n");
		sb.append("\n");

		sb.append("TOT IMPONIBILE VENDITE: \t" + getTotaleImponibileVendite());
		sb.append("\n");
		sb.append("TOT IVA: \t\t" + getTotaleImpostaVendite());
		sb.append("\n");
		sb.append("-------------------------------------------------------------------------------------------");
		sb.append("\n");

		return sb.toString();
	}

	/**
	 * @return the numeroCarichi
	 */
	public int getNumeroCarichi() {
		return numeroCarichi;
	}

	/**
	 * @return the numeroDdt
	 */
	public int getNumeroDdt() {
		return numeroDdt;
	}

	/**
	 * @return il numero totale di documenti
	 */
	public int getNumeroDocumenti() {
		return numeroCarichi + numeroDdt + numeroFatture;
	}

	/**
	 * @return the numeroFatture
	 */
	public int getNumeroFatture() {
		return numeroFatture;
	}

	/**
	 * @return il totale imponibile di tutti i documenti
	 */
	public BigDecimal getTotaleImponibileDocumenti() {
		return imponibileFatture.add(imponibileCarichi).add(imponibileDdt);
	}

	/**
	 * @return il totale imponibile di tutti i documenti
	 */
	public BigDecimal getTotaleImponibileVendite() {
		return imponibileFatture.add(imponibileDdt);
	}

	/**
	 * @return il totale iva di tutti i documenti
	 */
	public BigDecimal getTotaleImpostaDocumenti() {
		return impostaFatture.add(impostaCarichi).add(impostaDdt);
	}

	/**
	 * @return il totale iva per le vendite
	 */
	public BigDecimal getTotaleImpostaVendite() {
		return impostaFatture.add(impostaDdt);
	}

	/**
	 * @param codiceMaxCarichi
	 *            the codiceMaxCarichi to set
	 */
	public void setCodiceMaxCarichi(int codiceMaxCarichi) {
		if (this.codiceMaxCarichi == 0 || codiceMaxCarichi > this.codiceMaxCarichi) {
			this.codiceMaxCarichi = codiceMaxCarichi;
		}
	}

	/**
	 * @param codiceMaxDdt
	 *            the codiceMaxDdt to set
	 */
	public void setCodiceMaxDdt(int codiceMaxDdt) {
		if (this.codiceMaxDdt == 0 || codiceMaxDdt > this.codiceMaxDdt) {
			this.codiceMaxDdt = codiceMaxDdt;
		}
	}

	/**
	 * @param codiceMaxFatture
	 *            the codiceMaxFatture to set
	 */
	public void setCodiceMaxFatture(int codiceMaxFatture) {
		if (this.codiceMaxFatture == 0 || codiceMaxFatture > this.codiceMaxFatture) {
			this.codiceMaxFatture = codiceMaxFatture;
		}
	}

	/**
	 * @param codiceMinCarichi
	 *            the codiceMinCarichi to set
	 */
	public void setCodiceMinCarichi(int codiceMinCarichi) {
		if (this.codiceMinCarichi == 0 || codiceMinCarichi < this.codiceMinCarichi) {
			this.codiceMinCarichi = codiceMinCarichi;
		}
	}

	/**
	 * @param codiceMinDdt
	 *            the codiceMinDdt to set
	 */
	public void setCodiceMinDdt(int codiceMinDdt) {
		if (this.codiceMinDdt == 0 || codiceMinDdt < this.codiceMinDdt) {
			this.codiceMinDdt = codiceMinDdt;
		}
	}

	/**
	 * @param codiceMinFatture
	 *            the codiceMinFatture to set
	 */
	public void setCodiceMinFatture(int codiceMinFatture) {
		if (this.codiceMinFatture == 0 || codiceMinFatture < this.codiceMinFatture) {
			this.codiceMinFatture = codiceMinFatture;
		}
	}

	/**
	 * @param dataFine
	 *            the dataFine to set, chiamandolo più volte, imposta l'ultima data in ordine temporale
	 */
	public void setDataFine(Date dataFine) {
		if (this.dataFine == null) {
			this.dataFine = dataFine;
		} else {
			if (dataFine.after(this.dataFine)) {
				this.dataFine = dataFine;
			}
		}
	}

	/**
	 * @param dataInizio
	 *            the dataInizio to set, chiamandolo più volte imposta la prima data in ordine temporale
	 */
	public void setDataInizio(Date dataInizio) {
		if (this.dataInizio == null) {
			this.dataInizio = dataInizio;
		} else {
			if (dataInizio.before(this.dataInizio)) {
				this.dataInizio = dataInizio;
			}
		}
	}

	/**
	 * @param imponibileCarichi
	 *            the imponibileCarichi to set
	 */
	public void setImponibileCarichi(BigDecimal imponibileCarichi) {
		this.imponibileCarichi = imponibileCarichi;
	}

	/**
	 * @param imponibileDdt
	 *            the imponibileDdt to set
	 */
	public void setImponibileDdt(BigDecimal imponibileDdt) {
		this.imponibileDdt = imponibileDdt;
	}

	/**
	 * @param imponibileFatture
	 *            the imponibileFatture to set
	 */
	public void setImponibileFatture(BigDecimal imponibileFatture) {
		this.imponibileFatture = imponibileFatture;
	}

	/**
	 * @param impostaCarichi
	 *            the impostaCarichi to set
	 */
	public void setImpostaCarichi(BigDecimal impostaCarichi) {
		this.impostaCarichi = impostaCarichi;
	}

	/**
	 * @param impostaDdt
	 *            the impostaDdt to set
	 */
	public void setImpostaDdt(BigDecimal impostaDdt) {
		this.impostaDdt = impostaDdt;
	}

	/**
	 * @param impostaFatture
	 *            the impostaFatture to set
	 */
	public void setImpostaFatture(BigDecimal impostaFatture) {
		this.impostaFatture = impostaFatture;
	}

	/**
	 * @param numeroCarichi
	 *            the numeroCarichi to set
	 */
	public void setNumeroCarichi(int numeroCarichi) {
		this.numeroCarichi = numeroCarichi;
	}

	/**
	 * @param numeroDdt
	 *            the numeroDdt to set
	 */
	public void setNumeroDdt(int numeroDdt) {
		this.numeroDdt = numeroDdt;
	}

	/**
	 * @param numeroFatture
	 *            the numeroFatture to set
	 */
	public void setNumeroFatture(int numeroFatture) {
		this.numeroFatture = numeroFatture;
	}

}
