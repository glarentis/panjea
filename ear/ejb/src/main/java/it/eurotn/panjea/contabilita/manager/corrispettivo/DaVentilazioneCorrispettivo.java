/**
 *
 */
package it.eurotn.panjea.contabilita.manager.corrispettivo;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.CodiceIva.TipoCaratteristica;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipologiaCorrispettivo;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnagraficaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.RegistroIvaManager;
import it.eurotn.panjea.contabilita.service.exception.RegistroIvaAcquistiAssentePerVentilazione;
import it.eurotn.panjea.contabilita.service.exception.RegistroIvaAssenteException;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

/**
 * Classe che rappresenta la tipologia corrispettivo da ventilazione.
 * 
 * @author Leonardo
 */
public class DaVentilazioneCorrispettivo extends AbstractTipologiaCorrispettivo {

	private ContabilitaAnagraficaManager contabilitaAnagraficaManager = null;
	private RegistroIvaManager registroIvaManager = null;
	private List<TotaliCodiceIvaDTO> righeRiepilogoVentilazione = Collections.emptyList();

	/**
	 * Costruttore.
	 * 
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param codiceAzienda
	 *            codice azienda
	 */
	public DaVentilazioneCorrispettivo(final PanjeaDAO panjeaDAO, final String codiceAzienda) {
		super(TipologiaCorrispettivo.DA_VENTILARE, panjeaDAO, codiceAzienda);
	}

	/**
	 * Metodo per il calcolo della ventilazione per la tabella riepilogo ventilazione Il metodo viene chiamato nel
	 * metodo caricaTotaliCodiceIvaByTipologiaCorrispettivo dato che i risultati di questa tabella servono per la
	 * tabella principale dei corrispettivi.
	 * 
	 * @param righe
	 *            righe
	 * @param registroIva
	 *            registro iva
	 * @param dataInizioPeriodo
	 *            data inizio periodo
	 * @param dataFinePeriodo
	 *            data fine periodo
	 * @return List di {@link TotaliCodiceIvaDTO}
	 */
	private List<TotaliCodiceIvaDTO> calcolaTotaliVentilazione(Collection<TotaliCodiceIvaDTO> righe,
			RegistroIva registroIva, Date dataInizioPeriodo, Date dataFinePeriodo) {
		logger.debug("--> Enter calcolaTotaliVentilazione");
		List<TotaliCodiceIvaDTO> ventilazioneRows = new ArrayList<TotaliCodiceIvaDTO>();
		BigDecimal totale = BigDecimal.ZERO;
		// preparo le righe per la tabella di ventilazione e il totale di tutte le righe
		for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : righe) {
			// totale dell'importo per la ventilazione (imposta + imponibile)
			BigDecimal importoPerTabellaVentilazione = totaliCodiceIvaDTO.getImponibile().add(
					totaliCodiceIvaDTO.getImposta());
			logger.debug("--> totale della riga (imposta+imponibile) " + importoPerTabellaVentilazione);
			// creo una riga ventilazione con totale, codice iva e inizializzo a 0 la percentuale e l'importo
			// ventilazione
			TotaliCodiceIvaDTO rigaVentilazioneIvaDTO = new TotaliCodiceIvaDTO(totaliCodiceIvaDTO.getIdCodiceIva(),
					totaliCodiceIvaDTO.getCodiceIva(), totaliCodiceIvaDTO.getDescrizioneRegistro(),
					importoPerTabellaVentilazione, BigDecimal.ZERO, BigDecimal.ZERO);
			ventilazioneRows.add(rigaVentilazioneIvaDTO);

			// tengo il totale di tutte le righe
			totale = totale.add(importoPerTabellaVentilazione);
		}
		logger.debug("--> totale imposta+imponibile delle righe totaliCodiceIvaDTO " + totale);

		// totale Ventialzione
		List<TotaliCodiceIvaDTO> listTot = super.getTotali(registroIva, dataInizioPeriodo, dataFinePeriodo);
		BigDecimal totTipologiaCorrispettivoVentilazione = BigDecimal.ZERO;
		for (TotaliCodiceIvaDTO totaliCodiceIvaTipologiaVentilazione : listTot) {
			totTipologiaCorrispettivoVentilazione = totTipologiaCorrispettivoVentilazione
					.add(totaliCodiceIvaTipologiaVentilazione.getImponibile().add(
							totaliCodiceIvaTipologiaVentilazione.getImposta()));
		}
		logger.debug("--> tot risultato delle righe da ventilare da cui trovare percentuali e importo ventilazione "
				+ totTipologiaCorrispettivoVentilazione);

