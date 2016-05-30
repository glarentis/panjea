package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.AggiungiVariazioneDialog;

import java.math.BigDecimal;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public abstract class AbstractAggiungiVariazioneCommand extends ApplicationWindowAwareCommand {

	public static final String AREA_DOCUMENTO_KEY = "areaDocumento";

	/**
	 * Costruttore.
	 */
	public AbstractAggiungiVariazioneCommand() {
		super("aggiungiVariazioneCommand");
		RcpSupport.configure(this);
	}

	/**
	 * Esegue la chiamata al BD.
	 * 
	 * @param idArea
	 *            idArea
	 * @param variazione
	 *            variazione
	 * @param percProvvigione
	 *            percentuale provvigione agente
	 * @param variazioneScontoStrategy
	 *            strategia di variazione dello sconto
	 * @param tipoVariazioneScontoStrategy
	 *            tipo di variazione dello sconto
	 * @param variazioneProvvigioneStrategy
	 *            strategia di variazione della provvigione
	 * @param tipoVariazioneProvvigioneStrategy
	 *            tipo di variazione della provvigione
	 */
	protected abstract void aggiungiVariazione(Integer idArea, BigDecimal variazione, BigDecimal percProvvigione,
			RigaDocumentoVariazioneScontoStrategy scontoStrategy,
			TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
			RigaDocumentoVariazioneProvvigioneStrategy provvigioneStrategy,
			TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy);

	/**
	 * 
	 * @return il dialog per l'aggiunta della variazione.
	 */
	protected AggiungiVariazioneDialog creaAggiungiVarizionaDialog() {
		return new AggiungiVariazioneDialog();
	}

	@Override
	protected void doExecuteCommand() {
		Integer idArea = (Integer) getParameter(AREA_DOCUMENTO_KEY);

		AggiungiVariazioneDialog dialog = creaAggiungiVarizionaDialog();
		dialog.showDialog();

		BigDecimal variazione = dialog.getVariazione();
		RigaDocumentoVariazioneScontoStrategy scontoStrategy = dialog.getVariazioneScontoStrategy();
		TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy = dialog.getTipoVariazioneScontoStrategy();

		BigDecimal percProvvigione = dialog.getProvvigione();
		RigaDocumentoVariazioneProvvigioneStrategy provvigioneStrategy = dialog.getVariazioneProvvigioneStrategy();
		TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy = dialog
				.getTipoVariazioneProvvigioneStrategy();

		if (variazione != null || percProvvigione != null) {
			aggiungiVariazione(idArea, variazione, percProvvigione, scontoStrategy, tipoVariazioneScontoStrategy,
					provvigioneStrategy, tipoVariazioneProvvigioneStrategy);
		}
	}
}
