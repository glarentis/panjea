package it.eurotn.panjea.vending.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_anagrafiche_asl")
@EntityConverter(properties = "codice,descrizione,indirizzo")
@ToString
public class AnagraficaAsl extends EntityBase {

    @Getter
    @Setter
    private String codice;

    @Getter
    @Setter
    private String descrizione;

    @Getter
    @Setter
    private String indirizzo;

}