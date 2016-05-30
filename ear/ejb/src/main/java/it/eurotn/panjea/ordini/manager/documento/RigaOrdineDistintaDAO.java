package it.eurotn.panjea.ordini.manager.documento;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator.FormulaCalculatorClosure;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticoloComponente;
import it.eurotn.panjea.ordini.domain.RigaArticoloDistinta;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineDAO;

@Stateless(mappedName = "Panjea.RigaOrdineDistintaDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaOrdineDistintaDAO")
public class RigaOrdineDistintaDAO extends RigaOrdineAbstractDAOBean implements RigaOrdineDAO {
    private static Logger logger = Logger.getLogger(RigaOrdineDistintaDAO.class);

    @Override
    public RigaArticolo associaConfigurazioneDistintaARigaOrdine(RigaArticolo rigaArticolo,
            ConfigurazioneDistinta configurazioneDistintaDaAssociare) {
        logger.debug("--> Enter associaConfigurazioneDistintaARigaOrdine");

        RigaArticolo riga = panjeaDAO.loadLazy(rigaArticolo.getClass(), rigaArticolo.getId());
        if (riga instanceof HibernateProxy) {
            riga = (it.eurotn.panjea.ordini.domain.RigaArticolo) ((HibernateProxy) riga)
                    .getHibernateLazyInitializer().getImplementation();
        }

        // Nel caso in cui la rigaArticolo è una RigaArticoloDistinta, devo cancellare la
        // rigaArticoloDistinta e tutte
        // le righe componente associate e creare la nuova rigaArticoloDistinta con le righe
        // componente della
        // configurazione scelta.

        Integer idListinoAlternativo = null;
        Integer idListino = null;
        AreaOrdine areaOrdine = riga.getAreaOrdine();
        if (areaOrdine.getListinoAlternativo() != null) {
            idListinoAlternativo = areaOrdine.getListinoAlternativo().getId();
        }
        if (areaOrdine.getListino() != null) {
            idListino = areaOrdine.getListino().getId();
        }

        ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
        parametri.setIdConfigurazioneDistinta(configurazioneDistintaDaAssociare.getId());
        parametri.setIdArticolo(riga.getArticolo().getId());
        parametri.setData(riga.getAreaOrdine().getDocumento().getDataDocumento());
        parametri.setIdListinoAlternativo(idListinoAlternativo);
        parametri.setIdListino(idListino);
        parametri.setImporto(areaOrdine.getDocumento().getTotale());
        parametri.setCodiceIvaAlternativo(areaOrdine.getCodiceIvaAlternativo());
        parametri.setProvenienzaPrezzo(ProvenienzaPrezzo.LISTINO);
        parametri.setProvenienzaPrezzoArticolo(riga.getArticolo().getProvenienzaPrezzoArticolo());
        parametri.setNoteSuDestinazione(areaOrdine.getTipoAreaOrdine().isNoteSuDestinazione());
        parametri.setTipoMovimento(TipoMovimento.NESSUNO);
        parametri.setCodiceValuta(areaOrdine.getDocumento().getTotale().getCodiceValuta());
        parametri.setTipologiaCodiceIvaAlternativo(areaOrdine.getTipologiaCodiceIvaAlternativo());
        parametri.setNotaCredito(
                areaOrdine.getTipoAreaOrdine().getTipoDocumento().isNotaCreditoEnable());
        parametri.setIdDeposito(areaOrdine.getDepositoOrigine().getId());

        RigaArticolo rigaDistintaCreata = creaRigaArticolo(parametri);
        rigaDistintaCreata.setAreaOrdine(areaOrdine);
        rigaDistintaCreata.setQta(riga.getQta());
        rigaDistintaCreata.setDataProduzione(riga.getDataProduzione());
        rigaDistintaCreata.setDataConsegna(riga.getDataConsegna());
        rigaDistintaCreata.setConfigurazioneDistinta(configurazioneDistintaDaAssociare);
        rigaDistintaCreata.setPrezzoUnitario(riga.getPrezzoUnitario());
        rigaDistintaCreata.setSconto(new Sconto(riga.getVariazione1(), riga.getVariazione2(),
                riga.getVariazione3(), riga.getVariazione4()));
        rigaDistintaCreata.setPercProvvigione(riga.getPercProvvigione());
        rigaDistintaCreata.setRigheOrdineCollegate(riga.getRigheOrdineCollegate());

        RigaArticolo rigaOrdineSalvata = (RigaArticolo) salvaRigaOrdine(rigaDistintaCreata);
        rigaOrdineSalvata = (RigaArticolo) caricaRigaOrdine(rigaOrdineSalvata);
        // evito lazy
        if (rigaOrdineSalvata.getConfigurazioneDistinta() != null) {
            rigaOrdineSalvata.getConfigurazioneDistinta().getNome();
        }
        // cancello la riga articolo esistente
        cancellaRigaOrdine(riga);

        // Il dao porta il documento allo stato provvisorio
        // passo dal dao per reimpostare lo stato perchè non ho cambiato nulla
        IStatoDocumento statoInizialeAreaOrdine = riga.getAreaOrdine().getStato();
        if (statoInizialeAreaOrdine == StatoAreaOrdine.CONFERMATO) {
            AreaOrdine ao = rigaOrdineSalvata.getAreaOrdine();
            ao.setStatoAreaOrdine(StatoAreaOrdine.CONFERMATO);
            ao.getDatiValidazioneRighe().valida("automatico");
            try {
                ao = panjeaDAO.save(ao);
                rigaOrdineSalvata.setAreaOrdine(ao);
            } catch (DAOException e) {
                logger.error("-->errore nel salvare l'areaOrdine con satto confermato", e);
                throw new RuntimeException(e);
            }
        }

        // Cancello tutte le distinte personalizzate non legate ad una riga ordine
        StringBuilder sb = new StringBuilder(150);
        sb.append("delete  d.* ");
        sb.append("from maga_distinte_configurazione  d ");
        sb.append("left join ordi_righe_ordine  ro on ro.configurazioneDistinta_id=d.id ");
        sb.append("where d.TIPO_CONFIGURAZIONE='P' ");
        sb.append("and ro.id is null ");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.executeUpdate();

        logger.debug("--> Exit associaConfigurazioneDistintaARigaOrdine");
        return rigaOrdineSalvata;
    }

