package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipoFattureContabilitaManager;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaAnticipoFattureContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAnticipoFattureContabilitaManager")
public class AreaAnticipoFattureContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
		AreaAnticipoFattureContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaAnticipoFattureContabilitaManagerBean.class);

	@EJB
	private PianoContiManager pianoContiManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaPagamenti} passata come argomento.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @param areaPagamenti
	 *            area pagamenti
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaPagamenti areaPagamenti,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaPagamenti.getDocumento());
		Calendar calendarDataDocumento = Calendar.getInstance();
		calendarDataDocumento.setTime(areaContabile.getDocumento().getDataDocumento());

		// HACK data registrazione = data documento
		areaContabile.setDataRegistrazione(calendarDataDocumento.getTime());
		// HACK anno movimento da data documento
		areaContabile.setAnnoMovimento(calendarDataDocumento.get(Calendar.YEAR));

		areaContabile.setTipoAreaContabile(tipoAreaContabile);
		// inizializzazione dello stato di areaContabile dal valore presente in
		// tipoAreaContabile
		areaContabile.setStatoAreaContabile(tipoAreaContabile.getStatoAreaContabileGenerata());

		areaContabile.setValidRigheContabili(false);
		try {
			areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, true);
		} catch (ContabilitaException e) {
			logger.error("--> errore ContabilitaException in creaAreaContabile", e);
			throw new RuntimeException(e);
		} catch (AreaContabileDuplicateException e) {
			logger.error("--> errore AreaContabileDuplicateException in creaAreaContabile", e);
			throw new RuntimeException(e);
		} catch (DocumentoDuplicateException e) {
			logger.error("--> errore DocumentoDuplicateException in creaAreaContabile", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit creaAreaContabile");
		return areaContabile;

	}

	@Override
	public BigDecimal calcolaImportoAnticipato(Pagamento pagamento, BigDecimal percentualeAnticipoFatture,
			BigDecimal percentualeIvaAnticipoFatture) {

		if (percentualeAnticipoFatture == null || percentualeIvaAnticipoFatture == null) {
			throw new GenericException("Percentuale anticipo fatture e iva non impostato su rapporto bancario azienda!");
		}

		// recupera l'importo della rata perchè il pagamento di un anticipo fatture è zero
		Documento documentoDiOrigineDellaRata = pagamento.getRata().getAreaRate().getDocumento();

		BigDecimal totale = documentoDiOrigineDellaRata.getTotale().getImportoInValutaAzienda();
		BigDecimal imposta = documentoDiOrigineDellaRata.getImposta().getImportoInValutaAzienda();
		BigDecimal imponibile = totale.subtract(imposta);

		BigDecimal valPercImponibile = imponibile.multiply(percentualeAnticipoFatture).divide(
				BigDecimal.valueOf(100.00));
		BigDecimal valPercImposta = imposta.multiply(percentualeIvaAnticipoFatture).divide(BigDecimal.valueOf(100.00));

		// totale è la percentuale imponibile + la percentuale imposta
		BigDecimal importoRigaContabile = valPercImponibile.add(valPercImposta);
		return importoRigaContabile;
	}

	@Override
	public SottoConto caricaSottoContoAnticipiFatture(RapportoBancarioAzienda rapportoBancarioAzienda) {
		SottoConto sottoContoAnticipiFatture = null;
		try {
			sottoContoAnticipiFatture = pianoContiManager
					.caricaContoAnticipoFatturePerRapportoBancario(rapportoBancarioAzienda);
		} catch (Exception e2) {
			logger.error("--> errore imprevisto in creaAreaContabileAnticipoFatture", e2);
			throw new RuntimeException(e2);
		}

		if (sottoContoAnticipiFatture == null) {
			try {
				sottoContoAnticipiFatture = pianoContiManager
						.caricaContoPerTipoContoBase(ETipoContoBase.ANTICIPI_FATTURE);
			} catch (ContiBaseException e2) {
				logger.error("--> errore ContiBaseException in creaAreaContabileAnticipoFatture", e2);
				throw new RuntimeException(e2);
			} catch (Exception e2) {
				logger.error("--> errore imprevisto in creaAreaContabileAnticipoFatture", e2);
				throw new RuntimeException(e2);
			}
		}
		return sottoContoAnticipiFatture;
	}

	@Override
	public AreaPagamenti creaAreaContabileAnticipoFatture(AreaPagamenti areaPagamenti,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws PagamentiException {
		logger.debug("--> Enter creaAreaContabilePagamentoDiretto");

		// Recupero il tipoAreaContabile per il documento di pagamento
		TipoAreaContabile tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaPagamenti.getDocumento()
				.getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return areaPagamenti;
		}

		// Recupero i sotto conti per la scrittura, conto rapporto bancario e conto base con tipo conto anticipi
		// fatture.
		SottoConto sottoContoBanca = null;
		SottoConto sottoContoAnticipiFatture = caricaSottoContoAnticipiFatture(areaPagamenti.getDocumento()
				.getRapportoBancarioAzienda());

		// conto rapporto bancario
		switch (areaPagamenti.getTipoAreaPartita().getTipoDocumento().getTipoEntita()) {
		case BANCA:
			try {
				sottoContoBanca = pianoContiManager.caricaContoPerRapportoBancario(areaPagamenti.getDocumento()
						.getRapportoBancarioAzienda());
			} catch (ContoRapportoBancarioAssenteException e1) {
				logger.error("--> errore ContoRapportoBancarioAssenteException in creaAreaContabilePagamentoDiretto",
						e1);
				throw new RuntimeException(e1);
			}
			break;
		default:
			logger.error("--> errore TipoEntita non valida per tipo documento in creaAreaContabilePagamentoDiretto");
			throw new IllegalArgumentException("TipoEntita non valida per tipo documento incasso pagamento ("
					+ areaPagamenti.getTipoAreaPartita().getTipoDocumento().getTipoEntita() + ")");
		}

		// Creo l'area contabile
		AreaContabile areaContabile = new AreaContabile();
		areaContabile.setNote(parametriCreazioneAreaChiusure.getNoteContabili());
		areaContabile = aggiornaAreaContabile(areaContabile, areaPagamenti, tipoAreaContabile);

		// nel caso di entita' del documento di origine
		// (pagamento.getRata().getAreaRate().getDocumento()) null e quindi
		// entita' azienda, non genero nessuna delle righe contabili e quindi
		// esco dal metodo.
		// soluzione provvisoria per rimborsi e compensazioni bug 1641, da
		// rivedere completamente.
		Pagamento p = areaPagamenti.getPagamenti().iterator().next();
		if (p.getRata().getAreaRate().getDocumento().getEntita() == null) {
			// aggiorna il documento di area partite
			areaPagamenti.setDocumento(areaContabile.getDocumento());
			logger.debug("--> Exit creaAreaContabilePagamentoDiretto");
			return areaPagamenti;
		}

		// carica la lista di Pagamenti per l'area pagamenti corrente per la
		// generazione delle RigheContabili
		BigDecimal totaleConto = BigDecimal.ZERO;
		BigDecimal percentualeAnticipoFatture = areaPagamenti.getDocumento().getRapportoBancarioAzienda()
				.getPercAnticippoFatture();
		BigDecimal percentualeIvaAnticipoFatture = areaPagamenti.getDocumento().getRapportoBancarioAzienda()
				.getPercIvaAnticipoFatture();

		Set<Pagamento> pagamentiPerRaggruppamentoContoCassa = new HashSet<Pagamento>();
		for (Pagamento pagamento : areaPagamenti.getPagamenti()) {

			BigDecimal importoRigaContabile = calcolaImportoAnticipato(pagamento, percentualeAnticipoFatture,
					percentualeIvaAnticipoFatture);

			// totalizza importo di pagamento per la riga contabile
			totaleConto = totaleConto.add(importoRigaContabile);
			pagamentiPerRaggruppamentoContoCassa.add(pagamento);
		}

		boolean scritturaContabileInDare = true;
		if (TipoOperazione.CHIUSURA_ANTICIPO_FATTURE.equals(parametriCreazioneAreaChiusure.getTipoAreaPartita()
				.getTipoOperazione())) {
			scritturaContabileInDare = false;
		}

		// AVERE conto base anticipo fatture
		creaRigaContabile(areaContabile, sottoContoAnticipiFatture, totaleConto, !scritturaContabileInDare,
				pagamentiPerRaggruppamentoContoCassa);

		// DARE conto rapporto bancario
		creaRigaContabile(areaContabile, sottoContoBanca, totaleConto, scritturaContabileInDare,
				pagamentiPerRaggruppamentoContoCassa);

		// aggiorna il documento di area partite
		areaPagamenti.setDocumento(areaContabile.getDocumento());

		if (areaContabile.getTipoAreaContabile().getStatoAreaContabileGenerata() != StatoAreaContabile.PROVVISORIO) {
			boolean validaAreaContabile = true;
			for (RigaContabile rigaContabile : areaContabileManager.caricaRigheContabili(areaContabile.getId())) {
				if (!rigaContabile.isValid()) {
					validaAreaContabile = false;
					break;
				}
			}
			if (validaAreaContabile) {
				areaContabile.setStatoAreaContabile(areaContabile.getTipoAreaContabile()
						.getStatoAreaContabileGenerata());
				areaContabile.setValidRigheContabili(true);
				areaContabile.setValidDataRigheContabili(Calendar.getInstance().getTime());
				areaContabile.setValidUtenteRigheContabili(getPrincipal().getUserName());
			} else {
				areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
			}

			try {
				areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, false);
			} catch (Exception e) {
				logger.error("--> errore durante il salvataggio dell'area contabile.", e);
				throw new RuntimeException("errore durante il salvataggio dell'area contabile.", e);
			}
		}

		logger.debug("--> Exit creaAreaContabilePagamentoDiretto");
		return areaPagamenti;
	}

	/**
	 * crea una riga contabile.
	 * 
	 * @param areaContabile
	 *            area contabile da associare alla riga
	 * @param sottoConto
	 *            sotto conto della riga
	 * @param importo
	 *            importo
	 * @param rigaContabileInDare
	 *            <code>true</code> se il conto è in dare, <code>false</code> altrimenti
	 * @param pagamenti
	 *            i pagamenti associati alla riga contabile generata
	 * @return riga contrabile creata
	 */
	private RigaContabile creaRigaContabile(AreaContabile areaContabile, SottoConto sottoConto, BigDecimal importo,
			boolean rigaContabileInDare, Set<Pagamento> pagamenti) {
		logger.debug("--> Enter creaRigaContabile");

		if (BigDecimal.ZERO.compareTo(importo) > 0) {
			importo = importo.negate();
			rigaContabileInDare = !rigaContabileInDare;
		}

		RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, sottoConto, rigaContabileInDare,
				importo, null, false);
		rigaContabile.setPagamenti(pagamenti);

		logger.debug("--> Exit creaRigaContabile");
		return areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);
	}

}
