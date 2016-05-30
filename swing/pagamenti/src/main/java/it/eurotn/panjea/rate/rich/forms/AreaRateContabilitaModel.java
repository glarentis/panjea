/**
 *
 */
package it.eurotn.panjea.rate.rich.forms;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.rate.domain.AreaRate;

/**
 * Model di {@link AreaRate} per la sua gestione all'interno del modulo di contabilita'.
 * 
 * @author adriano
 * @version 1.0, 17/ott/2008
 */
public class AreaRateContabilitaModel extends AbstractAreaRateModel {

	private AreaContabileFullDTO areaContabileFullDTO;

	@Override
	public Object caricaAreaDocumentoFullDTO(Integer id) {
		return getRateBD().caricaAreaContabileFullDTO(id);
	}

	@Override
	public IAreaDocumento getAreaDocumento() {
		return areaContabileFullDTO.getAreaContabile();
	}

	@Override
	public AreaRate getAreaRate() {
		if (areaContabileFullDTO != null) {
			return this.areaContabileFullDTO.getAreaRate();
		}
		return null;
	}

	@Override
	public boolean isAreaRatePresente() {
		return areaContabileFullDTO.isAreaRateEnabled()
				&& areaContabileFullDTO.getAreaRate().isGenerazioneRateAllowed();
	}

	@Override
	public boolean isAreaRateValida() {
		logger.debug("--> Enter isAreaPartitaValida");
		boolean isValida = true;

		// se null non e' valida
		if (areaContabileFullDTO.getAreaRate() == null) {
			isValida = false;
		} else {
			// disabilita se Area Partite non e' istanziata o se risulta essere validata
			if ((areaContabileFullDTO.getAreaRate().isNew())
					|| (!areaContabileFullDTO.getAreaRate().getDatiValidazione().isValid())) {
				isValida = false;
			}
		}
		logger.debug("--> Exit isAreaPartitaValida con valore " + isValida);
		return isValida;
	}

	@Override
	public boolean isChanged() {
		logger.debug("--> Enter isChanged");
		boolean changed = true;
		AreaContabile areaContabile = (AreaContabile) getAreaDocumento();
		if ((areaContabile.getStatoAreaContabile() == StatoAreaContabile.PROVVISORIO || areaContabile
				.getStatoAreaContabile() == StatoAreaContabile.SIMULATO) && !isAreaRateValida()) {
			changed = false;
		}
		logger.debug("--> Exit isChanged con valore " + changed);
		return changed;
	}

	@Override
	public boolean isEnabled() {
		boolean enabled = false;

		if (areaContabileFullDTO == null) {
			return false;
		}
		/*
		 * se l'area contabile e' nuova o se lo stato e' uguale a VERIFICATO o se non e' presente una AreaPartite
		 * disabilita tutti i commands e la pagina
		 */
		if ((areaContabileFullDTO.isNew())
				|| (StatoAreaContabile.VERIFICATO.equals(areaContabileFullDTO.getAreaContabile()
						.getStatoAreaContabile())) || (areaContabileFullDTO.getAreaRate().isNew())
				|| !areaContabileFullDTO.getAreaRate().isGenerazioneRateAllowed()
				|| (!areaContabileFullDTO.getAreaContabile().isValidRigheContabili())) {
			enabled = false;
		} else {
			enabled = true;
		}
		return enabled;
	}

	/**
	 * Ricarica l'areaContabileFullDTO dato che e' stata modificata una area non gestita all'interno di questo model.
	 * 
	 * @param isAreaPartiteValidaNew
	 */
	@Override
	public void reloadAreaRate() {
		AreaRate areaRate = getRateBD().caricaAreaRateByDocumento(
				areaContabileFullDTO.getAreaRate().getDocumento().getId());
		areaContabileFullDTO.setAreaRate(areaRate);
	}

	@Override
	public void setObject(Object object) {
		areaContabileFullDTO = (AreaContabileFullDTO) object;
	}

	@Override
	public void validaAreaRate() {
		logger.debug("--> Enter validaAreaPartite");
		AreaRate areaRate = areaContabileFullDTO.getAreaRate();
		AreaContabile areaContabile = areaContabileFullDTO.getAreaContabile();
		getRateBD().validaRateAreaPartita(areaRate, areaContabile);
		// Ho cambiato lo stato del documento
		areaContabileFullDTO = getRateBD().caricaAreaContabileFullDTO(areaContabile.getId());
		// notifica all'editor che ho cambiato oggetto
		firePropertyChange(AREA_MODEL_AGGIORNATA, null, areaContabileFullDTO);
		logger.debug("--> Exit validaAreaPartite");
	}

}