    @Override
    public AreaOrdine cancellaRigaOrdine(RigaOrdine rigaOrdine) {

        RigaOrdine rigaDistinta = caricaRigaOrdine(rigaOrdine);
        cancellaRigheComponenti(rigaDistinta);

        return areaOrdineManager.checkInvalidaAreaMagazzino(rigaDistinta.getAreaOrdine());
    }

    /**
     * Cancella righe componenti.
     *
     * @param riga
     *            la riga di cui cancellare i componenti
     */
    private void cancellaRigheComponenti(RigaOrdine riga) {

        for (IRigaArticoloDocumento componente : ((IRigaArticoloDocumento) riga).getComponenti()) {
            cancellaRigheComponenti((RigaOrdine) componente);
        }

        try {
            panjeaDAO.delete(riga);
        } catch (DAOException e) {
            logger.error("-->errore nel cancellare il componente ", e);
        }
    }

    /**
     * Carica la lista di righe componente per la distinta.
     *
     * @param idRigaArticoloDistinta
     *            la distinta di cui caricare i componenti
     * @return Set<IRigaArticoloDocumento>
     */
    @SuppressWarnings("unchecked")
    private Set<IRigaArticoloDocumento> caricaComponentiDistinta(Integer idRigaArticoloDistinta) {
        Query query = panjeaDAO.prepareNamedQuery("RigaArticoloDistintaOrdine.caricaComponenti");
        query.setParameter("idDistinta", idRigaArticoloDistinta);
        Set<IRigaArticoloDocumento> componenti = null;
        try {
            componenti = new LinkedHashSet<IRigaArticoloDocumento>(panjeaDAO.getResultList(query));
            for (IRigaArticoloDocumento rigaArticoloDocumento : componenti) {
                rigaArticoloDocumento.getArticolo().getAttributiArticolo().size();
                rigaArticoloDocumento.getComponenti().addAll(
                        caricaComponentiRigaComponente((RigaArticolo) rigaArticoloDocumento));
            }
        } catch (DAOException e) {
            logger.error("-->errore nel caricare i componenti per la riga distinta "
                    + idRigaArticoloDistinta, e);
            throw new RuntimeException(e);
        }
        return componenti;
    }

