package it.eurotn.panjea.magazzino.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_versioni_listino", uniqueConstraints = @UniqueConstraint(columnNames = { "listino_id",
        "dataVigore" }) )
@NamedQueries({
        @NamedQuery(name = "VersioneListino.caricaLastCodice", query = "select max(v.codice) from VersioneListino v where v.listino = :paramListino "),
        @NamedQuery(name = "VersioneListino.caricaAll", query = " select v from VersioneListino v where v.listino.codiceAzienda=:paramCodiceAzienda order by v.listino.codice "),
        @NamedQuery(name = "VersioneListino.caricaByData", query = " select v from VersioneListino v where v.listino.id=:listino and v.dataVigore<=:data order by v.dataVigore desc ") })
@EntityConverter(properties = "codice,dataVigore")
public class VersioneListino extends EntityBase {

    private static final long serialVersionUID = -2406866277020813172L;

    @Column(nullable = false)
    private Integer codice;

    @Temporal(TemporalType.DATE)
    private Date dataVigore;

    @ManyToOne(optional = false)
    private Listino listino;

    @OneToMany(mappedBy = "versioneListino", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("articolo asc")
    private List<RigaListino> righeListino;

    /**
     * @return codice
     */
    public Integer getCodice() {
        return codice;
    }

    /**
     * @return dataVigore
     */
    public Date getDataVigore() {
        return dataVigore;
    }

    /**
     * @return listino
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return righeListino
     * @uml.property name="righeListino"
     */
    public List<RigaListino> getRigheListino() {
        return righeListino;
    }

    /**
     *
     * @return lista degli scaglioni del listino
     */
    public List<ScaglioneListino> getScaglioni() {
        List<ScaglioneListino> result = new ArrayList<ScaglioneListino>();
        for (RigaListino rigaListino : righeListino) {
            result.addAll(rigaListino.getScaglioni());
        }
        return result;
    }

    /**
     * @param codice
     *            the codice to set
     * @uml.property name="codice"
     */
    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    /**
     * @param dataVigore
     *            the dataVigore
     * @uml.property name="dataVigore"
     */
    public void setDataVigore(Date dataVigore) {
        this.dataVigore = dataVigore;
    }

    /**
     * @param listino
     *            the listino to set
     * @uml.property name="listino"
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

    /**
     * @param righeListino
     *            the righeListino to set
     * @uml.property name="righeListino"
     */
    public void setRigheListino(List<RigaListino> righeListino) {
        this.righeListino = righeListino;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("VersioneListino[");
        buffer.append("codice = ").append(codice);
        buffer.append(" dataVigore = ").append(dataVigore);
        buffer.append(" listino = ").append(listino);
        buffer.append("]");
        return buffer.toString();
    }
}
