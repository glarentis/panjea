/**
 *
 */
package it.eurotn.panjea.rate.rich.forms;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.math.BigDecimal;

/**
 * Model per la gestione di {@link AreaPartite} all'interno del modulo di magazzino.
 * 
 * @author adriano
 * @version 1.0, 17/ott/2008
 */
public class AreaRateMagazzinoModel extends AbstractAreaRateModel {

	private AreaMagazzinoFullDTO areaMagazzinoFullDTO;

	@Override
	public Object caricaAreaDocumentoFullDTO(Integer id) {
		return getRateBD().caricaAreaMagazzinoFullDTO(id);
	}

	@Override
	public IAreaDocumento getAreaDocumento() {
		return areaMagazzinoFullDTO.getAreaMagazzino();
	}

	@Override
	public AreaRate getAreaRate() {
		if (areaMagazzinoFullDTO != null) {
			return areaMagazzinoFullDTO.getAreaRate();
		}
		return null;
	}

	@Override
	public boolean isAreaRatePresente() {
		return areaMagazzinoFullDTO.isAreaRateEnabled()
				&& areaMagazzinoFullDTO.getAreaRate().isGenerazioneRateAllowed();
	}

	@Override
	public boolean isAreaRateValida() {
		logger.debug("--> Enter isAreaPartitaValida");
		boolean isValida = true;
		AreaRate areaRate = areaMagazzinoFullDTO.getAreaRate();

		// disabilita se Area Partite non e' istanziata o se risulta non essere
		// validata
		// se la lista di rate partita e' null o vuota
		if (((areaRate.isNew()) || (!areaRate.getDatiValidazione().isValid()))) {
			if (areaRate.getDocumento().getTotale().getImportoInValutaAzienda().compareTo(BigDecimal.ZERO) != 0
					&& ((areaRate.getRate() == null) || (areaRate.getRate() != null && areaRate.getRate().size() == 0))) {
				isValida = false;
			}
		}
		logger.debug("--> Exit isAreaPartitaValida");
		return isValida;
	}

	@Override
	public boolean isChanged() {
		logger.debug("--> Enter isChanged");
		boolean changed = true;
		AreaMagazzino areaMagazzino = (AreaMagazzino) getAreaDocumento();
		if (areaMagazzino.getStatoAreaMagazzino() == StatoAreaMagazzino.PROVVISORIO && !isAreaRateValida()) {
			changed = false;
		}
		logger.debug("--> Exit isChanged con valore " + changed);
		return changed;
	}

	@Override
	public boolean isEnabled() {
		if (areaMagazzinoFullDTO == null) {
			return false;
		}

		AreaRate areaPartite = areaMagazzinoFullDTO.getAreaRate();
		AreaMagazzino areaMagazzino = areaMagazzinoFullDTO.getAreaMagazzino();
		AreaIva areaIva = areaMagazzinoFullDTO.getAreaIva();
		AreaContabileLite areaContabileLite = areaMagazzinoFullDTO.getAreaContabileLite();

		boolean areaRateDiTipoNessuna = (areaPartite.getId() != null && areaPartite.getTipoAreaPartita()
				.getTipoOperazione().equals(TipoOperazione.NESSUNA));
		boolean areaMagazzinoInFatturazione = areaMagazzino.getStatoAreaMagazzino() == StatoAreaMagazzino.INFATTURAZIONE;
		boolean righeMagazzineNonValide = !areaMagazzino.getDatiValidazioneRighe().isValid();
		boolean areaIvaNonValida = !areaIva.getDatiValidazioneRighe().isValid();

		/*
		 * se l'area magazzino e' nuova o se non e' presente un area partite il model e' disabilitato
		 */
		// return (areaMagazzinoFullDTO.isNew() || areaPartite.isNew() || areaRateDiTipoNessuna
		// || areaMagazzinoInFatturazione || righeMagazzineNonValide || areaIvaNonValida || areaContabileLite == null);
		return (!areaMagazzinoFullDTO.isNew() && !areaPartite.isNew() && !areaRateDiTipoNessuna
				&& !areaMagazzinoInFatturazione && !righeMagazzineNonValide && !areaIvaNonValida && areaContabileLite == null);

	}

	@Override
	public void reloadAreaRate() {
		AreaRate areaRate = getRateBD().caricaAreaRateByDocumento(
				areaMagazzinoFullDTO.getAreaRate().getDocumento().getId());
		areaMagazzinoFullDTO.setAreaRate(areaRate);
	}

	@Override
	public void setObject(Object object) {
		areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) object;
	}

	@Override
	public void validaAreaRate() {
		logger.debug("--> Enter confermaRatePartite ");
		AreaRate areaPartite = areaMagazzinoFullDTO.getAreaRate();
		getRateBD().validaRateAreaPartita(areaPartite, areaMagazzinoFullDTO.getAreaMagazzino());
		// ho cambiato lo stato da provvisorio a confermato
		areaMagazzinoFullDTO = getRateBD().caricaAreaMagazzinoFullDTO(areaMagazzinoFullDTO.getAreaMagazzino().getId());

		// notifica all'editor che ho cambiato oggetto
		firePropertyChange(AREA_MODEL_AGGIORNATA, null, areaMagazzinoFullDTO);
		logger.debug("--> Exit confermaRatePartite");
	}

}
