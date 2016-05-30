/**
 * 
 */
package it.eurotn.panjea.rate.rich.forms;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.rich.bd.IRateBD;

import java.math.BigDecimal;

import org.springframework.binding.value.support.AbstractPropertyChangePublisher;

/**
 * 
 * @author adriano
 * @version 1.0, 17/ott/2008
 */
public abstract class AbstractAreaRateModel extends AbstractPropertyChangePublisher {

	public static final String AREA_MODEL_AGGIORNATA = "modelAggiornato";
	public static final String RIGA_AGGIORNATA = "rigaAggiornata";

	private IRateBD rateBD = null;

	/**
	 * Carica il fullDTO e lo risetta.
	 */
	public void aggiornaModel() {
		// se e' stato cambiato lo stato dell'area (magazzino o contabile) o il flag valido area partite devo notificare
		// alla page registrata che l'area documento e' cambiata
		if (isChanged()) {
			Object areaDocumentoFullDTO = caricaAreaDocumentoFullDTO(getAreaDocumento().getId());
			setObject(areaDocumentoFullDTO);
			firePropertyChange(AREA_MODEL_AGGIORNATA, null, areaDocumentoFullDTO);
		}
	}

	/**
	 * cancella una rata.
	 * 
	 * @param rata
	 *            rata da cancellare
	 */
	public void cancellaRataPartita(Rata rata) {
		logger.debug("--> Enter cancellaRata");
		// risetto l'areaRate per aggiornare l'oggetto in caso di cancellazione; le rate mantengono la versione
		// precedente
		rata.setAreaRate(getAreaRate());
		this.rateBD.cancellaRata(rata);
		getAreaRate().getRate().remove(rata);

		// notifico all'ascoltatori che ho aggiornato la lista di rate dell'area rate corrente
		// ricarico l'areaRate perche' lo stato puo essere cambiato dopo la cancellazione di una riga
		firePropertyChange(RIGA_AGGIORNATA, null, rata);
		aggiornaModel();
		logger.debug("--> Exit cancellaRata");
	}

	/**
	 * carica dal lato business l'oggetto AreaDocumento specifica per il modulo in cui è ereditata questa classe<br>
	 * (es: AreaContabileFullDTO per il modulo contabile, AreaMagazzinoFullDTO per il modulo di magazzino ).
	 * 
	 * @param id
	 *            id dell'area documento da caricare
	 * @return Full Dto di un area documento
	 */
	public abstract Object caricaAreaDocumentoFullDTO(Integer id);

	/**
	 * crea un nuovo object per l'area di appartenenza.
	 * 
	 * @return nuova istanza di dell'oggetto di dominio corrente
	 */
	public final Rata creaNuovaRiga() {
		logger.debug("--> Enter creaNuovaRiga");
		Rata nuovaRata = new Rata();
		nuovaRata.setAreaRate(getAreaRate());
		nuovaRata.getImporto().setCodiceValuta(getAreaDocumento().getDocumento().getTotale().getCodiceValuta());
		nuovaRata.getImporto().setTassoDiCambio(getAreaDocumento().getDocumento().getTotale().getTassoDiCambio());
		nuovaRata.setNumeroRata(getAreaRate().getRate().size() + 1);
		logger.debug("--> Exit creaNuovaRiga");
		return nuovaRata;
	}

	/**
	 * 
	 * @return IAreaDocumento gestita dalla classe concreta.
	 */
	public abstract IAreaDocumento getAreaDocumento();

	/**
	 * Restituisce l'areaPartite del model corrente.
	 * 
	 * @return {@link AreaRate}
	 */
	public abstract AreaRate getAreaRate();

	/**
	 * @return the rateBD
	 */
	public IRateBD getRateBD() {
		return rateBD;
	}

	/**
	 * 
	 * @return true se area rate presente
	 */
	public abstract boolean isAreaRatePresente();

	/**
	 * Verifica se il totale documento e il totale delle rate conincidono.
	 * 
	 * @return true se la somma delle rate equivale al totale documento, false altrimenti
	 */
	public boolean isAreaRateQuadrata() {
		logger.debug("--> Enter isAreaPartitaQuadrata");
		boolean areaQuadrata = false;
		Importo totaleRate = getAreaRate().getTotaleRate();
		Importo totaleDocumento = getAreaRate().getDocumento().getTotale();
		if (totaleRate.getImportoInValuta().abs().compareTo(totaleDocumento.getImportoInValuta().abs()) == 0
				|| (totaleDocumento.getImportoInValuta().compareTo(BigDecimal.ZERO) == 0)) {
			areaQuadrata = true;
		}

		if (getAreaRate().getId() != null
				&& getAreaRate().getTipoAreaPartita().getTipoOperazione().equals(TipoOperazione.NESSUNA)) {
			areaQuadrata = true;
		}
		logger.debug("--> Exit isAreaPartitaQuadrata con valore " + areaQuadrata);
		return areaQuadrata;
	}

	/**
	 * 
	 * @return true se {@link AreaPartite} corrente e' valida.
	 */
	public abstract boolean isAreaRateValida();

	/**
	 * 
	 * @return true se l'AreaPartite ha subito variazioni riguardanti il suo stato.
	 */
	public abstract boolean isChanged();

	/**
	 * @return true se {@link AreaPartite} e' abilitata.
	 */
	public abstract boolean isEnabled();

	/**
	 * Ricarica l'area rate.
	 */
	protected abstract void reloadAreaRate();

	/**
	 * Aggiorna il riferimento di areaPartite ricaricandolo dal business layer.
	 */
	public void ricaricaAreaRate() {
		if (isAreaRatePresente()) {
			reloadAreaRate();
		}
	}

	/**
	 * Salva la Rata e la aggiunge alla lista di righe di AreaPartite.
	 * 
	 * @param rata
	 *            la rata da aggiungere
	 * @return Rata salvata
	 * @throws CodiceIvaCollegatoAssenteException
	 */
	public Rata salvaRata(Rata rata) {
		logger.debug("--> Enter salvaRata");

		rata = rateBD.salvaRata(rata);
		// se l'id esiste aggiorno la riga
		getAreaRate().getRate().remove(rata);
		getAreaRate().getRate().add(rata);

		// notifico agli ascoltatori che ho aggiornato la lista di rate dell'area partite corrente
		firePropertyChange(RIGA_AGGIORNATA, null, rata);

		aggiornaModel();

		logger.debug("--> Exit salvaRata");
		return rata;
	}

	/**
	 * @param object
	 *            object corrente del Form.
	 */
	public abstract void setObject(Object object);

	/**
	 * @param rateBD
	 *            the rateBD to set
	 */
	public void setRateBD(IRateBD rateBD) {
		this.rateBD = rateBD;
	}

	/**
	 * conferma dell'area partite corrente.
	 */
	public abstract void validaAreaRate();

}
