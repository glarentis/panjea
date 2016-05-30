package it.eurotn.panjea.pagamenti.manager;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.pagamenti.manager.interfaces.CodicePagamentoManager;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoEsistenteException;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.CodicePagamentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CodicePagamentoManager")
public class CodicePagamentoManagerBean implements CodicePagamentoManager {

  private static Logger logger = Logger.getLogger(CodicePagamentoManagerBean.class.getName());

  @Resource
  private SessionContext context;

  /**
   * @uml.property name="panjeaDAO"
   * @uml.associationEnd
   */
  @EJB
  private PanjeaDAO panjeaDAO;

  /*
   * @see it.eurotn.panjea.pagamenti.manager.interfaces.CodicePagamentoManager#
   * cancellaCodicePagamento(it.eurotn.panjea .pagamenti.domain.CodicePagamento)
   */
  @Override
  public void cancellaCodicePagamento(CodicePagamento codicePagamento) {
    logger.debug("--> Enter cancellaCodicePagamento");
    try {
      panjeaDAO.delete(codicePagamento);
    } catch (Exception e) {
      logger.error("--> Errore nella cancella codice pagamento", e);
      throw new RuntimeException("--> Errore nella cancella codice pagamento", e);
    }
    logger.debug("--> Exit cancellaCodicePagamento");

  }

  /*
   * (non-Javadoc)
   *
   * @see it.eurotn.panjea.pagamenti.manager.interfaces.CodicePagamentoManager#
   * caricaCodicePagamento(java.lang.Integer)
   */
  @Override
  public CodicePagamento caricaCodicePagamento(Integer id) {
    logger.debug("--> Enter caricaCodicePagamento");
    CodicePagamento codicePagamento;
    try {
      Query query = panjeaDAO.prepareNamedQuery("CodicePagamento.caricaById");
      query.setParameter("paramId", id);

      codicePagamento = (CodicePagamento) panjeaDAO.getResultList(query).get(0);
      for (StrutturaPartita strutturaPartita : codicePagamento.getStrutturePartita()) {
        strutturaPartita.getRigheStrutturaPartita().size();
      }
      // codicePagamento = partiteDAO.load(CodicePagamento.class, id);
      // codicePagamento.getStrutturePartita().size();
    } catch (Exception e) {
      logger.error("--> Errore nella carica CodicePagamento", e);
      throw new RuntimeException("--> Errore nella carica CodicePagamento", e);
    }
    logger.debug("--> Exit caricaCodicePagamento");
    return codicePagamento;
  }

  @Override
  public CodicePagamento caricaCodicePagamento(String codice) {
    logger.debug("--> Enter caricaCodicePagamento");
    Query query = panjeaDAO.prepareNamedQuery("CodicePagamento.caricaByCodice");
    query.setParameter("paramCodiceAzienda", getAzienda());
    query.setParameter("paramCodice", codice);
    CodicePagamento codicePagamento;
    try {
      codicePagamento = (CodicePagamento) panjeaDAO.getSingleResult(query);
    } catch (Exception e) {
      logger.error("--> Errore impossibile recuperare il CodicePagamento con codice " + codice, e);
      throw new RuntimeException(
          "--> Errore impossibile recuperare il CodicePagamento con codice " + codice, e);
    }
    logger.debug("Carico stutture " + codicePagamento.getStrutturePartita().getClass());
    codicePagamento.getStrutturePartita().size();
    logger.debug("Dopo Carico stutture " + codicePagamento.getStrutturePartita().getClass());
    logger.debug("--> Exit caricaCodiciPagamento");
    return codicePagamento;
  }

