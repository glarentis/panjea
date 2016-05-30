/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Classe che raccoglie <code>BeneAmmortizzabile</code> e i suoi figli.
 * 
 * @author adriano
 * @version 1.0, 28/nov/06
 * 
 */
public class BeneAmmortizzabileConFigli implements Serializable {

	private static final long serialVersionUID = -4999349229780072381L;

	private BeneAmmortizzabile beneAmmortizzabile;
	private List<BeneAmmortizzabile> beniAmmortizzabiliFigli;

	/**
	 * Costruttore di default.
	 */
	public BeneAmmortizzabileConFigli() {
		initialize();
	}

	/**
	 * @return Returns the beneAmmortizzabile.
	 */
	public BeneAmmortizzabile getBeneAmmortizzabile() {
		return beneAmmortizzabile;
	}

	/**
	 * @return Returns the beniAmmortizzabiliFigli.
	 */
	public List<BeneAmmortizzabile> getBeniAmmortizzabiliFigli() {
		return beniAmmortizzabiliFigli;
	}

	/**
	 * @return JRDataSource per reports
	 */
	public JRDataSource getBeniAmmortizzabiliFigliDS() {
		return new JRBeanCollectionDataSource(getBeniAmmortizzabiliFigli());
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {

	}

	/**
	 * @param beneAmmortizzabile
	 *            The beneAmmortizzabile to set.
	 */
	public void setBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		this.beneAmmortizzabile = beneAmmortizzabile;
	}

	/**
	 * @param beniAmmortizzabiliFigli
	 *            The beniAmmortizzabiliFigli to set.
	 */
	public void setBeniAmmortizzabiliFigli(List<BeneAmmortizzabile> beniAmmortizzabiliFigli) {
		this.beniAmmortizzabiliFigli = beniAmmortizzabiliFigli;
	}

}
