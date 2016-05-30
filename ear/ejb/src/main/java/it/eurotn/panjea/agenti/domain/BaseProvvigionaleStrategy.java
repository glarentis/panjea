package it.eurotn.panjea.agenti.domain;

public enum BaseProvvigionaleStrategy {
    LISTINO("Panjea.ListinoCalcoloPrezzoProvvigioneStrategy"), ULTIMO_COSTO_AZIENDALE(
            "Panjea.UltimoCostoAziendaPrezzoProvvigioneStrategy"), ULTIMO_COSTO_DEPOSITO(
                    "Panjea.UltimoCostoDepositoPrezzoProvvigioneStrategy"), PREZZO_NETTO(
                            "Panjea.PrezzoNettoCalcoloPrezzoProvvigioneStrategy");

    private String jndiCalculatorStrategy;

    /**
     * Costruttore.
     * 
     */
    private BaseProvvigionaleStrategy() {
        this.jndiCalculatorStrategy = "";
    }

    /**
     * Costruttore.
     * 
     * @param jndiCalculatorStrategy
     *            nome jndi del bean che esegue il calcolo
     */
    private BaseProvvigionaleStrategy(final String jndiCalculatorStrategy) {
        this.jndiCalculatorStrategy = jndiCalculatorStrategy;
    }

    /**
     * @return the jndiCalculatorStrategy
     */
    public String getJndiCalculatorStrategy() {
        return jndiCalculatorStrategy;
    }
}