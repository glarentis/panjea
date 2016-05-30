/**
 *
 */
package it.eurotn.panjea.iva.rich.forms.righeiva;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.binding.value.support.AbstractPropertyChangePublisher;

/**
 * Model di areaIva, presenta metodi e proprieta' utili per gestire l'area iva, le sue righe e il suo ciclo rispetto
 * alle altre aree.
 * 
 * @author adriano
 * @version 1.0, 02/ott/2008
 * 
 */
public abstract class AbstractAreaIvaModel extends AbstractPropertyChangePublisher {

	private static Logger logger = Logger.getLogger(AbstractAreaIvaModel.class);

	private IAreaDocumento areaDocumento = null;
	public static final String RIGA_AGGIORNATA = "rigaAggiornata";
	public static final String AREA_MODEL_AGGIORNATA = "modelAggiornato";

	public static final String AREA_IVA_NON_PRESENTE = "ivaModel.hasntAreaIva";

	/**
	 * Costruttore.
	 */
	public AbstractAreaIvaModel() {
		super();
	}

	/**
	 * Aggiorna il modello dell'Iva.<br/>
	 * Se l'area iva è quadrata la valida, altrimenti controlla se l'area documento è cambiata, in questo caso la
	 * ricarica e notifica il cambiamento
	 */
	private void aggiornaModel() {
		// se e' stato cambiato lo stato dell'area (magazzino o contabile) o il flag valido area iva devo notificare
		// alla page registrata che l'area documento e' cambiata
		if (isChanged()) {
			Object areaDocumentoFullDTO = caricaAreaDocumentoFullDTO(getAreaDocumento().getId());
			setObject(areaDocumentoFullDTO);
			firePropertyChange(AREA_MODEL_AGGIORNATA, null, areaDocumentoFullDTO);
		}
	}

	/**
	 * Cancella la rigaIva e la rimuove dalla lista di righe di areaIva.
	 * 
	 * @param rigaIva
	 *            la riga da cancellare e rimuovere
	 */
	public void cancellaRigaIva(RigaIva rigaIva) {
		rigaIva.setAreaIva(getAreaIva());
		doDeleteRigaIva(rigaIva);
		// rimuovo la riga da this.areaIva
		removeRigaIva(rigaIva);

		// notifico all'ascoltatori che ho aggiornato la lista di righe iva dell'area iva corrente
		// ricarico l'areaIva perche' lo stato puo essere cambiato dopo la cancellazione di una riga
		firePropertyChange(RIGA_AGGIORNATA, null, rigaIva);
		aggiornaModel();
	}

	/**
	 * carica dal lato business l'oggetto AreaDocumento specifica per il modulo in cui è ereditata questa classe<br>
	 * (es: AreaContabileFullDTO per il modulo contabile, AreaMagazzinoFullDTO per il modulo di magazzino ).
	 * 
	 * @param id
	 *            area documento
	 * @return area documento full DTO
	 */
	public abstract Object caricaAreaDocumentoFullDTO(Integer id);

	/**
	 * crea e restituisce un istanza di {@link RigaIva} come nuovo oggetto.
	 * 
	 * @return riga iva creata
	 */
	public RigaIva creaNuovaRiga() {
		RigaIva rigaIva = new RigaIva();
		rigaIva.setAreaIva(getAreaIva());
		Importo totale = getAreaDocumento().getDocumento().getTotale();
		rigaIva.getImponibile().setCodiceValuta(totale.getCodiceValuta());
		rigaIva.getImponibile().setTassoDiCambio(totale.getTassoDiCambio());

		Importo imponibileVisualizzato = totale.clone();
		BigDecimal importoSquadrato = getImportoSquadrato();
		imponibileVisualizzato.setImportoInValuta(importoSquadrato);
		imponibileVisualizzato.calcolaImportoValutaAzienda(RigaIva.SCALE_FISCALE);

		if (importoSquadrato.compareTo(BigDecimal.ZERO) != 0) {
			rigaIva.setImponibileVisualizzato(imponibileVisualizzato);
		}
		rigaIva.getImposta().setCodiceValuta(totale.getCodiceValuta());
		rigaIva.getImposta().setTassoDiCambio(totale.getTassoDiCambio());
		rigaIva.getImpostaCollegata().setCodiceValuta(totale.getCodiceValuta());
		rigaIva.getImpostaCollegata().setTassoDiCambio(totale.getTassoDiCambio());
		return rigaIva;
	}

