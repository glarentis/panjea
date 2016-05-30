package it.eurotn.panjea.lotti.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Categoria;

public class StatisticaLotto implements Serializable {

	public enum StatoLotto {
		APERTO, CHIUSO
	}

	private static final long serialVersionUID = 3737362937806160623L;

	private DepositoLite deposito;

	private Lotto lotto;
	private LottoInterno lottoInterno;

	private Double rimanenza;
	private Double quantitaCarico;
	private Double quantitaScarico;

	private Integer lottoid;
	private Integer lottoVersione;
	private String lottoCodice;
	private Date lottoScadenza;
	private Integer lottoPriorita;
	private String lottoTipo;

	private Integer depositoId;
	private String depositoCodice;
	private String depositoDescrizione;
	private Integer articoloId;
	private Integer articoloVersion;

	private boolean articoloAbilitato;
	private String articoloBarCode;
	private String articoloCodice;

	private String articoloDescrizione;
	private Integer articoloNumDecPrezzo;
	private Integer articoloNumDecQta;

	private Integer categoriaId;
	private String categoriaCodice;
	private String categoriaDescrizione;

	{
		lottoInterno = new LottoInterno();
	}

	/**
	 * Costruttore.
	 *
	 */
	public StatisticaLotto() {
		super();
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
		StatisticaLotto other = (StatisticaLotto) obj;
		if (depositoId == null) {
			if (other.depositoId != null) {
				return false;
			}
		} else if (!depositoId.equals(other.depositoId)) {
			return false;
		}

		if (lottoInterno != null && lottoInterno.getId() != null) {
			if (lottoInterno != null && lottoInterno.getId() != null) {
				if (lottoInterno == null) {
					if (other.lottoInterno != null) {
						return false;
					}
				} else if (!lottoInterno.equals(other.lottoInterno)) {
					return false;
				}
			}
		} else {
			if (!Objects.equals(lottoid, other.lottoid)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Articolo del lotto o lotto interno.
	 *
	 * @return articolo
	 */
	public ArticoloLite getArticolo() {

		ArticoloLite articolo = null;

		if (lottoInterno != null && lottoInterno.getId() != null) {
			articolo = lottoInterno.getArticolo();
		} else {
			articolo = getLotto().getArticolo();
		}

		return articolo;
	}

	/**
	 * @return the articoloVersion
	 */
	public Integer getArticoloVersion() {
		return articoloVersion;
	}

	/**
	 * @return the deposito
	 */
	public DepositoLite getDeposito() {
		if (deposito == null && depositoId != null) {
			deposito = new DepositoLite();
			deposito.setId(depositoId);
			deposito.setCodice(depositoCodice);
			deposito.setDescrizione(depositoDescrizione);
		}
		return deposito;
	}

	/**
	 * @return the lotto
	 */
	public Lotto getLotto() {
		if (lotto == null && lottoid != null) {
			if ("F".equals(lottoTipo)) {
				lotto = new Lotto();
			} else {
				lotto = new LottoInterno();
			}
			lotto.setId(lottoid);
			lotto.setVersion(lottoVersione);
			lotto.setCodice(lottoCodice);
			lotto.setDataScadenza(lottoScadenza);
			lotto.setPriorita(lottoPriorita);
			lotto.setArticolo(new ArticoloLite());
			lotto.getArticolo().setId(articoloId);
			lotto.getArticolo().setVersion(articoloVersion);
			lotto.getArticolo().setCodice(articoloCodice);
			lotto.getArticolo().setDescrizione(articoloDescrizione);
			lotto.getArticolo().setAbilitato(articoloAbilitato);
			lotto.getArticolo().setBarCode(articoloBarCode);
			lotto.getArticolo().setNumeroDecimaliPrezzo(articoloNumDecPrezzo);
			lotto.getArticolo().setNumeroDecimaliQta(articoloNumDecQta);
			lotto.getArticolo().setCategoria(new Categoria());
			lotto.getArticolo().getCategoria().setId(categoriaId);
			lotto.getArticolo().getCategoria().setCodice(categoriaCodice);
			lotto.getArticolo().getCategoria().setDescrizione(categoriaDescrizione);
			lotto.setRimanenza(rimanenza);
		}
		return lotto;
	}

	/**
	 * @return the lottoInterno
	 */
	public LottoInterno getLottoInterno() {
		return lottoInterno;
	}

	/**
	 * Priorità del lotto o lotto interno.
	 *
	 * @return priorità
	 */
	public Integer getPrioritaLotto() {

		Integer priorita = 0;

		if (lottoInterno != null && lottoInterno.getId() != null) {
			priorita = lottoInterno.getPriorita();
		} else {
			priorita = getLotto().getPriorita();
		}

		return priorita;
	}

	/**
	 * @return the quantitaCarico
	 */
	public Double getQuantitaCarico() {
		return quantitaCarico;
	}

	/**
	 * @return the quantitaScarico
	 */
	public Double getQuantitaScarico() {
		return quantitaScarico;
	}

	/**
	 * @return the rimanenza
	 */
	public Double getRimanenza() {
		rimanenza = (rimanenza == null) ? 0.0 : rimanenza;
		BigDecimal val = BigDecimal.valueOf(rimanenza).setScale(getArticolo().getNumeroDecimaliQta(),
				RoundingMode.HALF_UP);
		return val.doubleValue();
	}

	/**
	 * Data di scadenza del lotto o lotto interno.
	 *
	 * @return scadenza
	 */
	public Date getScadenzaLotto() {

		Date scadenza = null;

		if (lottoInterno != null && lottoInterno.getId() != null) {
			scadenza = lottoInterno.getDataScadenza();
		} else {
			scadenza = getLotto().getDataScadenza();
		}

		return scadenza;
	}

	/**
	 * @return the statoLotto
	 */
	public StatoLotto getStatoLotto() {

		StatoLotto statoLotto = StatoLotto.CHIUSO;

		if (getRimanenza().compareTo(0.0) > 0) {
			statoLotto = StatoLotto.APERTO;
		}

		return statoLotto;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((depositoId == null) ? 0 : depositoId.hashCode());
		result = prime * result + ((lottoid == null) ? 0 : lottoid.hashCode());
		result = prime * result + ((lottoInterno == null) ? 0 : lottoInterno.hashCode());
		return result;
	}

	/**
	 * @param articoloAbilitatoParam
	 *            the deposito to set
	 */
	public void setArticoloAbilitato(boolean articoloAbilitatoParam) {
		this.articoloAbilitato = articoloAbilitatoParam;
	}

	/**
	 * @param articoloBarCodeParam
	 *            the deposito to set
	 */
	public void setArticoloBarCode(String articoloBarCodeParam) {
		this.articoloBarCode = articoloBarCodeParam;
	}

	/**
	 * @param articoloCodiceParam
	 *            the deposito to set
	 */
	public void setArticoloCodice(String articoloCodiceParam) {
		this.articoloCodice = articoloCodiceParam;
	}

	/**
	 * @param articoloDescrizioneParam
	 *            the deposito to set
	 */
	public void setArticoloDescrizione(String articoloDescrizioneParam) {
		this.articoloDescrizione = articoloDescrizioneParam;
	}

	/**
	 * @param articoloIdParam
	 *            the deposito to set
	 */
	public void setArticoloId(Integer articoloIdParam) {
		this.articoloId = articoloIdParam;
	}

	/**
	 * @param articoloNumDecPrezzoParam
	 *            the deposito to set
	 */
	public void setArticoloNumDecPrezzo(Integer articoloNumDecPrezzoParam) {
		this.articoloNumDecPrezzo = articoloNumDecPrezzoParam;
	}

	/**
	 * @param articoloNumDecQtaParam
	 *            the deposito to set
	 */
	public void setArticoloNumDecQta(Integer articoloNumDecQtaParam) {
		this.articoloNumDecQta = articoloNumDecQtaParam;
	}

	/**
	 * @param articoloVersion
	 *            the articoloVersion to set
	 */
	public void setArticoloVersion(Integer articoloVersion) {
		this.articoloVersion = articoloVersion;
	}

	/**
	 * @param categoriaCodiceParam
	 *            the deposito to set
	 */
	public void setCategoriaCodice(String categoriaCodiceParam) {
		this.categoriaCodice = categoriaCodiceParam;
	}

	/**
	 * @param categoriaDescrizioneParam
	 *            the deposito to set
	 */
	public void setCategoriaDescrizione(String categoriaDescrizioneParam) {
		this.categoriaDescrizione = categoriaDescrizioneParam;
	}

	/**
	 * @param categoriaIdParam
	 *            the deposito to set
	 */
	public void setCategoriaId(Integer categoriaIdParam) {
		this.categoriaId = categoriaIdParam;
	}

	/**
	 * @param depositoCodiceParam
	 *            the deposito to set
	 */
	public void setDepositoCodice(String depositoCodiceParam) {
		this.depositoCodice = depositoCodiceParam;
	}

	/**
	 * @param depositoDescrizioneParam
	 *            the deposito to set
	 */
	public void setDepositoDescrizione(String depositoDescrizioneParam) {
		this.depositoDescrizione = depositoDescrizioneParam;
	}

	/**
	 * @param depositoIdParam
	 *            the deposito to set
	 */
	public void setDepositoId(Integer depositoIdParam) {
		this.depositoId = depositoIdParam;
	}

	/**
	 * @param lottoCodiceParam
	 *            the lotto to set
	 */
	public void setLottoCodice(String lottoCodiceParam) {
		this.lottoCodice = lottoCodiceParam;
	}

	/**
	 * @param lottoidParam
	 *            the lotto to set
	 */
	public void setLottoId(Integer lottoidParam) {
		this.lottoid = lottoidParam;
	}

	/**
	 * @param lottoPrioritaParam
	 *            the lotto to set
	 */
	public void setLottoPriorita(Integer lottoPrioritaParam) {
		this.lottoPriorita = lottoPrioritaParam;
	}

	/**
	 * @param lottoScadenzaParam
	 *            the lotto to set
	 */
	public void setLottoScadenza(Date lottoScadenzaParam) {
		this.lottoScadenza = lottoScadenzaParam;
	}

	/**
	 * @param lottoTipoParam
	 *            the lotto to set
	 */
	public void setLottoTipo(String lottoTipoParam) {
		this.lottoTipo = lottoTipoParam;
	}

	/**
	 * @param lottoVersioneParam
	 *            the lotto to set
	 */
	public void setLottoVersione(Integer lottoVersioneParam) {
		this.lottoVersione = lottoVersioneParam;
	}

	/**
	 * @param quantitaCarico
	 *            the quantitaCarico to set
	 */
	public void setQuantitaCarico(Double quantitaCarico) {
		this.quantitaCarico = quantitaCarico;
	}

	/**
	 * @param quantitaScarico
	 *            the quantitaScarico to set
	 */
	public void setQuantitaScarico(Double quantitaScarico) {
		this.quantitaScarico = quantitaScarico;
	}

	/**
	 * @param rimanenza
	 *            the rimanenza to set
	 */
	public void setRimanenza(Double rimanenza) {
		// visto che non si riesce a fare un cast a un double, e visto che non si può fare un round senza che mi cambi
		// il tipo di dato risultante (Double o Integer), prendo un number e imposto il doublevalue
		if (rimanenza == null) {
			rimanenza = 0.0;
		}
		this.rimanenza = rimanenza;
	}

}
