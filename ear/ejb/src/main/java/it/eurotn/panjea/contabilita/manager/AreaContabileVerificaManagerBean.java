/*
 * 
 */
package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileVerificaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.LibroGiornaleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.RegistroIvaManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;

import java.math.BigDecimal;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Manager per verificare ed eseguire eventualmente delle operazioni sull'areaContabile stessa o su dati ad essa
 * collegati come ad es. registri, giornali, ecc.
 * 
 * @author Leonardo
 */
@Stateless(name = "Panjea.AreaContabileVerificaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaContabileVerificaManager")
public class AreaContabileVerificaManagerBean implements AreaContabileVerificaManager {

	private static Logger logger = Logger.getLogger(AreaContabileVerificaManagerBean.class);
	/**
	 * @uml.property name="libroGiornaleManager"
	 * @uml.associationEnd
	 */
	@EJB
	private LibroGiornaleManager libroGiornaleManager;
	/**
	 * @uml.property name="registroIvaManager"
	 * @uml.associationEnd
	 */
	@EJB
	private RegistroIvaManager registroIvaManager;
	/**
	 * @uml.property name="areaIvaManager"
	 * @uml.associationEnd
	 */
	@EJB
	@IgnoreDependency
	private AreaIvaManager areaIvaManager;
	/**
	 * @uml.property name="areaContabileManager"
	 * @uml.associationEnd
	 */
	@EJB
	@IgnoreDependency
	private AreaContabileManager areaContabileManager;

	@EJB
	@IgnoreDependency
	private AreaRateManager areaRateManager;

	@Override
	public void checkCreaInvalidaAreaIva(AreaContabile areaContabileOld, AreaContabile areaContabileNew) {
		logger.debug("--> Enter checkCreaInvalidaAreaIva");
		// controllo se devo creare una nuova areaIva o nel caso esista se devo aggiornare il link con
		// l'area contabile appena salvata
		AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabileNew);

		if (areaContabileOld != null && areaIva != null && areaIva.getId() != null
				&& isTotaliDiversi(areaContabileOld, areaContabileNew)) {
			areaIva = areaIvaManager.invalidaAreaIva(areaIva);
		}

		if (areaIva.isNew() && isRigheIvaEnabled(areaContabileNew)) {
			areaIva = areaIvaManager.creaAreaIva(areaContabileNew);
		} else if (!areaIva.isNew() && isRigheIvaEnabled(areaContabileNew)) {
			if (areaIva.getAreaContabile() == null) {
				areaIva = areaIvaManager.associaAreaContabile(areaIva, areaContabileNew);
			}
		}

