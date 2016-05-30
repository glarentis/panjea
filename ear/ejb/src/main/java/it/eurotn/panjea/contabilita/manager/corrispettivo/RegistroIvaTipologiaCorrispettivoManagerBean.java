/**
 *
 */
package it.eurotn.panjea.contabilita.manager.corrispettivo;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipologiaCorrispettivo;
import it.eurotn.panjea.contabilita.manager.corrispettivo.interfaces.RegistroIvaTipologiaCorrispettivoManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnagraficaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.RegistroIvaManager;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTOComparator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Classe manager per caricare i totali del registro iva corrispettivo considerando i diversi tipo corrispettivo.
 * 
 * @author Leonardo
 * @see TipologiaCorrispettivo
 */
@Stateless(name = "Panjea.RegistroIvaTipologiaCorrispettivoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RegistroIvaTipologiaCorrispettivoManager")
public class RegistroIvaTipologiaCorrispettivoManagerBean implements RegistroIvaTipologiaCorrispettivoManager {

	private static Logger logger = Logger.getLogger(RegistroIvaTipologiaCorrispettivoManagerBean.class);

	@Resource
	protected SessionContext context;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected ContabilitaSettingsManager contabilitaSettingsManager;

	@EJB
	protected ContabilitaAnagraficaManager contabilitaAnagraficaManager;

	@EJB
	@IgnoreDependency
	protected RegistroIvaManager registroIvaManager;

	@Override
	public LiquidazioneIvaDTO caricaTotali(RegistroIva registroIva, Date dataInizioPeriodo, Date dataFinePeriodo) {
		logger.debug("--> Enter caricaTotali");
		if (!(registroIva.getTipoRegistro().compareTo(TipoRegistro.CORRISPETTIVO) == 0)) {
			RuntimeException e = new RuntimeException("--> Il registro non e' di tipo registro CORRISPETTIVO");
			logger.error(e.getMessage(), e);
			throw e;
		}

		// verificare se il registro iva tipo corrispettivo a' usato, se cioa' esiste
		// un documento con legata area Contabile che ha associato il registro
		// iva di tipo corrispettivo sulla ricerca righe
		// verifico inoltre i settings contabilita' e verifico se ho abilitato il calcolo dei corrispettivi
		if (!verificaPresenzaRigheTipoCorrispettivo(registroIva) || !verificaCalcoloCorrispettiviAbilitato()) {
			return null;
		}

		// lista di totali raggruppati per codice di CodiceIva per il tipo corrispettivo DaFattura
		DaFatturaCorrispettivo daFatturaCorrispettivo = new DaFatturaCorrispettivo(panjeaDAO, getAzienda());
		List<TotaliCodiceIvaDTO> listTotaliDaFattura = daFatturaCorrispettivo.getTotali(registroIva, dataInizioPeriodo,
				dataFinePeriodo);
		logger.debug("--> Trovate per TipologiaCorrispettivo.DA_FATTURA n# " + listTotaliDaFattura.size());

		AliquotaNotaCorrispettivo aliquotaNotaCorrispettivo = new AliquotaNotaCorrispettivo(panjeaDAO, getAzienda());
		List<TotaliCodiceIvaDTO> listTotaliAliquotaNota = aliquotaNotaCorrispettivo.getTotali(registroIva,
				dataInizioPeriodo, dataFinePeriodo);
		logger.debug("--> Trovate per TipologiaCorrispettivo.ALIQUOTA_NOTA n# " + listTotaliAliquotaNota.size());

		RicevutaFiscaleCorrispettivo ricevutaFiscaleCorrispettivo = new RicevutaFiscaleCorrispettivo(panjeaDAO,
				getAzienda());
		List<TotaliCodiceIvaDTO> listTotaliRicevutaFiscale = ricevutaFiscaleCorrispettivo.getTotali(registroIva,
				dataInizioPeriodo, dataFinePeriodo);
		logger.debug("--> Trovate per TipologiaCorrispettivo.RICEVUTA_FISCALE n# " + listTotaliRicevutaFiscale.size());

		DaVentilazioneCorrispettivo daVentilazioneCorrispettivo = new DaVentilazioneCorrispettivo(panjeaDAO,
				getAzienda());
		// setto i manager che mi servono per il caricamento delle righe per il riepilogo corrispettivo e riepilogo
		// ventilazione
		daVentilazioneCorrispettivo.setContabilitaAnagraficaManager(contabilitaAnagraficaManager);
		daVentilazioneCorrispettivo.setRegistroIvaManager(registroIvaManager);
		// lista di totali per il riepilogo corrispettivo (report liquidazione)
		List<TotaliCodiceIvaDTO> listTotaliDaVentilazione = daVentilazioneCorrispettivo.getTotali(registroIva,
				dataInizioPeriodo, dataFinePeriodo);
		// lista di totali per il riepilogo ventilazione (report liquidazione)
		List<TotaliCodiceIvaDTO> listTotaliRiepilogoVentilazione = daVentilazioneCorrispettivo.getTotaliVentilazione();
		logger.debug("--> righe totali codice iva dto trovate " + listTotaliDaVentilazione.size());
		logger.debug("--> righe totali ventilazione iva dto trovate " + listTotaliRiepilogoVentilazione.size());

		// eseguo il merge delle righe per settare il totale per ogni tipologia corrispettivo
		List<TotaliCodiceIvaDTO> mergedRows = mergeTotaliCodiceIva(listTotaliDaFattura, listTotaliAliquotaNota,
				listTotaliRicevutaFiscale, listTotaliDaVentilazione, listTotaliRiepilogoVentilazione);

		// rimuovo le righe con tot ZERO
		mergedRows = removeZeroResult(mergedRows);

		// ordina la collection per codice iva
		Collections.sort(mergedRows, new TotaliCodiceIvaDTOComparator());

		// ordina per codice codice iva la collection righeVentilazione
		Collections.sort(listTotaliRiepilogoVentilazione, new TotaliCodiceIvaDTOComparator());

		LiquidazioneIvaDTO liquidazioneIvaDTO = new LiquidazioneIvaDTO();

		liquidazioneIvaDTO.addToTotaliRegistri(registroIva, mergedRows);
		if (verificaPresenzaCodiceIvaVentilazioneSettings()) {
			liquidazioneIvaDTO.addToTotaliVentilazioni(registroIva, listTotaliRiepilogoVentilazione);
		} else {
			liquidazioneIvaDTO.addToTotaliVentilazioni(registroIva, new ArrayList<TotaliCodiceIvaDTO>());
		}
		logger.debug("--> Trovate righe totaliCodiceIvaDTO n# " + mergedRows.size());
		logger.debug("--> Trovate righe rigaVentilazioneIvaDTO n# " + listTotaliRiepilogoVentilazione.size());
		logger.debug("--> Exit caricaTotali");
		return liquidazioneIvaDTO;
	}

