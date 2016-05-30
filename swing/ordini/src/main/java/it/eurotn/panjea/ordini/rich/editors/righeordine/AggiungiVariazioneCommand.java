package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.AggiungiVariazioneDialog;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;

import java.math.BigDecimal;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Aggiunge a tutte le righe dell'ordine una variazione. (in fondo ad eventuali altre variazioni).
 * 
 * @author giangi
 * @version 1.0, 12/dic/2011
 * 
 */
public class AggiungiVariazioneCommand extends ApplicationWindowAwareCommand {

	public static final String AREA_DOCUMENTO_KEY = "areaDocumento";

	/**
	 * Costruttore.
	 */
	public AggiungiVariazioneCommand() {
		super("aggiungiVariazioneCommand");
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		Integer idAreaOrdine = (Integer) getParameter(AREA_DOCUMENTO_KEY);

		AggiungiVariazioneDialog dialog = new AggiungiVariazioneDialog();
		dialog.showDialog();

		BigDecimal variazione = dialog.getVariazione();
		RigaDocumentoVariazioneScontoStrategy scontoStrategy = dialog.getVariazioneScontoStrategy();
		TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy = dialog.getTipoVariazioneScontoStrategy();

		BigDecimal percProvvigione = dialog.getProvvigione();
		RigaDocumentoVariazioneProvvigioneStrategy provvigioneStrategy = dialog.getVariazioneProvvigioneStrategy();
		TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy = dialog
				.getTipoVariazioneProvvigioneStrategy();

		if (variazione != null || percProvvigione != null) {
			IOrdiniDocumentoBD ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
			ordiniDocumentoBD.aggiungiVariazione(idAreaOrdine, variazione, percProvvigione, scontoStrategy,
					tipoVariazioneScontoStrategy, provvigioneStrategy, tipoVariazioneProvvigioneStrategy);
		}
	}
}