	/**
	 * metodo abstract incaricato di eseguire la cancellazione di {@link RigaIva} <br>
	 * verso il lato business.
	 * 
	 * @param rigaIva
	 *            riga iva da cancellare
	 */
	protected abstract void doDeleteRigaIva(RigaIva rigaIva);

	/**
	 * metodo abstract per il salvataggio di {@link RigaIva} verso il business layer.
	 * 
	 * @param rigaIva
	 *            riga iva da salvare
	 * @throws CodiceIvaCollegatoAssenteException
	 *             sollevata se manca il codice iva collegato
	 * @return riga iva salvata
	 */
	protected abstract RigaIva doSaveRigaIva(RigaIva rigaIva) throws CodiceIvaCollegatoAssenteException;

	/**
	 * @return Returns the areaDocumento.
	 */
	public abstract IAreaDocumento getAreaDocumento();

	/**
	 * @return area iva
	 */
	public abstract AreaIva getAreaIva();

	/**
	 * getter del codice valuta corrente del model.
	 * 
	 * @return codice valuta dell'area documento
	 */
	public String getCodiceValutaCorrente() {
		return areaDocumento.getDocumento().getTotale().getCodiceValuta();
	}

	/**
	 * Restituisce l'importo della squadratura dell'area iva.
	 * 
	 * @return importo
	 */
	public abstract BigDecimal getImportoSquadrato();

	/**
	 * ritorna l'oggetto gestito dal modello.
	 * 
	 * @return oggetto
	 */
	public abstract Object getObject();

	/**
	 * Ritorna una lista di righe iva per l'area iva corrente.
	 * 
	 * @return List<RigaIva> valorizzata per area iva esistente o una lista vuota se area iva e' null
	 */
	protected List<RigaIva> getRigheIva() {
		List<RigaIva> righeIva = null;
		if (isAreaIvaPresente()) {
			righeIva = getAreaIva().getRigheIva();
		}
		if (righeIva == null) {
			righeIva = new ArrayList<RigaIva>();
		}
		return righeIva;
	}

	/**
	 * Calcola il totale imponibile il calcolo dell'imposta è basato sull'attributo imponibileVisualizzato.
	 * 
	 * @return Importo
	 */
	public Importo getTotaleImponibile() {
		String codiceValuta = "";
		BigDecimal cambio = BigDecimal.ZERO;
		BigDecimal imponibileInValuta = BigDecimal.ZERO;
		BigDecimal imponibileInValutaAzienda = BigDecimal.ZERO;
		for (RigaIva rigaIva : getRigheIva()) {
			codiceValuta = rigaIva.getAreaIva().getDocumento().getTotale().getCodiceValuta();
			cambio = rigaIva.getAreaIva().getDocumento().getTotale().getTassoDiCambio();
			imponibileInValuta = imponibileInValuta.add(rigaIva.getImponibileVisualizzato().getImportoInValuta());
			imponibileInValutaAzienda = imponibileInValutaAzienda.add(rigaIva.getImponibileVisualizzato()
					.getImportoInValutaAzienda());
		}
		Importo totImponibile = new Importo(codiceValuta, cambio);
		totImponibile.setImportoInValuta(imponibileInValuta);
		totImponibile.setImportoInValutaAzienda(imponibileInValutaAzienda);
		return totImponibile;
	}

	/**
	 * Calcolca il totale imposta il calcolo dell'imposta è basato sull'attributo impostaVisualizzata.
	 * 
	 * @return Importo
	 */
	public Importo getTotaleImposta() {
		String codiceValuta = "";
		BigDecimal cambio = BigDecimal.ZERO;
		BigDecimal impostaInValuta = BigDecimal.ZERO;
		BigDecimal impostaInValutaAzienda = BigDecimal.ZERO;
		for (RigaIva rigaIva : getRigheIva()) {
			codiceValuta = rigaIva.getAreaIva().getDocumento().getTotale().getCodiceValuta();
			cambio = rigaIva.getAreaIva().getDocumento().getTotale().getTassoDiCambio();
			impostaInValuta = impostaInValuta.add(rigaIva.getImpostaVisualizzata().getImportoInValuta());
			impostaInValutaAzienda = impostaInValutaAzienda.add(rigaIva.getImpostaVisualizzata()
					.getImportoInValutaAzienda());
		}
		Importo totImposta = new Importo(codiceValuta, cambio);
		totImposta.setImportoInValuta(impostaInValuta);
		totImposta.setImportoInValutaAzienda(impostaInValutaAzienda);
		return totImposta;

	}

