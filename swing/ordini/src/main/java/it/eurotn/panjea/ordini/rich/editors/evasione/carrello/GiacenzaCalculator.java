package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe che si occupa di calcolare la giacenza di una lista di righe evasione. La giacenza per un nuovo
 * articolo-deposito viene calcolata effettuando una chiamata sul manager, in seguito il valore viene messo in cache
 * internamente alla classe per non dover più effettuare la chiamata al manager.
 * 
 * @author fattazzo
 * 
 */
public class GiacenzaCalculator {

	/**
	 * Contiene il valore dela giacenza di ogni singolo articolo per deposito.
	 */
	private Map<DepositoLite, Map<ArticoloLite, Double>> mapGiacenze;

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	private boolean isRunning;

	/**
	 * Costruttore.
	 * 
	 * @param ordiniDocumentoBD
	 *            ordiniDocumentoBD
	 */
	public GiacenzaCalculator(final IOrdiniDocumentoBD ordiniDocumentoBD) {
		super();
		mapGiacenze = new HashMap<DepositoLite, Map<ArticoloLite, Double>>();
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

	/**
	 * Calcola la giacenza per tutte le righe evasione usando la giacenza in cache se esiste.
	 * 
	 * @param righe
	 *            righe evasione
	 * @return {@link Collection} di {@link RigaDistintaCarico} con giacenza avvalorata
	 */
	public Collection<? extends RigaDistintaCarico> calculate(Collection<? extends RigaDistintaCarico> righe) {
		isRunning = true;
		try {
			Set<DepositoLite> giacenzeDepositiDaCercare = new HashSet<DepositoLite>();
			for (RigaDistintaCarico rigaEvasione : righe) {
				if (!mapGiacenze.containsKey(rigaEvasione.getDeposito())) {
					// non ho la giacenza quindi dovrò calcolarla
					giacenzeDepositiDaCercare.add(rigaEvasione.getDeposito());
				}
			}
			if (!giacenzeDepositiDaCercare.isEmpty()) {

				Date dataGiacenza = Calendar.getInstance().getTime();

				for (DepositoLite depositoLite : giacenzeDepositiDaCercare) {
					Map<ArticoloLite, Double> map = ordiniDocumentoBD.calcolaGiacenze(depositoLite, dataGiacenza);
					mapGiacenze.put(depositoLite, map);
				}
			}

			for (RigaDistintaCarico rigaEvasione : righe) {
				Double giacenza = mapGiacenze.get(rigaEvasione.getDeposito()).get(rigaEvasione.getArticolo());

				rigaEvasione.setQtaGiacenza((giacenza != null) ? giacenza : 0.0);
			}
		} finally {
			isRunning = false;
		}
		return righe;
	}

	/**
	 * Rimuove tutte le giacenze dalla cache.
	 */
	public void clear() {
		this.mapGiacenze.clear();
	}

	/**
	 * @return the mapGiacenze
	 */
	public Map<DepositoLite, Map<ArticoloLite, Double>> getMapGiacenze() {
		return Collections.synchronizedMap(mapGiacenze);
	}

	/**
	 * @return Returns the isRunning.
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Calcola la giacenza per tutte le righe evasione invalidando la cache interna in modo tale da ricalcolare tutte le
	 * giacenze dal manager.
	 * 
	 * @param righe
	 *            righe evasione
	 * @return {@link Collection} di {@link RigaDistintaCarico} con giacenza avvalorata
	 */
	public Collection<? extends RigaDistintaCarico> ricalculate(Collection<RigaDistintaCarico> righe) {
		this.mapGiacenze.clear();
		return calculate(righe);
	}
}
