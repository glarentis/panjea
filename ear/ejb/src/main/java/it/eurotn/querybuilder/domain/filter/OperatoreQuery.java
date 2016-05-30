package it.eurotn.querybuilder.domain.filter;

public enum OperatoreQuery {
    MINORE(new FiltroOperatore("<")), MINORE_UGUALE(new FiltroOperatore("<=")), UGUALE(
            new FiltroOperatore("=")), MAGGIORE(new FiltroOperatore(">")), MAGGIORE_UGUALE(
                    new FiltroOperatore(">=")), CONTIENE(new LikeOperatore()), TRA(new TraOperatore()), IN(
                            new InOperatore());

    private FiltroQuery filtro;

    /**
     *
     * @param operatore
     *            operatore sql
     */
    private OperatoreQuery(final FiltroQuery filtro) {
        this.filtro = filtro;

    }

    /**
     * @return Returns the filtro.
     */
    public FiltroQuery getFiltro() {
        return filtro;
    }
}