    /**
     * Carica ricorsivamente tutti i componenti della riga articolo.
     *
     * @param rigaArticolo
     *            riga articolo
     * @return componenti
     */
    @SuppressWarnings("unchecked")
    private Set<IRigaArticoloDocumento> caricaComponentiRigaComponente(RigaArticolo rigaArticolo) {

        Query query = panjeaDAO.prepareQuery(
                "select rc from RigaArticoloComponenteOrdine rc where rc.rigaPadre.id = :paramRigaPadreId and rc.areaOrdine.id = :paramIdArea ");
        query.setParameter("paramRigaPadreId", rigaArticolo.getId());
        query.setParameter("paramIdArea", rigaArticolo.getAreaOrdine().getId());
        Set<IRigaArticoloDocumento> componenti = null;
        try {
            componenti = new LinkedHashSet<IRigaArticoloDocumento>(panjeaDAO.getResultList(query));
            for (IRigaArticoloDocumento rigaArticoloDocumento : componenti) {
                rigaArticoloDocumento.getArticolo().getAttributiArticolo().size();
                rigaArticoloDocumento.getComponenti().addAll(
                        caricaComponentiRigaComponente((RigaArticolo) rigaArticoloDocumento));
            }
        } catch (DAOException e) {
            logger.error("-->errore nel caricare i componenti per la riga distinta " + rigaArticolo,
                    e);
            throw new RuntimeException(e);
        }

        return componenti;
    }

    @Override
    public RigaOrdine caricaRigaOrdine(RigaOrdine rigaOrdine) {
        RigaArticoloDistinta rigaArticoloDistinta = (RigaArticoloDistinta) super.caricaRigaOrdine(
                rigaOrdine);

        // carica rigaMagazzino può tornare null
        if (rigaArticoloDistinta != null) {
            Set<IRigaArticoloDocumento> componenti = caricaComponentiDistinta(
                    rigaArticoloDistinta.getId());
            rigaArticoloDistinta.setComponenti(componenti);
        }
        return rigaArticoloDistinta;

    }

    @Override
    public RigaArticolo creaRigaArticolo(
            ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        RigaArticoloDistinta rigaDistinta = (RigaArticoloDistinta) rigaDocumentoManager
                .creaRigaArticoloDocumento(new RigaArticoloDistinta(),
                        parametriCreazioneRigaArticolo);

        Integer idConfigurazioneDistinta = parametriCreazioneRigaArticolo
                .getIdConfigurazioneDistinta();
        Set<Componente> componenti = new HashSet<Componente>();
        ConfigurazioneDistinta configurazioneDistinta = null;
        // Creo i componenti e li associo all'articolo
        if (idConfigurazioneDistinta != null) {
            configurazioneDistinta = distintaBaseManager
                    .caricaConfigurazioneDistinta(idConfigurazioneDistinta);
            rigaDistinta.setConfigurazioneDistinta(configurazioneDistinta);
            try {
                componenti = distintaBaseManager.caricaComponenti(configurazioneDistinta);

                // se è una configurazione e l'articolo non è la distinta principale, devo trovare i
                // componenti
                // dell'articolo della riga (altrimenti mi ritrovo tutta la struttura della
                // configurazione)
                if (!configurazioneDistinta.getDistinta().getId()
                        .equals(rigaDistinta.getArticolo().getId())) {
                    componenti = findComponenti(rigaDistinta.getArticolo().getId(), componenti);
                }

            } catch (DistintaCircolareException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                componenti = distintaBaseManager
                        .caricaComponenti(rigaDistinta.getArticolo().creaProxyArticolo());
            } catch (DistintaCircolareException e) {
                throw new RuntimeException(e);
            }
        }

        Set<IRigaArticoloDocumento> righeComponenti = new LinkedHashSet<IRigaArticoloDocumento>();
        parametriCreazioneRigaArticolo
                .setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo.NESSUNO);

