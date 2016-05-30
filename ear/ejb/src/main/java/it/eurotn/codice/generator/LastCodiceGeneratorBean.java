package it.eurotn.codice.generator;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.interfaces.LastCodiceGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.LastCodiceGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LastCodiceGenerator")
public class LastCodiceGeneratorBean implements LastCodiceGenerator {

  @Resource
  private SessionContext sessionContext;

  @EJB
  private PanjeaDAO panjeaDAO;

  /**
   * Restituisce il principal corrente.
   *
   * @return JecPrincipal
   */
  private JecPrincipal getCurrentPrincipal() {
    return (JecPrincipal) sessionContext.getCallerPrincipal();
  }

  @Override
  public Integer nextCodice(Class<?> classEntity, String codiceAziendaPath) {
    StringBuilder sb = new StringBuilder(100);
    sb.append("select max(e.codice) from ");
    sb.append(classEntity.getSimpleName());
    sb.append(" e where e.");
    sb.append(codiceAziendaPath);
    sb.append(" = :paramCodiceAzienda ");
    Query query = panjeaDAO.prepareQuery(sb.toString());
    query.setParameter("paramCodiceAzienda", getCurrentPrincipal().getCodiceAzienda());

    Integer maxCodice = (Integer) query.getSingleResult();

    maxCodice = ObjectUtils.defaultIfNull(maxCodice, new Integer(0));

    maxCodice = maxCodice + 1;

    return maxCodice;
  }

}
