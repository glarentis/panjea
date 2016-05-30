package it.eurotn.panjea.magazzino.manager.documento;

import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.ConfermaMovimentoInFatturazioneManager;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.ConfermaMovimentoInFatturazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ConfermaMovimentoInFatturazioneManager")
public class ConfermaMovimentazioneInFatturazioneManagerBean implements ConfermaMovimentoInFatturazioneManager {

	private static Logger logger = Logger.getLogger(ConfermaMovimentazioneInFatturazioneManagerBean.class);

	@EJB
	@IgnoreDependency
	private AreaMagazzinoManager areaMagazzinoManager;

	@EJB
	@IgnoreDependency
	private AreaRateManager areaRateManager;

	@EJB
	@IgnoreDependency
	private MagazzinoDocumentoService magazzinoDocumentoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void confermaMovimentoInFatturazione(AreaMagazzino areaMagazzino, Date dataCreazione) {
		Calendar calendar = Calendar.getInstance();

		// datiGenerazione =
		// movimentoFatturazioneDTO.getAreaMagazzino().getDatiGenerazione();

		// setto il codice documento a null così gli verrà assegnato al
		// salvataggio
		areaMagazzino.getDocumento().getCodice().setCodice(null);
		// porto lo stato area magazzino in provvisorio
		areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);
		// setto la data di creazione fatturazione
		areaMagazzino.getDatiGenerazione().setDataCreazione(dataCreazione);
		areaMagazzino.setStatoSpedizione(StatoSpedizione.NON_SPEDITO);

		AreaMagazzino areaMagazzinoConfermata;
		try {
			areaMagazzinoConfermata = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, true);
		} catch (RuntimeException er) {
			logger.error("--> Errore durante la conferma del documento.", er);
			throw er;
		} catch (Exception e) {
			logger.error("--> Errore durante la conferma del documento.", e);
			throw new RuntimeException("Errore durante la conferma del documento.", e);
		}

		// datiGenerazione.setDataCreazione(dataCreazione.getTime());

		AreaRate areaRate = areaRateManager.caricaAreaRate(areaMagazzino.getDocumento());
		try {
			magazzinoDocumentoService.validaRigheMagazzino(areaMagazzinoConfermata, areaRate, false, false);
		} catch (Exception e) {
			logger.error("--> Errore durante la generazione del documento.", e);
			throw new RuntimeException("Errore durante la generazione del documento.", e);
		}

		logger.debug("--> Movimento confermato in "
				+ (Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()));
	}

}
