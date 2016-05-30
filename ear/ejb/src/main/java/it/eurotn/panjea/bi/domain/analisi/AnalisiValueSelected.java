package it.eurotn.panjea.bi.domain.analisi;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColonnaFunzione;

/**
 * Contiene una lista di valori selezionati in un campo per un analisi.<br/>
 *
 * @author giangi
 */
@Entity
@Audited
@Table(name = "bi_analisi_filtro")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_FILTRO", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("V")
public class AnalisiValueSelected extends EntityBase {

    private static final long serialVersionUID = -6239172193948439510L;

    @Column(length = 50)
    private String nomeCampo;

    // Memorizzo i parametri come stringa separata da #
    @Column(length = 8000)
    private String stringParameter;

    /**
     * @return nome del campo al quale è associato il filtro
     */
    public String getNomeCampo() {
        return nomeCampo;
    }

    /**
     * @return lista di valori selezionati nel campo
     */
    public Object[] getParameter() {
        return stringParameter.split("#");
    }

    /**
     * SQL.
     *
     * @param colonna
     *            colonna
     * @param aliasTabellaMisura
     *            alias
     * @return sql creato
     */
    public StringBuilder getSql(Colonna colonna, String aliasTabellaMisura) {
        StringBuilder whereSelected = new StringBuilder(100);

        whereSelected.append(" AND ");
        if (!(colonna instanceof ColonnaFunzione)) {
            whereSelected.append(colonna.getTabella().getAlias());
            whereSelected.append(".");
        }
        whereSelected.append(colonna.getNome());
        whereSelected.append(" IN (");

        Object[] valoriDaFiltrare = getParameter();
        for (Object object : valoriDaFiltrare) {
            if (object != null) {
                whereSelected.append("'");
                whereSelected.append(object.toString().replace("'", "''"));
                whereSelected.append("'");
                whereSelected.append(",");
            }
        }
        whereSelected = new StringBuilder(whereSelected.substring(0, whereSelected.length() - 1));
        whereSelected.append(" )");
        return whereSelected;
    }

    /**
     * @param nomeCampo
     *            nome del campo al quale è associato il filtro
     */
    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    /**
     * @param parameters
     *            lista di valori selezionati per il campo
     */
    public void setParameter(Object[] parameters) {
        StringBuilder sb = new StringBuilder();
        for (Object object : parameters) {
            sb.append(object);
            sb.append("#");
        }
        stringParameter = sb.toString();
    }
}
