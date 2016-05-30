package it.eurotn.panjea.magazzino.exception;

public class FormulaMrpCalcoloArticoloException extends FormulaException {

	private Integer idDistinta;
	private Integer idComponente;
	private String codDistinta;
	private String codComponente;

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 7621512337248049703L;

	/**
	 *
	 * @param formula
	 *            formula che ha generato l'errore
	 * @param errore
	 *            errore generato
	 * @param idDistinta
	 *            id della distinta
	 * @param idComponente
	 *            id del componente
	 */
	public FormulaMrpCalcoloArticoloException(final String formula, final String errore, final Integer idDistinta,
			final Integer idComponente) {
		super(formula, errore);
		this.idDistinta = idDistinta;
		this.idComponente = idComponente;
	}

	/**
	 * @return Returns the codComponente.
	 */
	public String getCodComponente() {
		return codComponente;
	}

	/**
	 * @return Returns the codDistinta.
	 */
	public String getCodDistinta() {
		return codDistinta;
	}

	/**
	 * @return Returns the idComponente.
	 */
	public Integer getIdComponente() {
		return idComponente;
	}

	/**
	 * @return Returns the idDistinta.
	 */
	public Integer getIdDistinta() {
		return idDistinta;
	}

	/**
	 *
	 * @return testo dell'eccezione.
	 */
	public String getMessageComplete() {
		StringBuilder sb = new StringBuilder("Errore nel calcolo del componente ").append(codComponente)
				.append(" per la distinta ").append(codDistinta).append("\n");
		sb.append("Formula: ").append(formula).append(". Err: ").append(getMessage());
		return sb.toString();
	}

	/**
	 * @param codComponente
	 *            The codComponente to set.
	 */
	public void setCodComponente(String codComponente) {
		this.codComponente = codComponente;
	}

	/**
	 * @param codDistinta
	 *            The codDistinta to set.
	 */
	public void setCodDistinta(String codDistinta) {
		this.codDistinta = codDistinta;
	}

	/**
	 * @param idComponente
	 *            The idComponente to set.
	 */
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}

	/**
	 * @param idDistinta
	 *            The idDistinta to set.
	 */
	public void setIdDistinta(Integer idDistinta) {
		this.idDistinta = idDistinta;
	}

}
