/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAssegnoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAssegnoManager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author leonardo
 */
@Stateless(name = "Panjea.AreaAssegnoContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAssegnoContabilitaManager")
public class AreaAssegnoContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
		AreaAssegnoContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaAssegnoContabilitaManagerBean.class);

	@EJB
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	@IgnoreDependency
	private AreaAssegnoManager areaAssegnoManager;

	@EJB
	protected PianoContiManager pianoContiManager;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaDistintaBancaria} passata come argomento.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @param areaAssegno
	 *            area accredito
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaAssegno areaAssegno,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaAssegno.getDocumento());
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
		if (areaContabile.getStatoAreaContabile() == StatoAreaContabile.CONFERMATO) {
			areaContabile.setValidRigheContabili(true);
			areaContabile.setValidDataRigheContabili(Calendar.getInstance().getTime());
			areaContabile.setValidUtenteRigheContabili(getPrincipal().getUserName());
		}
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
	public void cancellaAreaContabileAssegno(AreaAssegno areaAssegno) {
		// cancello l'area contabile se esiste
		try {
			AreaContabileLite areaContabileLite = areaContabileManager.caricaAreaContabileLiteByDocumento(areaAssegno
					.getDocumento());
			if (areaContabileLite != null) {
				areaContabileCancellaManager.cancellaAreaContabile(areaAssegno.getDocumento(), true);
			}
		} catch (Exception e) {
			logger.error("Errore nel cancellare l'area contabile dell'area assegno con id " + areaAssegno.getId(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void cancellaAreeContabiliAssegni(AreaAccreditoAssegno areaAccreditoAssegno) {
		// carica la lista di areaAssegno associate ad areaAccredito assegno e per ognuna cancella l'area contabile.
		List<AreaAssegno> assegni = areaAssegnoManager.caricaAreeAssegno(areaAccreditoAssegno);
		for (AreaAssegno areaAssegno : assegni) {
			cancellaAreaContabileAssegno(areaAssegno);
		}
	}

	@Override
	public void creaAreaContabileAssegno(AreaAccreditoAssegno areaAccreditoAssegno, AreaAssegno areaAssegno) {
		// crea la singola areaContabile per l'area assegno corrente.
		TipoAreaContabile tipoAreaContabileAssegno = getTipoAreaContabileByTipoDocumento(areaAssegno.getDocumento()
				.getTipoDocumento());
		TipoAreaContabile tipoAreaContabileAccreditoAssegno = getTipoAreaContabileByTipoDocumento(areaAccreditoAssegno
				.getDocumento().getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabileAssegno == null || tipoAreaContabileAccreditoAssegno == null) {
			return;
		}

		SottoConto sottoContoGiro = tipoAreaContabileAccreditoAssegno.getContoCassa();
		Set<Pagamento> pagamenti = areaAssegno.getPagamenti();
		Pagamento pagamento = pagamenti.iterator().next();
		EntitaLite entita = pagamento.getRata().getAreaRate().getDocumento().getEntita();

		SottoConto sottoContoEntita;
		try {
			sottoContoEntita = pianoContiManager.caricaSottoContoPerEntita(entita);
		} catch (ContabilitaException e) {
			logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
			throw new RuntimeException(e);
		}
		if (sottoContoEntita.isNew()) {
			ContoEntitaAssenteException contoEntitaAssenteException = new ContoEntitaAssenteException(entita);
			throw new RuntimeException(contoEntitaAssenteException);
		}

		AreaContabile areaContabile = new AreaContabile();
		areaContabile = aggiornaAreaContabile(areaContabile, areaAssegno, tipoAreaContabileAssegno);

		// DARE: totale documento sul conto definito sul TAC di AreaAccreditoAssegno
		creaRigaContabile(areaContabile, sottoContoGiro, areaContabile.getDocumento().getTotale().getImportoInValuta(),
				true);
		// AVERE: totale documento sul conto dell'entità sulla rata
		creaRigaContabile(areaContabile, sottoContoEntita, areaContabile.getDocumento().getTotale()
				.getImportoInValuta(), false);
	}

	@Override
	public void creaAreeContabiliAssegni(AreaAccreditoAssegno areaAccreditoAssegno) {
		// carica la lista di areaAssegno associate ad areaAccredito assegno e per ognuna crea l'area contabile.
		List<AreaAssegno> assegni = areaAssegnoManager.caricaAreeAssegno(areaAccreditoAssegno);
		for (AreaAssegno areaAssegno : assegni) {
			creaAreaContabileAssegno(areaAccreditoAssegno, areaAssegno);
		}
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

}
