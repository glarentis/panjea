package it.eurotn.panjea.contabilita.rich.editors.tabelle;

/**
 * Classe PM per la tabella dei mesi.
 * 
 * @author Leonardo
 */
public class MesePM {

	private int mese;
	private int anno;

	{
		anno = 0;
		mese = 0;
	}

	/**
	 * Costruttore.
	 */
	public MesePM() {
		super();
	}

	/**
	 * Costruttore di default.
	 * 
	 * @param mese
	 *            mese
	 * @param anno
	 *            anno
	 */
	public MesePM(final int mese, final int anno) {
		setMese(mese);
		setAnno(anno);
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
		MesePM other = (MesePM) obj;
		if (anno != other.anno) {
			return false;
		}
		if (mese != other.mese) {
			return false;
		}
		return true;
	}

	/**
	 * @return the anno
	 */
	public int getAnno() {
		return anno;
	}

	/**
	 * @return the mese
	 */
	public int getMese() {
		return mese;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anno;
		result = prime * result + mese;
		return result;
	}

	/**
	 * @param anno
	 *            the anno to set
	 */
	public void setAnno(int anno) {
		this.anno = anno;
	}

	/**
	 * @param mese
	 *            the mese to set
	 */
	public void setMese(int mese) {
		this.mese = mese;
	}

	@Override
	public String toString() {
		return "MesePM[mese:" + mese + " ,anno:" + anno + "]";
	}
}