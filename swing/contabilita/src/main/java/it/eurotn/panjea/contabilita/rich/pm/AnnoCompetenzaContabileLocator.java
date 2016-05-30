/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.anagrafica.rich.pm.AbstractAnnoCompetenzaLocator;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;

import java.io.Serializable;

import org.springframework.beans.factory.InitializingBean;

/**
 * Anno di competenza contabile.
 * 
 * @author Leonardo
 */
public class AnnoCompetenzaContabileLocator extends AbstractAnnoCompetenzaLocator implements InitializingBean,
		Serializable {

	private static final long serialVersionUID = -5960755210548521857L;
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;
	private ContabilitaSettings contabilitaSettings;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(contabilitaAnagraficaBD, "contabilitaAnagraficaBD cannot be null!");
	}

	@Override
	public Integer getAnnoCompetenza() {
		if (contabilitaSettings == null) {
			contabilitaSettings = contabilitaAnagraficaBD.caricaContabilitaSettings();
		}
		return contabilitaSettings.getAnnoCompetenza();
	}

	/**
	 * @return the contabilitaAnagraficaBD
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            the contabilitaAnagraficaBD to set
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}

}
