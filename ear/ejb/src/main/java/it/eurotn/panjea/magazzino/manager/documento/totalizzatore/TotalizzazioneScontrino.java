package it.eurotn.panjea.magazzino.manager.documento.totalizzatore;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.TotaliArea;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(mappedName = "Panjea.TotalizzazioneScontrino")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TotalizzazioneScontrino")
public class TotalizzazioneScontrino extends TotalizzazioneNormale implements Totalizzatore {

	private static Logger logger = Logger.getLogger(TotalizzazioneScontrino.class);

	@EJB
	private RigaMagazzinoManager rigaMagazzinoManager;

	/**
	 * Sovrascrivo il metodo perchè il calcolo del totale documento deve risultare come la somma riga per riga
	 * dell'importo ivato moltiplicato per la quantità.
	 * 
	 * @param documento
	 *            documento
	 * @param totaliArea
	 *            totali area
	 * @param executor
	 *            totalizzatore
	 */
	@Override
	protected void calcolaTotaliCategorie(TotaliArea totaliArea, Documento documento,
			ITotalizzatoriQueryExecutor executor) {
		logger.debug("--> Enter calcolaTotaleDocumento");

		BigDecimal totaleDocumento = BigDecimal.ZERO;
		BigDecimal totaleImpostaDocumento = BigDecimal.ZERO;

		List<RigaArticolo> righeArticolo = rigaMagazzinoManager.getDao().caricaRigheArticolo(
				(AreaMagazzino) executor.getAreaDocumento());

		Integer numDecimali = 2;
		for (RigaArticolo rigaArticolo : righeArticolo) {

			BigDecimal qta = BigDecimal.valueOf((rigaArticolo).getQta());
			qta = qta.setScale(numDecimali, BigDecimal.ROUND_DOWN);

			BigDecimal totaleRiga = rigaArticolo.getPrezzoIvato().multiply(qta);
			totaleRiga = totaleRiga.setScale(2, BigDecimal.ROUND_HALF_UP);
			totaleDocumento = totaleDocumento.add(totaleRiga);
			BigDecimal impostaRiga = rigaArticolo.getImpostaIvata().multiply(qta);
			impostaRiga = impostaRiga.setScale(2, BigDecimal.ROUND_HALF_UP);
			totaleImpostaDocumento = totaleImpostaDocumento.add(impostaRiga);
		}

		Importo importoDocumento = new Importo(documento.getTotale().getCodiceValuta(), documento.getTotale()
				.getTassoDiCambio());
		importoDocumento.setImportoInValuta(totaleDocumento);
		importoDocumento.calcolaImportoValutaAzienda(RigaIva.SCALE_FISCALE);
		documento.setTotale(importoDocumento);

		Importo importoImposta = importoDocumento.clone();
		importoImposta.setImportoInValuta(totaleImpostaDocumento);
		importoImposta.calcolaImportoValutaAzienda(RigaIva.SCALE_FISCALE);
		documento.setImposta(importoImposta);

		logger.debug("--> Exit calcolaTotaleDocumento");
	}

	@Override
	public Documento totalizzaDocumento(Documento documento, TotaliArea totaliArea,
			ITotalizzatoriQueryExecutor executor, List<RigaIva> righeIva) {

		calcolaTotaliCategorie(totaliArea, documento, executor);

		return documento;
	}
}
