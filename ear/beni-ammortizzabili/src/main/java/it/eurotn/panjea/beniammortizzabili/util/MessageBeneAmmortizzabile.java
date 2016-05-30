/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.util;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.security.JecPrincipal;

import java.io.Serializable;

/**
 * Classe che rappresenta l'object message di JMS per il ricalcolo delle simulazioni.
 * 
 * @author adriano
 * @version 1.0, 17/ott/07
 * 
 */
public class MessageBeneAmmortizzabile implements Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = -818775734276165418L;

	private BeneAmmortizzabile beneAmmortizzabile;

	private Integer anno;

	private JecPrincipal jecPrincipal;

	/**
	 * @return Returns the anno.
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return Returns the beneAmmortizzabile.
	 */
	public BeneAmmortizzabile getBeneAmmortizzabile() {
		return beneAmmortizzabile;
	}

	/**
	 * @return Returns the jecPrincipal.
	 */
	public JecPrincipal getJecPrincipal() {
		return jecPrincipal;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param beneAmmortizzabile
	 *            The beneAmmortizzabile to set.
	 */
	public void setBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		this.beneAmmortizzabile = beneAmmortizzabile;
	}

	/**
	 * @param jecPrincipal
	 *            The jecPrincipal to set.
	 */
	public void setJecPrincipal(JecPrincipal jecPrincipal) {
		this.jecPrincipal = jecPrincipal;
	}

}
