package it.eurotn.panjea.intra.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TotaliDichiarazione implements Serializable {
	private static final long serialVersionUID = -75290590943828653L;

	/**
	 * @return Returns the serialversionuid.
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private BigInteger numRigheSezione1;
	private BigInteger numRigheSezione2;
	private BigInteger numRigheSezione3;
	private BigInteger numRigheSezione4;

	private BigDecimal importoSezione1;
	private BigDecimal importoSezione2;
	private BigDecimal importoSezione3;
	private BigDecimal importoSezione4;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param totali
	 *            array contentente numero righe (posizione 0 della seconda dimensione) ed importi (posizione 1
	 */
	public TotaliDichiarazione(final BigDecimal[][] totali) {
		numRigheSezione1 = totali[1][0] == null ? BigInteger.valueOf(0) : totali[1][0].toBigInteger();
		numRigheSezione2 = totali[2][0] == null ? BigInteger.valueOf(0) : totali[2][0].toBigInteger();
		numRigheSezione3 = totali[3][0] == null ? BigInteger.valueOf(0) : totali[3][0].toBigInteger();
		numRigheSezione4 = totali[4][0] == null ? BigInteger.valueOf(0) : totali[4][0].toBigInteger();

		importoSezione1 = totali[1][1] == null ? BigDecimal.ZERO : totali[1][1];
		importoSezione2 = totali[2][1] == null ? BigDecimal.ZERO : totali[2][1];
		importoSezione3 = totali[3][1] == null ? BigDecimal.ZERO : totali[3][1];
		importoSezione4 = totali[4][1] == null ? BigDecimal.ZERO : totali[4][1];
	}

	/**
	 * @return Returns the importoSezione1.
	 */
	public BigDecimal getImportoSezione1() {
		return importoSezione1;
	}

	/**
	 * @return Returns the importoSezione2.
	 */
	public BigDecimal getImportoSezione2() {
		return importoSezione2;
	}

	/**
	 * @return Returns the importoSezione3.
	 */
	public BigDecimal getImportoSezione3() {
		return importoSezione3;
	}

	/**
	 * @return Returns the importoSezione4.
	 */
	public BigDecimal getImportoSezione4() {
		return importoSezione4;
	}

	/**
	 * @return Returns the numRigheSezione1.
	 */
	public BigInteger getNumRigheSezione1() {
		return numRigheSezione1;
	}

	/**
	 * @return Returns the numRigheSezione2.
	 */
	public BigInteger getNumRigheSezione2() {
		return numRigheSezione2;
	}

	/**
	 * @return Returns the numRigheSezione3.
	 */
	public BigInteger getNumRigheSezione3() {
		return numRigheSezione3;
	}

	/**
	 * @return Returns the numRigheSezione4.
	 */
	public BigInteger getNumRigheSezione4() {
		return numRigheSezione4;
	}

}
