package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.RapportoBancario;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccontoContabilitaManager;
import it.eurotn.panjea.tesoreria.service.exception.ContoCassaAssenteException;

import java.math.BigDecimal;
import java.util.Calendar;

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
@Stateless(name = "Panjea.AreaAccontoContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAccontoContabilitaManager")
public class AreaAccontoContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
		AreaAccontoContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaAccontoContabilitaManagerBean.class);

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private PianoContiManager pianoContiManager;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaAcconto} passata come argomento.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @param areaAcconto
	 *            area acconto
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaAcconto areaAcconto,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaAcconto.getDocumento());
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
		areaContabile.setStatoAreaContabile(StatoAreaContabile.CONFERMATO);
		areaContabile.setValidRigheContabili(true);
		areaContabile.setValidDataRigheContabili(Calendar.getInstance().getTime());
		areaContabile.setValidUtenteRigheContabili(getPrincipal().getUserName());

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
	public void cancellaAreaContabileAcconto(AreaAcconto areaAcconto) {

		if (areaAcconto.getDocumento() != null && areaAcconto.getDocumento().getId() != null) {
			AreaContabileLite areaContabileLite = areaContabileManager.caricaAreaContabileLiteByDocumento(areaAcconto
					.getDocumento());

			if (areaContabileLite != null) {
				try {
					areaContabileCancellaManager.cancellaAreaContabile(areaContabileLite.getDocumento(), true);
				} catch (Exception e) {
					logger.error("--> Errore durante la cancellazione dell'area contabile.", e);
					throw new RuntimeException("Errore durante la cancellazione dell'area contabile.", e);
				}
			}
		}
	}

	@Override
	public void creaAreaContabileAcconto(AreaAcconto areaAcconto) {
		logger.debug("--> Enter creaAreaContabileAcconto");

		TipoAreaContabile tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaAcconto.getTipoAreaPartita()
				.getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return;
		}

		AreaContabile areaContabile = new AreaContabile();
		areaContabile = aggiornaAreaContabile(areaContabile, areaAcconto, tipoAreaContabile);

		EntitaLite entitaDoc = areaAcconto.getDocumento().getEntita();

		// creo le righe contabili
		SottoConto sottoContoAcconto = getSottoContoEntitaAcconto(areaAcconto);
		SottoConto sottoConto = null;

		// se il rapporto bancario è presente prendo il suo sottoconto
		// altrimenti prendo il sottoconto cassa del tipo area contabile
		if (areaAcconto.getRapportoBancarioAzienda() != null && !areaAcconto.getRapportoBancarioAzienda().isNew()) {
			sottoConto = getSottoContoRapportoBancario(areaAcconto);
		} else {
			sottoConto = areaContabile.getTipoAreaContabile().getContoCassa();
			if (sottoConto == null) {
				throw new ContoCassaAssenteException(areaContabile.getTipoAreaContabile().getTipoDocumento());
			}
		}

		BigDecimal importoRiga = areaContabile.getDocumento().getTotale().getImportoInValutaAzienda();
		// creazione riga contabile dare
		creaRigaContabile(areaContabile, sottoContoAcconto, importoRiga, (entitaDoc instanceof ClienteLite) ? false
				: true);
		// creazione riga contabile avere
		creaRigaContabile(areaContabile, sottoConto, importoRiga, (entitaDoc instanceof ClienteLite) ? true : false);

		logger.debug("--> Exit creaAreaContabileAcconto");
	}

	@Override
	public void creaAreaContabileAccontoIva(AreaAcconto areaAcconto) {
		logger.debug("--> Enter creaAreaContabilePagamentoDiretto");

		TipoAreaContabile tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaAcconto.getTipoAreaPartita()
				.getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return;
		}

		RapportoBancario rapportoBancario = areaAcconto.getRapportoBancarioAzienda();

		SottoConto sottoContoRapportoBancario = null;
		try {
			sottoContoRapportoBancario = pianoContiManager.caricaContoPerRapportoBancario(rapportoBancario);
		} catch (ContoRapportoBancarioAssenteException e) {
			logger.error("--> errore ContoRapportoBancarioAssenteException in creaAreaContabileAccontoIva", e);
			throw new RuntimeException(e);
		}

		SottoConto sottoContoContoBaseAccontoIva = null;
		try {
			sottoContoContoBaseAccontoIva = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ACCONTO_IVA);
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento del tipo conto base ACCONTO_CLIENTE o ACCONTO_FORNITORE", e);
			throw new RuntimeException(
					"errore durante il caricamento del tipo conto base ACCONTO_CLIENTE o ACCONTO_FORNITORE", e);
		}

		// crea una nuova area contabile visto che viene cancellata
		// precedentemente se esiste gia'.
		AreaContabile areaContabile = new AreaContabile();

		areaContabile = aggiornaAreaContabile(areaContabile, areaAcconto, tipoAreaContabile);

		// carica la lista di Pagamenti per l'area pagamenti corrente per la
		// generazione delle RigheContabili
		boolean scritturaContabileInDare = false;

		// eseguo la scrittura del conto cassa per il singolo pagamento
		creaRigaContabile(areaContabile, sottoContoRapportoBancario, areaAcconto.getDocumento().getTotale()
				.getImportoInValutaAzienda(), scritturaContabileInDare);

		creaRigaContabile(areaContabile, sottoContoContoBaseAccontoIva, areaAcconto.getDocumento().getTotale()
				.getImportoInValutaAzienda(), !scritturaContabileInDare);

		// aggiorna il documento di area partite
		areaAcconto.setDocumento(areaContabile.getDocumento());
		logger.debug("--> Exit creaAreaContabilePagamentoDiretto");
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
	 */
	private void creaRigaContabile(AreaContabile areaContabile, SottoConto sottoConto, BigDecimal importo,
			boolean rigaContabileInDare) {
		logger.debug("--> Enter creaRigaContabile");

		if (BigDecimal.ZERO.compareTo(importo) > 0) {
			importo = importo.negate();
			rigaContabileInDare = !rigaContabileInDare;
		}

		RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, sottoConto, rigaContabileInDare,
				importo, null, false);

		areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);
		logger.debug("--> Exit creaRigaContabile");
	}

	/**
	 * Restituisce il sottoconto legato all'entità del documento dell'area acconto.
	 * 
	 * @param areaAcconto
	 *            area acconto
	 * @return sotto conto
	 */
	private SottoConto getSottoContoEntitaAcconto(AreaAcconto areaAcconto) {

		SottoConto sottoContoAcconto = null;
		EntitaLite entitaDoc = areaAcconto.getDocumento().getEntita();

		try {
			if (entitaDoc instanceof ClienteLite) {
				sottoContoAcconto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ACCONTO_CLIENTE);
			} else if (entitaDoc instanceof FornitoreLite) {
				sottoContoAcconto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ACCONTO_FORNITORE);
			} else {
				throw new RuntimeException("L'entita del documento non è fornitore o cliente.");
			}
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento del tipo conto base ACCONTO_CLIENTE o ACCONTO_FORNITORE", e);
			throw new RuntimeException(
					"errore durante il caricamento del tipo conto base ACCONTO_CLIENTE o ACCONTO_FORNITORE", e);
		}

		return sottoContoAcconto;
	}

	/**
	 * Restituisce il sottoconto legato al rapporto bancario.
	 * 
	 * @param areaAcconto
	 *            area acconto
	 * @return sottoconto
	 */
	private SottoConto getSottoContoRapportoBancario(AreaAcconto areaAcconto) {

		SottoConto sottoContoRapportoBancario = null;
		RapportoBancarioAzienda rapportoBancario = areaAcconto.getRapportoBancarioAzienda();

		if (rapportoBancario != null) {
			try {
				sottoContoRapportoBancario = pianoContiManager.caricaContoPerRapportoBancario(rapportoBancario);
			} catch (ContoRapportoBancarioAssenteException e) {
				logger.error("-->errore durante il caricamento del sotto conto del rapporto bancario", e);
				throw new RuntimeException("errore durante il caricamento del sotto conto del rapporto bancario", e);
			}
		}
		return sottoContoRapportoBancario;
	}

}
