package it.eurotn.panjea.dms.mdb;

import java.io.Serializable;

public class AttributoAggiornamentoDTO implements Serializable {

    public enum TipoAttributo {
        TIPO_DOC("TIPODOC"), ENTITA("CODENTITA"), ARTICOLO("CODARTICOLO");

        private final String nomeAttributoDMS;

        /**
         * Costruttore.
         *
         * @param nomeAttributoDMS
         *            nome attributo dms
         */
        private TipoAttributo(final String nomeAttributoDMS) {
            this.nomeAttributoDMS = nomeAttributoDMS;
        }

        /**
         * @return the nomeAttributoDMS
         */
        public String getNomeAttributoDMS() {
            return nomeAttributoDMS;
        }
    }

    private static final long serialVersionUID = 6305142870849671869L;

    private String azienda;

    private TipoAttributo tipoAttributo;

    private String value;

    /**
     * Costruttore.
     *
     * @param tipoAttributo
     *            tipo attributo
     * @param value
     *            valore
     * @param codiceAzienda
     *            codice azienda
     */
    public AttributoAggiornamentoDTO(final TipoAttributo tipoAttributo, final String value,
            final String codiceAzienda) {
        super();
        this.tipoAttributo = tipoAttributo;
        this.value = value;
        this.azienda = codiceAzienda;
    }

    /**
     * @return the azienda
     */
    public String getAzienda() {
        return azienda;
    }

    /**
     * @return the tipoAttributo
     */
    public TipoAttributo getTipoAttributo() {
        return tipoAttributo;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param azienda
     *            the azienda to set
     */
    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

}
