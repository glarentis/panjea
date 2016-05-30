/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.panjea.contabilita.domain.SottoConto;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.ObjectUtils;

/**
 * @author fattazzo
 *
 */
@Embeddable
public class SottocontiBeni implements Serializable {

	private static final long serialVersionUID = -4295225258536730555L;

	@ManyToOne(optional = true)
	private SottoConto sottoContoImmobilizzazione;

	@ManyToOne(optional = true)
	private SottoConto sottoContoAmmortamento;

	@ManyToOne(optional = true)
	private SottoConto sottoContoFondoAmmortamento;

	@ManyToOne(optional = true)
	private SottoConto sottoContoAmmortamentoAnticipato;

	@ManyToOne(optional = true)
	private SottoConto sottoContoFondoAmmortamentoAnticipato;

	/**
	 * Avvalora i sottoconti non presenti con i sottoconti passati come parametro.
	 *
	 * @param sottocontiBeniAdd
	 *            sottoconti da aggiungere
	 */
	public void avvaloraSottoContiMancanti(SottocontiBeni sottocontiBeniAdd) {
		if (sottocontiBeniAdd == null) {
			return;
		}
		setSottoContoAmmortamento(ObjectUtils.defaultIfNull(getSottoContoAmmortamento(),
				sottocontiBeniAdd.getSottoContoAmmortamento()));
		setSottoContoAmmortamentoAnticipato(ObjectUtils.defaultIfNull(
				getSottoContoAmmortamentoAnticipato(), sottocontiBeniAdd.getSottoContoAmmortamentoAnticipato()));
		setSottoContoFondoAmmortamento(ObjectUtils.defaultIfNull(getSottoContoFondoAmmortamento(),
				sottocontiBeniAdd.getSottoContoFondoAmmortamento()));
		setSottoContoFondoAmmortamentoAnticipato(ObjectUtils.defaultIfNull(
				getSottoContoFondoAmmortamentoAnticipato(),
				sottocontiBeniAdd.getSottoContoFondoAmmortamentoAnticipato()));
	}

	/**
	 * @return the sottoContoAmmortamento
	 */
	public SottoConto getSottoContoAmmortamento() {
		return sottoContoAmmortamento;
	}

	/**
	 * @return the sottoContoAmmortamentoAnticipato
	 */
	public SottoConto getSottoContoAmmortamentoAnticipato() {
		return sottoContoAmmortamentoAnticipato;
	}

	/**
	 * @return the sottoContoFondoAmmortamento
	 */
	public SottoConto getSottoContoFondoAmmortamento() {
		return sottoContoFondoAmmortamento;
	}

	/**
	 * @return the sottoContoFondoAmmortamentoAnticipato
	 */
	public SottoConto getSottoContoFondoAmmortamentoAnticipato() {
		return sottoContoFondoAmmortamentoAnticipato;
	}

	/**
	 * @return the sottoContoImmobilizzazione
	 */
	public SottoConto getSottoContoImmobilizzazione() {
		return sottoContoImmobilizzazione;
	}

	/**
	 * Indica se tutti i sotto conti necessari alla generazione delle aree contabili dall simulazione sono presenti.
	 *
	 * @return <code>true</code> se validi
	 */
	public boolean isSottoContiGenerazioneAreaContabileValid() {
		return sottoContoAmmortamento != null && sottoContoFondoAmmortamento != null
				&& sottoContoAmmortamentoAnticipato != null && sottoContoFondoAmmortamentoAnticipato != null;
	}

	/**
	 * @param sottoContoAmmortamento
	 *            the sottoContoAmmortamento to set
	 */
	public void setSottoContoAmmortamento(SottoConto sottoContoAmmortamento) {
		this.sottoContoAmmortamento = sottoContoAmmortamento;
	}

	/**
	 * @param sottoContoAmmortamentoAnticipato
	 *            the sottoContoAmmortamentoAnticipato to set
	 */
	public void setSottoContoAmmortamentoAnticipato(SottoConto sottoContoAmmortamentoAnticipato) {
		this.sottoContoAmmortamentoAnticipato = sottoContoAmmortamentoAnticipato;
	}

	/**
	 * @param sottoContoFondoAmmortamento
	 *            the sottoContoFondoAmmortamento to set
	 */
	public void setSottoContoFondoAmmortamento(SottoConto sottoContoFondoAmmortamento) {
		this.sottoContoFondoAmmortamento = sottoContoFondoAmmortamento;
	}

	/**
	 * @param sottoContoFondoAmmortamentoAnticipato
	 *            the sottoContoFondoAmmortamentoAnticipato to set
	 */
	public void setSottoContoFondoAmmortamentoAnticipato(SottoConto sottoContoFondoAmmortamentoAnticipato) {
		this.sottoContoFondoAmmortamentoAnticipato = sottoContoFondoAmmortamentoAnticipato;
	}

	/**
	 * @param sottoContoImmobilizzazione
	 *            the sottoContoImmobilizzazione to set
	 */
	public void setSottoContoImmobilizzazione(SottoConto sottoContoImmobilizzazione) {
		this.sottoContoImmobilizzazione = sottoContoImmobilizzazione;
	}
}
