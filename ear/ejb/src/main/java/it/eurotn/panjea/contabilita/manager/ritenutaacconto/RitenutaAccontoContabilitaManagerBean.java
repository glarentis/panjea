/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.ritenutaacconto;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.DatiRitenutaAccontoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.Importo.TIPO_OPERAZIONE_VALUTA;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.DatiRitenutaAccontoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.VariabiliFormulaManager;
import it.eurotn.panjea.contabilita.manager.rigacontabilebuider.EntitaRigaContabileBuilder;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoTesoreriaManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.RitenutaAccontoContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RitenutaAccontoContabilitaManager")
public class RitenutaAccontoContabilitaManagerBean implements RitenutaAccontoContabilitaManager {

	private static Logger logger = Logger.getLogger(RitenutaAccontoContabilitaManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private RitenutaAccontoManager ritenutaAccontoManager;

	@EJB
	private PianoContiManager pianoContiManager;

	@EJB
	private AreaIvaManager areaIvaManager;

	@EJB
	@IgnoreDependency
	private AreaContabileManager areaContabileManager;

	@EJB
	@IgnoreDependency
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private AziendeManager aziendeManager;

	@EJB
	@IgnoreDependency
	private RitenutaAccontoTesoreriaManager ritenutaAccontoTesoreriaManager;

	@EJB
	private EntitaManager entitaManager;

	@Override
	public void aggiornaImponibileDatiRitenutaAcconto(Integer idDocumento) {

		AreaContabile areaContabile = areaContabileManager.caricaAreaContabileByDocumento(idDocumento);
		if (areaContabile == null) {
			return;
		}

		AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabile);

		if (areaContabile.getTipoAreaContabile().isRitenutaAcconto()) {
			BigDecimal imponibileDocumento = areaIva.getTotaleImponibile(TIPO_OPERAZIONE_VALUTA.AZIENDA);

			// se non era mai stato settato un imponibile di riferimento vado ad aggiornare l'importo soggetto a
			// ritenuta d'acconto
			if (areaContabile.getDatiRitenutaAccontoAreaContabile().getImponibileRiferimento()
					.compareTo(BigDecimal.ZERO) == 0) {
				areaContabile.getDatiRitenutaAccontoAreaContabile().setImponibileSoggettoRitenuta(imponibileDocumento);
			}

			// l'importo di riferimento va sempre aggiornato
			areaContabile.getDatiRitenutaAccontoAreaContabile().setImponibileRiferimento(imponibileDocumento);
			try {
				areaContabileManager.salvaAreaContabileNoCheck(areaContabile);
			} catch (Exception e) {
				logger.error("--> errore durante il salvataggio dell'area contabile.", e);
				throw new RuntimeException("errore durante il salvataggio dell'area contabile.", e);
			}
		}

	}

	@Override
	public AreaContabile assegnaDatiRitenutaAccontoAreaContabile(AreaContabile areaContabile) {

		DatiRitenutaAccontoEntita datiRitenutaAccontoEntita = recuperaDatiRitenutaAcconto(areaContabile);

		AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabile);

		DatiRitenutaAccontoAreaContabile datiRitenutaAccontoAreaContabile = new DatiRitenutaAccontoAreaContabile(
				datiRitenutaAccontoEntita, areaContabile.getTipoAreaContabile(),
				areaIva.getTotaleImponibile(TIPO_OPERAZIONE_VALUTA.AZIENDA));

		areaContabile.setDatiRitenutaAccontoAreaContabile(datiRitenutaAccontoAreaContabile);

		return areaContabile;
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
	 * @return riga contrabile creata
	 */
	private RigaContabile creaRigaContabile(AreaContabile areaContabile, SottoConto sottoConto, BigDecimal importo,
			boolean rigaContabileInDare, Set<Pagamento> pagamenti, Long ordinamento) {
		logger.debug("--> Enter creaRigaContabile");

		if (BigDecimal.ZERO.compareTo(importo) > 0) {
			importo = importo.negate();
			rigaContabileInDare = !rigaContabileInDare;
		}

		RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, sottoConto, rigaContabileInDare,
				importo, null, false);

