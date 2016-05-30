package it.eurotn.codice.generator;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

/**
 * @author fattazzo
 *
 */
@Stateless(mappedName = "Panjea.DocumentoProtocolloAnnoGeneratorBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentoProtocolloAnnoGeneratorBean")
public class DocumentoProtocolloAnnoGeneratorBean extends ProtocolloAnnoGeneratorBean {

  @Override
  public CodiceProtocollo nextCodice(EntityBase entity) {

    Documento documento = (Documento) entity;

    return nextCodice(documento, documento.getTipoDocumento().getRegistroProtocollo());
  }

  @Override
  public CodiceProtocollo nextCodice(EntityBase entity, String registroProtocollo) {
    Documento documento = (Documento) entity;

    return nextCodice(documento, documento.getCodice().getCodice(), registroProtocollo,
        documento.getDataDocumento(), documento.getTipoDocumento().getPatternNumeroDocumento());
  }

  @Override
  public void restoreCodice(EntityBase entity) {
    Documento documento = (Documento) entity;

    restoreCodice(documento, documento.getTipoDocumento().getRegistroProtocollo());
  }

  @Override
  public void restoreCodice(EntityBase entity, String registroProtocollo) {
    Documento documento = (Documento) entity;

    restoreCodice(documento, documento.getValoreProtocollo(), registroProtocollo,
        documento.getDataDocumento());
  }

}
