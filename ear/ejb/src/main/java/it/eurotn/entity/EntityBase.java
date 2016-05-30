package it.eurotn.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import it.eurotn.entity.annotation.ExcludeFromQueryBuilder;
import it.eurotn.locking.IDefProperty;

/**
 * Classe ereditata da tutti gli oggetti persistenti.
 *
 * @author giangi
 *
 */
@MappedSuperclass
@EntityListeners(AuditEntityBaseListener.class)
public class EntityBase implements IDefProperty, Serializable, Comparable<EntityBase> {

    private static Logger logger = Logger.getLogger(EntityBase.class);

    private static final long serialVersionUID = -285546730382691201L;

    @Transient
    private int hashCode = Integer.MIN_VALUE;

    @ExcludeFromQueryBuilder
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ExcludeFromQueryBuilder
    @Version
    private Integer version;

    @ExcludeFromQueryBuilder
    private Long timeStamp;

    @Column(length = 50)
    private String userInsert;

    /**
     *
     * Costruttore.
     */
    public EntityBase() {
    }

    @Override
    public int compareTo(EntityBase obj) {
        if (obj.hashCode() > hashCode()) {
            return 1;
        } else if (obj.hashCode() < hashCode()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getId() == null) {
            return false;
        }
        if (!(obj instanceof IDefProperty)) {
            return false;
        }
        // Se è una classe proxy il nome della classe diventa xxx_$$_proxy,
        // quindi uso la startWith
        if (!obj.getClass().getCanonicalName().startsWith(this.getClass().getCanonicalName())) {
            return false;
        }
        return (this.getId().equals(((EntityBase) obj).getId()));
    }

    /**
     * @return nome della classe di dominio. Utilizzata nei VO e mantenuta per compatibilità. Di default restituisce il
     *         className della classe.
     */
    @Override
    public String getDomainClassName() {
        return this.getClass().getName();
    }

    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @return Returns the timeStamp.
     */
    public Long getTimeStamp() {
        return timeStamp;
    }

    /**
     * @return the userInsert
     */
    public String getUserInsert() {
        return userInsert;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            String hashStr = this.getClass().getName();
            try {
                if (null != this.getId()) {
                    hashStr += ":" + this.getId().hashCode();
                }
            } catch (Exception ex) {
                logger.error("-->errore nel calcolare l'hashCode", ex);
            }
            this.hashCode = hashStr.hashCode();
        }
        return this.hashCode;
    }

    @Override
    public boolean isNew() {
        return getId() == null || getId() == -1;
    }

    /**
     *
     * @param id
     *            id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param timeStamp
     *            The timeStamp to set.
     */
    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @param userInsert
     *            the userInsert to set
     */
    public void setUserInsert(String userInsert) {
        this.userInsert = userInsert;
    }

    /**
     *
     * @param version
     *            version to set
     */
    public void setVersion(Integer version) {
        this.version = version;

    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" id = ").append(id);
        buffer.append(" version = ").append(version);
        return buffer.toString();
    }
}