	/**
	 * Recupera il codice azienda dell'utente autenticato nel context.
	 * 
	 * @return String codice azienda
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	/**
	 * Date due liste, eseguo il merge della lista di confronto nella lista base, aggiungendo il totale dipendente dalla
	 * tipologia corrispettivo se la riga e' gia' presente nella lista base oppure aggiungendola ad essa se non trovo
	 * corrispondenze.
	 * 
	 * @param totaliBase
	 *            lista di base che per comodita' scelgo di tipologia corrispettivo DA_FATTURA
	 * @param totaliConfronto
	 *            lista di confronto che si unira' alla lista base
	 * @param tipologiaCorrispettivo
	 *            AbstractTipologiaCorrispettivo, definisce la tipologia corrispettivo per scegliere su quali totali
	 *            aggiungere l'importo.
	 * @return List<TotaliCodiceIvaDTO>
	 */
	private List<TotaliCodiceIvaDTO> merge(List<TotaliCodiceIvaDTO> totaliBase,
			List<TotaliCodiceIvaDTO> totaliConfronto, TipologiaCorrispettivo tipologiaCorrispettivo) {
		logger.debug("--> Enter merge");
		for (TotaliCodiceIvaDTO totaliTipologiaCorrispettivo : totaliConfronto) {
			// se trovo una riga identificata dal codice di CodiceIva aggiungo il totale
			// che dipende dalla tipologia corrispettivo al totale relativo sulla riga della lista base
			if (totaliBase.contains(totaliTipologiaCorrispettivo)) {
				int index = totaliBase.indexOf(totaliTipologiaCorrispettivo);

				TotaliCodiceIvaDTO rigaACuiAggiungereTotaleCorrispettivo = totaliBase.get(index);

				// a seconda della tipologia corrispettivo setto il totale relativo
				if (tipologiaCorrispettivo.equals(TipologiaCorrispettivo.ALIQUOTA_NOTA)) {
					rigaACuiAggiungereTotaleCorrispettivo.setTotAliquotaNota(totaliTipologiaCorrispettivo
							.getTotAliquotaNota());
				} else if (tipologiaCorrispettivo.equals(TipologiaCorrispettivo.RICEVUTA_FISCALE)) {
					rigaACuiAggiungereTotaleCorrispettivo.setTotRicevutaFiscale(totaliTipologiaCorrispettivo
							.getTotRicevutaFiscale());
				} else if (tipologiaCorrispettivo.equals(TipologiaCorrispettivo.DA_VENTILARE)) {
					rigaACuiAggiungereTotaleCorrispettivo.setTotDaVentilazione(totaliTipologiaCorrispettivo
							.getTotDaVentilazione());
				}
			} else {
				// se non trovo la riga per codice di CodiceIva la aggiungo alla lista di base
				totaliBase.add(totaliTipologiaCorrispettivo);
			}
		}
		logger.debug("--> Exit merge");
		return totaliBase;
	}

