package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.TipiDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AperturaContabileProcessor;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnnualeManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.AperturaEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public abstract class AbstractAperturaContabileProcessor implements AperturaContabileProcessor {
	private static Logger logger = Logger.getLogger(AbstractAperturaContabileProcessor.class);

	/**
	 * @uml.property name="contabilitaAnnualeManager"
	 * @uml.associationEnd
	 */
	@IgnoreDependency
	@EJB
	private ContabilitaAnnualeManager contabilitaAnnualeManager;

	/**
	 * @uml.property name="tipiAreaContabileManager"
	 * @uml.associationEnd
	 */
	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	/**
	 * @uml.property name="areaContabileManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected AreaContabileManager areaContabileManager;

	/**
	 * @uml.property name="aziendeManager"
	 * @uml.associationEnd
	 */
	@EJB
	private ValutaManager valutaManager;

	@Override
	public AreaContabile caricaDocumentoChisuraPrecedentePresente(Integer annoEsercizio) {
		List<AreaContabile> areeContabiliAperturaChiusura = null;
		try {
			TipoAreaContabile tipoAreaContabileChiusura = caricaTipoAreaContabileChiusura();
			areeContabiliAperturaChiusura = contabilitaAnnualeManager.caricaAreeContabiliAperturaChiusura(
					annoEsercizio - 1, getTipoConto());
			if (areeContabiliAperturaChiusura.isEmpty()) {
				return null;
			}

			// Verifico di avere una chiusura dell'anno precedente come primo movimento (altrimenti avrei un'apertura
			// dell'anno in corso)
			boolean tipoAreaChiusura = areeContabiliAperturaChiusura.get(0).getTipoAreaContabile()
					.equals(tipoAreaContabileChiusura);
			boolean annoPrecedente = areeContabiliAperturaChiusura.get(0).getAnnoMovimento() == annoEsercizio;
			if (!tipoAreaChiusura && !annoPrecedente) {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return areeContabiliAperturaChiusura.get(0);
	}

	/**
	 *
	 * @return tipoAreaContabile utilizzata per l'apertura
	 * @throws TipoDocumentoBaseException
	 *             rilanciata se non ho un tipo documento base per l'apertura.
	 */
	private TipoAreaContabile caricaTipoAreaContabileApertura() throws TipoDocumentoBaseException {
		logger.debug("--> Enter caricaTipoAraeContabileChiusura");
		TipoAreaContabile tipoDocumentoBaseApertura = null;
		try {
			TipiDocumentoBase tipiDocumentoBase = tipiAreaContabileManager.caricaTipiOperazione();
			if (!tipiDocumentoBase.containsKey(TipiDocumentoBase.getTipoOperazioneAperturaPerTipoConto(getTipoConto()))) {
				logger.warn("--> Tipo documento chiusura per il conto " + getTipoConto() + " non presente");
				throw new TipoDocumentoBaseException(new String[] { "Tipo operazione "
						+ TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(getTipoConto()).name() });
			}
			tipoDocumentoBaseApertura = tipiDocumentoBase.get(TipiDocumentoBase
					.getTipoOperazioneAperturaPerTipoConto(getTipoConto()));
		} catch (ContabilitaException e) {
			logger.error("--> errore nel caricare i tipi documento base", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoAraeContabileChiusura");
		return tipoDocumentoBaseApertura;
	}

	/**
	 *
	 * @return tipoAreaContabile utilizzata per la chiusura
	 * @throws TipoDocumentoBaseException
	 *             rilanciata se non ho un tipo documento base per la chiusura.
	 */
	private TipoAreaContabile caricaTipoAreaContabileChiusura() throws TipoDocumentoBaseException {
		logger.debug("--> Enter caricaTipoAraeContabileChiusura");
		TipoAreaContabile tipoDocumentoBaseChiusura = null;
		try {
			TipiDocumentoBase tipiDocumentoBase = tipiAreaContabileManager.caricaTipiOperazione();
			if (!tipiDocumentoBase.containsKey(TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(getTipoConto()))) {
				logger.warn("--> Tipo documento chiusura per il conto " + getTipoConto() + " non presente");
				throw new TipoDocumentoBaseException(new String[] { "Tipo operazione "
						+ TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(getTipoConto()).name() });
			}
			tipoDocumentoBaseChiusura = tipiDocumentoBase.get(TipiDocumentoBase
					.getTipoOperazioneChiusuraPerTipoConto(getTipoConto()));
		} catch (ContabilitaException e) {
			logger.error("--> errore nel caricare i tipi documento base", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoAraeContabileChiusura");
		return tipoDocumentoBaseChiusura;
	}

	/**
	 *
	 * @param areaContabile
	 *            areacontabile
	 * @param rigaContabileChiusura
	 *            righe contabili dell'ultima chiusura.
	 */
	protected abstract void creaRigheContabiliApertura(AreaContabile areaContabile,
			List<RigaContabile> rigaContabileChiusura);

	/**
	 * Crea e salva l'area contabile per la chiusura.
	 *
	 * @param annoEsercizio
	 *            anno di esercizio dell'area contabile
	 * @param dataDocumento
	 *            data del documento
	 * @param tipoAreaContabileApertura
	 *            tipoDocumento
	 * @return areaContabile di apertura generata
	 */
	private AreaContabile creaSalvaAreaContabileApertura(Integer annoEsercizio, Date dataDocumento,
			TipoAreaContabile tipoAreaContabileApertura) {
		AreaContabile areaContabileApertura = new AreaContabile();
		areaContabileApertura.setStatoAreaContabile(StatoAreaContabile.CONFERMATO);
		areaContabileApertura.setAnnoMovimento(annoEsercizio);
		areaContabileApertura.setDataRegistrazione(dataDocumento);
		areaContabileApertura.getDocumento().setDataDocumento(dataDocumento);
		areaContabileApertura.setTipoAreaContabile(tipoAreaContabileApertura);
		areaContabileApertura.getDocumento().setTipoDocumento(tipoAreaContabileApertura.getTipoDocumento());
		areaContabileApertura.getDocumento().getTotale()
				.setCodiceValuta(valutaManager.caricaValutaAziendaCorrente().getCodiceValuta());
		try {
			areaContabileApertura = areaContabileManager.salvaAreaContabileNoCheck(areaContabileApertura);
		} catch (Exception e) {
			logger.error("--> errore nel salvare l'area contabile per il documento di chiusura", e);
			throw new RuntimeException(e);
		}
		return areaContabileApertura;
	}

	@Override
	public void eseguiApertura(Integer annoEsercizio, Date dataDocumentoChiusura) throws AperturaEsistenteException,
			ContiBaseException, TipoDocumentoBaseException, ChiusuraAssenteException {
		logger.debug("--> Enter eseguiApertura");
		verificaTipoDocumentoBaseApertura();
		verificaDocumentiAperturaPresenti(annoEsercizio);
		verificaTipiContiApertura();

		// Carico l'area chiusura precedente. Se non esiste esco
		AreaContabile areaContabileChiusura = caricaDocumentoChisuraPrecedentePresente(annoEsercizio);
		if (areaContabileChiusura == null) {
			// se il tipo conto è ordine non dò la segnalazione di errore perchè non è detto che sia gestito e non ho
			// modo di saperlo
			if (getTipoConto() == TipoConto.ORDINE) {
				return;
			}
			logger.warn("--> Movimento di chiusura precedente non trovato per tipo conto " + getTipoConto());
			throw new ChiusuraAssenteException();
		}

		// Creo l'area contabile di apertura
		AreaContabile areaContabileApertura = creaSalvaAreaContabileApertura(annoEsercizio, dataDocumentoChiusura,
				caricaTipoAreaContabileApertura());

		List<RigaContabile> righeChiusuraPrecedente = areaContabileManager.caricaRigheContabili(areaContabileChiusura
				.getId());
		creaRigheContabiliApertura(areaContabileApertura, righeChiusuraPrecedente);
		logger.debug("--> Exit eseguiApertura");
	}

	/**
	 *
	 * @return tipo del conto da aprire/chiudere
	 */
	protected abstract TipoConto getTipoConto();

	@Override
	public void verificaDocumentiAperturaPresenti(Integer annoEsercizio) throws AperturaEsistenteException {
		try {
			TipoAreaContabile tipoAreaContabileApertura = caricaTipoAreaContabileApertura();
			List<AreaContabile> areeContabiliAperturaChiusura = contabilitaAnnualeManager
					.caricaAreeContabiliAperturaChiusura(annoEsercizio, getTipoConto());
			// Se il primo è un apertura dell'anno...error
			if (!areeContabiliAperturaChiusura.isEmpty()) {
				boolean tipoAreaApertura = areeContabiliAperturaChiusura.get(0).getTipoAreaContabile()
						.equals(tipoAreaContabileApertura);
				boolean annoCorrente = areeContabiliAperturaChiusura.get(0).getAnnoMovimento() == annoEsercizio;
				if (tipoAreaApertura && annoCorrente) {
					throw new AperturaEsistenteException(getTipoConto());
				}
			}
		} catch (Exception e) {
			logger.error("--> errore nel caricare la lista dei tipi documenti di apertura chiusura per il tipo conto "
					+ getTipoConto(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void verificaTipoDocumentoBaseApertura() throws TipoDocumentoBaseException {
		caricaTipoAreaContabileApertura();
	}

}