		BigDecimal percParzialeEsclusoUltimo = BigDecimal.ZERO;
		BigDecimal totParzialeEsclusoUltimo = BigDecimal.ZERO;
		int index = 1;
		int size = ventilazioneRows.size();

		// per ogni riga ventilazione valorizzo percentuale e totaleventilazione rispetto al totale
		for (TotaliCodiceIvaDTO rigaVentilazioneIvaDTO : ventilazioneRows) {
			BigDecimal totRiga = rigaVentilazioneIvaDTO.getTotale();
			BigDecimal percentualePesoVentilazione = (totRiga.divide(totale, 6, BigDecimal.ROUND_UP))
					.multiply(Importo.HUNDRED);
			percentualePesoVentilazione = percentualePesoVentilazione.setScale(6, BigDecimal.ROUND_UP);

			if (index != size || size == 1) {
				// trovo la percentuale rispetto al totale x ogni riga
				rigaVentilazioneIvaDTO.setPercentualePesoVentilazione(percentualePesoVentilazione);
				percParzialeEsclusoUltimo = percParzialeEsclusoUltimo.add(percentualePesoVentilazione);

				BigDecimal importoPesoVentilazione = (totTipologiaCorrispettivoVentilazione
						.multiply(percentualePesoVentilazione)).divide(Importo.HUNDRED);
				rigaVentilazioneIvaDTO.setImportoPesoVentilazione(importoPesoVentilazione);
				totParzialeEsclusoUltimo = totParzialeEsclusoUltimo.add(importoPesoVentilazione);
				logger.debug("--> riga ventilazione aggiornata " + rigaVentilazioneIvaDTO);
			} else {
				BigDecimal ultimoTotale = totTipologiaCorrispettivoVentilazione.subtract(totParzialeEsclusoUltimo);
				BigDecimal ultimaPercentuale = Importo.HUNDRED.subtract(percParzialeEsclusoUltimo);
				rigaVentilazioneIvaDTO.setPercentualePesoVentilazione(ultimaPercentuale);
				rigaVentilazioneIvaDTO.setImportoPesoVentilazione(ultimoTotale);
				logger.debug("--> ultima riga ventilazione aggiornata " + rigaVentilazioneIvaDTO);
			}

			index++;
		}
		logger.debug("--> Enter calcolaTotaliVentilazione # " + ventilazioneRows.size());
		return ventilazioneRows;
	}

	/**
	 * Cerca ricorsivamente il codice iva sostituto al codice iva ricevuto.
	 * 
	 * @param codiceIva
	 *            codice iva di cui cercare il sostituto
	 * @return CodiceIva
	 */
	private CodiceIva findCodiceIvaSostituzioneVentilazione(CodiceIva codiceIva) {
		logger.debug("--> Enter findCodiceIvaSostituzioneVentilazione " + codiceIva);
		CodiceIva codiceIvaSostituto = codiceIva;
		if (codiceIva.getCodiceIvaSostituzioneVentilazione() != null) {
			codiceIvaSostituto = findCodiceIvaSostituzioneVentilazione(codiceIva.getCodiceIvaSostituzioneVentilazione());
		}
		logger.debug("--> Exit findCodiceIvaSostituzioneVentilazione " + codiceIva);
		return codiceIvaSostituto;
	}

	/**
	 * @return contabilitaAnagraficaManager
	 */
	public ContabilitaAnagraficaManager getContabilitaAnagraficaManager() {
		return contabilitaAnagraficaManager;
	}

	/**
	 * @return registroIvaManager
	 */
	public RegistroIvaManager getRegistroIvaManager() {
		return registroIvaManager;
	}

	@Override
	public List<TotaliCodiceIvaDTO> getTotali(RegistroIva registroIva, Date dataInizioPeriodo, Date dataFinePeriodo) {
		logger.debug("--> Enter caricaRigheVentilazioneCorrispettivo");
		// carico il registro iva acquisto associato al corrispettivo, devo cioe' cercare
		// il registro acquisti con numero registro uguale al numero registro
		// del registro iva corrispettivo di origine
		List<TotaliCodiceIvaDTO> righeRiepilogoCorrispettivo = Collections.emptyList();

		// può essere che vengano utilizzati diversi registri corrispettivi che non hanno tipologia corrispettivo
		// "Da Ventilare"; in questo caso, per il registro iva non è necessario ricercare i totali per ventilazione; tra
		// l'altro è probabile che non esista un registro iva acquisti associato al registro corrispettivo
		boolean righeDaVentilarePresenti = verificaPresenzaRigheDaVentilare(registroIva, dataInizioPeriodo,
				dataFinePeriodo, TipoCaratteristica.MERCE);
		if (!righeDaVentilarePresenti) {
			return righeRiepilogoCorrispettivo;
		}

		RegistroIva registroIvaAcquisto;
		try {
			registroIvaAcquisto = contabilitaAnagraficaManager.caricaRegistroIva(registroIva.getNumero(),
					TipoRegistro.ACQUISTO);
		} catch (ObjectNotFoundException e) {
			logger.error("--> Nessun registro iva acquisto trovato con numero " + registroIva.getNumero(), e);

			RegistroIvaAssenteException registroIvaAssenteException = new RegistroIvaAcquistiAssentePerVentilazione(
					registroIva.getNumero());
			throw new RuntimeException(registroIvaAssenteException);
		}
		// carico la lista di righe con totali raggruppate per codice del codice iva

		righeRiepilogoCorrispettivo = registroIvaManager.caricaTotaliCodiciIvaByRegistro(dataInizioPeriodo,
				dataFinePeriodo, registroIvaAcquisto, TipoCaratteristica.MERCE, null);
		Collection<TotaliCodiceIvaDTO> righeSostituite = sostituzioneCodiceIva(righeRiepilogoCorrispettivo);

		righeRiepilogoVentilazione = calcolaTotaliVentilazione(righeSostituite, registroIva, dataInizioPeriodo,
				dataFinePeriodo);

		if (righeRiepilogoVentilazione != null && righeRiepilogoVentilazione.size() >= 0) {
			logger.debug("--> Exit caricaRigheVentilazioneCorrispettivo ventilate # "
					+ righeRiepilogoVentilazione.size());
			logger.debug("--> Exit caricaRigheVentilazioneCorrispettivo corrispettivi # "
					+ righeRiepilogoCorrispettivo.size());
		} else {
			righeRiepilogoCorrispettivo = Collections.emptyList();
			righeRiepilogoVentilazione = Collections.emptyList();
		}
		return righeRiepilogoCorrispettivo;
	}

	/**
	 * Restituisce la lista riepilogo ventilazione raggruppata per codice di codiceIva il metodo deve essere chiamato
	 * dopo la chiamata a getTotali dato che le lista per il riepilogo ventilazione e' dipendente dalla lista riepilogo
	 * corrispettivo.
	 * 
	 * @return List<TotaliCodiceIvaDTO>
	 */
	public List<TotaliCodiceIvaDTO> getTotaliVentilazione() {
		return righeRiepilogoVentilazione;
	}

	/**
	 * @param contabilitaAnagraficaManager
	 *            contabilitaAnagraficaManager
	 */
	public void setContabilitaAnagraficaManager(ContabilitaAnagraficaManager contabilitaAnagraficaManager) {
		this.contabilitaAnagraficaManager = contabilitaAnagraficaManager;
	}

	/**
	 * @param registroIvaManager
	 *            registroIvaManager
	 */
	public void setRegistroIvaManager(RegistroIvaManager registroIvaManager) {
		this.registroIvaManager = registroIvaManager;
	}

	@Override
	protected void setTotTipoCorrispettivo(TotaliCodiceIvaDTO totaliCodiceIvaDTO, BigDecimal tot) {
		totaliCodiceIvaDTO.setTotDaVentilazione(tot);
	}

	/**
	 * Data la lista di totaliCodiceIvaDTO, per ogni riga cerca ricorsivamente se il codice iva corrente è sostituito da
	 * un'altro codice iva e crea quindi una nuova lista con le nuove righe.
	 * 
	 * @param righe
	 *            righe da riorganizzare cercando i codici iva sostiutivi
	 * @return Collection<TotaliCodiceIvaDTO>
	 */
	private Collection<TotaliCodiceIvaDTO> sostituzioneCodiceIva(List<TotaliCodiceIvaDTO> righe) {
		logger.debug("--> Enter sostituzioneCodiceIva");
		Map<String, TotaliCodiceIvaDTO> righeSostituiteMap = new HashMap<String, TotaliCodiceIvaDTO>();

		for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : righe) {
			Integer idCodiceIva = totaliCodiceIvaDTO.getIdCodiceIva();
			CodiceIva codiceIva = null;
			try {
				codiceIva = getPanjeaDAO().load(CodiceIva.class, idCodiceIva);
			} catch (ObjectNotFoundException e) {
				throw new RuntimeException("--> ObjectNotFoundException", e);
			}
			CodiceIva codiceIvaSostitutivo = findCodiceIvaSostituzioneVentilazione(codiceIva);
			
			logger.debug("--> Codice iva originale " + codiceIva.getCodice());
			logger.debug("--> Codice iva sostituto " + codiceIvaSostitutivo.getCodice());

			String codice = codiceIva.getCodice();
			String codiceSostitutivo = codiceIvaSostitutivo.getCodice();

			// recupero la riga se esiste
			TotaliCodiceIvaDTO ivaDTO = righeSostituiteMap.get(codice);
			logger.debug("--> ivaDTO presente nella mappa " + ivaDTO);
			TotaliCodiceIvaDTO ivaSostituitoDTO = righeSostituiteMap.get(codiceSostitutivo);
			logger.debug("--> ivaSostituitoDTO presente nella mappa " + ivaSostituitoDTO);

			// il codice iva e' uguale al codice iva del sostituto
			if (codice.equals(codiceSostitutivo)) {
				logger.debug("--> codice iva uguale a codice iva sostituzione");
				// se esiste gia' un record devo sommare il totale precedente con quello attuale
				if (ivaDTO != null) {

					BigDecimal imponibileCorrente = totaliCodiceIvaDTO.getImponibile();
					BigDecimal impostaCorrente = totaliCodiceIvaDTO.getImposta();
					BigDecimal imponibilePrecedente = ivaDTO.getImponibile();
					BigDecimal impostaPrecedente = ivaDTO.getImposta();
					if (imponibilePrecedente == null) {
						imponibilePrecedente = BigDecimal.ZERO;
					}
					if (impostaPrecedente == null) {
						impostaPrecedente = BigDecimal.ZERO;
					}
					ivaDTO.setImponibile(imponibilePrecedente.add(imponibileCorrente));
					ivaDTO.setImposta(impostaPrecedente.add(impostaCorrente));

					logger.debug("--> aggiungo " + ivaDTO + " alla mappa che contiene " + righeSostituiteMap.size()
							+ " elementi");
					righeSostituiteMap.put(ivaDTO.getCodiceIva(), ivaDTO);
				} else {
					logger.debug("--> aggiungo " + totaliCodiceIvaDTO + " alla mappa che contiene "
							+ righeSostituiteMap.size() + " elementi");
					righeSostituiteMap.put(totaliCodiceIvaDTO.getCodiceIva(), totaliCodiceIvaDTO);
				}

			} else {
				// cerco la riga con codice iva codice, se esiste la tolgo e devo mettere i totali
				// in una nuova riga oppure nella riga ivaSostituitoDTO se != null
				TotaliCodiceIvaDTO rigaTotaliRimossa = righeSostituiteMap.remove(codice);
				if (ivaDTO != null) {
					logger.debug("--> rimuovo riga per codice " + codice + ":" + rigaTotaliRimossa);
					if (ivaSostituitoDTO != null) {
						BigDecimal impostaCorrente = totaliCodiceIvaDTO.getImposta();
						BigDecimal imponibileCorrente = totaliCodiceIvaDTO.getImponibile();
						BigDecimal impostaRimossa = rigaTotaliRimossa.getImposta();
						BigDecimal imponibileRimossa = rigaTotaliRimossa.getImponibile();
						BigDecimal impostaPrecedente = ivaSostituitoDTO.getImposta();
						BigDecimal imponibilePrecedente = ivaSostituitoDTO.getImponibile();
						if (impostaPrecedente == null) {
							impostaPrecedente = BigDecimal.ZERO;
						}
						if (imponibilePrecedente == null) {
							imponibilePrecedente = BigDecimal.ZERO;
						}
						ivaSostituitoDTO.setImponibile(imponibilePrecedente.add(imponibileCorrente).add(
								imponibileRimossa));
						ivaSostituitoDTO.setImposta(impostaPrecedente.add(impostaCorrente).add(impostaRimossa));

						logger.debug("--> aggiungo " + ivaSostituitoDTO + " alla mappa che contiene "
								+ righeSostituiteMap.size() + " elementi");
						righeSostituiteMap.put(ivaSostituitoDTO.getCodiceIva(), ivaSostituitoDTO);
					}
					// devo fare una nuova riga con le info della riga corrente ma il codice iva sostituito

				} else if (ivaSostituitoDTO != null) {
					BigDecimal impostaCorrente = totaliCodiceIvaDTO.getImposta();
					BigDecimal imponibileCorrente = totaliCodiceIvaDTO.getImponibile();
					BigDecimal impostaPrecedente = ivaSostituitoDTO.getImposta();
					BigDecimal imponibilePrecedente = ivaSostituitoDTO.getImponibile();
					if (impostaPrecedente == null) {
						impostaPrecedente = BigDecimal.ZERO;
					}
					if (imponibilePrecedente == null) {
						imponibilePrecedente = BigDecimal.ZERO;
					}
					ivaSostituitoDTO.setImponibile(imponibilePrecedente.add(imponibileCorrente));
					ivaSostituitoDTO.setImposta(impostaPrecedente.add(impostaCorrente));

					logger.debug("--> aggiungo " + ivaSostituitoDTO + " alla mappa che contiene "
							+ righeSostituiteMap.size() + " elementi");
					righeSostituiteMap.put(ivaSostituitoDTO.getCodiceIva(), ivaSostituitoDTO);
				} else {
					TotaliCodiceIvaDTO newTotaliCodiceIvaSostituto = new TotaliCodiceIvaDTO();
					newTotaliCodiceIvaSostituto.setImponibile(totaliCodiceIvaDTO.getImponibile());
					newTotaliCodiceIvaSostituto.setImposta(totaliCodiceIvaDTO.getImposta());
					newTotaliCodiceIvaSostituto.setIdCodiceIva(codiceIvaSostitutivo.getId());
					newTotaliCodiceIvaSostituto.setCodiceIva(codiceIvaSostitutivo.getCodice());
					newTotaliCodiceIvaSostituto.setDescrizioneRegistro(codiceIvaSostitutivo.getDescrizioneRegistro());
					newTotaliCodiceIvaSostituto.setPercApplicazione(codiceIvaSostitutivo.getPercApplicazione());
					newTotaliCodiceIvaSostituto.setPercIndetraibilita(codiceIvaSostitutivo.getPercIndetraibilita());
					newTotaliCodiceIvaSostituto.setConsideraPerLiquidazione(!codiceIvaSostitutivo.isIvaSospesa());

					newTotaliCodiceIvaSostituto.setTotale(totaliCodiceIvaDTO.getImponibile().add(
							totaliCodiceIvaDTO.getImposta()));

					logger.debug("--> aggiungo " + newTotaliCodiceIvaSostituto + " alla mappa che contiene "
							+ righeSostituiteMap.size() + " elementi");
					righeSostituiteMap.put(newTotaliCodiceIvaSostituto.getCodiceIva(), newTotaliCodiceIvaSostituto);
				}
			}
		}

		Collection<TotaliCodiceIvaDTO> righeSostituite = righeSostituiteMap.values();
		logger.debug("--> Exit sostituzioneCodiceIva # " + righeSostituite.size());
		return righeSostituite;
	}

	/**
	 * Verifica se ci sono righe iva associate a documenti con tipo area contabile con tipologia corrispettivo
	 * "Da ventilare".
	 * 
	 * @param registroIva
	 *            registroIva
	 * @param dataInizioPeriodo
	 *            dataInizioPeriodo
	 * @param dataFinePeriodo
	 *            dataFinePeriodo
	 * @param tipoCaratteristica
	 *            tipoCaratteristica
	 * @return true o false
	 */
	private boolean verificaPresenzaRigheDaVentilare(RegistroIva registroIva, Date dataInizioPeriodo,
			Date dataFinePeriodo, TipoCaratteristica tipoCaratteristica) {

		StringBuilder builder = new StringBuilder();
		builder.append("select count(r.id)");
		builder.append("from RigaIva r ");
		builder.append("where r.areaIva.areaContabile.documento.codiceAzienda = :paramCodiceAzienda ");
		builder.append("and r.areaIva.registroIva.id = :paramIdRegistroIva ");
		builder.append("and r.areaIva.areaContabile.statoAreaContabile in (0,1) ");
		builder.append("and (r.areaIva.areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
		builder.append("and (r.areaIva.areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
		builder.append("and r.areaIva.areaContabile.tipoAreaContabile.stampaRegistroIva=true ");
		builder.append("and r.areaIva.areaContabile.tipoAreaContabile.tipologiaCorrispettivo=0 ");
		builder.append("and (r.codiceIva.tipoCaratteristica = :paramTipoCaratteristica) ");
		builder.append("and r.codiceIva.liquidazionePeriodica=true ");

		Query query = getPanjeaDAO().prepareQuery(builder.toString());
		query.setParameter("paramIdRegistroIva", registroIva.getId());
		query.setParameter("paramDaDataRegistrazione", dataInizioPeriodo);
		query.setParameter("paramADataRegistrazione", dataFinePeriodo);
		query.setParameter("paramCodiceAzienda", getCodiceAzienda());
		query.setParameter("paramTipoCaratteristica", tipoCaratteristica);

		Long resultsNumber = null;
		try {
			resultsNumber = (Long) getPanjeaDAO().getSingleResult(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		return resultsNumber != null && resultsNumber > 0;
	}

}