	/**
	 * Scorro per l'ultima volta la lista per avvalorare i totali (somma dei totali dei 4 tipi di corrispettivi) e
	 * riassegnare imponibile e imposta rispetto al totale, i totali delle diverse tipologie corrispettivo devono essere
	 * stati gia' calcolati.
	 * 
	 * @param totaliCorrispettivo
	 *            lista di TotaliCodiceIvaDTO di cui calcolare il totale delle 4 tipologie corrispettivo
	 */
	private void mergeTotaleRiepilogo(List<TotaliCodiceIvaDTO> totaliCorrispettivo) {
		logger.debug("--> Enter mergeTotaleRiepilogo");
		for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : totaliCorrispettivo) {
			BigDecimal totalqnota = totaliCodiceIvaDTO.getTotAliquotaNota();
			BigDecimal totfatt = totaliCodiceIvaDTO.getTotDaFattura();
			BigDecimal totvent = totaliCodiceIvaDTO.getTotDaVentilazione();
			BigDecimal totricfisc = totaliCodiceIvaDTO.getTotRicevutaFiscale();
			BigDecimal tot4tipiCorr = BigDecimal.ZERO.add(totalqnota != null ? totalqnota : BigDecimal.ZERO)
					.add(totfatt != null ? totfatt : BigDecimal.ZERO).add(totvent != null ? totvent : BigDecimal.ZERO)
					.add(totricfisc != null ? totricfisc : BigDecimal.ZERO);

			totaliCodiceIvaDTO.setTotale(tot4tipiCorr);

			BigDecimal percAppl = totaliCodiceIvaDTO.getPercApplicazione();
			BigDecimal imponibile = tot4tipiCorr.multiply(Importo.HUNDRED).divide(percAppl.add(Importo.HUNDRED), 2,
					BigDecimal.ROUND_HALF_UP);

			totaliCodiceIvaDTO.setImponibile(imponibile);
			totaliCodiceIvaDTO.setImposta(tot4tipiCorr.subtract(imponibile));
		}
		logger.debug("--> Exit mergeTotaleRiepilogo");
	}

	/**
	 * Esegue il merge tra le liste di totaliCodiceIvaDTO impostando i diversi totali a seconda della tipologia
	 * corrispettivo.
	 * 
	 * @param totaliDaFattura
	 *            lista raggruppata per codice di CodiceIva per la tipologia corrispettivo DA_FATTURA che uso come lista
	 *            base per i merge successivi
	 * @param totaliAliquotaNota
	 *            lista raggruppata per codice di CodiceIva per la tipologia corrispettivo ALIQUOTA_NOTA
	 * @param totaliRicevutaFiscale
	 *            lista raggruppata per codice di CodiceIva per la tipologia corrispettivo RICEVUTA_FISCALE
	 * @param totaliDaVentilazione
	 *            lista raggruppata per codice di CodiceIva per la tipologia corrispettivo DA_VENTILARE
	 * @param totaliVentilazione
	 *            lista raggruppata per codice di CodiceIva per la tipologia
	 * @return List<TotaliCodiceIvaDTO> totali raggruppati per codice di CodiceIva con settati i totali per tutte le
	 *         tipologie corrispettivo
	 */
	private List<TotaliCodiceIvaDTO> mergeTotaliCodiceIva(List<TotaliCodiceIvaDTO> totaliDaFattura,
			List<TotaliCodiceIvaDTO> totaliAliquotaNota, List<TotaliCodiceIvaDTO> totaliRicevutaFiscale,
			List<TotaliCodiceIvaDTO> totaliDaVentilazione, List<TotaliCodiceIvaDTO> totaliVentilazione) {
		logger.debug("--> Enter mergeTotaliCodiceIva");
		List<TotaliCodiceIvaDTO> totali = merge(totaliDaFattura, totaliAliquotaNota,
				TipologiaCorrispettivo.ALIQUOTA_NOTA);
		totali = merge(totali, totaliRicevutaFiscale, TipologiaCorrispettivo.RICEVUTA_FISCALE);
		totali = merge(totali, totaliDaVentilazione, TipologiaCorrispettivo.DA_VENTILARE);

		// processo la lista per settare il totale ventilazione
		mergeTotaliVentilazione(totali, totaliVentilazione);

		// processo la lista per sommare i totali parziali nel totale riga
		// e lo divido poi per imposta e imponibile
		mergeTotaleRiepilogo(totali);
		logger.debug("--> Exit mergeTotaliCodiceIva # " + totali.size());
		return totali;
	}

	/**
	 * Scorro i totali generali da restituire per la tabella corrispettivi e associo il totale della lista di
	 * righeventilazione al totale ventilazione sulla rigaCodiceivadto.
	 * 
	 * @param totaliCorrispettivo
	 *            i totali corrispettivo per il riepilogo relativo
	 * @param righeVentilazione
	 *            le righe del riepilogo ventilazione
	 */
	private void mergeTotaliVentilazione(List<TotaliCodiceIvaDTO> totaliCorrispettivo,
			List<TotaliCodiceIvaDTO> righeVentilazione) {
		logger.debug("--> Enter mergeTotaliVentilazione");
		for (TotaliCodiceIvaDTO codIvaDTO : totaliCorrispettivo) {
			BigDecimal totDaVentilazione = null;
			for (TotaliCodiceIvaDTO rigaVentilazioneIvaDTO : righeVentilazione) {
				if (rigaVentilazioneIvaDTO.getCodiceIva().equals(codIvaDTO.getCodiceIva())) {
					totDaVentilazione = rigaVentilazioneIvaDTO.getImportoPesoVentilazione();
					break;
				}
			}
			if (totDaVentilazione != null) {
				codIvaDTO.setTotDaVentilazione(totDaVentilazione);
			}
		}
		logger.debug("--> Exit mergeTotaliVentilazione");
	}

	/**
	 * Rimuove le righe riepilogo corrispettivo che presentano un totale con valore ZERO.
	 * 
	 * @param mergedRows
	 *            le righe riepilogo corrispettivo da cui togliere quelle con valore a ZERO
	 * @return List<TotaliCodiceIvaDTO>
	 */
	private List<TotaliCodiceIvaDTO> removeZeroResult(List<TotaliCodiceIvaDTO> mergedRows) {
		logger.debug("--> Enter removeZeroResult");
		List<TotaliCodiceIvaDTO> rowsToRemove = new ArrayList<TotaliCodiceIvaDTO>();
		for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : mergedRows) {
			logger.debug("--> Totale della riga " + totaliCodiceIvaDTO.getTotale());
			// un bigdecimal con una certa precisione (6 decimali) e' diverso da uno con una precisione diversa (2
			// decimali)
			if (totaliCodiceIvaDTO.getTotale() != null
					&& totaliCodiceIvaDTO.getTotale().compareTo(BigDecimal.ZERO) == 0) {
				rowsToRemove.add(totaliCodiceIvaDTO);
			}
		}
		boolean removed = mergedRows.removeAll(rowsToRemove);
		logger.debug("--> Exit removeZeroResult rimossi " + rowsToRemove.size() + " elementi(" + removed + ")");
		return mergedRows;
	}

	/**
	 * Verifica se nei settings e' stato abilitato il calcolo dei corrispettivi.
	 * 
	 * @return true or false
	 */
	private boolean verificaCalcoloCorrispettiviAbilitato() {
		ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();
		return contabilitaSettings.isCalcoloCorrispettivi();
	}

	/**
	 * Verifica se e' impostato un codice iva per la ventilazione.
	 * 
	 * @return true or false
	 */
	private boolean verificaPresenzaCodiceIvaVentilazioneSettings() {
		return contabilitaSettingsManager.caricaContabilitaSettings().getCodiceIvaPerVentilazione() != null;
	}

	/**
	 * Verifica se ci sono riche di tipo corrispettivo per velocizzare il processo nel caso non ci siano.
	 * 
	 * @param registroIva
	 *            registro iva da verificare
	 * @return true o false
	 */
	private boolean verificaPresenzaRigheTipoCorrispettivo(RegistroIva registroIva) {
		logger.debug("--> Enter verificaPresenzaRigheTipoCorrispettivo");
		Query query = panjeaDAO.prepareNamedQuery("RigaIva.verificaPresenzaRigheTipoCorrispettivo");
		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setParameter("paramIdRegistroIva", registroIva.getId());

		Long l = (Long) query.getSingleResult();
		logger.debug("--> Risultato della ricerca di righe iva legate ad un documento di tipo corrispettivo # " + l);
		logger.debug("--> Exit verificaPresenzaRigheTipoCorrispettivo " + (l.longValue() != 0));
		return l.longValue() != 0;
	}

}
