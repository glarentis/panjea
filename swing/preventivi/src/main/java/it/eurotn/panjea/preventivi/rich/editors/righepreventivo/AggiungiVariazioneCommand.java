package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.AggiungiVariazioneDialog;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.bd.PreventiviBD;

import java.math.BigDecimal;

import org.springframework.richclient.util.RcpSupport;

public class AggiungiVariazioneCommand extends AbstractAggiungiVariazioneCommand {

	@Override
	protected void aggiungiVariazione(Integer idArea, BigDecimal variazione, BigDecimal percProvvigione,
			RigaDocumentoVariazioneScontoStrategy scontoStrategy,
			TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
			RigaDocumentoVariazioneProvvigioneStrategy provvigioneStrategy,
			TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
		IPreventiviBD preventiviBD = RcpSupport.getBean(PreventiviBD.BEAN_ID);
		preventiviBD.aggiungiVariazione(idArea, variazione, percProvvigione, scontoStrategy,
				tipoVariazioneScontoStrategy, provvigioneStrategy, tipoVariazioneProvvigioneStrategy);
	}

	@Override
	protected AggiungiVariazioneDialog creaAggiungiVarizionaDialog() {
		return new AggiungiVariazioneDialog(false);
	}
}
