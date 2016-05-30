/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import commonj.work.Work;

/**
 * @author leonardo
 */
public class ArticoloAnagraficaWork implements Work {

	private static Logger logger = Logger.getLogger(ArticoloAnagraficaWork.class);

	private Map<Integer, ArticoloAnagrafica> articoliAnagrafica;

	private MrpManager mrpManager;

	/**
	 * Costruttore.
	 * 
	 * @param mrpManager
	 *            mrpManager
	 */
	public ArticoloAnagraficaWork(final MrpManager mrpManager) {
		super();
		this.mrpManager = mrpManager;
	}

	/**
	 * @return the articoliAnagrafica to get
	 */
	public Map<Integer, ArticoloAnagrafica> getArticoliAnagrafica() {
		return articoliAnagrafica;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {
		articoliAnagrafica = null;
	}

	@Override
	public void run() {
		articoliAnagrafica = new HashMap<Integer, ArticoloAnagrafica>();
		try {
			List<ArticoloAnagrafica> articoliAnagraficaList = mrpManager.caricaArticoloAnagrafica();
			for (ArticoloAnagrafica articoloAnagrafica : articoliAnagraficaList) {
				articoliAnagrafica.put(articoloAnagrafica.getIdArticolo(), articoloAnagrafica);
			}
		} catch (Exception e) {
			logger.error("Errore nel caricare le anagrafiche articolo mrp", e);
			throw new RuntimeException("Errore nel caricare le anagrafiche articolo mrp", e);
		}
	}

}