	/**
	 * Verifica se e' presente l'area iva.
	 * 
	 * @return true se esiste una area iva o false altrimenti
	 */
	public final boolean isAreaIvaPresente() {
		return (getAreaIva() != null && getAreaIva().getId() != null);
	}

	/**
	 * Verifica se le righe iva sono quadrate rispetto al totale documento verificando i diversi casi (gestione iva
	 * intra o normale).
	 * 
	 * @return true o false
	 */
	public abstract boolean isAreaIvaQuadrata();

	/**
	 * metodo incaricato di verificare se l'AreaIva ha subito variazioni riguardanti il suo stato.
	 * 
	 * @return <code>true</code> se ci sono cambiamenti
	 */
	public abstract boolean isChanged();

	/**
	 * verifica se il Model di AreaIva è abilitato.
	 * 
	 * @return <code>true</code> se abilitato
	 */
	public abstract boolean isEnabled();

	/**
	 * testa se la gestione iva INTRA o ART.17 e' abilitata.
	 * 
	 * @return true or false
	 */
	public abstract boolean isIntraAbilitato();

	/**
	 * Verifica se l'area iva risulta essere valida.
	 * 
	 * @return true se l'area iva e' valida o false altrimenti
	 */
	public boolean isRigheIvaValide() {
		return isAreaIvaPresente() && getAreaIva().getDatiValidazioneRighe().isValid();
	}

	/**
	 * Verifica se l'areaIva e' legata a un tipo documento con validazione area iva automatica.
	 * 
	 * @return true se la validazione e' automatica false altrimenti
	 */
	public abstract boolean isValidazioneAreaIvaAutomatica();

	/**
	 * metodo abstract incaricato di ricaricare {@link AreaIva} <br>
	 * dal business layer.
	 */
	protected abstract void reloadAreaIva();

	/**
	 * Rimuove una riga da quelle presenti.
	 * 
	 * @param rigaIva
	 *            la riga da rimuovere
	 */
	private void removeRigaIva(RigaIva rigaIva) {
		getRigheIva().remove(rigaIva);
	}

	/**
	 * Aggiorna il riferimento di areaIva ricaricandolo dal business layer.
	 */
	public void ricaricaAreaIva() {
		if (isAreaIvaPresente()) {
			reloadAreaIva();
		}
	}

	/**
	 * Salva la rigaIva e la aggiunge alla lista di righe di areaIva.
	 * 
	 * @param rigaIva
	 *            la riga da aggiungere
	 * @return RigaIva salvata
	 * @throws CodiceIvaCollegatoAssenteException
	 *             sollevata se manca il codice iva collagato
	 */
	public RigaIva salvaRigaIva(RigaIva rigaIva) throws CodiceIvaCollegatoAssenteException {
		logger.debug("--> Enter salvaRigaIva size " + getRigheIva().size());
		RigaIva rigaIvaSalvata = doSaveRigaIva(rigaIva);
		// se l'id esiste aggiorno la riga
		if (rigaIva.getId() != null && rigaIva.getId().intValue() != -1) {
			int index = getRigheIva().indexOf(rigaIvaSalvata);
			getRigheIva().remove(rigaIvaSalvata);
			getRigheIva().add(index, rigaIvaSalvata);
		} else {
			// altrimenti ne ho una nuova da inserire
			getRigheIva().add(rigaIvaSalvata);
		}

		// notifico agli ascoltatori che ho aggiornato la lista di righe iva dell'area iva corrente
		// aggiorno area iva della riga iva salvata dato che l'areaIva ha documento e area contabile lazy
		// e devo fare delle verifiche sull'area iva che toccano queste proprieta'

		// notifica il cambio di una riga per aggiornare le somme
		firePropertyChange(AbstractAreaIvaModel.RIGA_AGGIORNATA, null, rigaIvaSalvata);

		aggiornaModel();
		logger.debug("--> Exit salvaRigaIva size " + getRigheIva().size());
		return rigaIvaSalvata;
	}

	/**
	 * metodo incaricato di ricevere l'object corrente del Form e recuperarne l'areaDocumento e l'areaIva.
	 * 
	 * @param object
	 *            oggetto da gestire
	 */
	public abstract void setObject(Object object);

	/**
	 * Valida l'areaIva restituendo l'areaIvaModel aggiornata.
	 */
	public abstract void validaAreaIva();

}
