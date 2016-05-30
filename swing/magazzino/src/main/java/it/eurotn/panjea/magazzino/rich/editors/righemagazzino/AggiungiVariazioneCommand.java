package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.rich.command.ICancellable;

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
public class AggiungiVariazioneCommand extends ApplicationWindowAwareCommand implements ICancellable {

	public static final String AREA_DOCUMENTO_KEY = "areaDocumento";
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private boolean cancelled;

	/**
	 * Costruttore.
	 */
	public AggiungiVariazioneCommand() {
		super("aggiungiVariazioneCommand");
		RcpSupport.configure(this);
		this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {
		Integer idAreaMagazzino = (Integer) getParameter(AREA_DOCUMENTO_KEY, null);
		AggiungiVariazioneDialog dialog = new AggiungiVariazioneDialog();
		dialog.showDialog();

		BigDecimal variazione = dialog.getVariazione();
		RigaDocumentoVariazioneScontoStrategy scontoStrategy = dialog.getVariazioneScontoStrategy();
		TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy = dialog.getTipoVariazioneScontoStrategy();

		BigDecimal percProvvigione = dialog.getProvvigione();
		RigaDocumentoVariazioneProvvigioneStrategy provvigioneStrategy = dialog.getVariazioneProvvigioneStrategy();
		TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy = dialog
				.getTipoVariazioneProvvigioneStrategy();

		cancelled = true;
		if (variazione != null || percProvvigione != null) {
			cancelled = false;
			magazzinoDocumentoBD.aggiungiVariazione(idAreaMagazzino, variazione, percProvvigione, scontoStrategy,
					tipoVariazioneScontoStrategy, provvigioneStrategy, tipoVariazioneProvvigioneStrategy);
		}
	}

	/**
	 * 
	 * @return true se è stata anullata l'operazione
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
}
