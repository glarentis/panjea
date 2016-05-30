package it.eurotn.panjea.magazzino.manager.documento.fatturazione;

import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces.DocumentoFatturazioneGenerator;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces.PreparaFatturazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneDifferitaGenerator;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Genera i documenti temporanei necessari alla rifatturazione differita.<br/>
 *
 * @author giangi
 */
@Stateless(mappedName = "Panjea.RifatturazioneDifferitaGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RifatturazioneDifferitaGenerator")
public class RifatturazioneDifferitaGeneratorBean extends AbstractFatturazioneDifferitaGenerator implements
		FatturazioneDifferitaGenerator {

	private static Logger logger = Logger.getLogger(RifatturazioneDifferitaGeneratorBean.class);

	@EJB
	@IgnoreDependency
	private PreparaFatturazioneManager preparaFatturazioneManager;

	@EJB
	private DocumentoFatturazioneGenerator documentoFatturazioneGenerator;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	@Override
	public void genera(List<AreaMagazzinoLite> areeDaFatturare, Date dataDocumentoDestinazione,
			String noteFatturazione, SedeMagazzinoLite sedePerRifatturazione, String utente)
					throws RigaArticoloNonValidaException, SedePerRifatturazioneAssenteException {
		Calendar calendarInizio = Calendar.getInstance();

		// genero l'UUID per la contabilizzazione
		Random random = new Random();
		long r1 = random.nextLong();
		long r2 = random.nextLong();
		String uuid = Long.toHexString(r1) + Long.toHexString(r2);

		// chiamo il carica sul preparaFatturazioneManager per aprire la
		// transazione che mi permette di eseguire la query
		List<AreaMagazzinoFatturazione> areeFatturazione = preparaFatturazioneManager
				.caricaAreeMagazzinoFatturazione(areeDaFatturare);

		AreaMagazzinoFatturazione areaRif = areeFatturazione.get(0);
		documentoFatturazioneGenerator.generaDocumentoRifatturazione(areeFatturazione,
				areaRif.getTipoAreaMagazzinoPerFatturazione(), dataDocumentoDestinazione, noteFatturazione,
				sedePerRifatturazione, uuid, utente);

		logger.debug("--> Exit generaDocumentoFatturazione. Tempo di esecuzione: "
				+ (Calendar.getInstance().getTimeInMillis() - calendarInizio.getTimeInMillis()));
	}

}
