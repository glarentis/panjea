package it.eurotn.panjea.contabilita.manager.rateirisconti;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaContabileRateiRisconti;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces.AreaChiusuraRateiManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.RigheContabiliNonValidiException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.AreaChiusuraRateiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaChiusuraRateiManager")
public class AreaChiusuraRateiManagerBean implements AreaChiusuraRateiManager {
	private static Logger logger = Logger.getLogger(AreaChiusuraRateiManagerBean.class);

	@EJB
	private PianoContiManager pianoContiManager;

	@EJB
	@IgnoreDependency
	private AreaContabileManager areaContabileManager;

	@EJB
	@IgnoreDependency
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public RigaContabile creaAreaContabileChiusura(RigaContabile rigaContabile, BigDecimal importoRateo,
			SottoConto contoRateo) throws ContiBaseException, TipoDocumentoBaseException {
		boolean isDare = rigaContabile.getAreaContabile().getDocumento().getTipoDocumento().getTipoEntita() == TipoEntita.CLIENTE;

		AreaContabile areaContabile = null;
		RigaContabileRateiRisconti rigaContabileRateoAnnoPrecedente = null;
		RigaContabile rigaContabileGiroContoAnnoPrecedente = null;

		// Creo eventuale documento anno precedente
		if (rigaContabile.getRigheRateoRisconto().get(0).getRateiRiscontiAnno().get(0).getRigaContabile() != null) {
			rigaContabileRateoAnnoPrecedente = rigaContabile.getRigheRateoRisconto().get(0).getRateiRiscontiAnno()
					.get(0).getRigaContabile();
			areaContabile = rigaContabileRateoAnnoPrecedente.getAreaContabile();
			areaContabile = areaContabileCancellaManager.cancellaRigheContabili(areaContabile);
		} else {
			// Creo il documento per il rateo anno precedente
			TipoAreaContabile tipoAreaContabile = null;
			try {
				tipoAreaContabile = tipiAreaContabileManager
						.caricaTipoAreaContabilePerTipoOperazione(TipoOperazioneTipoDocumento.CHIUSURA_RATEI);
			} catch (ContabilitaException e) {
				logger.error("Errore durante il caricamento del tipo documento di chiusura dei risconti.", e);
				throw new RuntimeException(
						"Errore durante il caricamento del tipo documento di chiusura dei risconti.", e);
			}
			areaContabile = new AreaContabile();
			areaContabile.getDocumento().setCodiceAzienda(
					rigaContabile.getAreaContabile().getDocumento().getCodiceAzienda());
			areaContabile.getDocumento().setTipoDocumento(tipoAreaContabile.getTipoDocumento());
			areaContabile.setTipoAreaContabile(tipoAreaContabile);
		}

		rigaContabileRateoAnnoPrecedente = (RigaContabileRateiRisconti) RigaContabile.creaRigaContabile(
				new RigaContabileRateiRisconti(), areaContabile, contoRateo, !isDare, importoRateo, null, false);

		rigaContabileGiroContoAnnoPrecedente = RigaContabile.creaRigaContabile(new RigaContabile(), areaContabile,
				rigaContabile.getConto(), isDare, importoRateo, null, false);

		int anno = rigaContabile.getRigheRateoRisconto().get(0).getRateiRiscontiAnno().get(0).getAnno();
		areaContabile.setAnnoIva(anno);
		areaContabile.setAnnoMovimento(anno);
		areaContabile.setCambio(BigDecimal.ONE);
		areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
		Calendar dataDocumentoCal = Calendar.getInstance();
		dataDocumentoCal.set(Calendar.YEAR, anno);
		dataDocumentoCal.set(Calendar.MONTH, 11);
		dataDocumentoCal.set(Calendar.DAY_OF_MONTH, 31);
		Date dataDocumento = dataDocumentoCal.getTime();
		dataDocumento = PanjeaEJBUtil.getDateTimeToZero(dataDocumento);
		areaContabile.setDataRegistrazione(dataDocumento);
		areaContabile.getDocumento().setDataDocumento(dataDocumento);
		Importo totaleDoc = new Importo(rigaContabile.getAreaContabile().getDocumento().getTotale().getCodiceValuta(),
				BigDecimal.ONE);
		totaleDoc.setImportoInValuta(importoRateo);
		totaleDoc.calcolaImportoValutaAzienda(2);
		areaContabile.getDocumento().setTotale(totaleDoc);
		try {
			areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, true);
			areaContabile = areaContabileManager.validaRigheContabili(areaContabile, false);
		} catch (AreaContabileDuplicateException | DocumentoDuplicateException | ContabilitaException
				| RigheContabiliNonValidiException e) {
			logger.error("-->errore nel salvare e validare l'area contabile", e);
			throw new RuntimeException("-->errore nel salvare e validare l'area contabile", e);
		}

		try {
			rigaContabileRateoAnnoPrecedente.setAreaContabile(areaContabile);
			rigaContabileRateoAnnoPrecedente.setImporto(importoRateo);
			rigaContabileRateoAnnoPrecedente = panjeaDAO.save(rigaContabileRateoAnnoPrecedente);
			for (RigaRateoRisconto rateoRisconto : rigaContabile.getRigheRateoRisconto()) {
				rateoRisconto.getRateiRiscontiAnno().get(0).setRigaContabile(rigaContabileRateoAnnoPrecedente);
			}

			if (rigaContabileGiroContoAnnoPrecedente != null) {
				rigaContabileGiroContoAnnoPrecedente.setAreaContabile(areaContabile);
				rigaContabileGiroContoAnnoPrecedente.setImporto(importoRateo);
				rigaContabileGiroContoAnnoPrecedente = panjeaDAO.save(rigaContabileGiroContoAnnoPrecedente);
			}
		} catch (DAOException e) {
			logger.error("-->errore nel salvare la riga rateo per il documento anno precendete", e);
			throw new RuntimeException("-->errore nel salvare la riga rateo per il documento anno precendete", e);
		}
		return rigaContabile;
	}
}