		// associo il documento all'areaIva come da standard scelto dove ogni area e' collegata
		// al documento senza conoscere altre aree.Al momento ho collegato areaContabile ad areaIva
		if (areaIva.getId() != null) {
			areaIva.setDocumento(areaContabileNew.getDocumento());
		}
		logger.debug("--> Exit checkCreaInvalidaAreaIva");
	}

	@Override
	public void checkInvalidaDocumentiContabilita(AreaContabile areaContabileOld, AreaContabile areaContabileNew)
			throws ContabilitaException {
		logger.debug("--> Enter invalidaDocumentiContabili");
		invalidaLibroGiornale(areaContabileOld, areaContabileNew);
		invalidaRegistroIva(areaContabileOld, areaContabileNew);
		logger.debug("--> Exit invalidaDocumentiContabili");
	}

	@Override
	public AreaContabile invalidaAreaContabile(AreaContabile areaContabile, boolean changeConfermatoToProvvisorio) {
		return areaContabileManager.invalidaAreaContabile(areaContabile, changeConfermatoToProvvisorio);
	}

	@Override
	public AreaIva invalidaAreaIva(AreaContabile areaContabile) {
		AreaIva areaIvaSalvata = null;
		AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabile);
		if (areaIva != null && areaIva.getId() != null) {
			areaIvaSalvata = areaIvaManager.invalidaAreaIva(areaIva);
		}
		return areaIvaSalvata;
	}

	@Override
	public void invalidaAreaRate(AreaContabile areaContabile) {
		AreaRate areaRate = areaRateManager.caricaAreaRate(areaContabile.getDocumento());
		if (areaRate != null && areaRate.getId() != null) {
			areaRate.getDatiValidazione().invalida();
			areaRateManager.salvaAreaRate(areaRate);
		}
	}

	@Override
	public void invalidaLibroGiornale(AreaContabile areaContabileOld, AreaContabile areaContabileNew)
			throws ContabilitaException {
		logger.debug("--> Enter invalidaLibroGiornale");
		// l'invalidazione del giornale devo sempre chiamarla dato che comunque anche se
		// inserisco un nuovo documento devo chiamare l'invalida in modo che possa verificare
		// ad es che inserisco un documento nel periodo in cui un giornale o registro iva � stato gi� stampato

		// devo invalidare il giornale se le date sono diverse,il totale diverso o note diverse
		if (areaContabileOld != null) {
			if (isStatoConfermato(areaContabileOld)
					&& (isDateRegistrazioneDiverse(areaContabileOld, areaContabileNew)
							|| isTotaliDiversi(areaContabileOld, areaContabileNew)
							|| isDateDocumentoDiverse(areaContabileOld, areaContabileNew) || isNoteDiverse(
								areaContabileOld, areaContabileNew))) {
				libroGiornaleManager.invalidaLibroGiornale(areaContabileNew, areaContabileOld.getDataRegistrazione());
			}
		} else {
			libroGiornaleManager.invalidaLibroGiornale(areaContabileNew);
		}
		logger.debug("--> Exit invalidaLibroGiornale");
	}

	@Override
	public void invalidaRegistroIva(AreaContabile areaContabileOld, AreaContabile areaContabileNew) {
		logger.debug("--> Enter invalidaRegistroIva");
		if (areaContabileOld != null) {
			if (isStatoConfermato(areaContabileOld)
					&& (isDateRegistrazioneDiverse(areaContabileOld, areaContabileNew)
							|| isDateDocumentoDiverse(areaContabileOld, areaContabileNew) || isTotaliDiversi(
								areaContabileOld, areaContabileNew))) {
				registroIvaManager.invalidaGiornaleIva(areaContabileNew, areaContabileOld.getDataRegistrazione());
			}
		} else {
			registroIvaManager.invalidaGiornaleIva(areaContabileNew);
		}
		logger.debug("--> Exit invalidaRegistroIva");
	}

	@Override
	public boolean isDateDocumentoDiverse(AreaContabile areaContabileOld, AreaContabile areaContabileNew) {
		logger.debug("--> Enter isDateDocumentoDiverse");
		boolean isConfirmed = false;
		if (areaContabileOld != null) {
			isConfirmed = areaContabileNew.getDocumento().getDataDocumento()
					.compareTo(areaContabileOld.getDocumento().getDataDocumento()) != 0;
		}
		logger.debug("--> Exit isDateDocumentoDiverse " + isConfirmed);
		return isConfirmed;
	}

	/**
	 * Se è stata aggiornata la data registrazione.
	 * 
	 * @param areaContabileOld
	 *            prima della modifica
	 * @param areaContabileNew
	 *            dopo la modifica
	 * @return true or false
	 */
	private boolean isDateRegistrazioneDiverse(AreaContabile areaContabileOld, AreaContabile areaContabileNew) {
		logger.debug("--> Enter isDateRegistrazioneDiverse");
		boolean isConfirmed = false;
		if (areaContabileOld != null) {
			isConfirmed = areaContabileNew.getDataRegistrazione().compareTo(areaContabileOld.getDataRegistrazione()) != 0;
		}
		logger.debug("--> Exit isDateRegistrazioneDiverse " + isConfirmed);
		return isConfirmed;
	}

	/**
	 * Verifica se la proprieta' note di areaContabile e' stata aggiornata.
	 * 
	 * @param areaContabileOld
	 *            area prima della modifica
	 * @param areaContabileNew
	 *            area dopo la modifica
	 * @return true o false
	 */
	private boolean isNoteDiverse(AreaContabile areaContabileOld, AreaContabile areaContabileNew) {
		logger.debug("--> Enter isNoteDiverse");
		boolean isConfirmed = false;
		String noteOld = areaContabileOld.getNote();
		String noteNew = areaContabileNew.getNote();

		boolean noteOldEmpty = noteOld == null || (noteOld != null && noteOld.equals(""));
		boolean noteNewEmpty = noteNew == null || (noteNew != null && noteNew.equals(""));

		if ((noteOld != null && noteNew != null && !noteNew.equals(noteOld)) || (noteOldEmpty && !noteNewEmpty)
				|| (!noteOldEmpty && noteNewEmpty)) {
			isConfirmed = true;
		}
		logger.debug("--> Exit isNoteDiverse");
		return isConfirmed;
	}

	@Override
	public boolean isRigheIvaEnabled(AreaContabile areaContabile) {
		logger.debug("--> Enter isRigheIvaEnabled");
		boolean isConfirmed = areaContabile.getDocumento().getTipoDocumento().isRigheIvaEnable();
		logger.debug("--> Exit isRigheIvaEnabled " + isConfirmed);
		return isConfirmed;
	}

	/**
	 * Verifica se lo stato è confermato.
	 * 
	 * @param areaContabile
	 *            su cui verificare lo stato
	 * @return true or false
	 */
	private boolean isStatoConfermato(AreaContabile areaContabile) {
		logger.debug("--> Enter isStatoConfermato");
		boolean isConfirmed = areaContabile.getStatoAreaContabile().equals(AreaContabile.StatoAreaContabile.CONFERMATO);
		logger.debug("--> Exit isStatoConfermato " + isConfirmed);
		return isConfirmed;
	}

	@Override
	public boolean isTotaliDiversi(AreaContabile areaContabileOld, AreaContabile areaContabileNew) {
		logger.debug("--> Enter isTotaliDiversi");
		// se non ho un vecchio valore da verificare non li considero diversi
		if (areaContabileOld == null) {
			return false;
		}
		BigDecimal totDocumento = areaContabileNew.getDocumento().getTotale().getImportoInValuta();
		BigDecimal totDocumentoPrecedente = areaContabileOld.getDocumento().getTotale().getImportoInValuta();
		logger.debug("--> totale documento attuale " + totDocumento.toString());
		logger.debug("--> totale documento precedente " + totDocumentoPrecedente.toString());
		boolean isConfirmed = totDocumento.compareTo(totDocumentoPrecedente) != 0;
		logger.debug("--> Exit isTotaliDiversi " + isConfirmed);
		return isConfirmed;
	}

}
