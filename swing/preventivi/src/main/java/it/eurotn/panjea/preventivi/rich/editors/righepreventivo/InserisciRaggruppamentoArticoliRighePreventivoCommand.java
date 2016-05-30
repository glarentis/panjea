package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.InserisciRaggruppamentoArticoliCommand;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.bd.PreventiviBD;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.math.BigDecimal;

import javax.swing.AbstractButton;

import org.springframework.richclient.util.RcpSupport;

public class InserisciRaggruppamentoArticoliRighePreventivoCommand extends InserisciRaggruppamentoArticoliCommand {

	/**
	 * Inserisce le righe per il raggruppamento.
	 * 
	 * @param raggruppamento
	 *            raggruppamento con le righe da inserire.
	 */
	@Override
	protected void inserisciRighe(RaggruppamentoArticoli raggruppamento) {
		IPreventiviBD preventiviBD = RcpSupport.getBean(PreventiviBD.BEAN_ID);
		AreaPreventivo areaPreventivo = (AreaPreventivo) getParameter(AREA_DOCUMENTO_KEY);
		if (areaPreventivo == null) {
			throw new IllegalArgumentException("impostare l'area preventivo");
		}
		AreaRate areaRate = (AreaRate) getParameter(AREA_RATE_KEY, null);
		if (areaRate == null) {
			throw new IllegalArgumentException("Impostare l'area rate");
		}
		Integer idListinoAlternativo = null;
		Integer idListino = null;
		Integer idSedeEntita = null;
		Integer idTipoMezzo = null;
		Integer idZonaGeografica = null;
		String codiceLingua = null;
		Integer idAgente = null;
		BigDecimal percentualeScontoCommerciale = null;

		if (areaPreventivo.getListinoAlternativo() != null) {
			idListinoAlternativo = areaPreventivo.getListinoAlternativo().getId();
		}
		if (areaPreventivo.getListino() != null) {
			idListino = areaPreventivo.getListino().getId();
		}
		if (areaPreventivo.getDocumento().getSedeEntita() != null) {
			idSedeEntita = areaPreventivo.getDocumento().getSedeEntita().getId();
			codiceLingua = areaPreventivo.getDocumento().getSedeEntita().getLingua();
		}
		if (areaRate.getCodicePagamento() != null) {
			percentualeScontoCommerciale = areaRate.getCodicePagamento().getPercentualeScontoCommerciale();
		}

		preventiviBD.inserisciRaggruppamentoArticoli(areaPreventivo.getId(), ProvenienzaPrezzo.LISTINO, raggruppamento
				.getId(), areaPreventivo.getDocumento().getDataDocumento(), idSedeEntita, idListinoAlternativo,
				idListino, areaPreventivo.getDocumento().getTotale(), areaPreventivo.getCodiceIvaAlternativo(),
				idTipoMezzo, idZonaGeografica, areaPreventivo.getTipoAreaPreventivo().isNoteSuDestinazione(),
				areaPreventivo.getDocumento().getTotale().getCodiceValuta(), codiceLingua, idAgente, areaPreventivo
						.getTipologiaCodiceIvaAlternativo(), percentualeScontoCommerciale);
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setName("RaggruppamentoArticoliPreventivoCommand");
	}
}
