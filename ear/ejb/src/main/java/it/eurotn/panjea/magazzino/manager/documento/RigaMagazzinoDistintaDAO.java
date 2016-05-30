package it.eurotn.panjea.magazzino.manager.documento;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.exception.RimanenzeLottiNonValideException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator.FormulaCalculatorClosure;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;

@Stateless(mappedName = "Panjea.RigaMagazzinoDistintaDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaMagazzinoDistintaDAO")
public class RigaMagazzinoDistintaDAO extends RigaArticoloDAO implements RigaMagazzinoDAO {
  private static Logger logger = Logger.getLogger(RigaMagazzinoDistintaDAO.class);

  @EJB
  private DistintaBaseManager distintaBaseManager;

  private Set<Componente> caricaComponenti(
      ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo, Articolo articolo) {
    Set<Componente> componenti = new HashSet<Componente>();
    try {
      if (parametriCreazioneRigaArticolo.getIdConfigurazioneDistinta() == null) {
        componenti = distintaBaseManager.caricaComponenti(articolo);
      } else {
        ConfigurazioneDistinta configurazioneDistinta = new ConfigurazioneDistinta();
        configurazioneDistinta.setId(parametriCreazioneRigaArticolo.getIdConfigurazioneDistinta());
        configurazioneDistinta.setDistinta(articolo);
        configurazioneDistinta.setVersion(0);
        componenti = distintaBaseManager.caricaComponenti(configurazioneDistinta);
      }
    } catch (DistintaCircolareException e) {
      throw new RuntimeException(e);
    }
    return componenti;
  }

  /**
   * Carica la lista di righe componente per la distinta.
   *
   * @param rigaArticoloDistinta
   *          la distinta di cui caricare i componenti
   * @return Set<IRigaArticoloDocumento>
   */
  @SuppressWarnings("unchecked")
  private Set<IRigaArticoloDocumento> caricaComponentiDistinta(
      RigaArticoloDistinta rigaArticoloDistinta) {
    Query query = panjeaDAO.prepareNamedQuery("RigaArticoloDistinta.caricaComponenti");
    query.setParameter("idDistinta", rigaArticoloDistinta.getId());
    Set<IRigaArticoloDocumento> componenti = null;
    try {
      componenti = new LinkedHashSet<IRigaArticoloDocumento>(panjeaDAO.getResultList(query));
      for (IRigaArticoloDocumento rigaArticoloDocumento : componenti) {
        rigaArticoloDocumento.getArticolo().getAttributiArticolo().size();
        rigaArticoloDocumento.getComponenti()
            .addAll(caricaComponentiRigaComponente((RigaArticolo) rigaArticoloDocumento));
      }
    } catch (DAOException e) {
      logger.error(
          "-->errore nel caricare i componenti per la riga distinta " + rigaArticoloDistinta, e);
      throw new RuntimeException(e);
    }
    return componenti;
  }

  /**
   * Carica ricorsivamente tutti i componenti della riga articolo.
   *
   * @param rigaArticolo
   *          riga articolo
   * @return componenti
   */
  @SuppressWarnings("unchecked")
  private Set<IRigaArticoloDocumento> caricaComponentiRigaComponente(RigaArticolo rigaArticolo) {

    Query query = panjeaDAO.prepareQuery(
        "select rc from RigaArticoloComponente rc where rc.rigaPadre.id = :paramRigaPadreId and rc.areaMagazzino.id = :paramIdArea ");
    query.setParameter("paramRigaPadreId", rigaArticolo.getId());
    query.setParameter("paramIdArea", rigaArticolo.getAreaMagazzino().getId());
    Set<IRigaArticoloDocumento> componenti = null;

    try {
      componenti = new LinkedHashSet<IRigaArticoloDocumento>(panjeaDAO.getResultList(query));
      for (IRigaArticoloDocumento rigaArticoloDocumento : componenti) {
        rigaArticoloDocumento.getArticolo().getAttributiArticolo().size();
        rigaArticoloDocumento.getComponenti()
            .addAll(caricaComponentiRigaComponente((RigaArticolo) rigaArticoloDocumento));
      }
    } catch (DAOException e) {
      logger.error("-->errore nel caricare i componenti per la riga distinta " + rigaArticolo, e);
      throw new RuntimeException(e);
    }

    return componenti;
  }

  @Override
  public RigaMagazzino caricaRigaMagazzino(RigaMagazzino rigaMagazzino) {
    RigaArticoloDistinta rigaArticoloDistinta = (RigaArticoloDistinta) super.caricaRigaMagazzino(
        rigaMagazzino);
    // carica rigaMagazzino può tornare null
    if (rigaArticoloDistinta != null) {
      Set<IRigaArticoloDocumento> componenti = caricaComponentiDistinta(rigaArticoloDistinta);
      rigaArticoloDistinta.setComponenti(componenti);
    }
    return rigaArticoloDistinta;
  }

