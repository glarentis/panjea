/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.search;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * SearchObject per eseguire la ricerca di {@link RegistroIva} filtri applicabili sono (gestioneIva e
 * (registroIvaCollegato o registroIvaCollegato)) per filtrare tipoRegistro ACQUISTO o VENDITA a seconda della presenza
 * del filtro registroIvaCollegato o registroIvaCollegato gestioneIva deve essere INTRA o ART17, in caso contrario non
 * vengono filtrati i valori ma vengono restituiti tutti.
 * 
 * @author Leonardo
 * @version 1.0, 27/ago/07
 */
public class RegistroIvaSearchObject extends AbstractSearchObject {

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	private static final String OBJECT_ID = "registroIvaSearchObject";

	/**
	 * Costruttore.
	 */
	public RegistroIvaSearchObject() {
		super(OBJECT_ID);
	}

	/**
	 * @return Returns the contabilitaAnagraficaBD.
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();

		// questi filtri sono aggiunti solo sulla TipoAreaContabileForm quindi
		// solo in quel caso
		// ho la possibilita' di filtrare i registri iva.
		GestioneIva gestioneIva = (GestioneIva) parameters.get("gestioneIva");
		// registro iva e' la proprieta' chiamata registroIva di
		// tipoAreaContabile
		boolean registroIva = parameters.containsKey("registroIva");
		// registro iva collegato e' la proprieta' registroIvaCollegato di
		// tipoAreaContabile
		boolean registroIvaCollegato = parameters.containsKey("registroIvaCollegato");

		List<RegistroIva> list = contabilitaAnagraficaBD.caricaRegistriIva(fieldSearch, valueSearch);
		// preparo una list di RegistroIva filtrati a seconda della proprieta'
		// gestioneIva
		// e a seconda che la search text in questione ha proprieta' registroIva
		// o registroIvaCollegato
		List<RegistroIva> registriFiltrati = new ArrayList<RegistroIva>();
		// se non sono sulla searchtext aggiunta in tipoAreaContabile non ho il
		// parametro gestioneIva
		// devo essere nel caso gestione iva INTRA o ART17 per filtrare, negli
		// altri casi carico tutto
		if (gestioneIva != null && !gestioneIva.equals(GestioneIva.NORMALE)) {
			for (RegistroIva ri : list) {
				// se ho la proprieta' registroIva devo filtrare solo i tipi
				// ACQUISTO
				if (registroIva && ri.getTipoRegistro().equals(TipoRegistro.ACQUISTO)) {
					registriFiltrati.add(ri);
				}
				// se ho la proprieta' registroIvaCollegato devo filtrare solo
				// VENDITA
				if (registroIvaCollegato && ri.getTipoRegistro().equals(TipoRegistro.VENDITA)) {
					registriFiltrati.add(ri);
				}
			}
			return registriFiltrati;
		}

		return list;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            The contabilitaAnagraficaBD to set.
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

}