        for (Componente componente : componenti) {
            RigaArticoloComponente riga = creaRigaRigaArticoloComponente(rigaDistinta, componente,
                    null, parametriCreazioneRigaArticolo, configurazioneDistinta);
            righeComponenti.add(riga);
        }
        rigaDistinta = (RigaArticoloDistinta) caricaQtaAttrezzaggio(rigaDistinta,
                configurazioneDistinta);
        rigaDistinta.setComponenti(righeComponenti);
        return rigaDistinta;
    }

    /**
     * Crea una riga articolo componente partendo dalla riga distinta.
     *
     * @param rigaDistinta
     *            riga distinta
     * @param componente
     *            componente
     * @param rigaPadre
     *            riga padre
     * @param parametri
     *            parametri di creazione
     * @param configurazioneDistinta
     *            configurazioneDistinta
     * @return riga creata
     */
    private RigaArticoloComponente creaRigaRigaArticoloComponente(RigaArticoloDistinta rigaDistinta,
            Componente componente, RigaArticolo rigaPadre, ParametriCreazioneRigaArticolo parametri,
            ConfigurazioneDistinta configurazioneDistinta) {

        parametri.setIdArticolo(componente.getArticolo().getId());
        RigaArticoloComponente riga = (RigaArticoloComponente) rigaDocumentoManager
                .creaRigaArticoloDocumento(new RigaArticoloComponente(), parametri);
        riga.setAreaOrdine(rigaDistinta.getAreaOrdine());
        riga.setRigaDistintaCollegata(rigaDistinta);
        riga.setRigaPadre(rigaPadre);
        riga.setFormulaComponente(componente.getFormula());

        riga = (RigaArticoloComponente) caricaQtaAttrezzaggio(riga, configurazioneDistinta);

        if (componente.getArticolo().getComponenti() != null) {
            for (Componente componenteArticolo : componente.getArticolo().getComponenti()) {
                RigaArticoloComponente rigaArticoloComponente = creaRigaRigaArticoloComponente(
                        rigaDistinta, componenteArticolo, riga, parametri, configurazioneDistinta);
                riga.getComponenti().add(rigaArticoloComponente);
            }
        }
        return riga;
    }

    /**
     * Restituisce, tra i componenti, il ramo di componenti con padre l'articolo rappresentato
     * dall'idArticolo.
     *
     * @param idArticolo
     *            idArticolo di cui cercare i componenti
     * @param componenti
     *            lista di componenti in cui cercare l'articolo per avere i suoi componenti
     * @return Set<Componente>
     */
    private Set<Componente> findComponenti(Integer idArticolo, Set<Componente> componenti) {
        Set<Componente> subComponenti = new HashSet<>();
        for (Componente componente : componenti) {
            subComponenti.addAll(componente.getArticolo().getComponenti());
            if (componente.getArticolo().getId().equals(idArticolo)) {
                return componente.getArticolo().getComponenti();
            }
        }
        return findComponenti(idArticolo, subComponenti);
    }

    @Override
    public RigaOrdine salvaRigaOrdine(RigaOrdine rigaOrdine) {
        logger.debug("--> Enter salvaRigaOrdine");
        RigaArticoloDistinta rigaDistinta = (RigaArticoloDistinta) rigaOrdine;
        Set<IRigaArticoloDocumento> righeComponente = new HashSet<IRigaArticoloDocumento>();
        Set<IRigaArticoloDocumento> righeComponentiDaSalvare = rigaDistinta.getComponenti();

        // aggiungo la qta di attrezzaggio per la distinta
        // creo una formula trasformazione fittizia per eseguire il calcolo
        FormuleRigaArticoloCalculator calculator = new FormuleRigaArticoloCalculator();
        calculator.setFormulaCalculatorClosure(new FormulaCalculatorClosure() {

            @Override
            public IRigaArticoloDocumento apply(IRigaArticoloDocumento rigaArticoloDocumento) {

                RigaArticolo ra = (RigaArticolo) rigaArticoloDocumento;

                double qtaAttrezzaggio = ra.getQtaAttrezzaggio();
                // sull'ordine non devo preoccuparmi di formule sulla qtaMagazzino
                rigaArticoloDocumento.setQta(rigaArticoloDocumento.getQta() + qtaAttrezzaggio);
                rigaArticoloDocumento
                        .setQtaMagazzino(rigaArticoloDocumento.getQtaMagazzino() + qtaAttrezzaggio);
                return rigaArticoloDocumento;
            }
        });

        try {
            IRigaArticoloDocumento rigaCalcolata = calculator.calcola(rigaDistinta);

            // Devo calcolare la qta di attrezzaggio data la configurazione.
            // double qtaAttrezzaggio =
            // distintaBaseManager.caricaQtaAttrezzaggioComponenti(rigaDistinta.getArticolo(),
            // rigaDistinta.getConfigurazioneDistinta());
            rigaDistinta.setQta(rigaCalcolata.getQta());
            rigaDistinta.setQtaMagazzino(rigaCalcolata.getQtaMagazzino());
            rigaDistinta.setAttributi(rigaCalcolata.getAttributi());
            righeComponentiDaSalvare = rigaCalcolata.getComponenti();
        } catch (FormuleTipoAttributoException e1) {
            throw new RuntimeException(e1);
        }

        rigaDistinta = (RigaArticoloDistinta) super.salvaRigaOrdine(rigaDistinta);
        double ordinamento = rigaDistinta.getOrdinamento();

        for (IRigaArticoloDocumento componenteInterface : righeComponentiDaSalvare) {
            RigaArticoloComponente rigaComponente = (RigaArticoloComponente) componenteInterface;

            righeComponente
                    .add(saveRigaComponente(rigaComponente, null, rigaDistinta, ordinamento));

            // di default incremento di 20 perchè non so quante righe componente ricorsivamente sono
            // state salvate
            ordinamento = ordinamento + 20;
        }

        rigaDistinta.setComponenti(righeComponente);
        // evito lazy
        if (((RigaArticolo) rigaDistinta).getConfigurazioneDistinta() != null) {
            ((RigaArticolo) rigaDistinta).getConfigurazioneDistinta().getNome();
        }
        logger.debug("--> Exit salvaRigaOrdine");
        return rigaDistinta;
    }

    /**
     * Salva una riga componente.
     *
     * @param rigaComponente
     *            riga componente
     * @param rigaPadre
     *            riga padre
     * @param rigaDistinta
     *            riga distinta
     * @param ordinamento
     *            ordinamento
     * @return riga salvata
     */
    private RigaArticoloComponente saveRigaComponente(RigaArticoloComponente rigaComponente,
            RigaOrdine rigaPadre, RigaArticoloDistinta rigaDistinta, double ordinamento) {
        Double qtaComp = rigaComponente.getQta();

        if (rigaComponente.getId() != null) {
            rigaComponente = panjeaDAO.loadLazy(rigaComponente.getClass(), rigaComponente.getId());
        }

        rigaComponente.setQta(qtaComp);
        rigaComponente.setAreaOrdine(rigaDistinta.getAreaOrdine());
        rigaComponente.setQtaMagazzino(rigaComponente.getQta());
        rigaComponente.setLivello(rigaDistinta.getLivello());
        rigaComponente.setOrdinamento(ordinamento++);
        rigaComponente.setRigaDistintaCollegata(rigaDistinta);
        rigaComponente.setRigaPadre((RigaArticolo) rigaPadre);
        rigaComponente.setEvasioneForzata(rigaDistinta.isEvasioneForzata());
        RigaArticoloComponente rigaSalvata = (RigaArticoloComponente) super.salvaRigaOrdine(
                rigaComponente);
        // evito lazy alla modifica dopo salvataggio
        if (rigaSalvata.getArticolo().getAttributiArticolo() != null) {
            rigaSalvata.getArticolo().getAttributiArticolo().size();
        }

        for (IRigaArticoloDocumento riga : rigaComponente.getComponenti()) {
            rigaSalvata.getComponenti().add(saveRigaComponente((RigaArticoloComponente) riga,
                    rigaSalvata, rigaDistinta, ordinamento));
        }

        return rigaSalvata;
    }
}
