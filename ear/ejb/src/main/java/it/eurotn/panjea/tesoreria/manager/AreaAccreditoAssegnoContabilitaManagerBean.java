/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
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
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoAssegnoContabilitaManager;
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
 * @author leonardo
 */
@Stateless(name = "Panjea.AreaAccreditoAssegnoContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAccreditoAssegnoContabilitaManager")
public class AreaAccreditoAssegnoContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
		AreaAccreditoAssegnoContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaAccreditoAssegnoContabilitaManagerBean.class);

	@EJB
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	protected PianoContiManager pianoContiManager;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaDistintaBancaria} passata come argomento.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @param areaAccreditoAssegno
	 *            area accredito
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaAccreditoAssegno areaAccreditoAssegno,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaAccreditoAssegno.getDocumento());
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
	public void cancellaAreaContabileAccreditoAssegno(AreaAccreditoAssegno areaAccreditoAssegno) {
		// cancello l'area contabile se esiste
		try {
			AreaContabileLite areaContabileLite = areaContabileManager
					.caricaAreaContabileLiteByDocumento(areaAccreditoAssegno.getDocumento());
			if (areaContabileLite != null) {
				areaContabileCancellaManager.cancellaAreaContabile(areaAccreditoAssegno.getDocumento(), true);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void creaAreaContabileAccreditoAssegno(AreaAccreditoAssegno areaAccreditoAssegno) {
		// crea la singola areaContabile per l'area assegno corrente.
		TipoAreaContabile tipoAreaContabileAccreditoAssegno = getTipoAreaContabileByTipoDocumento(areaAccreditoAssegno
				.getDocumento().getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabileAccreditoAssegno == null) {
			return;
		}

		SottoConto sottoContoGiro = tipoAreaContabileAccreditoAssegno.getContoCassa();
		if (sottoContoGiro == null) {
			ContoCassaAssenteException contoCassaAssenteException = new ContoCassaAssenteException(
					tipoAreaContabileAccreditoAssegno.getTipoDocumento());
			throw contoCassaAssenteException;
		}

		RapportoBancarioAzienda rapportoBancarioAzienda = areaAccreditoAssegno.getDocumento()
				.getRapportoBancarioAzienda();

		SottoConto sottoContoRapportoBancario = null;
		try {
			sottoContoRapportoBancario = pianoContiManager.caricaContoPerRapportoBancario(rapportoBancarioAzienda);
		} catch (ContoRapportoBancarioAssenteException e1) {
			logger.error("--> errore ContoRapportoBancarioAssenteException in creaAreaContabileAccreditoAssegno", e1);
			throw new RuntimeException(e1);
		}

		AreaContabile areaContabile = new AreaContabile();
		areaContabile = aggiornaAreaContabile(areaContabile, areaAccreditoAssegno, tipoAreaContabileAccreditoAssegno);

		// DARE: il totale accredito sul conto del rapporto bancario dell'accredito assegno
		creaRigaContabile(areaContabile, sottoContoRapportoBancario, areaAccreditoAssegno.getDocumento().getTotale()
				.getImportoInValuta(), true);

		// AVERE: il totale accredito sul conto definito sul TAC di AreaAccreditoAssegno
		creaRigaContabile(areaContabile, sottoContoGiro, areaAccreditoAssegno.getDocumento().getTotale()
				.getImportoInValuta(), false);

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
