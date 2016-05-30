/**
 *
 */
package it.eurotn.panjea.anagrafica.documenti.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author fattazzo
 *
 */
@Embeddable
public class CodiceDocumento implements Serializable, Comparable<CodiceDocumento> {

	private static final long serialVersionUID = 6904267682273944170L;

	private static final Pattern CODICE_ORDER_PATTERN = Pattern.compile("[\\D*]+|[0-9]+");

	@Column(length = 30)
	private String codice;

	@Column(length = 60)
	private String codiceOrder;

	@Override
	public int compareTo(CodiceDocumento o) {
		if (getCodiceOrder() == null) {
			return -1;
		} else if (o.getCodiceOrder() == null) {
			return 1;
		} else {
			return getCodiceOrder().compareTo(o.getCodiceOrder());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CodiceDocumento other = (CodiceDocumento) obj;
		if (codiceOrder == null) {
			if (other.codiceOrder != null) {
				return false;
			}
		} else if (!codiceOrder.equals(other.codiceOrder)) {
			return false;
		}
		return true;
	}

	/**
	 * Formatta il codice passato per ottenere il codice per gli ordinamenti.
	 *
	 * @param code
	 *            codice
	 * @return codice formattatto per gli ordinamenti
	 */
	private String formatCodeForOrder(String code) {

		StringBuilder codeResult = new StringBuilder(60);

		Matcher matcher = CODICE_ORDER_PATTERN.matcher(StringUtils.defaultString(code));

		while (matcher.find()) {
			String group = matcher.group();
			if (StringUtils.isNumeric(group)) {
				codeResult.append(StringUtils.leftPad(group, 10, "0"));
			} else {
				codeResult.append(group);
			}
		}

		return StringUtils.left(codeResult.toString(), 60);
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the codiceOrder
	 */
	public String getCodiceOrder() {
		return codiceOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codiceOrder == null) ? 0 : codiceOrder.hashCode());
		return result;
	}

	/**
	 * @return <code>true</code> se il codice è nullo o vuoto
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(codice);
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
		this.codiceOrder = formatCodeForOrder(codice);
	}

	/**
	 * @param codiceOrder
	 *            the codiceOrder to set
	 */
	public void setCodiceOrder(String codiceOrder) {
		this.codiceOrder = codiceOrder;
	}

	@Override
	public String toString() {
		return "CodiceDocumento [codice=" + codice + ", codiceOrder=" + codiceOrder + "]";
	}
}