  @Override
  public RigaArticolo creaRigaArticolo(
      ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
    RigaArticoloDistinta rigaDistinta = (RigaArticoloDistinta) rigaDocumentoManager
        .creaRigaArticoloDocumento(new RigaArticoloDistinta(), parametriCreazioneRigaArticolo);

    // Creo i componenti e li associo all'articolo
    Set<Componente> componenti = caricaComponenti(parametriCreazioneRigaArticolo,
        rigaDistinta.getArticolo().creaProxyArticolo());
    Set<IRigaArticoloDocumento> righeComponenti = new LinkedHashSet<IRigaArticoloDocumento>();
    parametriCreazioneRigaArticolo
        .setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo.NESSUNO);

    for (Componente componente : componenti) {
      RigaArticoloComponente riga = creaRigaRigaArticoloComponente(rigaDistinta, componente, null,
          parametriCreazioneRigaArticolo);
      righeComponenti.add(riga);
    }
    rigaDistinta.setComponenti(righeComponenti);
    return rigaDistinta;
  }

  /**
   * Crea una riga articolo componente partendo dalla riga distinta.
   *
   * @param rigaDistinta
   *          riga distinta
   * @param componente
   *          componente
   * @param rigaPadre
   *          riga padre
   * @param parametri
   *          parametri di creazione
   * @return riga creata
   */
  private RigaArticoloComponente creaRigaRigaArticoloComponente(RigaArticoloDistinta rigaDistinta,
      Componente componente, RigaArticolo rigaPadre, ParametriCreazioneRigaArticolo parametri) {

    parametri.setIdArticolo(componente.getArticolo().getId());
    RigaArticoloComponente riga = (RigaArticoloComponente) rigaDocumentoManager
        .creaRigaArticoloDocumento(new RigaArticoloComponente(), parametri);
    riga.setAreaMagazzino(rigaDistinta.getAreaMagazzino());
    riga.setRigaDistintaCollegata(rigaDistinta);
    riga.setRigaPadre(rigaPadre);
    riga.setFormulaComponente(componente.getFormula());

    if (componente.getArticolo().getComponenti() != null) {
      for (Componente componenteArticolo : componente.getArticolo().getComponenti()) {
        RigaArticoloComponente rigaArticoloComponente = creaRigaRigaArticoloComponente(rigaDistinta,
            componenteArticolo, riga, parametri);
        riga.getComponenti().add(rigaArticoloComponente);
      }
    }

    return riga;
  }

  @Override
  public AreaMagazzino removeRigaMagazzino(RigaMagazzino rigaMagazzino) {
    RigaArticoloDistinta riga = (RigaArticoloDistinta) rigaMagazzino;

    Set<IRigaArticoloDocumento> componenti = caricaComponentiDistinta(riga);
    // riga.setComponenti(componenti);

    for (IRigaArticoloDocumento componente : componenti) {
      removeRigheComponente(componente);
    }

    return super.removeRigaMagazzino(rigaMagazzino);
  }

  /**
   * Cancella la riga componente e ricorsivamente tutti i suoi componenti.
   *
   * @param rigaComponente
   *          riga da cancellare
   */
  private void removeRigheComponente(IRigaArticoloDocumento rigaComponente) {

    // cancello prima i componenti di livello più basso per finire poi con
    // la riga richiesta.
    if (!rigaComponente.getComponenti().isEmpty()) {
      for (IRigaArticoloDocumento riga : rigaComponente.getComponenti()) {
        removeRigheComponente(riga);
      }
    }

    magazzinoControlloSchedeArticolo.checkInvalidaSchedeArticolo((RigaArticolo) rigaComponente);

    try {
      panjeaDAO.delete((RigaArticoloComponente) rigaComponente);
    } catch (DAOException e) {
      logger.error("-->errore nel cancellare il componente ", e);
    }
  }

  /**
   * Salva una riga componente.
   *
   * @param rigaComponente
   *          riga componente
   * @param rigaPadre
   *          riga padre
   * @param rigaDistinta
   *          riga distinta
   * @param ordinamento
   *          ordinamento
   * @return riga salvata
   * @throws RimanenzaLottiNonValidaException
   *           .
   * @throws RigheLottiNonValideException
   *           .
   * @throws QtaLottiMaggioreException
   *           .
   */
  private RigaArticoloComponente saveRigaComponente(RigaArticoloComponente rigaComponente,
      RigaMagazzino rigaPadre, RigaArticoloDistinta rigaDistinta, double ordinamento)
          throws RimanenzaLottiNonValidaException, RigheLottiNonValideException,
          QtaLottiMaggioreException {
    Double qtaComp = rigaComponente.getQta();
    rigaComponente.getComponenti().size();
    if (rigaComponente.getId() != null) {
      rigaComponente = panjeaDAO.loadLazy(rigaComponente.getClass(), rigaComponente.getId());
    }

    rigaComponente.setQta(qtaComp);
    rigaComponente.setAreaMagazzino(rigaDistinta.getAreaMagazzino());
    rigaComponente.setQtaMagazzino(rigaComponente.getQta());
    rigaComponente.setLivello(rigaDistinta.getLivello() + 1);
    rigaComponente.setOrdinamento(ordinamento++);
    rigaComponente.setRigaDistintaCollegata(rigaDistinta);
    rigaComponente.setRigaPadre((RigaArticolo) rigaPadre);
    // i lotti dei componenti non sono gestibili dall'utente ma creati
    // manualmente per questo li posso ricreare
    // ogni volta per generarli corretti
    rigaComponente.getRigheLotto().clear();
    // uso il dao della riga articolo perchè esegue il controllo e
    // assegnazione dei lotti automatici
    RigaArticoloComponente rigaSalvata = null;
    if (rigaComponente.getQta().compareTo(0.0) != 0) {
      rigaSalvata = (RigaArticoloComponente) super.saveRigaMagazzino(rigaComponente);
    }

    // if (rigaSalvata != null && rigaComponente.getComponenti() != null) {
    // for (IRigaArticoloDocumento riga : righeComponenti) {
    // rigaSalvata.getComponenti().add(
    // saveRigaComponente((RigaArticoloComponente) riga, rigaSalvata,
    // rigaDistinta, ordinamento));
    // }
    // }

    return rigaSalvata;
  }

  @Override
  public RigaMagazzino saveRigaMagazzino(RigaMagazzino rigaMagazzino)
      throws RimanenzaLottiNonValidaException, RigheLottiNonValideException,
      QtaLottiMaggioreException {
    logger.debug("--> Enter salvaRigaMagazzinoDistinta");

    RimanenzeLottiNonValideException rimanenzeLottiNonValideException = new RimanenzeLottiNonValideException();

    // cancello le righe dei componenti ricreo le righe
    RigaArticoloDistinta rigaDistinta = (RigaArticoloDistinta) rigaMagazzino;
    Set<IRigaArticoloDocumento> righeComponente = new LinkedHashSet<IRigaArticoloDocumento>();
    Set<IRigaArticoloDocumento> righeComponentiDaSalvare = rigaDistinta.getComponenti();

    // creo una formula trasformazione fittizia per eseguire il calcolo
    FormuleRigaArticoloCalculator calculator = new FormuleRigaArticoloCalculator();
    calculator.setFormulaCalculatorClosure(new FormulaCalculatorClosure() {

      @Override
      public IRigaArticoloDocumento apply(IRigaArticoloDocumento rigaArticoloDocumento) {
        RigaArticolo ra = (RigaArticolo) rigaArticoloDocumento;
        double qtaAttrezzaggio = ra.getQtaAttrezzaggio();
        rigaArticoloDocumento.setQta(rigaArticoloDocumento.getQta() + qtaAttrezzaggio);
        rigaArticoloDocumento
            .setQtaMagazzino(rigaArticoloDocumento.getQtaMagazzino() + qtaAttrezzaggio);
        return rigaArticoloDocumento;
      }
    });
    try {
      RigaArticoloDistinta rigaCalcolata = (RigaArticoloDistinta) calculator.calcola(rigaDistinta);
      rigaDistinta.setQta(rigaCalcolata.getQta());
      rigaDistinta.setQtaMagazzino(rigaCalcolata.getQtaMagazzino());
      rigaDistinta.setAttributi(rigaCalcolata.getAttributi());
      righeComponentiDaSalvare = rigaCalcolata.getComponenti();
    } catch (FormuleTipoAttributoException e1) {
      throw new RuntimeException(e1);
    }

    rigaDistinta = (RigaArticoloDistinta) super.saveRigaMagazzino(rigaDistinta);
    double ordinamento = rigaDistinta.getOrdinamento();

    for (IRigaArticoloDocumento componenteInterface : righeComponentiDaSalvare) {
      RigaArticoloComponente rigaComponente = (RigaArticoloComponente) componenteInterface;

      try {
        RigaArticoloComponente rigaComponenteSalvata = saveRigaComponente(rigaComponente, null,
            rigaDistinta, ordinamento);
        if (rigaComponenteSalvata != null) {
          righeComponente.add(rigaComponenteSalvata);
        }

        // di default incremento di 20 perchè non so quante righe
        // componente ricorsivamente sono state salvate
        ordinamento = ordinamento + 20;
      } catch (RimanenzaLottiNonValidaException e) {
        rimanenzeLottiNonValideException.addRimanenzaNonValida(e);
      }
    }

    if (!rimanenzeLottiNonValideException.isEmpty()) {
      throw rimanenzeLottiNonValideException;
    }

    rigaDistinta.setComponenti(righeComponente);
    logger.debug("-->Exit salvaRigaMagazzinoDistinta");
    return rigaDistinta;
  }

}