		logger.debug("--> Exit creaRigaContabile");
		return areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);
	}

	@Override
	public void creaRigheContabiliPerErarioDaPagare(AreaContabile areaContabile, Set<Pagamento> pagamenti) {
		logger.debug("--> Enter creaRigheContabiliPerErarioDaPagare");

		boolean creaRighe = false;
		BigDecimal importoPagamenti = BigDecimal.ZERO;
		// considero solo i pagamenti che si riferiscono a una rata di ritenuta d'acconto
		for (Pagamento pagamento : pagamenti) {
			Rata rata = ritenutaAccontoTesoreriaManager.caricaRataRitenutaAcconto(pagamento);
			if (rata != null && !pagamento.getRata().isRitenutaAcconto()) {
				importoPagamenti = importoPagamenti.add(rata.getImporto().getImportoInValutaAzienda());
				creaRighe = true;
			}
		}

		if (creaRighe) {
			SottoConto sottoContoErarioTemp = null;
			SottoConto sottoContoErarioDaPagare = null;
			try {
				sottoContoErarioTemp = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ERARIO_TEMPORANEO);
				sottoContoErarioDaPagare = pianoContiManager
						.caricaContoPerTipoContoBase(ETipoContoBase.ERARIO_DA_PAGARE);
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento dei conti base per l'erario", e);
				throw new RuntimeException("errore durante il caricamento dei conti base per l'erario", e);
			}

			// chiudo il conto erario temporaneo
			creaRigaContabile(areaContabile, sottoContoErarioTemp, importoPagamenti, true, null, null);

			// apro il conto erario da pagare
			creaRigaContabile(areaContabile, sottoContoErarioDaPagare, importoPagamenti, false, null, null);
		}

		logger.debug("--> Exit creaRigheContabiliPerErarioDaPagare");
	}

	@Override
	public void creaRigheContabiliPerErarioPagato(AreaContabile areaContabile, Set<Pagamento> pagamenti,
			List<RigaContabile> righeContabiliEntita) {
		logger.debug("--> Enter creaRigheContabiliPerErarioPagato");

		boolean creaRighe = false;
		BigDecimal importoPagamenti = BigDecimal.ZERO;
		// considero solo i pagamenti che si riferiscono a una rata di ritenuta d'acconto
		for (Pagamento pagamento : pagamenti) {
			Rata rata = pagamento.getRata();
			if (rata != null && rata.isRitenutaAcconto()) {
				importoPagamenti = importoPagamenti.add(rata.getImporto().getImportoInValutaAzienda());
				creaRighe = true;
			}
		}

		if (creaRighe) {
			SottoConto sottoContoErarioDaPagare = null;
			try {
				sottoContoErarioDaPagare = pianoContiManager
						.caricaContoPerTipoContoBase(ETipoContoBase.ERARIO_DA_PAGARE);
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento dei conti base per l'erario", e);
				throw new RuntimeException("errore durante il caricamento dei conti base per l'erario", e);
			}

			boolean generaRigaErarioDaPagare = false;

			BigDecimal totaleEntita = BigDecimal.ZERO;
			Set<Pagamento> pagamentiRata = new TreeSet<Pagamento>();

			// scorro le riga contabili create per le entità per farle diventare la riga di chiusura del conto erario
			// da
			// pagare se fanno riferimento al papgamento di una rata di ritenuta d'acconto
			for (RigaContabile rigaContabile : righeContabiliEntita) {
				if (rigaContabile.getPagamenti().iterator().next().getRata().isRitenutaAcconto()) {
					generaRigaErarioDaPagare = true;
					totaleEntita = totaleEntita.add(rigaContabile.getImporto());
					pagamentiRata.addAll(rigaContabile.getPagamenti());

					((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rigaContabile);
					areaContabileCancellaManager.cancellaRigaContabile(rigaContabile);
				}
			}

			// chiudo il conto erario da pagare mettendo la riga in cima con ordinamento = 0
			if (generaRigaErarioDaPagare) {
				RigaContabile creaRigaContabile = creaRigaContabile(areaContabile, sottoContoErarioDaPagare,
						totaleEntita, true, pagamentiRata, new Long(0));
				creaRigaContabile.getPagamenti().size();
			}
		}

		logger.debug("--> Exit creaRigheContabiliPerErarioPagato");
	}

	@Override
	public int creaRigheContabiliRitenutaAcconto(AreaContabile areaContabile, long ordinamento)
			throws ContabilitaException, ContiBaseException {

		int righeCreate = 0;

		Map<String, BigDecimal> map = getMapVariabiliFromRitenutaAcconto(areaContabile);

		if (areaContabile.getTipoAreaContabile().isRitenutaAcconto()) {
			DatiRitenutaAccontoAreaContabile datiRitenutaArea = areaContabile.getDatiRitenutaAccontoAreaContabile();
			EntitaRigaContabileBuilder entitaRigaContabileBuilder = new EntitaRigaContabileBuilder(pianoContiManager,
					null, null, aziendeManager);
			SottoConto sottoContoEntita;
			try {
				sottoContoEntita = entitaRigaContabileBuilder.caricaSottoContoperEntita(areaContabile);
			} catch (ContoEntitaAssenteException e) {
				logger.error("--> errore conto entità assente", e);
				ContiEntitaAssentiException contiEntitaAssentiException = new ContiEntitaAssentiException();
				contiEntitaAssentiException.add(e);
				throw new RuntimeException(contiEntitaAssentiException);
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento del sottoconto per l'entità.", e);
				throw new RuntimeException("errore durante il caricamento del sottoconto per l'entità.", e);
			}

			if (datiRitenutaArea.isFondoProfessionistiPresente()) {
				SottoConto sottoConto = pianoContiManager
						.caricaContoPerTipoContoBase(ETipoContoBase.FONDO_PROFESSIONISTI);
				RigaContabile rigaFondo = RigaContabile.creaRigaContabile(areaContabile, sottoConto, true,
						map.get(VariabiliFormulaManager.IMPFONDOPROF), null, ordinamento++, true);
				areaContabileManager.salvaRigaContabile(rigaFondo);
				righeCreate++;
			}

			// struttura entità - erario temporaneo
			if (datiRitenutaArea.isCausaleRitenutaPresente()) {
				SottoConto sottoConto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ERARIO_TEMPORANEO);
				RigaContabile rigaErario = RigaContabile.creaRigaContabile(areaContabile, sottoConto, false,
						map.get(VariabiliFormulaManager.IMPRITENUTA), null, ordinamento++, true);
				areaContabileManager.salvaRigaContabile(rigaErario);
				righeCreate++;

				RigaContabile rigaEntita = RigaContabile.creaRigaContabile(areaContabile, sottoContoEntita, true,
						map.get(VariabiliFormulaManager.IMPRITENUTA), null, ordinamento++, true);
				areaContabileManager.salvaRigaContabile(rigaEntita);
				righeCreate++;
			}

			// struttura contributo previdenziale
			if (datiRitenutaArea.isContributoPrevidenzialePresente()) {
				ETipoContoBase tipoContoBase = areaContabile.getTipoAreaContabile().getTipoRitenutaAcconto()
						.getContoBasePrevidenziale();
				SottoConto sottoConto = pianoContiManager.caricaContoPerTipoContoBase(tipoContoBase);
				RigaContabile rigaPrev = RigaContabile.creaRigaContabile(areaContabile, sottoConto, false,
						map.get(VariabiliFormulaManager.IMPPREVIDENZIALE), null, ordinamento++, true);
				areaContabileManager.salvaRigaContabile(rigaPrev);
				righeCreate++;

				RigaContabile rigaLavoratore = RigaContabile.creaRigaContabile(areaContabile, sottoContoEntita, true,
						map.get(VariabiliFormulaManager.IMPPREVIDENZIALELAVORATORE), null, ordinamento++, true);
				areaContabileManager.salvaRigaContabile(rigaLavoratore);
				righeCreate++;

				sottoConto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.QUOTA_PREVIDENZIALE_AZIENDA);
				RigaContabile rigaEntita = RigaContabile.creaRigaContabile(areaContabile, sottoConto, true,
						map.get(VariabiliFormulaManager.IMPPREVIDENZIALEAZIENDA), null, ordinamento++, true);
				areaContabileManager.salvaRigaContabile(rigaEntita);
				righeCreate++;
			}
		}

		return righeCreate;
	}

	/**
	 * Calcola l'importo del fondo professionisti sull'imponibile in base alla percentuale.
	 * 
	 * @param imponibile
	 *            imponibile
	 * @param percFondo
	 *            percentuale
	 * @return importo.
	 */
	private BigDecimal getImportoFondoProfessionisti(BigDecimal imponibile, Double percFondo) {
		BigDecimal result = BigDecimal.ZERO;

		if (percFondo != null && percFondo.compareTo(0.0) != 0) {
			BigDecimal newImponibile = imponibile.divide(new BigDecimal(100 + percFondo), 4, RoundingMode.HALF_UP)
					.multiply(Importo.HUNDRED).setScale(2, RoundingMode.HALF_UP);
			result = imponibile.subtract(newImponibile);
		}

		return result;
	}

	@Override
	public BigDecimal getImportoRitenutaAcconto(Documento documento) {
		logger.debug("--> Enter getImportoRitenutaAcconto");

		BigDecimal importo = BigDecimal.ZERO;

		SottoConto sottoConto = null;
		try {
			sottoConto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ERARIO_TEMPORANEO);

			StringBuffer sb = new StringBuffer();
			sb.append("select r.importoDare + r.importoAvere from RigaContabile r inner join r.areaContabile ac ");
			sb.append("where r.automatica = true and ac.tipoAreaContabile.ritenutaAcconto = true and ");
			sb.append("ac.documento.id = :paramIdDocumento and ");
			sb.append("r.conto = :paramSottoConto ");

			Query query = panjeaDAO.prepareQuery(sb.toString());
			query.setParameter("paramIdDocumento", documento.getId());
			query.setParameter("paramSottoConto", sottoConto);

			importo = (BigDecimal) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.warn("Riga contabile per ritenuta d'acconto non trovata.");
		}

		logger.debug("--> Exit getImportoRitenutaAcconto");
		return importo;
	}

	@Override
	public Map<String, BigDecimal> getMapVariabiliFromRitenutaAcconto(AreaContabile areaContabile) {

		Map<String, BigDecimal> mapVariabili = new HashMap<String, BigDecimal>();

		if (areaContabile.getTipoAreaContabile().isRitenutaAcconto()) {
			DatiRitenutaAccontoAreaContabile datiRitenutaArea = areaContabile.getDatiRitenutaAccontoAreaContabile();

			BigDecimal importoFondo = BigDecimal.ZERO;
			// calcolo la % fondo professionisti se esiste
			if (datiRitenutaArea.isFondoProfessionistiPresente()) {
				importoFondo = getImportoFondoProfessionisti(datiRitenutaArea.getImponibileRiferimento(),
						datiRitenutaArea.getPercFondoProfessionisti());
				// tolgo l'importo del fondo dall'imponibile aggiornando la mappa
				mapVariabili.put(VariabiliFormulaManager.IMP,
						datiRitenutaArea.getImponibileRiferimento().subtract(importoFondo));
				mapVariabili.put(VariabiliFormulaManager.IMPFONDOPROF, importoFondo);
			}

			BigDecimal importoCalcolo = datiRitenutaArea.getImponibileSoggettoRitenuta().subtract(importoFondo);

			// calcolo l'importo della ritenuta
			if (datiRitenutaArea.isCausaleRitenutaPresente()) {
				BigDecimal importoRitenuta = getPercImporto(importoCalcolo, datiRitenutaArea.getPercentualeImponibile());
				importoRitenuta = getPercImporto(importoRitenuta, datiRitenutaArea.getPercentualeAliquota());
				mapVariabili.put(VariabiliFormulaManager.IMPRITENUTA, importoRitenuta);
			}

			// calcolo l'importo previdenziale
			if (datiRitenutaArea.isContributoPrevidenzialePresente()) {
				BigDecimal importoContributo = getPercImporto(importoCalcolo, datiRitenutaArea.getPercContributiva());
				BigDecimal importoContributoLavoratore = getPercImporto(importoContributo,
						datiRitenutaArea.getPercPrevidenzialeCaricoLavoratore());

				mapVariabili.put(VariabiliFormulaManager.IMPPREVIDENZIALE, importoContributo);
				mapVariabili.put(VariabiliFormulaManager.IMPPREVIDENZIALELAVORATORE, importoContributoLavoratore);
				mapVariabili.put(VariabiliFormulaManager.IMPPREVIDENZIALEAZIENDA,
						importoContributo.subtract(importoContributoLavoratore));
			}
		}

		return mapVariabili;
	}

	/**
	 * Calcola la percentuale dell'importo.
	 * 
	 * @param imponibile
	 *            imponibile
	 * @param percentuale
	 *            percentuale
	 * @return importo ritenuta d'acconto
	 */
	private BigDecimal getPercImporto(BigDecimal imponibile, Double percentuale) {
		BigDecimal impRitenuta = imponibile.multiply(BigDecimal.valueOf(percentuale)).setScale(4, RoundingMode.HALF_UP)
				.divide(Importo.HUNDRED, 2, RoundingMode.HALF_UP);

		return impRitenuta;
	}

	/**
	 * Restituisce i dati da utilizzare per il calcolo della ritenuta d'acconto. Se non è prevista la gestione o non
	 * sono presenti sull'entità viene restituito <code>null</code>.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @return dati per il calcolo
	 */
	private DatiRitenutaAccontoEntita recuperaDatiRitenutaAcconto(AreaContabile areaContabile) {
		DatiRitenutaAccontoEntita datiRitenutaAccontoEntita = null;

		if (areaContabile.getTipoAreaContabile().isRitenutaAcconto()
				&& areaContabile.getDocumento().getEntita() != null) {
			EntitaLite entita;
			try {
				entita = entitaManager.caricaEntitaLite(areaContabile.getDocumento().getEntita());
			} catch (Exception e) {
				throw new RuntimeException("Errore durante il caricamento dell'entità.", e);
			}
			// se il tipo area contabile gestisce la ritenuta d'acconto vedo se anche l'entità la gestisce
			switch (areaContabile.getTipoAreaContabile().getTipoRitenutaAcconto()) {
			case INPS:
				datiRitenutaAccontoEntita = ritenutaAccontoManager.caricaDatiRitenutaAccontoFornitore(entita.getId());
				break;
			case ENASARCO:
				datiRitenutaAccontoEntita = ritenutaAccontoManager.caricaDatiRitenutaAccontoAgente(entita
						.getAnagrafica().getId());
				break;
			default:
				datiRitenutaAccontoEntita = null;
				break;
			}
		}

		return datiRitenutaAccontoEntita;
	}
}
