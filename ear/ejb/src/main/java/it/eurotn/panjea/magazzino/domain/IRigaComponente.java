package it.eurotn.panjea.magazzino.domain;

public interface IRigaComponente extends IRigaArticoloDocumento {

    /**
     * @return Returns the articoloDistinta.
     */
    ArticoloLite getArticoloDistinta();

    /**
     * @return the formulaComponente
     */
    String getFormulaComponente();

    /**
     * @return Returns the rigaDistintaCollegata.
     */
    IRigaArticoloDocumento getRigaDistintaCollegata();

    /**
     * @param articoloDistinta
     *            the articoloDistinta to set
     */
    void setArticoloDistinta(ArticoloLite articoloDistinta);

    /**
     * @param formulaComponente
     *            the formulaComponente to set
     */
    void setFormulaComponente(String formulaComponente);

    /**
     * @param rigaDistintaCollegata
     *            The rigaDistintaCollegata to set.
     */
    void setRigaDistintaCollegata(IRigaArticoloDocumento rigaDistintaCollegata);

}
