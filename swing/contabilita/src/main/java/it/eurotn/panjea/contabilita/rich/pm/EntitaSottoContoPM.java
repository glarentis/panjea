/**
 *
 */
package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;

/**
 * 
 * @author adriano
 * @version 1.0, 11/giu/07
 * 
 */
public class EntitaSottoContoPM implements IDefProperty {

	private SottotipoConto sottoTipoConto;

	private SottoConto sottoConto;

	/**
	 * Costruttore.
	 * 
	 */
	public EntitaSottoContoPM() {
		this.sottoConto = new SottoConto();
	}

	@Override
	public String getDomainClassName() {
		return sottoConto.getDomainClassName();
	}

	@Override
	public Integer getId() {
		return sottoConto.getId();
	}

	/**
	 * @return Returns the sottoConto.
	 */
	public SottoConto getSottoConto() {
		return sottoConto;
	}

	/**
	 * @return Returns the sottoTipoConto.
	 */
	public SottotipoConto getSottoTipoConto() {
		return sottoTipoConto;
	}

	@Override
	public Integer getVersion() {
		return sottoConto.getVersion();
	}

	@Override
	public boolean isNew() {
		return getId() == null;
	}

	/**
	 * @param sottoConto
	 *            The sottoConto to set.
	 */
	public void setSottoConto(SottoConto sottoConto) {
		this.sottoConto = sottoConto;
	}

	/**
	 * @param sottoTipoConto
	 *            The sottoTipoConto to set.
	 */
	public void setSottoTipoConto(SottotipoConto sottoTipoConto) {
		this.sottoTipoConto = sottoTipoConto;
	}

}
