package it.eurotn.codice.generator;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.AreaContabile;

/**
 * @author fattazzo
 *
 */
@Stateless(mappedName = "Panjea.AreaContabileProtocolloAnnoGeneratorBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaContabileProtocolloAnnoGeneratorBean")
public class AreaContabileProtocolloAnnoGeneratorBean extends ProtocolloAnnoGeneratorBean {

  @Override
  public CodiceProtocollo nextCodice(EntityBase entity) {
    AreaContabile areaContabile = (AreaContabile) entity;

    return nextCodice(areaContabile, areaContabile.getTipoAreaContabile().getRegistroProtocollo());
  }

  @Override
  public CodiceProtocollo nextCodice(EntityBase entity, String registroProtocollo) {
    AreaContabile areaContabile = (AreaContabile) entity;

    return nextCodice(areaContabile, areaContabile.getCodice().getCodice(), registroProtocollo,
        areaContabile.getDataRegistrazione(),
        areaContabile.getTipoAreaContabile().getPatternNumeroProtocollo());
  }

  @Override
  public void restoreCodice(EntityBase entity) {
    AreaContabile areaContabile = (AreaContabile) entity;

    restoreCodice(areaContabile, areaContabile.getTipoAreaContabile().getRegistroProtocollo());
  }

  @Override
  public void restoreCodice(EntityBase entity, String registroProtocollo) {
    AreaContabile areaContabile = (AreaContabile) entity;

    restoreCodice(areaContabile, areaContabile.getValoreProtocollo(), registroProtocollo,
        areaContabile.getDataRegistrazione());
  }

}
