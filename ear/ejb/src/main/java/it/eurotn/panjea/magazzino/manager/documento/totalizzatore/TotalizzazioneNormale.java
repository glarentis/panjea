package it.eurotn.panjea.magazzino.manager.documento.totalizzatore;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.panjea.magazzino.domain.TotaliArea;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(mappedName = "Panjea.TotalizzazioneNormale")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TotalizzazioneNormale")
public class TotalizzazioneNormale implements Totalizzatore {

	private static Logger logger = Logger.getLogger(TotalizzazioneNormale.class);

	/**
	 * Calcola il totale del documento.
	 * 
	 * @param documento
	 *            documento
	 * @param righeIva
	 *            righe iva
	 */
	protected void calcolaTotaleDocumento(Documento documento, List<RigaIva> righeIva) {
		Importo totaleDocumento = new Importo(documento.getTotale().getCodiceValuta(), documento.getTotale()
				.getTassoDiCambio());
		Importo imposta = totaleDocumento.clone();

		for (RigaIva rigaIva : righeIva) {
			totaleDocumento = totaleDocumento.add(rigaIva.getImponibile(), RigaIva.SCALE_FISCALE).add(
					rigaIva.getImposta(), RigaIva.SCALE_FISCALE);
			imposta = imposta.add(rigaIva.getImposta(), RigaIva.SCALE_FISCALE);
		}
		if (documento.getTipoDocumento().isNotaCreditoEnable()) {
			totaleDocumento = totaleDocumento.negate();
			imposta = imposta.negate();
		}
		documento.setTotale(totaleDocumento);
		documento.setImposta(imposta);
	}

	/**
	 * 
	 * Calcola tutti i totali delle categorie del documento ( spese trasporto, altre spese, totale merce ).
	 * 
	 * @param totaliArea
	 *            .
	 * @param documento
	 *            .
	 * @param executor
	 *            .
	 */
	protected void calcolaTotaliCategorie(TotaliArea totaliArea, Documento documento,
			ITotalizzatoriQueryExecutor executor) {

		List<Object[]> totalizzatori = executor.execute();

		// Azzero ed inizializzo i totalizzatori prima di calcolarli
		totaliArea.getSpeseTrasporto().setCodiceValuta(documento.getTotale().getCodiceValuta());
		totaliArea.getSpeseTrasporto().setTassoDiCambio(documento.getTotale().getTassoDiCambio());
		totaliArea.getSpeseTrasporto().setImportoInValuta(BigDecimal.ZERO);
		totaliArea.getSpeseTrasporto().setImportoInValutaAzienda(BigDecimal.ZERO);

		totaliArea.getAltreSpese().setCodiceValuta(documento.getTotale().getCodiceValuta());
		totaliArea.getAltreSpese().setTassoDiCambio(documento.getTotale().getTassoDiCambio());
		totaliArea.getAltreSpese().setImportoInValuta(BigDecimal.ZERO);
		totaliArea.getAltreSpese().setImportoInValutaAzienda(BigDecimal.ZERO);

		totaliArea.getTotaleMerce().setCodiceValuta(documento.getTotale().getCodiceValuta());
		totaliArea.getTotaleMerce().setTassoDiCambio(documento.getTotale().getTassoDiCambio());
		totaliArea.getTotaleMerce().setImportoInValuta(BigDecimal.ZERO);
		totaliArea.getTotaleMerce().setImportoInValutaAzienda(BigDecimal.ZERO);

		for (Object[] totalizzatore : totalizzatori) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Aggiorno totalizzatore " + totalizzatore[2] + " con valore " + totalizzatore[0]);
			}
			switch ((ETipoArticolo) totalizzatore[2]) {
			case SPESE_TRASPORTO:
				totaliArea.getSpeseTrasporto().setImportoInValuta((BigDecimal) totalizzatore[0]);
				totaliArea.getSpeseTrasporto().setImportoInValutaAzienda((BigDecimal) totalizzatore[1]);
				break;
			case SPESE_ALTRE:

				totaliArea.getAltreSpese().setImportoInValuta((BigDecimal) totalizzatore[0]);
				totaliArea.getAltreSpese().setImportoInValutaAzienda((BigDecimal) totalizzatore[1]);
				break;
			default:
				// Sommo le altre tipologie di articolo sul totalizzatore totale
				// merce
				BigDecimal totaleMerceValuta = totaliArea.getTotaleMerce().getImportoInValuta()
						.add((BigDecimal) totalizzatore[0]);
				BigDecimal totaleMerceValutaAzienda = totaliArea.getTotaleMerce().getImportoInValuta()
						.add((BigDecimal) totalizzatore[1]);
				totaliArea.getTotaleMerce().setImportoInValuta(totaleMerceValuta);
				totaliArea.getTotaleMerce().setImportoInValutaAzienda(totaleMerceValutaAzienda);
				break;
			}
		}
	}

	@Override
	public Documento totalizzaDocumento(Documento documento, TotaliArea totaliArea,
			ITotalizzatoriQueryExecutor executor, List<RigaIva> righeIva) {
		logger.debug("--> Enter totalizzaDocumento");

		calcolaTotaleDocumento(documento, righeIva);

		calcolaTotaliCategorie(totaliArea, documento, executor);

		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit totalizzaDocumento con documeto " + documento.getId());
		}
		return documento;
	}

}
