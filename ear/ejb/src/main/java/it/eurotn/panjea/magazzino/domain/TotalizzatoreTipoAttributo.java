package it.eurotn.panjea.magazzino.domain;

/**
 * Enum per definire il totalizzatore documento associato al tipo attributo.<br>
 * La totalizzatore lega delle proprietà specifiche della testata del documento (Dati accompagnatori) ai relativi tipi
 * attributi:<br>
 * <ul>
 * <li>PESO: Peso netto</li>
 * <li>VOLUME: Volume</li>
 * <li>NUMERO_COLLI: Numero colli</li>
 * </ul>
 *
 * @author leonardo
 */
public enum TotalizzatoreTipoAttributo {

    NESSUNO(null), PESO("kg"), VOLUME("m3"), NUMERO_COLLI("nr");

    private String unitaMisuraDefault;

    /**
     * @param unitaMisuraDefault
     *            l'unità di misura di default
     */
    private TotalizzatoreTipoAttributo(final String unitaMisuraDefault) {
        this.unitaMisuraDefault = unitaMisuraDefault;
    }

    /**
     * @return the unitaMisuraDefault
     */
    public String getUnitaMisuraDefault() {
        return unitaMisuraDefault;
    }

}
