/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.domain.Importo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Contiene due valori di saldo per un conto per poterli conforntare.
 *
 * @author Leonardo,Giangi
 */
public class SaldoContoConfronto extends SaldoConto {

	private static final long serialVersionUID = 8079227378621743585L;

	private Map<String, SaldoContoConfronto> centriCostoConfronto;

	private BigDecimal importoDare2;
	private BigDecimal importoAvere2;

	/**
	 * Costruisce un saldo a confronto. Se saldo1=true allora il saldo che passo è saldo1, altrimenti verranno settati i
	 * campi del saldo2.
	 *
	 * @param saldoConto
	 *            saldoConto
	 * @param saldo1
	 *            saldo1
	 */
	public SaldoContoConfronto(final SaldoConto saldoConto, final boolean saldo1) {
		super();
		setMastroCodice(saldoConto.getMastroCodice());
		setMastroDescrizione(saldoConto.getMastroDescrizione());
		setMastroId(saldoConto.getMastroId());
		setContoCodice(saldoConto.getContoCodice());
		setContoDescrizione(saldoConto.getContoDescrizione());
		setContoId(saldoConto.getContoId());
		setSottoContoCodice(saldoConto.getSottoContoCodice());
		setSottoContoDescrizione(saldoConto.getSottoContoDescrizione());
		setSottoContoId(saldoConto.getSottoContoId());
		setTipoConto(saldoConto.getTipoConto());
		setSottoTipoConto(saldoConto.getSottoTipoConto());
		setCentroCostoCodice(saldoConto.getCentroCostoCodice());
		setCentroCostoDescrizione(saldoConto.getCentroCostoDescrizione());
		setCentroCostoId(saldoConto.getCentroCostoId());
		setImportoAvere(BigDecimal.ZERO);
		setImportoDare(BigDecimal.ZERO);
		this.importoDare2 = BigDecimal.ZERO;
		this.importoAvere2 = BigDecimal.ZERO;
		if (saldo1) {
			aggiungiImportoAvere(saldoConto.getImportoAvere());
			aggiungiImportoDare(saldoConto.getImportoDare());
		} else {
			aggiungiImportoAvere2(saldoConto.getImportoAvere());
			aggiungiImportoDare2(saldoConto.getImportoDare());
		}
		if (saldoConto.getCentriCosto() != null) {
			for (SaldoConto saldoCentroCosto : saldoConto.getCentriCosto()) {
				SaldoContoConfronto saldoContoConfronto = new SaldoContoConfronto(saldoCentroCosto, true);
				aggiungiCentroCostoConfronto(saldoContoConfronto);
			}
		}
	}

	/**
	 * @param saldoContoCentroCostoConfronto
	 *            saldo conto centro costo da aggiungere
	 */
	private void aggiungiCentroCostoConfronto(SaldoContoConfronto saldoContoCentroCostoConfronto) {
		if (centriCostoConfronto == null) {
			centriCostoConfronto = new TreeMap<>();
		}
		centriCostoConfronto.put(saldoContoCentroCostoConfronto.getCentroCostoCodice(), saldoContoCentroCostoConfronto);
	}

	/**
	 * Aggiunge importo dare e avere 2 per i centri di costo presenti.
	 *
	 * @param centriCosto
	 *            centriCosto
	 */
	public void aggiungiImporti2CentriCosto(List<SaldoConto> centriCosto) {
		if (centriCosto == null) {
			return;
		}
		if (centriCostoConfronto == null) {
			centriCostoConfronto = new TreeMap<>();
		}
		for (SaldoConto saldoConto : centriCosto) {
			SaldoContoConfronto saldoContoConfronto = centriCostoConfronto.get(saldoConto.getCentroCostoCodice());
			if (saldoContoConfronto != null) {
				saldoContoConfronto.aggiungiImportoDare2(saldoConto.getImportoDare());
				saldoContoConfronto.aggiungiImportoAvere2(saldoConto.getImportoAvere());
			} else {
				SaldoContoConfronto saldoTmpConfronto = new SaldoContoConfronto(saldoConto, false);
				saldoTmpConfronto.setCentroCostoCodice(saldoConto.getCentroCostoCodice());
				saldoTmpConfronto.setCentroCostoDescrizione(saldoConto.getCentroCostoDescrizione());
				saldoTmpConfronto.setCentroCostoId(saldoConto.getCentroCostoId());
				aggiungiCentroCostoConfronto(saldoTmpConfronto);
			}
		}
	}

	/**
	 * Aggiunge un importo all'avere.
	 *
	 * @param valore
	 *            valore da aggiungere
	 */
	public void aggiungiImportoAvere2(BigDecimal valore) {
		if (valore != null) {
			importoAvere2 = importoAvere2.add(valore);
		}
	}

	/**
	 * Aggiunge un importo al dare.
	 *
	 * @param valore
	 *            valore da aggiungere
	 */
	public void aggiungiImportoDare2(BigDecimal valore) {
		if (valore != null) {
			importoDare2 = importoDare2.add(valore);
		}
	}

	/**
	 * @return the centriCostoConfronto
	 */
	public List<SaldoContoConfronto> getCentriCostoConfronto() {
		if (centriCostoConfronto == null) {
			centriCostoConfronto = new TreeMap<>();
		}
		Collection<SaldoContoConfronto> saldiConfronto = centriCostoConfronto.values();
		return new ArrayList<>(saldiConfronto);
	}

	/**
	 * @return SaldoConfronto - saldoIniziale
	 */
	public BigDecimal getDifferenzaSaldi() {
		return getSaldo2().subtract(getSaldo());
	}

	/**
	 * @return the importoAvereConfronto
	 */
	public BigDecimal getImportoAvere2() {
		return importoAvere2;
	}

	/**
	 * @return the importoDareConfronto
	 */
	public BigDecimal getImportoDare2() {
		return importoDare2;
	}

	/**
	 * @return la differenza percentuale = (differenzaSaldi/differenzaIniziale)*100; nel caso in cui invece la
	 *         differenzaIniziale==0 restituisco 100
	 */
	public BigDecimal getPercentualeSaldi() {
		if (getSaldo().compareTo(BigDecimal.ZERO) == 0) {
			return Importo.HUNDRED;
		}
		return getDifferenzaSaldi().divide(getSaldo(), 4, BigDecimal.ROUND_HALF_UP).multiply(Importo.HUNDRED);
	}

	/**
	 * @return il saldo di confronto
	 */
	public BigDecimal getSaldo2() {
		return importoDare2.subtract(importoAvere2);
	}

	/**
	 * @param importoAvere
	 *            the importoAvere to set
	 */
	public void setImportoAvere2(BigDecimal importoAvere) {
		this.importoAvere2 = importoAvere;
	}

	/**
	 * @param importoDare
	 *            the importoDare to set
	 */
	public void setImportoDare2(BigDecimal importoDare) {
		this.importoDare2 = importoDare;
	}

}
