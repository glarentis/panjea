/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.pm;

import it.eurotn.panjea.anagrafica.rich.pm.AbstractAnnoCompetenzaLocator;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;

import java.io.Serializable;

import org.springframework.beans.factory.InitializingBean;

/**
 * Anno di competenza di magazzino.
 * 
 * @author Leonardo
 */
public class AnnoCompetenzaMagazzinoLocator extends AbstractAnnoCompetenzaLocator implements InitializingBean,
		Serializable {

	private static final long serialVersionUID = 2269576112497317626L;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private MagazzinoSettings magazzinoSettings;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(magazzinoAnagraficaBD, "magazzinoAnagraficaBD cannot be null !");
	}

	@Override
	public Integer getAnnoCompetenza() {
		if (magazzinoSettings == null) {
			magazzinoSettings = magazzinoAnagraficaBD.caricaMagazzinoSettings();
		}
		return magazzinoSettings.getAnnoCompetenza();
	}

	/**
	 * @return the magazzinoAnagraficaBD
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
