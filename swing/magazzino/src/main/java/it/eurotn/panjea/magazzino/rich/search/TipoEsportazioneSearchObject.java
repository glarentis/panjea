/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * SearchObject per il caricamento di {@link TipoAreaMagazzino} da utilizzare nelle ricerche.
 * 
 * @author adriano
 * @version 1.0, 29/ago/2008
 */
public class TipoEsportazioneSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(TipoEsportazioneSearchObject.class);

	private static final String SEARCH_OBJECT_ID = "tipoEsportazioneSearchObject";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * 
	 */
	public TipoEsportazioneSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter getData");
		List<TipoEsportazione> tipiEsportazione = magazzinoAnagraficaBD.caricaTipiEsportazione(valueSearch);
		logger.debug("--> Exit getData");
		return tipiEsportazione;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
