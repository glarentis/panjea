package it.eurotn.codice.generator;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.interfaces.CodicePatternManager;
import it.eurotn.codice.generator.interfaces.IProtocolloValore;
import it.eurotn.codice.generator.interfaces.ProtocolloAnnoGenerator;
import it.eurotn.codice.generator.interfaces.VariabiliCodiceManager;
import it.eurotn.dao.exception.GenerazioneProtocolloException;
import it.eurotn.dao.exception.ProtocolloNonEsistenteException;
import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.ProtocolloAnnoGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ProtocolloAnnoGenerator")
public abstract class ProtocolloAnnoGeneratorBean implements ProtocolloAnnoGenerator {

  private static Logger logger = Logger.getLogger(ProtocolloAnnoGeneratorBean.class);

  @Resource
  private SessionContext sessionContext;

  @EJB
  private PanjeaDAO panjeaDAO;

  @EJB
  private CodicePatternManager codicePatternManager;

  @EJB
  @IgnoreDependency
  private VariabiliCodiceManager variabiliCodiceManager;

  /**
   * 
   * @return valore della proprietà anno contenuta nel propertyPath <code>annoPropertyPath</code>
   */
  private Integer getAnno(Date dataAnno) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dataAnno);
    Integer annoValue = calendar.get(Calendar.YEAR);
    logger.debug("--> Exit getAnno");
    return annoValue;
  }

  /**
   * Restituisce il principal corrente.
   * 
   * @return JecPrincipal
   */
  private JecPrincipal getCurrentPrincipal() {
    JecPrincipal jecPrincipal = (JecPrincipal) sessionContext.getCallerPrincipal();
    return jecPrincipal;
  }

  /**
   * Recupera {@link IProtocolloValore} attraverso <code>CodiceProtocollo</code>. Se
   * <code>CodiceProtocollo</code> non � valorizzato tenta di recuperarlo attraverso l'attributo
   * <code>registroPropertyPath</code>, se anche questo fallisce si intende che codiceProtocollo
   * viene definito dall'utente e viene restituito null. Recuperato il <code>CodiceProtocollo</code>
   * recupera l'istanza attraverso la namedQuery "ProtocolloValore.caricaByCodice"
   * 
   * @return classe contenente il valore del protocollo
   */
  private IProtocolloValore getProtocollo(String codiceProtocollo, Date dataAnno) {
    logger.debug("--> Enter getProtocollo");

    if (StringUtils.isBlank(codiceProtocollo)) {
      logger.debug(
          "--> protocollo nullo/vuoto, il codice/progressivo non vi � richiesta la generazione    ");
      return null;
    }

    Integer anno = getAnno(dataAnno);
    Query query = panjeaDAO.prepareNamedQuery("ProtocolloAnno.caricaByCodiceProtocolloAnno");
    query.setParameter("paramCodiceAzienda", getCurrentPrincipal().getCodiceAzienda());
    query.setParameter("paramCodice", codiceProtocollo);
    query.setParameter("paramAnno", anno);
    IProtocolloValore iprotocolloValore = null;
    try {
      iprotocolloValore = (IProtocolloValore) query.getSingleResult();
    } catch (PersistenceException e) {
      logger.error("--> errore getSingleResult in getProtocollo", e);
    }
    if (iprotocolloValore == null) {
      logger.warn(
          "--> Non esiste il protocollo per codice:" + codiceProtocollo + " per anno:" + anno);
      throw new ProtocolloNonEsistenteException(codiceProtocollo, anno);
    }
    logger.debug("--> Exit getProtocollo");
    return iprotocolloValore;

  }

  protected CodiceProtocollo nextCodice(EntityBase entity, String codice, String codiceProtocollo,
      Date dataAnno, String patternCodice) {

    IProtocolloValore protocolloValore = null;
    try {
      protocolloValore = getProtocollo(codiceProtocollo, dataAnno);
    } catch (GenerazioneProtocolloException e) {
      logger.error("--> " + e.getMessage());
      throw new RuntimeException(e);
    }

    if (protocolloValore == null) {
      logger.debug("--> Exit nextCodice, codice generato dall'utente ");
      try {
        return new CodiceProtocollo(codice, null);
      } catch (Exception e) {
        logger.error("--> errore durante il caricamento del valore della proprietà codice.", e);
        throw new RuntimeException(
            "errore durante il caricamento del valore della proprietà codice.", e);
      }
    }
    Integer valore = protocolloValore.getValore();
    logger.debug("--> valore protocollo trovato  " + valore);
    valore++;
    protocolloValore.setValore(valore);

    /*
     * esegue il salvataggio di protocolloValore aggiornandone il valore del progressivo
     */
    panjeaDAO.getEntityManager().merge(protocolloValore);

    Map<String, String> mapVariabili = variabiliCodiceManager.creaMapVariabili(entity);
    mapVariabili.put(VariabiliCodiceManager.VALPROT, protocolloValore.getValore().toString());

    // il pattern per il protocollo anno deve contenere la variabile per il valore del protocollo
    // quindi se è vuoto
    // la aggiungo
    patternCodice = StringUtils.defaultString(patternCodice, VariabiliCodiceManager.VAR_SEPARATOR
        + VariabiliCodiceManager.VALPROT + VariabiliCodiceManager.VAR_SEPARATOR);

    String codiceGenerato = codicePatternManager.genera(patternCodice, mapVariabili);
    CodiceProtocollo codiceResult = new CodiceProtocollo(codiceGenerato,
        protocolloValore.getValore());

    logger.debug("--> Exit nextCodice valore " + valore);
    return codiceResult;
  }

  protected void restoreCodice(EntityBase entity, Integer valoreProtocolloEntity,
      String codiceProtocollo, Date dataAnno) {
    // Controllo se il baseEntity ha l'ultimo codice generato
    logger.debug("--> Enter restoreCodice");

    IProtocolloValore protocolloValore = null;

    try {
      protocolloValore = getProtocollo(codiceProtocollo, dataAnno);
    } catch (GenerazioneProtocolloException e) {
      logger.error("--> " + e.getMessage());
      throw new RuntimeException(e);
    }
    if (protocolloValore == null) {
      logger.debug("--> Exit nextCodice, codice generato dall'utente ");
      return;
    }
    Integer valore = protocolloValore.getValore();
    logger.debug("--> valore protocollo trovato  " + valore);

    if (valore != null && valore.equals(valoreProtocolloEntity)) {
      // ripristino il valore del numeratore
      if (valore.intValue() > 0) {
        valore--;
      }
      protocolloValore.setValore(valore);
      /*
       * esegue il salvataggio di protocolloValore aggiornandone il valore del progressivo
       */
      panjeaDAO.getEntityManager().merge(protocolloValore);
    }
    logger.debug("--> Exit restoreCodice");
  }

}
