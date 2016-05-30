package it.eurotn.panjea.magazzino.domain.documento;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.entity.EntityBase;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author leonardo
 */
@Entity
@Table(name = "maga_dati_accompagnatori_metadata")
public class DatoAccompagnatorioMagazzinoMetaData extends EntityBase implements Cloneable {

    public enum TipoDatoAccompagnatorioMagazzino {

        VETTORE("vettore"), CAUSALE_TRASPORTO("causaleTrasporto"), TRASPORTO_CURA("trasportoCura"), ASPETTO_ESTERIORE(
                "aspettoEsteriore"), TIPO_PORTO("tipoPorto"), PESO_NETTO("pesoNetto"), PESO_LORDO("pesoLordo"), VOLUME(
                        "volume"), RESPONSABILE_RITIRO("responsabileRitiroMerce"), NUMERO_COLLI(
                                "numeroColli"), DATA_INIZIO_TRASPORTO("dataInizioTrasporto"), DATA_CONSEGNA(
                                        "datiSpedizioniDocumento.dataConsegna"), PALLET("pallet"), MEZZO_TRASPORTO(
                                                "mezzoTrasporto");

        private String propertyName;

        /**
         * @param propertyName
         *            the propertyName to set
         */
        TipoDatoAccompagnatorioMagazzino(final String propertyName) {
            this.propertyName = propertyName;
        }

        /**
         * @return propertyName
         */
        public String getPropertyName() {
            return propertyName;
        }

    }

    private static final long serialVersionUID = -7580236692941447458L;

    @ManyToOne
    private TipoAreaMagazzino tipoAreaMagazzino;

    @Column(length = 50)
    private String name;

    private Integer ordinamento;

    @Enumerated
    private TipoDatoAccompagnatorioMagazzino tipoDatoAccompagnatorioMagazzino;

    /**
     * Costruttore.
     */
    public DatoAccompagnatorioMagazzinoMetaData() {
        super();
        ordinamento = 0;
    }

    /**
     * Costruttore.
     *
     * @param tipoDatoAccompagnatorioMagazzino
     *            tipoDatoAccompagnatorioMagazzino
     */
    public DatoAccompagnatorioMagazzinoMetaData(
            final TipoDatoAccompagnatorioMagazzino tipoDatoAccompagnatorioMagazzino) {
        super();
        this.name = tipoDatoAccompagnatorioMagazzino.getPropertyName();
        this.ordinamento = (tipoDatoAccompagnatorioMagazzino.ordinal() + 1) * 10;
        this.tipoDatoAccompagnatorioMagazzino = tipoDatoAccompagnatorioMagazzino;
    }

    /**
     *
     * @param tipo
     *            tipo da cercare in rows
     * @param datiAccompagnatori
     *            l'elenco dei dati accompagnatori
     * @return vero se datiAccompagnatori contiene un dato accompagnatorio del tipo indicato.
     */
    public static boolean contieneTipoDatoAccompagnatorio(final TipoDatoAccompagnatorioMagazzino tipo,
            Collection<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatori) {
        for (DatoAccompagnatorioMagazzinoMetaData datoAccompagnatorioImpostato : datiAccompagnatori) {
            if (datoAccompagnatorioImpostato.getTipoDatoAccompagnatorioMagazzino().equals(tipo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object clone() {
        DatoAccompagnatorioMagazzinoMetaData dato = PanjeaEJBUtil.cloneObject(this);
        dato.setId(null);
        dato.setVersion(0);
        dato.setTipoAreaMagazzino(null);

        return dato;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DatoAccompagnatorioMagazzinoMetaData)) {
            return false;
        }

        DatoAccompagnatorioMagazzinoMetaData other = (DatoAccompagnatorioMagazzinoMetaData) obj;

        if (!name.equals(other.name)) {
            return false;
        }

        if (tipoAreaMagazzino == null) {
            return other.tipoAreaMagazzino == null;
        }

        return tipoAreaMagazzino.equals(other.tipoAreaMagazzino);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the ordinamento
     */
    public Integer getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return the tipoAreaMagazzino
     */
    public TipoAreaMagazzino getTipoAreaMagazzino() {
        return tipoAreaMagazzino;
    }

    /**
     * @return the tipoDatoAccompagnatorioMagazzino
     */
    public TipoDatoAccompagnatorioMagazzino getTipoDatoAccompagnatorioMagazzino() {
        return tipoDatoAccompagnatorioMagazzino;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((tipoAreaMagazzino == null || tipoAreaMagazzino.getId() == null) ? 0
                : tipoAreaMagazzino.getId().intValue());
        return result;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     */
    public void setOrdinamento(Integer ordinamento) {
        this.ordinamento = ObjectUtils.defaultIfNull(ordinamento, 0);
    }

    /**
     * @param tipoAreaMagazzino
     *            the tipoAreaMagazzino to set
     */
    public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        this.tipoAreaMagazzino = tipoAreaMagazzino;
    }

    /**
     * @param tipoDatoAccompagnatorioMagazzino
     *            the tipoDatoAccompagnatorioMagazzino to set
     */
    public void setTipoDatoAccompagnatorioMagazzino(TipoDatoAccompagnatorioMagazzino tipoDatoAccompagnatorioMagazzino) {
        this.tipoDatoAccompagnatorioMagazzino = tipoDatoAccompagnatorioMagazzino;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = stringBuilder.append("DatoAccompagnatorioMagazzinoMetaData ");
        stringBuilder = stringBuilder.append("[id=");
        stringBuilder = stringBuilder.append(getId());
        stringBuilder = stringBuilder.append(", name=");
        stringBuilder = stringBuilder.append(name);
        stringBuilder = stringBuilder.append(", ordinamento=");
        stringBuilder = stringBuilder.append(ordinamento);
        stringBuilder = stringBuilder.append(", tipoDatoAccompagnatorioMagazzino=");
        stringBuilder = stringBuilder.append(tipoDatoAccompagnatorioMagazzino);
        stringBuilder = stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
