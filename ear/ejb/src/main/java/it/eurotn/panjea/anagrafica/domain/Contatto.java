/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Domain Object di Contatto.
 *
 * @author adriano
 * @version 1.0, 17/dic/07
 */
@Entity
@Audited
@Table(name = "anag_contatti")
public class Contatto extends EntityBase {

    private static final long serialVersionUID = 2140745834965721288L;

    public static final String REF = "Contatto";
    public static final String PROP_INTERNO = "interno";
    public static final String PROP_EMAIL = "email";
    public static final String PROP_COGNOME = "cognome";
    public static final String PROP_ID = "id";
    public static final String PROP_NOME = "nome";

    /**
     * @uml.property name="nome"
     */
    @Column(length = 60, nullable = false)
    private String nome;

    @Column(length = 20)
    private String fax;

    /**
     * @uml.property name="cognome"
     */
    @Column(length = 60)
    private String cognome;

    // numero di telefono
    /**
     * @uml.property name="interno"
     */
    @Column(length = 20)
    private String interno;

    /**
     * @uml.property name="cellulare"
     */
    @Column(length = 20)
    private String cellulare;

    /**
     * @uml.property name="email"
     */
    @Column(length = 100)
    private String email;

    /**
     * @uml.property name="mansioni"
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contatto")
    private Set<ContattoSedeEntita> mansioni;

    /**
     * @return Returns the cellulare.
     * @uml.property name="cellulare"
     */
    public String getCellulare() {
        return cellulare;
    }

    /**
     * @return Returns the cognome.
     * @uml.property name="cognome"
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * @return Returns the email.
     * @uml.property name="email"
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Returns the fax.
     */
    public String getFax() {
        return fax;
    }

    /**
     * @return Returns the Interno.
     * @uml.property name="interno"
     */
    public String getInterno() {
        return interno;
    }

    /**
     * @return Returns the mansioni.
     * @uml.property name="mansioni"
     */
    public Set<ContattoSedeEntita> getMansioni() {
        return mansioni;
    }

    /**
     * @return Returns the nome.
     * @uml.property name="nome"
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param cellulare
     *            The cellulare to set.
     * @uml.property name="cellulare"
     */
    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    /**
     * @param cognome
     *            The cognome to set.
     * @uml.property name="cognome"
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @param email
     *            The email to set.
     * @uml.property name="email"
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param fax
     *            The fax to set.
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @param interno
     *            The interno to set.
     * @uml.property name="interno"
     */
    public void setInterno(String interno) {
        this.interno = interno;
    }

    /**
     * @param mansioni
     *            The mansioni to set.
     * @uml.property name="mansioni"
     */
    public void setMansioni(Set<ContattoSedeEntita> mansioni) {
        this.mansioni = mansioni;
    }

    /**
     * @param nome
     *            The nome to set.
     * @uml.property name="nome"
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Contatto[");
        buffer.append(super.toString());
        buffer.append(" cognome = ").append(cognome);
        buffer.append(" email = ").append(email);
        buffer.append(" nome = ").append(nome);
        buffer.append(" telefono = ").append(interno);
        buffer.append(" cellulare = ").append(cellulare);
        buffer.append("]");
        return buffer.toString();
    }
}