  @Override
  public CodicePagamento caricaCodicePagamento(TipologiaPartita tipologiaPartita)
      throws CodicePagamentoNonTrovatoException {
    logger.debug("--> Enter caricaCodicePagamento");
    Query query = panjeaDAO.prepareNamedQuery("CodicePagamento.caricaByTipologiaPartita");
    query.setParameter("paramCodiceAzienda", getAzienda());
    query.setParameter("paramTipologiaPartita", tipologiaPartita);
    CodicePagamento codicePagamento;
    try {
      codicePagamento = (CodicePagamento) panjeaDAO.getSingleResult(query);
    } catch (ObjectNotFoundException e) {
      logger.warn("--> CodicePagamento non trovato per la tipologiaRata " + tipologiaPartita, e);
      throw new CodicePagamentoNonTrovatoException(
          "--> Errore impossibile recuperare il CodicePagamento con tipologiaRata "
              + tipologiaPartita);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    logger.debug("--> Exit caricaCodicePagamento");
    return codicePagamento;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<CodicePagamento> caricaCodiciPagamento(String filtro,
      TipoRicercaCodicePagamento tipoRicerca, boolean includiDisabilitati) {
    logger.debug("--> Enter caricaCodiciPagamento");

    if (filtro == null) {
      filtro = "";
    }
    filtro += "%";

    Query query = null;
    switch (tipoRicerca) {
    case CODICE:
      query = panjeaDAO.prepareNamedQuery("CodicePagamento.caricaLikeByCodice");
      query.setParameter("paramCodice", filtro);
      break;
    case DESCRIZIONE:
      query = panjeaDAO.prepareNamedQuery("CodicePagamento.caricaLikeByDescrizione");
      query.setParameter("paramDescrizione", filtro);
      break;
    default:
      query = panjeaDAO.prepareNamedQuery("CodicePagamento.caricaAll");
      break;
    }
    // if ((filterCodice == null) || (filterCodice.equals(""))) {
    // query = panjeaDAO.prepareNamedQuery("CodicePagamento.caricaAll");
    // } else {
    // query =
    // panjeaDAO.prepareNamedQuery("CodicePagamento.caricaLikeByCodice");
    // filterCodice += "%";
    // query.setParameter("paramCodice", filterCodice);
    // }
    query.setParameter("paramCodiceAzienda", getAzienda());
    query.setParameter("includiDisabilitati", includiDisabilitati);
    List<CodicePagamento> list;
    try {
      list = panjeaDAO.getResultList(query);
    } catch (Exception e) {
      logger.error("--> Errore impossibile recuperare la list di CodicePagamento", e);
      throw new RuntimeException("--> Errore impossibile recuperare la list di CodicePagamento", e);
    }
    logger.debug("--> Numero risultati query : " + list.size());
    logger.debug("--> Exit caricaCodiciPagamento");
    return list;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CodicePagamento> caricaCodiciPagamentoPos() {
    logger.debug("--> Enter caricaCodiciPagamentoPos");
    Query query = panjeaDAO.prepareNamedQuery("CodicePagamento.caricaForPOS");
    query.setParameter("paramCodiceAzienda", getAzienda());
    List<CodicePagamento> codici;
    try {
      codici = panjeaDAO.getResultList(query);
    } catch (Exception e) {
      logger.error("--> errore durante il caricamento dei codici di pagamento", e);
      throw new RuntimeException("errore durante il caricamento dei codici di pagamento", e);
    }
    logger.debug("--> Exit caricaCodiciPagamentoPos");
    return codici;
  }

  /**
   * @return codice azienda del principal loggato
   */
  private String getAzienda() {
    return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
  }

  @Override
  public CodicePagamento salvaCodicePagamento(CodicePagamento codicePagamento)
      throws CodicePagamentoEsistenteException {
    logger.debug("--> Enter salvaCodicePagamento");
    if (codicePagamento.getTipologiaPartita() == TipologiaPartita.LIQUIDAZIONE) {
      CodicePagamento ceckCodice = null;
      try {
        ceckCodice = caricaCodicePagamento(TipologiaPartita.LIQUIDAZIONE);
      } catch (CodicePagamentoNonTrovatoException e) {
        logger.warn("codice pagamento non trovato.");
      }
      // se esiste il codice pagamento liquidazione devo verificare se e'
      // se stesso prima di lanciare l'exception
      if (ceckCodice != null) {
        Integer idCodicePagamento = codicePagamento.getId();
        Integer idCodiceCheck = ceckCodice.getId();
        if (idCodicePagamento == null
            || (idCodicePagamento != null && !idCodicePagamento.equals(idCodiceCheck))) {
          throw new CodicePagamentoEsistenteException("Questo tipo di codice esiste gia\'");
        }
      }
    }
    CodicePagamento codicePagamentoReturn;
    if (codicePagamento.isNew()) {
      codicePagamento.setCodiceAzienda(getAzienda());
    }
    try {
      codicePagamentoReturn = panjeaDAO.save(codicePagamento);
    } catch (Exception e) {
      logger.error("--> errore salvataggio codice pagamento ", e);
      throw new RuntimeException("Impopssibile salvare il codice pagamento ", e);
    }
    logger.debug("--> Exit salvaCodicePagamento");
    return codicePagamentoReturn;
  }
}
