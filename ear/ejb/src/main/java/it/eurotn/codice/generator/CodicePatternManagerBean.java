package it.eurotn.codice.generator;

import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.interfaces.CodicePatternManager;
import it.eurotn.codice.generator.interfaces.VariabiliCodiceManager;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.CodicePatternManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CodicePatternManager")
public class CodicePatternManagerBean implements CodicePatternManager {

  @Override
  public String genera(String pattern, Map<String, String> mapVariabili) {
    StringBuilder codiceResult = new StringBuilder(60);

    if (!StringUtils.isEmpty(pattern)) {
      String[] codiceSplit = StringUtils.split(pattern, VariabiliCodiceManager.VAR_SEPARATOR);
      for (String token : codiceSplit) {
        codiceResult.append(StringUtils.defaultString(mapVariabili.get(token), token));
      }
    }

    return codiceResult.toString();
  }
}
