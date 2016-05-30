package it.eurotn.panjea.contabilita.domain;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Mappa per gestire i tipiDocumentoBase associati alla loro tipoOperazione. Estende una mappa dove la chiave è
 * {@link TipoOperazioneTipoDocumento} e il valore il {@link TipoDocumentoBase}
 * 
 * @author giangi
 * 
 */
public class TipiDocumentoBase extends HashMap<TipoOperazioneTipoDocumento, TipoAreaContabile> {

	private static final long serialVersionUID = 3094745443406814375L;

	/**
	 * Restituisce il tipo operazione di apertura in base al tipo conto.
	 * 
	 * @param tipoConto
	 *            tipo conto
	 * @return tipo operazione
	 */
	public static TipoOperazioneTipoDocumento getTipoOperazioneAperturaPerTipoConto(TipoConto tipoConto) {
		switch (tipoConto) {
		case ECONOMICO:
			return null;
		case PATRIMONIALE:
			return TipoOperazioneTipoDocumento.APERTURA_CONTO_PATRIMONIALE;
		case ORDINE:
			return TipoOperazioneTipoDocumento.APERTURA_CONTO_ORDINE;
		default:
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Restituisce il tipo operazione di chiusura in base al tipo conto.
	 * 
	 * @param tipoConto
	 *            tipo conto
	 * @return tipo operazione
	 */
	public static TipoOperazioneTipoDocumento getTipoOperazioneChiusuraPerTipoConto(TipoConto tipoConto) {
		switch (tipoConto) {
		case ECONOMICO:
			return TipoOperazioneTipoDocumento.CHIUSURA_CONTO_ECONOMICO;
		case PATRIMONIALE:
			return TipoOperazioneTipoDocumento.CHIUSURA_CONTO_PATRIMONIALE;
		case ORDINE:
			return TipoOperazioneTipoDocumento.CHIUSURA_CONTO_ORDINE;
		default:
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param tipiDocumentoList
	 *            tipi documento base
	 */
	public TipiDocumentoBase(final List<TipoDocumentoBase> tipiDocumentoList) {
		for (TipoDocumentoBase base : tipiDocumentoList) {
			put(base.getTipoOperazione(), base.getTipoAreaContabile());
		}
	}

	/**
	 * Restituisce i tipi documento di apertura e chiusura in base al tipo conto.
	 * 
	 * @param tipoConto
	 *            tipo conto
	 * @return tipi documento
	 * @throws TipoDocumentoBaseException
	 *             eccezione generica
	 */
	public List<TipoAreaContabile> getTipiDocumentiAperturaChisuraPerTipoConto(TipoConto tipoConto)
			throws TipoDocumentoBaseException {
		List<TipoAreaContabile> tipiAreeContabiliChiusuraApertura = new ArrayList<TipoAreaContabile>();

		// Carico il tipoAreaContabile per l'apertura
		if (getTipoOperazioneAperturaPerTipoConto(tipoConto) != null
				&& containsKey(getTipoOperazioneAperturaPerTipoConto(tipoConto))) {
			tipiAreeContabiliChiusuraApertura.add(get(getTipoOperazioneAperturaPerTipoConto(tipoConto)));
		}

		// Carico il tipoAreaContabile per la chiusura
		if (getTipoOperazioneChiusuraPerTipoConto(tipoConto) != null
				&& containsKey(getTipoOperazioneChiusuraPerTipoConto(tipoConto))) {
			tipiAreeContabiliChiusuraApertura.add(get(getTipoOperazioneChiusuraPerTipoConto(tipoConto)));
		}

		int size = tipiAreeContabiliChiusuraApertura.size();

		// se il tipoConto e' economico c'e' solo la chiusura, per tipo patrimoniale e ordine c'e' apertura e chiusura
		// qui lancio l'exception se queste condizioni non sono soddisfatte
		if (!(size == 1 && TipoConto.ECONOMICO.equals(tipoConto) || size == 2 && !TipoConto.ECONOMICO.equals(tipoConto))) {
			throw new TipoDocumentoBaseException(new String[] { "Tipo conto " + tipoConto.name() });
		}

		return tipiAreeContabiliChiusuraApertura;
	}

	/**
	 * Restituisce i tipi documento di apertura e chiusura.
	 * 
	 * @return tipi documento
	 * @throws TipoDocumentoBaseException
	 *             eccezione generica
	 */
	public List<TipoAreaContabile> getTipiDocumentiAperturaChiusura() throws TipoDocumentoBaseException {
		List<TipoAreaContabile> tipiAreeContabiliChiusuraApertura = new ArrayList<TipoAreaContabile>();

		List<TipoAreaContabile> tipiAreeContabiliChiusuraAperturaEconomico = getTipiDocumentiAperturaChisuraPerTipoConto(TipoConto.ECONOMICO);
		if (!tipiAreeContabiliChiusuraAperturaEconomico.isEmpty()) {
			tipiAreeContabiliChiusuraApertura.addAll(tipiAreeContabiliChiusuraAperturaEconomico);
		}

		List<TipoAreaContabile> tipiAreeContabiliChiusuraAperturaOrdine = getTipiDocumentiAperturaChisuraPerTipoConto(TipoConto.ORDINE);
		if (!tipiAreeContabiliChiusuraAperturaOrdine.isEmpty()) {
			tipiAreeContabiliChiusuraApertura.addAll(tipiAreeContabiliChiusuraAperturaOrdine);
		}

		List<TipoAreaContabile> tipiAreeContabiliChiusuraAperturaPartimoniale = getTipiDocumentiAperturaChisuraPerTipoConto(TipoConto.PATRIMONIALE);
		if (!tipiAreeContabiliChiusuraAperturaPartimoniale.isEmpty()) {
			tipiAreeContabiliChiusuraApertura.addAll(tipiAreeContabiliChiusuraAperturaPartimoniale);
		}

		return tipiAreeContabiliChiusuraApertura;
	}
}
