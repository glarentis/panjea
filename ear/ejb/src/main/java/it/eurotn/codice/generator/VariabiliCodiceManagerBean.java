package it.eurotn.codice.generator;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.interfaces.VariabiliCodiceManager;
import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.VariabiliCodiceManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.VariabiliCodiceManager")
public class VariabiliCodiceManagerBean implements VariabiliCodiceManager {

  private SimpleDateFormat anno2Format = new SimpleDateFormat("yy");
  private SimpleDateFormat anno4Format = new SimpleDateFormat("yyyy");

  private Map<String, String> creaMapVariabili(AreaContabile areaContabile) {
    Map<String, String> map = new HashMap<String, String>();

    if (areaContabile.getDataRegistrazione() != null) {
      map.put(ANNO2, anno2Format.format(areaContabile.getDataRegistrazione()));
      map.put(ANNO4, anno4Format.format(areaContabile.getDataRegistrazione()));
    }
    map.put(TIPODOC, areaContabile.getDocumento().getTipoDocumento().getCodice());

    return map;
  }

  private Map<String, String> creaMapVariabili(Documento documento) {
    Map<String, String> map = new HashMap<String, String>();

    if (documento.getDataDocumento() != null) {
      map.put(ANNO2, anno2Format.format(documento.getDataDocumento()));
      map.put(ANNO4, anno4Format.format(documento.getDataDocumento()));
    }
    map.put(TIPODOC, documento.getTipoDocumento().getCodice());

    return map;
  }

  @Override
  public Map<String, String> creaMapVariabili(EntityBase entity) {
    if (entity instanceof Documento) {
      return creaMapVariabili((Documento) entity);
    }

    if (entity instanceof AreaContabile) {
      return creaMapVariabili((AreaContabile) entity);
    }

    throw new IllegalArgumentException(
        "entity non prevista per le variabili della generazione codice.");
  }

  @Override
  public String[] getVariabili() {
    return new String[] { ANNO2, ANNO4, TIPODOC, VALPROT };
  }

}
