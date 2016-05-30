package it.eurotn.panjea.contabilita.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Salva le informazioni per i conti base.
 * 
 * @author giangi
 * 
 */
public class ContiBase {
	private Map<ETipoContoBase, SottoConto> mapContiBase;

	/**
	 * Costruttore.
	 * 
	 * @param contiBase
	 *            conti base
	 */
	public ContiBase(final List<ContoBase> contiBase) {
		mapContiBase = new HashMap<ETipoContoBase, SottoConto>();
		for (ContoBase contoBase : contiBase) {
			mapContiBase.put(contoBase.getTipoContoBase(), contoBase.getSottoConto());
		}
	}

	/**
	 * Controlla se esiste il conto in base al tipo conto.
	 * 
	 * @param tipoContoBase
	 *            tipo conto
	 * @return <code>true</code> se esiste
	 */
	public boolean containsKey(ETipoContoBase tipoContoBase) {
		return mapContiBase.containsKey(tipoContoBase);
	}

	/**
	 * Restitiusce il conto base in base al tipo.
	 * 
	 * @param tipoContoBase
	 *            tipo conto
	 * @return conto trovato
	 */
	public SottoConto get(ETipoContoBase tipoContoBase) {
		return mapContiBase.get(tipoContoBase);
	}
}
